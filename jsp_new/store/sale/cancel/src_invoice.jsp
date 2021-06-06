<%@ page import="com.dimata.posbo.entity.search.SrcReportSale,
                com.dimata.pos.entity.billing.PstBillMain,
                com.dimata.pos.entity.billing.BillMain,
                com.dimata.posbo.entity.search.SrcSaleReport,
                com.dimata.posbo.form.search.FrmSrcSaleReport,
                com.dimata.posbo.report.sale.SaleReportDocument,
                com.dimata.posbo.report.sale.SaleReportItem,
                 com.dimata.posbo.form.search.FrmSrcReportSale,
                 com.dimata.posbo.session.warehouse.SessReportSale,
                 com.dimata.posbo.session.warehouse.SessReportSaleNew,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.ContactClass,
                 com.dimata.pos.entity.billing.BillMain,
                 com.dimata.pos.entity.billing.PstBillMain,
                 com.dimata.pos.form.billing.FrmBillMain,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.pos.session.billing.SessBilling,
                 com.dimata.pos.entity.search.SrcInvoice,
                 com.dimata.pos.form.search.FrmSrcInvoice,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.gui.jsp.ControlCombo"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<%//@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */

public static int FLD_CUSTOMER_NAME =0;
public static int FLD_INVOICE_DATE =1;
public static int FLD_INVOICE_NUMBER =2;
public static int FLD_MEMBER_NAME =3;
public static int FLD_GROUPED_BYT =4;

public static final String textListHeader[][] = 
{
	{"Nama Customer","Tanggal Invoice","Nomor Invoice","Nama Member","Group","Status Transaksi","Open","Close","Deleted"},
	{"Customer Name","Invoice Date","Invoice Number","Member name","Grouped by","Transaction Status","Open","Close","Deleted"}	
};

public static final String textListTitleHeader[][] = 
{
	{"Pencarian Invoice ","Pencarian Invoice","Pencarian","Cari Invoice","--Semua"},
	{"Invoice Search","Invoice Search ","Searching","Search Invoice","--All"}	
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else
		{
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else
	{
		result = textListHeader[language][index];
	} 
	return result;
}

%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();

SrcInvoice srcInvoice = new SrcInvoice();
FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice();

SessBilling sessBilling= new SessBilling();
try{
	srcInvoice  = (SrcInvoice )session.getValue(SessBilling.SRC_INVOICE_FOR_CANCEL);
        
}catch(Exception e){
	srcInvoice  = new SrcInvoice();
}

if(srcInvoice ==null){
	srcInvoice = new SrcInvoice();
        
}

try{
	session.removeValue(SessBilling.SRC_INVOICE_FOR_CANCEL);
}catch(Exception e){}
%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
	document.frmsrccancelinvoice.command.value="<%=Command.LIST%>";
	
	document.frmsrccancelinvoice.action="invoice_list.jsp";
	document.frmsrccancelinvoice.submit();
}
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListTitleHeader[SESS_LANGUAGE][0]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][1]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrccancelinvoice" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="global_group" value="0">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <hr size="1">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="3" cellpadding="1">
                        
                      <tr>
                        <td height="21" valign="center" width="15%" align="left"><%=getJspTitle(FLD_INVOICE_NUMBER,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="center" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; 
                        <input type=text name='<%=FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>' value='<%=srcInvoice.getInvoiceNumber()%>'>
						  
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="center" width="9%" align="left"><%=getJspTitle(FLD_MEMBER_NAME,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="center" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; 
                        <input type=text name='<%=FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>' value='<%=srcInvoice.getMemberName()%>'>
						  
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="center" width="9%"><%=getJspTitle(FLD_INVOICE_DATE,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="center">:</td>
                        <td height="21">&nbsp; <%=ControlDate.drawDate(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE],srcInvoice.getInvoiceDate(),"formElemen",4,-8)%> &nbsp;s/d &nbsp;
                        <%=ControlDate.drawDate(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE_TO],srcInvoice.getInvoiceDateTo(),"formElemen",4,-8)%></td>
                      </tr>

                      <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; <% 
							Vector val_transStatus = new Vector(1,1);
							Vector key_transStatus = new Vector(1,1);                                                         
							val_transStatus.add(""+PstBillMain.TRANS_STATUS_CLOSE+"");
                                                            key_transStatus.add(getJspTitle(7,SESS_LANGUAGE,"",true));                                                        
                                                        val_transStatus.add(""+PstBillMain.TRANS_STATUS_OPEN+"");
                                                            key_transStatus.add(getJspTitle(6,SESS_LANGUAGE,"",true));
                                                        
                                                        val_transStatus.add(""+PstBillMain.TRANS_STATUS_DELETED+"");
                                                            key_transStatus.add(getJspTitle(8,SESS_LANGUAGE,"",true));
                                                            
							String select_doc = "--Semua"; 
						  %> <%=ControlCombo.draw(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_STATUS], null, ""+srcInvoice.getTransStatus(), val_transStatus, key_transStatus, "", "formElemen")%></td>
                      </tr>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="7%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Laporan"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdSearch()"><%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <SCRIPT language=JavaScript>
<!--
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
//-->
</SCRIPT>
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
