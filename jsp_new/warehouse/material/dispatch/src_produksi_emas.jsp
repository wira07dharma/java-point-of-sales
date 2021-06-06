<%-- 
    Document   : src_produksi_emas
    Created on : Mar 27, 2018, 3:29:24 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.form.masterdata.FrmMaterial"%>
<%@page import="com.dimata.posbo.session.masterdata.*"%>
<%@page import="com.dimata.posbo.session.sales.*"%>
<%@page import="com.dimata.posbo.entity.search.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.entity.warehouse.*"%>
<%@page import="com.dimata.common.entity.contact.*"%>
<%@page import="com.dimata.common.entity.custom.*"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.pos.form.billing.*"%>
<%@page import="com.dimata.pos.entity.billing.*"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    String tglAwal = FRMQueryString.requestString(request, "FRM_TGL_AWAL");
    String tglAkhir = FRMQueryString.requestString(request, "FRM_TGL_AKHIR");
    long idLokasi = FRMQueryString.requestLong(request, "FRM_LOKASI");
    long idBillMain = FRMQueryString.requestLong(request, "SELECTED_BILL_MAIN_ID");
    long idBillInvoice = FRMQueryString.requestLong(request, "SELECTED_BILL_INVOICE_ID");
    long idBillDetail = FRMQueryString.requestLong(request, "SELECTED_BILL_DETAIL_ID");
    int statusInvoicing = FRMQueryString.requestInt(request, "FRM_STATUS_INVOICING");
    int showModalBillDetail = FRMQueryString.requestInt(request, "SHOW_MODAL_BILL_DETAIL");
    int finishOrder = FRMQueryString.requestInt(request, "FINISH_ORDER");
    int tipeOrder = FRMQueryString.requestInt(request, "FRM_TIPE_ORDER");
    int duplicateOrder = FRMQueryString.requestInt(request, "DUPLICATE_ORDER");
    int finishInvoicing = FRMQueryString.requestInt(request, "FINISH_INVOICING");
    
    double cashierSuggestedDiscountValue = Double.valueOf(PstSystemProperty.getValueByName("CASHIER_SUGGESTED_DISCOUNT_VALUE"));

    if (iCommand == Command.NONE) {
        tglAwal = Formater.formatDate(new Date(), "yyyy-MM-dd");
        tglAkhir = Formater.formatDate(new Date(), "yyyy-MM-dd");
    }
    
    String tipeOrderTitle[] = {"Reguler", "Servis"};

    String where = ""
            + " ((BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED                        
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES+") OR "
			+ " (BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE                        
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_FINISH
            + ")) AND (DATE(BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")"
            + " BETWEEN '" + tglAwal + "' AND '" + tglAkhir + "')";
    
    where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = 0";
    
    if (statusInvoicing == 10) {        
        where += ""
                + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " NOT IN ("
                + "SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN 
                + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " <> 0"
                + ")";
    } else if (statusInvoicing == 11) {
        where += ""                
                + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " IN ("
                + "SELECT " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN 
                + " WHERE " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " <> 0"
                + ")";
    }
    
    if (idLokasi > 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + idLokasi;        
    }
    
    if (tipeOrder == 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + " <> 1";
    } else {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + " = 1";
    }
    
    if (idBillMain > 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = " + idBillMain;        
    }
    
    where += " GROUP BY BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];

    String order = "" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

    Vector listSalesOrder = new Vector();
    listSalesOrder = SessSalesOrder.listJoinBillMainDetail(0, 0, where, order);
    
    //==========================================================================
    boolean dpLebihBesar = false;    
    if (idBillMain > 0) {
        double amountBillMain = SessSalesOrder.getTotalBillOrder(idBillMain);
        double amountDP = SessSalesOrder.getFirstDownPayment(idBillMain);
        dpLebihBesar = amountDP > (amountBillMain / 2);
    }

    CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
    int iErrorCode = ctrlBillDetail.action(iCommand, idBillDetail, idBillInvoice);
    String sErrorMsg = ctrlBillDetail.getMessage();
    FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();
    Billdetail billdetail = ctrlBillDetail.getBillDetail();
    
    //duplicate order bill
    if (duplicateOrder == 1) {
        iErrorCode += SessSalesOrder.createInvoice(idBillMain);
        if (iErrorCode == 0) {
            sErrorMsg += "Pembuatan bill invoice berhasil ";
        } else {
            sErrorMsg += "Pembuatan bill invoice gagal ! ";
        }
    }

    Material material = new Material();
    if (billdetail.getMaterialId() > 0 && PstMaterial.checkOID(billdetail.getMaterialId())) {
        material = PstMaterial.fetchExc(billdetail.getMaterialId());
		if (iCommand == Command.SAVE){
			double hargaBeli = FRMQueryString.requestDouble(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]);
			String keterangan = FRMQueryString.requestString(request, FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DESCRIPTION]);
			if (hargaBeli > 0){
				material.setAveragePrice(hargaBeli);
				material.setMaterialDescription(keterangan);
				try {
					PstMaterial.updateExc(material);
				} catch (Exception exc){}

			}
		}
    }
	MaterialDetail materialDetail = new MaterialDetail();
	try {
		materialDetail = PstMaterialDetail.fetchExcMaterialDetailId(material.getOID());
		if (iCommand == Command.SAVE){
			double ongkos = FRMQueryString.requestDouble(request, "pokok_ongkos");
			double berat = FRMQueryString.requestDouble(request, "FRM_FIELD_BERAT");
			double beratTambah = FRMQueryString.requestDouble(request, "FRM_FIELD_ADDITIONAL_WEIGHT");
			if (ongkos > 0){
				materialDetail.setOngkos(ongkos);
				materialDetail.setBerat(berat+beratTambah);
				PstMaterialDetail.updateExc(materialDetail);
			}
		}
		
	} catch (Exception exc){}
	
    
    //get harga emas terbaru
    double hargaEmasBaru = SessSalesOrder.getHargaEmasBaru(billdetail.getMaterialId(), 1.0);
    double realItemPrice = dpLebihBesar ? billdetail.getItemPrice() : hargaEmasBaru;
    //hitung total
    double totalPrice = 0;
    if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
		if (dpLebihBesar){
			totalPrice = (billdetail.getBerat() * realItemPrice) + billdetail.getCost() + (billdetail.getSusutanWeight()* billdetail.getItemPrice()) + (billdetail.getAdditionalWeight() * billdetail.getLatestItemPrice());
		} else {
			totalPrice = (billdetail.getBerat() * realItemPrice) + billdetail.getCost() + (billdetail.getSusutanWeight()* realItemPrice) + (billdetail.getAdditionalWeight() * billdetail.getLatestItemPrice());
		}
    } else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
        totalPrice = billdetail.getItemPrice() + billdetail.getCost() + billdetail.getSusutanPrice();
    }
    double discountValue = (billdetail.getDiscPct()/100) * totalPrice;
    double afterDiscount = totalPrice - discountValue;
    double taxValue = (billdetail.getTaxPct()/100) * afterDiscount;
    double afterTax = afterDiscount + taxValue;
    
    //update data material, material detail dan bill detail
    if (material.getOID() > 0 && iCommand == Command.SAVE) {
        if (material.getMaterialJenisType() != Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
            material.setSupplierId(FRMQueryString.requestLong(request, "FRM_SUPPLIER"));
            material.setKepemilikanId(FRMQueryString.requestLong(request, "FRM_KEPEMILIKAN"));
            material.setCategoryId(FRMQueryString.requestLong(request, "FRM_CATEGORY"));
            material.setMerkId(FRMQueryString.requestLong(request, "FRM_MERK"));
            material.setPosKadar(FRMQueryString.requestLong(request, "FRM_KADAR"));
            material.setPosColor(FRMQueryString.requestLong(request, "FRM_WARNA"));
            material.setName(FRMQueryString.requestString(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]));
        }
        
        //update data material        
        iErrorCode += SessSalesOrder.updateDetailOrder(billdetail, material, userId, userName);
        if (iErrorCode > 0) {
            sErrorMsg += "Update data bill detail gagal ! ";
        }
        //fetch lagi untuk kembalikan data material jika tipe item adalah emas lantakan (karna emas lantakan tidak di update)
        material = PstMaterial.fetchExc(billdetail.getMaterialId());
    }

    //buat proses produksi
    if (finishOrder == 1) {
        MatDispatch matDispatch = new MatDispatch();
        matDispatch.setLocationId(FRMQueryString.requestLong(request, "FRM_LOKASI_AWAL"));
        matDispatch.setDispatchTo(FRMQueryString.requestLong(request, "FRM_LOKASI_TUJUAN"));
        String tglProduksi = FRMQueryString.requestString(request, "FRM_TGL_PRODUKSI");
        Date tgl = Formater.formatDate(tglProduksi, "yyyy-MM-dd hh:mm:ss");
        
        MatDispatchItem matDispatchItem = new MatDispatchItem();
        matDispatchItem.setGondolaId(FRMQueryString.requestLong(request, "FRM_ETALASE_AWAL"));
        matDispatchItem.setGondolaToId(FRMQueryString.requestLong(request, "FRM_ETALASE_TUJUAN"));        
        iErrorCode += SessSalesOrder.prosesProduksi(idBillMain, idBillInvoice, matDispatch, matDispatchItem, tgl);
        if (iErrorCode == 0) {
            sErrorMsg += "Proses produksi berhasil ";
        } else {
            sErrorMsg += "Proses produksi gagal ! ";
        }
        //Update location dan gondola di material, ambil material berdasarkan id bill
        String whereBillDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = "+idBillInvoice;
        Vector listDetail = PstBillDetail.list(0, 0, whereBillDetail, "");
        if (listDetail.size() > 0){
            for (int i=0; i < listDetail.size(); i++){
                Billdetail detail = (Billdetail) listDetail.get(i);
                try {
                    Material mat = PstMaterial.fetchExc(detail.getMaterialId());
                    mat.setUseSellLocation(FRMQueryString.requestLong(request, "FRM_LOKASI_TUJUAN"));
                    mat.setGondolaCode(FRMQueryString.requestLong(request, "FRM_ETALASE_TUJUAN"));
                    Material prevMaterial = PstMaterial.fetchExc(mat.getOID());
                    PstMaterial.updateExc(mat);
                    SessSalesOrder.insertHistoryMaterial(userId, userName, Command.UPDATE, mat.getOID(), mat, prevMaterial);
                } catch (Exception exc){
                    
                }
            }
        }
        
    }
    
    //update status invoicing
    if (finishInvoicing == 1) {
        iErrorCode += SessSalesOrder.updateStatusInvoicingBillMain(idBillMain, idBillInvoice);
        if (iErrorCode == 0) {
            sErrorMsg += "Proses update invoice berhasil ";
        } else {
            sErrorMsg += "Proses update invoice gagal ! ";
        }
    }
    
    boolean containsLantakanOrder = false;//nilai true jika bill main memiliki item emas lantakan
    boolean childBillMain = false;//nilai true jika bill main memiliki child    
    if (idBillMain > 0) {
        //cek ada emas lantakan atau tidak    
        String whereLantakan = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = " + idBillMain
                + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = " + Material.MATERIAL_TYPE_EMAS_LANTAKAN;
        Vector listDetailLantakan = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, whereLantakan, "");
        if (listDetailLantakan.size() > 0) {
            containsLantakanOrder = true;
        }
        //cek bill main punya child atau tidak
        Vector listBillMain = PstBillMain.list(0, 0, "" + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = '" + idBillMain + "'", "");
        if (listBillMain.size() > 0) {
            childBillMain = true;
        }
    }

    int taxInclude = SessSalesOrder.listTaxService("billMain." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'");    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" media="screen" href="../../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <style>

            th {font-size: 14px;}
            td {font-size: 14px; padding: 4px 8px !important}

            .tabel_detail th {text-align: center}
            .tabel_detail {margin-bottom: 10px}
            .tabel_data {font-size: 13px;}
            .tabel_detail * {font-size: 12px; border-color: #e2e2e2 !important}
            a:hover {cursor: pointer}            

            .modal-header {padding: 10px 20px; border-color: lightgray}
            //.modal-body {padding: 10px}
            .modal-footer {padding: 10px 20px; margin: 0px; border-color: lightgray}
            
            .long_text {overflow: hidden; white-space: nowrap; text-overflow: ellipsis;}

        </style>
        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/jquery.numeric.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script>

            function viewOrderItem(idBillInvoice, idBillDetail) {
                $('#command').val("<%=Command.LIST%>");
                $('#billInvoiceId').val(idBillInvoice);
                $('#biillDetailId').val(idBillDetail);
                $('#showModalBillDetail').val("1");
                $('#formSearch').submit();
            }
            
            function editBillDetail(idBillInvoice, idBillDetail) {
                $('#command').val("<%=Command.EDIT%>");                
                $('#billInvoiceId').val(idBillInvoice);
                $('#biillDetailId').val(idBillDetail);
                $('#showModalBillDetail').val("1");
                $('#formSearch').submit();
            }

            function deleteLantakan(idBillInvoice, idBillDetail, skuBillDetail) {
                if (confirm("Yakin ingin hapus data " + skuBillDetail + " ?")) {                    
                    $('#command').val("<%=Command.DELETE%>");
                    $('#billInvoiceId').val(idBillInvoice);
                    $('#biillDetailId').val(idBillDetail);
                    $('#showModalBillDetail').val("0");
                    $('#formSearch').submit();
                }
            }
            
            function prosesInvoice(idBillInvoice, canProses) {
                if (!canProses) {
                    alert("Pastikan SKU item produksi sudah di update !");
                 } else {
                    if (confirm("Yakin data item sudah benar ?")) {
                        $('#command').val("<%=Command.LIST%>");
                        $('#billInvoiceId').val(idBillInvoice);
                        $('#modalProduksi').modal('show');
                    }
                }
            }

            function finishProduksi(idBillInvoice, noInvoice) {
                if (confirm("Invoice " + noInvoice + " siap untuk pelunasan ?")) {
                    $('#command').val("<%=Command.LIST%>");
                    $('#billInvoiceId').val(idBillInvoice);
                    $('#finishInvoice').val("1");
                    $('#formSearch').submit();
                }
            }

            function formatNumber2 (number) {
                    return number.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
            }

            //==================================================================

            $(document).ready(function () {
                
                var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
                    $(this).getData({
                        onDone: function (data) {
                            onDone(data);
                        },
                        onSuccess: function (data) {
                            onSuccess(data);
                        },
                        approot: approot,
                        dataSend: dataSend,
                        servletName: servletName,
                        dataAppendTo: dataAppendTo,
                        notification: notification,
                        ajaxDataType: dataType
                    });
                };

                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };

                $('.datepick').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd',
                    minView: 2
                });
                
                $('.datetimepick').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd hh:ii:ss',
                    minView: 2
                });
                
                $('.datetimepick, .datepick').attr("autocomplete","off");

                modalSetting("#modalBillDetail", "static", false, false);
                modalSetting("#modalProduksi", "static", false, false);
                
                $('[data-toggle="tooltip"]').tooltip();

                //==============================================================

                if ("<%=showModalBillDetail%>" === "1") {
                    if ("<%=idBillDetail%>" > 0) {
                        $('#modalBillDetail .modal-title').html("Edit Item [" + "<%=Material.MATERIAL_TYPE_TITLE[material.getMaterialJenisType()] %>]");
                    } else {
                        $('#modalBillDetail .modal-title').html("Tambah Emas Lantakan");
                    }
                    $('#modalBillDetail').modal('show');
                }

                $('#btnSrc').click(function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");                    
                    $('#command').val("<%=Command.LIST%>");
                    $('#billOrderId').val("0");
                    $('#billInvoiceId').val("0");
                    $('#biillDetailId').val("0");
                    $('#showModalBillDetail').val("0");
                    $('#formSearch').submit();
                });
                
                $('#btnBack').click(function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");                    
                    $('#command').val("<%=Command.LIST%>");
                    $('#billOrderId').val("0");
                    $('#billInvoiceId').val("0");
                    $('#biillDetailId').val("0");
                    $('#showModalBillDetail').val("0");
                    $('#formSearch').submit();
                });

                $('.view_detail').click(function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    var id = $(this).data('idselect');
                    $('#command').val("<%=Command.LIST%>");
                    $('#billOrderId').val(id);
                    $('#billInvoiceId').val("0");
                    $('#biillDetailId').val("0");
                    $('#formSearch').submit();
                });

                $('.duplicate_order').click(function () {
                    if (confirm("Duplikasi bill order untuk pembuatan invoice baru ?")) {
                        $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                        var id = $(this).data('idselect');
                        $('#command').val("<%=Command.LIST%>");
                        $('#billOrderId').val(id);
                        $('#billInvoiceId').val("0");
                        $('#biillDetailId').val("0");
                        $('#duplicate').val("1");
                        $('#formSearch').submit();
                    }
                });

                $('.btnAddLantakan').click(function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    var id = $(this).data('idselect');
                    $('#command').val("<%=Command.ADD%>");                    
                    $('#billOrderId').val("<%=idBillMain%>");
                    $('#billInvoiceId').val(id);
                    $('#biillDetailId').val("0");
                    $('#showModalBillDetail').val("1");
                    $('#formSearch').submit();
                });

                $('#btnSaveBillDetail').click(function () {
                    var ok = true;
                    
                    if ("<%=iCommand%>" == "<%=Command.ADD%>") {
                        var itemId = $('.newItem').val();
                        if (itemId == "" || itemId == 0 || itemId == null) {
                            ok = false;
                            alert("Pilih item !");                        
                        }
                    }
                    
                    if ("<%=iCommand%>" == "<%=Command.EDIT%>") {                        
                        ok = true;
                    }
                    
                    if (ok){
                        $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");                        
                        $('#command').val("<%=Command.SAVE%>");
                        $('#showModalBillDetail').val("0");
                        $('#formSearch').submit();
                    }
                });
                
                $('.btnDeleteBillDetail').click(function () {
                    var id = $(this).data('idselect');
                    var idDelete = $(this).data('id-delete');
                    var msg = "Yakin ingin menghapus data ini ?";
                    if (confirm(msg)) {
                        $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                        $('#command').val("<%=Command.DELETE%>");
                        $('#billInvoiceId').val(id);
                        $('#biillDetailId').val(idDelete);
                        $('#showModalBillDetail').val("0");
                        $('#formSearch').submit();
                    }
                });
                
                $('#btnProduksi').click(function () {
                    var tgl = $('#tglProduksi').val();
                    var locAsal = $('.loc_asal').val();
                    var locTujuan = $('.loc_tujuan').val();
                    var etaAsal = $('.eta_asal').val();
                    var etaTujuan = $('.eta_tujuan').val();
                    
                    if (tgl == "" || locAsal == "" || locTujuan == "" || etaAsal == "" || etaTujuan == "") {
                        alert("Data belum lengkap !");
                        return false;
                    }
                    
                    /*if (etaAsal == etaTujuan) {
                        alert("Etalase tidak boleh sama !");
                        return false;
                    }*/
                    
                    var msg = "Proses produksi selesai ?";
                    if (confirm(msg)) {
                        $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");                        
                        $('#command').val("<%=Command.LIST%>");
                        $('#billOrderId').val("<%=idBillMain%>");
                        //$('#billInvoiceId').val("0");
                        $('#biillDetailId').val("0");
                        $('#showModalBillDetail').val("0");
                        $('#finishOrder').val("1");
                        $('#formSearch').submit();
                    }
                });
                
                //==============================================================
                $('#itemKadar').change(function() {
                    getHargaJual();
                });
                $('#itemWarna').change(function() {
                    getHargaJual();
                });
				
				function totalHPP(){
					var cost = <%=(materialDetail.getOngkos() > 0 ? materialDetail.getOngkos() : billdetail.getCost())%>;
					var total = <%=(material.getAveragePrice() > 0 ? material.getAveragePrice() : ((billdetail.getBerat() * billdetail.getItemPrice()) + (billdetail.getAdditionalWeight() * billdetail.getLatestItemPrice())))%>;
					var totalHpp = parseFloat(cost) + parseFloat(total);
					$("#pokok_ongkos").val(cost);
					$("#<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>").val(total);
					$("#totalHPP").val(totalHpp.toLocaleString());
				}
				totalHPP();
				
				$("#averagePrice").keyup(function() {
					var value = $(this).val().replace(/,/g , "") * 1;
					var cost = $("#inputOngkos").val().replace(/,/g , "") * 1;
					var totalHpp = parseFloat(value) + parseFloat(cost);
					$(this).val(value.toLocaleString());
					$("#<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>").val(value);
					$("#totalHPP").val(totalHpp.toLocaleString());
				});
				$("#inputOngkos").keyup(function() {
					var value = $(this).val().replace(/,/g , "") * 1;
					var price = $("#averagePrice").val().replace(/,/g , "") * 1;
					var totalHpp = parseFloat(value) + parseFloat(price);
					$(this).val(value.toLocaleString());
					$("#pokok_ongkos").val(value);
					$("#totalHPP").val(totalHpp.toLocaleString());
				});
                function getHargaJual() {
                    var command = "<%= Command.NONE%>";
                    var kadar = $('#itemKadar').val();
                    var color = $('#itemWarna').val();
                    var dataSend = {
                        "FRM_FIELD_DATA_FOR": "getHargaJual",
                        "FRM_KADAR_ID": kadar,
                        "FRM_COLOR_ID": color,
                        "command": command
                    };
                    onDone = function (data) {
                        var harga = parseFloat(data.RETURN_HARGA_JUAL);
                        $('.hargaBaru').val(harga.toLocaleString());
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, "<%=approot%>", command, dataSend, "AjaxEmasLantakan", null, false, "json");
                }
                //==============================================================
                
                //==============================================================
                function changeLocation(locationId, etalaseId) {
                    var idLocation = $(locationId).val();
                    var dataFor = "getEtalaseByLocation";
                    var command = "<%=Command.NONE%>";
                    var approot = "<%=approot%>";
                    var dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_LOCATION_ID": idLocation,
                        "command": command
                    };
                    onDone = function (data) {

                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", etalaseId, false, "json");
                }
                $('.loc_asal').change(function () {
                    changeLocation(".loc_asal", ".eta_asal");
                });
                $('.loc_tujuan').change(function () {
                    changeLocation(".loc_tujuan", ".eta_tujuan");
                });
                //==============================================================
                
                $(".itemChange").on('keyup keydown', function(event) {
                    var checkVal = $(this).val();
                    var ids = $(this).attr('id');
                    if (checkVal.length == 0) {
                        $(this).val(0);
                    }
                    var taxInc = $("<%= taxInclude%>").val();
                    var qty = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>").val());
                    var itemOngkos = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_COST]%>").val().replace(/,/g , ""));
                    var itemPrice = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>").val().replace(/,/g , ""));
                    var latestPrice = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>").val().replace(/,/g , ""));
                    var itemWeight = $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_BERAT]%>").val().replace(/,/g , "");
                    var addWeight = $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ADDITIONAL_WEIGHT]%>").val().replace(/,/g , "");                    
                    
                    var susutanWeight = $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>").val().replace(/,/g , "");
                    var susutanUang = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>").val().replace(/,/g , ""));                    
                    var discPct = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_PCT]%>").val());
                    var discNominal = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val().replace(/,/g , ""));                    
                    var taxPct = parseFloat($("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TAX_PCT]%>").val());
					var beratMengikat = $("#beratMengikat").val();
					var beratPesan = $("#beratPesan").val();
                    
                    var hargaTotal = 0;
                    if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                        hargaTotal = parseFloat($("#hargaTotal").val().replace(/,/g , ""));
                    } else if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN %>) {
                        hargaTotal = itemPrice;
                    }
                    
                    var hargaTambah = 0;
                    if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                        hargaTambah = parseFloat($("#hargaTambah").val().replace(/,/g , ""));
                    } else if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN %>) {
                        hargaTambah = latestPrice;
                    }
                    
                    var totalPrice = itemOngkos + hargaTotal + hargaTambah + susutanUang;
                    
                    switch (ids) {
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_BERAT]%>":                            
                            if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                                $("#hargaTotal").val(0);
								var value = $(this).val();
								var total = parseFloat(value) - parseFloat(beratPesan);
								if (beratMengikat === "1"){
									if (+total <= 0){
										hargaTotal = itemWeight * itemPrice;
									} else {
										$("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_BERAT]%>").val(beratPesan);
										hargaTotal = beratPesan * itemPrice;
										alert("Untuk berat tambahan harga mengikat tambahkan pada field Berat Tambah");
									}
								} else {
									hargaTotal = itemWeight * latestPrice;
								}
                            }
                            break;
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ADDITIONAL_WEIGHT]%>":
                            if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                                $("#hargaTambah").val(0);
                                hargaTambah = addWeight * latestPrice;
                            }
                            break;
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_PCT]%>":
                            $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val(0);
                            discNominal = totalPrice * (discPct / 100);
                            break;
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>":
                            $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_PCT]%>").val(0);
                            discPct = discNominal / totalPrice * 100;
                            break;
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>":
                            if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                                $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>").val(0);
								var value = $(this).val();
								var total = parseFloat(value) - parseFloat(beratPesan);
								if (beratMengikat === "1"){
									susutanUang = itemPrice * susutanWeight;
								} else {
									susutanUang = susutanWeight * latestPrice;
								}
                            }
                            break;
                        case "<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>":
                            if (<%=material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS %>) {
                                $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>").val(0);
                                susutanWeight = susutanUang / itemPrice;
                            }
                            break;
                        default:
                            if (discPct > 0) {
                                $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val(0);
                                discNominal = totalPrice * (discPct / 100);
                            } else {
                                $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_PCT]%>").val(0);
                                if (discNominal == 0) {
                                    discPct = 0;
                                } else {
                                    discPct = discNominal / totalPrice * 100;
                                }
                            }
                            break;
                    }
                    
                    totalPrice = itemOngkos + hargaTotal + hargaTambah + susutanUang;
					//alert(itemOngkos+" "+hargaTotal+" "+hargaTambah+" "+susutanUang);
                    
                    var total = 0;
                    var afterDiscount = totalPrice - discNominal;
                    var taxValue = afterDiscount * (taxPct / 100);
                    if (taxInc == <%= PstBillMain.INC_CHANGEABLE%> || taxInc == <%= PstBillMain.INC_NOT_CHANGEABLE%>) {
                        total = afterDiscount;
                    } else {
                        total = afterDiscount + taxValue;
                    }
                    
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_COST]%>").val(itemOngkos.toLocaleString());
                    $("#hargaTotal").val(hargaTotal.toLocaleString());
                    $("#hargaTambah").val(hargaTambah.toLocaleString());
                    //$("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_BERAT]%>").val(itemWeight);                    
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>").val(itemPrice.toLocaleString());
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>").val(latestPrice.toLocaleString());
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_PCT]%>").val(discPct);
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val(discNominal);
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_TAX]%>").val(taxValue);
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>").val(susutanWeight);
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>").val(susutanUang.toLocaleString());
                    $("#total1").val(formatNumber2(totalPrice));
                    $("#<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_PRICE]%>").val(formatNumber2(afterDiscount));                    
                    $("#grandTotal").val(formatNumber2(total));
                    
					var hel = (itemWeight * itemPrice) + (addWeight * latestPrice);
					$("#averagePrice").val(hel.toLocaleString());
					$("#<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>").val(hel);
					var costPokok = $("#pokok_ongkos").val();
					var totalHpp = parseFloat(hel) + parseFloat(costPokok);
					$("#totalHPP").val(totalHpp.toLocaleString());
					
                    //sugested discount
                    var suggestedDiscount = 0;
                    var roundedTotal = Math.floor(total / <%= cashierSuggestedDiscountValue %>) * <%= cashierSuggestedDiscountValue %>;
                    if (taxInc == <%= PstBillMain.INC_CHANGEABLE%> || taxInc == <%= PstBillMain.INC_NOT_CHANGEABLE%>) {
                        suggestedDiscount = Math.abs(roundedTotal - total);
                    } else {
                        suggestedDiscount = Math.abs(roundedTotal - total) / ((100 + taxPct) / 100);
                    }
                    $('#sugestDisc').val(suggestedDiscount);
                });

                $('#applyDisc').click(function() {
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val(0).keyup();
                    $("#<%= FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>").val($('#sugestDisc').val()).keyup();
                });
                
            });
        </script>
    </head>
    <body style="background-color: #eeeeee">
        <div class="col-md-12">
            
            <h4>Produksi</h4>
            
            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Pencarian Sales Order</h3>
                </div>

                <form id="formSearch" method="post" class="form-horizontal">
                    <input type="hidden" id="command" name="command" value="<%=iCommand%>"> <!--diisi/diganti lewat javascript-->
                    <input type="hidden" id="billOrderId" name="SELECTED_BILL_MAIN_ID" value="<%=idBillMain%>"> <!--diisi lewat javascript-->
                    <input type="hidden" id="billInvoiceId" name="SELECTED_BILL_INVOICE_ID" value="<%=idBillInvoice%>"> <!--diisi lewat javascript-->
                    <input type="hidden" id="biillDetailId" name="SELECTED_BILL_DETAIL_ID" value="<%=idBillDetail%>"> <!--diisi lewat javascript-->
                    <input type="hidden" id="showModalBillDetail" name="SHOW_MODAL_BILL_DETAIL" value=""> <!--diisi lewat javascript-->
                    <input type="hidden" id="duplicate" name="DUPLICATE_ORDER" value=""> <!--diisi lewat javascript-->
                    <input type="hidden" id="finishOrder" name="FINISH_ORDER" value=""> <!--diisi lewat javascript-->
                    <input type="hidden" id="finishInvoice" name="FINISH_INVOICING" value=""> <!--diisi lewat javascript-->

                    <div class="box-body">
                        <p></p>
                        <div class="form-group">
                            
                            <label class="col-sm-1 control-label">Tanggal</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>                                    
                                    <input type="text" placeholder="Tanggal awal" required="" name="FRM_TGL_AWAL" class="input-sm form-control datepick" value="<%=tglAwal%>">
                                </div>                                
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal akhir" required="" name="FRM_TGL_AKHIR" class="input-sm form-control datepick" value="<%=tglAkhir%>">
                                </div>
                            </div>                                   
                            
                            <label class="col-sm-2 control-label">Lokasi Produksi</label>
                            <div class="col-sm-2">
                                <%
                                    Vector val_locationid = new Vector(1, 1);
                                    Vector key_locationid = new Vector(1, 1);

                                    String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                    whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                    Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                    val_locationid.add(String.valueOf(0));
                                    key_locationid.add("Semua lokasi");

                                    for (int d = 0; d < vt_loc.size(); d++) {
                                        Location loc = (Location) vt_loc.get(d);
                                        val_locationid.add("" + loc.getOID() + "");
                                        key_locationid.add(loc.getName());
                                    }
                                %>                 
                                <%=ControlCombo.draw("FRM_LOKASI", null, "" + idLokasi, val_locationid, key_locationid, "", "form-control input-sm")%>
                            </div>
                                
                            <label class="col-sm-1 control-label">Tipe</label>
                            <div class="col-sm-2">
                                <select class="form-control input-sm" name="FRM_TIPE_ORDER">
                                    <option <%=tipeOrder == 0 ? "selected" : ""%> value="0">Reguler</option>                                    
                                    <option <%=tipeOrder == 1 ? "selected" : ""%> value="1">Servis</option>
                                </select>
                            </div>
                                
                        </div>
                                <%--
                        <div class="form-group">
                            
                            <label class="col-sm-1 control-label">Status</label>
                            <div class="col-sm-2">
                                <select class="form-control input-sm" name="FRM_STATUS_INVOICING">                                    
                                    <option <%=statusInvoicing == 0 ? "selected" : ""%> value="0">Order</option>                                    
                                    <option <%=statusInvoicing == 1 ? "selected" : ""%> value="1">Finish</option>
                                </select>
                            </div>
                                                        
                        </div>
                                --%>
                    </div>                

                    <div class="box-footer" style="border-color: lightgray">
                        <button type="button" id="btnSrc" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                    </div>

                    <%if(iCommand == Command.ADD || iCommand == Command.EDIT) {%>
                    
                    <div id="modalBillDetail" class="modal fade" role="dialog">
                        <div class="modal-dialog" style="width: 700px">

                            <div class="modal-content">
                                <div class="modal-header mo">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="form-horizontal">                                        
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID]%>" value="<%=billdetail.getOID()%>">
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_UNIT_ID]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_UNIT_ID]%>" value="<%=billdetail.getUnitId()%>">
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SERVICE_PCT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SERVICE_PCT]%>" value="<%=billdetail.getServicePct()%>">
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_TYPE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_TYPE]%>" value="<%=billdetail.getMaterialType()%>">
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_AMOUNT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_AMOUNT]%>" value="<%=billdetail.getTotalAmount()%>">
                                        <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_DISC]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_DISC]%>" value="<%=billdetail.getTotalDisc()%>">
        
                                        <div class="form-group">
                                            <div class="col-sm-2">Nama Item</div>
                                                                                        
                                            <% if (iCommand == Command.ADD) {%>                                            
                                            <div class="col-sm-4">
                                                <%
                                                    Vector val_mat = new Vector();
                                                    Vector key_mat = new Vector();
                                                    val_mat.add("0");
                                                    key_mat.add("- Pilih emas lantakan -");
                                                    Vector listEmasLantakan = PstMaterial.list(0, 0, ""+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]+"="+Material.MATERIAL_TYPE_EMAS_LANTAKAN, "");
                                                    for (int i=0; i < listEmasLantakan.size(); i++) {
                                                        Material mat = (Material) listEmasLantakan.get(i);                                                        
                                                        val_mat.add("" + mat.getOID());
                                                        key_mat.add("" + mat.getSku()+" - " + mat.getName());
                                                    }
                                                %>
                                                <%=ControlCombo.draw(frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_ID],"form-control input-sm newItem", null, ""+material.getMaterialId(), val_mat, key_mat, "")%>
                                            </div>
                                            <div class="col-sm-2">Berat</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                                    
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" value="<%=String.format("%,.3f", billdetail.getBerat())%>">
                                                    <span class="input-group-addon">gr</span>
                                                </div>
                                            </div>
                                            <%} else {%>
                                            <div class="col-sm-4">
                                                <input type="text" <%=(material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) ? "readonly":"" %> class="form-control input-sm" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_NAME]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_NAME]%>" value="<%=billdetail.getItemName() %>">
                                                <input type="hidden" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_ID]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_ID]%>" value="<%=billdetail.getMaterialId()%>">
                                            </div>
                                            <div class="col-sm-2">SKU</div>
                                            <div class="col-sm-4">                                                                                                                                            
                                                <input type="text" readonly="" class="form-control input-sm" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SKU]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SKU]%>" value="<%=billdetail.getSku() %>">
                                            </div>
                                            <%}%>
                                        </div>
                                        
                                        <% if (iCommand == Command.EDIT && material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                                        <div class="form-group">
                                            <div class="col-sm-2">Berat</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                                    
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" value="<%=String.format("%,.3f", billdetail.getBerat())%>">
                                                    <span class="input-group-addon">gr</span>
                                                </div>
                                            </div>
                                        </div>
                                        <%}%>

                                        <% if (iCommand == Command.EDIT && material.getMaterialJenisType() != Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-2">Supplier</div>
                                            <div class="col-sm-4">
                                                <%
                                                    SrcMemberReg objSrcMemberReg = new SrcMemberReg();
                                                    Vector vSupplier = SessMemberReg.searchSupplier(objSrcMemberReg, 0, 0);
                                                    Vector supplier_Val = new Vector(1, 1);
                                                    Vector supplier_Key = new Vector(1, 1);
                                                    //supplier_Val.add("0");
                                                    //supplier_Key.add("-");
                                                    for (int i = 0; i < vSupplier.size(); i++) {
                                                        ContactList contactList = (ContactList) vSupplier.get(i);
                                                        supplier_Val.add("" + contactList.getOID());
                                                        supplier_Key.add("" + contactList.getContactCode() + " - " + contactList.getCompName());
                                                    }                                                    
                                                %>
                                                <%=ControlCombo.draw("FRM_SUPPLIER","form-control input-sm", null, ""+material.getSupplierId(), supplier_Val, supplier_Key, null)%>
                                            </div>

                                            <div class="col-sm-2">Kepemilikan</div>
                                            <div class="col-sm-4">
                                                <%  
                                                    objSrcMemberReg = new SrcMemberReg();
                                                    Vector vKepemilikan = SessMemberReg.searchKepemilikan(objSrcMemberReg, 0, 0);
                                                    Vector kepemilikan_Val = new Vector(1,1);
                                                    Vector kepemilikan_Key = new Vector(1,1);
                                                    //kepemilikan_Val.add("0");
                                                    //kepemilikan_Key.add("-");    
                                                    for(int i=0; i<vKepemilikan.size(); i++){
                                                        ContactList contactList = (ContactList) vKepemilikan.get(i);
                                                        kepemilikan_Val.add(""+contactList.getOID());
                                                        kepemilikan_Key.add(""+contactList.getContactCode()+" - "+contactList.getPersonName());
                                                    }
                                                %>
                                                <%=ControlCombo.draw("FRM_KEPEMILIKAN","form-control input-sm", null, ""+material.getKepemilikanId(), kepemilikan_Val, kepemilikan_Key, null)%>
                                            </div>
                                        </div>
                                            
                                        <div class="form-group">
                                            <div class="col-sm-2">Jenis</div>
                                            <div class="col-sm-4">
                                                <select name="FRM_CATEGORY" class="form-control input-sm">
                                                    <%
                                                        //long matDepartId = material.getCategoryId();
                                                        //add opie-eyek 20130821
                                                        Vector masterCatAcak = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                        Category newCategory = new Category();
                                                        Vector materGroup = PstCategory.structureList(masterCatAcak);

                                                        Vector vectGroupVal = new Vector(1, 1);
                                                        Vector vectGroupKey = new Vector(1, 1);
                                                        if (materGroup != null && materGroup.size() > 0) {
                                                            String parent = "";
                                                            Vector<Category> resultTotal = new Vector();
                                                            Vector<Long> levelParent = new Vector<Long>();
                                                            for (int i = 0; i < materGroup.size(); i++) {
                                                                Category mGroup = (Category) materGroup.get(i);                                                                
                                                                if (mGroup.getCatParentId() != 0) {
                                                                    for (int lv = levelParent.size() - 1; lv > -1; lv--) {
                                                                        long oidLevel = levelParent.get(lv);
                                                                        if (oidLevel == mGroup.getCatParentId()) {
                                                                            break;
                                                                        } else {
                                                                            levelParent.remove(lv);
                                                                        }
                                                                    }
                                                                    parent = "";
                                                                    for (int lv = 0; lv < levelParent.size(); lv++) {
                                                                        parent = parent + "&nbsp;&nbsp;";
                                                                    }
                                                                    levelParent.add(mGroup.getOID());

                                                                } else {
                                                                    levelParent.removeAllElements();
                                                                    levelParent.add(mGroup.getOID());
                                                                    parent = "";
                                                                }
                                                    %>
                                                    <option  <%=mGroup.getOID() == material.getCategoryId() ? "selected" : ""%> value="<%=mGroup.getOID()%>"><%=parent%><%= mGroup.getCode() + " - " + mGroup.getName()%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <div class="col-sm-2">Permata</div>
                                            <div class="col-sm-4">
                                                <%
                                                    String orderBy = PstMerk.fieldNames[PstMerk.FLD_NAME];
                                                    Vector listMaterialMerk = PstMerk.list(0, 0, "", orderBy);
                                                    Vector vectMerkVal = new Vector(1, 1);
                                                    Vector vectMerkKey = new Vector(1, 1);
                                                    if (listMaterialMerk != null && listMaterialMerk.size() > 0) {
                                                        for (int i = 0; i < listMaterialMerk.size(); i++) {
                                                            Merk matMerk = (Merk) listMaterialMerk.get(i);
                                                            vectMerkVal.add("" + matMerk.getCode() + " - " + matMerk.getName());
                                                            vectMerkKey.add("" + matMerk.getOID());
                                                        }
                                                    }
                                                    out.println(ControlCombo.draw("FRM_MERK", "form-control input-sm", null, "" + material.getMerkId(), vectMerkKey, vectMerkVal, null));
                                                %>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2">Kadar</div>
                                            <div class="col-sm-4">
                                                <select class="form-control input-sm" name="FRM_KADAR" id="itemKadar">
                                                    <%
                                                        Vector listKadar = PstKadar.list(0, 0, "", "");
                                                        for (int i = 0; i < listKadar.size(); i++) {
                                                            Kadar k = (Kadar) listKadar.get(i);                                                            
                                                    %>
                                                    <option <%=material.getPosKadar() == k.getOID() ? "selected" : ""%> value="<%=k.getOID()%>"><%=k.getKodeKadar()%> - <%=k.getKadar()%> %</option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <div class="col-sm-2">Warna</div>
                                            <div class="col-sm-4">                                    
                                                <select class="form-control input-sm" name="FRM_WARNA" id="itemWarna">
                                                    <%
                                                        Vector listColor = PstColor.list(0, 0, "", "");
                                                        for (int i = 0; i < listColor.size(); i++) {
                                                            Color c = (Color) listColor.get(i);                                                            
                                                    %>
                                                    <option <%=material.getPosColor()== c.getOID() ? "selected" : ""%> value="<%=c.getOID()%>"><%=c.getColorCode()%> - <%=c.getColorName()%></option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>                                        
                                        
                                        <div class="form-group">
                                            <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS){%>
                                            <div class="col-sm-2 text-bold">Harga Pesan</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>                                                    
                                                    <input type="text" readonly="" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]%>" value="<%=String.format("%,.0f", billdetail.getItemPrice())%>">
                                                </div>
                                            </div>
                                            <%}%>
                                                    
                                            <div class="col-sm-2 text-bold">Harga Pasar</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS){%>
                                                    <input type="text" readonly="" class="form-control input-sm itemChange hargaBaru" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>" value="<%=String.format("%,.0f", hargaEmasBaru) %>">
                                                    <%}%>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){%>
                                                    <input type="text" readonly="" class="form-control input-sm itemChange hargaBaru" id="" name="" value="<%=String.format("%,.0f", hargaEmasBaru) %>">
                                                    <%}%>
                                                </div>
                                            </div>
                                        </div>
                                                
                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <hr style="margin: 0px; border-color: lightgrey">
                                            </div>
                                        </div>
                                                
                                        <div class="form-group">
                                            <div class="col-sm-2">Qty</div>
                                            <div class="col-sm-4">
                                                <input type="text" readonly="" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]%>" value="1">
                                            </div>
                                            
                                            <div class="col-sm-2">Ongkos</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_COST]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_COST]%>" value="<%=String.format("%,.0f", billdetail.getCost()) %>">
                                                </div>
                                            </div>                                            
                                        </div>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-2">Berat Pesan</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <%
                                                        String readOnly = "";
                                                        if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                                            readOnly = dpLebihBesar ? "readonly":"";
                                                        } else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                                            readOnly = "";
                                                        }
                                                    %>
													<input type="hidden" value="<%=(dpLebihBesar ? "1" : "0")%>" id="beratMengikat">
													<input type="hidden" value="<%=String.format("%,.3f", billdetail.getBerat())%>" id="beratPesan">
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_BERAT]%>" value="<%=String.format("%,.3f", billdetail.getBerat())%>">
                                                    <span class="input-group-addon">gr</span>
                                                </div>
                                            </div>

                                            <div class="col-sm-2">Total Harga</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                                    
                                                    <span class="input-group-addon">Rp</span>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS){%>
                                                    <input readonly="" class="form-control input-sm itemChange text-right" id="hargaTotal" name="" value="<%=String.format("%,.0f", billdetail.getBerat() * realItemPrice) %>">
                                                    <%}%>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){%>
                                                    <input class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]%>" value="<%=String.format("%,.0f", billdetail.getItemPrice()) %>">
                                                    <%}%>
                                                </div>
                                            </div>
                                        </div>
                                                
                                        <%if(dpLebihBesar && material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS){%>
                                        <div class="form-group">
                                            <div class="col-sm-12 text-center"><small class="text-red text-bold">* HARGA MENGIKAT *</small></div>                                                                                      
                                        </div>
                                        <%}%>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-2"><nobr>Berat Tambah</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <input type="text" <%=dpLebihBesar ? "":"readonly"%> class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ADDITIONAL_WEIGHT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ADDITIONAL_WEIGHT]%>" value="<%=String.format("%,.3f", billdetail.getAdditionalWeight())%>">
                                                    <span class="input-group-addon">gr</span>
                                                </div>
                                            </div>
                                            <div class="col-sm-2"><nobr>Harga Tambah</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS){%>
                                                    <input type="text" readonly="" class="form-control input-sm itemChange text-right" id="hargaTambah" name="" value="<%=String.format("%,.0f", billdetail.getAdditionalWeight() * billdetail.getLatestItemPrice()) %>">
                                                    <%}%>
                                                    <%if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){%>
                                                    <input type="text" class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_LATEST_ITEM_PRICE]%>" value="<%=String.format("%,.0f", billdetail.getLatestItemPrice()) %>">
                                                    <%}%>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2"><nobr>Berat Susutan</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SUSUTAN_WEIGHT]%>" value="<%=String.format("%,.3f", billdetail.getSusutanWeight())%>">
                                                    <span class="input-group-addon">gr</span>
                                                </div>
                                            </div>
                                            <div class="col-sm-2"><nobr>Nilai Susutan</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
													<%
														double susutanPrice = 0.0;
														if (dpLebihBesar){
															susutanPrice = billdetail.getItemPrice() * billdetail.getSusutanWeight();
														} else {
															susutanPrice = billdetail.getSusutanWeight() * hargaEmasBaru;
														}
													%>
                                                    <input type="text" <%=(material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) ? "":"readonly"%> class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SUSUTAN_PRICE]%>" value="<%=String.format("%,.0f", susutanPrice)%>">
                                                </div>
                                            </div>
                                        </div>
                                             
                                        <div class="form-group">
                                            <div class="col-sm-6"></div>
                                            <label class="col-sm-2 control-label">Total</label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" readonly="" class="form-control input-sm text-right" id="total1" name="" value="<%=String.format("%,.2f", totalPrice) %>">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <hr style="margin: 0px; border-color: lightgrey">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2">Diskon</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                        
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC_PCT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC_PCT]%>" value="<%=billdetail.getDiscPct()%>">
                                                    <span class="input-group-addon">%</span>
                                                </div>
                                            </div>
                                            <div class="col-sm-2"><nobr>Nilai Diskon</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC]%>" value="<%=billdetail.getDisc()%>">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">                                            
                                            <div class="col-sm-2"><nobr>Saran Diskon</nobr></div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                        
                                                    <input type="text" readonly="" class="form-control input-sm" id="sugestDisc" name="" value="0">
                                                    <span id="applyDisc" class="btn btn-success input-group-addon">Gunakan</span>
                                                </div>
                                            </div>
                                            <label class="col-sm-2 control-label">Total</label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" readonly="" class="form-control input-sm text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_PRICE]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_PRICE]%>" value="<%=String.format("%,.2f", afterDiscount) %>">                                                    
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <hr style="margin: 0px; border-color: lightgrey">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-2">PPN</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">                                        
                                                    <input type="text" class="form-control input-sm itemChange" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TAX_PCT]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TAX_PCT]%>" value="<%=billdetail.getTaxPct()%>">
                                                    <span class="input-group-addon">%</span>
                                                </div>
                                            </div>
                                            <div class="col-sm-2">Nilai PPN</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" readonly="" class="form-control input-sm itemChange text-right" id="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_TAX]%>" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_TOTAL_TAX]%>" value="<%=billdetail.getTotalTax()%>">
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-6"></div>
                                            <label class="col-sm-2 control-label">Grand Total</label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" readonly="" class="form-control input-sm itemChange text-right" id="grandTotal" name="" value="<%=String.format("%,.2f", afterTax)%>">
                                                </div>
                                            </div>                                            
                                        </div>

                                        <%}%>
                                        
                                        <div class="form-group">
                                            <div class="col-sm-2">Catatan</div>
                                            <div class="col-sm-10">
                                                <textarea style="resize: none" class="form-control input-sm" name="<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_NOTE]%>"><%=billdetail.getNote()%></textarea>
                                            </div>
                                        </div>
										<% if (iCommand == Command.EDIT && material.getMaterialJenisType() != Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
										<div class="form-group">
                                            <div class="col-sm-12 text-center"><small class="text-red text-bold">* PENENTUAN HARGA POKOK *</small></div>                                                                                      
                                        </div>
										<div class="form-group">
                                            <div class="col-sm-2">HE/Harga Beli</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">    
													<span class="input-group-addon">Rp</span>
                                                    <input type="hidden" class="form-control input-sm text-right" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>" value="">
													<input type="text" class="form-control input-sm text-right" id="averagePrice" value="<%=(material.getAveragePrice() > 0 ? String.format("%,.0f", material.getAveragePrice()) : String.format("%,.0f", ((billdetail.getBerat() * billdetail.getItemPrice()) + (billdetail.getAdditionalWeight() * billdetail.getLatestItemPrice()))))%>">
                                                </div>
                                            </div>
                                            <div class="col-sm-2">Ongkos Batu</div>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="hidden" class="form-control input-sm text-right" id="pokok_ongkos" name="pokok_ongkos" value="">
													<input type="text" class="form-control input-sm text-right" id="inputOngkos" value="<%=(materialDetail.getOngkos() > 0 ? String.format("%,.0f", materialDetail.getOngkos()) : String.format("%,.0f", billdetail.getCost()))%>">
                                                </div>
                                            </div>
                                        </div>
										 <div class="form-group">
                                            <div class="col-sm-6"></div>
                                            <label class="col-sm-2 control-label">Total HPP</label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <span class="input-group-addon">Rp</span>
                                                    <input type="text" readonly="" class="form-control input-sm text-right" id="totalHPP" name="" value="">
                                                </div>
                                            </div>                                            
                                        </div>
										<div class="form-group">
                                            <div class="col-sm-2">Keterangan</div>
                                            <div class="col-sm-10">
                                                <textarea style="resize: none" class="form-control input-sm" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DESCRIPTION]%>"><%=material.getMaterialDescription()%></textarea>
                                            </div>
                                        </div>
									<%}%>
                                    </div>

                                    <%if (iErrorCode > 0) {%>
                                    <div class="label-danger text-center"><%=sErrorMsg%></div>
                                    <%} else {%>
                                    <div class="label-success text-center"><%=sErrorMsg%></div>
                                    <%}%>

                                </div>
                                <%if (iCommand == Command.ADD || iCommand == Command.EDIT) {%>
                                <div class="modal-footer">
                                    <button type="button" id="btnSaveBillDetail" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</button>                                    
                                    <button type="button" class="btn btn-sm btn-default" data-dismiss="modal"><i class="fa fa-remove"></i>&nbsp; Keluar</button>
                                </div>
                                <%}%>
                            </div>

                        </div>
                    </div>
                    
                    <%}%>
                    
                    <div id="modalProduksi" class="modal fade" role="dialog">
                        <div class="modal-dialog" style="width: 30%">
                            
                            <div class="modal-content">
                                
                                <div class="modal-header mo">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Transfer Produksi</h4>
                                </div>
                                
                                <div class="modal-body">
                                    <div class="form-horizontal">
                                        
                                        <div class="form-group">
                                            <label class="col-sm-5">Tanggal Produksi</label>
                                            <div class="col-sm-7">
                                                <input type="text" id="tglProduksi" class="form-control input-sm datetimepick" name="FRM_TGL_PRODUKSI" value="<%=Formater.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") %>">
                                            </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-sm-5">Lokasi Asal</label>
                                            <div class="col-sm-7">
                                                <%
                                                    Vector val_locationAsal = new Vector(1, 1);
                                                    Vector key_locationAsal = new Vector(1, 1);
                                                    val_locationAsal.add("");
                                                    key_locationAsal.add("- Pilih lokasi awal -");
                                                            
                                                    String whereLocationAsal = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";
                                                    whereLocationAsal += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                    Vector vt_locationAsal = PstLocation.listLocationCreateDocument(0, 0, whereLocationAsal, "");
                                                    for (int d = 0; d < vt_locationAsal.size(); d++) {
                                                        Location loc = (Location) vt_locationAsal.get(d);
                                                        val_locationAsal.add("" + loc.getOID() + "");
                                                        key_locationAsal.add(loc.getName());
                                                    }
                                                %>
                                                <%=ControlCombo.draw("FRM_LOKASI_AWAL", null, "", val_locationAsal, key_locationAsal, "", "form-control input-sm loc_asal")%>                                                
                                            </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-sm-5">Etalase Asal</label>
                                            <div class="col-sm-7">
                                                <select class="form-control input-sm eta_asal" name="FRM_ETALASE_AWAL">
                                                    <option value="">- Pilih lokasi awal -</option>
                                                </select>
                                            </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label class="col-sm-5">Lokasi Tujuan</label>
                                            <div class="col-sm-7">
                                                <%
                                                    Vector val_locationTujuan = new Vector(1, 1);
                                                    Vector key_locationTujuan = new Vector(1, 1);
                                                    val_locationTujuan.add("");
                                                    key_locationTujuan.add("- Pilih lokasi tujuan -");
                                                    
                                                    //String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                    //String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                    //Vector vt_locationTujuan = PstLocation.list(0, 0, locWhClause, locOrderBy);
                                                    String whereLocationTujuan = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";
                                                    whereLocationTujuan += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                    Vector vt_locationTujuan = PstLocation.listLocationCreateDocument(0, 0, whereLocationTujuan, "");                                                    
                                                    for (int d = 0; d < vt_locationTujuan.size(); d++) {
                                                        Location loc1 = (Location) vt_locationTujuan.get(d);
                                                        val_locationTujuan.add("" + loc1.getOID() + "");
                                                        key_locationTujuan.add(loc1.getName());
                                                    }
                                                %>
                                                <%=ControlCombo.draw("FRM_LOKASI_TUJUAN", null, "", val_locationTujuan, key_locationTujuan, "", "form-control input-sm loc_tujuan")%>
                                            </div>
                                        </div>
                                                                                    
                                        <div class="form-group">
                                            <label class="col-sm-5">Etalase Tujuan</label>
                                            <div class="col-sm-7">
                                                <select class="form-control input-sm eta_tujuan" name="FRM_ETALASE_TUJUAN">
                                                    <option value="">- Pilih lokasi tujuan -</option>
                                                </select>
                                            </div>
                                        </div>                                        
                                        
                                    </div>                                        
                                </div>
                                
                                <div class="modal-footer">
                                    <button type="button" id="btnProduksi" class="btn btn-sm btn-primary"><i class="fa fa-gears"></i>&nbsp; Proses</button>                                    
                                    <button type="button" class="btn btn-sm btn-default" data-dismiss="modal"><i class="fa fa-remove"></i>&nbsp; Batal</button>
                                </div>
                                
                            </div>
                            
                        </div>
                    </div>

                </form>

            </div>

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Daftar Sales Order [<%=tipeOrderTitle[tipeOrder] %>]</h3>
                </div>

                <div class="box-body">
                    <%if (listSalesOrder.size() > 0) {%>

                    <table class="table table-bordered tabel_data">
                        <tr class="label-primary">
                            <th class="text-center" style="width: 1%">No.</th>
                            <th class="text-center">Tanggal</th>                            
                            <th class="text-center">Nomor Order</th>
                            <th class="text-center" style="width: 1%">Qty</th>
                            <th class="text-center">Nama Barang</th>
                            <th class="text-center">Catatan</th>
                            <th class="text-center">Aksi</th>
                        </tr>

                        <%
                            for (int i = 0; i < listSalesOrder.size(); i++) {
                                BillMain bm = (BillMain) listSalesOrder.get(i);
                                long billOrder = bm.getOID();
                                //get item order
                                Vector<Billdetail> listItemOrder = PstBillDetail.list(0, 0, "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billOrder, "");
                                String itemOrder = "";
                                double qtyOrder = 0;
                                for (Billdetail item : listItemOrder) {
                                    itemOrder += (itemOrder.length() > 0) ? " <b>,</b> " + item.getItemName() : item.getItemName();
                                    qtyOrder += item.getQty();
                                }
                                //get bill invoice
                                Vector<BillMain> listInvoice = PstBillMain.list(0, 0, PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = " + billOrder, "");
                        %>

                        <tr>
                            <td class="text-center"><%=(i + 1)%></td>                            
                            <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd")%></td>
                            <td class="text-center"><%=bm.getInvoiceNo()%></td>
                            <td class="text-center"><%=String.format("%,.0f", qtyOrder)%></td>
                            <td><%=itemOrder%></td>
                            <td><%=bm.getNotes()%></td>
                            <td class="text-center" style="max-width: 70px">
                                <%if (listInvoice.isEmpty()) {%>
                                <a class="duplicate_order" data-idselect="<%=billOrder%>"><nobr>Invoice Baru</nobr></a>
                                <%} else {%>
                                <a class="view_detail" data-idselect="<%=billOrder%>"><nobr>Detail Invoice</nobr></a>
                                <%}%>
                            </td>
                        </tr>

                        <%if (idBillMain == billOrder) {%>
                        <tr>
                            <td></td>
                            <td colspan="6">
                                
                                <%
                                    for (BillMain bi : listInvoice) {
                                        //status order
                                        boolean invoiceDraft = true;//bisa edit bill detail, bisa tambah lantakan, muncul tombol proses
                                        boolean orderInProgress = false;//muncul pesan menunggu proses final transfer dan penerimaan
                                        boolean invoiceReady = false;//muncul tombol selesai untuk update status invoice
                                        boolean productionFinish = false;//muncul pesan produksi sudah selesai
                                        boolean canProses = true;
										boolean productionCancel = false;
                                        
                                        Vector<MatDispatch> listTransfer = PstMatDispatch.list(0, 0, PstMatDispatch.fieldNames[PstMatDispatch.FLD_ID_BILL_MAIN_SALES_ORDER] + " = " + bi.getOID() , "");                                        
                                        boolean transferClosed = false;
                                        if (!listTransfer.isEmpty()) {
                                            invoiceDraft = false;
                                            orderInProgress = true;
                                            transferClosed = listTransfer.get(0).getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED;
                                        }
                                        boolean receiveClosed = false;
                                        Vector<MatReceive> listReceive = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER] + " = " + bi.getOID() , "");
                                        if (!listReceive.isEmpty()) {
                                            invoiceDraft = false;
                                            orderInProgress = true;
                                            receiveClosed = listReceive.get(0).getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED;
                                        }
                                        
                                        if (transferClosed && receiveClosed) {                                            
                                            orderInProgress = false;
                                            invoiceReady = true;
                                        }
                                        
                                        String finalWait = (transferClosed) ? "":"transfer";
                                        if (transferClosed == false && receiveClosed == false) {
                                            finalWait += " dan ";
                                        }
                                        finalWait += (receiveClosed) ? "":"penerimaan";
                                        
                                        if (bi.getStatusInv() == PstBillMain.INVOICING_FINISH || bi.getTransactionStatus() == PstBillMain.TRANS_STATUS_CLOSE) {
                                            invoiceReady = false;
                                            productionFinish = true;
                                        }
										
										 if ((bi.getDocType()== PstBillMain.TRANS_TYPE_CASH && bi.getTransctionType()== PstBillMain.NONE && bi.getStatusInv() == PstBillMain.INVOICING_ON_PROSES && bi.getTransactionStatus() == PstBillMain.TRANS_STATUS_DELETED)
												 || (bi.getStatusInv() == PstBillMain.INVOICING_ON_PROSES && bi.getTransactionStatus() == PstBillMain.TRANS_STATUS_CANCELED)) {
                                            invoiceReady = false;
                                            productionCancel = true;
                                        }
                                        
                                        //cek ada emas lantakan atau tidak
                                        boolean containsLantakan = false;//nilai true jika bill main memiliki item emas lantakan
                                        String whereLantakan = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = " + bi.getOID()
                                                + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = " + Material.MATERIAL_TYPE_EMAS_LANTAKAN;
                                        Vector listLantakan = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, whereLantakan, "");
                                        if (listLantakan.size() > 0) {
                                            containsLantakan = true;
                                        }
                                        
                                %>
                                
                                <label>Nomor Invoice : <%=bi.getInvoiceNo() %></label>
                                <table class="table table-bordered tabel_detail">
                                    <tr class="label-default">
                                        <th>SKU</th>
                                        <th>Nama Barang</th>
                                        <th>Qty</th>
                                        <th>Berat</th>
                                        <th>Harga Item</th>
                                        <th>Susutan</th>
                                        <th>Diskon</th>
                                        <th>PPN</th>                                        
                                        <th>Ongkos</th>
                                        <th>Catatan</th>
                                        <%--<th style="width: 1%"><i class="fa fa-trash"></i></th>--%>
                                    </tr>
                                    
                                    <%
                                        //get invoice detail
                                        Vector<Billdetail> listInvoiceDetail = PstBillDetail.list(0, 0, "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + bi.getOID(), "");
                                        String sku = "";
                                        for (Billdetail bd : listInvoiceDetail) {
                                            Material newMat = new Material();
                                            try {
                                                newMat = PstMaterial.fetchExc(bd.getMaterialId());
                                            } catch (Exception e) {                                                
                                                System.out.println(e.getMessage());
                                            }
                                            String itemName = SessMaterial.setItemNameForLitama(newMat.getOID());
                                            sku = bd.getSku();
                                            if (sku.length() <= 7 && newMat.getMaterialJenisType() != Material.MATERIAL_TYPE_EMAS_LANTAKAN){
                                                canProses = false;
                                            }
                                    %>

                                    <tr>
                                        <td class="">
                                            <%=bd.getSku()%>
                                            <%if (invoiceDraft && newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                                            <a href="javascript:deleteLantakan('<%=bi.getOID()%>','<%=bd.getOID()%>','<%=bd.getSku()%>')" class="pull-right text-red" title="Hapus emas lantakan"><i class="fa fa-remove"></i></a>
                                            <%}%>
                                        </td>
                                        <%if (invoiceDraft) {%>
                                        <td><a href="javascript:editBillDetail('<%=bi.getOID()%>','<%=bd.getOID()%>')"><%=itemName%>&nbsp; <i class="fa fa-pencil"></i></a></td>                                        
                                        <%}else{%>
                                        <td><%=itemName%></td>
                                        <%}%>
                                        <td class="text-center"><%=String.format("%,.0f", bd.getQty())%></td>
                                        <td class="text-right"><%=String.format("%,.3f", bd.getBerat()+bd.getAdditionalWeight())%></td>
                                        <td class="text-right"><%=String.format("%,.0f", bd.getItemPrice())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", bd.getSusutanPrice())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", bd.getDisc()) %>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", bd.getTotalTax()) %>.00</td>                                        
                                        <td class="text-right"><%=String.format("%,.0f", bd.getCost())%>.00</td>
                                        <td class="long_text" style="max-width: 170px" data-toggle="" title="<%=bd.getNote()%>"><%=bd.getNote()%></td>
                                        <td class="hidden text-center"><a class="btnDeleteBillDetail text-red" data-idselect="<%=bi.getOID()%>" data-id-delete="<%=bd.getOID()%>"><i class="fa fa-remove"></i></a></td>
                                    </tr>

                                    <%
                                        }
                                    %>
                                </table>

                                <%if (invoiceDraft) {%>
                                <div class="row">
                                    <div class="col-sm-6">
                                    <%if (!containsLantakan) {%>
                                        <button type="button" id="" class="btn btn-xs btn-primary btnAddLantakan" data-idselect="<%=bi.getOID()%>"><i class="fa fa-plus"></i>&nbsp; Tambah Emas Lantakan</button>
                                    <%}%>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="javascript:prosesInvoice('<%=bi.getOID()%>',<%=canProses%>)" class="btn btn-xs btn-warning pull-right"><i class="fa fa-gear"></i>&nbsp; Proses</a>
                                    </div>
                                </div>
                                <p></p>
                                <%}%>
                                
                                <% if (orderInProgress) {%>
                                <div class="text-center text-red">Menunggu proses final <%=finalWait%>...</div>
                                <%}%>
                                
                                <% if (invoiceReady) {%>                                
                                <div class="text-right"><a href="javascript:finishProduksi('<%=bi.getOID()%>','<%=bi.getInvoiceNo()%>')" class="btn btn-xs btn-success"><i class="fa fa-check-square"></i>&nbsp; Selesai</a></div>
                                <%}%>

                                <% if (productionFinish) {%>
                                <div class="text-center text-green text-bold">- Produksi selesai -</div>
                                <%}%>
                                <% if (productionCancel) {%>
                                <div class="text-center text-red text-bold">- Order Canceled -</div>
                                <%}%>
                                <%
                                    }
                                %>
                                
                                <hr style="margin: 10px 0px; border-color: lightgray">
                                
                                <div class="pull-right">
                                    <button type="button" id="btnBack" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i>&nbsp; Kembali</button>                                    
                                </div>

                            </td>
                        </tr>
                        <%}%>

                        <%
                            }
                        %>

                    </table>                        

                    <%} else {%>
                    <div>Tidak ada data sales order <%= (statusInvoicing == 0) ? "" : "yang sudah diproduksi"%></div>
                    <%}%>

                </div>
                    
                <%--if (idBillMain > 0) {--%>
                    
                <div class="box-footer" style="border-color: lightgrey">

                    <!--ERROR MESSAGE-->
                    <%if (iErrorCode > 0) {%>
                    <div class="label-danger text-center"><%=sErrorMsg%></div>
                    <%} else {%>
                    <div class="label-success text-center"><%=sErrorMsg%></div>
                    <%}%>
                    <!--END ERROR MESSAGE-->

                </div>
                
                <%--}--%>

            </div>

        </div>

    </body>
</html>
