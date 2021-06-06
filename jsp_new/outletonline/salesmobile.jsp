<%-- 
    Document   : salesmobile
    Created on : Dec 22, 2014, 10:18:34 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file = "../main/javainit.jsp" %>
<%
    
    int iCommand = FRMQueryString.requestCommand(request);
    
    long roomId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID]);
    long tableId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]);
    //long tableId=0;
    //long roomId=0;
    
    String whereOpenBill=" cbm.DOC_TYPE=0 AND cbm.TRANSACTION_TYPE=0 AND cbm.TRANSACTION_STATUS=1 AND cbm.TABLE_ID!=0 ";
    if(tableId!=0){
        whereOpenBill=whereOpenBill+" AND cbm.TABLE_ID="+tableId ;
    }
    if(roomId!=0){
        whereOpenBill=whereOpenBill+" AND cbm.ROOM_ID="+roomId;
    }
    Vector listTransaction = PstBillMain.listOpenBill(0, 0,whereOpenBill, "");
    
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Taking Order - Queens Bali</title>
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
<!-- jQuery -->
<script src="../styles/takingorder/js/jquery.js"></script>
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery_002.js"></script>
<script src="../styles/takingorder/js/jquery_003.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
<script src="../styles/takingorder/js/widget-pager.js"></script>
</head>
<body>
<div style="background:#f0f0f0; margin-bottom:10px; padding:5px;">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tbody><tr>
    <td style="text-align:left;">
	<i><strong> </strong> <span id="cartorder"></span></i>
    </td>
    <td style="text-align:right;">
	<i></i>
    </td>
  </tr>
</tbody></table>
</div>
<div class="container">
<center>
<a href="#"><img src="../styles/takingorder/img/queens-head.png" border="none" width="250"></a>
</center>
<div style="margin-top:20px;">
<a href="<%=approot%>/outletonline/tableorder.jsp">Back Table Order</a> | <a href="<%=approot%>/outletonline/mainmenumobile.jsp">Back to Main Menu</a>
<hr style="margin-top:10px;">
<script id="js">
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
        
        function cmdAddGuest(){
                alert("xx");
                document.frmsrcsalesorder.command.value="<%=Command.NONE%>";
                alert("xxt");
                document.frmsrcsalesorder.action="addorder.jsp";
                alert("xxy");
                document.frmsrcsalesorder.submit();
        }
        
        function cmdTableView(){
            document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
            document.frmsrcsalesorder.action="tableorder.jsp";
            document.frmsrcsalesorder.submit();
        }
        
        function cmdListView(){
            document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
            document.frmsrcsalesorder.action="salesmobile.jsp";
            document.frmsrcsalesorder.submit();
        }
</script>
<form name="frmsrcsalesorder" method ="post" action="" role="form">  
    <input type="hidden" name="command" value="<%=iCommand%>">
     <div class="form-group">
        <div class="col-md-6"> <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/tableorder.jsp"' type="button">Table View</button>
        </div>
        <div class="col-md-6"> <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/salesmobile.jsp"' type="button">List View</button>
        </div>
    </div>
    <div style="clear:left; margin-bottom:10px;"></div>  
    <button class="btn btn-primary btn-md btn-block" type="button" onclick='location.href="<%=approot%>/outletonline/addorder.jsp"' >Add New Guest</button>
    <div class="guest" style="margin:10px 0px;">
    <input data-lastsearchtime="1416898435043" class="form-control search selectable" placeholder="Search keyword.." data-column="all" type="search">
        <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
            <thead>
                <tr role="row" class="tablesorter-headerRow">
                    <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="15%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="30%"><div class="tablesorter-header-inner">Guest Name<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="20%"><div class="tablesorter-header-inner">Room<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="20%"><div class="tablesorter-header-inner">Table<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="&nbsp;: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="10%"><div class="tablesorter-header-inner">&nbsp;<i class="tablesorter-icon"></i></div></th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th class="tablesorter-headerAsc" data-column="0" width="15%">#</th>
                    <th data-column="1" width="35%">Guest Name</th>
                    <th data-column="2" width="20%">Room</th>
                    <th data-column="3" width="20%">Table No</th>
                    <th data-column="4" width="10%">&nbsp;</th>
                </tr>
            </tfoot>
            <tbody aria-relevant="all" aria-live="polite">
                <%
                int start = 0;
                for(int i = 0; i < listTransaction.size(); i++) {
                    Vector dataBill = (Vector) listTransaction.get(i);
                    BillMain billMain = (BillMain)dataBill.get(0);
                    Room room = (Room) dataBill.get(1);
                    TableRoom table =(TableRoom)dataBill.get(2);

                    start = start + 1;
                    %>
                    <tr style="" class="tablesorter-hasChildRow odd">
                    <td style="text-align:center;"><a href="#" class="toggle"><%=start%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=billMain.getGuestName()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=room.getName()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=table.getTableNumber()%></a></td>
                    <td align="left">
                        <a href="order.jsp?FRM_FIELD_CASH_BILL_MAIN_ID=<%=billMain.getOID()%>&FRM_FIELD_SALES_CODE=<%=salesName%>&FRM_FIELD_BILL_NUMBER=<%=billMain.getInvoiceNumber()%>&typeFood=0" style="color:#F90;"><em>ORDER</em>
                        </a> | 
                        <a href="listorder.jsp?FRM_FIELD_CASH_BILL_MAIN_ID=<%=billMain.getOID()%>&FRM_FIELD_SALES_CODE=<%=salesName%>&FRM_FIELD_BILL_NUMBER=<%=billMain.getInvoiceNumber()%>&typeFood=0" style="color:#F90;"><em>VIEW</em>
                        </a> |
                        <a href="#">Move</a>
                    </td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>  
    </div> 
    <button class="btn btn-primary btn-md btn-block" type="button" onclick='location.href="<%=approot%>/outletonline/addorder.jsp"' >Add New Guest</button>        
</form>
</div>
<hr>
<div style="margin:20px 0px; text-align:center;">
</div>
</div>

</body></html>