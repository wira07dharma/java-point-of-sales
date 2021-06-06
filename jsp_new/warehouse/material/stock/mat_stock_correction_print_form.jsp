<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.posbo.entity.warehouse.MatStockOpname,
         com.dimata.posbo.form.warehouse.FrmMatStockOpname,
         com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.contact.PstContactList"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_CORRECTION);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]= {
                {"Nomor", "Lokasi", "Tanggal", "Jam", "Status", "Supplier", "Kategori", "Sub Kategori", "Keterangan", "Total Lost"},
                {"No", "Location", "Date", "Jam", "Status", "Supplier", "Category", "Sub Category", "Remark", "Total Lost"}
            };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]= {
                {"No", "Sku", "Nama Barang", "Unit", "Kategori", "Sub Kategori", "Opname", "Terjual", "System", "Selisih", "Nilai Selisih", "Cost", "( + )", "( - )","Harga Jual","Barcode","Warna","Ukuran"},
                {"No", "Code", "Name", "Unit", "Category", "Sub Category", "Opname", "Sold", "System", "Lost", "Lost Value", "Cost", "( + )", "( - )","Sale Price","Barcode","Color","Size"}
            };
    public static final String textListPrintHeader[][]= {
                {"Stok opname item masih kosong ..", "Koreksi Stok", "Dibuat Oleh", "Disetujui Oleh", "Mengetahui Oleh"},
                {"Stock opname item still empty ..", "Stock Correction", "Created", "Approved", "Approved"}
            };

    /**
     * this method used to list all stock opname item
     */
    public Vector drawListOpnameItem(int language, Vector objectClass, int start) {
        String result = "";
        Vector resultObject = new Vector();
        double selisihMinus = 0.0;
        double selisihPlus = 0.0;
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0], "3%");
            ctrlist.addHeader(textListOrderItem[language][1], "15%");
            ctrlist.addHeader(textListOrderItem[language][2], "20%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "15%");
            ctrlist.addHeader(textListOrderItem[language][5], "15%");
            ctrlist.addHeader(textListOrderItem[language][6], "5%");
            //ctrlist.addHeader(textListOrderItem[language][7],"5%");
            ctrlist.addHeader(textListOrderItem[language][8], "5%");
            ctrlist.addHeader(textListOrderItem[language][9], "5%");
            ctrlist.addHeader(textListOrderItem[language][11], "5%");
            ctrlist.addHeader(textListOrderItem[language][10], "10%");
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

                //double qtyLost = soItem.getQtySystem() - (soItem.getQtyOpname() + soItem.getQtySold());
                double qtyLost = (soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();
                rowx.add("" + start + "");
                rowx.add("<div align=\"center\">" + mat.getSku() + "</div>");
                rowx.add(mat.getName());
                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                rowx.add(cat.getName());
                rowx.add(scat.getName());
                rowx.add("<div align=\"center\">" + String.valueOf(soItem.getQtyOpname()) + "</div>");
                //rowx.add("<div align=\"center\">"+String.valueOf(soItem.getQtySold())+"</div>");
                rowx.add("<div align=\"center\">" + String.valueOf(soItem.getQtySystem()) + "</div>");
                rowx.add("<div align=\"center\">" + String.valueOf(qtyLost) + "</div>");
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
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListPrintHeader[language][0] + "</div>";
        }
        resultObject.add(result);
        resultObject.add(FRMHandler.userFormatStringDecimal(selisihPlus));
        resultObject.add(FRMHandler.userFormatStringDecimal(selisihMinus));
        return resultObject;
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

    String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
    
    /**
     * initialization of some identifier
     */
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    /**
     * dispatch code and title
     */
    String soCode = ""; //i_pstDocType.getDocCode(docType);
    String opnTitle = textListPrintHeader[SESS_LANGUAGE][1];//i_pstDocType.getDocTitle(docType);
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
     * ---> START WORKFLOW PROCESS get value for each approval 'hidden form'
     */
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
//int recordToGetItem = 20;
    int recordToGetItem = 0;
    int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
    Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem, recordToGetItem, oidStockOpname);


%>
<html><!-- #BeginTemplate "/Templates/print.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title
        ><script language="JavaScript">
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">
        <!-- #EndEditable -->
        <style>
            .tabel_data td {padding: 5px}
        </style>
    </head>  
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr> 
                <td width="88%" valign="top" align="left" height="56"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_matopname" method="post" action="">
                                    <%    try {
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
                                            <td colspan="3" align="center"> <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="33%" align="left"> <%
                                                            if (so.getStockOpnameNumber() != "" && iErrCode == 0) {
                                                                out.println(textListOrderHeader[SESS_LANGUAGE][0] + " : " + so.getStockOpnameNumber());
                                                            } else {
                                                                out.println("");
                                                            }
                                                            %> </td>
                                                        <td align="center" valign="bottom">&nbsp;</td>
                                                        <td width="33%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : <%=Formater.formatDate(so.getStockOpnameDate(), "dd MMMM yyyy")%> </tr>
                                                    <tr> 
                                                        <td width="33%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%> : 
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(so.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %> <%=loc1.getName()%> </td>
                                                        <td align="center" valign="bottom"> <%//=strComboStatus%> </td>
                                                        <td width="33%" align="right"></td></tr>
                                                    <tr> 
                                                        <td width="33%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : 
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
                                                            %> <%=cntName%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="33%" align="left"> <%=textListOrderHeader[SESS_LANGUAGE][6]%> :
                                                            <%
                                                                Category cat = new Category();
                                                                try {
                                                                    if (so.getCategoryId() != 0) {
                                                                        cat = PstCategory.fetchExc(so.getCategoryId());
                                                                    } else {
                                                                        cat.setName("Semua Kategori");
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            %> <%=cat.getName()%></td>
                                                        <td width="33%" align="right"></td></tr>
                                                </table></td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" class="tabel_data" border="1" cellspacing="0" cellpadding="0">
                                                    <%
                                                        
                                                        Vector vectMember = new Vector(1,1);
                                                        String[] strMember = null;
                                                        Vector listTypeHrga =  new Vector ();
                                                        strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
                                                        String sStrMember="";
                                                        if(strMember!=null && strMember.length>0) {
                                                               for(int i=0; i<strMember.length; i++) {
                                                                       try {
                                                                           if(strMember[i] != null && strMember[i].length()>0){ 
                                                                            vectMember.add(strMember[i]);
                                                                           sStrMember=sStrMember+strMember[i]+",";
                                                                           }
                                                                       }
                                                                       catch(Exception exc) {
                                                                               System.out.println("err");
                                                                       }
                                                               }
                                                               if(sStrMember != null && sStrMember.length()>0){
                                                                   sStrMember=sStrMember.substring(0, sStrMember.length()-1);
                                                                   String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+sStrMember+")";
                                                                    listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
                                                               }
                                                        }
                                                    %>
                                                    <tr align="center"> 
                                                        <td width="1%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                        <td width="10%" rowspan="2" nowrap><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td width="10%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][15]%></td>
                                                        <% } %>
                                                        <td width="18%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                        <td width="5%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td width="15%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][16]%></td>
                                                        <td width="5%" rowspan="2"><%=textListOrderItem[SESS_LANGUAGE][17]%></td>
                                                        <% } %>
                                                        <td height="21" colspan="5">Quantity</td>
                                                        <%if(listTypeHrga.size()>0) {%>
                                                            <td width="6%" rowspan="2" ><%=textListOrderItem[SESS_LANGUAGE][14]%></td><%--Cost--%>
                                                        <%}else{%>
                                                            <td width="6%" rowspan="2" ><%=textListOrderItem[SESS_LANGUAGE][11]%></td><%--Cost--%>
                                                        <%}%>
                                                        <td width="8%" rowspan="2" ><%=textListOrderItem[SESS_LANGUAGE][10]%></td><%--nilai selisih--%>
                                                        <td height="21" colspan="2"></td>
                                                    </tr>
                                                    <tr align="center"> 
                                                        <td width="7%" height="21"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                        <td width="8%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                        <td width="7%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                        <td width="6%" nowrap><%=textListOrderItem[SESS_LANGUAGE][12]%></td><%-- + --%>
                                                        <td width="6%" nowrap><%=textListOrderItem[SESS_LANGUAGE][13]%></td><%-- - --%>
                                                        
                                                        <td width="6%" nowrap><%=textListOrderItem[SESS_LANGUAGE][12]%></td><%-- + --%>
                                                        <td width="6%" nowrap><%=textListOrderItem[SESS_LANGUAGE][13]%></td><%-- - --%>
                                                    </tr>
                                                    <%
                                                        int start = 0;
                                                        double selisihMinus = 0.0;
                                                        double selisihPlus = 0.0;
                                                        double qtyOpname=0.0;
                                                        double qtySystem=0.0;
                                                        double totQtyLost=0.0;
                                                        double totQtyLostPlus=0.0;
                                                        double totQtyLostMinus=0.0;
                                                        double totSelisih=0.0;
                                                        
                                                        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
                                                        for (int i = 0; i < listMatStockOpnameItem.size(); i++) {
                                                            Vector temp = (Vector) listMatStockOpnameItem.get(i);
                                                            MatStockOpnameItem soItem = (MatStockOpnameItem) temp.get(0);
                                                            Material mat = (Material) temp.get(1);
                                                            Unit unit = (Unit) temp.get(2);
                                                            Category catx = (Category) temp.get(3);
                                                            SubCategory scatx = (SubCategory) temp.get(4);
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
                                                            
                                                            start = start + 1;
                                                            double qtyLost = 0;//(soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();

                                                            if(soItem.getQtySystem()>0 && soItem.getQtySystem() > 0 ){
                                                                qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                                                             }else if (soItem.getQtyOpname()>0 && soItem.getQtySystem()==0){
                                                                qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                                                             }else if (soItem.getQtySystem()>0 && soItem.getQtyOpname()==0){
                                                                qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());    
                                                             }else if (soItem.getQtyOpname()==0 && soItem.getQtySystem()<0){
                                                                qtyLost = (soItem.getQtyOpname() - soItem.getQtySystem() + soItem.getQtySold());
                                                             }else if (soItem.getQtyOpname()>0 && soItem.getQtySystem()<0){
                                                                double balance = 0 - soItem.getQtySystem();
                                                                qtyLost = (soItem.getQtyOpname() + balance + soItem.getQtySold());
                                                             }
                                                            
                                                            qtyOpname = qtyOpname+soItem.getQtyOpname();
                                                            qtySystem=qtySystem+soItem.getQtySystem();
                                                            totQtyLost=totQtyLost+qtyLost;
                                                            
                                                            SrcMaterial srcMaterial = new SrcMaterial();
                                                            Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, mat, mat.getOID());
                                                            if(listTypeHrga != null && listTypeHrga.size()>0){
                                                                for(int k = 0; k<listTypeHrga.size();k++){
                                                                    PriceType pricetype = (PriceType)listTypeHrga.get(k); 
                                                                    for(int j=0;j<listCurrStandardX.size();j++){
                                                                        //Vector temp = (Vector)listCurrStandardX.get(j);
                                                                        Vector tempStand = (Vector)listCurrStandardX.get(j);
                                                                        CurrencyType currx = (CurrencyType)tempStand.get(0);
                                                                        StandartRate standart = (StandartRate)tempStand.get(1);
                                                                        PriceTypeMapping pTypeMapping = null; 
                                                                        if(memberPrice!=null && !memberPrice.isEmpty()){
                                                                           pTypeMapping = (PriceTypeMapping) memberPrice.get(""+pricetype.getOID()+"_"+standart.getOID());
                                                                           soItem.setCost(pTypeMapping.getPrice());
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            
                                                    %>
                                                    <tr class="listgensell"> 
                                                        <td align="center"><%=start%></td>
                                                        <td class="listgensell"><%=mat.getSku()%></td>
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td class="listgensell"><%=mat.getBarCode()%></td>
                                                        <% } %>
                                                        <td class="listgensell"><%=mat.getName()%></td>
                                                        <td align="center" class="listgensell"><%=unit.getCode()%></td>
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td class="listgensell"><%=color.getColorName()%></td>
                                                        <td class="listgensell"><%=masterTypeSize.getMasterName()%></td>
                                                        <% } %>
                                                        <td align="center" class="listgensell"><%=soItem.getQtyOpname()%></td>
                                                        <td align="center" class="listgensell"><%=soItem.getQtySystem()%></td>
                                                        <td align="center" class="listgensell"><%=qtyLost%></td>
                                                        
                                                        <%
                                                            if(qtyLost>0){
                                                                totQtyLostPlus=totQtyLostPlus+qtyLost;
                                                            %>
                                                                <td align="center" class="listgensell"><%=qtyLost%></td>
                                                                <td align="center" class="listgensell">0</td>
                                                            <%}else{
                                                                totQtyLostMinus=totQtyLostMinus+qtyLost;
                                                            %>
                                                                <td align="center" class="listgensell">0</td>
                                                                <td align="center" class="listgensell"><%=qtyLost%></td>
                                                            <%}
                                                        %>
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(soItem.getCost())%></td>
                                                       <!--<td align="center" class="listgensell"><%=Formater.formatNumber(qtyLost * soItem.getCost(), "###,##")%></td>-->
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(qtyLost * soItem.getCost())%></td>
                                                        <%
                                                            double resultNilaiSelisih = qtyLost * soItem.getCost();
                                                            totSelisih=totSelisih+resultNilaiSelisih;
                                                            if (resultNilaiSelisih > 0) {
                                                                selisihPlus = selisihPlus + resultNilaiSelisih;
                                                        %>
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(resultNilaiSelisih)%></td>
                                                        <td align="center" class="listgensell">0</td>
                                                        <%
                                                        } else {
                                                            selisihMinus = selisihMinus + resultNilaiSelisih;
                                                        %>
                                                        <td align="center" class="listgensell">0</td>
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(resultNilaiSelisih)%></td>
                                                        <%}
                                                        %>
                                                    </tr>
                                                    <%}%>
                                                    <tr class="listgensell">
                                                        <% if (useForGreenbowl.equals("1")) { %>
                                                        <td align="right" colspan="7" class="listgensell"><b>Total :</b></td>
                                                        <% } else { %>
                                                        <td align="right" colspan="4" class="listgensell"><b>Total :</b></td>
                                                        <% } %>
                                                        <td align="center" class="listgensell"><%=qtyOpname%></td>
                                                        <td align="center" class="listgensell"><%=qtySystem%></td>
                                                        <td align="center" class="listgensell"></td>
                                                        <td align="center" class="listgensell"><%=totQtyLostPlus%></td>
                                                        <td align="center" class="listgensell"><%=totQtyLostMinus%></td>
                                                        <td align="center" class="listgensell"></td>
                                                        <td align="center" class="listgensell"></td>
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(selisihPlus)%></td>
                                                        <td align="center" class="listgensell"><%=FRMHandler.userFormatStringDecimal(selisihMinus)%></td>
                                                    </tr>
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
                                                    <tr align="right" valign="top"> 
                                                        <%
                                                            try {
                                                        %>
                                                        <td height="22" valign="middle" colspan="3">
                                                            Total (+) : <%=FRMHandler.userFormatStringDecimal(selisihPlus)%>
                                                            &nbsp;&nbsp;Total (-) : <%=FRMHandler.userFormatStringDecimal(selisihMinus)%>

                                                        </td>
                                                        <%
                                                            } catch (Exception e) {
                                                                System.out.println(e);
                                                            }
                                                        %>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="22" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="40" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="34%" align="center" nowrap><%=textListPrintHeader[SESS_LANGUAGE][2]%>,</td>
                                                        <td align="center" valign="bottom" width="33%"><%=textListPrintHeader[SESS_LANGUAGE][3]%>,</td>
                                                        <td width="33%" align="center"><%=textListPrintHeader[SESS_LANGUAGE][4]%>,</td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="75" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="34%" align="center" nowrap> (.................................) 
                                                        </td>
                                                        <td align="center" valign="bottom" width="33%"> (.................................) 
                                                        </td>
                                                        <td width="33%" align="center"> (.................................) 
                                                        </td>
                                                    </tr>
                                                </table></td>
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
        </table>
    </body>
</html>
