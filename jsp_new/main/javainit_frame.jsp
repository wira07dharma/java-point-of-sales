<%-- 
    Document   : javainit_frame
    Created on : Jul 14, 2017, 11:12:33 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.entity.masterdata.Sales"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PictureCompany"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PstPictureCompany"%>
<%@ page import="java.util.*,
                 com.dimata.posbo.session.admin.SessUserSession,
                 com.dimata.common.entity.periode.Periode,
                 com.dimata.common.entity.periode.PstPeriode,
                 com.dimata.qdep.entity.I_DocType,
                 com.dimata.qdep.entity.I_DocStatus,
                 com.dimata.qdep.entity.I_Approval,
                 com.dimata.util.Formater,
                 com.dimata.posbo.entity.admin.AppUser,
                 com.dimata.common.entity.system.PstSystemProperty" %>
<%
com.dimata.posbo.entity.masterdata.Material.setMaterialSwitchType(com.dimata.posbo.entity.masterdata.Material.WITH_USE_SWITCH_MERGE_MANUAL);
String approotFrame= request.getContextPath();//"/D-ProChainPos-V3";
// user is logging in or not
boolean isLoggedInFrame = false;
int userGroupNewStatusframe = -1;
String userNameframe = "";
long userIdframe = 0;

//add session 20120711 by mirahu 
session.setMaxInactiveInterval(60 * 60 * 2);
SessUserSession userSessionframe = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
try{
    if(userSessionframe==null){
        userSessionframe= new SessUserSession();
    }else{
        if(userSessionframe.isLoggedIn()==true){
            isLoggedInFrame  = true;
            AppUser appUser = userSessionframe.getAppUser();
            userGroupNewStatusframe = appUser.getUserGroupNew();
            userNameframe = appUser.getLoginId();
            userIdframe = appUser.getOID();
        }
    }
}catch (Exception exc){
    //System.out.println(" >>> Exception during check login");
}

//add sesion untuk sales
long locationSalesFrame=0;
long salesCurrencyIdFrame=0;
long oidSalesLogFrame=0;
String warehouseSalesFrame="";
String salesNameFrame ="";
String salesCodeNewFrame ="";
Sales salesLogFrame = (Sales)session.getValue(SessUserSession.HTTP_SESSION_SALES);
try{
     if(salesLogFrame!=null){
        salesNameFrame = salesLogFrame.getName();
        salesCodeNewFrame=salesLogFrame.getCode();
        oidSalesLogFrame=salesLogFrame.getOID();
        Location xLocation=PstLocation.fetchExc(salesLogFrame.getLocationId());
        locationSalesFrame=salesLogFrame.getLocationId();
        warehouseSalesFrame= salesLogFrame.getName();
        salesCurrencyIdFrame = salesLogFrame.getDefaultCurrencyId();
     }
}catch (Exception exc){
}  
   
%>