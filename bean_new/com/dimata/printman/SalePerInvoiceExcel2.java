
package com.dimata.printman;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.gui.jsp.ControlList;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.posbo.entity.masterdata.PstRoom;
import com.dimata.posbo.entity.masterdata.Room;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.text.NumberFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class SalePerInvoiceExcel2 extends HttpServlet{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {        
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        
        int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel"); 
        
        HttpSession session = request.getSession(true);
        SrcSaleReport srcSaleReport = new SrcSaleReport();
       
       // srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);

        try {
            srcSaleReport = (SrcSaleReport) session.getValue(SaleReportDocument.SALE_REPORT_DOC);

            if (srcSaleReport == null) {
                srcSaleReport = new SrcSaleReport();
            }
        } catch (Exception e) {
            //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
           srcSaleReport = new SrcSaleReport();
        }
        
        //fungsi untuk menampilkan query laporan
        String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
        String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");
        String createstartDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyyMMdd");
        response.setHeader("Content-Disposition","attachment;filename=rekap_perinvoice_"+createstartDate+".xls");

        String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;

        if(iSaleReportType == PstCashPayment.CASH){
            where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
        }else{
            where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
        }

        if(srcSaleReport.getShiftId()!=0) {
            //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
            where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
        }

        if(srcSaleReport.getLocationId()!=0) {
            //where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
            where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
        }

        if(srcSaleReport.getCurrencyOid()!=0) {
            where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
        }

        //+perCashier
        if (srcSaleReport.getCashMasterId()!=0) {
            //where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+srcSaleReport.getCashMasterId();
            where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
         }

        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        Vector records = PstBillMain.listPerCashier(0, 0, where, order);

        Location location = new Location();
        try{
            location = PstLocation.fetchExc(srcSaleReport.getLocationId());
        }
        catch(Exception e){
            location.setName("Semua toko");
        }

        String cashier = "All Cashier";
        if(srcSaleReport.getCashMasterId() != 0) {
            String whereClause = PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
            Vector listCashier = PstCashMaster.list(0, 0, whereClause, "");
            CashMaster cashMaster = (CashMaster)listCashier.get(0);
            cashier = ""+cashMaster.getCashierNumber();

        }
        
        
        
        
        String result = "" + drawListDetail(records);
        response.getWriter().write(result);
        
    }
    
    public String drawListDetail(Vector objectClass) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setBorder(1);
            ctrlist.dataFormat("ID Transaksi","17%","0","0","center","bottom"); //kode produk34
            ctrlist.dataFormat("ID User","17%","0","0","center","bottom"); //nama produk
            ctrlist.dataFormat("Outlet","9%","0","0","center","bottom"); //harga
            ctrlist.dataFormat("Tanggal Input","9%","0","0","center","bottom"); //diskon
            ctrlist.dataFormat("Tanggal Transaksi","7%","0","0","center","bottom"); //qty
            ctrlist.dataFormat("Tanggal Bayar","5%","0","0","center","bottom"); //unit
            ctrlist.dataFormat("Jatuh Tempo","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("No Bill System","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("No Bill Kertas","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("ID Customer","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Nama Company","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Nama Guide","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Nama Tamu","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Jumlah Pax","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Venue (Kalau Outside)","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Type Event","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Booking Via","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Booking Dari","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Harga Pokok","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Harga Jual","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Service","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Tax","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Total","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Pembayaran","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Saldo","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Saldo Awal","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Saldo Akhir","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Attach","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Remark","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Tanggal Update","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("Type","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("stsdata","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("status","13%","0","0","center","bottom"); //harga total
            ctrlist.dataFormat("view","13%","0","0","center","bottom"); //harga total
           

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector();

            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();

            for(int i=0; i<objectClass.size(); i++) {
                BillMain billMain = (BillMain)objectClass.get(i);
                
                rowx = new Vector();
                rowx.add("<div align='left'>"+""+String.valueOf(billMain.getOID())+"</div>"); //1
                rowx.add("<div align='left'>"+""+String.valueOf(billMain.getAppUserId())+"</div>"); //2
                
                Location location = new Location();
                try {
                    location = PstLocation.fetchExc(billMain.getLocationId());
                } catch (Exception e) {
                }
                rowx.add("<div align='left'>"+location.getName()+"</div>");//3
                rowx.add("<div align='left'>"+Formater.formatDate(billMain.getBillDate(), "MM/dd/yyy")+"</div>");//4
                rowx.add("<div align='left'>"+Formater.formatDate(billMain.getBillDate(), "MM/dd/yyy")+"</div>");//5
                rowx.add("");
                // GET TANGGAL 30 HARI SETELAH TANGGAL TRANSAKSI
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(billMain.getBillDate());
                calendar.add(Calendar.DATE, 30);
                Date tglTemp = calendar.getTime();
               
                rowx.add(""+Formater.formatDate(tglTemp, "MM/dd/yyy")+"");//6
                rowx.add(""+billMain.getInvoiceNumber()+"");//7
                rowx.add("");//BILL KERTAS 8
                rowx.add(""+billMain.getCustomerId()+"");//9
                //GET CUSTOMER DETAIL 
                ContactList contactList = new ContactList();
                try {
                    contactList = PstContactList.fetchExc(billMain.getCustomerId());
                } catch (Exception e) {
                }
                rowx.add("<div align='left'>"+contactList.getCompName()+"</div>");
                rowx.add("");
                rowx.add("<div align='left'>"+contactList.getPersonName()+"</div>");
                rowx.add("<div align='right'>"+billMain.getPaxNumber()+"</div>");                
                //GET VENUE
                Room room = new Room();
                try {
                    room = PstRoom.fetchExc(billMain.getRoomID());
                } catch (Exception e) {
                }
                rowx.add("<div align='left'>"+room.getName()+"</div>");
                rowx.add("<div align='left'></div>");//TYPE EVENT
                rowx.add("<div align='left'></div>");
                rowx.add("<div align='left'></div>");
                
                //GET TOTAL COST , KEMUDIAN DIBAGI JUMLAH PAX
                double TotalCost = PstBillDetail.getSummaryCost(" "+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()+"");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(TotalCost/billMain.getPaxNumber())+"</div>");
                
                double totalPrice = PstBillDetail.getTotalPrice(" "+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()+"");
                double totals = totalPrice + billMain.getTaxValue() + billMain.getServiceValue();
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(totals/billMain.getPaxNumber())+"</div>");
                rowx.add("<div align='right'>"+billMain.getServicePct()+"</div>");
                rowx.add("<div align='right'>"+billMain.getTaxPercentage()+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(totals)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(0)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(totals)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(0)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(0)+"</div>");//SALDO AKHIR
                rowx.add("<div align='right'></div>");
                rowx.add("<div align='right'></div>");
                rowx.add("<div align='left'>"+Formater.formatDate(billMain.getBillDate(), "MM/dd/yyy")+"</div>");
                rowx.add("<div align='left'>Credit</div>");
                rowx.add("<div align='left'>NEW DATA</div>");
                rowx.add("<div align='left'>NOT PAID</div>");
                rowx.add("<div align='left'>YES</div>");
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(0));
            }
            result = ctrlist.draw();
	}
	return result;
} 
    
    
}
