<%-- 
    Document   : tableorder
    Created on : Jan 6, 2015, 9:22:01 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstTableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoom"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%-- 
    Document   : mainmenumobile
    Created on : Jan 6, 2015, 8:11:13 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>

<%@ include file = "../main/javainit.jsp" %>

<%
    
     int iCommand = FRMQueryString.requestCommand(request);
     int updateTable = FRMQueryString.requestInt(request, "updateTable");
     int updateStatusMeja = FRMQueryString.requestInt(request, "updateStatusMeja");
     long roomId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID]);
     long tableId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]);
            
     Vector val_locationid = new Vector(1,1);
     Vector key_locationid = new Vector(1,1);
     String whereRoom = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+locationSales+"'";
     Vector vt_loc = PstRoom.list(0,0,whereRoom, PstLocation.fieldNames[PstLocation.FLD_CODE]);
     val_locationid.add("0");
     key_locationid.add("All Room");
     long selectFirstRoom=0;
     for(int d=0;d<vt_loc.size();d++){
            Room room = (Room)vt_loc.get(d);
            if(d==0){
                selectFirstRoom=room.getOID();
            }
            val_locationid.add(""+room.getOID()+"");
            key_locationid.add(room.getName());
     }
     
     if(updateTable==1){
         //update meja
         if(tableId!=0){
            PstTableRoom.updateStatusConditionTable(tableId, updateStatusMeja);
         }
     }
     
     //disini pengecekan apakah ada bill atau tidak
   String whereTable=PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID]+"='"+locationSales+"'";
   int totalPaxFromTable = PstBillMain.getSumPaxOpenBillFromTable(whereTable);

   String whereTableBill=PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID]+"='"+locationSales+"'";
   int totalCapacity = PstTableRoom.getSumCapacity(whereTableBill);
   int sisaPax = totalCapacity - totalPaxFromTable;
     
  
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Taking Order - Queens Bali</title>
<link href="../styles/takingorder/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="../styles/bootstrap3.1/css/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- jQuery -->
<script src="../styles/takingorder/js/jquery.min.js"></script>
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.dropbox.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery.tablesorter.js"></script>
<script src="../styles/takingorder/js/jquery.tablesorter.widgets.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/js/jquery.tablesorter.pager.css">
<script src="../styles/takingorder/js/widgets/widget-pager.js"></script>
<script type="text/javascript" src="../styles/jquery.min.js"></script>
<script language="JavaScript">
    
    function cmdChangeLocation(idRoom,capacity){
       var capacity = document.frmsrcsalesorder.capacity.value;
        $("#posts").html("");
        $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CreateTableFromRoom?roomId="+idRoom+"&capacity="+capacity+"&locationId=<%=locationSales%>",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#posts");
            }
        });
    }
    
    function cmdBack(){
        document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
        document.frmsrcsalesorder.action="salesmobile.jsp";
        document.frmsrcsalesorder.submit();
    }
    
    function updateStatusMeja(){
        document.frmsrcsalesorder.action="tableorder.jsp";
        document.frmsrcsalesorder.submit();
    }
</script>
</head>
<body>

<%@ include file = "header.jsp" %>

<div style="margin-top:20px;">
    <a href="<%=approot%>/outletonline/mainmenumobile.jsp">Back to Main Menu</a>
<hr style="margin-top:10px;" />
<style type="text/css">
.items{
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	width:46%; 
	float:left; 
	background:#F0F0F0; 
	margin:5px 2%; 
	padding:5px; 
	height:145px; 
	text-align:center;
}
.items:hover{
	background:#F90;
}
</style>
<center><h1><b><%=sisaPax%>/<%=totalCapacity%></b></h1></center>
<form name="frmsrcsalesorder" method ="post" action="" role="form">
     <input type="hidden" name="command" value="<%=iCommand%>">
    <%--buat combo box tentang ruangan--%>
    <div class="form-group">
        <div class="col-md-6"> <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/tableorder.jsp"' type="button">Table View</button>
        </div>
        <div class="col-md-6"> <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/salesmobile.jsp"' type="button">List View</button>
        </div>
    </div>
    <div style="clear:left; margin-bottom:10px;"></div>
    <div class="form-group">
        <div class="col-md-12">
        <%=ControlCombo.drawBoostrap(FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID],"form-control",null,""+selectFirstRoom, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation(this.value)\"")%>
        </div>
    </div>
    <div style="clear:left; margin-bottom:10px;"></div>
    <div class="form-group">
         <div class="col-md-12">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Search" name="capacity" id="srch-term">
                <div class="input-group-btn">
                    <button class="btn btn-danger" type="button" onclick="javascript:cmdChangeLocation('0',this.value)" ><i class="glyphicon glyphicon-search"></i></button>
                </div>
            </div>
         </div>
     </div>
    <div style="clear:left; margin-bottom:10px;"></div>
    <div class="form-group">
        <div class="col-md-12" id="posts"></div>
    </div>
    <div style="clear:left; margin-bottom:10px;"></div>
</form>
<button name="submit" class="btn btn-danger btn-md btn-block" type="submit"></button>
</div>
<hr />
<div style="margin:20px 0px; text-align:center;">
</div>
</div>
<script language="JavaScript">
    $("#posts").html("");
        $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CreateTableFromRoom?roomId=<%=selectFirstRoom%>&capacity=0&locationId=<%=locationSales%>",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#posts");
            }
        });
</script>
</body>
</html>