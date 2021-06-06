
<%-- 
    Document   : srcprtowarehousematerial_new
    Created on : Oct 8, 2019, 3:06:26 PM
    Author     : Regen
--%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
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
  public static int getStrDutyFree(){
	String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
	System.out.println("#Duty Free: " + strDutyFree);
	int dutyFree = Integer.parseInt(strDutyFree);
	return dutyFree;
}

  /* this constant used to list text of listHeader */
  public static final String textListHeader[][]
          = {
            {"Nomor", "Supplier", "Tanggal", "Status", "Urut Berdasar", "Store Request", "Gudang", "Semua Tanggal", "Tanggal Awal", "Tanggal Akhir", "Pencarian", "Pilih Semua", "Batal Pilih Semua", "Request Transfer","Cari","Tambah"},
            {"Number", "Supplier", "Date", "Status", "Sort By", "Store Request", "Warehouse", "All Date", "From Date", "To Date", "Search", "Select All", "Cancel Select All", "Request Transfer","Search","Add"}
          };

  public static final String textMainHeader[][]
          = {
            {"Gudang", "Request Barang"},
            {"Warehouse", "Purchase Request"}
          };

  /* this constant used to list text of listHeader */
  public static final String textListMaterialHeader[][]
          = {
            {"No", "Kode", "Tanggal", "Lokasi Asal", "Status", "Keterangan", "Lokasi Tujuan"},
            {"No", "Code", "Date", "Origin", "Status", "Remark", "Destination"}
          };

  public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status) {
    String result = "";
    if (objectClass != null && objectClass.size() > 0) {
      ControlList ctrlist = new ControlList();
      ctrlist.setAreaWidth("90%");
      ctrlist.setListStyle("listgen");
      ctrlist.setTitleStyle("listgentitle");
      ctrlist.setCellStyle("listgensell");
      ctrlist.setHeaderStyle("listgentitle");
      ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
      ctrlist.addHeader(textListMaterialHeader[language][1], "15%");
      ctrlist.addHeader(textListMaterialHeader[language][2], "10%");
      ctrlist.addHeader(textListMaterialHeader[language][6],"20%");
      ctrlist.addHeader(textListMaterialHeader[language][3],"20%");
      ctrlist.addHeader(textListMaterialHeader[language][4], "7%");
      ctrlist.addHeader(textListMaterialHeader[language][5], "15%");

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

        Location asal = null;
        Location tujuan = null;

        try {
          asal = PstLocation.fetchExc(po.getLocationId());
          tujuan = PstLocation.fetchExc(po.getWarehouseSupplierId());            
          } catch (Exception e) {
          }

        start = start + 1;
        Vector rowx = new Vector();
        rowx.add("" + start);
        rowx.add(po.getPrCode());

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
        rowx.add(asal.getName());
        rowx.add(tujuan.getName());

        //rowx.add(cntName);
        rowx.add(i_status.getDocStatusName(docType, po.getPrStatus()));
        rowx.add(po.getRemark());

        lstData.add(rowx);
        lstLinkData.add(String.valueOf(po.getOID()));
      }
      result = ctrlist.draw();
    } else {
      result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";
    }
    return result;
  }

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
   * get data from 'hidden form'
   */
  int iCommand = FRMQueryString.requestCommand(request);

  /**
   * declaration of some identifier
   */
  String poCode = "PO"; //i_pstDocType.getDocCode(docType);
  String poTitle = textListHeader[SESS_LANGUAGE][5];
  String poItemTitle = poTitle + " Item";
  String poTitleBlank = "";

  long oidPurchaseRequest = FRMQueryString.requestLong(request, " hidden_material_request_id");
  /**
   * ControlLine
   */
  ControlLine ctrLine = new ControlLine();

  int iErrCode = FRMMessage.ERR_NONE;
  String msgStr = "";

  int start = FRMQueryString.requestInt(request, "start");
  int recordToGet = 20;
  int vectSize = 0;
  String whereClause = "";

  CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
  SrcPurchaseRequest srcPurchaseRequest = new SrcPurchaseRequest();
  SessPurchaseRequest sessPurchaseRequest = new SessPurchaseRequest();
  FrmSrcPurchaseRequest frmSrcPurchaseRequest = new FrmSrcPurchaseRequest(request, srcPurchaseRequest);
  srcPurchaseRequest.setSupplierWarehouse(-2);


  iErrCode = ctrlPurchaseRequest.action(iCommand, oidPurchaseRequest, userName, userId);
  FrmPurchaseRequest frmpo = ctrlPurchaseRequest.getForm();
  PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();

  /*try
{
	srcPurchaseRequest = (SrcPurchaseRequest)session.getValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
}
catch(Exception e)
{
	srcPurchaseRequest = new SrcPurchaseRequest();
}


if(srcPurchaseRequest==null){
	srcPurchaseRequest = new SrcPurchaseRequest();
}

try
{
	session.removeValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
}catch(Exception e){
}*/
  String sOidNumber = FRMQueryString.requestString(request, frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMNUMBER]);
  int oidDate = FRMQueryString.requestInt(request, frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]);
  int oidSortBy = FRMQueryString.requestInt(request, frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_SORTBY]);

//String sVendor = FRMQueryString.requestString(request,frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_VENDORNAME]);
  if (oidDate == 1 || sOidNumber != "" || oidSortBy != 0) {
    frmSrcPurchaseRequest.requestEntityObject(srcPurchaseRequest);
  }
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

  String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

  vectSize = sessPurchaseRequest.getCountPurchaseRequestMaterial(srcPurchaseRequest, docType, 1, whereClausex);
  if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
    start = ctrlPurchaseRequest.actionList(iCommand, start, vectSize, recordToGet);
  }

  Vector records = sessPurchaseRequest.searchPurchaseRequestMaterial(srcPurchaseRequest, docType, start, recordToGet, 1, whereClausex);

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
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

    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
      function cmdAdd() {
        document.frm_requestmaterial.command.value = "<%=Command.ADD%>";
        document.frm_requestmaterial.approval_command.value = "<%=Command.SAVE%>";
        document.frm_requestmaterial.add_type.value = "<%=ADD_TYPE_SEARCH%>";
        document.frm_requestmaterial.action = "prtowarehousematerial_edit.jsp";
        if (compareDateForAdd() == true)
          document.frm_requestmaterial.submit();
      }

      function cmdSearch() {

        document.frm_requestmaterial.command.value = "<%=Command.LIST%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }


      function cmdEdit(oid) {
        document.frm_requestmaterial.hidden_material_request_id.value = oid;
        document.frm_requestmaterial.command.value = "<%=Command.EDIT%>";
        document.frm_requestmaterial.action = "prtowarehousematerial_edit.jsp";
        document.frm_requestmaterial.submit();
      }

      function cmdListFirst() {
        document.frm_requestmaterial.command.value = "<%=Command.FIRST%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }

      function cmdListPrev() {
        document.frm_requestmaterial.command.value = "<%=Command.PREV%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }

      function cmdListNext() {
        document.frm_requestmaterial.command.value = "<%=Command.NEXT%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }

      function cmdListLast() {
        document.frm_requestmaterial.command.value = "<%=Command.LAST%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }

      function cmdBack() {
        document.frm_requestmaterial.command.value = "<%=Command.BACK%>";
        document.frm_requestmaterial.action = "srcprtowarehousematerial.jsp";
        document.frm_requestmaterial.submit();
      }

      function MM_swapImgRestore() {
        //v3.0
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
    <%if (menuUsed == MENU_ICON) {%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
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
      }
      a.btn.btn-primary.store {
    margin: 25px 0px 0px 30px;
}
.listgensell {
    color: #000000;
    background-color: #ffffff !important;
    border: 1px solid #3e85c3;
}

input#selectNomor {
    margin-bottom: 10px;
}

#CheckStatusAllBtn, #UnCheckStatusAllBtn{
cursor: pointer;
}
.list-status {
    list-style: none;
    display: flex;
    margin: 0 10px 5px 0px;
    padding: 0px;
}
li {
    padding-right: 15px;
}
.col-sm-10.stt {
    padding-left: 10px;
    padding-top: 10px;
}
label.control-label.col-sm-2 {
    padding-top: 10px;
}
select.tanggal {
    height: 30px;
    border: 1px #ccc solid;
    width: 24% !important;
}
select.formElemen {
    height: 30px;
    border: 1px #ccc solid;
    width: 29.3%;
}
.col-sm-4.col-sm-offset-2 {
    padding-left: 10px;
}
.tgl {
    padding: 0px !important;
    margin: 0px !important;
}
.col-sm-8.dated {
    margin-bottom: 5px !important;
    padding-bottom: 0px !important;
    height: 40px;
    padding-left: 31px;
    padding-right: 20px;
    padding-top: 10px;
}
label.kaput {
    border: 1px solid #f49503;
    padding: 5px 10px 5px 10px;
    background-color: #f59004;
    color: #fff;
    border-radius: 5px;
    font-size: 14px;
    font-weight: 100;
    margin-left: 5px;
}
.col-sm-5.polos {
    margin-left: 27.9%;
}
    </style>
  </head>
  <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <section class="content-header">
      <%if(getStrDutyFree() == 1){%>
            <h1><%=textListHeader[SESS_LANGUAGE][5]%>
            <small> menu</small></h1><%}else{%>
            <h1><%=textListHeader[SESS_LANGUAGE][13]%>
            <small> menu</small></h1><%}%>
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
        <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td></tr>
        <%} else {%>
        <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %></td></tr>
        <%}%>
        <tr>
          <td valign="top" align="left">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td ><!-- #BeginEditable "content" -->
                  <form name="frm_requestmaterial" method="post" action="">
                    <input type="hidden" name="command" value="<%=iCommand%>">
                    <input type="hidden" name="add_type" value="">
                    <input type="hidden" name="approval_command">
                    <input type="hidden" name="hidden_material_request_id" value="">
                    <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_DOCUMENT_TYPE]%>" value="PPBTBB">
                    <input type="hidden" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.fieldNames[PstLocation.TYPE_LOCATION_WAREHOUSE]%>">
                    <table width="100%" border="0">
                      <td style="font-size: 14px">
                        <table width="100%" border="0" cellspacing="1" cellpadding="1">
                          <div class="row">
                            <div class="col-sm-12">
								<!-- left Side -->
								<div class="col-md-6"> 
									<!--NOMOR-->
									<div class="form-group">
										<label class="control-label col-sm-4" for="selectNomor"><%=textListHeader[SESS_LANGUAGE][0]%> </label>
										<div class="col-sm-8">
											<input id="selectNomor" class="form-control" type="text" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMNUMBER]%>" value="<%= srcPurchaseRequest.getPrmnumber()%>">
										</div>
									</div>
									<!--Urut Berdasarka-->
									<div class="form-group">
										<label class="control-label col-sm-4" for="selectUrut"><%=textListHeader[SESS_LANGUAGE][4]%> </label>
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
									<div class="form-group">
										<label class="control-label col-sm-4"><%=textListHeader[SESS_LANGUAGE][3]%> </label>
										<div class="col-sm-8 stt">
											<ul class="list-status">
												<%
													Vector vectResult = i_status.getDocStatusFor(docType);
													for (int i = 0; i < vectResult.size(); i++) {
														if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL)) {
															Vector vetTemp = (Vector) vectResult.get(i);
															int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
															String strPrStatus = String.valueOf(vetTemp.get(1));
												%>
												<li>
													<input type="checkbox" class="formElemen checks" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>">
													<%=strPrStatus%>
												</li>
												<%
														}
													}
												%>
											</ul>
											<a id="CheckStatusAllBtn">
												<%= textListHeader[SESS_LANGUAGE][11]%>
											</a>
											|
											<a id="UnCheckStatusAllBtn">
												<%= textListHeader[SESS_LANGUAGE][12]%>
											</a>
										</div>
									</div> 

								</div>
								<!-- right Side -->
								<div class="col-md-6">
									<!--Tanggal-->
									<div class="form-group">
										<label class="control-label col-sm-3" for="CustomeDate"><%=textListHeader[SESS_LANGUAGE][8]%></label>
										<div class="col-sm-9 dated">
                        <input type="radio" id="CustomeDate" class="col-md-2" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]%>" <%if (srcPurchaseRequest.getStatusdate() == 1) {%>checked<%}%> value="1">
											<%=ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATEFROM], srcPurchaseRequest.getPrmdatefrom(), "tanggal", 1, -5)%>  &nbsp;
										</div>  
									</div>
									<div class="form-group">
                       <label class="control-label col-sm-4" style="padding-top: 10px"><%=textListHeader[SESS_LANGUAGE][9]%></label>
										<div class="col-sm-8 dated">
											<%=	ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATETO], srcPurchaseRequest.getPrmdateto(), "formElemen", 1, -5)%> 
										</div>  
									</div>
									<div class="form-group tgl">
										<div class="col-sm-5 polos">
                        <input id="allDate" type="radio" class="col-md-2" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]%>" <%if (srcPurchaseRequest.getStatusdate() == 0) {%>checked<%}%> value="0" ><label for="allDate" class="col-md-10">&nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></label>
										</div>
									</div>
									<!--formgroup--> 
								</div>
                            </div>
                        </div>                                            

                          <tr class="date-test">
                            <div class="row">
                                    <div class="col-sm-12">
                                         
                                    </div>
                                </div>
                          </tr>
                          
                          <tr>
                            <td class="btn-test" height="21" colspan="3" align="left" valign="top">
                              <table width="80%" border="0" cellspacing="0" cellpadding="0">
                                <%if(getStrDutyFree() == 1 ){ %>
                                <tr>
                                  <!--td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%>"></a></td-->
                                  <td class="command" nowrap width="1%"><a class="btn btn-primary store" href="javascript:cmdSearch()"><i class="fa fa-search"></i><%=textListHeader[SESS_LANGUAGE][14]%> <%=textListHeader[SESS_LANGUAGE][5]%></a>
                                  <% if (privAdd) {%>
                                  <a class="btn btn-primary store" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=textListHeader[SESS_LANGUAGE][15]%> <%=textListHeader[SESS_LANGUAGE][5]%></a></td>
                                  <%}%>
                                </tr>
                                <%}else{%>
                                <tr>
                                  <!--td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%>"></a></td-->
                                  <td class="command" nowrap width="1%"><a class="btn btn-primary store" href="javascript:cmdSearch()"><i class="fa fa-search"></i><%=textListHeader[SESS_LANGUAGE][14]%> <%=textListHeader[SESS_LANGUAGE][13]%></a>
                                  <% if (privAdd) {%>
                                  <a class="btn btn-primary store" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=textListHeader[SESS_LANGUAGE][15]%> <%=textListHeader[SESS_LANGUAGE][13]%></a></td>
                                  <%}%>
                                </tr><%}%>
                              </table></td>
                          </tr>
                        </table>
                      </td>
                      <td class="status" style="padding-bottom: 90px;">
                        <table width="85%" border="0" cellspacing="1" cellpadding="1">
                          
																<tr>
																	
																</tr>
                        </table>
                      </td>
                      </tr>
                    </table>
                  </form>
                  <!-- #EndEditable --></td>
              </tr>


              <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>	 
              <tr align="left" valign="top"> 
                <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, i_status)%></td>
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
                  <table width="52%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    </tr>
                  </table>
                </td>
              </tr>
              <%}%>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan="2" height="20">
            <%if (menuUsed == MENU_ICON) {%>
            <%@include file="../../../styletemplate/footer.jsp" %>
            <%} else {%>
            <%@ include file = "../../../main/footer.jsp" %>
            <%}%></td>
        </tr>
      </table>
    </div>
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
  <script>
    $(document).ready(function(){
    				$('#CheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', true);
				});
				$('#UnCheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', false);
				});
        
    $(".datepicker").datepicker({
        
        format: "yyyy-mm-dd",
        autoclose: true
    });
    $( ".datepicker" ).datepicker( "setDate", new Date());
    //ALL DATE
        $("#allDate").change(function () {
            if ($(this).is(':checked')) {
                $(".datepicker").attr('disabled', true);
                $(".datepicker").val("");
                
            } else {
                $(".datepicker").attr('disabled', false);
            }
        });
    });
  </script>
</body>
</html>
