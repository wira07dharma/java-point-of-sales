<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.posbo.form.warehouse.FrmMatStockOpname,
         com.dimata.posbo.entity.warehouse.MatStockOpname,
         com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.posbo.entity.masterdata.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi", "Tanggal", "Jam", "Status", "Supplier", "Kategori", "Sub Kategori", "Keterangan"},
                {"No", "Location", "Date", "Time", "Status", "Supplier", "Category", "Sub Category", "Remark"}
            };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama Barang", "Unit", "Kategori", "Sub Kategori", "Qty Opname","Warna","Ukuran"},
                {"No", "Code", "Name", "Unit", "Category", "Sub Category", "Qty Opname","Color","Size"}
            };
    public static final String textListPrintHeader[][]
            = {
                {"Opname Barang", "Semua Kategori", "Semua Sub Kategori", "Dibuat Oleh", "Disetujui Oleh", "Diketahui Oleh", "Stok opname item masih kosong .."},
                {"Goods Opname", "All Category", "All Sub Category", "Created by", "Approved by", "Approved by", "Stock opname item still empty .."}
            };

    /**
     * this method used to list all stock opname item
     */
    public String drawListOpnameItem(int language, Vector objectClass, int start, int groupby) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0], "3%");
            ctrlist.addHeader(textListOrderItem[language][1], "15%");
            ctrlist.addHeader("Barcode", "8%");
            ctrlist.addHeader(textListOrderItem[language][2], "20%");
            String textColumn = "";
            String textColumn2 = "";
            if (groupby == 1) { //Kategori
                textColumn = "Suplier";
            } else if (groupby == 2) {
                textColumn = "Kategori";
            } else if (groupby == 3) {
                textColumn = "Kategori";
            }
            ctrlist.addHeader("" + textColumn + "", "8%");
            ctrlist.addHeader("Sub Category", "8%");
            ctrlist.addHeader("Merk", "8%");

            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "15%");
            ctrlist.addHeader(textListOrderItem[language][5], "15%");
            ctrlist.addHeader(textListOrderItem[language][6], "5%");

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

                rowx.add("" + start + "");
                rowx.add(mat.getSku());
                rowx.add(mat.getName());
                rowx.add(unit.getCode());
                rowx.add(cat.getName());
                rowx.add(scat.getName());
                rowx.add("<div align=\"right\">" + String.valueOf(soItem.getQtyOpname()) + "</div>");

                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListPrintHeader[language][6] + "</div>";
        }
        return result;
    }

%>
<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_OPN);
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
    String groupByVal = "1";
    try {
        groupByVal = FRMQueryString.requestString(request, "groupByElement");
    } catch (Exception ex) {
        groupByVal = "1";
    }
    
    String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");

    int groupby = Integer.parseInt(groupByVal);
    String order = "";
    if (groupby == 1) {
        order = " CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ASC";
    } else if (groupby == 2) {
        order = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ASC";
    } else if (groupby == 3) {
        order = " PKG." + PstKsg.fieldNames[PstKsg.FLD_NAME] + " ASC";
    }
    //order for jewelry
    if (typeOfBusinessDetail == 2) {
        order = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }

    /**
     * initialization of some identifier
     */
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    /**
     * dispatch code and title
     */
    String soCode = "";// i_pstDocType.getDocCode(docType);
    String opnTitle = textListPrintHeader[SESS_LANGUAGE][0]; //i_pstDocType.getDocTitle(docType);
    String soItemTitle = opnTitle + " Item";

    /**
     * action process
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
    iErrCode = ctrlMatStockOpname.action(iCommand, oidStockOpname);
    FrmMatStockOpname frmso = ctrlMatStockOpname.getForm();
    MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
    errMsg = ctrlMatStockOpname.getMessage();

    /**
     * if iCommand = Commmand.ADD ---> Set default rate which value taken from
     * PstCurrencyRate
     */
    //double curr = PstCurrencyRate.getLastCurrency();
    //String priceCode = "Rp.";
    /**
     * check if document already closed or not
     */
    boolean documentClosed = false;
    if (so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
        documentClosed = true;
    }

    /**
     * check if document may modified or not
     */
    boolean privManageData = false;

    /**
     * list purchase order item
     */
    oidStockOpname = so.getOID();
    int recordToGetItem = 0;
    int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
    Vector listMatStockOpnameItem = PstMatStockOpnameItem.list2(startItem, recordToGetItem, oidStockOpname, order);

    response.setHeader("Content-Disposition","attachment; filename=Opname.xls");
%>
<%@ page contentType="application/x-msexcel" %>
<html><!-- #BeginTemplate "/Templates/print.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <style>
            .tabel_data td {padding: 5px}
            td {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 12px;
            }
        </style>
        <script language="JavaScript">
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    </head>  

    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <tr> 
                <td width="88%" valign="top" align="left" height="56"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_matopname" method="post" action="">
                                    <%                                try {
                                    %>
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">			  
                                    <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">			  			  
                                    <input type="hidden" name="hidden_opname_item_id" value="">
                                    <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">

                                    <table width="100%" border="0">
                                        <tr align="center"> 
                                            <td colspan="3" class="title"><b><%=opnTitle.toUpperCase()%></b></td>
                                        </tr>
                                        <tr> 
                                            <td colspan="3" valign="top"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="33%" align="left"> 
                                                            <%
                                                                if (so.getStockOpnameNumber() != "" && iErrCode == 0) {
                                                                    out.println(textListOrderHeader[SESS_LANGUAGE][0] + " : " + so.getStockOpnameNumber());
                                                                } else {
                                                                    out.println("");
                                                                }
                                                            %> 
                                                        </td>
                                                        <td align="center" valign="bottom">&nbsp;</td>
                                                        <td width="33%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : <%=Formater.formatDate(so.getStockOpnameDate(), "dd MMMM yyyy")%> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="33%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%> :                                
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(so.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %> 
                                                            <%=loc1.getName()%> 
                                                        </td>
                                                        <td align="center" valign="bottom"> <%//=strComboStatus%> </td>
                                                        <td width="33%" align="right"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="33%" align="left"> <%=textListOrderHeader[SESS_LANGUAGE][5]%> : 
                                                            <%
                                                                ContactList cnt = new ContactList();
                                                                try {
                                                                    cnt = PstContactList.fetchExc(so.getSupplierId());
                                                                } catch (Exception e) {
                                                                }
                                                                String cntName = cnt.getCompName();
                                                                if (cntName.length() == 0) {
                                                                    cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                                }
                                                            %>
                                                            <%=cntName%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="33%" align="left"> <%=textListOrderHeader[SESS_LANGUAGE][6]%> : 
                                                            <%
                                                                Category cat = new Category();
                                                                cat.setName(textListPrintHeader[SESS_LANGUAGE][1]);
                                                                try {
                                                                    if (so.getCategoryId() != 0) {
                                                                        cat = PstCategory.fetchExc(so.getCategoryId());
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            %> 
                                                            <%=cat.getName()%>
                                                        </td>
                                                        <td width="33%" align="right"></td>
                                                    </tr>
                                                </table>                                    
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td valign="top">
                                                <table width="100%" class="tabel_data" border="1" cellspacing="0" cellpadding="0">
                                                    <tr align="center"> 
                                                        <td width="1%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                        <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                        <td width="10%">Barcode</td>
                                                        <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                        <%
                                                            if (useForGreenbowl.equals("0")) {
                                                                String textColumn = "";
                                                                String textColumn2 = "";
                                                                if (groupby == 1) { //Kategori
                                                                    textColumn = "Suplier";
                                                                } else if (groupby == 2) {
                                                                    textColumn = "Kategori";
                                                                } else if (groupby == 3) {
                                                                    textColumn = "Kategori";
                                                                }
                                                                out.println("<td>" + textColumn + "</td>");
                                                                out.println("<td>Sub Category</td>");
                                                                out.println("<td>Merk</td>");
                                                                if (groupby == 1) { //Kategori
                                                                    textColumn2 = "Rak Gondola";
                                                                } else if (groupby == 2) { //Suplier
                                                                    textColumn2 = "Rak Gondola";
                                                                } else if (groupby == 3) {
                                                                    textColumn2 = "Suplier";
                                                                }
                                                                out.println("<td>" + textColumn2 + "</td>");
                                                            }
                                                        %>
                                                        <td width="5%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td width="15%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                        <td width="5%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                        <% } %>
                                                        <td width="5%"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                    </tr>
                                                    <%
                                                        int start = 0;
                                                        String header = "";
                                                        for (int i = 0; i < listMatStockOpnameItem.size(); i++) {
                                                            Vector temp = (Vector) listMatStockOpnameItem.get(i);
                                                            MatStockOpnameItem soItem = (MatStockOpnameItem) temp.get(0);
                                                            Material mat = (Material) temp.get(1);
                                                            Unit unit = (Unit) temp.get(2);
                                                            Category catx = (Category) temp.get(3);
                                                            SubCategory scatx = (SubCategory) temp.get(4);
                                                            Merk merk = (Merk) temp.get(5);
                                                            ContactList contactLis = (ContactList) temp.get(6);
                                                            Ksg ksg = (Ksg) temp.get(7);
                                                            Color color = new Color();
                                                            MasterType masterTypeSize = new MasterType();
                                                            try {
                                                                mat = PstMaterial.fetchExc(mat.getOID());
                                                                if (PstColor.checkOID(mat.getPosColor())) {
                                                                    color = PstColor.fetchExc(mat.getPosColor());
                                                                }
                                                                long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, mat.getOID());
                                                                if (PstMasterType.checkOID(oidMappingSize)) {
                                                                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                                                                }
                                                            } catch (Exception e) {
                                                                
                                                            }
                                                            
                                                            String headerCompare = "";
                                                            if (groupby == 1) {
                                                                headerCompare = catx.getName();
                                                            } else if (groupby == 2) {
                                                                headerCompare = contactLis.getCompName();
                                                                if (headerCompare == "" || headerCompare == null) {
                                                                    headerCompare = "No Suplier";
                                                                }
                                                            } else if (groupby == 3) {
                                                                headerCompare = ksg.getName();
                                                            }
                                                            if (headerCompare.equals(header)) {
                                                            } else {
                                                                header = headerCompare;
                                                                out.println("<tr><td colspan='8'><b>" + header + "</b></td><tr>");
                                                            }

                                                            start = start + 1;
                                                            out.println("<tr>");
                                                            out.println("<td>" + start + "</td>");
                                                            out.println("<td style='mso-number-format:\"\\@\"' nowrap>" + mat.getSku() + "</td>");
                                                            out.println("<td style='mso-number-format:\"\\@\"'>" + mat.getBarCode() + "</td>");
                                                            out.println("<td style='mso-number-format:\"\\@\"'>" + mat.getName() + "</td>");
                                                            
                                                            if (useForGreenbowl.equals("0")) {
                                                                String contentColumn = "";
                                                                if (groupby == 1) { //Kategori
                                                                    contentColumn = contactLis.getCompName();
                                                                } else if (groupby == 2) {
                                                                    contentColumn = catx.getName();
                                                                } else if (groupby == 3) {
                                                                    contentColumn = catx.getName();
                                                                }
                                                                out.println("<td>" + contentColumn + "</td>");
                                                                out.println("<td>" + scatx.getName() + "</td>");
                                                                out.println("<td>" + merk.getName() + "</td>");
                                                                String contentColumn2 = "";
                                                                if (groupby == 1) { //Kategori
                                                                    contentColumn2 = ksg.getName();
                                                                } else if (groupby == 2) { //Suplier
                                                                    contentColumn2 = ksg.getName();
                                                                } else if (groupby == 3) {
                                                                    contentColumn2 = contactLis.getCompName();
                                                                }
                                                                out.println("<td>" + contentColumn2 + "</td>");
                                                            }
                                                            out.println("<td>" + unit.getCode() + "</td>");
                                                            if (useForGreenbowl.equals("1")) {
                                                                out.println("<td>" + color.getColorName() + "</td>");
                                                                out.println("<td>" + masterTypeSize.getMasterName() + "</td>");
                                                            }
                                                            String opname = "";
                                                            if (soItem.getQtyOpname() == 0) {
                                                                opname = "";
                                                            } else {
                                                                opname = "" + soItem.getQtyOpname();
                                                            }
                                                            out.println("<td>" + opname + "</td>");
                                                            out.println("<tr>");
                                                        }
                                                    %>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                    <tr align="left" valign="top"> 
                                                        <%
                                                            try {
                                                        %>
                                                        <td height="22" valign="middle" colspan="3"> <%=textListOrderHeader[SESS_LANGUAGE][8]%> : <%=so.getRemark()%> </td>
                                                        <%
                                                            } catch (Exception e) {
                                                                System.out.println(e);
                                                            }
                                                        %>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="22" valign="middle" colspan="4"></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="40" valign="middle" colspan="4"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="25%" align="left" nowrap>PETUGAS 1</td>  
                                                        <td width="25%" align="left" nowrap>PETUGAS 2</td>
                                                        <td width="25" align="left" nowrap>KEPALA TOKO</td>
                                                        <td width="25%" align="left" nowrap>EDP/INVENTORY</td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="75" valign="middle" colspan="4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="25%" align="left" nowrap> (.................................)</td>  
                                                        <td width="25%" align="left" nowrap> (.................................)</td>
                                                        <td width="25%" align="left" nowrap> (.................................)</td>
                                                        <td width="25%" align="left" nowrap> (.................................)</td>
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
                                <!-- #EndEditable -->
                            </td> 
                        </tr> 
                    </table>
                </td>
            </tr>
        </table>
        <script type="text/javascript" src="../../../styles/jQuery-2.1.4.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                window.print();
            });
        </script>
    </body>
    <!-- #EndTemplate --></html>
