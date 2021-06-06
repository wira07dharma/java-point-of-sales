<%-- 
    Document   : license
    Created on : Nov 4, 2017, 12:31:37 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.entity.license.LicenseProduct"%>
<!DOCTYPE html>
<%@ include file = "main/javainit.jsp" %>
<%

    String license_key = FRMQueryString.requestString(request, "license_key");
    boolean isValidKey = false;
    if(!license_key.equals("")){
        isValidKey = LicenseProduct.btDekripActionPerformed(license_key);
    }
%>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>AISO | license product</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.6 -->
        <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
        <!-- Theme style -->
        <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script language="JavaScript">
        </script>
    </head>
    
    <body class="hold-transition lockscreen">
        <!-- Automatic element centering -->
        <div class="lockscreen-wrapper">
            <div class="lockscreen-logo">
                <b>Dimata</b>ProChain
            </div>
            <!-- User name -->
            <div class="lockscreen-name">License Product</div>
            <!-- START LOCK SCREEN ITEM -->
            <div class="lockscreen-item">
                <!-- lockscreen image -->
                <div class="lockscreen-image">
                    <img src="<%=approot%>/imgcompany/security.jpg" alt="User Image">
                </div>
                <!-- /.lockscreen-image -->

                <!-- lockscreen credentials (contains the form) -->
                <form class="lockscreen-credentials">
                    <div class="input-group">
                        <input type="text" class="form-control" placeholder="license" name="license_key">
                        <div class="input-group-btn">
                            <button type="submit" class="btn" ><i class="fa fa-arrow-right text-muted"></i></button>
                        </div>
                    </div>
                </form>
                <!-- /.lockscreen credentials -->

            </div>
            <!-- /.lockscreen-item -->
            <div class="help-block text-center">
                Enter your license to retrieve your session
            </div>
            <div class="lockscreen-footer text-center">
                Copyright &copy; 2016 <b><a href="http://dimata.com" class="text-black">Dimata Sora Jayate</a></b><br>
                All rights reserved
            </div>
        </div>
        <!-- /.center -->

        <!-- jQuery 2.2.3 -->
        <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
        <!-- Bootstrap 3.3.6 -->
        <script src="<%=approot%>/styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
    </body>
    <%
    if(!license_key.equals("")){
        %>
        <script language="javascript">
            <%if (isValidKey) {%>
                window.location = "login.jsp";
            <%} else {%>
                window.location = "license.jsp";
            <%}%>
        </script>
    <%
    }
    %>
</html>
