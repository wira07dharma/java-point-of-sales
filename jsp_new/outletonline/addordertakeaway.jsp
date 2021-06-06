<%-- 
    Document   : addordertakeaway
    Created on : Jan 6, 2015, 9:01:41 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlMemberReg"%>
<%@page import="com.dimata.hanoman.form.masterdata.CtrlContact"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%-- 
    Document   : addorder
    Created on : Dec 22, 2014, 4:02:26 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoom"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.CtrlOpenBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ include file = "../main/javainit.jsp" %>

<%
 int iCommand = FRMQueryString.requestCommand(request);
 long cashBillMainId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
 String billNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);
 String nameGuest = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]);
 String phoneNumber= FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_MOBILE_NUMBER]);
 String address= FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]);
 long oidCustomer = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
 
 CtrlOpenBillMain ctrlBillMain = new CtrlOpenBillMain(request);
 ControlLine ctrLine = new ControlLine();
 FrmBillMain frmBillMain = ctrlBillMain.getForm();
 
 CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
 
 Vector val_locationid = new Vector(1,1);
 Vector key_locationid = new Vector(1,1);
 String whereRoom = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+locationSales+"'";
 Vector vt_loc = PstRoom.list(0,0,whereRoom, PstLocation.fieldNames[PstLocation.FLD_CODE]);
 val_locationid.add("0");
 key_locationid.add("All Room");
 for(int d=0;d<vt_loc.size();d++){
        Room room = (Room)vt_loc.get(d);
        val_locationid.add(""+room.getOID()+"");
        key_locationid.add(room.getName());
 }

 int iErrCode = FRMMessage.NONE;
 
 if(cashBillMainId==0){
     
  if(oidCustomer==0){
         
        oidCustomer = ctrlMemberReg.actionSaveFromBill(iCommand, 0, 10,nameGuest,phoneNumber,address);
    } 
    
    iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, oidCustomer);
    
    
 } 
 
 BillMain billMain = ctrlBillMain.getBillMain();
 if(billMain.getOID()!= 0){
        response.sendRedirect("order.jsp?"+frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]+"="+billMain.getOID());
 }  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Taking Order - Queens Bali</title>
<%--
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../styles/takingorder/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- jQuery -->
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.dropbox.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery.tablesorter.js"></script>
<script src="../styles/takingorder/js/jquery.tablesorter.widgets.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/css/jquery.tablesorter.pager.css">
<script src="../styles/takingorder/js/widget-pager.js"></script>
<script type="text/javascript" src="../styles/jquery.min.js"></script>
--%>
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">

<%--    
<!-- jQuery -->
<script src="../styles/takingorder/js/jquery.js"></script>
<script type="text/javascript" src="../styles/jquery.min.js"></script>
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery_002.js"></script>
<script src="../styles/takingorder/js/jquery_003.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
<script src="../styles/takingorder/js/widget-pager.js"></script>
<script type="text/javascript" src="../styles/takingorder/js/bootstrap.min.js"></script>
--%>

<script language="JavaScript">
    
    $(function() {
            // hide child rows
            $('.tablesorter-childRow td').hide();
            var $table = $('.tablesorter-dropbox')
                    .tablesorter({
                            headerTemplate: '{content}{icon}', // dropbox theme doesn't like a space between the content & icon
                            sortList : [ [0,0] ],
                            showProcessing: true,
                            cssChildRow: "tablesorter-childRow",
                            widgets    : ["pager","zebra","filter"],
                            widgetOptions: {
                              filter_columnFilters: false,
                              filter_saveFilters : true,
                        }
            })
            $('.tablesorter-dropbox').delegate('.toggle', 'click' ,function(){
                    $(this).closest('tr').nextUntil('tr.tablesorter-hasChildRow').find('td').toggle();
                    return false;
            });

            $.tablesorter.filter.bindSearch( $table, $('.search') );
              $('select').change(function(){
              // modify the search input data-column value (swap "0" or "all in this demo)
              $('.selectable').attr( 'data-column', $(this).val() );
              // update external search inputs
              $.tablesorter.filter.bindSearch( $table, $('.search'), false );
            });
    });
    
    function cmdChangeLocation(idRoom){
        //alert("hellp");
        $("#posts").html("");
        $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckTableFromRoom?roomId="+idRoom+"",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#posts");
            }
        });
    }
    
    function cmdSave(){
        var nama =  document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value;
        if(nama==""){
            alert("Please Input Name First");
        }else{
            document.frmsrcsalesorder.command.value="<%=Command.SAVE%>";
            document.frmsrcsalesorder.action="addordertakeaway.jsp";
            document.frmsrcsalesorder.submit();
            
        }
    }

    function cmdBack(){
        document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
        document.frmsrcsalesorder.action="takeaway.jsp";
        document.frmsrcsalesorder.submit();
    }
    
    
    function cmdCheckGuest(nama,address,oid,nasionality){
        document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value=nama;
        document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_MOBILE_NUMBER]%>.value=address;
        document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value=oid;
        $(document).ready(function(){
		$('#myModal').modal('hide');
	});
   }
    
</script>
</head>
<body>
<div style="background:#f0f0f0; margin-bottom:10px; padding:5px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td style="text-align:left;">
	<i><strong> </strong> <span id="cartorder"></span></i>
    </td>
    <td style="text-align:right;">
	<i></i>
    </td>
  </tr>
</table>
</div>
    
<form name="frmsrcsalesorder" method ="post" action="" role="form">
    <input type="hidden" name="command" value="<%=iCommand%>">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=salesCodeNew%>">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationSales%>">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="1">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>" value="1">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]%>" value="0">
    <input type="hidden" name="sales_name" value="<%=salesName%>">
    <input type="hidden" name="warehouseSales" value="<%=warehouseSales%>">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=cashBillMainId%>">
    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>" value="<%=oidCustomer%>">    
    <input type="hidden" name="catId" value="" />
    <input type="hidden" name="oId" value="" />
    <input type="hidden" name="qty" value="" />
    <div class="container">
        <center>
        <a href="#"><img src="../styles/takingorder/img/queens-head.png" width="250" border="none" /></a>
        </center>
        <div style="margin-top:20px;">
        <a href="javascript:cmdBack()">&larr;back home</a>
            <hr style="margin-top:10px;" />
            <div class=row>
                <div class="col-md-12" style="margin-bottom:10px;">
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-8">
                                    <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" id="guestname" type="text" class="form-control" placeholder="Insert guest name.." required>
                                </div>
                                <div class="col-md-4">
                                    <span class="input-group-btn">
                                        <button class="search btn btn-flat" type="button">
                                            <img src="../images/search.jpg" border="none" />
                                        </button>
                                    </span>
                                    <%--<button class="btn btn-primary btn-md btn-block" type="button" data-toggle="modal" data-target="#myModal" >Search Member</button>--%>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-12">
                                    <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_MOBILE_NUMBER]%>" id="mobilenumber" type="text" class="form-control" placeholder="Insert Phone Number." required>
                                </div>
                            </div>
                        </div> 
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-12">
                                    <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdSave()" type="button" >Save Guest</button>
                                    <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdBack()" type="button" >Back to Main</button>
                                </div>
                            </div>
                        </div>
                </div>
            </div>
        </div>
        <hr/>
        <div style="margin:20px 0px; text-align:center;">
        </div>
    </div>                 
</form>
 
<script type="text/javascript" src="../styles/jquery.min.js"></script>
<!-- jQuery UI 1.10.3 -->
<script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<!-- Bootstrap -->
<script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript">
    $(document).ready(function(){
        function ajaxScriptParentPage(pageTarget, titlePage, pageShow, modalTemplate, oid, titleId, bodyId){
            $(titleId).html(titlePage);
            $(modalTemplate).modal("show");
            $(bodyId).html("Harap tunggu");
            var idNo = $("#guestname").val();
            $.ajax({
                type	: "POST",
                url	: pageTarget,
                data	: {"searchType":"parent", 
                            "pageShow":pageShow,
                            "studentName":idNo,
                            "oid":oid
                        },
                cache	: false,
                success	: function(data){
                    $(bodyId).html(data);

                },
                error : function(){
                    $(bodyId).html("Data not found");
                }
            });
        }
        
        //agar modal tidak close saat are di luar form di klik
        $("#myModal").modal({
                        backdrop:"static",
                        keyboard:false,
                        show:false
        });

        //fungsi jika tombol search di klik, maka akan menampilakn data
        $(".search").click(function(){
            ajaxScriptParentPage("ajaxDataSource.jsp","Search Member",2,"#myModal",0, "#modal-title", "#modal-body");
        });
        
        //dokument yang 
        function searchList(){
            $("form#searchList").submit(function(){
                $("#resultSearch").html("Harap Tunggu").fadeIn("medium");
                $("#resultFirst").hide();
                $.ajax({
                    type	: "POST",
                    url		: "ajaxDataSource.jsp",
                    data	: $(this).serialize(),
                    cache	: false,
                    success	: function(data){
                        $("#resultSearch").html(data).fadeIn("medium");

                    },
                    error : function(){

                        alert("error");
                    }
                });
                return false;
            });
        }
        
        //event modal, di tambahkan agar pada saat search, tetap berada di modal
        $("#myModal").on("shown.bs.modal",function (e){
            ajaxFunctionChildPage(searchList());

        });
        
        function ajaxFunctionChildPage(ajaxFunction){
            return ajaxFunction;
        }
        
        $("#buttonSave").click(function(){
            var checkedData = $("input[name=memberInfo]:checked");
            
            var dataName = checkedData.data("options").name;
            var telp = checkedData.data("options").telp;
            var oid = checkedData.data("options").oid;
            
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value=dataName;
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_MOBILE_NUMBER]%>.value=telp;
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value=oid;
            
            $("#myModal").modal("hide");
            
        });
   });
</script>             
<div id="myModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"></h4>
            </div>

            <div class="modal-body" id="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
                <button type="button" id="buttonSave" class="btn btn-primary">Select</button>
            </div>
        </div>
    </div>
</div>                                    
</body>
</html>

