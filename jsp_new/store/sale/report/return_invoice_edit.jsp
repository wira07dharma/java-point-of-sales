<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
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
                com.dimata.qdep.form.FRMMessage,
                com.dimata.qdep.form.FRMQueryString,
                com.dimata.gui.jsp.ControlLine,
                com.dimata.util.Command,
                com.dimata.common.entity.location.Location,
                com.dimata.common.entity.location.PstLocation,
				com.dimata.common.entity.contact.*,
                com.dimata.qdep.form.FRMHandler,
                com.dimata.pos.cashier.DefaultSaleModel,
                com.dimata.pos.session.processdata.SessTransactionData,
				com.dimata.pos.entity.billing.*"%>
<%@ page language = "java" %>
<!-- package java -->   

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_RETURN_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No Retur","Tanggal Invoice","Sales Person","Invoice Ref.","Customer ","Lokasi Transaksi","(tidak tercatat)","No.","SKU ","Nama Item","Serial No.","Harga ","Disc","Qty","Total ","Total Trans","Total Disc","Tax","Service","Net Trans","DP","Total Bayar","Kembali","No.","Tipe Pembayaran","Kurs","Jumlah","Tgl Bayar","(tidak tercatat)"},
	{"Invoice No. ","Bill Date","Sales Name","invoice Ref.","Customer Name","Sale Location","(uscpecified)","No.","Item Code","Item Name","Serial No.","Item Price","Disc","Qty","Total Price","Total Trans","Total Disc","Tax","Service","Net Trans","Last Payment(DP)","Total Payment","Return","No.","Payment Type","Rate","Pay Amount","Pay Date","(unspecified)"}
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

%>

<%
int iCommand = FRMQueryString.requestCommand(request);
long oidBillMainOid = FRMQueryString.requestLong(request, "hidden_billmain_id");
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = FRMQueryString.requestInt(request, "recordToGet");

ControlLine ctrLine = new ControlLine();
int iErrCode = FRMMessage.ERR_NONE;
String msgString = "";

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

String sWhereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oidBillMainOid;
Vector records = PstBillDetail.list(start, recordToGet, sWhereClause, "");
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdBack()
{
	document.frminvoice.command.value="<%=Command.BACK%>";
	document.frminvoice.action="reportlistreturn.jsp";
	document.frminvoice.submit();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;Laporan Penjualan > Invoice Retur Detail<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frminvoice" method="post" action=""> 
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="recordToGet" value="<%=recordToGet%>">              
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="<%=oidBillMainOid%>">
              <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">			  			  
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
                                <td align="left" valign="top" ><%=Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy HH:mm:SS")%></td>
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
							  	String strInvRefNumber = "";
								try
								{
									BillMain objReturMain = PstBillMain.fetchExc(billMain.getParentId());
									strInvRefNumber = objReturMain.getInvoiceNumber();
								}
								catch(Exception e)
								{
									System.out.println("Exc when get bill detail : " + e.toString());
								}
		                      %>
                              <td align="left" valign="top" ><%=strInvRefNumber%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][4]%></td>
                              <td align="left" valign="top" > : </td>
							  <%
								String custName = "";
								ContactList contactlist = new ContactList();
								try
								{
									contactlist = PstContactList.fetchExc(billMain.getCustomerId());
									custName = contactlist.getPersonName();
								}
								catch(Exception e)
								{
									System.out.println("Contact not found ...");
								}							  							  
							  %>
                              <td align="left" valign="top" ><%=custName%></td>
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
                              <td width="2%"><b><%=textListMaterialHeader[SESS_LANGUAGE][7]%></b></td>
                              <td width="16%"><b><%=textListMaterialHeader[SESS_LANGUAGE][8]%></b></td>
                              <td width="27%"><b><%=textListMaterialHeader[SESS_LANGUAGE][9]%></b></td>
                              <td width="9%"><b><%=textListMaterialHeader[SESS_LANGUAGE][10]%></b></td>
                              <td width="8%"><b><%=textListMaterialHeader[SESS_LANGUAGE][11]%></b></td>
                              <td width="7%"><b><%=textListMaterialHeader[SESS_LANGUAGE][12]%></b></td>
                              <td width="10%"><b><%=textListMaterialHeader[SESS_LANGUAGE][13]%></b></td>
                              <td width="12%"><b><%=textListMaterialHeader[SESS_LANGUAGE][14]%></b></td>
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
                              <td>
                                <div align="center"><%=i+1%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetail.getSku()%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetail.getItemName()%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetailCode.getStockCode()%></div>
                              </td>
                              <td> 
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getItemPrice())%></div>
                              </td>
                              <td> 
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getDisc())%></div>
                              </td>
                              <td>
                                <div align="right"><%=objBillDetail.getQty()%></div>
                              </td>
                              <td> 
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getTotalPrice())%></div>
                              </td>
                            </tr>
                            <%
                                }
							}
                            %>
                            <tr > 
                              <td colspan="6" rowspan="5">&nbsp;</td>
                              <td align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][15]%></b></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(returBruto)%></td>
                            </tr>
                            <tr > 
                              <td align="right"class="listgensell" ><b><%=textListMaterialHeader[SESS_LANGUAGE][16]%></b></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getDiscount())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][17]%></b></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getTaxValue())%></td>
                            </tr>
                            <tr > 
                              <td height="21" align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][18]%></b></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getServiceValue())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell" ><b><%=textListMaterialHeader[SESS_LANGUAGE][19]%></b></td>
                              <%
							  double netTrans = returBruto - billMain.getDiscount() + billMain.getTaxValue() + billMain.getServiceValue(); 
							  %>
                              <td align="right" class="listgensell"><b><%=FRMHandler.userFormatStringDecimal(netTrans)%></b></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                    <br>
                    <br>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" class="command"> <span class="command"> 
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top"> <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          </table>
						  <%
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
                              ctrLine.setDeleteCaption("");
							ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
                                                        if(billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_DELETED||billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
                                                                    ctrLine.setDeleteCaption("");
                                                                }else{
                                                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
                                                                }
							
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
	
							if (privDelete){
								ctrLine.setConfirmDelCommand(sconDelCom);
                                                                if(billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_DELETED||billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
                                                                    ctrLine.setDeleteCommand("");
                                                                }else{
                                                                    ctrLine.setDeleteCommand(scomDel);
                                                                }
								
								ctrLine.setEditCommand(scancel);
							}else{ 
								ctrLine.setConfirmDelCaption("");
								ctrLine.setDeleteCaption("");
								ctrLine.setEditCaption("");
							}

                              ctrLine.setDeleteCaption("");
							if(privAdd == false  && privUpdate == false){
								ctrLine.setSaveCaption("");
							}
	
							if (privAdd == false){
								ctrLine.setAddCaption("");
							}
							%>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
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
<!-- #EndTemplate --></html>
