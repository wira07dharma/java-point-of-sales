<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesOrder"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import="com.dimata.posbo.session.masterdata.SessPosting"%>

<%@ include file = "../../../main/javainit.jsp" %>
<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
//boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!    static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    static boolean bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Toko/Gudang", "Pencarian", "Daftar", "Edit", "Tidak ada data penerimaan...",
            "Cetak Penerimaan Barang", "Posting Penerimaan", "Lokasi penerimaan tidak boleh sama dengan lokasi asal barang", "Posting Stock", "Posting Harga Beli", "Cetak dengan PDF"},
        {"Receive", "From Store/Warehouse", "Search", "List", "Edit", "Receive", "No receive data available...",
            "Print Goods Receive", "Posting Receive", "Receive destination can't be equals with receive source", "Posting Stock", "Posting Harga Beli", "Generate PDF"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Tanggal", "Penerimaan Dari", "Status", "Keterangan", "Nomor Kirim", "Waktu","Etalase Awal","Etalase", "Jenis Dokumen", "Nomor BC", "Tanggal BC"},//9
        {"Number", "Location", "Date", "Receive From", "Status", "Remark", "Dispatch Number", "Time","Etalase Awal","Etalase", "Document Type", "Customs Number", "Customs Date"},
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Kadaluarsa", "Unit", "HPP", "Harga Jual", "Mata Uang", "Qty", "Total", " Hapus",//10
        "Berat"},//11
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Sell Price", "Currency", "Qty", "Total", "Delete",//10
        "Weight"}//11
    };

    public static final String textListTitleHeader[][] = {
        {"Toko", "Terima Barang > Dari Gudang", "Terima Barang", "Tidak ada data penerimaan ..", "Penerimaan Barang"},
        {"Store", "Goods Receive > from Warehouse", "Goods Receive", "No receive data available ..", "Goods Receive"}
    };

    public static final String textPosting[][] = {
        {"Anda yakin melakukan Posting Stok ?", "Anda yakin melakukan Posting Harga ?"},
        {"Are You Sure to Posting Stock ? ", "Are You Sure to Posting Cost Price?"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    /**
     * this method used to list all po item
     */
    public Vector drawListRecItem(int language, Vector objectClass, int start, boolean privManageData, int tranUsedPriceHpp, boolean privShowQtyPrice, String approot, int typeOfBusinessDetail, int recItemType) {
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
            ctrlist.addHeader(textListOrderItem[language][0], "1%");
            ctrlist.addHeader(textListOrderItem[language][1], "15%");
            ctrlist.addHeader(textListOrderItem[language][2], "20%");
            if (bEnableExpiredDate) {
                ctrlist.addHeader(textListOrderItem[language][3], "10%");
            }
            if(typeOfBusinessDetail != 2){
                ctrlist.addHeader(textListOrderItem[language][4], "5%");//unit
            }
            ctrlist.addHeader(textListOrderItem[language][8], "5%");//qty
            if(typeOfBusinessDetail == 2){
                ctrlist.addHeader(textListOrderItem[language][11], "5%");//berat
                ctrlist.addHeader("Ongkos/Batu", "5%");//ongkos
            }
            if (privShowQtyPrice) {
                if (tranUsedPriceHpp == 0) {
                    if(typeOfBusinessDetail == 2){
                        if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Harga Emas", "10%");//he
                        } else if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                            ctrlist.addHeader("Harga Beli", "10%");//harga beli
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][5], "10%");//hpp
                    }
                } else {
                    ctrlist.addHeader(textListOrderItem[language][6], "10%");//harga jual
                }
            }            
            if (privShowQtyPrice) {
                if(typeOfBusinessDetail == 2){
                    ctrlist.addHeader("Total HP", "15%");//total hp
                } else {
                    ctrlist.addHeader(textListOrderItem[language][9], "10%");//total
                }
            }
            ctrlist.addHeader(textListOrderItem[language][10], "1%");
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
                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                rowx = new Vector();
                start = start + 1;
                
                //added by dewok 2018-02-11
                String itemName = SessMaterial.setItemNameForLitama(recItem.getMaterialId());

                rowx.add("" + start + "");
                if (privManageData) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }
                if(typeOfBusinessDetail == 2){
                    rowx.add(itemName);
                } else {
                    rowx.add(mat.getName());
                }
                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }
                if(typeOfBusinessDetail != 2){
                    rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                }

                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItem.getOID();
                    int cnt = PstReceiveStockCode.getCount(where);
                    if (cnt < recItem.getQty()) {
                        if (listError.size() == 0) {
                            listError.add("Please Check :");
                        }
                        listError.add("" + listError.size() + ". jumlah Serial kode stok barang '" + mat.getName() + "' tidak sama dengan jumlah qty penerimaan");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(recItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                } else {
                    if(typeOfBusinessDetail == 2){
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getQty()) + "</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                    }
                    if(typeOfBusinessDetail == 2){
                        rowx.add("<div align=\"right\">" + String.format("%,.3f",recItem.getBerat()) + "</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f",recItem.getForwarderCost()) + ".00</div>");
                    }
                }

                if (privShowQtyPrice) {
                    if(typeOfBusinessDetail == 2){
                        rowx.add("<div align=\"right\">" + String.format("%,.0f",recItem.getCost()) + ".00</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                    }
                }

                if (privShowQtyPrice) {
                    if(typeOfBusinessDetail == 2){
                        rowx.add("<div align=\"right\">" + String.format("%,.0f",recItem.getTotal()) + ".00</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "</div>");
                    }
                }

                // add by fitra 17-05-2014
                if (privManageData) {
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><li class=\"fa fa-trash\"></li></a></div>");
                } else {
                    rowx.add("");
                }

                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][5] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }


%>


<!-- Jsp Block -->
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
// get request data from current form
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");

// initialization of some identifier
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;
	int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE")); 
	boolean showDF = (dutyFree == 1) ? true : false;
// purchasing ret code and title
    String recCode = "";//i_pstDocType.getDocCode(docType);
    String recTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);
    String recItemTitle = recTitle + " Item";

// action process
    ControlLine ctrLine = new ControlLine();
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(iCommand, oidReceiveMaterial, userName, userId);
    FrmMatReceive frmrec = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();
    errMsg = ctrlMatReceive.getMessage();

//proses posting dilakukan jika user, sudah mengubah status menjadi final
    if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
        try {
            System.out.println(">>> proses posting doc : " + oidReceiveMaterial);
            SessPosting sessPosting = new SessPosting();
            //sessPosting.postedReceiveDoc(oidReceiveMaterial);
            boolean isOK = sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
            if (isOK) {
                rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            }            
        } catch (Exception e) {
        }
    }

// generate code of current currency
    String priceCode = "Rp.";

// check if document may modified or not 
    boolean privManageData = true;
    if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
    }

// list purchase order item
    oidReceiveMaterial = rec.getOID();
    int recordToGetItem = 10;
    String whereClauseItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
            //+ " AND RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]
            //+ " = MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID];
    String orderClauseItem = "";
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
    Vector listMatReceiveItem = new Vector();
    if (rec != null && rec.getOID() != 0) {
        listMatReceiveItem = PstMatReceiveItem.list(startItem, recordToGetItem, whereClauseItem);
    }

//Fetch Dispatch Material Info
    MatDispatch df = new MatDispatch();
    try {
        if (rec != null && rec.getOID() != 0) {
            df = PstMatDispatch.fetchExc(rec.getDispatchMaterialId());
        }
    } catch (Exception e) {
    }

//add opie-eyek 20131206
    Vector list = drawListRecItem(SESS_LANGUAGE, listMatReceiveItem, startItem, privManageData, tranUsedPriceHpp, privShowQtyPrice, approot, typeOfBusinessDetail, rec.getReceiveItemType());
    Vector listError = (Vector) list.get(1);

    if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="srcreceive_store_wh_material.jsp"> 
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
    }

    /**
     * get list location
     */
    Vector obj_locationid = new Vector(1, 1);
    Vector val_locationid = new Vector(1, 1);
    Vector key_locationid = new Vector(1, 1);
//Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
//add opie-eyek
//algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
    String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

    whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

    Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
    for (int d = 0; d < vt_loc.size(); d++) {
        Location loc = (Location) vt_loc.get(d);
        val_locationid.add(String.valueOf(loc.getOID()));
        key_locationid.add(loc.getName());
    }
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
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
    
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function cmdEdit(oid) {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                document.frm_recmaterial.submit();
            }

            function posting() {
                var x = confirm("Are you sure do Posting ?");
                if (x) {
                    document.frm_recmaterial.command.value = "<%=Command.SUBMIT%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                    document.frm_recmaterial.submit();
                }
            }

            function gostock(oid) {

            }

            function compare() {
                var dt = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
                var mn = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
                var yy = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
                var dt = new Date(yy, mn - 1, dt);
                var bool = new Boolean(compareDate(dt));
                return bool;
            }

            function cmdSave() {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
            <%if (privManageData) {%>
                document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
            <%} else {%>
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
            <%}%>
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                var noInv = document.frm_recmaterial.txt_dfnumber.value;
                var receive_from = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM]%>.value;
                var receive_to = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value;
                if (noInv != '') {
                    //added by dewok for litama 2018-02-11
                    var litama = ("<%=typeOfBusinessDetail%>" !== "2");                    
                    //
                    if (receive_from == receive_to && litama) {
                        alert('<%=textListGlobal[SESS_LANGUAGE][8]%>');
                    } else {
                        var statusDoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>.value;
                        if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                            var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                            if (conf) {
                                if (compare() == true)
                                    document.frm_recmaterial.submit();
                            }
                        } else {
                            if (compare() == true)
                                document.frm_recmaterial.submit();
                        }
                    }
                } else {
                    alert('No invoice tidak boleh kosong!!!');
                }
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function cmdAsk(oid) {
                document.frm_recmaterial.command.value = "<%=Command.ASK%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdCancel() {
                document.frm_recmaterial.command.value = "<%=Command.CANCEL%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdConfirmDelete(oid) {
                document.frm_recmaterial.command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.hidden_receive_item_id.value = oid;
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                document.frm_recmaterial.submit();
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
                document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "srcreceive_store_wh_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdSelectDF() {
                var strvalue = "dfdosearch.jsp?command=<%=Command.FIRST%>" +
                        "&oidFrom=" + document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM]%>.value +
                        "&oidTo=" + document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value +
                        "&txt_dfnumber=" + document.frm_recmaterial.txt_dfnumber.value;
                window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function keyPressCheck() {
                if (event.keyCode == 13) {
                    //document.all.aSearch.focus();
                    cmdSelectDF();
                }
            }

            function printForm() {
                if ("<%=typeOfBusinessDetail%>" == 2) {
					var typePrint = document.frm_recmaterial.type_print_receive.value;
                    window.open("print_out_receive_material_store.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_receive="+typePrint, "receive_store");
                } else {
                    window.open("receive_store_wh_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>", "receivereport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                }
            }
           function printFormPDF() {
            window.open("<%=approot%>/ReceiptGudang?approot=<%= approot %>&sess_language=<%= SESS_LANGUAGE %>&receive_id=<%= oidReceiveMaterial %>");
          }

            function PostingStock() {
                var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                if (conf) {
                    document.frm_recmaterial.command.value = "<%=Command.POSTING%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                    document.frm_recmaterial.submit();
                }
            }


            function PostingCostPrice() {
                var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                if (conf) {
                    document.frm_recmaterial.command.value = "<%=Command.REPOSTING%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_store_wh_material_edit.jsp";
                    document.frm_recmaterial.submit();
                }
            }

            //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


            //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
            function addItem()
            {
            <%	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value = "<%=Command.ADD%>";
                document.frm_recmaterial.action = "receive_store_wh_materialitem.jsp";
                if (compareDateForAdd() == true)
                    document.frm_recmaterial.submit();
            <%
                } else {
            %>
                alert("Document has been posted !!!");
            <%
                    }
            %>
            }

            function editItem(oid)
            {
            <%	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.hidden_receive_item_id.value = oid;
                document.frm_recmaterial.action = "receive_store_wh_materialitem.jsp";
                document.frm_recmaterial.submit();
            <%
                } else {
            %>
                alert("Document has been posted !!!");
            <%
                    }
            %>
            }
            function gostock(oid) {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.rec_type.value = 0;
                document.frm_recmaterial.type_doc.value = 0;
                document.frm_recmaterial.hidden_receive_item_id.value = oid;
                document.frm_recmaterial.action = "rec_wh_stockcode.jsp";
                document.frm_recmaterial.submit();
            }

            function itemList(comm) {
                document.frm_recmaterial.command.value = comm;
                document.frm_recmaterial.prev_command.value = comm;
                document.frm_recmaterial.action = "receive_store_wh_materialitem.jsp";
                document.frm_recmaterial.submit();
            }
            //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


            //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
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

            function MM_findObj(n, d) { //v4.0
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
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
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

            function viewHistoryTable() {
                var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                        "&oidDocHistory=<%=oidReceiveMaterial%>";
                window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
    width: 96% !important;
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
    margin-right: 5%;
}
a.btn.btn-warning {
    margin-top: 15px;
    height: 30px;
}
label.history-view {
    margin-right: 5%;
    margin-top: 1%;
}
</style>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <SCRIPT language=JavaScript>
            function hideObjectForMarketing() {
            }

            function hideObjectForWarehouse() {
            }

            function hideObjectForProduction() {
            }

            function hideObjectForPurchasing() {
            }

            function hideObjectForAccounting() {
            }

            function hideObjectForHRD() {
            }

            function hideObjectForGallery() {
            }

            function hideObjectForMasterData() {
            }

        </SCRIPT>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <section class="content-header">
        <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
          <small><%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
        <ol class="ol">
          <li>
            <a>
              <li class="active">Receive</li>
            </a>
          </li>
        </ol>
      </section>
      <p class="border"></p>
      <div class="container-pos">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
          <%if (menuUsed == MENU_PER_TRANS) {%>
          <tr><td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td></tr>
          <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td></tr>
            <%} else {%>
          <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"> <%@include file="../../../styletemplate/template_header.jsp" %> </td></tr>
          <%}%>
          <tr> 
            <td valign="top" align="left"> 
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td><!-- #BeginEditable "content" --> 
                    <form name="frm_recmaterial" method="post" action="">
                      <input type="hidden" name="command" value="">
                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                      <input type="hidden" name="start_item" value="<%=startItem%>">
                      <input type="hidden" name="command_item" value="<%=cmdItem%>">
                      <input type="hidden" name="approval_command" value="<%=appCommand%>">			  
                      <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">			  			  
                      <input type="hidden" name="hidden_receive_item_id" value="">
                      <input type="hidden" name="rec_type" value="">
                      <input type="hidden" name="type_doc" value="">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_STORE%>">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_DISPATCH%>">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=rec.getDispatchMaterialId()%>">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]%>" value="<%=rec.getCurrencyId()%>">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" value="0">
                      <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>" value="<%=rec.getReceiveItemType()%>">
                      <table width="100%" border="0">
                        <tr> 
                          <td colspan="3"> 
                            <table width="100%" border="0" cellpadding="1">
                              <div class="row">
                                <div class="col-sm-12">
                                  <!-- left side -->
                                  <div class="col-sm-4">
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                                      <div class="col-sm-8">
                                        <input id="selectNomor" class="form-control" type="text" name="" value="<%=rec.getRecCode()%>" readonly>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="selectKirim"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label>
                                      <div class="col-sm-8">
                                        <%if (listMatReceiveItem.size() <= 0) {%>
                                        <input id="selectKirim" class="form-control" type="text" name="txt_dfnumber" value="<%= df.getDispatchCode()%>" onKeyPress="javascript:keyPressCheck()">
                                        <%} else {%>
                                        <input id="selectKirim" class="form-control" type="text" name="txt_dfnumber" value="<%= df.getDispatchCode()%>" onKeyPress="javascript:keyPressCheck()" readonly>
                                        <%}%>
                                        <%if (listMatReceiveItem.size() == 0) {%><a href="javascript:cmdSelectDF()">CHK</a><%}%>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                      <div class="col-sm-8">
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, String.valueOf(rec.getLocationId()), val_locationid, key_locationid, " tabindex=\"1\"", "formElemen")%>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label>
                                      <div class="col-sm-8">
                                        <%if (listMatReceiveItem.size() <= 0) {%>
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM], null, String.valueOf(rec.getReceiveFrom()), val_locationid, key_locationid, " tabindex=\"2\"", "formElemen")%>
                                        <%} else {
                                            Location location = new Location();
                                            location = PstLocation.fetchExc(rec.getReceiveFrom());
                                            out.println("<input value='" + rec.getReceiveFrom() + "' type='hidden' name ='" + FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM] + "'>");
                                            out.println("<input class='form-control' type='text' readonly value='" + location.getName() + "' >");
                                                            }%>
                                      </div>
                                    </div>
                                  </div>
                                  <!-- Center side -->             
                                  <div class="col-sm-4">
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                                      <div class="col-sm-8">
                                        <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), 1, -5, "tanggal", "")%>
                                      </div>
                                    </div>
                                    <% if (showDF) {%>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="tanggalBC"><%=textListOrderHeader[SESS_LANGUAGE][12]%></label>
                                      <div class="col-sm-8">
                                        <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TANGGAL_BC], (rec.getTglBc() == null) ? new Date() : rec.getReceiveDate(), 1, -5, "tanggal", "")%>
                                      </div>
                                    </div>
                                    <%}%>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="waktu"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label>
                                      <div class="col-sm-8">
                                        <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), "waktu")%>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="status"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label>
                                      <div class="col-sm-8">
                                        <%
                                          Vector obj_status = new Vector(1, 1);
                                          Vector val_status = new Vector(1, 1);
                                          Vector key_status = new Vector(1, 1);

                                          // update opie-eyek 19022013
                                          // user bisa memfinalkan penerimaan  jika  :
                                          // 1. punya priv. transfer approve = true
                                          // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                          //Receive ( penerimaan barang) : LGR= List of Goods Receive
                                          boolean locationAssign = false;
                                          locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", rec.getLocationId());
                                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                          if (listMatReceiveItem.size() > 0) {
                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                          }

                                          if (listMatReceiveItem.size() > 0 && privApproval == true && locationAssign == true && listError.size() == 0) {
                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                          }

                                          String select_status = "" + rec.getReceiveStatus();

                                          if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                          } else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                          } else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval == false || locationAssign == false)) {
                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                          } else {

                                        %>
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS], null, select_status, val_status, key_status, "", "formElemen")%>
                                        <%}%>
                                      </div>
                                    </div>
                                  </div>
                                  <!--Right Side-->
                                  <div class="col-sm-4">                                                                                                                                                                   
                                    <% if (showDF) {%>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label>
                                      <div class="col-sm-8">
                                        <input type="text" id="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>" class="form-control" name="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>" value="PPBTBB" readonly>
                                      </div>
                                    </div>
                                    <%}%>
                                    <% if (showDF) {%>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>"><%=textListOrderHeader[SESS_LANGUAGE][11]%></label>
                                      <div class="col-sm-8">
                                        <input type="text" class="form-control"  id="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>" name="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>" value="<%= rec.getNomorBc()%>">
                                      </div>
                                    </div>
                                    <%}%>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label>
                                      <div class="col-sm-8">
                                        <textarea id="textarea" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=rec.getRemark()%></textarea>
                                      </div>
                                    </div> 
                                  </div> 
                                </div>
                              </div>
                              <%-- if (typeOfBusinessDetail == 2) {%>
                              <td>Tipe Item Penerimaan</td>
                              <td>:</td>
                              <td><%=Material.MATERIAL_TYPE_TITLE[rec.getReceiveItemType()]%></td>
                              <%}--%>
                            </table>
                          </td>
                        </tr>
                        <tr>
                          <td colspan="3">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr align="left" valign="top">
                                <td height="22" valign="middle" colspan="3"><%=list.get(0)%></td>
                              </tr>
                              <%if (oidReceiveMaterial != 0) {%>
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
                              <tr align="left" valign="top"> 
                                <td height="22" valign="middle" colspan="3"> 
                                  <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                                    <tr> 
                                      <td><a href="javascript:addItem()" class="btn btn-success"><li class="fa fa-plus"></li> <%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%></a></td>                        
                                            <% if (privShowQtyPrice) {%>
                                      <td class="pull-right">
                                        <select name="type_print_receive">
                                          <option value="0">Price Cost</option>
                                          <option value="1">Sell Price</option>
                                          <option value="2">No Price</option>
                                        </select>
                                      </td>
                                      <%} else {%>
                                    <input type="hidden" name="type_print_receive" value="2">
                                    <%}%>
                                    <%if (privShowQtyPrice) {%>
                                    <%if (listMatReceiveItem != null && listMatReceiveItem.size() > 0) {%>
                                    <td width="5%">
                                      <div align="right"><%="TOTAL  : " + recCode%></div>
                                    </td>
                                    <td width="5%">
                                      <div align="right"></div>
                                    </td>
                                    <td width="10%">
                                      <div align="left">
                                        <%
                                          String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
                                          out.println(Formater.formatNumber(PstMatReceiveItem.getTotal(whereItem), "##,###.00"));
                                        %>
                                      </div>
                                    </td>
                                    <%}%>
                                    <%}%>
                              </tr>
                            </table>
                            <% } %>
                          </td>
                        </tr>
                        <%}%>
                      </table>
                  </td>
                </tr>

                <tr> 
                  <td colspan="3"><span class="fielderror">
                      <%
                        for (int k = 0; k < listError.size(); k++) {
                          if (k == 0) {
                            out.println(listError.get(k) + "<br>");
                          } else {
                            out.println("&nbsp;&nbsp;&nbsp;" + listError.get(k) + "<br>");
                          }
                        }
                      %></span></td>
                </tr>
                <tr> 
                  <td width="50%">
                    <%
                      ctrLine.setLocationImg(approot + "/images");

                      // set image alternative caption 
                      ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_SAVE, true));
                      ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) + " List");
                      ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_ASK, true));
                      ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_CANCEL, false));

                      ctrLine.initDefault();
                      ctrLine.setTableWidth("100%");
                      String scomDel = "javascript:cmdAsk('" + oidReceiveMaterial + "')";
                      String sconDelCom = "javascript:cmdConfirmDelete('" + oidReceiveMaterial + "')";
                      String scancel = "javascript:cmdEdit('" + oidReceiveMaterial + "')";
                      ctrLine.setCommandStyle("command");
                      ctrLine.setColCommStyle("command");

                      // set command caption 
                      ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_SAVE, true));
                      ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) + " List");
                      ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_ASK, true));
                      ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_DELETE, true));
                      ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_CANCEL, false));

                      if (privDelete && privManageData) {
                        ctrLine.setConfirmDelCommand(sconDelCom);
                        ctrLine.setDeleteCommand(scomDel);
                        ctrLine.setEditCommand(scancel);
                      } else {
                        ctrLine.setConfirmDelCaption("");
                        ctrLine.setDeleteCaption("");
                        ctrLine.setEditCaption("");
                      }

                      if (privAdd == false && privUpdate == false) {
                        ctrLine.setSaveCaption("");
                      }

                      if (privAdd == false) {
                        ctrLine.setAddCaption("");
                      }

                      if (iCommand == Command.SAVE && frmrec.errorSize() == 0) {
                        //iCommand=Command.EDIT;
                      }

                      out.println(ctrLine.drawImage(iCommand, iErrCode, errMsg));
                    %>
                  </td>
                  <td class="pull-right">
                    <%if (listMatReceiveItem != null && listMatReceiveItem.size() > 0) {%>
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <% if (useForRaditya.equals("1")) {
                              if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED){ %>
                        <td><a href="javascript:printForm()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][6]%></a>
                          <a href="javascript:printFormPDF()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                              <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                        <td width="5%" valign="top"><a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                        <td width="45%" nowrap>&nbsp; <a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][10]%> </a></td>
                        <%}} else {
                         if (privShowQtyPrice && typeOfBusiness == "3") {%>
                        <td width="5%" valign="top"><a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                        <td width="45%" nowrap>&nbsp; <a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][10]%> </a></td>
                        <%}
                                }
                              } else {%>
                        <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                        <td><a href="javascript:printForm()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][6]%></a>
                          <a href="javascript:printFormPDF()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                              <%} else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {%>
                        <td><a href="javascript:printForm()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][6]%></a>
                          <a href="javascript:printFormPDF()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                        <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][9]%>"></a></td>
                        <td width="45%" nowrap>&nbsp; <a href="javascript:PostingStock('<%=oidReceiveMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][9]%> </a></td>
                        <%} else {%>
                        <td><a href="javascript:printForm()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][6]%></a>
                          <a href="javascript:printFormPDF()" class="btn btn-warning"><li class="fa fa-print"></li> <%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                              <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                        <td width="5%" valign="top"><a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                        <td width="45%" nowrap>&nbsp; <a href="javascript:PostingCostPrice('<%=oidReceiveMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][10]%> </a></td>
                        <%}%>
                        <%}%>
                        <%}%>
                      </tr>
                    </table>
                    <%}%>
                  </td>
                </tr>
                <tr>
                  <td></td>
                  <td class="history"> <label class="history-view pull-right"> <a href="javascript:viewHistoryTable()" class="btn btn-info">VIEW TABEL HISTORY</a></label></td>
                </tr>
              </table>
              </form>
            </td> 
          </tr> 
        </table>
      </td>
    </tr>
    <tr> 
      <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
        <%if (menuUsed == MENU_ICON) {%>
        <%@include file="../../../styletemplate/footer.jsp" %>
        <%} else {%>
        <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
        <script language="JavaScript">
          <%
            // add by fitra 10-5-2014
            if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE) {%>
                        addItem();
          <% }%>
        </script>
      </td>
    </tr>
</table>
</div>
</body>
    <!-- #EndTemplate --></html>
