/*
 * DILARANG MENGHAPUS ATAU MENGUBAH HAK CIPTA  INI
 *
 * Copyright 2009 muhamadnur.net.
 * All rights reserved.
 *
 * Semua isi dalam file ini adalah hak cipta www.muhamadnur.net
 * Anda diperbolehkan untuk menggunakan sebagian atau seluruh isi file
 * asal tetap mencantumkan sumber asli dan tidak merubah lisensi ini
 *
 * Code ini dibuat dengan:
 * IDE              : Netbeans 6.9.1
 * Java Ver.     : 1.6
 * Platform      : Win 7
 * Mechine       : Acer Aspire 2920
 */


package com.dimata.posbo.entity.shoppingchart;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ahza
 */
public class addCart extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String MaterialId = (String) request.getAttribute("addCart");
        int qty = (Integer) request.getAttribute("qty");
        ShopCart shoppingCart = new ShopCart();
        shoppingCart.addToCart(MaterialId, qty);
        RequestDispatcher rdp = request.getRequestDispatcher("listdata.jsp");
        rdp.forward(request, response);
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
