<%-- 
    Document   : prmaterial_edit
    Created on : Feb 5, 2014, 10:25:29 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatch"%>
<%@page import="com.dimata.common.session.contact.SupplierFromWarehouse"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page import="org.apache.poi.hssf.record.SaveRecalcRecord"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstDistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.util.Command,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->

<%@ include file = "../../../main/javainit.jsp" %>
<%  int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_REQUEST);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%  boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
%>
<%!
  /* this constant used to list text of listHeader */
  public static final String textListOrderHeader[][] = {
    {"No", "Asal Barang", "Tanggal", "Supplier", "Contact", "Alamat", "Telp.", "Terms", "Days", "Ppn", "Ket.", "Mata Uang", "Gudang", "Store Request", "Nomor Revisi", "Status", "Include", "%", "Term Of Payment", "Gudang", "Tujuan Barang", "Request Transfer"},
    {"No", "from", "Date", "Supplier", "Contact", "Addres", "Phone", "Terms", "Days", "Ppn", "Remark", "Currency", "Warehouse", "Store Request", "Revisi Number", "Status", "Include", "%", "Term Of Payment", "Gudang", "To", "Request Transfer"}
  };

  /* this constant used to list text of listMaterialItem */
  public static final String textListOrderItem[][] = {
    {"No", "Sku", "Nama", "Qty Request", "Unit", "Hrg Beli Terakhir", "Hrg Beli", "Diskon Terakhir %",//7
      "Diskon1 %", "Diskon2 %", "Discount Nominal", "Netto Hrg Beli", "Total", "Qty Terima", "Stok", "Minimum Stok", "Approval", "Keterangan", "Hapus", "Nomor BC", "Jenis Dokumen", "Lokasi Asal", "Lokasi Tujuan"},//19
    {"No", "Code", "Name", "Qty Request", "Unit", "Last Cost", "Cost", "last Discount %", "Discount1 %",
      "Discount2 %", "Disc. Nominal", "Netto Buying Price", "Total", "Qty Receive", "Stok", "Minimum Stok", "Approval", "Keterangan", "Delete", "BC Number", "Document Type", "Origin", "Destintion"}
  };

  public static final String textListGlobal[][] = {
    {"Transfer", "Edit", "Tidak ada item transfer", "Cetak Transfer", "Proses transfer request tidak dapat dilakukan pada lokasi yang sama", "Cetak Delivery Order", "Posting Stock", "Posting Harga Beli", "Otomatis dibuat"},
    {"Dispatch", "Edit", "There is no Dispatch item", "Print Dispatch", "Transfer request cant'be proceed in same location", "Print Delivery Order", "Posting Stock", "Posting Harga Beli", "Automated Generated"}

  };
  /**
   * this method used to list all po item
   */

  public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
  };

public static int getStrDutyFree(){
	String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
	System.out.println("#Duty Free: " + strDutyFree);
	int dutyFree = Integer.parseInt(strDutyFree);
	return dutyFree;
}

// update by fitra 15-05-2014
  public String drawListRequestItem(int language, Vector objectClass, int start, boolean privManageData, long oidPurchaseOrder, int useBorder, String approot) {
    String result = "";
    if (objectClass != null && objectClass.size() > 0) {
      ControlList ctrlist = new ControlList();
      ctrlist.setAreaWidth("100%");
      ctrlist.setListStyle("listgen");
      ctrlist.setTitleStyle("listgentitle");
      ctrlist.setCellStyle("listgensell");
      ctrlist.setHeaderStyle("listgentitle");
      ctrlist.setBorder(useBorder);
      ctrlist.addHeader(textListOrderItem[language][0], "3%");
      ctrlist.addHeader(textListOrderItem[language][1], "10%");
      ctrlist.addHeader(textListOrderItem[language][2], "15%");
      ctrlist.addHeader(textListOrderItem[language][21], "10%");
      ctrlist.addHeader(textListOrderItem[language][22], "10%");
      ctrlist.addHeader(textListOrderItem[language][3], "5%");
      ctrlist.addHeader(textListOrderItem[language][13], "5%");
      ctrlist.addHeader(textListOrderItem[language][4], "3%");
      ctrlist.addHeader(textListOrderItem[language][16], "3%");
      ctrlist.addHeader(textListOrderItem[language][17], "3%");
      ctrlist.addHeader(textListOrderItem[language][18], "3%");
      Vector lstData = ctrlist.getData();
      Vector lstLinkData = ctrlist.getLinkData();
      Vector rowx = new Vector(1, 1);
      ctrlist.reset();
      int index = -1;
      if (start < 0) {
        start = 0;
      }

      for (int i = 0; i < objectClass.size(); i++) {
        Vector temp = (Vector) objectClass.get(i);
        PurchaseRequestItem poItem = (PurchaseRequestItem) temp.get(0);
        Material mat = (Material) temp.get(1);
        Unit unit = (Unit) temp.get(2);
        PurchaseRequest pr = (PurchaseRequest) temp.get(3);

        Location asal = null;
        Location tujuan = null;

        try {
          asal = PstLocation.fetchExc(pr.getLocationId());
          tujuan = PstLocation.fetchExc(pr.getWarehouseSupplierId());
        } catch (Exception e) {
        }

        //MatCurrency matCurrency = (MatCurrency)temp.get(3);
        rowx = new Vector(1, 1);
        start = start + 1;

        rowx.add("" + start + "");
        if (privManageData) {
          rowx.add("<a href=\"javascript:editItem('" + String.valueOf(poItem.getOID()) + "')\">" + mat.getSku() + "</a>");
        } else {
          rowx.add(mat.getSku());
        }

        rowx.add(mat.getName());
        rowx.add(asal.getName());
        rowx.add(tujuan.getName());

        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getQuantity()) + "</div>");
        //adding qty rec in po by mirahu 20120427
        double qtyRec = 0.0;
        //qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseRequest,poItem.getMaterialId());
        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyRec) + "</div>");
        // unit
        rowx.add(unit.getCode());
        rowx.add(PstPurchaseOrder.fieldsApprovalType[poItem.getApprovalStatus()]);
        rowx.add(poItem.getNote());
        // add by fitra 15-05-2014
        if (privManageData) {
          rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(poItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
        } else {
          rowx.add("");
        }

        lstData.add(rowx);
      }
      result = ctrlist.draw();
    } else {
      result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item order ...</div>";
    }
    return result;
  }

  public String drawListRequestItemEmail(int language, Vector objectClass, int start, boolean privManageData, long oidPurchaseOrder, int useBorder) {
    String result = "";
    if (objectClass != null && objectClass.size() > 0) {
      ControlList ctrlist = new ControlList();
      ctrlist.setAreaWidth("90%");
      ctrlist.setListStyle("listgen");
      ctrlist.setTitleStyle("listgentitle");
      ctrlist.setCellStyle("listgensell");
      ctrlist.setHeaderStyle("listgentitle");
      ctrlist.setBorder(useBorder);
      ctrlist.addHeader(textListOrderItem[language][0], "3%");
      ctrlist.addHeader(textListOrderItem[language][1], "10%");
      ctrlist.addHeader(textListOrderItem[language][2], "15%");
      ctrlist.addHeader(textListOrderItem[language][14], "3%");
      ctrlist.addHeader(textListOrderItem[language][15], "3%");
      ctrlist.addHeader(textListOrderItem[language][3], "5%");
      ctrlist.addHeader(textListOrderItem[language][4], "3%");

      Vector lstData = ctrlist.getData();
      Vector lstLinkData = ctrlist.getLinkData();
      Vector rowx = new Vector(1, 1);
      ctrlist.reset();
      int index = -1;
      if (start < 0) {
        start = 0;
      }

      for (int i = 0; i < objectClass.size(); i++) {
        Vector temp = (Vector) objectClass.get(i);
        PurchaseRequestItem poItem = (PurchaseRequestItem) temp.get(0);
        Material mat = (Material) temp.get(1);
        Unit unit = (Unit) temp.get(2);
        //MatCurrency matCurrency = (MatCurrency)temp.get(3);
        rowx = new Vector();

        start = start + 1;
        rowx.add("" + start + "");
        rowx.add(mat.getSku());
        rowx.add(mat.getName());
        // price
        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getCurrentStock()) + "</div>");
        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getMinimStock()) + "</div>");
        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getQuantity()) + "</div>");
        rowx.add(unit.getCode());

        lstData.add(rowx);
      }
      result = ctrlist.draw();
    } else {
      result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item order ...</div>";
    }
    return result;
  }


%>


<!-- Jsp Block -->
<%
  /**
   * get approval status for create document
   */
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_POR);
%>


<%
  /**
   * get request data from current form
   */
  int iCommand = FRMQueryString.requestCommand(request);
  int prevCommand = FRMQueryString.requestInt(request, "prev_command");
  int startItem = FRMQueryString.requestInt(request, "start_item");
  int cmdItem = FRMQueryString.requestInt(request, "command_item");
  int appCommand = FRMQueryString.requestInt(request, "approval_command");
  long oidPurchaseRequest = FRMQueryString.requestLong(request, "hidden_material_request_id");
  String textHistory = FRMQueryString.requestString(request, "text_history");
  int type = FRMQueryString.requestInt(request, "str_type");
  int poUseDirectPrinting = 0;
  int includePrintPrice = FRMQueryString.requestInt(request, "includePrinPrice");

  int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE"));
  
// for printing document
  if (iCommand == Command.PRINT) {
    System.out.println(" JSP 0 ");
    String hostIpIdx = "";
    try {
      String command = request.getParameter("command");
      hostIpIdx = request.getParameter("printeridx");
      System.out.println("Print on " + hostIpIdx + " Command = " + iCommand);
      if (hostIpIdx != null) {
        PrinterHost prnHost = RemotePrintMan.getPrinterHost(hostIpIdx, ";");
        PrnConfig prn = RemotePrintMan.getPrinterConfig(hostIpIdx, ";");
        DSJ_PrintObj obj = InternalExternalPrinting.printForm(oidPurchaseRequest, type, companyAddress); //TestPrn.getTestObj();// get object to print !
        obj.setPrnIndex(prn.getPrnIndex());
        RemotePrintMan.printObj(prnHost, obj);
      }
    } catch (Exception exc3) {
      System.out.println("Print Exc " + exc3);
    }

    iCommand = Command.EDIT;
  }
  /**
   * initialization of some identifier
   */
  String errMsg = "";
  String getTmpHistory;
  int iErrCode = FRMMessage.ERR_NONE;

  /**
   * purchasing pr code and title
   */
  String poCode = ""; // i_pstDocType.getDocCode(docType);
  String poTitle = "Transfer"; //i_pstDocType.getDocTitle(docType);
  String poDocTitle = textListOrderHeader[SESS_LANGUAGE][13]; //i_pstDocType.getDocTitle(docType);
  String poItemTitle = poTitle + " Item";
  String poTitleBlank = "";

  /**
   * purchasing pr code and title
   */
  String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_PRR));

  /**
   * action process
   */
  LogSysHistory logHistory = new LogSysHistory();

  ControlLine ctrLine = new ControlLine();
  CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
  iErrCode = ctrlPurchaseRequest.action(iCommand, oidPurchaseRequest, userName, userId);
  FrmPurchaseRequest frmpo = ctrlPurchaseRequest.getForm();
  PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();
  errMsg = ctrlPurchaseRequest.getMessage();

  /**
   * if iCommand = Commmand.ADD ---> Set default rate which value taken from
   * PstCurrencyRate
   */
//double curr = PstCurrencyRate.getLastCurrency();
  if (iCommand == Command.ADD) {
    //po.setVendorExchangeRate(curr);
  }

  /**
   * generate code of current currency
   */
  String priceCode = "Rp.";
  /*if(po.getPriceCurrency()==PstCurrencyRate.RATE_CODE_USD)
{
	priceCode = "US$.";
}*/

  /**
   * check if document already closed or not
   */
  boolean documentClosed = false;
  if (po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT && po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_FINAL && po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED) {
    documentClosed = true;
  }

  /**
   * check if document may modified or not
   */
  boolean privManageData = true;
  if (po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
    privManageData = false;
  }

  /**
   * list purchase order item
   */
//textHistory = logHistory.getHistory();
  oidPurchaseRequest = po.getOID();
  int recordToGetItem = 10;
  int recordToGetItemEmail = 0;
  String whereClauseItem = PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] + "=" + oidPurchaseRequest;
  String orderClauseItem = "";
  int vectSizeItem = PstPurchaseRequestItem.getCount(whereClauseItem);
  whereClauseItem = "POI." + PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID] + "=" + oidPurchaseRequest;
  Vector listPurchaseRequestItem = new Vector();

  listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem, recordToGetItemEmail, whereClauseItem);

//sent email if document to be approve
  String contentEmailItem = "";
  int sentNotif = Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
  String toEmail = PstSystemProperty.getValueByName("POS_EMAIL_TO");
  String urlOnline = PstSystemProperty.getValueByName("POS_URL_ONLINE");
  String hasilEmail = "";
//disini di cek jika transfer request tidak perlu kirim email
  if (po.getWarehouseSupplierId() == 0) {
    if (sentNotif == 1) {
      if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && iCommand == Command.SAVE && iErrCode == FRMMessage.ERR_NONE) {
        Vector listPurchaseRequestItemEmail = new Vector();

        listPurchaseRequestItemEmail = PstPurchaseRequestItem.listWithStokNMinStock(startItem, recordToGetItemEmail, whereClauseItem, po.getLocationId());

        //create content
        contentEmailItem = "Dear Sir,<br>";
        contentEmailItem += "<b>Document :</b>" + po.getPrCode() + "<br>";
        Location lokasiPO = new Location();
        try {
          lokasiPO = PstLocation.fetchExc(po.getLocationId());
        } catch (Exception ex) {

        }
        contentEmailItem += "<b>Lokasi&nbsp;:&nbsp;&nbsp;</b>" + lokasiPO.getName() + "<br>";
        contentEmailItem += "<b>Tanggal&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;</b>" + Formater.formatDate(po.getPurchRequestDate(), "dd-MM-yyyy") + "<br>";
        contentEmailItem += "<b>Remark&nbsp;&nbsp;:</b>" + po.getRemark() + "<br>";
        contentEmailItem += "<br><br>";
        contentEmailItem += drawListRequestItemEmail(SESS_LANGUAGE, listPurchaseRequestItemEmail, startItem, privManageData, oidPurchaseRequest, 1);
        contentEmailItem += "<br><br>";
        contentEmailItem += "<br><br>";
        contentEmailItem += "Thanks<br><br><br>";
        contentEmailItem += "Don't forget to Follow up<br><br><br>";
        contentEmailItem += "Akses Onsite Aplication :&nbsp; <a href=\"http://localhost:8080/" + approot + "/login.jsp?nodocument='" + po.getOID() + "'&typeView=1\">Online Aplication</a><br>";
        contentEmailItem += "Akses Online Aplication :&nbsp; <a href=\"" + urlOnline + "/login.jsp?nodocument=" + po.getOID() + "&typeView=2\">Online Aplication</a>";
        SessEmail sessEmail = new SessEmail();
        hasilEmail = sessEmail.sendEamil(toEmail, "Purchase Request Approval - " + lokasiPO.getName() + " - " + po.getPrCode(), contentEmailItem, "");
      }
    }
  }

  listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem, recordToGetItem, whereClauseItem);
  double defaultPpn = 0;
  defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

  if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="pomaterial_list.jsp">
  <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
  }
%>

<!-- FUNGSI UNTUK MENAMPILKAN HISTORY DATA PURCHASE REQUEST -->
<%
  String msgString = "";
  long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
  Vector listDistributionPO = new Vector(1, 1);
//Vector listGetLastData = new Vector(1, 1);
  int start = FRMQueryString.requestInt(request, "start");
  int recordToGet = 0;
//String whereClausePurchaseRequest =" dpo."+PstDistributionPurchaseRequest.fieldNames[PstDistributionPurchaseRequest.FLD_PURCHASE_REQUEST_ID] + "="+oidPurchaseRequest;
//listDistributionPO= PstDistributionPurchaseRequest.getListWithLocationName(0, 0, whereClausePurchaseRequest, "");
%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
  <head>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>
    <!-- #BeginEditable "doctitle" -->
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
      //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

      function cmdAdd() {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.target = "";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.VIEW%>";
        //document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_material_request_id.value=0;
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }

      function cmdEdit(oid) {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.EDIT%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }




      function compare() {
        var dt = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_dy.value;
        var mn = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_mn.value;
        var yy = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_yr.value;
        var dt = new Date(yy, mn - 1, dt);
        var bool = new Boolean(compareDate(dt));
        return bool;
      }

      function cmdSave() {
      <%
        if ((po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (po.getPrStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
      %>
        var transfer_from = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID]%>.value;
        var transfer_to = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_WAREHOUSE_SUPP_ID]%>.value;
        if (transfer_from != transfer_to) {
          document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.SAVE%>";
          document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
          document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
          if (compare() == true)
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
        } else {
          alert("<%=textListGlobal[SESS_LANGUAGE][4]%>")
        }
      <%

      } else {

      %>

        alert("Document has been posted !!!");

      <%      }

      %>
      }

      function cmdAsk(oid) {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.ASK%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }





      function cmdCancel() {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.CANCEL%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }


      function cmdDelete(oid) {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.DELETE%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.approval_command.value = "<%=Command.DELETE%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }


      function cmdConfirmDelete(oid) {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.DELETE%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_mat_request_item_id.value = oid;
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.approval_command.value = "<%=Command.DELETE%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerialitem.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }

      // add by fitra 17-05-2014

      function cmdNewDelete(oid) {
        var msg;
        msg = "Apakah Mang Kemed Yakin Akan Menghapus Data ? ";
        var agree = confirm(msg);
        if (agree)
          return cmdConfirmDelete(oid);
        else
          return cmdEdit(oid);
      }

      function cmdBack() {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.FIRST%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "search_request_transfer.jsp"; //"srcprtowarehousematerial.jsp
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }

      function printDirectForm()
      {
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.PRINT%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = "<%=prevCommand%>";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerial_edit.jsp";
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
      }

      function printForm() {
        window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderPrintPDF?hidden_material_request_id=<%=oidPurchaseRequest%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPrCode()%>");
          }

          function printOutForm() {
            var include = 0;
            var includeprint = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.includePrinPrice.checked;
            if (includeprint) {
              include = 1;
            }
            window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseStoreRequestNoShippingPrintPDF?hidden_material_store_id=<%=oidPurchaseRequest%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPrCode()%>&showprintprice=" + include);
          }

          function printFormHtml() {
            var include = 0;
            var includeprint = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.includePrinPrice.checked;
            if (includeprint) {
              include = 1;
            }
            window.open("rt_material_print_form.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice=" + include, "receivereport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
          }

          function klik() {
            var bool = new Boolean();
      <%
        if (listPurchaseRequestItem.size() > 0) {
      %>
            alert("Supplier tidak bisa di ubah\nKarena sudah ada data item");
            bool = true;
      <%} else {%>
            bool = false;
      <%}%>
            return bool;
          }


          //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


          //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
          function addItem() {
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.ADD%>";
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerialitem.jsp";
            if (compareDateForAdd() == true)
              document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
          }

          function editItem(oid) {
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = "<%=Command.EDIT%>";
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_mat_request_item_id.value = oid;
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerialitem.jsp";
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
          }

          function itemList(comm) {
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = comm;
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value = comm;
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "prtowarehousematerialitem.jsp";
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
          }
          //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------

          //------------------------- END JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------


          //------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
          function paymentList(comm) {
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value = comm;
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action = "ordermaterialpayment.jsp";
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
          }
          //------------------------- END JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------


          //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
          function MM_swapImgRestore() { //v3.0
            var i, x, a = document.MM_sr;
            for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
              x.src = x.oSrc;
          }

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

          function MM_findObj(n, d) { //v4.0
            var p, i, x;
            if (!d)
              d = document;
            if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
              d = parent.frames[n.substring(p + 1)].document;
              n = n.substring(0, p);
            }
            if (!(x = d[n]) && d.all)
              x = d.all[n];
            for (i = 0; !x && i < d.forms.length; i++)
              x = d.forms[i][n];
            for (i = 0; !x && d.layers && i < d.layers.length; i++)
              x = MM_findObj(n, d.layers[i].document);
            if (!x && document.getElementById)
              x = document.getElementById(n);
            return x;
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

          function viewHistoryTable() {
            var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                    "&oidDocHistory=<%=oidPurchaseRequest%>";
            window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
          }

          function viewDistributionLocation() {
            var strvalue = "po_distribution_location.jsp?command=<%=Command.ADD%>" +
                    "&hidden_oidPurchaseRequest=<%=oidPurchaseRequest%>";
            window.open(strvalue, "material", "height=250,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
          }
          //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
    </script>
    <!-- #EndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <!-- #BeginEditable "styles" -->
    <%if (menuUsed == MENU_ICON) {%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
    <!-- #EndEditable -->
    <!-- #BeginEditable "stylestab" -->
    <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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

    </SCRIPT>
    <style>
      .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
      }
      table.listgen {
        margin: auto 3%;
        text-align: center;
        width: 90% !important;
      }
      a.btn.btn-primary.store {
        margin: 25px 0px 0px 30px;
      }
      .listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3;
      }
      .btn-add {
        font-family: sans-serif;
        text-decoration: none;
        background-color: #6cd85f;
        color: #fff;
        cursor: pointer;
        margin-left: 10px;
      }
      .btnini {
        padding: 10px 8px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
        margin: 10px 0px 0px 10px;
      }
      .btn {
        padding: 5px 8px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
      }
      .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
        cursor: not-allowed;
        background-color: #fff !important;
        opacity: 1;
      }
      select {
    height: 30px !important;
    border: 1px solid #ccc !important;
    width: 100%;
}
textarea.formElemen {
    border-radius: 0px;
    width: 100%;
}
.row {
    margin-bottom: 10px;
}
label.control-label.col-sm-4.padd {
    padding: 10px 0px;
}
label.control-label.col-sm-4.paddd {
    padding: 0px;
}
.col-sm-8 {
    margin-bottom: 10px;
}
label.control-label.col-sm-4 {
    padding-top: 10px;
}
select.tanggal {
    width: 30.9%;
}
i.fa.fa-backward {
    padding: 3px;
}
a.command.btn.btn-warning.atas {
    margin-top: -75px;
    margin-right: 20px;
}
.form-control {
  font-size: 12px
}
    </style>
  </head>
  <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <section class="content-header">
      <%if(getStrDutyFree() == 1){%>
      <h1><%=textListOrderHeader[SESS_LANGUAGE][13]%>
        <small> Add</small></h1>
        <%}else{%>
      <h1><%=textListOrderHeader[SESS_LANGUAGE][21]%>
        <small> Add</small></h1>
        <%}%>
      <ol class="ol">
        <li>
          <a>
            <li class="active">Transfer</li>
          </a>
        </li>
      </ol>
    </section>
    <p class="border"></p>
    <div class="container-pos">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
        <%if (menuUsed == MENU_PER_TRANS) {%>
        <tr>
          <td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td>
        </tr>
        <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td></tr>
        <%} else {%>
        <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %> </td> </tr>
        <%}%>
        <tr><td valign="top" align="left"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><!-- #BeginEditable "content" -->
                  <form name="<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>" method="post" action="">
                    <input type="hidden" name="command" value="">
                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                    <input type="hidden" name="start_item" value="<%=startItem%>">
                    <input type="hidden" name="command_item" value="<%=cmdItem%>">
                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                    <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
                    <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_DOCUMENT_TYPE]%>" value="PPBTBB">
                    <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REQUEST_SOURCE]%>" value="<%=PurchaseRequest.TYPE_SOURCE_TRANSFER %>">
                    <input type="hidden" name="hidden_mat_request_item_id" value="">
                    <input type="hidden" name="hidden_order_deliver_sch_id" value="">
                    <input type="hidden" name="includePrinPrice" value="">
                    <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_CODE]%>" value="<%=po.getPrCode()%>">
                    <table width="100%" border="0">
                      <tr valign="top">
                        <td colspan="3">
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <div class="row">
                              <div class="col-sm-12">
                                <!-- left side -->
                                <div class="col-sm-4">
                                  <div class="form-group">
                                    <label class="control-label col-sm-4" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                                    <div class="col-sm-8">
                                        <input id="selectNomor" class="form-control" type="text" name="" value="<%= ((po.getPrCode() == null) || (po.getPrCode().equals(""))) ? textListGlobal[SESS_LANGUAGE][8] : po.getPrCode() %>" readonly="">
                                    </div>
                                  </div>
                                    <div class="form-group">
                                    <label class="control-label col-sm-4" for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        Vector val_locationid = new Vector(1, 1);
                                        Vector key_locationid = new Vector(1, 1);
                                        //add opie-eyek
                                        //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                        String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                        whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                        for (int d = 0; d < vt_loc.size(); d++) {
                                          Location loc = (Location) vt_loc.get(d);
                                          val_locationid.add("" + loc.getOID() + "");
                                          key_locationid.add(loc.getName());
                                        }
                                        String select_locationid = "" + po.getLocationId();%>
                                      <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                                    </div>
                                  </div>
                                    <div class="form-group">
                                    <label class="control-label col-sm-4" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                                    <div class="col-sm-8">
                                      <%=ControlDate.drawDate(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE], (po.getPurchRequestDate() == null) ? new Date() : po.getPurchRequestDate(), "tanggal", 1, -5)%>  &nbsp;
                                    </div>
                                  </div>
                                </div>
								<!-- center side -->             
                                <div class="col-sm-4">
                                  <% if (getStrDutyFree() == 1) {%>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 padd" for="jenisDokumen"><%=textListOrderItem[SESS_LANGUAGE][20]%></label>
                                    <div class="col-sm-8">
                                      <input type="text" id="jenisDokumen" class="form-control" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_DOCUMENT_TYPE]%>" value="PPBTBB" readonly="true">
                                    </div>
                                  </div>
                                    <%}%>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][20]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        Vector val_locationidSup = new Vector(1, 1);
                                        Vector key_locationidSup = new Vector(1, 1);
                                        //add opie-eyek
                                        //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                        //Vector vt_loc_sup = PstLocation.list(0, 0, "", "");
                                        for (int d = 0; d < vt_loc.size(); d++) {
                                          Location supplierFromWarehouse = (Location) vt_loc.get(d);
                                          val_locationidSup.add("" + supplierFromWarehouse.getOID() + "");
                                          key_locationidSup.add(supplierFromWarehouse.getName());
                                        }

                                        String select_locationid_sup = "" + po.getWarehouseSupplierId(); //selected on combo box
%>
                                      <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_WAREHOUSE_SUPP_ID], null, select_locationid_sup, val_locationidSup, key_locationidSup, "", "formElemen")%>
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 paddd" for="status"><%=textListOrderHeader[SESS_LANGUAGE][15]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        Vector obj_status = new Vector(1, 1);
                                        Vector val_status = new Vector(1, 1);
                                        Vector key_status = new Vector(1, 1);
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                        // update opie-eyek 19022013
                                        // user bisa memfinalkan purchase request  jika  :
                                        // 1. punya approve document pr = true
                                        // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                        boolean locationAssign = false;
                                        locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", po.getLocationId());
                                        if ((listPurchaseRequestItem != null) && (listPurchaseRequestItem.size() > 0) && locationAssign == true && privApproval == true) {
                                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                        }
                                        String select_status = "" + po.getPrStatus();
                                        if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                          out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                        } else if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                          out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                        } else {
                                          try {%>
                                      <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS], null, select_status, val_status, key_status, "", "formElemen")%> </td>
                                      <%} catch (Exception ex) {
                                                System.out.print("xxxx " + ex);
                                              }
                                            }%>
                                    </div>
                                  </div>
                                </div>
                                <!--right side-->
                                <div class="col-sm-4">
                                  <% if (getStrDutyFree() == 1) {%>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4" for="nomorBC"><%=textListOrderItem[SESS_LANGUAGE][19]%></label>
                                    <div class="col-sm-8">
                                      <input type="text" id="nomorBC" class="form-control" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_BC_NUMBER]%>" value="<%=po.getNomorBc()%>">
                                    </div>
                                  </div>
                                    <%}%>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4" for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label>
                                    <div class="col-sm-8">
                                      <textarea id="textarea" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=po.getRemark()%></textarea>
                                    </div>
                                  </div> 
                                </div> 
                              </div>
                            </div>
                                </table>
                                </td>
                                </tr>
                                <tr>
                                  <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr align="left" valign="top">
                                        <td height="22" valign="middle" colspan="3"><%=drawListRequestItem(SESS_LANGUAGE, listPurchaseRequestItem, startItem, privManageData, oidPurchaseRequest, 0, approot)%></td> 
                                      </tr>
                                      <tr align="left" valign="top">
                                        <td height="22" valign="middle" colspan="3"><%=hasilEmail%></td> 
                                      </tr>
                                      <%if (oidPurchaseRequest != 0) {%>
                                      <tr align="left" valign="top">
                                        <td height="8" align="left" colspan="3" class="command">
                                          <span class="command">
                                            <%
                                              if (cmdItem != Command.FIRST && cmdItem != Command.PREV && cmdItem != Command.NEXT && cmdItem != Command.LAST) {
                                                cmdItem = Command.FIRST;
                                              }
                                              ctrLine.setLocationImg(approot + "/images");
                                              ctrLine.initDefault();
                                              ctrLine.setImageListName(approot + "/images", "item");

                                              ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "')");
                                              ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "')");
                                              ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "')");
                                              ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "')");
                                            %>
                                            <%=ctrLine.drawImageListLimit(cmdItem, vectSizeItem, startItem, recordToGetItem)%>
                                          </span>
                                        </td>
                                      </tr>
                                      <tr align="left" valign="top">
                                        <td height="22" valign="middle" colspan="3">
                                          <%
                                            if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                          %>
                                          <table width="50%" border="0" cellspacing="2" cellpadding="0" height="30px">
                                            <tr>
                                              <td><a href="javascript:addItem()"  style="margin-left: 10px; margin-top: 10px" class="btn btn-success" id="btnAdd" data-oid="0" data-for="addStoreRequest"><i class="fa fa-plus"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                            </tr>
                                          </table>
                                          <%}%>
                                        </td>
                                      </tr>
                                      <%}%>
                                    </table>
                                  </td>
                                </tr>
                                <tr>
                                  <td width="55%">
                                    <%
                                      ctrLine.setLocationImg(approot + "/images");

                                      // set image alternative caption
                                      ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SAVE, true));
                                      ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) + " List");
                                      ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ASK, true));
                                      ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_CANCEL, false));
                                      ctrLine.initDefault();
                                      ctrLine.setTableWidth("25%");
                                      String scomDel = "javascript:cmdAsk('" + oidPurchaseRequest + "')";
                                      String sconDelCom = "javascript:cmdDelete('" + oidPurchaseRequest + "')";
                                      String scancel = "javascript:cmdEdit('" + oidPurchaseRequest + "')";
                                      ctrLine.setCommandStyle("command");
                                      ctrLine.setColCommStyle("command");
                                      // set command caption
                                      ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SAVE, true));
                                      ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) + " List");
                                      ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ASK, true));
                                      ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_DELETE, true));
                                      ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_CANCEL, false));
                                      if (privDelete && privManageData) {
                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);
                                      } else {
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                      }
                                      if (privAdd == false && privUpdate == false) {
                                        ctrLine.setSaveCaption("");
                                      }
                                      if (privAdd == false) {
                                        ctrLine.setAddCaption("");
                                      }
                                      if (iCommand == Command.SAVE && frmpo.errorSize() == 0) {
                                        //iCommand=Command.EDIT;
                                      }
                                      if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                                        ctrLine.setDeleteCaption("");
                                      }
                                      if (documentClosed) {
                                        ctrLine.setSaveCaption("");
                                        ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) + " List");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setCancelCaption("");
                                      }
                                    %>
                                    <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%>
                                  </td>
                                  <td width="45%">
                                    <%if (poUseDirectPrinting == 1) {%>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                      <tr>
                                        <td colspan="2" valign="top">
                                          <%
                                            Vector valtype = new Vector(1, 1);
                                            Vector keytype = new Vector(1, 1);
                                            valtype.add(String.valueOf(0));
                                            keytype.add("INTERNAL");
                                            valtype.add(String.valueOf(1));
                                            keytype.add("EXTERNAL");
                                            String selecttype = "" + type;
                                          %> <%=ControlCombo.draw("str_type", null, selecttype, valtype, keytype, "", "formElemen")%>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td width="5%">
                                          <%
                                            Vector hostLst = null;
                                            try {
                                              hostLst = RemotePrintMan.getHostList();
                                              System.out.println(" JSP 1 1");
                                            } catch (Exception exc) {
                                              System.out.println("HostLst:  " + exc);
                                            }
                                          %>
                                          <select name="printeridx">
                                            <%
                                              Vector prnLst = null;
                                              PrinterHost host = null;
                                              if (hostLst != null) {
                                                for (int h = 0; h < hostLst.size(); h++) {
                                                  try {
                                                    host = (PrinterHost) hostLst.get(h);
                                                    if (host != null) {
                                                      prnLst = host.getListOfPrinters(false);//getPrinterListWithStatus(host);
                                                    }
                                                    if (prnLst != null) {
                                                      for (int i = 0; i < prnLst.size(); i++) {
                                                        try {
                                                          PrnConfig prnConf = (PrnConfig) prnLst.get(i);
                                                          out.print(" <option value='" + host.getHostIP() + ";" + prnConf.getPrnIndex() + "'> ");
                                                          out.println(prnConf.getPrnName() + " on " + host.getHostName() + " (" + prnConf.getPrnPort() + ")");
                                                          out.print("f</option>");
                                                        } catch (Exception exc) {
                                                          out.println("ERROR " + exc);
                                                        }
                                                      }
                                                    }
                                                  } catch (Exception exc1) {
                                                    out.println("ERROR" + exc1);
                                                  }
                                                }
                                              }
                                            %>
                                          </select>
                                        </td>
                                        <td width="95%" nowrap>&nbsp; <a href="javascript:printDirectForm()" class="command" >
                                            <%if (hostLst.size() > 0 && hostLst != null) {%> CETAK SR <%}%></a>
                                        </td>
                                      </tr>
                                      <%} else {%>
                                      <tr>
                                        <td width="95%" nowrap align="left">&nbsp;
                                          <%
                                            includePrintPrice = 0;
                                          %>
                                        </td>
                                      </tr>
                                      <tr>
                                        
                              <% if(useForRaditya.equals("1")){
                              if(po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){ %>
                                        <td width="90%" nowrap align="right">&nbsp;
                                          <a href="javascript:printOutForm()" class="command btn btn-warning atas" ><i class="fa fa-print"></i> Generate PDF</a>
                                          <a href="javascript:printFormHtml()" class="command btn btn-warning atas" ><i class="fa fa-print"></i> Generate HTML</a>
                                        </td>
                              <%}}else{%>
                                        <td width="90%" nowrap align="right">&nbsp;
                                          <a href="javascript:printOutForm()" class="command btn btn-warning atas" ><i class="fa fa-print"></i> Generate PDF</a>
                                          <a href="javascript:printFormHtml()" class="command btn btn-warning atas" ><i class="fa fa-print"></i> Generate HTML</a>
                                        </td>
                              <%}%>
                                      </tr>
                                    </table>
                                    <%}%>
                                  </td>
                                </tr>
                                <tr>
                                  <td>
                                <centre> <a href="javascript:viewHistoryTable()">TABEL HISTORY</a></centre>
                                </td>
                                </tr>
                                <!--==================================================================================================== -->
                                </table>
                                </td>
                                </tr>
                                </table>
                                </form>
                                </td>
                                </tr>
                                </table>
                              </div>
                              </td>
                              </tr>
                              <tr>
                                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                                  <%if (menuUsed == MENU_ICON) {%>
                                  <%@include file="../../../styletemplate/footer.jsp" %>
                                  <%} else {%>
                                  <%@ include file = "../../../main/footer.jsp" %>
                                  <%}%>
                                  <script type="text/javascript" src="../../../styles/jquery-3.1.1.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
                                  <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
                                  <script type="text/javascript" src="../../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/iCheck/icheck.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.js"></script>
                                  <script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
                                  <script type="text/javascript" src="../../../styles/bootstrap-notify.js"></script>
                                  <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
                                  <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datepicker.js"/></script>
                                  <script language="JavaScript">
                                      changeVendorFisrt();
                                      document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID]%>.focus();
                                  </script>
                                  <!-- #EndEditable --> </td>

                              <script language="JavaScript">
                                <%

                                  // add by fitra 10-5-2014
                                  if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE) {%>
                                    addItem();
                                <% }%>

                              </script>

                              </tr>
                          </table>
                          </body>

                          <!-- #EndTemplate --></html>


