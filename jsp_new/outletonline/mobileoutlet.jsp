<%-- 
    Document   : mobileoutlet
    Created on : Nov 14, 2014, 9:53:20 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<!DOCTYPE html>
<%@ include file = "../main/javainit.jsp" %>

<%!
  //table       
%>
<%
 int iCommand = FRMQueryString.requestCommand(request);
%>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Prochain | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <link href="../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="../styles/bootstrap3.1/libs/html5shiv.js"></script>
          <script src="../styles/bootstrap3.1/libs/respond.min.js"></script>
        <![endif]-->
        <script language="JavaScript">
             function cmdAdd(typeAdd){
                document.general.command.value="<%=Command.ADD%>";
                document.general.typetab.value=typeAdd;
                document.general.action="general.jsp";
                document.general.submit();
             }
             function cmdSave(typeAdd){
                document.general.command.value="<%=Command.SAVE%>";
                document.general.typetab.value=typeAdd;
                document.general.action="general.jsp";
                document.general.submit();
             }
             function cmdCancel(typeAdd){
                document.general.command.value="<%=Command.CANCEL%>";
                document.general.typetab.value=typeAdd;
                document.general.action="general.jsp";
                document.general.submit();
             }
             function cmdDelete(typeAdd){
                document.general.command.value="<%=Command.DELETE%>";
                document.general.typetab.value=typeAdd;
                document.general.action="general.jsp";
                document.general.submit();
             }
        </script>
    </head>
    <body class="skin-blue">
        <%@ include file = "../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                 <section class="content">
                    <form name="general" method ="post" action="" role="form">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <div class='row'>
                            <div class='col-xs-12'>
                               <!-- your free content -->
                              <div class="col-lg-3 col-xs-6">
                                    <!-- small box -->
                                    <div class="small-box bg-aqua">
                                        <div class="inner">
                                            <p>
                                                New Orders
                                            </p>
                                        </div>
                                        <a href="<%=approot%>/outletonline/mobileoutletonline.jsp" class="small-box-footer">
                                            Add <i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-lg-3 col-xs-6">
                                    <!-- small box -->
                                    <div class="small-box bg-green">
                                        <div class="inner">
                                            <p>
                                                My Orders
                                            </p>
                                        </div>
                                        <a href="<%=approot%>/outletonline/listtransaction.jsp" class="small-box-footer">
                                            Search <i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class='col-xs-12'>
                                 <!-- your free content -->
                                   <div class="col-lg-3 col-xs-6">
                                    <!-- small box -->
                                    <div class="small-box bg-yellow">
                                        <div class="inner">
                                            <p>
                                                Help
                                            </p>
                                        </div>
                                        <a href="#" class="small-box-footer">
                                            Search <i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-lg-3 col-xs-6">
                                    <!-- small box -->
                                    <div class="small-box bg-red">
                                        <div class="inner">
                                            <p>
                                                Info
                                            </p>
                                        </div>
                                        <a href="#" class="small-box-footer">
                                            Search <i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <div class="small-box bg-purple">
                                            <div class="inner">
                                                <a href="<%=approot%>/logout.jsp" class="small-box-footer">
                                                    <center><font color="white">logout</font></center></i>
                                                </a>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>                   
                 </section>
            </aside>
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../styles/bootstrap3.1/libs/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>
