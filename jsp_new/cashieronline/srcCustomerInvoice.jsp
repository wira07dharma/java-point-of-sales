<%--
    Document   : srcCustomerInvoice.jsp
    Created on : 09 Jul 13, 14:44:40
    Author     : Wiweka
--%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*,
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.posbo.entity.masterdata.MemberReg,
         com.dimata.posbo.entity.masterdata.PstMemberReg,
         com.dimata.posbo.entity.search.SrcMemberReg,
         com.dimata.posbo.session.masterdata.SessMemberReg,
         com.dimata.posbo.form.masterdata.CtrlMemberReg,
         com.dimata.posbo.form.masterdata.FrmMemberReg,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.jsp.JspInfo,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.pos.form.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.session.masterdata.SessDiscountCategory" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
            boolean privEditPrice = true;
%>
<%!   public static final String textMaterialHeader[][] = {
        {"Person Name", "Company Name"},
        {"Person Name", "Company Name"}
    };
public static final String textListTitle1[][] = {
        {"Daftar Member", "Harus diisi", "Cetak "},
        {"Registration Member", "required", "Print "}
    };
    public static final String textListHeader1[][] = {
        {"Kode", "Nama", "Perusahaan", "Alamat", "No Telp", "No HP", "Tempat/Tgl Lahir", "Jenis Kelamin", //8
            "Credit Limit", "Consigment Limit", "Mata Uang"}, //15

        {"Code", "Name", "Company", "Address", "Telp No", "HP No", "Place/Birthdate", "Sex",
            "Credit Limit", "Consigment Limit", "Currency"}
    };
%>
<%!    public String drawList(Vector objectClass, long contactId, int languange, int startMember) {
        Vector result = new Vector(1, 1);
        String listAll = "";
        Vector tableHeader = new Vector(1, 1);

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("No", "3%");
        ctrlist.addHeader(textListHeader1[languange][0], "7%");
        ctrlist.addHeader(textListHeader1[languange][1], "10%");
        ctrlist.addHeader(textListHeader1[languange][2], "15%");
        ctrlist.addHeader(textListHeader1[languange][3], "20%");
        ctrlist.addHeader(textListHeader1[languange][4], "10%");
        ctrlist.addHeader(textListHeader1[languange][5], "10%");
        ctrlist.addHeader(textListHeader1[languange][8], "10%");
        ctrlist.addHeader("date","10%");
        //ctrlist.addHeader(textListHeader1[languange][9], "10%");


        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
                        
            Vector tmp = (Vector) objectClass.get(i);
            MemberReg memberReg = (MemberReg) tmp.get(0);
            BillMain billMain = (BillMain) tmp.get(1);

            Vector rowx = new Vector();
            if (contactId == memberReg.getOID()) {
                index = i;
            }

            rowx.add("" + (i + 1 + startMember));//
            String code = memberReg.getMemberBarcode();
            if (code.length() == 0) {
                code = memberReg.getContactCode();
            }
            rowx.add(code);//

            String name = "";
            if (memberReg.getPersonName() != null && memberReg.getPersonName().length() > 0) {
                name = memberReg.getPersonName();
            } else {
                name = memberReg.getCompName();
            }

            String address = "";
            if(memberReg.getBussAddress() != null && memberReg.getBussAddress().length() > 0){
                address = memberReg.getBussAddress();
            }else{
                address = memberReg.getHomeAddr();
            }

            rowx.add(name);//
            rowx.add(memberReg.getCompName());//

            rowx.add(address);//
            rowx.add(memberReg.getHomeTelp());//
            rowx.add(memberReg.getTelpMobile());//


            Vector list = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX] + "=1", "");
            CurrencyType currSell = new CurrencyType();
            if (list != null && list.size() > 0) {
                currSell = (CurrencyType) list.get(0);
            }

            double totalKredit = PstBillMain.getTotalKredit(memberReg.getOID());
            double totalRetur = PstBillMain.getReturnKredit(memberReg.getOID());
            double outstanding = totalKredit - totalRetur;
            double available = memberReg.getMemberCreditLimit() - outstanding;
            // rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(outstanding) + "</div>");
            
            rowx.add(billMain.getBillDate().toString());
            lstData.add(rowx);
            

            

            lstLinkData.add(String.valueOf(memberReg.getOID()));
           // lstLinkData.add(memberReg.getOID());
        }

       return ctrlist.draw();
}
%>

<%
            int iCommandMember = FRMQueryString.requestCommand(request);
            int startMember = FRMQueryString.requestInt(request, "startMember");
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommandMember = FRMQueryString.requestInt(request, "prev_command");
            long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            String compName = FRMQueryString.requestString(request, "compName");
            String personName = FRMQueryString.requestString(request, "personName");
            String kode = FRMQueryString.requestString(request, "kode");
            String status_lunas = FRMQueryString.requestString(request, "status_lunas");
            double creditLimit = FRMQueryString.requestDouble(request, "creditLimit");
            /*variable declaration*/
            //boolean privManageData = true;
            // update by Hendra McHen 2015-01-05
            String start_mm_now = FRMQueryString.requestString(request, "start_mm_now");
            String start_dd_now = FRMQueryString.requestString(request, "start_dd_now");
            String start_yy_now = FRMQueryString.requestString(request, "start_yy_now");
            String tanggal_sekarang = "";
            String date_kedepan = FRMQueryString.requestString(request, "date_kedepan");
            int dayAkhir = FRMQueryString.requestInt(request, "batas_hari");
            
            
            
            int recordCustomerToGet = 10;
            String msgStringMember = "";
            int iErrCodeMember = FRMMessage.NONE;
            String whereClauseCustomer = "";
            //String orderClause = ""+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];

            CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
            ControlLine ctrLineCust = new ControlLine();
            Vector listMemberReg = new Vector(1, 1);
                       /*switch statement */
            iErrCodeMember = ctrlMemberReg.action(iCommandMember, oidMemberReg);
            /* end switch*/
            FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();
            

            /*count list All MemberReg*/
            //int vectSizeMember = PstMemberReg.getCount(whereClauseCustomer);
            /* begin result */
            SrcMemberReg srcMemberReg = new SrcMemberReg();


            if (iCommandMember == Command.LIST || iCommandMember == Command.FIRST || iCommandMember == Command.PREV
                    || iCommandMember == Command.NEXT || iCommandMember == Command.LAST || iCommandMember == Command.BACK) {
                try {
                    srcMemberReg = (SrcMemberReg) session.getValue(SessMemberReg.SESS_SRC_MEMBERREG);
                } catch (Exception e) {
                    srcMemberReg = new SrcMemberReg();
                }

                if (session.getValue(SessMemberReg.SESS_SRC_MEMBERREG) == null) {
                    srcMemberReg = new SrcMemberReg();
                }

                session.putValue(SessMemberReg.SESS_SRC_MEMBERREG, srcMemberReg);
            }

            srcMemberReg.setName(personName);
            srcMemberReg.setCompanyName(compName);

            MemberReg memberReg = ctrlMemberReg.getMemberReg();
            msgStringMember = ctrlMemberReg.getMessage();
            // PstContactList.TBL_CONTACT_LIST AS CL
            
            
            
            // Hendra McHen [2014-12-29]
            
            if (personName != "" && compName != "" && kode != ""){
                whereClauseCustomer = " CL."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" LIKE '%"+personName+"%' ";
                whereClauseCustomer = whereClauseCustomer + " OR ";
                whereClauseCustomer = whereClauseCustomer + " CL."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIKE '%"+compName+"%' ";
                whereClauseCustomer = whereClauseCustomer + " OR ";
                whereClauseCustomer = whereClauseCustomer + " CL."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+ " LIKE '%"+kode+"%' ";
                
                /*
                doc_type :0	
                transaction_type: 1	
                transaction_status : 0	
                Transaksi kredit yang sudah lunas
                */
               
                if (status_lunas.equals("2")){//
                    whereClauseCustomer = whereClauseCustomer + " AND ";
                    whereClauseCustomer = whereClauseCustomer + " (CDM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = 0 AND ";
                    whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = 1 AND ";
                    whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = 0) ";
                }
                
                                
            } else {
                if (personName != ""){
                   whereClauseCustomer = " CL."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" LIKE '%"+personName+"%' ";
                }

                if (compName != ""){   
                    if(personName.length() > 0){whereClauseCustomer = whereClauseCustomer + " OR ";}
                   
                   whereClauseCustomer = " CL."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIKE '%"+compName+"%' ";
                }
                
                if (kode != ""){
                    if(personName.length() > 0 || compName.length() > 0){whereClauseCustomer = whereClauseCustomer + " OR ";}
                   whereClauseCustomer = " CL."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+ " LIKE '%"+kode+"%' ";
                }
                
                /*
                doc_type :0	
                transaction_type: 1	
                transaction_status : 0	
                Transaksi kredit yang sudah lunas
                */
                
                if (status_lunas.equals("2")){
                    if(personName.length() > 0 || compName.length() > 0 || kode.length() > 0)
                    {
                        whereClauseCustomer = whereClauseCustomer + " AND ";
                    }
                    
                    whereClauseCustomer = whereClauseCustomer + " (CDM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = 0 AND ";
                    whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = 1 AND ";
                    whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = 0) ";
                }
                
                if (date_kedepan != ""){
                    if (date_kedepan.equals("0")){
                        tanggal_sekarang = start_yy_now + "-" + start_mm_now + "-" + start_dd_now;
                        whereClauseCustomer = whereClauseCustomer + " CDM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" LIKE '"+tanggal_sekarang+"%' ";
                    } else if (date_kedepan.equals("3")){
                        String dayCurr = start_dd_now;
                        String monthCurr = start_mm_now;
                        String yearCurr = start_yy_now;
                        
                        int inc = 3;
                        int dayNow = Integer.valueOf(start_dd_now);
                        int monthNow = Integer.valueOf(start_mm_now);
                        int yearNow = Integer.valueOf(start_yy_now);
                        for(int i=0; i<inc; i++){
                        
                            if(dayNow < dayAkhir){
                                dayCurr = String.valueOf(dayNow);
                                if(dayCurr.length() < 2){
                                    dayCurr = "0" + dayCurr;
                                }
                                whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ " LIKE '" + yearCurr + "-"+monthCurr+"-"+dayCurr+"%' ";
                            } else {
                                dayNow = dayNow - dayAkhir;
                                monthNow = monthNow + 1;
                                if (monthNow > 12){
                                    monthCurr = "01";
                                    yearNow = yearNow + 1;
                                    yearCurr = String.valueOf(yearNow);
                                } else {
                                    monthCurr = String.valueOf(monthNow);
                                }

                                dayCurr = String.valueOf(dayNow);
                                whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ " LIKE '" + yearCurr + "-"+monthCurr+"-"+dayCurr+"%' ";
                            }
                            if (i < inc-1){
                                whereClauseCustomer = whereClauseCustomer + " OR ";
                            } else {
                                whereClauseCustomer = whereClauseCustomer + " ";
                            }
                            dayNow = dayNow + 1;
                        }
                        
                        
                    } else {
                        String dayCurr = start_dd_now;
                        String monthCurr = start_mm_now;
                        String yearCurr = start_yy_now;
                        
                        int inc = 7;
                        int dayNow = Integer.valueOf(start_dd_now);
                        int monthNow = Integer.valueOf(start_mm_now);
                        int yearNow = Integer.valueOf(start_yy_now);
                        for(int i=0; i<inc; i++){
                        
                            if(dayNow < dayAkhir){
                                dayCurr = String.valueOf(dayNow);
                                if(dayCurr.length() < 2){
                                    dayCurr = "0" + dayCurr;
                                }
                                whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ " LIKE '" + yearCurr + "-"+monthCurr+"-"+dayCurr+"%' ";
                            } else {
                                dayNow = dayNow - dayAkhir;
                                monthNow = monthNow + 1;
                                if (monthNow > 12){
                                    monthCurr = "01";
                                    yearNow = yearNow + 1;
                                    yearCurr = String.valueOf(yearNow);
                                } else {
                                    monthCurr = String.valueOf(monthNow);
                                }

                                dayCurr = String.valueOf(dayNow);
                                whereClauseCustomer = whereClauseCustomer + " CDM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ " LIKE '" + yearCurr + "-"+monthCurr+"-"+dayCurr+"%' ";
                            }
                            if (i < inc-1){
                                whereClauseCustomer = whereClauseCustomer + " OR ";
                            } else {
                                whereClauseCustomer = whereClauseCustomer + " ";
                            }
                            dayNow = dayNow + 1;
                        }
                    }
                }
                
            }
            
            /*switch list MemberReg*/
            if ((iCommandMember == Command.SAVE) && (iErrCodeMember == FRMMessage.NONE)) {
                startMember = PstMemberReg.findLimitStart(memberReg.getOID(), recordCustomerToGet, whereClauseCustomer);
            }
            
            int vectSizeMember = PstMemberReg.listCountMember(startMember, recordCustomerToGet, "", "");

            if ((iCommandMember == Command.FIRST || iCommandMember == Command.PREV)
                    || (iCommandMember == Command.NEXT || iCommandMember == Command.LAST)) {
                startMember = ctrlMemberReg.actionList(iCommandMember, startMember, vectSizeMember, recordCustomerToGet);
            }
            
            listMemberReg = PstMemberReg.listMember(startMember, recordCustomerToGet, whereClauseCustomer, "");

            if (listMemberReg.size() < 1 && startMember > 0) {
                if (vectSizeMember - recordCustomerToGet > recordCustomerToGet) {
                    startMember = startMember - recordCustomerToGet;   //go to Command.PREV
                } else {
                    startMember = 0;
                    iCommandMember = Command.FIRST;
                    prevCommandMember = Command.FIRST; //go to Command.FIRST
                }
                listMemberReg = PstMemberReg.listMember(startMember, recordCustomerToGet, "", ""); 
            }
            
            
// combo box status lunas

    Vector val_status_lunas = new Vector(1, 1); //hidden values that will be deliver on request (oids) 

    Vector key_status_lunas = new Vector(1, 1); //texts that displayed on combo box

    val_status_lunas.add("0");

    key_status_lunas.add("- select -");

    

        val_status_lunas.add("1");

        key_status_lunas.add("Belum Lunas");

    val_status_lunas.add("2");

        key_status_lunas.add("Lunas");

    String select_compcountry = status_lunas; //selected on combo box
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
    <!-- #BeginEditable "doctitle" -->
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">


        function cmdAdd(){
            document.frmmember.hidden_contact_id.value="0";
            document.frmmember.command.value="<%=Command.ADD%>";
            document.frmmember.prev_command.value="<%=prevCommandMember%>";
            document.frmmember.action="memberreg_edit.jsp";
            document.frmmember.submit();
        }

        function cmdAsk(oidMemberReg){
            document.frmmember.hidden_contact_id.value=oidMemberReg;
            document.frmmember.command.value="<%=Command.ASK%>";
            document.frmmember.prev_command.value="<%=prevCommandMember%>";
            document.frmmember.action="customersearch.jsp";
            document.frmmember.submit();
        }

        function cmdConfirmDelete(oidMemberReg){
            document.frmmember.hidden_contact_id.value=oidMemberReg;
            document.frmmember.command.value="<%=Command.DELETE%>";
            document.frmmember.prev_command.value="<%=prevCommandMember%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }
        function cmdSave(){
            document.frmmember.command.value="<%=Command.SAVE%>";
            document.frmmember.prev_command.value="<%=prevCommandMember%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdEdit(oid){            
            document.frmmember.start.value="0";
            document.frmmember.cust_id.value = oid;
            //document.frmmember.action="srcCreditInvoice.jsp";
            document.frmmember.action="payment_credit.jsp";
            document.frmmember.submit();
        }

        function cmdCancel(oidMemberReg){
            document.frmmember.hidden_contact_id.value=oidMemberReg;
            document.frmmember.command.value="<%=Command.EDIT%>";
            document.frmmember.prev_command.value="<%=prevCommandMember%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdBack(){
            document.frmmember.command.value="<%=Command.BACK%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdListFirst(){
            document.frmmember.command.value="<%=Command.FIRST%>";
            document.frmmember.prev_command.value="<%=Command.FIRST%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdListPrev(){
            document.frmmember.command.value="<%=Command.PREV%>";
            document.frmmember.prev_command.value="<%=Command.PREV%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdListNext(){
            document.frmmember.command.value="<%=Command.NEXT%>";
            document.frmmember.prev_command.value="<%=Command.NEXT%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }

        function cmdListLast(){
            document.frmmember.command.value="<%=Command.LAST%>";
            document.frmmember.prev_command.value="<%=Command.LAST%>";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }
        
        function cmdSearchCust(){
            document.frmmember.startMember.value="0";
            document.frmmember.action="srcCustomerInvoice.jsp";
            document.frmmember.submit();
        }


        //-------------- script control line -------------------
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
    <style type="text/css">
        #btn {padding: 3px 5px; color: #fff; background-color: #333; border:1px solid #000; border-radius: 3px;}
        #btn:hover {background-color:#CC3300; border: 1px solid #800000; }
    </style>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
     <%if(menuUsed == MENU_PER_TRANS){%>
      <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
          <%@ include file = "../main/header.jsp" %>
          <!-- #EndEditable --></td>
      </tr>
      <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
          <%@ include file = "../main/mnmain.jsp" %>
          <!-- #EndEditable --> </td>
      </tr>
      <%}else{%>
       <tr bgcolor="#FFFFFF">
        <td height="10" ID="MAINMENU">
          <%@include file="../styletemplate/template_header.jsp" %>
        </td>
      </tr>
      <%}%>
        <tr>
            <td valign="top" align="left">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td><!-- #BeginEditable "content" -->
                            <form name="frmmember" method ="post" action="">
                                <input type="hidden" name="command" value="<%=iCommandMember%>">
                                <input type="hidden" name="vectSizeMember" value="<%=vectSizeMember%>">
                                <input type="hidden" name="startMember" value="<%=startMember%>">
                                <input type="hidden" name="start" value="<%=start%>">
                                <input type="hidden" name="prev_command" value="<%=prevCommandMember%>">
                                <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">
                                <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                                <input type="hidden" name="cust_id" value="<%=oidMemberReg%>">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr align="left" valign="top">
                                        <td height="8"  colspan="3">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr align="left" valign="top">
                                                    <td height="8" valign="middle" colspan="3">
                                                        <hr>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                    <td height="22" width="87%"> :
                                                        <input type="text" name="personName" size="30" value="<%=personName%>" class="formElemen">
                                                    </td>
                                                    <td height="22"></td>
                                                </tr>
                                                <tr>
                                                    <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                    <td height="22" width="87%"> :
                                                        <input type="text" name="compName" size="30" value="<%=compName%>" class="formElemen">
                                                    </td>
                                                    <td height="22"></td>
                                                </tr>
                                                <!-- hendra [2014-12-29] -->
                                                <tr>
                                                    <td height="22" width="13%">Kode</td>
                                                    <td height="22" width="87%"> :
                                                        <input type="text" name="kode" size="30" value="<%=kode%>" class="formElemen">
                                                    </td>
                                                    <td height="22"></td>
                                                </tr>
                                                <tr>
                                                    <td height="22" width="13%">Customer berdasarkan</td>
                                                    <td height="22" width="87%"> :
                                                        <%=ControlCombo.draw("status_lunas", null, select_compcountry, val_status_lunas, key_status_lunas, "", "form-control")%>
                                                    </td>
                                                    <td height="22"></td>
                                                </tr>
                                                <tr>
                                                    <td height="22" width="13%">&nbsp;</td>
                                                    <td height="22" width="87%">&nbsp;
                                                        <input type="button" name="Button" id="btn" value="Search" onClick="javascript:cmdSearchCust()" class="formElemen">
                                                    </td>
                                                    <td height="22"></td>
                                                </tr>
                                                <tr align="left" valign="top">
                                                    <td height="14" valign="middle" colspan="3" class="comment"><%=textListTitle1[SESS_LANGUAGE][0]%></td>
                                                </tr>
                                                <%
                                                            try {
                                                                if (listMemberReg.size() > 0) {
                                                %>
                                                <tr align="left" valign="top">
                                                    <td height="22" valign="middle" colspan="3">
                                                        <%=drawList(listMemberReg, oidMemberReg, SESS_LANGUAGE, startMember)%>
                                                    </td>
                                                </tr>
                                                <%  }
                                  } catch (Exception exc) {
                                  }%>
                                                <tr align="left" valign="top">
                                                    <td height="8" align="left" colspan="3" class="command">
                                                        <span class="command">

                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr align="left" valign="top">
                                                    <td height="8" align="left" colspan="3" class="command">
                                                        <span class="command">
                                                            <!--%
                                                                        int cmd = 0;
                                                                        if ((iCommandMember == Command.FIRST || iCommandMember == Command.PREV)
                                                                                || (iCommandMember == Command.NEXT || iCommandMember == Command.LAST)) {
                                                                            cmd = iCommandMember;
                                                                        } else {
                                                                            if (iCommandMember == Command.NONE || prevCommandMember == Command.NONE) {
                                                                                cmd = Command.FIRST;
                                                                            } else {
                                                                                cmd = prevCommandMember;
                                                                            }
                                                                        }
                                                            %-->
                                                            <% ctrLineCust.setLocationImg(approot + "/images");
                                                                        ctrLineCust.initDefault();
                                                            %>
                                                            <%=ctrLineCust.drawImageListLimit(iCommandMember, vectSizeMember, startMember, recordCustomerToGet)%> </span> </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <%//@ include file = "../main/menumain.jsp" %>
                            <!-- #EndEditable --></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../styletemplate/footer.jsp" %>

                <%}else{%>
                    <%@ include file = "../main/footer.jsp" %>
                <%}%>
              <!-- #EndEditable --> </td>
          </tr>
    </table>
</body>
<!-- #EndTemplate --></html>

