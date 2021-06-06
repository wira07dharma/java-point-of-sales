<%-- 
    Document   : export_excel_barang
    Created on : Mar 27, 2020, 1:14:55 PM
    Author     : WiraDharma
--%>
<%@page import="com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.sedana.entity.kredit.TypeKredit"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialTypeMapping"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.sedana.entity.kredit.PstTypeKredit"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialMappingType"%>
<%@page import="com.dimata.sedana.entity.kredit.Pinjaman"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.admin.AppUser"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.sedana.entity.kredit.PstPinjaman"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<%
    BillMain billMain = new BillMain();
    DataCustom dc = new DataCustom();
    Location location = new Location();
    JenisKredit typeKredit = new JenisKredit();
    Vector user = new Vector();
    Vector listData = new Vector();
    String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
    String sedanaAppUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
    String start_date = FRMQueryString.requestString(request, "start_date");
    String end_date = FRMQueryString.requestString(request, "end_date");
    long userId = FRMQueryString.requestLong(request, "userId");

    AppUser au = new AppUser();
    try {
        au = PstAppUser.fetch(userId);
    } catch (Exception e) {
    }
    String where = PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = 'user_create_document_location' " + " AND " + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + " = " + userId;
    user = PstDataCustom.list(0, 0, where, "");
    for (int i = 0; i < user.size(); i++) {
        dc = (DataCustom) user.get(i);
    }

    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "Report Item Production.xls");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Excel</title>
        <style>
            .text-center{
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="box-body">
            <div class="row">
                <div class="col-md-12">
                    <h3 class="text-center">LAPORAN BARANG PRODUKSI</h3>
                    <label class="col-md-2">Tanggal</label>
                    <label>: <%=start_date%></label>
                    <label> s/d </label>
                    <label><%=end_date%></label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div id="listBarang">
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important" border="1">
                            <thead>
                                <tr>
                                    <th>NO</th>
                                    <th>NO CREDIT/INVOICE</th>
                                    <th>SKU</th>
                                    <th>NAMA BARANG</th>
                                    <th>TIPE</th>
                                    <th>QTY</th>
                                    <th>PETUGAS DELIVERY</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    String whereClause = "";
                                    whereClause += " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + "<>" + PstBillMain.PETUGAS_DELIVERY_STATUS_DRAFT;
                                    String stt = "";
                                    if (dc.getOwnerId() != 0) {
                                        for (int i = 0; i < user.size(); i++) {
                                            dc = (DataCustom) user.get(i);
                                            if (stt.length() != 0) {
                                                stt = stt + " OR " + "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " =" + dc.getDataValue() + ")";
                                            } else {
                                                stt = "( LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + dc.getDataValue() + ")";
                                            }
                                        }
                                        stt = "(" + stt + ")";
                                        whereClause += " AND " + stt;
                                    }
                                    if ((start_date != null && start_date.length() > 0) && (end_date != null && end_date.length() > 0)) {
                                        whereClause += " AND ("
                                                + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                + ">= TO_DAYS('" + start_date + "')) AND "
                                                + "(TO_DAYS( CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                + "<= TO_DAYS('" + end_date + "'))"
                                                + ")";
                                    }
                                    String order = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
                                    listData = PstPinjaman.listBarangProduksi(0, 0, whereClause, order);
                                    if(!listData.isEmpty()){
                                    for (int i = 0; i <= listData.size() - 1; i++) {
                                        Vector data = (Vector) listData.get(i);
                                        billMain = (BillMain) data.get(0);
                                        Billdetail bd = (Billdetail) data.get(1);
                                        Pinjaman pinjaman = new Pinjaman();
                                        String whereMapping = PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + "=" + bd.getMaterialId()
                                                + " AND " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_TYPE_ID] + "<> 0";
                                        Vector listGroupMapping = PstMaterialMappingType.list(0, 0, whereMapping, "");
                                        try {
                                            location = PstLocation.fetchExc(billMain.getLocationId());

                                            String url = sedanaAppUrl + "/kredit/pengajuan/status-by-bill/" + billMain.getOID();
                                            JSONObject jsonObj = WebServices.getAPI("", url);

                                            PstPinjaman.convertPinjamanFromJson(jsonObj, pinjaman);

                                            typeKredit = PstTypeKredit.fetchExc(pinjaman.getTipeKreditId());

                                        } catch (Exception e) {
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
                                %>
                                <tr>
                                    <td class="text-center"><%=i + 1%></td>
                                    <td class="text-center"><%=(pinjaman.getNoKredit().equals("") ? billMain.getInvoiceNumber() : pinjaman.getNoKredit()) %></td>
                                    <td class="text-center"><%=bd.getSku() %></td>
                                    <td class="text-center"><%=bd.getItemName() %></td>
                                    <td class="text-center"><%=typeMaster%></td>
                                    <td class="text-center"><%=bd.getQty() %></td>
                                    <td class="text-center"><%=petugas %></td>
                                </tr>
                                <% }
                                    }else {
                                %>
                                <tr> 
                                    <td colspan="7"> Data Tidak Ditemukan</td>
                                </tr>
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

