<%@ page language = "java" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessStockCard,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.StockCardReport,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
				 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.pos.entity.billing.PstBillMain,
                 com.dimata.posbo.form.search.FrmSrcStockCard,
                 com.dimata.posbo.entity.search.SrcStockCard,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] ={
	{"Tanggal","Keterangan","Nomor Dokumen","Stok Masuk","Stok Keluar","Sisa"},
	{"Date","Remark","Number","Stock In","Stock Out","Residue"}
};

public static final String textListTitleHeader[][] ={
	{"Laporan Kartu Stok","Lokasi","Nama Barang","Periode"," s/d ","Tidak Ada Data...","Kartu Stok"},
	{"Stock Card Report","Location","Goods Name","Period"," to ","No Data Available...","Stock Card"}
};

public String drawList(int language, int satrt, Vector listAll){
	String result = "";
	if(listAll != null && listAll.size( )> 0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");                   
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"11%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"30%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"13%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"13%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"13%");

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		double qtyawal = 0;
		Vector rowx = new Vector();
        StockCardReport stockCrp = (StockCardReport)listAll.get(0);
		
		rowx.add(Formater.formatDate(stockCrp.getDate(),"dd/MM/yyyy"));
        rowx.add(stockCrp.getKeterangan());
        rowx.add(stockCrp.getDocCode());
        rowx.add("");
		rowx.add("");
        if((stockCrp.getDate().getYear()+1900) == 2005)
            stockCrp.setQty(0);
        qtyawal = stockCrp.getQty();
        rowx.add("<div align=\"right\">"+stockCrp.getQty()+"</div>");
		lstData.add(rowx);
		
		Vector objectClass = (Vector)listAll.get(1);
		for(int i = 0; i < objectClass.size(); i++){
            StockCardReport stockCardReport = (StockCardReport)objectClass.get(i);
			rowx = new Vector();
			
			rowx.add(Formater.formatDate(stockCardReport.getDate(),"dd/MM/yyyy"));
            rowx.add(stockCardReport.getKeterangan());
            rowx.add(stockCardReport.getDocCode());
			
			switch(stockCardReport.getDocType()){
                case I_DocType.MAT_DOC_TYPE_LMRR:
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                    rowx.add("");
                    qtyawal = qtyawal + stockCardReport.getQty();
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                    break;
					
                case I_DocType.MAT_DOC_TYPE_ROMR:
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                    
                    qtyawal = qtyawal - stockCardReport.getQty();
                    if(qtyawal<0){
                        rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(qtyawal)+")</div>");
                    }else{
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                    }
                    break;
					
                case I_DocType.MAT_DOC_TYPE_DF:
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                    qtyawal = qtyawal - stockCardReport.getQty();
                    if(qtyawal<0){
                        rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(qtyawal* -1)+")</div>");
                    }else{
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                    }
                    break;
					
                case I_DocType.MAT_DOC_TYPE_OPN:
                    qtyawal = (stockCardReport.getQty() - qtyawal);
                    if(qtyawal<0){
                        rowx.add("");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal * -1)+"</div>");
                    }else{
                        if(qtyawal==0)
                            rowx.add("");
                        else
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                        rowx.add("");
                    }
                    qtyawal = stockCardReport.getQty();
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                    break;
					
                case I_DocType.MAT_DOC_TYPE_SALE:
                    switch(stockCardReport.getTransaction_type()){
                        case PstBillMain.TYPE_INVOICE:
                            rowx.add("");
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                            qtyawal = qtyawal - stockCardReport.getQty();
                            break;
							
                        case PstBillMain.TYPE_RETUR:
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                            rowx.add("");
                            rowx.setElementAt("Terima Ret.Konsumen",3);
                            qtyawal = qtyawal + stockCardReport.getQty();
                            break;
							
                        case PstBillMain.TYPE_GIFT:
                            break;
							
                        case PstBillMain.TYPE_COST:
                            break;
							
                        case PstBillMain.TYPE_COMPLIMENT:
                            break;
                    }

                    if(qtyawal<0){
                        rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(qtyawal * -1)+")</div>");
                    }else{
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                    }
                    break;

                case I_DocType.MAT_DOC_TYPE_COS:
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockCardReport.getQty())+"</div>");
                    qtyawal = qtyawal - stockCardReport.getQty();
                    if(qtyawal<0){
                        rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(qtyawal)+")</div>");
                    }else{
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyawal)+"</div>");
                    }
                    break;
            }
            lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][5]+"</div>";
	}
	return result;
}

%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int index = FRMQueryString.requestInt(request,"type");

boolean isCategory = false;
boolean isSubCategory = false;
boolean isDispatchTo = false;
boolean isSupplier = false;

ControlLine ctrLine = new ControlLine();
SrcStockCard srcStockCard = new SrcStockCard();
SessStockCard sessStockCard = new SessStockCard();
FrmSrcStockCard frmSrcStockCard = new FrmSrcStockCard(request, srcStockCard);
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	 try{
		srcStockCard = (SrcStockCard)session.getValue(SessStockCard.SESS_STOCK_CARD);
		if (srcStockCard == null) srcStockCard = new SrcStockCard();
	 }catch(Exception e){
		srcStockCard = new SrcStockCard();
	 }
}else{
	 frmSrcStockCard.requestEntityObject(srcStockCard);
	 session.putValue(SessStockCard.SESS_STOCK_CARD, srcStockCard);
}

/**
* get vectSize, start and data to be display in this page
*/
//srcStockCard.setMaterialId(504404263670541348L);
Vector records = SessStockCard.createHistoryStockCard(srcStockCard);
Material mat = new Material();
try{
	mat = PstMaterial.fetchExc(srcStockCard.getMaterialId());
}catch(Exception e){}
//int vectSize = records.size();
    //System.out.println("vectSize : "+vectSize);
//if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
	//start = ctrlStockCard.actionList(iCommand,start,vectSize,recordToGet);
//}

/**
	set value vector for stock report print
	biar tidak load data lagi
*/

%>
<!-- End of Jsp Block -->

<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdEdit(oid)
{
}

function cmdListFirst()
{
	document.frm_reportcosting.command.value="<%=Command.FIRST%>";
	document.frm_reportcosting.action="reportcosting_list.jsp";
	document.frm_reportcosting.submit();
}

function cmdListPrev()
{
	document.frm_reportcosting.command.value="<%=Command.PREV%>";
	document.frm_reportcosting.action="reportdispatch_list.jsp";
	document.frm_reportcosting.submit();
}

function cmdListNext()
{
	document.frm_reportcosting.command.value="<%=Command.NEXT%>";
	document.frm_reportcosting.action="reportdispatch_list.jsp";
	document.frm_reportcosting.submit();
}

function cmdListLast()
{
	document.frm_reportcosting.command.value="<%=Command.LAST%>";
	document.frm_reportcosting.action="reportdispatch_list.jsp";
	document.frm_reportcosting.submit();
}

function cmdBack()
{
	document.frm_reportcosting.command.value="<%=Command.BACK%>";
	document.frm_reportcosting.action="src_stockcard.jsp";
	document.frm_reportcosting.submit();
}

function printForm(){
	window.open("reportcosting_form_print.jsp","dispatchreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader">
            &nbsp;<%=textListTitleHeader[SESS_LANGUAGE][0]%></td>
        </tr>
        <tr>
          <td>
		    <form name="frm_reportcosting" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="type" value="<%=index%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" class="command"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
					<%
                        if (srcStockCard.getLocationId() != 0){
							try{
								Location loc1 = PstLocation.fetchExc(srcStockCard.getLocationId());
								isDispatchTo = true;
					%>
                    <tr>
                      <td width="15%"><b><%=textListTitleHeader[SESS_LANGUAGE][1]%></b></td>
                        <td width="77%"><b>: </b><%=loc1.getName().toUpperCase()%></td>
                      <td width="8%">&nbsp;</td>
                    </tr>
					<%
						}catch(Exception exx){}
					}
					%>
                    <tr>
                      <td><b><%=textListTitleHeader[SESS_LANGUAGE][2]%></b></td>
                        <td><b>: </b><%=mat.getName()%></td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td><strong>Periode</strong></td>
                        <td><strong>: </strong><%=Formater.formatDate(srcStockCard.getStardDate(), "dd-MM-yyyy")%> <%= textListTitleHeader[SESS_LANGUAGE][3] %> <%=Formater.formatDate(srcStockCard.getEndDate(), "dd-MM-yyyy")%></td>
                      <td>&nbsp;</td>
                    </tr>
                  </table></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, start, records)%></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr>
                        <td width="66%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch">
							  	<img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][6],ctrLine.CMD_BACK_SEARCH,true)%>"></a>
							  </td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdBack()">
							  	<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][6],ctrLine.CMD_BACK_SEARCH,true)%></a>
							  </td>
                            </tr>
                          </table></td>
                        <td width="34%">
						  <%if(true==false){%>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListTitleHeader[SESS_LANGUAGE][4]%>
                                </a></td>
                            </tr>
                          </table>
						  <%}else{%>
						  &nbsp;
						  <%}%>
						</td>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </form></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>

</html>
