<%-- 
    Document   : marketing_management
    Created on : Nov 7, 2019, 2:32:48 PM
    Author     : Sunima
--%>

<%@page import="com.dimata.posbo.entity.marketing.MarketingCatalog"%>
<%@page import="com.dimata.posbo.entity.marketing.PstMarketingCatalog"%>
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
    Vector listCatalog = PstMarketingCatalog.list(0, 0, "", "");
    int allCatalog = listCatalog.size();
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Katalog</title>
        
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
                    .card {
    box-shadow: 0px 0px 20px #eeeeeea8;
}
    .card .panel-heading {
    font-weight: bold;
    font-size: 17px;
}
.card .panel-body>h2 {
    font-weight: bold;
    color: #444;
    text-align: center;
    font-size: 30px;
}

span.more {
    text-align: right;
    display: block;
}
.container{
    width:1000px;
    padding-bottom: 50px;
}
h3.title {
    margin-bottom: 30px;
    font-size: 20px;
    font-weight: bold;
}
button#add_promotion {
    float: right;
    margin: 20px;
    padding: 9px 15px;
    border-radius: 3px;
    font-weight: normal;
    border: none;
    background: #21d087;
    color: #fff;
    font-size: 17px;
}

.btn_edit {
    background: #21d087;
    color: #fff;
}
.statusDraft {
    color: #ffb100;
    font-weight: bold;
}
.statusApprove {
    color: #21d087;
    font-weight: bold;
}

.statusCancel {
    color: #ff5656;
    font-weight: bold;
}
li.paginate_button.active a {
    border-radius: 3em !important;
    background: #dddddd;
    color: #444;
    margin-left: 5px;
    margin-right: 5px;
    border: none;
}
span.more a {
    color: #0043ff;
    font-weight: 600;
}
.badge-new {
    height: 25px;
    width: 25px;
    text-align: center;
    background: #21d087;
    float: right;
}
.btn_delete {
    background: #f74d4d;
    color: #fff;
}
button#print_price_list {
    float: right;
    margin-top: 20px;
    padding: 10px 15px;
    background: #ff9c22;
    color: #fff;
}

.count-list {
    width: 100px;
    height: 100px;
    display: grid;
    align-items: center;
    border-radius: 100%;
    margin: 0 auto;
    justify-items: center;
    border: 6px solid #55d6a0;
}
.wrapper-total {
    margin-top: 50px;
    margin-bottom: 50px;
}
.wrapper-total h4{
    text-align: center;
    margin-top: 20px;
}
.card-wrapper {
    box-shadow: 0px 0px 20px #eee;
    border-radius: 20px;
    padding: 30px;
    height: 200px;
    text-align: center;
    overflow: hidden;
}
span.main-value {
    font-weight: bold;
    font-size: 50px;
    color: #444;
}
#btn-add-brosur {
    background: #55d6a0;
    padding: 11px 30px;
    font-weight: bold;
    color: #fff;
}

                </style>
                
                
    </head>
    <body>
        
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
        
       <!-- start content -->
       <div class="container">
           <h3 class="title">Katalog</h3>
           <div class="row">
               <div class="col-md-3 wrapper-total">
                   <div class="card-wrapper">
                       <div class="count-list">
                       <span class="main-value">
                           <%=allCatalog%>
                       </span>
                   </div>
                   <h4>Total</h4>
                   </div>
                   
               </div>
               <div class="col-md-3 wrapper-total">
                   <div class="card-wrapper">
                       <button class="btn" id="btn-add-brosur">Add Katalog</button>
                   </div>
                   
               </div>
           </div>
       <div class="row">
                 <div class="panel card">
                     <div class="panel-heading"><b>Status Pemasaran Elektronik</b></div>
  <div class="panel-body">
      <table id="table_item" class="table table-striped table-bordered" style="width:100%">
          <thead>
              <tr>
                <th>Title</th>
                <th>Periode</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
         
          </thead>
          <tbody>
              <%
                
                 
                // validasi status
                String statusName = "";
                Vector listData = PstMarketingCatalog.list(0, 0, "", "");
                
                for(int i = 0; i < listData.size(); i++){
                MarketingCatalog marketingCatalog = (MarketingCatalog) listData.get(i);
                // status
                String status = PstMarketingCatalog.status[marketingCatalog.getMarketing_katalog_status()];
                
              %>
              <tr>
                  <td class="title-item"><b><%=marketingCatalog.getMarketing_katalog_title()%></b></td>
                  <td><%=marketingCatalog.getMarketing_katalog_start_date()+" / "+marketingCatalog.getMarketing_katalog_end_date()%></td>
                  <td class="status<%=status%>"><%=status%></td>
                  <td><button data-oid="<%=marketingCatalog.getOID()%>" class="btn btn_edit" data-toggle="tooltip" data-placement="top" title="Edit"><i class="fa fa-edit"></i></button>  <button data-oid="<%=marketingCatalog.getOID()%>" class="btn btn_delete" data-toggle="tooltip" data-placement="top" title="Delete"><i class="fa fa-trash"></i></button></td>
              </tr>  
              <%
                  }
              %>
             
          </tbody>
      </table>
  </div>
</div>
             </div>
       </div>
       <!-- javascript -->
       <script>
           $(document).ready(function(){
            $('#table_item').DataTable();
               
             $(function () {
  $('[data-toggle="tooltip"]').tooltip()
});      
var base = "<%=approot%>";
           $(".btn_edit").click(function(){
               
               var oid = $(this).data('oid');
               // moving page
               window.location ="add_katalog.jsp?command=<%=Command.EDIT%>&oid="+oid;
           });
           $(".btn_delete").click(function(){
                    var url = ""+base+"/AjaxMarketing";
                    var oid = $(this).data('oid');
                    var data = "command=<%=Command.DELETE%>&FRM_FIELD_DATA_FOR=deleteCatalog&OID_CATALOG="+ oid;
                    
                    $.ajax({
                        url : ""+url+"",
                        data : ""+data+"",
                        type : "POST",
                        dataType : "json",
                        async : false,
                        cache : false,
                        success: function (data) {
                        
                    },
                    error: function (data) {
                        
                    }
                    
                    }).done(function(data){
                        window.location="marketing_katalog.jsp";
                    });
                    
                    
                });
            $("#btn-add-brosur").click(function(){
               window.location='add_katalog.jsp?command=<%=Command.ADD%>';
           });
           
            });
          
           </script>
    </body>
</html>
