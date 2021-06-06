<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

         com.dimata.common.entity.location.PstLocation,

         com.dimata.common.entity.location.Location,

         com.dimata.gui.jsp.ControlList,

         com.dimata.qdep.form.FRMHandler,

         com.dimata.qdep.form.FRMQueryString,

         com.dimata.qdep.entity.I_PstDocType,

         com.dimata.gui.jsp.ControlLine,

         com.dimata.qdep.form.FRMMessage,

         com.dimata.util.Command,

         com.dimata.gui.jsp.ControlDate,

         com.dimata.gui.jsp.ControlCombo,

         com.dimata.posbo.entity.masterdata.Material,

         com.dimata.posbo.entity.masterdata.Unit,

         com.dimata.posbo.entity.masterdata.PstMaterial,

         com.dimata.posbo.form.warehouse.CtrlMatDispatch,

         com.dimata.posbo.form.warehouse.FrmMatDispatch,

         com.dimata.posbo.entity.warehouse.*" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>



<%@ include file = "../../../main/javainit.jsp" %>       

<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
    int appObjCodePayment = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_PAYMENT);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodePayment, AppObjInfo.COMMAND_FINAL));
%>
<%!    public static final String textListGlobal[][] = {
        {"Transfer", "Edit", "Tidak ada item transfer", "Cetak Transfer", "Proses transfer tidak dapat dilakukan pada lokasi yang sama", "Cetak Delivery Order", "Posting Stock", "Posting Harga Beli"},
        {"Dispatch", "Edit", "There is no Dispatch item", "Print Dispatch", "Transfer cant'be proceed in same location", "Print Delivery Order", "Posting Stock", "Posting Harga Beli"}

    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Keterangan", "Nota Supplier", "Waktu", "Etalase Asal", "Etalase Tujuan", "Jenis Dokumen", "Nomor BC", "Tanggal BC"},
                {"Number", "From Location", "Destination", "Date", "Status", "Remark", "Supplier Invoice", "Time", "From Etalase", "Etalase Destination", "Document Type", "Customs Number", "Customs Date"}

            };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama Barang", "Unit", "Qty", "HPP", "Harga Jual", "Total", "Gondola", "Hapus", "Berat Awal", //10
                    "Berat Akhir", "Berat Selisih"}, //12

                {"No", "Code", "Goods Name", "Unit", "Qty", "Avg. Cost", "Price", "Total", "Brand", "Delete", "Last Weight", //10
                    "Current Weight", "Weight Difference"} //12
            };

    public static final String textPosting[][] = {
        {"Anda yakin melakukan Posting Stok ?", "Anda yakin melakukan Posting Harga ?"},
        {"Are You Sure to Posting Stock ? ", "Are You Sure to Posting Cost Price?"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
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
     *
     * this method used to list all po item
     *
     */
    public Vector drawListOrderItem(int language, Vector objectClass, int start, String invoiceNumber,
            int tranUsedPriceHpp, int brandInUse, boolean privManageData, boolean privShowQtyPrice,
            String approot, int typeOfBusinessDetail, int dispatchItemType, String syspropEtalase, String syspropTotal, String syspropHPP, boolean privPayment) {

        Vector list = new Vector(1, 1);

        Vector listError = new Vector(1, 1);

        String result = "";

        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0], "1%");//no
            ctrlist.addHeader(textListOrderItem[language][1], "10%");//sku
            if (brandInUse == 1) {
                if (typeOfBusinessDetail != 2 && syspropEtalase.equals("1")) {
                    ctrlist.addHeader(textListOrderItem[language][8], "10%");//gondola
                }
            }
            ctrlist.addHeader(textListOrderItem[language][2], "15%");//nama barang
            if (typeOfBusinessDetail != 2) {
                ctrlist.addHeader(textListOrderItem[language][3], "5%");//unit
            }
            ctrlist.addHeader(textListOrderItem[language][4], "5%");//qty
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Etalase Awal", "10%");//etalase awal
                ctrlist.addHeader("Etalase Akhir", "10%");//etalase akhir
            }
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader(textListOrderItem[language][10], "5%");//b.awal
                ctrlist.addHeader(textListOrderItem[language][11], "5%");//b.akhir
                ctrlist.addHeader(textListOrderItem[language][12], "5%");//b.selisih
            }
            if (privShowQtyPrice) {
                if (tranUsedPriceHpp == 0) {
                    if (typeOfBusinessDetail == 2) {
                        if (dispatchItemType == Material.MATERIAL_TYPE_EMAS || dispatchItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("HE Awal", "10%");//harga emas awal
                            ctrlist.addHeader("HE Akhir", "10%");//harga emas akhir (hpp)
                        } else if (dispatchItemType == Material.MATERIAL_TYPE_BERLIAN) {
                            ctrlist.addHeader("Harga Beli Awal", "10%");//harga pokok awal
                            ctrlist.addHeader("Harga Beli Akhir", "10%");//harga pokok akhir (hpp)
                        }
                    } else {
                        if (syspropHPP.equals("1")) {
                            ctrlist.addHeader(textListOrderItem[language][5], "10%");//hpp
                        }
                    }
                } else {
                    ctrlist.addHeader(textListOrderItem[language][6], "10%");//harga jual
                }
                if (typeOfBusinessDetail == 2) {
                    ctrlist.addHeader("Oks/Batu Awal", "10%");//ongkos awal
                    ctrlist.addHeader("Oks/Batu Akhir", "10%");//ongkos
                    if (dispatchItemType == Material.MATERIAL_TYPE_EMAS || dispatchItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                        ctrlist.addHeader("Total HP Awal", "10%");//total awal
                        ctrlist.addHeader("Total HP Akhir", "10%");//total
                    } else if (dispatchItemType == Material.MATERIAL_TYPE_BERLIAN) {
                        ctrlist.addHeader("HP Awal", "10%");//total awal
                        ctrlist.addHeader("HP Akhir", "10%");//total
                    }
                } else if (privPayment) {
                    ctrlist.addHeader(textListOrderItem[language][7], "10%");//total
                }
            }
            ctrlist.addHeader(textListOrderItem[language][9], "1%");//hapus

            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }
            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                //added by dewok 2018 for jewelry
                MaterialDetail md = new MaterialDetail();
                Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + "='" + dfItem.getMaterialId() + "'", "");
                Ksg ksg = new Ksg();
                Ksg ksgTo = new Ksg();
                Location locLast = new Location();
                Location locNew = new Location();
                try {
                    ksg = PstKsg.fetchExc(dfItem.getGondolaId());
                    ksgTo = PstKsg.fetchExc(dfItem.getGondolaToId());
                    locLast = PstLocation.fetchExc(ksg.getLocationId());
                    locNew = PstLocation.fetchExc(ksgTo.getLocationId());
                    if (!listMatDetail.isEmpty()) {
                        md = PstMaterialDetail.fetchExc(listMatDetail.get(0).getOID());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (typeOfBusinessDetail == 2) {
                    mat.setName(SessMaterial.setItemNameForLitama(dfItem.getMaterialId()));
                }
                rowx = new Vector();
                start = start + 1;

                rowx.add("" + start + "");
                if (privManageData) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(dfItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add("" + mat.getSku() + "");
                }
                if (brandInUse == 1) {
                    if (typeOfBusinessDetail != 2 && syspropEtalase.equals("1")) {
                        rowx.add("" + mat.getGondolaCode());
                    }
                }
                rowx.add(mat.getName());
                if (typeOfBusinessDetail != 2) {
                    rowx.add(unit.getCode());
                }
                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + dfItem.getOID();
                    int cnt = PstDispatchStockCode.getCount(where);
                    if (cnt < dfItem.getQty()) {
                        if (listError.size() == 0) {
                            listError.add("Silahkan cek :");
                        }
                        listError.add("" + listError.size() + ". Jumlah serial kode stok " + mat.getName() + " tidak sama dengan qty pengiriman");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(dfItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(dfItem.getQty()) + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(dfItem.getQty()) + "</div>");
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add(locLast.getCode() + " - " + ksg.getCode());
                    rowx.add(locNew.getCode() + " - " + ksgTo.getCode());
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", dfItem.getBeratLast()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", dfItem.getBeratCurrent()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", dfItem.getBeratSelisih()) + "</div>");
                }
                if (privShowQtyPrice) {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getHppAwal()) + "</div>");//he.awal
                        rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getHpp()) + "</div>");//he.akhir
                        rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getOngkosAwal()) + "</div>");//ongkos awal
                        rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getOngkos()) + "</div>");//ongkos
                        rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getHppTotalAwal()) + "</div>");//total hp awal
                        if (privPayment) {
                            rowx.add("<div align=\"right\">" + String.format("%,.2f", dfItem.getHppTotal()) + "</div>");//total hp
                        }
                    } else {

                        if (syspropHPP.equals("1")) {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(dfItem.getHpp()) + "</div>");
                        }
                        if (privPayment) {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(dfItem.getHppTotal()) + "</div>");
                        }
                    }
                }
                // add by fitra 17-05-2014
                if (privManageData) {
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(dfItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
                } else {
                    rowx.add("");
                }
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][2] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }


%>





<!-- Jsp Block -->

<%
    /**
     *
     * get approval status for create document
     *
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();

    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();

    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();

    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);

%>





<%    /**
     *
     * get request data from current form
     *
     */
    int iCommand = FRMQueryString.requestCommand(request);

    int prevCommand = FRMQueryString.requestInt(request, "prev_command");

    int startItem = FRMQueryString.requestInt(request, "start_item");

    int cmdItem = FRMQueryString.requestInt(request, "command_item");

    int appCommand = FRMQueryString.requestInt(request, "approval_command");

    long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");

    long oidRes = FRMQueryString.requestLong(request, "dispatch");

    long oidCashBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");

    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");

    long timemls = System.currentTimeMillis();

    int oidDate = FRMQueryString.requestInt(request, FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]);
    String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE_TRANSFER");
    String syspropHPP = PstSystemProperty.getValueByName("SHOW_HPP_TRANSFER");
    String syspropTotal = PstSystemProperty.getValueByName("SHOW_TOTAL_TRANSFER");

    int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE"));

    /**
     * add opie-eyek 20131205 untuk posting nilai jual
     */
    /*if(iCommand==Command.REPOSTING){
    try{
        //System.out.println(">>> proses posting doc : "+oidReceiveMaterial);
        SessPosting sessPosting = new SessPosting();
        sessPosting.postedDispatchDoc(oidDispatchMaterial);
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}*/
    if (oidReceiveMaterial != 0 && iCommand == Command.EDIT) {

        try {

            String whereItemList = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + oidReceiveMaterial + "'";

            String orderItemList = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];

            Vector listItemRec = PstMatReceiveItem.listWithCurrentHPP(0, 1000, whereItemList, orderItemList);

            if (listItemRec != null && listItemRec.size() > 0) {

                for (int i = 0; i <= listItemRec.size(); i++) {

                    MatReceiveItem matRI = (MatReceiveItem) listItemRec.get(i);
                    if (matRI != null) {
                        MatDispatchItem matDfi = new MatDispatchItem();
                        matDfi.setDispatchMaterialId(oidDispatchMaterial);

                        matDfi.setMaterialId(matRI.getMaterialId());

                        matDfi.setUnitId(matRI.getUnitId());

                        matDfi.setHpp(matRI.getCost());
                        try {
                            PstMatDispatchItem.insertExc(matDfi);

                        } catch (Exception e) {
                        }
                    }
                }
            }

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    /**
     *
     * initialization of some identifier
     *
     */
    String errMsg = "";

    int iErrCode = FRMMessage.ERR_NONE;

    /**
     *
     * dispatch code and title
     *
     */
    String dfCode = "";//i_pstDocType.getDocCode(docType);

    String dfTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);

    String dfItemTitle = dfTitle + " Item";

    /**
     *
     * action process
     *
     */
    ControlLine ctrLine = new ControlLine();

    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

//by dyas
//tambah userName dan userId
    iErrCode = ctrlMatDispatch.action(iCommand, oidDispatchMaterial, userName, userId);

    FrmMatDispatch frmdf = ctrlMatDispatch.getForm();

    MatDispatch df = ctrlMatDispatch.getMatDispatch();

    errMsg = ctrlMatDispatch.getMessage();

    /**
     * add opie-eyek 20131205 untuk posting stock
     */
    if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
        try {
            //System.out.println(">>> proses posting qty penerimaan tanpa PO : "+oidReceiveMaterial);
            SessPosting sessPosting = new SessPosting();
            sessPosting.postedDispatchDoc(oidDispatchMaterial);
            //rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            df.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            iCommand = Command.EDIT;
        } catch (Exception e) {
            iCommand = Command.EDIT;
        }
    }

//double curr = PstCurrencyRate.getLastCurrency();
    String priceCode = "Rp.";

//add opie, melakukan pengecekan apakah lokasi tujuan di assign ke user
    boolean locationAssign = false;
    boolean privManageData = true;
    if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
    }

    /**
     * list purchase order item
     */
    oidDispatchMaterial = df.getOID();
    oidCashBillMain = df.getCashBillMainId();

    int recordToGetItem = 10;

    int vectSizeItem = PstMatDispatchItem.getCount(PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " = " + oidDispatchMaterial);
    String order = " DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
    if (typeOfBusinessDetail == 2) {
        order = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }
    Vector listMatDispatchItem = PstMatDispatchItem.list(startItem, recordToGetItem, oidDispatchMaterial, order);

    Vector list = drawListOrderItem(SESS_LANGUAGE, listMatDispatchItem, startItem, df.getInvoiceSupplier(), tranUsedPriceHpp, brandInUse, privManageData, privShowQtyPrice, approot, typeOfBusinessDetail, df.getDispatchItemType(), syspropEtalase, syspropTotal, syspropHPP, privPayment);
    Vector listError = (Vector) list.get(1);

// add by fitra 17-05-2014
    if (iCommand == Command.DELETE && iErrCode == 0) {

%>

<jsp:forward page="srcdf_stock_wh_material.jsp">

    <jsp:param name="command" value="<%=Command.FIRST%>"/>

</jsp:forward>

<%

    }

    double total = PstMatDispatchItem.getTotalTransfer(oidDispatchMaterial);
    double totalQty = PstMatDispatchItem.getTotalQtyTransfer(oidDispatchMaterial);

%>

<!-- End of Jsp Block -->



<html>

    <head>

        <title>Dimata - ProChain POS</title>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>

        <script language="JavaScript">

<!--

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

            function cmdEdit(oid) {

                document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

                document.frm_matdispatch.submit();

            }



            function gostock(oid) {

                document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

                document.frm_matdispatch.hidden_dispatch_item_id.value = oid;

                document.frm_matdispatch.hidden_dispatch_id.value = "<%=oidDispatchMaterial%>";

                document.frm_matdispatch.action = "df_stockcode.jsp";

                document.frm_matdispatch.submit();

            }



            function compare() {

                var dt = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_dy.value;
                var mn = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_mn.value;
                var yy = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_yr.value;
                var dt = new Date(yy, mn - 1, dt);
                var bool = new Boolean(compareDate(dt));
                return bool;

            }




            function cmdSave() {
            <%
                if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                var transfer_from = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID]%>.value;
                var transfer_to = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO]%>.value;
                //alert(transfer_from);
                //alert(transfer_to);
                if (transfer_from != transfer_to) {
                    var statusDoc = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>.value;
                    if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                        var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                        if (conf) {
                            document.frm_matdispatch.command.value = "<%=Command.SAVE%>";
                            document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                            document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                            if (compare() == true) {
                                document.frm_matdispatch.submit();
                            }
                        }
                    } else {
                        document.frm_matdispatch.command.value = "<%=Command.SAVE%>";
                        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                        document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                        if (compare() == true) {
                            document.frm_matdispatch.submit();
                        }
                    }
                } else {
            <%if (typeOfBusinessDetail == 2) {%>
                    var statusDoc = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>.value;
                    if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                        var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                        if (conf) {
                            document.frm_matdispatch.command.value = "<%=Command.SAVE%>";
                            document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                            document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                            if (compare() == true) {
                                document.frm_matdispatch.submit();
                            }
                        }
                    } else {
                        document.frm_matdispatch.command.value = "<%=Command.SAVE%>";
                        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                        document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                        if (compare() == true) {
                            document.frm_matdispatch.submit();
                        }
                    }
            <%} else {%>
                    alert("<%=textListGlobal[SESS_LANGUAGE][4]%>")
            <%}%>
                }
            <%

            } else {

            %>

                alert("Document has been posted !!!");

            <%        }

            %>

            }

            function cmdAsk(oid) {

            <%        if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {

            %>

                document.frm_matdispatch.command.value = "<%=Command.ASK%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

                document.frm_matdispatch.submit();

            <%

            } else {

            %>

                alert("Document has been posted !!!");

            <%        }

            %>

            }



            function cmdCancel() {

                document.frm_matdispatch.command.value = "<%=Command.CANCEL%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

                document.frm_matdispatch.submit();

            }

            function cmdDelete(oid) {

                document.frm_matdispatch.command.value = "<%=Command.DELETE%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.approval_command.value = "<%=Command.DELETE%>";

                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

                document.frm_matdispatch.submit();

            }


            function cmdConfirmDelete(oid) {

                document.frm_matdispatch.command.value = "<%=Command.DELETE%>";

                document.frm_matdispatch.hidden_dispatch_item_id.value = oid;

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.approval_command.value = "<%=Command.DELETE%>";

                document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                document.frm_matdispatch.submit();

            }
// add by fitra 17-05-2014
            function cmdNewDelete(oid) {
                var msg;
                msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
                var agree = confirm(msg);
                if (agree)
                    return cmdConfirmDelete(oid);
                else
                    return cmdEdit(oid);
            }


            function cmdBack() {

                document.frm_matdispatch.command.value = "<%=Command.FIRST%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";

                document.frm_matdispatch.submit();

            }



            function printForm()
            {
            <%if (typeOfBusinessDetail == 2) {%>
                var typePrint = document.frm_matdispatch.type_print_tranfer.value;
                window.open("print_out_df_stock_wh_material_item.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&type_print_tranfer=" + typePrint, "printout_transfer");
            <%} else {%>
                var typePrint = document.frm_matdispatch.type_print_tranfer.value;
                window.open("df_stock_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer=" + typePrint + "&timemls=<%=timemls%>", "pireport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            <%}%>
            }

            function printDeliveryOrder()
            {
                var typePrint = document.frm_matdispatch.type_print_tranfer.value;
                window.open("../../../cashierweb/df_stock_order_print.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer=" + typePrint + "&timemls=<%=timemls%>&hidden_bill_main_id=<%=oidCashBillMain%>", "pireport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function findInvoice()

            {

                window.open("df_wh_material_receive.jsp", "invoice_supplier", "scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");

            }
            function printFormPDF() {
                window.open("<%=approot%>/DispatchStoreRequest?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&hidden_dispatch_id=<%= oidDispatchMaterial%>");
                    }


//add opie-eyek 20131205 untuk posting
                    function PostingStock() {
                        var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                        if (conf) {
                            document.frm_matdispatch.command.value = "<%=Command.POSTING%>";
                            document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                            document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                            document.frm_matdispatch.submit();
                        }
                    }

//add opie-eyek 20131205 untuk posting
                    function PostingCostPrice() {
                        var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                        if (conf) {
                            document.frm_matdispatch.command.value = "<%=Command.REPOSTING%>";
                            document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";
                            document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                            document.frm_matdispatch.submit();
                        }
                    }

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
                    function addItem()

                    {

            <%

                if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {

            %>

                        document.frm_matdispatch.command.value = "<%=Command.ADD%>";

                        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                        if (compareDateForAdd() == true)
                            document.frm_matdispatch.submit();

            <%

            } else {

            %>

                        alert("Document has been posted !!!");

            <%        }

            %>

                    }


                    function addItemReceive()

                    {

            <%        if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {

            %>

                        document.frm_matdispatch.command.value = "<%=Command.ADD%>";

                        document.frm_matdispatch.action = "df_stock_material_receive_search.jsp";

                        if (compareDateForAdd() == true)
                            document.frm_matdispatch.submit();

            <%

            } else {

            %>

                        alert("Document has been posted !!!");

            <%        }

            %>

                    }



                    function editItem(oid)

                    {

            <%        if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {

            %>

                        document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

                        document.frm_matdispatch.hidden_dispatch_item_id.value = oid;

                        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                        document.frm_matdispatch.submit();

            <%

            } else {

            %>

                        alert("Document has been posted !!!");

            <%        }

            %>

                    }



                    function itemList(comm) {

                        document.frm_matdispatch.command.value = comm;

                        document.frm_matdispatch.prev_command.value = comm;

                        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                        document.frm_matdispatch.submit();

                    }


                    /**
                     * by Dyas
                     * penambahan function viewHistoryTable() untuk menampilkan halaman logHistory
                     */
                    function viewHistoryTable() {
                        var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                                "&oidDocHistory=<%=oidDispatchMaterial%>";
                        window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }


                    function MM_swapImgRestore() { //v3.0

                        var i, x, a = document.MM_sr;
                        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc

                                ; i++)
                            x.src = x.oSrc;

                    }



                    function MM_preloadImages() { //v3.0

                        var d = document;
                        if (d.images) {
                            if (!d.MM_p)
                                d.MM_p = new Array();

                            var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                            for (i = 0; i < a.length; i++)
                                if (a[i].indexOf("#") != 0) {
                                    d.MM_p[j] = new Image;
                                    d.MM_p[j++].src = a[i];
                                }
                        }

                    }



                    function MM_swapImage() { //v3.0

                        var i, j = 0, x, a = MM_swapImage.arguments;
                        document.MM_sr = new Array;
                        for (i = 0; i < (a.length - 2); i += 3)
                            if ((x = MM_findObj(a[i])) != null) {
                                document.MM_sr[j++] = x;
                                if (!x.oSrc)
                                    x.oSrc = x.src;
                                x.src = a[i + 2];
                            }

                    }



                    function MM_findObj(n, d) { //v4.01

                        var p, i, x;
                        if (!d)
                            d = document;
                        if ((p = n.indexOf("?")) > 0 && parent.frames.length) {

                            d = parent.frames[n.substring(p + 1)].document;
                            n = n.substring(0, p);
                        }

                        if (!(x = d[n]) && d.all)
                            x = d.all[n];
                        for (i = 0; !x && i < d.forms.length; i++)
                            x = d.forms[i][n];

                        for (i = 0; !x && d.layers && i < d.layers.length; i++)
                            x = MM_findObj(n, d.layers[i].document);

                        if (!x && d.getElementById)
                            x = d.getElementById(n);
                        return x;

                    }

//-->

</script>
        <style>
            .listgentitle {
                font-size: 12px !important;
                font-style: normal;
                font-weight: bold;
                color: #FFFFFF;
                background-color: #3e85c3 !important;
                text-align: center;
                height: 25px !important;
                border: 1px solid !important;
            }
            table.listgen {
                margin: auto 1%;
                text-align: center;
                width: 96.5% !important;
            }

            .listgensell {
                color: #000000;
                background-color: #ffffff !important;
                border: 1px solid #3e85c3;
            }


            .btn {
                padding: 5px 8px;
                font-size: 12px;
                line-height: 1.5;
                border-radius: 3px;
            }
            .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
                cursor: not-allowed;
                background-color: #fff !important;
                opacity: 1;
            }
            select {
                height: 30px !important;
                border: 1px solid #ccc !important;
                width: 100%;
            }
            textarea.formElemen {
                min-height: 80px;
                border-radius: 0px;
                width: 100%;
            }
            .row {
                margin-bottom: 10px;
            }
            label.control-label.col-sm-4.padd {
                padding: 10px 0px;
            }
            label.control-label.col-sm-4.paddd {
                padding: 0px;
            }
            .col-sm-8 {
                margin-bottom: 10px;
            }
            label.control-label.col-sm-4 {
                padding-top: 10px;
            }
            select.tanggal {
                width: 30.9%;
            }

            .form-control {
                font-size: 12px
            }
            select.waktu {
                width: 30.2%;
            }

            li.fa.fa-trash {
                color: #ff0700;
                width: 25px;
                height: 25px;
                font-size: 18px;
            }
            .btn {
                padding: 6px 8px !important;
                font-size: 12px;
                line-height: 1.5;
                border-radius: 3px;
                height: 30px;
            }
            a.btn.btn-success {
                margin-left: 10px;
            }
            td.pull-right {
                margin-right: 12%;
            }
            a.btn.btn-warning {
                margin-top: 15px;
                height: 30px;
            }
            label.history-view {
                margin-right: 2.8%;
                margin-top: 1%;
            }
</style>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <%if (menuUsed == MENU_ICON) {%>
            <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
            <%}%>
            <link rel="stylesheet" href="../../../styles/main.css" type="text/css">

            <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

        </head>

        <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>', '<%=approot%>')">
            <section class="content-header">
                <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
                    <small><%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
                <ol class="ol">
                    <li>
                        <a>
                            <li class="active">Transfer</li>
                        </a>
                    </li>
                </ol>
            </section>
            <p class="border"></p>
            <div class="container-pos">  

                <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

                    <%if (menuUsed == MENU_PER_TRANS) {%>
                    <tr><td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td></tr>
                    <tr><td height="20" ID="MAINMENU"> <%@ include file = "../../../main/mnmain.jsp" %></td></tr>
                    <%} else {%>
                    <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %></td></tr>
                        <%}%>
                    <tr>
                        <td width="88%" valign="top" align="left">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td>
                                        <form name="frm_matdispatch" method="POST" action="">
                                            <%
                                                try {
                                            %>
                                            <input type="hidden" name="command" value="">
                                            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                            <input type="hidden" name="start_item" value="<%=startItem%>">
                                            <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                            <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                            <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
                                            <input type="hidden" name="hidden_bill_main_id" value="<%=oidCashBillMain%>">
                                            <input type="hidden" name="dispatch" value="<%=oidRes%>">
                                            <input type="hidden" name="hidden_dispatch_item_id" value="">
                                            <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE%>">
                                            <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%=df.getDispatchCode()%>">
                                            <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE%>">
                                            <input type="hidden" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="<%=oidDate%>">
                                            <table width="100%" border="0">
                                                <tr>
                                                    <td colspan="3">
                                                        <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <!-- left side -->
                                                                    <div class="col-sm-4">
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                                                                            <div class="col-sm-8">
                                                                                <input id="selectNomor" class="form-control" type="text" name="" value="<%
                                                                                    if (df.getDispatchCode() != "" && iErrCode == 0) {
                                                                                        out.println(df.getDispatchCode());
                                                                                    } else {
                                                                                        out.println("Nomor Otomatis");
                                                                                    }
                                                                                       %>" readonly>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                                                            <div class="col-sm-8">
                                                                                <%

                                                                                    Vector obj_locationid = new Vector(1, 1);

                                                                                    Vector val_locationid = new Vector(1, 1);

                                                                                    Vector key_locationid = new Vector(1, 1);

                                                                                    //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                                                    //add opie-eyek
                                                                                    //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                                                    String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                                                            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                                                                    whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                                                    Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                                                                    //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                                                    for (int d = 0; d < vt_loc.size(); d++) {

                                                                                        Location loc = (Location) vt_loc.get(d);

                                                                                        val_locationid.add("" + loc.getOID() + "");

                                                                                        key_locationid.add(loc.getName());

                                                                                    }

                                                                                    String select_locationid = "" + df.getLocationId(); //selected on combo box
%>
                                                                                <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>

                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                                                                            <div class="col-sm-8">

                                                                                <%

                                                                                    Vector obj_locationid1 = new Vector(1, 1);

                                                                                    Vector val_locationid1 = new Vector(1, 1);

                                                                                    Vector key_locationid1 = new Vector(1, 1);

                                                                                    String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;

                                                                                    String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);

                                                                                    //Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
                                                                                    for (int d = 0; d < vt_loc.size(); d++) {

                                                                                        Location loc1 = (Location) vt_loc.get(d);

                                                                                        val_locationid1.add("" + loc1.getOID() + "");

                                                                                        key_locationid1.add(loc1.getName());

                                                                                    }

                                                                                    String select_locationid1 = "" + df.getDispatchTo(); //selected on combo box

                                                                                %>

                                                                                <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>

                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="status"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label>
                                                                            <div class="col-sm-8">
                                                                                <%

                                                                                    Vector obj_status = new Vector(1, 1);

                                                                                    Vector val_status = new Vector(1, 1);

                                                                                    Vector key_status = new Vector(1, 1);

                                                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));

                                                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                                                    //add by fitra
                                                                                    if (listMatDispatchItem.size() > 0) {
                                                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                                                    }

                                                                                    // update opie-eyek 19022013
                                                                                    // user bisa memfinalkan DF tidak bisa mengubah status dari final ke draft jika  jika  :
                                                                                    // 1. punya priv. transfer approve = true
                                                                                    // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                                                                    locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", df.getLocationId());
                                                                                    if (listMatDispatchItem.size() > 0 && privApproval == true && locationAssign == true && listError.size() == 0) {
                                                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                                    }

                                                                                    String select_status = "" + df.getDispatchStatus();

                                                                                    if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {

                                                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                                                                                    } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {

                                                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);

                                                                                    } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval == false || locationAssign == false)) {

                                                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);

                                                                                    } else {

                                                                                %>

                                                                                <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS], null, select_status, val_status, key_status, "", "formElemen")%>

                                                                                <%}%>

                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <!-- Center side -->             
                                                                    <div class="col-sm-4">
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label>
                                                                            <div class="col-sm-8">
                                                                                <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), 1, -5, "tanggal", "")%>
                                                                            </div>
                                                                        </div>
                                                                        <%if (dutyFree == 1) {%>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4" for="tanggalBC"><%= textListOrderHeader[SESS_LANGUAGE][12]%></label>
                                                                            <div class="col-sm-8">
                                                                                <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_TANGGAL_BC], (df.getTanggalBC() == null) ? new Date() : df.getTanggalBC(), 1, -5, "tanggal", "")%>
                                                                            </div>
                                                                        </div>
                                                                        <%}%>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4" for="waktu"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label>
                                                                            <div class="col-sm-8">
                                                                                <%=ControlDate.drawTimeSec(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), "waktu")%>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <!--Right Side-->
                                                                    <div class="col-sm-4">                                                                                                                                                                   
                                                                        <%if (dutyFree == 1) {%>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="jenisDokumen"><%= textListOrderHeader[SESS_LANGUAGE][10]%></label>
                                                                            <div class="col-sm-8">
                                                                                <input type="text" id="jenisDokumen" class="form-control" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_JENIS_DOKUMEN]%>" value="PPBTBB" readonly>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="nomorBC"><%= textListOrderHeader[SESS_LANGUAGE][11]%></label>
                                                                            <div class="col-sm-8">
                                                                                <input type="text" class="form-control"  id="nomorBC" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]%>" value="<%=df.getNomorBeaCukai()%>">
                                                                            </div>
                                                                        </div>
                                                                        <%}%>
                                                                        <div class="form-group">
                                                                            <label class="control-label col-sm-4 padd" for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label>
                                                                            <div class="col-sm-8">
                                                                                <textarea id="textarea" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=df.getRemark()%></textarea>
                                                                            </div>
                                                                        </div> 
                                                                    </div> 
                                                                </div>
                                                            </div>

                                                            <tr>
                                                                <%if (typeOfBusinessDetail == 2) {%>
                                                                <td width="5%">Tipe Item Transfer</td>
                                                                <td width="1%">:</td>
                                                                <td width="20%">
                                                                    <%
                                                                        Vector val_itemType = new Vector(1, 1);
                                                                        Vector key_itemType = new Vector(1, 1);
                                                                        for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                                                            if (main_material == Material.MATERIAL_TYPE_GENERAL) {
                                                                                continue;
                                                                            }
                                                                            key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[main_material]);
                                                                            val_itemType.add("" + main_material);
                                                                        }
                                                                    %>
                                                                    <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE], null, "" + df.getDispatchItemType(), val_itemType, key_itemType, "", "formElemen")%>
                                                                </td>
                                                                <%}%>
                                                            </tr>
                                                            <tr>
                                                                <%if (typeOfBusinessDetail == 2) {%>
                                                                <td width="5%">Tipe Item Transfer</td>
                                                                <td width="1%">:</td>
                                                                <td width="20%">
                                                                    <%
                                                                        Vector val_itemType = new Vector(1, 1);
                                                                        Vector key_itemType = new Vector(1, 1);
                                                                        for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                                                            if (main_material == Material.MATERIAL_TYPE_GENERAL) {
                                                                                continue;
                                                                            }
                                                                            key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[main_material]);
                                                                            val_itemType.add("" + main_material);
                                                                        }
                                                                    %>
                                                                    <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE], null, "" + df.getDispatchItemType(), val_itemType, key_itemType, "", "formElemen")%>
                                                                </td>
                                                                <%}%>
                                                            </tr>
                                                        </table>

                                                    </td>

                                                </tr>

                                                <tr>

                                                    <td colspan="3">
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                            <tr align="left" valign="top">
                                                                <td height="22" valign="middle" colspan="3">
                                                                    <%=list.get(0)%>
                                                                </td>
                                                            </tr>
                                                            <%if (oidDispatchMaterial != 0) {%>
                                                            <tr align="left" valign="top">
                                                                <td height="8" align="left" colspan="3" class="command">
                                                                    <span class="command">
                                                                        <%
                                                                            if (cmdItem != Command.FIRST && cmdItem != Command.PREV && cmdItem != Command.NEXT && cmdItem != Command.LAST) {
                                                                                cmdItem = Command.FIRST;
                                                                            }
                                                                            ctrLine.setLocationImg(approot + "/images");
                                                                            ctrLine.initDefault();
                                                                            ctrLine.setImageListName(approot + "/images", "item");
                                                                            ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "')");
                                                                            ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "')");
                                                                            ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "')");
                                                                            ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "')");
                                                                        %>
                                                                        <%=ctrLine.drawImageListLimit(cmdItem, vectSizeItem, startItem, recordToGetItem)%> </span> </td>
                                                            </tr>
                                                            <% if (privShowQtyPrice && syspropHPP.equals("1")) {%>
                                                            <tr align="left" valign="top">
                                                                <td>
                                                                    <div class="form-group pull-right" style="margin-right: 1%;">
                                                                        <label class="control-label col-sm-4 padd" for="total">Total Hpp</label>
                                                                        <div class="col-sm-8">
                                                                            <input type="text" id="total" class="form-control"  value="<%=FRMHandler.userFormatStringDecimal(total)%>" readonly>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                                <td height="22" colspan="3" valign="middle" class="errfont">
                                                                    <%
                                                                        for (int k = 0; k < listError.size(); k++) {
                                                                            if (k == 0) {
                                                                                out.println(listError.get(k) + "<br>");
                                                                            } else {
                                                                                out.println("&nbsp;&nbsp;&nbsp;" + listError.get(k) + "<br>");
                                                                            }
                                                                        }
                                                                    %>
                                                                </td>
                                                            </tr>
                                                            <%}%>
                                                            <tr align="left" valign="top">
                                                                <td>
                                                                    <div class="form-group pull-right" style="margin-right: 1%;">
                                                                        <label class="control-label col-sm-4 padd" for="total">Total</label>
                                                                        <div class="col-sm-8">
                                                                            <input type="text" id="total" class="form-control"  value="<%=FRMHandler.userFormatStringDecimal(totalQty)%>" readonly>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr align="left" valign="top">
                                                                <td height="22" valign="middle" colspan="3">
                                                                    <%
                                                                        if (privAdd == true) {
                                                                    %>
                                                                    <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                        <tr>
                                                                            <% if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                            <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                            <td width="15%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                                <%if (!typeOfBusiness.equals("3")) {%>
                                                                            <td width="6%"><a href="javascript:addItemReceive()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                            <td width="30%"><a href="javascript:addItemReceive()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item Receive", ctrLine.CMD_ADD, true)%></a></td>
                                                                                <%}%>
                                                                                <% } %>
                                                                        </tr>
                                                                    </table>
                                                                    <%}%>
                                                                </td>
                                                            </tr>
                                                            <%}%>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3">
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                            <tr>
                                                                <td width="77%" rowspan="2">
                                                                    <%
                                                                        ctrLine.setLocationImg(approot + "/images");
                                                                        // set image alternative caption
                                                                        ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));
                                                                        ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");
                                                                        ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));
                                                                        ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));
                                                                        ctrLine.initDefault();
                                                                        ctrLine.setTableWidth("80%");
                                                                        String scomDel = "javascript:cmdAsk('" + oidDispatchMaterial + "')";
                                                                        String sconDelCom = "javascript:cmdDelete('" + oidDispatchMaterial + "')";
                                                                        String scancel = "javascript:cmdEdit('" + oidDispatchMaterial + "')";
                                                                        ctrLine.setCommandStyle("command");
                                                                        ctrLine.setColCommStyle("command");
                                                                        // set command caption
                                                                        ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));
                                                                        ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");
                                                                        ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));
                                                                        ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_DELETE, true));
                                                                        ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));
                                                                        if (privDelete) {
                                                                            ctrLine.setConfirmDelCommand(sconDelCom);
                                                                            ctrLine.setDeleteCommand(scomDel);
                                                                            ctrLine.setEditCommand(scancel);
                                                                        } else {
                                                                            ctrLine.setConfirmDelCaption("");
                                                                            ctrLine.setDeleteCaption("");
                                                                            ctrLine.setEditCaption("");
                                                                        }
                                                                        if ((privAdd == false && privUpdate == false) || df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED || df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                                            ctrLine.setSaveCaption("");
                                                                        }
                                                                        if (privAdd == false || df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                                            ctrLine.setAddCaption("");
                                                                        }
                                                                        if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                                            ctrLine.setDeleteCaption("");
                                                                        }
                                                                        if (iCommand == Command.SAVE && frmdf.errorSize() == 0) {
                                                                            // iCommand=Command.EDIT;
                                                                        }
                                                                    %>
                                                                    <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%> </td>
                                                                    <% if (privShowQtyPrice) {%>
                                                                <td class="pull-right">
                                                                    <select name="type_print_tranfer" class="type_print">
                                                                        <option value="0">Price Cost</option>
                                                                        <option value="1">Sell Price</option>
                                                                        <option value="2">No Price</option>
                                                                    </select>
                                                                </td>
                                                                <%} else {%>
                                                            <input type="hidden" name="type_print_tranfer" value="2">
                                                            <%}%>
                                                            </tr>
                                                            <tr>
                                                                <td class="pull-right">
                                                                    <%if (listMatDispatchItem != null && listMatDispatchItem.size() > 0) {%>

                                                                    <% if (useForRaditya.equals("1")) {
                                                                            if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {%>
                                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                        <tr>
                                                                            <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][3]%></a></td>
                                                                            <td width="5%" valign="top"><a href="javascript:printFormPDF('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> Print PDF</a></td>
                                                                                        <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                                                                            <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidDispatchMaterial%>')" class="btn btn-danger command"><li class="fa fa-upload"></li> <%=textListGlobal[SESS_LANGUAGE][6]%> </a></td>
                                                                                        <%}%>
                                                                        </tr>
                                                                    </table>
                                                                    <%} else {
                                                                        if (privShowQtyPrice && typeOfBusiness == "3") {%>
                                                                <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidDispatchMaterial%>')" class="btn btn-danger command"><li class="fa fa-upload"></li> <%=textListGlobal[SESS_LANGUAGE][6]%> </a></td>
                                                                            <%}
                                                                                }
                                                                            } else {%>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <% if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                    <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][3]%></a></td>
                                                                    <td width="5%" valign="top"><a href="javascript:printFormPDF('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> Print PDF</a></td>
                                                                                <%} else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {%>
                                                                    <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][3]%></a></td>
                                                                    <td width="5%" valign="top"><a href="javascript:printFormPDF('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> Print PDF</a></td>
                                                                                <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                                                                    <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidDispatchMaterial%>')" class="btn btn-danger command"><li class="fa fa-upload"></li> <%=textListGlobal[SESS_LANGUAGE][6]%> </a></td>
                                                                                <%}%>
                                                                                <%} else {%>
                                                                    <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][3]%></a></td>
                                                                    <td width="5%" valign="top"><a href="javascript:printFormPDF('<%=oidDispatchMaterial%>')" class="btn btn-warning"><li class="fa fa-print"></li> Print PDF</a></td>
                                                                                <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                                                                    <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidDispatchMaterial%>')" class="btn btn-danger command"><li class="fa fa-upload"></li> <%=textListGlobal[SESS_LANGUAGE][6]%> </a></td>
                                                                                <%}%>
                                                                                <%}%>
                                                                </tr>
                                                            </table>
                                                            <%}%>
                                                            <%}%>                        </td>
                                                </tr>
                                            </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="history"> <label class="history-view pull-right"> <a href="javascript:viewHistoryTable()" class="btn btn-info">VIEW TABEL HISTORY</a></label></td>
                                </tr>

                            </table>

                            <%

                                } catch (Exception e) {

                                    System.out.println(e);

                                }

                            %>

                            </form>

                        </td>

                    </tr>

                </table>

            </td>

        </tr>

        <tr>

            <td colspan="2" height="20">
                <%if (menuUsed == MENU_ICON) {%>
                <%@include file="../../../styletemplate/footer.jsp" %>
                <%} else {%>
                <%@ include file = "../../../main/footer.jsp" %>
                <%}%>

            </td>


        <script language="JavaScript">
            <%

                // add by fitra 10-5-2014s
                if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE) {%>
            addItem();
            <% }%>
        </script>

    </tr>

</table>
</div>
</body>

</html>









