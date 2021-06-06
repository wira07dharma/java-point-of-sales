<%-- 
    Document   : addorder
    Created on : Dec 22, 2014, 4:02:26 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.location.Negara"%>
<%@page import="com.dimata.common.entity.location.PstNegara"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
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
 long roomId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID]);
 String negaraId = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]);
 long tableId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]);
 long oidCustomer = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
 int splitBill = FRMQueryString.requestInt(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SPLIT]);
 int pax = FRMQueryString.requestInt(request, "PAX_BALANCE");
 
 CtrlOpenBillMain ctrlBillMain = new CtrlOpenBillMain(request);
 ControlLine ctrLine = new ControlLine();
 FrmBillMain frmBillMain = ctrlBillMain.getForm();
 
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
    iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, 0);
 } 
 
 BillMain billMain = ctrlBillMain.getBillMain();
 if(billMain.getOID()!= 0){
     if(splitBill>1){
         response.sendRedirect("salesmobile.jsp?"+frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID]+"="+roomId+"&"+frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]+"="+tableId);        
     }else{
         response.sendRedirect("order.jsp?"+frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]+"="+billMain.getOID());
     }
        
 }

Vector listNegara = new Vector(1,1);
Vector val_negaraid = new Vector(1,1);
Vector key_negaraid = new Vector(1,1);
//update by fitra
listNegara = PstNegara.list(0,0,"","");  
for(int d=0;d<listNegara.size();d++){
        Negara negara = (Negara) listNegara.get(d);
        val_negaraid.add(""+negara.getNmNegara()+"");
        key_negaraid.add(negara.getNmNegara());
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Taking Order - Queens Bali</title>
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
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
        //alert("1");
        var mejano =  document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]%>.value;
        if(mejano==0){
            alert("Please Select Table Number First");
        }else{
            var nasionality = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value;
            if(nasionality!=""){
                var pax = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PAX_NUMBER]%>.value;
                if(pax!=0){
                    document.frmsrcsalesorder.command.value="<%=Command.SAVE%>";
                    document.frmsrcsalesorder.action="addorder.jsp";
                    document.frmsrcsalesorder.submit();
                }else{
                    alert("Please Input Pax Number First");
                }
            }else{
                alert("Please Input Nasionality First");
            }
            
        }
    }

    function cmdBack(){
        document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
        document.frmsrcsalesorder.action="tableorder.jsp";
        document.frmsrcsalesorder.submit();
    }
    
    function cmdOpenMember(nameMember){
        $("#posts").html("");
        $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckMember?nameMember="+nameMember+"",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#tableMember");
            }
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
    <input type="hidden" name="PAX_BALANCE" value="" />
    <div class="container">
        <center>
        <a href="#"><img src="../styles/takingorder/img/queens-head.png" width="250" border="none" /></a>
        </center>
        <div style="margin-top:20px;">
        <a href="javascript:cmdBack()"> &larr; Back Table Order</a>
            <hr style="margin-top:10px;" />
            <div class=row>
                <div class="col-md-12" style="margin-bottom:10px;">
                    <form method="post" class="form-horizontal" role="form" id="validate">
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
                                </div>
                            </div>
                       </div>
                       <div class="form-group">
                           <div class=row>
                                <div class="col-md-12">
                                    <%=ControlCombo.drawBoostrap(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY],"form-control",null,""+negaraId, val_negaraid, key_negaraid, "")%>
                                </div>
                            </div>
                       </div> 
                       <div class="form-group">
                           <div class=row>
                                <div class="col-md-12">
                                    <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PAX_NUMBER]%>" id="paxnumber" onKeyUp="javascript:cmdCheckPax(this.value)" type="text" class="form-control" placeholder="Insert Pax Number.." required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-12">
                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SPLIT]%>" id="paxnumber"  type="text" class="form-control" placeholder="Insert Number Of Split.." required>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-12">
                                <%=ControlCombo.drawBoostrap(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID],"form-control",null,""+roomId, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation(this.value)\"")%>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class=row>
                                <div class="col-md-12" id="posts"></div>
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
                    </form>
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
            ajaxScriptParentPage("ajaxDataSource.jsp","Search Member",1,"#myModal",0, "#modal-title", "#modal-body");
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
            var dataOid = checkedData.data("options").oid;
            var dataNationality = checkedData.data("options").nationality;
            
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value=dataName;
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value=dataOid;
            document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value=dataNationality;
              
            $("#myModal").modal("hide");
        });
    
   });
</script>                             
<script language="JavaScript">
    $("#posts").html("");
        $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckTableFromRoom?roomId=<%=roomId%>&tableId=<%=tableId%>",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#posts");
            }
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
</body>
</html>
