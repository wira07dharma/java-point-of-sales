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
    // simple dashboard
    //e katalog
    Vector katalogSize = PstMarketingManagement.list(0, 0, PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_TYPE]+"=1", "");
    int allKatalog = katalogSize.size();
    // e price
    Vector priceSize = PstMarketingManagement.list(0, 0, PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_TYPE]+"=2", "");
    int allPrice = priceSize.size();
    // e brosur
    Vector brosurSize = PstMarketingManagement.list(0, 0, PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_TYPE]+"=3", "");
    int allBrosur = brosurSize.size();
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
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
           <h3 class="title">Manajemen Pemasaran</h3>
       
       <div class="row">
               <div class="col-md-4">
                   <div class="panel card">
                <div class="panel-heading">E-Brosur
                    <span id="badge-brosur" class="badge badge-new"></span>
                </div>
                <div class="panel-body">
                    <h2><%=allBrosur%></h2>
                    
                    <span class="more"><a href="#">More</a></span>
                </div>
              </div>
               </div>
               <div class="col-md-4">
                   <div class="panel card">
                <div class="panel-heading">E-Katalog
                <span id="badge-katalog" class="badge badge-new"></span>
                </div>
                <div class="panel-body">
                    <h2><%=allKatalog%></h2>
                    
                    <span class="more"><a href="#">More</a></span>
                </div>
              </div>
               </div>
               <div class="col-md-4">
                   <div class="panel card">
                <div class="panel-heading">E-Price
                <span id="badge-price" class="badge badge-new"></span>
                </div>
                <div class="panel-body">
                    <h2><%=allPrice%></h2>
                    
                    <span class="more"><a href="#">More</a></span>
                </div>
              </div>
               </div>
          
       </div>
                    <div class="row">
                        <button id="add_promotion" class="btn"><i class="fa fa-plus">  Tambah Pemasaran</i></button>
                        <button id="print_price_list" class="btn"><i class="fa fa-print"> Print Price List</i></button>
                    </div>
       <div class="row">
                 <div class="panel card">
                     <div class="panel-heading"><b>Status Pemasaran Elektronik</b></div>
  <div class="panel-body">
      <table id="table_item" class="table table-striped table-bordered" style="width:100%">
          <thead>
              <tr>
                <th>Title</th>
                <th>Type</th>
                <th>Periode</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
         
          </thead>
          <tbody>
              <%
                Vector listData = new Vector();
                
                 
                // validasi status
                String typeName = "";
                String statusName = "";
                String label = "", marketingNote = "";
                listData = PstMarketingManagement.list(0, 0, "", "");
                
                for(int i = 0; i < listData.size(); i++){
                MarketingManagement entMarketingManagement = (MarketingManagement) listData.get(i);
                               
                int typeNum = entMarketingManagement.getMarketing_type();
                int statusNum = entMarketingManagement.getMarketing_status();
                marketingNote = entMarketingManagement.getMarketing_note();
              
                if(typeNum == 1){
                    typeName = "E-Katalog";
                }else if(typeNum == 2){
                    typeName = "E-Price";
                }else if(typeNum == 3){
                    typeName = "E-Brosur";
                }             
                
                // status
                String status = PstMarketingManagement.status[statusNum];
                
              %>
              <tr>
                  <td class="title-item"><b><%=entMarketingManagement.getMarketing_title() %></b> <%=label%></td>
                  <td><%=typeName%></td>
                  <td><%=entMarketingManagement.getStart_date()+"/"+entMarketingManagement.getEnd_date() %></td>
                  <td class="status<%=status%>"><%=status%></td>
                  <td><button data-oid="<%=entMarketingManagement.getOID()%>" class="btn btn_edit" data-toggle="tooltip" data-placement="top" title="Edit"><i class="fa fa-edit"></i></button>  <button data-oid="<%=entMarketingManagement.getOID()%>" class="btn btn_view" data-toggle="tooltip" data-placement="top" title="Check"><i class="fa fa-eye"></i></button> <button data-oid="<%=entMarketingManagement.getOID()%>" class="btn btn_delete" data-toggle="tooltip" data-placement="top" title="Delete"><i class="fa fa-trash"></i></button></td>
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
            showBagde();
            function showBagde(){
                var bSize = $(".note3").length;
                var pSize = $(".note2").length;
                var kSize = $(".note1").length;
                $("#badge-brosur").text(bSize);
                $("#badge-price").text(pSize);
                $("#badge-katalog").text(kSize);
            }

            $(".btn_edit").click(function(){
               var oid = $(this).data('oid');
               
               // moving page
               window.location ="add_marketing.jsp?command=<%=Command.EDIT%>&oid="+oid;
           });
           
           $(".btn_view").click(function(){
               
               var oid = $(this).data('oid');
               // moving page
               window.location ="add_marketing.jsp?command=<%=Command.VIEW%>&oid="+oid;
           });
           $(".btn_delete").click(function(){
               
               var oid = $(this).data('oid');
               // moving page
               window.location ="add_marketing.jsp?command=<%=Command.DELETE%>&oid="+oid;
           });
           
            $("#add_promotion").click(function(){
               window.location='add_marketing.jsp?command=<%=Command.ADD%>';
           });
           $("#print_price_list").click(function(){
              window.open("<%=approot%>/servlet/com.dimata.posbo.report.marketing.PrintPriceListPdf"); 
           });
           
            });
          
           </script>
    </body>
</html>
