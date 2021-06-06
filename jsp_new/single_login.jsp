<%-- 
    Document   : single_login
    Created on : Jan 31, 2014, 10:26:25 AM
    Author     : dimata005
--%>

<!DOCTYPE html>
<!--[if IE 8]> 				 <html class="no-js lt-ie9" lang="en" > <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en" > <!--<![endif]-->

  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="fresh Gray Bootstrap 3.0 Responsive Theme "/>
	<meta name="keywords" content="Template, Theme, web, html5, css3, Bootstrap,Bootstrap 3.0 Responsive Theme" />
	<meta name="author" content="Mindfreakerstuff"/>
    <link rel="shortcut icon" href="favicon.png">

    <title>Login Page</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/login.css" rel="stylesheet">
    <link href="css/animate-custom.css" rel="stylesheet">
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->

     <script src="js/custom.modernizr.js" type="text/javascript" ></script>

  </head>
    <body>
    	<!-- start Login box -->
    	<div class="container" id="login-block">
    		<div class="row">
			    <div class="col-sm-6 col-md-4 col-sm-offset-3 col-md-offset-4">

			       <div class="login-box clearfix animated flipInY">
			       		<div class="page-icon animated bounceInDown">
			       			<img class="img-responsive" src="img/login-key-icon.png" alt="Key icon" />
			       		</div>
			        	<div class="login-logo">
			        		<a href="#"><img src="img/login-logo.png" alt="Company Logo" /></a>
			        	</div>
			        	<hr />
			        	<div class="login-form">
			        		<div class="alert alert-error hide">
								  <button type="button" class="close" data-dismiss="alert">&times;</button>
								  <h4>Error!</h4>
								   Your Error Message goes here
							</div>
			        		<form action="#" method="get"  >
						   		 <input type="text" placeholder="User name" class="input-field" required/>
						   		 <input type="password"  placeholder="Password" class="input-field" required/>
						   		 <button type="submit" class="btn btn-login">Login</button>
							</form>
			        	</div>
			       </div>
			    </div>
			</div>
    	</div>

      	<!-- End Login box -->
     	<footer class="container">
     		<p id="footer-text"><small>Copyright &copy; 2013 <a href="http://ad-says.com/">AdSays Creation Pvt. Ltd.</a></small></p>
     	</footer>

        <script src="styles/bootstrap3.1/js/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/jquery-1.9.1.min.js"><\/script>')</script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/placeholder-shim.min.js"></script>
        <script src="js/custom.js"></script>
    </body>
</html>
