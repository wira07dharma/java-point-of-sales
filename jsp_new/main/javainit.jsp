<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.entity.masterdata.Sales"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PictureCompany"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PstPictureCompany"%>
<%@ page import="java.util.*,
                 com.dimata.posbo.session.admin.SessUserSession,
                 com.dimata.common.entity.periode.Periode,
                 com.dimata.common.entity.periode.PstPeriode,
                 com.dimata.qdep.entity.I_DocType,
                 com.dimata.qdep.entity.I_DocStatus,
                 com.dimata.qdep.entity.I_Approval,
                 com.dimata.util.Formater,
                 com.dimata.posbo.entity.admin.AppUser,
                 com.dimata.common.entity.system.PstSystemProperty" %>
<%!
 
 final static int MODUS_EHOTEL=0;
 final static int MODUS_PROCHAIN_HANOMAN=1;
 static int MODUS_VIEW = MODUS_PROCHAIN_HANOMAN;

String useProduction = PstSystemProperty.getValueByName("USE_PRODUCTION");
 //update opie-eyek 20130805
 //disini untuk menyeting aplikasi hanya untuk transfer (MODUS_TRANSFER_KASIR) atau untuk server
 final static int MODUS_TRANSFER_SERVER=0;
 final static int MODUS_TRANSFER_KASIR=1;
 static int MODUS_TRANSFER = MODUS_TRANSFER_SERVER;

 //update opie-eyek 20130805
 //disini untuk menyeting modus icon, apakah yang javascript atau yang menggunkan icon
 final static int MODUS_JAVASCRIPT=0;
 final static int MODUS_ICON=1;
 static int MODUS_MENU = MODUS_JAVASCRIPT;

 //update opie-eyek 20131127
 //disini untuk menyeting modus produk yang di pergunakan, apakah produk atas nama dimata, telkom atau client (tdk mau ada brending)
 final static int MODUS_PRODUCT_DIMATA=0;
 final static int MODUS_PRODUCT_TELKOM=1;
 final static int MODUS_PRODUCT_CLIENT=2;
 static int MODUS_PRODUCT =MODUS_PRODUCT_DIMATA;

 //update opie-eyek 20131216
 final static int MODUS_RETAIL=0;
 final static int MODUS_NONE=1;
 final static int MODUS_RESTAURANT=2;
 final static int MODUS_DISTRIBUTIONS=3;
 
 final static int MODUS_VIEW_NOT_USEFRAME = 0;
 final static int MODUS_VIEW_USEFRAME=1;
 static int MODUS_VIEW_HEADER = MODUS_VIEW_USEFRAME;

 final static int MODE_IMPLEMENTASI=0;
 final static int MODE_TESTING = 1;
 static int MODUS_APPLICATION_MODE = MODE_IMPLEMENTASI;
%>

<%
com.dimata.posbo.entity.masterdata.Material.setMaterialSwitchType(com.dimata.posbo.entity.masterdata.Material.WITH_USE_SWITCH_MERGE_MANUAL);
// application path
String approot= request.getContextPath();//"/D-ProChainPos-V3";
String printrootx=approot+"/servlet/com.dimata.posbo";


String urlC = request.getRequestURL().toString();
String baseURL = urlC.substring(0, urlC.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

// class name that implement workflow component
String docTypeClassName = I_DocType.DOCTYPE_CLASSNAME;
String docStatusClassName = I_DocStatus.DOCTSTATUS_CLASSNAME;
String approvalClassName = I_Approval.APPROVAL_CLASSNAME;

int SETTING_DEFAULT = 0;
int SETTING_COLONIAS = 1;
int specsetting = SETTING_COLONIAS;

/** Variabel jenis Menu */
int MENU_DEFAULT = 0;
int MENU_PER_TRANS = 1;
int MENU_ICON = 2;

// user is logging in or not
boolean isLoggedIn = false;
int userGroupNewStatus = -1;
String userName = "";
long userId = 0;

//add session 20120711 by mirahu 
session.setMaxInactiveInterval(60 * 60 * 2);
SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
AppUser appU = new AppUser();
try{
    if(userSession==null){
        userSession= new SessUserSession();
    }else{
        if(userSession.isLoggedIn()==true){
            isLoggedIn  = true;
            appU = userSession.getAppUser();
            userGroupNewStatus = appU.getUserGroupNew();
            userName = appU.getLoginId();
            userId = appU.getOID();
        }
    }
}catch (Exception exc){
    //System.out.println(" >>> Exception during check login");
}

//add sesion untuk sales
long locationSales=0;
long salesCurrencyId=0;
long oidSalesLog=0;
String warehouseSales="";
String salesName ="";
String salesCodeNew ="";
Sales salesLog = (Sales)session.getValue(SessUserSession.HTTP_SESSION_SALES);
try{
     if(salesLog!=null){
        salesName = salesLog.getName();
        salesCodeNew=salesLog.getCode();
        oidSalesLog=salesLog.getOID();
        Location xLocation=PstLocation.fetchExc(salesLog.getLocationId());
        locationSales=salesLog.getLocationId();
        warehouseSales= salesLog.getName();
        salesCurrencyId = salesLog.getDefaultCurrencyId();
     }
}catch (Exception exc){
    //System.out.println(" >>> Exception during check login");
}  
  
/**
 * set language
 */
String strLanguage = "";
if (session.getValue("APPLICATION_LANGUAGE") != null) {
    strLanguage = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
}
int langDefault = com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT;
int langForeign = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
int SESS_LANGUAGE = (strLanguage!=null && strLanguage.length() > 0) ? Integer.parseInt(strLanguage) : 0;

/**
set device use
*/
String strDeviceUse = "";
if (session.getValue("APPLICATION_DEVICE_USE") != null) {
    strDeviceUse = String.valueOf(session.getValue("APPLICATION_DEVICE_USE"));
}
int SESS_DEVICE_USE = (strDeviceUse!=null && strDeviceUse.length() > 0) ? Integer.parseInt(strDeviceUse) : 0;

Periode matPeriodeInit = PstPeriode.getPeriodeRunning();
// get period interval from system property
int periodInterval = 0; //Integer.parseInt(PstSystemProperty.getValueByName("MATERIAL_PERIOD"));
String databaseHome = ""; // String.valueOf(PstSystemProperty.getValueByName("DATABASE_HOME"));

/** variabel ini di gunakan untuk membedakan harga retur, df, menggunakan harga hpp atau harga jual retail */
int tranUsedPriceHpp = 0;

int valueDiv = 1;

//membuat default local currency
String localCurrencyDefault = PstSystemProperty.getValueByName("LOCAL_CURRENCY_DEFAULT");

String USER_LOGBOOK = PstSystemProperty.getValueByName("LOGBOOK_COMPANY_ID");
String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
String PASSWORD_LOGBOOK = PstSystemProperty.getValueByName("LOGBOOK_COMPANY_PWD");
/** variabel ini digunakan untuk menentukkan pilihan menu yang digunakan
 0=DEFAULT,
 1=Per Transaksi. gwawan@dimata 20070906
 2=menu icon @opie-eyek 20131220
*/
//cek tipe browser, browser detection
String userAgent = request.getHeader("User-Agent");
boolean isMSIE = (userAgent!=null && userAgent.indexOf("MSIE") !=-1);
int menuUsed = 0;
int brandInUse = 0;
int cashierUserOpeningClosing=0;

    tranUsedPriceHpp = Integer.parseInt(PstSystemProperty.getValueByName("PRICE_TRANSACTION_USED_HPP"));
    valueDiv = Integer.parseInt(PstSystemProperty.getValueByName("VALUE_DIV"));
    cashierUserOpeningClosing =  Integer.parseInt(PstSystemProperty.getValueByName("CASHIER_USE_OPENING_BALANCE"));
    if(isMSIE){
        menuUsed=3;
    }else{
        menuUsed =3;// Integer.parseInt(PstSystemProperty.getValueByName("MENU_USED"));
    }
    brandInUse = Integer.parseInt(PstSystemProperty.getValueByName("SYSTEM_USE_BRAND"));

    String strCompany = "<b>Your Company</b><br>Address";

    //add update 28012013
    String strInit = PstSystemProperty.getValueByName("COMPANY_NAME");//copyValueOf).Integer.parseInt(PstSystemProperty.getValueByName("PRICE_TRANSACTION_USED_HPP"));
    // view for in print
    Vector companyAddress = new Vector();
    companyAddress.add(strInit);

    strInit = PstSystemProperty.getValueByName("COMPANY_ADDRESS");
    companyAddress.add(strInit);

    Vector compAddDetail = new Vector();
    strInit = PstSystemProperty.getValueByName("COMPANY_PHONE");
    compAddDetail.add(strInit);
    compAddDetail.add("");
    companyAddress.add(compAddDetail);

    compAddDetail = new Vector();
    compAddDetail.add(""); //compAddDetail.add("SIA");
    compAddDetail.add(""); //compAddDetail.add("503/937/DIKES");
    companyAddress.add(compAddDetail);

    compAddDetail = new Vector();
    compAddDetail.add(""); //compAddDetail.add("Apoteker");
    compAddDetail.add(""); //compAddDetail.add("JEMY THEIS,S.Si.,APT");
    companyAddress.add(compAddDetail);

    compAddDetail = new Vector();
    compAddDetail.add(""); //compAddDetail.add("SP/SIK");
    compAddDetail.add(""); //compAddDetail.add("Kp.01.01.1.2.3340");
    companyAddress.add(compAddDetail);

    String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
    String kategoriName = PstSystemProperty.getValueByName("NAME_OF_CATEGORY");
    if(kategoriName.equals("0")){
        kategoriName="Category";
    }
    //cek jenis usaha update opie-eyek 20131216 apakah retail, restaurant,
    String typeOfBusiness = PstSystemProperty.getValueByName("TYPE_OF_BUSINESS");
    int useEtalase = Integer.valueOf(PstSystemProperty.getValueByName("USE_ETALASE"));
    int typeOfBusinessDetail = Integer.valueOf(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
    int typeOfUseConsignment = Integer.valueOf(PstSystemProperty.getValueByName("USE_CONSIGNMENT"));
    String daftarPeriode = PstSystemProperty.getValueByName("SHOW_DAFTAR_PERIODE");
    String tipePotongan = PstSystemProperty.getValueByName("SHOW_TIPE_POTONGAN");
    String tipeCustomer = PstSystemProperty.getValueByName("SHOW_TIPE_CUSTOMER");
    String pointMember = PstSystemProperty.getValueByName("SHOW_POINT_MEMBER");
    String nilaiTukarHarian = PstSystemProperty.getValueByName("SHOW_NILAI_TUKAR_HARIAN");
    String nilaiTukarStandart = PstSystemProperty.getValueByName("SHOW_NILAI_TUKAR_STANDART");
    String showAksesoris = PstSystemProperty.getValueByName("SHOW_AKSESORIS");
    String showProduksi = PstSystemProperty.getValueByName("SHOW_PRODUKSI");
    String showDailyKoreksiStok = PstSystemProperty.getValueByName("SHOW_DAILY_SELISIH_KOREKSI_STOK");
    String showUseForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL"); 
    String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA"); 
    
    //update opie-eyek untuk template dinamis nya 20131220
    //tanya kegunaannya dengan ramayu
    String pathImage = approot+"/imgupload";//untuk baground
    String pathImg = approot+"/imgcompany";//untuk company client
    String whereClausePic = PstPictureBackground.fieldNames[PstPictureBackground.FLD_LOGIN_ID] + "=" + 0;
    Vector listPictureBackground = PstPictureBackground.list(0, 0, whereClausePic, "");
    PictureBackground pictureBackground = new PictureBackground();

    PictureCompany pictureCompany = new PictureCompany();
    TempDinamis tempDinamis = new TempDinamis();

    if(menuUsed!=1||menuUsed!=0){
        Vector listPictureCompany = PstPictureCompany.list(0, 1, "", "");
        if(listPictureCompany!=null && listPictureCompany.size()>0){
            pictureCompany = (PictureCompany)listPictureCompany.get(0);
        }

        Vector listTempDinamis = PstTempDinamis.list(0, 1, "", "");
        if(listTempDinamis!=null && listTempDinamis.size()>0){
            tempDinamis = (TempDinamis)listTempDinamis.get(0);
        }
    }

    String navigation = tempDinamis!=null && tempDinamis.getNavigation()!=null ? tempDinamis.getNavigation():"menu_i";
    String bgColorBody = tempDinamis!=null && tempDinamis.getTempColor()!=null ? "#"+tempDinamis.getTempColor():"white";
    String bgColorHeader = tempDinamis!=null && tempDinamis.getHeaderColor()!=null ? "bgcolor=\""+tempDinamis.getHeaderColor()+"\"":"bgcolor=\"#42b6e8\"";
    String bgColorContent = tempDinamis!=null && tempDinamis.getContentColor()!=null ? "#"+tempDinamis.getContentColor():"white";
    String bgMenu = tempDinamis!=null && tempDinamis.getBgMenu()!=null ? "#"+tempDinamis.getBgMenu():"#FF8D1C";
    String fontMenu = tempDinamis!=null && tempDinamis.getFontMenu()!=null ? "#"+tempDinamis.getFontMenu():"white";
    String hoverMenu = tempDinamis!=null && tempDinamis.getHoverMenu()!=null ? "#"+tempDinamis.getHoverMenu():"#C1E4EC";
    String menu_1 =  "#42b6e8";
    String menu_1_bgFont = "#42b6e8";
    String header1 = tempDinamis!=null && tempDinamis.getHeaderColor()!=null ? "#"+tempDinamis.getHeaderColor():"#42B6E8";
    String header2 = tempDinamis!=null && tempDinamis.getTempColorHeader()!=null ? "#"+tempDinamis.getTempColorHeader():"#42B6E8";
    String garis1 = tempDinamis!=null && tempDinamis.getGarisHeader1()!=null ? "#"+tempDinamis.getGarisHeader1():"white";
    String garis2 = tempDinamis!=null && tempDinamis.getGarisHeader2()!=null ? "#"+tempDinamis.getGarisHeader2():"white";
    String footerBg = tempDinamis!=null && tempDinamis.getFooterBackground()!=null ? "#"+tempDinamis.getFooterBackground():"#42B6E8";
    String garisFooter = tempDinamis!=null && tempDinamis.getFooterGaris()!=null ? "#"+tempDinamis.getFooterGaris():"white";
    String warnaFont = tempDinamis!=null && tempDinamis.getFontMenu()!=null ? "#"+tempDinamis.getFontMenu():"white";
    String garisContent = tempDinamis!=null && tempDinamis.getGarisContent()!=null ? "#"+tempDinamis.getGarisContent():"sky-blue";
    String tableHeader =  tempDinamis!=null && tempDinamis.getTableHeader()!=null ? "#"+tempDinamis.getTableHeader():"#42B6E8";
    String tableCell = tempDinamis!=null && tempDinamis.getTableCell()!=null ? "#"+ tempDinamis.getTableCell():"white";
    
   
%>

<script language=JavaScript src="<%=approot%>/main/dsj_common.js"></script>
<script>var baseUrl = function(uri = "") { return ("<%=request.getProtocol().split("/")[0].toLowerCase()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>/<%=request.getRequestURI().split("/")[1]%>/" + uri); }</script>
<script language="JavaScript" type="text/JavaScript">
<%
    try{
%>
function compareDate(trans){
	var bool = new Boolean();
	var dt = new Date();
	var dtstartinit = new Date(<%=matPeriodeInit.getStartDate().getYear()+1900%>,<%=matPeriodeInit.getStartDate().getMonth()%>,<%=matPeriodeInit.getStartDate().getDate()%>);
	var dtendinit = new Date(<%=matPeriodeInit.getEndDate().getYear()+1900%>,<%=matPeriodeInit.getEndDate().getMonth()%>,<%=matPeriodeInit.getEndDate().getDate()%>);
	var inttrans = trans.getTime();
	var intdtstartinit = dtstartinit.getTime();
	var intdtendinit = dtendinit.getTime();
	var startdate = "<%=Formater.formatDate(matPeriodeInit.getStartDate(),"dd MMM yyyy")%>";
	var enddate = "<%=Formater.formatDate(matPeriodeInit.getEndDate(),"dd MMM yyyy")%>";
	if((inttrans > intdtendinit)){
		alert("Document tidak bisa di simpan,\nTanggal transaksi harus di antara "+startdate+" s/d "+enddate);
		bool = false;
	}else if(inttrans < intdtstartinit){
		alert("Document tidak bisa di simpan,\nTanggal transaksi harus di antara "+startdate+" s/d "+enddate);
		bool = false;
	}else{
		bool = true;
	}
	return bool;
}

function compareDateForAdd(){
	var bool = new Boolean(true);
	var dt = new Date();
	var dtentryinit = new Date(<%=matPeriodeInit.getLastEntry().getYear()+1900%>,<%=matPeriodeInit.getLastEntry().getMonth()%>,<%=matPeriodeInit.getLastEntry().getDate()%>);
	var intdt = dt.getTime();
	var intdtentryinit = dtentryinit.getTime();
	if(intdt > intdtentryinit){
		alert("Tidak bisa menambah Document,\nKarena tanggal entry periode telah berakhir !");
		bool = false;
	}
	return bool;
}
<%
    }catch(Exception e){
       // System.out.println("err : "+e.toString());
    }
    //System.out.println("masuk : "+matPeriodeInit);
%>
</script>