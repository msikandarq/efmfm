<div ng-include = "'partials/showAlertMessageModalTemplate.jsp'">
</div>
<div class="loading">
</div>
<div class = "addNewMultipleLocationTemplate">
  <div class = "row">
    <div class="modal-header modal-header1 col-md-12">
      <div class = "row"> 
        <div class = "icon-map-marker mapMarkerIconCreateRequest col-md-1 floatLeft">
        </div>
        <div class = "modalHeading col-md-10 floatLeft">Add Multiple Request
        </div>
        <div class = "col-md-1 floatRight pointer">
          <img src="images/portlet-remove-icon-white.png" class = "floatRight" ng-click = "cancel()">
        </div>  
      </div>  
    </div>        
  </div>
  <div class="modal-body addNewShiftTimeModal modalMainContent minHeight220">
  <div class="Invoice">
<table class="table table-bordered">
    <thead>
      <tr>
        <th class="tableCell">Req No</th>
        <th class="tableCell">Start Time</th>
        <th class="tableCell">End Time</th>
        <th class="tableCell">Trip Type/Shift Time</th>
        <th class="tableCell">Pickup Location</th>
        <th class="tableCell">Drop Location</th>
        <th class="removeBtn"></th>
      </tr>
    </thead>
    <tbody>
      <tr ng-repeat="destinationPoint in destinationPoints track by $index">
        <td class="text-center">{{$index + 1}}</td>
        <td class="tableCell">
                    <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                      <button class="btn btn-default" 
                  ng-click="reqstartDate($event, $index)">
                <i class = "icon-calendar calInputIcon"></i></button></span>
            <input type="text" 
               ng-model="destinationPoint.startDate"
               ng-change = "startDateSet(destinationPoint.startDate, $index)"
               class="form-control" 
               placeholder = "Start Date" 
               datepicker-popup="dd-MM-yyyy"
               is-open="sdatePicker[$index]" 
               min-date="minDate[$index]"
               show-button-bar = false
               show-weeks=false
               required
               readonly
               name = 'startDate'
               >  
            
           </div>
           <!-- {{destinationPoint.startDate}} -->
          </td>
        <td class="tableCell">
                    <div class = "input-group calendarInput">
                    <span class="input-group-btn">
                      <button class="btn btn-default" 
                  ng-click="reqendDate($event, $index)">
                <i class = "icon-calendar calInputIcon"></i></button></span>
            <input type="text" 
               ng-model="destinationPoint.endDate" 
               ng-change = "endDateSet(destinationPoint.endDate, $index)"
               class="form-control" 
               placeholder = "End Date" 
               min="requestDate.startDate" 
               min-date="endMinDate[$index]"
               datepicker-popup="dd-MM-yyyy"
               is-open="edatePicker[$index]" 
               show-button-bar = false
               show-weeks=false
               required
               readonly
               name = 'endDate'
               >             
           </div>
           <!-- {{destinationPoint.startDate}} -->
          </td>
        <td class="tableCell" ng-click="setShiftTimes($index,destinationPoints)">
                   <span class = "pointer">

              <h5 class=""><button class = "icon-time AEVbtn btn-info" 
                  tooltip="TripType:{{destinationPoint.tripType.value}} TimeType:{{destinationPoint.timeType.value}} PickupShiftTime:{{destinationPoint.pickupTimeNomal.shiftTime}} 
                  DropShiftTime:{{destinationPoint.dropTimeNormal.shiftTime}}"
                  tooltip-placement="right"
                  tooltip-trigger="mouseenter"> Add
              </button></h5>
            </span>
            </td>
        <td class="tableCell minWidthBtn">
                    <div class = "input-group" ng-if= "destinationPoint.tripType.value != 'DROP'">
                <button class = "icon-plus-sign btn-success AEVbtn pointer" 
                ng-click="setPickupLocation($index,destinationPoints, 'add')"> Add
                </button>
                <button class = "icon-edit-sign btn-info AEVbtn pointer" ng-disabled="!destinationPoint.startAreaPickup" 
                ng-click="setPickupLocation($index,destinationPoint, 'edit')"> Edit
              </button>
              <button class = "icon-eye-open btn-warning AEVbtn pointer" 
                  tooltip="{{destinationPoint.startAreaPickup.areaName}}-to-{{destinationPoint.endAreaPickup.areaName}}"
                  tooltip-placement="right"
                  tooltip-trigger="mouseenter"> View
              </button>              
            </div>
            </td>
        <td class="tableCell minWidthBtn"> 
                            <div class = "input-group" ng-if= "destinationPoint.tripType.value != 'PICKUP'">
                <button class = "icon-plus-sign btn-success AEVbtn pointer" ng-click="setDropLocation($index,destinationPoints, 'add')"> Add
                </button>
                <button class = "icon-edit-sign btn-info AEVbtn pointer" ng-disabled="!destinationPoint.startAreaDrop" 
                ng-click="setDropLocation($index,destinationPoint, 'edit')"> Edit
              </button>
              <button class = "icon-eye-open btn-warning AEVbtn pointer" 
                  tooltip="{{destinationPoint.startAreaDrop.areaName}}-to-{{destinationPoint.endAreaDrop.areaName}}"
                  tooltip-placement="bottom"
                  tooltip-trigger="mouseenter"> View
              </button>
            </div>           
        </td>
        <td class="removeBtn pointer" ng-click="deleteDestinationLocation(destinationPoint.destination, $index)""> X </td>
      </tr>
      <tr>
        <td class="text-center tableCell" 
        >
        <button ng-click="addDestination(1)" 
        class="btn btn-success btn-xs width100per pointer"> &#43;  
        </button>
        </td>
      </tr>
    </tbody>
  </table>
</div>
          <!-- <pre>{{destinationPoints | json}}</pre> -->
      </div>      
    <div class="modal-footer modalFooter"> 
      <div class="">
        <button type="button" 
                class="btn btn-success btn-sm"
                ng-click = "saveMultipleRequest(destinationPoints)" ng-disabled="desinationDisabledButton">Submit
        </button>
<!--         <button type="button" 
                class="btn btn-info btn-sm"
                ng-click = "resetLocationDetails(destinationPoints)">Reset
        </button>  -->       
        <button type="button" 
                class="btn btn-danger btn-sm"
                ng-click = "cancel()">Cancel
        </button>
      </div>
    </div>
  </div>
