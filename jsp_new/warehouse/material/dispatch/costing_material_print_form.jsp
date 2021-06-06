<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@page import="com.dimata.posbo.entity.masterdata.Costing"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.form.warehouse.*,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.Unit" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.Material" %>

<%@ include file = "../../../main/javainit.jsp" %>
<%
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Keterangan","Tipe Biaya","Consume"},
        {"No", "Location", "Destination", "Date", "Status", "Remark","Type Of Costing","Consume"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Unit", "Qty", "HPP", "Total", "Harga Jual", "Barcode","Stock Balance","Stock Fisik"},
        {"No", "Code", "Name", "Unit", "Qty", "Cost", "Total", "Sell Price", "Barcode","Stock Balance","Stock Fisik"}
    };

    public double getPriceCost(Vector list, long oid) {
        double cost = 0.00;
        if (list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                MatReceiveItem matReceiveItem = (MatReceiveItem) list.get(k);
                if (matReceiveItem.getMaterialId() == oid) {
                    cost = matReceiveItem.getCost();
                    break;
                }
            }
        }
        return cost;
    }

    /**
     * this method used to maintain dfList
     */
    public String drawListDfItem(int language, int iCommand,
            FrmMatDispatchItem frmObject, MatDispatchItem objEntity,
            Vector objectClass, long dfItemId, int start, String invoiceNumber, int tranUsedPriceHpp) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "3%");
        ctrlist.addHeader(textListOrderItem[language][1], "15%");
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");
        ctrlist.addHeader(textListOrderItem[language][4], "5%");
        if (tranUsedPriceHpp == 0) {
            ctrlist.addHeader(textListOrderItem[language][5], "10%");
        } else {
            ctrlist.addHeader(textListOrderItem[language][7], "10%");
        }

        ctrlist.addHeader(textListOrderItem[language][6], "10%");

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        /**
         * get data receive for get price cost
         */
        String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + "='" + invoiceNumber + "'";
        Vector list = PstMatReceive.list(0, 0, whereClause, "");
        Vector listItem = new Vector(1, 1);
        if (list != null && list.size() > 0) {
            MatReceive matReceive = (MatReceive) list.get(0);
            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
            listItem = PstMatReceiveItem.list(0, 0, whereClause, "");
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            rowx = new Vector();
            start = start + 1;

            double cost = getPriceCost(listItem, dfItem.getMaterialId());

            rowx.add("<div align=\"center\">" + start + "</div>");
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
            rowx.add("<div align=\"center\">" + String.valueOf(dfItem.getQty()) + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(cost) + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()) + "</div>");

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
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);
%>
<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidCostingMaterial = FRMQueryString.requestLong(request, "hidden_costing_id");
    long oidCostingMaterialItem = FRMQueryString.requestLong(request, "hidden_costing_item_id");
    int entrystokfisik = FRMQueryString.requestInt(request, "entrystokfisik");
//adding dynamic sign rec by mirahu 20120427
    String signCost1 = PstSystemProperty.getValueByName("SIGN_COSTING_1");
    String signCost2 = PstSystemProperty.getValueByName("SIGN_COSTING_2");
    String signCost3 = PstSystemProperty.getValueByName("SIGN_COSTING_3");

//adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
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
    String dfCode = ""; //i_pstDocType.getDocCode(docType);
    
    String dfTitle = "Costing Barang"; //i_pstDocType.getDocTitle(docType);
    if(SESS_LANGUAGE==1){
        dfTitle="Costing Barang";
    }else{
        dfTitle="ITEM COSTING DOCUMENT";
    }
    String dfItemTitle = dfTitle + " Item";

    /**
     * purchasing pr code and title
     */
    String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF));


    /**
     * process on df main
     */
    CtrlMatCosting ctrlMatCosting = new CtrlMatCosting(request);
    iErrCode = ctrlMatCosting.action(Command.EDIT, oidCostingMaterial);
    FrmMatCosting frmMatCosting = ctrlMatCosting.getForm();
    MatCosting costing = ctrlMatCosting.getMatCosting();

    /**
     * check if document may modified or not 
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlMatCostingItem ctrlMatCostingItem = new CtrlMatCostingItem(request);
    ctrlMatCostingItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatCostingItem.action(iCommand, oidCostingMaterialItem, oidCostingMaterial);
    FrmMatCostingItem frmMatCostingItem = ctrlMatCostingItem.getForm();
    MatCostingItem costingItem = ctrlMatCostingItem.getMatCostingItem();
    msgString = ctrlMatCostingItem.getMessage();

    String whereClauseItem = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + "=" + oidCostingMaterial;
    String orderClauseItem = "";
    int vectSizeItem = PstMatCostingItem.getCount(whereClauseItem);
    int recordToGetItem = 0;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatCostingItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }
    
    String whereClauseItemx = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+"="+oidCostingMaterial+
                         " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
    Vector listMatCostingItem = PstMatCostingItem.list(startItem, recordToGetItem, oidCostingMaterial,whereClauseItemx);
    if (listMatCostingItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listMatCostingItem = PstMatCostingItem.list(startItem, recordToGetItem, oidCostingMaterial);
    }
	
	Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
	Location lAwal = new Location();
    Location lTujuan = new Location();
	Costing cos = new Costing();
    try {
        lAwal = PstLocation.fetchExc(costing.getLocationId());
        lTujuan = PstLocation.fetchExc(costing.getCostingTo());
		cos = PstCosting.fetchExc(costing.getCostingId());
    } catch (Exception e) {

    }
%>
<html><!-- #BeginTemplate "/Templates/print.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">
        <!-- #EndEditable -->
		<link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>                        
            .tabelHeader td {padding-bottom: 10px; font-size: 12px}
            
            .tabel_data {
                font-size: 11px;
                border-top-color: black !important;
            }
            
            .tabel_data th {
                text-align: center;
                border-color: black !important;
                border-width: thin !important;
            }
            
            .tabel_data td {
                border-color: black !important;
            }
            
            .sum_total td {font-weight: bold}
            /*
            print-area { visibility: hidden; display: none; }
            print-area.preview { visibility: visible; display: block; }
            @media print
            {
                body .main-page * { visibility: hidden; display: none; } 
                body print-area * { visibility: visible; }
                body print-area   { visibility: visible; display: unset !important; position: static; top: 0; left: 0; }
            }
            */
        </style>
    </head>  
	<%
		if (typeOfBusinessDetail == 2){
	%>
	<body>
        <div class="main-page">
            <div style="margin: 10px">
                <br>
                <div>
                    <img style="width: 100px" src="../../../images/litama.jpeg">
                    <span style="font-size: 24px; font-family: TimesNewRomans">
                        <b><%=compName%></b>
                        <small><i>Gallery</i></small>
                    </span>
                </div>
                <div>
                    <h4><b>Laporan Pembiayaan Barang</b></h4>
                </div>
                <div>
                    <table class="tabelHeader" style="width: 100%">
                        <tr>
                            <td style="width: 10%">Nomor</td>
                            <td style="width: 20%">: <%=costing.getCostingCode()%></td>
                            
                            <td style="width: 10%">Lokasi Awal</td>
                            <td style="width: 25%">: <%=lAwal.getName()%></td>
                            
                            <td style="width: 15%">Keterangan</td>
                            <td style="width: ">: <%=costing.getRemark()%></td>
                        </tr>
                        <tr>
                            <td>Tanggal</td>
                            <td>: <%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></td>
                            
                            <td>Lokasi Tujuan</td>
                            <td>: <%=lTujuan.getName()%></td>
                            
                            <td>Tipe Biaya</td>
                            <td>: <%=cos.getName()%></td>
                        </tr>
                    </table>
                </div>

                <hr style="border-width: medium; border-color: black; margin-top: 5px">

                <table class="table table-bordered tabel_data">
                    <thead>
                        <tr>
                            <th>Kode</th>
                            <th>Nama Barang</th>
                            <th>Qty</th>
                            <th>Berat</th>
                            <th>HPP</th>
                            <th>Ongkos</th>
							<th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            double totalQty = 0;
                            double totalHpp = 0;
                            double totalBerat = 0;
                            double totalCost = 0;
                            double totalTotalHp = 0;
                            for (int i = 0; i < listMatCostingItem.size(); i++) {
                                Vector temp = (Vector) listMatCostingItem.get(i);
                                MatCostingItem costingItemx = (MatCostingItem) temp.get(0);
								Material mat = (Material) temp.get(1);
								Unit unit = (Unit) temp.get(2);
                                MaterialDetail detail = new MaterialDetail();
                                Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + "='"+costingItemx.getMaterialId()+"'", "");
                                Category category = new Category();
                                Color color = new Color();
                                try {
                                     category = PstCategory.fetchExc(mat.getCategoryId());
                                    color = PstColor.fetchExc(mat.getPosColor());
                                    if (!listMatDetail.isEmpty()) {
                                        detail = PstMaterialDetail.fetchExc(listMatDetail.get(0).getOID());
                                    }
                                } catch (Exception e) {

                                }
                                String itemName = SessMaterial.setItemNameForLitama(costingItemx.getMaterialId());
								if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){
									totalTotalHp += costingItemx.getTotalHppComposite();
									totalHpp = costingItemx.getHppComposite();
									totalQty += costingItemx.getQtyComposite();
								}else{
									totalTotalHp += (costingItemx.getHpp() * costingItemx.getQty()) + costingItemx.getCost();
									totalHpp += costingItemx.getHpp();
									totalQty += costingItemx.getQty();
								}
                                
                                
                                totalBerat += costingItemx.getWeight();
                                totalCost += costingItemx.getCost();
                        %>
                        <tr>
                            <td class="text-center"><%=mat.getSku()%></td>
                            <td><%=itemName%></td>
							<% if (mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){ %>
								<td class="text-center"><%=String.format("%,.0f", costingItemx.getQtyComposite())%></td>
							<% } else { %>
								<td class="text-center"><%=String.format("%,.0f", costingItemx.getQty())%></td>
							<% } %>
                            <td class="text-right"><%=String.format("%,.3f", costingItemx.getWeight())%></td>
                            <% if (mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){ %>
								<td class="text-right"><%=String.format("%,.0f", costingItemx.getHppComposite())%>.00</td>
							<% } else { %>
								<td class="text-right"><%=String.format("%,.0f", costingItemx.getHpp())%>.00</td>
							<% } %>
                            <td class="text-right"><%=String.format("%,.0f", costingItemx.getCost())%>.00</td>
                            <% if (mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){ %>
								<td class="text-right"><%=String.format("%,.0f", costingItemx.getTotalHppComposite())%>.00</td>
							<% } else { %>
								<td class="text-right"><%=String.format("%,.0f", (costingItemx.getHpp() * costingItemx.getQty()) + costingItemx.getCost())%>.00</td>
							<% } %>
                        </tr>
                        <%
                            }
                        %>
                        <tr class="sum_total">
                            <td colspan="2" class="text-right">Total :</td>
                            <td class="text-center"><%=String.format("%,.0f", totalQty)%></td>
                            <td class="text-right"><%=String.format("%,.3f", totalBerat)%></td>
                            <td class="text-right"><%=String.format("%,.0f", totalHpp)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalCost)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalTotalHp)%>.00</td>
                        </tr>
                    </tbody>                                                                                    
                </table>

                <!--//START TEMPLATE TANDA TANGAN//-->
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                    
            </div>
        </div>
	</body>
	<%
		} else {
	%>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr> 
                <td width="88%" valign="top" align="left" height="56"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_matdispatch" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_dispatch_id" value="<%=oidCostingMaterial%>">
                                    <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidCostingMaterialItem%>">
                                    <input type="hidden" name="<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_COSTING_MATERIAL_ID]%>" value="<%=oidCostingMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="entrystokfisik" value="<%=entrystokfisik%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr> 
                                            <td valign="top" colspan="3">&nbsp; </td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title"><b>DOKUMEN&nbsp;<%=dfTitle.toUpperCase()%></b></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" align="center"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="9%" align="left" nowrap><%=dfCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="35%" align="left"> : <%=costing.getCostingCode()%> </td>
                                                        <td align="center" valign="bottom" width="18%">&nbsp; </td>
                                                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : 
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(costing.getCostingTo());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc1.getName()%> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="9%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                        <td width="35%" align="left">: <%=Formater.formatDate(costing.getCostingDate(), "dd MMMM yyyy")%> </td>
                                                        <td align="center" valign="bottom" width="18%"> 
                                                        </td>
                                                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][1]%> : 
                                                            <%
                                                                Location loc2 = new Location();
                                                                try {
                                                                    loc2 = PstLocation.fetchExc(costing.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc2.getName()%> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="9%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                        <td width="35%" align="left">: 
                                                            <%
                                                                Costing costingx = new Costing();
                                                                try {
                                                                    costingx = PstCosting.fetchExc(costing.getCostingId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=costingx.getName()%> 
                                                        </td>
                                                        <td align="center" valign="bottom" width="18%"> </td>
                                                        <td width="38%" align="right"> <%=textListOrderHeader[SESS_LANGUAGE][7]%>
                                                        <%
                                                                Employee employee = new Employee();
                                                                try {
                                                                    employee = PstEmployee.fetchExc(costing.getContactId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=employee.getFullName()%> </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td valign="top"> 
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                                                <tr align="center"> 
                                                                    <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                    <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                    <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                                    <% } else {%>
                                                                    <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                    <% }%>
                                                                    <td width="30%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                    
                                                                    <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                    <%if (entrystokfisik == 1) {%>
                                                                        <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                                        <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][10]%></td>
                                                                    <% }%>
                                                                    <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                    <% if(privShowQtyPrice){%>
                                                                        <%
                                                                            if (tranUsedPriceHpp == 0) {
                                                                        %>
                                                                        <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                                                        <%} else {%>
                                                                        <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                        <%}%>
                                                                        <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                                    <%}%>
                                                                </tr>
                                                                <%
                                                                    int start = 0;

                                                                    /**
                                                                     * get data receive for get price cost
                                                                     */
                                                                    String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + "='" + costing.getInvoiceSupplier() + "'";
                                                                    Vector list = PstMatReceive.list(0, 0, whereClause, "");
                                                                    Vector listItem = new Vector(1, 1);
                                                                    if (list != null && list.size() > 0) {
                                                                        MatReceive matReceive = (MatReceive) list.get(0);
                                                                        whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
                                                                        listItem = PstMatReceiveItem.list(0, 0, whereClause, "");
                                                                    }

                                                                    double total = 0;
                                                                    for (int i = 0; i < listMatCostingItem.size(); i++) {
                                                                        Vector temp = (Vector) listMatCostingItem.get(i);
                                                                        MatCostingItem costingItemx = (MatCostingItem) temp.get(0);
                                                                        Material mat = (Material) temp.get(1);
                                                                        Unit unit = (Unit) temp.get(2);
                                                                        start = start + 1;
                                                                        
                                                                        if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){
                                                                            total += costingItemx.getTotalHppComposite();
                                                                        }else{
                                                                            total += costingItemx.getHpp() * costingItemx.getQty();
                                                                        }
                                                                        
                                                                        String listStockCode= "";
                                                                        if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                                                                             String where = PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]+"="+costingItemx.getOID();
                                                                             Vector cntStockCode = PstCostingStockCode.list(0,0,where,"");
                                                                             for (int s = 0; s < cntStockCode.size(); s++) {
                                                                                CostingStockCode costingStockCode = (CostingStockCode) cntStockCode.get(s);
                                                                                if(s==0){
                                                                                    listStockCode=listStockCode+"<br>&nbsp;SN : "+costingStockCode.getStockCode();
                                                                                }else{
                                                                                     listStockCode=listStockCode+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+costingStockCode.getStockCode();
                                                                                }

                                                                             }
                                                                        }
%>
                                                                <tr class="listgensell"> 
                                                                    <td width="5%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                    <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) { %>
                                                                    <td width="10%" class="listgensell">&nbsp;<%=mat.getBarCode()%></td>
                                                                     <% } else { %>
                                                                     <td width="10%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                      <% } %>
                                                                    <td width="30%" class="listgensell">&nbsp;<%=mat.getName()%><%=listStockCode%></td>
                                                                    <td width="10%" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                    <%if (entrystokfisik == 1) {%>
                                                                        <td width="10%" class="listgentitle"><%=costingItemx.getBalanceQty()%></td>
                                                                        <td width="10%" class="listgentitle"><%=costingItemx.getResidueQty()%></td>
                                                                    <% }%>
                                                                     <%if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){%>
                                                                        <td width="5%" align="right" class="listgensell">&nbsp;<%=costingItemx.getQtyComposite()%></td>
                                                                    <%}else{%>
                                                                        <td width="5%" align="right" class="listgensell">&nbsp;<%=costingItemx.getQty()%></td>
                                                                    <%}%>
                                                                    <% if(privShowQtyPrice){%>
                                                                        <%
                                                                        if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(costingItemx.getHppComposite())%></td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(costingItemx.getTotalHppComposite())%></td>
                                                                        <%}else{%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(costingItemx.getHpp())%></td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(costingItemx.getHpp() * costingItemx.getQty())%></td>
                                                                        <%}
                                                                        %>
                                                                    <%}%>
                                                                </tr>
                                                                <%}%>
                                                                <% if(privShowQtyPrice){%>
                                                                <tr class="listgensell">
                                                                     <%if (entrystokfisik == 1) {%>
                                                                         <td width="70%" colspan="8" align="right" class="listgensell">Total:</td>
                                                                     <% } else { %>
                                                                         <td width="70%" colspan="6" align="right" class="listgensell">Total:</td>
                                                                      <% } %>
                                                                    <td width="10%" align="right" class="listgensell"><b>&nbsp;<%=FRMHandler.userFormatStringDecimal(total)%></b></td>
                                                                </tr>
                                                                <%}%>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=costing.getRemark()%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="40" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                         <%if (signCost1.equals(signCost1) && !signCost1.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost1%>,</td>
                                                        <%} else {%>
                                                        <td width="34%" align="center" nowrap>Mengetahui,</td>
                                                        <%}%>
                                                        <%if (signCost2.equals(signCost2) && !signCost2.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost2%>,</td>
                                                        <%} else {%>
                                                        <td align="center" valign="bottom" width="33%">Pengirim,</td>
                                                        <%}%>
                                                         <%if (signCost3.equals(signCost3) && !signCost3.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost3%>,</td>
                                                        <%} else {%>
                                                        <td width="33%" align="center">Penerima,</td>
                                                        <%}%>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="75" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="34%" align="center" nowrap> (.................................) 
                                                        </td>
                                                        <td align="center" valign="bottom" width="33%"> (.................................) 
                                                        </td>
                                                        <td width="33%" align="center"> (.................................) 
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
	<%
		}
	%>
    <!-- #EndTemplate --></html>
