<%-- 
    Document   : coba
    Created on : Aug 11, 2011, 2:25:34 PM
    Author     : Dimata
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.posbo.session.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
        /**double qty = 0.0;
        double konversiUnit = 0.0;
        double qtyAfterKonversi = 0.0;
        qty = 36;
        long unitFrom = 0;
        long unitTo = 0;
        unitFrom = 50039;
        unitTo = 50006;
        konversiUnit = PstUnit.getUnitFactory(unitFrom, unitTo, 0, 4);
        System.out.println("Hasil konversi unit : "+konversiUnit);
        qtyAfterKonversi = qty * konversiUnit;
        System.out.println("Hasil qty konversi unit : "+qtyAfterKonversi);**/

        //PstTempPostDoc.deleteHistoryPosting();
        String locationId = "";
        locationId = "504404430007198895";
        //locationId = "504404430007221239";
        long locationId1 = Long.parseLong(locationId);
        //PstTempPostDoc.insertSelectReceivePosting(locationId1);
        //PstTempPostDoc.insertSelectDispatchPosting(locationId1);
        //PstTempPostDoc.insertSelectCostingPosting(locationId1);
        //PstTempPostDoc.insertSelectReturnPosting(locationId1);
        //PstTempPostDoc.insertSelectSalesPosting(locationId1);

        try {
               boolean hasil = false;
                System.out.println("update Stok ");
                hasil = SessPosting_1.updateQtyStock(locationId1, 1);
                 if (hasil == true ){
                    System.out.println("Success");

                } else {
                System.out.println("Failed");
              }

            } catch (Exception e) {
                System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            }


        %>
        <h1><%//=konversiUnit %></h1>
        <p>
          <h1><%//=//qtyAfterKonversi %></h1>
    </body>
</html>
