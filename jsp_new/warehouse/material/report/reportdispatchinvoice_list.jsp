<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER_REPORT, AppObjInfo.OBJ_TRANSFER_REPORT_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;


public static final String listTextGlobal[][] = {
    {"LAPORAN TRANSFER BARANG PER INVOICE","Transfer","Laporan Per Invoice","Pencarian",
             "Cetak Laporan Transfer"," Tidak ada data transfer barang...","Laporan Transfer"},
    {"GOODS TRANSFER REPORT BY INVOICE","Transfer","By Invoice Report","Search",
             "Print Transfer Report","There is no goods transfer data ...","Transfer Report"}
};

public static final String listTextTitle[][] = {
    {"Periode"," s/d ","Lokasi Asal","Lokasi Tujuan","Suplier","Kategori","Semua Lokasi"},
    {"Period"," to ","From Location","To Location","Supplier","Category","All Location"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","QTY","UNIT","HPP","TOTAL HPP"},
    {"NO","SKU","GOODS NAME","QTY","UNIT","COGS","TOTAL COGS"}
};

public static final String textListTitleHeader[][] = {
    {"Laporan Pengiriman Barang ke Toko Per Invoice","LAPORAN PENGIRIMAN BARANG KE TOKO PER INVOICE"," s/d ",
             "ASAl","TUJUAN","Pengiriman Barang","Print Pengiriman Barang","Tidak ada data pengiriman .."},
    {"Dispatch Goods to Store per Invoice Report","DISPATCH GOODS TO STORE PER INVOICE REPORT"," to ",
             "SOURCE","DESTINATION","Dispatch Goods","Dispatch Goods Print","No dispatch data available .."}
};

public Vector drawLineHorizontal() {
    Vector rowx = new Vector();
    //Add Under line
    rowx.add("-");
    rowx.add("--------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("-----------------------------------------------------------------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("---------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    return rowx;
}

public Vector drawHeader(int language) {
    Vector rowx = new Vector();
    //Add Header
    rowx.add("|");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][6]+"</div>");
    rowx.add("<div align=\"center\">"+"|"+"</div>");
    return rowx;
}

public Vector drawLineSubTotal() {
    Vector rowx = new Vector();
    rowx.add("|");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    return rowx;
}

public Vector drawLineTotal() {
    Vector rowx = new Vector();
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("----------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    return rowx;
}

public Vector drawLineSingleSpot() {
    Vector rowx = new Vector();
    rowx.add("-");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    return rowx;
}

public Vector drawLineTotalSide() {
    Vector rowx = new Vector();
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("--------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("--------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("--------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    rowx.add("-------------------------------");
    rowx.add("<div align=\"center\">"+"-"+"</div>");
    return rowx;
}

public Vector drawList(int language,Vector objectClass, boolean isCategory,
        boolean isSubCategory,boolean isSupplier,boolean isDispatchTo) {
    Vector result = new Vector();
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("0");
        
        ctrlist.dataFormat("","1%","0","0","left","top");
        ctrlist.dataFormat("","3%","0","0","center","top");
        ctrlist.dataFormat("","1%","0","0","left","top");
        ctrlist.dataFormat("","10%","0","0","center","top");
        ctrlist.dataFormat("","1%","0","0","left","top");
        ctrlist.dataFormat("","30%","0","0","center","top");
        ctrlist.dataFormat("","1%","0","0","right","bottom");
        ctrlist.dataFormat("","4%","0","0","right","bottom");
        ctrlist.dataFormat("","1%","0","0","right","bottom");
        ctrlist.dataFormat("","4%","0","0","center","bottom");
        ctrlist.dataFormat("","1%","0","0","right","bottom");
        ctrlist.dataFormat("","10%","0","0","right","bottom");
        ctrlist.dataFormat("","1%","0","0","right","bottom");
        ctrlist.dataFormat("","10%","0","0","right","bottom");
        ctrlist.dataFormat("","1%","0","0","right","bottom");
        
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        double totalPrice = 0.00;
        double totalCost = 0.00;
        double subTotalPrice = 0.00;
        double subTotalCost = 0.00;
        double totalQty = 0;
        double subTotalQty = 0;
        long noBill = 0;
        boolean firstRow = true;
        int baris = 0;
        int countTrue = 0;
        //Tentukan nilai baris untuk halaman pertama
        
        int maxlines = 72;
        int maxlinespgdst = 74;
        boolean boolmaxlines = true;
        
        for(int i=0; i<objectClass.size(); i++){
            Vector rowx = new Vector();
            Vector vt = (Vector)objectClass.get(i);
            MatDispatch df = (MatDispatch)vt.get(0);
            MatDispatchItem dfi = (MatDispatchItem)vt.get(1);
            Material mat = (Material)vt.get(2);
            Unit unt = (Unit)vt.get(3);
            String nama_barang = mat.getName();
            if (nama_barang.length() >= 22){
                nama_barang = nama_barang.substring(0,22);
            }
            String unit_name = unt.getCode();
            if (unit_name.length() >= 5){
                unit_name = unit_name.substring(0,5);
            }
            
            String str = "<a href=\"javascript:cmddetail(\'"+dfi.getOID()+"')\">[Code]</a>";
            if(mat.getRequiredSerialNumber()==0){
                str = "";
            }
            
            //Tambahkan header tanggal dan invoice
            if (noBill != df.getOID()){
                String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+df.getInvoiceSupplier()+"'";
                Vector vect = PstMatReceive.list(0,0,whereClause,"");
                
                ContactList contactList = new ContactList();
                if(vect!=null && vect.size()>0){
                    MatReceive receive = (MatReceive)vect.get(0);
                    try{
                        contactList = PstContactList.fetchExc(receive.getSupplierId());
                    }catch(Exception e){}
                }
                
                noBill = df.getOID();
                if (firstRow == true){
                    firstRow = false;
                    //Add header only
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    //Add date
                    rowx.add("*3");
                    rowx.add("|");
                    rowx.add("Tanggal");
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*3");
                    rowx.add("|");
                    rowx.add("Nomor");
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+df.getDispatchCode()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    rowx = new Vector();
                }else{
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    if (baris == maxlines){
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    //Add sub total then header
                    //Add sub total
                    rowx.add("|");
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    //lstData.add(drawLineTotal());
                    lstData.add(drawLineSubTotal());
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    //Add date
                    rowx = new Vector();
                    rowx.add("*3");
                    rowx.add("|");
                    rowx.add("Tanggal");
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    
                    /*rowx = new Vector();
                    //Add date
                    rowx.add("*3");
                    rowx.add("|");
                    rowx.add("Supplier");
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+contactList.getCompName()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }*/
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*3");
                    rowx.add("|");
                    rowx.add("Nomor");
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+df.getDispatchCode()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    rowx = new Vector();
                    subTotalPrice = 0.00;
                    subTotalCost = 0.00;
                    subTotalQty = 0;
                }
            }
            
            rowx.add("|");
            rowx.add(""+(i+1));
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add(mat.getSku());
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add(nama_barang);
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add("<div align=\"right\">"+str+" "+FRMHandler.userFormatStringDecimal(dfi.getQty())+"</div>");
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add(unit_name);
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfi.getHpp())+"</div>");
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfi.getHpp() * dfi.getQty())+"</div>");
            rowx.add("<div align=\"center\">"+"|"+"</div>");
            lstData.add(rowx);
            baris += 1;
            if (baris == maxlines) {
                if(boolmaxlines){
                    maxlines = maxlinespgdst;
                }
                //Add line
                lstData.add(drawLineHorizontal());
                //Add header for next page and restart counting baris
                lstData.add(drawLineHorizontal());
                baris = 1;
                lstData.add(drawHeader(language));
                baris += 1;
                lstData.add(drawLineHorizontal());
                baris += 1;
            }
            
            totalCost += (dfi.getHpp() * dfi.getQty());
            totalPrice += (dfi.getHpp() * dfi.getQty());
            totalQty += dfi.getQty();
            subTotalCost += (dfi.getHpp() * dfi.getQty());
            subTotalPrice += (dfi.getHpp() * dfi.getQty());
            subTotalQty += dfi.getQty();
            lstLinkData.add("");
        }
        lstData.add(drawLineHorizontal());
        Vector rowx = new Vector();
        //Add SUB TOTAL
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        lstData.add(rowx);
        lstData.add(drawLineTotal());
        //Add TOTAL
        rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">"+"TOTAL"+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        lstData.add(rowx);
        lstData.add(drawLineTotal());
        result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
    } else {
        result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][7]+"</div>");
    }
    return result;
}
%>

<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;
boolean isDispatchTo = false;

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcReportDispatch srcReportDispatch = new SrcReportDispatch();
SessReportDispatch sessReportDispatch = new SessReportDispatch();
FrmSrcReportDispatch frmSrcReportDispatch = new FrmSrcReportDispatch(request, srcReportDispatch);

/**
 * handle current search data session
 */
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcReportDispatch = (SrcReportDispatch)session.getValue(SessReportDispatch.SESS_SRC_REPORT_DISPATCH_INVOICE);
        if (srcReportDispatch == null) srcReportDispatch = new SrcReportDispatch();
    } catch(Exception e) {
        srcReportDispatch = new SrcReportDispatch();
    }
} else {
    frmSrcReportDispatch.requestEntityObject(srcReportDispatch);
    session.putValue(SessReportDispatch.SESS_SRC_REPORT_DISPATCH_INVOICE, srcReportDispatch);
}

/**
 * get vectSize, start and data to be display in this page
 */
Vector records = sessReportDispatch.getReportDispatch(srcReportDispatch);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    //start = ctrlReportDispatch.actionList(iCommand,start,vectSize,recordToGet);
}

/**
 * set value vector for stock report print
 * biar tidak load data lagi
 */

Location location_from = new Location();
if (srcReportDispatch.getLocationId() != 0) {
    try	{
        location_from = PstLocation.fetchExc(srcReportDispatch.getLocationId());
    } catch(Exception e) {
    }
}
else {
    location_from.setName(listTextTitle[SESS_LANGUAGE][6]);
}

Location location_to = new Location();
if (srcReportDispatch.getDispatchTo() != 0) {
    try	{
        location_to = PstLocation.fetchExc(srcReportDispatch.getDispatchTo());
    } catch(Exception e) {
    }
}
else {
    location_to.setName(listTextTitle[SESS_LANGUAGE][6]);
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdEdit(oid)
{
}

function cmdListFirst()
{
	document.frm_reportdispatch.command.value="<%=Command.FIRST%>";
	document.frm_reportdispatch.action="reportdispatchinvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListPrev()
{
	document.frm_reportdispatch.command.value="<%=Command.PREV%>";
	document.frm_reportdispatch.action="reportdispatchinvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListNext()
{
	document.frm_reportdispatch.command.value="<%=Command.NEXT%>";
	document.frm_reportdispatch.action="reportdispatchinvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListLast()
{
	document.frm_reportdispatch.command.value="<%=Command.LAST%>";
	document.frm_reportdispatch.action="reportdispatchinvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdBack()
{
	document.frm_reportdispatch.command.value="<%=Command.BACK%>";
	document.frm_reportdispatch.action="src_reportdispatchinvoice.jsp";
	document.frm_reportdispatch.submit();
}

function cmddetail(oid){
	window.open("df_stockcode.jsp?hidden_dispatch_item_id="+oid+"","report_stockcode","scrollbars=yes,height=200,width=500,status=no,toolbar=no,menubar=yes,location=no");
}

function printForm(){
	window.open("reportdispatchinvoice_form_print.jsp","dispatchreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportdispatch" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
				  	<h4><strong><%=listTextGlobal[SESS_LANGUAGE][0]%></strong></h3>
				  </td>
                </tr>
				<tr align="left" valign="top"> 
                  <td class="command" width="11%"><strong><%=(listTextTitle[SESS_LANGUAGE][0]).toUpperCase()%></strong></td>
				  <td class="command" width="0%"><strong>:</strong></td>
				  <td class="command" width="89%"><%=Formater.formatDate(srcReportDispatch.getDateFrom(), "dd-MM-yyyy")%><%=listTextTitle[SESS_LANGUAGE][1]%><%=Formater.formatDate(srcReportDispatch.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td class="command"><strong><%=(listTextTitle[SESS_LANGUAGE][2]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%=location_from.getName().toUpperCase()%></td>
                </tr>
				<tr align="left" valign="top"> 
                  <td class="command"><strong><%=(listTextTitle[SESS_LANGUAGE][3]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%=location_to.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%
					Vector hasil = drawList(SESS_LANGUAGE,records,isCategory,isSubCategory,isSupplier,isDispatchTo);
					Vector report = new Vector(1,1);
					report.add(srcReportDispatch);
					report.add(hasil);
					try
					{
						session.putValue("SESS_MAT_REPORT_DISPATCH_INVOICE",report);
					}
					catch(Exception e){}
					
					for(int k=0;k<hasil.size();k++){
						out.println(hasil.get(k));
					}
					
					%> </td>
                </tr>
				<tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%">
						  <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][6],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][6],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
						</td>
						<% if(records.size() != 0) { //kondisi ada data %>
                        <td width="20%">
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=listTextGlobal[SESS_LANGUAGE][4]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command"><%=listTextGlobal[SESS_LANGUAGE][4]%></a></td>
                            </tr>
                          </table>
						</td>
						<% } //kondisi ada data%>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
        <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
