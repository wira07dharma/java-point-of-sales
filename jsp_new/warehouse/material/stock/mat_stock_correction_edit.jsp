<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_CORRECTION);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public static final String textListGlobal[][] = {
        {"Stok", "Koreksi Stok", "Pencarian", "Daftar", "Edit", "Proses sebagai Koreksi Stok", "Stok Opname sudah dikoreksi...",
            "Cetak Koreksi Stok", "Bandingkan Stock dan Opname", "Hitung Ulang Qty Sistem", "Cetak Koreksi Stok Excel"},
        {"Stock", "Stock Correction", "Search", "List", "Edit", "Process as Stock Correction", "Stock Opname has been corrected...",
            "Print Stock Correction", "Comparing Stock and Opname", "ReCalculate Qty Sistem", "Print Correction Stok Excel"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"No", "Lokasi", "Tanggal", "Time", "Status", "Supplier", "Kategori", "Sub Kategori", "Keterangan", "Total Lost", "Semua"},
                {"No", "Location", "Date", "Jam", "Status", "Supplier", "Category", "Sub Category", "Remark", "Total Lost", "All"}
            };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama", "Unit", "Kategori", "Sub Kategori", "Qty Opname", "Qty Terjual", "Qty System", "Qty Selisih", "Nilai Selisih", "Cost", "( + )", "( - )","Warna","Ukuran"},//13
                {"No", "Code", "Name", "Unit", "Category", "Sub Category", "Opname Qty", "Sold Qty", "System Qty", "Lost Qty", "Lost Value", "Cost", "( + )", "( - )","Color","Size"}
            };

    /**
     * this method used to list all stock opname item
     */
    public Vector drawListOpnameItem(int language, Vector objectClass, int start, int typeOfBusinessDetail) {
        Vector resultObject = new Vector();
        String result = "";
        double selisihMinus = 0.0;
        double selisihPlus = 0.0;
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            
            String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
            
            ctrlist.addHeader(textListOrderItem[language][0], "1%"); //no
            ctrlist.addHeader(textListOrderItem[language][1], "8%"); //sku
            if (useForGreenbowl.equals("1")) {
                ctrlist.addHeader("Barcode", "8%");
            }
            ctrlist.addHeader(textListOrderItem[language][2], "25%");//nama
            ctrlist.addHeader(textListOrderItem[language][3], "5%"); //unit
            if (useForGreenbowl.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][14], "5%"); //color
                ctrlist.addHeader(textListOrderItem[language][15], "5%"); //size
            }
            //ctrlist.addHeader(textListOrderItem[language][4],"15%");
            //ctrlist.addHeader(textListOrderItem[language][5],"15%");
            ctrlist.addHeader(textListOrderItem[language][6], "5%"); //Qty Opname
            //ctrlist.addHeader(textListOrderItem[language][7],"5%"); //Qty Terjual
            ctrlist.addHeader(textListOrderItem[language][8], "5%"); //Qty System
            ctrlist.addHeader(textListOrderItem[language][9], "5%"); //Qty Selisih
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Berat Awal", "5%");
                ctrlist.addHeader("Berat SO", "5%");
                ctrlist.addHeader("Berat Selisih", "5%");
            }
            ctrlist.addHeader(textListOrderItem[language][11], "5%"); //Qty Selisih
            ctrlist.addHeader(textListOrderItem[language][10], "8%");//Nilai Selisih
            ctrlist.addHeader(textListOrderItem[language][12], "8%");//Nilai Selisih
            ctrlist.addHeader(textListOrderItem[language][13], "8%");//Nilai Selisih

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
                MatStockOpnameItem soItem = (MatStockOpnameItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                Category cat = (Category) temp.get(3);
                SubCategory scat = (SubCategory) temp.get(4);
                rowx = new Vector();
                start = start + 1;

                //added by gunadi 2k19 for litama
                Material newmat = new Material();
                Category category = new Category();
                Color color = new Color();
                Kadar kadar = new Kadar();
                Kadar kadarOpname = new Kadar();
                MasterType masterTypeSize = new MasterType();
                try {
                    newmat = PstMaterial.fetchExc(soItem.getMaterialId());
                    category = PstCategory.fetchExc(newmat.getCategoryId());
                    if (PstColor.checkOID(newmat.getPosColor())) {
                        color = PstColor.fetchExc(newmat.getPosColor());
                    }
                    if (PstKadar.checkOID(soItem.getKadarId())) {
                        kadar = PstKadar.fetchExc(soItem.getKadarId());
                    }
                    if (PstKadar.checkOID(soItem.getKadarOpnameId())) {
                        kadarOpname = PstKadar.fetchExc(soItem.getKadarOpnameId());
                    }
                    long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, newmat.getOID());
                    if (PstMasterType.checkOID(oidMappingSize)) {
                        masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                    }
                } catch (Exception e) {

                }
                String name = "";
                name = mat.getName();
                String itemName = "" + newmat.getName();
                if (typeOfBusinessDetail == 2) {
                    if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                        itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                    } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                        itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                    }
                    name = itemName;
                }
                //double qtyLost = soItem.getQtySystem() - (soItem.getQtyOpname() + soItem.getQtySold());
                double qtyLost = 0;//(soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();

                if (soItem.getQtySystem() > 0 && soItem.getQtySystem() > 0) {
                    qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                } else if (soItem.getQtyOpname() > 0 && soItem.getQtySystem() == 0) {
                    qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                } else if (soItem.getQtySystem() > 0 && soItem.getQtyOpname() == 0) {
                    qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                } else if (soItem.getQtyOpname() == 0 && soItem.getQtySystem() < 0) {
                    qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                } else if (soItem.getQtyOpname() > 0 && soItem.getQtySystem() < 0) {
                    double balance = 0 - soItem.getQtySystem();
                    qtyLost = (soItem.getQtyOpname() + balance + soItem.getQtySold());
                }
                /*if(soItem.getQtySystem()>0 && soItem.getQtySystem() > 0 ){
                 qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                 }else{
                 if(soItem.getQtySystem()>0){
                 qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                 }else{
                 double balance = 0 + soItem.getQtySystem();
                 qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() - balance + soItem.getQtySold());
                 }                            
                 }*/

                rowx.add("" + start + "");
                //rowx.add("<div align=\"center\">"+mat.getSku()+"</div>");
                rowx.add("<div align=\"left\"><a href=\"javascript:cmdViewKartuStock('" + mat.getOID() + "','" + mat.getSku() + "','" + mat.getName() + "')\">" + mat.getSku() + "</a></div>");
                if (useForGreenbowl.equals("1")) {
                    rowx.add(newmat.getBarCode());
                }
                rowx.add(name);
                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                if (useForGreenbowl.equals("1")) {
                    rowx.add(color.getColorName());
                    rowx.add(masterTypeSize.getMasterName());
                }
                         //rowx.add(cat.getName());
                // rowx.add(scat.getName());
                rowx.add("<div align=\"center\">" + String.valueOf(soItem.getQtyOpname()) + "</div>");
                //rowx.add("<div align=\"center\">"+String.valueOf(soItem.getQtySold())+"</div>");
                rowx.add("<div align=\"center\">" + String.valueOf(soItem.getQtySystem()) + "</div>");
                rowx.add("<div align=\"center\">" + String.valueOf(qtyLost) + "</div>");
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"center\">" + String.valueOf(soItem.getBerat()) + "</div>");
                    rowx.add("<div align=\"center\">" + String.valueOf(soItem.getBeratOpname()) + "</div>");
                    rowx.add("<div align=\"center\">" + String.valueOf(soItem.getBeratSelisih()) + "</div>");
                }
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getCost()) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyLost * soItem.getCost()) + "</div>");
                double resultNilaiSelisih = qtyLost * soItem.getCost();
                if (resultNilaiSelisih > 0) {
                    selisihPlus = selisihPlus + resultNilaiSelisih;
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(resultNilaiSelisih) + "</div>");
                    rowx.add("<div align=\"right\">" + 0 + "</div>");
                } else {
                    selisihMinus = selisihMinus + resultNilaiSelisih;
                    rowx.add("<div align=\"right\">" + 0 + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(resultNilaiSelisih) + "</div>");
                }

                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
        }
        resultObject.add(result);
        resultObject.add(FRMHandler.userFormatStringDecimal(selisihPlus));
        resultObject.add(FRMHandler.userFormatStringDecimal(selisihMinus));

        return resultObject;
    }


%>


<!-- Jsp Block -->
<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
//I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
//I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
//int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
//int ownIndex = i_approval.getUserApprovalIndex(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID());
//long appMappingId = i_approval.getUserApprovalId(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID(),ownIndex);
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
    long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
    int finish = FRMQueryString.requestInt(request, "finish");
    int getQty = FRMQueryString.requestInt(request, "getQty");
    int correctstock = FRMQueryString.requestInt(request, "correctstock");

    /**
     * initialization of some identifier
     */
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    /**
     * dispatch code and title
     */
    String soCode = ""; //i_pstDocType.getDocCode(docType);
    String opnTitle = textListGlobal[SESS_LANGUAGE][1];//"Koreksi Stok"; //i_pstDocType.getDocTitle(docType);
    String soItemTitle = opnTitle + " Item";

    /**
     * action process
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
    iErrCode = ctrlMatStockOpname.action(Command.EDIT, oidStockOpname);
    FrmMatStockOpname frmso = ctrlMatStockOpname.getForm();
    MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
    errMsg = ctrlMatStockOpname.getMessage();

    oidStockOpname = so.getOID();
    int recordToGetItem = 50;
    int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);

    if ((iCommand == Command.FIRST) || (iCommand == Command.NEXT) || (iCommand == Command.PREV) || (iCommand == Command.LAST) || (iCommand == Command.LIST)) {
        startItem = ctrlMatStockOpname.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    if (finish == 1) {
        boolean hasil = PstMatStockOpname.StockCorrection(oidStockOpname, so.getLocationId());
        // boolean hasil = PstMatStockOpname.StockCorrectionForFixing(oidStockOpname);
        if (hasil == true) {
            finish = 0;
            System.out.println("Success !!!");
            try {
                so = PstMatStockOpname.fetchExc(oidStockOpname);
            } catch (Exception e) {
            }
        } else {
            System.out.println("Fail !!!");
        }
    }

    boolean documentClosed = false;
    if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
        documentClosed = true;
    }

    Location loc = new Location();
    if (so.getLocationId() != 0) {
        try {
            loc = PstLocation.fetchExc(so.getLocationId());
        } catch (Exception e) {
        }
    } else {
        loc.setName(textListOrderHeader[SESS_LANGUAGE][10] + " " + textListOrderHeader[SESS_LANGUAGE][1]);
    }

    Category category = new Category();
    if (so.getCategoryId() != 0) {
        try {
            category = PstCategory.fetchExc(so.getCategoryId());
        } catch (Exception e) {
        }
    } else {
        category.setName(textListOrderHeader[SESS_LANGUAGE][10] + " " + textListOrderHeader[SESS_LANGUAGE][6]);
    }

    ContactList contactList = new ContactList();
    if (so.getSupplierId() != 0) {
        try {
            contactList = PstContactList.fetchExc(so.getSupplierId());
        } catch (Exception e) {
        }
    } else {
        contactList.setCompName(textListOrderHeader[SESS_LANGUAGE][10] + " " + textListOrderHeader[SESS_LANGUAGE][5]);
    }

//getLostQty
//by Mirahu
//20110804
    Date startDate = so.getStockOpnameDate();

    if (getQty == 1) {
        boolean hasil = PstMatStockOpname.getMaterialOpname(oidStockOpname, so.getLocationId(), startDate);

        // boolean hasil = PstMatStockOpname.StockCorrectionForFixing(oidStockOpname);
        if (hasil == true) {
            getQty = 0;
            System.out.println("Success !!!");
            try {
                so = PstMatStockOpname.fetchExc(oidStockOpname);
            } catch (Exception e) {
            }
        } else {
            System.out.println("Fail !!!");
        }
    }

    /*
     * getKoreksiStock
     * Algoritma : 1. Koreksi stock jika status dokumen opname = final
     *             2. Stock sekarang dikurangi dengan selisih stock opname pada saat dokumen tersebut diopname
     * by Mirahu
     * 20110927
     */
    if (correctstock == 1) {
        //boolean hasil = PstMatStockOpname.StockCorrectionNew(oidStockOpname,so.getLocationId());
        boolean hasil = PstMatStockOpname.StockCorrectionAfterOpname(oidStockOpname, so.getLocationId(), startDate);

        // boolean hasil = PstMatStockOpname.StockCorrectionForFixing(oidStockOpname);
        if (hasil == true) {
            correctstock = 0;
            System.out.println("Success !!!");
            try {
                so = PstMatStockOpname.fetchExc(oidStockOpname);
            } catch (Exception e) {
            }
        } else {
            System.out.println("Fail !!!");
        }
    }

//UPDATED BY DEWOK 2018-12-22
//posisi coding get list data opname dipindah ke bawah untuk memastikan aksi membandingkan stok sudah dieksekusi terlebih dahulu
//baru setelah itu data di get untuk mendapatkan data opname (qty sistem) yg sesuai setelah di update
    Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem, recordToGetItem, oidStockOpname);

    Vector resultDraw = drawListOpnameItem(SESS_LANGUAGE, listMatStockOpnameItem, startItem, typeOfBusinessDetail);
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">

        //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function cmdCorrect() {        
                document.frm_matstockcorrection.command.value = "<%=Command.EDIT%>";
                document.frm_matstockcorrection.finish.value = "1";
                document.frm_matstockcorrection.action = "mat_stock_correction_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matstockcorrection.submit();
            }

            function cmdBack() {        
                document.frm_matstockcorrection.command.value = "<%=Command.FIRST%>";        
                document.frm_matstockcorrection.prev_command.value = "<%=prevCommand%>";
                document.frm_matstockcorrection.action = "mat_stock_correction_list.jsp";
                document.frm_matstockcorrection.submit();
            }

            function printForm() {
                var coodoo = "";
                var cboxes = document.getElementsByName('FRM_FIELD_PRICE_TYPE_ID');
                var len = cboxes.length;
                for (var i = 0; i < len; i++) {
                    if (cboxes[i].checked) {
                        //alert("checked");
                        coodoo = cboxes[i].value;
                    } else {
                        //alert("unchecked");
                    }
                }        
                window.open("mat_stock_correction_print_form.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&FRM_FIELD_PRICE_TYPE_ID='" + coodoo + "'", "corectionwh", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function printFormExcel(oidStockOpname) {
                var coodoo = "";
                var cboxes = document.getElementsByName('FRM_FIELD_PRICE_TYPE_ID');
                var len = cboxes.length;
                for (var i = 0; i < len; i++) {
                    if (cboxes[i].checked) {
                        //alert("checked");
                        coodoo = cboxes[i].value;
                    } else {
                        //alert("unchecked");
                    }
                }
                window.open("mat_stock_correction_print_excel.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&FRM_FIELD_PRICE_TYPE_ID='" + coodoo + "'", "corectionwhexcel", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function cmdGetLostQty() {        
                document.frm_matstockcorrection.command.value = "<%=Command.EDIT%>";
                document.frm_matstockcorrection.getQty.value = "1";
                document.frm_matstockcorrection.action = "mat_stock_correction_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matstockcorrection.submit();
            }

            function cmdRefresh()
            {
                document.frm_matstockcorrection.reload();
            }

            function cmdCorrectNew() {        
                document.frm_matstockcorrection.command.value = "<%=Command.EDIT%>";
                document.frm_matstockcorrection.correctstock.value = "1";
                document.frm_matstockcorrection.action = "mat_stock_correction_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matstockcorrection.submit();
            }

            function cmdViewKartuStock(oidMat, txtkode, txtnama) {
                //alert("jajs");
                var locationId = '<%=loc.getOID()%>';
                var date = '<%=Formater.formatDate(so.getStockOpnameDate(), "dd-MMM-yyyy")%>';
                //alert("asa"+date);
                var strvalue = "../report/src_stockcard.jsp?type=1&<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>=" + oidMat + "&txtkode=" + txtkode + "&txtnama=" + txtnama + "&locationId=" + locationId + "&typeCheckKartu=2&fromEndDate=" + date;
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
            }

        //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
            function itemList(comm) {
                document.frm_matstockcorrection.command.value = comm;
                document.frm_matstockcorrection.prev_command.value = comm;
                document.frm_matstockcorrection.action = "mat_stock_correction_edit.jsp";
                document.frm_matstockcorrection.submit();
            }
        //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------



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
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable -->
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> 
                </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr>
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%><!-- #EndEditable -->
                            </td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" -->
                                <form name="frm_matstockcorrection" method="post" action="">
                                    <%
                                        try {
                                    %>
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
                                    <input type="hidden" name="hidden_opname_item_id" value="">
                                    <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
                                    <input type="hidden" name="finish" value="<%=finish%>">
                                    <input type="hidden" name="getQty" value="<%=getQty%>">
                                    <input type="hidden" name="correctstock" value="<%=correctstock%>">
                                    <table width="100%" border="0">
                                        <tr>
                                            <td valign="top" colspan="3"> <hr size="1"> </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr>
                                                        <td width="10%" align="left">
                                                            <%
                                                                if (so.getStockOpnameNumber() != "" && iErrCode == 0) {
                                                                    out.println("Opname Number");
                                                                } else {
                                                                    out.println("");
                                                                }
                                                            %>
                                                        </td>
                                                        <td width="25%" align="left">
                                                            <%
                                                                if (so.getStockOpnameNumber() != "" && iErrCode == 0) {
                                                                    out.println(": <strong>" + so.getStockOpnameNumber() + "</strong>");
                                                                } else {
                                                                    out.println("");
                                                                }
                                                            %>
                                                        </td>
                                                        <td width="10%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td width="20%" align="left" valign="bottom">: <%=Formater.formatDate(so.getStockOpnameDate(), "dd-MMM-yyyy")%></td>
                                                        <td width="10%" rowspan="4" align="left" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                                                        <td width="25%" rowspan="4" align="left" valign="top">: <%=so.getRemark()%>                
                                                    </tr>
                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                        <td>: <%=loc.getName()%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                        <td>: <%=category.getName()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                        <td>: <%=contactList.getCompName()%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                        <td>: 
                                                            <%if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                                                } else if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                } else if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                                } else if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                            }%>
                                                        </td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                </table></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"><%=resultDraw.get(0)%></td>
                                                    </tr>
                                                    <%if (oidStockOpname != 0) {%>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="3" class="command">
                                                            <span class="command">
                                                                <%
                                                                    if (prevCommand != Command.FIRST && prevCommand != Command.PREV && prevCommand != Command.NEXT && prevCommand != Command.LAST) {
                                                                        prevCommand = Command.FIRST;
                                                                    }
                                                                    ctrLine.setLocationImg(approot + "/images");
                                                                    ctrLine.initDefault();
                                                                    ctrLine.setImageListName(approot + "/images", "item");

                                                                    ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "')");
                                                                    ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "')");
                                                                    ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "')");
                                                                    ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "')");

                                                                %>
                                                                <%=ctrLine.drawImageListLimit(prevCommand, vectSizeItem, startItem, recordToGetItem)%> </span> </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                    <td width="6%"></td>
                                                                    <td width="94%"></td>
                                                                </tr>
                                                            </table></td>
                                                    </tr>
                                                    <%}%>
                                                </table></td>
                                        </tr>
                                        <tr>

                                            <td colspan="2" valign="top" class="comment">
                                                <!-- getQtyLost -->
                                                <%
                                                    //if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                    if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL || so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                %>
                                                <table width="38%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td width="5%" valign="top">&nbsp;&nbsp;<a href="javascript:cmdGetLostQty()"><img src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][8]%>"></a></td>
                                                        <td width="95%" nowrap>&nbsp; <a href="javascript:cmdGetLostQty()"><%=textListGlobal[SESS_LANGUAGE][8]%></a></td>
                                                    </tr>
                                                </table>
                                                <%
                                                } else if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                %>

                                                <table width="38%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td width="5%" valign="top">&nbsp;&nbsp;<a href="javascript:cmdGetLostQty()"><img src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][9]%>"></a></td>
                                                        <td width="95%" nowrap>&nbsp; <a href="javascript:cmdGetLostQty()"><%=textListGlobal[SESS_LANGUAGE][9]%></a></td>
                                                    </tr>
                                                </table>
                                                <%
                                                    }
                                                %>

                                                <%
                                                    //if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                    if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                                                %>
                                                <table width="38%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <!--td width="5%" valign="top">&nbsp;&nbsp;<a href="javascript:cmdCorrect()"><img src="<%//=approot%>/images/BtnSave.jpg" width="24" height="24" border="0" alt="<%//=textListGlobal[SESS_LANGUAGE][5]%>"></a></td>
                                                        <td width="95%" nowrap>&nbsp; <a href="javascript:cmdCorrect()"><%//=textListGlobal[SESS_LANGUAGE][5]%></a></td>-->
                                                        <td width="5%" valign="top">&nbsp;&nbsp;<a href="javascript:cmdCorrectNew()"><img src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][5]%>"></a></td>
                                                        <td width="95%" nowrap>&nbsp; <a href="javascript:cmdCorrectNew()"><%=textListGlobal[SESS_LANGUAGE][5]%></a></td>

                                                    </tr>
                                                </table>
                                                <%
                                                } else if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                %>
                                                <%=textListGlobal[SESS_LANGUAGE][6]%>
                                                <%
                                                    }
                                                %>
                                            </td>
                                            <td width="20%" align="right">
                                                &nbsp;Total Lost (+)&nbsp;&nbsp;<%=resultDraw.get(1)%><br>
                                                &nbsp;Total Lost (-)&nbsp;&nbsp;<%=resultDraw.get(2)%><br><br>
                                                &nbsp;<%=textListOrderHeader[SESS_LANGUAGE][9]%> <%=FRMHandler.userFormatStringDecimal(PstMatStockOpnameItem.getLostQty(oidStockOpname))%>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3"> <table width="100%" border="0">
                                                    <tr>
                                                        <td width="60%">
                                                            <%
                                                                ctrLine.setLocationImg(approot + "/images");

                                                                // set image alternative caption
                                                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_SAVE, true));
                                                                ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) + " List");
                                                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_ASK, true));
                                                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_CANCEL, false));

                                                                ctrLine.initDefault();
                                                                ctrLine.setTableWidth("100%");
                                                                    //String scomDel = "javascript:cmdAsk('"+oidStockOpname+"')";
                                                                //String sconDelCom = "javascript:cmdConfirmDelete('"+oidStockOpname+"')";
                                                                //String scancel = "javascript:cmdEdit('"+oidStockOpname+"')";
                                                                ctrLine.setCommandStyle("command");
                                                                ctrLine.setColCommStyle("command");

                                                                // set command caption
                                                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_SAVE, true));
                                                                ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) + " List");
                                                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_ASK, true));
                                                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_DELETE, true));
                                                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_CANCEL, false));

                                                                ctrLine.setConfirmDelCaption("");
                                                                ctrLine.setDeleteCaption("");
                                                                ctrLine.setEditCaption("");
                                                                ctrLine.setSaveCaption("");
                                                                ctrLine.setAddCaption("");

                                                                if (iCommand == Command.SAVE && frmso.errorSize() == 0) {
                                                                    iCommand = Command.EDIT;
                                                                }

                                                                if (documentClosed) {
                                                                    ctrLine.setSaveCaption("");
                                                                    ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, opnTitle, ctrLine.CMD_BACK, true) + " List");
                                                                    ctrLine.setDeleteCaption("");
                                                                    ctrLine.setConfirmDelCaption("");
                                                                    ctrLine.setCancelCaption("");
                                                                }
                                                            %>
                                                            <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%> 
                                                        </td>
                                                        <td width="40%">
                                                            <%if (listMatStockOpnameItem != null && listMatStockOpnameItem.size() > 0) {%>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td width="20%" valign="top"> Cetak Dengan Harga</td>  
                                                                    <td width="30%" valign="top">
                                                                        <%
                                                                            //add opie-eyek 20130805
                                                                            ControlCheckBox controlCheckBox = new ControlCheckBox();
                                                                            String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
                                                                            Vector listPriceType = PstPriceType.list(0, 0, "", ordPrice);
                                                                            Vector prTypeVal = new Vector(1, 1);
                                                                            Vector prTypeKey = new Vector(1, 1);
                                                                            Vector vSelecetd = new Vector(1, 1);
                                                                            for (int i = 0; i < listPriceType.size(); i++) {
                                                                                PriceType prType = (PriceType) listPriceType.get(i);
                                                                                prTypeVal.add("" + prType.getOID() + "");
                                                                                prTypeKey.add("" + prType.getCode() + "");

                                                                                if (listPriceType.size() == 1) {
                                                                                    vSelecetd.add("" + prType.getOID());
                                                                                }
                                                                            }
                                                                            controlCheckBox.setWidth(5);
                                                                        %> 
                                                                        <%=controlCheckBox.draw("FRM_FIELD_PRICE_TYPE_ID", prTypeVal, prTypeKey, vSelecetd)%>
                                                                    </td>  
                                                                    <td width="5%" valign="top"><a href="javascript:printForm('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td>
                                                                    <td width="20%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidStockOpname%>')" class="command" ><%=textListGlobal[SESS_LANGUAGE][7]%></a></td>
                                                                    <td width="5%" valign="top"><a href="javascript:printFormExcel('<%=oidStockOpname%>')" ><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                                                                    <td width="20%" nowrap>&nbsp; <a href="javascript:printFormExcel('<%=oidStockOpname%>')" class="command" ><%=textListGlobal[SESS_LANGUAGE][10]%></a></td>
                                                                </tr>
                                                            </table>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <%
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    %>
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
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>

