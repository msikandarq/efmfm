
<div class="container">
 
  <div class="panel-group">
    <div class="panel panel-primary">
      <div class="panel-heading">Import Data</div>
      <div class="panel-body">

      </div>

 <div class="container"><br/>
  <div class="col-md-2"><br/>Choose Upload File Type</div>
  <div class="col-md-4">
    <div class="funkyradio excelUploadTopMargin">
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="employeeData" 
            	   value="employeeData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="employeeData">Import Employee Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="employeeRequest" 
            	   value="employeeRequest" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="employeeRequest">Import Employee Request</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="vendorData" 
            	   value="vendorData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="vendorData">Import Vendor Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="driverData" 
            	   value="driverData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="driverData">Import Driver Data</label>
        </div>
         <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="vehicleData" 
            	   value="vehicleData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="vehicleData">Import Vehicle Data</label>
        </div>
         <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="escortData"
            	   value="escortData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="escortData">Import Escort Data</label>
        </div>
         <div class="funkyradio-success">
            <input type="radio" 
                   name="radio" 
                   id="nodalData" 
                   value="nodalData" 
                   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="nodalData">Import Nodal Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="homeData" 
            	   value="homeData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="homeData">Import Home Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
             	   name="radio" 
             	   id="guestData" 
             	   value="guestData" 
             	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="guestData">Import Guest Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
            	   name="radio" 
            	   id="batchDeleteData" 
            	   value="batchDeleteData" 
            	   ng-model="importDataType" 
            	   ng-change="setimportDataType(importDataType)"/>
            <label for="batchDeleteData">Import Batch Delete Data</label>
        </div>
        <div class="funkyradio-success">
            <input type="radio" 
                   name="radio" 
                   id="employeeRequestManager" 
                   value="employeeRequestManager" 
                   ng-model="importDataType" 
                   ng-change="setimportDataType(importDataType)"/>
            <label for="employeeRequestManager">Import Employee Request - Manager</label>
        </div>
        <br/> 
        <br/>
    </div>
</div>

<div class="col-md-5">
	<div class="panel panel-success">
	<div class="panel-heading">{{uploadTypeHeader}}</div>
	  
	<div class="panel-body">
        <form class='importEmpForm' id="addinstgroup" action="{{actionTypeUrl}}"
                    method="post" enctype="multipart/form-data"
                    class="form-horizontal form-bordered"
                    name="myForm">

    		<select name="format" class="hidden">
    			<option value="csv" selected> CSV</option>
    		</select>
		    <div id="drop" class="hidden"></div>
		    <p><input type="file" class="btn btn-default btn-md" name="xlfile" id="xlf" ng-model="filename" valid-file required
                       accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ng-file-model="filename"/></p>
			<input type="checkbox" name="useworker" checked class="hidden"><br />
			<input type="checkbox" name="xferable" checked class="hidden"><br />
			<input type="checkbox" name="userabs" checked class="hidden"><br />
		<pre id="out" class="hidden"></pre>
    </form>

    <div class = "">
        <div class = "input-group calendarInput" > 
          <button type="button" class="btn btn-success floatLeft" id='imp_stud' style="margin-top: -50px;" ng-disabled="uploadButtonDisabled" ng-click = "uploadFiles(filename)" ng-disabled="!myForm.$valid">{{UploadButtonText}}</button>
           <span class="input-group-btn">
                <img src="images/cube.gif" width="30px" ng-show="spinnerShow" class="spinnerIconPosition">   
            </span> 
        </div>
    </div>

	</div>
</div>
    <div class="col-md-1"></div>
    </div>
   
  </div>
</div>



