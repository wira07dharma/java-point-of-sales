/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax.production;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
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
import com.dimata.pos.form.billing.FrmBillDetail;
import com.dimata.pos.form.billing.FrmBillMain;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.posbo.form.purchasing.CtrlPurchaseRequest;
import com.dimata.posbo.form.purchasing.CtrlPurchaseRequestItem;
import com.dimata.posbo.form.purchasing.FrmPurchaseRequest;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.entity.kredit.PstTypeKredit;
import com.dimata.services.WebServices;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
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
 * @author arise
 */
public class AjaxProduksi extends HttpServlet {

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
    private String apiUrl = "";
    private String sedanaAppUrl = "";
    private String hrApiUrl = "";
    private String oidDelivery = "";
    private String form_location = "";
    private String form_position = "";

    private long userId = 0;
    private String userName = "";
    private String whereSedana = "";
    private String whereHairisma = "";
    private long oidCbm = 0;
    private long oid = 0;

    private boolean sessLogin = false;

    private int iCommand = 0;
    private int iErrCode = 0;
    private int sessLanguage = 0;

    private String start_date = "";
    private String end_date = "";
    private String petugas_delivery = "";
    private String noBillConvert = "";
    private String personName = "";

    private NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ID", "id"));

    DataCustom dc = new DataCustom();
    Vector user = new Vector();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        this.jsonArrayJenisProduksi = new JSONArray();
        this.jsonArrayDataProduksi = new JSONArray();
        this.jsonArrayPrintValue = new JSONArray();
        this.jSONArray = new JSONArray();
        this.jSONObject = new JSONObject();

        this.hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
        this.sedanaAppUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
        this.oidDelivery = PstSystemProperty.getValueByName("STAFF_DELIVERY_OID");
        this.form_location = FRMQueryString.requestString(request, "form_location");
        this.form_position = FRMQueryString.requestString(request, "position");
        this.htmlReturn = "";
        this.message = "";

        this.userId = FRMQueryString.requestLong(request, "SEND_USER_ID");
        this.userName = FRMQueryString.requestString(request, "SEND_USER_NAME");
        this.oidCbm = FRMQueryString.requestLong(request, "oid");
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.start_date = FRMQueryString.requestString(request, "start_date");
        this.end_date = FRMQueryString.requestString(request, "end_date");
        this.petugas_delivery = FRMQueryString.requestString(request, "petugas_delivery");

        this.sessLogin = false;

        AppUser au = new AppUser();
        iCommand = FRMQueryString.requestCommand(request);

        try {
            au = PstAppUser.fetch(this.userId);
        } catch (Exception e) {
        }
        String where = PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = 'user_create_document_location' " + " AND " + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + " = " + this.userId;
        this.user = PstDataCustom.list(0, 0, where, "");
        for (int i = 0; i < this.user.size(); i++) {
            dc = (DataCustom) this.user.get(i);
        }

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
                    commandSave(request, response);
                    break;

                case Command.DELETE:
                    //commandDelete(request);
                    break;

                case Command.LIST:
                    commandList(request, response);
                    break;

                default:
                    commandNone(request);
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

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("getPrintKwitansi")) {
            getPrintKwitansi(request);
        }
    }

    public void getPrintKwitansi(HttpServletRequest request) {
        String data = "";
        try {
            String url = this.apiUrl + "/document/document/kwitansi-pembayaran/" + this.oid;
            JSONObject res = WebServices.getAPI("", url);
            data = res.getString("DATA");
            System.out.println("URL Document : " + url);
        } catch (Exception e) {
            data = "";
        }
        this.htmlReturn = data;
    }

    public void commandSave(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("assignPetugasDelivery")) {
            assignPetugas(request);
        } else if (this.dataFor.equals("createSrDoc")) {
            createSrDoc(request, response);
        }
    }

    public void assignPetugas(HttpServletRequest request) {
        long cbmOID = FRMQueryString.requestLong(request, "cbmOID");
        long empOID = FRMQueryString.requestLong(request, "empOID");
        int deliveryStatus = FRMQueryString.requestInt(request, "deliveryStatus");
        Date jadwalKirim = Formater.formatDate(FRMQueryString.requestString(request, "jadwalKirim"), "yyyy-MM-dd");
        BillMain cbm = new BillMain();
        String url = this.apiUrl + "/credit/update-bill-status-produksi/" + deliveryStatus + "/" + cbmOID;
        JSONObject obj = WebServices.getAPI("", url);
        if (deliveryStatus==PstBillMain.PETUGAS_DELIVERY_STATUS_DIAMBIL_LANGSUNG){
            String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
            url = apiUrl + "/pinjaman-update-realisasi/" + cbmOID;
            JSONObject j = WebServices.getAPI("", url);
        } else if (deliveryStatus==PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM){
            String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
            url = apiUrl + "/pinjaman-update-realisasi-by-tanggal?oid=" + cbmOID+"&tanggal="+Formater.formatDate(jadwalKirim,"yyyy-MM-dd");
            JSONObject j = WebServices.getAPI("", url);
        }
        try {
            cbm = PstBillMain.fetchExc(cbmOID);
            if (empOID != 0) {
                cbm.setDoPersonId(empOID);
            }
            if (jadwalKirim != null) {
                cbm.setShippingDate(jadwalKirim);
            }
            cbm.setStatus(deliveryStatus);
            long oid = PstBillMain.updateExc(cbm);
            if (oid != 0) {
                this.message = "Simpan Berhasil!";
            } else {
                this.message = "Simpan Gagal! Pastikan data kembali dengan benar";
            }
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
    }

    public void createSrDoc(HttpServletRequest request, HttpServletResponse response) {
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        long oid = FRMQueryString.requestLong(request, "oid");
        String[] cbdIds = FRMQueryString.requestStringValues(request, FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID]);
        int errCnt = -1;
        if (cbdIds.length > 0) {
            try {
                CtrlPurchaseRequest cpr = new CtrlPurchaseRequest(request);
                cpr.action(this.iCommand, oid, userName, userId);
                oid = cpr.getOID();
                for (int i = 0; i < cbdIds.length; i++) {
                    long tempBdId = Long.parseLong(cbdIds[i]);
                    Billdetail cbd = PstBillDetail.fetchExc(tempBdId);

                    PurchaseRequestItem prItem = new PurchaseRequestItem();
                    prItem.setMaterialId(cbd.getMaterialId());
                    prItem.setUnitId(cbd.getUnitId());
                    prItem.setQuantity(cbd.getQty());
                    prItem.setPurchaseOrderId(oid);

                    long oidRes = PstPurchaseRequestItem.insertExc(prItem);
                    if (oidRes != 0) {
                        errCnt = 0;
                    } else {
                        errCnt++;
                    }

                }
            } catch (Exception e) {
                printErrorMessage(e.getMessage());
            }
        }
        if (errCnt == 0) {
            this.message = "Dokumen Store Request Berhasil Dibuat";
            this.htmlReturn = "/purchasing/material/pom/prtowarehousematerial_edit.jsp?command=" + Command.EDIT + "&hidden_material_request_id=" + oid;
        }
    }

    //COMMAND LIST==============================================================
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listProduksiAll")) {
            String[] cols = {
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE],
                " JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT],
                " LC." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT],
                " AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listPengirimanAll")) {
            String[] cols = {
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE],
                " JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT],
                " LC." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE],
                " AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listStatusPengiriman")) {
            String[] cols = {
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE],
                " JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT],
                " LC." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE],
                " AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listPetugas")) {
            String[] cols = {
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_PHONE],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE],
                " emp." + PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listPemesanan")) {
            String[] cols = {
                " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                " MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                " MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                " CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME],
                " UN." + PstUnit.fieldNames[PstUnit.FLD_NAME],
                " MK." + PstMerk.fieldNames[PstMerk.FLD_NAME],
                " MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listItemForExchange")) {
            String[] cols = {
                " " + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                " " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                " " + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                " " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listBarangProduksi")) {
            String[] cols = {
                " " + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU],
                " " + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID],
                " " + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME],
                " " + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME],
                " " + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listDokumenPengiriman")) {
            String[] cols = {
                " " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE],
                " " + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("select2Delivery")) {
            jSONObject = listSelect2(request, response, this.dataFor, this.jSONObject);
        }
    }

    public JSONObject listSelect2(HttpServletRequest request, HttpServletResponse response, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "searchTerm");
        this.start = FRMQueryString.requestInt(request, "limitStart");
        this.amount = FRMQueryString.requestInt(request, "recordToGet");
        String oidDelivery = PstSystemProperty.getValueByName("STAFF_DELIVERY_OID");
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        JSONObject pagination = new JSONObject();

        String whereClause = "";
        String whereClausee = "";
        String order = "";
        Vector listData = new Vector(1, 1);
        int countData = 0;
        JSONObject jo = new JSONObject();
        JSONObject jon = new JSONObject();
        if (this.dataFor.equals("select2Delivery")) {
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + oidDelivery + ")"
                    + " AND ( " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + this.searchTerm + "%' )";
            whereClausee = PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + oidDelivery + ")";
            String param = "limitStart=" + WebServices.encodeUrl("" + this.start) + "&recordToGet=" + WebServices.encodeUrl("" + this.amount)
                    + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
            String parama = "limitStart=" + WebServices.encodeUrl("") + "&recordToGet=" + WebServices.encodeUrl("")
                    + "&whereClause=" + WebServices.encodeUrl(whereClausee) + "&order=" + WebServices.encodeUrl("");
            jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
            jon = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", parama);
        }
        try {

            JSONArray jArr = new JSONArray();
            JSONArray jArra = new JSONArray();
            JSONArray array = new JSONArray();
            int countDataFetched = 0;
            jArr = jo.getJSONArray("DATA");
            countData = jon.getInt("COUNT");
            if (jArr.length() > 0) {
                for (int x = 0; x < jArr.length(); x++) {
                    JSONObject temp = new JSONObject();
                    if (this.dataFor.equals("select2Delivery")) {
                        JSONArray jArray = jArr.getJSONArray(x);
                        JSONObject empObj = jArray.getJSONObject(0);
                        JSONObject divObj = jArray.getJSONObject(1);
//                        System.out.println("ArrayData Petugas : " + jArr);
                        long id = empObj.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                        if ((this.start + x) == 0) {
                            String namaNasabah = PstSystemProperty.getValueByName("SEDANA_NASABAH_NAME");
                            temp.put("id", 0);
                            temp.put("text", "-- Semua Delivery --");
                            array.put(temp);
                        }
                        temp = new JSONObject();
                        temp.put("id", String.valueOf(id));
                        temp.put("text", empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-"));
                        array.put(temp);
                    }
                    countDataFetched++;
                }
            }

            pagination.put("CURRENT_PAGE", start + countDataFetched);
            pagination.put("ITEM_PER_PAGE", this.amount);

            result.put("ANGGOTA_DATA", array);
            result.put("ANGGOTA_TOTAL", countData);
            result.put("PAGINATION_HEADER", pagination);
            System.out.println("Ajax Produksi | PAGINATION_HEADER : " + pagination);
//            System.out.println("Ajax Produksi | WebServices : " + jo + " JUMLAH : "+ countData);
        } catch (Exception e) {
            System.out.println("Ajax Produksi | listSelect2 : " + e.getMessage());
        }

        return result;
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

        try {
            String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
            String hairismaUrlApi = PstSystemProperty.getValueByName("HARISMA_URL");
            String url = apiUrl + "/no-kredit-to-no-bill?nokredit=" + searchTerm;
            if (searchTerm.length() > 3) {
                JSONObject jsonObj = WebServices.getAPI("", url);
                this.noBillConvert = jsonObj.optString("NO_BILL", "");
            }

            if (dataFor.equals("listPengirimanAll") || dataFor.equals("listStatusPengiriman")) {
                if (searchTerm.length() > 3) {
                    String urlHaris = hairismaUrlApi + "/courier-person?name=" + searchTerm;
                    JSONObject obj = WebServices.getAPI("", urlHaris);
                    this.personName = obj.optString("PERSON_ID", "");
                }
            }
        } catch (Exception exc) {

        }

        String whereClause = "";
        if (dataFor.equals("listProduksiAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    //+ " OR AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_TGL_PENGAJUAN] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    //+ " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    //+ " OR AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN] + " LIKE '%" + searchTerm + "%'"
                    //                    + " OR AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_NO_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            this.whereSedana = " " + PstPinjaman.fieldNames[PstPinjaman.FLD_NO_KREDIT] + " LIKE '%" + searchTerm + "%' ";
        } else if (dataFor.equals("listPengirimanAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    + (this.personName.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " IN (" + personName + ")" : "")
                    + ")";
            System.out.println("PersonName : " + this.personName);
            this.whereSedana = " " + PstPinjaman.fieldNames[PstPinjaman.FLD_NO_KREDIT] + " LIKE '%" + searchTerm + "%' ";
            this.whereHairisma = " " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%' ";

        } else if (dataFor.equals("listStatusPengiriman")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    + (this.personName.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " IN (" + personName + ")" : "")
                    + ")";
            this.whereSedana = " " + PstPinjaman.fieldNames[PstPinjaman.FLD_NO_KREDIT] + " LIKE '%" + searchTerm + "%' ";
            this.whereHairisma = " " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%' ";

        } else if (dataFor.equals("listPetugas")) {
            whereClause += "("
                    + " emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_PHONE] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_HANDPHONE] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_ADDRESS] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            whereClause += " AND emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + this.form_position + ")";
            if (this.form_location != null && !this.form_location.equals("0")) {
                whereClause += " AND dv." + PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] + "='" + this.form_location + "'";
            }
        } else if (dataFor.equals("listPemesanan")) {
            whereClause += "("
                    + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR UN." + PstUnit.fieldNames[PstUnit.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR MK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                    + ")";
        } else if (dataFor.equals("listItemForExchange")) {
            whereClause += "("
                    + "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " LIKE '%" + searchTerm + "%'"
                    + ")";
        } else if (dataFor.equals("listBarangProduksi")) {
            whereClause += "("
                    + " BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            if ((this.start_date != null && this.start_date.length() > 0) && (this.end_date != null && this.end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + ">= TO_DAYS('" + this.start_date + "')) AND "
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + "<= TO_DAYS('" + this.end_date + "'))"
                        + ")";
            }
        } else if (dataFor.equals("listDokumenPengiriman")) {
            String noInv = FRMQueryString.requestString(request, "no_invoice");
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            if ((this.start_date != null && this.start_date.length() > 0) && (this.end_date != null && this.end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                        + ">= TO_DAYS('" + this.start_date + "')) AND "
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                        + "<= TO_DAYS('" + this.end_date + "'))"
                        + ")";
            }
            if (this.petugas_delivery != null && !this.petugas_delivery.equals("0")) {
                whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + "='" + this.petugas_delivery + "'";
            }
            String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
            String url = apiUrl + "/no-kredit-to-no-bill?nokredit=" + noInv;
            if (noInv.length() > 0) {
                JSONObject jsonObj = WebServices.getAPI("", url);
                String noBillConvert = jsonObj.optString("NO_BILL", "");
                if (noBillConvert.length()>0){
                    whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")";
                } else {
                    whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN ('" + noInv + "')";
                }
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listProduksiAll")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            total = PstPinjaman.countProduksi(whereClause);
        } else if (dataFor.equals("listPengirimanAll")) {
            whereClause += " AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " <>  0";
            total = PstPinjaman.countProduksi(whereClause);
        } else if (dataFor.equals("listStatusPengiriman")) {
            whereClause += " AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA + ""
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " <>  0";
            total = PstPinjaman.countProduksi(whereClause);
        } else if (dataFor.equals("listBarangProduksi")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            total = PstPinjaman.countBarangProduksi(whereClause);
        } else if (dataFor.equals("listDokumenPengiriman")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            total = PstPinjaman.countProduksi(whereClause);
        } else if (dataFor.equals("listPetugas")) {
            JSONArray jArr = new JSONArray();
            //whereClause = PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + this.oidDelivery + ")";
            String param = "limitStart=" + WebServices.encodeUrl("" + 0) + "&recordToGet=" + WebServices.encodeUrl("" + 0)
                    + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("");
            JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
            try {
                jArr = jo.getJSONArray("DATA");
            } catch (Exception e) {
            }
            total = jArr.length();
        } else if (dataFor.equals("listPemesanan")) {
            if (this.oidCbm != 0) {
                Pinjaman pinjam = new Pinjaman();
                try {
                    pinjam = PstPinjaman.fetchExc(this.oidCbm);
                } catch (Exception e) {
                }
                whereClause += " AND BM." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + pinjam.getBillMainId();
            }
            total = PstBillDetail.getCountListPemesanan(whereClause);
        } else if (dataFor.equals("listItemForExchange")) {
            total = PstMaterial.getCount(whereClause);
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
        Contact anggota = new Contact();
//		SumberDana sumberDana = new SumberDana();
//		LogSysHistory history = new LogSysHistory();
//		JenisTransaksi jenisTransaksi = new JenisTransaksi();

        String whereClause = "";
        String order = "";

//  To Data Custom get UserId :)
        boolean isLoggedIn = false;
        int userGroupNewStatus = -1;
        String userName = "";
        long userId = 0;
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 2);
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
        try {
            if (userSession == null) {
                userSession = new SessUserSession();
            } else {
                if (userSession.isLoggedIn() == true) {
                    isLoggedIn = true;
                    AppUser appUser = userSession.getAppUser();
                    userGroupNewStatus = appUser.getUserGroupNew();
                    userName = appUser.getLoginId();
                    userId = appUser.getOID();
                }
            }
        } catch (Exception exc) {
            //System.out.println(" >>> Exception during check login");
        }

        if (datafor.equals("listProduksiAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    //+ " OR AP." + PstPinjaman.fieldNames[PstPinjaman.FLD_TGL_PENGAJUAN] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    //+ " OR JK." + PstJenisKredit.fieldNames[PstJenisKredit.FLD_NAME_KREDIT] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    + ")";
        } else if (dataFor.equals("listPengirimanAll")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    + (this.personName.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " IN (" + personName + ")" : "")
                    + ")";

        } else if (dataFor.equals("listStatusPengiriman")) {
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CNT." + PstContact.fieldNames[PstContact.FLD_PERSON_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + (this.noBillConvert.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")" : "")
                    + (this.personName.length() > 0 ? " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " IN (" + personName + ")" : "")
                    + ")";

        } else if (dataFor.equals("listPetugas")) {
            whereClause += "("
                    + " emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_PHONE] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_HANDPHONE] + " LIKE '%" + searchTerm + "%'"
                    + " OR emp." + PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_ADDRESS] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            whereClause += " AND emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + this.form_position + ")";
            if (this.form_location != null && !this.form_location.equals("0")) {
                whereClause += " AND dv." + PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] + "='" + this.form_location + "'";
            }
        } else if (datafor.equals("listPemesanan")) {
            whereClause += "("
                    + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                    + " OR MT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR UN." + PstUnit.fieldNames[PstUnit.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR MK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                    + ")";
        } else if (dataFor.equals("listItemForExchange")) {
            whereClause += "("
                    + "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " LIKE '%" + searchTerm + "%'"
                    + ")";
        } else if (dataFor.equals("listBarangProduksi")) {
            whereClause += "("
                    + " BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                    + " OR BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            if ((this.start_date != null && this.start_date.length() > 0) && (this.end_date != null && this.end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + ">= TO_DAYS('" + this.start_date + "')) AND "
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + "<= TO_DAYS('" + this.end_date + "'))"
                        + ")";
            }
        } else if (dataFor.equals("listDokumenPengiriman")) {
            String noInv = FRMQueryString.requestString(request, "no_invoice");
            whereClause += "("
                    + " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " LIKE '%" + searchTerm + "%'"
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            if ((this.start_date != null && this.start_date.length() > 0) && (this.end_date != null && this.end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                        + ">= TO_DAYS('" + this.start_date + "')) AND "
                        + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                        + "<= TO_DAYS('" + this.end_date + "'))"
                        + ")";
            }
            if (this.petugas_delivery != null && !this.petugas_delivery.equals("0")) {
                whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + "='" + this.petugas_delivery + "'";
            }
            String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
            String url = apiUrl + "/no-kredit-to-no-bill?nokredit=" + noInv;
            if (noInv.length() > 0) {
                JSONObject jsonObj = WebServices.getAPI("", url);
                String noBillConvert = jsonObj.optString("NO_BILL", "");
                if (noBillConvert.length()>0){
                    whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN (" + noBillConvert + ")";
                } else {
                    whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " IN ('" + noInv + "')";
                }
            }
        }

        if (this.colOrder >= 0) {
            if (dataFor.equals("listProduksiAll")) {
                order += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
            } else if (dataFor.equals("listPengirimanAll")) {
                order += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
            } else if (dataFor.equals("listStatusPengiriman")) {
                order += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
            } else if (dataFor.equals("listBarangProduksi")) {
                order += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
            } else if (dataFor.equals("listDokumenPengiriman")) {
                order += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + ", CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + " ASC";
            } else {
                order += "" + colName + " " + dir + "";
            }
        }

        Vector listData = new Vector(1, 1);
        JSONArray jArr = new JSONArray();
        if (datafor.equals("listProduksiAll")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            listData = PstPinjaman.listProduksi(start, amount, whereClause, order);
        } else if (datafor.equals("listPengirimanAll")) {
            whereClause += " AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_AKAN_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " <>  0";
            listData = PstPinjaman.listProduksi(start, amount, whereClause, order);
        } else if (dataFor.equals("listStatusPengiriman")) {
            whereClause += " AND ( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DIKIRIM
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA + ""
                    + " OR CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.PETUGAS_DELIVERY_STATUS_RESCHEDULE + ")";
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " <>  0";
            listData = PstPinjaman.listProduksi(start, amount, whereClause, order);
        } else if (datafor.equals("listBarangProduksi")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            listData = PstPinjaman.listBarangProduksi(start, amount, whereClause, order);
        } else if (datafor.equals("listDokumenPengiriman")) {
            whereClause += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " <> " + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
            String stt = "";
            if (this.dc.getOwnerId() != 0) {
                for (int i = 0; i < this.user.size(); i++) {
                    this.dc = (DataCustom) this.user.get(i);
                    if (stt.length() != 0) {
                        stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                    } else {
                        stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + dc.getDataValue() + ")";
                    }
                }
                stt = "(" + stt + ")";
                whereClause += " AND " + stt;
            }
            listData = PstPinjaman.listProduksi(start, amount, whereClause, order);
        } else if (datafor.equals("listPemesanan")) {
            if (this.oidCbm != 0) {
                whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + "=" + this.oidCbm
                        + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
                        + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            }
            listData = PstBillDetail.listPemesanan(start, amount, whereClause, order);
        } else if (datafor.equals("listPetugas")) {
            System.out.println(whereClause);
            String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
            String param = "limitStart=" + WebServices.encodeUrl("" + start) + "&recordToGet=" + WebServices.encodeUrl("" + amount)
                    + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl("" + order);
            JSONObject jo = WebServices.getAPIWithParam("", hrApiUrl + "/employee/employee-list", param);
            try {
                jArr = jo.getJSONArray("DATA");
                if (jArr.length() > 0) {
                    for (int x = 0; x < jArr.length(); x++) {
                        JSONArray ja = new JSONArray();
                        JSONArray jArray = jArr.getJSONArray(x);
                        JSONObject empObj = jArray.getJSONObject(0);
                        JSONObject divObj = jArray.getJSONObject(1);
                        Company comp = new Company();
                        System.out.println("ArrayData Petugas : " + jArr);

                        ja.put("" + (this.start + x + 1) + ".");
                        ja.put("" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_NUM], "-") + "");
                        ja.put("" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME], "-"));
                        ja.put("<div class='text-center'> " + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_ADDRESS], "-") + " </div>");
                        ja.put("" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_PHONE], "-") + "");
                        ja.put("<div class='text-center'>" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_HANDPHONE], "-") + "</div>");
                        String button = "<div class='text-center'>"
                                + "<button type='button' title='Detail' "
                                + "class='btn btn-xs btn-warning select-petugas-btn' "
                                + "data-oid='" + empObj.optLong(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_ID]) + "'"
                                + "data-name='" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME]) + "'"
                                + "data-empNum='" + empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_NUM]) + "'> "
                                + "<i class='fa fa-pencil'></i>"
                                + "</button>";
                        ja.put(button);

                        array.put(ja);

                    }
                }
            } catch (Exception e) {
            }

//			listData = PstEmployee.listPetugas(start, amount, whereClause, order);
        } else if (datafor.equals("listPemesanan")) {
            if (this.oidCbm != 0) {
                whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + "=" + this.oidCbm
                        + " AND MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]
                        + " = BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID];
            }
            listData = PstBillDetail.listPemesanan(start, amount, whereClause, order);
        } else if (datafor.equals("listItemForExchange")) {
            listData = PstMaterial.list(start, amount, whereClause, order);
        }
        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listProduksiAll")) {
                billMain = (BillMain) listData.get(i);
                Pinjaman pinjaman = new Pinjaman();
                JenisKredit typeKredit = new JenisKredit();

                String isCredit = "";
                double jumlahPengajuan = 0;
                String custName = "";
                try {
                    //billMain = PstBillMain.fetchExc(pinjaman.getBillMainId());
                    location = PstLocation.fetchExc(billMain.getLocationId());
                    Pinjaman p = new Pinjaman();
                    Contact ang = new Contact();

                    whereClause = PstPinjaman.fieldNames[PstPinjaman.FLD_CASH_BILL_MAIN_ID] + " = " + billMain.getOID();
                    whereClause = PstPinjaman.fieldNames[PstPinjaman.FLD_STATUS_PINJAMAN] + " = 5";
                    whereClause += " AND " + this.whereSedana;
                    Vector dataPinjaman = PstPinjaman.list(0, 0, whereClause, "");
                    if (dataPinjaman.size() > 0) {
                        p = (Pinjaman) dataPinjaman.get(0);
                    }
                    try {
                        if (billMain.getCustomerId() > 0) {
                            ang = PstContact.fetchExc(billMain.getCustomerId());
                            custName = ang.getPersonName();
                        } else {
                            ang = PstContact.fetchExc(p.getAnggotaId());
                            custName = ang.getPersonName();
                        }
                    } catch (Exception e) {
                    }
                    if (ang.getOID() == 0) {
                        if (!billMain.getGuestName().equals("")) {
                            custName = billMain.getGuestName();
                        } else {
                            custName = "-";
                        }
                    }

                    String url = this.sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                    JSONObject jsonObj = WebServices.getAPI("", url);

                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    try {
                        typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());
                        isCredit = jsonObj.getString("CREDIT");
                    } catch (Exception e) {
                    }

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                if (pinjaman.getJumlahPinjaman() == 0) {
                    Vector<Billdetail> listBillDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID(), "");
                    if (!listBillDetail.isEmpty()) {
                        for (Billdetail bd : listBillDetail) {
                            jumlahPengajuan += bd.getTotalPrice() + bd.getTotalTax();
                        }
                    }
                } else {
                    jumlahPengajuan = pinjaman.getJumlahPinjaman();
                }
                String tipeTransaksi = "";
                if (pinjaman.getNoKredit().equals("") && pinjaman.getOID() == 0) {
                    tipeTransaksi = PstBillMain.payType[0];
                } else {
                    tipeTransaksi = PstBillMain.payType[1];
                }

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + (pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) + "");
                ja.put("" + custName);
                ja.put("<div class='text-center'> " + ((pinjaman.getTglPengajuan() == null) ? Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") : Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyyy")) + " </div>");
                ja.put("" + tipeTransaksi + "");
                ja.put("<div class='text-center'>" + ((typeKredit.getNamaKredit().equals("")) ? "-" : typeKredit.getNamaKredit()) + "</div>");
                //ja.put("" + sumberDana.getKodeSumberDana() + " - " + sumberDana.getJudulSumberDana());
                ja.put("<div class='text-center'>" + location.getName() + "</div>");
                ja.put("<div class='text-center'>" + numberFormat.format(jumlahPengajuan) + "</div>");
                ja.put("<div class='text-center'>" + PstBillMain.produksiDeliveryStatus[billMain.getStatus()] + "</div>");
                String button = "<div class='text-center'>"
                        + "<button type='button' title='Detail' "
                        + "class='btn btn-xs btn-warning detail-produksi-btn' "
                        + "data-oid='" + billMain.getOID() + "'> "
                        + "<i class='fa fa-pencil'></i>"
                        + "</button>";
                if (billMain.getStatus() != PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION) {
                    button += "<button type='button' title='Dokumen' "
                            + "class='btn btn-xs btn-primary dokumen-produksi-btn' "
                            + "data-oid='" + billMain.getOID() + "'> "
                            + "<i class='fa fa-file-text-o'></i>"
                            + "</button> <br> "
                            + "</div>";
                }
                ja.put(button);
                array.put(ja);

            } else if (datafor.equals("listPengirimanAll")) {
                billMain = (BillMain) listData.get(i);
                Pinjaman pinjaman = new Pinjaman();
                JenisKredit typeKredit = new JenisKredit();
                String nameEmp = "";
                String numEmp = "";
                double jumlahPengajuan = 0;
                String custName = "";
                String isCredit = "";
                try {
                    //billMain = PstBillMain.fetchExc(pinjaman.getBillMainId());
                    location = PstLocation.fetchExc(billMain.getLocationId());
                    Pinjaman p = new Pinjaman();
                    Contact ang = new Contact();
                    whereClause = PstPinjaman.fieldNames[PstPinjaman.FLD_CASH_BILL_MAIN_ID] + " = " + billMain.getOID();
                    whereClause += " AND " + this.whereSedana;
                    Vector dataPinjaman = PstPinjaman.list(0, 0, whereClause, "");
                    if (dataPinjaman.size() > 0) {
                        p = (Pinjaman) dataPinjaman.get(0);
                    }
                    try {
                        if (billMain.getCustomerId() > 0) {
                            ang = PstContact.fetchExc(billMain.getCustomerId());
                            custName = ang.getPersonName();
                        } else {
                            ang = PstContact.fetchExc(p.getAnggotaId());
                            custName = ang.getPersonName();
                        }
                    } catch (Exception e) {
                    }
                    if (ang.getOID() == 0) {
                        if (!billMain.getGuestName().equals("")) {
                            custName = billMain.getGuestName();
                        } else {
                            custName = "-";
                        }
                    }

                    String url = this.sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                    JSONObject jsonObj = WebServices.getAPI("", url);

                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    try {
                        typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());
                        isCredit = jsonObj.getString("CREDIT");
                    } catch (Exception e) {
                    }

                    String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
                    if (billMain.getDoPersonId() != 0) {
                        String urlEmp = hrApiUrl + "/employee/employee-fetch/" + billMain.getDoPersonId();
                        JSONObject jo = WebServices.getAPI("", urlEmp);
                        if (jo.length() > 0) {

                            numEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], "-");
                            nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

                        }
                    }

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                if (pinjaman.getJumlahPinjaman() == 0) {
                    Vector<Billdetail> listBillDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID(), "");
                    if (!listBillDetail.isEmpty()) {
                        for (Billdetail bd : listBillDetail) {
                            jumlahPengajuan += bd.getTotalPrice() + bd.getTotalTax();
                        }
                    }
                } else {
                    jumlahPengajuan = pinjaman.getJumlahPinjaman();
                }
                String tipeTransaksi = "";
                if (pinjaman.getNoKredit().equals("") && pinjaman.getOID() == 0) {
                    tipeTransaksi = PstBillMain.payType[0];
                } else {
                    tipeTransaksi = PstBillMain.payType[1];
                }

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + (pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) + "");
                ja.put("" + custName);
                ja.put("<div class='text-center'> " + ((pinjaman.getTglPengajuan() == null) ? Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") : Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyyy")) + " </div>");
                ja.put("" + tipeTransaksi + "");
                ja.put("<div class='text-center'>" + ((typeKredit.getNamaKredit().equals("")) ? "-" : typeKredit.getNamaKredit()) + "</div>");
                //ja.put("" + sumberDana.getKodeSumberDana() + " - " + sumberDana.getJudulSumberDana());
                ja.put("<div class='text-center'>" + location.getName() + "</div>");
                ja.put("<div class='text-center'>" + numberFormat.format(jumlahPengajuan) + "</div>");
                ja.put("<div class='text-center'>" + Formater.formatDate(billMain.getShippingDate()) + "</div>");
                ja.put("<div class='text-center'>" + nameEmp + "</div>");
                ja.put("<div class='text-center'>" + PstBillMain.produksiDeliveryStatus[billMain.getStatus()] + "</div>");
                String button = "<div class='text-center'>"
                        + "<input type='checkbox' title='send' "
                        + "class='checkbox detail-produksi-btn' "
                        + "id='choose' name=" + FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID] + " "
                        + "data-oid=" + billMain.getOID() + " value=" + billMain.getOID() + "></div>";

                ja.put(button);
                array.put(ja);
            } else if (datafor.equals("listStatusPengiriman")) {
                billMain = (BillMain) listData.get(i);
                Pinjaman pinjaman = new Pinjaman();
                JenisKredit typeKredit = new JenisKredit();
                String nameEmp = "";
                String numEmp = "";
                double jumlahPengajuan = 0;
                String custName = "";
                String isCredit = "";
                try {
                    //billMain = PstBillMain.fetchExc(pinjaman.getBillMainId());
                    location = PstLocation.fetchExc(billMain.getLocationId());
                    Pinjaman p = new Pinjaman();
                    Contact ang = new Contact();

                    whereClause = PstPinjaman.fieldNames[PstPinjaman.FLD_CASH_BILL_MAIN_ID] + " = " + billMain.getOID();
                    whereClause += " AND " + this.whereSedana;
                    Vector dataPinjaman = PstPinjaman.list(0, 0, whereClause, "");
                    if (dataPinjaman.size() > 0) {
                        p = (Pinjaman) dataPinjaman.get(0);
                    }
                    try {
                        if (billMain.getCustomerId() > 0) {
                            ang = PstContact.fetchExc(billMain.getCustomerId());
                            custName = ang.getPersonName();
                        } else {
                            ang = PstContact.fetchExc(p.getAnggotaId());
                            custName = ang.getPersonName();
                        }
                    } catch (Exception e) {
                    }
                    if (ang.getOID() == 0) {
                        if (!billMain.getGuestName().equals("")) {
                            custName = billMain.getGuestName();
                        } else {
                            custName = "-";
                        }
                    }

                    String url = this.sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                    JSONObject jsonObj = WebServices.getAPI("", url);

                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    
                    try {
                        typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());
                        isCredit = jsonObj.getString("CREDIT");
                    } catch (Exception e) {
                    }

                    String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
                    if (billMain.getDoPersonId() != 0) {
                        String urlEmp = hrApiUrl + "/employee/employee-fetch/" + billMain.getDoPersonId();
                        JSONObject jo = WebServices.getAPI("", urlEmp);
                        if (jo.length() > 0) {

                            numEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], "-");
                            nameEmp = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");

                        }
                    }

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                if (pinjaman.getJumlahPinjaman() == 0) {
                    Vector<Billdetail> listBillDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID(), "");
                    if (!listBillDetail.isEmpty()) {
                        for (Billdetail bd : listBillDetail) {
                            jumlahPengajuan += bd.getTotalPrice() + bd.getTotalTax();
                        }
                    }
                } else {
                    jumlahPengajuan = pinjaman.getJumlahPinjaman();
                }
                String tipeTransaksi = "";
                if (pinjaman.getNoKredit().equals("") && pinjaman.getOID() == 0) {
                    tipeTransaksi = PstBillMain.payType[0];
                } else {
                    tipeTransaksi = PstBillMain.payType[1];
                }

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + (pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) + "");
                ja.put("" + custName);
                ja.put("<div class='text-center'> " + ((pinjaman.getTglPengajuan() == null) ? Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") : Formater.formatDate(pinjaman.getTglPengajuan(), "dd-MM-yyyy")) + " </div>");
                ja.put("" + tipeTransaksi + "");
                ja.put("<div class='text-center'>" + ((typeKredit.getNamaKredit().equals("")) ? "-" : typeKredit.getNamaKredit()) + "</div>");
                //ja.put("" + sumberDana.getKodeSumberDana() + " - " + sumberDana.getJudulSumberDana());
                ja.put("<div class='text-center'>" + location.getName() + "</div>");
                ja.put("<div class='text-center'>" + numberFormat.format(jumlahPengajuan) + "</div>");
                ja.put("<div class='text-center'>" + (billMain.getShippingDate() != null ? Formater.formatDate(billMain.getShippingDate()) : "-") + "</div>");
                ja.put("<div class='text-center'>" + nameEmp + "</div>");
                ja.put("<div class='text-center'>" + PstBillMain.produksiDeliveryStatus[billMain.getStatus()] + "</div>");
                String button = "";
                if (billMain.getStatus() != PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA) {
                    button += "<div class='text-center'>"
                            + "<button type='button' title='send' "
                            + "class='btn btn-xs btn-success btn-send-barang' "
                            + "data-empoid='" + billMain.getDoPersonId() + "'"
                            + "data-sendnote='" + billMain.getNotes() + "'"
                            + "data-cbmoid='" + billMain.getOID() + "'"
                            + "> "
                            + "<i class='fa fa-file'></i> Status"
                            + "</button>";
                } else if (billMain.getStatus() == PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA) {
                    button += "<button type='button' title='Dokumen' "
                            + "class='btn btn-xs btn-primary btn-dokumen' "
                            + "data-bmoid='" + billMain.getOID() + "'> "
                            + "<i class='fa fa-file'></i> Dokumen"
                            + "</button> <br> "
                            + "</div>";
                }
                ja.put(button);
                array.put(ja);
            } else if (datafor.equals("listDokumenPengiriman")) {
                billMain = (BillMain) listData.get(i);
                Pinjaman pinjaman = new Pinjaman();
                JenisKredit typeKredit = new JenisKredit();
                try {
                    String url = this.sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                    JSONObject jsonObj = WebServices.getAPI("", url);
                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());
                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                boolean kredit = false;
                try {
                    String url = this.apiUrl + "/support/info/data-kredit/" + billMain.getOID();
                    JSONObject res = WebServices.getAPI("", url);
                    kredit = res.getBoolean("SUCCES");
                } catch (Exception e) {
                    kredit = false;
                }

                String petugas = "";
                String dateDelivery = "-";
                String urll = hrApiUrl + "/employee/employee-fetch/" + billMain.getDoPersonId();
                JSONObject obj = WebServices.getAPI("", urll);
                if (obj.length() > 0) {
                    petugas = obj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
                } else {
                    petugas = "Petugas Delivery perlu di set";
                }

                ja.put("<div class='text-center'> " + (this.start + i + 1) + ". </div>");
                ja.put("<div class='text-center'> " + (pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) + " </div>");
                ja.put("<div class='text-center'> " + petugas + " </div>");
                ja.put("<div class='text-center'> " + (billMain.getShippingDate() == null ? dateDelivery : Formater.formatDate(billMain.getShippingDate(), "yyyy-MM-dd")) + " </div>");
                String button = "<button type='button' title='Print All Dokumen' "
                        + "class='btn btn-primary dokumen-produksi-btn' "
                        + "data-oid='" + billMain.getOID() + "'> "
                        + "<i class='fa fa-file-pdf-o'></i>"
                        + "</button> ";
                ja.put("<div class='text-center'>" + button + "</div>");
                array.put(ja);

            } else if (datafor.equals("listBarangProduksi")) {
                Vector data = (Vector) listData.get(i);
                billMain = (BillMain) data.get(0);
                Billdetail bd = (Billdetail) data.get(1);
                Pinjaman pinjaman = new Pinjaman();
                JenisKredit typeKredit = new JenisKredit();
                String whereMapping = PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + "=" + bd.getMaterialId()
                        + " AND " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID] + "<> 0";
                Vector listGroupMapping = PstMaterialMappingType.list(0, 0, whereMapping, "");
                try {
                    anggota = PstContact.fetchExc(billMain.getCustomerId());
                    location = PstLocation.fetchExc(billMain.getLocationId());

                    String url = this.sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                    JSONObject jsonObj = WebServices.getAPI("", url);
                    PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);
                    typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());

                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                String petugas = "";
                String urll = hrApiUrl + "/employee/employee-fetch/" + billMain.getDoPersonId();
                JSONObject obj = WebServices.getAPI("", urll);
                if (obj.length() > 0) {
                    petugas = obj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
                } else {
                    petugas = "Petugas Delivery perlu di set";
                }
                String typeMaster = "";
                MasterType masterType = new MasterType();
                if (!listGroupMapping.isEmpty()) {
                    for (int x = 0; x < listGroupMapping.size(); x++) {
                        MaterialTypeMapping materialTypeMapping = (MaterialTypeMapping) listGroupMapping.get(x);
                        long oidType = materialTypeMapping.getTypeId();
                        String whereMas = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "= 2"
                                + " AND " + PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + "=" + oidType;
                        Vector listMasType = PstMasterType.list(0, 0, whereMas, "");
                        for (int xx = 0; xx < listMasType.size(); xx++) {
                            masterType = (MasterType) listMasType.get(xx);
                            if (typeMaster.length() != 0) {
                                typeMaster += ", " + masterType.getMasterName();
                            } else {
                                typeMaster = masterType.getMasterName();
                            }
                        }
                    }
                } else {
                    typeMaster = "-";
                }
                ja.put("<div class='text-center'>" + (this.start + i + 1) + ". </div>");
                ja.put("" + (pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) + "");
                ja.put("<div class='text-center'>" + bd.getSku() + "</div>");
                ja.put("" + bd.getItemName());
                ja.put("<div class='text-center'>" + typeMaster + "</div>");
                ja.put("<div class='text-center'>" + bd.getQty() + "</div>");
                ja.put("<div class='text-center'>" + petugas + "</div>");
                array.put(ja);
            } //            else if (datafor.equals("listPetugas")) {
            //				Employee emp = (Employee) listData.get(i);
            //				Position pos = new Position();
            //				try {
            //					comp = PstCompany.fetchExc(emp.getCompanyId());
            //					pos = PstPosition.fetchExc(emp.getPositionId());
            //				} catch (Exception e) {
            //					printErrorMessage(e.getMessage());
            //					message = e.toString();
            //				}
            //				ja.put("" + (this.start + i + 1) + ".");
            //				ja.put("" + emp.getEmployeeNum() + "");
            //				ja.put("" + emp.getFullName());
            //				ja.put("<div class='text-center'> " + comp.getCompanyName() + " </div>");
            //				ja.put("" + emp.getAddress() + "");
            //				ja.put("<div class='text-center'>" + emp.getPhone() + "</div>");
            //				ja.put("<div class='text-center'>" + emp.getHandphone() + "</div>");
            //				String button = "<div class='text-center'>"
            //						+ "<button type='button' title='Detail' "
            //						+ "class='btn btn-xs btn-warning select-petugas-btn' "
            //						+ "data-oid='" + emp.getOID() + "'"
            //						+ "data-name='" + emp.getFullName() + "'"
            //						+ "data-empNum='" + emp.getEmployeeNum() + "'> "
            //						+ "<i class='fa fa-pencil'></i>"
            //						+ "</button>";
            //				ja.put(button);
            //				array.put(ja);
            //            } 
            else if (datafor.equals("listPemesanan")) {
                Vector tempList = (Vector) listData.get(i);
                BillMain bm = (BillMain) tempList.get(0);
                Material material = (Material) tempList.get(1);
                Category cat = (Category) tempList.get(2);
                Unit unit = (Unit) tempList.get(3);
                Merk mk = (Merk) tempList.get(4);
                MaterialStock ms = (MaterialStock) tempList.get(5);
                Billdetail bd = (Billdetail) tempList.get(6);

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + bm.getInvoiceNo() + "");
                ja.put("" + material.getSku());
                ja.put("<div class='text-center'> " + ((material.getBarcode() == null) ? "-" : material.getBarcode()) + " </div>");
                ja.put("" + material.getName() + "");
                ja.put("<div class='text-center'>" + cat.getName() + "</div>");
                ja.put("<div class='text-center'>" + unit.getName() + "</div>");
                ja.put("<div class='text-center'>" + mk.getName() + "</div>");
                ja.put("<div class='text-center'>" + bd.getQty() + "</div>");
                ja.put("<div class='text-center'>" + ms.getQty() + "</div>");
//				String button = "<div class='text-center'>"
//						+ "<button type='button' title='Buat Store Request' "
//						+ "data-billnumber='" + bm.getInvoiceNo() + "' "
//						+ "data-matid='" + material.getOID() + "' "
//						+ "data-unitid='" + unit.getOID() + "' "
//						+ "data-sku='" + material.getSku() + "' "
//						+ "data-namabarang='" + material.getName() + "' "
//						+ "data-unit='" + unit.getName() + "' "
//						+ "value='" + bm.getOID() + "'"
//						+ "class='create-new-sr btn btn-xs btn-warning'> "
//						+ "SR"
//						+ "</button>"
//						+ "<span>&nbsp;</span>"
//						+ "<button type='button' title='Buat Order Request' "
//						+ "class='btn btn-xs btn-success'> "
//						+ "OR"
//						+ "</button>"
//						+ "</div>";
                String button = "<div class='form-group'>"
                        + "    <div class='form-check'>"
                        + "     <input class='form-check-input' type='checkbox' id='gridCheck'"
                        + "	name='" + FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID] + "'"
                        + "	value='" + bd.getOID() + "'>"
                        + "    </div>"
                        + "  </div>";
                ja.put(button);
                array.put(ja);
            } else if (datafor.equals("listItemForExchange")) {
                Material material = (Material) listData.get(i);
                Category cat = new Category();
                try {
                    cat = PstCategory.fetchExc(material.getCategoryId());
                } catch (Exception e) {
                }

                double price = 0;
                int roundValue = 1000;
                String formulaCash = PstSystemProperty.getValueByName("CASH_FORMULA");

                if (checkString(formulaCash, "HPP") > -1) {
                    formulaCash = formulaCash.replaceAll("HPP", "" + material.getAveragePrice());
                }
//                if (checkString(formulaCash, "INCREASE") > -1) {
//                    formulaCash = formulaCash.replaceAll("INCREASE", "" + cat.getKenaikanHarga());
//                }
                price += getValue(formulaCash);
                price = (Math.floor((price + (roundValue - 1)) / roundValue)) * roundValue;

                ja.put("" + (this.start + i + 1) + ".");
                ja.put("" + material.getSku());
                ja.put("<div class='text-center'> " + ((material.getBarcode() == null) ? "-" : material.getBarcode()) + " </div>");
                ja.put("" + material.getName() + "");
                ja.put("" + Formater.formatNumber(price, "###,###,###") + "");
                String button = "<div class='text-center'>"
                        + "<button class='btn btn-primary getItemExchange' type='button' "
                        + "data-oidmat='" + material.getOID() + "' "
                        + "data-price='" + price + "' > Pilih"
                        + "</button>"
                        + "  </div>";
                ja.put(button);
                array.put(ja);
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













