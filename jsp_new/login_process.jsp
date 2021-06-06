<%-- 
    Document   : login_process
    Created on : Nov 3, 2015, 10:21:19 AM
    Author     : Witar
--%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
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
 //final static int CMD_NONE =0;
 final static int CMD_LOGIN=1;
 final static int MAX_SESSION_IDLE=100000;
%>
<%
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
String whereClause="";
String passwd="";

// Then get the day of week from the Date based on specific locale.
String nameDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateNow);
//System.out.println("xx XXXXXX"+nameDay);
String numDay = FrmAppUser.getSerchValue(nameDay);
int userGroup = -1;

if(iCommand==CMD_LOGIN){
	String loginID = FRMQueryString.requestString(request,"login_id");
        appLanguage  = FRMQueryString.requestInt(request,"app_language");
        
        whereClause=" "+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"= '"+loginID+"' and "+PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS]+"=1";
        Vector listAppUser = PstAppUser.listFullObj(0, 0, whereClause, "");
        if (listAppUser.size()>0){
            AppUser appUser = (AppUser)listAppUser.get(0);
            passwd = appUser.getPassword();

            String remoteIP = request.getRemoteAddr();
            com.dimata.posbo.session.admin.SessUserSession  userSess = new com.dimata.posbo.session.admin.SessUserSession(remoteIP );
            dologin=userSess.doLogin(loginID, passwd);
            dologinSales = userSess.doLoginSales(loginID, passwd);
            if(dologin==SessUserSession.DO_LOGIN_OK){
                    dayAssign  = true;
                    int strhour = Integer.parseInt(String.valueOf(appUser.getStartTime().getHours()));
                    int endhour = Integer.parseInt(String.valueOf(appUser.getEndTime().getHours()));
                    //cek group

                    userGroup = appUser.getUserGroupNew();
                    //cek hari assign
                    if(dayAssign==true &&   strhourNow >= strhour &&  strhourNow <= endhour){
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
%>
<%
    if((iCommand==CMD_LOGIN)&&(dologin == SessUserSession.DO_LOGIN_OK)){
        if(userGroup==3){
            response.sendRedirect("outletonline/kitchen_list_order.jsp");
        }else{
            if(privMobileDisplay){
                if(typeView==0){
                            response.sendRedirect("homepage_mobile.jsp");
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
             response.sendRedirect("cashierweb/srcsalesordecashier.jsp?FRM_FIELD_SALES_CODE="+salesCode+"");
         }else{
             response.sendRedirect("outletonline/mainmenumobile.jsp?FRM_FIELD_SALES_CODE="+salesCode+"");
         }


     }}
%>
     
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
