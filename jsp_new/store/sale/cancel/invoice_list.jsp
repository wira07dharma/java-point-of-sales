<%@ page import="com.dimata.pos.entity.billing.Billdetail,
                com.dimata.pos.entity.billing.PstBillMain,
                com.dimata.pos.entity.billing.BillMain,
                 com.dimata.pos.entity.search.SrcInvoice,
                com.dimata.pos.form.search.FrmSrcInvoice,
                com.dimata.pos.session.billing.SessBilling,
                com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command"%>
<%@ page language = "java" %>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<%//@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"NO","NOMOR INVOICE","TANGGAL INVOICE","NAMA CUSTOMER","(tidak tercatat)","STATUS","CLOSED","OPEN","DELETED"},
	{"NO","INVOICE NUMBER","INVOICE DATE","CUSTOMER NAME","(unspecified)","STATUS","CLOSED","OPEN","DELETED"}
};

public static final String textListTitleHeader[][] = 
{
	{"DAFTAR INVOICE "," Invoice ","Tidak ada data transaksi ..","Transaksi Invoice","Semua","s/d"},
	{"INVOICE LIST","Invoice ","No transaction data available ..","Invoice Transaction ","TYPE","All","to"}
};

public Vector drawList(int language,Vector objectClass,SrcInvoice srcParameter,int start)
{
    String result = "";
    Vector listAll = new Vector(1,1);

	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("90%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.dataFormat(textListMaterialHeader[language][0],"5%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][1]+"","12%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][2]+"","17%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][3]+"","45%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][5]+"","25%","0","0","center","bottom");
		ctrlist.setLinkRow(1);

		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++)
		{
			Vector rowx = new Vector();
			SrcInvoice srcInvoice = (SrcInvoice)objectClass.get(i);
			String linkParameter = ""+srcInvoice.getInvoiceId(); 
                        
			rowx.add("<div align=\"center\">"+(i+1+start)+"</div>");
			rowx.add("<div align=\"left\">"+srcInvoice.getInvoiceNumber()+"</div>");
			rowx.add("<div align=\"center\">"+Formater.formatDate(srcInvoice.getInvoiceDate(),"dd-MMM-yyyy HH:mm:SS")+"</div>");
			rowx.add("<div align=\"left\">"+srcInvoice.getMemberName()+"</div>");
			switch(srcInvoice.getTransStatus())
			{
				case PstBillMain.TRANS_STATUS_CLOSE :
					rowx.add("<div align=\"left\">"+textListMaterialHeader[language][6]+"</div>");
					break;
				case PstBillMain.TRANS_STATUS_OPEN :
					rowx.add("<div align=\"left\">"+textListMaterialHeader[language][7]+"</div>");
					break;
				case PstBillMain.TRANS_STATUS_DELETED :
					rowx.add("<div align=\"left\">"+textListMaterialHeader[language][8]+"</div>");
					break;
				default :
					rowx.add("<div align=\"left\">"+textListMaterialHeader[language][4]+"</div>");
					break;
			}
                        
			lstData.add(rowx);
            lstLinkData.add(linkParameter);
	    }

        result = ctrlist.draw(); 
	}
	else
	{
	    result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
	}
	listAll.add(result);    
	return listAll;
}
%>

<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidGroup = FRMQueryString.requestLong(request, "hidden_billmain_id");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 15;
int vectSize = 0;
String whereClause = "";
int sortCommand = 9999; 

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcInvoice srcInvoice = new SrcInvoice();

SessBilling sessBilling = new SessBilling();
FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice(request, srcInvoice);
frmSrcInvoice.setDecimalSeparator(",");
frmSrcInvoice.setDigitSeparator(".");

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==sortCommand)
{
 try
 {
	srcInvoice= (SrcInvoice)session.getValue(SessBilling.SRC_INVOICE_FOR_CANCEL); 
	if (srcInvoice== null) 
	{
		srcInvoice= new SrcInvoice();
	}
 }
 catch(Exception e)
 {
	srcInvoice = new SrcInvoice();
 }         
}
else
{
	 frmSrcInvoice.requestEntityObject(srcInvoice);         
}

   
vectSize = sessBilling.getCountListInvoice(srcInvoice, PstBillMain.TYPE_INVOICE);
Vector records = new Vector();
switch(iCommand)
{
   case Command.FIRST:
       start = 0;
        break;   
   case Command.PREV:
       start = start - recordToGet;
        break;    
   case Command.NEXT:
       start = start + recordToGet;
        break;
   case Command.LAST:
       start = vectSize-recordToGet;
        break;
   default:
       break;
}

records = sessBilling.getListInvoice(srcInvoice,start,recordToGet,PstBillMain.TYPE_INVOICE,SessBilling.LOG_MODE_CONSOLE);  
Vector list = drawList(SESS_LANGUAGE,records,srcInvoice,start);
String str = "";
str = (String)list.get(0); 

session.putValue(SessBilling.SRC_INVOICE_FOR_CANCEL, srcInvoice); 
session.putValue("start", ""+start); 
session.putValue("recordToGet", ""+recordToGet); 
%>
<!-- End of Jsp Block -->

<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdEdit(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.EDIT%>";
	document.frminvoice.action="invoice_edit.jsp";	
	document.frminvoice.submit();
}

function cmdListFirst()
{
	document.frminvoice.command.value="<%=Command.FIRST%>";
	document.frminvoice.prev_command.value="<%=Command.FIRST%>";
	document.frminvoice.action="invoice_list.jsp";	
	document.frminvoice.submit();
}

function cmdListPrev()
{
	document.frminvoice.command.value="<%=Command.PREV%>";
	document.frminvoice.prev_command.value="<%=Command.PREV%>";
	document.frminvoice.action="invoice_list.jsp";	
	document.frminvoice.submit();
}

function cmdListNext()
{
	document.frminvoice.command.value="<%=Command.NEXT%>";
	document.frminvoice.prev_command.value="<%=Command.NEXT%>";
	document.frminvoice.action="invoice_list.jsp";	
	document.frminvoice.submit();
}

function cmdListLast()
{
	document.frminvoice.command.value="<%=Command.LAST%>";
	document.frminvoice.prev_command.value="<%=Command.LAST%>";	
	document.frminvoice.action="invoice_list.jsp";
	document.frminvoice.submit();
}

function cmdBack()
{
	document.frminvoice.command.value="<%=Command.BACK%>";
	history.back();
	document.frminvoice.action="src_invoice.jsp";
	document.frminvoice.submit();
}

function printForm()
{
    window.open("<%=approot%>/servlet/com.dimata.material.report.RekapPenjualanPerShiftPDFByDoc");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" --> 
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            <%=textListTitleHeader[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frminvoice" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">          
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="0">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="3"> <hr size="1"> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center"><b><%=textListTitleHeader[SESS_LANGUAGE][0]%> </b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center">
				  <b> Periode
                  <%
				  out.println(Formater.formatDate(srcInvoice.getInvoiceDate(), "dd-MM-yyyy"));
				  %> <%=textListTitleHeader[SESS_LANGUAGE][5]%>  <%
				  out.println(Formater.formatDate(srcInvoice.getInvoiceDateTo(), "dd-MM-yyyy"));
				  %>
				  </b> 
				  </td>
                </tr>                
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="3" class="command">
                                <b><%%> </b> </td>
                            </tr>
                            
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%=str%></td>
                </tr>
                <tr align="left" valign="top"> 
                        <td height="8" align="left" colspan="3" class="command"> 
                          <span class="command"> 
                          <% 
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
										(iCommand == Command.NEXT || iCommand == Command.LAST))
											cmd =iCommand; 
								   else{
									  if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
									  else 
									  	cmd =prevCommand; 
								   } 
							    %>
                          <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                          <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Sale Report",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="95%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][1],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="39%"> <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <!--tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="
                                <%//=approot%>
                                /images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" >
                                <%//=textListTitleHeader[SESS_LANGUAGE][6]%>
                                  </a></td>
                            </tr-->
                            
                          </table></td>
                      </tr>
                    </table></td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --> 
</html>
