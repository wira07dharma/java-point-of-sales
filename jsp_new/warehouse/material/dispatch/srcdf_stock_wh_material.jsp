<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.session.warehouse.SessMatDispatch,
         com.dimata.posbo.entity.search.SrcMatDispatch,
         com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Transfer", "Pencarian"},
        {"Dispatch", "Searching"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Urut Berdasar", "Semua", "Tanggal Awal", " Tanggal Akhir ", "Lokasi", "Pilih Semua", "Batal Pilih Semua"},
        {"Number", "From Location", "To Location", "Date", "Status", "Sort By", "All", "From", " to ", "Location", "Select All", "Remove Select All"}
    };

    public static final String textListSortBy[][] = {
        {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status"},
        {"Number", "From Location", "To Location", "Date", "Status"}
    };

    public static final String textListMaterialHeader[][] = {
        {"No", "Nomor", "Tanggal", "Lokasi Asal", "Lokasi Tujuan", "Status", "Keterangan", "Kode Bea Cukai"},
        {"No", "Number", "Date", "Dispatch From", "Dispatch To", "Status", "Keterangan", "Customs Code"}
    };

    public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status) {
        int typeOfBusinessDetail = Integer.valueOf(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS_DETAIL"));
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][0], "1%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "15%");            
            if(typeOfBusinessDetail == 2){
                ctrlist.addHeader("Tipe Item Transfer", "15%");
            }
            ctrlist.addHeader(textListMaterialHeader[language][2], "10%");
			int dutyFree = SessMatDispatch.getStrDutyFree();
			if(dutyFree == 1){
				ctrlist.addHeader(textListMaterialHeader[language][7], "15%");			
			}
            ctrlist.addHeader(textListMaterialHeader[language][3], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "9%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "20%");

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
                Vector rowx = new Vector();
                Vector vt = (Vector) objectClass.get(i);
                MatDispatch df = (MatDispatch) vt.get(0);
                Location loc1 = (Location) vt.get(1);
                Location loc2 = new Location();
                MatDispatch newDf = new MatDispatch();
                try {
                    loc2 = PstLocation.fetchExc(df.getDispatchTo());
                    newDf = PstMatDispatch.fetchExc(df.getOID());
                } catch (Exception e) {
                }
                start += 1;

                rowx.add("<div align=\"center\">"+ start +"</div>");
                rowx.add("<div align=\"center\">"+ df.getDispatchCode() +"</div>");
                if(typeOfBusinessDetail == 2){
                    rowx.add(Material.MATERIAL_TYPE_TITLE[newDf.getDispatchItemType()]);
                }
                rowx.add("<div align=\"center\">"+ Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy") +"</div>");
				if(dutyFree == 1){
					rowx.add("<div align=\"center\">"+df.getNomorBeaCukai()+"</div>");
				}
                rowx.add(loc1.getName());
                rowx.add(loc2.getName());
                rowx.add("<div align=\"center\">"+ i_status.getDocStatusName(docType, df.getDispatchStatus()) +"</div>");
                rowx.add(df.getRemark());

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(df.getOID()));
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data pengiriman ...</div>";
        }
        return result;
    }

    public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            result = textListHeader[language][index];
        } else {
            result = textListHeader[language][index];
        }
        return result;
    }

    public String getJspTitle2(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            result = textListSortBy[language][index];
        } else {
            result = textListSortBy[language][index];
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
<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_DF);
    boolean privManageData = true;%>

<%
    /**
     * if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){ privAdd=false;
     * privUpdate=false; privDelete=false;
}*
     */
%>

<%
    /**
     * get data from 'hidden form'
     */
    int iCommand = FRMQueryString.requestCommand(request);

    /**
     * declaration of some identifier
     */
    String dfCode = i_pstDocType.getDocCode(docType);
    String dfTitle = textListGlobal[SESS_LANGUAGE][0];
    String dfItemTitle = dfTitle + " Item";

    long oidMatDispatch = FRMQueryString.requestLong(request, "hidden_dispatch_id");
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

    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
    SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
    SessMatDispatch sessMatDispatch = new SessMatDispatch();
    FrmSrcMatDispatch frmSrcMatDispatch = new FrmSrcMatDispatch(request, srcMatDispatch);

    String sOidNumber = FRMQueryString.requestString(request, frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_CODE]);
    int oidDate = FRMQueryString.requestInt(request, FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]);
    int oidSortBy = FRMQueryString.requestInt(request, frmSrcMatDispatch.fieldNames[frmSrcMatDispatch.FRM_FIELD_SORT_BY]);
    long oidLocFrom = FRMQueryString.requestLong(request, frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_FROM]);
    long oidLoc = FRMQueryString.requestLong(request, frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_TO]);
//String sVendor = FRMQueryString.requestString(request,frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_VENDORNAME]);
    int oidStatus = FRMQueryString.requestInt(request, FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS]);

    if (oidDate == 0 || sOidNumber != "" || oidSortBy != 0 || oidLocFrom != 0 || oidStatus != 0 || oidLoc != 0 && iCommand != Command.FIRST) {
        frmSrcMatDispatch.requestEntityObject(srcMatDispatch);
    }
    try {
        Vector listreq = (Vector) session.getValue("BARCODE_DISPATCH");
        if (listreq != null && listreq.size() > 0) {
            String strcode = "";
            for (int k = 0; k < listreq.size(); k++) {
                MatDispatch matDispatch = (MatDispatch) listreq.get(k);
                if (strcode.length() > 0) {
                    strcode = strcode + "," + matDispatch.getDispatchCode();
                } else {
                    strcode = matDispatch.getDispatchCode();
                }
            }
            srcMatDispatch.setDispatchCode(strcode);
            srcMatDispatch.setIgnoreDate(true);
        }
    } catch (Exception e) {
    }

//
//    Vector vectSt = new Vector(1,1);
//     String[] strStatus = request.getParameterValues(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS]);
//     if(strStatus!=null && strStatus.length>0) {
//     for(int i=0; i<strStatus.length; i++) {        
//     try	{
//     vectSt.add(strStatus[i]);
//     }
//     catch(Exception exc) {
//     System.out.println("err");
//     }
//     }
//     }
//     srcMatDispatch.setStatus(vectSt);
//     
    String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

    vectSize = sessMatDispatch.getCountMatDispatch(srcMatDispatch, whereClausex);
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMatDispatch.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector records = sessMatDispatch.listMatDispatch(srcMatDispatch, start, recordToGet, whereClausex);
    int testCheck = 0;
%>
<html>
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
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdAdd()
            {
                document.frm_matdispatch.command.value = "<%=Command.ADD%>";
                document.frm_matdispatch.approval_command.value = "<%=Command.SAVE%>";
                document.frm_matdispatch.add_type.value = "<%=ADD_TYPE_SEARCH%>";
                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matdispatch.submit();
            }

            function cmdSearch()
            {
                //alert("xx");
                document.frm_matdispatch.command.value = "<%=Command.LIST%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }


            function cmdEdit(oid)
            {
                document.frm_matdispatch.hidden_dispatch_id.value = oid;
                document.frm_matdispatch.start.value = 0;
                document.frm_matdispatch.approval_command.value = "<%=Command.APPROVE%>";
                document.frm_matdispatch.command.value = "<%=Command.EDIT%>";
                document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListFirst()
            {
                document.frm_matdispatch.command.value = "<%=Command.FIRST%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListPrev()
            {
                document.frm_matdispatch.command.value = "<%=Command.PREV%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListNext()
            {
                document.frm_matdispatch.command.value = "<%=Command.NEXT%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListLast()
            {
                document.frm_matdispatch.command.value = "<%=Command.LAST%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdBack()
            {
                document.frm_matdispatch.command.value = "<%=Command.BACK%>";
                document.frm_matdispatch.action = "srcdf_stock_wh_material.jsp";
                document.frm_matdispatch.submit();
            }

        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet"/>
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
        <!-- #EndEditable --> 
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
        text-align: center;
      }

.listgensell {
    color: #000000;
    background-color: #ffffff !important;
    border: 1px solid #3e85c3;
}

#CheckStatusAllBtn, #UnCheckStatusAllBtn{
cursor: pointer;
}

li {
    padding-right: 15px;
}

select.tanggal {
    height: 30px;
    border: 1px #ccc solid;
}
select.formElemen {
    height: 30px;
    border: 1px #ccc solid;
    width: 28%;
}

.tgl {
    padding: 0px !important;
    margin: 0px !important;
}
.col-sm-8.dated {
    margin-bottom: 5px !important;
    padding-bottom: 0px !important;
    height: 40px;
    padding-left: 0px;
    padding-right: 0px;
    padding-top: 0px;
}
.col-sm-8.da {
    margin-bottom: 5px !important;
    padding-bottom: 0px !important;
    height: 40px;
    padding-left: 17px;
    padding-right: 0px;
    padding-top: 0px;
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

.row {
    margin-bottom: 10px;
}
.col-sm-8 {
    margin-bottom: 10px;
}
label.control-label.col-sm-4 {
    padding-top: 10px;
    text-align: right;
}
select.tanggal {
    width: 30.9%;
}
i.fa.fa-backward {
    padding: 3px;
}

.form-control {
  font-size: 12px
}
li {
    padding-right: 20px;
    display: inline-block;
}
.list-status {
    list-style: none;
    display: block;
    margin: 0 0px 5px 0px;
    padding: 0px;
    position: relative;
    float: left;
}
.col-sm-8.stt {
    padding-right: 0px;
}
label.control-label.col-sm-4.padd {
    padding: 10px 15px 0px 0px;
}
label.control-label.col-sm-4.paddd {
    padding: 10px 15px 0px 0px;
    text-align: left;
}
select.formtest {
    width: 30.2%;
    padding-left: 0px;
}
a.btn.btn-primary {
    height: 15px;
    margin-left: 30px;
    padding: 10px 10px 25px 10px;
    margin-bottom: 15px;
}
    </style>
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <section class="content-header">
      <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
        <small> <%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
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
            <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td>
            </tr><%} else {%>
            <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %> </td></tr>
            <%}%>
            <tr> 
              <td valign="top" align="left"> 
                <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                  <tr> 
                    <td>
                      <form name="frm_matdispatch" method="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="add_type" value="">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="hidden_dispatch_id" value="<%=oidMatDispatch%>">
                        <input type="hidden" name="approval_command">
                        <input type="hidden" id="testCheck" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_TEST_CHECK]%>" value="<%=testCheck%>">
                        <table width="100%" border="0">
                          <td width ="100%"> 
                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                              <div class="row">
                                <div class="col-sm-12">
                                  <!-- left side -->
                                  <div class="col-sm-4">
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="selectNomor"><%=getJspTitle(0, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <input id="selectNomor" class="form-control" type="text" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_CODE]%>"  value="<%= srcMatDispatch.getDispatchCode()%>">
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4" for="lokasi-asal"><%=getJspTitle(1, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <%
                                          Vector obj_locationid = new Vector(1, 1);
                                          Vector val_locationid = new Vector(1, 1);
                                          Vector key_locationid = new Vector(1, 1);
                                          //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                          //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                          //add opie-eyek
                                          //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                          whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                  + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                          whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                          Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                          val_locationid.add("0");
                                          key_locationid.add(getJspTitle(6, SESS_LANGUAGE, dfCode, true) + " " + getJspTitle(9, SESS_LANGUAGE, dfCode, true));
                                          for (int d = 0; d < vt_loc.size(); d++) {
                                            Location loc = (Location) vt_loc.get(d);
                                            val_locationid.add("" + loc.getOID() + "");
                                            key_locationid.add(loc.getName());
                                          }
                                        %>
                                        <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_FROM], null, "" + oidLocFrom, val_locationid, key_locationid, "", "formLocation")%>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=getJspTitle(2, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <%
                                          Vector obj_locationid1 = new Vector(1, 1);
                                          Vector val_locationid1 = new Vector(1, 1);
                                          Vector key_locationid1 = new Vector(1, 1);
                                          String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                          String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                          //Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
                                          val_locationid1.add("0");
                                          key_locationid1.add(getJspTitle(6, SESS_LANGUAGE, dfCode, true) + " " + getJspTitle(9, SESS_LANGUAGE, dfCode, true));
                                          for (int d = 0; d < vt_loc.size(); d++) {
                                            Location loc1 = (Location) vt_loc.get(d);
                                            val_locationid1.add("" + loc1.getOID() + "");
                                            key_locationid1.add(loc1.getName());
                                          }
                                        %>
                                        <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_TO], null, "" + oidLoc, val_locationid1, key_locationid1, "", "formLocation")%>
                                      </div>
                                    </div>
                                  </div>
                                  <!-- right side -->             
                                  <div class="col-sm-4">
                                    <!--Urut Berdasarka-->
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="selectUrut"><%=getJspTitle(5, SESS_LANGUAGE, dfCode, false)%></label>
                                      <div class="col-sm-8">
                                        <%
                                          Vector key_sort = new Vector(1, 1);
                                          Vector val_sort = new Vector(1, 1);
                                          for (int i = 0; i < textListSortBy[0].length; i++) {
                                            key_sort.add("" + i);
                                            val_sort.add("" + getJspTitle2(i, SESS_LANGUAGE, dfCode, true));
                                          }
                                          String select_sort = "" + srcMatDispatch.getSortBy();
                                          out.println(ControlCombo.draw(frmSrcMatDispatch.fieldNames[frmSrcMatDispatch.FRM_FIELD_SORT_BY], null, select_sort, key_sort, val_sort, "", "formSortBy"));
                                        %>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4"><%=getJspTitle(4, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8 stt">
                                        <ul class="list-status">
                                            <%
                                              Vector vectResult = i_status.getDocStatusFor(docType);
                                              for (int i = 0; i < vectResult.size(); i++) {
                                                if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL) || (i == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)) {
                                                  Vector vetTemp = (Vector) vectResult.get(i);
                                                  int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                                  String strPrStatus = String.valueOf(vetTemp.get(1));
                                            %>
                                            <li>
                                              <input type="checkbox" class="formElemen  checks" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS]%>" value="<%=(indexPrStatus)%>"<%if (srcMatDispatch.getStatus() == indexPrStatus) {%>checked<%}%>>
                                              <%=strPrStatus%>
                                            </li>
                                            <%
                                                }
                                              }
                                            %>
                                        </ul>
                                        <a id="CheckStatusAllBtn">
                                          <%= textListHeader[SESS_LANGUAGE][10]%>
                                        </a>
                                        |
                                        <a id="UnCheckStatusAllBtn">
                                          <%= textListHeader[SESS_LANGUAGE][11]%>
                                        </a>
                                      </div>
                                    </div> 
                                  </div>
                                  <!--Nomor BC-->
                                  <div class="col-sm-4">
                                    <!--Tanggal-->
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 paddd" for="CustomeDate"><%=getJspTitle(7, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8 dated">
                                        <input type="radio" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="0" <%if (!srcMatDispatch.getIgnoreDate()) {%>checked<%}%>>
                                        <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_FROM], (srcMatDispatch.getDispatchDateFrom() == null) ? new Date() : srcMatDispatch.getDispatchDateFrom(), 1, -5, "formElemen", "")%>
                                      </div>  
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 paddd" style="padding-top: 10px"><%=getJspTitle(8, SESS_LANGUAGE, dfCode, true)%></label>
                                      <div class="col-sm-8 da">
                                        <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_TO], (srcMatDispatch.getDispatchDateTo() == null) ? new Date() : srcMatDispatch.getDispatchDateTo(), 1, -5, "formtest", "")%>
                                      </div>  
                                    </div>
                                    <div class="form-group tgl">
                                      <label class="col-sm-4"></label>
                                      <div class="col-sm-8 dated">
                                        <input id="allDate" type="radio" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="1" <%if (srcMatDispatch.getIgnoreDate()) {%>checked<%}%>> <label for="allDate" >&nbsp;<%=getJspTitle(6, SESS_LANGUAGE, dfCode, true)%> <%=getJspTitle(3, SESS_LANGUAGE, dfCode, true)%></label>
                                      </div>
                                    </div>
                                  </div> 
                                </div>
                              </div>
                            </table>
                          </td> 
                        </table>
                      </form>
                      </td> 
                  </tr>
                  <tr align="left" valign="top"> 
                    <td height="18" valign="top" colspan="3">
                      <table width="52%" border="0" cellspacing="0" cellpadding="0">
                        <tr>  
                          <td class="command" nowrap width="26%"><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0], ctrLine.CMD_SEARCH, true)%></a></td>
                          <% if (privAdd) {%>
                          <td class="command" nowrap width="99%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ADD, true)%></a></td>
                          <%}%>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>	 
                  <tr align="left" valign="top"> 
                    <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, i_status)%></td>
                  </tr>	
                  <tr align="left" valign="top">
                    <td height="8" align="left" colspan="3" class="command bawah">
                      <span class="command">
                        <%
                          ctrLine.setLocationImg(approot + "/images");
                          ctrLine.initDefault();
                          out.println(ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet));
                        %>
                      </span>
                    </td>
                  </tr>
                  <tr> 
                    <td height="21" valign="top" width="100%" align="left">&nbsp;</td>
                  </tr>  
                  <tr align="left" valign="top"> 
                    <td height="18" valign="top" colspan="3">
                      <table width="52%" border="0" cellspacing="0" cellpadding="0">
                        <tr>  
                          <% if (privAdd) {%>
                          <td nowrap width="5%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ADD, true)%></a></td>
                          <%}%>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <%}%>
                </table>
              </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                 </td>
            </tr>
        </table>
                 					
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>					
    <script>
        $(document).ready(function () {

            $(".checks").click(function () {
                var checkboxVal = "";
                $(".checks").each(function (i) {
                    if ($(this).is(":checked")) {
                        if (checkboxVal.length == 0) {
                            checkboxVal += "" + $(this).val();
                        } else {
                            checkboxVal += "," + $(this).val();
                        }
                    }
                });

                if (checkboxVal.length > 0) {
                    $("#testCheck").val("1");
                } else {
                    $("#testCheck").val("0");
                }
            });
            
        $('#CheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', true);
				});
				$('#UnCheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', false);
				});
            
        });
    </script>
    </div>
    </body>
    
</html>
