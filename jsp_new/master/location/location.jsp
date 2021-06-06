<%--
    Document   : company
    Created on : Jan 24, 2014, 3:45:09 PM
    Author     : Acer
--%>




<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.posbo.entity.masterdata.PayGeneral"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstPayGeneral"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberGroup"%>
<%@page import="com.dimata.common.entity.payment.DiscountType"%>
<%@page import="com.dimata.common.entity.payment.PstDiscountType"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.harisma.entity.masterdata.Department"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDepartment"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.posbo.entity.masterdata.NotaSetting"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstNotaSetting"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmNotaSetting"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlNotaSetting"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {
            "No", "Kode", "Nama", "Alamat", "Telepon", "Fax", "Kontak Person", "E-mail", "Website", "Locasi Induk", "Tipe",//10
            "Kontak Perusahaan", "Keterangan", "Tax Persentase", "Service Percentase", "Tax & Service Standart", "Persentasi Distribusi PO (%)", "Nama Perusahaan", "Department", "Report Group", "Price Type",//20
            "Standart Rate", "Footer Text", "Atur Footer Text", "Simpan", "Tutup", "Email Akunting", "IP Lokasi", "Alamat Histori Oulet", "Waktu Buka", "Waktu Tutup",//30
            "Discount Type","Member Type","Maksimum Hari Penukaran Item"
        },
        {"No", "Code", "Name", "Address", "Phone", "Fax", "Person Name", "E-mail", "Website", "Parent Location", "Type", "Contact Link", "Description", "Tax Persentase", "Service Percentase", "Tax & Service Default", "Persentasi Distribusi PO (%)", "Company", "Department", "Report Group", "Price Type", "Standart Rate", "Footer Text", "Set Footer Text", "Save", "Close", "Accounting Email", "Location IP", "Outlet History Address", "Opening Time", "Closing Time","Discount Type","Member Type", "Max Exchange Day"}
    };
    public static final String textListTitleHeader[][] = {
        {"Lokasi Barang"},
        {"Goods Location"}
    };

    public String drawList(int language, Vector objectClass, long locationId, String approot, int start, int recordToGet) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "3%");//1
        ctrlist.addHeader(textListHeader[language][1], "7%");//2
        ctrlist.addHeader(textListHeader[language][2], "10%");//3
        ctrlist.addHeader(textListHeader[language][3], "10%");//4
        ctrlist.addHeader(textListHeader[language][4], "10%");//5
        ctrlist.addHeader(textListHeader[language][5], "10%");//6
        ctrlist.addHeader(textListHeader[language][6], "10%");//7
        ctrlist.addHeader(textListHeader[language][17], "10%");//8
        ctrlist.addHeader(textListHeader[language][18], "10%");//9
        ctrlist.addHeader(textListHeader[language][10], "5%");//10
        ctrlist.addHeader(textListHeader[language][16], "5%");//11
        ctrlist.addHeader(textListHeader[language][20], "10%");//10
        ctrlist.addHeader(textListHeader[language][21], "10%");//11
        ctrlist.addHeader(textListHeader[language][26], "10%");//12
        ctrlist.addHeader(textListHeader[language][27], "10%");//13
        ctrlist.addHeader(textListHeader[language][28], "10%");//14
        ctrlist.addHeader(textListHeader[language][29], "10%");//15
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector lstLinkDataa = ctrlist.getLinkData();
        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Location location = (Location) objectClass.get(i);

            Vector rowx = new Vector();
            if (locationId == location.getOID()) {
                index = i;
            }

            start = start + 1;
            rowx.add("" + start);//1
            rowx.add("<a href=\"javascript:cmdEdit('" + location.getOID() + "')\">" + location.getCode() + "</a>");//2
            rowx.add(cekNull(location.getName()));//3
            rowx.add(cekNull(location.getAddress()));//4
            rowx.add(cekNull(location.getTelephone()));//5
            rowx.add(cekNull(location.getFax()));//6
            rowx.add(cekNull(location.getPerson()));//7		
            //update by fitra

            rowx.add(cekNull(location.getCompanyName()));//8
            rowx.add(cekNull(location.getDepartmentName()));//8
            rowx.add(PstLocation.fieldLocationType[location.getType()]);//9
            rowx.add("" + location.getPersentaseDistributionPurchaseOrder());//10
            rowx.add("" + location.getPriceTypeName());//10
            rowx.add("" + location.getSymbolCurr());//11
            //update by De Koyo
            rowx.add("" + location.getAcountingEmail());//12
            rowx.add("" + location.getLocationIp());//13
            rowx.add("" + location.getSistemAddressHistoryOutlet());//14
            Date openTime = location.getOpeningTime();
            Date closeTime = location.getClosingTime();
            String open = "";
            String close = "";
            if (openTime == null) {
                open = "00:00:00";
            } else {
                open = Formater.formatDate(location.getOpeningTime(), "kk:mm:ss");
            }
            if (closeTime == null) {
                close = "00:00:00";
            } else {
                close = Formater.formatDate(location.getClosingTime(), "kk:mm:ss");
            }
            rowx.add(open + " - " + close);
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(location.getOID()));
        }
        return ctrlist.draw();
    }

    public String cekNull(String val) {
        if (val == null) {
            val = "";
        }
        return val;
    }
%>

<%
    /* get data from request form */
    int iCommand = FRMQueryString.requestCommand(request);
    int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
    int startLocation = FRMQueryString.requestInt(request, "start_location");
    int startMaterial = FRMQueryString.requestInt(request, "start_material");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
    long oidMinimum = FRMQueryString.requestLong(request, "hidden_mat_minimum_id");
    String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
//String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];

    /* variable declaration */
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = PstLocation.fieldNames[PstLocation.FLD_CODE] + "," + PstLocation.fieldNames[PstLocation.FLD_NAME];

    /* ControlLine */
    ControlLine ctrLine = new ControlLine();

    /* Control LOcation */
    CtrlLocation ctrlLocation = new CtrlLocation(request);
    FrmLocation frmLocation = ctrlLocation.getForm();
    iErrCode = ctrlLocation.action(iCommand, oidLocation, request);
    Location location = ctrlLocation.getLocation();
    msgString = ctrlLocation.getMessage();

if(iCommand == Command.SAVE){
      String urlSedana = PstSystemProperty.getValueByName("SEDANA_URL");
      String url = urlSedana+"/master/location/insert";
      JSONObject jSONObject = PstLocation.fetchJSON(location.getOID());
      JSONObject objStatus = WebServices.postAPI(jSONObject.toString(),url);
      boolean statusObj = objStatus.optBoolean("SUCCES", false);
     
}
    /* get start value for list location */
    if (iCommand == Command.SAVE && iErrCode == FRMMessage.NONE) {
        startLocation = PstLocation.findLimitStart(location.getOID(), recordToGet, whereClause);
    }
//update by fitra
    int vectSize = PstLocation.getCountJoin(whereClause);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startLocation = ctrlLocation.actionList(iCommand, startLocation, vectSize, recordToGet);
    }

    /* get record to display */
    Vector listLocation = new Vector(1, 1);

//update by fitra
    listLocation = PstLocation.listJoin(startLocation, recordToGet, whereClause, orderClause);
    if (listLocation.size() < 1 && startLocation > 0) {
        if (vectSize - recordToGet > recordToGet) {
            startLocation = startLocation - recordToGet;
        } else {
            startLocation = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        //update by fitra
        listLocation = PstLocation.listJoin(startLocation, recordToGet, whereClause, orderClause);
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <%@include file="../../styles/plugin_component.jsp" %>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmLocation.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                //window.location="#go";
            <%}%>

                /*------------- start location function ---------------*/
                function cmdAdd()
                {
                    document.frmlocation.hidden_location_id.value="0";
                    document.frmlocation.command.value="<%=Command.ADD%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdAsk(oidLocation)
                {
                    document.frmlocation.hidden_location_id.value=oidLocation;
                    document.frmlocation.command.value="<%=Command.ASK%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdConfirmDelete(oidLocation)
                {
                    document.frmlocation.hidden_location_id.value=oidLocation;
                    document.frmlocation.command.value="<%=Command.DELETE%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdSave()
                {
                    document.frmlocation.command.value="<%=Command.SAVE%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdEdit(oidLocation)
                {
                    document.frmlocation.hidden_location_id.value=oidLocation;
                    document.frmlocation.command.value="<%=Command.EDIT%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdCancel(oidLocation)
                {
                    document.frmlocation.hidden_location_id.value=oidLocation;
                    document.frmlocation.command.value="<%=Command.EDIT%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdBack()
                {
                    document.frmlocation.command.value="<%=Command.BACK%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdListFirst()
                {
                    document.frmlocation.command.value="<%=Command.FIRST%>";
                    document.frmlocation.prev_command.value="<%=Command.FIRST%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdListPrev()
                {
                    document.frmlocation.command.value="<%=Command.PREV%>";
                    document.frmlocation.prev_command.value="<%=Command.PREV%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdListNext()
                {
                    document.frmlocation.command.value="<%=Command.NEXT%>";
                    document.frmlocation.prev_command.value="<%=Command.NEXT%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function cmdListLast()
                {
                    document.frmlocation.command.value="<%=Command.LAST%>";
                    document.frmlocation.prev_command.value="<%=Command.LAST%>";
                    document.frmlocation.action="location.jsp";
                    document.frmlocation.submit();
                }

                function srcContact()
                {
                    window.open("contact_search.jsp?command=<%=Command.FIRST%>&contact_name="+document.frmlocation.contact_name.value,"group", "height=400,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
                }
                /*------------- end location function ---------------*/


                /*------------- start vendor price function -----------------*/
                function addMinimumQty()
                {
                    document.frmlocation.command.value="<%=Command.ADD%>";
                    document.frmlocation.prev_command.value="<%=prevCommand%>";
                    document.frmlocation.action="minimum_qty.jsp";
                    document.frmlocation.submit();
                }

                function editMinimumQty(locationId,minimumOid)
                { 
                    document.frmlocation.hidden_location_id.value=locationId;
                    document.frmlocation.hidden_mat_minimum_id.value=minimumOid;
                    document.frmlocation.command.value="<%=Command.EDIT%>";
                    document.frmlocation.action="minimum_qty.jsp";
                    document.frmlocation.submit();
                }
                
                function showWarehouse() {
                    var x = document.getElementById("<%=FrmLocation.fieldNames[FrmLocation.FRM_FIELD_TYPE]%>").value;
                    if (x == <%=PstLocation.TYPE_LOCATION_STORE%>){
                        document.getElementById("locWarehouse").removeAttribute("style");
                    } else {
                        document.getElementById("locWarehouse").style.display="none";
                    }
                }
                showWarehouse
                /*------------- end vendor price function -----------------*/


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
        <style>
          
			.tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
				font-size: 12px;
				padding: 5px;
				border-color: #DDD;
			}

			.tabel-data tbody tr th {
				text-align: center;
				white-space: nowrap;
				text-transform: uppercase;
			}

			.form-group label {padding-top: 5px}
			.table, form {margin-bottom: 0px}
			.middle-inline {text-align: center; white-space: nowrap}
			th {
                text-align: center;
                font-size: 12px;
            }
            thead.headerList {
                background-color: #3c8dbc;
                color: #fff;
                text-align: center;
            }
            td .table-list {
                font-size: 14px;
                text-align: center;
                border: 1px solid #428bca !important;
            }
            table.dataTable thead > tr > th {
                padding-right: 25px !important;
            }
            th.sorting {
                padding: 0px !important;
                padding-bottom: 7px !important;
                padding-left: 5px !important;
            }
            .line {
                margin-left: 15px;
                border-bottom: 3px solid #cccccc;
            }
            div#example_wrapper {
                margin: auto 1%;
            }
            .content-wrapper {
                min-height: 2206px !important;
            }
            body {
              min-height: 2300px;
          }
        </style>
        <!-- #EndEditable --> 
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
                  <section class="content-header">
                    <h1>Masterdata <small> <%=locationTitle%></small> </h1>
                    <ol class="breadcrumb">
                      <li><%=locationTitle%></li>
                    </ol>
                  </section>
                  <p class="line"></p>
                  <tr> 
                    <td><!-- #BeginEditable "content" --> 
                      <form name="frmlocation" method ="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="start_location" value="<%=startLocation%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
                        <input type="hidden" name="hidden_mat_minimum_id" value="<%=oidMinimum%>">			  
                        <input type="hidden" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_CONTACT_ID]%>" value="<%=location.getContactId()%>">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <%
                            if ((iCommand == Command.ADD) || (iCommand == Command.EDIT)  || (iCommand == Command.ASK) || ((iCommand == Command.SAVE) && iErrCode > 0) || (iCommand == Command.DELETE && iErrCode > 0)) {
                            }else{
                          %>
                          
                          <table id="example" class="table table-striped table-bordered" style="width:100%">
                            <thead>
                              <tr style="background-color: #428bca; color: #fff;">
                                <th><%=textListHeader[SESS_LANGUAGE][0]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][1]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][2]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][3]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][4]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][5]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][6]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][17]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][18]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][10]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][16]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][20]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][21]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][26]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][27]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][28]%></th>
                                <th><%=textListHeader[SESS_LANGUAGE][29]%></th>
                              </tr>
                            </thead>
                            <tbody>
                              <%
                                for (int i = 0; i < listLocation.size(); i++) {
                                  Location loc = (Location) listLocation.get(i);
                                  Date openTime = location.getOpeningTime();
                                  Date closeTime = location.getClosingTime();
                                  String open = "";
                                  String close = "";
                                  if (openTime == null) {
                                    open = "00:00:00";
                                  } else {
                                    open = Formater.formatDate(location.getOpeningTime(), "kk:mm:ss");
                                  }
                                  if (closeTime == null) {
                                    close = "00:00:00";
                                  } else {
                                    close = Formater.formatDate(location.getClosingTime(), "kk:mm:ss");
                                  }
                              %>
                              <tr>
                                <td class="table-list"><%=i + 1%></td>
                                <td class="table-list"><a href="javascript:cmdEdit('<%=loc.getOID()%>')"><%=loc.getCode()%></a></td>
                                <td class="table-list"><%=cekNull(loc.getName())%></td>
                                <td class="table-list"><%=cekNull(loc.getAddress())%></td>
                                <td class="table-list"><%=cekNull(loc.getTelephone())%></td>
                                <td class="table-list"><%=cekNull(loc.getFax())%></td>
                                <td class="table-list"><%=cekNull(loc.getPerson())%></td>
                                <td class="table-list"><%=cekNull(loc.getCompanyName())%></td>
                                <td class="table-list"><%=cekNull(loc.getDepartmentName())%></td>
                                <td class="table-list"><%=PstLocation.fieldLocationType[loc.getType()]%></td>
                                <td class="table-list"><%=loc.getPersentaseDistributionPurchaseOrder()%></td>
                                <td class="table-list"><%=loc.getPriceTypeName()%></td>
                                <td class="table-list"><%=loc.getSymbolCurr()%></td>
                                <td class="table-list"><%=loc.getAcountingEmail()%></td>
                                <td class="table-list"><%=loc.getLocationIp()%></td>
                                <td class="table-list"><%=loc.getSistemAddressHistoryOutlet()%></td>
                                <td class="table-list"><%=open + " - " + close%></td>
                              </tr>
                              <%}%>
                            </tbody>
                          </table>
                            <%}%>
                          <tr align="left" valign="top"> 
                            <td height="8"  colspan="3"> 
                              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <%
                                  if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST
                                          || iCommand == Command.NONE
                                          || iCommand == Command.BACK
                                          || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {
                                %>					  
                                <tr align="left" valign="top"> 
                                  <td> 
                                    <%if (((iCommand != Command.ADD) && (iCommand != Command.EDIT) && (iCommand != Command.ASK)) || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {%>
                                    <table cellpadding="0" cellspacing="0" border="0">
                                      <tr> 
                                        <%if (privAdd) {%>
                                      <br><a class="btn-sm btn-primary" id="add" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%></a>
                                      <%}%>
                                </tr>
                              </table>
                              <%}%>
                            </td>
                          </tr>
                          <%}%>					  
                        </table>
                    </td>
                  </tr>

                  <tr align="left" valign="top"> 
                    <td height="8" valign="middle" colspan="3"> 
                      <%
                        if ((iCommand == Command.ADD) || (iCommand == Command.EDIT)  || (iCommand == Command.ASK) || ((iCommand == Command.SAVE) && iErrCode > 0) || (iCommand == Command.DELETE && iErrCode > 0)) {
                      %>
                      <table id="add" width="100%" border="0" cellspacing="2" cellpadding="2">
                        <tr> 
                          <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor " + locationTitle : locationTitle + " Editor"%></u></td>
                        </tr>
                        <tr> 
                          <td height="100%" width="100%" colspan="2"> 
                            <table border="0" cellspacing="1" cellpadding="1" width="100%">
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_CODE]%>"  value="<%= location.getCode()%>" class="formElemen" size="20">
                                  * <%= frmLocation.getErrorMsg(FrmLocation.FRM_FIELD_CODE)%> </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_NAME]%>"  value="<%= location.getName()%>" class="formElemen" size="40">
                                  * <%= frmLocation.getErrorMsg(FrmLocation.FRM_FIELD_NAME)%></td>
                              </tr>
                              <tr align="left"> 
                                <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <textarea name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_ADDRESS]%>" class="formElemen" cols="39" rows="2" wrap="VIRTUAL"><%= location.getAddress()%></textarea>
                                  * <%= frmLocation.getErrorMsg(FrmLocation.FRM_FIELD_ADDRESS)%> </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_TELEPHONE]%>"  value="<%= location.getTelephone()%>" class="formElemen" size="20">
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_FAX]%>"  value="<%= location.getFax()%>" class="formElemen" size="20">
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_PERSON]%>"  value="<%= location.getPerson()%>" class="formElemen" size="40">
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_EMAIL]%>"  value="<%= location.getEmail()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_WEBSITE]%>"  value="<%= location.getWebsite()%>" class="formElemen" size="30">
                                </td>
                              </tr>

                              <!-- update by fitra -->
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][17]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <% Vector company_value = new Vector();
                                    Vector company_key = new Vector();
                                    company_key.add("- Select -");
                                    company_value.add("" + 0);
                                    Vector vlistCompany = PstCompany.list(0, 0, "", "");
                                    if (vlistCompany != null && vlistCompany.size() > 0) {
                                      for (int x = 0; x < vlistCompany.size(); x++) {
                                        Company company = (Company) vlistCompany.get(x);
                                        company_key.add(company.getCompanyName());
                                        company_value.add("" + company.getOID());
                                      }
                                    }
                                  %>
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_COMPANY_ID], null, "" + location.getCompanyId(), company_value, company_key)%> 
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][18]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <% Vector department_value = new Vector();
                                    Vector department_key = new Vector();
                                    Vector vlistDepartment = PstDepartment.list(0, 0, "", "");
                                    department_key.add("No Departement");
                                    department_value.add("0");
                                    if (vlistDepartment != null && vlistDepartment.size() > 0) {
                                      for (int x = 0; x < vlistDepartment.size(); x++) {
                                        Department department = (Department) vlistDepartment.get(x);
                                        department_key.add(department.getDepartment());
                                        department_value.add("" + department.getOID());
                                      }
                                    }
                                  %>
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_DEPARTMENT_ID], null, "" + location.getDepartmentId(), department_value, department_key)%> 
                                </td>
                              </tr>
                              <tr align="left"> 
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][19]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <% Vector reportGroup_value = new Vector();
                                    Vector reportGroup_key = new Vector();
                                    reportGroup_key.add("Default/Shown On Report");
                                    reportGroup_value.add("" + 0);
                                    reportGroup_key.add("Food Beverage");
                                    reportGroup_value.add("" + 1);
                                    reportGroup_key.add("Other Outlet");
                                    reportGroup_value.add("" + 2);
                                    reportGroup_key.add("Not Shown On Report DRR");
                                    reportGroup_value.add("" + 3);
                                  %>
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_REPORT_GROUP], null, "" + location.getReportGroup(), reportGroup_value, reportGroup_key)%> 
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][10]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <%
                                    Vector type_value = new Vector(1, 1);
                                    Vector type_key = new Vector(1, 1);
                                    String sel_type = "" + location.getType();
                                    type_value.add(PstLocation.fieldLocationType[PstLocation.TYPE_LOCATION_WAREHOUSE]);
                                    type_key.add("" + PstLocation.TYPE_LOCATION_WAREHOUSE);
                                    type_value.add(PstLocation.fieldLocationType[PstLocation.TYPE_LOCATION_STORE]);
                                    type_key.add("" + PstLocation.TYPE_LOCATION_STORE);
                                    type_value.add(PstLocation.fieldLocationType[PstLocation.TYPE_DUTY_FREE]);
                                    type_key.add("" + PstLocation.TYPE_DUTY_FREE);
                                    type_value.add(PstLocation.fieldLocationType[PstLocation.TYPE_LOCATION_PABEAN]);
                                    type_key.add("" + PstLocation.TYPE_LOCATION_PABEAN);
                                  %>
                                  <%= ControlCombo.draw(frmLocation.fieldNames[FrmLocation.FRM_FIELD_TYPE], null, sel_type, type_key, type_value, "onchange=\"showWarehouse()\"", "formElemen")%> </td>
                              </tr>
                              <tr align="left"> 
                                <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][12]%><br>ID Mesin Data (Max. 3 Number)</td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top"> 
                                  <textarea name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_DESCRIPTION]%>" class="formElemen" cols="39" rows="2" wrap="VIRTUAL"><%= location.getDescription()%></textarea>
                                </td>
                              </tr>
                              <%--add opie 13-06-2012 untuk penambahan percentase tax dan service --%>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][13]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_TAX_PERCENT]%>"  value="<%= location.getTaxPersen()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][14]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_SERVICE_PERCENT]%>"  value="<%= location.getServicePersen()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][33]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_MAX_EXCHANGE_DAY]%>"  value="<%= location.getMaxExchangeDay()%>" class="formElemen" size="5">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][15]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <%
                                    Vector tsdValue = new Vector(1, 1);
                                    Vector tsdKey = new Vector(1, 1);

                                    tsdKey.add(PstLocation.tsdNames[SESS_LANGUAGE][PstLocation.TSD_INCLUDE_NOTCHANGABLE]);
                                    tsdValue.add("" + PstLocation.TSD_INCLUDE_NOTCHANGABLE);

                                    tsdKey.add(PstLocation.tsdNames[SESS_LANGUAGE][PstLocation.TSD_NOTINCLUDE_NOTCHANGABLE]);
                                    tsdValue.add("" + PstLocation.TSD_NOTINCLUDE_NOTCHANGABLE);

                                    tsdKey.add(PstLocation.tsdNames[SESS_LANGUAGE][PstLocation.TSD_INCLUDE_CHANGABLE]);
                                    tsdValue.add("" + PstLocation.TSD_INCLUDE_CHANGABLE);

                                    tsdKey.add(PstLocation.tsdNames[SESS_LANGUAGE][PstLocation.TSD_NOTINCLUDE_CHANGABLE]);
                                    tsdValue.add("" + PstLocation.TSD_NOTINCLUDE_CHANGABLE);

                                  %> <%=ControlCombo.draw(frmLocation.fieldNames[FrmLocation.FRM_FIELD_TAX_SERVICE_DEFAULT], "formElemen", null, "" + location.getTaxSvcDefault(), tsdValue, tsdKey, null)%></td>
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][20]%></td>
                                <td width="1%" valign="top">:</td>
                                <%
                                  Vector price_type_value = new Vector();
                                  Vector price_type_key = new Vector();
                                  Vector vlistPriceType = PstPriceType.list(0, 0, "", "");
                                  if (vlistPriceType != null && vlistPriceType.size() > 0) {
                                    for (int x = 0; x < vlistPriceType.size(); x++) {
                                      PriceType priceType = (PriceType) vlistPriceType.get(x);
                                      price_type_key.add(priceType.getName());
                                      price_type_value.add("" + priceType.getOID());
                                    }
                                  }
                                %>
                                <td width="87%" valign="top">
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_PRICE_TYPE_ID], null, "" + location.getPriceTypeId(), price_type_value, price_type_key)%> 
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][31]%></td>
                                <td width="1%" valign="top">:</td>
                                <%
                                  Vector discount_type_value = new Vector();
                                  Vector discount_type_key = new Vector();
                                  Vector vlistDiscountType = PstDiscountType.list(0, 0, "", "");
                                  if (vlistDiscountType != null && vlistDiscountType.size() > 0) {
                                    for (int x = 0; x < vlistDiscountType.size(); x++) {
                                      DiscountType discountType = (DiscountType) vlistDiscountType.get(x);
                                      discount_type_key.add(discountType.getName());
                                      discount_type_value.add("" + discountType.getOID());
                                    }
                                  }
                                %>
                                <td width="87%" valign="top">
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_DISCOUNT_TYPE_ID], null, "" + location.getDiscountTypeId(), discount_type_value, discount_type_key)%> ( untuk mendapatkan default diskon reguler )
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][32]%></td>
                                <td width="1%" valign="top">:</td>
                                <%
                                  Vector member_group_value = new Vector();
                                  Vector member_group_key = new Vector();
                                  Vector vlistMemberGroup = PstMemberGroup.list(0, 0, "", "");
                                  if (vlistMemberGroup != null && vlistMemberGroup.size() > 0) {
                                    for (int x = 0; x < vlistMemberGroup.size(); x++) {
                                      MemberGroup memberGroup = (MemberGroup) vlistMemberGroup.get(x);
                                      member_group_key.add(memberGroup.getName());
                                      member_group_value.add("" + memberGroup.getOID());
                                    }
                                  }
                                %>
                                <td width="87%" valign="top">
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_MEMBER_GROUP_ID], null, "" + location.getMemberGroupId(), member_group_value, member_group_key)%> ( untuk mendapatkan default diskon quantity )
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][21]%></td>
                                <td width="1%" valign="top">:</td>
                                <%
                                  Vector standart_value = new Vector();
                                  Vector standart_key = new Vector();
                                  Vector vStandart = PstStandartRate.listCurrStandard();
                                  if (vStandart != null && vStandart.size() > 0) {
                                    for (int x = 0; x < vStandart.size(); x++) {
                                      Vector vListStandart = (Vector) vStandart.get(x);
                                      CurrencyType currencyType = (CurrencyType) vListStandart.get(0);
                                      StandartRate standartRate = (StandartRate) vListStandart.get(1);
                                      standart_key.add(currencyType.getCode());
                                      standart_value.add("" + standartRate.getOID());
                                    }
                                  }
                                %>
                                <td width="87%" valign="top">
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_STANDARTD_ID], null, "" + location.getStandarRateId(), standart_value, standart_key)%> 
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][21]%></td>
                                <td width="1%" valign="top">:</td>
                                <% Vector table_value = new Vector();
                                  Vector table_key = new Vector();
                                  table_key.add("No");
                                  table_value.add("" + 0);
                                  table_key.add("Yes");
                                  table_value.add("" + 1);
                                %>
                                <td width="87%" valign="top">
                                  <%= ControlCombo.draw(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_USE_TABLE], null, "" + location.getUseTable(), table_value, table_key)%> 
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][16]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER]%>"  value="<%= location.getPersentaseDistributionPurchaseOrder()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][26]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_ACCOUNTING_EMAIL]%>"  value="<%= location.getAcountingEmail()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][27]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_LOCATION_IP]%>"  value="<%= location.getLocationIp()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][28]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <input type="text" name="<%=frmLocation.fieldNames[FrmLocation.FRM_FIELD_SISTEM_ADDRESS_HISTORY_OUTLET]%>"  value="<%= location.getSistemAddressHistoryOutlet()%>" class="formElemen" size="30">
                                </td>
                              </tr>
                              <tr align="left">

                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][29]%></td>
                                <td width="1%" valign="top">:</td>

                                <td height="21" colspan="2" width="83%"><%= ControlDate.drawTime(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_OPENING_TIME], location.getOpeningTime(), 1)%></td>

                              <tr align="left">

                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][30]%></td>
                                <td width="1%" valign="top">:</td>

                                <td height="21" colspan="2" width="83%"><%= ControlDate.drawTime(FrmLocation.fieldNames[FrmLocation.FRM_FIELD_CLOSING_TIME], location.getClosingTime(), 1)%></td>
                                <%
                                  String styleDisplay = "style='display: none;'";
                                  if (location.getType() == PstLocation.TYPE_LOCATION_STORE) {
                                    styleDisplay = "";
                                  }
                                %>
                              <tr align="left" <%=styleDisplay%> id="locWarehouse">
                                <td width="12%">Location Warehouse</td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <%
                                    Vector obj_locationid = new Vector(1, 1);
                                    Vector val_locationid = new Vector(1, 1);
                                    Vector key_locationid = new Vector(1, 1);
                                    String whereClauseLoc = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;

                                    Vector vt_loc = PstLocation.list(0, 0, whereClauseLoc, "");

                                    val_locationid.add(String.valueOf(0));
                                    key_locationid.add("-");
                                    for (int d = 0; d < vt_loc.size(); d++) {
                                      Location loc = (Location) vt_loc.get(d);
                                      val_locationid.add("" + loc.getOID() + "");
                                      key_locationid.add(loc.getName());
                                    }
                                  %>                 
                                  <%=ControlCombo.draw(frmLocation.fieldNames[FrmLocation.FRM_FIELD_PARENT_LOCATION_ID], null, "" + location.getParentLocationId(), val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "formElemen")%>
                                </td>
                              </tr>    
                              <%if (iCommand == Command.EDIT) {%>
                              <tr align="left">
                                <td width="12%"><%=textListHeader[SESS_LANGUAGE][22]%></td>
                                <td width="1%" valign="top">:</td>
                                <td width="87%" valign="top">
                                  <button type="button" id="btnFooterText"><%=textListHeader[SESS_LANGUAGE][23]%></button>
                                </td>
                              </tr>
                              <%}%>
                              <%-- finish eyek 13-06-2012--%>
                            </table>
                          </td>
                        </tr>
                        <tr align="left" valign="top" > 
                          <td colspan="2" class="command"> 
                            <%
                              ctrLine.setLocationImg(approot + "/images");

                              // set image alternative caption
                              ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_SAVE, true));
                              ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) + " List");
                              ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ASK, true));
                              ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_CANCEL, false));

                              ctrLine.initDefault();
                              ctrLine.setTableWidth("100%");
                              String scomDel = "javascript:cmdAsk('" + oidLocation + "')";
                              String sconDelCom = "javascript:cmdConfirmDelete('" + oidLocation + "')";
                              String scancel = "javascript:cmdEdit('" + oidLocation + "')";
                              ctrLine.setCommandStyle("command");
                              ctrLine.setColCommStyle("command");

                              // set command caption
                              ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_SAVE, true));
                              ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) + " List");
                              ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ASK, true));
                              ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_DELETE, true));
                              ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_CANCEL, false));

                              if (privDelete) {
                                ctrLine.setConfirmDelCommand(sconDelCom);
                                ctrLine.setDeleteCommand(scomDel);
                                ctrLine.setEditCommand(scancel);
                              } else {
                                ctrLine.setConfirmDelCaption("");
                                ctrLine.setDeleteCaption("");
                                ctrLine.setEditCaption("");
                              }

                              if (iCommand == Command.EDIT && privUpdate == false) {
                                ctrLine.setSaveCaption("");
                              }

                              if (privAdd == false) {
                                ctrLine.setAddCaption("");
                              }
                            %>
                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                        </tr>
                      </table>
                      <%}%>

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
        <%if (menuUsed == MENU_ICON) {%>
        <%@include file="../../styletemplate/footer.jsp" %>

        <%} else {%>
        <%@ include file = "../../main/footer.jsp" %>
        <%}%>
        <!-- #EndEditable --> </td>
</tr>
</table>
  <script>
      $(document).ready( function () {
    $('#example').DataTable({
			"ordering": true
    });
} );
  </script>
</body>
<script type="text/javascript">
        $(document).ready(function(){
            $('#btnFooterText').click(function(){
                $('#modalReport').modal('show');
            });
        });
</script>
<%
    /* get data from request form */
    int cmdNota = FRMQueryString.requestInt(request, "cmdNotaSetting");
    long notaSettingOid = FRMQueryString.requestLong(request, "notaSettingOid");

    NotaSetting notaSetting = new NotaSetting();
    CtrlNotaSetting ctrlNotaSetting = new CtrlNotaSetting(request);
    ctrlNotaSetting.action(cmdNota, notaSettingOid);

    String whereNotaSetting = " " + PstNotaSetting.fieldNames[PstNotaSetting.FLD_LOCATION_ID] + "= " + oidLocation + "";
    Vector listNotaSetting = PstNotaSetting.list(0, 0, whereNotaSetting, "");
    if (listNotaSetting.size() > 0) {
        notaSetting = (NotaSetting) listNotaSetting.get(0);
    }

%>
<div id="modalReport" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">Footer Text</h4>
            </div>
            <form name="frmNotaSetting" action="location.jsp" method="post">
                <div class="modal-body" id="modal-body">
                    <table style="width:100%">
                        <input type="hidden" name="cmdNotaSetting" value="<%=Command.SAVE%>">
                        <input type="hidden" name="notaSettingOid" value="<%= notaSetting.getOID()%>">
                        <input  type="hidden" name ="<%= FrmNotaSetting.fieldNames[FrmNotaSetting.FRM_FLD_LOCATION_ID]%>" value="<%= oidLocation%>">
                        <tr>
                            <td style="width:30%">Location</td>
                            <td style="width:5%">:</td>
                            <td style="width:65%"><input class="form-control"  type="text" readonly="readonly" value="<%= location.getName()%>"></td>
                        </tr>
                        <tr>
                            <td style="width:30%">Text</td>
                            <td style="width:5%"></td>
                            <td style="width:65%"><textarea class="form-control" name="<%= FrmNotaSetting.fieldNames[FrmNotaSetting.FRM_FLD_FOOTER_TEXT]%>"><%= notaSetting.getFooterText()%></textarea></td>
                        </tr>

                    </table>
                </div>
                <div class="modal-footer">
                    <button  type="submit" class="btn btn-primary"><%=textListHeader[SESS_LANGUAGE][24]%></button>
                    <button type="button" data-dismiss="modal" class="btn btn-danger"><%=textListHeader[SESS_LANGUAGE][25]%></button>
                </div>
            </form>
        </div>
    </div>
</div>
</html>

