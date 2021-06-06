<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@ page import = "com.dimata.gui.jsp.ControlList,
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
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*" %>
<%@ page contentType = "text/html" %>

<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<!--%@ include file = "../../../main/checkuser.jsp" %-->

<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Kategori", "Sku", "Nama Barang", "Semua Barang", "Unit"},
        {"Category", "Code", "Material Name", "All Goods", "Unit"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"NO", "SKU", "NAMA BARANG", "UNIT","QTY REAL STOCK","QTY BOOKING","QTY AVAILABLE","QTY INCOMING","HARGA", "RATE", "TOTAL PRICE"},
        {"NO", "CODE", "NAME PRODUCT", "UNIT","QTY REAL STOCK","QTY BOOKING","QTY AVAILABLE","QTY INCOMING","HARGA","RATE", "TOTAL PRICE"}
    };

    public String drawListMaterial(int currency, int language, Vector objectClass, int start, long salesWerehouse, double rate) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "12%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "40%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][8], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][9], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][10], "15%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("' )");
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }
            //cek periode yang sedang running
            Periode maPeriode = PstPeriode.getPeriodeRunning();
            
            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                Material material = (Material) vt.get(0);
                PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                Unit unit = (Unit) vt.get(2);
                StandartRate standart = (StandartRate) vt.get(3);
                
                start = start + 1;

                Vector rowx = new Vector();

                rowx.add("" + start);
                rowx.add(material.getSku());
                rowx.add(material.getName());

                //cek qty real
                double qtyStockReal = PstMaterialStock.getStockWh(salesWerehouse, material.getOID(), maPeriode.getOID());
                
                //cek qty booking
                double qtyBookingSalesOrder = PstBillMain.getStockBooking(salesWerehouse, material.getOID());
              
                //cek qty booking bukan dari invoicing
                double qtyBookingNonSalesOrder = PstBillMain.getStockBookingNonSalesOrder(salesWerehouse, material.getOID());

                //cek qty booking
                double qtyBooking = qtyBookingSalesOrder+qtyBookingNonSalesOrder;

                //cek available stock
                double qtyAvailable =qtyStockReal-qtyBookingSalesOrder-qtyBookingNonSalesOrder;
                double  resultKonversi = PstDailyRate.getCurrentDailyRateSales(standart.getCurrencyTypeId());
                rowx.add(""+qtyStockReal);
                rowx.add(""+qtyBooking);
                rowx.add(""+qtyAvailable);
                rowx.add(""+FRMHandler.userFormatStringDecimal(priceTypeMapping.getPrice()));
                rowx.add(""+FRMHandler.userFormatStringDecimal(resultKonversi));
                double tot = priceTypeMapping.getPrice()*resultKonversi;
                rowx.add(""+FRMHandler.userFormatStringDecimal(tot));

                lstData.add(rowx);

                String name = "";
                name = material.getName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');

                lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + material.getDefaultStockUnitId()
                        + "','" + tot + "','" + unit.getCode()+ "','" + qtyAvailable);

            }
            //return ctrlist.draw();
            return ctrlist.drawTableNavigation();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
        }
        return result;
    }


     public String drawListMaterialIncoming(int currency, int language, Vector objectClass, int start, long salesWerehouse) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "12%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "40%");
            ctrlist.addHeader(textListMaterialHeader[language][7], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "15%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "15%");


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
            //cek periode yang sedang running
            Periode maPeriode = PstPeriode.getPeriodeRunning();

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                Material material = (Material) vt.get(0);
                PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                Unit unit = (Unit) vt.get(2);
                
                start = start + 1;

                Vector rowx = new Vector();

                rowx.add("" + start);
                rowx.add(material.getSku());
                rowx.add(material.getName());

                //cek qty stock incoming ?
                double qtyStockReal = PstMaterialStock.getStockWh(salesWerehouse, material.getOID(), maPeriode.getOID());
                //cek qty booking
                double qtyBooking = PstBillMain.getStockBooking(salesWerehouse, material.getOID());

                //cek qty booking bukan dari invoicing
                double qtyBookingNonSalesOrder = PstBillMain.getStockBookingNonSalesOrder(salesWerehouse, material.getOID());

                //cek available stock
                double qtyAvailable = qtyStockReal-qtyBooking-qtyBookingNonSalesOrder;

                rowx.add(""+qtyStockReal);
                //rowx.add(""+qtyBooking);
                //rowx.add("<a  href=\"javascript:viewBook('"+material.getOID()+"','"+salesWerehouse+"')\">"+qtyBooking+"</a> ");
                rowx.add(""+qtyBooking);
                rowx.add(""+qtyAvailable);

                lstData.add(rowx);

                String name = "";
                name = material.getName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');

                lstLinkData.add(material.getOID() + "','" + material.getSku() + "','" + name + "','" + material.getDefaultStockUnitId()
                        + "','" + priceTypeMapping.getPrice() + "','" + unit.getCode()+ "','" + qtyAvailable);

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
            String materialcode = FRMQueryString.requestString(request, "mat_code");
            long materialgroup = FRMQueryString.requestLong(request, "txt_materialgroup");
            String materialname = FRMQueryString.requestString(request, "txt_materialname");
            long oidVendor = FRMQueryString.requestLong(request, "mat_vendor");
            long oidCurrency = FRMQueryString.requestLong(request, "currency_id");
            int currType = FRMQueryString.requestInt(request, "currency_type");
            long oidCustomer = FRMQueryString.requestLong(request, "customer_id");
            long oidPriceType = FRMQueryString.requestLong(request, "price_type_id");
            long oidStandartRate = FRMQueryString.requestLong(request, "standart_rate_id");
            double curr = FRMQueryString.requestDouble(request, "rate");
            long transType = FRMQueryString.requestLong(request, "trans_type");
            int typeSalesOrder = FRMQueryString.requestInt(request, "type_sales_order");
            long salesWarehouse = FRMQueryString.requestLong(request, "salesWarehouse");
            
            int start = FRMQueryString.requestInt(request, "start");
            int iCommand = FRMQueryString.requestCommand(request);
            int recordToGet = 20;
            //show all good
            int showAllGoods = FRMQueryString.requestInt(request, "show_all_good");
            double rate = FRMQueryString.requestDouble(request, "rate");
            System.out.println("currency_id : " + oidCurrency);
            String vendorName = "";
            try {
                PstContactList pstContactList = new PstContactList();
                ContactList contactList = pstContactList.fetchExc(oidVendor);
                vendorName = contactList.getCompName();
            } catch (Exception e) {
                System.out.println("Err when fetch Supplier : " + e.toString());
            }

            Location xLocation= new Location();
            try{
                xLocation=PstLocation.fetchExc(salesWarehouse);
            }catch(Exception ex){

            }

            String orderBy = PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            Material objMaterial = new Material();
            objMaterial.setCategoryId(materialgroup);
            objMaterial.setSku(materialcode);
            objMaterial.setName(materialname);
            objMaterial.setDefaultCostCurrencyId(oidCurrency);
            objMaterial.setBarCode(materialcode);

            //Mencari Customer Type
            int CustType = 0;
            try {
                PstMemberGroup pstMemberGroup = new PstMemberGroup();
                MemberGroup memberG = pstMemberGroup.fetchExc(transType);
                CustType = memberG.getGroupType();
            } catch (Exception e) {
                System.out.println("Err when fetch Supplier : " + e.toString());
            }

            long priceType2 = FRMQueryString.requestLong(request, "price_type");
            ;
            if (CustType == 3) {//untuk member
                //Mencari Price Type Id
                Vector listPriceType = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='1'", "");
                for (int i = 0; i < listPriceType.size(); i++) {
                    MemberGroup memberGroup = (MemberGroup) listPriceType.get(i);
                    priceType2 = memberGroup.getPriceTypeId();
                }
            }

            int vectSize = PstMaterial.getCountMaterialItemCashier(priceType2, objMaterial, orderBy, oidPriceType, oidStandartRate);

            //System.out.println("kdjhgkjfdhgkdfgksdjfhkgjshdkfjghlksjdfhglkjsdl");

            CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
            }
            String pageTitle = "";
            /*if (showAllGoods > 0) {
                pageTitle = "DAFTAR SEMUA BARANG ";
            } else {
                if(typeSalesOrder==1){
                     pageTitle = "DAFTAR BARANG INCOMING";
                }else{
                    pageTitle = "DAFTAR BARANG";
                }
            }*/
            if(typeSalesOrder==1){
                     pageTitle = "DAFTAR BARANG INCOMING DI GUDANG "+xLocation.getName();
                }else{
                    pageTitle = "DAFTAR BARANG DI GUDANG "+xLocation.getName();
            }

            Vector vect = PstMaterial.getListMaterialItemCashierConvertToRupiah(priceType2, objMaterial, start, recordToGet, orderBy, oidPriceType, oidStandartRate);

            if (vectSize == 1) {
                Vector vt = (Vector) vect.get(0);
                Material material = (Material) vt.get(0);
                PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                Unit unit = (Unit) vt.get(2);
                %>
                <script language="JavaScript">
                   // alert("hello");
                    self.close();
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                    self.opener.document.forms.frmcashier.matUnit.value = "<%=unit.getCode()%>";
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value = "<%=material.getSku()%>";
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.value = "<%=material.getName()%>";
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>.value = "<%=priceTypeMapping.getPrice()%>";
                    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_UNIT_ID]%>.value = "<%=material.getDefaultStockUnitId()%>";
                </script>
                <%
                }

%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function addNewItem(){
                document.frmvendorsearch.command.value="<%=Command.ADD%>";
                document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdEdit(matOid,matCode,matItem,unitId,matPrice,matUnit,qtyAvailable){
                        self.close();
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                        self.opener.document.forms.frmcashier.matUnit.value = matUnit;
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value = matCode;
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.value = matItem;
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>.value = matPrice;
                        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_UNIT_ID]%>.value = unitId;
                        self.opener.document.forms.frmcashier.AvailableQty.value = qtyAvailable;   
                
            }

            function cmdListFirst(){
                document.frmvendorsearch.command.value="<%=Command.FIRST%>";
                document.frmvendorsearch.action="materialdosearchcashier.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListPrev(){
                document.frmvendorsearch.command.value="<%=Command.PREV%>";
                document.frmvendorsearch.action="materialdosearchcashier.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListNext(){
                document.frmvendorsearch.command.value="<%=Command.NEXT%>";
                document.frmvendorsearch.action="materialdosearchcashier.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListLast(){
                document.frmvendorsearch.command.value="<%=Command.LAST%>";
                document.frmvendorsearch.action="materialdosearchcashier.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdSearch(){
                document.frmvendorsearch.start.value="0";
                document.frmvendorsearch.command.value="<%=Command.LIST%>";
                document.frmvendorsearch.action="materialdosearchcashier.jsp";
                document.frmvendorsearch.submit();
            }

            function clear(){
                document.frmvendorsearch.txt_materialcode.value="";
            }

            function viewBook(materialId,location){
                var strvalue  = "viewbook.jsp?command=<%=Command.FIRST%>"+
                                "&materialId="+materialId+
                                 "&locationwarhouse="+location;
                window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function changeFocus(element){
                    if(element.name == "mat_code") {
                         cmdSearch();
                    }else if(element.name == "txt_materialname") {
                         cmdSearch();
                    }
             }

               function cmdEnter(element, evt){
                    if (evt.keyCode == 13) {//enter
                       changeFocus(element);
                    }else if(evt.keyCode == 27  ){ //esc
                       switch(element.name) {
                             case 'txt_materialname':
                                     document.frmvendorsearch.mat_code.focus();
                                     break;
                             case 'mat_code':
                                     self.close();
                                     self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                                     break;
                             default:
                                 document.frmvendorsearch.mat_code.focus();
                                break;
                         }
                    }else if(evt.keyCode == 40 ){ //keydown
                         switch(element.name) {
                             case 'txt_materialname':
                                      activeTable();
                                     break;
                             case 'mat_code':
                                     document.frmvendorsearch.txt_materialname.focus();
                                     break;
                             default:
                                break;
                         }
                    }else if(evt.keyCode == 38 ){ //keydown
                        switch(element.name) {
                             case 'txt_materialname':
                                      document.frmvendorsearch.mat_code.focus();
                                     break;
                             case 'mat_code':
                                     break;
                             default:
                                break;
                         }
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
        <script src="../styles/jquery.js"></script>
        <script src="../styles/jquery.table_navigation.js"></script>
        <script type="text/javascript">
                function activeTable(){
                     jQuery.tableNavigation();
                }
                    
	</script>
       	<style type="text/css">
		table {border-collapse: collapse;}
		th, td, tr {margin: 0; padding: 0.25em 0.5em; font-size:16px;}
		/* This "tr.selected" style is the only rule you need for yourself. It highlights the selected table row. */
		tr.selected {background-color: #87CEEB; color: white;}
		/* Not necessary but makes the links in selected rows white to... */
		tr.selected a {color: white;}
                a{font-size:16px;}
	</style>
       <link rel="stylesheet" href="../styles/main.css" type="text/css">
       <link rel="stylesheet" href="../styles/tab.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onkeydown="javascript:cmdEnter(this,event)">
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
                                <form name="frmvendorsearch" method="post" action="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="curr_type" value="<%=currType%>">
                                    <input type="hidden" name="currency_id" value="<%=oidCurrency%>">
                                    <input type="hidden" name="standart_rate_id" value="<%=oidStandartRate%>">
                                    <input type="hidden" name="customer_id" value="<%=oidCustomer%>">
                                    <input type="hidden" name="price_type_id" value="<%=oidPriceType%>">
                                    <input type="hidden" name="price_type" value="<%=priceType2%>">
                                    <input type="hidden" name="rate" value="<%=curr%>">
                                    <input type="hidden" name="type_sales_order" value="<%=typeSalesOrder%>">
                                    <input type="hidden" name="salesWarehouse" value="<%=salesWarehouse%>">
                                    <input type="hidden" name="source_link2" value="materialdosearchcashier.jsp">
                                    <input type="hidden" name="AvailableQty" value="">
                                    <table width="100%" border="0" cellspacing="1" cellpadding="1">

                                        <tr>
                                            <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                            <td width="87%"> :
                                                <input type="text" name="mat_code" size="15" value="<%=materialcode%>" class="formElemen" onkeydown="javascript:cmdEnter(this,event)">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                                            <td width="87%"> :
                                                <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen" onkeydown="javascript:cmdEnter(this,event)">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="13%">&nbsp;</td>
                                            <td width="87%">
                                                <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen" >

                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <%if(typeSalesOrder==1){%>
                                                    <%=drawListMaterialIncoming(currType, SESS_LANGUAGE, vect, start,salesWarehouse)%>
                                                <%}else{%>
                                                    <%=drawListMaterial(currType, SESS_LANGUAGE, vect, start,salesWarehouse, rate)%>
                                                <%}%>
                                            </td>
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
                                    </table>
                                </form>
                                <script language="JavaScript">
                                 document.frmvendorsearch.mat_code.focus();
                                </script>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>
