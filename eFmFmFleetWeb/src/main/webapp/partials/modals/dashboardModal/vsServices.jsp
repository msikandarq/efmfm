<div class = "vsModal container-fluid">
    <div class = "row">
        <div class="modal-header modal-header1 col-md-12"> 
            <div class = "row">
                <div class = "icon-signin edsModal-icon col-md-1 floatLeft"></div>
                <div class = "modalHeading col-md-10 floatLeft">Vehicle(s) Picked</div>
                <div class = "col-md-1 floatRight pointer">
                    <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
                </div>        
            </div>
        </div>        
    </div>  
    <div class = "row">
        <div class="modal-header2 col-md-12">
            <form><input class = "searchModal" 
                         type="text" 
                         class="form-control" 
                         placeholder="Search..." 
                         id="formGroupInputSmall"
                         ng-model = "search"></form>
        </div>
    </div>
    
<div class="modal-body modalMainContent">
    <h3>To be Determine</h3>
<!--
    <table class="table table-bordered table-hover table-responsive container-fluid">
        <thead class ="tableHeading">
            <tr>
              <th></th>
		      <th>>Vendor Name</th>
		      <th>Vehicle Number</th>
		      <th>Vehicle Type</th>
		      <th>Driver Name</th>
		      <th>Mobile Number</th>
              <th>Rolled-Out Time</th>
            </tr> 
            <tr>
                <td>1</td>
                <td>Dallas Yellow Cab</td>
                <td>123-1233</td>
                <td>Toyota</td>
                <td>Smith John0</td>
                <td>123-456-7890</td>
                <td>4:55</td>
            </tr>
            <tr>
                <td>1</td>
                <td>Dallas Yellow Cab</td>
                <td>123-1233</td>
                <td>Toyota</td>
                <td>Smith John0</td>
                <td>123-456-7890</td>
                <td>4:55</td>
            </tr>
            <tr>
                <td>1</td>
                <td>Dallas Yellow Cab</td>
                <td>123-1233</td>
                <td>Toyota</td>
                <td>Smith John0</td>
                <td>123-456-7890</td>
                <td>4:55</td>
            </tr>
            <tr>
                <td>1</td>
                <td>Dallas Yellow Cab</td>
                <td>123-1233</td>
                <td>Toyota</td>
                <td>Smith John0</td>
                <td>123-456-7890</td>
                <td>4:55</td>
            </tr>
        </thead>
        
    </table>
-->
</div>
    
<div class="modal-footer modalFooter">     
    <button type="button" class="btn btn-success-vs" ng-click = "cancel()">Close</button>
</div>
</div>