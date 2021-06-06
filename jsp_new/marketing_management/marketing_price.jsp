<%-- 
    Document   : marketing_management
    Created on : Nov 7, 2019, 2:32:48 PM
    Author     : Sunima
--%>

<%@page import="com.dimata.util.Formater"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.marketing.MarketingManagement"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.marketing.PstMarketingManagement"%>
<%@ include file = "../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String URL = PstSystemProperty.getValueByName("SEDANA_APP_URL");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Marketing Management</title>
        
        <!-- Import data for css -->
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap-theme.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
                 <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/dist/css/skins/_all-skins.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/font-awesome/font-awesome.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/iCheck/flat/blue.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/iCheck/all.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/select2/css/select2.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
                <link rel="stylesheet" type="text/css" href="../styles/plugin/datatables/dataTables.bootstrap.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap-notify.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/JavaScript-autoComplete-master/auto-complete.css"/>
                
                <style>
                    .image-ilustartion {
    text-align: center;
    margin-top: 50px;
}
.image-ilustartion img {
    width: 450px;
}
.title h4 {
    font-size: 26px;
    font-weight: bold;
    text-align: center;
    margin-top: 20px;
}
.button-print {
    text-align: center;
}
#btn-price-list {
    color: #fff;
    background: #5FE3B3;
    font-weight: bold;
    padding: 14px 40px;
    margin-top: 20px;
}
                </style>
                
                
    </head>
    <body>
        
        <div class="container">
            <div class="row">
                
                <div class="col-md-12">
                    <div class="image-ilustartion">
                        <img src="https://cdn.dribbble.com/users/2893989/screenshots/8425069/media/99d252ae7bac2a17364f6909ac2e8348.png"/>
                    </div>
                    <div class="title">
                        
                    <h4>
                        Lihat Daftar semua barang Sekaran !
                    </h4>
                    </div>
                    <div class="button-print"><button class="btn" id="btn-price-list">Print Price List</button></div>
                    
                </div>
            </div>
        </div>
        
    <script type="text/javascript" src="../styles/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../styles/bootstrap/js/bootstrap.min.js"></script>  
    <script type="text/javascript" src="../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
    <script type="text/javascript" src="../styles/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-notify.js"></script>
    <script type="text/javascript" src="../styles/select2/js/select2.min.js"></script>
    <script>
        $(document).ready(function(){
           $("#btn-price-list").click(function(){
              window.open("<%=URL%>/PrintPriceListPdf"); 
           }); 
        });
    </script>
    </body>
</html>
