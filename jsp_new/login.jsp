<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.common.entity.license.LicenseProduct"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<!DOCTYPE html>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.form.admin.FrmAppUser"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language="java" %>
<%@ include file="main/javainit.jsp"%>
<%@ page import="com.dimata.printman.RemotePrintMan,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.session.admin.SessUserSession,
                 java.util.Vector,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.masterdata.*" %>
<%!
  final static int CMD_NONE = 0;
  final static int CMD_LOGIN = 1;
  final static int MAX_SESSION_IDLE = 10000000;
  int langDefault = com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT;
  int langForeign = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
%>

<%  
    String root= request.getContextPath();//"/D-ProChainPos-V3";
    int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN_DEFAULT_DISPLAY, AppObjInfo.OBJ_LOGIN_MOBILE); 
    boolean privMobileDisplay = false;
    int iCommand = Integer.parseInt((request.getParameter("command")==null) ? "0" : request.getParameter("command"));
    int typeView = FRMQueryString.requestInt(request, "typeView");
    String  noLog = FRMQueryString.requestString(request, "nodocument");
    int deviceUse = FRMQueryString.requestInt(request,"deviceuse");

    int dologin = 0;
    int dologinSales = 0;
    int appLanguage  =  0;
    String salesCode="";
    boolean dayAssign=false;
    Date dateNow = new Date();
    int strDate = Integer.parseInt(String.valueOf(dateNow.getDate()));
    int strhourNow = Integer.parseInt(String.valueOf(dateNow.getHours()));
    int opie=0;

    // Then get the day of week from the Date based on specific locale.
    String nameDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateNow);
    //System.out.println("xx XXXXXX"+nameDay);
    String numDay = FrmAppUser.getSerchValue(nameDay);
    int userGroup = -1;

    
    if(iCommand==CMD_LOGIN){
            String loginID = FRMQueryString.requestString(request,"login_id");
            String passwd  = FRMQueryString.requestString(request,"pass_wd");
            appLanguage  = FRMQueryString.requestInt(request,"app_language");

            String remoteIP = request.getRemoteAddr();
            com.dimata.posbo.session.admin.SessUserSession  userSess = new com.dimata.posbo.session.admin.SessUserSession(remoteIP );
            dologin=userSess.doLogin(loginID, passwd);
            dologinSales = userSess.doLoginSales(loginID, passwd);
            if(dologin==SessUserSession.DO_LOGIN_OK){
                    AppUser appUser = userSess.getAppUser();
                    dayAssign  = true;
                    int strhour = Integer.parseInt(String.valueOf(appUser.getStartTime().getHours()));
//                    int endhour = Integer.parseInt(String.valueOf(appUser.getEndTime().getHours()));

                    //update cash bill detail untuk disc_global is null, menjadi 0
                    //int updateDisc = PstBillDetail.updateDiscItem(0,0);
                    //&&  strhourNow <= endhour
                    userGroup = appUser.getUserGroupNew();
                    //cek hari assign
                    if(dayAssign==true &&   strhourNow >= strhour){
                        session.setMaxInactiveInterval(MAX_SESSION_IDLE);
                        session.putValue(SessUserSession.HTTP_SESSION_NAME, userSess);
                        userSess = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);

                        session.putValue("APPLICATION_LANGUAGE", String.valueOf(appLanguage));
                        session.putValue("APPLICATION_DEVICE_USE", String.valueOf(deviceUse));
                        String strLang = "";
                        String strDevUse="";

                        if(session.getValue("APPLICATION_LANGUAGE")!=null){
                            strLang = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
                        }

                        if(session.getValue("APPLICATION_DEVICE_USE")!=null){
                            strDevUse = String.valueOf(session.getValue("APPLICATION_DEVICE_USE"));
                        }

                        appLanguage = (strLang!=null && strLang.length()>0) ? Integer.parseInt(strLang) : 0;

                        deviceUse = (strDevUse!=null && strDevUse.length()>0) ? Integer.parseInt(strDevUse) : 0;

                        privMobileDisplay = userSess.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

                    }else{

                        dologin=SessUserSession.DO_LOGIN_NO_PRIV_ASIGNED;

                    }
            }
            /**
            * Ari_wiweka 20130827
            * Untuk login group sales order
            **/
//            else if(dologinSales==SessUserSession.DO_LOGIN_OK ){
//                Sales salesUser = PstSales.getByLoginIDAndPassword(loginID, passwd); 
//                salesCode = salesUser.getCode();
//                session.setMaxInactiveInterval(MAX_SESSION_IDLE);
//                session.putValue(SessUserSession.HTTP_SESSION_SALES, salesUser);
//            }
            else if(dologinSales==SessUserSession.DO_LOGIN_OK ){
                AppUser ap = PstAppUser.getByLoginIDAndPassword(loginID, passwd); 
                session.setMaxInactiveInterval(MAX_SESSION_IDLE);
                session.putValue(SessUserSession.HTTP_SESSION_SALES, ap);
            }
    }



    /** cek integrasi */
    int INTEGRASI_POS = 0;
    int INTEGRASI_HANOMAN = 1;

    int pos_integ = 0;
    try {
            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            pos_integ = Integer.parseInt(designMat);
    }
    catch (Exception e) {
            pos_integ = 0;
    }
    
    if((iCommand==CMD_LOGIN)&&(dologin == SessUserSession.DO_LOGIN_OK)){
        if(userGroup==3){
            dologin = SessUserSession.DO_LOGIN_NOT_VALID;
            //response.sendRedirect("outletonline/kitchen_list_order.jsp");
        }else{
            if(privMobileDisplay){
                if(typeView==0){
                            response.sendRedirect("homepage_menuicon.jsp");
                }else{
                        response.sendRedirect("purchasing/material/pom/m_create_pofrompr.jsp?pomaterial="+noLog+"&pomaterial_"+noLog+"=1");
                }
            }else{
               if(deviceUse==1){//mobile
                    if(typeView==0){
                            response.sendRedirect("homepage_mobile.jsp");
                    }else{
                            response.sendRedirect("purchasing/material/pom/m_create_pofrompr.jsp?pomaterial="+noLog+"&pomaterial_"+noLog+"=1");
                    }
                }else{
                    if(typeView==0){
                        //System.out.println("Reload Host and Printers");
                        if(menuUsed==3){
                            response.sendRedirect("homepage_menuicon.jsp");
                        }else if(menuUsed==4){
                            response.sendRedirect("homepage_menuicon.jsp");
                        }else{
                            response.sendRedirect("homepage_menuicon.jsp");
                        }
                    }else{
                        response.sendRedirect("purchasing/material/pom/create_pofrompr.jsp?pomaterial="+noLog+"&pomaterial_"+noLog+"=1");
                    }
                }
            }

        }


     }//Ari_wiweka 20130827
     else if((iCommand==CMD_LOGIN)&&(dologinSales == SessUserSession.DO_LOGIN_OK)){
         if(typeOfBusiness.equals("3")){
             //response.sendRedirect("cashierweb/srcsalesordecashier.jsp?FRM_FIELD_SALES_CODE="+salesCode+"");
              dologin = SessUserSession.DO_LOGIN_NOT_VALID;
         }else{
             //response.sendRedirect("outletonline/mainmenumobile.jsp?FRM_FIELD_SALES_CODE="+salesCode+"");
              dologin = SessUserSession.DO_LOGIN_NOT_VALID;
         }


     }

    boolean isValidKey = false;
    String freeLicense = String.valueOf(PstSystemProperty.getValueByName("FREE_LICENSE"));
    if (freeLicense.equals("1")) {
        isValidKey = true;
    } else {
        String txtChiperText = "";
        try {
            txtChiperText = PstCompany.getLicenseKey();
        } catch (Exception e) {
        }
        isValidKey = LicenseProduct.btDekripActionPerformed(txtChiperText);
    }
%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>ProChain - Centralized & Distributed in Harmony</title>
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

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style> html {position: fixed; width: 100%; height: 100% !important; overflow: auto;} body { background-image: url(./images/background.jpg) !important; background-position: left bottom !important; height: unset !important; } </style>
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

        <form name="frmLogin" method="post" onsubmit="javascript:cmdLogin()">
          <input type="hidden" name="sel_top_mn" value="4">
          <input type="hidden" name="command" value="<%=CMD_LOGIN%>">
          <div class="form-group has-feedback">
            <input type="text" class="form-control" name="login_id" placeholder="User Id">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input type="password" name="pass_wd" class="form-control" placeholder="Password">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          <% if((iCommand==CMD_LOGIN) && (dologin != SessUserSession.DO_LOGIN_OK)) {%>
          <div class="row">
            <div class="col-xs-12">
              <span style="display: inline-block; text-align: center; width: 100%; margin-bottom: 10px;" class="alert-error"><%=SessUserSession.soLoginTxt[dologin]%></span>
            </div>
          </div>
          <%}%>
          <div class="row">
            <div class="col-xs-8">
              <div class="form-group">
                <label>Language</label>
                <% String[] strLang = com.dimata.util.lang.I_Language.langName; %>
                <select name="app_language" class="form-control">
                  <option value="<%=langDefault %>"><%=strLang[langDefault]%></option>
                  <option value="<%=langForeign %>"><%=strLang[langForeign]%></option>
                </select>
              </div>
            </div>
            <!-- /.col -->
            <div class="col-xs-4">
              <button type="submit" style="margin-top: 24px;" class="btn bg-orange btn-block btn-flat">Login</button>
            </div>
            <div class="col-xs-12">
                <center><span>Copyright &copy; 2010 Dimata&reg; IT Solution <br>
                              Telp. (0361) 499029, 7869752; Fax (0361) 499029<br>
                              Hotline Support : 081237710011 <br>
                              Website : <a href="http://www.dimata.com" class="footerLink">www.dimata.com</a><br></span></center>

            </div>
            <!-- /.col -->
          </div>
        </form>
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
    
    <% if((iCommand!=CMD_LOGIN) && (dologin != SessUserSession.DO_LOGIN_OK)) { %>
        <script language="JavaScript">
          document.frmLogin.login_id.focus();
        </script>
    <%}%>
    <%
    if(true){
        %>
        <script language="javascript">
            <%if (isValidKey) {%>
            <%} else {%>
                window.location = "license.jsp";
            <%}%>
        </script>
    <%
    }
    %>
  </body>
</html>