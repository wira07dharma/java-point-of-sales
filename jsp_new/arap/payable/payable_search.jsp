<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%
  /* 
 * Page Name  		:  payable_view.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
   */

  /**
   * *****************************************************************
   * Page Description : [project description ... ] Imput Parameters : [input
   * parameter ...] Output : [output ...] 
 ******************************************************************
   */
%>

<%@ page language="java" %>
<!-- package java -->
<%@ page import="java.util.*"%>
<!-- package qdep -->
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.qdep.entity.*"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<!-- package project -->
<%@ page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@ page import="com.dimata.posbo.session.arap.SessAccPayable"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%!  public static final String textListHeader[][] = {
    {"Supplier", "Nomor Invoice /<br>Payment Number", "Tanggal Invoice", "Mata Uang", "Urut Berdasar", "dari", "s/d", "Cari Rekap Utang", "Gudang", "Penerimaan Barang", "Rekap Utang", "Status", "Tanggal Pembayaran", "Lokasi"},
    {"Supplier", "Invoice Number /<br>Payment Number", "Invoice Date", "Currency", "Order By", "from", "to", "Search List AP", "Warehouse", "Receive Goods", "AP Summary", "Status", "Date Payment", "Location"}
  };

  public boolean getTruedFalse(Vector vect, int index) {
    for (int i = 0; i < vect.size(); i++) {
      int iStatus = Integer.parseInt((String) vect.get(i));
      if (iStatus == index) {
        return true;
      }
    }
    return false;
  }
%>

<!-- JSP Block -->
<%//for status
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_LMRR);
//end of status

  int iCommand = FRMQueryString.requestCommand(request);
  SrcAccPayable srcAccPayable = new SrcAccPayable();
  FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable();

  try {
    srcAccPayable = (SrcAccPayable) session.getValue(SessAccPayable.SESS_ACC_PAYABLE);
  } catch (Exception e) {
    srcAccPayable = new SrcAccPayable();
  }

  if (srcAccPayable == null) {
    srcAccPayable = new SrcAccPayable();
  }

  try {
    session.removeValue(SessAccPayable.SESS_ACC_PAYABLE);
  } catch (Exception e) {
  }

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
  <head>
    <!-- #BeginEditable "doctitle" --> 
    <title>Dimata - ProChain POS</title>

    <script language="JavaScript">
      function cmdSearch() {
        document.frmSrcOpname.command.value = "<%=Command.LIST%>";
        document.frmSrcOpname.action = "payable_view.jsp";
        document.frmSrcOpname.submit();
      }

      //-------------- script control line -------------------
      function MM_preloadImages() { //v3.0
        var d = document;
        if (d.images) {
          if (!d.MM_p)
            d.MM_p = new Array();
          var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
          for (i = 0; i < a.length; i++)
            if (a[i].indexOf("#") != 0) {
              d.MM_p[j] = new Image;
              d.MM_p[j++].src = a[i];
            }
        }
      }

      function MM_swapImage() { //v3.0
        var i, j = 0, x, a = MM_swapImage.arguments;
        document.MM_sr = new Array;
        for (i = 0; i < (a.length - 2); i += 3)
          if ((x = MM_findObj(a[i])) != null) {
            document.MM_sr[j++] = x;
            if (!x.oSrc)
              x.oSrc = x.src;
            x.src = a[i + 2];
          }
      }
    </script>
    <!-- #EndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <!-- #BeginEditable "styles" -->
    <%if (menuUsed == MENU_ICON) {%>
    <link href="../../stylesheets/general_home_style.css" type="text/css"
          rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../../styles/main.css" type="text/css">
    <!-- #EndEditable -->
    <!-- #BeginEditable "stylestab" --> 
    <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
    <!-- #EndEditable -->
    <!-- #BeginEditable "headerscript" --> 
    <SCRIPT language=JavaScript>
      function hideObjectForMarketing() {
      }

      function hideObjectForWarehouse() {
      }

      function hideObjectForProduction() {
      }

      function hideObjectForPurchasing() {
      }

      function hideObjectForAccounting() {
      }

      function hideObjectForHRD() {
      }

      function hideObjectForGallery() {
      }

      function hideObjectForMasterData() {
      }
      function hideObjectForSystem() {
      }

    </SCRIPT>
    <!-- #EndEditable --> 
  </head> 

  <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
      <%if (menuUsed == MENU_PER_TRANS) {
     %>
      <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
          <%@ include file = "../../main/header.jsp" %>
          <!-- #EndEditable --></td>
      </tr>
      <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
          <%@ include file = "../../main/mnmain.jsp" %>
          <!-- #EndEditable --> </td>
      </tr>
      <%} else {%>
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
                <%=textListHeader[SESS_LANGUAGE][8]%> &gt; <%=textListHeader[SESS_LANGUAGE][9]%> &gt; <%=textListHeader[SESS_LANGUAGE][10]%>
                <!-- #EndEditable --></td>
            </tr>
            <tr> 
              <td><!-- #BeginEditable "content" -->
                <%try {%>
                <form name="frmSrcOpname" method="post">
                  <input type="hidden" name="command" value="<%=iCommand%>">
                  <table width="100%" border="0" cellspacing="1" cellpadding="1">
                    <tr> 
                      <td> 

                        <table width="100%" border="0" cellspacing="2" cellpadding="2">
                          <tr> 
                            <td colspan="2" height="18">&nbsp;</td>
                          </tr>
                          <%--location--%> 
                          <tr> 
                            <td width="13%"  ><%=textListHeader[SESS_LANGUAGE][13]%></td>
                            <td width="87%" > : 
                              <%
                                Vector val_locationid = new Vector(1, 1);
                                Vector key_locationid = new Vector(1, 1);
                                
                                //add opie-eyek
                                //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                        + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                val_locationid.add("0");
                                key_locationid.add("ALL");
                                for (int d = 0; d < vt_loc.size(); d++) {
                                  Location loc = (Location) vt_loc.get(d);
                                  val_locationid.add("" + loc.getOID() + "");
                                  key_locationid.add(loc.getName());
                                }
                              %>
                              <%=ControlCombo.draw(frmSrcAccPayable.fieldNames[frmSrcAccPayable.FRM_FIELD_LOCATION], null, "", val_locationid, key_locationid, " tabindex=\"1\"", "formElemen")%> 
                            </td>
                          </tr>
                          <tr> 
                            <td width="13%" ><%=textListHeader[SESS_LANGUAGE][0]%></td>
                            <td width="87%" > : 
                              <input type="text" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_SUPPLIER_NAME]%>" value="<%=srcAccPayable.getSupplierName()%>" size="40"> 
                            </td>
                          </tr>
                          <tr> 
                            <td width="13%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                            <td width="87%"> : 
                              <input type="text" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_INVOICE_NUMBER]%>" value="<%=srcAccPayable.getInvoiceNumber()%>" size="30"> 
                            </td>
                          </tr>
                          <tr> 
                            <td width="13%" ><%=textListHeader[SESS_LANGUAGE][2]%></td>
                            <td width="87%" > :
                              <input type="checkbox" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_INVOICE_DATE_STATUS]%>" <%if (srcAccPayable.getInvoiceDateStatus() == 1) {%>checked<%}%> value="1">
                              <%=textListHeader[SESS_LANGUAGE][5]%> <%=ControlDate.drawDate(frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_START_DATE], srcAccPayable.getStartDate(), 1, -5)%>
                              <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDate(frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_END_DATE], srcAccPayable.getEndDate(), 1, -5)%> </td>
                          </tr>
                          <tr>
                            <td width="13%" ><%=textListHeader[SESS_LANGUAGE][12]%></td>
                            <td width="87%" > :
                              <input type="checkbox" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_INVOICE_PAYMENT_DATE_STATUS]%>" <%if (srcAccPayable.getInvoicePaymentDateStatus() == 1) {%>checked<%}%> value="1">
                              <%=textListHeader[SESS_LANGUAGE][5]%> <%=ControlDate.drawDate(frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE], srcAccPayable.getPaymentStartDate(), 1, -5)%>
                              <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDate(frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE], srcAccPayable.getPaymentEndDate(), 1, -5)%> </td>
                          </tr>
                          <tr> 
                            <td width="13%"  ><%=textListHeader[SESS_LANGUAGE][3]%></td>
                            <td width="87%" >: 
                              <%
                                Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                Vector vectCurrVal = new Vector(1, 1);
                                Vector vectCurrKey = new Vector(1, 1);
                                for (int i = 0; i < listCurr.size(); i++) {
                                  CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                  vectCurrKey.add(currencyType.getCode());
                                  vectCurrVal.add("" + currencyType.getOID());
                                }
                              %>
                              <%=ControlCombo.draw(FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_CURRENCY_ID], null, "" + srcAccPayable.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                          </tr>
                          <tr> 
                            <td width="13%"  ><%=textListHeader[SESS_LANGUAGE][4]%></td>
                            <td width="87%" > : 
                              <%
                                for (int i = 0; i < SessAccPayable.listSortKey().size(); i++) {
                              %> <input type="radio" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_SORTBY]%>" value="<%=String.valueOf(i)%>"> 
                              <%=SessAccPayable.sortKey[i]%><img src="<%=approot%>/images/spacer.gif" width="14" height="8"> 
                              <%}%> </td>
                          </tr>

                          <!-- tambah status di rekap summary -->
                          <tr>
                            <td valign="top" width="12%" align="left"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                            <td valign="top" width="1%" align="left">:<!--</td>--!>
                              <!--<td height="21" width="87%" valign="top" align="left">-->
                              <%
                                Vector vectResult = i_status.getDocStatusFor(docType);
                                for (int i = 0; i < vectResult.size(); i++) {
                                  if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL)) {
                                    Vector vetTemp = (Vector) vectResult.get(i);
                                    int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                    String strPrStatus = String.valueOf(vetTemp.get(1));
                              %>
                              <input type="checkbox" class="formElemen" name="<%=frmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_AP_STATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcAccPayable.getApstatus(), indexPrStatus)) {%>checked<%}%>>
                              <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <%
                                  }
                                }
                              %>
                            </td>
                          </tr>
                          <!-- end of summary search -->
                          <tr>
                            <td valign="top" width="12%" align="left">Term Of Payment</td>
                            <td valign="top" width="1%" align="left">:<!--</td>--!>
                              <!--<td height="21" width="87%" valign="top" align="left">-->
                              <%
                                Vector val_terms = new Vector(1, 1);
                                Vector key_terms = new Vector(1, 1);
                                val_terms.add("-1");
                                key_terms.add("All Terms");
                                for (int d = 0; d < PstMatReceive.fieldsPaymentType.length; d++) {
                                  val_terms.add(String.valueOf(d));
                                  key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                }
                                String select_terms = "" + srcAccPayable.getTermsOfPayment();
                              %>
                              <%=ControlCombo.draw(frmSrcAccPayable.fieldNames[frmSrcAccPayable.FRM_FIELD_TERMS_OF_PAYMENT], null, select_terms, val_terms, key_terms, "", "formElemen")%>
                            </td>
                          </tr>
                          <tr> 
                            <td width="13%">&nbsp;</td>
                            <td width="87%">&nbsp;</td>
                          </tr>
                          <tr> 
                            <td width="13%" >&nbsp;</td>
                            <td width="87%" > <table width="35%" border="0" cellspacing="0" cellpadding="0">
                                <tr> 
                                  <td width="90%" ><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></i></a></td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </form>
                <%} catch (Exception exc) {
                            out.println(exc);
                          }%>
                <!-- #EndEditable --></td> 
            </tr> 
          </table>
        </td>
      </tr>
      <tr> 
        <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
          <%if (menuUsed == MENU_ICON) {%>
          <%@include file="../../styletemplate/footer.jsp" %>

          <%} else {%>
          <%@ include file = "../../main/footer.jsp" %>
          <%}%>
          <!-- #EndEditable --> </td>
      </tr>
    </table>
  </body>
  <!-- #EndTemplate --></html>

