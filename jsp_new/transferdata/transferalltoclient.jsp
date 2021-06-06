<%@ page language="java" %>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.session.transferdata.TransMasterdata,
                 com.dimata.posbo.entity.transferdata.SrcTransferData,
                 com.dimata.posbo.form.transferdata.FrmSrcTransferData,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo"%>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_TRANSFER_DATA); %>
<%@ include file = "../main/checkuser.jsp" %>


<!-- JSP Block -->
<%!
public static final String textListGlobal[][] = {
    {"Sistem","Data Exchange","Expor Data","Tipe Data","Nama Data",
    "Proses Expor Data akan melakukan expor data pada sistem ke direktori"},
    {"System","Data Exchange","Export Data","Data Type","Data Name",
    "Export Data Processing will export data in system to directory"}
};
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int choice = FRMQueryString.requestInt(request,"hidden_choice");

com.dimata.posbo.session.transferdata.TransMasterdata.OUT_TO_FILE = "No";
com.dimata.posbo.session.transferdata.TransMasterdata.CMD_DUMP = "mysqldump ";
com.dimata.posbo.session.transferdata.TransMasterdata.CMD_UNDUMP = "mysql ";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_NAME = "pos_yashoda_real";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_USER = "root";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_PASSWORD = "";

//array of table in database used
String[] table_names = TransMasterdata.showTable(); 

//variable use to transfer data
String pathDataOut = String.valueOf(PstSystemProperty.getValueByName("PATH_DATA_OUT"));
String pathMysql = String.valueOf(PstSystemProperty.getValueByName("PATH_MYSQL_BIN"));
//Hashtable hashData = TransMasterdata.listDataToTransfer(table_names,choice);
Vector listDataToTransfer = TransMasterdata.listDataToTransfer(table_names,choice);
TransMasterdata transferMasterdata = new TransMasterdata(pathMysql,pathDataOut);
SrcTransferData srcTransferData = new SrcTransferData();
FrmSrcTransferData frmSrcTransferData = new FrmSrcTransferData(request,srcTransferData);
String message = "";
try{
    if(iCommand==Command.SUBMIT||iCommand==Command.LOAD){
	
        frmSrcTransferData.requestEntityObject(srcTransferData);
        srcTransferData = frmSrcTransferData.getEntityObject();
        session.putValue("SESS_TRANSFER",srcTransferData);
        choice = srcTransferData.getTypeTransfer();
        if (iCommand==Command.SUBMIT){
            int result = transferMasterdata.transferAllToClient(srcTransferData.getIndexTable(),srcTransferData.getTypeTransfer());
            message = transferMasterdata.statusNames[SESS_LANGUAGE][result];
        }
    }else{
        if(session.getValue("SESS_TRANSFER")!=null){
            srcTransferData = (SrcTransferData)session.getValue("SESS_TRANSFER");
        }
    }
} catch(Exception e) {
    srcTransferData = new SrcTransferData();
}
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdSearch(){
	document.frmsrctransfer.command.value="<%=Command.SUBMIT%>";
	document.frmsrctransfer.action="transferalltoclient.jsp";
	document.frmsrctransfer.submit();
}
function chgChoice(){
        var valChoice = document.frmsrctransfer.<%=FrmSrcTransferData.fieldNames[FrmSrcTransferData.FRM_FIELD_TYPE_TRANSFER]%>.value;
	document.frmsrctransfer.command.value="<%=Command.LOAD%>";
        document.frmsrctransfer.hidden_choice.value=valChoice;
	document.frmsrctransfer.action="transferalltoclient.jsp";
	document.frmsrctransfer.submit();
}

//-------------- script control line -------------------
	function MM_swapImgRestore() { //v3.0
		var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}

function MM_preloadImages() { //v3.0
		var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}

function MM_findObj(n, d) { //v4.0
		var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
		if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		if(!x && document.getElementById) x=document.getElementById(n); return x;
	}

function MM_swapImage() { //v3.0
		var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
function hideObjectForMarketing(){
}

function hideObjectForWarehouse(){
}

function hideObjectForProduction(){
}

function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListGlobal[SESS_LANGUAGE][0]%>
            &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmsrctransfer" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="hidden_choice" value="<%=choice%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="4"><hr size="1"></td>
                </tr>
                <tr> 
                  <td colspan="4"><%=textListGlobal[SESS_LANGUAGE][5]%>&nbsp;<b><%=pathDataOut%></b>
                </tr>
                <tr> 
                  <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                  <td width="4%">&nbsp;</td>
                  <td width="14%"><%=textListGlobal[SESS_LANGUAGE][3]%></td>
                  <td width="74%">
                  <%
                    String onChangeChoice = " onChange=\"javascript:chgChoice()\"";
                    Vector transfer_value = new Vector(1,1);
                    Vector transfer_key = new Vector(1,1);
                    String selValue = String.valueOf(srcTransferData.getTypeTransfer()==0?srcTransferData.getTypeTransfer():choice);
                    for(int i=0;i<=SrcTransferData.typeNames.length;i++){
                        transfer_value.add(""+i);
                        transfer_key.add(SrcTransferData.typeNames[SESS_LANGUAGE][i]);
                    }
                   %>
                   <%= ControlCombo.draw(frmSrcTransferData.fieldNames[FrmSrcTransferData.FRM_FIELD_TYPE_TRANSFER],"formElemen", null, selValue, transfer_value, transfer_key,onChangeChoice) %>
                  </td>
                  <td width="8%">&nbsp;</td>
                </tr>
                <%if(!selValue.equals(""+SrcTransferData.ALL_DATA+"")){%>
                <tr>
                  <td width="4%">&nbsp;</td>
                  <td width="14%"><%=textListGlobal[SESS_LANGUAGE][4]%></td>
                  <td width="74%">
                    <%
                    Vector table_value = new Vector(1,1);
                  Vector table_key = new Vector(1,1);
                  String txtTable = "Semua ";
                  if(selValue.equals(""+SrcTransferData.TO_CASHIER_OUTLET+"")){
                      txtTable = txtTable +"Data Master ";
                  }else{
                      txtTable = txtTable +"Data Transaksi ";
                  }
                  table_value.add("-1");
                  table_key.add(txtTable);
                  String selectValue = ""+srcTransferData.getIndexTable();
                  
                  /*for(int i=0;i<table_names.length;i++){
                      String rslt = (String)hashData.get(table_names[i]);
                      if(rslt!=null&&rslt.length()>0){
                          table_value.add(""+i);
                          //table_key.add(table_names[i]);
                          table_key.add(rslt);
                      }
                  }*/
                  
                  for(int i=0;i<listDataToTransfer.size();i++){
                      TransMasterdata obj = (TransMasterdata)listDataToTransfer.get(i);
                      table_value.add(""+obj.getIndexDB());
                      table_key.add(obj.getNameDB());
                  }
                  %>
                  <%= ControlCombo.draw(frmSrcTransferData.fieldNames[FrmSrcTransferData.FRM_FIELD_INDEX_TABLE],"formElemen", null, selectValue, table_value, table_key) %> </td>
                  <td width="8%">&nbsp;</td>
                </tr>
                <%}else{%>
                    <input type="hidden" name="<%=frmSrcTransferData.fieldNames[FrmSrcTransferData.FRM_FIELD_INDEX_TABLE]%>" value="<%=-1%>">
                <%}%>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td class="errfont">
                   <%
                   if(iCommand == Command.SUBMIT) { 
                       out.println(message);
                    }
                   %>
                    <table width="34%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="13%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnService.png',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnService.png" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][2]%>"></a></td>
                        <td class="command" nowrap width="87%"><a href="javascript:cmdSearch()"><%=textListGlobal[SESS_LANGUAGE][2]%></a></td>
                      </tr>
                    </table>
                  </td>
                  <td>&nbsp;</td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%@ include file = "../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
