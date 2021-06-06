<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%
    /* 
     * Page Name  		:  src_receive_material.jsp
     * Created on 		:  Selasa, 2 Agustus 2007 10:40 AM 
     * 
     * @author  		:  gwawan
     * @version  		:  -
     */

    /**
     * *****************************************************************
     * Page Description : page ini merupakan gabungan dari page : -
     * srcreceive_wh_supp_material.jsp - srcreceive_wh_supp_po_material.jsp
     * Imput Parameters : [input parameter ...] Output : [output ...]
     * *****************************************************************
     */
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.search.SrcMatReceive,
         com.dimata.posbo.form.search.FrmSrcMatReceive,
         com.dimata.posbo.session.warehouse.SessMatReceive,
         com.dimata.posbo.entity.warehouse.PstMatReceive" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    int appObjCodeWithoutPO = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_WITHOUT_PO);
    boolean privAddWithoutPO = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeWithoutPO, AppObjInfo.COMMAND_ADD));
%>
<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Pembelian", "Pencarian", "Daftar", "Edit", "Dengan PO", "Tanpa PO", "Tidak ada data penerimaan barang", "Consignment Sold", "Dari Buyback"},
        {"Receive", "From Purchase", "Search", "List", "Edit", "With PO", "Without PO", "Receiving goods not found", "Consigment Sold", "From Buyback"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Kode", "Supplier", "Invoice Supplier", "Lokasi Terima", "Tanggal", "Dari Tanggal", " Sampai Tanggal ", "Status", "Urut Berdasar", "Semua", "Pilih Semua", "Batal Pilih Semua"},
        {"Code", "Supplier", "Supplier Invoice", "Receive Location", "Date", "From", " to ", "Status", "Sort By", "All", "Select All", "Cancel Select All"} 
    };

    public static final String textListOrderBy[][] = {
        {"Kode", "Supplier", "Tanggal", "Status"},
        {"Code", "Supplier", "Date", "Status"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"No", "Kode", "Tanggal", "Mata Uang", "Nomor PO", "Supplier", "Status", "Keterangan", "Term"},
        {"No", "Code", "Date", "Currency", "PO Number", "Supplier", "Status", "Description", "Term"}
    };

    public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status, int typeOfBusinessDetail) {
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
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Tipe Item Penerimaan", "5%");
            }
            ctrlist.addHeader(textListMaterialHeader[language][2], "8%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "14%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "18%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "8%");
            ctrlist.addHeader(textListMaterialHeader[language][8], "8%");
            ctrlist.addHeader(textListMaterialHeader[language][7], "25%");

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
                MatReceive rec = (MatReceive) vt.get(0);
                ContactList contact = (ContactList) vt.get(1);
                CurrencyType currencyType = (CurrencyType) vt.get(2);
                PurchaseOrder purchaseOrder = (PurchaseOrder) vt.get(3);

                String cntName = contact.getCompName();
                if (cntName.length() == 0) {
                    cntName = String.valueOf(contact.getPersonName() + " " + contact.getPersonLastname());
                }

                String str_dt_RecDate = "";
                try {
                    Date dt_RecDate = rec.getReceiveDate();
                    if (dt_RecDate == null) {
                        dt_RecDate = new Date();
                    }
                    str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
                } catch (Exception e) {
                    str_dt_RecDate = "";
                }

                Vector rowx = new Vector();
                rowx.add("" + (start + i + 1));
                rowx.add(rec.getRecCode());
                if (typeOfBusinessDetail == 2) {
                    rowx.add(Material.MATERIAL_TYPE_TITLE[rec.getReceiveItemType()]);
                }
                rowx.add(str_dt_RecDate);
                rowx.add("<div align=\"center\">" + currencyType.getCode() + "</div>");
                rowx.add(purchaseOrder.getPoCode());
                rowx.add(cntName);
                rowx.add(i_status.getDocStatusName(docType, rec.getReceiveStatus()));
                if (rec.getTermOfPayment() == 0) {
                    rowx.add("Cash");
                } else {
                    rowx.add("Credit");
                }

                rowx.add(rec.getRemark());

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(rec.getOID()) + "', '" + purchaseOrder.getOID());
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][7] + "</div>";
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

    public String getJspTitle2(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            if (language == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                result = textListOrderBy[language][index] + " " + prefiks;
            } else {
                result = prefiks + " " + textListOrderBy[language][index];
            }
        } else {
            result = textListOrderBy[language][index];
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
<%
    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_LMRR);
    boolean privManageData = true;
%>


<%
    /**
     * get data from 'hidden form'
     */
    int iCommand = FRMQueryString.requestCommand(request);
    long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");

    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";

    int start = FRMQueryString.requestInt(request, "start");

    int recordToGet = 20;
    int vectSize = 0;

    /**
     * declaration of some identifier
     */
    String recCode = i_pstDocType.getDocCode(docType);
    String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
    String recItemTitle = recTitle + " Item";

    /**
     * ControlLine
     */
    ControlLine ctrLine = new ControlLine();

    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    SrcMatReceive srcMatReceive = new SrcMatReceive();
    SessMatReceive sessMatReceive = new SessMatReceive();
    FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);

    String sOidNumber = FRMQueryString.requestString(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER]);
    int oidDate = FRMQueryString.requestInt(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]);
    int oidSortBy = FRMQueryString.requestInt(request, frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY]);
    String sOidVendor = FRMQueryString.requestString(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME]);
    String sInvoiceSupp = "";
    if (iCommand != 23) {
        sInvoiceSupp = FRMQueryString.requestString(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER]);
    }
    long oidLoc = FRMQueryString.requestLong(request, frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID]);
    int PurchaseID = FRMQueryString.requestInt(request, "PurchaseID");

    if (oidDate == 1 || sOidNumber != "" || oidSortBy != 0 || sOidVendor != "" || sInvoiceSupp != "" || oidLoc != 0 && iCommand != Command.FIRST) {
        frmSrcMatReceive.requestEntityObject(srcMatReceive);
    }

    Vector vectSt = new Vector(1, 1);
    String[] strStatus = request.getParameterValues(FrmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]);
    if (strStatus != null && strStatus.length > 0) {
        for (int i = 0; i < strStatus.length; i++) {
            try {
                vectSt.add(strStatus[i]);
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
    }

    srcMatReceive.setReceiveSource(-1);
    srcMatReceive.setReceivestatus(vectSt);

    if (PurchaseID == -1) {
        srcMatReceive.setPurchaseOrderId(PurchaseID);
    }
    if (PurchaseID == -2) {
        srcMatReceive.setPurchaseOrderId(PurchaseID);
    }

    String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

    vectSize = sessMatReceive.getCountSearchSupplier(srcMatReceive, whereClausex);

    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMatReceive.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector records = sessMatReceive.searchMatReceiveSupplier(srcMatReceive, start, recordToGet, whereClausex);

    //MENGECEK SISTEM PROPERTI, APAKAH TEMPLATE UNTUK MELAKUKAN PENERIMAAN, BERBASIS BOOTSTRAP ATAU TIDAK
    int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
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
        <script language="JavaScript">
            function cmdSearch() {
                document.frm_recmaterial.command.value = "<%=Command.LIST%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdAddPO() {
                document.frm_recmaterial.command.value = "<%=Command.ADD%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.SAVE%>";
                document.frm_recmaterial.add_type.value = "<%=ADD_TYPE_SEARCH%>";

            <%if (prochainMenuBootstrap == 0) {%>
                document.frm_recmaterial.action = "receive_wh_supp_po_material_edit.jsp";
                document.frm_recmaterial.submit();
            <%} else {%>
                document.frm_recmaterial.action = "receive_wh_supp_material_edit.jsp";
                document.frm_recmaterial.submit();
            <%}%>
            }

            function cmdAdd() {
                document.frm_recmaterial.command.value = "<%=Command.ADD%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.SAVE%>";
                document.frm_recmaterial.add_type.value = "<%=ADD_TYPE_SEARCH%>";
            <%if (prochainMenuBootstrap == 0) {%>
                document.frm_recmaterial.action = "receive_wh_supp_material_edit_old.jsp";
            <%} else {%>
                document.frm_recmaterial.action = "receive_wh_supp_material_edit.jsp";
            <%}%>
                if (compareDateForAdd() == true)
                    document.frm_recmaterial.submit();
            }
            
            function cmdAddConsigment() {
                document.frm_recmaterial.action = "consignment_sold.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdEdit(idRcv, idPo) {
                document.frm_recmaterial.hidden_receive_id.value = idRcv;
                document.frm_recmaterial.start.value = 0;
                document.frm_recmaterial.approval_command.value = "<%=Command.APPROVE%>";
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                
                var prochainMenuBootstrap = "<%=prochainMenuBootstrap%>";
                if (parseInt(idPo) != 0) {
                    if (prochainMenuBootstrap == 0) {
                        document.frm_recmaterial.action = "receive_wh_supp_po_material_edit.jsp";
                    } else {
                        document.frm_recmaterial.action = "receive_wh_supp_material_edit.jsp";
                    }
                } else {
                    if (prochainMenuBootstrap == 0) {
                        document.frm_recmaterial.action = "receive_wh_supp_material_edit_old.jsp";
                    } else {
                        document.frm_recmaterial.action = "receive_wh_supp_material_edit.jsp";
                    }
                }
                document.frm_recmaterial.submit();
            }

            function cmdListFirst() {
                document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListPrev() {
                document.frm_recmaterial.command.value = "<%=Command.PREV%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListNext() {
                document.frm_recmaterial.command.value = "<%=Command.NEXT%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListLast() {
                document.frm_recmaterial.command.value = "<%=Command.LAST%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdBack() {
                document.frm_recmaterial.command.value = "<%=Command.BACK%>";
                document.frm_recmaterial.action = "src_receive_material.jsp";
                document.frm_recmaterial.submit();
            }


            function fnTrapKD() {
                if (event.keyCode == 13) {
                    document.all.aSearch.focus();
                    cmdSearch();
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
        
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
    padding: 10px 10px 0px 0px;
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
label.control-label.col-sm-4.padddd {
    padding: 10px 0px 0px 0px;
}
li {
    padding-right: 0px !important;
    display: inline-block;
}
a.btn-primary.btn-lg {
    border-radius: 0px;
    border: none;
    background-color: #3e85c3;
}
    </style>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <section class="content-header">
        <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
          <small><%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
        <ol class="ol">
          <li>
            <a>
              <li class="active">Penerimaan</li>
            </a>
          </li>
        </ol>
      </section>
      <p class="border"></p>
      <div class="container-pos">      
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <script language="JavaScript">
                        window.focus();
                    </script>
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
            <tr>
                <td height="10" ID="MAINMENU">
                    <script language="JavaScript">
                        window.focus();
                    </script>
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_recmaterial" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="add_type" value="">			
                                    <input type="hidden" name="approval_command">			    			  			  			  			  			  
                                    <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                                    <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
                                    <input type="hidden" name="PurchaseID" value="<%=PurchaseID%>">
                                    <table width="100%" border="0">
                                      <div class="row">
                                        <div class="col-sm-12">
                                          <!-- left side -->
                                          <div class="col-sm-4" style="padding-right: 0px;">
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 padddd" for="selectNomor"><%=getJspTitle(0, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8" >
                                                <input id="selectNomor" class="form-control" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER]%>"  value="<%= srcMatReceive.getReceivenumber()%>" onKeyDown="javascript:fnTrapKD()">
                                              </div>
                                            </div>
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 padddd" for="selectSupplier"><%=getJspTitle(1, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8" >
                                                <input id="selectSupplier" class="form-control" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME]%>"  value="<%= srcMatReceive.getVendorname()%>" onKeyDown="javascript:fnTrapKD()">
                                              </div>
                                            </div>
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 padddd" for="selectInvoice"><%=getJspTitle(2, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8" >
                                                <input id="selectInvoice" class="form-control" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>"  value="<%= srcMatReceive.getInvoiceSupplier()%>" onKeyDown="javascript:fnTrapKD()">
                                              </div>
                                            </div>
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 padddd" for="lokasi-asal"><%=getJspTitle(3, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8" >
                                                <%
                                                  Vector obj_locationid = new Vector(1, 1);
                                                  Vector val_locationid = new Vector(1, 1);
                                                  Vector key_locationid = new Vector(1, 1);
                                                  /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                     whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                     Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);*/
                                                  //add opie-eyek
                                                  //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                  String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                          + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                                  whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                  Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                                  val_locationid.add(String.valueOf(0));
                                                  key_locationid.add(getJspTitle(9, SESS_LANGUAGE, recCode, false) + " " + getJspTitle(3, SESS_LANGUAGE, recCode, false));
                                                  for (int d = 0; d < vt_loc.size(); d++) {
                                                    Location loc = (Location) vt_loc.get(d);
                                                    val_locationid.add("" + loc.getOID() + "");
                                                    key_locationid.add(loc.getName());
                                                  }
                                                %>                 
                                                <%=ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID], null, "" + oidLoc, val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "formLocation")%>
                                              </div>
                                            </div>
                                          </div>
                                          <!-- center side -->             
                                          <div class="col-sm-4">
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 padd" for="selectUrut"><%=getJspTitle(8, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8">
                                                <%
                                                  Vector key_sort = new Vector(1, 1);
                                                  Vector val_sort = new Vector(1, 1);
                                                  int maxItem = textListOrderBy[0].length;
                                                  for (int i = 0; i < maxItem; i++) {
                                                    key_sort.add("" + i);
                                                    val_sort.add("" + getJspTitle2(i, SESS_LANGUAGE, recCode, false));
                                                  }
                                                  String select_sort = "" + srcMatReceive.getReceivesortby();
                                                  out.println("&nbsp;" + ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort, key_sort, val_sort, " onKeyDown=\"javascript:fnTrapKD()\"", "formSortBy"));
                                                %>
                                              </div>
                                            </div>
                                            <div class="form-group">
                                              <label class="control-label col-sm-4"><%=getJspTitle(7, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8 stt">
                                                <div class="col-md-6" style="padding: 0px">
                                                <ul class="list-status">
                                                  <%
                                                    int indexPrStatus = 0;
                                                    String strPrStatus = "Draft";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%>
                                                  </li>
                                                  <%
                                                    indexPrStatus = 10;
                                                    strPrStatus = "Approved";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%>
                                                  </li>
                                                  <%
                                                    indexPrStatus = 5;
                                                    strPrStatus = "Closed";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%> 
                                                  </li>

                                                </ul>
                                                </div>
                                                  <div class="col-md-6" style="padding: 0px 6px 0px 0px;">
                                                <ul class="list-status">
                                                  <%
                                                    indexPrStatus = 1;
                                                    strPrStatus = "To Be Confirm";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%>
                                                  </li>
                                                  <%
                                                    indexPrStatus = 2;
                                                    strPrStatus = "Final";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%>
                                                  </li>
                                                  <%
                                                    indexPrStatus = 7;
                                                    strPrStatus = "Posted";
                                                  %>
                                                  <li>
                                                    <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReceive.getReceivestatus(), indexPrStatus)) {%>checked<%}%>><%=strPrStatus%> 
                                                  </li>
                                                </ul>
                                                  </div>
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
                                              <label class="control-label col-sm-4 paddd" for="CustomeDate"><%=getJspTitle(5, SESS_LANGUAGE, recCode, false)%> </label>
                                              <div class="col-sm-8 dated">
                                                <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]%>" <%if (srcMatReceive.getReceivedatestatus() == 1) {%>checked<%}%> value="1" onKeyDown="javascript:fnTrapKD()">
                                                <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE], (srcMatReceive.getReceivefromdate() == null) ? new Date() : srcMatReceive.getReceivefromdate(), "formElemen", 1, -5)%>
                                              </div>  
                                            </div>
                                            <div class="form-group">
                                              <label class="control-label col-sm-4 paddd" style="padding-top: 10px"><%=getJspTitle(6, SESS_LANGUAGE, recCode, false)%></label>
                                              <div class="col-sm-8 da">
                                                <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE], (srcMatReceive.getReceivetodate() == null) ? new Date() : srcMatReceive.getReceivetodate(), "formElemen", 1, -5)%>
                                              </div>  
                                            </div>
                                            <div class="form-group tgl">
                                              <label class="col-sm-4"></label>
                                              <div class="col-sm-8 dated">
                                                <input id="allDate" type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]%>" <%if (srcMatReceive.getReceivedatestatus() == 0) {%>checked<%}%> value="0" onKeyDown="javascript:fnTrapKD()"> <label for="allDate" >&nbsp;<%=getJspTitle(9, SESS_LANGUAGE, recCode, false)%>&nbsp;<%=getJspTitle(4, SESS_LANGUAGE, recCode, false)%></label>
                                              </div>
                                            </div>
                                          </div> 
                                        </div>
                                      </div>
                                    </table>
                                </form>
                                <!-- #EndEditable -->
                            </td> 
                        </tr>
                        <tr align="left" valign="top">
                            <td height="18" valign="top" colspan="3">
                                <a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0], ctrLine.CMD_SEARCH, true)%></i></a>
                                <% if (privAdd) {%>
                                <a class="btn-primary btn-lg" href="javascript:cmdAddPO()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_ADD, true)%></i></a>
                                <%}%>
                                <% if (privAddWithoutPO) {%>
                                <a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][6], ctrLine.CMD_ADD, true)%></i></a>
                                <%}%>
                                <a class="btn-primary btn-lg" href="javascript:cmdAddConsigment()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][8], ctrLine.CMD_ADD, true)%></i></a>
                            </td>
                        </tr> 
                        <tr align="left" valign="top">
                            <td height="18" valign="top" colspan="3"> 
                                <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td nowrap width="1%">&nbsp;</td>
                                        <td class="command" nowrap width="26%">&nbsp;</td>
                                        <% if (privAdd) {%>
                                        <td nowrap width="4%">&nbsp;</td>
                                        <td class="command" nowrap width="31%">&nbsp;</td>
                                        <td nowrap width="4%">&nbsp;</td>
                                        <td class="command" nowrap width="31%">&nbsp;</td>
                                        <%}%>
                                    </tr>
                                </table>
                            </td>
                        </tr> 
                        <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>	 
                        <tr align="left" valign="top"> 
                            <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, i_status, typeOfBusinessDetail)%></td>
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
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr align="left" valign="top">
                            <td height="18" valign="top" colspan="3"> 
                                <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <% if (privAdd) {%>
                                        <td class="command" nowrap width="31%"><a class="btn-primary btn-lg" href="javascript:cmdAddPO()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_ADD, true)%></i></a></td>
                                        <%}%>
                                        <% if (privAddWithoutPO) {%>
                                        <td class="command" nowrap width="31%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][6], ctrLine.CMD_ADD, true)%></i></a></td>
                                        <%}%>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
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
      </div>
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
    </body>
</html>
