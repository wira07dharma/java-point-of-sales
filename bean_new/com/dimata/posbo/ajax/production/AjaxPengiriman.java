/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax.production;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.form.billing.FrmBillMain;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.BillImageMapping;
import com.dimata.posbo.entity.warehouse.PstBillImageMapping;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.form.kredit.FrmPinjaman;
import com.dimata.services.WebServices;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author wira
 */
public class AjaxPengiriman extends HttpServlet {

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    private JSONArray jsonArrayJenisProduksi = new JSONArray();
    private JSONArray jsonArrayDataProduksi = new JSONArray();
    private JSONArray jsonArrayPrintValue = new JSONArray();

    private String dataFor = "";
    private String approot = "";
    private String htmlReturn = "";
    private String message = "";
    private String oidPinjaman = "";
    private String exchange = "";

    private boolean sessLogin = false;

    private int iCommand = 0;
    private int iErrCode = 0;
    private int sessLanguage = 0;
    private long oidBm = 0;
    private long oid = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        this.jsonArrayJenisProduksi = new JSONArray();
        this.jsonArrayDataProduksi = new JSONArray();
        this.jsonArrayPrintValue = new JSONArray();
        this.jSONArray = new JSONArray();
        this.jSONObject = new JSONObject();

        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";
        this.message = "";
        this.oidPinjaman = "";
        this.exchange = "";

        this.sessLogin = false;

        this.oidBm = FRMQueryString.requestLong(request, "BILL_MAIN_OID");
        this.oid = FRMQueryString.requestLong(request, "oid");
        iCommand = FRMQueryString.requestCommand(request);

        //CHECK USER LOGIN SESSION
        HttpSession session = request.getSession();
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
        if (userSession != null) {
            if (userSession.isLoggedIn()) {
                this.sessLogin = true;
            }
        }

        if (this.sessLogin == true) {
            switch (this.iCommand) {
                case Command.SAVE:
                    commandSave(request);
                    break;

                case Command.DELETE:
                    commandDelete(request);
                    break;

                case Command.LIST:
                    commandList(request, response);
                    break;

                case Command.NONE:
                    commandNone(request, response);
                    break;

                default:
                    //commandNone(request);
                    break;
            }
        } else {
            this.iErrCode = 1;
            this.message = "Sesi login Anda telah berakhir. Silakan login ulang untuk melanjutkan.";
        }

        try {

            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("RETURN_DATA_JENIS_PRODUKSI", this.jsonArrayJenisProduksi);
            this.jSONObject.put("RETURN_DATA_PRODUKSI", this.jsonArrayDataProduksi);
            this.jSONObject.put("RETURN_PRINT_VALUE", this.jsonArrayPrintValue);
            this.jSONObject.put("RETURN_DATA_ARRAY", this.jSONArray);
            this.jSONObject.put("RETURN_SESSION_LOGIN", this.sessLogin);
            this.jSONObject.put("RETURN_ERROR_CODE", "" + this.iErrCode);
            this.jSONObject.put("RETURN_MESSAGE", "" + this.message);
            this.jSONObject.put("RETURN_DATA_PINJAMAN", "" + this.oidPinjaman);
            this.jSONObject.put("RETURN_DATA_EXCHANGE", "" + this.exchange);

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

        String responses = "";
        long oid = FRMQueryString.requestLong(request, "oid");
        String type = FRMQueryString.requestString(request, "type");
        if (iCommand == Command.SAVE) {
            if (type.equals("sendDispatch")) {
                JSONObject messages = new JSONObject();
                long dispatchOid = FRMQueryString.requestLong(request, "FRM_FIELD_CASH_BILL_MAIN_ID");
                String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
                BillMain cbm = new BillMain();
                for (int i = 0; i < oids.length; i++) {
                    try {
                        cbm = PstBillMain.fetchExc(Long.parseLong(oids[i]));
                        cbm.setStatus(PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM);
                        long oidDis = PstBillMain.updateExc(cbm);
                        if (oidDis != 0) {
                            this.message = "Pengiriman berhasil ditambahkan";
                        }
                    } catch (Exception e) {
                        printErrorMessage(e.getMessage());
                    }
                }
            }
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("assignDelivery")) {
            assignDelivery(request);
        } else if (this.dataFor.equals("updatePengiriman")) {
            updatePengiriman(request);
        } else if (this.dataFor.equals("exchangeItem")) {
            exchangeItem(request);
        } else if (this.dataFor.equals("saveDokumen")) {
            saveDokumen(request);
        }
    }

    public void assignDelivery(HttpServletRequest request) {
        long oidDispatch = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
        String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
        BillMain cbm = new BillMain();
        for (int i = 0; i < oids.length; i++) {
            try {
                cbm = PstBillMain.fetchExc(oidDispatch);
                cbm.setStatus(PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM);
                long oid = PstBillMain.updateExc(cbm);
                if (oid != 0) {
                    this.message = "Pengiriman berhasil ditambahkan";
                }
            } catch (Exception e) {
                printErrorMessage(e.getMessage());
            }
        }
    }

    public void updatePengiriman(HttpServletRequest request) {
        long cbmOID = FRMQueryString.requestLong(request, "BILL_MAIN_OID");
        String notes = FRMQueryString.requestString(request, "DELIVERY_NOTES");
        int status = FRMQueryString.requestInt(request, "DELIVERY_STATUS");
        BillMain cbm = new BillMain();
        String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");

        String url = apiUrl + "/credit/update-pengiriman/" + status + "/" + cbmOID;
        System.out.println("Update Status : "+ url);
        try {
            cbm = PstBillMain.fetchExc(cbmOID);        
            cbm.setNotes(notes);
            cbm.setStatus(status);
            if (status == PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION) {
                cbm.setDoPersonId(0);
                cbm.setShippingDate(new Date());
            }
            long oid = PstBillMain.updateExc(cbm);
            JSONObject obj = WebServices.getAPI("", url);
            if (oid != 0) {
                this.message = "Update status pengiriman berhasil!";
            } else {
                this.message = "Update status pengiriman gagal!";
            }

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    public void exchangeItem(HttpServletRequest request) {
        long cbmOID = FRMQueryString.requestLong(request, "BILL_MAIN_OID");
        long bdOID = FRMQueryString.requestLong(request, "BILL_DETAIL_OID");
        String notes = FRMQueryString.requestString(request, "DELIVERY_NOTES");
        long matOID = FRMQueryString.requestLong(request, "MATERIAL_ID");
        double qty = FRMQueryString.requestDouble(request, "QTY");
        long oidNew = 0;

        BillMain cbm = new BillMain();
        Billdetail bd = new Billdetail();
        Material mat = new Material();
        Unit unit = new Unit();
        Category cat = new Category();

        String where = "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] + " = " + bdOID;
        String order = "";
        int start = 0;
        int record = 0;

        //Get Data Item Yang di Exchange
        Vector<Billdetail> listDetail = PstBillDetail.list(start, record, where, order);
        try {
            if (!listDetail.isEmpty()) {

                //Delete Data Item Sebelumnya
                long oidBd = PstBillDetail.deleteExc(bdOID);

                mat = PstMaterial.fetchExc(matOID);
                cbm = PstBillMain.fetchExc(cbmOID);
                unit = PstUnit.fetchExc(mat.getBuyUnitId());
                cat = PstCategory.fetchExc(mat.getCategoryId());

                double price = 0;
                int roundValue = 1000;
                String formulaCash = PstSystemProperty.getValueByName("CASH_FORMULA");

                if (checkString(formulaCash, "HPP") > -1) {
                    formulaCash = formulaCash.replaceAll("HPP", "" + mat.getAveragePrice());
                }
//                if (checkString(formulaCash, "INCREASE") > -1) {
//                    formulaCash = formulaCash.replaceAll("INCREASE", "" + cat.getKenaikanHarga());
//                }
                price += getValue(formulaCash);
                price = Math.round(price);

                bd.setBillMainId(cbmOID);
                bd.setUnitId(unit.getOID());
                bd.setMaterialId(matOID);
                bd.setQty(qty);
                bd.setItemPrice(price);
                bd.setTotalPrice(price * qty);
                bd.setSku(mat.getSku());
                bd.setItemName(mat.getName());
                bd.setCost(mat.getAveragePrice());
                bd.setLengthOrder(new Date());
                oidNew = PstBillDetail.insertExc(bd);

                if (oidNew != 0) {
                    cbm.setNotes(notes);
                    long oidBmNew = PstBillMain.updateExc(cbm);
                }

                if (oidNew != 0) {
                    this.message = "Exchange Item berhasil!";
                } else {
                    this.message = "Exchange Item gagal!";
                }

            } else {
                this.message = "Exchange Item gagal! \nData item yang akan di exchange tidak ditemukan";
            }

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
            this.message = "Exchange Item gagal!";
        }
    }
//        public void exchangeItem(HttpServletRequest request) {
//            long cbmOID = FRMQueryString.requestLong(request, "BILL_MAIN_OID");
//            long bdOID = FRMQueryString.requestLong(request, "BILL_DETAIL_OID");
//            String notes = FRMQueryString.requestString(request, "DELIVERY_NOTES");
//            int status = FRMQueryString.requestInt(request, "DELIVERY_STATUS");
//            BillMain cbm = new BillMain();
//
//            String urlSedana = PstSystemProperty.getValueByName("SEDANA_APP_URL");
//            String url = urlSedana + "/kredit/exchange-item/" + cbmOID + "/" + bdOID;
//            String urlInsert = url;
//
//            JSONObject objStatus = WebServices.postAPI("", urlInsert);
//            boolean statusObj = objStatus.optBoolean("SUCCES", false);
//            long oidPinjaman = objStatus.optLong("OID");
//            boolean exchangeStatus = objStatus.optBoolean("EXCHANGE", false);
//            try {
//                if (statusObj) {
//                    cbm = PstBillMain.fetchExc(cbmOID);
//                    cbm.setNotes(notes);
//                    cbm.setStatus(status);
//                    long oidNew = PstBillMain.updateExc(cbm);
//                    long bdOid = PstBillDetail.deleteExc(bdOID);
//                    this.oidPinjaman = "" + oidPinjaman;
//                    this.exchange = "" + exchangeStatus;
//                    this.message = "Exchange Item berhasil!";
//                } else {
//                    this.message = "Exchange Item gagal!";
//                }
//
//            } catch (Exception e) {
//                printErrorMessage(e.getMessage());
//            }
//        }

    public void saveDokumen(HttpServletRequest request) {
        String dokumenName = FRMQueryString.requestString(request, "DOKUMEN_NAME");
        String keterangan = FRMQueryString.requestString(request, "KETERANGAN");
        BillMain cbm = new BillMain();
        BillImageMapping bim = new BillImageMapping();
        try {
            long oidNew = 0;
            bim.setBillMainId(this.oidBm);
            bim.setDocumentName(dokumenName);
            bim.setKeterangan(keterangan);
            if (this.oidBm != 0) {
                oidNew = PstBillImageMapping.insertExc(bim);
            }
            if (oidNew != 0) {
                this.message = "Tambah Dokumen Berhasil!";
            } else {
                this.message = "Tambah Dokumen Gagal!";
            }
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    //COMMAND DELETE==============================================================
    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("deleteDokumen")) {
            deleteDokumen(request);
        }
    }

    public void deleteDokumen(HttpServletRequest request) {
        BillMain cbm = new BillMain();
        BillImageMapping bim = new BillImageMapping();
        try {
            bim = PstBillImageMapping.fetchExc(this.oid);
            long oidNew = PstBillImageMapping.deleteExc(this.oid);

            if (oidNew != 0) {
                this.message = "Penghapusan Dokumen Berhasil!";
            } else {
                this.message = "Penghapusan Dokumen Gagal!";
            }

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    //COMMAND NONE==============================================================
    public void commandNone(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("getDokumen")) {
            getDokumenPengiriman(request);
        } else if (this.dataFor.equals("sendProduction")) {
            sendProduction(request);
        } else if (this.dataFor.equals("checkCourier")) {
            checkCourier(request);
        }
    }

    public void getDokumenPengiriman(HttpServletRequest request) {
        long cbmOID = FRMQueryString.requestLong(request, "BILL_MAIN_OID");
//    String link = PstSystemProperty.getValueByName("IMGDOC");
        String url = PstSystemProperty.getValueByName("POS_APP_URL");
        String link = url + "/imgupload/";
        BillMain cbm = new BillMain();
        try {
            String html = "";
            String li = "";
            String div = "";
            String active = "";
            String active1 = "";
            int count = -1;
            String where = PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_BILL_MAIN_ID] + " = " + cbmOID;
            Vector<BillImageMapping> listData = PstBillImageMapping.list(0, 0, where, "");
            if (!listData.isEmpty()) {
                for (BillImageMapping bim : listData) {
                    if (!bim.getFileName().equals("")) {
                        count++;
                        if (count == 0 && active.equals("")) {
                            active = "active";
                            active1 = "active";
                        } else {
                            active = "";
                            active1 = "";
                        }
                        div += ""
                                + "<div class='item " + active1 + "'>"
                                + "<img src='" + link + bim.getFileName() + "' alt='" + bim.getFileName() + "' class='center'>"
                                + "</div>"
                                + "";
                    }
                }
                html += ""
                        + "<div class='carousel-inner'>"
                        + "" + div + ""
                        + "</div>";
                this.htmlReturn = html;
            } else {
                html += ""
                        + "<div class='carousel-inner'>"
                        + "<div class='item active'>"
                        + "<img src='...' alt='No Document' style='width: 100%;height: 380px;overflow: hidden;'>"
                        + "</div>"
                        + "</div>";
                this.htmlReturn = html;
            }
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    public void sendProduction(HttpServletRequest request) {
        try {
            JSONObject messages = new JSONObject();
            String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
            BillMain cbm = new BillMain();
            BillMain bm = new BillMain();
            boolean benar = false;
            long petugasnya = 0;

            //Mendapatkan Sample Petugas
            for (int ii = 0; ii < oids.length; ii++) {
                bm = PstBillMain.fetchExc(Long.parseLong(oids[ii]));
                petugasnya = bm.getDoPersonId();
            }

            //Cek Validasi Petugas Apakah sudah sama
            for (int ii = 0; ii < oids.length; ii++) {
                bm = PstBillMain.fetchExc(Long.parseLong(oids[ii]));
                if (bm.getDoPersonId() == petugasnya) {
                    benar = true;
                } else {
                    this.iErrCode = 1;
                    this.message = "Pastikan delivery courrier sama";
                    return;

                }
            }

            //Esekusi Jika Benar
            for (int i = 0; i < oids.length; i++) {
                try {
                    if (benar) {
                        cbm = PstBillMain.fetchExc(Long.parseLong(oids[i]));
                        cbm.setStatus(PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM);
//                        String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
//                        String url = apiUrl + "/pinjaman-update-realisasi/" + cbm.getOID();
//                        JSONObject obj = WebServices.getAPI("", url);
                        long oidDis = PstBillMain.updateExc(cbm);
                        if (oidDis != 0) {
                            this.message = "Pengiriman berhasil ditambahkan";
                        }
                    } else {
                        this.iErrCode = 1;
                        this.message = "Pastikan delivery courrier sama";
                        return;
                    }

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                }
            }
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    public void checkCourier(HttpServletRequest request) {
        try {
            JSONObject messages = new JSONObject();
            String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
            BillMain cbm = new BillMain();
            BillMain bm = new BillMain();
            boolean benar = false;
            long petugasnya = 0;

            //Mendapatkan Sample Petugas
            for (int i = 0; i < oids.length; i++) {
                bm = PstBillMain.fetchExc(Long.parseLong(oids[i]));
                petugasnya = bm.getDoPersonId();
            }

            //Cek Validasi Petugas Apakah sudah sama
            for (int ii = 0; ii < oids.length; ii++) {
                bm = PstBillMain.fetchExc(Long.parseLong(oids[ii]));
                if (bm.getDoPersonId() == petugasnya) {
                    benar = true;
                } else {
                    this.iErrCode = 1;
                    this.message = "Pastikan delivery courrier sama";
                    return;

                }
            }
            if (benar) {
                this.iErrCode = 0;
            }

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    //COMMAND LIST==============================================================
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listProduksiAll")) {
            String[] cols = {
                "",
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE],
                " JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT],
                " LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT],
                " AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            String[] cols = {
                "",
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE],
                " JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT],
                " LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT],
                " AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listItemDetail")) {
            String[] cols = {
                "",
                PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME],
                PstBillDetail.fieldNames[PstBillDetail.FLD_SKU],
                PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME],
                PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                PstColor.fieldNames[PstColor.FLD_COLOR_NAME],
                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME],
                PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("itemBanyak")) {
            String[] cols = {
                "",
                PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME],
                PstBillDetail.fieldNames[PstBillDetail.FLD_SKU],
                PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME],
                PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                PstColor.fieldNames[PstColor.FLD_COLOR_NAME],
                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME],
                PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listDokumenDetail")) {
            String[] cols = {
                "",
                PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_DOCUMENT_NAME],
                PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_FILE_NAME],
                PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_KETERANGAN],
                PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_KETERANGAN]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "sSearch");
        int amount = 10;
        int start = 0;
        int col = 0;
        String addSql = "";
        String dir = "asc";
        String sStart = request.getParameter("iDisplayStart");
        String sAmount = request.getParameter("iDisplayLength");
        String sCol = request.getParameter("iSortCol_0");
        String sdir = request.getParameter("sSortDir_0");

        if (sStart != null) {
            start = Integer.parseInt(sStart);
            if (start < 0) {
                start = 0;
            }
        }
        if (sAmount != null) {
            amount = Integer.parseInt(sAmount);
            if (amount < 10) {
                amount = 10;
            }
        }
        if (sCol != null) {
            col = Integer.parseInt(sCol);
            if (col < 0) {
                col = 0;
            }
        }
        if (sdir != null) {
            if (!sdir.equals("asc")) {
                dir = "desc";
            }
        }

        String whereClause = "";
        if (dataFor.equals("listProduksiAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR EM." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + "AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR EM." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + "AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
        } else if (this.dataFor.equals("listDokumenDetail")) {
            whereClause += "("
                    + " " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_DOCUMENT_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_FILE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_KETERANGAN] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + "AND ( " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_BILL_MAIN_ID] + " = " + this.oid + ")";
        } else if (this.dataFor.equals("listItemDetail")) {
            whereClause += PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + this.oid;
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listProduksiAll")) {
            total = PstPinjaman.countPengiriman(whereClause);
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            total = PstPinjaman.countPengiriman(whereClause);
        } else if (this.dataFor.equals("listItemDetail")) {
            total = PstBillDetail.getCount(whereClause);
        } else if (this.dataFor.equals("listDokumenDetail")) {
            total = PstBillImageMapping.getCount(whereClause);
        }
        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor, addSql);
        } catch (Exception ex) {
            printErrorMessage(ex.getMessage());
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor, String addSql) {
        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        BillMain billMain = new BillMain();
        Location location = new Location();
        JenisKredit typeKredit = new JenisKredit();
        Contact anggota = new Contact();
        Pinjaman pinjaman = new Pinjaman();
        Employee employee = new Employee();
        Billdetail billdetail = new Billdetail();
        Material material = new Material();
//		SumberDana sumberDana = new SumberDana();
//		LogSysHistory history = new LogSysHistory();
//		JenisTransaksi jenisTransaksi = new JenisTransaksi();

        String whereClause = "";
        String order = "";

        if (datafor.equals("listProduksiAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR EM." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + " AND ( "
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE
                    + ")";
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + " OR EM." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + "AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
        } else if (this.dataFor.equals("listItemDetail")) {
            whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + this.oid;
        } else if (this.dataFor.equals("listDokumenDetail")) {
            whereClause += "("
                    + " " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_DOCUMENT_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_FILE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_KETERANGAN] + " LIKE '%" + searchTerm + "%'"
                    + ")"
                    + "AND ( " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_BILL_MAIN_ID] + " = " + this.oid + ")";
        }
        if (this.colOrder >= 0 && !this.dataFor.equals("listDokumenDetail")) {
            order += "CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " ASC";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listProduksiAll")) {
            listData = PstPinjaman.listPengiriman(start, amount, whereClause, order);
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            listData = PstPinjaman.listPengiriman(start, amount, whereClause, order);
        } else if (this.dataFor.equals("listItemDetail")) {
            listData = PstBillDetail.list(start, amount, whereClause, "");
        } else if (this.dataFor.equals("listDokumenDetail")) {
            listData = PstBillImageMapping.list(start, amount, whereClause, "");
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listProduksiAll")) {
                pinjaman = (Pinjaman) listData.get(i);
                try {
                    billMain = PstBillMain.fetchExc(pinjaman.getBillMainId());
                    anggota = PstContact.fetchExc(pinjaman.getAnggotaId());
                    location = PstLocation.fetchExc(billMain.getLocationId());
                    typeKredit = PstJenisKredit.fetch(pinjaman.getTipeKreditId());
                    employee = PstEmployee.fetchExc(billMain.getDoPersonId());

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + billMain.getInvoiceNumber() + "");
                ja.put("" + anggota.getPersonName());
                ja.put("<div class='text-center'> " + Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyyy") + " </div>");
                ja.put("" + PstBillMain.payType[billMain.getTransctionType()] + "");
                ja.put("<div class='text-center'>" + typeKredit.getNamaKredit() + "</div>");
                ja.put("<div class='text-center'>" + location.getName() + "</div>");
                ja.put("<div class='text-center'>" + Formater.formatNumber(billMain.getAmount(), "###,###") + "</div>");
                ja.put("<div class='text-center'> " + Formater.formatDate(billMain.getShippingDate(), "dd-MM-yyyy") + " </div>");
                ja.put("" + employee.getFullName());
                ja.put("<div class='text-center'>" + PstBillMain.produksiDeliveryStatus[billMain.getStatus()] + "</div>");
                String button = "<div class='text-center'>"
                        + "<input type='checkbox' title='send' "
                        + "class='checkbox detail-produksi-btn' id='choose'"
                        + "name='" + FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID] + "' data-oid='" + pinjaman.getOID() + "'>"
                        + "<button type='button' title='Dokumen' "
                        + "class='btn btn-xs btn-primary dokumen-produksi-btn' "
                        + "data-oid='" + pinjaman.getOID() + "'> "
                        + "<i class='fa fa-file-text-o'></i>"
                        + "</button> <br> "
                        + "</div>";
                ja.put(button);
                array.put(ja);
            } else if (this.dataFor.equals("listStatusPengiriman")) {
                pinjaman = (Pinjaman) listData.get(i);
                try {
                    billMain = PstBillMain.fetchExc(pinjaman.getBillMainId());
                    anggota = PstContact.fetchExc(pinjaman.getAnggotaId());
                    location = PstLocation.fetchExc(billMain.getLocationId());
                    typeKredit = PstJenisKredit.fetch(pinjaman.getTipeKreditId());
                    employee = PstEmployee.fetchExc(billMain.getDoPersonId());

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + billMain.getInvoiceNumber() + "");
                ja.put("" + anggota.getPersonName());
                ja.put("<div class='text-center'> " + Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyyy") + " </div>");
                ja.put("" + PstBillMain.payType[billMain.getTransctionType()] + "");
                ja.put("<div class='text-center'>" + typeKredit.getNamaKredit() + "</div>");
                ja.put("<div class='text-center'>" + location.getName() + "</div>");
                ja.put("<div class='text-center'>" + Formater.formatNumber(billMain.getAmount(), "###,###") + "</div>");
                ja.put("<div class='text-center'> " + Formater.formatDate(billMain.getShippingDate(), "dd-MM-yyyy") + " </div>");
                ja.put("" + employee.getFullName());
                ja.put("<div class='text-center'>" + PstBillMain.produksiDeliveryStatus[billMain.getStatus()] + "</div>");
                String button = "<div class='text-center'>"
                        + "<button type='button' title='send' "
                        + "class='btn btn-sm btn-success' data-toggle=\"modal\" data-target=\"#modalItem\""
                        + "data-oid='" + billMain.getOID() + "'> "
                        + "<i class=\"fa fa-file\"> </i> Item</button></div>";
                ja.put(button);
                array.put(ja);
            } else if (this.dataFor.equals("listItemDetail")) {
                billdetail = (Billdetail) listData.get(i);
                ContactList con = new ContactList();
                Color col = new Color();
                MaterialTypeMapping mtm = new MaterialTypeMapping();
                String namaKonsumen = "";
                String sku = "";
                String itemName = "";
                String barcode = "";
                String color = "";
                String type = "";
                double qty = 0;
                try {
                    material = PstMaterial.fetchExc(billdetail.getMaterialId());
                    billMain = PstBillMain.fetchExc(billdetail.getBillMainId());
                    con = PstContactList.fetchExc(billMain.getCustomerId());
                    if (material.getPosColor() != 0) {
                        col = PstColor.fetchExc(material.getPosColor());
                    }

                    namaKonsumen = con.getPersonName();
                    sku = material.getSku();
                    itemName = material.getName();
                    barcode = material.getBarCode();
                    color = col.getColorName();
                    qty = billdetail.getQty();

                    Vector listMatype = PstMaterialMappingType.list(0, 0, PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + " = " + material.getOID(), "");
                    for (int xx = 0; xx < listMatype.size(); xx++) {
                        mtm = (MaterialTypeMapping) listMatype.get(xx);
                        Vector listMastype = PstMasterType.list(0, 0, PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + " = 2 AND " + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + " = " + mtm.getTypeId(), "");
                        for (int xxx = 0; xxx < listMastype.size(); xxx++) {
                            MasterType mt = (MasterType) listMastype.get(xxx);
                            type = mt.getMasterName();

                        }
                    }

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("<div class='text-center'> " + ((namaKonsumen.equals("") || namaKonsumen == null) ? "-" : namaKonsumen) + " </div>");
                ja.put("<div class='text-center'> " + ((sku.equals("") || sku == null) ? "-" : sku) + " </div>");
                ja.put("<div class='text-center'> " + ((itemName.equals("") || itemName == null) ? "-" : itemName) + " </div>");
                ja.put("<div class='text-center'> " + ((barcode.equals("") || barcode == null) ? "-" : barcode) + " </div>");
                ja.put("<div class='text-center'> " + ((color.equals("") || color == null) ? "-" : color) + " </div>");
                ja.put("<div class='text-center'> " + ((type.equals("") || type == null) ? "-" : type) + " </div>");
                ja.put("<div class='text-center'> " + ((qty == 0) ? "0" : qty) + " </div>");
                array.put(ja);

            } else if (this.dataFor.equals("listDokumenDetail")) {
                BillImageMapping bim = (BillImageMapping) listData.get(i);
                String url = PstSystemProperty.getValueByName("POS_APP_URL");
                String link = url + "/imgupload/";

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("<div class='text-center'> " + bim.getDocumentName() + " </div>");
                if (bim.getFileName().equals("")) {
                    ja.put("<div class='text-center'>"
                            + "<button type='button' class='btn btn-primary btn-upload' data-oidbim='" + bim.getOID() + "' data-bmain='" + bim.getBillMainId() + "'><i class='fa fa-upload'> </i> Upload</button>"
                            + "</div>");
                } else {
                    ja.put("<div class='text-center'>" + bim.getFileName() + " </div>");
                }
                ja.put("<div class='text-center'> " + bim.getKeterangan() + " </div>");
                if (!bim.getFileName().equals("")) {
                    ja.put("<div class='text-center'>"
                            + "<a class='btn btn-primary' href='" + link + bim.getFileName() + "' data-oidbim='" + bim.getOID() + "' download ><i class='fa fa-download'> </i> </a>"
                            + "<a type='button' class='btn btn-danger delete-dokumen'  data-oidbim='" + bim.getOID() + "' data-bmain='" + bim.getBillMainId() + "'><i class='fa fa-trash'> </i> </a>"
                            + "</div>");
                } else {
                    ja.put("<div class='text-center'>"
                            + "<a type='button' class='btn btn-danger delete-dokumen'  data-oidbim='" + bim.getOID() + "' data-bmain='" + bim.getBillMainId() + "'><i class='fa fa-trash'> </i> </a>"
                            + "</div>");
                }
                array.put(ja);
            }
        }
        if (this.dataFor.equals("itemBanyak")) {
            String[] oids = FRMQueryString.requestStringValues(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
            BillMain cbm = new BillMain();
            for (int i = 0; i < oids.length; i++) {
                try {
                    cbm = PstBillMain.fetchExc(Long.parseLong(oids[i]));
                } catch (Exception e) {
                }
                whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + cbm.getOID();
                listData = PstBillDetail.list(start, amount, whereClause, "");
                for (int x = 0; x < listData.size(); x++) {
                    JSONArray ja = new JSONArray();
                    billdetail = (Billdetail) listData.get(x);
                    ContactList con = new ContactList();
                    Color col = new Color();
                    MaterialTypeMapping mtm = new MaterialTypeMapping();

                    String namaKonsumen = "";
                    String sku = "";
                    String itemName = "";
                    String barcode = "";
                    String color = "";
                    String type = "";
                    double qty = 0;
                    try {
                        material = PstMaterial.fetchExc(billdetail.getMaterialId());
                        billMain = PstBillMain.fetchExc(billdetail.getBillMainId());
                        con = PstContactList.fetchExc(billMain.getCustomerId());
                        if (material.getPosColor() != 0) {
                            col = PstColor.fetchExc(material.getPosColor());
                        }

                        namaKonsumen = con.getPersonName();
                        sku = material.getSku();
                        itemName = material.getName();
                        barcode = material.getBarCode();
                        color = col.getColorName();
                        qty = billdetail.getQty();

                        Vector listMatype = PstMaterialMappingType.list(0, 0, PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + " = " + material.getOID(), "");
                        for (int xx = 0; xx < listMatype.size(); xx++) {
                            mtm = (MaterialTypeMapping) listMatype.get(xx);
                            Vector listMastype = PstMasterType.list(0, 0, PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + " = 2 AND " + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + " = " + mtm.getTypeId(), "");
                            for (int xxx = 0; xxx < listMastype.size(); xxx++) {
                                MasterType mt = (MasterType) listMastype.get(xxx);
                                type = mt.getMasterName();

                            }
                        }

                    } catch (Exception e) {
                        printErrorMessage(e.getMessage());
                        message = e.toString();
                    }

                    ja.put("" + (this.start + i + 1) + ".");
                    ja.put("<div class='text-center'> " + ((namaKonsumen.equals("") || namaKonsumen == null) ? "-" : namaKonsumen) + " </div>");
                    ja.put("<div class='text-center'> " + ((sku.equals("") || sku == null) ? "-" : sku) + " </div>");
                    ja.put("<div class='text-center'> " + ((itemName.equals("") || itemName == null) ? "-" : itemName) + " </div>");
                    ja.put("<div class='text-center'> " + ((barcode.equals("") || barcode == null) ? "-" : barcode) + " </div>");
                    ja.put("<div class='text-center'> " + ((color.equals("") || color == null) ? "-" : color) + " </div>");
                    ja.put("<div class='text-center'> " + ((type.equals("") || type == null) ? "-" : type) + " </div>");
                    ja.put("<div class='text-center'> " + ((qty == 0) ? "0" : qty) + " </div>");
                    array.put(ja);
                }
            }
        }

        totalAfterFilter = total;
        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
        return result;
    }

    public static int checkString(String strObject, String toCheck) {
        if (toCheck == null || strObject == null) {
            return -1;
        }
        if (strObject.startsWith("=")) {
            strObject = strObject.substring(1);
        }

        String[] parts = strObject.split(" ");
        if (parts.length > 0) {
            for (int i = 0; i < parts.length; i++) {
                String p = parts[i];
                if (toCheck.trim().equalsIgnoreCase(p.trim())) {
                    return i;
                };
            }
        }
        return -1;
    }

    public static double getValue(String formula) {
        DBResultSet dbrs = null;
        double compValueX = 0;
        try {
            String sql = "SELECT (" + formula + ")";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                compValueX = rs.getDouble(1);
            }

            rs.close();
            return compValueX;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public int convertInteger(int scale, double val) {
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.HALF_UP);
        return bDecimal.intValue();
    }

    public void printErrorMessage(String errorMessage) {
        System.out.println("");
        System.out.println("========================================>>> WARNING <<<========================================");
        System.out.println("");
        System.out.println("MESSAGE : " + errorMessage);
        System.out.println("");
        System.out.println("========================================<<< * * * * >>>========================================");
        System.out.println("");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}






