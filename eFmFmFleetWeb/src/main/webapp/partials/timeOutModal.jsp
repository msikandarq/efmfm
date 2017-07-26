    <section class="hidden">
      <p>
        <button type="button" class="btn btn-success" data-ng-hide="started" data-ng-click="start()">Start Demo</button>
        <button type="button" class="btn btn-danger" data-ng-show="started" data-ng-click="stop()">Stop Demo</button>
      </p>
    </section>

    <a ng-href="j_spring_security_logout" class="hidden" target="{{condition ? ' _blank' : '_self'}}" id="logOut" href="j_spring_security_logout"><i class="icon-key"></i> Log Out</a>

    <script type="text/ng-template" id="warning-dialog.html">
     <div class="panel-group">
        <div class="panel panel-warning">
          <div class="panel-heading"><strong>You're Idle.</strong></div>
          <div class="panel-body">You'll be logged out in {{countdown}} second(s).</div>
        </div>
      </div>

      <div idle-countdown="countdown" ng-init="countdown=5" class="hidden">
          <progress max="100" percent="progress" class='progress-striped' ng-class='{"active": active}'></progress>
           <div class="container_progress">
                     <div class="progress progress-striped"   ng-class='{"active": active, "":!active}'>
                       <div  class="bar" ng-style="bar_upload_style" >{{bar_upload_text}}</div>  
                         </div>
                      </div>
      </div>

    </script>
    <script type="text/ng-template" id="timedout-dialog.html">
       <div class="panel-group">
        <div class="panel panel-danger">
          <div class="panel-heading"><strong>You've Timed Out!</strong></div>
          <div class="panel-body">You were idle too long. Please login again</div>
        </div>
      </div>
    </script>