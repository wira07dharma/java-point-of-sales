
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
<!-- % int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CASHIER); %>
<!--%@ include file = "../main/checkuser.jsp" %>

<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Jenis Invoice", "Tanggal", "Nomor Invoice", "Sales", "Sales Person", "Order", "Nama Barang", "Semua Tanggal", "Dari", "s/d", "Nama Customer", "Kode Barang", "Person Name", "Tipe Nota", "Tipe Penjualan", "Mata Uang"},//16
        {"Invoice Type", "Date", "Invoice No. ", "Sales", "Sales Person", "Order", "Material Name", "All Date", "From", "To", "Customer Name", "Material Code", "Person Name", "Nota Type", "Sales type", "Currency"}
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
            /**
             * get approval status for create document
             */
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
            I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
            I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
            int systemName = I_DocType.SYSTEM_MATERIAL;
            int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>s
<%
            int iCommand = FRMQueryString.requestCommand(request);
            int NotaSales = FRMQueryString.requestInt(request, "notasalestype");
            int idSaleType = FRMQueryString.requestInt(request, "transType");

            String poCode = "PO"; //i_pstDocType.getDocCode(docType);
            String poTitle = textListHeader[SESS_LANGUAGE][5];
            String poItemTitle = poTitle + " Item";
            String poTitleBlank = "";

            /**
             * ControlLine
             */
            ControlLine ctrLine = new ControlLine();

            BillMain bmain = new BillMain();

            SrcInvoice srcInvoice = new SrcInvoice();
            FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice();
            FrmBillMain frmBillMain = new FrmBillMain();

            //int transStatus = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_STATUS]);
            //int transType = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_TYPE]);
            int doctype = FRMQueryString.requestInt(request, frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DOC_TYPE]);
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
                document.frmdlvrorder.command.value="<%=Command.ADD%>";
                document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>.value = <%=PstBillMain.DISC_TYPE_PCT%>;
                document.frmdlvrorder.approval_command.value="<%=Command.SAVE%>";
                document.frmdlvrorder.add_type.value="<%=ADD_TYPE_SEARCH%>";
                document.frmdlvrorder.action="outlet_cashier.jsp";
                if(compareDateForAdd()==true)
                    document.frmdlvrorder.submit();
            }

            function cmdSearch(){
                document.frmdlvrorder.command.value="<%=Command.LIST%>";
                document.frmdlvrorder.action="deliveryOrder_list.jsp";
                document.frmdlvrorder.submit();
            }

            function goDocType(){
                var notaType = document.frmdlvrorder.notasalestype.value;
                var docType ="";

                if(notaType == 4){
                    docType = 0;
                }else if(notaType == 2){
                    docType = 1;
                }
                document.frmdlvrorder.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DOC_TYPE]%>.value = docType;
                document.frmdlvrorder.submit();
            }

            function goTransType(){
                document.frmdlvrorder.submit();
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
        <SCRIPT language=JavaScript>

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

        </SCRIPT>
        <!-- #EndEditable -->
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
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListHeader[SESS_LANGUAGE][5]%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmdlvrorder" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>">
                                    <input type="hidden" name="add_type" value="">
                                    <input type="hidden" name="approval_command">

                                    <table width="100%" border="0">
                                        <tr>
                                            <td colspan="2">&nbsp;
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                   
                                                    <tr>
                                                        <td height="21" width="9%"><%=getJspTitle(14, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="90%">&nbsp;
                                                            <%
                                                                        Vector val_saletype = new Vector(1, 1);
                                                                        Vector key_saletype = new Vector(1, 1);

                                                                        val_saletype.add("0");
                                                                        key_saletype.add("Cash Invoice");
                                                                        val_saletype.add("1");
                                                                        key_saletype.add("Credit Invoice");
                                                                        

                                                                        String select_saletype = "" + idSaleType;
                                                                        int transstatus = 0;
                                                                        int transtype = 0;
                                                                        if (idSaleType == 0) {
                                                                            transstatus = 0;
                                                                            transtype = 0;
                                                                        } else if (idSaleType == 1) {
                                                                            transtype = 1;
                                                                            transstatus = -1;
                                                                        } else {
                                                                            transstatus = 1;
                                                                            transtype = 0;
                                                                        }

                                                            %>
                                                            <%=ControlCombo.draw("transType", null, (idSaleType == 0) ? select_saletype : "" + idSaleType, val_saletype, key_saletype, "onChange=\"javascript:goTransType()\"", "formElemen")%>

                                                            <input type="hidden" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_STATUS]%>"  value="<%=transstatus%>">
                                                            <input type="hidden" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_TRANSACTION_TYPE]%>"  value="<%=transtype%>">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="9%"><%=getJspTitle(2, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="90%">&nbsp;
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_NUMBER]%>"  value="<%= srcInvoice.getInvoiceNumber()%>" class="formElemen" size="20">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="9%"><%=getJspTitle(10, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="90%">&nbsp;
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>"  value="<%= srcInvoice.getCustomerName()%>" class="formElemen" size="20">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="9%"><%=getJspTitle(12, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="90%">&nbsp;
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_MEMBER_NAME]%>"  value="<%= srcInvoice.getMemberName()%>" class="formElemen" size="20">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="9%"><%=getJspTitle(4, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="90%">&nbsp;
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_PERSON]%>"  value="<%= srcInvoice.getSalesPerson()%>" class="formElemen" size="20">
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td height="43" rowspan="2" valign="top" width="9%" align="left"><%=getJspTitle(1, SESS_LANGUAGE, poCode, false)%></td>
                                                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                                                        <td height="21" width="90%" valign="top" align="left">
                                                            <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" <%if (srcInvoice.getStatusDate() == 0) {%>checked<%}%> value="0">
                                                            <%=textListHeader[SESS_LANGUAGE][7]%></td>
                                                    </tr>
                                                    <tr align="left">
                                                        <td height="21" width="90%" valign="top">
                                                            <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" <%if (srcInvoice.getStatusDate() == 1) {%>checked<%}%> value="1">
                                                            <%=textListHeader[SESS_LANGUAGE][8]%> <%=ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE], srcInvoice.getInvoiceDate(), "formElemen", 1, -5)%>  &nbsp;<%=textListHeader[SESS_LANGUAGE][9]%>&nbsp; <%=	ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE_TO], srcInvoice.getInvoiceDateTo(), "formElemen", 1, -5)%> </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="top" width="9%" align="left"><%=getJspTitle(15,SESS_LANGUAGE,poCode,false)%></td>
                                                        <td height="21" valign="top" width="1%" align="left"></td>
                                                        <td height="21" width="90%" valign="top" align="left">
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
                                                        <td height="21" valign="top" width="9%" align="left"><!--%=getJspTitle(3,SESS_LANGUAGE,poCode,false)%--></td>
                                                        <td height="21" valign="top" width="1%" align="left"></td>
                                                        <td height="21" width="90%" valign="top" align="left">
                                                            <!--%
						  Vector vectResult = i_status.getDocStatusFor(docType);
						  for(int i=0; i<vectResult.size(); i++){
						  	if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED)|| (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL))
							{
								Vector vetTemp = (Vector)vectResult.get(i);
								int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
								String strPrStatus = String.valueOf(vetTemp.get(1));
							  %>
							  <input type="checkbox" class="formElemen" name="<!--%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_PRMSTATUS]%>" value="<!--%=(indexPrStatus)%>" <!--%if(getTruedFalse(srcInvoice.getPrmstatus(),indexPrStatus)){%>checked<!--%}%>>
							  <!--%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							  <!--%
							}
						  }
						  %-->
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                                                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                                                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                                                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                                                        <td height="21" width="90%" valign="top" align="left">
                                                            <table width="47%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                    <td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%>"></a></td>
                                                                    <td class="command" nowrap width="39%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SEARCH, true)%></a></td>
                                                                    <!--% if(privAdd){%-->
                                                                   <!--%}%-->
                                                                </tr>
                                                            </table>
                                                        </td>
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
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
