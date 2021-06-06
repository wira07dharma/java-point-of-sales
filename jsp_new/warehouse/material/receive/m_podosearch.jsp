<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.common.entity.payment.PstCurrencyType" %>
<%@ page import = "java.lang.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<!-- package qdep -->
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textPurchaseOrderHeader[][] = {
	{"Bulan,Tahun Order"},
	{"PO Month"}
};

/* this constant used to list text of listHeader */
public static final String textListPurchaseOrderHeader[][] = { 
	{"No","Kode","Tanggal","Status","Keterangan","Mata Uang"},
	{"No","Code","Date","Status","Remark","Currency"}
};

public String drawListPurchaseOrder(int language,Vector objectClass,int start,I_DocStatus i_status,int docType) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListPurchaseOrderHeader[language][0],"3%");	
		ctrlist.addHeader(textListPurchaseOrderHeader[language][1],"15%");	
		ctrlist.addHeader(textListPurchaseOrderHeader[language][2],"15%");
                ctrlist.addHeader(textListPurchaseOrderHeader[language][5],"10%");
		ctrlist.addHeader(textListPurchaseOrderHeader[language][3],"15%");
		ctrlist.addHeader(textListPurchaseOrderHeader[language][4],"20%");	
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
		
		if(start<0) start = 0;
			
		for(int i=0; i<objectClass.size(); i++) {
			//Vector vt = (Vector)objectClass.get(i);			
			PurchaseOrder po = (PurchaseOrder)objectClass.get(i);
			start = start + 1;			
	
			Vector rowx = new Vector();
			
			String str_dt_PurchDate = ""; 
			try	{
				Date dt_PurchDate = po.getPurchDate();
				if(dt_PurchDate==null) {
					dt_PurchDate = new Date();
				}	
				str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
				str_dt_PurchDate = "";
			}
			
            CurrencyType currencyType = new CurrencyType();
            try {
                currencyType = PstCurrencyType.fetchExc(po.getCurrencyId());
            }
			catch(Exception e) {
			}
			
			rowx.add(""+start);
			rowx.add(po.getPoCode());
			rowx.add(str_dt_PurchDate);
            rowx.add(currencyType.getCode());
			rowx.add(i_status.getDocStatusName(docType,po.getPoStatus()));
			rowx.add(po.getRemark());
			
			lstData.add(rowx);
			lstLinkData.add(po.getOID()+"','"+po.getPoCode()+"','"+currencyType.getCode()+"','"+po.getCurrencyId()
							+"', '"+po.getPoStatus()+"', '"+po.getSupplierId()+"', '"+po.getPpn()+"', '"+po.getLocationId()+"', '"+po.getTermOfPayment()+"','"+po.getCreditTime()+"','"+po.getIncludePpn()+"','"+po.getExchangeRate());
                       // lstLinkData.add(po.getOID()+"','"+po.getPoCode()+"','"+currencyType.getCode()+"','"+po.getCurrencyId()
							//+"', '"+po.getPoStatus()+"', '"+po.getSupplierId()+"', '"+po.getPpn()+"', '"+po.getLocationId()+"', '"+po.getIncludePpn());
		}
		return ctrlist.drawBootstrap();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian...</div>";				
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long oidVendor = FRMQueryString.requestLong(request,"oidVendor");
String poCode = FRMQueryString.requestString(request,"po_code");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int month_combo = FRMQueryString.requestInt(request, "month_combo");
int year_combo = FRMQueryString.requestInt(request, "year_combo");
int recordToGet = 15;
int monthOfPO = month_combo;
int yearOfPO = year_combo;
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
String pageTitle = "Pencarian Order Pembelian";

if (monthOfPO == 0) {
	Date asu = new Date();
	monthOfPO = asu.getMonth() + 1;
}
if (yearOfPO == 0) {
	Date asulagi = new Date();
	yearOfPO = asulagi.getYear() + 1900;	
}
String whereClause = "";
if(poCode == ""){
//String whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] + " = " + oidVendor +
  whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] + " = " + oidVendor +
		" AND Month(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + monthOfPO +
		" AND Year(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + yearOfPO ;

//Po yang ditampilkan hanya final dan closed saja
      /* whereClause += " AND (" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 2 " +
                     " OR " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 7 )";*/
   whereClause += " AND (" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 2 )";
}
if(poCode != "") {
	//whereClause += " OR " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + " like '%" + poCode + "%'";
        whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + " like '%" + poCode + "%'";
}

int vectSize = PstPurchaseOrder.getCount(whereClause);

CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	start = ctrlPurchaseOrder.actionList(iCommand, start, vectSize, recordToGet);
} 
Vector vect = PstPurchaseOrder.list(start, recordToGet, whereClause, "");

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title
><script language="JavaScript">
function cmdEdit(poOid, poCode, currcode, currOid, poStatus, supplierId, ppn, locationId,termsPay,creditTime,includePpn,exchangeRate) {
	if(poStatus != <%=I_DocStatus.DOCUMENT_STATUS_POSTED%>) {
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>.value = poOid;
		self.opener.document.forms.frm_recmaterial.txt_ponumber.value = poCode;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]%>.value = currOid;
		/*self.opener.document.forms.frm_recmaterial.CURRENCY_CODE.value = currOid;*/
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>.value = supplierId;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>.value = ppn;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value = locationId;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>.value = termsPay;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>.value = creditTime;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value = exchangeRate;
                //self.opener.document.forms.frm_recmaterial.exchangeRate.value = exchangeRate;
                //self.opener.document.forms.frm_recmaterial.<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>.value = includePpn;
		self.close();
	}
	else {
		alert("Document has been posted!");
	}
}




function cmdListFirst() {
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}	

function clear(){
	document.frmvendorsearch.txt_materialcode.value="";
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

<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
        
        

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->


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
</head>

<body class="skin-blue">
        <%@ include file = "../../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small><%=pageTitle%> </small>
                    </h1>
                    <hr size="1">
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmcashcashier" method ="post" action=""  role="form">
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                    <div class="row">
                                            <div class="col-md-12">
                                                
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textPurchaseOrderHeader[SESS_LANGUAGE][0]%></label><br />
                                                     <% 
                                                                Vector obj_monthid = new Vector(1,1); 
                                                                Vector val_monthid = new Vector(1,1); 
                                                                Vector key_monthid = new Vector(1,1); 
                                                                Date jani = new Date();
                                                                int bulan_awal = jani.getMonth()+1;
                                                                for(int d = 1;d < 13; d++)
                                                                {
                                                                        val_monthid.add(""+d);
                                                                        key_monthid.add(""+d);
                                                                }
                                                                String select_monthid = "";
                                                                if (month_combo != 0)
                                                                {
                                                                        select_monthid = ""+month_combo; //selected on combo box
                                                                }
                                                                else
                                                                {
                                                                        select_monthid = ""+bulan_awal; //selected on combo box
                                                                }	
                                                     %>
                                                     <%=ControlCombo.drawBootsratap("month_combo", null, select_monthid, val_monthid, key_monthid, "", "form-control-date")%>
                                                          ,
                                                          <% 
                                                                Vector obj_yearid = new Vector(1,1); 
                                                                Vector val_yearid = new Vector(1,1); 
                                                                Vector key_yearid = new Vector(1,1); 
                                                                Date sekarang = new Date();
                                                                int tahun_awal = sekarang.getYear() + 1900 -2;
                                                                for(int d = 0;d < 5; d++)
                                                                {
                                                                        val_yearid.add(""+(d+tahun_awal));
                                                                        key_yearid.add(""+(d+tahun_awal));
                                                                }
                                                                String select_yearid = "";
                                                                if (year_combo != 0)
                                                                {
                                                                        select_yearid = ""+year_combo; //selected on combo box
                                                                }
                                                                else
                                                                {
                                                                        select_yearid = ""+(String)key_yearid.get(2); //selected on combo box
                                                                }	
                                                          %>
                                                          <%=ControlCombo.drawBootsratap("year_combo", null, select_yearid, val_yearid, key_yearid, "", "form-control-date")%>
                                                    
                                                </div>

                                                
                                                
                                                
                                            </div>
                                        <!-- end of col-md-12-->
                                        
                                            
                                    </div>
                                
                                
                               <br />
                                    <div class="box-footer">
                                            <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                          
                                    </div>
                               
                               <br />
                               
                                    <div class="row">
                                                 <div class="col-md-12">
                                                     
                                                     <%=drawListPurchaseOrder(SESS_LANGUAGE,vect,start,i_status,docType)%>

                                                 </div>
                                    </div>
                                            <br />         
                                     <div class="row">
                                                 <div class="col-md-12">
                                                     
                                                    <%
                                                            ControlLine ctrLine = new ControlLine();
                                                            ctrLine.setLocationImg(approot+"/images");
                                                            ctrLine.initDefault();
                                                            out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                                                    %> 

                                                 </div>
                                    </div>                 
                                                     
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
       <script src="../../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>       

    </body>
</html>
