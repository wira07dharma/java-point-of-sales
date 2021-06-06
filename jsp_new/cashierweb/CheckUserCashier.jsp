<%-- 
    Document   : CheckUserCashier
    Created on : 18 Jun 13, 13:34:38
    Author     : Wiweka
--%>

<%@ page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page import="com.dimata.qdep.system.I_SystemInfo"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*, java.sql.Time,
         com.dimata.posbo.form.admin.service.FrmBackUpService" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.pos.entity.balance.*" %>
<%@ page import = "com.dimata.pos.form.balance.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.pos.entity.masterCashier.*" %>


<%
            int countUserCashier = PstCashCashier.getCount(PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+" = '1' AND "+ PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+ " = '" +userId+ "'");
            if(countUserCashier != 0){
                //response.sendRedirect("cashier_lyt.jsp");
                switch (userGroupNewStatus) {
                    case PstAppUser.GROUP_CASHIER:
                           // response.sendRedirect("cashier_lyt.jsp?FRM_FIELD_DISC_TYPE=1");
                             response.sendRedirect("cashier_lyt.jsp?");
                        break;

                    case PstAppUser.GROUP_SALES_ADMIN:
                            response.sendRedirect("srcsalesordecashier.jsp");
                        break;
                  default:
                }
            }
%>

