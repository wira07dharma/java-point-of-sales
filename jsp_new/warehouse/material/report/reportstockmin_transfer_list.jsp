<%-- 
    Document   : reportstockmin_transfer_list
    Created on : Dec 8, 2015, 2:54:01 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
				 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.search.SrcMinimumStock,
                 com.dimata.posbo.session.warehouse.SessMinimumStock,
                 com.dimata.posbo.form.search.FrmSrcMinimumStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.form.warehouse.CtrSourceStockCode,
                 com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem"%>
<%@ page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@ page import="com.dimata.common.entity.contact.ContactList"%>
<%@ page import="com.dimata.common.entity.contact.PstContactList"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Kode Baramg","Nama Barang","Stok Minimum","Stok","Total","Delta","Order Belum Dikirim"},
	{"No","Product Code","Product Name","Minimum Stock","Stock on Hand","Total","Delta Minus","PO Outstanding"}
};

public Vector drawList(int language,Vector objectClass, Vector vLovation,int start,Vector vectLocWh){
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("1");

        ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","2","0","center","center");
        ctrlist.dataFormat(textListMaterialHeader[language][1],"8%","2","0","center","midle");
        //ctrlist.dataFormat("Unit","2%","2","0","center","midle");
        ctrlist.dataFormat(textListMaterialHeader[language][2],"15%","2","0","center","midle");

        ctrlist.dataFormat(textListMaterialHeader[language][3],""+(5*vLovation.size())+"%","0",""+(vLovation.size()+1)+"","center","center");
        ctrlist.dataFormat(textListMaterialHeader[language][4],""+(5*vLovation.size())+"%","0",""+(vLovation.size()+1)+"","center","center");
        ctrlist.dataFormat(textListMaterialHeader[language][7],""+(5*vectLocWh.size())+"%","0",""+(vectLocWh.size()+1)+"","center","center");
        ctrlist.dataFormat(textListMaterialHeader[language][6],"5%","2","0","center","center");

        // vector untuk lokasi minimum stock
        if(vLovation!=null && vLovation.size()>0){
            for(int k=0;k<vLovation.size();k++){
                Location location = (Location)vLovation.get(k);
                ctrlist.dataFormat(location.getName(),"5%","0","0","center","center");
            }
            ctrlist.dataFormat(textListMaterialHeader[language][5],"5%","0","0","center","center");
        }

        // vector untuk lokasi stock
        if(vLovation!=null && vLovation.size()>0){
            for(int k=0;k<vLovation.size();k++){
                Location location = (Location)vLovation.get(k);
                ctrlist.dataFormat(location.getName(),"5%","0","0","center","center");
            }
            ctrlist.dataFormat(textListMaterialHeader[language][5],"5%","0","0","center","center");
        }

        // vector untuk lokasi stock
        if(vectLocWh!=null && vectLocWh.size()>0){
            for(int k=0;k<vectLocWh.size();k++){
                Location location = (Location)vectLocWh.get(k);
                ctrlist.dataFormat(location.getName(),"5%","0","0","center","center");
            }
            ctrlist.dataFormat(textListMaterialHeader[language][5],"5%","0","0","center","center");
        }

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
        double totalMin = 0;
        double totalQty = 0;
        int idx = 1;
        for(int i=0; i<objectClass.size(); i++){
	    Vector rowx = new Vector();
	    Vector vtitem = (Vector)objectClass.get(i);
            Material material = (Material)vtitem.get(0);
            Vector vMin = (Vector)vtitem.get(1);
            Vector vStock = (Vector)vtitem.get(2);
            
            double totSubTotal = 0;
            double totDelta = 0;

            rowx.add(""+(start+idx));
            rowx.add(material.getSku());
            rowx.add(material.getName()+" "+
                    "<input type=\"hidden\" class=\"formElemen\" name=\"txt_unit"+material.getOID()+"\" size=\"6\" value=\""+material.getDefaultStockUnitId()+"\">");
            //rowx.add(""+material.getDefaultStockUnitId());

            // ini proses untuk qty min
            for(int k=0; k<vMin.size(); k++){
                double qtyMin = Double.parseDouble((String)vMin.get(k));
                rowx.add(FRMHandler.userFormatStringDecimal(qtyMin));
                totSubTotal = totSubTotal + qtyMin;
            }
            totalMin = totalMin + totSubTotal;
            totDelta = totSubTotal;
            rowx.add("<b>"+FRMHandler.userFormatStringDecimal(totSubTotal)+"</b>");
            totSubTotal = 0;

            // ini adalan proses untuk qty stock
            double totalQtyRequest = 0;
            for(int k=0; k<vStock.size(); k++){
                double qtyStock = Double.parseDouble((String)vStock.get(k));
                double qtyMin = Double.parseDouble((String)vMin.get(k));

                Location location = (Location)vLovation.get(k);

                if(qtyMin!=0){
                    if(location.getType()==PstLocation.TYPE_LOCATION_WAREHOUSE)
                        rowx.add(FRMHandler.userFormatStringDecimal(qtyStock) + "  ( "+FRMHandler.userFormatStringDecimal(qtyStock - qtyMin)+" ) <input type=\"hidden\" class=\"formElemen\" name=\"txt_stock_"+material.getOID()+"\" size=\"2\" " +
                            "value=\""+qtyStock+"\">");
                    else{
                        double qtytransfer = qtyStock - qtyMin;
                        if(qtytransfer<0){
                            qtytransfer = qtytransfer * -1;
                            totalQtyRequest = totalQtyRequest + qtytransfer;
                        }else
                            qtytransfer = 0;
                        rowx.add(FRMHandler.userFormatStringDecimal(qtyStock) + "  ( "+FRMHandler.userFormatStringDecimal(qtyStock - qtyMin)+" ) <br> " +
                            "<input type=\"text\" class=\"formElemen\" name=\"txt_dispatch_"+location.getOID()+"_"+material.getOID()+"\" size=\"6\" " +
                            "value=\""+qtytransfer+"\">");
                    }
                }else{
                    if(location.getType()==PstLocation.TYPE_LOCATION_WAREHOUSE){
                        if((qtyStock - totalQtyRequest) < 0){
                            rowx.add(FRMHandler.userFormatStringDecimal(qtyStock) + "  ( "+FRMHandler.userFormatStringDecimal(qtyMin)+" )<br>Need Stock= "+FRMHandler.userFormatStringDecimal((qtyStock - totalQtyRequest)*-1)+" <input type=\"hidden\" class=\"formElemen\" name=\"txt_stock_"+material.getOID()+"\" size=\"2\" " +
                            "value=\""+qtyStock+"\">");
                        }else{
                            rowx.add(FRMHandler.userFormatStringDecimal(qtyStock) + "  ( "+FRMHandler.userFormatStringDecimal(qtyMin)+" ) <br> <input type=\"hidden\" class=\"formElemen\" name=\"txt_stock_"+material.getOID()+"\" size=\"2\" " +
                            "value=\""+qtyStock+"\">");
                        }
                    }else
                        rowx.add(FRMHandler.userFormatStringDecimal(qtyStock) + "  ( "+FRMHandler.userFormatStringDecimal(qtyMin)+" ) <br> <input type=\"text\" class=\"formElemen\" name=\"txt_dispatch_"+location.getOID()+"_"+material.getOID()+"\" size=\"6\" " +
                            "value=\"\">");
                }
                totSubTotal = totSubTotal + qtyStock;
            }
            totalQty = totalQty + totSubTotal;
            totDelta = totDelta - totSubTotal;
            rowx.add("<b>"+FRMHandler.userFormatStringDecimal(totSubTotal)+"</b>");
            totSubTotal = 0;

            // ini proses untuk pencarian
            // qty po yang masih blm di kirim
            Vector list = new Vector();
            double qtyNeedStock = 0;
            if(vectLocWh!=null && vectLocWh.size()>0){
                for(int k=0;k<vectLocWh.size();k++){
                    list = new Vector();
                    Location location = (Location)vectLocWh.get(k);
                    double qtyPo = PstPurchaseOrderItem.getQtyResidue(material.getOID(),location.getOID());

                    // ini proses pemcarian data khusus untuk no PO yang belum terkirim
                    list = PstPurchaseOrderItem.getPOPendingOrder(list, material.getOID(),location.getOID());
                    String strNoPo = "";
                    ContactList contact = null;
                    for(int a=0;a<list.size();a++){ 
                        contact = new ContactList();
                        PurchaseOrder purch = (PurchaseOrder)list.get(a);
                        try{
                            contact = PstContactList.fetchExc(purch.getSupplierId());
                        }catch(Exception e){}
                        if(purch.getTermOfPayment()!=0){
                            strNoPo = strNoPo+"<br>["+purch.getTermOfPayment()+"]<a nowrap href=\"javascript:cmdgopo('"+purch.getOID()+"')\">"+contact.getCompName()+"</a>";
                            System.out.println(purch.getPoCode()+" strNoPo : "+strNoPo);
                        }
                    }
                    rowx.add(FRMHandler.userFormatStringDecimal(qtyPo)+strNoPo);
                    totSubTotal = totSubTotal + qtyPo;
                }
            }
            totalQty = totalQty + totSubTotal;
            totDelta = totSubTotal - totDelta;
            rowx.add("<b>"+FRMHandler.userFormatStringDecimal(totSubTotal)+"</b>");
            totSubTotal = 0;

            // delta qty minimum
            String strTotDelta = "";
            String strQtyPO = "";
            if(totDelta<0){
                strTotDelta = "("+FRMHandler.userFormatStringDecimal(totDelta* -1)+")";
                strQtyPO = ""+FRMHandler.userFormatStringDecimal(totDelta* -1);
            }else{
                strTotDelta = ""+FRMHandler.userFormatStringDecimal(totDelta);
                strQtyPO = "";
            }

            rowx.add("<b>"+strTotDelta+"<br><input type=\"text\" class=\"formElemen\" name=\""+material.getOID()+"\" size=\"6\" value=\""+strQtyPO+"\">" +
                                                        "<input type=\"hidden\" class=\"formElemen\" name=\"txt_order\" size=\"6\" value=\""+material.getOID()+"\"></b>");

            //if(totDelta != 0){
                idx = idx + 1;
                lstData.add(rowx);
            //}
        }
        result = ctrlist.drawVectorMeList();
    }
	return result;
}
%>

<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
long supplier_id_select = FRMQueryString.requestLong(request, FrmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_SUPPLIER_ID]);
int typeRequest =Integer.parseInt((request.getParameter("typeRequest")==null) ? "0" : request.getParameter("typeRequest"));

int recordToGet = 0;
int vectSize = 0;
String whereClause = "";
long select_location_id=0;
/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcMinimumStock srcMinimumStock = new SrcMinimumStock();
SessMinimumStock sessMinimumStock = new SessMinimumStock();
FrmSrcMinimumStock frmSrcMinimumStock = new FrmSrcMinimumStock(request, srcMinimumStock);

/**
* get vectSize, start and data to be display in this page
*/
//Vector vectLoc = PstLocation.list(0,0,"","");

//String whereLoc = "( "+PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_WAREHOUSE;
//whereLoc += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE+" )";
String whereClausex = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                      " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";
whereClausex += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
Vector vectLocWh = PstLocation.listLocationCreateDocument(0,0,whereClausex,"");

Location location = new Location();

if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	 try{
		srcMinimumStock = (SrcMinimumStock)session.getValue(SessMinimumStock.SESSION_MINIMUM_NAME);
		if (srcMinimumStock == null) srcMinimumStock = new SrcMinimumStock();
	 }catch(Exception e){
		srcMinimumStock = new SrcMinimumStock();
	 }
}else{
    
    frmSrcMinimumStock.requestEntityObject(srcMinimumStock);
    String[] strLocation = request.getParameterValues(FrmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_LOCATION_ID]);

    try{
        if(strLocation.length>0){
            for(int j=0;j<strLocation.length;j++){
                String oidCateg = strLocation[j];
                if(Long.parseLong(oidCateg)!=0){
                    location = new Location();
                    try{
                        location = PstLocation.fetchExc(Long.parseLong(oidCateg));
                        //if(location.getType()!=PstLocation.TYPE_LOCATION_WAREHOUSE){ disable opie-eyek 20140124
                            srcMinimumStock.setvLocation(location);
                        //}
                    if(strLocation.length==1){
                        if(srcMinimumStock.getvLocation().size()>0){
                        Vector list = srcMinimumStock.getvLocation();
                         if(list !=null && list.size()>0){
                            for(int k=0;k<list.size();k++){
                               Location locationSelect = (Location)list.get(k);
                               select_location_id=locationSelect.getOID();
                            }
                        }
                       }
                    }

                    }catch(Exception e){

                 }

                }
            }
            //disable opie-eyek kenapa harus di cari warehouse 20140124
           /* long  oidWarehouse = PstLocation.getLocationByType(PstLocation.TYPE_LOCATION_WAREHOUSE);
            try{
                Location location = PstLocation.fetchExc(oidWarehouse);
                srcMinimumStock.setvLocation(location);

            }catch(Exception e){}*/
        }
    }catch(Exception e){}

//add opie-eyek 20122612


    /*if(srcMinimumStock.getLocationId() == 0){
        srcMinimumStock.setvLocation(vectLoc);
    }else{
        srcMinimumStock.setvLocation(new Vector());
        for(int i=0;i<vectLoc.size();i++){
            Location location = (Location)vectLoc.get(i);
            if(location.getOID()==srcMinimumStock.getLocationId()){
                srcMinimumStock.setvLocation(location);
                break;
            }
        }
    }*/
}
session.putValue(SessMinimumStock.SESSION_MINIMUM_NAME, srcMinimumStock);

vectSize = SessMinimumStock.getCountReportMinimumStock(srcMinimumStock);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
    CtrSourceStockCode ctrSourceStockCode = new CtrSourceStockCode(request);
	start = ctrSourceStockCode.actionList(iCommand,start,vectSize,recordToGet);
}
Vector records = SessMinimumStock.getReportMinimumStock(srcMinimumStock,start,recordToGet);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdgopo(oid){
    var cmd = document.frm_reportstock.command.value="<%=Command.EDIT%>";
    var oid = document.frm_reportstock.hidden_material_order_id.value = oid;
    //document.frm_reportstock.action="../../../purchasing/material/pom/pomaterial_edit.jsp";
    //document.frm_reportstock.target("openpo")
    //document.frm_reportstock.submit();
    //document.frm_reportstock.target("")
    window.open("../../../purchasing/material/pom/pomaterial_edit.jsp?command="+cmd+"&hidden_material_order_id="+oid,"openpo","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function createtransfer(oid){
        document.frm_reportstock.command.value="<%=Command.SAVE%>";
        document.frm_reportstock.action="<%=approot%>/warehouse/material/dispatch/create_transmaterial.jsp";
        document.frm_reportstock.submit();
}

function cmdListFirst()
{
	document.frm_reportstock.command.value="<%=Command.FIRST%>";
	document.frm_reportstock.action="reportstockmin_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListPrev()
{
	document.frm_reportstock.command.value="<%=Command.PREV%>";
	document.frm_reportstock.action="reportstockmin_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListNext()
{
	document.frm_reportstock.command.value="<%=Command.NEXT%>";
	document.frm_reportstock.action="reportstockmin_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListLast()
{
	document.frm_reportstock.command.value="<%=Command.LAST%>";
	document.frm_reportstock.action="reportstockmin_list.jsp";
	document.frm_reportstock.submit();
}

function cmdBack()
{
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportstockmin_transfer.jsp";
	document.frm_reportstock.submit();
}

function printForm(){
	window.open("reportstockmin_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function createpo(){
    document.frm_reportstock.command.value="<%=Command.LIST%>";
	document.frm_reportstock.action="<%=approot%>/purchasing/material/pom/create_pomaterial.jsp";
	document.frm_reportstock.submit();
}

function createpr(){
        document.frm_reportstock.command.value="<%=Command.LIST%>";
        document.frm_reportstock.typeRequest.value="<%=typeRequest%>";
	document.frm_reportstock.action="<%=approot%>/purchasing/material/pom/create_prmaterial.jsp";
	document.frm_reportstock.submit();
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

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
  <%if(menuUsed == MENU_PER_TRANS){%>
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
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Laporan Minimum Stok <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="hidden_material_order_id" value="0">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="location_id_select" value="<%=select_location_id%>">
              <input type="hidden" name="supplier_id_select" value="<%=supplier_id_select%>">
              <input type="hidden" name="typeRequest" value="<%=typeRequest%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td valign="middle" colspan="3">
                    <hr size="1">
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3">
				<%
                    Vector list = drawList(SESS_LANGUAGE,records,srcMinimumStock.getvLocation(),start,vectLocWh);
                    if(list !=null && list.size()>0){
                        for(int k=0;k<list.size();k++){
                            out.println(list.get(k));
                        }
                    }
				%>
				  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="8" align="left" colspan="3" class="command"> <span class="command">
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					//out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
                    </span> </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3">
                    <table width="100%" border="0">
                      <tr>
                        <td width="30%">
                          <table width="55%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <!--td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Report",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td nowrap width="2%">&nbsp;</td>
                              <td class="command" nowrap width="92%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Report",ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="70%">
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <%if(typeRequest==0){%>
                                <td width="80%" align="right" nowrap>
                                    <%
                                        String whereSource = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " NOT IN ('" + location.getOID() + "')";
                                        Vector val_lokasi_source = new Vector(1, 1);
                                        Vector key_lokasi_source = new Vector(1, 1);
                                        Vector vt_lokasi_source = PstLocation.list(0, 0, whereSource, PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        if (vt_lokasi_source != null && vt_lokasi_source.size() > 0) {
                                            for (int d = 0; d < vt_lokasi_source.size(); d++) {
                                                Location loc = (Location) vt_lokasi_source.get(d);
                                                String cntName = loc.getName();
                                                if (cntName.compareToIgnoreCase("'") >= 0) {
                                                    cntName = cntName.replace('\'', '`');
                                                }
                                                val_lokasi_source.add(String.valueOf(loc.getOID()));
                                                key_lokasi_source.add(cntName);
                                            }
                                        }
                                    %>
                                   Lokasi Pengirim <%=ControlCombo.draw("lokasi_source_id", null, "" + 0, val_lokasi_source, key_lokasi_source, "", "formElemen")%>
                                </td>
                                <td width="20%" align="right" nowrap><a href="javascript:createtransfer()" class="command" ><%if(records.size()>0){%>Next Process Transfer Stock<%}%></a></td>
                                
                              <%}else{%>
                                <td width="80%" align="right" nowrap><a href="javascript:createpr()"><%if(records.size()>0){%>Next Proses PR<%}%></a></td>
                                <td width="20%" align="right" nowrap>&nbsp;</td>
                              <%}%>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
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
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
