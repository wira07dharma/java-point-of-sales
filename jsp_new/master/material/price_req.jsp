<%-- 
    Document   : price_req
    Created on : Oct 23, 2017, 4:14:37 PM
    Author     : dimata005
--%>

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
    {"No","SKU","Barcode","Nama Barang","Merk","Kategori","Tipe","Unit Stok","Harga Jual", //8
             "Mata Uang Jual","Harga Beli","Mata Uang Beli","Tipe Supplier", "Supplier", "Tipe Harga 1", //14
             "Tipe Harga 2", "Tipe Harga 3","Group","Discount Terakhir","PPN Terakhir",
             "Harga Beli Terakhir","Tanggal Kadaluarsa","Profit",//22
             "Poin Minimum","Nomor Serial","Nilai Stok","Foto","Konsinyasi","Kode Gondola",//29
    "Ongkos Kirim Terakhir","Poin Sales","Unit Beli","Sub Category","Descripsi"},//29
     {"No","Code","Barcode","Name","Merk","Category","Type","Stock Unit","Default Price",
             "Default Price Currency","Default Cost","Default Cost Currency","Default Supplier Type",
             "Supplier", "Price Type 1", "Price Type 2",
             "Price Type 3","Type","Last Discount","Last Vat","Last Buying Price","Expired Date","Profit",
             "Minimum Point","Serial Number","Inventory Value","Picture","Consigment","Gondola Code",
    "Last Cost Cargo","Poin Sales","Buying Unit","Sub Category","Description"}// 29
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

int startComposit = FRMQueryString.requestInt(request, "start_price");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
int cmdPrice = FRMQueryString.requestInt(request, "command_price");
long oidMatComposit = FRMQueryString.requestLong(request, "hidden_mat_composit_id");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
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
                //CurrencyType ctType = (CurrencyType)temp.get(0);
                
                System.out.println("oidRetail : "+oidRetail+ " - "+priceType.getOID());
                System.out.println("oidCurr : "+oidCurrSell+ " - "+standart.getCurrencyTypeId());
                if((oidRetail==priceType.getOID()) && (oidCurrSell==standart.getCurrencyTypeId())){
                    if(valPrice=="")
                        valPrice = "0.0";
                    priceRetail = Double.parseDouble(valPrice); 
                }
                
                //if(!valPrice.equals("0")){
                //vect2.add(""+ctType.getOID());
                vect2.add(""+priceType.getOID());
                vect2.add(valPrice);
                vect2.add(""+standart.getOID());
                //}
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

long kepemilikanId = FRMQueryString.requestLong(request, "kepemilikan_id_input");
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
            document.frmmaterial.discount_2<%=i%>0.value = formatFloat(discval, '', guiDigitGroup, guiDecimalSymbol, decPlace);

            if(<%=i%>!=0){
               // alert(<%=i%>);
                var pricereal1 = cleanNumberFloat(document.frmmaterial.price_00.value, guiDigitGroup, guiDecimalSymbol);
                var prcreal1 = (pricereal1 * price) / 100
                document.frmmaterial.price_<%=i%>0.value = formatFloat(pricereal1 - prcreal1, '', guiDigitGroup, guiDecimalSymbol, decPlace);
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
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
	//Cek panjang SKU
	var my_sku = document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value;
	var panjang_sku = my_sku.length;
	if (panjang_sku <= 1000)
	{

		document.frmmaterial.command.value="<%=Command.SAVE%>";
		document.frmmaterial.prev_command.value="<%=prevCommand%>";
		document.frmmaterial.action="material_main.jsp";
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
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
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
	document.frmmaterial.action="material_list.jsp";
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
-->
</style>
<%--autocomplate add by fitra--%>
<script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
<script src="../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../styles/style.css" /> 
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
                <form name="frmmaterial" method ="post" action="">
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
                                        <li role="presentation"><a href="price_req.jsp?hidden_material_id=<%=material.getOID()%>&typemenu=<%=typemenu%>" style="background-color: #f3f3f3; border: 0; margin-top: 1px; color: #949494;">Price</a></li>
                                        <li role="presentation"><a href="product_req.jsp?hidden_material_id=<%=material.getOID()%>&typemenu=<%=typemenu%>" style="background-color: #f3f3f3; border: 0; margin-top: 1px; color: #949494;">Product Requirements </a></li>
                                  <%}%>
                                </ul>
                              </td>
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
                                    <td width="20%" class="listgensell"><%=prType.getCode()%></td>
                                    <%
                                    String readOnly = "readonly";
                                    for(int j=0;j<listCurrStandard.size();j++){
                                        double valuePrice=0;
                                        StandartRate sRate=null;
                                        Vector temp= null;
                                        try{
                                        temp = (Vector)listCurrStandard.get(j);
                                        sRate = (StandartRate)temp.get(1);
                                        valuePrice = PstPriceTypeMapping.getPrice(listPriceMapping,oidMaterial,prType.getOID(),sRate.getOID());
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
                                            if(((valuePrice-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice())-0.5 > material.getProfit()){
                                                       cfont ="<font class=\"msgquestion\">";
                                                       planMargin = "";
                                             } else {
                                                    if(((valuePrice-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice())+0.5 < material.getProfit()){
                                                                           cfont ="<font class=\"msgerror\">";
                                                                           planMargin = "";
                                                                     } else {
                                                                         cfont ="<font>";
                                                                     }
                                             }
                                        %>
                                        <td width="20%" class="listgensell"><%=cfont%>(<%=material.getCurrBuyPrice()>0 ? Formater.formatNumber((valuePrice-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice(),"###.#") :""%>)%</font></td>
                                        <%}else{%>
                                        <td width="20%" class="listgensell"><%=cfont%>(<%=material.getCurrBuyPrice()>0 ? Formater.formatNumber( (valuePrice-material.getCurrBuyPrice()) * 100 / material.getCurrBuyPrice(),"###.#") :""%>)%</font></td>
                                        <%}
                                    %>
                                    <td width="60%" align="right" class="listgensell testing">
                                      <input type="text" name="price_<%=i%><%=j%>" size="15" value="<%=(valuePrice>=0.0 ? FRMHandler.userFormatStringDecimal(valuePrice) : "") %>" class="formElemen" style="text-align:right" onKeyUp="javascript:cmdKeyUpPriceType(this)">
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
                                    <td width="15%" class="listgensell"><%=dscType.getCode()%></td>
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
                                        
                                       javascriptPrice=javascriptPrice + "\",discount_1\""+i+j;
                                       javascriptPricex=javascriptPricex +"\",discount_1"+i+j+":focus";
                                       javascriptPrice=javascriptPrice + ",discount_2"+i+j;
                                       javascriptPricex=javascriptPricex + ",discount_2"+i+j+":focus"; 
                                    %>
                                    <td width="3%" align="right" class="listgensell">
                                      <input type="text" <%=readOnly%> name="discount_1<%=i%><%=j%>" size="3" class="formElemen" value="<%=valuePct%>"  onKeyUp="javascript:cmdKeyUpPriceDiscType(this)" style="text-align:right">
                                    </td>
                                    <td width="5%" align="right" class="listgensell">
                                      <input type="text" <%=readOnly2%> name="discount_2<%=i%><%=j%>" size="10" class="formElemen" value="<%=value%>" style="text-align:right">
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
                                              <!--td width="5%"><a href="javascript:setPrice('<%=material.getOID()%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][3]%>"></a></td-->
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
                                    <td width="20%" valign="top">
                                    <% 
                                        if(srcMaterial.getShowImage()==1) {
                                              if(pictPath!=null && pictPath.length()>0) {
                                    %>
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
                                    </table><%}}%></td>
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
                                            <table width="60%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                                                <tr>
                                                    <td width ="5" align="center" nowrap class="listgentitle">Currency</td>
                                                    <td width ="10" align="center" nowrap class="listgentitle">Type Member</td>
                                                    <td width ="15" align="center" nowrap class="listgentitle">Location</td>
                                                    <td width ="5" align="center" nowrap class="listgentitle">Start Qty</td>
                                                    <td width ="5" align="center" nowrap class="listgentitle">To Qty</td>
                                                    <td width ="10" align="center" nowrap class="listgentitle">Discount</td>
                                                    <td width ="10" align="center" nowrap class="listgentitle">Discount Type</td>
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
