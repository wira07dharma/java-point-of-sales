
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page language = "java"  %>
<%@ page import = " java.util.* ,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
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
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.pos.entity.payment.CashPayments1,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*,
         com.dimata.pos.entity.search.*,
         com.dimata.pos.form.search.*,
         com.dimata.pos.session.billing.*"%>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../main/javainit.jsp" %>

<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;
    public static final String textMainHeader[][] = {
        {"List", "Sales Order"},
        {"List", "Sales Order"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"No", "No. Invoice", "Tanggal", "Nama Customer", "Alamat", "Telepon/Fax", "Email", "Mata Uang", "Total", "Paid", "Sales Person", "Status", "detail","Status Invoicing","Type Sales Order"},
        {"No", "No. Invoice", "Date", "Customer Name", "Address", "Telepon/Fax", "Email", "Currency", "Total", "Paid", "Sales Person", "Status", "detail","Status Invoicing","Type Sales Order"}
    };

    public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][10], "7%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][7], "5%");
            ctrlist.addHeader(textListMaterialHeader[language][8], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][13], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][14], "10%");
             
            ctrlist.setLinkRow(0);

            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit(");
            ctrlist.setLinkSufix(")");
            ctrlist.reset();
            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                BillMain billMain = (BillMain) vt.get(0);
                MemberReg memberReg = (MemberReg) vt.get(1);
                CurrencyType currencyType = (CurrencyType) vt.get(2);
                AppUser appUser = (AppUser) vt.get(3);
                Sales saler = (Sales) vt.get(4);
                AppUser ap = new AppUser();
                try {
                    ap = PstAppUser.fetch(billMain.getAppUserSalesId());
                  } catch (Exception e) {
                  }

                String cntName = memberReg.getCompName();
                if (cntName.length() == 0) {
                    cntName = String.valueOf(memberReg.getPersonName());
                }
                start = start + 1;

                Vector rowx = new Vector();
                rowx.add("" + start);

                String str_dt_BillDate = "";
                try {
                    Date dt_BillDate = billMain.getBillDate();
                    if (dt_BillDate == null) {
                        dt_BillDate = new Date();
                    }
                    str_dt_BillDate = Formater.formatDate(dt_BillDate, "dd-MM-yyyy HH:mm:ss");
                } catch (Exception e) {
                    str_dt_BillDate = "";
                }
                String salesName="";
                try {
                    salesName = ap.getFullName(); //PstSales.getNameSales(billMain.getSalesCode());
//                    salesName = saler.getName(); //PstSales.getNameSales(billMain.getSalesCode());
                } catch (Exception e) {
                    salesName="";
                }
                rowx.add("<div align=\"left\">"+salesName+"</div>");
                rowx.add("<div align=\"center\">"+str_dt_BillDate+"</div>");//
                rowx.add("<div align=\"right\">"+cntName+"</div>");//
                
                String address = "";
                if(memberReg.getBussAddress()==""){
                    address = memberReg.getHomeAddr();
                }else{
                    address = memberReg.getBussAddress();
                }
                rowx.add("<div align=\"right\">"+address+"</div>");

                String tlp = "";
                if(memberReg.getFax()==""){
                    tlp = memberReg.getHomeTelp();
                }else{
                    tlp = memberReg.getFax();
                }
                rowx.add("<div align=\"right\">"+tlp+"</div>");

                CurrencyType currType = new CurrencyType();
                try {
                    currType = PstCurrencyType.fetchExc(billMain.getCurrencyId());
                } catch (Exception e) {
                }
                rowx.add("<div align=\"center\">"+currType.getCode()+"</div>");
                double bruto = 0;
                bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
                
                if(billMain.getStatusInv()==1){
                    rowx.add("<div align=\"center\">On Prosess</div>");
                }else if (billMain.getStatusInv()==2){
                    rowx.add("<div align=\"center\">Done</div>");
                }else{
                    rowx.add("<div align=\"center\">Draft</div>");
                }

                if( billMain.getTypeSalesOrder()==1){
                    rowx.add("<div align=\"center\">Incoming Sales Order</div>");
                }else{
                    rowx.add("<div align=\"center\">Sales Order</div>");
                }


                lstData.add(rowx);
                lstLinkData.add("'" + String.valueOf(billMain.getOID())+ "','" + billMain.getTypeSalesOrder()+ "'");
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data sales order...</div>";
        }
        return result;
    }
%>

<%
            /**
             * get approval status for create document
             */
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
            I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
            I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
            int systemName = I_DocType.SYSTEM_MATERIAL;
            int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>

<%
    Date today = new Date();  
    String klik = FRMQueryString.requestString(request, "klik");

            String poCode = "PO";
            String poTitle = textMainHeader[SESS_LANGUAGE][1];
            String poItemTitle = poTitle + " Item";
            String poTitleBlank = "";

            long oidBillMain = FRMQueryString.requestLong(request, "oidbillmaintmp");
            String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");

            int iErrCode = FRMMessage.ERR_NONE;
            String msgStr = "";
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int recordToGet = 20;
            int vectSize = 0;
            String whereClause = "";

            ControlLine ctrLine = new ControlLine();
            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            SrcInvoice srcInvoice = new SrcInvoice();
            AppUser ap = new AppUser();
            try {
                ap = PstAppUser.fetch(srcInvoice.getSalesId());
              } catch (Exception e) {
              }

            SessSales sessSales = new SessSales();
            

    
            FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice(request, srcInvoice);///
            frmSrcInvoice.requestEntityObject(srcInvoice);
                
            if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                try {
                    srcInvoice = (SrcInvoice) session.getValue(SessSales.SESS_SRC_ORDERMATERIAL);
                    if (srcInvoice == null) {
                        srcInvoice = new SrcInvoice();
                    }
                } catch (Exception e) {
                    System.out.println(" Session null : " + e);
                    srcInvoice = new SrcInvoice();
                }
            } else {
                try {
                    srcInvoice = (SrcInvoice) session.getValue(SessSales.SESS_SRC_ORDERMATERIAL);
                } catch (Exception e) {
                    srcInvoice = new SrcInvoice();
                }

                if (srcInvoice == null) {
                    srcInvoice = new SrcInvoice();
                }

                try {
                    session.removeValue(SessSales.SESS_SRC_ORDERMATERIAL);
                } catch (Exception e) {
                }

                frmSrcInvoice.requestEntityObject(srcInvoice);
                Vector vectStCurr = new Vector(1, 1);

                String[] strStatusCurr = request.getParameterValues(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CURRENCY_ID]);
                if (strStatusCurr != null && strStatusCurr.length > 0) {
                    for (int i = 0; i < strStatusCurr.length; i++) {
                        try {
                            vectStCurr.add(strStatusCurr[i]);
                        } catch (Exception exc) {
                            System.out.println("err");
                        }
                    }
                }
                srcInvoice.setPrmstatusCurr(vectStCurr);
                session.putValue(SessSales.SESS_SRC_ORDERMATERIAL, srcInvoice);
            }

            vectSize = sessSales.getCount(srcInvoice, docType);
            if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }

            //Vector records = sessSales.searchSalesOrder(srcInvoice, docType, start, recordToGet);
            Vector records = sessSales.getList(srcInvoice, docType);
%>

<%
        int appObjCodeSalesOrder = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_ORDER);
        int appObjCodeSalesRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_RETUR);
        int appObjCodeSalesInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_INVOICE);
%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalSalesOrder = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesOrder, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesRetur = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesRetur, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesInvoice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesInvoice, AppObjInfo.COMMAND_VIEW));
%>
<%!    

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Jenis Invoice", "Tanggal", "Nomor Order", "Sales", "Sales Code", "Sales Order", "Nama Barang", "Semua Tanggal", "Dari", "s/d", "Nama Customer", "Kode Barang", "Person Name", "Tipe Nota", "Tipe Penjualan", "Mata Uang","Urut Berdasarkan"},//16
        {"Invoice Type", "Date", "Order No. ", "Sales", "Sales Code", "Sales Order", "Material Name", "All Date", "From", "To", "Customer Name", "Material Code", "Person Name", "Nota Type", "Sales type", "Currency","Sort By"}
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

    public boolean getTruedFalseCurr(Vector vect, long index) {
        for (int i = 0; i < vect.size(); i++) {
            long iStatusCurr = Long.valueOf((String) vect.get(i));
            if (iStatusCurr == index) {
                return true;
            }
        }
        return false;
    }
%>
<!-- Jsp Block -->
<%
        /*
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
            I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
            I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
            int systemName = I_DocType.SYSTEM_MATERIAL;
            int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
            */ 
%>
<%
            //int iCommand = FRMQueryString.requestCommand(request);
            int NotaSales = FRMQueryString.requestInt(request, "notasalestype");
            int idSaleType = FRMQueryString.requestInt(request, "transType");
            /*
            String poCode = "PO"; //i_pstDocType.getDocCode(docType);
            String poTitle = textListHeader[SESS_LANGUAGE][5];
            String poItemTitle = poTitle + " Item";
            String poTitleBlank = "";
            */
            /**
             * ControlLine
             */
            //ControlLine ctrLine = new ControlLine();

            BillMain bmain = new BillMain();

            //SrcInvoice srcInvoice = new SrcInvoice();
            //FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice();//
            FrmBillMain frmBillMain = new FrmBillMain();

            //String salesCode = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]);

            //int transStatus = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_STATUS]);
            //int transType = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_TYPE]);
            int doctype = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DOC_TYPE]);
            if (srcInvoice.getInvoiceDate()==null){
                srcInvoice.setInvoiceDate(today);
                srcInvoice.setInvoiceDateTo(today);
            }         
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdAdd(){
                var salesCode = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>.value;
                document.frmsrcsalesorder.command.value="<%=Command.ADD%>";
                document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>.value = salesCode;
                document.frmsrcsalesorder.approval_command.value="<%=Command.SAVE%>";
                document.frmsrcsalesorder.add_type.value="<%=ADD_TYPE_SEARCH%>";
                document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>.value="0";
                document.frmsrcsalesorder.action="outlet_cashier.jsp";
                if(compareDateForAdd()==true)
                    document.frmsrcsalesorder.submit();
            }
            
            function cmdEdit(oid,typeSalesOrder){
                document.frmsrcsalesorder.start.value=0;
                document.frmsrcsalesorder.oidbillmaintmp.value = oid;
                document.frmsrcsalesorder.commandDetail.value="<%=Command.LIST%>";
                document.frmsrcsalesorder.command.value="<%=Command.EDIT%>";
                document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>.value=typeSalesOrder;
                document.frmsrcsalesorder.action="outlet_cashier.jsp";

                document.frmsrcsalesorder.submit();

            }
            

            function cmdAddIncoming(){
                var salesCode = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>.value;
                document.frmsrcsalesorder.command.value="<%=Command.ADD%>";
                document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>.value = salesCode;
                document.frmsrcsalesorder.approval_command.value="<%=Command.SAVE%>";
                document.frmsrcsalesorder.add_type.value="<%=ADD_TYPE_SEARCH%>";
                document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>.value="1";
                document.frmsrcsalesorder.action="outlet_cashier.jsp";
                if(compareDateForAdd()==true)
                    document.frmsrcsalesorder.submit();
            }

            function cmdSearch(){
                var salesCode = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>.value;
                document.frmsrcsalesorder.command.value="<%=Command.LIST%>";
                document.frmsrcsalesorder.FRM_FIELD_SALES_CODE.value=salesCode;
                document.frmsrcsalesorder.action="";
                document.frmsrcsalesorder.submit();
            }

            function goDocType(){
                var notaType = document.frmsrcsalesorder.notasalestype.value;
                var docType ="";

                if(notaType == 4){
                    docType = 0;
                }else if(notaType == 2){
                    docType = 1;
                }
                document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DOC_TYPE]%>.value = docType;
                document.frmsrcsalesorder.submit();
            }

            function goTransType(){
                document.frmsrcsalesorder.submit();
            }

            function keybrdPressSearch(frmObj, event,value){
                if(event.keyCode == 13) { //enter
                   switch(frmObj.name){
                      case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>':
                            document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>.focus();
                            break;
                      case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>':
                                document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>.focus();
                                break;
                      case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>':
                             document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_PERSON]%>.focus();
                            break;
                      default:
                            alert("F1 = Pencarian, F2 = Tambah Sales Order, F8 = Tambah Incoming Sales Order ");
                            break;
                   }
                }else if (event.keyCode==27){ //esc
                    switch(frmObj.name){
                         case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>':
                                break;
                         case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>':
                                document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>.focus();
                                break;
                         case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>':
                                document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>.focus();
                                break;
                         case '<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_PERSON]%>':
                                document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>.focus();
                                break;
                         default:
                               break;
                    }
                }else if (event.keyCode==113){ //F2
                    cmdAdd(); 
                }else if (event.keyCode==112){ //F1
                    cmdSearch();
                }else if (event.keyCode==119){ //F8
                    cmdAddIncoming();
                }
            }


            function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                        if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                }

                function MM_findObj(n, d) { //v4.0
                    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                    if(!x && document.getElementById) x=document.getElementById(n); return x;
                }

                function MM_swapImage() { //v3.0
                    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                        if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                }
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <script language=JavaScript>

                function hideObjectForMarketing(){
                }

                function hideObjectForWarehouse(){
                }

                function hideObjectForProduction(){
                }

                function hideObjectForPurchasing(){
                }

                function hideObjectForAccounting(){
                }

                function hideObjectForHRD(){
                }

                function hideObjectForGallery(){
                }

                function hideObjectForMasterData(){
                }

        </script>
        <!-- #EndEditable -->
        <style type="text/css">
            #tdS {
                padding: 3px 14px;
                border-bottom: 1px solid #eee;
                border-collapse: collapse;
                background-color: #fff;
            }
            #tdS1 {
                padding: 3px 14px;
                border-left: 1px solid #eee;
                border-bottom: 1px solid #eee;
                border-collapse: collapse;
                background-color: #fff;
            }
        </style>
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#E1CA9F">
                        <tr>
                            <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
                        </tr>
                        <tr>
                            <td nowrap>
                                <div align="center"> <a href="../logout.jsp">  Logout </a> </div>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
                        </tr>
                    </table>
                    <!--%@ include file = "../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <div class="mainheader"><%=textListHeader[SESS_LANGUAGE][5]%></div>
                    <form name="frmsrcsalesorder" method="post" action="">
                        <input type="hidden" name="start" value="" />
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="klik" value="1" />
                        <input type="hidden" name="oidbillmaintmp" value="" />
                        <input type="hidden" name="commandDetail" value="" />
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=salesCode%>">
                        <input type="hidden" name="add_type" value="">
                        <input type="hidden" name="approval_command">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>" value="">
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <td id="tdS"><%=getJspTitle(13, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS">
                                    <%
                                        Vector val_notatype = new Vector(1, 1);
                                        Vector key_notatype = new Vector(1, 1);
                                        if (privApprovalSalesOrder || salesCode != "") {
                                            val_notatype.add("" + PstBillMain.SALES_ORDER);
                                            key_notatype.add("Sales Order");
                                        }
                                        if (privApprovalSalesRetur) {
                                            val_notatype.add("" + PstBillMain.RETUR);
                                            key_notatype.add("Sales Retur");
                                        }
                                        String NotaSalesCode = "" + NotaSales;

                                    %>
                                    <%=ControlCombo.draw("notasalestype", null, (NotaSales == 0) ? NotaSalesCode : "" + NotaSales, val_notatype, key_notatype, "onChange=\"javascript:goDocType()\"", "formElemen")%>
                                    <input type="hidden" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DOC_TYPE]%>"  value="<%=doctype%>">
                                </td>
                                <td id="tdS1"><%=getJspTitle(10, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS">

                                    <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>"  value="<%= srcInvoice.getCustomerName()%>" class="formElemen" size="20" onKeyDown="keybrdPressSearch(this, event, this.value)">

                                </td>
                            </tr>
                            <tr>
                                <td id="tdS"><%=getJspTitle(4, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS"><input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_CODE]%>"  value="<%= ap.getFullName()%>" class="formElemen" size="20" onKeyDown="keybrdPressSearch(this, event, this.value)"></td>
                                <td id="tdS1">Sales Name</td>
                                <td id="tdS">:</td>
                                <td id="tdS"><input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_PERSON]%>"  value="<%= srcInvoice.getSalesPerson()%>" class="formElemen" size="20" onKeyDown="keybrdPressSearch(this, event, this.value)"></td>
                            </tr>
                            <tr>
                                <td id="tdS"><%=getJspTitle(1, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS">
                                    <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" <%if (srcInvoice.getStatusDate() == 0) {%>checked<%}%> value="0" >
                                    <%=textListHeader[SESS_LANGUAGE][7]%>
                                    <div>
                                        <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" <%if (srcInvoice.getStatusDate() == 1) {%>checked<%}%> value="1">
                                        <%=textListHeader[SESS_LANGUAGE][8]%> <%=ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE], srcInvoice.getInvoiceDate(), "formElemen", 1, -5)%>  &nbsp;<%=textListHeader[SESS_LANGUAGE][9]%>&nbsp; <%=	ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE_TO], srcInvoice.getInvoiceDateTo(), "formElemen", 1, -5)%> 
                                    </div>
                                </td>
                                <td id="tdS1"><%=getJspTitle(15, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS">
                                    <%
                                        Vector vectCurr = PstCurrencyType.getCurr();
                                        for (int i = 0; i < vectCurr.size(); i++) {

                                            Vector vetTemp = (Vector) vectCurr.get(i);
                                            long indexCurr = Long.parseLong(String.valueOf(vetTemp.get(0)));
                                            String strCurr = String.valueOf(vetTemp.get(1));
                                    %>
                                    <input type="checkbox" class="formElemen" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CURRENCY_ID]%>" value="<%=(indexCurr)%>" <%if (getTruedFalseCurr(srcInvoice.getPrmstatusCurr(), indexCurr)) {%>checked<%}%>>
                                    <%=strCurr%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%

                                        }
                                    %>
                                </td>
                            </tr>
                            <tr>
                                <td id="tdS"><%=getJspTitle(16, SESS_LANGUAGE, poCode, false)%></td>
                                <td id="tdS">:</td>
                                <td id="tdS">

                                    <%
                                        Vector key_sort = new Vector(1, 1);
                                        Vector val_sort = new Vector(1, 1);

                                        key_sort.add("0");
                                        val_sort.add("No. Invoice");

                                        key_sort.add("1");
                                        val_sort.add("Tanggal");

                                        key_sort.add("2");
                                        val_sort.add("Nama Customer");

                                        String select_sort = "" + srcInvoice.getSortBy();
                                        out.println("&nbsp;" + ControlCombo.draw(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SORTBY], null, select_sort, key_sort, val_sort, "", "formElemen"));
                                    %>

                                </td>
                                <td id="tdS">&nbsp;</td>
                                <td id="tdS">&nbsp;</td>
                                <td id="tdS">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdS" colspan="2">
                                    <a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%>"></a>
                                    <a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%></a>
                                </td>
                                <td id="tdS" colspan="4">
                                    <a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ADD, true)%>"></a>
                                    <a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ADD, true)%></a>
                                </td>
                                
                                
                            </tr>
                        </table>

                    </form> 
                    <%
                        if(iCommand == Command.LIST){
                    %>
                        <%=drawList(SESS_LANGUAGE, records, start, docType, i_status)%>
                    <%
                        }
                    %>
                    <script type="javascript">
                         document.frmsrcsalesorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>.focus();
                    </script>
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
               <!--                       
            <tr><td><table>
                        <tr>
                            <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                            <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                            <td height="21" width="90%" valign="top" align="left">
                                <fieldset>
                                    <legend >
                                        <span class="style1"><h2>Kunci Bantuan</h2></span>
                                    </legend>
                                    <h3>F1 = Pencarian &nbsp;&nbsp; F2 = Tambah Sales Order &nbsp;&nbsp; F8 = Tambah Incoming Sales Order </h3>
                                  </fieldset>
                            </td>
                        </tr>
                    </table></td></tr>
            
            -->

            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
