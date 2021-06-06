/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.ajax.formater;

import com.dimata.qdep.form.FRMHandler;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Regen
 */
public class MaskMoney extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/javascript;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
      /* TODO output your page here. You may use following sample code. */
      String sysDecimalSymbol = FRMHandler.SYSTEM_DECIMAL_SYMBOL;
      String userDecimalSymbol = ".";
      String userDigitGroup = ",";
      int precision = 2;
      out.println("'use strict'; var maskMoney = function(e) { var that = this; this.e = e; this.displayDecimal = '"+userDecimalSymbol+"'; this.displayThousand = '"+userDigitGroup+"'; this.valueDecimal = '"+sysDecimalSymbol+"';this.precission = "+precision+";this.first = true; this.input = $(that.e).val() ? $(that.e).val() : ($(that.e).html() ? $(that.e).html() : 0); this.value = '0'; this.unmaskedValue = '0'; this.maskedValue = '0'; this.min = $(that.e).data('min') ? $(that.e).data('min') : ''; this.max = $(that.e).data('max') ? $(that.e).data('max') : ''; this.valuecast = $(that.e).data('cast-class') ? $(that.e).parent().find('.'+$(that.e).data('cast-class')) : $('#'+$(that.e).data('cast-id')); this.rounding = $(e).data('round') ? $(e).data('round') : ''; this.fixing = function() { that.input = \"\"+($(that.e).val() ? $(that.e).val() : ($(that.e).html() ? $(that.e).html() : 0)); that.negative = that.input.indexOf(\"(\")>-1&&that.input.indexOf(\")\")>-1; that.negative = (that.input.indexOf(\"-\")>-1) ? !that.negative : that.negative; if(this.first) { if(that.valueDecimal != '.') { that.input.replace(that.valueDecimal, '.'); that.input = (parseFloat(that.input).toFixed(that.precission)+''); that.input.replace('.', that.valueDecimal); } else { that.input = (parseFloat(that.input).toFixed(that.precission)+''); }; }; }; this.init = function() { that.fixing(); that.unmask(); that.doRounding(); that.mask(); that.first = false; !that.valuecast || $(that.valuecast).val(that.value); $(that.e).val() ? $(that.e).val(that.maskedValue) : $(that.e).html(that.maskedValue); $(e).keydown(that.change); $(e).keyup(that.change); }; this.change = function() { that.fixing(); that.unmask(); that.calculateMinMax(); that.mask(); $(that.valuecast).val(that.value); $(that.e).val(that.maskedValue); }; this.unmask = function() { let amount = that.input; let separator = that.displayDecimal; let decimal = that.displayThousand; that.unmaskedValue = 0; that.value = 0; if (amount) { let vd = that.valueDecimal; if (that.first){ amount = amount.replace(vd, separator); } else { while (amount.charAt(0) == '0' || amount.charAt(0) == '(' || amount.charAt(0) == ')' || amount.charAt(0) == '-') { if (amount.length == 1) { break }; if (amount.charAt(1) == separator) { break }; amount = amount.substr(1, amount.length-1) }; (amount.split(separator).length - 1 <= 1) || (amount = amount.replace(',', '')); }; let total = ''; for(var j=0;j<amount.length;j++){ switch(amount.charAt(j)){ case ' ': break; case separator: total = total + amount.charAt(j); break; case decimal: break; default: var num = new Number(amount.charAt(j)); if(!isNaN(num)){ total = total +''+ amount.charAt(j); }; break; }; }; (vd === separator) || (total = total.replace(separator, vd)); that.unmaskedValue = (total == '') ? 0 : total; (total.charAt(total.length-1) != vd) || (total+='0'); that.value = (total == '') ? 0 : total; }; if(that.negative) { that.value = \"-\"+that.value; that.unmaskedValue = \"=\"+that.unmaskedValue; } return that.value; }; this.mask = function() { let digitDecimal = that.displayDecimal; let digitSeparator = that.displayThousand; let vd = that.valueDecimal; that.maskedValue = '0'; let total = that.unmaskedValue.length > 0 ? that.unmaskedValue : 0; if (total) { (vd === digitDecimal) || (total = total.replace(vd, digitDecimal)); let many = total.length; let j =0; let i =0; let strTotalValue = total; let strTotalDecimal = ''; let status = false; total = ''; for(j=0;j<strTotalValue.length;j++){ switch(strTotalValue.charAt(j)){ case ' ': break; case digitDecimal: total = total + strTotalValue.charAt(j); break; case digitSeparator: break; default: var num = new Number(strTotalValue.charAt(j)); if(!isNaN(num)){ total = total +''+ strTotalValue.charAt(j); }; break; }; }; strTotalValue = total; for(j=0;j<many;j++){ switch(total.charAt(j)){ case digitDecimal: strTotalDecimal = total.substring(j+1,many); strTotalValue = total.substring(0,j); status = true; break; }; }; var x = parseInt(strTotalValue.length) % 3; var varValue = ''; var maxInt = 0; for(j=0;j<strTotalValue.length;j++){ varValue = varValue + strTotalValue.charAt(j); maxInt = maxInt + 1; if(strTotalValue.length > 3) { if(maxInt==3){ if((strTotalValue.length-1) != j){ varValue = varValue + digitSeparator; maxInt = 0; }; }else{ if(varValue.length < 3){ if(x==maxInt){ if(strTotalValue.length > 3){ varValue = varValue + digitSeparator; maxInt = 0; }; }; }; }; }; }; if(strTotalDecimal.length > 0){ that.maskedValue = varValue+digitDecimal+strTotalDecimal; }else{ if(status){ that.maskedValue = varValue+digitDecimal; }else{ that.maskedValue = varValue; }; }; if(that.first) { if($(that.e).is('input')) { let prec = that.displayDecimal; for(i = 0; i < that.precission; i++) {prec+=0;} let check = that.maskedValue.slice(that.maskedValue.length-that.precission-1, that.maskedValue.length); (check != prec) || (that.maskedValue = that.maskedValue.slice(0, that.maskedValue.length-(that.precission+that.displayDecimal.length))); }; }; }; that.maskedValue = (that.negative) ? \"(\"+that.maskedValue+\")\" : that.maskedValue; return that.maskedValue; }; this.doRounding = function() { if(that.rounding == 'up'){ that.value = (Math.round(that.value)+''); (!that.first) || (that.unmaskedValue = (Math.round(that.unmaskedValue)+'')); } else if (that.rounding == 'down') { that.value = (Math.floor(that.value)+''); (!that.first) || (that.unmaskedValue = (Math.floor(that.unmaskedValue)+'')); }; }; this.calculateMinMax = function() { if (that.min != '') if (that.min > that.value) {that.value = (that.min+''); that.unmaskedValue = (that.min+'');}; if (that.max != '') if (that.max < that.value) { var tmpVal = that.value; while (tmpVal > that.max) { tmpVal = (Math.round(tmpVal/10)+''); }; if(that.min <= tmpVal) { that.value = tmpVal; that.unmaskedValue = tmpVal; }; }; }; }; var jMoney = function(element) { var e = $(element); var m = new maskMoney($(e)); m.init(); }; $(document).ready(function(){$('.money').each(function(){jMoney($(this));});});");
    } finally {
      out.close();
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
