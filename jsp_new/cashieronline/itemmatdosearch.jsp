<%@ page import = " com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.posbo.form.warehouse.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*" %>
<%@ page contentType = "text/html" %>

<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<!--%@ include file = "../../../main/checkuser.jsp" %-->

<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Serial Code", "Nama Barang", "QTY", "HARGA"},
        {"Serial Code", "Material Name", "QTY", "PRICE"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"NO", "SERIAL CODE", "NAMA BARANG", "QTY", "HARGA"},
        {"NO", "SERIAL CODE", "NAME PRODUCT", "QTY", "PRICE"}
    };

    public String drawListMaterial(long oidBillMain, int language, Vector objectClass, int start) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "10%");


            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                //Vector vt = (Vector) objectClass.get(i);
                Billdetail billdetail = (Billdetail) objectClass.get(i);
                start = start + 1;

                Vector rowx = new Vector();

                rowx.add("" + start);
                rowx.add(billdetail.getSku());
                rowx.add(billdetail.getItemName());
                rowx.add(""+billdetail.getQty());
                rowx.add(FRMHandler.userFormatStringDecimal(billdetail.getTotalPrice()));

                lstData.add(rowx);

                String name = "";
                name = billdetail.getItemName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');

                double sisaStock = PstBillDetail.getCountStock(billdetail.getBillMainId(), billdetail.getMaterialId());
                Unit unit = new Unit();
                try {
                    unit = PstUnit.fetchExc(billdetail.getUnitId());
                } catch (Exception e) {
                    System.out.println("unit code not found ...");
                }

                lstLinkData.add(name + "','" + billdetail.getUnitId() + "','" + billdetail.getMaterialId() + "','" + billdetail.getSku() + "','" + (billdetail.getQty())
                        + "','" + (billdetail.getItemPrice()) + "','" + (billdetail.getDisc()) + "','" + (billdetail.getDisc1()) + "','" + (billdetail.getDisc2())
                        + "','" + (billdetail.getDiscGlobal()) + "','" + (billdetail.getTotalAmount()) + "','" + sisaStock + "','" + unit.getCode());

            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
        }
        return result;
    }
%>

<!-- JSP Block -->
<%
            long oidBillMain = FRMQueryString.requestLong(request, "bill_main_id");
            int start = FRMQueryString.requestInt(request, "start");
            int iCommand = FRMQueryString.requestCommand(request);
            int recordToGet = 20;
            String orderBy = "";
            String whereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oidBillMain;
            int vectSize = 0;

            vectSize = PstBillDetail.getCount(whereClause);

            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                start = ctrlBillDetail.actionList(iCommand, start, vectSize, recordToGet);
            }

            String pageTitle = "";
            pageTitle = "DAFTAR SERIAL NUMBER BARANG";
            Vector vect = PstBillDetail.list(start, recordToGet, whereClause, orderBy);

%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function addNewItem(){
                document.frmstockcodedosearch.command.value="<%=Command.ADD%>";
                document.frmstockcodedosearch.action="<%=approot%>/master/material/material_main.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdEdit(itemName, unitId, matid, sku, qty, itemprice, disc, disc1, disc2, discGlobal,totalAmount,stockreturn,unitCode){
                self.close();
                //self.opener.document.forms.frmcashier.billdetailTmp.value = stockCode;
                self.opener.document.forms.frmcashier.stock_return.value = stockreturn;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                self.opener.document.forms.frmcashier.matUnit.value = unitCode;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.value = itemName;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>.value = matid;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value = sku;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_UNIT_ID]%>.value = unitId;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>.value = itemprice;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.value = disc;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.value = disc1;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>.value = disc2;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISCON_GLOBAL]%>.value = discGlobal;
                self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.value = totalAmount;


            }

            function cmdConfrmApprove(){
                document.frmstockcodedosearch.command.value="<%=Command.GOTO%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdApproval(){
                self.close();
                self.opener.document.forms.frmcashier.commandDetail.value = "<%=Command.GOTO%>";
                self.opener.document.forms.frmcashier.nota_type.value = <%=PstBillMain.RETUR_ALL%>;
                self.opener.document.forms.frmcashier.submit();
            }


            function cmdListFirst(){
                document.frmstockcodedosearch.command.value="<%=Command.FIRST%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListPrev(){
                document.frmstockcodedosearch.command.value="<%=Command.PREV%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListNext(){
                document.frmstockcodedosearch.command.value="<%=Command.NEXT%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListLast(){
                document.frmstockcodedosearch.command.value="<%=Command.LAST%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdSearch(){
                document.frmstockcodedosearch.start.value="0";
                document.frmstockcodedosearch.command.value="<%=Command.LIST%>";
                document.frmstockcodedosearch.action="itemmatdosearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function clear(){
                document.frmstockcodedosearch.txt_materialcode.value="";
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
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <script language="JavaScript">
                window.focus();
        </script>
        <table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
            <tr><%@include file="../styletemplate/template_header_empty.jsp" %>
                <td width="88%" valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <hr size="1">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form name="frmstockcodedosearch" method="post" action="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="bill_main_id" value="<%=oidBillMain%>">
                                    <input type="hidden" name="source_link2" value="itemmatdosearch.jsp">
                                    <table width="100%" border="0" cellspacing="1" cellpadding="1">


                                        <tr>
                                            <td colspan="2"><%=drawListMaterial(oidBillMain, SESS_LANGUAGE, vect, start)%></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><span class="command">
                                                    <%
                                                                ControlLine ctrlLine = new ControlLine();
                                                    %>
                                                    <%
                                                                ctrlLine.setLocationImg(approot + "/images");
                                                                ctrlLine.initDefault();
                                                    %>
                                                    <%=ctrlLine.drawImageListLimit(iCommand, vectSize, start, recordToGet)%> </span></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <span class="command">
                                                    <input type="button" name="Button" value="Retur Semua Item" onClick="javascript:cmdConfrmApprove()" class="formElemen">
                                                    <!--a href="javascript:cmdApproval(2)" onKeyDown="javascript:keybrdPress(this)"><span >SAVE</span></a-->
                                                </span>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <%
                if (iCommand == Command.GOTO) {
                    int itemReadyStock = 0;
                    for (int i = 0; i < vect.size(); i++) {
                        Vector temp = (Vector) vect.get(i);
                        Billdetail billdetail = (Billdetail) temp.get(0);

                        double stockReturn = PstBillDetail.getCountStock(oidBillMain, billdetail.getMaterialId());
                        if (stockReturn > 0) {
                            itemReadyStock++;
                        }
                    }
                    if (itemReadyStock == 0) {
    %>
    <script type="text/javascript">
         alert("All item is already returned !");

    </script>
    <%} else {%>
        <script type="text/javascript">
         cmdApproval();

    </script>
    <%}
                            }%>

</html>
