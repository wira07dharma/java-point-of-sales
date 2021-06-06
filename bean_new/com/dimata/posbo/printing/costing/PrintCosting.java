/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.printing.costing;

/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.qdep.form.FRMQueryString;

import com.dimata.util.Formater;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;

import javax.servlet.ServletException;


import java.util.Vector;


import java.io.PrintWriter;

import com.dimata.printman.DSJ_PrintObj;

public class PrintCosting extends HttpServlet {

    //public static String strURL = SessSystemProperty.PROP_APPURL;

    public void init(ServletConfig config) throws ServletException {

        super.init(config);

    }



    /** Destroys the servlet.

     */

    public void destroy() {



    }



    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.

     * @param request servlet request

     * @param response servlet response

     */



    protected void processRequest (HttpServletRequest request, HttpServletResponse response)

    throws ServletException, java.io.IOException {


        int iCommand = FRMQueryString.requestCommand(request);
        int startItem = FRMQueryString.requestInt(request, "start_item");
        int prevCommand = FRMQueryString.requestInt(request, "prev_command");
        int appCommand = FRMQueryString.requestInt(request, "approval_command");
        long oidCostingMaterial = FRMQueryString.requestLong(request, "hidden_costing_id");
        long oidCostingMaterialItem = FRMQueryString.requestLong(request, "hidden_costing_item_id");

    //adding dynamic sign rec by mirahu 20120427
        String signCost1 = PstSystemProperty.getValueByName("SIGN_COSTING_1");
        String signCost2 = PstSystemProperty.getValueByName("SIGN_COSTING_2");
        String signCost3 = PstSystemProperty.getValueByName("SIGN_COSTING_3");

    //adding useBarcode or sku by mirahu 20120426
        int chooseTypeViewSkuOrBcd = 0;
        String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
        if (useBarcodeorSku.equals("Not initialized")) {
            useBarcodeorSku = "0";
        }
        chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);

        String strForeignCurrencyDefault = PstSystemProperty.getValueByName("EXCHANGE_RATE");
        
        String strLocalCurrencyDefault = PstSystemProperty.getValueByName("LOCAL_CURRENCY_DEFAULT");
        
        int start = 0;
        
        
        String whereClauseItemx = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+"="+oidCostingMaterial+
                         " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
        
        Vector listMatCostingItem = PstMatCostingItem.list(startItem, 0, oidCostingMaterial,whereClauseItemx);
        
       /* response.setContentType("text/txt");
        PrintWriter out = null;
        String eol = System.getProperty("line.separator");*/

        response.setContentType("text/txt");
        PrintWriter out = null;
        String eol = System.getProperty("line.separator");
        DSJ_PrintObj prnObj = new DSJ_PrintObj();
        int lineNr = 0;

        //setting margin
        //prnObj.setPageLength(50);
        //prnObj.setPageWidth(50);
        prnObj.setSkipLineIsPaperFix(2);
        prnObj.setLeftMargin(1);
        prnObj.setFont(DSJ_PrintObj.SANS_SERIF);
        prnObj.setCpiIndex(3);

        int tinggiHeaderInputan=10;
        int tinggiFooterReal=3;


        String useHeader = PstSystemProperty.getValueByName("PRINT_SMALL_USE_HEADER");
        String useFooter =PstSystemProperty.getValueByName("PRINT_SMALL_USE_FOOTER");
        int tinggiHeaderReal=Integer.parseInt(PstSystemProperty.getValueByName("PRINT_SMALL_BILL_HIGHT_HEADER"));
        int maxItemOnPage = Integer.parseInt(PstSystemProperty.getValueByName("PRINT_SMALL_MAXIMUM_PER_PAGE"));
        try{
            out = response.getWriter();
            //membuat garis strip

            String curr = "";
//            if(billingRec.getBillingCurrency()==PstBillingRec.CURRENCY_RP){
//                curr = strLocalCurrencyDefault;
//            }
//            else{
//                curr = strForeignCurrencyDefault;
//            }
            //keterangan
            // setLineCenterAlign= untuk format perataan (kiri,kanan,tengah-tengah)
            // 80 : mulai print bari ke 80
            // 20 : panjang karakter yang bisa di print
            //prnObj.setLineCenterAlign(lineNr,80, 10, ""+hotelProfile.getHotelName().toUpperCase().trim());
            //membuat header lokasi bill dari nota
            //tanya use header atau tidak? tanya lagi, apakah use default header atau tidak?
            
            
            Location loc2 = new Location();
            MatCosting costing = new MatCosting();
            try {
                costing = PstMatCosting.fetchExc(oidCostingMaterial);
            } catch (Exception e) {
            }
            
            try {
                loc2 = PstLocation.fetchExc(costing.getLocationId());
            } catch (Exception e) {
            }
            AppUser user = new AppUser();
            try {
                user = PstAppUser.fetch(costing.getCashCashierId());
            } catch (Exception e) {
            }
            
            Employee employee = new Employee();
            try {
                employee = PstEmployee.fetchExc(costing.getContactId());
            } catch (Exception e) {
            }
            
            if(useHeader.equals("0")){
                try {
                    prnObj.addRptLine("-");
                    lineNr = lineNr + 1;
                    prnObj.setLineCenterAlign(lineNr,0, prnObj.getCharacterSelected(), ""+loc2.getName().toUpperCase().trim());

                    lineNr = lineNr + 1; //membuat baris baru
                    prnObj.setLineCenterAlign(lineNr,0, prnObj.getCharacterSelected(), ""+loc2.getAddress());

                    lineNr = lineNr + 1;
                    prnObj.setLineCenterAlign(lineNr,0, prnObj.getCharacterSelected(), ""+"T:" + loc2.getTelephone().trim() + ", F:" + loc2.getFax().trim());

                    lineNr = lineNr + 1;
                    prnObj.setLineCenterAlign(lineNr,0, prnObj.getCharacterSelected(), ""+"W:" + loc2.getWebsite());

                } catch (Exception exc) {
                }

                //spasi
                lineNr = lineNr + 1;
                prnObj.addLine("");

                //informasi tentang jenis bill dan no bill

                String billingOwner = "";
                billingOwner = "COMPLIMENTARY";

                lineNr = lineNr + 1;
                prnObj.setLineRightAlign(lineNr, 0, billingOwner, prnObj.getCharacterSelected());

                /*
                 * ----------------------------------------
                    NO :002.171212.01000001   Tgl:17/12/2012
                    Kasir:kasir               Waktu:15:14:57
                    Waiter:dimata Meja :26
                    ----------------------------------------
                 */

                lineNr = lineNr + 1;
                prnObj.addRptLine("-");

                String dateStr = "";
                //String timeStr ="";

                if(costing.getCostingDate()==null){
                    dateStr = "-";
                } else{
                    dateStr = Formater.formatDate(costing.getCostingDate(),"dd MMMM yyyy") ;
                    //timeStr= Formater.formatDate(billingRec.getDate(),"HH:mm:ss") ;
                }

                String btName = "";
                btName = "__________";

                lineNr = lineNr + 1;
                prnObj.addLine("Number : "+costing.getCostingCode());
                lineNr = lineNr + 1;
                prnObj.addLine("Tanggal : "+dateStr);

                lineNr = lineNr + 1;
                prnObj.addLine("Cshr : "+user.getFullName());
                int addLine=tinggiHeaderReal-tinggiHeaderInputan;
                if (addLine<=0 ){
                     for(int i=0; i<addLine; i++){ //tinggi headernya
                     lineNr = lineNr + 1;
                     prnObj.addLine("");
                 }
                }

            }else if(useHeader.equals("1")){
                
                for(int i=0; i<tinggiHeaderReal; i++){ //tinggi headernya
                     lineNr = lineNr + 1;
                     prnObj.addLine("");
                 }
                
                String dateStr = "";
                //String timeStr ="";

                if(costing.getCostingDate()==null){
                    dateStr = "-";
                } else{
                    dateStr = Formater.formatDate(costing.getCostingDate(),"dd MMMM yyyy") ;
                }
                lineNr = lineNr + 1;
                prnObj.addLine("Cshr : "+user.getFullName());
                
                lineNr = lineNr + 1;
                prnObj.addLine(" Date : "+dateStr);
            }else{
                 for(int i=0; i<tinggiHeaderReal; i++){ //tinggi headernya
                     lineNr = lineNr + 1;
                     prnObj.addLine("");
                 }
            }

            //untuk header detail penjualan
            lineNr = lineNr + 1;
            prnObj.addRptLine("-");
            int colNr = 0;
            int cwNr = 14;
            int colDesc = colNr + cwNr;
            int cwDesc = 10;
            int colAmt$ = colDesc + cwDesc;
            int cwAmt$ = 7;
            int colAmtRp = colAmt$ + cwAmt$;
            int cwAmtRp =12;

            //untuk body - disini di tampilkan barang2 yang di beli
            double totalQty=0.0;
            double totalCost=0.0;
            int baris=0;
            int pageNext=1;
            
            lineNr = lineNr + 1;
            prnObj.setLine(lineNr,"Name");
            prnObj.setLineCenterAlign(lineNr, colDesc, cwDesc, "Cost");   
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "Qty");
            prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp,"Total");
            
            lineNr = lineNr + 1;
            prnObj.addRptLine("-");
            
            
            if(listMatCostingItem!=null && listMatCostingItem.size()>0){
                for(int i=0; i< listMatCostingItem.size(); i++){
                    Vector temp = (Vector) listMatCostingItem.get(i);
                    MatCostingItem costingItemx = (MatCostingItem) temp.get(0);
                    Material mat = (Material) temp.get(1);
                    Unit unit = (Unit) temp.get(2);
                    baris=baris+1;
                        if(baris==maxItemOnPage){
                            pageNext=pageNext+1;

                            for(int h=0; h<tinggiHeaderReal; h++){ //tinggi headernya
                                 lineNr = lineNr + 1;
                                 prnObj.addLine("");
                             }

                             for(int h=0; h<tinggiHeaderReal; h++){ //tinggi headernya
                                 lineNr = lineNr + 1;
                                 prnObj.addLine("");
                             }

                             lineNr = lineNr + 1;
                             prnObj.addLine("page "+pageNext);
                             baris=0;
                        }
                        
                        lineNr = lineNr + 1;
                        String codename = "";
                        codename = mat.getName();
                        String Str = new String(codename);
                        prnObj.setLine(lineNr,codename);
                        if(Str.length()<10){
                            prnObj.setLine(lineNr,codename);
                        }else{
                            try{
                                codename = Str.substring(0, 12);
                            }catch(Exception ex){
                                codename = mat.getName();
                            }
                            prnObj.setLine(lineNr,codename);
                        }
                        

                        if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){
                             totalQty+=costingItemx.getQtyComposite();
                             totalCost+=costingItemx.getTotalHppComposite();
                             prnObj.setLineCenterAlign(lineNr, colDesc, cwDesc, ""+Formater.formatNumber(costingItemx.getTotalHppComposite()/costingItemx.getQtyComposite(), "#,###.##"));   
                             prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, ""+costingItemx.getQtyComposite());
                             prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, ""+Formater.formatNumber(costingItemx.getTotalHppComposite(), "#,###.##"));
                        }else{
                             totalQty+=costingItemx.getQty();
                             totalCost+=costingItemx.getHpp() * costingItemx.getQty();
                             prnObj.setLineCenterAlign(lineNr, colDesc, cwDesc, ""+Formater.formatNumber(costingItemx.getHpp(), "#,###.##"));
                             prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, ""+costingItemx.getQty());
                             prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, ""+Formater.formatNumber(costingItemx.getHpp() * costingItemx.getQty(), "#,###.##"));
                        }
                        
                }
            }

         //disini untuk total
         /*----------------------------------------
            Total Qty                3
            Total                   Rp       105,000
            Service                 Rp        10,500
            Pajak                   Rp        11,550
            Total Nota              Rp       127,050
            ----------------------------------------
          *
          */
            lineNr = lineNr + 1;
            prnObj.addRptLine("-");

            lineNr = lineNr + 1;
            prnObj.setLine(lineNr,"Total Qty");
            prnObj.setLineCenterAlign(lineNr, colDesc, cwDesc, "");
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, ""+totalQty);
            prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, "");

            lineNr = lineNr + 1;
            prnObj.setLine(lineNr, "Total");
            prnObj.setLineCenterAlign(lineNr, colDesc, cwDesc, "");
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, ""+curr);

            //String value = "";
            prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, ""+Formater.formatNumber(totalCost, "#,###.##"));

            lineNr = lineNr + 1;
            prnObj.addRptLine("-");

            if(useFooter.equals("0")){
                //disini untuk guest, room dan tanda tangan
                lineNr = lineNr + 1;
                prnObj.setLine(lineNr,"Name  : " +(employee.getFullName()).toUpperCase());

                prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
                prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
                prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, "Sign");
                
            }else{
                 for(int i=0; i<3; i++){ //tinggi headernya
                     lineNr = lineNr + 1;
                     prnObj.addLine("");
                 }
            }


            lineNr = lineNr + 1;
            //String footerTexGF = PstSystemProperty.getValueByName("FOOTER_TEXT_GUEST_FOLIO");
            prnObj.setLine(lineNr,"");
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
            prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, "");

            lineNr = lineNr + 1;
            String footerTexGF = PstSystemProperty.getValueByName("FOOTER_TEXT_GUEST_FOLIO");
            prnObj.setLine(lineNr,""+footerTexGF);
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
            prnObj.setLineCenterAlign(lineNr, colAmt$, cwAmt$, "");
            prnObj.setLineCenterAlign(lineNr, colAmtRp, cwAmtRp, "");
            

        } catch (Exception exc) {
            System.out.println(exc);
        }


        try {
            Vector lines = prnObj.getLines();
            if (lines != null && lines.size() > 0) {
                for (int il = 0; il < lines.size(); il++) {
                    out.println(lines.get(il));
                }
            }
        } catch (Exception exc) {
            System.out.println("" + exc);
        }
        out.flush();

    }


    public static DSJ_PrintObj getSummaryPaymentPersonal(int viewCurrency) {
        DSJ_PrintObj prnObj = new DSJ_PrintObj();

        return   prnObj;
    }

    /** Handles the HTTP <code>GET</code> method.

     * @param request servlet request

     * @param response servlet response

     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)

    throws ServletException, java.io.IOException {

        processRequest(request, response);

    }



    /** Handles the HTTP <code>POST</code> method.

     * @param request servlet request

     * @param response servlet response

     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response)

    throws ServletException, java.io.IOException {

        processRequest(request, response);

    }



    /** Returns a short description of the servlet.

     */

    public String getServletInfo() {

        return "Short description";

    }



}

