/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.util;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.masterdata.Ksg;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstKsg;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.printman.DSJ_PrintObj;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dedy_blinda
 */
public class PrintBarcodeIssuer extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    //public static String strURL = SessSystemProperty.PROP_APPURL;
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

    }

    /** Destroys the servlet.

     */
    public void destroy() {
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //String userLoginName = FRMQueryString.requestString(request, "userLoginName");
        int keyCommand = FRMQueryString.requestInt(request, "keycommand");
        int commandKey = FRMQueryString.requestInt(request, "commandKey");
        String mat_oid = FRMQueryString.requestString(request, "mat_oid");
        String qtyAll = FRMQueryString.requestString(request, "qty_all");
        String userLoginName = FRMQueryString.requestString(request, "userLoginName");
        String command="";
        Material mat = null;
        MaterialDetail matDet = null;
        Ksg matKsg = null;
        String etalase = "";
        Vector listKsg = new Vector();
        //mencari reservasi dan kamar
        
        String sFaktorJual = "";
        String sEtalase = "";
        String sBarcode = "";
        String sInter = "";
        String sBerat = "";
        String sName = "";
        String sRemak1 = "";
        String sRemak2 = "";
        String sRemak3 = "";
        String sQty = "";
        
        if (!mat_oid.equals("") && !mat_oid.equals(null) && !qtyAll.equals("") && !qtyAll.equals(null)) {

            String[] parts = mat_oid.split(";");
            String[] parts2 = qtyAll.split(";");
            
            for(int i = 0; i < parts.length; i++){
                String sOid = parts[i];
                String qty = parts2[i];
                if(!sOid.equals("") && !sOid.equals(null)){
                    try {
                        mat = PstMaterial.fetchExc(Long.valueOf(sOid));

                    } catch (Exception exc) {
                        System.out.println("exc...1" + exc.toString());
                    }

                    try{
                        matDet = PstMaterialDetail.fetchExcMaterialDetailId(Long.valueOf(sOid));
                    }catch(Exception exc){
                        System.out.println("exc...2" + exc.toString());

                    }

                    try{
                        matKsg = PstKsg.fetchExc(mat.getGondolaCode());
                        Location l = new Location();
                        if (matKsg.getLocationId() != 0) {
                            l = PstLocation.fetchExc(matKsg.getLocationId());
                            etalase = l.getCode()+"-"+matKsg.getCode();
                        }
                    }catch(Exception exc){
                        System.out.println("exc...2" + exc.toString());

                    }
                    
                    String barcode5= "";
                    String remarks1= "";
                    String remarks2= "";
                    String remarks3= "";

                    if(!mat.getBarCode().equals("") && !mat.getBarCode().equals(null)){
                        barcode5 = mat.getBarCode().substring(7, 14);
                    }

                    if(!mat.getMaterialDescription().equals("") && !mat.getMaterialDescription().equals(null)){
                        int lDesc = mat.getMaterialDescription().length();
                        if(lDesc > 13){ 
                            remarks1 = mat.getMaterialDescription().substring(0, 13);
                            remarks2 = mat.getMaterialDescription().substring(13, lDesc);
                            
                            while(remarks2.charAt(0)==' ') {
                                remarks2 = remarks2.trim();
                            }
                        }else{
                            remarks1 = mat.getMaterialDescription();
                        }
                    }
                    
                    sFaktorJual += ";"+String.format("%,.2f",matDet.getFaktorJual());
                    sEtalase += ";"+etalase;
                    sBarcode += ";"+barcode5;
                    sInter += ";"+mat.getBarCode();
                    sBerat += ";"+String.format("%,.3f",matDet.getBerat());
                    sName += ";"+mat.getFullName();
                    sRemak1 += ";"+remarks1;
                    sRemak2 += ";"+remarks2;
                    sRemak3 += ";"+remarks3;
                    sQty += ";"+qty;
                    
                }
            }
            
            sFaktorJual = sFaktorJual.substring(1);
            sEtalase = sEtalase.substring(1);
            sBarcode = sBarcode.substring(1);
            sInter = sInter.substring(1);
            sBerat = sBerat.substring(1);
            sName = sName.substring(1);
            sRemak1 = sRemak1.substring(1);
            sRemak2 = sRemak2.substring(1);
            sRemak3 = sRemak3.substring(1);
            sQty = sQty.substring(1);

            //String msgLoadString = "";
            response.setContentType("text/keylock");
            response.setHeader("Content-Disposition", "filename=downloadname.txt");
            PrintWriter out = null;
            String nl = "\r\n";//System.getProperty("line.separator");
            DSJ_PrintObj prnObj = new DSJ_PrintObj();
            prnObj.setSkipLineIsPaperFix(2);
            prnObj.setLeftMargin(1);
            prnObj.setFont(DSJ_PrintObj.SANS_SERIF);
            prnObj.setCpiIndex(3);
            int lineNr = 0;


            try {
                out = response.getWriter();

                lineNr = lineNr + 1;
                prnObj.addLine("faktorjual="+sFaktorJual);
                lineNr = lineNr + 1;
                prnObj.addLine("etalase="+sEtalase);
                lineNr = lineNr + 1;
                prnObj.addLine("barcode="+sBarcode);
                lineNr = lineNr + 1;
                prnObj.addLine("interpretation="+sInter);
                lineNr = lineNr + 1;
                prnObj.addLine("berat="+sBerat);
                lineNr = lineNr + 1;
                prnObj.addLine("name="+sName);
                lineNr = lineNr + 1;
                prnObj.addLine("remak1="+sRemak1);
                lineNr = lineNr + 1;
                prnObj.addLine("remak2="+sRemak2);
                lineNr = lineNr + 1;
                prnObj.addLine("remak3="+sRemak3);
                lineNr = lineNr + 1;
                prnObj.addLine("qty="+sQty);

            } catch (Exception exc) {
                System.out.println("" + exc);
            }

            try {
                Vector lines = prnObj.getLines();
                if (lines != null && lines.size() > 0) {
                    for (int il = 0; il < lines.size(); il++) {
                        out.print(lines.get(il) + "\r\n");
                    }
                }
            } catch (Exception exc) {
                System.out.println("" + exc);
            }

            out.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
