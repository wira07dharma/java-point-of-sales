<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequestItem"%>
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
         com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem,
         com.dimata.posbo.session.masterdata.SessMaterial,	
         com.dimata.common.entity.payment.CurrencyType,
        
         
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
        {"PURCHASE REQUEST", "Nomor", "Lokasi", "Tanggal", "Supplier", "Nama", "Alamat", "Telp", "Contact", "Keterangan","Terms Of Payment"},
        {"PURCHASE REQUEST", "Number", "From Location", "Date", "Supplier", "Name", "Address", "Phone", "Contact", "Description","Terms Of Payment"}
    };
    public static final String textHeaderItem[][] = {
        {"No", "SKU", "Nama", "Qty Request", "Unit Request", "Harga", "Sub Total", "Barcode", "Term"},
        {"No", "Code", "Name", "Qty Request", "Unit Request", "Price", "Sub Total", "Barcode", "Term"}
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
         ctrlist.addHeader(textHeaderItem[language][6], "10%");
          ctrlist.addHeader(textHeaderItem[language][7], "10%");

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
            PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            
            Unit unitStock = new Unit();
            try{
            unitStock=PstUnit.fetchExc(poItem.getUnitRequestId());
                       }catch(Exception ex){}
            
            
            rowx = new Vector();
            start = start + 1;

            rowx.add("<div align=\"center\">" + start + "</div>");
          
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            // price
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getCurrentStock())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getMinimStock())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            rowx.add(unitStock.getCode());
          
           
     
           
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
    long oidPurchaseRequest = FRMQueryString.requestLong(request,"hidden_material_request_id");
    long oidPurchaseRequestItem = FRMQueryString.requestLong(request,"hidden_mat_request_item_id");

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
    CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
    iErrCode = ctrlPurchaseRequest.action(Command.EDIT,oidPurchaseRequest,"",0);
    FrmPurchaseRequest frmPurchaseRequest = ctrlPurchaseRequest.getForm();
    PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();
    /**
     * check if document may modified or not 
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlPurchaseRequestItem ctrlPurchaseRequestItem = new CtrlPurchaseRequestItem(request);
    ctrlPurchaseRequestItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlPurchaseRequestItem.action(iCommand,oidPurchaseRequestItem,oidPurchaseRequest,"",0);
    FrmPurchaseRequestItem frmPurchaseRequestItem = ctrlPurchaseRequestItem.getForm();
    PurchaseRequestItem poItem = ctrlPurchaseRequestItem.getPurchaseRequestItem();
    msgString = ctrlPurchaseRequestItem.getMessage();

    String whereClauseItem = PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;
    String orderClauseItem = "";
    int vectSizeItem = PstPurchaseRequestItem.getCount(whereClauseItem);
    whereClauseItem = "POI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] + "=" + oidPurchaseRequest;
    int recordToGetItem = 25;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlPurchaseRequestItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    Vector listPurchaseOrderItem = PstPurchaseRequestItem.list(0, 0, whereClauseItem);
    if (listPurchaseOrderItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listPurchaseOrderItem = PstPurchaseRequestItem.list(startItem, recordToGetItem, whereClauseItem);
    }


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
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">
        <!-- #EndEditable --> 
        <style>
            .btnPrint {
                float: right;
                width: auto;
                padding: 3px;
                min-width: 80px;
                color: rgb(255, 255, 255);
                border: thin solid rgb(76, 174, 76);
                background-color: rgb(92, 184, 92);
                border-radius: 3px;
                cursor:pointer;
            }
            @media print {
                .btnPrint{
                    display:none;
                }
            }
            img.title.logo {
              width: 85px;
              text-align: center;
              margin: 15px auto;
              display: block;
          }
        </style>
        
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <button style="float:right;" onClick="window.print()" class="btnPrint" type="button">Print</button>
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
                                    <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
                                    <input type="hidden" name="hidden_mat_request_item_id" value="<%=oidPurchaseRequestItem%>">
                                   
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr align="center"> 
                                            <td colspan="3" class="title" align="center"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr align="left" class="listgensell">
                                                        <td colspan="4">
                                                            <div class="col-md-12">
                                                              <img class="title logo" src="../../../images/company.png">
                                                          </div>
                                                            <table width="100%" border="0" cellpadding="1">
                                                                <tr>
                                                                    <td class="title" align="left" width="15%"></td>
                                                                    <td class="title" align="center" width="70%"><b>&nbsp;<%=textHeaderMain[SESS_LANGUAGE][0].toUpperCase()%></b></td>
                                                                    <td width="15%"></td>
                                                                </tr>
                                                            </table>                                                        
                                                        </td>
                                                    </tr>
                                                    <tr align="center" class="listgensell"> 
                                                        <td colspan="4" nowrap><b>&nbsp;</b></td>
                                                    </tr>
                                                    
                                                    <tr class="listgensell"> 
                                                        <td width="10%" align="left" nowrap><%=textHeaderMain[SESS_LANGUAGE][1]%></td>
                                                        <td width="30%" align="left"> : <%=po.getPrCode()%> </td>
                                                        <td width="10%" align="left"><%=textHeaderMain[SESS_LANGUAGE][2]%></td>                                                       
                                                        <td width="40%" align="left">: 
                                                        <%
                                                                Location loc = new Location();
                                                                try {
                                                                    loc = PstLocation.fetchExc(po.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc.getName()%> </td>                          
                                                   </tr>
                                                   <tr class="listgensell"> 
                                                        <td width="10%" align="left"><%=textHeaderMain[SESS_LANGUAGE][3]%></td>
                                                        <td width="30%" align="left">: <%=Formater.formatDate(po.getPurchRequestDate(), "dd-MM-yyyy")%></td>
                                                        <td width="10%" align="left"><%=textHeaderMain[SESS_LANGUAGE][6]%></td>                                                       
                                                        <td width="40%" align="left">: <%=loc.getAddress()%></td>
                                                    </tr>
                                                    <tr class="listgensell"> 
                                                        <td width="10%" align="left"><%=textHeaderMain[SESS_LANGUAGE][7]%></td>
                                                        <td width="30%" align="left">: <%=loc.getTelephone()%></td>
                                                        
                                                        
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
                                                                                <td width="12%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][7]%></td>
                                                                                <% } else { %>
                                                                                <td width="12%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][1]%></td>
                                                                                <% } %>
                                                                                <td width="27%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][2]%></td>

                                                                                <td width="6%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][3]%></td>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][4]%></td>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][8]%></td>
                                                                                <% if (includePrintPrice!=0){%>
                                                                                <td width="9%" class="listgentitle"><%=textHeaderItem[SESS_LANGUAGE][5]%></td>
                                                                                
                                                                                <%}%>

                                                                            </tr>
                                                                            <%
                                                                                int start = 0;
                                                                                double totalharga = 0.0;
                                                                                double totalQty = 0.0;
                                                                                for (int i = 0; i < listPurchaseOrderItem.size(); i++) {
                                                                                    
                                                                                    Vector temp = (Vector) listPurchaseOrderItem.get(i);
                                                                                    
                                                                                    PurchaseRequestItem poItemx = (PurchaseRequestItem) temp.get(0);
                                                                                    Material mat = (Material) temp.get(1);
                                                                                    Unit unit = (Unit) temp.get(2);
                                                                                    
                                                                                    start = start + 1;
                                                                                    mat.setOID(poItemx.getMaterialId());
                                                                                    totalharga = totalharga + (poItemx.getTotalPrice()/po.getExhangeRate());
                                                                                    totalQty = totalQty + poItemx.getQuantity();
                                                                            %>
                                                                            <tr> 
                                                                                <td width="6%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                    <td width="12%" class="listgensell">&nbsp;<%=mat.getBarCode()%></td>  
                                                                                <% } else { %>
                                                                                    <td width="12%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <% } %>
                                                                                <td width="25%" class="listgensell">&nbsp;<%=mat.getName()%></td>
                                                                                <td width="9%" align="center" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(poItemx.getQuantity())%></td>
                                                                                <td width="5%" align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                                <td width="5%" align="center" class="listgensell">&nbsp;<%=PstPurchaseOrder.fieldsPurchaseRequestType[poItemx.getTermPurchaseRequest()]%></td>
                                                                                <%if (includePrintPrice!=0){%>
                                                                                    <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(poItemx.getTotalPrice()/po.getExhangeRate())%></td>
                                                                                <%}%>

                                                                            </tr>
                                                                            <%}%>
                                                                            <tr> 
                                                                                <td width="6%" colspan="3" align="center" class="listgensell">&nbsp;<b>TOTAL</b></td>
                                                                                <td width="5%" align="center" class="listgensell">&nbsp;<b><%=totalQty%></b></td>
                                                                                <td width="5%" align="center" class="listgensell">&nbsp;<b></b></td>
                                                                                <td width="5%" align="center" class="listgensell">&nbsp;<b></b></td>
                                                                                <%if (includePrintPrice!=0){%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;</td>
                                                                                <td width="10%" align="center" class="listgensell">&nbsp;<b><%=FRMHandler.userFormatStringDecimal(totalharga)%></b></td>
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
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <%//if((listMatReceiveItem!=null) && (listMatReceiveItem.size()>0) && (rec.getTotalPpn()>0)){%>
                                                    <%if ((listPurchaseOrderItem != null) && (listPurchaseOrderItem.size() > 0)) {%>
                                                    <tr> 
                                                        <td width="27%" valign="top"> 
                                                            <table width="100%" border="0">
                                                                <tr class="listgensell"> 
                                                                    <td width="44%"> 
                                                                        <div align="right"><%//="Total"%></div>
                                                                    </td>
                                                                    <td width="15%"> 
                                                                        <div align="right"></div>
                                                                    </td>
                                                                    <td width="41%"> 
                                                                        <div align="right"> 
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>	
                                     <%
                                     if(useForRaditya.equals("0")){
                                     %>
                                    <table width="100%">			  
                                        <tr align="left" valign="top"> 
                                            <td height="40" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>

                                            <%if (signPO1.equals(signPO1) && !signPO1.equals("Not initialized")) {%>
                                            <td width="25%" align="center" nowrap><%=signPO1%></td>
                                            <%} else {%>
                                            <td width="25%" align="center" nowrap>Mengetahui,</td>
                                            <%}%>
                                            <%if (signPO2.equals(signPO2) && !signPO2.equals("Not initialized")) {%>
                                            <td width="25%" align="center" nowrap><%=signPO2%></td>
                                            <%} else {%>
                                            <td align="center" valign="bottom" width="25%">Merchandise,</td>
                                            <%}%>
                                            <%if (signPO3.equals(signPO3) && !signPO3.equals("Not initialized")) {%>
                                            <td width="25%" align="center" nowrap><%=signPO3%></td>
                                            <%} else {%>
                                            <td width="25%" align="center">Accounting,</td>
                                            <%}%>
                                            <%if (signPO4.equals(signPO4) && !signPO4.equals("Not initialized")) {%>
                                            <td width="25%" align="center" nowrap><%=signPO4%></td>
                                            <%} else {%>
                                            <td width="25%" align="center">Inventory,</td>
                                            <%}%>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="75" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center" nowrap>
                                                (.................................)
                                            </td>
                                            <td align="center" valign="bottom" width="25%">
                                                (.................................)
                                            </td>
                                            <td width="25%" align="center">
                                                (.................................)
                                            </td> 
                                            <td width="25%" align="center">
                                                (.................................)
                                            </td> 
                                        </tr>
                                    </table>
                                        <%}%>
                                </form>
                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <button style="float:right;" onClick="window.print()" class="btnPrint" type="button">Print</button>
    </body>
    <!-- #EndTemplate -->
</html>
