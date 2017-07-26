<div class = "loginPageTemplate container-fluid">
    
    <div class = "row loginHeader">
        <div class = "col-md-12 logHolder"><img src = "../images/logo.png"></div>
        
     </div>
    
      <div class ="row loginContent">
          <div class = "col-md-4  col-md-offset-4 loginBox"> 
              <div class = "row">
                  <div class="col-md-12 col-xs-12">
                      <div class = "ribbonHeader">
                        <span class = "loginLabel">USER LOGIN</span>
                      </div>
                        <div class = "ribbonArrow"></div>
                        <div class = "ribbonleft"></div>
                  </div>
                  <div class = "row">
                    <div class= "col-md-12 col-xs-12">
                      <form type="submit">
                        <ul class = "loginUl">
                            <li class = "loginli">
                               <input type = "text" class = "loginUserId" placeholder="User ID" ng-model="userId">
                                <i class ="iconLogin user"></i>
                            </li>
                            <li class = "passwordli">
                               <input type = "password" class = "loginpassword" placeholder="Password" ng-model="password">
                                <i class ="iconPassword password"></i>
                            </li>                            
                         </ul>
                         <button class="signInButton"  ng-click="login()"><span>Sign In</span></button>                         
                      </form>
                    </div>
                  </div>
              </div>              
          </div>
      </div>
      <div class = "row footer">
            <div class = "col-md-12">
                   <span class="floatRight">Copyright @ Newt|Global Consulting LLC.</span> 
                </div>
            </div>
        </div>
   
</div>