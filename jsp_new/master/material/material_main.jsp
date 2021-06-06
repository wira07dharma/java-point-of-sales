
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.db.DBHandler"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlMatVendorPrice"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@ page language = "java" %>

<!-- package java -->
<%@ page import = "java.util.*
                   ,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.session.masterdata.SessMaterial,
                   com.dimata.posbo.entity.search.SrcMaterial,
                   com.dimata.posbo.form.masterdata.FrmMaterial,
                   com.dimata.posbo.form.masterdata.CtrlMaterial,
                   com.dimata.posbo.form.masterdata.CtrlMaterialComposit,
                   com.dimata.common.entity.payment.*,
                   com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                   com.dimata.common.entity.payment.PstDiscountType,
                   com.dimata.common.entity.payment.DiscountType, com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem"%>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);
//int  appObjCodeShowPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
// ini privilege pelengkap
// ini privilege untuk harga selling price
//appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTER_D, AppObjInfo.G2_MASTER_D_MATERIAL, AppObjInfo.OBJ_MASTER_D_VIEW_SELL_PRICE);
boolean privEditPrice = true; //userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
boolean privShowPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW_VALUE));
// ini privilege untuk harga supplier
//appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTER_D, AppObjInfo.G2_MASTER_D_MATERIAL, AppObjInfo.OBJ_MASTER_D_VIEW_SUPP_PRICE);
boolean privEditSuppPrice = true; //userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

%>


<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR) {
    privAdd=false;
    privUpdate=false;
    privDelete=false;
}
%>

<!-- Jsp Block -->
<%!
public static String formatNumberList = "#,###.00";
public static String formatNumberEdit = "##.###";

static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"No","SKU","Barcode","Nama Barang","Merk","Kategori","Tipe","Unit Stok","Harga Jual", "Mata Uang Jual","Harga Beli", //10
            "Mata Uang Beli","Tipe Supplier", "Supplier", "Tipe Harga 1", "Tipe Harga 2", "Tipe Harga 3","Group","Discount Terakhir","PPN Terakhir", "Harga Beli Terakhir", //20
            "Tanggal Kadaluarsa","Profit", "Poin Minimum","Nomor Serial","Nilai Stok","Foto","Konsinyasi","Kode Gondola", "Ongkos Kirim Terakhir","Poin Sales", //30
            "Unit Beli","Sub Category","Deskripsi","Tipe Material Item", "Tipe Penjualan", "Tipe Retur"},//36
     {"No","Code","Barcode","Name","Merk","Category","Type","Stock Unit","Default Price",
             "Default Price Currency","Default Cost","Default Cost Currency","Default Supplier Type",
             "Supplier", "Price Type 1", "Price Type 2",
             "Price Type 3","Type","Last Discount","Last Vat","Last Buying Price","Expired Date","Profit",
             "Minimum Point","Serial Number","Inventory Value","Picture","Consigment","Gondola Code",
    "Last Cost Cargo","Poin Sales","Buying Unit","Sub Category","Description","Item Material Type", "Sales Rule", "Return Rule"}// 34
};

public static final String textListTitleHeader[][] = {
    //    0             1       2               3                                4                     5                            6              7
    {"Item Editor","Barang","Pabrik","Set Harga Supplier","Panjang kode lebih dari 12 huruf!!","Tidak ada data group ..", "Set Komponen ","View History"},
    {"Goods Editor","Goods","Factory","Supplier Price Setting","Code`s length more than 12 character!!","No group data available ..", "Component Setting","View History"}
};


public static String parserSerialCode(Vector listSerialCode){
    String strSerial = "";
    if(listSerialCode!=null && listSerialCode.size()>0){
        for(int k=0;k<listSerialCode.size();k++){
            MaterialStockCode materialStockCode = (MaterialStockCode)listSerialCode.get(k);
            if(strSerial.length()>0){
                strSerial = strSerial + "<br>"+materialStockCode.getStockCode();
            }else{
                strSerial = materialStockCode.getStockCode();
            }
        }
    }
    return strSerial;
}

/**
 * this method used to draw material item
 */
public String drawList(int language, Vector objectClass, long materialId, int start) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
    ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
    ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
    ctrlist.addHeader(textListMaterialHeader[language][3],"20%");
    ctrlist.addHeader(textListMaterialHeader[language][5],"15%");
    ctrlist.addHeader(textListMaterialHeader[language][6],"15%");
    ctrlist.addHeader(textListMaterialHeader[language][12],"10%");
    
    ctrlist.setLinkRow(1);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData = ctrlist.getLinkData();
    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset();
    Vector rowx = new Vector();
    int index = -1;
    
    if(start<0) start = 0;
    
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        Material material = (Material)temp.get(0);
        Category category = (Category)temp.get(1);
        SubCategory subCategory = (SubCategory)temp.get(2);
        
        rowx = new Vector();
        
        start = start + 1;
        
        rowx.add(""+start+"");
        rowx.add("<a href=\"javascript:cmdEdit('"+material.getOID()+"')\">"+material.getSku()+"</a>");
        rowx.add(material.getBarCode());
        rowx.add(material.getName());
        rowx.add(category.getName());
        rowx.add(subCategory.getName());
        rowx.add(PstMaterial.SpTypeSourceKey[material.getDefaultSupplierType()]);
        lstData.add(rowx);
    }
    return ctrlist.draw();
}

/* this constant used to list text of listHeader */
public static final String textListMaterialCompositHeader[][] = {
    {"No","SKU","Nama","Unit","Qty"},
    {"No","Code","Name","Unit","Qty"}
};

/**
 * if composite, draw list composer
 */
public String drawList(int language, Vector objectClass, int start) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("50%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListMaterialCompositHeader[language][0],"3%");
    ctrlist.addHeader(textListMaterialCompositHeader[language][1],"15%");
    ctrlist.addHeader(textListMaterialCompositHeader[language][2],"20%");
    ctrlist.addHeader(textListMaterialCompositHeader[language][3],"5%");
    ctrlist.addHeader(textListMaterialCompositHeader[language][4],"5%");
    
    ctrlist.setLinkRow(0);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData = ctrlist.getLinkData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    int index = -1;
    
    if(start<0) start = 0;
    
    for (int i = 0; i < objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MaterialComposit materialComposit = (MaterialComposit)temp.get(0);
        Unit unit = (Unit)temp.get(1);
        
        rowx = new Vector();
        start = start + 1;
        
        //Fetch Material Composit Info
        Material mat = new Material();
        try {
            mat = PstMaterial.fetchExc(materialComposit.getMaterialComposerId());
        } catch(Exception exx) {
        }
        
        rowx.add(""+start);
        rowx.add("<a href=\"javascript:editComposit('"+materialComposit.getOID()+"')\">"+mat.getSku()+"</a>");
        rowx.add(mat.getName());
        rowx.add(unit.getCode());
        rowx.add("<div align=\"right\">"+materialComposit.getQty()+"</div>");
        
        lstData.add(rowx);
    }
    return ctrlist.draw();
}

public String drawCheck(Vector checked) {
    ControlCheckBox chkBx=new ControlCheckBox();
    chkBx.setWidth(4);
    chkBx.setCellSpace("0");
    chkBx.setCellStyle("");
    chkBx.setTableAlign("center");
    
    Vector checkName = new Vector(1,1);
    Vector checkValue = new Vector(1,1);
    Vector checkCaption = new Vector(1,1);
    
    Vector sttRsv = PstLocation.list(0,0, PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE, null);
    Vector vname = new Vector(1,1);
    for(int i = 0;i < sttRsv.size();i++){
        Location temp = (Location)sttRsv.get(i);
        //int val = Integer.parseInt((String)temp.get(1));
        checkValue.add(""+temp.getOID());
        checkCaption.add(temp.getName());
        vname.add(""+temp.getOID()+"_"+i);
    }
    
    checkName = vname; //checkValue;
    return chkBx.draw(checkName,checkValue,checkCaption,checked);    
}

%>

<%
/* get data from form request */
int iCommand = FRMQueryString.requestCommand(request);
int startMat = FRMQueryString.requestInt(request, "start_mat");
int start_search = FRMQueryString.requestInt(request, "start_search");
int start_search2 = FRMQueryString.requestInt(request, "start_search2");

//get data dari menu penerimaan item (add new item) LITAMA & dari transfer lebur
int directPenerimaan = FRMQueryString.requestInt(request, "direct_penerimaan");
int itemMaterialType = FRMQueryString.requestInt(request, "material_type");
int itemRecType = FRMQueryString.requestInt(request, "receive_type");
double beratSebelum = FRMQueryString.requestDouble(request, "berat_sebelum");
double heSebelum = FRMQueryString.requestDouble(request, "he_sebelum");
long oidLocationLitama = FRMQueryString.requestLong(request, "location_id");

int startComposit = FRMQueryString.requestInt(request, "start_price");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
int cmdPrice = FRMQueryString.requestInt(request, "command_price");
long oidMatComposit = FRMQueryString.requestLong(request, "hidden_mat_composit_id");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidCatalog = FRMQueryString.requestLong(request, "oid_catalog");
String sourceCatalog = FRMQueryString.requestString(request, "source");
long oidVendor = FRMQueryString.requestLong(request, "hidden_vendor_id");
int catType = FRMQueryString.requestInt(request, "hidden_catType");

//untuk discount Qty
CurrencyType currencyType2 = new CurrencyType();
DiscountType discType = new DiscountType();
Location loc = new Location();
long oidCurrencyType = FRMQueryString.requestLong(request, "hidden_currency_type_id");
long oidDiscountType = FRMQueryString.requestLong(request, "hidden_discount_type_id");
long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
oidCurrencyType = currencyType2.getOID();
oidDiscountType = discType.getOID();
oidLocation = loc.getOID();
int typemenu = FRMQueryString.requestInt(request, "typemenu");

String sourceLink = FRMQueryString.requestString(request, "source_link");
String sourceLink2 = FRMQueryString.requestString(request, "source_link2");
double transRate = FRMQueryString.requestDouble(request,"trans_rate");
String materialTitle = textListTitleHeader[SESS_LANGUAGE][1]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_CATALOG];
String vendorPriceTitle = "Composite"; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_COMPOSIT];
String nameSpesialRequest = FRMQueryString.requestString(request, "namespesialrequest");
//MatReceiveItem matRI= new MatReceiveItem();
String skuCodeOtomatic="";

/* variable declaration */
boolean editWithoutList = false;
int recordToGet = 20;
int recordToGetVendor = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstMaterial.fieldNames[PstMaterial.FLD_SKU];
String vendor_name = "";
String subcategory_name = "";

SrcMaterial srcMaterial = new SrcMaterial();
if(session.getValue(SessMaterial.SESS_SRC_MATERIAL)!=null){
    srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
    session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
}

/* for manupalate price type */
// proses men set program yang untk hanoman / material sendiri.
long oidCurr = 0;
try {
    oidCurr = Long.parseLong((String)com.dimata.system.entity.PstSystemProperty.getValueByName("OID_CURR_FOR_PRICE_SALE"));
} catch (Exception e) {
    oidCurr = 0;
}
// - selesai

int design = 0;
try {
    design = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
} catch (Exception e) {
    design = 0;
}

long oidCurrSell = 0;
oidCurrSell = oidCurr;
//update opie-eyek jika dipakai baru di buat
if(design==1){
    oidCurr = 0;
}

long oidRetail = 0;
try {
    oidRetail = Long.parseLong((String)com.dimata.system.entity.PstSystemProperty.getValueByName("RETAIL"));
} catch (Exception e) {
    oidRetail = 0;
}

int multiLanguageName = 0;
try {
    multiLanguageName = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueByName("NAME_MATERIAL_MULTI_LANGUAGE"));
} catch (Exception e) {
    multiLanguageName = 0;
}

//>>> added by dewok 20190204, cek diskon hitung otomatis atau tidak
boolean autoCalculate = false;
try {
    autoCalculate = 1 == Integer.parseInt(PstSystemProperty.getValueByName("AUTO_CALCULATING_DISCOUNT_TO_PRICE"));
} catch (Exception e) {
    autoCalculate = false;
}

Vector listCurrStandard = PstStandartRate.listCurrStandard(oidCurr); //list currency type & standart rate
String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; //PstPriceType.fieldNames[PstPriceType.FLD_CODE]+", "+PstPriceType.fieldNames[PstPriceType.FLD_NAME];
Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);

System.out.println("listCurrStandard : "+listCurrStandard);

double priceRetail = 0.0;

Vector vectData = new Vector();
if(iCommand==Command.SAVE && privEditPrice==true && oidMaterial!=0){
    if(listPriceType!=null&&listPriceType.size()>0){
        for(int i=0;i<listPriceType.size();i++){
            PriceType priceType = (PriceType)listPriceType.get(i);
            Vector vect = new Vector(1,1);
            for(int j=0;j<listCurrStandard.size();j++){
                Vector vect2 = new Vector(1,1);
                Vector temp = (Vector)listCurrStandard.get(j);
                StandartRate standart = (StandartRate)temp.get(1);
                    
                String valPrice = request.getParameter("price_"+i+""+j);
                valPrice = FRMHandler.deFormatStringDecimal(valPrice);
               
                System.out.println("oidRetail : "+oidRetail+ " - "+priceType.getOID());
                System.out.println("oidCurr : "+oidCurrSell+ " - "+standart.getCurrencyTypeId());
                if((oidRetail==priceType.getOID()) && (oidCurrSell==standart.getCurrencyTypeId())){
                    if(valPrice=="")
                        valPrice = "0.0";
                    priceRetail = Double.parseDouble(valPrice); 
                }
                
                vect2.add(""+priceType.getOID());
                vect2.add(valPrice);
                vect2.add(""+standart.getOID());
                
                vect.add(vect2);
            }
            
            vectData.add(vect);
        }
    }
}

/* for manipulate discount */
String whClauseCrType = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+
        " = "+PstCurrencyType.INCLUDE;
String ordCrType = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX];
if(oidCurr!=0)
    whClauseCrType = whClauseCrType + " AND "+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+oidCurr;

Vector listCrType = PstCurrencyType.list(0,0,whClauseCrType,ordCrType);
String ordDsc = PstDiscountType.fieldNames[PstDiscountType.FLD_INDEX];
Vector listDscType = PstDiscountType.list(0,0,"",ordDsc);
//list Qty Discount
//Vector listDscQty = PstDiscountQtyMapping.listDiscQtyAll(0,0,"","");
//Vector listLokasi = PstLocation.list(0, 0, "", "");
//Vector listDscQty = PstDiscountQtyMapping.list(0,0,"","");

Vector vectDisc = new Vector(1,1);
if(iCommand==Command.SAVE && privEditPrice==true && oidMaterial!=0){
    if(listDscType!=null&&listDscType.size()>0){
        for(int i=0;i<listDscType.size();i++){
            Vector vect = new Vector(1,1);
            DiscountType disType = (DiscountType)listDscType.get(i);
            for(int j=0;j<listCurrStandard.size();j++){
                Vector vect2 = new Vector(1,1);
                Vector temp = (Vector)listCurrStandard.get(j);
                //StandartRate standart = (StandartRate)temp.get(1);
                CurrencyType ctType = (CurrencyType)temp.get(0);
                //CurrencyType ctType = (CurrencyType)listCrType.get(j);
                
                String valPct = request.getParameter("discount_1"+i+""+j);
                String value = request.getParameter("discount_2"+i+""+j);
                valPct = FRMHandler.deFormatStringDecimal(valPct);
                value = FRMHandler.deFormatStringDecimal(value);
                vect2.add(""+disType.getOID());
                vect2.add(""+ctType.getOID());
                vect2.add(valPct);
                vect2.add(value);
                vect.add(vect2);
            }
            vectDisc.add(vect);
        }
    }
}

/* for manipulate discount qty */
Vector vectDiscQty = new Vector(1,1);
if(iCommand==Command.SAVE && privEditPrice==true && oidMaterial!=0){
    if(listDscType!=null&&listDscType.size()>0){
        for(int i=0;i<listDscType.size();i++){
            Vector vect = new Vector(1,1);
            DiscountType disType = (DiscountType)listDscType.get(i);
            for(int j=0;j<listCurrStandard.size();j++){
                Vector vect2 = new Vector(1,1);
                Vector temp = (Vector)listCurrStandard.get(j);
                //StandartRate standart = (StandartRate)temp.get(1);
                CurrencyType ctType = (CurrencyType)temp.get(0);
                //CurrencyType ctType = (CurrencyType)listCrType.get(j);

                String valPct = request.getParameter("discount_1"+i+""+j);
                String value = request.getParameter("discount_2"+i+""+j);
                valPct = FRMHandler.deFormatStringDecimal(valPct);
                value = FRMHandler.deFormatStringDecimal(value);
                vect2.add(""+disType.getOID());
                vect2.add(""+ctType.getOID());
                vect2.add(valPct);
                vect2.add(value);
                vect.add(vect2);
            }
            
            // update by fitra
            vectDiscQty.add(vect);
        }
    }
}


/* ControlLine */
ControlLine ctrLine = new ControlLine();

/* Control material catalog */
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
FrmMaterial frmMaterial = ctrlMaterial.getForm();
// set format
//frmMaterial.setDecimalSeparator(",");
//frmMaterial.setDigitSeparator(".");

//integrasi dengan BO
Vector vectLocation = new Vector();
Vector selected = new Vector(1,1);
if (ctrlMaterial.DESIGN_MATERIAL_FOR == ctrlMaterial.DESIGN_HANOMAN){
    vectLocation = PstLocation.list(0,0, PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE, "");
    if(iCommand!=Command.EDIT){
        if(vectLocation!=null && vectLocation.size()>0){
            for(int i=0; i<vectLocation.size(); i++){
                Location bt = (Location)vectLocation.get(i);
                long stt = FRMQueryString.requestLong(request,""+bt.getOID()+"_"+i);
                //System.out.println("stt : "+stt);
                if(stt!=0){
                    selected.add(String.valueOf(stt));
                }
            }
        }
    }
}
//end ----

Material material = new Material();
//iErrCode = ctrlMaterial.action(iCommand,oidMaterial,vectData,vectDisc, selected);
// update by Fitra 20-04-2014
iErrCode = ctrlMaterial.action(iCommand,oidMaterial,vectData,vectDisc, selected,userId,userName,request);
frmMaterial = ctrlMaterial.getForm();
msgString = ctrlMaterial.getMessage();
material = ctrlMaterial.getMaterial();
oidMaterial = material.getOID();
Vector listPriceMapping = ctrlMaterial.getListPriceMapping();
Vector listDiscMapping = ctrlMaterial.getListDiscountMapping();

//data kiriman untuk input item penerimaan (LITAMA)
Unit unitToSend = new Unit();
try{unitToSend = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}
long matOid = material.getOID();
String matCode = "" + material.getSku();
String matItem = "";
Category category = new Category();
    Color col = new Color();
    long idCategori = material.getCategoryId();
    long idColor = material.getPosColor();
    try {
        category = PstCategory.fetchExc(idCategori);
        col = PstColor.fetchExc(idColor);
    } catch (Exception ex) {
        //ex.getMessage();
    }

if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
        matItem = "" + category.getName() + " " + col.getColorName() + " " + material.getName();
    } else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
        matItem = "" + category.getName() + " " + col.getColorName() + " Berlian " + material.getName();
    }
String matUnit = "" + unitToSend.getCode();
String matDesc = "" + material.getMaterialDescription();
long matUnitId = material.getDefaultStockUnitId();
int itemType = material.getMaterialJenisType();
double berat = 0;
double qtyInput = 0;
if (iErrCode == 0 && iCommand == Command.SAVE) {
    Vector<MaterialDetail> detail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + material.getOID() + "'", "");
    if (!detail.isEmpty()) {
        berat = detail.get(0).getBerat();
        qtyInput = detail.get(0).getQty();
    }
}
Billdetail billdetail = new Billdetail();
try {
    billdetail = PstBillDetail.fetchExc(oidMaterial);
  } catch (Exception e) {
  }
BillMain billMain = new BillMain();
try {
  billMain = PstBillMain.fetchExc(billdetail.getBillMainId());
  } catch (Exception e) {
  }


// proses barcode otomatis
// yang kode + price (harga jual Rp)
//Jika DELETE, switch to NONE for listing object
if (iCommand == Command.DELETE) {
    if(frmMaterial.errorSize()==0) {
	%>
            <jsp:forward page="material_list.jsp">
                <jsp:param name="command" value="<%=Command.FIRST%>"/>
            </jsp:forward>
	<%
    }
}

if(oidMaterial!=0){
    try{
        material = PstMaterial.fetchExc(oidMaterial);
        try{
            if(material.getSubCategoryId()!=0){
                SubCategory scat = PstSubCategory.fetchExc(material.getSubCategoryId());
                subcategory_name = scat.getName();
            }
        } catch(Exception e){
            System.out.println(e.toString());
            subcategory_name = "";
        }
        try{
            if(material.getSupplierId()!=0){
                ContactList cl = PstContactList.fetchExc(material.getSupplierId());
                if(cl.getContactType()==PstContactList.EXT_COMPANY || cl.getContactType()==PstContactList.OWN_COMPANY){
                    vendor_name = cl.getCompName();
                }
                if(cl.getContactType()==PstContactList.EXT_PERSONEL){
                    vendor_name = cl.getPersonName() + " " + cl.getPersonLastname();
                }
            }
        } catch(Exception e){
            System.out.println(e.toString());
            vendor_name = "";
        }
        
        System.out.println("priceRetail : "+priceRetail);
        if(iCommand==Command.SAVE && oidMaterial!=0){
            // this for update material price.
            material.setDefaultPrice(priceRetail);
            //PstMaterial.updateExc(material);
            PstMaterial.updateExcWithUpdateDate(material);
        }
    } catch(Exception exc){
        System.out.println(exc.toString());
        material = new Material();
    }
}else{

int otomaticSku =Integer.parseInt(PstSystemProperty.getValueByName("SKU_GENERATE_OTOMATIC"));
if(otomaticSku==1){
    skuCodeOtomatic="-";
}
}

// update material for barcode
if(1==2){
    if(iCommand==Command.SAVE && frmMaterial.errorSize()==0){
        PriceTypeMapping priceTypeMapping = PstPriceTypeMapping.getSellPriceRupiah(material.getOID(),"Rp");
        material.setBarCode(material.getSku()+""+Integer.parseInt(""+Formater.formatNumber(priceTypeMapping.getPrice(),"###")));
        material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
        try{
            //PstMaterial.updateExc(material);
            PstMaterial.updateExcWithUpdateDate(material);
        }catch(Exception e){}
    }
}

/**
 * get status of material group : stockable or unstockable
 */
boolean matStockable = true;

int vectSize = PstMaterial.getCount(whereClause);
if(iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST) {
    startMat = ctrlMaterial.actionList(iCommand,startMat,vectSize,recordToGet);
}

Vector listMaterial = new Vector(1,1);
listMaterial = PstMaterial.list(startMat,recordToGet,orderClause);
if(listMaterial.size()<1 && startMat>0) {
    if(vectSize - recordToGet > recordToGet)
        startMat = startMat - recordToGet;
    else {
        startMat = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    listMaterial = PstMaterial.list(startMat,recordToGet,orderClause);
}

CtrlMaterialComposit ctrlMaterialComposit= new CtrlMaterialComposit(request);
Vector listMaterialComposit = new Vector(1,1);

whereClause = " MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+" = "+oidMaterial;
int vectSizeComposit = PstMaterialComposit.getCount(whereClause);
orderClause = "";
MaterialComposit materialComposit = ctrlMaterialComposit.getMaterialComposit();
listMaterialComposit = PstMaterialComposit.list(startComposit,recordToGet, whereClause , orderClause);
Vector listUnit = PstUnit.list(0,0,"","");

/*Vector vtCurr = PstCurrencyType.list(0,0,"","");
CurrencyType matCurrency = new CurrencyType();
if(vtCurr!=null && vtCurr.size()>0){
    matCurrency = (CurrencyType)vtCurr.get(0);
}*/
//Currency Id untuk HPP
CurrencyType defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();

double qtyPerbaseUnit = PstUnit.getQtyPerBaseUnit(material.getBuyUnitId(), material.getDefaultStockUnitId());

Periode maPeriode = PstPeriode.getPeriodeRunning();
Vector vlocation = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);

//update opie-eyek 20130809
String whereLoc = PstLocation.fieldNames[PstLocation.FLD_TYPE]+"='"+PstLocation.TYPE_LOCATION_STORE+"'";
Vector vlocationDiscountQty = PstLocation.list(0,0,whereLoc,PstLocation.fieldNames[PstLocation.FLD_NAME]);

// proses set/get minimum stock
if(iCommand==Command.SAVE){
    // proses get request nilai minimum stock barang
        ctrlMaterial.setMinimumStockPerLocation(vlocation,oidMaterial,0, maPeriode.getOID(),userId,userName,request);
}

//integrasi dengan BO
if (ctrlMaterial.DESIGN_MATERIAL_FOR == ctrlMaterial.DESIGN_HANOMAN){
    if((iCommand==Command.EDIT || iCommand==Command.SAVE || iCommand==Command.ASK) && oidMaterial!=0){
        selected = new Vector(1,1);
        String w = PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID]+"="+oidMaterial;
        Vector vct = PstMatMappLocation.list(0,0, w, null);
        //out.println(vct);
        if(vct!=null && vct.size()>0){
            for(int i=0; i<vct.size(); i++){
                MatMappLocation io = (MatMappLocation)vct.get(i);
                selected.add(""+io.getLocationId());
            }
        }
    }
}

String pictPath = "";
if(srcMaterial.getShowImage()==1){
    SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
    pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(material.getSku());
    //objSessUploadCatalogPhoto.getRealFileName(String.valueOf(oidMaterial));// objSessUploadCatalogPhoto.fetchImage(oidMaterial) ; //
}

merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
String javascriptPrice="";
String javascriptPricex="";
int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));



//proses simpan supplier
long supplierId = FRMQueryString.requestLong(request, "FRM_FIELD_SUPPLIER_ID");
if(supplierId!=0){
    if(iCommand==Command.SAVE){
        //lakukan proses simpan ke vendor
        CtrlMatVendorPrice ctrlMatVendorPrice = new CtrlMatVendorPrice(request);
        int ccc = ctrlMatVendorPrice.actionInsertDefaultSupplier(0, supplierId, material);
    }
}
/*tambahan untuk litama*/
long kepemilikanId = FRMQueryString.requestLong(request, "kepemilikan_id_input");
double rateCurreny=0.0;
rateCurreny = PstStandartRate.getStandardRate();
MaterialDetail materialDetail =  new MaterialDetail();
long oidMaterialDetailId=PstMaterialDetail.checkOIDMaterialDetailId(oidMaterial);
try{
    materialDetail = PstMaterialDetail.fetchExc(oidMaterialDetailId);
    if(materialDetail.getRate()!=0){
        rateCurreny=materialDetail.getRate();
    }
}catch(Exception ex){
}

int masterGroupSize = FRMQueryString.requestInt(request, "groupSize");
if (masterGroupSize > 0 && iCommand == Command.SAVE){
    String sql = " DELETE FROM " + PstMaterialMappingType.TBL_MATERIAL_TYPE_MAPPING + " WHERE " + PstMaterialMappingType.fieldNames[PstMaterialMappingType.FLD_MATERIAL_ID] + " = " + oidMaterial;
	PstDataCustom.insertDataForSyncAllLocation(sql);
    DBHandler.execUpdate(sql);
    for (int i =0; i < masterGroupSize; i++){
        long typeId = FRMQueryString.requestLong(request, "MASTER_GROUP"+(i+1));
        MaterialTypeMapping map = new MaterialTypeMapping();
        map.setMaterialId(oidMaterial);
        map.setTypeId(typeId);
        map.setNote("");
        try {
            PstMaterialMappingType.insertExc(map);
        } catch (Exception exc){

        }
    }
}
String whereMapKsg = "MAP."+PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID]+"="+oidMaterial;
Vector vMapKsg = PstMatMappKsg.listJoinKsg(0, 0, whereMapKsg, "KSG."+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]);

%>

<%	
    SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto(); 
    String pictPath2 = objSessUploadCatalogPhoto.fetchImagePeserta(material.getSku());	
%>

<%
    int ext =0;
    ext =FRMQueryString.requestInt(request,"root");
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<link href="../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
<%
    try{
%>
<!--
/*------------- start main function ----------------------*/
function cmdSelectVendor() {
    window.open("vendordosearch.jsp?command=<%=Command.FIRST%>&txt_vendorname="+document.frmmaterial.txt_vendorname.value+"&frm_id=0","material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdSelectSubCategory() {
    window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmmaterial.txt_subcategory.value+
	"&oidCategory="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

<%
  long mat_vendor = FRMQueryString.requestLong(request, "mat_vendor");
  long currency_id = FRMQueryString.requestLong(request, "currency_id");
  double trans_rate = FRMQueryString.requestDouble(request, "trans_rate");

if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){ %>

function cmdCheck() {
    /* var strvalue  = "<%=approot%>/warehouse/material/receive/materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value+
                     "&txt_materialname="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>.value+
                    "&mat_vendor="+document.frmmaterial.mat_vendor.value+
                    "&currency_id="+document.frmmaterial.currency_id.value+
                    "&trans_rate="+document.frmmaterial.trans_rate.value;
        */
var strvalue  = "<%=approot%>/warehouse/material/receive/materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+
                     "&txt_materialname="+
                    "&mat_vendor="+document.frmmaterial.mat_vendor.value+
                    "&currency_id="+document.frmmaterial.currency_id.value+
                    "&trans_rate="+document.frmmaterial.trans_rate.value;
    window.location.href=strvalue;
}
<%}%>

//Open link to purchasing
<%if(  sourceLink2!=null && sourceLink2.equals("materialdosearch.jsp")){ %>

function cmdCheck() {
    /* var strvalue  = "<%=approot%>/warehouse/material/receive/materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value+
                     "&txt_materialname="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>.value+
                    "&mat_vendor="+document.frmmaterial.mat_vendor.value+
                    "&currency_id="+document.frmmaterial.currency_id.value+
                    "&trans_rate="+document.frmmaterial.trans_rate.value;
        */
var strvalue  = "<%=approot%>/purchasing/material/pom/materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+
                     "&txt_materialname="+
                    "&mat_vendor="+document.frmmaterial.mat_vendor.value+
                    "&currency_id="+document.frmmaterial.currency_id.value+
                    "&trans_rate="+document.frmmaterial.trans_rate.value;
    window.location.href=strvalue;
}
<%}%>

function cmdKeyUpPriceType(objForm){
    
	<%
        if(listPriceType!=null && listPriceType.size()>0) {
        int maxPriceType = listPriceType.size();
        for(int i=0; i<maxPriceType; i++) {
	%>

	if(objForm == document.frmmaterial.price_<%=i%>0) {
	<%
        if(i==0){
            if((maxPriceType > 1) && (valueDiv > 0)) {
        %>
                    ///document.frmmaterial.price_<%=i+1%>0.value = document.frmmaterial.price_<%=i%>0.value/<%=valueDiv%>;
        <%
                }
            }
        %>

        var price = cleanNumberFloat(objForm.value, guiDigitGroup, guiDecimalSymbol);
        // alert("as "+price);
		<%
		if(listCurrStandard!=null && listCurrStandard.size()>0)
		{
			int maxCurrStandard = listCurrStandard.size();
			for(int j=1; j<maxCurrStandard; j++)
			{
				Vector temp = (Vector)listCurrStandard.get(j);
				StandartRate sRate = (StandartRate)temp.get(1);
		%>
                                
		var price_rate = price / <%=sRate.getSellingRate()%>;
		if((isNaN(price_rate)) || (price_rate==""))
		{
                       
			price_rate = "0";
		}
		document.frmmaterial.price_<%=i%><%=j%>.value = formatFloat(price_rate, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                <%
                    if(i==0){
                        if((maxPriceType > 1) && (valueDiv > 0)){
                %>
                    var price_rate1 = (price/<%=valueDiv%>) / <%=sRate.getSellingRate()%>;
                    if((isNaN(price_rate)) || (price_rate=="")){
                        price_rate = "0";
                    }
                        //document.frmmaterial.price_<%=i+1%><%=j%>.value = formatFloat(price_rate1, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                <%
                        }
                    }
                %>

    	<%
			}
		}
		%>

	}

	<%
		}
	}
	%>
}

function cmdKeyUpPriceTypePerText(i)
{
        var price = cleanNumberFloat(0, guiDigitGroup, guiDecimalSymbol);

		<%
		if(listCurrStandard!=null && listCurrStandard.size()>0)
		{
			int maxCurrStandard = listCurrStandard.size();
			for(int j=1; j<maxCurrStandard; j++)
			{
				Vector temp = (Vector)listCurrStandard.get(j);
				StandartRate sRate = (StandartRate)temp.get(1);
		%>
                var price_rate = price / <%=sRate.getSellingRate()%>;
                if((isNaN(price_rate)) || (price_rate==""))
                {
                    price_rate = "0";
                }
                document.frmmaterial.price_0<%=j%>.value = formatFloat(price_rate, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    	<%
			}
		}
		%>
}


function cmdKeyUpPriceDiscType(objForm)
{
    //UPDATED BY DEWOK 20190205
    //AKSI HITUNG HARGA OTOMATIS SAAT KETIK NILAI DISKON DIANGGAP TIDAK BENAR
    //KARENA KOMBINASI TIPE HARGA DAN TIPE POTONGAN TERGANTUNG PADA SETTINGAN DEFAULT DI MASTER LOKASI ATAU MEMBER
    
	<%
    if(listDscType!=null && listDscType.size()>0)
    {
		int maxPriceType = listDscType.size();
		for(int i=0; i<maxPriceType; i++)
		{
	%>

        if(objForm == document.frmmaterial.discount_1<%=i%>0){
            var price = cleanNumberFloat(objForm.value, guiDigitGroup, guiDecimalSymbol);
            var pricereal = cleanNumberFloat(document.frmmaterial.price_<%=i%>0.value, guiDigitGroup, guiDecimalSymbol);
            var discval = (pricereal * price) / 100
            //document.frmmaterial.discount_1<%=i%>1.value = formatFloat(discval, '', guiDigitGroup, guiDecimalSymbol, decPlace);
            //document.frmmaterial.discount_2<%=i%>0.value = formatFloat(discval, '', guiDigitGroup, guiDecimalSymbol, decPlace);

            if(<%=i%>!=0){
               // alert(<%=i%>);
                var pricereal1 = cleanNumberFloat(document.frmmaterial.price_00.value, guiDigitGroup, guiDecimalSymbol);
                var prcreal1 = (pricereal1 * price) / 100
                //updated by dewok 20190204, hitung otomatis dimatikan, perhitungan dilakukan saat transaksi
                //document.frmmaterial.price_<%=i%>0.value = formatFloat(pricereal1 - prcreal1, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                cmdKeyUpPriceType(document.frmmaterial.price_<%=i%>0);

            }
        <%
            if(i==0){
                if((maxPriceType > 1) && (valueDiv > 0)){
        %>
                    document.frmmaterial.discount_1<%=i%>1.value = document.frmmaterial.discount_1<%=i%>0.value/<%=valueDiv%>;
                    //document.frmmaterial.price_<%=i+1%>0.value = document.frmmaterial.price_<%=i%>0.value/<%=valueDiv%>;
        <%
                }
            }
        %>
        <%
		if(listCurrStandard!=null && listCurrStandard.size()>0){
			int maxCurrStandard = listCurrStandard.size();
			for(int j=1; j<maxCurrStandard; j++){
				Vector temp = (Vector)listCurrStandard.get(j);
				StandartRate sRate = (StandartRate)temp.get(1);
		%>
                var price_rate = discval / <%=sRate.getSellingRate()%>;
                if((isNaN(price_rate)) || (price_rate=="")){
                    price_rate = "0";
                }
                document.frmmaterial.discount_1<%=i%><%=j%>.value = formatFloat(price_rate, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                var valp = cleanNumberFloat(document.frmmaterial.discount_1<%=i%><%=j%>.value, guiDigitGroup, guiDecimalSymbol);
                document.frmmaterial.discount_2<%=i%><%=j%>.value = formatFloat(parseFloat(valp) * parseFloat(<%=sRate.getSellingRate()%>) , '', guiDigitGroup, guiDecimalSymbol, decPlace);
    	<%
			}
		}
		%>

	}
        //Entry Discount dalam bentuk uang

        if(objForm == document.frmmaterial.discount_2<%=i%>0){
           var price = cleanNumberFloat(objForm.value, guiDigitGroup, guiDecimalSymbol);
            var pricereal = cleanNumberFloat(document.frmmaterial.price_<%=i%>0.value, guiDigitGroup, guiDecimalSymbol);
            var discval = (price * 100) / pricereal;
            //document.frmmaterial.discount_1<%=i%>1.value = formatFloat(discval, '', guiDigitGroup, guiDecimalSymbol, decPlace);
            document.frmmaterial.discount_1<%=i%>0.value = formatFloat(discval, '', guiDigitGroup, guiDecimalSymbol, decPlace);

            if(<%=i%>!=0){
               // alert(<%=i%>);
                var pricereal1 = cleanNumberFloat(document.frmmaterial.price_00.value, guiDigitGroup, guiDecimalSymbol);
                var prcreal1 = (price * 100) / pricereal;
                document.frmmaterial.price_<%=i%>0.value = formatFloat(pricereal1 - price, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                cmdKeyUpPriceType(document.frmmaterial.price_<%=i%>0);

            }

             <%
            if(i==0){
                if((maxPriceType > 1) && (valueDiv > 0)){
        %>
                    document.frmmaterial.discount_2<%=i%>1.value = document.frmmaterial.discount_2<%=i%>0.value/<%=valueDiv%>;
                    //document.frmmaterial.price_<%=i+1%>0.value = document.frmmaterial.price_<%=i%>0.value/<%=valueDiv%>;
        <%
                }
            }
        %>
        <%
		if(listCurrStandard!=null && listCurrStandard.size()>0){
			int maxCurrStandard = listCurrStandard.size();
			for(int j=1; j<maxCurrStandard; j++){
				Vector temp = (Vector)listCurrStandard.get(j);
				StandartRate sRate = (StandartRate)temp.get(1);
		%>
                var price_rate = discval / <%=sRate.getSellingRate()%>;
                if((isNaN(price_rate)) || (price_rate=="")){
                    price_rate = "0";
                }
                document.frmmaterial.discount_2<%=i%><%=j%>.value = formatFloat(price_rate, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                var valp = cleanNumberFloat(document.frmmaterial.discount_2<%=i%><%=j%>.value, guiDigitGroup, guiDecimalSymbol);
                document.frmmaterial.discount_1<%=i%><%=j%>.value = formatFloat(parseFloat(valp) * parseFloat(<%=sRate.getSellingRate()%>) , '', guiDigitGroup, guiDecimalSymbol, decPlace);
    	<%
			}
		}
		%>

        }

	<%
		}
	}
	%>
}

function cmdAdd(){
	document.frmmaterial.hidden_material_id.value="0";
	document.frmmaterial.command.value="<%=Command.ADD%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}


function cmdViewHistory(oid) {
    var strvalue ="../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid;
    window.open(strvalue,"material", "height=600,width=800,left=300,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}



function cmdAsk(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.command.value="<%=Command.ASK%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdConfirmDelete(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.command.value="<%=Command.DELETE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function changeUnit(){
	var currId = document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_STOCK_UNIT_ID]%>.value;
	switch(currId){
	<%
        if(listUnit!=null && listUnit.size()>0){
            long deptId = 0;
            for(int i=0; i<listUnit.size(); i++){
            Unit unit = (Unit)listUnit.get(i);
	%>
		case "<%=unit.getOID()%>" :
			 <%
                 String whereUnit = ""+PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID]+"="+unit.getOID();
                 Vector vtUnit = PstUnit.list(0,0,whereUnit,"");
                 if(vtUnit!=null && vtUnit.size()>0){
			 %>
				for(var k=document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.length-1; k>-1; k--){
					document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.options.remove(k);
				}
                oOption = document.createElement("OPTION");
				oOption.value = "<%=unit.getOID()%>";
				oOption.text = "<%=unit.getCode()%>";
				document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.add(oOption);
			 <%
                for(int j=0; j<vtUnit.size(); j++){
                    Unit xUnit = (Unit)vtUnit.get(j);
			 %>

				oOption = document.createElement("OPTION");
				oOption.value = "<%=xUnit.getOID()%>";
				oOption.text = "<%=xUnit.getCode()%>";
				document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.add(oOption);
			 <%
                     }
                }else{
			%>
				for(var k=document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.length-1; k>-1; k--){
					document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.options.remove(k);
				}
                oOption = document.createElement("OPTION");
				oOption.value = "<%=unit.getOID()%>";
				oOption.text = "<%=unit.getCode()%>";
				document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.add(oOption);
			<%}%>
			break;
	<%
            }
        }
	%>
		default :
				for(var k=document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.length-1; k>-1; k--){
					document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>.options.remove(k);
				}
			break;
	}
}

function cmdSave()
{
    <%if(typeOfBusinessDetail == 2){%>
        var etalase = document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_GONDOLA_CODE]%>.value;
        if (etalase == "" || etalase == 0) {
            alert("Etalase belum dipilih");
            return false;
        }
    <%}%>
	//Cek panjang SKU
	var my_sku = document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value;
	var panjang_sku = my_sku.length;
	if (panjang_sku <= 1000)
	{

		document.frmmaterial.command.value="<%=Command.SAVE%>";
		document.frmmaterial.prev_command.value="<%=prevCommand%>";
		document.frmmaterial.action="material_main.jsp?source=<%=sourceCatalog%>&oid_catalog=<%=oidCatalog%>";
		document.frmmaterial.submit();

	}
	else
	{
		alert('<%=textListTitleHeader[SESS_LANGUAGE][4]%>');
	}

}

function cmdSelectItemToReceive(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode){
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
    self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
    self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matCode); //document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    self.close();
}

function cmdSelectItemToPurchasing(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,disc,currPrice){
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_purchaseorder.matCode.value = matCode;
    self.opener.document.forms.frm_purchaseorder.matItem.value = matItem;
    self.opener.document.forms.frm_purchaseorder.matUnit.value = matUnit;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.document.forms.frm_purchaseorder.<%//=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>.value = disc;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.value = disc;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value = matPrice;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value=matPrice;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>.focus();
    //self.opener.document.forms.calculate();

    self.close();
    //self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
   // self.opener.document.forms.frm_purchaseorder.matCode.value = matCode;
    //self.opener.document.forms.frm_purchaseorder.matItem.value = matItem;
    //self.opener.document.forms.frm_purchaseorder.matUnit.value = matUnit;
   // self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value = matPrice;
    //self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
   // self.opener.changeFocus(self.opener.document.forms.frm_purchaseorder.matCode); //document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    self.close();
}

function cmdEdit(oidMaterial)
{
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.start_price.value=0;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}


function setPrice(oid){
	document.frmmaterial.hidden_material_id.value=oid;
	document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.command.value="<%=Command.LIST%>";
	document.frmmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdCancel(oidMaterial)
{
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdBack()
{
	document.frmmaterial.command.value="<%=Command.BACK%>";
	document.frmmaterial.action="material.jsp";
	document.frmmaterial.submit();
}

function cmdListFirst(){
	document.frmmaterial.command.value="<%=Command.FIRST%>";
	document.frmmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdListPrev(){
	document.frmmaterial.command.value="<%=Command.PREV%>";
	document.frmmaterial.prev_command.value="<%=Command.PREV%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdListNext(){
	document.frmmaterial.command.value="<%=Command.NEXT%>";
	document.frmmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdListLast(){
	document.frmmaterial.command.value="<%=Command.LAST%>";
	document.frmmaterial.prev_command.value="<%=Command.LAST%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}
/*------------- end main function -----------------*/


/*------------- start vendor price function -----------------*/
function addComposit(){
	document.frmmaterial.command.value="<%=Command.ADD%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="material_composit.jsp";
	document.frmmaterial.submit();
}

function listComposit(comm)
{
	document.frmmaterial.command.value=comm;
	document.frmmaterial.prev_command.value=comm;
	document.frmmaterial.hidden_material_id.value="<%=oidMaterial%>";
	document.frmmaterial.action="material_composit.jsp";
	document.frmmaterial.submit();
}

function editComposit(mCompositOid)
{
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.hidden_mat_composit_id.value=mCompositOid;
	document.frmmaterial.action="material_composit.jsp";
	document.frmmaterial.submit();
}

function cmdViewMin()
{
	document.frmmaterial.command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="material_minmax.jsp";
	document.frmmaterial.submit();
}

function calculate(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = 0.0; //cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var profit = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>.value,guiDigitGroup,guiDecimalSymbol);


	if((isNaN(cost)) || (cost=="")){
		cost = 0;
	}

	if((isNaN(lastDisc)) || (lastDisc=="")){
		lastDisc = 0;
	}

	if((isNaN(lastVat)) || (lastVat=="")){
		lastVat = 0;
	}

	var totaldiscount = (cost * lastDisc) / 100;
	var totalMinus = cost - totaldiscount;
    var totalppn = (totalMinus * lastVat) / 100;
	var totalCost = totalMinus + totalppn;

	document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    var currBuyProfit = (totalCost * profit) / 100;
    var lastCurrBuyProfit = totalCost + currBuyProfit;
	var lastCurrBuyWithProfit = lastCurrBuyProfit/<%=qtyPerbaseUnit%>;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE]%>.value = lastCurrBuyWithProfit;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>.value = lastCurrBuyWithProfit;
}

//calculate with last cost cargo
function calculate1(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = 0.0; //cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var profit = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>.value,guiDigitGroup,guiDecimalSymbol);
        //last cost cargo
        var lastCostCargo = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_COST_CARGO]%>.value,guiDigitGroup,guiDecimalSymbol);

	if((isNaN(cost)) || (cost=="")){
		cost = 0;
	}

	if((isNaN(lastDisc)) || (lastDisc=="")){
		lastDisc = 0;
	}

	if((isNaN(lastVat)) || (lastVat=="")){
		lastVat = 0;
	}

        if((isNaN(lastCostCargo)) || (lastCostCargo=="")){
		lastCostCargo = 0;
	}

	var totaldiscount = (cost * lastDisc) / 100;
	var totalMinus = cost - totaldiscount;
    var totalppn = (totalMinus * lastVat) / 100;
	var totalCost = totalMinus + totalppn;
        //var totalCostAll = totalCost + lastCostCargo;
        var totalCostAll = parseFloat(totalCost) + parseFloat(lastCostCargo);

	document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>.value = formatFloat(totalCostAll, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    var currBuyProfit = (totalCost * profit) / 100;
    var lastCurrBuyProfit = totalCost + currBuyProfit;
	var lastCurrBuyWithProfit = lastCurrBuyProfit/<%=qtyPerbaseUnit%>;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE]%>.value = lastCurrBuyWithProfit;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>.value = lastCurrBuyWithProfit;
}

//add opie-eyek 20130812 untuk cek harga dari penerimaan terakhir
function calculateWithLastReceive(){
	var cost = cleanNumberFloat(document.frmmaterial.lastreceiveprice.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = 0.0; //cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var profit = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var lastCostCargo = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_COST_CARGO]%>.value,guiDigitGroup,guiDecimalSymbol);

	if((isNaN(cost)) || (cost=="")){
		cost = 0;
	}

	if((isNaN(lastDisc)) || (lastDisc=="")){
		lastDisc = 0;
	}

	if((isNaN(lastVat)) || (lastVat=="")){
		lastVat = 0;
	}

        if((isNaN(lastCostCargo)) || (lastCostCargo=="")){
		lastCostCargo = 0;
	}

	var totaldiscount = (cost * lastDisc) / 100;
	var totalMinus = cost - totaldiscount;
        var totalppn = (totalMinus * lastVat) / 100;
	var totalCost = totalMinus + totalppn;
        var totalCostAll = parseFloat(totalCost) + parseFloat(lastCostCargo);

	document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>.value = formatFloat(totalCostAll, '', guiDigitGroup, guiDecimalSymbol, decPlace);
        document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>.value=cost;
        
        var currBuyProfit = (totalCost * profit) / 100;
        var lastCurrBuyProfit = totalCost + currBuyProfit;
	var lastCurrBuyWithProfit = lastCurrBuyProfit/<%=qtyPerbaseUnit%>;
        document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE]%>.value = lastCurrBuyWithProfit;
        document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>.value = lastCurrBuyWithProfit;
}

function viewDocumentReceive(idRcv, idPo){
    <%if (prochainMenuBootstrap==1){%>
	if(parseInt(idPo) != 0) {
                window.open("../../warehouse/material/receive/receive_wh_supp_material_edit.jsp?hidden_receive_id="+idRcv+"&start=0&approval_command=44&command=3", '_blank');
	}
	else {
                window.open("../../warehouse/material/receive/receive_wh_supp_material_edit.jsp?hidden_receive_id="+idRcv+"&start=0&approval_command=44&command=3", '_blank');
	}
    <%}else{%>
        if(parseInt(idPo) != 0) {
                window.open("../../warehouse/material/receive/receive_wh_supp_po_material_edit.jsp?hidden_receive_id="+idRcv+"&start=0&approval_command=44&command=3", '_blank');
	}
	else {
                window.open("../../warehouse/material/receive/receive_wh_supp_material_edit_old.jsp?hidden_receive_id="+idRcv+"&start=0&approval_command=44&command=3", '_blank');
	} 
    <%}%>     
}

function showHistoryReceive(oidMaterial){
    var strvalue  = "List_Last_Receive.jsp?command=<%=Command.EDIT%>&oidMaterial="+oidMaterial;
                    //"&hidden_material_id="+oidMaterial+
                    //"&mat_code="+document.frm_recmaterial.matCode.value+
                    //"&txt_materialname="+document.frm_recmaterial.matItem.value;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

//end of calculate with last cost cargo
function cmdEditDiscQty(oidCurrencyType, oidDiscountType, oidLocation, oidMaterial)
{
	//document.frmmaterial.hidden_currency_type_id.value=oidCurrencyType;
        //document.frmmaterial.hidden_discount_type_id.value=oidDiscountType;
        //document.frmmaterial.hidden_location_id.value=oidLocation;
        document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="discount_qty.jsp";
	document.frmmaterial.submit();
}


function cmdDiscQty(oidDiscountType, oidCurrencyType, oidLocation, oidMaterial) {
    var strvalue  = "<%=approot%>/master/material/discount_qty.jsp?command=<%=Command.ADD%>"+
                    "&hidden_discount_type_id="+oidDiscountType+
                    "&hidden_currency_type_id="+oidCurrencyType+
                    "&hidden_location_id="+oidLocation+
                    "&hidden_material_id="+oidMaterial;

    winSrcMaterial = window.open(strvalue,"materialdiscqty", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

//component composit
//by  Mirahu
//3 June 2011
function setComponenComposit(oid){
	document.frmmaterial.hidden_material_id.value=oid;
	document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.command.value="<%=Command.LIST%>";
	document.frmmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="componen_composit_edit.jsp";
	document.frmmaterial.submit();
}


function addUnitBuy(oidMaterial){
    var strvalue  = "material_unit_order.jsp?command=<%=Command.LIST%>&oidMaterial="+oidMaterial;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function addUnit(oidMaterial){
    var strvalue  = "matunit.jsp?command=<%=Command.LIST%>&oidMaterial="+oidMaterial+"&source=1";
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function addCategory(oidMaterial){
    var strvalue  = "matcategory.jsp?command=<%=Command.LIST%>&oidMaterial="+oidMaterial+"&source=1";
    winSrcMaterial = window.open(strvalue,"PopupWindow", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function addSubCategory(oidMaterial){
    var strvalue  = "matsubcategory.jsp?command=<%=Command.LIST%>&oidMaterial="+oidMaterial+"&source=1";
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function addMerk(oidMaterial){
    var strvalue  = "matmerk.jsp?command=<%=Command.LIST%>&oidMaterial="+oidMaterial+"&source=1";
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function viewSerialCode(oidMaterial,locationId){
    var strvalue  = "view_serial_number.jsp?command=<%=Command.LIST%>&hidden_material_id="+oidMaterial+"&hidden_location_id="+locationId;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}


function faktorJualChange(val){    
    var hargajual=0;    
    var val = cleanNumberFloat(document.frmmaterial.harga_jual.value,guiDigitGroup,guiDecimalSymbol);
    if((isNaN(val)) || (val=="")){
            val = 0;
    }
    
    var rate = cleanNumberFloat(document.frmmaterial.rate_penjualan.value,guiDigitGroup,guiDecimalSymbol);
    if((isNaN(rate)) || (rate=="")){
            rate = 0;
    }        
    
    hargajual=val/rate;
    
    <% if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {%>
        document.frmmaterial.faktor_jual.value = hargajual.toFixed(2);
    <%} else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {%>
    
    if((isNaN(hargajual)) || (hargajual=="")){
            hargajual = 0;
    }
            
    var hargabeli = cleanNumberFloat(document.frmmaterial.harga_beli.value,guiDigitGroup,guiDecimalSymbol);
    if((isNaN(hargabeli)) || (hargabeli=="")){
            hargabeli = 0;
    }
    
    var uphet_persentase = cleanNumberFloat(document.frmmaterial.uphet_persentase.value,guiDigitGroup,guiDecimalSymbol);
    if((isNaN(uphet_persentase)) || (uphet_persentase=="")){
            uphet_persentase = 0;
    }        
   // alert(""+hargabeli+(hargabeli*(uphet_persentase/100)));
   // alert(""+(hargabeli*(uphet_persentase/100)));
    var hh = hargabeli*(uphet_persentase/100);
    //alert("- "+hargabeli);
    //alert("-- "+hargabeli*(uphet_persentase/100));
    //alert((parseInt(hargabeli)+parseInt(hh)));
    var uphetRp2=(parseInt(hargabeli)+parseInt(hh));
    var uphetRp=(parseInt(hargabeli)+parseInt(hh)) - parseInt(hargabeli);
    var uphetPresentase=((uphetRp/hargabeli)*100)-100;
    
    var uphetRpTotal=uphetRp2 * (30/100);
    //alert(uphetRpTotal);
    var uphetRpTotal2=parseInt(uphetRp2)+(parseInt(uphetRp2*(30/100)));
    
    var koreksi = val-uphetRpTotal2;
    document.frmmaterial.faktor_jual.value = hargajual.toFixed(2);
    document.frmmaterial.uphet_value.value = formatFloat(uphetRp, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    document.frmmaterial.uphet_value_2.value = formatFloat(uphetRp2, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    document.frmmaterial.uphet_value_total.value = formatFloat(uphetRpTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    
    document.frmmaterial.uphet_value_total_2.value = formatFloat(uphetRpTotal2, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    document.frmmaterial.koreksi.value = koreksi.toFixed(2);
    <%}%>
}

function changeRateJual(){
    var hargajual=0;
    var uphet_persentase = cleanNumberFloat(document.frmmaterial.faktor_jual.value,guiDigitGroup,guiDecimalSymbol);
    //document.frmmaterial.harga_jual.value = formatFloat(hargajual, '', guiDigitGroup, guiDecimalSymbol, decPlace);
}
/*------------- end vendor price function -----------------*/



function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

//-->

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function uploadPicture(){
  window.open("simple_upload_image.jsp?hidden_material_id=<%=oidMaterial%>");   
}

function setProductReq(materialOID){
  window.open("product_req.jsp?hidden_material_id=<%=oidMaterial%>");   
}

function printBarcode(valueRp,counterLoop,form){
    var con = confirm("Print Barcode ? ");
    var kata = "document.frmmaterial."+counterLoop+".value";
    var loop = document.frmmaterial.price_barcode_0.value;
    var loop2 = kata.valueOf();
    if (con ==true)
    {
        window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcodeRetail?hidden_material_id=<%=oidMaterial%>&value="+valueRp+"&counter="+loop+"&command=<%=Command.EDIT%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    }
}
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<style type="text/css">
<!--
.style9 {font-size: 14px; font-weight: bold; font-family: Arial, Helvetica, sans-serif; color: #FFFFFF; }

.label_button {
    border-style: solid;
    border-width: thin;
    border-color: grey;
    border-radius: 2px;
    padding: 3px 5px;
    cursor: pointer;
    background-color: window;
}
-->
</style>
<%--autocomplate add by fitra--%>
<script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
<script src="../../styles/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../styles/dimata-app.js"></script>
<link rel="stylesheet" type="text/css" href="../../styles/style.css" /> 
<script>                                    

    var loadFile = function (event) {
        var output = document.getElementById('output');
        output.src = URL.createObjectURL(event.target.files[0]);
    };
    
    function cmdSendToPenerimaan(matOid,matCode,matItem,matUnit,matUnitId,matQty,matBerat,matDesc){
        //alert(""+matQty+"-"+matBerat);
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
        self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
        self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
        self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
        //self.opener.document.forms.frm_recmaterial.<%--=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]--%>.value = matQty;
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = matQty;
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BERAT]%>.value = matBerat;
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_REMARK]%>.value = matDesc;
        self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>);
        self.close();    
    }

    function cmdBackToPenerimaan() {
        //self.opener.location.reload();
        self.close(); 
    }

    function cmdSendToTransferLebur(matOid,matCode,matItem,matUnit,matUnitId,matQty,matBerat,matDesc,itemType){
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_MATERIAL_TARGET_ID]%>.value = matOid;
        self.opener.document.forms.frm_matdispatch.matCode1.value = matCode;
        self.opener.document.forms.frm_matdispatch.matItem1.value = matItem;
        self.opener.document.forms.frm_matdispatch.matUnit1.value = matUnit;    
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_UNIT_TARGET_ID]%>.value = matUnitId;
        self.opener.document.forms.frm_matdispatch.avbl_qty1.value = matQty;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.style.display="";    
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.value = matQty;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BERAT]%>.value = matBerat;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_REMARK]%>.value = matDesc;
        var beratAwal = <%=beratSebelum%>;
        var heAwal = <%=heSebelum%>;
        var heBaru = (+heAwal * +matBerat / +beratAwal);
        if (itemType === "<%=Material.MATERIAL_TYPE_EMAS%>") {
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET]%>.value = 0;//formatFloat(heBaru, '', guiDigitGroup, guiDecimalSymbol, decPlace);
            //self.opener.document.forms.frm_matdispatch.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.focus();
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET]%>.focus();
        } else {
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET]%>.focus();
        }
        self.close();
    }
        
    $(document).ready(function () {
        
        var direct = "<%=directPenerimaan%>";
        var command = <%=iCommand%>;
        var error = "<%=iErrCode%>";
        var matOid = "<%=matOid%>";
        var matCode = "<%=matCode%>";
        var matItem = "<%=matItem%>";
        var matUnit = "<%=matUnit%>";
        var matUnitId = "<%=matUnitId%>";
        var matQty = "<%=qtyInput%>";
        var matBerat = "<%=berat%>";
        var matDesc = "<%=matDesc%>";
        if (direct === "1" && command === <%=Command.SAVE%> && error === "0") {  
            //alert("direct="+direct+"-command="+command+"-error="+error);
            //alert("" + matQty + " " + matBerat);
            cmdSendToPenerimaan(matOid,matCode,matItem,matUnit,matUnitId,matQty,matBerat,matDesc);
        } else if (direct === "2" && command === <%=Command.SAVE%> && error === "0") {
            cmdBackToPenerimaan();
        } else if (direct === "3" && command === <%=Command.SAVE%> && error === "0") {
            cmdSendToTransferLebur(matOid,matCode,matItem,matUnit,matUnitId,matQty,matBerat,matDesc,"<%=itemType%>");
        }
        
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
        
        $('#btnBrowse').change(function () {
            $('#msg').html($(this).val());
        });
        
        $('#btnUploadFoto').click(function () {            
            var foto = $('#btnBrowse').val();
            if (foto === "") {                
                alert("Silakan pilih foto terlebih dahulu");                
            } else {
                $(this).html("Tunggu...");
                $(this).attr({"disabled" : true});
                $('#formUpload').submit();
                return false;
            }
        });
        
    });
</script>
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#FCFDEC" >    
    <%if((sourceLink==null || !sourceLink.equals("materialdosearch.jsp"))|| (sourceLink2==null || !sourceLink2.equals("materialdosearch.jsp"))){ %>
 <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%if (ext!=1){%>
      <%@ include file = "../../main/header.jsp" %>
      <%}%>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%if (ext!=1){%>
      <%@ include file = "../../main/mnmain.jsp" %>
      <%}%>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%if (ext!=1){%>
      <%@include file="../../styletemplate/template_header.jsp" %>
      <%}%>
    </td>
  </tr>
  <%}%>
  <%}%>
  <tr> 
    <td valign="top" align="left" style="background-color:white;"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0"  style="">  
        <tr> 
          <td style="padding:8px 5px;background-color: #e8e8e8;color:#656565" height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Masterdata
            &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%> : (<%=material.getSku()%>) <%=material.getFullName()%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <%String lokasiSimpanFoto = "";%>
                <form id="formUpload" class="form-horizontal" action="<%=approot%>/AjaxUploadFile?hidden_material_id=<%=material.getOID()%>&user_name=<%=userName%>&user_id=<%=userId%>" method="post" enctype="multipart/form-data">                        
                    <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
                    <input type="hidden" name="direct_penerimaan" value="<%=directPenerimaan%>">
                    <input type="file" name="file" id="btnBrowse" style="display: none" accept=".jpg" onchange="loadFile(event)" />
                </form>
                <form name="frmmaterial" method ="post" action="">
                    <!--for litama-->
              <input type="hidden" name="direct_penerimaan" value="<%=directPenerimaan%>">
              <input type="hidden" name="berat_sebelum" value="<%=beratSebelum%>">
              <input type="hidden" name="he_sebelum" value="<%=heSebelum%>">
              <input type="hidden" name="location_id" value="<%=oidLocationLitama%>">
              <!---->
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start_price" value="<%=startComposit%>">
              <input type="hidden" name="start_search" value="1">
              <input type="hidden" name="start_search2" value="<%=start_search2%>"> 
              <input type="hidden" name="start_mat" value="<%=startMat%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_mat_composit_id" value="<%=oidMatComposit%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_vendor_id" value="<%=oidVendor%>">
              <input type="hidden" name="hidden_catType" value="<%=catType%>">
              <input type="hidden" name="source_link" value="<%=sourceLink%>">
              <input type="hidden" name="source_link2" value="<%=sourceLink2%>">
              <input type="hidden" name="trans_rate" value="<%=transRate%>">
              <input type="hidden" name="typemenu" value="<%=typemenu%>">
              <% if((sourceLink!=null && sourceLink.equals("materialdosearch.jsp"))||(sourceLink2!=null && sourceLink2.equals("materialdosearch.jsp"))){
                %>
                <input type="hidden" name="mat_vendor" value="<%=mat_vendor%>">
                <input type="hidden" name="currency_id" value="<%=currency_id%>">                
              <% } %>
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE_CURRENCY_ID]%>" value="<%=defaultCurrencyType.getOID()%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>" value="<%=material.getCurrSellPriceRecomentation()%>">
              <table width="100%" border="0">
                <tr>
                  <td width="7%" valign="top">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <%if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.LIST || iCommand==Command.NONE || iCommand==Command.BACK){%>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3">&nbsp;<u><b><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+materialTitle : materialTitle+" List"%></b></u></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE,listMaterial,oidMaterial,startMat)%> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                        <%
                        int cmd = 0;
                        if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
                                (iCommand == Command.NEXT || iCommand == Command.LAST))
                            cmd =iCommand;
                        else{
                            if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                cmd = Command.FIRST;
                            else
                                cmd = prevCommand;
                        }
			%>
			<%
                        ctrLine.setLocationImg(approot+"/images");
                        ctrLine.initDefault();
                        %>
                        <%=ctrLine.drawImageListLimit(cmd,vectSize,startMat,recordToGet)%> 
                          </span>
                        </td>
                      </tr>
                      <%}%>
                      <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.SAVE || (iCommand==Command.DELETE && frmMaterial.errorSize()>0)){%>
                      <tr align="left" valign="top">
                        <td height="18" valign="top" colspan="3">
                          <table width="100%" border="0" cellspacing="0" cellpadding="1">
                            <tr>
                              <td colspan="2" height="20" valign="top" style="background-color: #e8e8e8;">
                                <div align="left"><u></u></div>
                              </td>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <ul class="nav nav-tabs" style="padding: 0 10px; margin-bottom: 20px; background-color: #e8e8e8;">
                                  <li role="presentation" class="active"><a href="#">Main Data</a></li>
                                  <% if(material.getOID()!=0){%>
                                  <%--<li role="presentation"><a href="price_req.jsp?hidden_material_id=<%=material.getOID()%>&typemenu=<%=typemenu%>" style="background-color: #f3f3f3; border: 0; margin-top: 1px; color: #949494;">Price</a></li>--%>
                                        <li role="presentation"><a href="product_req.jsp?hidden_material_id=<%=material.getOID()%>&typemenu=<%=typemenu%>" style="background-color: #f3f3f3; border: 0; margin-top: 1px; color: #949494;">Product Requirements </a></li>
                                  <%}%>
                                </ul>
                              </td>
                            </tr>
                            <tr>
                              <td width="50%" valign="top">
                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                  <%if(material.getOID() != 0){%>
                                  <tr>
                                    <td width="" class="" height="20">Foto Item</td>
                                    <td width=""></td>
                                    <td width="" class="" height="20">
                                        <%
                                            String lokasiAmbilFoto = PstSystemProperty.getValueByName("PATH_IMAGE");
                                            String path = lokasiAmbilFoto + material.getMaterialImage();
                                            String msgUpload = FRMQueryString.requestString(request, "message_upload");
                                            int statusUpload = FRMQueryString.requestInt(request, "status_upload");
                                            //path = "http://localhost:8080/dProchain_20161124/imgcache/";
                                        %>
                                        <% if (material.getMaterialImage() != null && !material.getMaterialImage().isEmpty()) {%>
                                        <img id="output" height="auto" width="150" src="<%=path%>" alt="Foto tidak ditemukan"/>
                                        <%}%>
                                        <p></p>
                                        <span id="msg"><%=msgUpload%></span>
                                        <% if (statusUpload == 1) {%>
                                        <p></p>
                                        Jika foto tidak muncul klik <a href="">disini</a>
                                        <%}%>
                                        <p></p>
                                        <label class="label_button" for="btnBrowse">Pilih foto</label>
                                        <label class="label_button" id="btnUploadFoto">Simpan foto</label>
                                    </td>
                                  </tr>
                                  <%}%>
								  <%if(typeOfBusinessDetail == 2){%>
                                  <tr>
                                    <td width="20%"><a id="firstFocus"></a><%=textListMaterialHeader[SESS_LANGUAGE][34]%></td>
                                    <td width="3%">:</td>
                                    <td width="38%">
                                        <%
                                            Vector vectValue = new Vector(1,1);
                                            Vector vectKey = new Vector(1,1);
                                            
                                            for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                                if (main_material == Material.MATERIAL_TYPE_GENERAL) {continue;}
                                                vectKey.add("" + Material.MATERIAL_TYPE_TITLE[main_material]);
                                                vectValue.add("" + main_material);
                                            }
                                            
                                            int valueSelected = 0;
                                            if (iCommand == Command.ADD) {
                                                valueSelected = itemMaterialType;
                                            } else if (iCommand == Command.EDIT || iCommand == Command.SAVE) {
                                                valueSelected = material.getMaterialJenisType();
                                            }
                                        %>  
                                        <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MATERIAL_JENIS_TYPE],"formElemen", null, ""+valueSelected, vectValue, vectKey, null)%>
                                    </td>                               
                                  </tr>
                                  <%}%>
                                  <tr> 
                                    <td width="20%"><a id="firstFocus"></a><%=textListMaterialHeader[SESS_LANGUAGE][17]%></td>
                                    <td width="3%">:</td>
                                    <td width="38%">
                                    <%
                                    Vector vectValue = new Vector(1,1);
                                    Vector vectKey = new Vector(1,1);
                                    
                                    if(typeOfBusiness.equals("0")){
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_REGULAR]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_REGULAR);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_SERVICE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_SERVICE);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_COMPOSITE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_COMPOSITE);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_PACKAGE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_PACKAGE);
                                    }else{
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_REGULAR]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_REGULAR);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_SERVICE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_SERVICE);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_COMPOSITE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_COMPOSITE);
                                        vectKey.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_PACKAGE]);
                                        vectValue.add(""+PstMaterial.MAT_TYPE_PACKAGE);
                                    }
                                    
                                    %> <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MATERIAL_TYPE],"formElemen", null, ""+material.getMaterialType(), vectValue, vectKey, null)%></td>
                                    <%if(typeOfBusinessDetail != 2){%>
									<!--<td width="39%"><% //if(material.getOID()!=0){%><a href="javascript:uploadPicture()">Upload Picture </a><%//}%></td>-->
                                    <%}%>
                                   
                                  </tr>
                                  <tr>
                                    <td><%=textListMaterialHeader[SESS_LANGUAGE][27]%></td>
                                    <td>:</td>
                                    <td>
                                    <%
                                    vectValue = new Vector(1,1);
                                    vectKey = new Vector(1,1);
                                    
                                    vectKey.add(PstMaterial.strTypeCatalogConsigment[SESS_LANGUAGE][PstMaterial.CATALOG_TYPE_OTHER]);
                                    vectValue.add(""+PstMaterial.CATALOG_TYPE_OTHER);
                                    
                                    vectKey.add(PstMaterial.strTypeCatalogConsigment[SESS_LANGUAGE][PstMaterial.CATALOG_TYPE_CONSIGMENT]);
                                    vectValue.add(""+PstMaterial.CATALOG_TYPE_CONSIGMENT);
                                    int consType = material.getMatTypeConsig();
                                    //cek jika item diinput dari tipe bisnis jewelry
                                    if (iCommand == Command.ADD && typeOfBusinessDetail == 2) {
                                        consType = itemRecType;
                                    }
                                    %>
                                    <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CONSIGMENT_TYPE],"formElemen", null, ""+consType , vectValue, vectKey, null)%></td>
                                    <td width="39%" rowspan="5">&nbsp;
                                        
                                        
                                      <%
                                        if(pictPath2!=null && pictPath2.length()>0){
                                            out.println("<img width=\"100\" src=\""+approot+"/"+pictPath2+"\">");
                                        }
                                      %>
                                    </td>
                                  </tr>
                                  <tr> 
                                    <td width="20%"><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                                    <td width="3%">:</td>
                                    <td width="38%"> <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>" value="<%=oidMaterial!=0?material.getSku():skuCodeOtomatic%>"  size="20" maxlength="10000" >
                                      * <%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_SKU)%></td>
                                  </tr>
                                  <tr> 
                                    <td><%=textListMaterialHeader[SESS_LANGUAGE][2]%></td>
                                    <td>:</td>
                                    <td> <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BARCODE]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BARCODE]%>" value="<%=material.getBarCode()%>"  size="20" maxlength="13" >                                    </td>
                                  </tr>
                                  <tr> 
                                    <td width="20%"><%=kategoriName%></td>
                                    <td width="3%">:</td>
                                    <td width="50%">
                                        <select id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID]%>"  name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
                                    <%
                                    //long matDepartId = material.getCategoryId();
                                    //add opie-eyek 20130821
                                    Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                    Category newCategory = new Category();
                                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;

                                    Vector vectGroupVal = new Vector(1,1);
                                    Vector vectGroupKey = new Vector(1,1);
                                    if(materGroup!=null && materGroup.size()>0) {
                                        String parent="";
                                        Vector<Category> resultTotal= new Vector();
                                        Vector<Long> levelParent = new Vector<Long>();
                                        for(int i=0; i<materGroup.size(); i++) {
                                            Category mGroup = (Category)materGroup.get(i);
                                                String select="";
                                                if(mGroup.getOID()==material.getCategoryId()){
                                                    select="selected";
                                                }
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
                                                <option value="<%=mGroup.getOID()%>" <%=select%>><%=parent%><%= mGroup.getCode()+" - "+mGroup.getName()%></option>
                                            <%
                                        }
                                    } else {
                                        vectGroupVal.add(textListTitleHeader[SESS_LANGUAGE][5]);
                                        vectGroupKey.add("0");
                                    }
                                    %>
                                        </select><a href="javascript:addCategory('<%=material.getOID()%>')"> New <%=kategoriName%></a>   
                                    </td>
                                  </tr>
                                  <%
                                  int useSubCategory = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueIntByName("useSubCategory"));
                                  if(useSubCategory==1){%>
                                  <tr> 
                                    <td width="20%"><%=textListMaterialHeader[SESS_LANGUAGE][32]%></td>
                                    <td width="3%">:</td>
                                    <td width="38%">
                                    <%
                                    String orderSubBy = PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
                                    Vector listMaterialSubcategory = PstSubCategory.list(0,0,"",orderSubBy);
                                    Vector vectSubCatVal = new Vector(1,1);
                                    Vector vectSubCatKey = new Vector(1,1);
                                    if(listMaterialSubcategory!=null && listMaterialSubcategory.size()>0) {
                                        for(int i=0; i<listMaterialSubcategory.size(); i++) {
                                            SubCategory SubCategory = (SubCategory)listMaterialSubcategory.get(i);
                                            vectSubCatVal.add(SubCategory.getName());
                                            vectSubCatKey.add(""+SubCategory.getOID());
                                        }
                                    }
                                    String attTag =  "";
                                    out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUB_CATEGORY_ID],"formElemen", null, ""+material.getSubCategoryId(), vectSubCatKey, vectSubCatVal, attTag));
                                    %> <a href="javascript:addSubCategory('<%=material.getOID()%>')">New <%=textListMaterialHeader[SESS_LANGUAGE][32]%></a>                                      
                                    </td>
                                  </tr>
                                  <%}
                                  %>
                                  <tr> 
                                    <td width="20%"><%=merkName%></td>
                                    <td width="3%">:</td>
                                    <td width="38%">
                                    <%
                                    String orderBy = PstMerk.fieldNames[PstMerk.FLD_NAME];
                                    Vector listMaterialMerk = PstMerk.list(0,0,"",orderBy);
                                    Vector vectMerkVal = new Vector(1,1);
                                    Vector vectMerkKey = new Vector(1,1);
                                    if(listMaterialMerk!=null && listMaterialMerk.size()>0) {
                                        for(int i=0; i<listMaterialMerk.size(); i++) {
                                            Merk matMerk = (Merk)listMaterialMerk.get(i);
                                            vectMerkVal.add(""+matMerk.getCode()+" - "+matMerk.getName());
                                            vectMerkKey.add(""+matMerk.getOID());
                                        }
                                    }
                                    out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MERK_ID],"formElemen", null, ""+material.getMerkId(), vectMerkKey, vectMerkVal, null));
                                    %> <a href="javascript:addMerk('<%=material.getOID()%>')">New <%=merkName%> </a>                               
                                    </td>
                                  </tr>
                                  <tr> 
                                    <td width="20%">
                                        <%if(typeOfBusinessDetail==2){%>
                                            Model
                                        <%}else{%>
                                            <%=textListMaterialHeader[SESS_LANGUAGE][3]%>
                                        <%}%>
                                    </td>
                                    <td width="3%">:</td>
                                    <td width="77%" colspan="2">
                                    <%
                                    material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                                    String goodName = "";
                                    if(nameSpesialRequest.equals("")){
                                        goodName = material.getName();
                                    }else{
                                        goodName = nameSpesialRequest;
                                    }
                                    
                                    String goodName1="";
                                    String goodName2="";
                                    String[] smartPhonesSplits;
                                    smartPhonesSplits = goodName.split("\\;");
                                    if(material.getMaterialSwitchType()==Material.WITH_USE_SWITCH_MERGE_AUTOMATIC){
                                        try{
                                            goodName = goodName.substring(goodName.lastIndexOf(Material.getSeparate())+1,goodName.length());
                                        }catch(Exception e){}
                                    }
                                    
                                    if(multiLanguageName==1){
                                        goodName = "";
                                        try{
                                             goodName = smartPhonesSplits[0];
                                        }catch(Exception ex){}
                                    }
                                    
                                    %> <input type="text" class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>" value="<%=goodName%>"  size="40" maxlength="1000">
                                     <%if(multiLanguageName==1){%>* Eng
                                     <%}%>
                                     <%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_NAME)%></td>
                                  </tr>
                                  <%--for queen tandoor--%>
                                  <%
                                  if(multiLanguageName==1){
                                     goodName1 = "";
                                        try{
                                             goodName1=smartPhonesSplits[1];
                                        }catch(Exception ex){}
                                  %>
                                 
                                  <tr> 
                                    <td width="20%"></td>s
                                    <td width="3%"></td>
                                    <td width="77%" colspan="2">
                                    <%
                                    %> <input type="text" class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_1]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_1]%>" value="<%=goodName1%>"  size="40" maxlength="1000">
                                      * Indo<%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_NAME)%></td>
                                  </tr>
                                  <tr> 
                                    <td width="20%"></td>
                                    <td width="3%">:</td>
                                    <td width="77%" colspan="2">
                                    <%
                                     goodName2= "";
                                        try{
                                             goodName2=smartPhonesSplits[2];
                                        }catch(Exception ex){}
                                    %> <input type="text" class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_2]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME_2]%>" value="<%=goodName2%>"  size="40" maxlength="1000">
                                      * India<%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_NAME)%></td>
                                  </tr>
                                   <%}
                                    %>
                                  <% if (typeOfBusiness.equals("0") && vMapKsg.size() == 0){ %>
                                      <tr>
                                        <td>
                                            <%if(typeOfBusinessDetail==2){%>
                                                Etalase
                                            <%}else{%>
                                                 <%=textListMaterialHeader[SESS_LANGUAGE][28]%>
                                           <%}%>
                                        </td>
                                        <td>:</td>
                                        <td colspan="2">
                                        <%
                                        String whereKsg = "";                                        
                                        if (directPenerimaan != 0) {
                                            whereKsg = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " = '" + oidLocationLitama + "'";
                                        }
                                        Vector listKsg = PstKsg.list(0,0,whereKsg,PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+","+PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                        Vector vectKsgVal = new Vector(1,1);
                                        Vector vectKsgKey = new Vector(1,1);
                                        //vectKsgKey.add("Semua");
                                        //vectKsgVal.add("0");
                                        for(int i=0; i<listKsg.size(); i++){                                            
                                            Ksg matKsg = (Ksg)listKsg.get(i);
                                            Location l = new Location();
                                            if (matKsg.getLocationId() != 0) {
                                                l = PstLocation.fetchExc(matKsg.getLocationId());
                                            }
                                            if (directPenerimaan != 0) {
                                                vectKsgKey.add(matKsg.getCode()+" - "+l.getCode());
                                                vectKsgVal.add(""+matKsg.getOID());
                                            } else if (material.getGondolaCode() == matKsg.getOID()){
                                                vectKsgKey.add(matKsg.getCode()+" - "+l.getCode());
                                                vectKsgVal.add(""+matKsg.getOID());
                                            } else if (material.getGondolaCode() == 0){
                                                vectKsgKey.add(matKsg.getCode()+" - "+l.getCode());
                                                vectKsgVal.add(""+matKsg.getOID());
                                            }else if (typeOfBusinessDetail == 0) {
                                                vectKsgKey.add(matKsg.getCode()+" - "+l.getCode());
                                                vectKsgVal.add(""+matKsg.getOID());
                                            }
                                        }                                        
                                        %> 
                                        <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_GONDOLA_CODE],"formElemen", null, ""+material.getGondolaCode(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%>                                    
                                        * <%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_GONDOLA_CODE)%>
                                        <b style="color: red"><%if (listKsg.isEmpty()){out.print("ETALASE TIDAK DITEMUKAN !");}%></b>
                                        </td>
                                      </tr>
                                  <%}%>
                                  <%-- edit material add opie 12-06-2012 
                                  <%  if(typeOfBusiness.equals("2") || typeOfBusiness.equals("0")){ %>--%>
                                        <tr>
                                              <td width="20%">Edit Material</td>
                                              <td width="3%">:</td>
                                              <td width="38%">
                                              <%
                                            Vector editValue = new Vector(1,1);
                                            Vector editKey = new Vector(1,1);

                                            editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.NO_EDIT]);
                                            editValue.add(""+PstMaterial.NO_EDIT);

                                            editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.EDIT_NAME]);
                                            editValue.add(""+PstMaterial.EDIT_NAME);

                                            editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.EDIT_HARGA]);
                                            editValue.add(""+PstMaterial.EDIT_HARGA);

                                            editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.EDIT_HARGA_NAME]);
                                            editValue.add(""+PstMaterial.EDIT_HARGA_NAME);
                                            
                                            editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.EDIT_NON_AKTIVE]);
                                            editValue.add(""+PstMaterial.EDIT_NON_AKTIVE);

                                            %> 
                                            <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_EDIT_MATERIAL],"formElemen", null, ""+material.getEditMaterial(), editValue, editKey, null)%></td>
                                              </td>
                                          </tr>
                                 <%--<%}%>--%>
                                  <tr> 
                                    <td width="20%"><%=textListMaterialHeader[SESS_LANGUAGE][33]%></td>
                                    <td width="3%">:</td>
                                    <td width="77%" colspan="2">
                                    <textarea id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DESCRIPTION]%>"  name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DESCRIPTION]%>"  cols="30" class="formElemen"><%=material.getMaterialDescription() %></textarea>       
                                      * <%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_DESCRIPTION)%></td>
                                  </tr>
								  <% if((typemenu==1 || ctrlMaterial.DESIGN_MATERIAL_FOR == ctrlMaterial.DESIGN_HANOMAN) && typeOfBusinessDetail == 2){%>  
									<tr>
									  <td width="20%">Sell Location</td>
									  <td width="3%">:</td>
									  <td width="77%" colspan="2">
										  <table width="50%" border="0" cellspacing="0" cellpadding="0">
											  <tr>
												<td><%=drawCheck(selected)%></td>
											  </tr>
										  </table>
									  </td>
									</tr>
								<% } %>
                                  <%
                                      int useMasterGroup = 0;
                                      try {
                                        useMasterGroup = Integer.valueOf(PstSystemProperty.getValueByName("USE_MASTER_GROUP"));
                                      } catch (Exception exc){
                                      }
                                      
                                      Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, 
                                              PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_MATERIAL, "");
                                      
                                        if (listGroupMapping.size()>0){
                                                for (int i=0;i< listGroupMapping.size();i++){
                                                    MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(i);
                                                    MasterGroup masterGroup = new MasterGroup();
                                                    try {
                                                        masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                                                    } catch (Exception exc){}
                                  %> 
                                                    <tr>
                                                        <td width="20%"><%=masterGroup.getNamaGroup()%></td>
                                                        <td width="3%">:</td>
                                                        <td width="77%" colspan="2">
                                                            <%
                                                                Vector vValue = new Vector(1,1);
                                                                Vector vKey = new Vector(1,1);
                                                                
                                                                vValue.add("0");
                                                                vKey.add("-");
                                                                
                                                                Vector listType = PstMasterType.list(0, 0, 
                                                                        PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                                                        PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);
                                                                
                                                                long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(),material.getOID());
                                                                if (listType.size() > 0){
                                                                    for (int x=0; x < listType.size(); x++){
                                                                        MasterType masterType = (MasterType) listType.get(x);
                                                                        vValue.add(""+masterType.getOID());
                                                                        vKey.add(masterType.getMasterName());
                                                                    }
                                                                }
                                                            %>
                                                            <%=ControlCombo.draw("MASTER_GROUP"+masterGroup.getTypeGroup(),"formElemen", null, ""+oidMapping, vValue, vKey, null)%></td>
                                                    </tr>
                                  <%    
                                                }
                                  %>
                                  <input type="hidden" name="groupSize" value="<%=listGroupMapping.size()%>">
                                  <%
                                        }    
                                  %>
                                </table>
                              </td>
                              <td valign="top" width="50%">
                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                  <%if(typeOfBusinessDetail != 2){%>
                                  <tr> 
                                    <td><%=textListMaterialHeader[SESS_LANGUAGE][24]%> </td>
                                    <td>:</td>
                                    <td><input type="checkbox" class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_REQUIRED_SERIAL_NUMBER]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_REQUIRED_SERIAL_NUMBER]%>" value="<%=PstMaterial.REQUIRED%>"
                                    <%if(material.getRequiredSerialNumber()==PstMaterial.REQUIRED){%>checked<%}%>> 
                                    <%out.println("  "+PstMaterial.requiredNames[SESS_LANGUAGE][PstMaterial.REQUIRED]);%> </td>
                                  </tr>
                                  <%}%>
                                  <tr> 
                                    <td>Tampilkan di mobile</td>
                                    <td>:</td>
                                    <td><input type="checkbox" class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_VIEW_IN_SHOPPING_CHART]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_VIEW_IN_SHOPPING_CHART]%>" value="1"
                                    <%if(material.getViewInChart()==1){%>checked<%}%>> </td>
                                  </tr>
                                    <tr> 
                                    <td><%=textListMaterialHeader[SESS_LANGUAGE][35]%> </td>
                                    <td>:</td>
                                    <td>
										<%
										Vector salesValue = new Vector(1,1);
										Vector salesKey = new Vector(1,1);
										
										salesKey.add(PstMaterial.strSalesRule[SESS_LANGUAGE][PstMaterial.SALES_NOT_MANDATORY]);
										salesValue.add(""+PstMaterial.SALES_NOT_MANDATORY);
										
										salesKey.add(PstMaterial.strSalesRule[SESS_LANGUAGE][PstMaterial.SALES_MANDATORY]);
										salesValue.add(""+PstMaterial.SALES_MANDATORY);
										
										salesKey.add(PstMaterial.strSalesRule[SESS_LANGUAGE][PstMaterial.SALES_WITH_CONDITION]);
										salesValue.add(""+PstMaterial.SALES_WITH_CONDITION);

										%>
										<%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SALES_RULE],"formElemen", null, ""+material.getSalesRule(), salesValue, salesKey, null)%></td>
									</td>
                                  </tr>
								  <tr> 
                                    <td><%=textListMaterialHeader[SESS_LANGUAGE][36]%> </td>
                                    <td>:</td>
                                    <td>
										<%
										Vector returnValue = new Vector(1,1);
										Vector returnKey = new Vector(1,1);
										
										returnKey.add(PstMaterial.strSalesReturn[SESS_LANGUAGE][PstMaterial.CAN_RETURN]);
										returnValue.add(""+PstMaterial.CAN_RETURN);
										
										returnKey.add(PstMaterial.strSalesReturn[SESS_LANGUAGE][PstMaterial.CANNOT_RETURN]);
										returnValue.add(""+PstMaterial.CANNOT_RETURN);

										%>
										<%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_RETURN_RULE],"formElemen", null, ""+material.getReturnRule(), returnValue, returnKey, null)%></td>
									</td>
                                  </tr>
                                  <%--update disini --%>
                                  <% if(privShowPrice){%>
                                      <tr>
                                        <td width="27%"><%=textListMaterialHeader[SESS_LANGUAGE][23]%></td>
                                        <td width="2%">:</td>
                                        <td width="71%">
                                            <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MINIMUM_POINT]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MINIMUM_POINT]%>" value="<%=material.getMinimumPoint() %>" style="text-align:right" size="5" maxlength="5" >
                                        &nbsp;&nbsp;<%=textListMaterialHeader[SESS_LANGUAGE][30]%>
                                        &nbsp;&nbsp;:
                                        &nbsp;&nbsp;<input type="text" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_POINT_SALES]%>"  class="formElemen" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_POINT_SALES]%>" value="<%=material.getPointSales() %>" style="text-align:right" size="5" maxlength="5" >
                                        </td>
                                      </tr>
                                      <tr>
                                        <td><%=textListMaterialHeader[SESS_LANGUAGE][7]%></td>
                                        <td>:</td>
                                        <td>
                                        <%
                                        String whereBaseUnit = "";//PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID] + " = 0";
                                        Vector listBaseUnit = PstUnit.list(0,0,whereBaseUnit,"");
                                        Vector vectUnitVal = new Vector(1,1);
                                        Vector vectUnitKey = new Vector(1,1);
                                        long oidview = 0;
                                        for(int i=0; i<listBaseUnit.size(); i++){
                                            Unit mateUnit = (Unit)listBaseUnit.get(i);
                                            vectUnitKey.add(mateUnit.getCode());
                                            vectUnitVal.add(""+mateUnit.getOID());
                                            if(i==0){
                                                if(iCommand==Command.ADD){
                                                    oidview = mateUnit.getOID();
                                                    material.setDefaultStockUnitId(oidview);
                                                }
                                            }
                                        }                                                             ////1                            2           3                 4                          5          6                7
                                        %> <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_STOCK_UNIT_ID],"formElemen", null,""+material.getDefaultStockUnitId(), vectUnitVal, vectUnitKey, "onChange=\"javascript:changeUnit()\"")%> 
                                        <a href="javascript:addUnit('<%=material.getOID()%>')">New <%=textListMaterialHeader[SESS_LANGUAGE][7]%></a>
                                        </td>
                                      </tr>
                                      <%if(material.getOID()!=0){%>  
                                          <tr>

                                            <td><%=textListMaterialHeader[SESS_LANGUAGE][31]%></td>
                                            <td>:</td>
                                            <td>

                                            <%
                                            String whereBaseUnitOrder = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID] + " ='"+material.getOID()+"'";
                                            Vector listBaseUnitOrder = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
                                            for(int i=0; i<listBaseUnitOrder.size(); i++){
                                                MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listBaseUnitOrder.get(i);
                                                %>
                                                <input type="checkbox"  class="formElemen" name="" value="1" checked> <%=materialUnitOrder.getUnitKode()%>
                                                <%
                                            }                                                             ////1                            2           3                 4                          5          6                7
                                                %><b>
                                                    <%if(material.getOID()!=0){%>
                                                    <a href="javascript:addUnitBuy('<%=material.getOID()%>')">Add New</a>
                                                    <%}%>
                                                </b>
                                            </td>
                                          </tr>
                                      <%}%>
                                      
                                      <tr>
                                        <td><%=textListMaterialHeader[SESS_LANGUAGE][20]%></td>
                                        <td>:</td>
                                        <td>
                                        <%
                                        String wherex  = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE;
                                        Vector listCurr = PstCurrencyType.list(0,0,wherex,"");
                                        Vector vectCurrVal = new Vector(1,1);
                                        Vector vectCurrKey = new Vector(1,1);
                                        for(int i=0; i<listCurr.size(); i++){
                                            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                            vectCurrKey.add(currencyType.getCode());
                                            vectCurrVal.add(""+currencyType.getOID());
                                        }
                                        %> 
                                        <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST_CURRENCY_ID],"formElemen", null, ""+material.getDefaultCostCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                                        <%
                                        Unit firstUnit = new Unit();
                                        try{
                                            firstUnit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                                        }catch(Exception e){}

                                        String whereUnit = "";//+PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID]+"="+material.getDefaultStockUnitId();
                                        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
                                        Vector unit_Val = new Vector(1,1);
                                        Vector unit_Key = new Vector(1,1);

                                        unit_Key.add(firstUnit.getCode());
                                        unit_Val.add(""+firstUnit.getOID());
                                        for(int i=0; i<listBuyUnit.size(); i++){
                                            Unit mateUnit = (Unit)listBuyUnit.get(i);
                                            if(firstUnit.getOID()!=mateUnit.getOID()){
                                                unit_Key.add(mateUnit.getCode());
                                                unit_Val.add(""+mateUnit.getOID());
                                            }
                                        }
                                        %> <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID],"formElemen", null, ""+material.getBuyUnitId(), unit_Val, unit_Key, null)%> <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getDefaultCost())%>"  size="15" style="text-align:right">
                                         
                                        </td>
                                      </tr>   
                                      <%if(typeOfBusinessDetail != 2){%>
                                      <tr>
                                        <td></td>
                                        <td></td>
                                        <%
                                             String orderLastReceiveBy = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" DESC ";
                                             String whereLastReceiveBy = "("+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+"=5 OR "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+"=7)";
                                             Vector listLastReceive = PstMatReceiveItem.listLastReceive(oidMaterial,0,1,"RECEIVE_SOURCE IN (0,1) AND "+whereLastReceiveBy,orderLastReceiveBy);
                                        %>
                                        <td nowrap><blink>Receive Terakhir</blink>
                                            <%
                                                for(int i=0; i<listLastReceive.size(); i++){
                                                    MatReceiveItem matreceiveitem = (MatReceiveItem)listLastReceive.get(i);//new MatReceiveItem();
                                                    out.print("<input type='hidden' name='lastreceiveprice' value='"+matreceiveitem.getCost()+"')>");
                                                    %>
                                                    &nbsp;|&nbsp;<b><a href="javascript:viewDocumentReceive('<%=matreceiveitem.getReceiveMaterialId()%>','0')"><%=matreceiveitem.getRecCode()%></a></b>&nbsp;|&nbsp;<%=matreceiveitem.getReceiveDate()%>&nbsp;|&nbsp;<b><%=matreceiveitem.getCost()%></b>&nbsp;|&nbsp;&nbsp;[<a href="javascript:calculateWithLastReceive()" >Set As Harga Beli</a>]&nbsp;&nbsp;[<a href="javascript:showHistoryReceive('<%=matreceiveitem.getMaterialId()%>')" >History</a>]
                                                    <%
                                                }
                                            %>
                                        </td>
                                      </tr>
                                      <%}%>
                                      <tr>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                      </tr>
                                      <%if(typeOfBusinessDetail != 2){%>
                                      <tr>
                                        <td><%=textListMaterialHeader[SESS_LANGUAGE][19]%></td>
                                        <td>:</td>
                                        <td>
                                            <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getLastVat())%>"  onKeyUp="javascript:calculate1" size="5" style="text-align:right">
                                          % &nbsp; <%=textListMaterialHeader[SESS_LANGUAGE][29]%>&nbsp;<input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_COST_CARGO]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_COST_CARGO]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getLastCostCargo())%>"  onKeyUp="javascript:calculate1" size="7" style="text-align:right">

                                        </td>
                                      </tr>
                                      <tr>
                                        <td nowrap ><%=textListMaterialHeader[SESS_LANGUAGE][20]%> + PPN</td>
                                        <td>:</td>
                                        <td nowrap>
                                          <input type="text"  class="formElemen" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getCurrBuyPrice())%>"  readonly size="15" style="text-align:right">
                                          <!--* (automatic <a href="javascript:calculate()">calculate</a>)-->
                                          * (automatic <a href="javascript:calculate1()">calculate</a>)
                                        </td>
                                      </tr>
                                      <tr>
                                        <td width="27%"><%=textListMaterialHeader[SESS_LANGUAGE][22]%></td>
                                        <td width="2%">:</td>
                                        <td width="71%">
                                          <input type="text" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>"  class="formElemen" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getProfit())%>"  size="7" onKeyUp="javascript:calculate()" style="text-align:right">
                                          % </td>
                                      </tr>
                                      <%}%>
                                      <tr>
                                        <td><%=textListMaterialHeader[SESS_LANGUAGE][25]%> (<%=defaultCurrencyType.getCode()%>)</td>
                                        <td>:</td>
                                        <%
                                        double averagePrice = material.getAveragePrice();
                                            averagePrice = Math.round(averagePrice);
                                        %>
                                        <td> <input type="text" id="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>"  class="formElemen" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_AVERAGE_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(averagePrice)%>"  size="15" style="text-align:right">
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>Supplier</td>
                                        <td>:</td>
                                        <td>
                                            <%
                                            SrcMemberReg objSrcMemberReg = new SrcMemberReg();
                                            Vector vSupplier = SessMemberReg.searchSupplier(objSrcMemberReg, 0, 0);
                                            Vector supplier_Val = new Vector(1,1);
                                            Vector supplier_Key = new Vector(1,1);
                                            supplier_Val.add("0");
                                            supplier_Key.add("-");    
                                            for(int i=0; i<vSupplier.size(); i++){
                                                ContactList contactList = (ContactList) vSupplier.get(i);
                                                supplier_Val.add(""+contactList.getOID());
                                                supplier_Key.add(""+contactList.getContactCode()+" - "+contactList.getCompName());
                                            }
                                            long supplierSelected=0;
                                            if(material.getSupplierId()==0){
                                                supplierSelected=supplierId;
                                            }else{
                                                supplierSelected=material.getSupplierId();
                                            }
                                            %>
                                            <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUPPLIER_ID],"formElemen", null, ""+supplierSelected, supplier_Val, supplier_Key, null)%> 
                                        </td>
                                      </tr>
                                      <%if(typeOfBusinessDetail==2){%>
                                        <tr>
                                          <td>Kepemilikan</td>
                                          <td>:</td>
                                          <td>
                                              <%  
                                              objSrcMemberReg = new SrcMemberReg();
                                              Vector vKepemilikan = SessMemberReg.searchKepemilikan(objSrcMemberReg, 0, 0);
                                              Vector kepemilikan_Val = new Vector(1,1);
                                              Vector kepemilikan_Key = new Vector(1,1);
                                              kepemilikan_Val.add("0");
                                              kepemilikan_Key.add("-");    
                                              for(int i=0; i<vKepemilikan.size(); i++){
                                                  ContactList contactList = (ContactList) vKepemilikan.get(i);
                                                  kepemilikan_Val.add(""+contactList.getOID());
                                                  String name = contactList.getCompName().equals("") ? contactList.getPersonName() : contactList.getCompName();
                                                  kepemilikan_Key.add(""+contactList.getContactCode()+" - "+ name);
                                              }    
                                              long kepemilikanSelected=0;
                                              if(material.getKepemilikanId()==0){
                                                  kepemilikanSelected=kepemilikanId;
                                              }else{
                                                  kepemilikanSelected=material.getKepemilikanId();
                                              }
                                              %>
                                              <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_KEPEMILIKAN],"formElemen", null, ""+kepemilikanSelected, kepemilikan_Val, kepemilikan_Key, null)%> 
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>Kadar</td>
                                          <td>:</td>
                                          <td>
                                              <%  
                                              Vector vKadar = PstKadar.listAll();
                                              Vector kadar_Val = new Vector(1,1);
                                              Vector kadar_Key = new Vector(1,1);
                                              //kadar_Val.add("0");
                                              //kadar_Key.add("-");    
                                              for(int i=0; i<vKadar.size(); i++){
                                                  Kadar kadar = (Kadar) vKadar.get(i);
                                                  kadar_Val.add(""+kadar.getOID());
                                                  kadar_Key.add(""+kadar.getKodeKadar()+" - "+kadar.getKadar()+" - "+kadar.getKarat());
                                              }    
                                              long kadarIdSelected=0;
                                              if(material.getPosKadar()==0){
                                                  kadarIdSelected=kepemilikanId;
                                              }else{
                                                  kadarIdSelected=material.getPosKadar();
                                              }
                                              %>
                                              <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_KADAR],"formElemen", null, ""+kadarIdSelected, kadar_Val, kadar_Key, null)%> 
                                          </td>
                                        </tr>
                                      <%}%>
                                  <%}%>
                                  <tr>
                                    <td>Warna</td>
                                    <td>:</td>
                                    <td>
                                        <%  
                                        Vector vWarna = PstColor.list(0,0,"",PstColor.fieldNames[PstColor.FLD_COLOR_CODE]);
                                        Vector warna_Val = new Vector(1,1);
                                        Vector warna_Key = new Vector(1,1);
                                        //warna_Val.add("0");
                                        //warna_Key.add("-");    
                                        for(int i=0; i<vWarna.size(); i++){
                                            Color color = (Color) vWarna.get(i);
                                            warna_Val.add(""+color.getOID());
                                            warna_Key.add(""+color.getColorCode()+" - "+color.getColorName());
                                        }      
                                        long warnaIdSelected=0;
                                        if(material.getPosColor()==0){
                                            warnaIdSelected=kepemilikanId;
                                        }else{
                                            warnaIdSelected=material.getPosColor();
                                        }
                                        %>
                                        <%=ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_WARNA],"formElemen", null, ""+warnaIdSelected, warna_Val, warna_Key, null)%> 
                                    </td>
                                  </tr>
                                  <tr>
                                    <td>Etalase</td>
                                    <td>:</td>                             
                                    <td>
                                        <table width="100%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                                            <tr>
                                                <td class="listgentitle">Location</td>
                                                <td class="listgentitle">Etalase</td>
                                            </tr>
                                            <%
                                                String whereMapLoc = PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID]+"="+oidMaterial;
                                                Vector listMapLoc = PstMatMappLocation.list(0, 0, whereMapLoc, "");
                                                for (int i=0; i < listMapLoc.size(); i++){
                                                    MatMappLocation mapLoc = (MatMappLocation) listMapLoc.get(i);
                                                    Location locStore = PstLocation.fetchExc(mapLoc.getLocationId());
                                                    Location locWh = new Location();
                                                    try {
                                                        locWh = PstLocation.fetchExc(locStore.getParentLocationId());
                                                    } catch (Exception exc){}
                                                    String whereEtalase = "KSG."+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"="+locStore.getParentLocationId()
                                                                        + " AND MAP."+PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID]+"="+oidMaterial;
                                                    Vector listEtalaseLoc = PstMatMappKsg.listJoinKsg(0, 0, whereEtalase, "");
                                                    if (listEtalaseLoc.size()>0){
                                                        %>
                                                        <tr>
                                                            <td class="listgensell" <%=(listEtalaseLoc.size()>1 ? "rowspan='"+listEtalaseLoc.size()+">'" : "")%>><%=locWh.getName()%>      
                                                        <%
                                                        for (int x=0; x < listEtalaseLoc.size();x++){
                                                            MatMappKsg mapKsg = (MatMappKsg) listEtalaseLoc.get(x);
                                                            Ksg etalase = new Ksg();
                                                            try {
                                                                etalase = PstKsg.fetchExc(mapKsg.getKsgId());
                                                            } catch (Exception exc){}
                                                            if (x==0){
                                                                %>
                                                                    <td class="listgensell"><%=etalase.getName()%></td></tr>
                                                                <%
                                                            } else {
                                                                %>
                                                                    <tr><td class="listgensell"><%=etalase.getName()%></td></tr>
                                                                <%
                                                            }
                                                        }
                                                        %>
                                                        </tr>
                                                        <%
                                                    }
                                                    
                                                }
                                            %>
                                        </table>    
                                    </td>
                                  </tr>
                                  <%--update disini --%>
                                  <tr> 
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                  </tr>
                                  <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <%if(typeOfBusinessDetail==2){%>
                                <tr>
                                  <td valign="top">
                                      <%
                                        double qty = 0;
                                        if (iCommand == Command.ADD) {
                                            qty = 1;
                                        } else {
                                            qty = materialDetail.getQty();
                                        }
                                      %>
                                      <b>QTY &nbsp;  </b> <input type="text" id="qty_stok_satuan"  class="formElemen" name="qty_stok_satuan" value="<%=String.format("%,.0f",qty)%>"  size="15" style="text-align:right"> 
                                      &nbsp;&nbsp;
                                      <b>Berat &nbsp;  </b> <input type="text" id="qty_stok_berat"  class="formElemen" name="qty_stok_berat" value="<%=String.format("%,.3f",materialDetail.getBerat())%>"  size="15" style="text-align:right"> 
                                      &nbsp;&nbsp;
                                  </td>
                                  <%if(iCommand != Command.ADD){%>
                                  <td valign="top">
                                      <% if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS || material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                                      <b>Ongkos / Batu &nbsp;:&nbsp;</b> <input type="text" id="ongkos"  class="formElemen" name="ongkos" value="<%=String.format("%,.2f",materialDetail.getOngkos())%>"  size="10" style="text-align:right">                                
                                      &nbsp;&nbsp;                                      
                                      <b>Ongkos &nbsp;:&nbsp;</b> <input type="text" id="harga_jual"  class="formElemen" name="harga_jual" value="<%=String.format("%,.2f",materialDetail.getHargaJual())%>"  size="10" style="text-align:right" onkeyup="faktorJualChange(this.value)">                                                                            
                                      &nbsp;&nbsp;
                                      <%} else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){%>                                      
                                      <b>Harga Pokok &nbsp;:&nbsp;</b> <input type="text" id="harga_beli"  class="formElemen" name="harga_beli" value="<%=String.format("%,.2f",materialDetail.getHargaBeli())%>"  size="10" style="text-align:right" onkeyup="faktorJualChange(this.value)">                                       
                                      &nbsp;&nbsp;                                      
                                      <b>Harga Jual &nbsp;:&nbsp;</b> <input type="text" id="harga_jual"  class="formElemen" name="harga_jual" value="<%=String.format("%,.2f",materialDetail.getHargaJual())%>"  size="10" style="text-align:right" onkeyup="faktorJualChange(this.value)">                                                                            
                                      &nbsp;&nbsp;
                                      <%}%>
                                                                              
                                      <%
                                        double rateJual = 0;
                                        Vector<RateJualBerlian> listRateJual = PstRateJualBerlian.list(0, 0, "" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_STATUS_AKTIF] + " = 0", "");
                                        if (!listRateJual.isEmpty()) {
                                            rateJual = listRateJual.get(0).getRate();
                                        }
                                      %>
                                      <b>Rate &nbsp;:&nbsp;</b> <input type="text" id="rate_penjualan"  class="formElemen" name="rate_penjualan" value="<%=String.format("%,.2f",rateJual)%>"  size="10" style="text-align:right" onkeyup="changeRateJual(this.value)">                                                                  
                                  </td>
                                  <%}%>
                                </tr>
                                <%if(iCommand != Command.ADD){%>
                                <tr>
                                  <td valign="top"></td>
                                  <td valign="top">
                                      <b>Faktor Jual &nbsp;:&nbsp;</b> <input type="text" id="faktor_jual"  class="formElemen" readonly="" name="faktor_jual" value="<%=String.format("%,.2f",materialDetail.getFaktorJual())%>"  size="10" style="text-align:right">                                                                                                        
                                  </td>
                                </tr>
                                <% if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {%>
                                <tr>
                                  <td valign="top"></td>
                                  <td valign="top">
                                    <b>UP ACC &nbsp;:&nbsp;</b> % &nbsp;<input type="text" id="uphet_persentase"  class="formElemen" name="uphet_persentase" value="<%=String.format("%,.2f",materialDetail.getUphetPersentase())%>"  size="10" style="text-align:right" onkeyup="faktorJualChange(this.value)">
                                    &nbsp;&nbsp;Rp. <input type="text" id="uphet_value"  class="formElemen" name="uphet_value" value="<%=String.format("%,.2f",materialDetail.getUphetValue())%>"  size="10" style="text-align:right">
                                    <%                                                                                
                                        double hh = 0;
                                        double upAcc = 0; 
                                        if (materialDetail.getFaktorJual() > 0) {
                                            hh = materialDetail.getHargaBeli() * (materialDetail.getUphetPersentase()/100);
                                            upAcc = materialDetail.getHargaBeli() + hh;
                                        }
                                    %>
                                    &nbsp;&nbsp;=&nbsp;&nbsp; Rp. <input type="text" id="uphet_value_2"  class="formElemen" name="uphet_value_2" value="<%=String.format("%,.2f",upAcc)%>"  size="10" style="text-align:right">                               
                                  </td>
                                </tr>
                                <tr>
                                  <td valign="top"></td>
                                  <td valign="top">
                                    <b>UP HET &nbsp;:&nbsp;</b> % &nbsp;&nbsp;<input type="text" id="uphet_persentase_total"  class="formElemen" name="uphet_persentase_total" value="30"  size="10" style="text-align:right">
                                    &nbsp;&nbsp;Rp. <input type="text" id="uphet_value_total"  class="formElemen" name="uphet_value_total" value="<%=String.format("%,.2f",materialDetail.getUphetValueTot())%>"  size="10" style="text-align:right">
                                    <%   
                                        double persenHet = 30;
                                        double upHet = 0;
                                        if (materialDetail.getFaktorJual() > 0) {
                                            upHet = upAcc + (upAcc * (persenHet/100));
                                        }
                                    %>
                                    &nbsp;&nbsp;=&nbsp;&nbsp; Rp. <input type="text" id="uphet_value_total_2"  class="formElemen" name="uphet_value_total_2" value="<%=String.format("%,.2f",upHet)%>"  size="10" style="text-align:right">                                                               
                                  </td>
                                </tr>
                                <tr>
                                  <td valign="top"></td>
                                  <td valign="top">
                                    <%
                                        double koreksi = 0;
                                        if (materialDetail.getFaktorJual() > 0) {
                                            koreksi = materialDetail.getHargaJual() - upHet;
                                        }
                                    %>
                                    <b>Koreksi &nbsp;:&nbsp;</b> % &nbsp;&nbsp;<input type="text" id="koreksi"  class="formElemen" name="koreksi" value="<%=String.format("%,.2f",koreksi)%>"  size="10" style="text-align:right">                                                               
                                  </td>
                                </tr>
                                <%}%>
                                <%}%>
                                <tr>
                                  <td valign="top">&nbsp;</td>
                                  <td valign="top">&nbsp;</td>
                                </tr>
                            <%}%>    
                            <tr>
                              <td valign="top">&nbsp;</td>
                              <td valign="top">&nbsp;</td>
                            </tr>
                            <%
                            if(privEditPrice && privShowPrice) {
                                  if(oidMaterial!=0) {
                             %>
                            <tr>
                              <td valign="top"><b><u>Daftar Tipe Harga</u></b></td>
                              <td valign="top"><b><u>Daftar Tipe Potongan</u></b></td>
                            </tr>
                            <tr>
                              <!-- price type -->
                              <td valign="top">
                                <table width="100%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                                  <%
                                  try{
                                      if(listCurrStandard!=null && listCurrStandard.size()>0) {
                                  %>
                                  <tr>
                                    <td class="listgentitle">Tipe Harga</td>
                                    <%
                                    for(int i=0;i<listCurrStandard.size();i++){
                                        Vector temp = (Vector)listCurrStandard.get(i);
                                        CurrencyType currencyType = (CurrencyType)temp.get(0);
                                        StandartRate sRate = (StandartRate)temp.get(1);
                                        
                                    %>
                                    <td class="listgentitle">
                                      Profit
                                    </td>
                                    <td class="listgentitle">
                                      <%=(currencyType.getCode()+" = "+FRMHandler.userFormatStringDecimal(sRate.getSellingRate()))%>
                                    </td>
                                    <%}%>
                                  </tr>
                                 
                                  <%}
                                  } catch(Exception e) {
                                      System.out.println("err : "+e.toString());
                                  }
                                  
                                  if(listPriceType!=null&&listPriceType.size()>0){
                                      for(int i=0;i<listPriceType.size();i++){
                                          PriceType prType = (PriceType)listPriceType.get(i);
                                  %>
                                  <tr>
                                    <td width="10%" class="listgensell"><%=prType.getCode()%></td>
                                    <%
                                    String readOnly = "readonly";
                                    for(int j=0;j<listCurrStandard.size();j++){
                                        double valuePriceTotal=0;
                                        StandartRate sRate=null;
                                        Vector temp= null;
                                        
                                        try{
                                        temp = (Vector)listCurrStandard.get(j); 
                                        sRate = (StandartRate)temp.get(1);
                                        //Cash Formula
//                                        valuePrice1 = material.getCurrBuyPrice() + (material.getCurrBuyPrice() * category.getKenaikanHarga() / 100);
//                                        valuePrice2 = valuePrice1 + (valuePrice1 * 100 / 100);
//                                        valuePriceTotal = valuePrice2 - (valuePrice2 * 35 / 100);
                                          if (useForRaditya.equals("1")) {
                                            double price = 0;
                                            double itemPrice = 0;
                                            String formulaCash = PstSystemProperty.getValueByName("CASH_FORMULA");
                                            Category cat = new Category();
                                              cat = PstCategory.fetchExc(material.getCategoryId());
                                              if (PstPriceTypeMapping.checkString(formulaCash, "HPP") > -1) {
                                                formulaCash = formulaCash.replaceAll("HPP", "" + material.getAveragePrice());
                                              }
//                                              if (PstPriceTypeMapping.checkString(formulaCash, "INCREASE") > -1) {
//                                                formulaCash = formulaCash.replaceAll("INCREASE", "" + cat.getKenaikanHarga());
//                                              }
                                              price += PstPriceTypeMapping.getValue(formulaCash);
                                              price = Math.round(price);
                                              valuePriceTotal = price;
                                          }else {
                                        valuePriceTotal = PstPriceTypeMapping.getPrice(listPriceMapping,oidMaterial,prType.getOID(),sRate.getOID());
                                        }
                                    
                                        if(i==0 && j==0)
                                            readOnly = "";
                                        else
                                            readOnly = "readonly";
                                        }catch(Exception exc){
                                            System.out.println(">>listCurrStandard-PstPriceTypeMapping : "+exc);
                                        }
                                         String cfont="";
                                        String planMargin="";
                                         if(material.getCurrBuyPrice()>0){
                                            if(((valuePriceTotal-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice())-0.5 > material.getProfit()){
                                                       cfont ="<font class=\"msgquestion\">";
                                                       planMargin = "";
                                             } else {
                                                    if(((valuePriceTotal-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice())+0.5 < material.getProfit()){
                                                                           cfont ="<font class=\"msgerror\">";
                                                                           planMargin = "";
                                                                     } else {
                                                                         cfont ="<font>";
                                                                     }
                                             }
                                        %>
                                        <td width="10%" class="listgensell"><%=cfont%>(<%=material.getCurrBuyPrice()>0 ? Formater.formatNumber((valuePriceTotal-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice(),"###.#") :""%>)%</font></td>
                                        <%}else{%>
                                        <td width="10%" class="listgensell"><%=cfont%>(<%=material.getCurrBuyPrice()>0 ? Formater.formatNumber( (valuePriceTotal-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice(),"###.#") :""%>)%</font></td>
                                        <%}
                                    %>
                                    <td width="10%" align="right" class="listgensell testing">
                                      <input type="text" name="price_<%=i%><%=j%>" size="15" value="<%=(valuePriceTotal>=0.0 ? FRMHandler.userFormatStringDecimal(valuePriceTotal) : "") %>" class="formElemen" style="text-align:right" onKeyUp="javascript:cmdKeyUpPriceType(this)">
                                      &nbsp;
                                    </td>
                                    <% 
                                       javascriptPrice=javascriptPrice + ",price_"+i+j;
                                       javascriptPricex=javascriptPricex +  ",price_"+i+j+":focus";
                                    } %>
                                  </tr>
                                  <% }}

                                %>
                                </table>
                              </td>
                              <!-- end price type-->
                              <!-- start discount type-->
                              <td valign="top">
                                <table width="100%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                                  <% if(listCrType!=null&&listCrType.size()>0) { %>
                                  <tr>
                                    <td class="listgentitle">Tipe Potongan</td>
                                    <%
                                    for(int i=0;i<listCurrStandard.size();i++) {
                                        Vector temp = (Vector)listCurrStandard.get(i);
                                        CurrencyType currType = (CurrencyType)temp.get(0);
                                    %>
                                    <td align="center" nowrap class="listgentitle"><%=currType.getCode()%>(%)</td>
                                    <td align="center" nowrap class="listgentitle"><%=currType.getCode()%>(Amount)</td>
                                    <% } %>
                                  </tr>
                                  <% } %>
                                  <%
                                  if(listDscType!=null&&listDscType.size()>0) {
                                      
                                      for(int i=0;i<listDscType.size();i++) {
                                          DiscountType dscType = (DiscountType)listDscType.get(i);
                                  %>
                                  <tr>
                                    <td width="10%" class="listgensell"><%=dscType.getCode()%></td>
                                    <%
                                    for(int j=0;j<listCurrStandard.size();j++) {
                                        try{
                                        String readOnly = "readonly";
                                        String readOnly2 = "readonly";
                                        Vector temp2 = (Vector)listCurrStandard.get(j);
                                        CurrencyType cType = (CurrencyType)temp2.get(0);
                                        
                                        //CurrencyType cType = (CurrencyType)listCrType.get(j);
                                        String value = "";
                                        String valuePct = "";
                                        try{
                                        Vector listValue = PstDiscountMapping.getDiscount(listDiscMapping,oidMaterial,dscType.getOID(),cType.getOID());
                                        if(listValue.size()==2){
                                            value = FRMHandler.userFormatStringDecimal(Double.parseDouble((String)listValue.get(1)));
                                            valuePct = FRMHandler.userFormatStringDecimal(Double.parseDouble((String)listValue.get(0)));//(String)listValue.get(0);
                                        }
                                        } catch(Exception exc){
                                            System.out.println(">>PstDiscountMapping-getDiscount : " +exc);
                                        }
                                        if(i>0 && j>0) {
                                            readOnly = "readonly";
                                            readOnly2 = "readonly";
                                        } else {
                                            readOnly = "";
                                            readOnly2 = "readonly";
                                        }
                                        String bgcolor = (readOnly.length() == 0) ? "white" : "lightgrey";
                                        String bgcolor2 = (readOnly2.length() == 0) ? "white" : "lightgrey";
                                        
                                        
                                       javascriptPrice=javascriptPrice + "\",discount_1\""+i+j;
                                       javascriptPricex=javascriptPricex +"\",discount_1"+i+j+":focus";
                                       javascriptPrice=javascriptPrice + ",discount_2"+i+j;
                                       javascriptPricex=javascriptPricex + ",discount_2"+i+j+":focus"; 
                                    %>
                                    <td width="10%" align="right" class="listgensell">
                                      <input type="text" <%=readOnly%> name="discount_1<%=i%><%=j%>" size="3" class="formElemen" value="<%=valuePct%>"  onKeyUp="javascript:cmdKeyUpPriceDiscType(this)" style="text-align:right; background-color: <%= bgcolor %>">
                                    </td>
                                    <td width="10%" align="right" class="listgensell">
                                      <input type="text" <%=readOnly2%> name="discount_2<%=i%><%=j%>" size="10" class="formElemen" value="<%=value%>" style="text-align:right; background-color: <%= bgcolor2 %>">
                                    </td>
                                    <%
                                    }catch(Exception exc){System.out.println(exc);}
                                    }  %>
                                  </tr>
                                  <% } } %>
                                </table>
                                </td>
                              <!-- end discount type-->
                            </tr>
                            <tr>
                              <td colspan="2" valign="top"><table width="100%"  border="0">
                                  <tr>
                                      <td width="80%" valign="top" nowrap><table width="100%"  border="0">					  
                                        <tr>
                                          <td>
                                              <table width="60%"  border="0" cellspacing="0" cellpadding="0">
                                              <tr> 
                                                <td width="35%"><strong>Minimum Stock <br>
                                                  Location</strong></td>
                                                <td width="20%" align="center" valign="middle"><strong>&nbsp;<br>
                                                  Real Stock</strong></td>
                                                <td width="20%" align="center" valign="middle"><strong>&nbsp;<br>
                                                  Min. Stock</strong></td>
                                                <td width="25%" align="center" valign="middle"><strong>&nbsp;<br>
                                                 Safety. Stock</strong></td>
                                              </tr>
                                              <%
                                              String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+material.getOID()+
                                                      " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+maPeriode.getOID();
                                              if(vlocation!=null && vlocation.size()>0){
                                                  for(int k =0;k<vlocation.size();k++){
                                                      Location location = (Location)vlocation.get(k);
                                                      Vector list = PstMaterialStock.list(0,0,where + " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+location.getOID(),"");
                                                      MaterialStock materialStock = new MaterialStock();
                                                      Vector listserial = new Vector();
                                                      
                                                      Date dateNow = new Date();
                                                      double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), location.getOID(), dateNow, 0);
                                                      if(list!=null && list.size()>0){
                                                          materialStock = (MaterialStock)list.get(0);
                                                         /* if(materialStock.getQty() > 0){
                                                              String wherexx = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+location.getOID()+
                                                                      " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+material.getOID()+
                                                                      " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
                                                              listserial = PstMaterialStockCode.list(0,0,wherexx,"");
                                                          }*/
                                                      }
                                              %>
                                              <tr> 
                                                <td width="35%"><%=location.getName()%></td>
                                                <td width="20%" nowrap><div align="center">
                                                        <a  href="javascript:viewSerialCode('<%=material.getOID()%>','<%=location.getOID()%>')"><%=stokSisa%>&nbsp;
                                                <%
                                                Unit unitStok = PstUnit.fetchExc(material.getDefaultStockUnitId());
                                                out.println(unitStok.getCode());
                                                %></a>
                                                <br>
                                                    <%//=parserSerialCode(listserial)%>
                                                    </div>
                                                </td>
                                                <td width="20%"><span class="formElemen"> 
                                                  <div align="center"> 
                                                    <input align="right" name="<%=location.getOID()%>" value="<%=materialStock.getQtyMin()%>" class="formElemen" type="text" size="5">
                                                    <%=(unitStok.getCode())%>
                                                  </div>
                                                  </span>
                                                </td>
                                                <td width="25%"><span class="formElemen"> 
                                                  <div align="center"> 
                                                    <input align="right" name="optimum_<%=location.getOID()%>" value="<%=materialStock.getQtyOptimum()%>" class="formElemen" type="text" size="5">
                                                    <%=(unitStok.getCode())%>
                                                  </div>
                                                  </span>
                                                </td>
                                              </tr>
                                              <%}}%>
                                            </table></td>
                                        </tr>
                                        <% if(typemenu==1 || ctrlMaterial.DESIGN_MATERIAL_FOR == ctrlMaterial.DESIGN_HANOMAN){%>  
                                            <tr>
                                              <td><% if (ctrlMaterial.DESIGN_MATERIAL_FOR == ctrlMaterial.DESIGN_HANOMAN) { %>
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                  <tr>
                                                    <td width="15%">&nbsp;</td>
                                                    <td width="1%">&nbsp;</td>
                                                    <td width="84%">&nbsp;</td>
                                                  </tr>
                                                  <tr>
                                                    <td width="15%">Sell Location</td>
                                                    <td width="1%">:</td>
                                                    <td width="84%">
                                                      <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                                        <tr>
                                                          <td><%=drawCheck(selected)%></td>
                                                        </tr>
                                                    </table></td>
                                                  </tr>
                                                  <tr>
                                                    <td width="15%">&nbsp;</td>
                                                    <td width="1%">&nbsp;</td>
                                                    <td width="84%">&nbsp;</td>
                                                  </tr>
                                                </table>
                                                <% } } %>
                                              </td>
                                            </tr>
                                        <%}%>
                                        <tr>
                                          <td>
                                          <% if(iCommand==Command.BACK || iCommand==Command.NONE || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {%>
                                            <table width="30%" border="0" cellspacing="2" cellpadding="3">
                                              <tr>
                                                <% if(privAdd) { %>
                                                <td width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1002','','<%=approot%>"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%>" name="Image1002" width="24" height="24" border="0" id="Image1002"></a></td>
                                                <td nowrap width="93%"><%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></td>
                                                <% } %>
                                              </tr>
                                            </table>
                                            <a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></a>
                                            <% } %>
                                          </td>
                                        </tr>
                                        <tr>
                                            <td> <table><tr><td width="70%"><%
                                          ctrLine.setLocationImg(approot+"/images");
                                          
                                          // set image alternative caption
                                          ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));
                                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");
                                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
                                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));
                                          
                                          ctrLine.initDefault(SESS_LANGUAGE,materialTitle);
                                          ctrLine.setTableWidth("100%");
                                          String scomDel = "javascript:cmdAsk('"+oidMaterial+"')";
                                          String sconDelCom = "javascript:cmdConfirmDelete('"+oidMaterial+"')";
                                          String scancel = "javascript:cmdEdit('"+oidMaterial+"')";
                                          ctrLine.setCommandStyle("command");
                                          ctrLine.setColCommStyle("command");
                                          
                                          // set command caption
                                          ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));

                                          String cmdSelectToLgr ="";
                                          if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){
                                             if(material==null){
                                                  material= new Material();
                                                 }
                                            //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");

                                            ctrLine.setBackCaption("Back To Search");
                                            ctrLine.setBackCommand("javascript:cmdCheck()");


                                            String name = "";
                                            name = material.getName();
                                            name = name.replace('\"','`');
                                            name = name.replace('\'','`');
                                            //added by dewok 2018 for jewelry
                                            if(typeOfBusinessDetail == 2) {                                                
                                                Category cat = new Category();
                                                Color color = new Color();
                                                try {
                                                    cat = PstCategory.fetchExc(material.getCategoryId());
                                                    color = PstColor.fetchExc(material.getPosColor());
                                                } catch (Exception e) {

                                                }
                                                if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                                    name = "" + cat.getName() + " " + color.getColorName() + " " + material.getName();
                                                } else if (material.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                                    name = "" + cat.getName() + " " + color.getColorName() + " Berlian " + material.getName();
                                                }
                                            }
                                            Unit unit = new Unit();
                                            try{unit = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}
                                            
                                            Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
                                                        double standardRate = 0;
                                            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}
                                            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
                                            try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}

                                            MatCurrency matCurr = new MatCurrency();
                                            try{matCurr = PstMatCurrency.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e1){}

                                            cmdSelectToLgr ="javascript:cmdSelectItemToReceive('"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                                            "','"+(defaultCost >0.00 ? ""+FRMHandler.userFormatStringDecimal(defaultCost) : "")+"','"+unit.getOID()+
                                            "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"')";

                                          }
                                          //select To Purchasing
                                          String cmdSelectToPch ="";
                                          if(sourceLink2!=null && sourceLink2.equals("materialdosearch.jsp")){
                                             if(material==null){
                                                 material= new Material();
                                                }
                                            //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");

                                            ctrLine.setBackCaption("Back To Search");
                                            ctrLine.setBackCommand("javascript:cmdCheck()");


                                            String name = "";
                                            name = material.getName();
                                            name = name.replace('\"','`');
                                            name = name.replace('\'','`');
                                            Unit unit = new Unit();
                                            try{unit = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}

                                            Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
                                                        double standardRate = 0;
                                            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}
                                            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
                                            try{defaultCost = material.getDefaultCost();}catch(Exception exc){;}

                                            MatCurrency matCurr = new MatCurrency();
                                            try{matCurr = PstMatCurrency.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e1){}
                                            
                                            MatVendorPrice matVendorPrice = new MatVendorPrice();
                                            try{matVendorPrice = PstMatVendorPrice.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e2){}

                                            cmdSelectToPch ="javascript:cmdSelectItemToPurchasing('"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                                            "','"+(defaultCost >0.00 ? ""+FRMHandler.userFormatStringDecimal(defaultCost) : "")+"','"+unit.getOID()+
                                            "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"')";
                                          }

                                          ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
                                          ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_DELETE,true));
                                          ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));
                                          
                                          if (privDelete){
                                              ctrLine.setConfirmDelCommand(sconDelCom);
                                              ctrLine.setDeleteCommand(scomDel);
                                              ctrLine.setEditCommand(scancel);
                                          }else{
                                              ctrLine.setConfirmDelCaption("");
                                              ctrLine.setDeleteCaption("");
                                              ctrLine.setEditCaption("");
                                          }
                                          
                                          if(privAdd == false  && privUpdate == false) {
                                              ctrLine.setSaveCaption("");
                                          }
                                          
                                          if (privAdd == false) {
                                              ctrLine.setAddCaption("");
                                          }
                                          
                                          if(iCommand==Command.SAVE && frmMaterial.errorSize()==0) {
                                              iCommand=Command.EDIT;
                                              editWithoutList = true;
                                          }
                                          
                                          if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.SAVE || (iCommand==Command.DELETE && frmMaterial.errorSize()>0)) {
                                              out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                                          }
                                          %> </td>
                                          <td> &nbsp;<%

                                          if(material.getOID()!=0 && cmdSelectToLgr!=null && cmdSelectToLgr.length()>0){
                                          %>

                                          <a href="<%=cmdSelectToLgr%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="Select to Receive%>" name="Image1003" width="24" height="24" border="0" id="Image1003">Select to Receive</a>
                                          <%}%>

                                          <%//add Menu
                                          if(material.getOID()!=0 && cmdSelectToPch!=null && cmdSelectToPch.length()>0){
                                          //if(material.getOID()!=0){
                                          %>

                                          <a href="<%=cmdSelectToPch%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="Select to Purchasing%>" name="Image1003" width="24" height="24" border="0" id="Image1003">Select to Purchasing</a>
                                          <%}%>
                                          </td>
                                          </tr>
                                              </table>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                          <% if(oidMaterial!=0) { %><table width="64%" border="0" cellspacing="2" cellpadding="3">
                                            <tr><br>
                                              <% if(privAdd) { %>
                                              <!--td width="5%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%>" name="Image1003" width="24" height="24" border="0" id="Image1003"></a></td-->
                                              <td nowrap width="45%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></a></td>
                                              <% } %>
                                              <%
                                                  if(sourceCatalog.equals("katalog")){
                                              %>
                                              <td nowrap width="45%"><a class="btn btn-primary" href="<%=approot%>/marketing_management/add_katalog.jsp?command=<%=Command.EDIT%>&oid=<%=oidCatalog%>">Back to Katalog Detail</a></td>
                                              <%
                                              }
                                              %>
                                              <td nowrap width="45%"><a class="btn btn-primary" href="javascript:setPrice('<%=material.getOID()%>')"><i class="fa fa-gear"></i> <%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                                              <!-- Set componen composit -->
                                               <% if((oidMaterial != 0) && (material.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE)) { %>
                                              <!--td width="5%"><a href="javascript:setComponenComposit('<%=material.getOID()%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][6]%>"></a></td-->
                                              <td nowrap width="45%"><a class="btn btn-primary" href="javascript:setComponenComposit('<%=material.getOID()%>')"><i class="fa fa-gear"></i> <%=textListTitleHeader[SESS_LANGUAGE][6]%></a></td>
                                              <%}%>
                                              <!--td width="5%"><a href="javascript:cmdViewHistory('<%=oidMaterial%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][7]%>"></a></td-->
                                              <td nowrap width="45%"><a class="btn btn-primary" href="javascript:cmdViewHistory('<%=oidMaterial%>')"><i class="fa fa-file"></i> <%=textListTitleHeader[SESS_LANGUAGE][7]%></a></td>
                                              
                                            </tr>
                                          </table><% } %></td>
                                        </tr>
                                      </table></td>
                                    
                                    <% 
                                        if(srcMaterial.getShowImage()==1) {
                                              if(pictPath!=null && pictPath.length()>0) {
                                    %>
                                    <td width="20%" valign="top">
                                    <table width="100%" border="1" cellpadding="1" cellspacing="1" bordercolor="#FF8D1C"  bgcolor="#FCFDEC">
                                        <tr align="center" bgcolor="#7B869A" class="listgentitle">
                                          <td bgcolor="#FF9900"><span class="style9"><%=textListMaterialHeader[SESS_LANGUAGE][26]%></span></td>
                                        </tr>
                                        <tr>
                                          <td width="31%" align="center" valign="top">
                                            <%
                                                out.println("<img width = \"300\" src=\""+approot+"/"+pictPath+"\">");
                                            %>
                                          </td>
                                        </tr>
                                    </table></td><%}}%>
                                  </tr>
                                  <!-- Discount By Qty -->
                                   <%
                                    if(privEditPrice && privEditPrice) {
                                        if(oidMaterial!=0) {
                                    %>
                                    <tr>
                                        <td valign="top">&nbsp;</td>
                                   </tr>
                                   <tr>
                                        <td valign="top">&nbsp;</td>
                                   </tr>
                                   <tr>
                                        <td width="45%"><strong><u>Discount Qty</u></strong></td>
                                   </tr>
				   <tr>
					<td>
                                            <table width="70%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                                                <tr>
                                                    <td width ="1%" align="center" nowrap class="listgentitle">Currency</td>
                                                    <td width ="5%" align="center" nowrap class="listgentitle">Type Member</td>
                                                    <td width ="10%" align="center" nowrap class="listgentitle">Location</td>
                                                    <td width ="1%" align="center" nowrap class="listgentitle">Qty Start</td>
                                                    <td width ="1%" align="center" nowrap class="listgentitle">Qty End</td>
                                                    <td width ="1%" align="center" nowrap class="listgentitle">Discount</td>
                                                    <td width ="5%" align="center" nowrap class="listgentitle">Discount Type</td>
                                                </tr>
                                                     <%
                                                     for(int a=0;a<listCurrStandard.size();a++) {
                                                            Vector temp = (Vector)listCurrStandard.get(a);
                                                            CurrencyType currType = (CurrencyType)temp.get(0);
                                                     %>
                                                <tr>
                                                     <td width ="5" align="center" nowrap class="listgensell">
                                                         <b><%=currType.getCode()%></b></td>
                                                     <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                     <td width ="15" align="center" nowrap class="listgensell">&nbsp</td>
                                                     <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                     <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                     <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                     <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                </tr>

                                                      <%
                                                         for(int i=0;i<listDscType.size();i++) {
                                                            DiscountType dscType = (DiscountType)listDscType.get(i);
                                                      %>
                                                <tr>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell"><b><%=dscType.getCode()%></b></td>
                                                      <td width ="15" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                </tr>
                                                      <%
                                                        //update opie-eyek 20130809
                                                          for(int j=0;j<vlocationDiscountQty.size();j++){
                                                             Location location = (Location)vlocationDiscountQty.get(j);
                                                      %>
                                                <tr>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="15" align="left" nowrap class="listgensell"><%="<a href=\"javascript:cmdDiscQty('"+String.valueOf(dscType.getOID())+"','"+String.valueOf(currType.getOID())+"','"+String.valueOf(location.getOID())+"','"+String.valueOf(material.getOID())+"')\">" +location.getName()%></td>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                      <td width ="10" align="center" nowrap class="listgensell">&nbsp</td>
                                                </tr>
                                                       <%

                                                            String whClauseDiscQty = PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID]+
                                                                            " = "+dscType.getOID()+
                                                                            " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID]+
                                                                            " = "+currType.getOID()+
                                                                            " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID]+
                                                                            " = "+location.getOID()+
                                                                            " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+
                                                                            " = "+material.getOID();
                                                            String ordDiscQty = PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID];
                                                            Vector listDscQty = PstDiscountQtyMapping.list(0,0,whClauseDiscQty,ordDiscQty);
                                                            for(int k=0;k<listDscQty.size();k++){
                                                                DiscountQtyMapping discQty = (DiscountQtyMapping)listDscQty.get(k);
                                                       %>
                                                <tr>
                                                       <td width ="5" align="center" nowrap class="listgensell">&nbsp</td>
                                                       <td width ="10" align="right" nowrap class="listgensell">&nbsp</td>
                                                       <td width ="15" align="right" nowrap class="listgensell">&nbsp</td>
                                                       <td width ="5" align= "right"  nowrap class="listgensell"><%=discQty.getStartQty()%></td>
                                                       <td width ="5" align= "right"  nowrap class="listgensell"><%=discQty.getToQty()%></td>
                                                       <td width ="10" align="right" nowrap class="listgensell"><%=discQty.getDiscountValue()%></td>
                                                       <td width ="10" align="left" nowrap class="listgensell"><%=PstDiscountQtyMapping.typeDiscount[discQty.getDiscountType()]%></td>

                                                </tr>
                                                    <% }}}} %>
                                            </table>
					</td>
                                    </tr>
                                    <% }}%>
                              <!-- End of Discount Qty-->
                              </table></td>
                            </tr>
                            <%}else{%>
                                  <tr>
                                      <td>
                                      <% if(iCommand==Command.BACK || iCommand==Command.NONE || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {%>
                                        <table width="30%" border="0" cellspacing="2" cellpadding="3">
                                          <tr>
                                            <% if(privAdd) { %>
                                            <td width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1002','','<%=approot%>"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%>" name="Image1002" width="24" height="24" border="0" id="Image1002"></a></td>
                                            <td nowrap width="93%"><%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></td>
                                            <% } %>
                                          </tr>
                                        </table>
                                        <a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></a>
                                        <% } %>
                                      </td>
                                 </tr>

                                 <tr>
                                            <td> <table><tr><td width="70%"><%
                                          ctrLine.setLocationImg(approot+"/images");

                                          // set image alternative caption
                                          ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));
                                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");
                                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
                                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));

                                          ctrLine.initDefault(SESS_LANGUAGE,materialTitle);
                                          ctrLine.setTableWidth("100%");
                                          String scomDel = "javascript:cmdAsk('"+oidMaterial+"')";
                                          String sconDelCom = "javascript:cmdConfirmDelete('"+oidMaterial+"')";
                                          String scancel = "javascript:cmdEdit('"+oidMaterial+"')";
                                          ctrLine.setCommandStyle("command");
                                          ctrLine.setColCommStyle("command");

                                          // set command caption
                                          ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));

                                          String cmdSelectToLgr ="";
                                          if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){
                                             if(material==null){
                                                  material= new Material();
                                                 }
                                            //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");

                                            ctrLine.setBackCaption("Back To Search");
                                            ctrLine.setBackCommand("javascript:cmdCheck()");


                                            String name = "";
                                            name = material.getName();
                                            name = name.replace('\"','`');
                                            name = name.replace('\'','`');
                                            Unit unit = new Unit();
                                            try{unit = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}

                                            Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
                                                        double standardRate = 0;
                                            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}
                                            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
                                            try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}

                                            MatCurrency matCurr = new MatCurrency();
                                            try{matCurr = PstMatCurrency.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e1){}

                                            cmdSelectToLgr ="javascript:cmdSelectItemToReceive('"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                                            "','"+(defaultCost >0.00 ? ""+FRMHandler.userFormatStringDecimal(defaultCost) : "")+"','"+unit.getOID()+
                                            "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"')";

                                          }
                                          //select To Purchasing
                                          String cmdSelectToPch ="";
                                          if(sourceLink2!=null && sourceLink2.equals("materialdosearch.jsp")){
                                             if(material==null){
                                                 material= new Material();
                                                }
                                            //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");

                                            ctrLine.setBackCaption("Back To Search");
                                            ctrLine.setBackCommand("javascript:cmdCheck()");


                                            String name = "";
                                            name = material.getName();
                                            name = name.replace('\"','`');
                                            name = name.replace('\'','`');
                                            Unit unit = new Unit();
                                            try{unit = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}

                                            Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
                                                        double standardRate = 0;
                                            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}
                                            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
                                            try{defaultCost = material.getDefaultCost();}catch(Exception exc){;}

                                            MatCurrency matCurr = new MatCurrency();
                                            try{matCurr = PstMatCurrency.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e1){}

                                            MatVendorPrice matVendorPrice = new MatVendorPrice();
                                            try{matVendorPrice = PstMatVendorPrice.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e2){}

                                            cmdSelectToPch ="javascript:cmdSelectItemToPurchasing('"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                                            "','"+(defaultCost >0.00 ? ""+FRMHandler.userFormatStringDecimal(defaultCost) : "")+"','"+unit.getOID()+
                                            "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"')";
                                          }

                                          ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
                                          ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_DELETE,true));
                                          ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));

                                          if (privDelete){
                                              ctrLine.setConfirmDelCommand(sconDelCom);
                                              ctrLine.setDeleteCommand(scomDel);
                                              ctrLine.setEditCommand(scancel);
                                          }else{
                                              ctrLine.setConfirmDelCaption("");
                                              ctrLine.setDeleteCaption("");
                                              ctrLine.setEditCaption("");
                                          }

                                          if(privAdd == false  && privUpdate == false) {
                                              ctrLine.setSaveCaption("");
                                          }

                                          if (privAdd == false) {
                                              ctrLine.setAddCaption("");
                                          }

                                          if(iCommand==Command.SAVE && frmMaterial.errorSize()==0) {
                                              iCommand=Command.EDIT;
                                              editWithoutList = true;
                                          }

                                          if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.SAVE || (iCommand==Command.DELETE && frmMaterial.errorSize()>0)) {
                                              out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                                          }
                                          %> </td>
                                          <td> &nbsp;<%

                                          if(material.getOID()!=0 && cmdSelectToLgr!=null && cmdSelectToLgr.length()>0){
                                          %>

                                          <a href="<%=cmdSelectToLgr%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="Select to Receive%>" name="Image1003" width="24" height="24" border="0" id="Image1003">Select to Receive</a>
                                          <%}%>

                                          <%//add Menu
                                          if(material.getOID()!=0 && cmdSelectToPch!=null && cmdSelectToPch.length()>0){
                                          //if(material.getOID()!=0){
                                          %>

                                          <a href="<%=cmdSelectToPch%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="Select to Purchasing%>" name="Image1003" width="24" height="24" border="0" id="Image1003">Select to Purchasing</a>
                                          <%}%>
                                          </td>
                                          </tr>
                                              </table>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                          <% if(oidMaterial!=0) { %><table width="64%" border="0" cellspacing="2" cellpadding="3">
                                            <tr>
                                              <% if(privAdd) { %>
                                              <td width="5%"><a id="saveForm" href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%>" name="Image1003" width="24" height="24" border="0" id="Image1003"></a></td>
                                              <td nowrap width="45%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ADD,true)%></a></td>
                                              <% } %>
                                               <% if((oidMaterial != 0) && (material.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE)) { %>
                                              <td width="5%"><a href="javascript:setComponenComposit('<%=material.getOID()%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][6]%>"></a></td>
                                              <td nowrap width="45%"><a href="javascript:setComponenComposit('<%=material.getOID()%>')"><%=textListTitleHeader[SESS_LANGUAGE][6]%></a></td>
                                              <%}%>
                                            </tr>
                                          </table><% } %></td>
                                        </tr>
                            <%}
                            }
                            %>
                          </table>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="18" valign="middle" colspan="3">
                          <p>&nbsp;</p>                          
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="18" valign="middle" colspan="3">&nbsp;
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">&nbsp;
                        </td>
                      </tr>
                      <% if(iCommand==Command.SAVE || iCommand==Command.EDIT) { %>
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">&nbsp;</td>
                      </tr>
                      <% } %>
                      <%if(iCommand==Command.ADD){%>
                      <%}%>
                    </table>
                  </td>
                </tr>
              </table>
                <button style="width:0px; height:0px;" type="button" id="lastClick" onclick="javascript:cmdSave()"></button>
            </form>
<%
    }catch(Exception e){
        System.out.println("err : "+e.toString());
    }
%>

            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<script>
    jQuery(function(){
            $("#FRM_FIELD_NAME").autocomplete("list.jsp");
    });
</script>
</html>

