<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatCostingItem,
                 com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.warehouse.MatDispatchItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.warehouse.PstMatDispatchItem,
                 com.dimata.posbo.form.warehouse.FrmMatDispatchItem,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportStock"%>
<%@page contentType="text/html"%>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
    {"Pencarian Barang","Tidak ada data barang"},
    {"Goods Searching","No goods data available"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
    {"Kategori","Sku","Nama Barang"},
    {"Category","Material Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
    {"No","Sku","Nama Barang","Unit","Qty Stok Asal","Qty Stok Terima"},
    {"No","Code","Item Name","Unit","Stock Qty Asal","Qty Stok Terima"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
    long oidDispatch = 0;
    if(vectDf!=null && vectDf.size()>0){
        for(int k=0;k<vectDf.size();k++){
            MatDispatchItem matDf = (MatDispatchItem)vectDf.get(k);
            if(matDf.getMaterialId()==oidmaterial){
                oidDispatch = matDf.getOID();
                break;
            }
        }
    }
    return oidDispatch;
}
%>

<%!
    public String drawListMaterial(int language, Vector objectClass, Vector vectDf, int start, int tranUsedPriceHpp, long locationAsalId, long locationPenerimaId, Date tranferDate, int typeOfBusinessDetail, int allowZero) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {

            String locAsal = "";
            String locTerima = "";
            if (locationAsalId != 0) {
                try {
                    Location location = PstLocation.fetchExc(locationAsalId);
                    locAsal = location.getName();
                    location = PstLocation.fetchExc(locationPenerimaId);
                } catch (Exception ex) {
                }
            }

            if (locationPenerimaId != 0) {
                try {
                    Location location = PstLocation.fetchExc(locationPenerimaId);
                    locTerima = location.getName();
                } catch (Exception ex) {
                }
            }

            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "5%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "35%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "5%");
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Berat Stok Asal " + locAsal, "15%");
                ctrlist.addHeader("Berat Stok Terima " + locTerima, "15%");
            }
            ctrlist.addHeader(textListMaterialHeader[language][4] + " " + locAsal, "15%");
            ctrlist.addHeader(textListMaterialHeader[language][5] + " " + locTerima, "15%");

            ctrlist.setLinkRow(2);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector list = (Vector) objectClass.get(i);
                Material material = (Material) list.get(0);
                MaterialStock materialStock = (MaterialStock) list.get(1);
                Unit unit = (Unit) list.get(2);
                //added by dewok 2018 for jewelry
                MaterialDetail detail = new MaterialDetail();
                if (typeOfBusinessDetail == 2) {
                    material.setName(SessMaterial.setItemNameForLitama(material.getOID()));
                    long oidMatDetail = PstMaterialDetail.checkOIDMaterialDetailId(material.getOID());
                    try {
                        detail = PstMaterialDetail.fetchExc(oidMatDetail);
                    } catch (Exception e) {

                    }
                }
                
                Vector rowx = new Vector();

                /*Unit unit = new Unit();
                 try{
                 unit = PstUnit.fetchExc(material.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
                 }catch(Exception e){}*/
                double stokSisaAsal = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationAsalId, tranferDate, 0);
                double stokSisaTerima = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationPenerimaId, tranferDate, 0);
                //added by dewok 2018
                double beratSisaAsal = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), locationAsalId, tranferDate, 0);
                double beratSisaTerima = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), locationPenerimaId, tranferDate, 0);
                
//                if (allowZero == 0 && stokSisaAsal <= 0) {continue;}
                
                start = start + 1;
                rowx.add("<div align=\"center\">" + start + "</div>");
                rowx.add("<div align=\"center\">" + material.getSku() + "</div>");               
                rowx.add(material.getName());                
                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(beratSisaAsal) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(beratSisaTerima) + "</div>");
                }
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stokSisaAsal) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stokSisaTerima) + "</div>");

                lstData.add(rowx);
                long oid = getOidDfItem(vectDf, material.getOID());

                String name = "";
                name = material.getName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');
                if (typeOfBusinessDetail == 2) {
                    name = SessMaterial.setItemNameForLitama(material.getOID());
                }
                if (tranUsedPriceHpp == 0) {
                    lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + unit.getCode()
                            + "','" + material.getAveragePrice()
                            + "','" + material.getDefaultStockUnitId() + "','" + oid + "','" + materialStock.getQty() + "','" + detail.getBerat()
                            + "','" + material.getGondolaCode() + "','" + material.getAveragePrice() + "','" + detail.getOngkos());
                } else {
                    double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                    lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + unit.getCode()
                            + "','" + FRMHandler.userFormatStringDecimal(sellPrice)
                            + "','" + material.getDefaultStockUnitId() + "','" + oid + "','" + materialStock.getQty() + "','" + detail.getBerat()
                            + "','" + material.getGondolaCode() + "','" + material.getAveragePrice() + "','" + detail.getOngkos());
                }
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][1] + "</div>";
        }
        return result;
    }

    public String drawListMaterialAuto(int language, Vector objectClass, Vector vectDf, int start, int tranUsedPriceHpp, long locationAsalId, long locationPenerimaId, Date tranferDate, int typeOfBusinessDetail, int allowZero) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {

            String locAsal = "";
            String locTerima = "";
            if (locationAsalId != 0) {
                try {
                    Location location = PstLocation.fetchExc(locationAsalId);
                    locAsal = location.getName();
                    location = PstLocation.fetchExc(locationPenerimaId);
                } catch (Exception ex) {
                }
            }

            if (locationPenerimaId != 0) {
                try {
                    Location location = PstLocation.fetchExc(locationPenerimaId);
                    locTerima = location.getName();
                } catch (Exception ex) {
                }
            }

            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "5%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "35%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "5%");
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Berat Stok Asal " + locAsal, "15%");
                ctrlist.addHeader("Berat Stok Terima " + locTerima, "15%");
            }
            ctrlist.addHeader(textListMaterialHeader[language][4] + " " + locAsal, "15%");
            ctrlist.addHeader(textListMaterialHeader[language][5] + " " + locTerima, "15%");

            ctrlist.setLinkRow(2);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEditAuto('");
            if (objectClass.size() == 1){
                ctrlist.setLinkSufix("')\" class=\"autoClick");
            } else {
                ctrlist.setLinkSufix("')");
            }
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector list = (Vector) objectClass.get(i);
                Material material = (Material) list.get(0);
                MaterialStock materialStock = (MaterialStock) list.get(1);
                Unit unit = (Unit) list.get(2);
                //added by dewok 2018 for jewelry
                MaterialDetail detail = new MaterialDetail();
                if (typeOfBusinessDetail == 2) {
                    material.setName(SessMaterial.setItemNameForLitama(material.getOID()));
                    long oidMatDetail = PstMaterialDetail.checkOIDMaterialDetailId(material.getOID());
                    try {
                        detail = PstMaterialDetail.fetchExc(oidMatDetail);
                    } catch (Exception e) {

                    }
                }
                
                Vector rowx = new Vector();

                /*Unit unit = new Unit();
                 try{
                 unit = PstUnit.fetchExc(material.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
                 }catch(Exception e){}*/
                double stokSisaAsal = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationAsalId, tranferDate, 0);
                double stokSisaTerima = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationPenerimaId, tranferDate, 0);
                //added by dewok 2018
                double beratSisaAsal = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), locationAsalId, tranferDate, 0);
                double beratSisaTerima = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), locationPenerimaId, tranferDate, 0);
                
//                if (allowZero == 0 && stokSisaAsal <= 0) {continue;}
                
                start = start + 1;
                rowx.add("<div align=\"center\">" + start + "</div>");
                rowx.add("<div align=\"center\">" + material.getSku() + "</div>");               
                rowx.add(material.getName());                
                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(beratSisaAsal) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(beratSisaTerima) + "</div>");
                }
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stokSisaAsal) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stokSisaTerima) + "</div>");

                lstData.add(rowx);
                long oid = getOidDfItem(vectDf, material.getOID());

                String name = "";
                name = material.getName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');
                if (typeOfBusinessDetail == 2) {
                    name = SessMaterial.setItemNameForLitama(material.getOID());
                }
                if (tranUsedPriceHpp == 0) {
                    lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + unit.getCode()
                            + "','" + FRMHandler.userFormatStringDecimal(material.getAveragePrice())
                            + "','" + material.getDefaultStockUnitId() + "','" + oid + "','" + materialStock.getQty() + "','" + detail.getBerat()
                            + "','" + material.getGondolaCode() + "','" + material.getAveragePrice() + "','" + detail.getOngkos());
                } else {
                    double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                    lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + unit.getCode()
                            + "','" + FRMHandler.userFormatStringDecimal(sellPrice)
                            + "','" + material.getDefaultStockUnitId() + "','" + oid + "','" + materialStock.getQty() + "','" + detail.getBerat()
                            + "','" + material.getGondolaCode() + "','" + material.getAveragePrice() + "','" + detail.getOngkos());
                }
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][1] + "</div>";
        }
        return result;
    }
%>

<!-- JSP Block -->
<%!
/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */
static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");
static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;

%>

<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long locationId = FRMQueryString.requestLong(request,"location_id");
long locationIdPenerima = FRMQueryString.requestLong(request,"location_id_penerima");
long periodeId = FRMQueryString.requestLong(request,"periode_id");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
String syspropAutoSave = PstSystemProperty.getValueByName("AUTO_SAVE_TRANSFER");
//added by dewok 2018
int allowZero = FRMQueryString.requestInt(request, "ALLOW_ZERO_QTY");
//for litama
int dispatchItemType = FRMQueryString.requestInt(request, "dispatch_item_type");
long idGondola = FRMQueryString.requestLong(request, "etalase_id");
//
int recordToGet = 15;
String stringTranfertDate = FRMQueryString.requestString(request, "transfer_date");
DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
Date transferDate = new Date();
if(!stringTranfertDate.equals("")){
    transferDate = format.parse(stringTranfertDate);
}

String whereClause = "";
if(typeOfBusinessDetail == 2){
    whereClause += " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + dispatchItemType + "'"
            + " AND M." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " = '" + idGondola + "'";
}
String orderBy = "MAT."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

if(periodeId==0){
    Vector vt_mp = PstPeriode.list(0,0,""+PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING,"");
    if(vt_mp!=null && vt_mp.size()>0){
        Periode matPeriode = (Periode)vt_mp.get(0);
        periodeId = matPeriode.getOID();
    }
}


/** objek pencarian */
SrcReportStock srcReportStock = new SrcReportStock();
srcReportStock.setLocationId(locationId);
srcReportStock.setPeriodeId(periodeId);
srcReportStock.setCategoryId(materialgroup);
srcReportStock.setSku(materialcode);
srcReportStock.setMaterialName(materialname);

//int vectSize = PstMaterialStock.getCountDaftarBarangStock(locationId,periodeId,materialgroup,materialcode,materialname);
/** get size stock list and stock value */
//Vector vctStockValue = SessReportStock.getStockValue(srcReportStock, calculateQtyNull);
Vector vctStockValue = SessReportStock.getStockValueBarcode(srcReportStock, calculateQtyNull, whereClause);
int vectSize = Integer.parseInt((String)vctStockValue.get(0));

CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(start<0){
    start=0;
}
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
    start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}
if(start<0){
    start=0;
}


//Vector vect = PstMaterialStock.getDaftarBarangStock(start,recordToGet,locationId,periodeId,materialgroup,materialcode,materialname);
//Vector vect = SessReportStock.getReportStockAll(srcReportStock, calculateQtyNull, start, recordToGet);
Vector vect = SessReportStock.getReportStockAllBarcode(srcReportStock, calculateQtyNull, start, recordToGet, whereClause);

String whr = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;
Vector vectCos = PstMatDispatchItem.list(0,0,whr,"");

/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */

int automaticSelected = Integer.valueOf(AppValue.getValueByKey("AUTOMATIC_SELECTED"));
if(vect.size() == 1 && automaticSelected==0 && !syspropAutoSave.equals("1")) {
    
    Vector list = (Vector)vect.get(0);
    Material material = (Material)list.get(0);
    MaterialStock materialStock = (MaterialStock)list.get(1);
    
    Unit unit = new Unit();
    try{
        unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
    }catch(Exception e){
    }
    long oidDfItem = getOidDfItem(vectCos,material.getOID());

    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
    
    %>
    <script language="JavaScript">
        <% if(oidDfItem == 0) { %>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
            self.opener.document.forms.frm_matdispatch.matCode.value = "<%=material.getSku()%>";
            self.opener.document.forms.frm_matdispatch.matItem.value = "<%=name%>";
            self.opener.document.forms.frm_matdispatch.matUnit.value = "<%=unit.getCode()%>";
            
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value = "<%=FRMHandler.userFormatStringDecimal(material.getAveragePrice())%>";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_UNIT_ID]%>.value = "<%=material.getDefaultStockUnitId()%>";
            self.opener.document.forms.frm_matdispatch.avbl_qty.value = "<%=materialStock.getQty()%>";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.style.display="";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();
	<% } else { %>
            self.opener.document.forms.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
            self.opener.document.forms.frm_matdispatch.command.value = "<%=Command.EDIT%>";
            self.opener.document.forms.frm_matdispatch.submit();
	<% } %>
        self.close();
    </script>
    <%
}

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matHpp,matUnitId,oidDfItem,avblQty,matBerat,etalaseId,hppawal,ongkosAwal){
    if(true){     
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
        self.opener.document.forms.frm_matdispatch.matCode.value = matCode;
        self.opener.document.forms.frm_matdispatch.matItem.value = matItem;
        <%if(typeOfBusinessDetail != 2){%>
            self.opener.document.forms.frm_matdispatch.matUnit.value = matUnit;
        <%}%>
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value = matHpp;
        <%if(typeOfBusinessDetail != 2){%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
        <%}%>
        self.opener.document.forms.frm_matdispatch.avbl_qty.value = avblQty;        
        <%if(typeOfBusinessDetail == 2){%>
            var hppAwalValue = hppawal*1;
            var hppTotalAwalValue = (+hppawal + +ongkosAwal)*1;
            var hppTotalValue = (+hppawal + +ongkosAwal)*1;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_LAST]%>.value = matBerat;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_CURRENT]%>.value = matBerat;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_SELISIH]%>.value = 0.0;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_ID]%>.value = etalaseId;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_AWAL]%>.value = hppAwalValue.toLocaleString();
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_ONGKOS_AWAL]%>.value = ongkosAwal;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_ONGKOS]%>.value = ongkosAwal;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL_AWAL]%>.value = hppTotalAwalValue.toLocaleString();
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = hppTotalValue.toLocaleString();
        <%}%>
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.style.display="";        
        <%if(typeOfBusinessDetail == 2){%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value="1";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_TO_ID]%>.focus();        
        <%} else {%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();        
        <%}%>
        try{
             self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.style.display="";
            }catch(err){
            }
            
    }else{
        self.opener.document.forms.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
        self.opener.document.forms.frm_matdispatch.command.value = "<%=Command.EDIT%>";
        self.opener.document.forms.frm_matdispatch.submit();
    }
    self.close();		
}

function cmdEditAuto(matOid,matCode,matItem,matUnit,matHpp,matUnitId,oidDfItem,avblQty,matBerat,etalaseId,hppawal,ongkosAwal){
    if(true){     
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
        self.opener.document.forms.frm_matdispatch.matCode.value = matCode;
        self.opener.document.forms.frm_matdispatch.matItem.value = matItem;
        <%if(typeOfBusinessDetail != 2){%>
            self.opener.document.forms.frm_matdispatch.matUnit.value = matUnit;
        <%}%>
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value = matHpp;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value = 1;
        <%if(typeOfBusinessDetail != 2){%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
        <%}%>
        self.opener.document.forms.frm_matdispatch.avbl_qty.value = avblQty;        
        <%if(typeOfBusinessDetail == 2){%>
            var hppAwalValue = hppawal*1;
            var hppTotalAwalValue = (+hppawal + +ongkosAwal)*1;
            var hppTotalValue = (+hppawal + +ongkosAwal)*1;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_LAST]%>.value = matBerat;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_CURRENT]%>.value = matBerat;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_BERAT_SELISIH]%>.value = 0.0;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_ID]%>.value = etalaseId;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_AWAL]%>.value = hppAwalValue.toLocaleString();
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_ONGKOS_AWAL]%>.value = ongkosAwal;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_ONGKOS]%>.value = ongkosAwal;
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL_AWAL]%>.value = hppTotalAwalValue.toLocaleString();
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = hppTotalValue.toLocaleString();
        <%}%>
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.style.display="";        
        <%if(typeOfBusinessDetail == 2){%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value="1";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_TO_ID]%>.focus();        
        <%} else {%>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();        
        <%}%>
        try{
             self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.style.display="";
            }catch(err){
            }
            
    }else{
        self.opener.document.forms.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
        self.opener.document.forms.frm_matdispatch.command.value = "<%=Command.EDIT%>";
        self.opener.document.forms.frm_matdispatch.submit();
    }
    self.opener.document.forms.frm_matdispatch.command.value=<%=Command.SAVE%>;
    self.opener.document.forms.frm_matdispatch.submit();
    self.close();		
}

function cmdListFirst()
{
    document.frmvendorsearch.command.value="<%=Command.FIRST%>";
    document.frmvendorsearch.action="dfwhdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListPrev(){
    document.frmvendorsearch.command.value="<%=Command.PREV%>";
    document.frmvendorsearch.action="dfwhdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListNext(){
    document.frmvendorsearch.command.value="<%=Command.NEXT%>";
    document.frmvendorsearch.action="dfwhdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListLast(){
    document.frmvendorsearch.command.value="<%=Command.LAST%>";
    document.frmvendorsearch.action="dfwhdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function cmdSearch(){
    document.frmvendorsearch.command.value="<%=Command.LIST%>";
    document.frmvendorsearch.start.value = '0';
    document.frmvendorsearch.action="dfwhdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function clear(){
    document.frmvendorsearch.txt_materialcode.value="";
}
function fnTrapKD(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSearch();
    }
}

function keyDownCheck(e){
    //activeTable();
   var trap = document.frmvendorsearch.trap.value;

   if (e.keyCode == 13 && trap==0) {
    document.frmvendorsearch.trap.value="1";
   }
   if (e.keyCode == 13 && trap==1) {
       document.frmvendorsearch.trap.value="0";
       cmdSearch();
       
   }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frmvendorsearch.txt_materialname.value="";
   }
}


function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
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
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<SCRIPT language=JavaScript>
function hideObjectForMarketing(){    
} 
	 
function hideObjectForWarehouse(){ 
}
	
function hideObjectForProduction(){
}
	
function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

</SCRIPT>


<%--autocomplate--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> <%@include file="../../../styletemplate/template_header_empty.jsp" %>
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%> </td>
        </tr>
        <tr> 
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="location_id" value="<%=locationId%>">
              <input type="hidden" name="location_id_penerima" value="<%=locationIdPenerima%>">
              <input type="hidden" name="periode_id" value="<%=periodeId%>">   
              <input type="hidden" name="dispatch_item_type" value="<%=dispatchItemType%>">                 
               <%--autocomplate--%>
              <input type="hidden" name="trap" value="">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
               <tr>
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="82%"> 
                      <%--
                  <%
                    Vector category = PstCategory.list(0,0,"","");
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    vectGroupVal.add("Select ...");
                    vectGroupKey.add("0");
                    if(category!=null && category.size()>0) {
                        for(int i=0; i<category.size(); i++) {
                            Category mGroup = (Category)category.get(i);
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                    } else {
                        vectGroupVal.add("No Category Available");
                        vectGroupKey.add("0");
                    }
                  %>
                  <%=ControlCombo.draw("txt_materialgroup", null, ""+materialgroup, vectGroupKey, vectGroupVal, " onkeydown=\"javascript:fnTrapKD(event)\"", "formElemen")%>
                  --%>
                  <select  name="txt_materialgroup" class="formElemen">
                   <option value="-1">Semua Category</option>
                   <%
                    Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                    //Category newCategory = new Category();
                    //add opie-eyek 20130821
                    String checked="selected";
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    if(materGroup!=null && materGroup.size()>0) {
                        String parent="";
                       // Vector<Category> resultTotal= new Vector();
                        Vector<Long> levelParent = new Vector<Long>();
                        for(int i=0; i<materGroup.size(); i++) {
                            Category mGroup = (Category)materGroup.get(i);
                                if(mGroup.getCatParentId()!=0){
                                    for(int lv=levelParent.size()-1; lv > -1; lv--){
                                        long oidLevel=levelParent.get(lv);
                                        if(oidLevel==mGroup.getCatParentId()){
                                            break;
                                        }else{
                                            levelParent.remove(lv);
                                        }
                                    }
                                    parent="";
                                    for(int lv=0; lv<levelParent.size(); lv++){
                                       parent=parent+"&nbsp;&nbsp;";
                                    }
                                    levelParent.add(mGroup.getOID());

                                }else{
                                    levelParent.removeAllElements();
                                    levelParent.add(mGroup.getOID());
                                    parent="";
                                }
                            %>
                                <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==srcReportStock.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                            <%
                        }
                    } else {
                        vectGroupVal.add("Tidak Ada Category");
                        vectGroupKey.add("-1");
                    }
                  %>
                  </select>
                  </td>
                </tr>				
                <tr> 
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="82%"> 
                    <input type="text" name="mat_code" size="30" value="<%=materialcode%>" class="formElemen" onkeyup="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr> 
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="82%"> 
                    <input type="text" onKeyDown="javascript:keyDownCheck(event)" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen" onkeyup="javascript:fnTrapKD(event)" id="txt_materialname">
                  </td>
                </tr>
                <tr>
                    <td>Termasuk Qty 0 (Nol)</td>
                    <%
                        String checkStatus = "";if(allowZero == 1) {checkStatus = "checked";}
                    %>
                    <td><input type="checkbox" <%=checkStatus%> name="ALLOW_ZERO_QTY" value="1"></td>
                </tr>
                <%if(typeOfBusinessDetail == 2){%>
                <tr>
                    <td>Etalase</td>
                        <td>
                            <%
                                String whereKsg = whereKsg = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " = '" + locationId + "'";                                
                                Vector listKsg = PstKsg.list(0,0,whereKsg,PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                Vector vectKsgVal = new Vector(1,1);
                                Vector vectKsgKey = new Vector(1,1);
                                for(int i=0; i<listKsg.size(); i++){
                                    Ksg matKsg = (Ksg)listKsg.get(i);
                                    Location location = new Location();
                                    try{
                                        location = PstLocation.fetchExc(locationId);
                                    }catch(Exception ex ){
                                        System.out.println(ex.getMessage());
                                    } 
                                    vectKsgKey.add(""+location.getCode()+" - "+matKsg.getCode());
                                    vectKsgVal.add(""+matKsg.getOID());
                                }
                            %>
                            <%=ControlCombo.draw("etalase_id","formElemen", null, ""+idGondola, vectKsgVal, vectKsgKey, "")%>
                        </td>
                </tr>
                <%}%>
                <tr>  
                  <td width="18%">&nbsp;</td>
                  <td width="82%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2">
                      <% if (syspropAutoSave.equals("1")){%>
                        <%=drawListMaterialAuto(SESS_LANGUAGE,vect,vectCos,start,tranUsedPriceHpp,locationId,locationIdPenerima,transferDate, typeOfBusinessDetail, allowZero)%>
                      <% } else { %>
                        <%=drawListMaterial(SESS_LANGUAGE,vect,vectCos,start,tranUsedPriceHpp,locationId,locationIdPenerima,transferDate, typeOfBusinessDetail, allowZero)%>
                      <% } %>
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <% 
                    ControlLine ctrlLine= new ControlLine();
                    %>
                    <%
                    ctrlLine.setLocationImg(approot+"/images");
                    ctrlLine.initDefault();
                    %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span></td>
                </tr>
              </table>
            </form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
<script language="JavaScript">
    document.frmvendorsearch.txt_materialname.focus();
</script>

<%-- add by Fitra --%>
<% if (syspropAutoSave.equals("1") && vect.size() == 1) {%> 
<script type="text/javascript">
$(function(){
    //window.location.href = $('.autoClick').attr('href');
});
</script>
<% } %>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>
</html>
