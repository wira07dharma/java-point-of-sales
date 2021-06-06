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
         com.dimata.posbo.session.warehouse.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location" %>
<%@ page contentType = "text/html" %>

<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<!--%@ include file = "../../../main/checkuser.jsp" %-->

<%!    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"N0.", "SKU", "Nama Barang", "Unit", "Qty", "Harga", "Gudang", "QTY on Order", "Total Qty Outstanding"},
        {"No.", "SKU", "Material Name", "Unit", "Qty", "Price", "Warehouse", "QTY on Order","Total Qty Outstanding"}
    };

    public String drawListMaterial(long oidBillMain, int language, Vector objectClass, int start, Vector vectLocWh) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");

            ctrlist.dataFormat(textListMaterialHeader[language][0], "3%", "2", "", "center", "center");
            ctrlist.dataFormat(textListMaterialHeader[language][1], "10%", "2", "0", "center", "midle");
            ctrlist.dataFormat(textListMaterialHeader[language][2], "15%", "2", "0", "center", "midle");
            ctrlist.dataFormat(textListMaterialHeader[language][3], "5%", "2", "0", "center", "midle");
            ctrlist.dataFormat(textListMaterialHeader[language][4], "5%", "2", "0", "center", "midle");
            ctrlist.dataFormat(textListMaterialHeader[language][7], "5%", "2", "0", "center", "midle");
            ctrlist.dataFormat(textListMaterialHeader[language][8], "5%", "2", "0", "center", "midle");
            //ctrlist.dataFormat(textListMaterialHeader[language][6],""+(5*vectLocWh.size())+"%","0",""+(vectLocWh.size()+1)+"","center","center");

            // vector untuk lokasi stock
            if (vectLocWh != null && vectLocWh.size() > 0) {
                for (int k = 0; k < vectLocWh.size(); k++) {
                    Location location = (Location) vectLocWh.get(k);
                    ctrlist.dataFormat(location.getName(), "10%", "0", "2", "center", "center");
                }
            }

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            //ctrlist.setLinkPrefix("javascript:cmdEdit('");
            //ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                Billdetail billdetail = (Billdetail) vt.get(0);
                start = start + 1;

                Vector rowx = new Vector();
                //double sisaStock = PstBillDetail.getCountStock(billdetail.getBillMainId(), billdetail.getMaterialId());
                Unit unit = new Unit();
                try {
                    unit = PstUnit.fetchExc(billdetail.getUnitId());
                } catch (Exception e) {
                    System.out.println("unit code not found ...");
                }

                rowx.add("" + start);
                rowx.add(billdetail.getSku() + "<input type=\"hidden\" name=\"matid\" value=\"" + billdetail.getMaterialId() + "\">");
                rowx.add(billdetail.getItemName());
                rowx.add(unit.getCode() + "<input type=\"hidden\" name=\"unitid\" value=\"" + unit.getOID() + "\">");
                rowx.add("" + billdetail.getQty());
                
                //cek dispatch
                double qtyOrder=0;
                double totalQty=0;
                try {
                    /*chek qty order adalah qty yang sudah di buatkan invoicing*/
                    //qtyOrder = PstMatDispatch.getQtyOrder(oidBillMain,  billdetail.getMaterialId());
                    qtyOrder = PstBillMain.getStockOrder(oidBillMain,  billdetail.getMaterialId());
                } catch (Exception e) {
                    System.out.println(" qtyOrder ");
                }
                rowx.add(""+qtyOrder);
                
                //total
                totalQty=billdetail.getQty()-qtyOrder;
                rowx.add(""+totalQty);
                double stockWh = 0;
                if (vectLocWh != null && vectLocWh.size() > 0) {
                    for (int k = 0; k < vectLocWh.size(); k++) {
                        Location location = (Location) vectLocWh.get(k);
                        stockWh = PstMaterialStock.getStockWh(location.getOID(), billdetail.getMaterialId()); 
                        rowx.add("<div align=\"center\">" + stockWh
                                + "<input tabindex=\"33\" type=\"text\" size=\"3\"  name=\"matqty[" + k + "]\" value=\"\" class=\"formElemenR\" style=\"text-align:right\" onchange =\"checkValue(this,"+billdetail.getQty()+","+stockWh+","+k+")\">"
                                + "</div>");
                    }
                }


                lstData.add(rowx);

                String name = "";
                name = billdetail.getItemName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');
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
            long oidBillMain = FRMQueryString.requestLong(request, "bill_main_id_old");
            long bill_main_id_new = FRMQueryString.requestLong(request, "bill_main_id_new");
            long oidLocation = FRMQueryString.requestLong(request, "location_id");
            long hiddenOidSalesOrderBillMain =  FRMQueryString.requestLong(request, "hidden_bill_main_id");
            long warehouseSalesId= FRMQueryString.requestLong(request, "warehouseSales");
            
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
            pageTitle = "DAFTAR BARANG";
            Vector vect = PstBillDetail.listDetailInvoicing(start, recordToGet, whereClause, orderBy);
            String whereLoc = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+warehouseSalesId+"'";
            Vector vectLocWer = PstLocation.list(0, 0,whereLoc, "");

            Vector listBillDetail = new Vector();
            int recordToGetDetail = 1000;
            String whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;
            listBillDetail = PstBillDetail.listMat(start, recordToGetDetail, whereClauseDetail, "");

            if (iCommand == Command.GOTO) {
                //untuk insert bill detail dan check list
                boolean qtyMatch = true;
                for (int i = 0; i < listBillDetail.size(); i++) {
                    Vector temp = (Vector) listBillDetail.get(i);
                    Billdetail billdetail = (Billdetail) temp.get(0);
                    double stockQty = 0;
                    double stockAvailable = 0;
                    for (int j = 0; j < vectLocWer.size(); j++) {
                        String qtyAvailable[] = request.getParameterValues("matqty[" + j + "]");
                        try{
                            stockQty = Double.parseDouble(qtyAvailable[i]);
                        }catch(Exception e){
                            stockQty =0;
                        }
                        double stockTmp = stockAvailable;
                        stockAvailable = stockTmp + stockQty;
                    }
                    
                    //cek apakah
                    double qtyOrderSales=0;
                    double qtyOrder=0;
                    double qtySisa=0;
                    try {
                         qtyOrderSales = PstBillMain.getStockOrderSales(oidBillMain,  billdetail.getMaterialId());
                         qtyOrder = PstBillMain.getStockOrder(oidBillMain,  billdetail.getMaterialId());
                         qtySisa=qtyOrderSales-(qtyOrder+stockQty);
                    } catch (Exception e) {
                        System.out.println(" qtyOrder ");
                    }
                    
                    if(qtySisa>0){
                        qtyMatch=false;
                    }
                    
                    double disc1 = (billdetail.getItemPrice()) * (billdetail.getDisc1()) / 100;
                    double discPrice1 = (billdetail.getItemPrice()) - disc1;
                    double disc2 = discPrice1 * (billdetail.getDisc2()) / 100;
                    double discPrice2 = discPrice1 - disc2;
                    double netPrice = discPrice2 - billdetail.getDisc();
                    double totalDisc = (billdetail.getItemPrice()) - netPrice;
                    double totalPrice = stockAvailable * netPrice;
                    if(stockAvailable>0){
                        billdetail.setBillMainId(bill_main_id_new);
                        billdetail.setQty(stockAvailable);
                        billdetail.setTotalDisc(totalDisc);
                        billdetail.setTotalPrice(totalPrice);
                        long oid = PstBillDetail.insertExc(billdetail);
                    }
                }

                //lakukan pengecekan statusnya..
                //apakah semua data sudah di buatkan document distcpatch atau belum
                //jika qtyMatch = true, maka update cash bill main sales order menjadi Done (doc_type=0, Transaction_type=0, Transaction_status=4)
                if(qtyMatch){
                    long oid = PstBillMain.updateSalesOrder(oidBillMain,0,0,1,2);
                }else{//jika qtyMatch = false, maka update cash bill main sales order menjadi On Prosess (doc_type=0, Transaction_type=0, Transaction_status=3)
                    long oid = PstBillMain.updateSalesOrder(oidBillMain,0,0,1,1);
                }

                iCommand = Command.APPROVE;
            }

        //cek kredit limit pelanggan
        BillMain billMain = new BillMain();
        MemberReg memberReg = new MemberReg();
        double totalKredit=0.0;
        double totalRetur = 0.0;
        double outstanding = 0.0;
        double creditLimit = 0.0;
        double available = 0.0;

        try{
            billMain = PstBillMain.fetchExc(oidBillMain);
            Vector listMemberReg = PstMemberReg.list(start, recordToGet, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " = '" + billMain.getCustomerId() + "'", "");
            if (listMemberReg.size() > 0) {
                memberReg = (MemberReg) listMemberReg.get(0);
            }
            //mencari credit limit
            totalKredit = PstBillMain.getTotalKredit(billMain.getCustomerId());
            totalRetur = PstBillMain.getReturnKredit(billMain.getCustomerId());
            outstanding = totalKredit - totalRetur;

            creditLimit = memberReg.getMemberCreditLimit();
            available = creditLimit - outstanding;
        }catch(Exception ex){

        }
       
        

%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            

            function cmdEdit(itemName, unitId, matid, sku, qty, itemprice, disc, disc1, disc2, discGlobal,totalAmount,stockreturn,unitCode){
                self.close();
                //self.opener.document.forms.frmcashier.billdetailTmp.value = stockCode;
                // self.opener.document.forms.frmcashier.stock_return.value = stockreturn;
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
                document.frmstockcodedosearch.typeCashier.value="1";
                document.frmstockcodedosearch.nota_type.value="1";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdApproval(){
                self.close();
                self.opener.document.forms.frmcashier.nota_type.value = <%=PstBillMain.INVOICING%>;
                self.opener.document.forms.frmcashier.submit();
            }

            function cmdListFirst(){
                document.frmstockcodedosearch.command.value="<%=Command.FIRST%>";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListPrev(){
                document.frmstockcodedosearch.command.value="<%=Command.PREV%>";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListNext(){
                document.frmstockcodedosearch.command.value="<%=Command.NEXT%>";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdListLast(){
                document.frmstockcodedosearch.command.value="<%=Command.LAST%>";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function cmdSearch(){
                document.frmstockcodedosearch.start.value="0";
                document.frmstockcodedosearch.command.value="<%=Command.LIST%>";
                document.frmstockcodedosearch.action="itemsalesearch.jsp";
                document.frmstockcodedosearch.submit();
            }

            function clear(){
                document.frmstockcodedosearch.txt_materialcode.value="";
            }
             function checkValue(changeVal, qty, stockVal, idx){
                var inputVal = parseFloat(changeVal.value);
                var stockVal = parseFloat(stockVal);
                var qtyOrder=parseFloat(qty);
                //var nameVariable="matqty[" + idx + "]";
                if(inputVal>qtyOrder || inputVal>stockVal){
                    alert("sorry,  value input must less than or same as qty order");
                    document.frmstockcodedosearch.matqty[0].value="0";
                }
            }

             function modifKey(frmObj, event,value){
                // alert("heehe");
                if(event.keyCode == 13) { //enter
                  //alert("heheh");
                  document.all.matqty[0].focus;
                }else if (event.keyCode==112){ //F1=Open Sales Order

                }else if (event.keyCode==40){
                    switch(frmObj.name) {
                        case 'cust_name':
                                //document.frmopenbill.person_name .focus();
                            break;
                        case 'person_name':
                                //activeTable();
                            break;
                                // document.frmopenbill.person_name .focus();
                        default:
                            break;
                    }
                }else if (event.keyCode==38){

                }else if (event.keyCode==27 ){//esc
                    self.close();
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
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onkeydown="modifKey(this, event, this.value)">
        <script language="JavaScript">
                window.focus();
        </script>
        <table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
            <tr>
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
                                    <input type="hidden" name="bill_main_id_old" value="<%=oidBillMain%>">
                                    <input type="hidden" name="bill_main_id_new" value="<%=bill_main_id_new%>">
                                    <input type="hidden" name="location_id" value="<%=oidLocation%>">
                                    <input type="hidden" name="warehouseSales" value="<%=warehouseSalesId%>">
                                    <input type="hidden" name="source_link2" value="itemsalesearch.jsp">
                                    <input type="hidden" name="typeCashier" value="">
                                    <input type="hidden" name="nota_type" value="">
                                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                        <tr>
                                            <td>Nama Perusahaan</td>
                                            <td><%=memberReg.getCompName()%></td>
                                        <tr>
                                            <td>Credit Limit</td>
                                            <td>Rp. <input name="creditlimit" type="text" align="right" value="<%=creditLimit == 0 ? 0 : FRMHandler.userFormatStringDecimal(creditLimit)%>" size="15" tabindex="15" class="hiddenLabel" readonly></td>
                                        </tr>
                                        <tr>
                                            <td>Outstanding</td>
                                            <td>Rp. <input name="outstanding" type="text" align="right" value="<%=outstanding == 0 ? 0 : FRMHandler.userFormatStringDecimal(outstanding)%>" size="15" maxlength="64" tabindex="16" class="hiddenLabel" readonly> </td>
                                        </tr>
                                        <tr>
                                            <td>Available</td>
                                            <td>Rp. <input name="available" type="text" value="<%=available == 0 ? 0 : FRMHandler.userFormatStringDecimal(available)%>" align="right" size="15" maxlength="64" tabindex="17" class="hiddenLabel" readonly>
                                                <input name="availableTmp" type="hidden" value="<%=available == 0 ? 0 : available%>" align="right" size="15" maxlength="64" tabindex="17" class="hiddenLabel" readonly> </td>
                                        </tr>
                                         <tr>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><%=drawListMaterial(oidBillMain, SESS_LANGUAGE, vect, start, vectLocWer)%></td>
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
                                                    <input type="button" name="Button" value="Select All" onClick="javascript:cmdConfrmApprove()" class="formElemen">
                                                </span>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                 <script language="JavaScript">
                                        //alert("sa");
                                        document.frmstockcodedosearch.matqty[0].value="10";
                                </script>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <%if (iCommand == Command.APPROVE) {%>
    <script type="text/javascript">
            cmdApproval();
    </script>
    <%}%>
</html>
