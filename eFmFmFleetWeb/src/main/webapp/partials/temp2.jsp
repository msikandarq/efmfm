
<div class="indexPageTemplate container-fluid">
 <div class = "row topHeader_index">
    <div class = "col-md-12 col-xs-12">
<!--        <div class="floatRight">HELP</div>-->
        <div class="floatRight login" ng-click = "openLoginModal()">LOGIN</div>
<!--        <div class="floatRight search" ng-click = "showSerchTextBox()">SEARCH</div>-->
        <div class = "verticalBar_index floatRight"><span>|</span></div>
        <div class = "searchTexBox floatRight">
            <form type="submit">
                <input type="text" class="form-control searchInput floatLeft" placeholder="Search for...">
            </form>
<!--
                <input type="text" class="form-control searchInput floatLeft" placeholder="Search for...">
                <span class="searchButton floatLeft">
                    <button class="searchButton" type="button">Go!</button>
                </span>
-->
            
        </div>
    </div>
 </div>
    
 <!--Navigation Bar-->
 <div class = "row navHeader_index">   
   <nav class = "navbar navbar-static navBar_index">
        <div class = "container-fluid">
            <!--Branding Log - Company Name-->
            <div class="navbar-header">
<!--                <div class="row">-->
                <a ui-sref="index2.home" class = "navbar-brand custome_navbarBrand hidden-xs">eFmFm - Employee Transport Management</a>   
                    <a ui-sref="index2.home" class = "navbar-brand custome_navbarBrand visible-xs-* hidden-lg hidden-md hidden-sm">eFmFm</a>
                <!--Navigation button will appear for small screen devices-->
              
                <button type="button" 
                        class="navbar-toggle" 
                        data-toggle="collapse" 
                        data-target=".targetForButton">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>                
                </button>
<!--                </div>-->
            </div>
            
            <!--Navigetion Links on the Right-->
            <div class = "collapse navbar-collapse targetForButton">
                <div class= "navbarInner">
                    <ul class="nav navbar-nav navbar-right navMenu_index">
                        <li class="active">
                            <a ui-sref="index2.home">Home</a></li>
                        <li><a ui-sref="index2.aboutUs">About Us</a></li>
                        <li><a ui-sref="index2.features">Features</a></li>
                        <li><a ui-sref="index2.contact">Contact</a></li>
                    </ul>
                </div>
            </div>
            
        </div>
    </nav> 

  </div>
 <!--END Navigation Bar-->      
    
        <div ui-view></div>
    
</div>