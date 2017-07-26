<!DOCTYPE html>
<html lang="en">
<head>
  <title>eFmFm - Access Denied</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="bower_components/bootstrap/css/bootstrap.min.css"
    rel="stylesheet">
  <script src="bower_components/jquery/dist/jquery.min.js"></script>
  <script src="bower_components/bootstrap/js/bootstrap.min.js"></script>

  <style type="text/css">
    .error-template {padding: 40px 15px;text-align: center;}
    .error-actions {margin-top:15px;margin-bottom:15px;}
    .error-actions .btn { margin-right:10px; }
    .errorTemplateStyle { background-color: whitesmoke; padding-top: 150px; padding-bottom: 160px;}
</style>

</head>
<body>
<div class="container-fluid errorTemplateStyle">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>
                    Oops!</h1>
                <h2>
                    Access Denied </h2>
                <div class="error-details">
                    Sorry, You don't have access permission to eFmFm application. 
                    Please contact your administrator to get access to this application.
                </div>
                <div class="error-actions">
                 <!-- <a href=" <%=request.getContextPath() %>/saml/logout" > Logout </a> from application. -->
                    <a href=" <%=request.getContextPath() %>/saml/logout" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-log-out"></span>
                        Click here to Logout from application</a>
                     <a href="mailto:support@efmfm.com?Subject=Subject%20Here" target="_top" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-comment"></span> Contact Support</a>   
                        
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


