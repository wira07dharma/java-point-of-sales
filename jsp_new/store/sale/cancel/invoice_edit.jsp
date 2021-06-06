<%@ page import="com.dimata.pos.entity.billing.Billdetail,
                com.dimata.pos.entity.billing.BillDetailCode,
                com.dimata.pos.entity.billing.PstBillMain,
                com.dimata.pos.entity.billing.BillMain,
                com.dimata.pos.entity.payment.CashPayments,
                com.dimata.pos.entity.payment.CashReturn,
                com.dimata.pos.entity.payment.PstCashPayment,
                com.dimata.pos.form.billing.FrmBillMain,
                com.dimata.pos.form.billing.CtrlBillMain,
                com.dimata.pos.entity.search.SrcInvoice,
                com.dimata.pos.form.search.FrmSrcInvoice,
                com.dimata.pos.session.billing.SessBilling,
                com.dimata.posbo.report.sale.SaleReportDocument,
                com.dimata.posbo.entity.masterdata.MemberReg,
                java.util.*,
                com.dimata.posbo.entity.masterdata.*,
                com.dimata.qdep.entity.I_DocStatus,				
                com.dimata.qdep.form.FRMMessage,
                com.dimata.qdep.form.FRMQueryString,
                com.dimata.gui.jsp.ControlLine,
                com.dimata.util.Command,
                com.dimata.common.entity.location.Location,
                com.dimata.common.entity.location.PstLocation,
				com.dimata.common.entity.contact.*,
                com.dimata.qdep.form.FRMHandler,
				com.dimata.pos.entity.billing.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.common.entity.payment.CurrencyType"%>
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
	{"No. Invoice","Tanggal Invoice","Sales Person","Customer ","Kode Customer ","Lokasi Transaksi","(tidak tercatat)","No.","SKU ","Nama Item","Serial No.","Harga ","Disc","Qty","Total ","Total Trans","Total Disc","Tax","Service","Net Trans","DP Deduction","Total Bayar","Kembali","No.","Tipe Pembayaran","Kurs","Jumlah","Tgl Bayar","(tidak tercatat)"},
	{"Invoice No. ","Bill Date","Sales Name","Customer Name","Customer Code","Sale Location","(uscpecified)","No.","Item Code","Item Name","Serial No.","Item Price","Disc","Qty","Total Price","Total Trans","Total Disc","Tax","Service","Net Trans","DP Deduction","Total Payment","Return","No.","Payment Type","Rate","Pay Amount","Pay Date","(unspecified)"}
};
public static final String textListTitle[][] = 
{
	{"Invoice ","NOMOR INVOICE","TANGAAL INVOICE","NAMA CUSTOMER","(tidak tercatat)"},
	{"Invoice ","INVOICE NUMBER","INVOICE DATE","CUSTOMER NAME","(unspecified)"}
};

public static final String textListTitleHeader[][] = 
{
	{"DETAIL INVOICE ","Invoice ","Tidak ada data transaksi ..","Transaksi Invoice","Semua"},
	{"INVOICE DETAIL","Invoice ","No transaction data available ..","Invoice Transaction ","TYPE","All"}
};

public String drawListPayment(Vector vListPayment) 
{
	String sResult = "";
	if(vListPayment!=null && vListPayment.size()>0)
	{
		// get list currency type
		Vector vCurrType = PstCurrencyType.list(0,0,"","");
		Hashtable hashCurrType = new Hashtable();
		if(vCurrType!=null && vCurrType.size()>0){
			int iCurrTypeCount = vCurrType.size();
			for(int i=0; i<iCurrTypeCount; i++){
				CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
				hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
			}
		}
		
		
		String sHeader = generateHeader();
		String strContent = "";
		
		double dTotalPayment = 0;
		int iListPaymentCount = vListPayment.size();
		for(int i=0; i<iListPaymentCount; i++)
		{
			Vector vObj = (Vector) vListPayment.get(i);
			if(vObj!=null && vObj.size()>0)
			{
				double dPaymentPerType = 0;
				String strPaymentTypeName = "";
				for(int j=0; j<vObj.size(); j++)
				{
					CashPayments objCashPayment = (CashPayments) vObj.get(j);
					
					String strPaymentType = PstCashPayment.paymentType[objCashPayment.getPaymentType()];
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()/objCashPayment.getRate() ));
					String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount());					
					dPaymentPerType = dPaymentPerType + objCashPayment.getAmount();
					
					if(j==0)
					{
						strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
						strPaymentTypeName = strPaymentType.toUpperCase();
					}					
					
					strContent = strContent + generateContent(strNumber, strCurrency, strAmount, strRate, strTotal);
				}
				
				strContent = strContent + generateItemFooter("TOTAL "+strPaymentTypeName, FRMHandler.userFormatStringDecimal(dPaymentPerType));
				strContent = strContent + generateBlankSpace();
				dTotalPayment = dTotalPayment + dPaymentPerType;
			}			
		}
		
		String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));		
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
	}
	
	return sResult;
}

public String generateHeader()
{
	return "<tr>" + 
			  "<td width=\"4%\" class=\"listgentitle\">NO</td>" + 
			  "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
			  "<td width=\"26%\" class=\"listgentitle\">TOTAL</td>" +
			"</tr>";
}

public String generateItemHeader(String strItemHeader)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"5\"><b>"+strItemHeader+"</b></td>" +
			"</tr>";
}

public String generateContent(String strNum, String strCurrency, String strAmount, String strRate, String strTotal)
{
	return "<tr valign=\"top\">" + 
			  "<td width=\"4%\" class=\"listgensell\" align=\"center\">"+strNum+"</td>" + 
			  "<td width=\"24%\" class=\"listgensell\">"+strCurrency+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strAmount+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strRate+"</td>" +
			  "<td width=\"26%\" class=\"listgensell\" align=\"right\">"+strTotal+"</td>" +
			"</tr>";			
}

public String generateItemFooter(String strItemFooter, String strValue)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\"><b>"+strItemFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateFooter(String strFooter, String strValue)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\" ><b>"+strFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateBlankSpace()
{
	return "<tr valign=\"top\">"  +
			  "<td class=\"listgensell\" colspan=\"5\">&nbsp;</td>" +
			"</tr>";
}
%>


<%
int iCommand = FRMQueryString.requestCommand(request);
long oidBillMainOid = FRMQueryString.requestLong(request, "hidden_billmain_id");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = FRMQueryString.requestInt(request, "recordToGet");

ControlLine ctrLine = new ControlLine();
int iErrCode = FRMMessage.ERR_NONE;
String msgString = "";

// fetch BillMain object depend on oidBillMainOid
BillMain billMain = new BillMain();
AppUser ap = new AppUser();
try {
	billMain = PstBillMain.fetchExc(oidBillMainOid);
   ap = PstAppUser.fetch(billMain.getAppUserId());
}
catch(Exception e)
{
	System.out.println("Exc when get bill detail : " + e.toString());
}

// fetch BillDetail object depend on oidBillMainOid
    String sWhereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oidBillMainOid;
    Vector records = PstBillDetail.list(0, 0, sWhereClause, "");

/**
* handle proses delete BillMain
*/
if (iCommand==Command.DELETE)
{    
    try{
        /*if(records.size()>0){
            for(int k=0;k<records.size();k++){
                Billdetail objBillDetail = (Billdetail) records.get(k);
                //PstBillDetail.deleteExc(objBillDetail.getOID());
            }
        }*/
        billMain.setTransactionStatus(PstBillMain.TRANS_STATUS_DELETED);
	billMain.setBillStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
	PstBillMain.updateExc(billMain);
        //oidBillMainOid = PstBillMain.deleteExc(oidBillMainOid);
    }catch(Exception e){
        System.out.println("Err when delete Bill Main : " + e.toString());
    }
}

boolean privDelete = true;
boolean privAdd = true;
boolean privUpdate = true;

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

function cmdAsk(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.ASK%>";
	document.frminvoice.action="invoice_edit.jsp";
	document.frminvoice.submit();
}

function cmdDelete(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.DELETE%>";
	document.frminvoice.action="invoice_edit.jsp";
	document.frminvoice.submit();
}

function cmdAdd(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.ADD%>";
	document.frminvoice.action="invoice_edit.jsp";
	document.frminvoice.submit();
}

function cmdSave(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.SAVE%>";
	document.frminvoice.action="invoice_edit.jsp";
	document.frminvoice.submit();
}

function cmdConfirmDelete(oid)
{
	document.frminvoice.hidden_billmain_id.value=oid;
	document.frminvoice.command.value="<%=Command.DELETE%>";
	document.frminvoice.action="invoice_edit.jsp";
	document.frminvoice.submit();
}

function cmdBack()
{
	document.frminvoice.start.value="<%=start%>";
	document.frminvoice.command.value="<%=Command.BACK%>";
	document.frminvoice.action="invoice_list.jsp";
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
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="recordToGet" value="<%=recordToGet%>">              
              <input type="hidden" name="hidden_billmain_id" value="<%=oidBillMainOid%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="127" valign="top"> 
                    <table width="100%" border="0" cellspacing="1">
                      <tr> 
                        <td align="left" valign="top">
                           <table border="0" cellspacing="1">
                            <tr>
                           <td>
                            <table width="100%" border='0'>
                                  <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][0]%></td>
                              <td align="left" valign="top" > : </td>
                                <td align="left" valign="top" > 
                                <div align="left"><%=billMain.getInvoiceNumber()%></div></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                                    <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" ><%=Formater.formatDate(billMain.getBillDate(),"dd-MMM-yyyy : HH-mm-SS")%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][2]%></td>
                                    <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" ><%=ap.getFullName() %></td>
                            </tr>
                            
                            </table>
                           </td>
                           <td width='10%'>&nbsp;
                           </td>
                           <td>
                            <table width="100%" border='0'>
                                  <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][3]%></td>
                              <td align="left" valign="top" > : </td>
							  <%
								String custName = "";
								String custCode = "";
								ContactList contactlist = new ContactList();
								try
								{
									contactlist = PstContactList.fetchExc(billMain.getCustomerId());
									custName = contactlist.getPersonName();
									custCode = contactlist.getContactCode();
								}
								catch(Exception e)
								{
									System.out.println("Contact not found ...");
								}							  							  
							  %>
                              <td align="left" valign="top" ><%=custName%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][4]%></td>
                              <td align="left" valign="top" > : </td>
                              <td align="left" valign="top" ><%=custCode%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][5]%></td>
                              <td align="left" valign="top" > : </td>
                              <%
                                String locationname = "";
                                Location location = new Location();
                                try
								{
                                   location = PstLocation.fetchExc(billMain.getLocationId());
                                   locationname = location.getName(); 
                                }
								catch(Exception e)
								{
                                   locationname = textListMaterialHeader[SESS_LANGUAGE][6];
                                }
                              %>
                              <td align="left" valign="top" ><%=locationname %></td>
                            </tr>
                            </table>
                           </td>
                           </tr>
                            
                          </table></td>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td colspan="2" align="left" valign="top">
                            <table border="0" cellspacing="1"  width='100%'>
                            <tr class="listgentitle"> 
                              <td width="2%"><%=textListMaterialHeader[SESS_LANGUAGE][7]%></td>
                              <td width="16%"><%=textListMaterialHeader[SESS_LANGUAGE][8]%></td>
                              <td width="27%"><%=textListMaterialHeader[SESS_LANGUAGE][9]%></td>
                              <td width="9%"><%=textListMaterialHeader[SESS_LANGUAGE][10]%></td>
                              <td width="8%"><%=textListMaterialHeader[SESS_LANGUAGE][11]%></td>
                              <td width="7%"><%=textListMaterialHeader[SESS_LANGUAGE][12]%></td>
                              <td width="10%"><%=textListMaterialHeader[SESS_LANGUAGE][13]%></td>
                              <td width="12%"><%=textListMaterialHeader[SESS_LANGUAGE][14]%></td>
                            </tr>
                            <%
							double returBruto = 0;
							if(records!=null && records.size()>0)
							{
								int maxDetail = records.size();								
								for(int i=0; i<maxDetail; i++)
								{
									Billdetail objBillDetail = (Billdetail) records.get(i);										
									BillDetailCode objBillDetailCode = PstBillDetailCode.getBillDetailCode(objBillDetail.getOID());
									returBruto = returBruto + objBillDetail.getTotalPrice();
                            %>
                            <tr class="listgensell"> 
                              <td><div align="center"><%=i+1%></div></td>
                              <td><div align="left"><%=objBillDetail.getSku()%></div></td>
                              <td><div align="left"><%=objBillDetail.getItemName()%></div></td>
                              <td><div align="left"><%=objBillDetailCode.getStockCode()%></div></td>
                              <td><div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getItemPrice())%></div></td>
                              <td><div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getDisc())%></div></td>
                              <td><div align="right"><%=objBillDetail.getQty()%></div></td>
                              <td><div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getTotalPrice())%></div></td>
                            </tr>
                            <%
                                }
							}
                            %>
                            <tr > 
                              <td colspan="6" rowspan="8">&nbsp;</td>
                              <td align="right" class="listgensell"><%=textListMaterialHeader[SESS_LANGUAGE][15]%></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(returBruto)%></td>
                            </tr>
                            <tr > 
                              <td align="right"class="listgensell" ><%=textListMaterialHeader[SESS_LANGUAGE][16]%></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getDiscount())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell"><%=textListMaterialHeader[SESS_LANGUAGE][17]%></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getTaxValue())%></td>
                            </tr>
                            <tr > 
                              <td height="21" align="right" class="listgensell"><%=textListMaterialHeader[SESS_LANGUAGE][18]%></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getServiceValue())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell" ><%=textListMaterialHeader[SESS_LANGUAGE][19]%></td>
                              <%
							  double netTrans = returBruto - billMain.getDiscount() + billMain.getTaxValue() + billMain.getServiceValue();
							  %>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(netTrans)%></td>
                            </tr>
                            <tr >
                              <td align="right" class="listgensell"><%=textListMaterialHeader[SESS_LANGUAGE][20]%></td>
							  <%
                                                          
							  double lastPayment = PstPendingOrder.getDpValue(oidBillMainOid);
							  %>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(lastPayment)%></td>
                            </tr>
                          </table>
						 </td>
                      </tr>
					  
						<%
						//if(oidBillMainOid!=0 && iSaleReportType == PstCashPayment.CASH)
						if(records!=null && records.size()>0 && oidBillMainOid!=0)
						{
							PstCashPayment objPstCashPayment = new PstCashPayment();
							Vector vResult = objPstCashPayment.getListPayment(oidBillMainOid);
							String strTemp = drawListPayment(vResult);
						%>				
						<tr align="left" valign="top"> 
						  <td height="22" valign="middle" colspan="2">&nbsp;</td>
						</tr>															
						<tr align="left" valign="top"> 
						  <td height="22" valign="middle" colspan="2"><b>Daftar Pembayaran</b></td>
						</tr>							
						<tr align="left" valign="top"> 
						  <td height="22" valign="middle" colspan="2"><%=strTemp%></td>
						</tr>							
						<%
						}
						%>
					  
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" class="command"> <span class="command"> 
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top"> 
					<%						  
					String strMsgString = "";
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
					ctrLine.setTableWidth("100%");
					ctrLine.setSaveImageAlt("");
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
					
					String scomDel = "javascript:cmdAsk('"+oidBillMainOid+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidBillMainOid+"')";
					String scancel = "javascript:cmdEdit('"+oidBillMainOid+"')";
					
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");
					
					// set command caption
					ctrLine.setAddCaption("");
					ctrLine.setSaveCaption("");
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

					if (privDelete) //
					{
						ctrLine.setConfirmDelCommand(sconDelCom);
						if( billMain.getBillStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
						{
							if( billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_DELETED)
							{
								ctrLine.setDeleteCaption("");
								ctrLine.setDeleteCommand("");
								strMsgString = "Invoice ini sudah Canceled ...";							
							}
							else
							{
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));									
								ctrLine.setDeleteCommand(scomDel);
							}
						}
						else
						{
							ctrLine.setDeleteCaption("");								
							ctrLine.setDeleteCommand("");
							strMsgString = "Invoice ini sudah Posted ...";									
						}								
						ctrLine.setEditCommand(scancel);
					}
					else
					{ 
						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					if(privAdd == false  && privUpdate == false)
					{
						ctrLine.setSaveCaption("");
					}

					if (privAdd == false)
					{
						ctrLine.setAddCaption("");
					}
					%>				  
					<table width="100%">
						<tr>
							<td><div class="msgquestion"><%=strMsgString%></div></td>
						</tr>
					</table>				  
					
				  	<table width="100%" border="0">
                      <tr> 
                        <td width="61%"><%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
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
