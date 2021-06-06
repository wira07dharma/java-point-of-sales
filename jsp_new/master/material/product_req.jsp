<%@page import="com.dimata.harisma.entity.masterdata.Position"%>
<%@page import="com.dimata.aiso.entity.masterdata.ModulAktiva"%>
<%@page import="com.dimata.aiso.entity.masterdata.PstModulAktiva"%>
<%@page import="com.dimata.hanoman.form.outletrsv.materialreqfacility.FrmMaterialReqFacility"%>
<%@page import="com.dimata.harisma.entity.masterdata.Competency"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstCompetency"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstLevel"%>
<%@page import="com.dimata.harisma.entity.masterdata.Section"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstSection"%>
<%@page import="com.dimata.harisma.entity.masterdata.Department"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDepartment"%>
<%@page import="com.dimata.hanoman.form.outletrsv.materialreqpersonoption.FrmMaterialReqPersonOption"%>
<%@page import="com.dimata.hanoman.form.outletrsv.materialreqperson.FrmMaterialReqPerson"%>
<%@page import="com.dimata.posbo.entity.masterdata.RoomClass"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoomClass"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.hanoman.entity.outletrsv.materialreqlocation.PstMaterialReqLocation"%>
<%@page import="com.dimata.hanoman.entity.outletrsv.materialreqlocation.MaterialReqLocation"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMaterial"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.hanoman.form.outletrsv.materialreqlocation.FrmMaterialReqLocation"%>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);%>
<%@ include file = "../../main/javainit.jsp" %>
<%@ include file = "../../main/checkuser.jsp" %>
<%!  public static final int ADD_TYPE_SEARCH = 0;
  public static final int ADD_TYPE_LIST = 1;
  public static final String textListHeader[][] = {
    {"Room Class", "Duration", "Kasir", "Supplier", "Kategori", "Tanggal", "Urut Berdasar", "Kode Sales", "Semua Lokasi", "Cari"},
    {"Location", "Shift", "Cashier", "Supplier", "Category", "Sale Date", "Sort By", "Sales Code", "All Location", "Search"}
  };
  public static final String textButton[][] = {
    {"Rekap Harian", "Laporan Rekap Penjualan Per Tanggal", "Grafik Rekap Harian"},
    {"Daily Report", "Summary Report of Sales Per Day", "Sales Per Day Chart"}
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

%>
<%
  int iCommand = FRMQueryString.requestCommand(request);
  long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
  Material material = new Material();
  if(oidMaterial!=0){
    material = PstMaterial.fetchExc(oidMaterial);
  }

  long roomClassId = FRMQueryString.requestLong(request, FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_POS_ROOM_CLASS_ID]);
  int duration = FRMQueryString.requestInt(request, FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_DURATION]);
  int locIndex = FRMQueryString.requestInt(request, FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_ORDER_INDEX]);
  long materialId = FRMQueryString.requestLong(request, FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_MATERIAL_ID]);

  long oidMaterialReqLocation = 0;
  boolean privManageData = true;
  int recordToGet = 100;
  String msgString = "";
  int iErrCode = FRMMessage.NONE;
  String whereClause = "" + PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_ID] + "=\"" + oidMaterial + "\"";
  String orderClause = "" + PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_REQ_LOCATION_ID];
  int typemenu = FRMQueryString.requestInt(request, "typemenu");

%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- #BeginEditable "doctitle" -->
    <title>Dimata - ProChain POS</title>
    <script>
      var save = <%=Command.SAVE%>;
      var list = <%=Command.LIST%>;
      var del = <%=Command.DELETE%>;
      var approve = <%=Command.APPROVE%>;
      var approot = "<%=approot%>";
      var baseUrl = function (uri = "") {
        return approot + "/" + uri;
      };
    </script>
    <script language="JavaScript">

      function cmdAdd() {
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.value = "0";
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.command.value = "<%=Command.ADD%>";
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.action = "product_req.jsp";
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.submit();
      }

      function cmdSave() {
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.command.value = "<%=Command.SAVE%>";
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.action = "product_req.jsp";
        document.<%=FrmMaterialReqLocation.FRM_NAME_MATERIAL_REQ_LOCATION%>.submit();
      }

      function cmdEdit(oid)
      {
        document.frm_material.hidden_material_id.value = oid;
        document.frm_material.start.value = 0;
        document.frm_material.approval_command.value = "<%=Command.APPROVE%>";
        document.frm_material.command.value = "<%=Command.EDIT%>";
        document.frm_material.action = "material_main.jsp";
        document.frm_material.submit();
      }


      function cmdsearchcode(oidcode) {
        document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value = oidcode;
        //alert(document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value);
        document.frmsrcmaterial.command.value = "<%=Command.LIST%>";
        document.frmsrcmaterial.action = "material_list.jsp?hidden_range_code_id=" + oidcode + "";
        document.frmsrcmaterial.submit();
      }

      function cmdSelectSubCategory() {
        window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory=" + document.frmsrcmaterial.txt_subcategory.value +
                "&caller=1" +
                "&oidCategory=" + document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>.value, "material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
      }

      function fnTrapKD(evt) {
        if (evt.keyCode === 13) {
          document.all.aSearch.focus();
          cmdSearch();
        }
      }



      function cmdPrintAllPriceTag() {
        var strvalue = "approot/masterdata/barcode.jsp?command=<%=Command.ADD%>";
        winSrcMaterial = window.open(strvalue, "searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        if (window.focus) {
          winSrcMaterial.focus();
        }
      }

      //-------------- script control line -------------------
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

    </SCRIPT>
    <!-- #EndEditable --> 
    <%--autocomplate add by fitra--%>
    <script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
    <script src="../../styles/jquery.autocomplete.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="../../vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <!-- Font Awesome -->
    <link href="../../vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="../../vendors/nprogress/nprogress.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="../../vendors/build/css/custom.min.css" rel="stylesheet">

    <link href="../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
    <link href="../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
    <link href="../../styles/toast/src/jquery.toast.css" rel="stylesheet" media="screen">
    <link href="../../styles/product.css" rel="stylesheet" media="screen">

  </head>
  <body class="nav-md" style="diplay:none"> 
    <div class="right_col" role="main">
      <div class="">
        <div class="clearfix"></div>

        <div>
          <div>
            <table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#FCFDEC" >

              <%if (/*menuUsed == MENU_PER_TRANS*/false) {%>

              <tr>
                <td class="table-hover" height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                  <%@ include file = "../../../../main/header.jsp" %>
                </td>
              </tr>
              <tr>
                <td class="table-hover" height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                  <%@ include file = "../../../../main/mnmain.jsp" %>
                  <!-- #EndEditable --> </td>
              </tr>
              <%} else {%>
              <tr bgcolor="#FFFFFF">
                <td class="table-hover" eight="10" ID="MAINMENU">
                  <%@include file="../../../../styletemplate/template_header.jsp" %>
                </td>
              </tr>
              <%}%>
              <tr> 
                <td style="padding: 7px 5px; background-color: #e8e8e8; color: #656565;" height="20" class="mainh"><!-- #BeginEditable "contenttitle" -->&nbsp;Masterdata
                  &gt; Item Editor : (<%=material.getSku()%>) <%=material.getFullName()%><!-- #EndEditable --></td>
              </tr>
              <tr> 
                <td style="background-color: #e8e8e8;" height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;</td>
              </tr>
            </table>
          </div>
          <div style="padding-bottom:30px;">
            <ul class="nav nav-tabs" style="padding: 0 10px; margin-bottom: 20px; background-color: #e8e8e8;">
              <li role="presentation"><a style="background-color: #f3f3f3; border: 0; margin-top: 1px; color: #949494;" href="javascript:cmdEdit('<%=material.getOID()%>')">Main Data</a></li>
              <% if(material.getOID()!=0){%><li class="active" role="presentation"><a href="javascript:setProductReq('<%=material.getOID()%>')">Product Requirements </a></li><%}%>
            </ul>
          </div>
          <div class="mainheader container-fluid"><!-- #BeginEditable "contenttitle" --> 
            <div class="col-xs-12">
              <new-product id="product-new" class="room-class ">
                <div class="panel panel-default super-form">
                  <div class="panel-heading">
                    <strong>Add New Product Requirements</strong>
                  </div>
                  <div class="panel-body">
                    <form data-value="0" method="post" style="margin-top: -15px; padding-top: 15px; background-color: #d4d4d4; margin-bottom: -15px;" class="row reqlocation">
                      <input type="hidden" class="hidden reqloc-material-id" name="<%=FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_MATERIAL_ID]%>" value="<%=oidMaterial%>">
                      <div class="col-sm-3">
                        <div class="input-group">
                          <span class="input-group-addon" id="basic-addon3">Room Class</span>
                          <select required name="<%=FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_POS_ROOM_CLASS_ID]%>" required="" <%=(oidMaterialReqLocation == 0) ? "" : "disabled"%> class="form-control reqloc-input select-room-class">
                            <option value="">-select-</option>
                            <%
                              Vector listRoomClass = PstRoomClass.list(0, 0, "", PstRoomClass.fieldNames[PstRoomClass.FLD_CLASS_NAME]);
                              for (int i = 0; i < listRoomClass.size(); i++) {
                                RoomClass r = (RoomClass) listRoomClass.get(i);
                            %>
                            <option class="rc-<%=r.getOID()%>" <%=(r.getOID() == roomClassId) ? "selected" : ""%> value="<%=r.getOID()%>"><%=r.getClassName()%></option>
                            <%}%>
                          </select>
                        </div>
                      </div>
                      <div class="col-sm-2">
                        <div class="input-group">
                          <span class="input-group-addon">Duration</span>
                          <input type="number" min="0" required="" name="<%=FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_DURATION]%>" <%=(oidMaterialReqLocation == 0) ? "" : "disabled"%> value="<%=duration%>" class="form-control reqloc-input reqloc-duration">
                          <span class="input-group-addon">m</span>
                        </div>
                      </div>
                      <div class="col-sm-2">
                        <div class="input-group">
                          <span class="input-group-addon">Index</span>
                          <input type="number" min="0" required="" name="<%=FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_ORDER_INDEX]%>" <%=(oidMaterialReqLocation == 0) ? "" : "disabled"%> value="<%=locIndex%>" class="form-control reqloc-input reqloc-index">
                        </div>
                      </div>
                      <div class="col-sm-2">
                        <div class="input-group">
                          <span class="input-group-addon">PIC</span>
                          <select required name="<%=FrmMaterialReqLocation.fieldNames[FrmMaterialReqLocation.FRM_FIELD_IGNORE_PIC]%>" required="" <%=(oidMaterialReqLocation == 0) ? "" : "disabled"%> class="form-control reqloc-input select-pic-type">
                            <option selected value="0">Only handle single client</option>
                            <option value="1">Handle as much as room capacity</option>
                          </select>
                        </div>
                      </div>
                      <div class="col-sm-3"> 
                        <div class="input-group">
                          <span class="input-group-addon">Alt. Room</span>
                          <select id="select-alt-room" multiple name="alt_room" required="" <%=(oidMaterialReqLocation == 0) ? "" : "disabled"%> class="form-control reqloc-input select-alt-room">
                            <%
                              listRoomClass = PstRoomClass.list(0, 0, "", PstRoomClass.fieldNames[PstRoomClass.FLD_CLASS_NAME]);
                              for (int i = 0; i < listRoomClass.size(); i++) {
                                RoomClass r = (RoomClass) listRoomClass.get(i);
                            %>
                            <option class="rc-<%=r.getOID()%>" <%=(r.getOID() == roomClassId) ? "selected" : ""%> value="<%=r.getOID()%>"><%=r.getClassName()%></option>
                            <%}%>
                          </select>
                        </div>
                      </div>
                    </form>
                    <div style="margin-top: 30px;" class="col-xs-12">
                      <table class="table table-bordered table-hover table-person" style="background-color:#f9f9f9;">
                        <thead>
                          <tr> 
                            <th colspan="5">
                              <div class="pull-left add-button add-person">&nbsp;<i style="font-size: 18px; line-height: 22px;" class="fa fa-plus-circle"></i>&nbsp;</div>
                              <div class="pull-left">REQ PERSON</div>
                            </th>
                            <th class="darker-table" colspan="6">REQ OPTION</th> 
                          </tr> 
                          <tr class="second-heading">
                            <td style="text-align: center;width: 2%;">#</td>
                            <td style="width: 7%;">Person</td>
                            <td>Jobdesc</td> 
                            <td style="width: 9%;">Job Weight (%)</td>
                            <td>RO</td>
                            <td class="darker-table" style=" width: 14%; ">Department</td>
                            <td class="darker-table" style=" width: 10%; ">Section</td>
                            <td class="darker-table" style=" width: 10%; ">Level</td>
                            <td class="darker-table" style=" width: 10%; ">Position</td>
                            <td class="darker-table" style=" width: 10%; ">Competency</td>
                            <td class="darker-table" style=" width: 7%;  ">Priority</td>
                          </tr>
                        </thead>
                        <tbody class="tbody-person"> 
                          <tr class="tr-person"> 
                            <th style="text-align: center;" scope="row">
                              <span class="d">1</span>
                              <span class="n"><i class="fa fa-trash"></i></span>
                              <input type="hidden" name="<%=FrmMaterialReqPerson.fieldNames[FrmMaterialReqPerson.FRM_FIELD_MATERIAL_REQ_LOCATION_ID]%>" class="form-control hidden req-location-input-id">
                            </th>
                            <td class="ro"><input type="number" min="0" name="<%=FrmMaterialReqPerson.fieldNames[FrmMaterialReqPerson.FRM_FIELD_NUMBER_OF_PERSON]%>" disabled class="form-control ro-number ro-input"></td>
                            <td class="ro"><input name="<%=FrmMaterialReqPerson.fieldNames[FrmMaterialReqPerson.FRM_FIELD_JOBDESC]%>" disabled class="form-control ro-jobdesc ro-input"></td>
                            <td class="ro"><input type="number" min="0" step="10" max="100" name="<%=FrmMaterialReqPerson.fieldNames[FrmMaterialReqPerson.FRM_FIELD_JOB_WEIGHT]%>" disabled class="form-control ro-jobweight ro-input"></td>
                            <td style="font-size: 19px; text-align:center;"><i style="line-height: 32px;" class="fa fa-plus small-button add"></i></td>
                            <td class="darker-table roo roo-change-position">
                              <input type="hidden" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_MATERIAL_REQ_PERSON_ID]%>" class="hidden req-person-input-id roo-person-id">
                              <div style="width: 25%; width: calc(25px - 0px);" class="pull-left"><i style="font-size: 19px; color: #F44336;" class="fa fa-close small-button delete"></i></div>
                              <div style="width: 75%; width: calc(100% - 25px);" class="pull-left">
                                <input type="hidden" class="hidden form-control roo-person-option-id">
                                <input type="hidden" class="hidden form-control roo-department-id" id="clone-department-id" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_DEPARTMENT_ID]%>">
                                <select disabled="" class="form-control clone-input select-department roo-department-id" data-for="clone-department-id">
                                  <option value="0">-select-</option>
                                  <% Vector listDepartment = PstDepartment.list(0, 0, "", PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);%>
                                  <%for(int i=0; i<listDepartment.size();i++) {%>
                                  <% Department d = (Department) listDepartment.get(i); %>
                                  <option class="d-<%=d.getOID()%>" value="<%=d.getOID()%>"><%=d.getDepartment()%></option>
                                  <%}%>
                                </select>
                              </div>
                            </td>
                            <td class="darker-table roo roo-change-position">
                              <input type="hidden" class="hidden form-control roo-section-id" id="clone-section-id" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_SECTION_ID]%>">
                              <select disabled="" class="form-control clone-input select-section roo-section-id" data-for="clone-section-id">
                                <option value="0">-select-</option>
                                <% Vector listSection = PstSection.list(0, 0, "", PstSection.fieldNames[PstSection.FLD_SECTION]); %>
                                <%for(int i=0; i<listSection.size();i++) {%>
                                <% Section s = (Section) listSection.get(i); %>
                                <option class="s-<%=s.getOID()%>" value="<%=s.getOID()%>"><%=s.getSection()%></option>
                                <%}%>
                              </select>
                            </td>
                            <td class="darker-table roo roo-change-position">
                              <input type="hidden" class="hidden form-control roo-level-id" id="clone-level-id" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_LEVEL_ID]%>">
                              <select disabled=""class="form-control clone-input select-level roo-level-id" data-for="clone-level-id">
                                <option value="0">-select-</option>
                                <% Vector listLevel = PstLevel.list(0, 0, "", PstLevel.fieldNames[PstLevel.FLD_LEVEL]);%>
                                <%for(int i=0; i<listLevel.size();i++) {%>
                                <% com.dimata.harisma.entity.masterdata.Level l = (com.dimata.harisma.entity.masterdata.Level) listLevel.get(i); %>
                                <option class="l-<%=l.getOID()%>" value="<%=l.getOID()%>"><%=l.getLevel()%></option>
                                <%}%>
                              </select>
                            </td>
                            <td class="darker-table roo roo-change-competency">
                              <input type="hidden" class="hidden form-control roo-position-id" id="clone-position-id" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_POSITION_ID]%>">
                              <select disabled="" class="form-control clone-input roo-position-id select-position" data-for="clone-position-id">
                                <option value="0">-select-</option>
                                <% Vector listPosition = PstPosition.list(0, 0, "", PstPosition.fieldNames[PstPosition.FLD_POSITION]);%>
                                <%for(int i=0; i<listPosition.size();i++) {%>
                                <% Position p = (Position) listPosition.get(i); %>
                                <option class="p-<%=p.getOID()%>" value="<%=p.getOID()%>"><%=p.getPosition()%></option>
                                <%}%>
                              </select>
                            </td>
                            <td class="darker-table roo roo-changes">
                              <input type="hidden" class="hidden form-control roo-competency-id" id="clone-competency-id" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_COMPETENCY_ID]%>">
                              <select disabled="" class="form-control clone-input roo-competency-id select-competency" data-for="clone-competency-id">
                                <option value="0">-select-</option>
                                <% Vector listCompetency = PstCompetency.list(0, 0, "", PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_NAME]);%>
                                <%for(int i=0; i<listCompetency.size();i++) {%>
                                <% Competency c = (Competency) listCompetency.get(i); %>
                                <option class="c-<%=c.getOID()%>" value="<%=c.getOID()%>"><%=c.getCompetencyName()%></option>
                                <%}%>
                              </select>
                            </td>
                            <td class="darker-table roo"><input type="number" min="0" name="<%=FrmMaterialReqPersonOption.fieldNames[FrmMaterialReqPersonOption.FRM_FIELD_PRIORITY_INDEX]%>" disabled="" class="form-control roo-index"></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="col-xs-12">
                      <table class="table table-bordered table-hover table-facility" style="background-color:#f9f9f9;">
                        <thead>
                          <tr>
                            <th colspan="6">
                              <div id="add-facility" class="pull-left add-button">&nbsp;<i style="font-size: 18px; line-height: 22px;" class="fa fa-plus-circle"></i>&nbsp;</div>
                              <div class="pull-left">REQ FACILITY</div>
                            </th>
                          </tr>
                          <tr>
                            <td>#</td>
                            <td>Facility</td>
                            <td>Number</td>
                            <td>Note</td>
                            <td>Order Index</td>
                            <td>Duration (minutes)</td>
                          </tr>
                        </thead>
                        <tbody id="tbody-facility">
                          <tr class="tr-facility">
                            <th style="text-align: center;" scope="row">
                              <span class="d">1</span>
                              <span class="n"><i class="fa fa-trash"></i></span>
                              <input type="hidden" name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_MATERIAL_ID]%>" value="<%=oidMaterial%>" class="form-control hidden rf-material-id">
                              <input type="hidden" name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_MATERIAL_REQ_LOCATION_ID]%>" class="form-control hidden req-location-input-id rf-location-id">
                            </th>
                            <td class="rf">
                              <input type="hidden" class="hidden form-control rf-aktiva-id" id="clone-aktiva-id" name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_AKTIVA_ID]%>">
                              <select disabled class="form-control clone-input select-aktiva rf-aktiva-id rf-input" data-for="clone-aktiva-id">
                                <option value="0">-select-</option>
                                <% Vector listAktiva = PstModulAktiva.list(0, 0, "", PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]); %>
                                <%for(int i=0; i<listAktiva.size();i++) {%>
                                <% ModulAktiva a = (ModulAktiva) listAktiva.get(i); %>
                                <option class="s-<%=a.getOID()%>" value="<%=a.getOID()%>"><%=a.getName()%></option>
                                <%}%>
                              </select>
                            </td>
                            <td class="rf"><input type="number" min="0" disabled name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_NUMBER]%>" class="form-control rf-number rf-input"></td>
                            <td class="rf"><input disabled name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_NOTE]%>" class="form-control rf-note rf-input"></td>
                            <td class="rf"><input type="number" min="0" disabled name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_ORDER_INDEX]%>" class="form-control rf-order-index rf-input"></td>
                            <td class="rf"><input type="number" min="0" disabled name="<%=FrmMaterialReqFacility.fieldNames[FrmMaterialReqFacility.FRM_FIELD_DURATION]%>" class="form-control rf-duration rf-input"></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="col-lg-1 pull-right">
                      <button type="button" style="width:100%;" class="btn btn-primary finish-add-product">Finish</button>
                    </div>
                  </div>
                </div>
              </new-product>
              <edit-product id="form-edit"></edit-product>
              <div id="listing" class="panel panel-success">
                <div class="panel-heading">
                  <strong>Product Requirements</strong>
                </div>
                <ul id="prodreq-list-group" class="list-group">
                  <li class="list-group-item">
                    <div class="first-container" clas="row">
                      <div class="col-lg-10">
                        <div class="pull-left">
                          <strong>Room Class :</strong>
                          <span class="rl-room-class">-</span>
                        </div>
                        <div style="margin-left:25px;" class="pull-left">
                          <strong>Duration :</strong>
                          <span class="rl-duration">0</span>
                        </div>
                        <div style="margin-left:25px;" class="pull-left">
                          <strong>Index :</strong>
                          <span class="rl-index">0</span>
                        </div>
                        <div style="margin-left:25px;" class="pull-left">
                          <strong>PIC :</strong>
                          <span class="rl-pic">0</span>
                        </div>
                        <div style="margin-left:25px;" class="pull-left">
                          <strong>Alternative Room :</strong>
                          <span class="rl-alt-room">0</span>
                        </div>
                      </div>
                      <div class="col-lg-2 action">
                        <div class="pull-right">
                          <span class="edit" data-location="#form-edit"><i class="fa fa-pencil"></i> Edit</span>
                          &nbsp;&nbsp;&nbsp;
                          <span class="delete"><i class="fa fa-trash"></i> Delete</span>
                        </div>
                      </div>
                    </div>
                    <div class="second-container" style="margin-top: 30px;" class="row">
                      <div class="col-sm-7 sub1">
                        <table class="table table-bordered table-small" style="background-color:#f9f9f9;">
                          <thead>
                            <tr>
                              <th style="text-align: center; font-weight: normal;" colspan="4">REQ PERSON</th>
                              <th style="text-align: center; font-weight: normal;" class="darker-table" colspan="5">REQ OPTION</th> 
                            </tr>
                            <tr class="second-heading">
                              <td style="text-align: center; widtd: 20px;">#</td>
                              <td>Person</td>
                              <td>Jobdesc</td>
                              <td>Job Weight (%)</td>
                              <td class="darker-table">Department</td>
                              <td class="darker-table">Section</td>
                              <td class="darker-table">Level</td>
                              <td class="darker-table">Position</td>
                              <td class="darker-table">Competency</td>
                              <td class="darker-table">Priority</td>
                            </tr>
                          </thead>
                          <tbody class="tbody-person"> 
                            <tr class="tr-person"> 
                              <td class="ro-no" rowspan="2" style="text-align: center;width: 10px;" scope="row">1</td>
                              <td class="ro-person" rowspan="2" style="width: 30px">-</td>
                              <td class="ro-jobdesc" rowspan="2" style="width: 30px">-</td>
                              <td class="ro-job-weight" rowspan="2" style="width: 80px">-</td>
                            </tr>
                            <tr class="tr-person-option"> 
                              <td class="darker-table rpo-department" style="width: 110px;">-</td>
                              <td class="darker-table rpo-section" style="width: 50px">-</td>
                              <td class="darker-table rpo-level" style="width: 50px">-</td>
                              <td class="darker-table rpo-position" style="width: 50px">-</td>
                              <td class="darker-table rpo-competency" style="width: 50px">-</td>
                              <td class="darker-table rpo-priority" style="width: 50px">-</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                      <div class="col-sm-5 sub2">
                        <table class="table table-bordered table-hover" style="background-color:#f9f9f9;">
                          <thead>
                            <tr>
                              <th style="text-align: center;font-weight:normal;" colspan="6">REQ FACILITY</th>
                            </tr>
                            <tr class="second-heading">
                              <td style="width: 35px;text-align: center;">#</td>
                              <td>Facility</td>
                              <td>Number</td>
                              <td>Note</td>
                              <td>Order Index</td>
                              <td>Duration (minutes)</td>
                            </tr>
                          </thead>
                          <tbody class="tbody-facility">
                            <tr class="tr-facility">
                              <td class="rf-no rf-input" style="width:35px; text-align: center;" scope="row">-</td>
                              <td class="rf-aktiva rf-input" style="width:35px;" scope="row">-</td>
                              <td class="rf-number rf-input" style="width: 30px">-</td>
                              <td class="rf-note rf-input" style="width: 30px">-</td>
                              <td class="rf-order-index rf-input" style="width: 30px">-</td>
                              <td class="rf-duration rf-input" style="width: 30px">-</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                    <div class="clearfix"></div>
                  </li>
                </ul>
              </div>
              <div style="margin-bottom:20px">
                <a href="#product-new" id="new-add-form" class="btn btn-lg btn-success"><i class="fa fa-plus-circle"></i>&nbsp;&nbsp;Add Requirement</a>
              </div>
              <pre-loader style="display:none;" data-load="0"></pre-loader>
            </div>
          </div>
        </div>
      </div>

      <!-- jQuery -->
      <script src="../../vendors/jquery/dist/jquery.min.js"></script>
      <!-- Bootstrap -->
      <script src="../../vendors/bootstrap/dist/js/bootstrap.min.js"></script>
      <!-- FastClick -->
      <script src="../../vendors/fastclick/lib/fastclick.js"></script>
      <!-- NProgress -->
      <script src="../../vendors/nprogress/nprogress.js"></script>
      <!-- Custom Theme Scripts -->
      <script src="../../vendors/build/js/custom.min.js"></script>

      <script src="../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
      <script src="../../styles/dimata-app.js" type="text/javascript"></script>

      <script src="../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
      <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
      <script type="text/javascript" src="../../styles/chart/highcharts.js"></script>
      <script type="text/javascript" src="../../styles/lib.js"></script>
      <script type="text/javascript" src="../../styles/product.js"></script>
      <script type="text/javascript" src="../../styles/toast/src/jquery.toast.js"></script>

      <script>
      $(document).ready(function () {
        var form = $('body').find('.super-form').clone();
        $('body').find('.super-form').remove();

        var fn = function () {
          listing.get(listing.init);
        };
        var formEdit = form.clone().addClass('panel-warning');
        $(formEdit).find('.panel-heading').children('strong').html('Edit Product Requirement');
        var listing = new prodReqListing($('body').find('#prodreq-list-group'), formEdit);
        listing.oid = "<%=oidMaterial%>";
        listing.finishEdit = listing.finishDelete = fn;
        listing.edit = $('body').find('edit-product');
        listing.get(listing.init);

        var newProdContainer = $('body').find('new-product');
        $('body').find('#new-add-form').click(function () {
          if (newProdContainer.children().length < 1) {
            var newForm = new formNewProduct(form.clone());
            newForm.container = newProdContainer;
            newForm.finish = fn;
            newForm.init();
          }
        });

        $('body').show();
      });
      </script>
      <form name="frm_material" method="post" action="">
        <input type="hidden" name="typemenu" value="<%=typemenu%>">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="add_type" value="">			  			  			  			  
        <input type="hidden" name="start" value=0">
        <input type="hidden" name="start2" value="0">
        <input type="hidden" name="start_search" value="0">
        <input type="hidden" name="start_search2" value="0">
        <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
        <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
        <input type="hidden" name="hidden_type" value="">
        <input type="hidden" name="typemenu" value="">
        <input type="hidden" name="approval_command" value="">
      </form>


      <table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FCFDEC" >
        <tr> 
          <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
            <%if (/*menuUsed == MENU_ICON*/false) {%>
            <%@include file="../../styletemplate/footer.jsp" %>

            <%} else {%>
            <%@ include file ="../../main/footer.jsp" %>
            <%}%>
            <!-- #EndEditable --> 
          </td>
        </tr>
      </table>
  </body>

</html>