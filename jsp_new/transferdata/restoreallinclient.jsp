<%@ page language="java" %>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.session.transferdata.TransMasterdata,
                 com.dimata.util.Command"%>
				 
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_RESTORE_DATA); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!
public static final String textListGlobal[][] = {
    {"Sistem","Data Exchange","Impor Data","Tipe Data","Nama Data",
    "Proses Impor Data akan melakukan impor data pada direktori","ke sistem"},
    {"System","Data Exchange","Import Data","Data Type","Data Name",
    "Import Data Processing will import data in directory","to system"}
};
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
String dbName = "pos_anugerah";
String[] table_names = {"cash_balance","cash_bill_detail","cash_bill_main",
                        "cash_cashier","cash_credit_card","cash_credit_payment",
                        "cash_credit_payment_info","cash_credit_payment_main",
                        "cash_payment","cash_pending_order","member_point_stock", "cash_return_payment"};
// TransMasterdata.showTable();
String pathDataOut = PstSystemProperty.getValueByName("PATH_DATA_OUT");
String pathDataIn = PstSystemProperty.getValueByName("PATH_DATA_IN");
String pathMysql = PstSystemProperty.getValueByName("PATH_MYSQL_BIN"); 

System.out.println("pathDataOut :"+pathDataOut);
System.out.println("pathDataIn :"+pathDataIn);
System.out.println("pathMysql :"+pathMysql);

TransMasterdata transferMasterdata = new TransMasterdata(table_names,pathMysql,pathDataOut,pathDataIn);
String message = "";

com.dimata.posbo.session.transferdata.TransMasterdata.CMD_DUMP = "mysqldump ";
com.dimata.posbo.session.transferdata.TransMasterdata.CMD_UNDUMP = "mysql ";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_NAME = "pos_yashoda";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_USER = "root";
com.dimata.posbo.session.transferdata.TransMasterdata.DB_PASSWORD = "";

if (iCommand==Command.SUBMIT) {
    int result = transferMasterdata.restoreAllInClient();
    message = transferMasterdata.statusNames[SESS_LANGUAGE][result];
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
    document.frmsrctransfer.action="restoreallinclient.jsp";
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
            &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrctransfer" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr> 
                  <td colspan="4"><hr size="1"></td>
                </tr>
                <tr> 
                  <td colspan="4"><%=textListGlobal[SESS_LANGUAGE][5]%>&nbsp;<b><%=pathDataIn%></b>&nbsp;<%=textListGlobal[SESS_LANGUAGE][6]%></td>
                </tr>
                <tr> 
                  <td colspan="4">&nbsp;</td>
                </tr>
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
                <tr> 
                  <td colspan="4">&nbsp;</td>
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
