<%-- 
    Document   : detail_penjualan
    Created on : Nov 8, 2019, 7:32:52 PM
    Author     : WiraDharma
--%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.sedana.entity.masterdata.JangkaWaktu"%>
<%@page import="com.dimata.sedana.entity.masterdata.PstJangkaWaktu"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialTypeMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialMappingType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.aiso.entity.masterdata.anggota.PstVocation"%>
<%@page import="com.dimata.aiso.entity.masterdata.anggota.Vocation"%>
<%@page import="com.dimata.pos.form.billing.FrmBillDetail"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.sedana.entity.tabungan.JenisTransaksi"%>
<%@page import="com.dimata.sedana.entity.masterdata.BiayaTransaksi"%>
<%@page import="com.dimata.sedana.entity.tabungan.PstJenisTransaksi"%>
<%@page import="com.dimata.sedana.entity.masterdata.PstBiayaTransaksi"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequestItem"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.sedana.entity.kredit.PstPenjamin"%>
<%@page import="com.dimata.sedana.entity.kredit.Penjamin"%>
<%@page import="com.dimata.sedana.entity.kredit.AgunanMapping"%>
<%@page import="com.dimata.sedana.entity.kredit.PstAgunanMapping"%>
<%@page import="com.dimata.sedana.entity.kredit.PstAgunan"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.aiso.entity.masterdata.anggota.Anggota"%>
<%@page import="com.dimata.aiso.entity.masterdata.anggota.PstAnggota"%>
<%@page import="com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import="com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit"%>
<%@page import="com.dimata.sedana.entity.kredit.Agunan"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.sedana.entity.kredit.PstPinjaman"%>
<%@page import="com.dimata.sedana.entity.kredit.Pinjaman"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file ="../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION);%>
<%@ include file ="../../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!  public static final String textListGlobal[][] = {
        {"Produksi", "Daftar Penjualan", "Data Permohonan", "Data Agunan", "Data Analis", "Data Pengajuan", "Data Penjamin", "Data Kolektor", "Data Pengiriman", "Keterangan", "Daftar Petugas Pengiriman", "Membership", "Atur Jadwal", "Cek Stok Pemesanan", "Status Pemesanan", "Stock Pemesanan", "Store Request", "Data Biaya"},
        {"Produksi", "Daftar Penjualan", "Data Permohonan", "Data Agunan", "Data Analyst", "Data Pengajuan", "Data Penjamin", "Data Kolektor", "Data Pengiriman", "Keterangan", "Daftar Petugas Pengiriman", "Membership", "Atur Jadwal", "Cek Stok Pemesanan", "Status Pemesanan", "Stock Pemesanan", "Store Request", "Data Biaya"}
    };
    public static final String textListHeader[][] = {
        {"Nama", "Pemohon", "Nomor KTP", "Alamat", "Pekerjaan", " Nomor Telp/HP", "Tanggal Pengajuan", "Jenis Kredit", "List Barang", "Jumlah Pengajuan", "Suku Bunga", "Jangka Waktu", "Kendaraan Bermotor", "BPKB", "Alamat Agunan", "Bukti Kepemilikan", "Nilai NJOP", "Nilai Ekonomis", "Nilai Jaminan", "Presentase Pinjaman", "Tidak ada pinjaman", "Jadwal Pengiriman", "Nama Petugas Delivery", "Kode Petugas Delivery", "Dijamin Sebesar", "No", "Tipe Agunan", "Dokumen", "Bill Number"},
        {"Nama", "Pemohon", "Nomor KTP", "Alamat", "Pekerjaan", " Nomor Telp/HP", "Tanggal Pengajuan", "Jenis Kredit", "List Barang", "Jumlah Pengajuan", "Suku Bunga", "Jangka Waktu", "Kendaraan Bermotor", "BPKB", "Alamat Agunan", "Bukti Kepemilikan", "Nilai NJOP", "Nilai Ekonomis", "Nilai Jaminan", "Presentase Pinjaman", "Tidak ada pinjaman", "Jadwal Pengiriman", "Nama Petugas Delivery", "Kode Petugas Delivery", "Guaranteed Amout", "No", "Collateral Type", "Document", "Bill Number"}};

    public static final String textHeaderTable[][] = {
        {"No", "Nomor Pengiriman", "Nomor Pemesanan", "Kode", "Petugas", "Nama", "Jadwal Pengiriman", "Status", "Perusahaan", "Alamat", "Telp", "HP", "Member ID", "Kredit Limit", "Consigment Limit", "Detail Pot", "SKU", "Barcode", "Nama Barang", "Category Barang", "Unit", "Merk", "Real Stock", "Aksi", "Qty"},
        {"No", "Nomor Pengiriman", "Nomor Pemesanan", "Kode", "Petugas", "Nama", "Jadwal Pengiriman", "Status", "Perusahaan", "Alamat", "Telp", "HP", "Member ID", "Kredit Limit", "Consigment Limit", "Detail Pot", "SKU", "Barcode", "Nama Barang", "Category Barang", "Unit", "Merk", "Real Stock", "Action", "Qty"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"No", "Lokasi Asal", "Tanggal", "Supplier", "Contact", "Alamat", "Telp.", "Terms", "Days", "Ppn", "Ket.", "Mata Uang", "Gudang", "Store Request", "Nomor Revisi", "Status", "Include", "%", "Term Of Payment", "Gudang", "Lokasi Tujuan", "Request Transfer"},
        {"No", "Location", "Date", "Supplier", "Contact", "Addres", "Phone", "Terms", "Days", "Ppn", "Remark", "Currency", "Warehouse", "Store Request", "Revisi Number", "Status", "Include", "%", "Term Of Payment", "Gudang", "Lokasi Request", "Request Transfer"}
    };

    public static final String dataTableTitle[][] = {
        {
            "Tampilkan _MENU_ data per halaman",
            "Data Tidak Ditemukan",
            "Menampilkan halaman _PAGE_ dari _PAGES_",
            "Belum Ada Data",
            "(Disaring dari _MAX_ data)",
            "Pencarian :",
            "Awal",
            "Akhir",
            "Berikutnya",
            "Sebelumnya"
        }, {
            "Display _MENU_ records per page",
            "Nothing found - sorry",
            "Showing page _PAGE_ of _PAGES_",
            "No records available",
            "(filtered from _MAX_ total records)",
            "Search :",
            "First",
            "Last",
            "Next",
            "Previous"}
    };

%>

<%  long oidCBM = FRMQueryString.requestLong(request, "oid_cbm");
    long oidPinjaman = 0;
    int iCommand = FRMQueryString.requestCommand(request);
    int enableTabungan = Integer.parseInt(PstSystemProperty.getValueByName("SEDANA_ENABLE_TABUGAN"));
    String apiUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
    String apiSedanaUrl = PstSystemProperty.getValueByName("SEDANA_URL");
    String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
    String oidDelivery = PstSystemProperty.getValueByName("STAFF_DELIVERY_OID");
    String msg = "";
    String whereClause = "";

    Pinjaman pinjaman = new Pinjaman();
    Anggota anggota = new Anggota();
    JenisKredit jenisKredit = new JenisKredit();
    Anggota accOfficer = new Anggota();
    Anggota kolektor = new Anggota();
    BillMain cbm = new BillMain();
    Employee emp = new Employee();
    Vocation voc = new Vocation();
    Vector listBarang = new Vector(1, 1);
    Vector listAgunanMap = new Vector(1, 1);
    Vector listPenjamin = new Vector(1, 1);
    Pinjaman p = new Pinjaman();
    JangkaWaktu jk = new JangkaWaktu();
    ArrayList listData = new ArrayList();
    JSONObject objTransaksi = new JSONObject();
    JSONObject objBiaya = new JSONObject();

    String numEmp = "";
    String nameEmp = "";
    String phoneAO = "";
    String phoneCol = "";
    String AOname = "";
    String Colname = "";
    String custName = "";
    String tipeTransaksi = "";

    try {
        cbm = PstBillMain.fetchExc(oidCBM);
        Pinjaman pin = new Pinjaman();

        whereClause = PstPinjaman.fieldNames[PstPinjaman.FLD_CASH_BILL_MAIN_ID] + "=" + cbm.getOID();
        Vector listPinjaman = PstPinjaman.list(0, 0, whereClause, "");
        if (listPinjaman.size() > 0) {
            pin = (Pinjaman) listPinjaman.get(0);
        }

        try {
            if (cbm.getCustomerId() > 0) {
                anggota = PstAnggota.fetchExc(cbm.getCustomerId());
                custName = anggota.getName();
            } else {
                anggota = PstAnggota.fetchExc(pin.getAnggotaId());
                custName = anggota.getName();
            }
        } catch (Exception e) {
        }
        if (anggota.getOID() == 0) {
            if (!cbm.getGuestName().equals("")) {
                custName = cbm.getGuestName();
            } else {
                custName = "-";
            }
        }
        if (anggota.getVocationId() != 0) {
            voc = PstVocation.fetchExc(anggota.getVocationId());
        }
        if (cbm.getDoPersonId() != 0) {
            JSONArray jArr = new JSONArray();
            String url = hrApiUrl + "/employee/employee-fetch/" + cbm.getDoPersonId();
            JSONObject jo = WebServices.getAPI("", url);
            if (jo.length() > 0) {

                numEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], "-");
                nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

            }
        }

        String url = apiUrl + "/kredit/pengajuan/status-by-bill/" + cbm.getOID();
        JSONObject jsonObj = WebServices.getAPI("", url);
        PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
        oidPinjaman = pinjaman.getOID();
        //Using to JangkaWaktu
        String linkJangkaWaktu = apiUrl + "/kredit/jangka-waktu/" + pinjaman.getJangkaWaktuId();
        JSONObject objJangkaWaktu = WebServices.getAPI("", linkJangkaWaktu);
        long oidJangkaWaktu = PstJangkaWaktu.syncExc(objJangkaWaktu);
        if (oidJangkaWaktu != 0) {
            jk = PstJangkaWaktu.fetchExc(oidJangkaWaktu);
        }

    } catch (Exception e) {
        msg = e.getMessage();
    }
    try {
        accOfficer = PstAnggota.fetchExc(pinjaman.getAccountOfficerId());
        String url = hrApiUrl + "/employee/employee-fetch/" + pinjaman.getAccountOfficerId();
        JSONObject objAO = WebServices.getAPI("", url);
        if (objAO.length() > 0) {
            AOname = objAO.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
            phoneAO = objAO.optString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], "-");
        }
        kolektor = PstAnggota.fetchExc(pinjaman.getCollectorId());
        String urll = hrApiUrl + "/employee/employee-fetch/" + pinjaman.getCollectorId();
        JSONObject objCol = WebServices.getAPI("", urll);
        if (objCol.length() > 0) {
            Colname = objCol.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
            phoneCol = objCol.optString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], "-");
        }

        jenisKredit = PstJenisKredit.fetch(pinjaman.getTipeKreditId());

        whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + cbm.getOID();
        listBarang = PstBillDetail.list(0, 0, whereClause, "");

        whereClause = PstAgunanMapping.fieldNames[PstAgunanMapping.FLD_PINJAMAN_ID] + "=" + pinjaman.getOID();
        listAgunanMap = PstAgunanMapping.list(0, 0, whereClause, "");

        whereClause = PstPenjamin.fieldNames[PstPenjamin.FLD_PINJAMAN_ID] + "=" + pinjaman.getOID();
        listPenjamin = PstPenjamin.list(0, 0, whereClause, "");

    } catch (Exception e) {
    }

    //data biaya
    
    try {
        String tempUrl = apiUrl + "/kredit/pinjaman/data-biaya/" + oidPinjaman;
        JSONObject tempObj = WebServices.getAPI("", tempUrl);
        int err = tempObj.optInt("ERROR", 0);

        if (err == 0) {
            JSONArray array = tempObj.getJSONArray("DATA");
            for (int i = 0; i < array.length(); i++) {
                JSONArray temp = array.optJSONArray(i);
                objTransaksi = (JSONObject) temp.get(0);
                objBiaya = (JSONObject) temp.get(1);
                ArrayList data = new ArrayList();
                data.add(objTransaksi.optString(PstJenisTransaksi.fieldNames[PstJenisTransaksi.FLD_JENIS_TRANSAKSI], ""));
                data.add(objBiaya.optDouble(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_VALUE_BIAYA], 0));
                data.add(objBiaya.optInt(PstBiayaTransaksi.fieldNames[PstBiayaTransaksi.FLD_TIPE_BIAYA], 0));
                listData.add(data);
            }
        }

        if (jenisKredit.getNamaKredit().equals("")) {
            tipeTransaksi = PstBillMain.payType[0];
        } else {
            tipeTransaksi = PstBillMain.payType[1];
        }
    } catch (Exception e) {
    }
    int vount = 0;
    int record = 0;
    String where = "";
    String order = "";
    Vector<Location> listLocation = PstLocation.list(vount, record, where, order);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../../../styles/plugin_component.jsp" %>
        <title>Dimata - ProChain POS</title>
        <style>
            span.input-group-addon {
                padding: 5px;
            }
            button.btn.btn-sm.btn-danger.pull-right {
                margin-left: 10px;
                width: 10%;
            }
            button.btn.btn-sm.btn-success.pull-right {
                margin-left: 10px;
                width: 10%;
            }
            input.form-control.input-sm.datePicker {
                padding: 5px;
            }
            body {background-color: #EEE}

            .box .box-header, .box .box-footer {border-color: lightgray}

            .datetimepicker th {font-size: 14px}
            .datetimepicker td {font-size: 12px}

            .tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
                font-size: 12px;
                padding: 5px;
                border-color: #DDD;
            }

            .tabel-data tbody tr th {
                text-align: center;
                white-space: nowrap;
                text-transform: uppercase;
            }

            .form-group label {padding-top: 5px}
            .table, form {margin-bottom: 0px}
            .middle-inline {text-align: center; white-space: nowrap}
            th {
                text-align: center;
                font-size: 12px;
            }
            thead.headerList {
                background-color: #3c8dbc;
                color: #fff;
                text-align: center;
            }
            td {
                font-size: 14px;
                text-align: center;
                border: 1px solid #428bca !important;
            }
            table.dataTable thead > tr > th {
                padding-right: 25px !important;
            }
            th.sorting {
                padding: 0px !important;
                padding-bottom: 7px !important;
                padding-left: 5px !important;
            }
            .pad-top-8{
                padding: 8px 0px 0px 0px;
            }
            .pad-0{
                padding: 0;
            }
            p {
                margin: 0 0 5px;
                font-size: 12px;
            }
            .row{
                margin:  0px;
            }
            .pad-left-0{
                padding-left: 0;
            }
            /* =========
      Get Fonts */

            /* ================
               Assign Variables */
            /* ===========================
               Setup Mixins/Helper Classes */
            .clearfix:after, .container:after, .tab-nav:after {
                content: ".";
                display: block;
                height: 0;
                clear: both;
                visibility: hidden;
            }

            /* ==========
               Setup Page */
            *, *:before, *:after {
                box-sizing: border-box;
            }

            body {
                padding: 0em 3em 3em 3em;
            }
            .content-header {
                position: relative;
                padding: 15px 0px 15px 0px;
            }

            /* ===========
               Tab Styling */
            .tab-group {
                position: relative;
                border: 1px solid #eee;
                margin-top: 2.5em;
                border-radius: 0 0 10px 10px;
            }
            .tab-group section {
                opacity: 0;
                height: 0;
                padding: 0 1em;
                overflow: hidden;
                transition: opacity 0.4s ease, height 0.4s ease;
            }
            .tab-group section.active {
                opacity: 1;
                height: auto;
                overflow: visible;
            }

            .tab-nav {
                list-style: none;
                margin: -2.5em -1px 0 0;
                padding: 0;
                height: 2.5em;
                overflow: hidden;
            }
            .tab-nav li {
                display: inline;
            }
            .tab-nav li a {
                top: 1px;
                position: relative;
                display: block;
                float: left;
                border-radius: 10px 10px 0 0;
                background: #eee;
                line-height: 2em;
                padding: 0 1em;
                text-decoration: none;
                color: grey;
                margin-top: .5em;
                margin-right: 1px;
                transition: background .2s ease, line-height .2s ease, margin .2s ease;
            }
            .tab-nav li.active a {
                background: #3c8dbc;
                color: white;
                line-height: 2.5em;
                margin-top: 0;
            }
            .box {
                position: relative;
                border-radius: 3px;
                background: #ffffff;
                border-top: 3px solid #d2d6de;
                margin-bottom: 20px;
                width: 100%;
                box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
            }
            .tab-group section {
                padding: 0 0em;
                transition: opacity 0.4s ease, height 0.4s ease;
            }
            .group-me {
                display: flex;
            }
            a#modal-petugas {
                border-radius: 0px;
                padding: 8px 0px 0px 0px;
            }
            .mg-top-10{
                margin-top: 10px
            } 
            .mg-right-10{
                margin-right: 10px
            }

            .modal-dialog {
                width: 85%;
                margin: 10% auto;
            }
            pad-0{
                padding-left: 0px;
                padding-right: 0px;
            }
            .beauty{
                border-top: 3px solid #3c8dbc;
                background-color: #eeeeee;
                min-height: 10%;
                padding: 10px;
            }
            .formElemen{
                width: 30%;
                float: left;
                margin: 4px;
            }

        </style>
    </head>
    <body>
        <section class="content-header">
            <h1><%=textListGlobal[SESS_LANGUAGE][0]%><small> <%=textListGlobal[SESS_LANGUAGE][1]%></small> </h1>
            <ol class="breadcrumb">
                <li><%=textListGlobal[SESS_LANGUAGE][0]%></li>
            </ol>
        </section>
        <div class="tab-group">
            <section id="tab1" title="<%=textListGlobal[SESS_LANGUAGE][12]%>">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h4 class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][2]%></h4>
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-sm-12">
                                <form id="frmSrcPenjualan" class="form-horizontal" method="post" action="">
                                    <div class="row">
                                        <!--LEFT SIDE-->
                                        <div class="col-sm-4 pad-0">
                                            <label class="col-sm-12 pad-top-8"><%=textListGlobal[SESS_LANGUAGE][2]%></label> <!--Data Pemohonan-->
                                            <%if(tipeTransaksi.equals("Cash")){%>
                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][28]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%= cbm.getInvoiceNo()%></p>
                                            <%}else{%>
                                            
                                            <p class="col-sm-5  pad-0">No PK</p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%=pinjaman.getNoKredit().equals("") ? "-" : pinjaman.getNoKredit() %></p>
                                            <%}%>

                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][0]%> <%=textListHeader[SESS_LANGUAGE][1]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%=custName%></p>

                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][2]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%if (anggota.getIdCard().equals("")) {%> - <%} else {%><%= anggota.getIdCard()%><%}%></p>

                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][3]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%if (anggota.getAddressPermanent().equals("")) {%> - <%} else {%><%= anggota.getAddressPermanent()%><%}%></p>

                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][4]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%if (voc.getVocationName().equals("")) {%> - <%} else {%><%= voc.getVocationName()%><%}%></p>

                                            <p class="col-sm-5  pad-0"><%=textListHeader[SESS_LANGUAGE][5]%></p>
                                            <p class="col-sm-1 pad-0">:</p>
                                            <p class="col-sm-6  pad-0"><%if (anggota.getHandPhone().equals("")) {%> - <%} else {%><%= anggota.getHandPhone()%><%}%></p>
                                        </div>

                                        <!--CENTER SIDE-->
                                        <div class="col-md-4">
                                            <div class="row">
                                                <div class="col-sm-12 pad-0">
                                                    <label class="col-sm-12 pad-top-8"><%=textListGlobal[SESS_LANGUAGE][4]%></label><!--Data AOA-->
                                                    <div class="col-sm-4  pad-0">
                                                        <p><%=textListHeader[SESS_LANGUAGE][0]%></p>
                                                        <p><%=textListHeader[SESS_LANGUAGE][5]%></p>
                                                    </div>
                                                    <div class="col-sm-1  pad-0">
                                                        <p>:</p>
                                                        <p>:</p>
                                                    </div>
                                                    <div class="col-sm-7  pad-0">
                                                        <p><%= ((AOname.equals("")) ? "-" : AOname)%></p>
                                                        <p><%= ((phoneAO.equals("")) ? "-" : phoneAO)%></p>
                                                    </div>
                                                </div>								  
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12 pad-top-8">
                                                    <label class="col-sm-12 pad-top-8"><%=textListGlobal[SESS_LANGUAGE][7]%></label><!--Data Kolektor-->
                                                    <div class="col-sm-4  pad-0">
                                                        <p><%=textListHeader[SESS_LANGUAGE][0]%></p>
                                                        <p><%=textListHeader[SESS_LANGUAGE][5]%></p>
                                                    </div>
                                                    <div class="col-sm-1  pad-0">
                                                        <p>:</p>
                                                        <p>:</p>
                                                    </div>
                                                    <div class="col-sm-7  pad-0">
                                                        <p><%= ((Colname.equals("")) ? "-" : Colname)%></p>
                                                        <p><%= ((phoneCol.equals("")) ? "-" : phoneCol)%></p>
                                                    </div>
                                                </div>										  
                                            </div>
                                        </div>

                                        <!--RIGHT SIDE-->
                                        <div class="col-md-4">
                                            <label class="col-sm-12 pad-top-8"><%=textListGlobal[SESS_LANGUAGE][9]%></label><!--Keterangan-->
                                            <p class="col-sm-12  pad-0"><%= (pinjaman.getKeterangan().equals("") && pinjaman.getKeterangan().length() <= 0) ? "-" : pinjaman.getKeterangan()%></p>

                                            <label class="col-sm-12 pad-top-8"><%=textListGlobal[SESS_LANGUAGE][14]%></label><!--Status Pemesanan-->
                                            <div class="col-sm-6  pad-0">
                                                <% if (cbm.getStatus() == PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION) {%>
                                                <select class="form-control" id="delivery-status" name="delivery-status">
                                                    <option value="<%= PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION%>" >
                                                        <%= PstBillMain.produksiDeliveryStatus[PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION]%>
                                                    </option>
                                                    <option value="<%= PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM%>">
                                                        <%= PstBillMain.produksiDeliveryStatus[PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM]%>
                                                    </option>
                                                    <option value="<%= PstBillMain.PETUGAS_DELIVERY_STATUS_DIAMBIL_LANGSUNG%>">
                                                        <%= PstBillMain.produksiDeliveryStatus[PstBillMain.PETUGAS_DELIVERY_STATUS_DIAMBIL_LANGSUNG]%>
                                                    </option>
                                                </select>
                                                <% } else {%>
                                                <p> <%= PstBillMain.produksiDeliveryStatus[cbm.getStatus()]%> </p>
                                                <% }%>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="tab-group">
                                            <section id="tab3" title="<%=textListGlobal[SESS_LANGUAGE][5]%>">
                                                <div class="col-md-12 beauty"><br>
                                                    <div class="col-md-8">
                                                        <p class="col-sm-12  pad-0"><%=textListHeader[SESS_LANGUAGE][8]%></p>
                                                        <table id="listItem" class="table table-striped table-bordered" style="width:100%">
                                                            <thead class="headerList">
                                                                <tr>
                                                                    <th>No</th>
                                                                    <th>SKU</th>
                                                                    <th>Name</th>
                                                                    <th>Barcode</th>
                                                                    <th>Warna</th>
                                                                    <th>Tipe</th>
                                                                    <th>Qty</th>
                                                                    <th>Aksi</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    long oidType = 0;
                                                                    if (!listBarang.isEmpty()) {
                                                                        for (int i = 0; i < listBarang.size(); i++) {
                                                                            Billdetail bd = (Billdetail) listBarang.get(i);
                                                                            Material mat = PstMaterial.fetchExc(bd.getMaterialId());
                                                                            Color col = new Color();
                                                                            if (mat.getPosColor() != 0) {
                                                                                col = PstColor.fetchExc(mat.getPosColor());

                                                                            };
                                                                            String whereMapping = PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + "=" + mat.getOID()
                                                                                    + " AND " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID] + "<> 0";
                                                                            Vector listGroupMapping = PstMaterialMappingType.list(0, 0, whereMapping, "");

                                                                %>
                                                                <tr>
                                                                    <td><%=i + 1%></td>
                                                                    <td><%=bd.getSku()%></td>
                                                                    <td><%=bd.getItemName()%></td>
                                                                    <td><%=mat.getBarCode()%></td>
                                                                    <td><%=col.getColorName()%></td>
                                                                    <%
                                                                        MasterType masterType = new MasterType();
                                                                        if (!listGroupMapping.isEmpty()) {
                                                                    %>
                                                                    <td>
                                                                        <%
                                                                            for (int x = 0; x < listGroupMapping.size(); x++) {
                                                                                MaterialTypeMapping materialTypeMapping = (MaterialTypeMapping) listGroupMapping.get(x);
                                                                                oidType = materialTypeMapping.getTypeId();
                                                                                String whereMas = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "= 2"
                                                                                        + " AND " + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + "=" + oidType;
                                                                                Vector listMasType = PstMasterType.list(0, 0, whereMas, "");
                                                                                for (int xx = 0; xx < listMasType.size(); xx++) {
                                                                                    masterType = (MasterType) listMasType.get(xx);
                                                                                }
                                                                        %>
                                                                        <%=masterType.getMasterName()%>
                                                                        <%
                                                                            }%>
                                                                    </td>  
                                                                    <%
                                                                    } else {
                                                                    %>
                                                                    <td>-</td>
                                                                    <%
                                                                        }
                                                                    %>
                                                                    <td><%=bd.getQty()%></td>
                                                                    <td><%if (cbm.getDoPersonId() == 0) {%>
                                                                        <button type="button" class="btn btn-success btn-xs btn-exchange"  data-cbmoid="<%= cbm.getOID()%>" data-sendnote="<%= cbm.getNotes()%>" data-bdoid="<%= bd.getOID()%>" title="Exchange"><i class="fa fa-exchange"></i></button>
                                                                        <%}%></td>
                                                                </tr>
                                                                <%
                                                                    }
                                                                } else {%>
                                                            <td colspan="8">-</td>
                                                            <%	}
                                                            %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][6]%></p><!--Tanggal Pengajuan-->
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-5  pad-0"><%= ((pinjaman.getTglPengajuan() == null) ? "-" : Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyy"))%></p>
                                                        <br>
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][7]%></p>
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-5  pad-0"><%= (jenisKredit.getNamaKredit().equals("")) ? "-" : jenisKredit.getNamaKredit()%></p>
                                                        <br>
                                                        <% if (enableTabungan == 1) {%>
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][10]%></p>
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-5  pad-0"><%= (pinjaman.getSukuBunga() == 0) ? "-" : pinjaman.getSukuBunga() + "%"%></p>
                                                        <% }%>
                                                        <br>
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][9]%></p>
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-5  pad-0"><%= (pinjaman.getJumlahPinjaman() == 0) ? "-" : FRMHandler.userFormatStringDecimal(pinjaman.getJumlahPinjaman()+pinjaman.getDownPayment())%></p>
                                                        <br>
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][11]%></p>
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-5  pad-0"><%= (jk.getJangkaWaktu() == 0) ? "-" : jk.getJangkaWaktu() + " bulan"%></p> 
                                                    </div>

                                                </div>
                                            </section>
                                            <% if (enableTabungan == 1) {%>
                                            <section id="tab4" title="<%=textListGlobal[SESS_LANGUAGE][3]%>"><!--Data Agunan-->
                                                <div class="col-md-12 beauty">
                                                    <div class="table-responsive">
                                                        <table id="list-agunan" class="dataTable table table-striped table-bordered" style="width:100%">
                                                            <thead class="headerList">
                                                                <tr>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][25]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][26]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][27]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][14]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][15]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][16]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][17]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][18]%></th>
                                                                    <th><%=textListHeader[SESS_LANGUAGE][19]%></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (!listAgunanMap.isEmpty()) {
                                                                        for (int i = 0; i < listAgunanMap.size(); i++) {
                                                                            AgunanMapping am = (AgunanMapping) listAgunanMap.get(i);
                                                                            Agunan agunan = PstAgunan.fetchExc(am.getAgunanId());
                                                                            HashMap<String, String> tipeAgunan = agunan.TIPE_AGUNAN.get(agunan.getTipeAgunan());
                                                                %>
                                                                <tr>
                                                                    <td><%= i + 1%></td>
                                                                    <td><%= tipeAgunan.get("TITLE")%></td>
                                                                    <td><%= tipeAgunan.get("NOTE_NAME")%> <br> <%= agunan.getNoteTipeAgunan()%> </td>
                                                                    <td><%= agunan.getAlamatAgunan()%></td>
                                                                    <td><%= agunan.getBuktiKepemilikan()%></td>
                                                                    <td><%= Formater.formatNumber(agunan.getNilaiAgunanNjop(), "###,###")%></td>
                                                                    <td><%= Formater.formatNumber(agunan.getNilaiEkonomis(), "###,###")%></td>
                                                                    <td><%= Formater.formatNumber(agunan.getNilaiJaminanAgunan(), "###,###")%></td>
                                                                    <td><%= Formater.formatNumber(am.getProsentasePinjaman(), "###,###")%>%</td>
                                                                </tr>
                                                                <%		}
                                                                } else { %>
                                                                <tr>
                                                                    <td colspan="9">Data agunan tidak ditemukan</td>
                                                                </tr>
                                                                <%	}
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </section>
                                            <%}%>
                                            <section id="tab5" title="<%=textListGlobal[SESS_LANGUAGE][6]%>"><!--Data Penjamin-->
                                                <div class="col-md-12 beauty">
                                                    <%
                                                        if (listPenjamin.isEmpty()) {
                                                    %>
                                                    <p class="col-sm-12  pad-0"><%=textListHeader[SESS_LANGUAGE][20]%></p>
                                                    <%
                                                    } else {
                                                        for (int i = 0; i < listPenjamin.size(); i++) {
                                                            Penjamin tempPenjamin = (Penjamin) listPenjamin.get(i);
                                                            Anggota penjamin = PstAnggota.fetchExc(tempPenjamin.getContactId());
                                                    %>
                                                    <div class="col-md-4">
                                                        <p class="col-sm-5  pad-0"><%= (i + 1) + ". " + penjamin.getName()%></p>
                                                        <p class="col-sm-1 pad-0">:</p>
                                                        <p class="col-sm-6  pad-0"><%=textListHeader[SESS_LANGUAGE][24]%> <%= Formater.formatNumber(tempPenjamin.getCoverage(), "###")%>%</p>
                                                    </div>
                                                    <div class="col-md-4">	
                                                    </div>
                                                    <div class="col-md-4">
                                                    </div>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </div>
                                            </section>
                                            <section id="tab6" title="<%=textListGlobal[SESS_LANGUAGE][17]%>"><!--Data Biaya--> 
                                                <div class="col-md-12 beauty">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div style="width: 100%">
                                                                <table class="table table-bordered table-striped">
                                                                    <thead class="headerList">
                                                                        <tr>
                                                                            <th style="width: 40px">No.</th>
                                                                            <th style="width: 60%">Jenis Biaya</th>
                                                                            <th style="width: 40%;">Nilai Biaya</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <%

                                                                            for (int i = 0; i < listData.size(); i++) {
                                                                                ArrayList al = (ArrayList) listData.get(i);
                                                                                BiayaTransaksi bt = new BiayaTransaksi();
                                                                                String persen = "";
                                                                                String uang = "Rp ";

                                                                                int tipe = Integer.parseInt(String.valueOf(al.get(2)));
                                                                                double biaya = Double.parseDouble(String.valueOf(al.get(1)));
                                                                                double jumlah = 0;
                                                                                if (tipe == BiayaTransaksi.TIPE_BIAYA_PERSENTASE) {
                                                                                    //persen = " %";
                                                                                    jumlah = pinjaman.getJumlahPinjaman() * biaya / 100;
                                                                                } else if (tipe == BiayaTransaksi.TIPE_BIAYA_UANG) {
                                                                                    jumlah = biaya;
                                                                                }
                                                                        %>
                                                                        <tr>
                                                                            <td><%=(i + 1)%>.</td>
                                                                            <td><%=al.get(0)%></td>
                                                                            <td><%=uang%><span class="money"><%=Formater.formatNumber(jumlah, "###,###,###")%></span></td>
                                                                        </tr>
                                                                        <%}%>

                                                                        <% if (listData.isEmpty()) {%>
                                                                        <tr><td colspan="3" class="text-center label-default">Tidak ada data biaya</td></tr>
                                                                        <%}%>
                                                                    </tbody>
                                                                </table><br>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                    <br>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <label class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][8]%></label>
                    </div>
                    <div class="box-body">
                        <div class="row" style="margin-bottom: 20px;">
                            <input type="hidden" id="cbmOID" name="cbmOID" value="<%= cbm.getOID()%>">
                            <input type="hidden" id="empOID" name="empOID" value="<%= cbm.getDoPersonId()%>">
                            <% if (cbm.getDoPersonId() == 0 || (cbm.getDoPersonId() != 0 && iCommand == Command.EDIT)) {%>
                            <div class="col-sm-12">
                                <div class="col-sm-3 pad-left-0">
                                    <div class="form-group">
                                        <label class="control-label col-sm-12 pad-left-0" for="tanggal"><%=textListHeader[SESS_LANGUAGE][21]%></label>
                                        <div class="col-sm-12 pad-left-0">
                                            <input id="jadwalKirim_" class="form-control datePicker jadwalKirim_" type="text" name="jadwalKirim"
                                                   value="<%= Formater.formatDate((cbm.getShippingDate() == null) ? new Date() : cbm.getShippingDate(), "yyyy-MM-dd")%>">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-4 pad-left-0">
                                    <div class="form-group">
                                        <label class="control-label col-sm-12" for="nama"><%=textListHeader[SESS_LANGUAGE][22]%></label>
                                        <div class="col-sm-12 group-me">
                                            <input id="nama_" class="form-control col-sm-9 nama_" type="text" name="name"
                                                   value="<%=nameEmp%>" readonly="">
                                            <a class="btn btn-primary col-sm-3" id="modal-petugas-btn"><i class="fa fa-search"></i></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-4 pad-left-0">
                                    <div class="form-group">
                                        <label class="control-label col-sm-12" for="code"><%=textListHeader[SESS_LANGUAGE][23]%></label>
                                        <div class="col-sm-12">
                                            <input id="empNum_" class="form-control empNum_" type="text" name="empNum"
                                                   value="<%= numEmp%>" readonly="">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%} else if (cbm.getDoPersonId() != 0) {%>
                        <div id="tabel-petugas">
                            <div class="row">
                                <div class="table-responsive">
                                    <table id="list-assign-petugas" class="dataTable table table-striped table-bordered" style="width:100%">
                                        <thead class="headerList">
                                            <tr>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][2]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][3]%> <%=textHeaderTable[SESS_LANGUAGE][4]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][5]%> <%=textHeaderTable[SESS_LANGUAGE][4]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][6]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][7]%></th>
                                                <th>Aksi</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                String noInvoice = cbm.getInvoiceNo();
                                                String dateShipping = Formater.formatDate(cbm.getShippingDate(), "dd MMMM yyyy");
                                                String status = PstBillMain.produksiDeliveryStatus[cbm.getStatus()];
                                            %>
                                            <tr>
                                                <td><%= noInvoice.equals("") ? "-" : noInvoice%></td>
                                                <td><%= numEmp.equals("") ? "-" : numEmp%></td>
                                                <td><%= nameEmp.equals("") ? "-" : nameEmp%></td>
                                                <td><%= dateShipping.equals("") ? "-" : dateShipping%></td>
                                                <td><%= status.equals("") ? "-" : status%></td>
                                                <%if (cbm.getStatus() == PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION) {%>
                                                <td><button class="btn btn-primary" id="editPetugas"> <i class="fa fa-pencil"></i></button></td>
                                                        <%} else {%>
                                                <td>-</td>
                                                <%}%>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>   
                            </div>
                        </div>	
                    </div>
                    <%}
                        if (cbm.getStatus() == PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION) {%>
                    <div class="box-footer">
                        <div class="col-md-12">
                            <span id="update-petugas" class="btn btn-success pull-left mg-top-10 mg-right-10"><i class="fa fa-check"> Simpan</i></span>
                            <span class="btn btn-danger pull-left mg-top-10 mg-right-10 back-btn"><i class="fa fa-refresh"> Kembali</i></span>
                        </div>
                        <%} else {%>
                        <div class=" col-md-12 pull-right">
                            <span id="print-all-btn" class="btn btn-primary"><i class="fa fa-print"> Print All List </i></span>
                            <span id="print-bukti-mutasi-barang" class="btn btn-primary"><i class="fa fa-print"> Bukti Mutasi Barang </i></span>
                            <span id="print-surat-jalan" class="btn btn-primary"><i class="fa fa-print"> Print Surat Jalan </i></span>
                            <span id="print-spb-btn" class="btn btn-primary"><i class="fa fa-print">Print Surat Pengiriman Barang</i></span>
                            <span class="btn btn-primary back-btn"><i class="fa fa-arrow-left"> Kembali</i></span>
                        </div>
                    </div>
                    <%}%>
                </div>
            </section>
            <section id="tab2" title="<%=textListGlobal[SESS_LANGUAGE][13]%>">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <label class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][15]%></label>
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="table-responsive">
                                <form id="<%= FrmBillDetail.FRM_NAME_BILL_DETAIL%>">
                                    <table id="listPemesanan" class="dataTable table table-striped table-bordered" style="width:100%">
                                        <thead class="headerList">
                                            <tr>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][0]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][2]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][16]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][17]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][18]%> </th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][19]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][20]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][21]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][24]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][22]%></th>
                                                <th><%=textHeaderTable[SESS_LANGUAGE][23]%></th>
                                            </tr>
                                        </thead>
                                    </table>
                                </form>
                            </div>   
                            <p></p>
                            <div class="box-footer pull-right">
                                <div class="pull-right">
                                    <button id="create-store-request-btn" class="btn btn-sm btn-warning">Store Request</button>
                                    <button class="btn btn-sm btn-success">Order Request</button>
                                </div>
                                <br>
                                <br>
                                <a class="btn btn-primary"><i class="fa fa-print"> Print All List</i></a>
                                <a class="btn btn-primary"><i class="fa fa-print"> Print PDF</i></a>
                                <a class="btn btn-primary"><i class="fa fa-arrow-left"> Kembali</i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="ModalPetugas" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel"><%=textListGlobal[SESS_LANGUAGE][10]%></label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <select class="form-control select2" name="form_location" id="form_location">
                                    <option value="0">All Location</option>
                                    <%
                                        for (Location loc : listLocation) {
                                    %>
                                    <option value="<%=loc.getOID()%>"><%=loc.getName()%></option>
                                    <%}
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <select class="form-control select2" name="form_position" id="form_position">
                                    <%
                                        String hrUrl = PstSystemProperty.getValueByName("HARISMA_URL");
                                        String param = "limitStart=" + WebServices.encodeUrl("" + 0) + "&recordToGet=" + WebServices.encodeUrl("" + 0)
                                                + "&whereClause=&order=" + WebServices.encodeUrl("");
                                        JSONObject jo = WebServices.getAPIWithParam("", hrUrl + "/employee/position-list", param);
                                        Vector listPosition = new Vector(1, 1);
                                        String oidDelivPos = PstSystemProperty.getValueByName("STAFF_DELIVERY_OID");
                                        try {
                                            JSONArray lists = jo.getJSONArray("DATA");
                                            for (int i = 0; i < lists.length(); i++) {
                                                JSONObject tempObj = lists.getJSONObject(i);
                                                String selected = "";
                                                if (tempObj.optString(PstPosition.fieldNames[PstPosition.FLD_POSITION_ID], "").equals(oidDelivPos)){
                                                    selected = "selected";
                                                }
                                                %>
                                                <option value="<%=tempObj.optLong(PstPosition.fieldNames[PstPosition.FLD_POSITION_ID], 0)%>" <%=selected%>><%=tempObj.optString(PstPosition.fieldNames[PstPosition.FLD_POSITION], "")%></option>
                                                <%}
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <table id="listPetugas" class="dataTable table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][0]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][3]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][5]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][9]%> </th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][0]%> <%=textHeaderTable[SESS_LANGUAGE][10]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][0]%> <%=textHeaderTable[SESS_LANGUAGE][11]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][23]%></th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="create-store-request" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document" style="width: 40%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel"><%=textListGlobal[SESS_LANGUAGE][16]%></label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="<%= FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>" name="<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>" method="post" action="">
                                    <input type="hidden" name="<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS]%>" id="<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS]%>" value="<%= I_DocStatus.DOCUMENT_STATUS_DRAFT%>">
                                    <input type="hidden" name="<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCHASE_REQUEST_ID]%>" id="<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCHASE_REQUEST_ID]%>"  value="0">
                                    <div class="row">
                                        <div class="form-group">
                                            <label for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label><br>
                                            <%=ControlDate.drawDate(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE], new Date(), "form-control formElemen", 1, -5)%>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <label for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                            <%
                                                Vector val_locationid = new Vector(1, 1);
                                                Vector key_locationid = new Vector(1, 1);
                                                whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                        + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                                for (int d = 0; d < vt_loc.size(); d++) {
                                                    Location loc = (Location) vt_loc.get(d);
                                                    val_locationid.add("" + loc.getOID() + "");
                                                    key_locationid.add(loc.getName());
                                                }
                                                String select_locationid = "" + cbm.getLocationId();
                                            %>
                                            <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <label for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][20]%></label>
                                            <%
                                                Vector val_locationidSup = new Vector(1, 1);
                                                Vector key_locationidSup = new Vector(1, 1);
                                                for (int d = 0; d < vt_loc.size(); d++) {
                                                    Location supplierFromWarehouse = (Location) vt_loc.get(d);
                                                    val_locationidSup.add("" + supplierFromWarehouse.getOID() + "");
                                                    key_locationidSup.add(supplierFromWarehouse.getName());
                                                }

                                                String select_locationid_sup = "0";
                                            %>
                                            <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_WAREHOUSE_SUPP_ID], null, select_locationid_sup, val_locationidSup, key_locationidSup, "", "form-control")%>
                                        </div>										
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <label for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label>
                                            <textarea id="notes" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="form-control"></textarea>
                                        </div>										
                                    </div>
                                </form>
                            </div>
                            <!--
                                 <div class="col-md-6">
                                  <form id="<%= FrmPurchaseRequestItem.FRM_NAME_PURCHASE_ORDER_ITEM%>">
                                   <input type="hidden" id="<%= FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_MATERIAL_ID]%>"
                                       name="<%= FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_MATERIAL_ID]%>">
                                   <input type="hidden" id="<%= FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_UNIT_ID]%>"
                                       name="<%= FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_UNIT_ID]%>">
                                   <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control" name="sku" id="sku_" readonly="">
                                   </div>
                                   <div class="form-group">
                                    <label>Nama Barang</label>
                                    <input class="form-control" name="nama" id="nama_" readonly="">
                                   </div>
                                   <div class="form-group">
                                    <label>Unit</label>
                                    <input class="form-control" name="unit" id="unit_" readonly="">
                                   </div>
                                   <div class="form-group">
                                    <label>Qty.</label>
                                    <input class="form-control" name="<%= FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>" id="">
                                   </div>
                                  </form>
                                 </div> 
                            -->
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button id="create-sr-btn" class="btn btn-primary" value="0"><i class="fa fa-plus"></i> Buat</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Status-->
        <div class="modal fade" id="modalExchangeItem" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">Exchange Item</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="oidBillMain_" name="oidBillMain" value="">
                        <input type="hidden" id="oidBillDetail_" name="oidBillDetail" value="">
                        <table id="listItemExchange" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th>No</th>
                                    <th>SKU</th>
                                    <th>Barcode</th>
                                    <th>Name</th>
                                    <th>Harga</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>

                        </table>
                    </div>
                    <div class="modal-footer">
                        <div class="text-center">
                            <button class="btn btn-danger" data-dismiss="modal"><i class="fa fa-calendar-times-o"></i> Cancel </button>
                            <button class="btn btn-success exchange-btn" value="<%= PstBillMain.DELIVERY_STATUS_DRAFT%>"><i class="fa fa-calendar-check-o"></i> Exchange </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Status-->
        <div class="modal fade" id="modalStatus" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">Exchange Item</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="oidBillMain1_" name="oidBillMain" value="">
                        <input type="hidden" id="oidBillDetail1_" name="oidBillDetail" value="">
                        <input type="hidden" id="oidMaterial_" name="oidMaterial" value="">
                        <div class="form-group">
                            <label for="note_">Jumlah Qty</label>
                            <input class="form-control" id="qty_" rows="3" type="number" style="resize: none;">
                        </div>
                        <div class="form-group">
                            <label for="note_">Catatan</label>
                            <textarea class="form-control" id="note_" rows="3" style="resize: none;"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-center">
                            <button class="btn btn-danger" data-dismiss="modal"><i class="fa fa-calendar-times-o"></i> Cancel </button>
                            <button class="btn btn-success exchange-btn"><i class="fa fa-calendar-check-o"></i> Exchange </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                var getDataFunction = function (onDone, onSuccess, dataSend, servletName, dataAppendTo, notification) {
                    $(this).getData({
                        onDone: function (data) {
                            onDone(data);
                        },
                        onSuccess: function (data) {
                            onSuccess(data);
                        },
                        approot: "<%=approot%>",
                        dataSend: dataSend,
                        servletName: servletName,
                        dataAppendTo: dataAppendTo,
                        notification: notification
                    });
                };

                // DATABLE SETTING
                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";
                    var form = $('#form_location');
                    var pos = $('#form_position').val();
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({
                        "bDestroy": true,
                        "ordering": false,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>",
                            "sLengthMenu": "<%=dataTableTitle[SESS_LANGUAGE][0]%>",
                            "sZeroRecords": "<%=dataTableTitle[SESS_LANGUAGE][1]%>",
                            "sInfo": "<%=dataTableTitle[SESS_LANGUAGE][2]%>",
                            "sInfoEmpty": "<%=dataTableTitle[SESS_LANGUAGE][3]%>",
                            "sInfoFiltered": "<%=dataTableTitle[SESS_LANGUAGE][4]%>",
                            "sSearch": "<%=dataTableTitle[SESS_LANGUAGE][5]%>",
                            "oPaginate": {
                                "sFirst ": "<%=dataTableTitle[SESS_LANGUAGE][6]%>",
                                "sLast ": "<%=dataTableTitle[SESS_LANGUAGE][7]%>",
                                "sNext ": "<%=dataTableTitle[SESS_LANGUAGE][8]%>",
                                "sPrevious ": "<%=dataTableTitle[SESS_LANGUAGE][9]%>"
                            }
                        },
                        "bServerSide": true,
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&oid=<%= cbm.getOID()%>&" + form.serialize()+"&position="+pos,
                        aoColumnDefs: [
                            {
                                bSortable: false,
                                aTargets: [-1]
                            }
                        ],
                        "initComplete": function (settings, json) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnPageChange": function (oSettings) {

                        }
                    });
                    $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
                    $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
                    $("#" + elementId).css("width", "100%");
                }

                function runDataTable(elementidparent, elementid, servlet, dataFor, callBack) {
                    dataTablesOptions("#" + elementidparent, elementid, servlet, dataFor, callBack);
                }

                function runListPemesananTable() {
                    console.log('tab');
                    var elementParentId = 'tab2';
                    var elementId = 'listPemesanan';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listPemesanan';
                    runDataTable(elementParentId, elementId, servlet, dataFor, null);
                }

                runListPemesananTable();

                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });
                //          $('#listPetugas').DataTable({
                //              "ordering": false
                //            });
                //        $('#modal-petugas-btn').click(function () {
                //          $('#ModalPetugas').modal('show');
                //          $('html, body', window.parent.document).animate({
                //            scrollTop: 0
                //          }, 'slow');
                //        });

                var tabelPetugas = $('#list-assign-petugas').DataTable({
                    "paging": false,
                    "lengthChange": false,
                    "searching": false,
                    "ordering": false,
                    "info": false,
                    "autoWidth": false,
                });

                $('body').on('click', '#modal-petugas-btn', function () {
                    var ModalPetugas = $('#ModalPetugas');
                    ModalPetugas.modal('show');
                    $('html, body', window.parent.document).animate({
                        scrollTop: 0
                    }, 'slow');

                    ModalPetugas.one('shown.bs.modal', function () {
                        var elementidparent = 'ModalPetugas';
                        var servlet = 'AjaxProduksi';
                        var dataFor = 'listPetugas';
                        var elementid = 'listPetugas';
                        runDataTable(elementidparent, elementid, servlet, dataFor, null);
                    });

                });

                $('#form_location').change(function () {
                    var elementidparent = 'ModalPetugas';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listPetugas';
                    var elementid = 'listPetugas'
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                });

                $('#form_position').change(function () {
                    var elementidparent = 'ModalPetugas';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listPetugas';
                    var elementid = 'listPetugas'
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                });

                $('body').on('click', '.select-petugas-btn', function () {
                    var name = $(this).data('name');
                    var oid = $(this).data('oid');
                    var empNum = $(this).data('empnum');

                    $('#nama_').val(name);
                    $('#empNum_').val(empNum);
                    $('#empOID').val(oid);

                    $('#ModalPetugas').modal('hide');
                    $('html, body', window.parent.document).animate({
                        scrollTop: $(document).height()
                    }, 'slow');
                });

                $('#update-petugas').click(function () {
                    var delivId = $("#empOID").val();
                    var status = $("#delivery-status").val();
                    
                    if (delivId == 0 && status == 2){
                        alert("Pilih Petugas Delivery!");
                    } else {
                        var formData = $('#assign-petugas').serialize();
                        //$(this).html("Tunggu...").attr({"disabled": "true"});
                        var dataSend = {
                            "FRM_FIELD_DATA_FOR": "assignPetugasDelivery",
                            "empOID": $('#empOID').val(),
                            "cbmOID": $('#cbmOID').val(),
                            "jadwalKirim": $('.jadwalKirim_').val(),
                            "deliveryStatus": $('#delivery-status').val(),
                            "command": "<%= Command.SAVE%>"
                        };
                        onDone = function (data) {
                            alert(data.RETURN_MESSAGE);
                            //            window.location.reload();
                            window.location.href = "detail_penjualan.jsp?oid_cbm=<%=oidCBM%>&command=<%=Command.NONE%>";
                        };
                        onSuccess = function (data) {};
                        console.log(dataSend);
                        getDataFunction(onDone, onSuccess, dataSend, "AjaxProduksi", null, false);
                    }

                });

                $('#editPetugas').click(function () {
                    window.location.href = "detail_penjualan.jsp?oid_cbm=<%=oidCBM%>&command=<%=Command.EDIT%>";
                });

                $('#create-sr-btn').click(function () {
                    var oid = $(this).val();

                    var locReq = $('#<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID]%>').val();
                    var locOrd = $('#<%= FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_WAREHOUSE_SUPP_ID]%>').val();

                    if (locReq == locOrd) {
                        alert('Lokasi tidak bisa sama!')
                    } else {
                        if (confirm("Buat Dokumen Store Request?")) {
                            $.ajax({
                                type: 'POST',
                                url: "<%= approot%>/AjaxProduksi?command=<%= Command.SAVE%>&FRM_FIELD_APPROOT=<%= approot%>&FRM_FIELD_DATA_FOR=createSrDoc&userName<%= userName%>&userId<%= userId%>&oid=" + oid,
                                data: $('#<%= FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>').serialize() + "&" + $('#<%= FrmBillDetail.FRM_NAME_BILL_DETAIL%>').serialize(),
                                cache: false,
                                dataType: 'json',
                                success: function (data) {
                                    alert(data.RETURN_MESSAGE);
                                    window.location.href = "<%= approot%>" + data.FRM_FIELD_HTML;
                                }
                            }).done();
                        }
                    }

                });

                $('body').on('click', '.back-btn', function () {
                    window.location = "src_penjualan.jsp";
                });

                $('#print-all-btn').click(function () {
                    window.open("<%= approot%>/PrintDocumentProduction?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=<%= cbm.getOID()%>");
                            });
                            $('#print-spb-btn').click(function () {
                                window.open("<%= approot%>/PrintSuratPerintahPengirimanBarang?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=<%= cbm.getOID()%>");
                                        });
                                        $('#print-surat-jalan').click(function () {
                                            window.open("<%= approot%>/PrintSuratJalan?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=<%= cbm.getOID()%>");
                                                    });
                                                    $('#print-bukti-mutasi-barang').click(function () {
                                                        window.open("<%= approot%>/BuktiMutasiBarang?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=<%= cbm.getOID()%>");
                                                                });

                                                                $('body').on('click', '.create-new-sr', function () {
                                                                    var modal = $('#create-store-request');
                                                                    modal.modal('show');

                                                                });

                                                                $('#create-store-request-btn').click(function () {
                                                                    var modal = $('#create-store-request');
                                                                    modal.modal('show');
                                                                    console.log();
                                                                });

                                                                (function (defaults, $, window, document, undefined) {

                                                                    'use strict';

                                                                    $.extend({
                                                                        // Function to change the default properties of the plugin
                                                                        // Usage:
                                                                        // jQuery.tabifySetup({property:'Custom value'});
                                                                        tabifySetup: function (options) {

                                                                            return $.extend(defaults, options);
                                                                        }
                                                                    }).fn.extend({
                                                                        // Usage:
                                                                        // jQuery(selector).tabify({property:'value'});
                                                                        tabify: function (options) {

                                                                            options = $.extend({}, defaults, options);

                                                                            return $(this).each(function () {
                                                                                var $element, tabHTML, $tabs, $sections;

                                                                                $element = $(this);
                                                                                $sections = $element.children();

                                                                                // Build tabHTML
                                                                                tabHTML = '<ul class="tab-nav">';
                                                                                $sections.each(function () {
                                                                                    if ($(this).attr("title") && $(this).attr("id")) {
                                                                                        tabHTML += '<li><a href="#' + $(this).attr("id") + '">' + $(this).attr("title") + '</a></li>';
                                                                                    }
                                                                                });
                                                                                tabHTML += '</ul>';

                                                                                // Prepend navigation
                                                                                $element.prepend(tabHTML);

                                                                                // Load tabs
                                                                                $tabs = $element.find('.tab-nav li');

                                                                                // Functions
                                                                                var activateTab = function (id) {
                                                                                    $tabs.filter('.active').removeClass('active');
                                                                                    $sections.filter('.active').removeClass('active');
                                                                                    $tabs.has('a[href="' + id + '"]').addClass('active');
                                                                                    $sections.filter(id).addClass('active');
                                                                                }

                                                                                // Setup events
                                                                                $tabs.on('click', function (e) {
                                                                                    activateTab($(this).find('a').attr('href'));
                                                                                    e.preventDefault();
                                                                                });

                                                                                // Activate first tab
                                                                                activateTab($tabs.first().find('a').attr('href'));

                                                                            });
                                                                        }
                                                                    });
                                                                })({
                                                                    property: "value",
                                                                    otherProperty: "value"
                                                                }, jQuery, window, document);


                                                                // Calling the plugin
                                                                $('.tab-group').tabify();

                                                                $('body').on('click', '.btn-exchange', function () {
                                                                    var cbmOID = $(this).data('cbmoid');
                                                                    var empOID = $(this).data('bdoid');
                                                                    //                     var sendNote = $(this).data('sendnote');
                                                                    var status = $(this).val();
                                                                    var modalExchangeItem = $('#modalExchangeItem');
                                                                    modalExchangeItem.modal('show');
                                                                    modalExchangeItem.one('shown.bs.modal', function () {
                                                                        modalExchangeItem.find('.modal-body #oidBillMain_').val(cbmOID);
                                                                        modalExchangeItem.find('.modal-body #oidBillDetail_').val(empOID);
                                                                        //                      modalExchangeItem.find('.modal-body #note_').val(sendNote);
                                                                        var elementidparent = 'modalExchangeItem';
                                                                        var servlet = 'AjaxProduksi';
                                                                        var dataFor = 'listItemForExchange';
                                                                        var elementid = 'listItemExchange';
                                                                        runDataTable(elementidparent, elementid, servlet, dataFor, null);
                                                                    });

                                                                });


                                                                $('body').on('click', '.getItemExchange', function () {
                                                                    var cbmOID = $('#oidBillMain_').val();
                                                                    var bdOID = $('#oidBillDetail_').val();
                                                                    var matOID = $(this).data('oidmat');
                                                                    var price = $(this).data('price');
                                                                    var sendNote = $(this).data('sendnote');
                                                                    var status = $(this).val();
                                                                    if (price != 0) {
                                                                        var modalStatus = $('#modalStatus');
                                                                        modalStatus.modal('show');
                                                                        modalStatus.one('shown.bs.modal', function () {
                                                                            modalStatus.find('.modal-body #oidBillMain1_').val(cbmOID);
                                                                            modalStatus.find('.modal-body #oidBillDetail1_').val(bdOID);
                                                                            modalStatus.find('.modal-body #oidMaterial_').val(matOID);
                                                                            modalStatus.find('.modal-body #note_').val(sendNote);
                                                                        });
                                                                    } else {
                                                                        alert('Please check price item before selected! \nPrice Item must not zero!');
                                                                        $('.btn-exchange').trigger("click");
                                                                    }
                                                                });

                                                                $('body').on('click', '.exchange-btn', function () {
                                                                    var notes = $('#note_').val();
                                                                    var qty = $('#qty_').val();
                                                                    var cbmOID = $('#oidBillMain1_').val();
                                                                    var bdOID = $('#oidBillDetail1_').val();
                                                                    var matOID = $('#oidMaterial_').val();
                                                                    if (qty == "" || qty == "-" || qty == "." || qty == "e") {
                                                                        alert("Pastikan jumlah item terisi dengan benar dan tidak kosong!");
                                                                    } else if (notes == "" || notes == "-" || notes == "." || notes == " ") {
                                                                        alert("Catatan sangat diperlukan dalam penukaran barang");
                                                                    } else {

                                                                        //                  exchangeItem(status);
                                                                        var dataSend = {
                                                                            "FRM_FIELD_DATA_FOR": "exchangeItem",
                                                                            "BILL_MAIN_OID": cbmOID,
                                                                            "BILL_DETAIL_OID": bdOID,
                                                                            "QTY": qty,
                                                                            "MATERIAL_ID": matOID,
                                                                            "DELIVERY_NOTES": notes,
                                                                            "command": "<%= Command.SAVE%>"
                                                                        };

                                                                        onDone = function (data) {
                                                                            alert(data.RETURN_MESSAGE);
                                                                            window.location.href = "detail_penjualan.jsp?oid_cbm=" + cbmOID;
                                                                        };
                                                                        onSuccess = function (data) {
                                                                        };

                                                                        if (confirm("Yakin exchange item?")) {
                                                                            getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);
                                                                        }
                                                                    }
                                                                });

                                                                //                    function exchangeItem(status){
                                                                //                     var status = status;
                                                                //                     var cbmoid = $('#oidBillMain_').val();
                                                                //                     var bdoid = $('#oidBillDetail_').val();
                                                                //                     var notes = $('#note_').val();
                                                                //
                                                                //                    var pinjamanOid;
                                                                //                     var exchangeStatus;
                                                                //                     var dataSend = {
                                                                //                      "FRM_FIELD_DATA_FOR": "exchangeItem",
                                                                //                      "DELIVERY_STATUS":status,
                                                                //                      "BILL_MAIN_OID":cbmoid,
                                                                //                      "BILL_DETAIL_OID":bdoid,
                                                                //                      "DELIVERY_NOTES":notes,
                                                                //                      "command":"<%= Command.SAVE%>"
                                                                //                     };
                                                                //                     onDone = function (data){
                                                                //                      alert(data.RETURN_MESSAGE);
                                                                //                      pinjamanOid = data.RETURN_DATA_PINJAMAN;
                                                                //                      exchangeStatus = data.RETURN_DATA_EXCHANGE;
                                                                //                      if(pinjamanOid != 0){
                                                                //                      window.location.href = "<%=apiUrl%>/sedana/transaksikredit/transaksi_kredit.jsp?command=3&pinjaman_id="+pinjamanOid+"&exchange_status="+exchangeStatus;
                                                                //                      }else{
                                                                //                        alert("Data Pinjaman Tidak Ditemukan!");
                                                                //                      }
                                                                //                     };			
                                                                //                     onSuccess = function(data){
                                                                //                       
                                                                //                     };
                                                                //
                                                                //                     if(confirm("Yakin exchange item?")){
                                                                //                      getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);
                                                                //                     }
                                                                //                    }
                                                            });
        </script>
    </body>
</html>

















