<%@ page language = "java" %>
<%@ page import="com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.entity.admin.PstAppUser,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.posbo.entity.search.SrcMaterial,
         com.dimata.posbo.form.search.FrmSrcMaterial,
         com.dimata.posbo.session.masterdata.SessMaterial,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.gui.jsp.ControlList,
         com.dimata.gui.jsp.ControlCheckBox,
         com.dimata.common.entity.payment.*,
         com.dimata.common.entity.payment.PstDiscountType,
         java.util.Vector,com.dimata.common.entity.location.*"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);%>
<%@ include file = "../../main/checkuser.jsp" %>


<%!//
    public static final String textListMaterialHeader[][] = {
        {"No", "SKU", "Barcode", "Nama Barang", "Jenis", "Tipe", "Hpp", "Hrg Jual", "Kategori", "Merk", "Kode Grup"},
        {"No", "Code", "Barcode", "Name", "Jenis", "Type", "Average", "Price", "Category", "Merk", "Code Range"}
    };

    public String drawCodeRangeList(int language, Vector objectClass) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("20%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][10], "3%");
            //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdsearchcode('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();
            for (int i = 0; i < objectClass.size(); i++) {
                CodeRange codeRange = (CodeRange) objectClass.get(i);
                rowx = new Vector();
                rowx.add("<a href=\"javascript:cmdsearchcode('" + codeRange.getOID() + "')\">" + codeRange.getFromRangeCode() + "</a>");
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        }
        return result;
    }

    public String drawCheck(Vector checked) {
        ControlCheckBox chkBx = new ControlCheckBox();
        chkBx.setWidth(4);
        chkBx.setCellSpace("0");
        chkBx.setCellStyle("");
        chkBx.setTableAlign("center");

        Vector checkName = new Vector(1, 1);
        Vector checkValue = new Vector(1, 1);
        Vector checkCaption = new Vector(1, 1);

        Vector sttRsv = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE, null);
        Vector vname = new Vector(1, 1);
        for (int i = 0; i < sttRsv.size(); i++) {
            Location temp = (Location) sttRsv.get(i);
            //int val = Integer.parseInt((String)temp.get(1));
            checkValue.add("" + temp.getOID());
            checkCaption.add(temp.getName());
            vname.add("" + temp.getOID() + "_" + i);
        }

        checkName = vname; //checkValue;
        return chkBx.draw(checkName, checkValue, checkCaption, checked);
    }

%>

<%!//
    public static final String textListTitleHeader[][] = {
        {"Pencarian Barang", "SKU", "Nama", "Supplier", "Kategori", "Tipe", "Urut Berdasar", "Cari Barang",//7
            "Tambah Barang Baru", "Tipe Barang", "Merk", "Tampilkan Gambar", "Refresh Catalog", "Group", "Semua Group", "Tipe Harga",
            "Show Use Serial Number", "Lokasi Stok", "Status Barang", "Lokasi Jual", "Import Barang Baru"
        },//15

        {"Goods Searching", "Code", "Name", "Supplier", "Group", "Tipe", "Sort By", "Goods Search",
            "Add New Goods", "Goods Type", "Category", "Show Picture", "Refresh Catalog", "Group", "All Group", "Price Type",
            "Show Use Serial Number", "Location Stock", "Status Item", "Sell Location", "Import Barang Baru"
        }
    };

    public static final String typeItemNames[][] = {
        {"Semua Tipe Barang", "Hadiah"},
        {"All", "Gift"}
    };
%>

<%//
    if (userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR) {
        privAdd = false;
        privUpdate = false;
        privDelete = false;
    }
%>

<!-- Jsp Block -->
<%//
    int iCommand = FRMQueryString.requestCommand(request);
    int typemenu = FRMQueryString.requestInt(request, "typemenu");
    int expandSearch = FRMQueryString.requestInt(request, "expand_search");

    //updated for litama by dewok 2018-02-07
    String kodeAwal = FRMQueryString.requestString(request, "FRM_KODE_START");
    String kodeAkhir = FRMQueryString.requestString(request, "FRM_KODE_END");
    int tipeItem = FRMQueryString.requestInt(request, "tipe_item");

    SrcMaterial srcMaterial = new SrcMaterial();
    FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();
    try {
        srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        srcMaterial.setShowHpp(1);
    } catch (Exception e) {
        srcMaterial = new SrcMaterial();
    }

    // Vector vectSubCategory = PstSubCategory.listAll();
    if (srcMaterial == null) {
        srcMaterial = new SrcMaterial();
    }
    srcMaterial.setShowHpp(1);

    try {
        session.removeValue(SessMaterial.SESS_SRC_MATERIAL);
    } catch (Exception e) {
    }

    Vector recordsCodeRange = PstCodeRange.list(0, 0, "", PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);
    merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");

    String useSubCategory = PstSystemProperty.getValueByName("USE_SUB_CATEGORY");
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function expandSearch(expand) {
//                document.frmsrcmaterial.command.value = "<%=Command.NONE%>";
//                document.frmsrcmaterial.expand_search.value = expand;
//                document.frmsrcmaterial.submit();

				var x = document.getElementById("expand");
				var s = document.getElementById("btnexpand");
				if (x.style.display === "none") {
				  x.style.display = "block";
				  s.innerHTML = "<i class='fa fa-minus-circle'></i> Kurangi parameter pencarian";
				} else {
				  x.style.display = "none";
				  s.innerHTML = "<i class='fa fa-plus-circle'></i> Tambah parameter pencarian";
				}

            }

            function cmdAdd() {
                document.frmsrcmaterial.hidden_material_id.value = "0";
                document.frmsrcmaterial.command.value = "<%=Command.ADD%>";
                document.frmsrcmaterial.action = "material_main.jsp";
                document.frmsrcmaterial.submit();
            }

            function cmdSearch() {
                document.frmsrcmaterial.command.value = "<%=Command.LIST%>";
                document.frmsrcmaterial.typemenu.value = "<%=typemenu%>";
                document.frmsrcmaterial.action = "material_list.jsp";
                document.frmsrcmaterial.submit();
            }

            function cmdsearchcode(oidcode) {
                document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value = oidcode;
                document.frmsrcmaterial.command.value = "<%=Command.LIST%>";
                document.frmsrcmaterial.action = "material_list.jsp?hidden_range_code_id=" + oidcode + "";
                document.frmsrcmaterial.submit();
            }

            function cmdSelectSubCategory() {
                window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory=" + document.frmsrcmaterial.txt_subcategory.value +
                        "&caller=1" +
                        "&oidCategory=" + document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>.value, "material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function cmdImport() {
                document.frmsrcmaterial.action = "import_excel_material.jsp";
                document.frmsrcmaterial.submit();
            }

            function fnTrapKD(evt) {
                if (evt.keyCode == 13) {
                    document.all.aSearch.focus();
                    cmdSearch();
                }
            }

            function keyDownCheck(e) {
                var trap = document.frmsrcmaterial.trap.value;
                if (e.keyCode == 13 && trap == 0) {
                    document.frmsrcmaterial.trap.value = "1";
                }
                if (e.keyCode == 13 && trap == "0" && document.frm_purchaseorder.<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME]%>.value == "") {
                    document.frmsrcmaterial.trap.value = "0";
                    cmdSearch();
                }
                if (e.keyCode == 13 && trap == "1") {
                    document.frmsrcmaterial.trap.value = "0";
                    cmdSearch();
                }
                /*if (e.keyCode == 27) {
                 document.frmsrcmaterial.txt_materialname.value="";
                 }*/

            }

            function cmdPrintAllPriceTag() {
                var strvalue = "<%=approot%>" + "/masterdata/barcode.jsp?command=<%=Command.ADD%>";
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
                    for (i = 0; i < a.length; i++) {
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
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
                for (i = 0; i < (a.length - 2); i += 3) {
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
                }
            }
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" --> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
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

        </SCRIPT>
        <!-- #EndEditable --> 
        <%--autocomplate add by fitra--%>
        <script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
        <script src="../../styles/jquery.autocomplete.js"></script>
        <link rel="stylesheet" type="text/css" href="../../styles/style.css" />
        <style>
            #FRM_FIELD_MERK_ID {width: 90%}
        </style>
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
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
                                &nbsp;Masterdata &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable -->
                            </td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmsrcmaterial" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="typemenu" value="<%=typemenu%>">
                                    <input type="hidden" name="hidden_material_id" value="">
                                    <input type="hidden" name="trap" value="">
                                    <input type="hidden" name="expand_search" value="<%=expandSearch %>">
                                    <!--input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>"  value="-1"-->
                                    <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>"  value="-1" >

                                    <table>
                                        <%if (typeOfBusinessDetail == 2) {%>
                                        <tr>
                                            <td>Rentang Kode</td>
                                            <td>:</td>
                                            <td>
                                                <input tabindex="1" type="text" placeholder="Kode awal" name="FRM_KODE_START"  value="<%=kodeAwal%>" class="formElemen" size="20">
                                                &nbsp; s.d. &nbsp;
                                                <input tabindex="1" type="text" placeholder="Kode akhir" name="FRM_KODE_END"  value="<%=kodeAkhir%>" class="formElemen" size="20">
                                            </td>
                                        </tr>
                                        <%}%>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                                            <td>:</td>
                                            <td> 
                                                <input tabindex="1" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE]%>"  value="<%= srcMaterial.getMatcode()%>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD(event)">
                                            </td>
                                        </tr>

                                        <tr>
                                            <td> <%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                                            <td>:</td>
                                            <td>
                                                <input tabindex="2" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME]%>"  value="<%=srcMaterial.getMatname()%>" class="formElemen" size="40" onKeyDown="javascript:keyDownCheck(event)" id="txt_materialname" >
                                            </td>
                                        </tr>

                                        <tr>
                                            <td> <%=textListTitleHeader[SESS_LANGUAGE][4]%></td>
                                            <td>:</td>
                                            <td>
                                                <select id="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID]%>"  name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID]%>" class="formElemen">
                                                    <option value="-1">Semua Category</option>
                                                    <%
                                                        Vector masterCatAcak = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);

                                                        Vector materGroup = PstCategory.structureList(masterCatAcak);
                                                        Vector vectGroupVal = new Vector(1, 1);
                                                        Vector vectGroupKey = new Vector(1, 1);
                                                        if (materGroup != null && materGroup.size() > 0) {
                                                            String parent = "";

                                                            Vector<Long> levelParent = new Vector();
                                                            for (int i = 0; i < materGroup.size(); i++) {
                                                                Category mGroup = (Category) materGroup.get(i);
                                                                if (mGroup.getCatParentId() != 0) {
                                                                    for (int lv = levelParent.size() - 1; lv > -1; lv--) {
                                                                        long oidLevel = levelParent.get(lv);
                                                                        if (oidLevel == mGroup.getCatParentId()) {
                                                                            break;
                                                                        } else {
                                                                            levelParent.remove(lv);
                                                                        }
                                                                    }
                                                                    parent = "";
                                                                    for (int lv = 0; lv < levelParent.size(); lv++) {
                                                                        parent = parent + "&nbsp;&nbsp;";
                                                                    }
                                                                    levelParent.add(mGroup.getOID());

                                                                } else {
                                                                    levelParent.removeAllElements();
                                                                    levelParent.add(mGroup.getOID());
                                                                    parent = "";
                                                                }
                                                    %>
                                                    <option value="<%=mGroup.getOID()%>"><%=parent%><%=mGroup.getName()%></option>
                                                    <%
                                                            }
                                                        } else {
                                                            vectGroupVal.add("Tidak Ada Category");
                                                            vectGroupKey.add("-1");
                                                        }
                                                    %>
                                                </select>
                                            </td>
                                        </tr>
                                        
                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][11]%></td>
                                            <td>:</td>
                                            <td>
                                                <input type="checkbox" <%if (srcMaterial.getShowImage() == 1) {%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_SHOW_IMAGE]%>" value="1">
                                            </td>
                                        </tr>
										
										<%if (typeOfBusinessDetail == 2) {%>
                                        <tr>
                                            <td>Tipe Item</td>
                                            <td>:</td>
                                            <td>
                                                <select class="formElemen" name="tipe_item">
                                                    <%
                                                        for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                                            if (main_material == Material.MATERIAL_TYPE_GENERAL) {
                                                                continue;
                                                            }
                                                    %>                            
                                                    <option <%=tipeItem == main_material ? "selected" : ""%> value="<%=main_material%>"><%=Material.MATERIAL_TYPE_TITLE[main_material]%></option>
                                                    <%
                                                        }
                                                    %>                            
                                                </select>
                                            </td>
                                        </tr>
                                        <%}%>

                                    </table>

                                    <br>
									<p><a class="btn btn-primary" onClick="expandSearch()" id="btnexpand"><i class='fa fa-plus-circle'></i> Tambah parameter pencarian</a></p>
                                    <br>

                                    <% //if (expandSearch == 1) {%>
									<div id="expand" style="display: none">
                                    <table width="50%" align="left">
                                        <tr>
                                            <td>
                                                <% if (typemenu == 0) {%>  
                                                <%=textListTitleHeader[SESS_LANGUAGE][3]%>
                                                <%}%>
                                            </td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    Vector val_supplier = new Vector(1, 1);
                                                    Vector key_supplier = new Vector(1, 1);
                                                    val_supplier.add("-1");
                                                    key_supplier.add(" ");

                                                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                                                            + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                                                            + " != " + PstContactList.DELETE;
                                                    Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                    if (vt_supp != null && vt_supp.size() > 0) {
                                                        int maxSupp = vt_supp.size();
                                                        for (int d = 0; d < maxSupp; d++) {
                                                            ContactList cnt = (ContactList) vt_supp.get(d);
                                                            String cntName = cnt.getCompName();
                                                            val_supplier.add(String.valueOf(cnt.getOID()));
                                                            key_supplier.add(cntName);
                                                        }
                                                    }
                                                    String select_supplier = "" + srcMaterial.getSupplierId();
                                                %>

                                                <% if (typemenu == 0) {%>

                                                <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SUPPLIERID], "formElemen", null, select_supplier, val_supplier, key_supplier, " tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 

                                                <%} else {%>

                                                <input type="hidden" name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SUPPLIERID]%>" value="-1">

                                                <%}%>
                                            </td>
                                        </tr>

                                        <%if (useSubCategory.equals("1")) {%>
                                        <tr>
                                            <td>Sub Categori </td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    String orderSubBy = PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
                                                    Vector listMaterialSubcategory = PstSubCategory.list(0, 0, "", orderSubBy);
                                                    Vector vectSubCatVal = new Vector(1, 1);
                                                    Vector vectSubCatKey = new Vector(1, 1);
                                                    vectSubCatVal.add(" ");
                                                    vectSubCatKey.add("0");
                                                    if (listMaterialSubcategory != null && listMaterialSubcategory.size() > 0) {
                                                        for (int i = 0; i < listMaterialSubcategory.size(); i++) {
                                                            SubCategory SubCategory = (SubCategory) listMaterialSubcategory.get(i);
                                                            vectSubCatVal.add(SubCategory.getName());
                                                            vectSubCatKey.add("" + SubCategory.getOID());
                                                        }
                                                    }
                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID], "formElemen", null, "" + srcMaterial.getSubCategoryId(), vectSubCatKey, vectSubCatVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>
                                        <%}%>

                                        <tr>
                                            <td><%=merkName%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    //Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                                                    materGroup = PstMerk.list(0, 0, PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "!=0", PstMerk.fieldNames[PstMerk.FLD_NAME]);
                                                    vectGroupVal = new Vector(1, 1);
                                                    vectGroupKey = new Vector(1, 1);
                                                    vectGroupVal.add(" ");
                                                    vectGroupKey.add("-1");
                                                    if (materGroup != null && materGroup.size() > 0) {
                                                        int maxGrp = materGroup.size();
                                                        for (int i = 0; i < maxGrp; i++) {
                                                            Merk mGroup = (Merk) materGroup.get(i);
                                                            vectGroupVal.add(mGroup.getCode() + " - " + mGroup.getName());
                                                            vectGroupKey.add("" + mGroup.getOID());
                                                        }
                                                    } else {
                                                        vectGroupVal.add("Tidak Ada Merk");
                                                        vectGroupKey.add("0");
                                                    }
                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID], "formElemen", null, "" + srcMaterial.getMerkId(), vectGroupKey, vectGroupVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>Etalase</td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    String whereKsg = "";
                                                    Vector listKsg = PstKsg.list(0, 0, whereKsg, PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                                    Vector vectKsgVal = new Vector(1, 1);
                                                    Vector vectKsgKey = new Vector(1, 1);
                                                    vectKsgKey.add("Semua");
                                                    vectKsgVal.add("0");
                                                    for (int i = 0; i < listKsg.size(); i++) {
                                                        Ksg matKsg = (Ksg) listKsg.get(i);
                                                        vectKsgKey.add("" + matKsg.getCode() + " - " + matKsg.getName());
                                                        vectKsgVal.add("" + matKsg.getOID());
                                                    }
                                                %> <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_GONDOLA_CODE], "formElemen", null, "" + srcMaterial.getGondolaId(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][15]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    //add opie-eyek 20130805
                                                    String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
                                                    Vector listPriceType = PstPriceType.list(0, 0, "", ordPrice);
                                                    Vector prTypeVal = new Vector(1, 1);
                                                    Vector prTypeKey = new Vector(1, 1);
                                                    for (int i = 0; i < listPriceType.size(); i++) {
                                                        PriceType prType = (PriceType) listPriceType.get(i);
                                                        prTypeVal.add("" + prType.getOID() + "");
                                                        prTypeKey.add("" + prType.getCode() + "");
                                                    }

                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID], "formElemen", null, "" + srcMaterial.getPriceTypeId(), prTypeVal, prTypeKey, " tabindex=\"8\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>

                                        <% if (typeOfBusiness.equals("3")) {%>
                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][16]%></td>
                                            <td>:</td>
                                            <td><input type="checkbox"  class="formElemen" name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_REQUIRED_SERIAL_NUMBER]%>"  value="1">
                                            </td>
                                        </tr>
                                        <%}%>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][17]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    //add opie-eyek 20130805
                                                    //String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
                                                    Vector listLocation = PstLocation.list(0, 0, "", "");
                                                    Vector locationVal = new Vector(1, 1);
                                                    Vector locationKey = new Vector(1, 1);
                                                    locationVal.add("0");
                                                    locationKey.add("All Location");
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location location = (Location) listLocation.get(i);
                                                        srcMaterial.setLocationId(location.getOID());
                                                        locationVal.add("" + location.getOID() + "");
                                                        locationKey.add("" + location.getName() + "");
                                                    }

                                                    long selectedId = 0;
                                                    if (listLocation.size() == 1) {
                                                        selectedId = srcMaterial.getLocationId();
                                                    }
                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_LOCATIONID], "formElemen", null, "" + selectedId, locationVal, locationKey, " tabindex=\"9\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][18]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    Vector editValue = new Vector(1, 1);
                                                    Vector editKey = new Vector(1, 1);

                                                    editKey.add("Aktive");
                                                    editValue.add("" + PstMaterial.NO_EDIT);

                                                    editKey.add(PstMaterial.editNames[SESS_LANGUAGE][PstMaterial.EDIT_NON_AKTIVE]);
                                                    editValue.add("" + PstMaterial.EDIT_NON_AKTIVE);

                                                %> 
                                                <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_EDIT_MATERIAL], "formElemen", null, "" + srcMaterial.getStatusMaterial(), editValue, editKey, null)%>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>View HPP vs Price</td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    Vector priceValue = new Vector(1, 1);
                                                    Vector priceKey = new Vector(1, 1);
                                                    priceKey.add("ALL");
                                                    priceValue.add("0");
                                                    priceKey.add("Hpp=0");
                                                    priceValue.add("1");
                                                    priceKey.add("Hpp > price");
                                                    priceValue.add("2");
                                                %> 
                                                <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_VIEW_PRICE_HPP], "formElemen", null, "" + srcMaterial.getViewHppvsPrice(), priceValue, priceKey, null)%>
                                            </td>
                                        </tr>

                                    </table>

                                    <table>
                                        <%
                                            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
                                            int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
                                            if (typemenu == 1 || DESIGN_MATERIAL_FOR == 1) {

                                                Vector selected = new Vector(1, 1);
                                                if (DESIGN_MATERIAL_FOR == 1) {
                                        %>
                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][19]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    //add opie-eyek 20130805
                                                    Vector sttRsv = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE, null);
                                                    Vector locationSellVal = new Vector(1, 1);
                                                    Vector locationSellKey = new Vector(1, 1);
                                                    for (int i = 0; i < sttRsv.size(); i++) {
                                                        Location temp = (Location) sttRsv.get(i);
                                                        locationSellVal.add("" + temp.getOID() + "");
                                                        locationSellKey.add("" + temp.getName() + "");
                                                    }
                                                    ControlCheckBox controlCheckBoxSellLoc = new ControlCheckBox();
                                                    controlCheckBoxSellLoc.setWidth(5);
                                                    out.println(controlCheckBoxSellLoc.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SELL_LOCATION], locationSellVal, locationSellKey, new Vector()));
                                                %>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>

                                        <tr>
                                            <!-- Option member type in Discount Qty -->
                                            <td>Tipe Member</td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    String ordDsc = PstDiscountType.fieldNames[PstDiscountType.FLD_INDEX];
                                                    Vector listDscType = PstDiscountType.list(0, 0, "", ordDsc);
                                                    Vector vectDscTypeVal = new Vector(1, 1);
                                                    Vector vectDscTypeKey = new Vector(1, 1);

                                                    for (int i = 0; i < listDscType.size(); i++) {
                                                        com.dimata.common.entity.payment.DiscountType dscType = (com.dimata.common.entity.payment.DiscountType) listDscType.get(i);
                                                        vectDscTypeVal.add("" + dscType.getOID() + "");
                                                        vectDscTypeKey.add("" + dscType.getCode() + "");
                                                    }
                                                    ControlCheckBox controlCheckBox = new ControlCheckBox();
                                                    controlCheckBox.setWidth(5);
                                                %>
                                                <%=controlCheckBox.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_MEMBER_TYPE_ID], vectDscTypeVal, vectDscTypeKey, new Vector())%><!--</td>-->
                                            </td>
                                        </tr>

                                        <tr> 
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>
                                                <%=drawCodeRangeList(SESS_LANGUAGE, recordsCodeRange)%>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>Dari</td>
                                            <td>:</td>
                                            <td>
                                                <%=ControlDate.drawDate(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_DATE_FROM], srcMaterial.getDateFrom(), "formElemen", 4, -8)%>
                                                &nbsp;&nbsp; s/d &nbsp;&nbsp;&nbsp;<%=ControlDate.drawDate(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_DATE_TO], srcMaterial.getDateTo(), "formElemen", 4, -8)%>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][6]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    Vector vectSortVal = new Vector(1, 1);
                                                    Vector vectSortKey = new Vector(1, 1);

                                                    vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][1]);
                                                    vectSortKey.add("0");

                                                    vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][4]);
                                                    vectSortKey.add("1");

                                                    vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][2]);
                                                    vectSortKey.add("2");
                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SORTBY], "formElemen", null, "" + srcMaterial.getSortby(), vectSortKey, vectSortVal, " tabindex=\"6\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td><%=textListTitleHeader[SESS_LANGUAGE][13]%></td>
                                            <td>:</td>
                                            <td>
                                                <%
                                                    Vector vectGroupMatVal = new Vector(1, 1);
                                                    Vector vectGroupMatKey = new Vector(1, 1);
                                                    
                                                    if (typeOfBusiness.equals("0")) {
                                                        vectGroupMatVal.add(textListTitleHeader[SESS_LANGUAGE][14]);
                                                        vectGroupMatKey.add("-1");
                                                        vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_REGULAR]);
                                                        vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_REGULAR);
                                                        vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_COMPOSITE]);
                                                        vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_COMPOSITE);
                                                        vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_SERVICE]);
                                                        vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_SERVICE);
                                                        vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_PACKAGE]);
                                                        vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_PACKAGE);
                                                    } else {
                                                        if (typemenu == 0) {
                                                            vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_REGULAR]);
                                                            vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_REGULAR);
                                                        } else {
                                                            vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_COMPOSITE]);
                                                            vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_COMPOSITE);
                                                            vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_SERVICE]);
                                                            vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_SERVICE);
                                                            vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_PACKAGE]);
                                                            vectGroupMatKey.add("" + PstMaterial.MAT_TYPE_PACKAGE);
                                                        }
                                                    }

                                                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_GROUP_ITEM], "formElemen", null, "" + srcMaterial.getGroupItem(), vectGroupMatKey, vectGroupMatVal, " tabindex=\"7\" onkeydown=\"javascript:fnTrapKD(event)\""));
                                                %>
                                            </td>
                                        </tr>

                                    </table>

                                    <table width="50%" align="right">
                                        <tr>
                                            <td>
                                                <input type="checkbox" <%if (srcMaterial.getShowUpdateCatalog() == 1) {%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SHOW_UPDATE_CATALOG]%>" value="1">
                                                Updated Catalog

                                                <input type="checkbox" <%if (srcMaterial.getShowHpp() == 1) {%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SHOW_HPP]%>" value="1">
                                                Print Hpp

                                                <input type="checkbox" <%if (srcMaterial.getShowDiscountQty() == 1) {%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SHOW_DISCOUNT_QTY]%>" value="1">
                                                Print With Discount Qty
                                            </td>
                                        </tr>

                                        <tr>
                                            <!-- Option currency in Discount Qty -->
                                            <td>
                                                <input type="checkbox" <%if (srcMaterial.getShowCurrency() == 1) {%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SHOW_CURRENCY]%>" value="1"> Mata Uang
                                            </td>
                                        </tr>
                                    </table>
									</div>
                                    <% //}%>

                                    <table style="clear: left">
                                        <tr> 
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr> 
                                            <td nowrap ><a class="btn btn-lg btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=textListTitleHeader[SESS_LANGUAGE][7]%></a></td>                        
                                            <% if (privAdd) {%>
                                            <td class="command" nowrap width=""><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus"></i> <%=textListTitleHeader[SESS_LANGUAGE][8]%></a></td>
                                            <td class="command" nowrap width=""><a class="btn btn-lg btn-primary" href="javascript:cmdImport()"><i class="fa fa-file-excel-o"></i> <%=textListTitleHeader[SESS_LANGUAGE][20]%></a></td>
                                            <%}%>
                                            <td class="command" nowrap width=""><a class="btn btn-lg btn-primary" href="javascript:cmdPrintAllPriceTag()"><i class="fa fa-print"></i> Print Price Tag</a></td>    
                                        </tr>
                                    </table>

                                    <script language="javascript">
                                        document.frmsrcmaterial.<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE]%>.focus();
                                    </script>			  
                                </form>
                                <!-- #EndEditable -->
                            </td> 
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
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>
    </body>

    <% if (typemenu == 0) {%>  
    <script>
        jQuery(function () {
            $("#txt_materialname").autocomplete("list.jsp");
        });
    </script>
    <%} else {%>
    <script>
        jQuery(function () {
            $("#txt_materialname").autocomplete("list_component.jsp");
        });

    </script>
    <%}%>
    <!-- #EndTemplate -->
</html>
