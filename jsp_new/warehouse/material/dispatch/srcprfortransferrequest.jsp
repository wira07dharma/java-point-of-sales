<%-- 
    Document   : srcprfortransferrequest.jsp
    Created on : Apr 23, 2014, 10:22:58 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!  public static final int ADD_TYPE_SEARCH = 0;
  public static final int ADD_TYPE_LIST = 1;

  public static final String textMainHeader[][]
          = {
            {"Gudang", "Store Request"},
            {"Warehouse", "Store Request"}
          };

  /* this constant used to list text of listHeader */
  public static final String textListMaterialHeader[][]
          = {
            {"No", "Kode", "Tanggal", "Supplier", "Status", "Action", "Mata Uang", "Lokasi"},
            {"No", "Code", "Date", "Supplier", "Status", "Action", "Currency", "Location"}
          };

  /* this constant used to list text of listHeader */
  public static final String textListHeader[][]
          = {
            {"Nomor", "Supplier", "Tanggal", "Status", "Urut Berdasar", "Transfer dengan Store Request", "Gudang", "Semua Tanggal", "Tanggal Awal", "Tanggal Akhir", "Buat PO"},
            {"Number", "Supplier", "Date", "Status", "Sort By", "Dispatch for Store Request", "Warehouse", "All Date", "Date From", "Date To", "Create PO"}
          };

  public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
    String result = "";
    if (addBody) {
      if (language == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
        result = textListHeader[language][index] + " " + prefiks;
      } else {
        result = prefiks + " " + textListHeader[language][index];
      }
    } else {
      result = textListHeader[language][index];
    }
    return result;
  }

  public boolean getTruedFalse(Vector vect, int index) {
    for (int i = 0; i < vect.size(); i++) {
      int iStatus = Integer.parseInt((String) vect.get(i));
      if (iStatus == index) {
        return true;
      }
    }
    return false;
  }

  public String drawList(int language, Vector objectClass, int start, int docType, boolean  privAdd,  I_DocStatus i_status) {
    String result = "";
    if (objectClass != null && objectClass.size() > 0) {
      ControlList ctrlist = new ControlList();
      ctrlist.setAreaWidth("100%");
      ctrlist.setListStyle("listgen");
      ctrlist.setTitleStyle("listgentitle");
      ctrlist.setCellStyle("listgensell");
      ctrlist.setHeaderStyle("listgentitle");
      ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
      ctrlist.addHeader(textListMaterialHeader[language][1], "14%");
      ctrlist.addHeader(textListMaterialHeader[language][7], "7%");
      ctrlist.addHeader(textListMaterialHeader[language][2], "7%");
      //ctrlist.addHeader(textListMaterialHeader[language][3],"30%");
      ctrlist.addHeader(textListMaterialHeader[language][4], "7%");
      ctrlist.addHeader(textListMaterialHeader[language][5], "10%");

      ctrlist.setLinkRow(1);
      ctrlist.setLinkSufix("");
      Vector lstData = ctrlist.getData();
      Vector lstLinkData = ctrlist.getLinkData();
      ctrlist.setLinkPrefix("javascript:cmdEdit('");
      ctrlist.setLinkSufix("')");
      ctrlist.reset();
      if (start < 0) {
        start = 0;
      }

      for (int i = 0; i < objectClass.size(); i++) {
        Vector vt = (Vector) objectClass.get(i);
        PurchaseRequest po = (PurchaseRequest) vt.get(0);
        //ContactList contact = (ContactList)vt.get(1);

        start = start + 1;

        Vector rowx = new Vector();
        rowx.add("" + start);
        rowx.add(po.getPrCode());
        rowx.add(po.getLocationName());

        String str_dt_PurchDate = "";
        try {
          Date dt_PurchDate = po.getPurchRequestDate();
          if (dt_PurchDate == null) {
            dt_PurchDate = new Date();
          }
          str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
        } catch (Exception e) {
          str_dt_PurchDate = "";
        }
        rowx.add(str_dt_PurchDate);

        //rowx.add(cntName);
        rowx.add(i_status.getDocStatusName(docType, po.getPrStatus()));

        //button
		String button = "";
        button = "<center>"
                + "<input type=\"hidden\" name=\"pomaterial\" value=\"" + po.getOID() + "\">"
                + "<input type=\"radio\" name=\"pomaterial_" + po.getOID() + "\" value=\"1\">"
                + "</center>";
//    if(privAdd){
//		button = "<input type=\"hidden\" name=\"pomaterial\" value=\"" + po.getOID() + "\">" 
//           + "<a href=\"javascript:cmdAdd()\" class=\"btn btn-primary\" name=\"pomaterial_" + po.getOID() + "\" value=\"1\">"
//					+ textListHeader[language][10]
//					+ "</a>";
//      }
		rowx.add(button);

        lstData.add(rowx);
        lstLinkData.add(String.valueOf(po.getOID()));
      }
      result = ctrlist.draw();
    } else {
      result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";
    }
    return result;
  }

%>


<!-- Jsp Block -->
<%  /**
   * get approval status for create document
   */
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>

<%
  /**
   * get title for purchasing(pr) document
   */
  String poCode = "PO"; //i_pstDocType.getDocCode(docType);
  String poTitle = textMainHeader[SESS_LANGUAGE][1]; //i_pstDocType.getDocTitle(docType);
  String poItemTitle = poTitle + " Item";
  String poTitleBlank = "";

  /**
   * get request data from current form
   */
  long oidPurchaseRequest = FRMQueryString.requestLong(request, " hidden_material_request_id");

  /**
   * initialitation some variable
   */
  int iErrCode = FRMMessage.ERR_NONE;
  String msgStr = "";
  int iCommand = FRMQueryString.requestCommand(request);
  int start = FRMQueryString.requestInt(request, "start");
  int recordToGet = 20;
  int vectSize = 0;
  String whereClause = "";

  /**
   * instantiate some object used in this page instantiate some object used in
   * this page
   */
  ControlLine ctrLine = new ControlLine();
  CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
  SrcPurchaseRequest srcPurchaseRequest = new SrcPurchaseRequest();
  SessPurchaseRequest sessPurchaseRequest = new SessPurchaseRequest();
  FrmSrcPurchaseRequest frmSrcPurchaseRequest = new FrmSrcPurchaseRequest(request, srcPurchaseRequest);

  /**
   * handle current search data session
   */
  if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
    try {
      srcPurchaseRequest = (SrcPurchaseRequest) session.getValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
      if (srcPurchaseRequest == null) {
        srcPurchaseRequest = new SrcPurchaseRequest();
      }
    } catch (Exception e) {
      System.out.println(" Session null : " + e);
      srcPurchaseRequest = new SrcPurchaseRequest();
    }
  } else {
    frmSrcPurchaseRequest.requestEntityObject(srcPurchaseRequest);
    Vector vectSt = new Vector(1, 1);
    String[] strStatus = request.getParameterValues(FrmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]);
    if (strStatus != null && strStatus.length > 0) {
      for (int i = 0; i < strStatus.length; i++) {
        try {
          vectSt.add(strStatus[i]);
        } catch (Exception exc) {
          System.out.println("err");
        }
      }
    }
    srcPurchaseRequest.setPrmstatus(vectSt);
    session.putValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL, srcPurchaseRequest);
  }

  /**
   * get vectSize, start and data to be display in this page
   */
  String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

  vectSize = sessPurchaseRequest.getCountTransferRequestMaterial(srcPurchaseRequest, docType, 1, whereClausex);
  if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
    start = ctrlPurchaseRequest.actionList(iCommand, start, vectSize, recordToGet);
  }

  Vector records = sessPurchaseRequest.searchPurchaseRequestForPOMaterial(srcPurchaseRequest, docType, start, recordToGet, 1, whereClausex);

%>
<%  /**
   * get data from 'hidden form'
   */
  try {
    srcPurchaseRequest = (SrcPurchaseRequest) session.getValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
  } catch (Exception e) {
    srcPurchaseRequest = new SrcPurchaseRequest();
  }

  if (srcPurchaseRequest == null) {
    srcPurchaseRequest = new SrcPurchaseRequest();
  }

  try {
    session.removeValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
  } catch (Exception e) {
  }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
  <head>
    <!-- #BeginEditable "doctitle" -->
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">

      function cmdAdd() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.ADD%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.approval_command.value = "<%=Command.SAVE%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.add_type.value = "<%=ADD_TYPE_SEARCH%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "create_transferfromtransferequest.jsp";
        if (compareDateForAdd() == true)
          document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdEdit(oid) {
        var strvalue = "<%=approot%>/purchasing/material/pom/prtowarehousematerial_edit.jsp?hidden_material_request_id=" + oid + "&command=3";
        winSrcMaterial = window.open(strvalue, "", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        if (window.focus) {
          winSrcMaterial.focus();
        }
      }
      function cmdListFirst() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.FIRST%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "prmaterial_list.jsp";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdListPrev() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.PREV%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "prmaterial_list.jsp";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdListNext() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.NEXT%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "prmaterial_list.jsp";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdListLast() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.LAST%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "prmaterial_list.jsp";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdBack() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.BACK%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "srcprfortransferrequest.jsp";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
      function cmdSearch() {
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.command.value = "<%=Command.LIST%>";
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.action = "srcprfortransferrequest.jsp"; //srcprfortransferrequest.jsptransferfromtrmaterial_list
        document.<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>.submit();
      }
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
    </script>
    <!-- #EndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <!-- #BeginEditable "styles" -->
    <%if (menuUsed == MENU_ICON) {%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
	<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
	<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
	<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
	<link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>
    <style>
      .listgentitle {
        font-size: 11px;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        border: 1px solid #fff !important;
      }.listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3 !important;    
        text-align: center;
      }
      td.command {
        margin-bottom: 10px !important;
        padding-bottom: 10px;
      }
      select {
        -webkit-transition: all 0.30s ease-in-out;
        -moz-transition: all 0.30s ease-in-out;
        -ms-transition: all 0.30s ease-in-out;
        -o-transition: all 0.30s ease-in-out;
        outline: none;
        padding: 3px 0px 3px 3px;
        margin: 5px 1px 3px 0px;
        border: 1px solid rgb(62, 133, 195);
        height: 30px;
        border-radius: 4px;
      }

   select.formElemen {
    height: 30px;
    border: 1px #ccc solid;
    width: 30% !important;
    border-radius: 0px;
}
.col-sm-4.col-sm-offset-2 {
    padding-left: 10px;
}
.tgl {
    padding: 0px !important;
    margin: 0px !important;
}
.col-sm-8.dated {
    margin-bottom: 0px !important;
    padding-bottom: 0px !important;
    height: 40px;
}
select#selectUrut {
    margin: 0;
}
a.btn.btn-primary {
    margin: 10px 10px 10px 30px;
    font-size: 12px;
}
table.listarea {
    width: 95%;
    margin: 10px 10px 10px 20px;
}
table.table-list {
    margin-left: 10px;
}
a.btn.btn-primary.btn-add {
    margin: 0px 0px 0px 20px;
}
select.formDate {
    width: 31.87%;
    border: 1px solid #ccc;
    border-radius: 0px;
}
    </style>
    <!-- #EndEditable -->
  </head>

  <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <section class="content-header">
        <h1><%=textListHeader[SESS_LANGUAGE][5]%>
            <small> menu</small></h1>
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
      <tr><td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td></tr>
      <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td></tr><%} else {%>
      <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %></td></tr><%}%>
      <tr>
        <td valign="top" align="left">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><!-- #BeginEditable "content" -->
                <form name="<%=FrmSrcPurchaseRequest.FRM_NAME_SRCORDERMATERIAL%>" method="post" action="">
                  <input type="hidden" name="command" value="<%=iCommand%>">
                  <input type="hidden" name="add_type" value="">
                  <input type="hidden" name="approval_command">
                  <input type="hidden" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                  <table width="100%" border="0">
                    <div class="row">
                            <div class="col-sm-12">
                                <!--NOMOR-->
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3" for="selectNomor"><%=getJspTitle(0, SESS_LANGUAGE, poCode, false)%> </label>
                                        <div class="col-sm-8">
                                            <input id="selectNomor" class="form-control" type="text" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMNUMBER]%>" value="<%= srcPurchaseRequest.getPrmnumber()%>">
                                        </div>
                                    </div>
                                </div>
                                <!--Urut Berdasarka-->
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label class="control-label col-sm-4" for="selectUrut"><%=getJspTitle(4, SESS_LANGUAGE, poCode, false)%> </label>
                                        <div class="col-sm-8">
                                          <select class="form-control" id="selectUrut" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_SORTBY]%>" value="<%= srcPurchaseRequest.getSortby()%>" >
                                            <option value="0">Nomor</option>
                                            <option value="1">Lokasi Asal</option>
                                            <option value="2">Lokasi Tujuan</option>
                                            <option value="3">Tanggal</option>
                                            <option value="4">Status</option>
                                            </select>
                                        </div>
                                    </div> <!--formgroup--> 
                                </div> 
                            </div>
                        </div>
                    <tr class="date-test">
                            <div class="row">
                                    <div class="col-sm-12">
                                        <!--Tanggal-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-3" for="CustomeDate"><%=textListHeader[SESS_LANGUAGE][8] %></label>
                                                <div class="col-sm-8 dated">
                                                        <input type="radio" id="CustomeDate" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]%>" <%if (srcPurchaseRequest.getStatusdate() == 1) {%>checked<%}%> value="1">
                                                        <%=ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATEFROM], (srcPurchaseRequest.getPrmdatefrom()==null) ? new Date() : srcPurchaseRequest.getPrmdatefrom(), "formElemen", 1, -5)%>  &nbsp;
                                                </div>  
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                              <label class="control-label col-sm-4"><%=textListHeader[SESS_LANGUAGE][9] %></label>
                                                <div class="col-sm-8 dated">
                                                  <%=	ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATETO], (srcPurchaseRequest.getPrmdateto()==null) ? new Date() : srcPurchaseRequest.getPrmdateto(), "formDate", 1, -5)%> 
                                                </div>  
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group tgl">
                                                <div class="col-sm-4 col-sm-offset-3">
                                                  <input id="allDate" type="radio" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]%>" <%if (srcPurchaseRequest.getStatusdate() == 0) {%>checked<%}%> value="0" ><label for="allDate">&nbsp;<%=textListHeader[SESS_LANGUAGE][7] %></label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                          </tr>
                          <a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%></a>
                    
                    <tr>
                    <div class="table-responsive">
                      <table width="100%" cellspacing="0" cellpadding="3" class="table-list">
                        <tr align="left" valign="top">
                          <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, privAdd, i_status)%></td>
                        </tr>
                        <tr align="left" valign="top">
                          <td height="8" align="left" colspan="3" class="command">
                            <span class="command">
                              <%
                                ctrLine.setLocationImg(approot + "/images");
                                ctrLine.initDefault();
                                out.println(ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet));
                              %>
                            </span>
                          </td>
                        </tr>
                        <tr align="left" valign="top">
                          <td height="18" valign="top" colspan="3">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <%if (privAdd) {%>
                                <a class="btn btn-primary btn-add" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ADD, true)%></a>
                                <%}%>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </div>
                    </tr>
                  </table>
              </td>
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
    <%if (menuUsed == MENU_ICON) {%>
    <%@include file="../../../styletemplate/footer.jsp" %>
    <%} else {%><%@ include file = "../../../main/footer.jsp" %><%}%></td>
</tr>
</table>
    </div>
<%--autocomplate--%>
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>					
<script type="text/javascript">
	$(function (){
		console.log("Dokumen redi bois");
	});
</script>	
	
	
</body>
<!-- #EndTemplate --></html>

