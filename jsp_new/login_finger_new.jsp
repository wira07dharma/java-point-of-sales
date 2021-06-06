<%-- 
    Document   : login_finger_new
    Created on : Jun 30, 2017, 3:24:24 PM
    Author     : dimata005
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!  
    public static final String listTextTitle[][] = {
        {"Hasil","Proses login berhasil","Tutup"},
        {"Result","Login process success","Close"}
    };
    int deviceUse =0;
    final static int CMD_LOGIN=1;
%>
<!DOCTYPE html>
<%@ include file="main/javainit.jsp"%>
<%@ page import = "com.dimata.util.Command"%>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>SEDANA - Sistem Manajemen Simpan Pinjam</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/bootstrap/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/bootstrap/css/ionicons.min.css">
    <!-- Datetime Picker -->
    <link href="styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <!-- Select2 -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/plugins/select2/select2.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
    
    <!-- jQuery 2.2.3 -->
    <script src="styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
    
    <style> html {position: fixed; width: 100%; height: 100% !important; overflow: auto;} body { background-image: url(./images/background.jpg) !important; background-position: left bottom !important; height: unset !important; } </style>
    <style>
        .finger{
           width:20%; 
           height:80px;
           padding : 2%;
           float:left;
        }
        .finger_spot{
            width:96%;
            height: 70px;
            background-color :#FFF;
            font-size: 14px;
            font-family:calibri;
            text-align:center;
            color :#FFF;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function(){
            var interval =0;
            function ajaxUser(url,data,type,appendTo,another,optional,optional2){
                $.ajax({
                    url : ""+url+"",
                    data: ""+data+"",
                    type : ""+type+"",
                    async : false,
                    cache: false,
                    success : function(data) {
                        //alert(data);
                        $(''+appendTo+'').html(data);
                    },
                    error : function(data){
                        //alert('error');
                    }
                }).done(function(data){
                    if (another=="checkUser"){
                        fingerClick();
                    }else if (another=="checkStatusUser"){
                        if (data==1){
                            //$('#modalReport').modal({backdrop: 'static', keyboard: false});
                            clearInterval(interval);
                            alert("<%= listTextTitle[0][1]%>");
                            //if(!alert("<%= listTextTitle[0][1]%>")) document.location = 'http://stackoverflow.com/';
                            $('#loginFingers').submit();
                        }
                    }
                });
            }
            
            function checkUser(){
                var url = "<%=approot%>/AjaxUser";
                var loginId = $('#login_id').val();
                var data="command=<%=Command.ASK%>&login="+loginId+"&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>";
                ajaxUser(url,data,"POST","#dynamicPlace","checkUser","","");
            }
            
            function checkStatusUser(userId){
                var url = "<%=approot%>/AjaxUser";
                var data="command=<%=Command.SEARCH%>&loginId="+userId+"";
                ajaxUser(url,data,"POST","","checkStatusUser","","");
            }
            
            function fingerClick(){
                $('.loginFinger').click(function(){
                    var loginId= $('#login_id').val();
                    interval = setInterval(function() {
                        checkStatusUser(loginId);
                    },5000);
                });
            }
            
            $('#login_id').keyup(function(){
                var content = $('#login_id').val();
                $('#login_id_send').val(content);
                checkUser();
            });
            
            $('.tutup').click(function(){
               $('#loginFingers').submit();
            });
            
            checkUser();
        });
    </script>
  </head>
  <body class="hold-transition login-page">
    <div style="margin: 2% auto;" class="login-box">
      <div class="login-logo">
        <div style="color:white;"><b style="width: 100%; display: inline-block;">ProChain</b><span style="font-size: 22px;width: 100%; display: block;">Centralized & Distributed in Harmony</span></div>
      </div>
      <!-- /.login-logo -->
      <div class="login-box-body" style="box-shadow: 0 0 20px rgba(0, 0, 0, 0.35); border-radius: 2px;">
        <div class="login-logo">
          <img src="<%=approot%>/images/logo_prochain.jpg" style="display: inline-block; width: 320px;" />
        </div>
        <p class="login-box-msg">Login to start your session</p>

        <form id="loginFingers" action="login_process.jsp" method="POST">
            <input type="hidden" name="command" value="<%=CMD_LOGIN%>">
            <input id="login_id_send" name="login_id" placeholder="User Id..."  type="hidden" class="form-control">
        </form>
        <div style="background-color:#A8D164;">
            <div class="form-group">
                <input id="login_id" placeholder="User Id..."  type="text" class="form-control">
            </div>
            <div class="form-group">&nbsp;&nbsp;&nbsp;
                <%
                        String strLang[] = com.dimata.util.lang.I_Language.langName;       
                %>
                <select class="selectpicker" name="app_language" data-size="5">
                       <option value="<%=0%>"><%=strLang[0]%></option>
                       <option value="<%=1%>"><%=strLang[1]%></option>
                </select> 
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="deviceuse" value="1"  <%=deviceUse==1?"checked":""%>>Mobile &nbsp;&nbsp;&nbsp; <input type="radio" name="deviceuse" value="0"  <%=deviceUse==0?"checked":""%>> Dekstop
            </div>
            <div class="form-group" id="dynamicPlace" style="margin-bottom:10px;"></div>
            <div class="form-group">&nbsp;</div>
        </div>
      </div>
       <!--modal bootstrap untuk keterangan apabila login berhasil  -->
        <div id="modalReport" class="modal fade" tabindex="-1">
            <div class="modal-dialog modal-sm" style="max-width: 400px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close tutup" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modal-title"><%= listTextTitle[0][0]%>...</h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                        <%= listTextTitle[0][1]%>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger tutup"><%= listTextTitle[0][2]%></button>
                    </div>
                </div>
            </div>
        </div>                  
      <!-- /.login-box-body -->
    </div>
    <!-- /.login-box -->

    <!-- Bootstrap 3.3.6 -->
    <script src="./style/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="./style/AdminLTE-2.3.11/plugins/fastclick/fastclick.js"></script>
    <!-- AdminLTE App -->
    <script src="./style/AdminLTE-2.3.11/dist/js/app.min.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="./style/AdminLTE-2.3.11/dist/js/demo.js"></script>
    
  </body>
</html>