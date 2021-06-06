package com.dimata.posbo.report.sale;

import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.Negara;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.PstNegara;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstPaymentSystem;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.gui.jsp.ControlList;
import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstSales;
import com.dimata.posbo.entity.masterdata.PstTableRoom;
import com.dimata.posbo.entity.masterdata.Sales;
import com.dimata.posbo.entity.masterdata.Shift;
import com.dimata.posbo.entity.masterdata.TableRoom;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.form.search.FrmSrcReportSale;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaleReportHandler extends HttpServlet{
    public static final String textListHeader[][] = {
        {"Laporan Harian Kasir","Periode","Lokasi","Bill","Meja","Waktu","Pax","Makanan","Minuman","Lainnya","Diskon","Service","Tax","Kredit<br> Bruto","Semua Meja","Meja","Semua Member","Total<br> Bruto"},
        {"Daily Cashier Report","Period","Location","Bill","Table","Time","Pax","Food","Beverage","Other","Discount","Service","tax","Credit <br>Bruto","All Table","Table","All Member", "Total<br> Bruto"}
    };
    
    public static final String textListHeader2[][] = {
        //  0                        1       2         3                 4             5               6          7 
        {"Laporan Menu Teratas","Periode","Lokasi","Kategori Order","Pembayaran","Kewarganegaraan","Kategori","Waiter"},
        {"Top Menu Report","Period","Location","Order Category","Payment","Nationality","Category","Waiter"}         
    };
    
    public static final String textListHeader3[][] = {        
        {"No","Kode","Nama","Qty","Satuan","Jumlah","Pax","Makanan","Minuman","Lainnya"},
        {"No","Code","Name","Qty","Unit","Amount","Pax","Food","Beverage","Other"}         
    };
    
    public static final String textListHeader4[][] = {        
        {"Laporan Kartu Kredit","Lokasi","Periode","No Bill","Tanggal","Nilai","Jumlah","Tipe Kartu","No Kartu","Nama"},
        {"Credit Card Report","Location","Period","Bill No","Date","Value","Pax","Amount","Card Type","Card No","Name"}         
    };
    
    public static final String textListHeader5[][] = {        
        {"Laporan Compliment","Lokasi","Periode","Tanggal","No Bill","Menu","Qty","Unit","Harga","Total Harga","Biaya","Total Biaya","Remark"},
        {"Compliment Report","Location","Period","Date","Bill No","Menu","Qty","Unit","Price","Total Price","Cost","Total Cost","Remark"}         
    };
    
    public static final String textListHeader6[][] = {        
        {"Laporan Rangkuman Penjualan (Tanpa Compliment)","Lokasi","Periode","Rata-rata Pax","Pax","Sesi","Tax","Service","Diskon","Makanan","Minuman","Lainnya","Kredit Netto","Total Penjualan Net","Total Penjualan Bruto"},
        {"Sales Summary Report (Non Compliment)","Location","Period","Date","Average Chek/Pax","Pax","Session","Tax","Service","Discount","Food","Beverage","Credit Netto","Total Net Sales","Total Bruto Sales"}         
    };
    
    public static final String comboBox[][] = {
        {"Dine In","Take Away","Delivery Order","Lunas","Kredit","Semua"},
        {"Dine In","Take Away" ,"Delivery Order","Cash","Credit","All"}
    };
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        int command = FRMQueryString.requestInt(request, "command");
        response.setContentType("text/html;charset=UTF-8");
        String result="";
        
        switch (command) {
            case Command.LIST:   
                result = getListSearch(request);
                response.getWriter().write(result);  
            break;
            
            case Command.GET:   
                result = getMemberControl(request);
                response.getWriter().write(result);  
            break;
                
            case Command.LOAD:   
                result = getListmember(request);
                response.getWriter().write(result);  
            break;
                
            case Command.SEARCH:   
                result = getBodyReport(request);
                response.getWriter().write(result);  
            break;
  
            default:        
            break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        int command = FRMQueryString.requestInt(request, "command");
        response.setContentType("text/html;charset=UTF-8");
        String result="";
        
        switch (command) {
            
            case Command.GET:   
                result = getBodyTopCategory(request);
                response.getWriter().write(result);  
            break;
            
            case Command.LIST:   
                result = getBodyCCReport(request);
                response.getWriter().write(result);  
            break;
            
            case Command.LOAD:   
                result = getComplimentBodyReport(request);
                response.getWriter().write(result);  
            break;
                
            case Command.SEARCH:   
                result = getSummaryBody(request);
                response.getWriter().write(result);  
            break;
                
            default:        
            break;
        }
    }
    
    private String getListSearch(HttpServletRequest request){
        String result ="";
        int searchBy = FRMQueryString.requestInt(request, "searchBy");
        String location = FRMQueryString.requestString(request, "location");
        //GET BY TABLE
        if (searchBy==0){
            String whereTable = ""+PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID]+"="+location+"";
            Vector listTable = PstTableRoom.list(0, 0, whereTable, "");
            Vector valTable = new Vector(1,1);
            Vector keytable= new Vector(1,1);
            valTable.add("");
            keytable.add("--All--");
            for (int i=0;i<listTable.size();i++){
                TableRoom tableRoom = (TableRoom)listTable.get(i);
                valTable.add(""+tableRoom.getOID());
                keytable.add(""+tableRoom.getTableNumber());
            }
            try {
                result += "" + ControlCombo.draw("tableId", null, "", valTable, keytable, "", "formElemen")+"";
            } catch (Exception e) {
                
            }
        //GET BY MEMBER
        }else if (searchBy==1){
            result += ""
                + "<input type='hidden' id='idMember' name='idMember' value=''>"
                + "<input type='text' id='memberName' name='memberName' value=''>"
                + "<img style='cursor:pointer' src='../../../images/BtnSearch.jpg' id='openSearchMember'>";
        }
        
        return result;
    }
    
    private String getMemberControl(HttpServletRequest request){
        String result ="";
       
        result += ""
            + "<div class='col-md-12'>"               
                + "<input type='text' class='form-control' value='' id='typeMember'>"
            + "</div>";
       
       return result; 
    }
    
    private String getListmember(HttpServletRequest request){
        String result ="";
        String typeText = "";
        String whereContact ="";

        typeText = FRMQueryString.requestString(request, "typeText");
        whereContact = ""
            + " CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+"=7"
            + " AND (CONT."+PstContact.fieldNames[PstContact.FLD_PERSON_NAME]+" like '%"+typeText+"%'"
            + " OR CONT."+PstContact.fieldNames[PstContact.FLD_HOME_ADDR]+" like '%"+typeText+"%')";
        
        Vector listContact = PstContact.listContactByClassType(0, 10, whereContact, "");
        result += ""
           + "<div class='col-md-12'>"
                + drawMemberList(listContact)
           + "</div>";
       
       return result;
    }
    
    private String drawMemberList(Vector objectClass){
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%"); 
        ctrlist.setAreaStyle("listgen"); 
        ctrlist.setTitleStyle("tableheader"); 
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); 
        ctrlist.addHeader("No", "10%");
        ctrlist.addHeader("Name", "40%");
	ctrlist.addHeader("Address","40%");
        
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        ctrlist.reset();
        int index = -1;

        Vector rowx = new Vector(1, 1);
        Vector listItem = new Vector(1,1);

        for (int i = 0; i < objectClass.size(); i++) {
            Contact contact = (Contact)objectClass.get(i);
            int no = i+1;
            rowx = new Vector(1, 1);
            rowx.add("<a class='selectCtCustomer' style='cursor:pointer' data-oId='"+contact.getOID()+"' data-personName='"+contact.getPersonName()+"' data-addr='"+contact.getHomeAddr()+"'>"+no+"</a>");
            rowx.add("<a class='selectCtCustomer' style='cursor:pointer' data-oId='"+contact.getOID()+"' data-personName='"+contact.getPersonName()+"' data-addr='"+contact.getHomeAddr()+"'>"+contact.getPersonName()+"</a>");
            rowx.add("<a class='selectCtCustomer' style='cursor:pointer' data-oId='"+contact.getOID()+"' data-personName='"+contact.getPersonName()+"' data-addr='"+contact.getHomeAddr()+"'>"+contact.getHomeAddr()+"</a>");
            lstData.add(rowx);
            
        }
        return ctrlist.drawBootstrapStripted();
    }
    
    private String getBodyReport(HttpServletRequest request){
        String result ="";
        String searchBy = "";
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        long idTable = 0;
        long idMember =0;
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        Location loc = new Location();
        //GET DATA FROM FORM        
        long location = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        int lang =FRMQueryString.requestInt(request, "lang");
        
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        int sortBy =FRMQueryString.requestInt(request, frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_SORT_BY]);
        if (sortBy==0){
            searchBy = ""+textListHeader[lang][15];
            idTable = FRMQueryString.requestLong(request, "tableId");
            if (idTable==0){
                searchBy += " - "+textListHeader[lang][14]+"";
            }else{
                TableRoom  tableRoom = new TableRoom();
                try {
                    tableRoom = PstTableRoom.fetchExc(idTable);
                } catch (Exception e) {
                }
                searchBy += " - "+tableRoom.getTableNumber()+"";
            }
           
        }else if (sortBy==1){
            idMember = FRMQueryString.requestLong(request, "idMember");
            String memberName = FRMQueryString.requestString(request, "memberName");
            searchBy ="MEMBER";
            if(memberName==""){
                searchBy =searchBy + " - "+textListHeader[lang][16]+"";
            }else{
                searchBy =searchBy + " - "+memberName+"";              
            }
           
        }
        
        try {
            loc = PstLocation.fetchExc(location);
        } catch (Exception e) {
        }
        
       
        result += ""
            + "<table cellpadding='3' cellspacing='0' width='100%'>"
                + "<tbody>"
                    + "<tr align='left' valign='top'>"
                        +"<td colspan='3' align='center' height='14' valign='middle'>"
                            + "<h4>"+textListHeader[lang][0]+"</h4>"
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:10%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader[lang][1]+""
                        +"</td>"
                        +"<td style='width:2%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1'  height='14' valign='middle'>"
                            + ""+fromDate+" s/d "+toDate+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:10%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader[lang][2]+""
                        +"</td>"
                        +"<td style='width:2%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1'  height='14' valign='middle'>"
                            + ""+loc.getName()+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:10%' colspan='1'  height='14' valign='middle'>"
                            + "Search By"
                        +"</td>"
                        +"<td style='width:2%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1'  height='14' valign='middle'>"
                            + ""+searchBy+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td  colspan='3'  height='14' valign='middle'>"
                            + getReport(request)
                        +"</td>"
                        
                    + "</tr>"
            + "</table>";
        
        return result;
    }
    
    private String getReport(HttpServletRequest request){
        String result ="";
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        long idTable = 0;
        long idMember =0;
        String memberName = "";
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String whereExtra = "";
        
        //GET DATA FROM FORM        
        long location = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        int sortBy =FRMQueryString.requestInt(request, frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_SORT_BY]);
        if (sortBy==0){
            idTable = FRMQueryString.requestLong(request, "tableId");
            if (idTable!=0){
                whereExtra = " AND pt."+PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_ID]+"="+idTable+"";
            }
        }else if (sortBy==1){
            idMember = FRMQueryString.requestLong(request, "idMember");
            memberName = FRMQueryString.requestString(request, "memberName");
            if(memberName!=""){
                whereExtra = " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+"="+idMember+"";
            }
        }
        int lang =FRMQueryString.requestInt(request, "lang"); 
        //Cari cash cashier
        String cashCashierID = PstBillMain.listCashCashierPerDay(fromDate, toDate,0);
        String whereCashPayment = ""
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+location+" AND ("+cashCashierID+")";
        
        Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");
        
//        String whereCashPaymentReturnTransaction = whereCashPayment+" "
//                                                  + " AND (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1 AND "
//                                                  + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
//                                                  + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";
//        Vector listPaymentSystemReturn = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPaymentReturnTransaction, "");
        
        //BILL PAID    
        String whereBillMainCash = ""
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+location+" AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0 AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 AND ("+cashCashierID+")";
//                + "("
//            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") >= '"+fromDate+"' AND "
//            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") <= '"+toDate
//            +"')"
            //+ "";
        if (whereExtra!=""){
            whereBillMainCash += whereExtra;
        }
        
        Vector listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(0, 0, whereBillMainCash);
        
        //BILL NOT PAID
        String whereBillMainCredit = ""
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+location+" AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0 AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 AND ("+cashCashierID+")";
//            + "("
////            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") >= '"+fromDate+"' AND "
////            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") <= '"+toDate
//            +"')"
//            + "";
        if (whereExtra!=""){
            whereBillMainCredit += whereExtra;
        }
        Vector listBillMainCredit = PstBillMain.listSummaryTranscationReportHarian(0, 0, whereBillMainCredit);
        
        double totalData = listBillMainCredit.size() + listBillMaincash.size();
        
        if (totalData>0){
            result +=""
            + "<table width='100%' border='1' cellspacing='1' cellpadding='1'>"
                + "<thead>"
                    + "<tr style='text-align:center'>"
                        + "<th><center>No</center></th>"
                        + "<th><center>"+textListHeader[lang][3]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][4]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][5]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][6]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][7]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][8]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][9]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][10]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][11]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][12]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][17]+"</center></th>"
                        + "<th><center>"+textListHeader[lang][13]+"</center></th>";
                        
                        for (int i = 0; i<listPaymentSystem.size();i++){
                            PaymentSystem paymentsystem = (PaymentSystem)listPaymentSystem.get(i);
                            result += "<th><center>"+paymentsystem.getPaymentSystem()+"</center></th>";
                        }
            result +=""
                    + "</tr>"   
                + "</thead>"
                + "<tbody>";
                    //UNTUK TRANSAKSI YANG DIBAYAR
                    double totalSumFood=0;
                    double totalSumBeverage=0;
                    double totalSumOther=0;
                    double totalSumDiscount=0;
                    double totalSumService=0;
                    double totalSumTax=0;
                    double totalSumSub=0;
                    double totalSumKredit=0;
                    String whereBill="";
                    String whereBillReturn="";
                    String whereBillKembali="";
                    String whereBillReturnKembalian="";
                    int urutretunrn=0;
                    int urut=0;
                    int nomor=0;
                    for (int j=0;j<listBillMaincash.size();j++){
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        if(billMain.getDocType()==1 && billMain.getTransctionType()==0 && billMain.getTransactionStatus()==0){
                            
                            if(urutretunrn==0){
                                whereBillReturn = " ( cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ='"+billMain.getOID()+"' ";
                                whereBillReturnKembalian = " ( cbm."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"='"+billMain.getOID()+"'";
                            }else{
                                whereBillReturn =whereBillReturn+ " OR cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ='"+billMain.getOID()+"' ";
                                whereBillReturnKembalian = whereBillReturnKembalian+" OR cbm."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"='"+billMain.getOID()+"'";
                            }
                            
                            result += "<tr>";
                            result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                            result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";
                            if(tableRoom.getTableNumber()==null){
                                result += "<td style='text-align: center; color:red'>-</td>";
                            }else{
                               result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                            }
                            
                            result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                            result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                            //FOOD
                            double totalFood = getTotalByCategory(billMain.getOID(), 0);
                            totalSumFood=totalSumFood-totalFood;
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                            //BEVERAGE
                            
                            double totalBeverage = getTotalByCategory(billMain.getOID(), 1);
                            totalSumBeverage=totalSumBeverage-totalBeverage;
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                            //OTHER
                            double totalOther = getTotalByCategory(billMain.getOID(), 3);
                            totalSumOther=totalSumOther-totalOther;
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                            totalSumDiscount=totalSumDiscount-billMain.getDiscount();
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                            totalSumService=totalSumService-billMain.getServiceValue();
                            result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                            totalSumTax=totalSumTax-billMain.getTaxValue();
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                            totalSumSub=totalSumSub-(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                            //KREDIT
                            result += "<td style='text-align: right; color:red'>0</td>";
                            //PAYMENT SYSTEM
                            for (int a = 0; a<listPaymentSystem.size();a++){
                                PaymentSystem paymentSystems = (PaymentSystem)listPaymentSystem.get(a);
                                double totalPayment = getTotalPaymentByPaymentSystem(billMain.getOID(),paymentSystems.getOID());
                                result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalPayment)+"</td>";
                            }
                            result += "</tr>";
                            urutretunrn=urutretunrn+1;
                        }else{
                            
                            if(urut==0){
                                whereBill = " ( cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ='"+billMain.getOID()+"' ";
                                whereBillKembali = " ( cbm."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"='"+billMain.getOID()+"'";
                            }else{
                                whereBill =whereBill+ " OR cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ='"+billMain.getOID()+"' ";
                                whereBillKembali = whereBillKembali+" OR cbm."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"='"+billMain.getOID()+"'";
                            }
                            
                            result += "<tr>";
                            result += "<td style='text-align: center;' >"+nomor+"</td>";
                            result += "<td style='text-align: center;'>"+billMain.getInvoiceNumber()+"</td>";
                            //result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()==null?"-":tableRoom.getTableNumber()+"</td>";
                            if(tableRoom.getTableNumber()==null){
                                result += "<td style='text-align: center;'>-</td>";
                            }else{
                               result += "<td style='text-align: center;'>"+tableRoom.getTableNumber()+"</td>";
                            }
                            result += "<td style='text-align: center;'>"+printFormat.format(billMain.getBillDate())+"</td>";
                            result += "<td style='text-align: right;'>"+billMain.getPaxNumber()+"</td>";
                            //FOOD
                            double totalFood = getTotalByCategory(billMain.getOID(), 0);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalFood)+"</td>";
                            //BEVERAGE
                            double totalBeverage = getTotalByCategory(billMain.getOID(), 1);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalBeverage)+"</td>";
                            //OTHER
                            double totalOther = getTotalByCategory(billMain.getOID(), 3);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalOther)+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                            //KREDIT
                            result += "<td style='text-align: right;'>0</td>";
                            //PAYMENT SYSTEM
                            totalSumFood=totalSumFood+totalFood;
                            totalSumBeverage=totalSumBeverage+totalBeverage;
                            totalSumOther=totalSumOther+totalOther;
                            totalSumDiscount=totalSumDiscount+billMain.getDiscount();
                            totalSumService=totalSumService+billMain.getServiceValue();
                            totalSumTax=totalSumTax+billMain.getTaxValue();
                            totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                            for (int a = 0; a<listPaymentSystem.size();a++){
                                PaymentSystem paymentSystems = (PaymentSystem)listPaymentSystem.get(a);
                                double totalPayment = getTotalPaymentByPaymentSystem(billMain.getOID(),paymentSystems.getOID());
                                result += "<td style='text-align: right;'>"+decimalFormat.format(totalPayment)+"</td>";
                            }
                            result += "</tr>";
                            urut=urut+1;
                        }
                    }
                    
                    //UNTUK TRANSAKSI YANG TIDAK DIBAYAR
                    for (int k=0;k<listBillMainCredit.size();k++){
                        Vector tempData = (Vector)listBillMainCredit.get(k);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        if(billMain.getDocType()==1 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==0){
                            result += "<tr>";
                            result += "<td style='text-align: center;' color:red' >"+nomor+"</td>";
                            result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";
                            result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()==null?"-":tableRoom.getTableNumber()+"</td>";
                            result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                            result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                            //FOOD
                            double totalFood = getTotalByCategory(billMain.getOID(), 0);
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                            //BEVERAGE
                            double totalBeverage = getTotalByCategory(billMain.getOID(), 1);
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                            //OTHER
                            double totalOther = getTotalByCategory(billMain.getOID(), 3);
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                            result += "<td style='text-align: right; color:red'>0</td>";
                            //KREDIT
                            double totalKredit = billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue();
                            totalKredit=totalKredit;
                            result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalKredit)+"</td>";
                            
                            
                            totalSumFood=totalSumFood-totalFood;
                            totalSumBeverage=totalSumBeverage-totalBeverage;
                            totalSumOther=totalSumOther-totalOther;
                            totalSumDiscount=totalSumDiscount-billMain.getDiscount();
                            totalSumService=totalSumService-billMain.getServiceValue();
                            totalSumTax=totalSumTax-billMain.getTaxValue();
                            totalSumKredit=totalSumKredit-totalKredit;
                            
                            //PAYMENT SYSTEM
                            for (int a = 0; a<listPaymentSystem.size();a++){
                                PaymentSystem paymentsystem = (PaymentSystem)listPaymentSystem.get(a);
                                double totalPayment = getTotalPaymentByPaymentSystem(billMain.getOID(),paymentSystem.getOID());
                                result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalPayment)+"</td>";
                            }
                            result += "</tr>";
                        }else{
                            result += "<tr>";
                            result += "<td style='text-align: center;'>"+nomor+"</td>";
                            result += "<td style='text-align: center;'>"+billMain.getInvoiceNumber()+"</td>";
                            result += "<td style='text-align: center;'>"+tableRoom.getTableNumber()+"</td>";
                            result += "<td style='text-align: center;'>"+printFormat.format(billMain.getBillDate())+"</td>";
                            result += "<td style='text-align: right;'>"+billMain.getPaxNumber()+"</td>";
                            //FOOD
                            double totalFood = getTotalByCategory(billMain.getOID(), 0);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalFood)+"</td>";
                            //BEVERAGE
                            double totalBeverage = getTotalByCategory(billMain.getOID(), 1);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalBeverage)+"</td>";
                            //OTHER
                            double totalOther = getTotalByCategory(billMain.getOID(), 3);
                            result += "<td style='text-align: right;'>"+decimalFormat.format(totalOther)+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                            result += "<td style='text-align: right;'>0</td>";
                            //KREDIT
                            
                            result += "<td style='text-align: right;'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                            
                            totalSumFood=totalSumFood+totalFood;
                            totalSumBeverage=totalSumBeverage+totalBeverage;
                            totalSumOther=totalSumOther+totalOther;
                            totalSumDiscount=totalSumDiscount+billMain.getDiscount();
                            totalSumService=totalSumService+billMain.getServiceValue();
                            totalSumTax=totalSumTax+billMain.getTaxValue();
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                            
                            //PAYMENT SYSTEM
                            for (int a = 0; a<listPaymentSystem.size();a++){
                                PaymentSystem paymentsystem = (PaymentSystem)listPaymentSystem.get(a);
                                double totalPayment = getTotalPaymentByPaymentSystem(billMain.getOID(),paymentSystem.getOID());
                                result += "<td style='text-align: right;'>"+decimalFormat.format(totalPayment)+"</td>";
                            }
                            result += "</tr>";
                        }
                    }
                    
                    if(!whereBill.equals("")){
                        whereBill=whereBill+" )";
                    }
                    if(!whereBillReturn.equals("")){
                        whereBillReturn=whereBillReturn+ " )";
                    }
                    if(!whereBillKembali.equals("")){
                        whereBillKembali= whereBillKembali + " )";
                    }
                    if(!whereBillReturnKembalian.equals("")){
                        whereBillReturnKembalian=whereBillReturnKembalian+" )";
                    }
                    
                    //subtotal
                    result += "<tr>";
                    result += "<td style='text-align: center;'></td>";
                    result += "<td style='text-align: center;'></td>";
                    result += "<td style='text-align: center;'></td>";
                    result += "<td style='text-align: center;'></td>";
                    result += "<td style='text-align: right;'>Sub Total</td>";
                    //FOOD
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumFood)+"</td>";
                    //BEVERAGE
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumBeverage)+"</td>";
                    //OTHER
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumOther)+"</td>";
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumDiscount)+"</td>";
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumService)+"</td>";
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumTax)+"</td>";
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumSub)+"</td>";
                    //KREDIT
                    result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(totalSumKredit)+"</td>";
                    //PAYMENT SYSTEM
                    for (int a = 0; a<listPaymentSystem.size();a++){
                        PaymentSystem paymentSystems = (PaymentSystem)listPaymentSystem.get(a);
                        double totalPayment = getSumaryTotalPaymentByPaymentSystem(""+whereBill,""+whereBillKembali ,paymentSystems.getOID(),true);
                        double totalPaymentBillReturn = getSumaryTotalPaymentByPaymentSystem(""+whereBillReturn,""+whereBillReturnKembalian,paymentSystems.getOID(),true);
                        double total = totalPayment-totalPaymentBillReturn;
                        result += "<td style='text-align: right;'>"+FRMHandler.userFormatStringDecimal(total)+"</td>";
                    }
                    result += "</tr>";
                    
            result +=""
                + "</tbody>"
            + "</table>"
            + "<br/>"
            + "<img style='cursor:pointer' src='../../../images/BtnPrint.jpg' class='printReport'>"
            + "<a style='cursor:pointer' class='printReport'>Print Report</a>";
        }else{
            result += ""
                + "<table width='100%' border='1' cellspacing='1' cellpadding='1'>"
                    + "<tr>"
                        +"<td style='width:100%'><center>No Data</center></td>"
                    + "</tr>"
                + "</table>"
                + "";
        }
        
        
        return result;
    }
    
    private double getTotalByCategory(Long oidBillMain, int categoryFood){
        double total = 0;
        String whereCatalog="";
        String whereClause="";
        String whereFood="";
        
        if (categoryFood!=0 && categoryFood!=1){
            whereFood = " "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0 AND "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 ";  
        }else{
            whereFood = " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"="+categoryFood+"";
                     
        }
         
        Vector listCategory= PstCategory.list(0,0, whereFood, "");
        
        for (int i=0;i<listCategory.size();i++){
            Category category = (Category) listCategory.get(i);
            if(i==0){
                whereCatalog += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereCatalog += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        
        whereClause =" cbm."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+oidBillMain+"";
        whereClause += " AND ("+whereCatalog+")";
        total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);
        
        
        
        return total;
    }
    
    private double getTotalByCategory2(String date,long shiftId, int categoryFood, long cashCashierId){
        double total = 0;
        double subtotal=0;
        String whereCatalog="";
        String whereClause="";
        String whereFood="";
        
        if (categoryFood!=0 && categoryFood!=1){
            whereFood = " "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0 AND "
                + " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 ";  
        }else{
            whereFood = " "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"="+categoryFood+"";
                     
        }
         
        Vector listCategory= PstCategory.list(0,0, whereFood, "");
        
        for (int i=0;i<listCategory.size();i++){
            Category category = (Category) listCategory.get(i);
            if(i==0){
                whereCatalog += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereCatalog += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        
        whereClause =" "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+cashCashierId+"'"    
            + " AND ("
            + "( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " ) OR ( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
            + " ) OR ( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " ))"
            + " AND DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' ";
        whereClause += " AND ("+whereCatalog+")";
        total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);
        
        
       String  whereClauseTransactionReturn =" "
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+cashCashierId+"'"   
            + " AND ("
            + "( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='1' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " ))"
            + " AND DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' ";
        whereClauseTransactionReturn += " AND ("+whereCatalog+")";
        double totalFoodReturn = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClauseTransactionReturn);
        subtotal = total-totalFoodReturn;
        
        return subtotal;
    }
    
    private double getTotalByTransaction(String date,long shiftId, int transactionStatus, long paymentType){
        return getTotalByTransaction( date, shiftId,  transactionStatus,  paymentType, 0,"",0);
    }
    
    
    
    private double getTotalByTransaction(String date, long shiftId, int transactionStatus, long paymentType, int typeTransaction, String cashCashierId, long cashierId){
        double total=0;
        String whereClause="";
        String whereReturn ="";
        PaymentSystem paymentSystem;
        
        if(typeTransaction==0){
            if (shiftId!=0){
                whereClause =" "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+cashierId+"'"    
                    + " AND DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' "
                    + " AND ("
                    + "( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + "))";
            }else{
                whereClause =" "
                    + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' ";
            }



            if (transactionStatus==0){
                //TRANSAKSI BELUM DI BAYAR
                whereClause += ""
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+cashierId+"' "    
                    + " AND (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 AND"
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"= 1)";

                total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);

            }else{
                //TRANSAKSI SUDAH DIBAYAR, SESUAI DENGAN CASH PAYMENTNYA
                whereClause += ""
                    + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+paymentType+"";
                total = PstBillMain.getSummaryByPaymentSystem(whereClause);

                //MASALAH RETURN 
                try {
                    paymentSystem = PstPaymentSystem.fetchExc(paymentType);
                } catch (Exception e) {
                    paymentSystem = new PaymentSystem();
                }

                if (paymentSystem.getPaymentType()==1){
                    double totalReturn = 0;
                    if (shiftId!=0){
                        whereReturn = ""
                            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' "
                            + " AND cbm.SHIFT_ID ="+shiftId+""
                            + " AND cbm.CASH_CASHIER_ID ="+cashierId+""
                            + " AND ("
                            + "( cbm."
                            + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                            + ") OR ( cbm."
                            + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
                            + ") OR ( cbm."
                            + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                            + "))";
                    }else{
                        whereReturn = ""
                            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' ";       
                    }

                    totalReturn = PstCashReturn.getReturnByBillMainDateTransactionReturn(whereReturn);
                    total = total- totalReturn;
                }
            }
        }else if (typeTransaction==1){
            //mencari total per summary
            if (shiftId!=0){
                whereClause =" "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
                    + " AND DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' "
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+cashierId+"'"      
                    + " AND ("
                    + "( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + "))";
            }else{
                whereClause =" "
                    + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' ";
            }



            if (transactionStatus==0){
                //TRANSAKSI BELUM DI BAYAR
                whereClause += ""
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 AND"
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"= 1";

                total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);

            }else{
                //TRANSAKSI SUDAH DIBAYAR, SESUAI DENGAN CASH PAYMENTNYA
                whereClause += ""
                    + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+paymentType+"";
                total = PstBillMain.getSummaryByPaymentSystem(whereClause);

                //MASALAH RETURN 
                try {
                    paymentSystem = PstPaymentSystem.fetchExc(paymentType);
                } catch (Exception e) {
                    paymentSystem = new PaymentSystem();
                }

                if (paymentSystem.getPaymentType()==1){
                    double totalReturn = 0;
                    if (shiftId!=0){
                        whereReturn = ""
                            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' "
                            + " AND cbm.SHIFT_ID ="+shiftId+""
                            + " AND cbm.CASH_CASHIER_ID ="+cashierId+""
                            + " AND ("
                            + "( cbm."
                            + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='1' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                            + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                            + "))";    
                    }else{
                        whereReturn = ""
                            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' ";       
                    }

                    totalReturn = PstCashReturn.getReturnByBillMainDateTransactionReturn(whereReturn);
                    total = total- totalReturn;
                }
            }    
        }else{
            //mencari total keseluruahn
            if (shiftId!=0){
                whereClause =" "
                    + " ("+cashCashierId+" )"
                    + " AND ("
                    + "( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + "))";
            }else{
                whereClause =" "
//                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+shiftId+""
//                    + " AND DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")='"+date+"' "
                    + " ("+cashCashierId+" )"  
                    + " AND ("
                    + "( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + "))";
            }



            if (transactionStatus==0){
                //TRANSAKSI BELUM DI BAYAR
                whereClause += ""
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 AND"
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"= 1";

                total = PstBillDetail.getSumTotalPriceByCategoryAndBillMain(whereClause);

            }else{
                //TRANSAKSI SUDAH DIBAYAR, SESUAI DENGAN CASH PAYMENTNYA
                whereClause += ""
                    + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+paymentType+"";
                total = PstBillMain.getSummaryByPaymentSystem(whereClause);

                //MASALAH RETURN 
                try {
                    paymentSystem = PstPaymentSystem.fetchExc(paymentType);
                } catch (Exception e) {
                    paymentSystem = new PaymentSystem();
                }

                if (paymentSystem.getPaymentType()==1){
                    double totalReturn = 0;
                    if (shiftId!=0){
                        whereReturn = ""
                            + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' "    
                            + " AND cbm.SHIFT_ID ="+shiftId+"";
                    }else{
                        whereReturn = ""
                            + " ("+cashCashierId+" )";       
                            //+ " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")= '"+date+"' ";       
                    }

                    totalReturn = PstCashReturn.getReturnByBillMainDateTransactionReturn(whereReturn);
                    total = total- totalReturn;
                }
            }
            
        }
        
        return total;
    }
   
    private double getTotalPaymentByPaymentSystem(Long oidBillMain,Long oidPaymentSystem){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        
        wherePaymentSystem =""
            + " ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"" 
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ="+oidBillMain+"";
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        
        whereReturnSystem =""
            + " "+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"="+oidBillMain+"";
        Vector listReturnSystem = PstCashReturn.list(0, 0, whereReturnSystem, "");
        if (listReturnSystem.size()>0){
            CashReturn cashReturn = (CashReturn) listReturnSystem.get(0);
            totalReturn = cashReturn.getAmount();
        }
        
        if (totalPayment>0){
            total = totalPayment - totalReturn;
        }else{
            total = totalPayment;
        }
        
        return total;
    }
    
    private double getTotalPaymentByPaymentSystem(String whereMainId, String whereMainReturn, Long oidPaymentSystem){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        
        wherePaymentSystem =""
            + " ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"" 
            + " AND "+whereMainId;//cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ="+oidBillMain+"";
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        
        whereReturnSystem =""+whereMainReturn;
            //+ " "+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"="+oidBillMain+"";
        Vector listReturnSystem = PstCashReturn.list(0, 0, whereReturnSystem, "");
        if (listReturnSystem.size()>0){
            CashReturn cashReturn = (CashReturn) listReturnSystem.get(0);
            totalReturn = cashReturn.getAmount();
        }
        
        if (totalPayment>0){
            total = totalPayment - totalReturn;
        }else{
            total = totalPayment;
        }
        
        return total;
    }
    
    
    private double getSumaryTotalPaymentByPaymentSystem(String whereMainId, String whereMainReturn, Long oidPaymentSystem, boolean isCash){
        double total = 0;
        double totalPayment = 0;
        double totalReturn = 0;
        String wherePaymentSystem = "";
        String whereReturnSystem = "";
        
        wherePaymentSystem =""+ " ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]+" = "+oidPaymentSystem+"";
        
        if(!whereMainId.equals("")){
            wherePaymentSystem=wherePaymentSystem+ " AND "+whereMainId;//cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" ="+oidBillMain+"";
        }
            
        totalPayment = PstBillMain.getSummaryByPaymentSystem(wherePaymentSystem);
        
        whereReturnSystem =" cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+"='"+oidPaymentSystem+"'";
        
        if(!whereMainReturn.equals("")){
            whereMainReturn=whereMainReturn+ " AND "+whereMainReturn;
        }
            //+ " "+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+"="+oidBillMain+"";
        //if(isCash){
            totalReturn = PstCashReturn.getReturnByBillMainDateTransactionReturn(whereReturnSystem);
        //}
//        if (listReturnSystem.size()>0){
//            CashReturn cashReturn = (CashReturn) listReturnSystem.get(0);
//            totalReturn = cashReturn.getAmount();
//        }
        
        if (totalPayment>0){
            total = totalPayment - totalReturn;
        }else{
            total = totalPayment;
        }
        
        return total;
    }
    
    private String getBodyTopCategory(HttpServletRequest request){
        String result ="";
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        String whereTemp ="";
        String kategoriOrder="";
        String payment="";
        Location location;
        Negara negara;
        Category category;
        Sales sales;
        String namaNegara, namaKategori, namaSales = "";
        
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        long country = FRMQueryString.requestLong(request, "country");
        long categoryId= FRMQueryString.requestLong(request, "categoryId");
        int categoryOrder = FRMQueryString.requestInt(request, "FRM_FIELD_SORTBY");
        long salesId = FRMQueryString.requestLong(request, "sales");
        int paymentType = FRMQueryString.requestInt(request, "paymentType");
        int lang = FRMQueryString.requestInt(request, "lang"); 
        
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        
        try {
            location = new Location();
            location = PstLocation.fetchExc(locationId);
        } catch (Exception e) {
            location = new Location();
        }
        
        if (categoryOrder==0){
            kategoriOrder = "Dine In";
        }else if(categoryOrder==1){
            kategoriOrder = "Take Away";
        }else if (categoryOrder==2){
            kategoriOrder ="Delivery Order";
        }else{
            kategoriOrder = ""+comboBox[lang][5]+"";
        }
        
        if (paymentType==1){
            payment=""+comboBox[lang][4]+"";
        }else if(paymentType==0){
            payment=""+comboBox[lang][3]+"";
        }else{
            payment=""+comboBox[lang][5]+"";
        }
        
        if (country==0){
            namaNegara = comboBox[lang][5];
        }else{
            try {
                negara = new Negara();
                negara = PstNegara.fetchExc(country);
            } catch (Exception e) {
                negara = new Negara();
            }
            namaNegara = negara.getNmNegara();
        }
        
        if (categoryId==0){
            namaKategori = comboBox[lang][5];
        }else{
            try {
                category = new Category();
                category = PstCategory.fetchExc(categoryId);
            } catch (Exception e) {
                category = new Category();
            }
            namaKategori = category.getName();
        }
        
        if (salesId==0){
            namaSales = comboBox[lang][5];
        }else{
            try {
                sales = new Sales();
                sales = PstSales.fetchExc(salesId);
            } catch (Exception e) {
                sales = new Sales();
            }
            namaSales = sales.getName();
        }

        result += ""
            + "<table cellpadding='3' cellspacing='0' width='100%'>"
                + "<tbody>"
                    + "<tr align='left' valign='top'>"
                        +"<td colspan='6' align='center' height='14' valign='middle'>"
                            + "<h4>"+textListHeader2[lang][0]+"</h4>"
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][1]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+fromDate+" s/d "+toDate+""
                        +"</td>"
                        //
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][5]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+namaNegara+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][2]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+location.getName()+""
                        +"</td>"
                        //
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][6]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+namaKategori+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][3]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+kategoriOrder+""
                        +"</td>"
                        //
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][7]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+namaSales+""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader2[lang][4]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+payment+""
                        +"</td>"
                        //
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ""
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:100%' colspan='6'  height='14' valign='middle'>"
                            + "&nbsp;"
                        +"</td>"                        
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:100%' colspan='6'  height='14' valign='middle'>"
                            //GET REPORT
                            + getReportTopMenu(locationId, country, categoryId, categoryOrder, salesId,  paymentType, lang, fromDate,  toDate )
                            + ""
                        +"</td>"                        
                    + "</tr>"
                    
            + "</table>";
        
        
        
        
        return result;
    }
    
    private String getReportTopMenu(long locationId, long country, long categoryId, long categoryOrder, long salesId, int paymentType,int lang,String dateFrom, String dateTo ){
        String result ="";
        String whereFood ="";
        String whereBeverage="";
        String whereOther="";
        String whereOrder="";
        String whereCategory="";
        Negara entNegara = new Negara();
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        
        try {
            entNegara = PstNegara.fetchExc(country);
        } catch (Exception e) {
        }
        
        
        String whereBillMain = " "
            + " "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+locationId+""
            + " AND (DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")>='"+dateFrom+"'"
            + " AND DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")<='"+dateTo+"')"
            + " AND ("
            + " ( "
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " ) OR ( "
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
            + " ) OR ( "
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' " 
            + " ))";
        
        //TAMBAHAN APABILA ADA SALES
        if(salesId!=0){
            whereBillMain += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]+"="+salesId+"";
        }
        
        //TAMBAHAN APABILA ADA NEGARA
        if (entNegara.getOID()!=0){
            whereBillMain += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_COUNTRY]+" like '%"+entNegara.getNmNegara()+"%'";
        }
        
        //DINE IN, ATAU TAKE AWAY, ATAU DELIVERY
        if (categoryOrder==0){
            //DINE IN (ADA TABLE )
            whereBillMain += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"<>''";
        }else if (categoryOrder==1){
            // TAKE AWAY (TIDAK ADA TABLE DAN TIDAK ADA ALAMAT )
            whereBillMain += ""
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=''"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"=''";
        }else if (categoryOrder==2){
            // DELIVERY ORDER (TIDAK ADA TABLE NAMUN ADA ALAMAT)
            whereBillMain += ""
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=''"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"<>''";
        }
        
        //PAYMENT, CASH ATAU CREDIT
        if (paymentType==0){
            //LUNAS 
            whereBillMain += ""
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"=0";
        }else if (paymentType==1){
            whereBillMain += ""
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DISC_TYPE]+"=0"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1"
                + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"=1";
        }
        
        Vector listBillMain = PstBillMain.list(0, 0, whereBillMain, "");
        String whereBillDetail ="";
        String whereBills ="";
        for (int a=0;a<listBillMain.size();a++){
            BillMain billMain = (BillMain)listBillMain.get(a);
            if (a==0){
                whereBillDetail += " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+" IN ( "+billMain.getOID()+"";                 
                whereBills += ""+billMain.getOID()+"";
            }else{
                whereBillDetail += ","+billMain.getOID()+"";
                whereBills += ","+billMain.getOID()+"";
            }
        }
        whereBillDetail += ")";
        
        
        
        //FOOD, BEVERAGE OTHER
        
        //WHERE FOOD
        Vector listCategoryMaterial = PstCategory.list(0,0, ""+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"=0", "");
        for (int b=0;b<listCategoryMaterial.size();b++){
            Category category = (Category) listCategoryMaterial.get(b);
            if(b==0){
                whereFood += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereFood += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        
        //WHERE BEVERAGE
        listCategoryMaterial = PstCategory.list(0,0, ""+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"=1", "");
        for (int c=0;c<listCategoryMaterial.size();c++){
            Category category = (Category) listCategoryMaterial.get(c);
            if(c==0){
                whereBeverage += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereBeverage += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        
        //WHERE OTHER 
        listCategoryMaterial = PstCategory.list(0,0, ""+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>1 AND "+PstCategory.fieldNames[PstCategory.FLD_CATEGORY]+"<>0", "");
        for (int d=0;d<listCategoryMaterial.size();d++){
            Category category = (Category) listCategoryMaterial.get(d);
            if(d==0){
                whereOther += " pm.CATEGORY_ID='"+category.getOID()+"'";
            }else{
                whereOther += " or pm.CATEGORY_ID='"+category.getOID()+"'";
            }
        }
        
        
        
        if (categoryId!=0){
            //KHUSUS MENGARAH KE KATEGORI TERTENTU
           whereFood=" pm.CATEGORY_ID="+categoryId+""; 
           whereBeverage=" pm.CATEGORY_ID="+categoryId+"";
           whereOther=" pm.CATEGORY_ID="+categoryId+"";
        }
        
        String whereTopFood = whereBillDetail + " AND (" + whereFood + ") GROUP BY cbd."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" ORDER BY cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+" DESC";
        String whereTopBeverage = whereBillDetail + " AND( " + whereBeverage + ") GROUP BY cbd."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" ORDER BY cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+" DESC";
        String whereTopOther = whereBillDetail + " AND (" + whereOther + ") GROUP BY cbd."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" ORDER BY cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+" DESC";
       
        Vector listTopFood = PstBillDetail.listJoinMaterialUnit(0, 0, whereTopFood, "");
        Vector listTopBeverage = PstBillDetail.listJoinMaterialUnit(0, 0, whereTopBeverage, "");
        Vector listTopOther = PstBillDetail.listJoinMaterialUnit(0, 0, whereTopOther, "");
        
        result += ""
            + "<table style='width: 49%; float: left; margin-right: 1%' border='1' cellspacing='1' cellpadding='1'>"
                + "<tr>"
                    + "<td colspan='1'>"+textListHeader3[lang][0]+"</td>"
                    + "<td colspan='6'><center>"+textListHeader3[lang][7]+"</center></td>"                   
                + "</tr>"
                + "<tr>"
                    + "<td colspan='1'></td>"
                    + "<td colspan='1'>"+textListHeader3[lang][1]+"</td>"
                    + "<td colspan='1'>"+textListHeader3[lang][2]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][3]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][4]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][5]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][6]+"</td>" 
                + "</tr>";
                for (int tf = 0; tf<listTopFood.size();tf++){
                    Vector temp = (Vector) listTopFood.get(tf);
                    Billdetail entBilldetail = (Billdetail)temp.get(0);
                    Material entMaterial = (Material)temp.get(1);
                    Unit entUnit = (Unit) temp.get(2);
                    int noUrut = tf+1;
                    
                    String tamp[] = entMaterial.getName().split(";");
                    
                    //GET PAX PER MATERIAL
                    String wherePaxMaterial = ""
                        + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "+entBilldetail.getMaterialId()+" "
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" IN ("+whereBills+")" 
                        + " GROUP BY cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"";
                    
                    int paxCount = PstBillMain.getSumPaxByMaterial(wherePaxMaterial);
                    
                    result += ""
                        + "<tr>"
                            + "<td>"+noUrut+"</td>"
                            + "<td>"+entMaterial.getSku()+"</td>"
                            + "<td>"+tamp[0]+"</td>"
                            + "<td style='text-align:right'>"+entBilldetail.getQty()+"</td>"
                            + "<td>"+entUnit.getName()+"</td>"
                            + "<td style='text-align:right'>"+decimalFormat.format(entBilldetail.getTotalPrice())+"</td>" 
                            + "<td style='text-align:right'>"+paxCount+"</td>" 
                        + "</tr>";
                }
        result += ""
            + "</table>";
        
        //BEVERAGE
        result += ""
            + "<table style ='width:50%' border='1' cellspacing='1' cellpadding='1'>"
                + "<tr>"
                    + "<td colspan='1'>"+textListHeader3[lang][0]+"</td>"
                    + "<td colspan='6'><center>"+textListHeader3[lang][8]+"</center></td>"                   
                + "</tr>"
                + "<tr>"
                    + "<td colspan='1'></td>"
                    + "<td colspan='1'>"+textListHeader3[lang][1]+"</td>"
                    + "<td colspan='1'>"+textListHeader3[lang][2]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][3]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][4]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][5]+"</td>" 
                    + "<td colspan='1'>"+textListHeader3[lang][6]+"</td>" 
                + "</tr>";
                for (int tb = 0; tb<listTopBeverage.size();tb++){
                    Vector temp = (Vector) listTopBeverage.get(tb);
                    Billdetail entBilldetail = (Billdetail)temp.get(0);
                    Material entMaterial = (Material)temp.get(1);
                    Unit entUnit = (Unit) temp.get(2);
                    int noUrut = tb+1;
                    
                    String tamp[] = entMaterial.getName().split(";");
                    
                    //GET PAX PER MATERIAL
                    String wherePaxMaterial = ""
                        + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+" = "+entBilldetail.getMaterialId()+" "
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" IN ("+whereBills+")" 
                        + " GROUP BY cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"";
                    
                    int paxCount = PstBillMain.getSumPaxByMaterial(wherePaxMaterial);
                    
                    result += ""
                        + "<tr>"
                            + "<td>"+noUrut+"</td>"
                            + "<td>"+entMaterial.getSku()+"</td>"
                            + "<td>"+tamp[0]+"</td>"
                            + "<td style='text-align:right'>"+entBilldetail.getQty()+"</td>"
                            + "<td>"+entUnit.getName()+"</td>"
                            + "<td style='text-align:right'>"+decimalFormat.format(entBilldetail.getTotalPrice())+"</td>" 
                            + "<td style='text-align:right'>"+paxCount+"</td>" 
                        + "</tr>";
                }
        result += ""
            + "</table>"
            + "<br/>" 
            + "<img style='cursor:pointer' src='../../../images/BtnPrint.jpg' class='printReport'>" 
            + "<a style='cursor:pointer' class='printReport'>Print Report</a>";
        
        return result;
    }
    
    private String getBodyCCReport(HttpServletRequest request){
        String result ="";
        Location location ;

        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        int lang = FRMQueryString.requestInt(request, "lang"); 
        
        try {
            location = new Location();
            location = PstLocation.fetchExc(locationId);
        } catch (Exception e) {
            location = new Location();
        }
        
        result += ""
            + "<table cellpadding='3' cellspacing='0' width='100%'>"
                + "<tbody>"
                    + "<tr align='left' valign='top'>"
                        +"<td colspan='3' align='center' height='14' valign='middle'>"
                            + "<h4>"+textListHeader4[lang][0]+"</h4>"
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader4[lang][2]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+fromDate+" s/d "+toDate+""
                        +"</td>"
                        //
                    + "</tr>"
                    + "<tr>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader4[lang][1]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+location.getName()+""
                        +"</td>"
                    + "</tr>"                   
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:100%' colspan='3'  height='14' valign='middle'>"
                            //GET REPORT
                            +getReportCC(locationId,fromDate, toDate,lang )
                            + ""
                        +"</td>"                        
                    + "</tr>"
                    
            + "</table>";
        
        return result;
    }
    
    private String getReportCC(long locationId,String fromDate, String toDate,int lang ){
        String result ="";
        String whereReportCC="";
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        
        whereReportCC += ""
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = 0 "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = 0 "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = 0 "
            + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_INFO_OUT]+" = 0 "
            + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO]+" = 1 "
            + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO]+" = 0 "
            + " AND ps."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE]+"=0"   
            + "";
        
        whereReportCC += ""
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+locationId+""
            + " AND (DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")>= '"+fromDate+"'"
            + " AND DATE ("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")<= '"+toDate+"')";
        
        Vector listCreditCardTrans = PstBillMain.listCreditCardTransaction(0, 0, whereReportCC, "");
        if (listCreditCardTrans.size()>0){
            result += ""
            + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                + "<tr>"
                    + "<td>"+textListHeader4[lang][3]+"</td>"
                    + "<td>"+textListHeader4[lang][4]+"</td>"
                    + "<td>"+textListHeader4[lang][5]+"</td>"
                    + "<td>"+textListHeader4[lang][6]+"</td>"
                    + "<td>"+textListHeader4[lang][7]+"</td>"
                    + "<td>"+textListHeader4[lang][8]+"</td>"
                    + "<td>"+textListHeader4[lang][9]+"</td>"
                + "</tr>";
                for (int i = 0;i<listCreditCardTrans.size();i++){
                    
                    Vector dataTemp = (Vector) listCreditCardTrans.get(i);
                    BillMain billMain =(BillMain) dataTemp.get(0);
                    CashPayments cashPayments =(CashPayments) dataTemp.get(1);
                    PaymentSystem paymentSystem =(PaymentSystem) dataTemp.get(2);
                    CashCreditCard cashCreditCard =(CashCreditCard) dataTemp.get(3);
                    
                    result += "<tr>";                   
                    result += "<td>"+billMain.getInvoiceNumber()+"</td>";
                    result += "<td>"+billMain.getBillDate()+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(cashPayments.getAmount())+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(cashPayments.getAmount())+"</td>";
                    result += "<td>"+paymentSystem.getPaymentSystem()+"</td>";
                    result += "<td>"+cashCreditCard.getCcNumber()+"</td>";
                    result += "<td>"+cashCreditCard.getCcName()+"</td>";
                    result += "</tr>";
                }
        result += ""
            + "</table>"
            + "<br/>" 
            + "<img style='cursor:pointer' src='../../../images/BtnPrint.jpg' class='printReport'>" 
            + "<a style='cursor:pointer' class='printReport'>Print Report</a>";
        }else{
            result += ""
                + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                    + "<tr>"
                        + "<td><center>No Data</center></td>"
                    + "</tr>"
                + "</table>";
        }
        
        
        return result;
    }
    
    private String getComplimentBodyReport(HttpServletRequest request){
        String result ="";                
        Location location ;

        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        int lang = FRMQueryString.requestInt(request, "lang"); 
        
        try {
            location = new Location();
            location = PstLocation.fetchExc(locationId);
        } catch (Exception e) {
            location = new Location();
        }
        
        result += ""
            + "<table cellpadding='3' cellspacing='0' width='100%'>"
                + "<tbody>"
                    + "<tr align='left' valign='top'>"
                        +"<td colspan='3' align='center' height='14' valign='middle'>"
                            + "<h4>"+textListHeader5[lang][0]+"</h4>"
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader5[lang][2]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+fromDate+" s/d "+toDate+""
                        +"</td>"
                        //
                    + "</tr>"
                    + "<tr>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader5[lang][1]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+location.getName()+""
                        +"</td>"
                    + "</tr>"                   
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:100%' colspan='3'  height='14' valign='middle'>"
                            //GET REPORT
                            +getComplimentReport(locationId,fromDate, toDate,lang )
                            + ""
                        +"</td>"                        
                    + "</tr>"    
            + "</table>";
        
        return result;
    }
    
    private String getComplimentReport(long locationId, String fromDate, String toDate, int lang){
        String result ="";
        String whereCosting="";
        String whereDetailCosting="";
        Location location = new Location();
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        
        try {
            location = PstLocation.fetchExc(locationId);
        } catch (Exception e) {
        }
        
        whereCosting = " "
            + " "+PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]+"="+locationId+""
            + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") >= '"+fromDate+"'"
            + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") <= '"+toDate+"'";
        
        Vector listCosting = PstMatCosting.list(0, 0, whereCosting, "");
        
        if (listCosting.size()>0){
            result += ""
            + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                + "<tr>"
                    + "<td>No</td>"
                    + "<td>"+textListHeader5[lang][3]+"</td>"
                    + "<td>"+textListHeader5[lang][4]+"</td>"
                    + "<td>"+textListHeader5[lang][5]+"</td>"
                    + "<td>"+textListHeader5[lang][6]+"</td>"
                    + "<td>"+textListHeader5[lang][7]+"</td>"
                    + "<td>"+textListHeader5[lang][8]+"</td>"
                    + "<td>"+textListHeader5[lang][9]+"</td>"
                    + "<td>"+textListHeader5[lang][10]+"</td>"
                    + "<td>"+textListHeader5[lang][11]+"</td>"
                    + "<td>"+textListHeader5[lang][12]+"</td>"
                + "</tr>";
                int noUrut=0;
                for (int i = 0;i<listCosting.size();i++){
                    MatCosting matCosting = (MatCosting)listCosting.get(i);
                    whereDetailCosting = " "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
                    Vector listDetailCosting = PstMatCostingItem.list(0, 0, matCosting.getOID(), whereDetailCosting);
                    noUrut=noUrut+1;
                    for (int a = 0; a<listDetailCosting.size();a++){
                        Vector dataTemp = (Vector) listDetailCosting.get(a);
                        MatCostingItem detailCosting = (MatCostingItem) dataTemp.get(0);
                        Material mat = (Material) dataTemp.get(1);
                        Unit unit = (Unit) dataTemp.get(2);
                        String s3="";
                        String s1="";
                        String s0="";
                        try{
                            //String remarkTemp[] = matCosting.getRemark().split(";");
                            String nameTemp [] = mat.getName().split(";");
                           // s3=remarkTemp[3];
                            s1=matCosting.getRemark();
                            s0=nameTemp[0];
                        }catch(Exception ex){
                        }
                        double qty=0;
                        if(mat.getMaterialType()==0){
                            qty=detailCosting.getQty();
                        }else{
                            qty=detailCosting.getQtyComposite();
                        }
                        
                        result += "<tr>";
                        if (a==0){
                            result += "<td rowspan='"+listDetailCosting.size()+"'>"+noUrut+"</td>";
                            result += "<td rowspan='"+listDetailCosting.size()+"'>"+matCosting.getCostingDate()+"</td>";
                            result += "<td rowspan='"+listDetailCosting.size()+"'>"+matCosting.getCostingCode()+"</td>";
                        }
                        result += "<td>"+s0+"</td>";
                        result += "<td style='text-align:right'>"+qty+"</td>";
                        result += "<td>"+unit.getCode()+"</td>";
                        
                        //price
                        double price = PstPriceTypeMapping.getPrice(detailCosting.getMaterialId(), location.getStandarRateId(), location.getPriceTypeId());
                        result += "<td style='text-align:right'>"+decimalFormat.format(price)+"</td>";                        
                        result += "<td style='text-align:right'>"+decimalFormat.format(price*qty)+"</td>";
                        result += "<td style='text-align:right'>"+decimalFormat.format(detailCosting.getHpp())+"</td>";
                        double total = qty * detailCosting.getHpp();
                        result += "<td style='text-align:right'>"+decimalFormat.format(total)+"</td>";
                        if (a==0){
                            result += "<td rowspan='"+listDetailCosting.size()+"'>"+s1+"</td>";
                        }
                        
                        result += "</tr>";
                    }
                    
                }
        result += ""
            + "</table>"
            + "<br/>" 
            + "<img style='cursor:pointer' src='../../../images/BtnPrint.jpg' class='printReport'>" 
            + "<a style='cursor:pointer' class='printReport'>Print Report</a>";
        
        
        }else{
            result += ""
                + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                    + "<tr>"
                        + "<td><center>No Data</center></td>"
                    + "</tr>"
                + "</table>";
        }
        
        return result;
    }
    
    private String getSummaryBody(HttpServletRequest request){
        String result ="";               
        Location location ;

        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
        
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        String fromDate =  ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_FROM]+"_dy")+"";
        String toDate = ""
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_yr")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_mn")+"-"
            + ""+FRMQueryString.requestString(request,""+frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_DATE_TO]+"_dy")+""; 
        int lang = FRMQueryString.requestInt(request, "lang"); 
        
        try {
            location = new Location();
            location = PstLocation.fetchExc(locationId);
        } catch (Exception e) {
            location = new Location();
        }
        
        result += ""
            + "<table cellpadding='3' cellspacing='0' width='100%'>"
                + "<tbody>"
                    + "<tr align='left' valign='top'>"
                        +"<td colspan='3' align='center' height='14' valign='middle'>"
                            + "<h4>"+textListHeader6[lang][0]+"</h4>"
                        +"</td>"
                    + "</tr>"
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader6[lang][2]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+fromDate+" s/d "+toDate+""
                        +"</td>"
                        //
                    + "</tr>"
                    + "<tr>"
                        +"<td style='width:9%' colspan='1'  height='14' valign='middle'>"
                            + ""+textListHeader6[lang][1]+""
                        +"</td>"
                        +"<td style='width:1%' colspan='1'  height='14' valign='middle'>"
                            + ":"
                        +"</td>"
                        +"<td colspan='1' style='width:40%'  height='14' valign='middle'>"
                            + ""+location.getName()+""
                        +"</td>"
                    + "</tr>"                   
                    + "<tr align='left' valign='top'>"
                        +"<td style='width:100%' colspan='3'  height='14' valign='middle'>"
                            //GET REPORT
                            +getSummaryReport(location,fromDate, toDate,lang )
                            + ""
                        +"</td>"                        
                    + "</tr>"    
            + "</table>";
               
        return result;
    }
    
    private String getSummaryReport(Location location,String fromDate, String toDate, int lang ){
        String result="";
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        
        //CEK CASH CASHIER YANG ADA
        
        String cashCashierID = PstBillMain.listCashCashierPerDay(fromDate, toDate,0,"cash_master.LOCATION_ID='"+location.getOID()+"'");
        String cashCashierID2 = PstBillMain.listCashCashierPerDay(fromDate, toDate,1,"cash_master.LOCATION_ID='"+location.getOID()+"'");
        
        //GET THE PAYMENT TYPE THAT USED ON PAID TRANSACTION
        String whereCashPayment = ""
//            + " DATE(cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+")>='"+fromDate+"'"
//            + " AND DATE(cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+")<='"+toDate+"'"
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+location.getOID()+""
            + " AND("
            + " ( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " ) OR ( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
            + " ) OR ( cbm."
            +   PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + " )) AND ( "+cashCashierID+") ";
        
        Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");
        
        //GET LIST TRANSACTION PER DAY (ALREADY PAID, NO MATTER CASH OR CREDIT)
        String whereForPlus = ""
            + "("
                + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + ") OR ("
                + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
            + ") OR ("
                + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                + "AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
            + ")";

        String whereForMin =""
            + "";
        
        String whereBillMain = ""
//            + " DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")>='"+fromDate+"'"
//            + " AND DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+")<='"+toDate+"'"
            + " ("+whereForPlus+")"
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+location.getOID()+""
            + " AND ( "+cashCashierID2+") "
            + " GROUP BY DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") , CASH_CASHIER_ID "
            + " ORDER BY DATE("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") ASC";
        
        Vector listBillMain = PstBillMain.listSummaryTranscationGroupByDate(0, 0, whereBillMain);
        
//        
//        double totalSumFood=0;
//        double totalSumBeverage=0;
//        double totalSumOther=0;
//        double totalSumDiscount=0;
//        double totalSumService=0;
//        double totalSumTax=0;
//        double totalSumSub=0;
//        
        double totalFoodADay=0;
        double totalBeverageADay=0;
        double totalOtherADay=0;
        double totalCreditADay = 0;
        double totalSumDiscount = 0;
        double totalSumService = 0;
        double totalSumTax = 0;
        double totalNetADay = 0;
        double totalBrutoADay = 0;
        
        if (listBillMain.size()>0){
           //INIT THE HEAD OF THE TABLE
           result += ""
            + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                + "<tr>"
                    + "<td>"+textListHeader6[lang][3]+"</td>"
                    + "<td>"+textListHeader6[lang][4]+"</td>"
                    + "<td>"+textListHeader6[lang][5]+"</td>"
                    + "<td>"+textListHeader6[lang][6]+"</td>"
                    + "<td>"+textListHeader6[lang][7]+"</td>"
                    + "<td>"+textListHeader6[lang][8]+"</td>"
                    + "<td>"+textListHeader6[lang][9]+"</td>"
                    + "<td>"+textListHeader6[lang][10]+"</td>"
                    + "<td>"+textListHeader6[lang][11]+"</td>"
                    + "<td>"+textListHeader6[lang][12]+"</td>";
                    //PAYMENT TYPE EXCEPT CREDIT
                    for(int a=0;a<listPaymentSystem.size();a++){
                        PaymentSystem paymentSystem = (PaymentSystem)listPaymentSystem.get(a);
                        result += "<td>"+paymentSystem.getPaymentSystem()+"</td>";
                    }
            result += ""
                    + "<td>"+textListHeader6[lang][13]+"</td>"
                    + "<td>"+textListHeader6[lang][14]+"</td>"
                + "</tr>";
           for (int i = 0 ; i<listBillMain.size();i++){
                BillMain billMain = (BillMain)listBillMain.get(i);
                //GET TRANSACTION THIS DAY GROUP BY SHIFT
                
                //VARIABLE USING FOR TOTAL
                  
                String whereMainByShift=""
                    + " DATE(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+") = '"+billMain.getBillDate()+"'"
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"='"+billMain.getCashCashierId()+"' "
                    + " AND ("
                    + "( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='1' "
                    + ") OR ( cbm."
                    + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='1' "
                    + "AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"='0' "
                    + "))"
                    + " GROUP BY cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"";
                Vector listBillMainShift = PstBillMain.listSummaryTranscationGroupByShift(0, 0, whereMainByShift,true);
                
                for (int j = 0; j<listBillMainShift.size();j++){
                    Vector dataTemp = (Vector)listBillMainShift.get(j);
                    BillMain billMainShift = (BillMain)dataTemp.get(0);
                    Shift shift = (Shift)dataTemp.get(1);
                    
                    
                    result += "<tr>";
                    if (j==0){
                        result += "<td rowspan='"+listBillMainShift.size()+"'>"+billMain.getBillDate()+"</td>";
                    }
                    result += "<td style='text-align:right'>"+billMainShift.getPaxNumber()+"</td>";
                    result += "<td>"+shift.getName()+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(billMainShift.getTaxValue())+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(billMainShift.getServiceValue())+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(billMainShift.getDiscount())+"</td>";
                    
                    double totalFood = getTotalByCategory2(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 0, billMain.getCashCashierId());
                    double totalBeverage = getTotalByCategory2(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 1, billMain.getCashCashierId());
                    double totalOther = getTotalByCategory2(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 5, billMain.getCashCashierId());
                    
                    totalFoodADay += totalFood;
                    totalBeverageADay += totalBeverage;
                    totalOtherADay += totalOtherADay;
                    totalSumDiscount +=billMainShift.getDiscount();
                    totalSumTax += billMainShift.getTaxValue();
                    totalSumService += billMainShift.getServiceValue();
                    
                    result += "<td style='text-align:right'>"+decimalFormat.format(totalFood)+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(totalBeverage)+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(totalOther)+"</td>";
                    //TRANSAKSI BELUM DIBAYAR 
                    double total = getTotalByTransaction( String.valueOf(billMain.getBillDate()),  billMainShift.getShiftId(),  0,  0,  0,"",billMain.getCashCashierId());//getTotalByTransaction(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 0, 0);
                    totalCreditADay += total;
                    result += "<td style='text-align:right'>"+decimalFormat.format(total)+"</td>";
                    //TRANSAKSI SUDAH DI BAYAR 
                    for(int k=0;k<listPaymentSystem.size();k++){
                        PaymentSystem paymentSystem = (PaymentSystem)listPaymentSystem.get(k);
                        double totalPayment = getTotalByTransaction(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 1, paymentSystem.getOID(),0,"",billMain.getCashCashierId());
                        double totalPaymentTransactionReturn = getTotalByTransaction(String.valueOf(billMain.getBillDate()), billMainShift.getShiftId(), 1, paymentSystem.getOID(),1,"",billMain.getCashCashierId());
                        double totalsub = totalPayment - totalPaymentTransactionReturn;
                        result += "<td style='text-align:right'>"+decimalFormat.format(totalsub)+"</td>";                       
                    }
                    double netPerShift = totalFood + totalBeverage + totalOther -billMainShift.getDiscount()+total;
                    double brutoPerShift = totalFood + totalBeverage + totalOther -billMainShift.getDiscount()+total+billMainShift.getTaxValue()+billMainShift.getServiceValue();
                    totalNetADay += netPerShift;
                    totalBrutoADay +=brutoPerShift;
                    result += "<td style='text-align:right'>"+decimalFormat.format(netPerShift)+"</td>";
                    result += "<td style='text-align:right'>"+decimalFormat.format(brutoPerShift)+"</td>";
                    result += "</tr>";
                }
            }
           
            double averagePax = 0;//totalNetADay/billMain.getPaxNumber();
                result += ""
                    + "<tr>"
                        + "<td style='text-align:right'>TOTAL</td>"
                        + "<td style='text-align:right'>"+decimalFormat.format(averagePax)+"</td>"
                        + "<td style='text-align:center'></td>"
                        + "<td style='text-align:right'>"+decimalFormat.format(totalSumTax)+"</td>"//tax
                        + "<td style='text-align:right'>"+decimalFormat.format(totalSumService)+"</td>"//service
                        + "<td style='text-align:right'>"+decimalFormat.format(totalSumDiscount)+"</td>" //disc
                        + "<td style='text-align:right'>"+decimalFormat.format(totalFoodADay)+"</td>"//food
                        + "<td style='text-align:right'>"+decimalFormat.format(totalBeverageADay)+"</td>" //beverage
                        + "<td style='text-align:right'>"+decimalFormat.format(totalOtherADay)+"</td>"//other
                        + "<td style='text-align:right'>"+decimalFormat.format(totalCreditADay)+"</td>";//credit
                        for(int l=0;l<listPaymentSystem.size();l++){
                            PaymentSystem paymentSystemTotal = (PaymentSystem)listPaymentSystem.get(l);
                            double totalPaymentTotal = getTotalByTransaction("", 0, 1, paymentSystemTotal.getOID(),2, cashCashierID,0);
                            double totalPaymentTotalTransactionReturn = getTotalByTransaction("", 1, 1, paymentSystemTotal.getOID(),2, cashCashierID,0);
//                          double totalPayment = getSumaryTotalPaymentByPaymentSystem(""+whereBill,""+whereBillKembali ,paymentSystems.getOID(),true);
//                          double totalPaymentBillReturn = getSumaryTotalPaymentByPaymentSystem(""+whereBillReturn,""+whereBillReturnKembalian,paymentSystems.getOID(),true);
//                          double total = totalPayment-totalPaymentBillReturn;
                            double total = totalPaymentTotal - totalPaymentTotalTransactionReturn;
                            result += "<td style='text-align:right'>"+decimalFormat.format(total)+"</td>";                       
                        }
                result +=""
                    + "<td style='text-align:right'>"+decimalFormat.format(totalNetADay)+"</td>" //net
                     + "<td style='text-align:right'>"+decimalFormat.format(totalBrutoADay)+"</td>" //net    
                    + "</tr>";
            result += ""
                + "</table>"
                + "<br/>" 
                + "<img style='cursor:pointer' src='../../../images/BtnPrint.jpg' class='printReport'>" 
                + "<a style='cursor:pointer' class='printReport'>Print Report</a>";
           
        }else{
            result += ""
                + "<table style='width: 100%;' border='1' cellspacing='1' cellpadding='1'>"
                    + "<tr>"
                        + "<td><center>No Data</center></td>"
                    + "</tr>"
                + "</table>";
        }
        
        return result;
    }
}
