<%-- 
    Document   : kitchen_list_order
    Created on : Jan 7, 2015, 4:34:38 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
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
    long oidBillDetail = FRMQueryString.requestLong(request, "FRM_FIELD_CASH_BILL_DETAIL_ID");
    int update = FRMQueryString.requestInt(request, "UPDATE");
    int status = FRMQueryString.requestInt(request, "STATUS");
    
    //disini update item sudah check out dari kitchent
    if(oidBillDetail!=0 & update!=0){
        boolean result = PstBillDetail.checkUpdateStatusItem(oidBillDetail,status);
        if(result){
             int xx = PstBillDetail.updateStatusItem(oidBillDetail,status);
        }
    }
    
    String where ="";
    if (1==1){
        where = " cbm.TRANSACTION_STATUS=1 AND cbm.TRANSACTION_TYPE=0 AND cbm.DOC_TYPE=0 AND bd.STATUS=0 ";
    }else{
        where = " lc.LOCATION_ID = 0"+
                " AND cbm.TRANSACTION_STATUS=1 AND cbm.TRANSACTION_TYPE=0 AND cbm.DOC_TYPE=0 ";
    }
    
    Vector listTransaction = PstBillMain.listItemOpenBill(0, 0,where, "");
    
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="refresh" content="5">
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
<a href="<%=approot%>/logout.jsp">Log Out</a>
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
</script>
<script language="JavaScript">
            var displaycountdown="no";
</script>
<form name="frmsrcsalesorder" method ="post" action="" role="form">
    <input type="hidden" name="command" value="<%=iCommand%>"> 
    <div class="guest" style="margin:10px 0px;">
    <input data-lastsearchtime="1416898435043" class="form-control search selectable" placeholder="Search keyword.." data-column="all" type="search">
        <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
            <thead>
                <tr role="row" class="tablesorter-headerRow">
                    <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="5%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="25%"><div class="tablesorter-header-inner">Name<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="20%"><div class="tablesorter-header-inner">Qty<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="10%"><div class="tablesorter-header-inner">Table No<i class="tablesorter-icon"></i></div></th>
                     <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="20%"><div class="tablesorter-header-inner">Note<i class="tablesorter-icon"></i></div></th>
                     <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="5" width="10%"><div class="tablesorter-header-inner">Status<i class="tablesorter-icon"></i></div></th>
                    <th aria-label="&nbsp;: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="6" width="20%"><div class="tablesorter-header-inner">&nbsp;<i class="tablesorter-icon"></i></div></th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th class="tablesorter-headerAsc" data-column="0" width="5%">#</th>
                    <th data-column="1" width="25%">Name</th>
                    <th data-column="2" width="10%">Qty</th>
                    <th data-column="3" width="10%">Table No</th>
                    <th data-column="4" width="20%">Note</th>
                    <th data-column="5" width="10%">Status</th>
                    <th data-column="6" width="20%">&nbsp;</th>
                </tr>
            </tfoot>
            <tbody aria-relevant="all" aria-live="polite">
                <%
                int start = 0;
                for(int i = 0; i < listTransaction.size(); i++) {
                    Vector dataBill = (Vector) listTransaction.get(i);
                    Billdetail billDetail = (Billdetail)dataBill.get(0);
                    TableRoom table =(TableRoom)dataBill.get(1);
                    String statusItem="";
                    if(billDetail.getStatus()==0){
                        statusItem="Order";
                    }else if(billDetail.getStatus()==1){
                        statusItem="Check Out Kitchen";
                    }else{
                        statusItem="Reserved";
                    }
                    start = start + 1;
                    %>
                    <tr style="" class="tablesorter-hasChildRow odd">
                    <td style="text-align:center;"><a href="#" class="toggle"><%=start%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=billDetail.getItemName()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=billDetail.getQty()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=table.getTableNumber()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=billDetail.getNote()%></a></td>
                    <td align="left"><a href="#" class="toggle"><%=statusItem%></a></td>
                    <td align="left">
                        <a href="kitchen_list_order.jsp?FRM_FIELD_CASH_BILL_DETAIL_ID=<%=billDetail.getOID()%>&STATUS=1&UPDATE=1" style="color:#F90;"><em>CHECK OUT</em>
                        </a>
                    </td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>  
    </div> 
</form>
</div>
<hr>
<div style="margin:20px 0px; text-align:center;">
</div>
</div>
<script language="JavaScript">
  window.open("<%=approot%>/servlet/com.dimata.posbo.printing.kitchenorder.PrintItemOrder","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
</script>    
</body></html>
