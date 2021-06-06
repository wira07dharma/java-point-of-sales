package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.posbo.entity.search.SrcReportSale;
import com.dimata.posbo.form.search.FrmSrcReportSale;
import com.dimata.posbo.session.report.SessSalesTrend;
import com.dimata.posbo.session.warehouse.SessReportSale;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxSaleGraphic extends HttpServlet {

    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    ArrayList<String> listCategories = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        String chartType = FRMQueryString.requestString(request, "FRM_FIELD_CHART_TYPE");
        String dataChart = FRMQueryString.requestString(request, "FRM_FIELD_CHART_DATA_FOR");
        String returnHTML = "";
        String returnChart = "";
        String whereClause = "";
        String message = "";
        String start = "";
        String end = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        int error = 0;

        //JSONOBJECT
        JSONObject colors = new JSONObject();
        JSONArray colorArrays = new JSONArray();
        try {
            colors.put("orange", "#ED7D31");
            colors.put("blue", "#5B9BD5");
            colors.put("yellow", "#FFC000");
            colors.put("gray", "#A5A5A5");
            colors.put("green", "#70AD47");
            colors.put("blackGray", "#7F7F7F");
            colorArrays.put("#ED7D31");
            colorArrays.put("#5B9BD5");
            colorArrays.put("#FFC000");
            colorArrays.put("#A5A5A5");
            colorArrays.put("#70AD47");
            colorArrays.put("#7F7F7F");
        } catch (Exception ex) {
        }

        JSONArray seriesDatas = new JSONArray();
        JSONObject seriesData = new JSONObject();
        JSONObject chart = new JSONObject();
        JSONObject chartData = new JSONObject();
        JSONArray chartDatas = new JSONArray();
        JSONArray categories = new JSONArray();
        JSONObject categoriesTitle = new JSONObject();
        JSONObject chartOptions = new JSONObject();
        JSONObject titleChart = new JSONObject();
        JSONObject subtitleChart = new JSONObject();
        JSONObject xAxisChart = new JSONObject();
        JSONObject yAxisChart = new JSONObject();
        JSONObject tooltipChart = new JSONObject();
        JSONObject plotOptions = new JSONObject();

        //ARRAYLIST DATA
        ArrayList<Double> datas = new ArrayList<Double>();

        if (iCommand == Command.NONE) {
            if (dataFor.equals("dataSaleChart")) {
                try {
                    //
                    SrcReportSale srcReportSale = new SrcReportSale();
                    SessReportSale sessReportSale = new SessReportSale();
                    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);

                    frmSrcReportSale.requestEntityObject(srcReportSale);
                    String categoryMultiple[] = request.getParameterValues("" + FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID] + "");
                    String locationMultiple[] = request.getParameterValues("" + FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID] + "");
                    String categoryInText = "";
                    String locationInText = "";
                    try {
                        for (int a = 0; a < categoryMultiple.length; a++) {
                            if (categoryInText.length() > 0) {
                                categoryInText = categoryInText + "," + categoryMultiple[a];
                            } else {
                                categoryInText = categoryMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }

                    try {
                        for (int a = 0; a < locationMultiple.length; a++) {
                            if (locationInText.length() > 0) {
                                locationInText = locationInText + "," + locationMultiple[a];
                            } else {
                                locationInText = locationMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }

                    String dateFrom = FRMQueryString.requestString(request, "" + FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM] + "");
                    String dateTo = FRMQueryString.requestString(request, "" + FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO] + "");

                    Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
                    Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");

                    srcReportSale.setDateFrom(tglmulai);
                    srcReportSale.setDateTo(tglberakhir);

                    srcReportSale.setCategoryMultiple(categoryInText);
                    srcReportSale.setLocationMultiple(locationInText);
                    Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);

                    String categoryName[] = {"Total Jual", "Total HPP", "Total Profit", "Total Tax", "Total Service", "Rata-Rata Harian"};

                    //CATCH 
                    int language = FRMQueryString.requestInt(request, "language");

                    //MENAMBAHKAN TANGGAL
                    listCategories.clear();
                    for (int k = 0; k < records.size(); k++) {
                        Vector rowx = new Vector();
                        Vector vt = (Vector) records.get(k);
                        String tanggal = (String) vt.get(0);
                        listCategories.add(tanggal);
                    }

                    //GENERATE CHART
                    generateLineChartMonth(chartType, dataChart, "", "", "", chart, listCategories, "");

                    //DATA OBJECT
                    JSONArray pencapaianObject = new JSONArray();
                    JSONArray targetObject = new JSONArray();
                    JSONArray selisihObject = new JSONArray();
                    ArrayList<Object> dataObjectPencapaians = new ArrayList<Object>();
                    ArrayList<Object> dataObjectTargets = new ArrayList<Object>();

                    double totalRata = 0;
                    double rata = 0;

                    Vector rataVector = new Vector(1, 1);

                    if (records.size() != 0) {
                        for (int i = 0; i < categoryName.length; i++) {
                            int select = i + 1;
                            try {
                                seriesData = new JSONObject();
                                pencapaianObject = new JSONArray();
                                selisihObject = new JSONArray();
                                seriesData.put("name", "" + categoryName[i]);
                                seriesData.put("color", "" + colorArrays.get(i));

                                for (int j = 0; j < records.size(); j++) {
                                    double totalValue = 0;
                                    if (i != 5) {
                                        Vector rowx = new Vector();
                                        Vector vt = (Vector) records.get(j);
                                        totalValue = (Double) vt.get(select);
                                    }

                                    if (i == 0) {
                                        totalRata = totalRata + totalValue;
                                        rata = totalRata / (j + 1);
                                        rataVector.add("" + rata);
                                    }
                                    if (i == 5) {
                                        Double temp = Double.parseDouble((String) rataVector.get(j));
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(temp, "#.##")));
                                    } else {
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(totalValue, "#.##")));
                                    }

                                }

                                seriesData.put("data", pencapaianObject);
                                seriesDatas.put(seriesData);

                            } catch (Exception ex) {

                            }
                        }

                    }

                } catch (Exception e) {
                }

            } else if (dataFor.equals("reportTrendSales")) {

                String dateFrom = FRMQueryString.requestString(request, "TGL_AWAL");
                String dateTo = FRMQueryString.requestString(request, "TGL_AKHIR");
                int reportType = FRMQueryString.requestInt(request, "REPORT_TYPE");

                if (dateFrom.length() == 0 || dateTo.length() == 0) {
                    error += 1;
                    message += "Tanggal harus diisi !";
                    return;
                }

                try {
                    Date awal = Formater.formatDate(dateFrom, "yyyy-MM-dd");
                    Date akhir = Formater.formatDate(dateTo, "yyyy-MM-dd");
                    start = Formater.formatDate(awal, "dd MMM yyyy");
                    end = Formater.formatDate(akhir, "dd MMM yyyy");
                } catch (Exception e) {
                    error += 1;
                    message += "Tanggal tidak sesuai !";
                    return;
                }

                String titleChartName = "";
                String subTitleChartName = "";

                //set data jason
                int materialType[] = {Material.MATERIAL_TYPE_EMAS, Material.MATERIAL_TYPE_BERLIAN};

                switch (reportType) {
                    case 1:
                        titleChartName = "Trend By Customer";
                        subTitleChartName = "Grafik penjualan berdasarkan jumlah customer yang membeli item untuk masing-masing berat";

                        listCategories.clear();
                        //get data item weight
                        String whereBerat = ""
                                + " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                + "";
                        Vector listBerat = SessSalesTrend.getListBerat(0, 0, whereBerat, "");
                        if (listBerat.isEmpty()) {
                            error += 1;
                            message += "Data tidak ditemukan";
                        }

                        ArrayList<Double> nilaiBerat = new ArrayList<Double>();
                        for (int j = 0; j < listBerat.size(); j++) {
                            Billdetail billdetail = (Billdetail) listBerat.get(j);
                            nilaiBerat.add(billdetail.getBerat());
                            listCategories.add("" + String.format("%,.3f", billdetail.getBerat()) + " gr");
                        }

                        generateBarChart(chartType, dataChart, titleChartName, subTitleChartName, "", chart, listCategories, "", "<b>Berat Item</b>", "<b>Jumlah Customer</b>");

                        for (int i = 0; i < materialType.length; i++) {
                            try {
                                seriesData = new JSONObject();
                                seriesData.put("name", "" + Material.MATERIAL_TYPE_TITLE[materialType[i]]);
                                seriesData.put("color", "" + colorArrays.get(i));
                                JSONArray jumlahCustomer = new JSONArray();

                                //get data customer by weight & material main
                                for (int j = 0; j < nilaiBerat.size(); j++) {
                                    String where = " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                            + " AND cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_BERAT] + " = '" + nilaiBerat.get(j) + "'"
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + materialType[i] + "'";
                                    int count = SessSalesTrend.getCountCustomer(where);
                                    jumlahCustomer.put(count);
                                }

                                seriesData.put("data", jumlahCustomer);
                                seriesDatas.put(seriesData);
                            } catch (JSONException ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }
                        break;
                    case 2:
                        titleChartName = "Trend By Supplier";
                        subTitleChartName = "Grafik penjualan berdasarkan jumlah item yang terjual dari masing-masing supplier";

                        listCategories.clear();
                        String whereCustomer = ""
                                + " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " != 0"
                                + "";
                        Vector listCustomer = SessSalesTrend.getListCustomer(0, 0, whereCustomer, "");
                        if (listCustomer.isEmpty()) {
                            error += 1;
                            message += "Data tidak ditemukan";
                        }

                        ArrayList<Long> idCustomer = new ArrayList<Long>();
                        for (int j = 0; j < listCustomer.size(); j++) {
                            Material m = (Material) listCustomer.get(j);
                            idCustomer.add(m.getSupplierId());
                            ContactList cl;
                            try {
                                cl = PstContactList.fetchExc(m.getSupplierId());
                                listCategories.add("" + cl.getCompName());
                            } catch (DBException ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }

                        generateBarChart(chartType, dataChart, titleChartName, subTitleChartName, "", chart, listCategories, "", "<b>Supplier</b>", "<b>Jumlah Item Terjual</b>");

                        for (int i = 0; i < materialType.length; i++) {
                            try {
                                seriesData = new JSONObject();
                                seriesData.put("name", "" + Material.MATERIAL_TYPE_TITLE[materialType[i]]);
                                seriesData.put("color", "" + colorArrays.get(i));
                                JSONArray jumlahItem = new JSONArray();

                                for (int j = 0; j < idCustomer.size(); j++) {
                                    String where = " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = '" + idCustomer.get(j) + "'"
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + materialType[i] + "'";
                                    int count = SessSalesTrend.getCountQtyItem(where);
                                    jumlahItem.put(count);
                                }

                                seriesData.put("data", jumlahItem);
                                seriesDatas.put(seriesData);

                            } catch (JSONException e) {
                                error += 1;
                                message += " " + e.getMessage();
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        titleChartName = "Trend By Item Type";
                        subTitleChartName = "Grafik penjualan berdasarkan jumlah item yang terjual untuk masing-masing jenis item";

                        listCategories.clear();
                        String whereCategory = ""
                                + " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " <> 0"
                                + "";
                        Vector listCategory = SessSalesTrend.getListCategory(0, 0, whereCategory, "");
                        if (listCategory.isEmpty()) {
                            error += 1;
                            message += "Data tidak ditemukan";
                        }

                        ArrayList<Long> idCategory = new ArrayList<Long>();
                        for (int j = 0; j < listCategory.size(); j++) {
                            Material m = (Material) listCategory.get(j);
                            idCategory.add(m.getCategoryId());
                            Category c;
                            try {
                                c = PstCategory.fetchExc(m.getCategoryId());
                                listCategories.add("" + c.getName());
                            } catch (DBException ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }

                        generateBarChart(chartType, dataChart, titleChartName, subTitleChartName, "", chart, listCategories, "", "<b>Jenis Item</b>", "<b>Jumlah Item Terjual</b>");

                        for (int i = 0; i < materialType.length; i++) {
                            try {
                                seriesData = new JSONObject();
                                seriesData.put("name", "" + Material.MATERIAL_TYPE_TITLE[materialType[i]]);
                                seriesData.put("color", "" + colorArrays.get(i));
                                JSONArray jumlahItem = new JSONArray();

                                for (int j = 0; j < idCategory.size(); j++) {
                                    String where = " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = '" + idCategory.get(j) + "'"
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + materialType[i] + "'";
                                    int count = SessSalesTrend.getCountQtyItem(where);
                                    jumlahItem.put(count);
                                }

                                seriesData.put("data", jumlahItem);
                                seriesDatas.put(seriesData);

                            } catch (JSONException ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }
                        break;
                    case 4:
                        titleChartName = "Trend By Sales Counter";
                        subTitleChartName = "Grafik penjualan berdasarkan jumlah item yang terjual oleh masing-masing sales counter";

                        listCategories.clear();
                        String whereSales = ""
                                + " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                + "";
                        Vector listSales = SessSalesTrend.getListSales(0, 0, whereSales, "");
                        if (listSales.isEmpty()) {
                            error += 1;
                            message += "Data tidak ditemukan";
                        }

                        ArrayList<String> kodeSales = new ArrayList<String>();
                        for (int j = 0; j < listSales.size(); j++) {
                            BillMain bm = (BillMain) listSales.get(j);
                            AppUser ap = new AppUser();
                            try {
                                ap = PstAppUser.fetch(bm.getAppUserSalesId());
                              } catch (Exception e) {
                              }
                            kodeSales.add(ap.getFullName());
                            AppUser s;
                            try {
                                Vector<AppUser> sales = PstAppUser.listFullObj(0, 0, PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + bm.getAppUserSalesId(), "");
//                                Vector<Sales> sales = PstSales.list(0, 0, PstSales.fieldNames[PstSales.FLD_CODE] + " = " + bm.getSalesCode(), "");
                                if (sales.size() > 0) {
                                    s = PstAppUser.fetch(sales.get(0).getOID());
                                    listCategories.add("" + s.getFullName());
                                }
                            } catch (Exception ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }

                        generateBarChart(chartType, dataChart, titleChartName, subTitleChartName, "", chart, listCategories, "", "<b>Supplier</b>", "<b>Jumlah Item Terjual</b>");

                        for (int i = 0; i < materialType.length; i++) {
                            try {
                                seriesData = new JSONObject();
                                seriesData.put("name", "" + Material.MATERIAL_TYPE_TITLE[materialType[i]]);
                                seriesData.put("color", "" + colorArrays.get(i));
                                JSONArray totalSale = new JSONArray();

                                for (int j = 0; j < kodeSales.size(); j++) {
                                    String where = " DATE(cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + dateFrom + "' AND '" + dateTo + "'"
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " = '" + kodeSales.get(j) + "'"
                                            + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + materialType[i] + "'"
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                                            + " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
//                                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH
//                                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT + ")"
//                                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
//                                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_OPEN + ")"
                                            + "";
                                    double count = SessSalesTrend.getTotalSale(where);
                                    totalSale.put(count);
                                }

                                seriesData.put("data", totalSale);
                                seriesDatas.put(seriesData);

                            } catch (JSONException ex) {
                                error += 1;
                                message += " " + ex.getMessage();
                                System.out.println(ex.getMessage());
                            }
                        }
                        break;
                    default:
                        break;
                }

            }

            try {

                chart.put("series", seriesDatas);
            } catch (Exception ex) {

            }

            JSONObject jSONObject = new JSONObject();

            try {
                jSONObject.put("CHART_DATA", chart);
                jSONObject.put("HTML_DATA", returnHTML);
                jSONObject.put("ERROR", error);
                jSONObject.put("MESSAGE", message);
                jSONObject.put("START", start);
                jSONObject.put("END", end);
            } catch (JSONException ex) {
                returnChart = "{'CHART_DATA':'" + ex.toString() + "'}";
            }

            response.getWriter().println(jSONObject);
        }

    }

    public void generateLineChartMonth(String chartType, String dataChart,
            String titleChartName, String subtitleChartName,
            String categoriesTitleName, JSONObject chart,
            ArrayList<String> listCategories, String toolTips) {

        //SET CHART OPTIONS
        JSONArray categories = new JSONArray();

        JSONObject categoriesTitle = new JSONObject();

        JSONObject chartOptions = new JSONObject();
        JSONObject titleChart = new JSONObject();
        JSONObject subtitleChart = new JSONObject();
        JSONObject xAxisChart = new JSONObject();
        JSONObject yAxisChart = new JSONObject();
        JSONObject tooltipChart = new JSONObject();
        JSONObject plotOptions = new JSONObject();
        JSONObject rangeOptions = new JSONObject();
        JSONObject month = new JSONObject();
        JSONObject label = new JSONObject();

        try {
            chartOptions.put("type", chartType);
            chartOptions.put("renderTo", dataChart);
            titleChart.put("text", titleChartName);

            subtitleChart.put("text", "");
            categoriesTitle.put("text", "");
            xAxisChart.put("categories", listCategories);

            chart.put("chart", chartOptions);
            chart.put("title", titleChart);
            chart.put("subtitle", subtitleChart);
            chart.put("xAxis", xAxisChart);
            chart.put("plotOptions", plotOptions);
        } catch (Exception ex) {

        }
    }

    //GENERATE BAR & COLUMN CHART
    public void generateBarChart(String chartType, String dataChart, String titleChartName,
            String subtitleChartName, String categoriesTitleName, JSONObject chart, ArrayList<String> listCategories,
            String toolTips, String xTitle, String yTitle) {

        //SET CHART OPTIONS
        JSONArray categories = new JSONArray();
        JSONObject categoriesTitleX = new JSONObject();
        JSONObject categoriesTitleY = new JSONObject();
        JSONObject chartOptions = new JSONObject();
        JSONObject titleChart = new JSONObject();
        JSONObject subtitleChart = new JSONObject();
        JSONObject xAxisChart = new JSONObject();
        JSONObject yAxisChart = new JSONObject();
        JSONObject tooltipChart = new JSONObject();
        JSONObject plotOptions = new JSONObject();

        try {
            chartOptions.put("type", chartType);
            chartOptions.put("renderTo", dataChart);

            titleChart.put("text", titleChartName);
            subtitleChart.put("text", subtitleChartName);

            categoriesTitleX.put("text", xTitle);
            categoriesTitleY.put("text", yTitle);

            xAxisChart.put("categories", listCategories);
            xAxisChart.put("title", categoriesTitleX);
            xAxisChart.put("crosshair", true);

            yAxisChart.put("min", 0);
            yAxisChart.put("title", categoriesTitleY);

            tooltipChart.put("valueSuffix", " " + toolTips);
            tooltipChart.put("shared", true);
            tooltipChart.put("useHTML", true);

            chart.put("chart", chartOptions);
            chart.put("title", titleChart);
            chart.put("subtitle", subtitleChart);
            chart.put("xAxis", xAxisChart);
            chart.put("yAxis", yAxisChart);
            chart.put("tooltip", tooltipChart);
        } catch (Exception ex) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
