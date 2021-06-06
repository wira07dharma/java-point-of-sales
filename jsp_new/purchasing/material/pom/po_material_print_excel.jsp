<%-- 
    Document   : po_material_print_excel
    Created on : Apr 11, 2017, 12:58:54 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@ page import="com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
         com.dimata.posbo.session.masterdata.SessMaterial,	
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
         com.dimata.posbo.form.purchasing.CtrlPurchaseOrderItem,
         com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
         com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem,
         com.dimata.posbo.entity.masterdata.MatCurrency,
         com.dimata.posbo.entity.masterdata.PstMatCurrency,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.util.Formater
         "%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textHeaderMain[][] = {
        {"PURCHASE ORDER", "Nomor", "Lokasi", "Tanggal", "Supplier", "Nama", "Alamat", "Telp", "Contact", "Keterangan","STORE REQUESTION FORM"},
        {"PURCHASE ORDER", "Number", "From Location", "Date", "Supplier", "Name", "Address", "Phone", "Contact", "Description","STORE REQUESTION FORM"}
    };
    public static final String textHeaderItem[][] = {
        {"No", "Kode", "Nama Barang", "Unit", "Qty", "Harga", "Sub Total", "Barcode"},
        {"No", "Code", "Description", "Unit", "Qty", "Price", "Sub Total", "Barcode"}
    };
    
    public static final String textListOrderItem[][] = {
    {"No","Sku","Barcode","Nama","Qty","Unit","Hrg Beli Terakhir","Hrg Beli","Diskon Terakhir %",
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Qty Terima"},
    {"No","Code","Barcode","Name","Qty","Unit","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Qty Receive"}
};
   
    String signPO1 = PstSystemProperty.getValueByName("SIGN_PURCHASE_ORDER_1");
    String signPO2 = PstSystemProperty.getValueByName("SIGN_PURCHASE_ORDER_2");
    String signPO3 = PstSystemProperty.getValueByName("SIGN_PURCHASE_ORDER_3");
    String signPO4 = PstSystemProperty.getValueByName("SIGN_PURCHASE_ORDER_4");
    
  
    /**
     * this method used to maintain poMaterialList
     */
    public String drawListPoItem(int language, int iCommand, FrmMatReceiveItem frmObject, MatReceiveItem objEntity, Vector objectClass, long recItemId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textHeaderItem[language][0], "4%");
        ctrlist.addHeader(textHeaderItem[language][1], "11%");
        ctrlist.addHeader(textHeaderItem[language][2], "21%");
        ctrlist.addHeader(textHeaderItem[language][4], "5%");
        ctrlist.addHeader(textHeaderItem[language][5], "10%");


        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);
            rowx = new Vector();
            start = start + 1;

            rowx.add("<div align=\"center\">" + start + "</div>");
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            //rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
            rowx.add("<div align=\"center\">" + currencyType.getCode() + "</div>");
            rowx.add("<div align=\"center\">" + String.valueOf(recItem.getQty()) + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "</div>");
            lstData.add(rowx);
        }
        return ctrlist.draw();
    }

%>
<%
    /**
     * get approval status for create document 
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>
<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");
    long oidPurchaseOrderItem = FRMQueryString.requestLong(request, "hidden_mat_order_item_id");
    int typeRequest = FRMQueryString.requestInt(request,"typeRequest");
     
    int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");
    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    //add opie-eyek 20130812 untuk print price atau tidak
    int includePrintPrice = FRMQueryString.requestInt(request,"showprintprice");


    //adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
     if(useBarcodeorSku.equals("Not initialized")){
       useBarcodeorSku= "0";
     }
      chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);  
   
   
    

    /**
     * initialization of some identifier
     */
    int iErrCode = FRMMessage.NONE;
    String msgString = "";

    /**
     * purchasing pr code and title
     */
    String recCode = i_pstDocType.getDocCode(docType);
    String retTitle = "Purchase Order Barang"; //i_pstDocType.getDocTitle(docType);
    String recItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
    iErrCode = ctrlPurchaseOrder.action(Command.EDIT, oidPurchaseOrder);
    FrmPurchaseOrder frmPurchaseOrder = ctrlPurchaseOrder.getForm();
    PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();

    /**
     * check if document may modified or not 
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlPurchaseOrderItem ctrlPurchaseOrderItem = new CtrlPurchaseOrderItem(request);
    ctrlPurchaseOrderItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlPurchaseOrderItem.action(iCommand, oidPurchaseOrderItem, oidPurchaseOrder);
    FrmPurchaseOrderItem frmPurchaseOrderItem = ctrlPurchaseOrderItem.getForm();
    PurchaseOrderItem poItem = ctrlPurchaseOrderItem.getPurchaseOrderItem();
    msgString = ctrlPurchaseOrderItem.getMessage();

    String whereClauseItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder;
    String orderClauseItem = "";
    int vectSizeItem = PstPurchaseOrderItem.getCount(whereClauseItem);
    int recordToGetItem = 25;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlPurchaseOrderItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    Vector listPurchaseOrderItem = PstPurchaseOrderItem.list(0, 0, whereClauseItem);
    if (listPurchaseOrderItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listPurchaseOrderItem = PstPurchaseOrderItem.list(startItem, recordToGetItem, whereClauseItem);
    }

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "purchase_request.xls" ); 
%>

<html>
    <!-- #BeginTemplate "/Templates/print.dwt" --> 
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title
        ><script language="JavaScript">
            <!--
            //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function main(oid,comm)
            {
                document.frm_purchaseorder.command.value=comm;
                document.frm_purchaseorder.hidden_receive_id.value=oid;
                document.frm_purchaseorder.action="receive_wh_supp_po_material_edit.jsp";
                document.frm_purchaseorder.submit();
            }


            //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
            function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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
            //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------//-->
        </script>
        <!-- #EndEditable --> 
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #EndEditable --> 
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr> 
                <td width="88%" valign="top" align="left" height="56"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_purchaseorder" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
                                    <input type="hidden" name="hidden_mat_order_item_id" value="<%=oidPurchaseOrderItem%>">
                                    <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PURCHASE_ORDER_ID]%>" value="<%=oidPurchaseOrder%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr align="center"> 
                                            <td colspan="3" class="title" align="center"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr align="left" class="listgensell">
                                                        <td colspan="4">
                                                            <table width="100%" border="0" cellpadding="1">
                                                                <tr>
                                                                    <td class="title" align="left" width="15%"></td>
                                                                    <%if(typeRequest==0){%>
                                                                        <td class="title" align="center" width="70%"></td>
                                                                    <%}else{%>
                                                                        <td class="title" align="center" width="70%"></td>
                                                                    <%}%>
                                                                    <td width="15%"></td>
                                                                </tr>
                                                                <tr>
                                                                    <%if(typeRequest==0){%>
                                                                        <td class="title" align="center" width="70%"><b>&nbsp;<%=textHeaderMain[SESS_LANGUAGE][0].toUpperCase()%></b></td>
                                                                    <%}else{%>
                                                                        <td class="title" align="center" width="70%"><b>&nbsp;<%=textHeaderMain[SESS_LANGUAGE][10].toUpperCase()%></b></td>
                                                                    <%}%>
                                                                    <td width="15%"></td>
                                                                </tr>
                                                            </table>                                                        
                                                        </td>
                                                    </tr>
                                                    <tr align="center" class="listgensell"> 
                                                        <td colspan="4" nowrap><b>&nbsp;</b></td>
                                                    </tr>
                                                </table>
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr>
                                                        <td class="title" align="left" width="15%"></td>
                                                        <td class="title" align="left" width="15%">Supplier</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][1]%>: <%=po.getPoCode()%></td>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][5]%>:
                                                        <%
                                                                ContactList cnt = new ContactList();
                                                                try {
                                                                    cnt = PstContactList.fetchExc(po.getSupplierId());
                                                                } catch (Exception e) {
                                                                }
                                                                
                                                                long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
                                                                //kode untuk menentukan nama suplier
                                                                String cntName ="";
                                                                
                                                                
                                                                
                                                                if(oidNewSupplier==po.getSupplierId()){
                                                                    if (po.getRemark().indexOf(";")==-1){
                                                                        String arrRemark[] = po.getRemark().split(":");
                                                                        cntName = arrRemark[1]; 
                                                                    }else{
                                                                        String arrRemark[] = po.getRemark().split(";");
                                                                        String arrSuplierName [] = arrRemark[0].split(":");
                                                                        cntName = arrSuplierName[1];
                                                                    }

                                                                } else{
                                                                    ContactList objSuplier = PstContactList.fetchExc(po.getSupplierId());
                                                                    cntName  = objSuplier.getCompName();
                                                                }
                                            %>
                                                                
                                                                

                                                                
                                                            
                                                            <%=cntName%>  </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][3]%>: <%=Formater.formatDate(po.getPurchDate(), "dd-MM-yyyy")%></td>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][6]%> : <%=cnt.getBussAddress()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][2]%>: 
                                                            <%
                                                                Location loc = new Location();
                                                                try {
                                                                    loc = PstLocation.fetchExc(po.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc.getName()%></td>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][7]%> : <%=cnt.getTelpNr()%> </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="title" align="left" width="15%"><%=loc.getAddress()%></td>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][8]%> : <%=cnt.getTelpNr()%> </td>
                                                    </tr>
                                                     <tr>
                                                        <td class="title" align="left" width="15%"><%=textHeaderMain[SESS_LANGUAGE][7]%>: <%=loc.getTelephone()%></td>
                                                        <td class="title" align="left" width="15%"></td>
                                                    </tr>
                                                </table>                    
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td valign="top"> 
                                                <table width="100%" cellpadding="1" cellspacing="1">
                                                    <tr> 
                                                        <td colspan="2" > 
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                <tr align="left" valign="top"> 
                                                                    <td height="22" valign="middle" colspan="3"> 
                                                                        <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                                                            <tr align="center"> 
                                                                                <td width="6%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][0]%></td>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) { %>
                                                                                    <td width="12%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][1]%></td>
                                                                                    <td width="12%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][7]%></td>
                                                                                <% } else { %>
                                                                                    <td width="12%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][1]%></td>
                                                                                <% } %>
                                                                                    <td width="27%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][2]%></td>

                                                                                <td width="6%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][3]%></td>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][4]%></td>

                                                                                <% if (includePrintPrice!=0){%>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][5]%></td>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][6]%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%
                                                                                int start = 0;
                                                                                double totalharga = 0.0;
                                                                                double totalQty = 0.0;
                                                                                for (int i = 0; i < listPurchaseOrderItem.size(); i++) {
                                                                                    
                                                                                    Vector temp = (Vector) listPurchaseOrderItem.get(i);
                                                                                    
                                                                                    PurchaseOrderItem poItemx = (PurchaseOrderItem) temp.get(0);
                                                                                    Material mat = (Material) temp.get(1);
                                                                                    Unit unit = (Unit) temp.get(2);
                                                                                    MatCurrency matCurr = (MatCurrency) temp.get(3);
                                                                                    start = start + 1;
                                                                                    mat.setOID(poItemx.getMaterialId());
                                                                                    /*if (typePrint == 1) {
                                                                                    double priceSales = SessMaterial.getPriceSale(mat);
                                                                                    recItemx.setCost(priceSales);
                                                                                    recItemx.setTotal(priceSales * recItemx.getQty());
                                                                                    }*/
                                                                                    
                                                                                    /*untuk mendapatkan unit konversi*/
                                                                                    Unit unitKonv = new Unit();
                                                                                    try{
                                                                                        unitKonv=PstUnit.fetchExc(poItemx.getUnitRequestId());
                                                                                    }catch(Exception ex){}

                                                                                    totalharga = totalharga + (poItemx.getTotal()/po.getExchangeRate());
                                                                                    totalQty = totalQty + poItemx.getQuantity();
                                                                            %>
                                                                            <tr> 
                                                                                <td width="6%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                    <td width="12%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                 <td width="12%" class="listgensell">&nbsp;<%=mat.getBarCode()%></td>  
                                                                                <% } else { %>
                                                                                <td width="12%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <% } %>
                                                                                <td width="25%" class="listgensell">&nbsp;<%=mat.getName()%></td>

                                                                                <td width="9%" align="center" class="listgensell">&nbsp;<%=unitKonv.getName()%></td>
                                                                                <td width="5%" align="right" class="listgensell">&nbsp;<%=poItemx.getQuantity()%></td>
                                                                                <%if (includePrintPrice!=0){%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(poItemx.getCurBuyingPrice()/po.getExchangeRate())%></td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(poItemx.getTotal()/po.getExchangeRate())%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%}%>
                                                                            <tr> 
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                    <td width="6%" colspan="5" align="center" class="listgensell">&nbsp;<b>TOTAL</b></td>
                                                                                <% } else { %>
                                                                                    <td width="6%" colspan="4" align="center" class="listgensell">&nbsp;<b>TOTAL</b></td>
                                                                                <% } %>
                                                                                <td width="5%" align="right" class="listgensell">&nbsp;<b><%=totalQty%></b></td>
                                                                                <%if (includePrintPrice!=0){%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;</td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<b><%=FRMHandler.userFormatStringDecimal(totalharga)%></b></td>
                                                                                <%}%>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr> 
                                                        <td valign="top" rowspan="2"><%=textHeaderMain[SESS_LANGUAGE][9]%> : <%=po.getRemark()%> </td>
                                                        <td width="35%" valign="top">
                                                            <table width="100%" border="0">
                                                                <%
                                                                    /*String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                                                                    
                                                                    double total = 0;
                                                                    total = PstMatReceiveItem.getTotal(whereItem);
                                                                    if(typePrint==1){
                                                                    total = totalharga;
                                                                    }*/
                                                                    double ppn = po.getPpn()/po.getExchangeRate();
                                                                    if (ppn == 0) {
                                                                        ppn = defaultPpn/po.getExchangeRate();
                                                                    }
                                                                    double totalBeliWithPPN = (totalharga * (ppn / 100)) + totalharga;
                                                                    double valuePpn = 0.0;
                                                                    if (po.getIncludePpn() == 1) {
                                                                        valuePpn = totalharga - (totalharga / 1.1);
                                                                    } else if (po.getIncludePpn() == 0) {
                                                                        valuePpn = totalharga * (ppn / 100);
                                                                    }
                                                                %>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        
                                        
                                        
                                        <tr> 
                                            <td valign="top"> 
                                                <table width="100%" cellpadding="1" cellspacing="1">
                                                    <tr> 
                                                        <td colspan="2" > 
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                <tr align="left" valign="top"> 
                                                                    <td height="22" valign="middle" colspan="3"> 
                                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                            <tr align="center"> 
                                                                                <td width="6%" class="listgentitle"><%=signPO1%></td>
                                                                                <td width="6%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle"><%=signPO3%></td>
                                                                                <td width="9%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle"><%=signPO4%></td>
                                                                            </tr>
                                                                            <tr align="center"> 
                                                                                <td width="6%" class="listgentitle"></td>
                                                                                <td width="6%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle"></td>
                                                                            </tr>
                                                                            <tr align="center"> 
                                                                                <td width="6%" class="listgentitle">(.................................)</td>
                                                                                <td width="6%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle">(.................................)</td>
                                                                                <td width="9%" class="listgentitle"></td>
                                                                                <td width="9%" class="listgentitle">(.................................)</td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
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
        </table>
    </body>
    <!-- #EndTemplate -->
</html>
