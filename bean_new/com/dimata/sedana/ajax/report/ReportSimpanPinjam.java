/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.ajax.report;

import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.reportsearch.RscReport;
import com.dimata.sedana.form.reportsearch.FrmRscReport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
 */
/**
 *
 * @author Adi
 */
public class ReportSimpanPinjam extends HttpServlet {
  
  HttpServletRequest req = null;
  HttpServletResponse res = null;
  RscReport rscReport  = new RscReport();

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
    
    this.req = request;
    this.res = response;
    int reportType = FRMQueryString.requestInt(this.req, "reportType");
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_TABUNGAN]).equals("")) {
      this.rscReport.setTabungan(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_TABUNGAN]));
    }
    this.rscReport.setStartDate(FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_START_DATE]));
    this.rscReport.setEndDate(FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_END_DATE]));
    this.rscReport.setNamaNasabah(FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_NASABAH]));
    this.rscReport.setNoAnggotaStart(FRMQueryString.requestLong(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_NO_ANGGOTA_START]));
    this.rscReport.setNoAnggotaEnd(FRMQueryString.requestLong(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_NO_ANGGOTA_END]));
    this.rscReport.setNoRekeningStart(FRMQueryString.requestLong(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_NO_REKENING_START]));
    this.rscReport.setNoRekeningEnd(FRMQueryString.requestLong(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_NO_REKENING_END]));
    switch(reportType) {
      case FrmRscReport.REPORT_TABUNGAN :
        reportTabungan();
        break;
        
      case FrmRscReport.REPORT_RANGKUMAN_TABUNGAN :
        reportUsaha();
        break;
        
      case FrmRscReport.REPORT_KOLEKTIBILITAS :
        reportKolektibilitas();
        break;
        
      case FrmRscReport.REPORT_RANGKUMAN_KOLEKTIBILITAS :
        reportRangkumanKolektibilitas();
        break;
    }
    
    /*
    response.setContentType("text/html;charset=UTF-8");
    String path = this.getPath("com/dimata/sedana/report/jasper/report1.jrxml");
    ServletOutputStream servletOutputStream = response.getOutputStream();
    Connection conn = null;
    JasperReport jasperReport;
    JasperPrint jasperPrint;
    JasperDesign jasperDesign;
    try {
      // get a database connection
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmp3", "root", "root");

      // create a map of parameters to pass to the report.
      Map parameters = new HashMap();
      //parameters.put("Report_Title", "Salesman Details");

      // load JasperDesign from XML and compile it into JasperReport
      jasperDesign = JRXmlLoader.load(path);
      jasperReport = JasperCompileManager.compileReport(jasperDesign);

      // fill JasperPrint using fillReport() method
      jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
      response.setContentType("application/pdf"); 
      JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
      
      servletOutputStream.flush();
      servletOutputStream.close();
    } catch (SQLException sqle) {
      System.err.println(sqle.getMessage());
    } catch (ClassNotFoundException e) {
      System.err.println("No such class found!");
    } catch (JRException e) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      e.printStackTrace(printWriter);
      response.setContentType("text/plain");
      response.getOutputStream().print(stringWriter.toString());
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception ignored) {
        }
      }
    }
     */
  }
  
  private void reportTabungan() {
    this.req.setAttribute("rscReport", this.rscReport);
    try {
      String jspPath = "/report/simpanpinjam/tabungan.jsp";
      RequestDispatcher dispatcher = this.req.getRequestDispatcher(jspPath);
      dispatcher.forward(this.req, this.res);
    } catch (Error e) {
      System.out.println(e);
    } catch (ServletException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void reportUsaha() {
    this.req.setAttribute("rscReport", this.rscReport);
    try {
      String jspPath = "/report/simpanpinjam/rangkuman_tabungan.jsp";
      RequestDispatcher dispatcher = this.req.getRequestDispatcher(jspPath);
      dispatcher.forward(this.req, this.res);
    } catch (Error e) {
      System.out.println(e);
    } catch (ServletException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void reportKolektibilitas() {
    this.rscReport.setListSort(FRMQueryString.requestInt(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_LIST_SORT]));
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_KREDIT]).equals("")) {
      this.rscReport.setJenisKredit(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_KREDIT]));
    }
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_TINGKAT_KOLEKTIBILITAS]).equals("")) {
      this.rscReport.setTingkatKolektibilitas(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_TINGKAT_KOLEKTIBILITAS]));
    }
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_SUMBER_DANA]).equals("")) {
      this.rscReport.setSumberDana(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_SUMBER_DANA]));
    }
    this.req.setAttribute("rscReport", this.rscReport);
    try {
      String jspPath = "/report/simpanpinjam/kolektibilitas.jsp";
      RequestDispatcher dispatcher = this.req.getRequestDispatcher(jspPath);
      dispatcher.forward(this.req, this.res);
    } catch (Error e) {
      System.out.println(e);
    } catch (ServletException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void reportRangkumanKolektibilitas() {
    
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_KREDIT]).equals("")) {
      this.rscReport.setJenisKredit(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_KREDIT]));
    }
    
    if (!FRMQueryString.requestString(this.req, FrmRscReport.fieldNames[FrmRscReport.FRM_CONTACT_TYPE]).equals("")) {
      this.rscReport.setContactType(this.req.getParameterValues(FrmRscReport.fieldNames[FrmRscReport.FRM_CONTACT_TYPE]));
    }
    
    this.req.setAttribute("rscReport", this.rscReport);
    try {
      String jspPath = "/report/simpanpinjam/rangkuman_kolektabilitas.jsp";
      RequestDispatcher dispatcher = this.req.getRequestDispatcher(jspPath);
      dispatcher.forward(this.req, this.res);
    } catch (Error e) {
      System.out.println(e);
    } catch (ServletException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ReportSimpanPinjam.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
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
   * Handles the HTTP <code>POST</code> method.
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
