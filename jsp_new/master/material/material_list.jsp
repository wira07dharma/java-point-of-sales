<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.posbo.session.masterdata.SessCategory"%>
<%@ page language = "java" %>
<%@ page import="
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.admin.PstAppUser,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.session.masterdata.SessMaterial,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.search.SrcMaterial,
                 com.dimata.posbo.form.search.FrmSrcMaterial,
                 com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                 com.dimata.common.entity.location.*"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
boolean privShowPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW_VALUE));
%>
<%!
//public static final int SESS_LANGUAGE = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
public static final String textListTitleHeader[][] = {
    {"Daftar Barang","Barang","Tidak ada data","Masuk ke Exception"},
    {"Goods List","Goods","No data available","Go Into Exception"}
};
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
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
/*public static final String textListMaterialHeader[][] =
{
	{"No","Sku","Nama","Kategori","Sub Kategori","Tipe Supplier","Supplier","Hpp","Hrg Jual"},
	{"No","Code","Name","Category","Sub Kategori","Supplier Type","Supplier","Average","Price"}
};*/

public static final String textListMaterialHeader[][] = {
    {"No","SKU","Barcode","Kategori / Merk / Nama Barang","Jenis","Tipe","HPP","Harga Jual","Kategori","Merk","Kode Grup","Foto","Mata Uang HPP","Mata Uang Jual","Harga Beli", "Margin"},
    {"No","Code","Barcode","Category / Merk / Name","Jenis","Type","Average Price","Price","Category","Merk","Code Range","Picture","Curr. Avg Price","Curr. Sell","Buy.PRice","Margin"}
};

//{"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hpp","Hrg Jual","Kategori","Merk","Kode Grup"},
//{"No","Code","Barcode","Name","Jenis","Type","Average","Price","Category","Merk","Code Range"}

//untuk discount Qty mapping
public static final String textListDiscountQtyHeader[][] = {
    {"No","Mata Uang","Tipe Member","Lokasi","Start Qty","To Qty","Diskon","Tipe Diskon","Harga Per Unit"},
    {"No","Currency","Member Type","Location","Start Qty","To Qty","Discount","Discount type","Unit Price"}
};

/* this constant used to list text of listHeader */
public static final String textListHeaderReposting[][] = {
	{"Lokasi","Periode"},
	{"Location","Period"}
};

public String drawList(int language,Vector objectClass,int start, int showImage, String approot, int showUpdateCatalog, SrcMaterial srcMaterial, int start2, int showDiscountQty, int showHpp, int showCurrency, boolean privShowPrice,long locationSelected, int typeOfBusinessDetail) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        /** mendapatkan mata uang default */
        CurrencyType defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();
        int multiLanguageName = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
        int useSubCategory = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueIntByName("USE_SUB_CATEGORY"));
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"5%");
        if(showImage==1)
            ctrlist.addHeader(textListMaterialHeader[language][11],"10%");
        
        ctrlist.addHeader(textListMaterialHeader[language][2],"5%");
        //ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
	
        //String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
	//String name = "Category / "+ merkName+" / Name";
	ctrlist.addHeader("Category","10%");
        if(useSubCategory==1){
             ctrlist.addHeader("Sub Category","10%");
        }
        if(multiLanguageName==1){
            ctrlist.addHeader("Name English","10%"); 
            ctrlist.addHeader("Name Indonesia","10%"); 
            ctrlist.addHeader("Name Indian","10%"); 
        }else{
            ctrlist.addHeader("Name","15%"); //textListMaterialHeader[language][3]
        }
        ctrlist.addHeader("Merk","5%");

        ctrlist.addHeader("Gondola","5%");

        ctrlist.addHeader("Supplier","5%");

        ctrlist.addHeader("Unit Stock","5%");
        ctrlist.addHeader("Cost price","5%"); 
        if(privShowPrice){
            if(showHpp == 1){
             ctrlist.addHeader(textListMaterialHeader[language][6]+" ("+defaultCurrencyType.getCode()+")","10%");
             ctrlist.addHeader(textListMaterialHeader[language][14]+" ("+defaultCurrencyType.getCode()+")","10%");
            }
            ctrlist.addHeader(textListMaterialHeader[language][13],"5%");
            ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
            if(showHpp == 1){
             ctrlist.addHeader(textListMaterialHeader[language][15]+" (%)","10%");
            }
        }
        ctrlist.addHeader("Safety stocks","5%"); 
        ctrlist.addHeader("Min. stocks","5%"); 
        ctrlist.addHeader("Real Stock","5%"); 
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        if(start<0) {
            start = 0;
        }
        Vector rowx = new Vector();
        Vector list = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+"=1","");
        CurrencyType currSell = new CurrencyType();
        if(list!=null && list.size()>0){
            currSell = (CurrencyType)list.get(0);
        }
        Periode maPeriode = PstPeriode.getPeriodeRunning();
        SessCategory sessCategory = new SessCategory();
        //cek suppliernya
        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            Material material = (Material)temp.get(0);
            Category category = (Category)temp.get(1);
            SubCategory subCategory = (SubCategory)temp.get(2);
            Merk merk = (Merk)temp.get(3);
            Unit unit = (Unit) temp.get(4);
            Ksg ksg = (Ksg) temp.get(5);
            //ContactList cnt = (ContactList)temp.get(3);
            
            //added by dewok 2018 for jewelry
            String itemName = "" + material.getName();
            if(typeOfBusinessDetail == 2) {                
                itemName = SessMaterial.setItemNameForLitama(material.getOID());
            }
            rowx = new Vector();
            
            start = start + 1;
            
            Material matUpdate = new Material();
            
            try{
                matUpdate = PstMaterial.fetchExc(material.getOID());
            }catch(Exception e){}
            rowx.add(""+start+"");
            rowx.add("<a title=\"\" href=\"javascript:cmdEdit('"+material.getOID()+"')\">"+material.getSku()+"</a>");
            
            // untuk menampilkan gambar
            if(showImage==1){
                SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(material.getSku());
                if(pictPath.length()>0){
                    rowx.add("<div align=\"center\"><img heigth = \"110\" width = \"110\" src=\""+approot+"/"+pictPath+"\"></div>");
                }else{
                    rowx.add("&nbsp");
                }
            }
            
            rowx.add(material.getBarCode());
            String cat = sessCategory.cleanCategoryName(category.getOID());
            cat = sessCategory.getCategoryName(category.getOID());
            rowx.add(cat);
            //rowx.add(category.getName());
            if(useSubCategory==1){
                rowx.add(subCategory.getName());
            }
            String[] smartPhonesSplits = material.getName().split("\\;");
            if(multiLanguageName==1){
                try{
                   rowx.add(smartPhonesSplits[0]);
                }catch(Exception ex){
                    rowx.add("");
                }
                 try{
                   rowx.add(smartPhonesSplits[1]);
                }catch(Exception ex){
                    rowx.add("");
                }
                 try{
                   rowx.add(smartPhonesSplits[2]);
                }catch(Exception ex){
                    rowx.add("");
                }
            }else{
                rowx.add(itemName);                
            }
            
            rowx.add(merk.getName());
            rowx.add(""+ksg.getName());
            //supplier
            //String nameSupplier = PstMatVendorPrice.listJoin(limitStart, recordToGet, whereClause, order);
            String whereClauseSupp = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+material.getOID();
            Vector listMatVendorPrice = PstMatVendorPrice.list(0,0, whereClauseSupp , "");
            String namaSupplier="";
            if(listMatVendorPrice.size()>0){
                for (int k = 0; k < listMatVendorPrice.size(); k++) {
                    MatVendorPrice matVendorPrice = (MatVendorPrice)listMatVendorPrice.get(k);
                    if(k==0){
                        namaSupplier = PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
                    }else{
                        namaSupplier = namaSupplier +", "+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
                    }
                }
            }
            
            rowx.add(""+namaSupplier);

            rowx.add(unit.getCode());
            rowx.add(""+FRMHandler.userFormatStringDecimal(material.getAveragePrice()));
            
            if(privShowPrice){
                CurrencyType curr = new CurrencyType();
                try{
                    curr = PstCurrencyType.fetchExc(matUpdate.getDefaultCostCurrencyId());
                }catch(Exception e){}
                //rowx.add("<div align=\"right\">"+curr.getCode()+"</div>");
                if(showHpp==1){
                   rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matUpdate.getAveragePrice())+"</div>");
                   rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matUpdate.getCurrBuyPrice())+"</div>");
                }
                //add opie-eyek 20130708
                double priceSales = SessMaterial.getPriceSaleInTypePrice(material, srcMaterial.getSelectPriceTypeId());
                try{
                    //matUpdate.setDefaultPrice(priceSales);
                    //PstMaterial.updateExc(matUpdate);
                }catch(Exception e){}
                //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getDefaultPrice())+"</div>");
                rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales)+"</div>");

                if(showHpp == 1){
                    if(matUpdate.getCurrBuyPrice()>0){
                     String cfont="";
                     String planMargin="";
                     if(((priceSales-matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice())-0.5 > matUpdate.getProfit()){
                           cfont ="<font class=\"msgquestion\">";
                           planMargin = "";
                     } else {
                        if(((priceSales-matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice())+0.5 < matUpdate.getProfit()){
                                               cfont ="<font class=\"msgerror\">";
                                               planMargin = "";
                                         } else {
                                             cfont ="<font>";
                                         }
                     }

                     rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales-matUpdate.getCurrBuyPrice())+cfont+  " (  " +
                            ( matUpdate.getCurrBuyPrice()>0 ? Formater.formatNumber((priceSales-matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice(),"###.#") :"") +"%) <font> </div>");
                    } else{
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales-matUpdate.getCurrBuyPrice())+  " (  " +
                            ( matUpdate.getCurrBuyPrice()>0 ? Formater.formatNumber( (priceSales-matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice(),"###.#") :"") +"%) </div>");
                    }
                }
            }
            
            //sefty & min. stock add opie-eyek 20140318
            String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+material.getOID()+
                           " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+maPeriode.getOID();
            Vector listLoc = PstMaterialStock.list(0,0,where + " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationSelected,"");
            MaterialStock materialStock = new MaterialStock();
            if(listLoc!=null && listLoc.size()>0){
                materialStock = (MaterialStock)listLoc.get(0);
            }
            rowx.add(""+materialStock.getQtyMin());
            rowx.add(""+materialStock.getQtyOptimum());

            //stock?
            Date dateNow = new Date();
            double stokSisa =0;
            if(locationSelected!=0){
                 stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationSelected, dateNow, 0);
                 rowx.add(""+stokSisa);
            }else{
                rowx.add(""+stokSisa);
            }
            
            
            lstData.add(rowx);

            //table for list discount qty mapping
            String whereClause = "";
             whereClause = " DISCQTY. "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+"="+material.getOID();
              String strfromDate = Formater.formatDate(srcMaterial.getDateFrom(), "yyyy-MM-dd 00:00:01");
              String strtoDate = Formater.formatDate(srcMaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
             whereClause = whereClause + " AND DISCQTY." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";

             String orderClause = " LOC."+PstLocation.fieldNames[PstLocation.FLD_CODE];
              orderClause = orderClause + ", DISC." + com.dimata.common.entity.payment.PstDiscountType.fieldNames[PstDiscountType.FLD_CODE];
              orderClause = orderClause + ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
              orderClause = orderClause + ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE];
            //Vector listDiscQty = PstDiscountQtyMapping.listDiscQtyAll(0, 0, whereClause, orderClause);
              Vector listDiscQty = SessMaterial.getDiscountQtyMapping(srcMaterial,material.getOID());
            if(showUpdateCatalog==1 && showDiscountQty==1) {
              if(listDiscQty.size()!=0){
              rowx = new Vector();
              rowx.add("");
              rowx.add("");
              if(showImage==1){
              rowx.add("");
              }
              rowx.add("<div align=\"center\"class=\"comment\"><b>DISCOUNT QTY</b></div>");
              rowx.add(drawDiscountQty(language,listDiscQty,start2,showCurrency));
              //rowx.add("");
              //rowx.add("");
              //rowx.add("");
              lstData.add(rowx);
             }
           }
            
        }
        result = ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
    }
    return result;
}

public String drawCodeRangeList(int language,Vector objectClass){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("20%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListMaterialHeader[language][10],"20%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        Vector rowx = new Vector();
        for(int i=0; i<objectClass.size(); i++){
            CodeRange codeRange = (CodeRange)objectClass.get(i);
            rowx = new Vector();
            rowx.add("<a href=\"javascript:cmdEditSearch('"+codeRange.getOID()+"')\">"+codeRange.getFromRangeCode()+"</a>");
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }
    return result;
}

//for list discount qty mapping
public String drawDiscountQty(int language, Vector objectClass, int start, int showCurrency) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListDiscountQtyHeader[language][0],"5%");
        if (showCurrency == 1){
        ctrlist.addHeader(textListDiscountQtyHeader[language][1],"10%");
        }
        ctrlist.addHeader(textListDiscountQtyHeader[language][2],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][3],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][4],"5%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][5],"5%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][6],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][7],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][8],"10%");


        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        Vector rowx = new Vector();
        for(int i=0; i<objectClass.size(); i++){
            //Vector temp = (Vector)objectClass.get(i);
            DiscountQtyMapping discountqtymapping = (DiscountQtyMapping)objectClass.get(i);
            //DiscountType discountType = (DiscountType)temp.get(1);
            //CurrencyType currencyType = (CurrencyType)temp.get(2);
            //Location location = (Location)temp.get(3);
            //Material material = (Material)temp.get(4);

            com.dimata.common.entity.payment.DiscountType discountType = new com.dimata.common.entity.payment.DiscountType();
            try {
		  discountType = com.dimata.common.entity.payment.PstDiscountType.fetchExc(discountqtymapping.getDiscountTypeId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstDiscountType.fetchExc() : " + e.toString());
		}
            CurrencyType currencyType = new CurrencyType();
            try {
		  currencyType = PstCurrencyType.fetchExc(discountqtymapping.getCurrencyTypeId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstCurrencyType.fetchExc() : " + e.toString());
		}

            Location location = new Location();
            try {
		  location = PstLocation.fetchExc(discountqtymapping.getLocationId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstLocation.fetchExc() : " + e.toString());
		}

            Material material = new Material();
	    try {
		  material = PstMaterial.fetchExc(discountqtymapping.getMaterialId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
		}



            //start = start + 1;
            double diskon = 0;
            double diskonPersen =0;
            double hargaUnit = 0;
            double priceSales = SessMaterial.getPriceSale(material);

            rowx = new Vector();
            rowx.add(""+(start+i+1)+"");
            if (showCurrency == 1){
            rowx.add(currencyType.getCode());
            }
            rowx.add(discountType.getCode());
            rowx.add(location.getCode());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getStartQty()));
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getToQty()));
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getDiscountValue()));
            rowx.add(PstDiscountQtyMapping.typeDiscount[discountqtymapping.getDiscountType()]);

            
            if(discountqtymapping.getDiscountType()== 0){
               diskonPersen = discountqtymapping.getDiscountValue();
               diskon = priceSales *diskonPersen/100;
            }
            else if (discountqtymapping.getDiscountType()== 1){
               diskon = discountqtymapping.getDiscountValue();
            }

            hargaUnit = priceSales - diskon;
            if(discountqtymapping.getDiscountType()== 2){
              rowx.add("<div align=\"center\">-</div>");
            }

            else {
              rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(hargaUnit));
            }


            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }


  return result;
}

%>

<%
/**
* get approval status for create document 
*/
int systemName = I_DocType.SYSTEM_MATERIAL;
boolean privManageData = true; 
%>

<%
/**
 * get title for purchasing(pr) document
 */
String dfTitle = textListTitleHeader[SESS_LANGUAGE][1];
String dfItemTitle = dfTitle + " Item";

/**
 * get request data from current form
 */
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidCodeRange = FRMQueryString.requestLong(request, "hidden_range_code_id");
int type = FRMQueryString.requestInt(request, "hidden_type");
int typemenu = FRMQueryString.requestInt(request, "typemenu");
int typehome = FRMQueryString.requestInt(request, "typehome");

int expandSearch = FRMQueryString.requestInt(request, "expand_search");
/**
 * initialitation some variable
 */
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");

int start_search = FRMQueryString.requestInt(request, "start_search");
int start_search2 = FRMQueryString.requestInt(request, "start_search2");

int recordToGet = 20;
if(start_search==1){
    start=start_search2-recordToGet;
    iCommand=22;
}

int vectSize = 0;
String whereClause = "";
//for discount qty
int start2 = FRMQueryString.requestInt(request, "start2");

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine  = new ControlLine();
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
SrcMaterial srcMaterial = new SrcMaterial();
SessMaterial sessMaterial = new SessMaterial();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);

/**
 * handle current search data session
 */
if(iCommand==Command.BACK || iCommand==Command.FIRST ||
        iCommand==Command.PREV || iCommand==Command.NEXT ||
        iCommand==Command.LAST){
    try{
        srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        
        if (srcMaterial == null){
            srcMaterial = new SrcMaterial();
        }
    }catch(Exception e){
        out.println(textListTitleHeader[SESS_LANGUAGE][3]);
        srcMaterial = new SrcMaterial();
    }
}else{
    if(type==0){
        frmSrcMaterial.requestEntityObject(srcMaterial);
        //add opsi currency Type nd memberType
        Vector vectCurr = new Vector(1,1);
	 String[] strCurrencyType = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
	 if(strCurrencyType!=null && strCurrencyType.length>0) {
		 for(int i=0; i<strCurrencyType.length; i++) {
			try {
				vectCurr.add(strCurrencyType[i]);
			}
			catch(Exception exc) {
				System.out.println("err");
			}
		 }
	 }

         Vector vectMember = new Vector(1,1);
	 String[] strMember = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MEMBER_TYPE_ID]);
	 if(strMember!=null && strMember.length>0) {
		for(int i=0; i<strMember.length; i++) {
			try {
				vectMember.add(strMember[i]);
			}
			catch(Exception exc) {
				System.out.println("err");
			}
		}
	}
        srcMaterial.setMemberTypeId(vectMember);
        
        String[] selectedSellItem = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SELL_LOCATION]);
        Vector vectSellItem = new Vector(1,1);
        if(selectedSellItem!=null && selectedSellItem.length>0) {
            for(int i=0; i<selectedSellItem.length; i++) {
                    try {
                            vectSellItem.add(selectedSellItem[i]);
                            
                    }
                    catch(Exception exc) {
                            System.out.println("err");
                    }
            }
	}
               
        srcMaterial.setSellLocation(vectSellItem);
        
        
        String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
        int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
        
        srcMaterial.setDesignMat(DESIGN_MATERIAL_FOR);
        
        //end of add opsi currency Type nd memberType
        srcMaterial.setOidCodeRange(oidCodeRange);
        
        session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
        
    }else{
        srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        srcMaterial.setOidCodeRange(oidCodeRange);
    }
}

/**
 * get vectSize, start and data to be display in this page
 */
if(typehome==1){
    srcMaterial.setSupplierId(-1);
    srcMaterial.setMerkId(-1);
    srcMaterial.setViewHppvsPrice(1);
    srcMaterial.setShowHpp(1);
}

if(typehome==2){
  srcMaterial.setSupplierId(-1);
  srcMaterial.setMerkId(-1);
  srcMaterial.setViewHppvsPrice(2);
  srcMaterial.setShowHpp(1);
  String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
  Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
  long priceTypeIdd=0;
  for(int i=0;i<listPriceType.size();i++) {
            PriceType prType = (PriceType)listPriceType.get(0);
            priceTypeIdd=prType.getOID();
  }
 srcMaterial.setSelectPriceTypeId(priceTypeIdd);
}

    //added by dewok 2018-01-17
    String kodeAwal = FRMQueryString.requestString(request, "FRM_KODE_START");
    String kodeAkhir = FRMQueryString.requestString(request, "FRM_KODE_END");
    String tipeItem = FRMQueryString.requestString(request, "tipe_item");
    String multiMatId = "";
    if (kodeAwal.length() > 0 && kodeAkhir.length() > 0) {
        try {
            long lKodeAwal = Long.valueOf(kodeAwal);
            long lKodeAkhir = Long.valueOf(kodeAkhir);        
            while (lKodeAwal <= lKodeAkhir) {
                Vector<Material> listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + lKodeAwal + "'", "");
                for (Material m : listItem) {
                    multiMatId += multiMatId.length() > 0 ? "," + m.getOID() : "" + m.getOID();
                }
                lKodeAwal++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //add where clause
    String whereAdd = "";
    if(typeOfBusinessDetail == 2) {
        if (multiMatId.length() > 0) {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " IN (" + multiMatId + ")";
        }
        if (!tipeItem.equals("")) {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + tipeItem + "'";
        }        
    }

vectSize = sessMaterial.getCountSearch(srcMaterial, whereAdd);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV ||
        iCommand==Command.LAST || iCommand==Command.LIST){
    start = ctrlMaterial.actionList(iCommand,start,vectSize,recordToGet);
}

Vector records = sessMaterial.searchMaterial(srcMaterial,start,recordToGet, whereAdd);
//Vector records = sessMaterial.searchMaterialWithMultiSupp(srcMaterial,start,recordToGet);
String vendorName = "";
if(srcMaterial.getSupplierId()>0){
    try{
        ContactList contact = new ContactList();
        contact = PstContactList.fetchExc(srcMaterial.getSupplierId());
        vendorName = ", Supplier : "+contact.getCompName();
    }catch(Exception e){}
}
/**
 * ini di gunakan untuk mencari range code
 */
Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>

<style>
  .tool-tip{
    color:#444444;
    width:250px;
    z-index:13000;
    }
    
    .tool-title{
    font-weight:normal;
    font-size:16px;
    font-family:Georgia, "Times New Roman", Times, serif;
    margin:0;
    color:#343434;
    padding:8px 8px 5px 8px;
    background:url(images/tips-trans.png) top left;
    text-align:left;
    }
    .tool-text{
    font-size:12px;
    padding:0 8px 8px 8px;
    background:url(images/tips-trans.png) bottom right;
    text-align:left;
    } 
    </style>
    
<script language="JavaScript">
function cmdAdd()
{
    document.frm_material.start.value=0;
    document.frm_material.approval_command.value="<%=Command.SAVE%>";			
    document.frm_material.command.value="<%=Command.ADD%>";
    document.frm_material.add_type.value="<%=ADD_TYPE_LIST%>";			
    document.frm_material.action="material_main.jsp";
    document.frm_material.submit();
}

function cmdEdit(oid)
{
    document.frm_material.hidden_material_id.value=oid;
    document.frm_material.start.value=0;
    document.frm_material.approval_command.value="<%=Command.APPROVE%>";					
    document.frm_material.command.value="<%=Command.EDIT%>";
    document.frm_material.typemenu.value="<%=typemenu%>";
    document.frm_material.action="material_main.jsp";
    document.frm_material.submit();
}

function cmdEditSearch(oid){
document.frm_material.hidden_type.value = "1";
    document.frm_material.hidden_range_code_id.value=oid;
    document.frm_material.approval_command.value="<%=Command.FIRST%>";
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.action="material_list.jsp";
    document.frm_material.submit();
}

function cmdPrintAllExcel(){
     window.open("material_list_excel.jsp?tms=<%=System.currentTimeMillis()%>&iCommand=14","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");

}

function cmdListFirst()
{
    document.frm_material.command.value="<%=Command.FIRST%>";
    document.frm_material.action="material_list.jsp";
    document.frm_material.submit();
}

function cmdListPrev()
{
    document.frm_material.command.value="<%=Command.PREV%>";
    document.frm_material.action="material_list.jsp";
    document.frm_material.submit();
}

function cmdListNext()
{
    document.frm_material.command.value="<%=Command.NEXT%>";
    document.frm_material.action="material_list.jsp";
    document.frm_material.submit();
}

function cmdListLast()
{
    document.frm_material.command.value="<%=Command.LAST%>";
    document.frm_material.action="material_list.jsp";
    document.frm_material.submit();
}

function cmdBack()
{
    document.frm_material.command.value="<%=Command.BACK%>";
    document.frm_material.action="srcmaterial.jsp";
    document.frm_material.submit();
}

function cmdPrintAll(){
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.target = "print_catalog";
    document.frm_material.action="material_list_print.jsp";
    document.frm_material.submit();
}

function cmdPrintAllWithoutPrice(){
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.target = "print_catalog";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}

function printForm() {
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListWoPricePdf?tms=<%=System.currentTimeMillis()%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}



function cmdPrintAllPriceTag() {
    var strvalue  = "<%=approot%>/master/material/search_tipe_harga.jsp?command=<%=Command.ADD%>";
    winSrcMaterial = window.open(strvalue,"searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function printFormAllPdf() {
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPdf?tms=<%=System.currentTimeMillis()%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}



//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">   
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
 <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../main/header.jsp" %>
    </td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;Masterdata &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable -->
            <%
                if(typeOfBusinessDetail == 2) {
                    if (tipeItem.equals("")) {
                        out.print(" : Semua Tipe");
                    } else {
                        out.println(" : "+Material.MATERIAL_TYPE_TITLE[Integer.valueOf(tipeItem)]);
                    }
                }
            %>
          </td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_material" method="post" action=""> 
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">			  			  			  			  
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="start2" value="<%=start2%>">
              <input type="hidden" name="start_search" value="0">
              <input type="hidden" name="start_search2" value="<%=start%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_type" value="<%=type%>">
              <input type="hidden" name="typemenu" value="<%=typemenu%>">
              <!--for litama-->
              <input type="hidden" name="FRM_KODE_START" value="<%=kodeAwal%>">
              <input type="hidden" name="FRM_KODE_END" value="<%=kodeAkhir%>">
              <input type="hidden" name="tipe_item" value="<%=tipeItem%>">
              <!---->
              <input type="hidden" name="expand_search" value="<%=expandSearch %>">
              <input type="hidden" name="approval_command">
                <table width="100%" cellspacing="0" cellpadding="3">
                  <tr align="left" valign="top">
                    <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,srcMaterial.getShowImage(),approot, srcMaterial.getShowUpdateCatalog(), srcMaterial, start2, srcMaterial.getShowDiscountQty(), srcMaterial.getShowHpp(), srcMaterial.getShowCurrency(),privShowPrice,srcMaterial.getLocationId(), typeOfBusinessDetail)%></td>
                  </tr>
                  <tr align="left" valign="top">
                    <td height="8" align="left" colspan="3" class="command"> 
                      <span class="command"> 
                      <%
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                      %>
                      </span>
                    </td>
                  </tr>
                  <%=drawCodeRangeList(SESS_LANGUAGE,recordsCodeRange)%>
                  <tr align="left"> 
                      <td height="21" valign="top" width="13%">&nbsp;<br></td>
                  </tr>  
                  <tr align="left" valign="top">
                    <td height="18" valign="top" colspan="3"> 
                       <table width="100%" border="0" cellspacing="0" cellpadding="0">
                         <tr align="left"> 
                            <td height="21" valign="top" width="13%">&nbsp;</td>
                          </tr>  
                         <tr>
                           <%if(privAdd && privManageData){%>
                           <!--td width="3%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%></a></td>
                           <!--td width="3%" nowrap><a href="javascript:cmdPrintAll()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdPrintAll()"><i class="fa fa-print"></i> Print All List</a></td>
                           
                           <!--td width="3%" nowrap><a href="javascript:cmdPrintAllExcel()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdPrintAllExcel()"><i class="fa fa-print"></i> Print All List Excel</a></td>
                           
                           <!--td width="3%" nowrap><a href="javascript:cmdPrintAll()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:printFormAllPdf()"><i class="fa fa-print"></i> Print Pdf</a></td>
                           <td width="">All List W/O Price :</td>
                           <!--td width="3%" nowrap><a href="javascript:cmdPrintAllWithoutPrice()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdPrintAllWithoutPrice()"><i class="fa fa-print"></i> As HTML</a></td>
                           <td width="">Or</td>
                           <!--td width="3%" nowrap><a href="javascript:printForm()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:printForm()"><i class="fa fa-file-pdf-o"></i> As PDF</a></td>
                           <!-- print print price tag -->
                           <!-- by mirahu 20120419 -->
                           <!--td width="3%" nowrap><a href="javascript:cmdPrintAllPriceTag()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdPrintAllPriceTag()"><i class="fa fa-print"></i> Print Price Tag</a></td>
                           <%}%>
                           <!--td width="6%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                           <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                         </tr>
                        
                      </table>                        						  
                    </td>
                  </tr>					  
                </table>
            </form>
                         
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <%
       /*if(start_search>0){
            iCommand=0;
            start_search=0;
       }*/
    %>
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
<!-- #EndTemplate --></html>
