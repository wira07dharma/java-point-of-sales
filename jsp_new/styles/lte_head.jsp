<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN);%>
<%@ include file = "./../main/checkuser.jsp" %>
<% String root = approot; %>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<% // Tell the browser to be responsive to screen width %>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<% /* - - - - - - - - - - - - - - - - Styles - - - - - - - - - - - - - - - */ %>
<% // Bootstrap 3.3.6 %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
<% // Font Awesome %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
<% // Ionicons %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
<% // Theme style %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
<% /* AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. */%>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">

<%--
<% // iCheck %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/iCheck/flat/blue.css">
<% // Morris chart %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/morris/morris.css">
<% // jvectormap %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<% // Date Picker %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/datepicker/datepicker3.css">
<% // Daterange picker %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/daterangepicker/daterangepicker.css">
<% // bootstrap wysihtml5 - text editor %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
--%>
<% /*  - - - - - - - - - - - - - - - Scripts - - - - - - - - - - - - - - - */ %>
<% // jQuery 2.2.3 %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
<% // jQuery UI 1.11.4 %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
<% // Resolve conflict in jQuery UI tooltip with Bootstrap tooltip %>
<script>$.widget.bridge('uibutton', $.ui.button);</script>
<% // Bootstrap 3.3.6 %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
<%--
<% // Morris.js charts %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/raphael/2.1.0/raphael-min.js"></script>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/morris/morris.min.js"></script>
<% // Sparkline %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/sparkline/jquery.sparkline.min.js"></script>
<% // jvectormap %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<% // jQuery Knob Chart %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/knob/jquery.knob.js"></script>
<% // daterangepicker %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/moment.js/2.11.2/moment.min.js"></script>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/daterangepicker/daterangepicker.js"></script>
<% // datepicker %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/datepicker/bootstrap-datepicker.js"></script>
<% // Bootstrap WYSIHTML5 %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<% // Slimscroll %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<% // FastClick %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/plugins/fastclick/fastclick.js"></script>
--%>
<% // AdminLTE App %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>
<% // AdminLTE dashboard demo (This is only for demo purposes) %>
<script src="<%=root%>/styles/AdminLTE-2.3.11/dist/js/pages/dashboard.js"></script>
<% // HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries %>
<% // WARNING: Respond.js doesn't work if you view the page via file:// %>
<style type="text/css">
  .content-wrapper, .flexbox { background-color: rgb(241, 236, 231) !important;}
  .content-wrapper { position: relative; }
  .content-wrapper.inner { display: none; position: absolute; top: 0; right: 0; bottom: 0; left: 0; width: 100%; height: 100%; padding: 0 !important; margin: 0 !important; }
  .app-frame { width: 100%; height: 100%; overflow: hidden; border: none !important; position: absolute; top: 0; right: 0; bottom: 0; left: 0; }
  .menu-list { padding-left: 0; }
  .menu-list li { list-style-type: none; font-size: 16px; display: inline-block; width: 100%; padding-top: 2.5px; padding-bottom: 2.5px; }
  .menu-list li i { font-size: 14px; line-height: 20px; display: inline-block; margin-top: -3px; vertical-align: middle; padding-right: 5px; }
</style>
<script>var obj = null;
  function delay(length = 1) {
    var that = this;
    this.length = length * 1000;
    this.invoke = null;
    this.t = null;
    this.start = function () {
      that.t = setTimeout(that.invoke, that.length);
    };
    this.clear = function () {
      clearTimeout(that.t);
      that.t = null;
    };
  }
  function resizeFrame(o) {
    obj = o;
    var d = new delay(1.1);
    d.invoke = function () {
      var height = 0;
      height = (($(".main-sidebar").height() + $('.navbar.navbar-static-top').height()) >= $(window).height() ? $(".main-sidebar").height() : ($("body").height() - $('.navbar.navbar-static-top').height()));
      $(obj).parent().css('min-height', height);
      if (height !== 0) {
        var newHeight = $(obj.contentDocument).height();
        if (height < newHeight) {
          $(obj).parent().css('min-height', newHeight);
        }
      }
    };
    var invoke = function () {
      d.clear();
      d.start();
    };
    $('.treeview').click(invoke);
    $(window).resize(invoke);
    $(obj).show();
    invoke();
  }
  ;</script>
<script>
  $(window).load(function () {
    $('.i-link').each(function () {
      var e = $(this);
      var link = $(e).attr("href");
      $(e).attr("href", "javascript:;");
      $(e).click(function () {
        let id = $(this).data('for');
        $(document.getElementById(id)).attr("src", link);
      });
    });
  });
</script>
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->