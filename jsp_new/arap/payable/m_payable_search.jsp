<%-- 
    Document   : m_payable_search
    Created on : Apr 27, 2015, 11:22:07 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.search.SrcReportReceive"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<% 
/* 
 * Page Name  		:  payable_view.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
 
<%@ page language="java" %>
<!-- package java -->
<%@ page import="java.util.*"%>
<!-- package qdep -->
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.qdep.entity.*"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<!-- package project -->
<%@ page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@ page import="com.dimata.posbo.session.arap.SessAccPayable"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static final String textListHeader[][] = {
	{"Periode","Nomor Invoice","Tanggal Invoice","Mata Uang","Urut Berdasar","dari","s/d","Cari Rekap Utang","Gudang","Penerimaan Barang","Rekap Utang","Status", "Tanggal Pembayaran","Lokasi"},
	{"Periode","Invoice Number","Invoice Date","Currency","Order By","from","to","Search List AP","Warehouse","Receive Goods","AP Summary","Status", "Date Payment","Location"}
};

public boolean getTruedFalse(Vector vect, int index) {
	for(int i=0;i<vect.size();i++) {
		int iStatus = Integer.parseInt((String)vect.get(i));
		if(iStatus==index)
			return true;
	}
	return false;
}
%>

<!-- JSP Block -->
<%
//for status
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
//end of status

int iCommand = FRMQueryString.requestCommand(request);
SrcAccPayable srcAccPayable = new SrcAccPayable();
FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable();	
SrcReportReceive srcReportReceive =  new SrcReportReceive();
try {
	srcAccPayable = (SrcAccPayable)session.getValue(SessAccPayable.SESS_ACC_PAYABLE);
}
catch(Exception e) {
	srcAccPayable = new SrcAccPayable();
}

if(srcAccPayable==null) {
	srcAccPayable = new SrcAccPayable();
}

try {
	session.removeValue(SessAccPayable.SESS_ACC_PAYABLE);
}
catch(Exception e) {
}

%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
        <script language="JavaScript">
        function cmdSearch() {
                document.frmSrcOpname.command.value="<%=Command.LIST%>";
                document.frmSrcOpname.action="payable_view.jsp";
                document.frmSrcOpname.submit();
        }

        //-------------- script control line -------------------
        function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
        }

        function MM_swapImage() { //v3.0
                        var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                        if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                }
        </script>
<!-- #EndEditable --> 
</head> 

<body class="skin-blue">
        <%@ include file = "../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar --> 
            <%@ include file = "../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>Dashboard
                        <small> <%=textListHeader[SESS_LANGUAGE][8]%> &gt; <%=textListHeader[SESS_LANGUAGE][9]%> &gt; <%=textListHeader[SESS_LANGUAGE][10]%></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmSrcOpname" method="post">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListHeader[SESS_LANGUAGE][0]%></label><br>
                                                <%=ControlDate.drawDate(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM], srcReportReceive.getDateFrom(),"formElemen",1,-5)%><%=textListHeader[SESS_LANGUAGE][6]%><%=ControlDate.drawDate(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO], srcReportReceive.getDateTo(),"formElemen",1,-5) %>
                                           </div>
                                           <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListHeader[SESS_LANGUAGE][13]%></label><br>
                                                <%
                                                  Vector obj_locationid = new Vector(1,1);
                                                  Vector val_locationid = new Vector(1,1);
                                                  Vector key_locationid = new Vector(1,1);
                                                  //add opie-eyek
                                                  //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                  String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                                       " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                                  whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                  Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                                    for(int d=0;d<vt_loc.size();d++){
                                                        Location loc = (Location)vt_loc.get(d);
                                                        val_locationid.add(""+loc.getOID()+"");
                                                        key_locationid.add(loc.getName());
                                                    }
                                                %>
                                                <%=ControlCombo.drawBootsratap(frmSrcAccPayable.fieldNames[frmSrcAccPayable.FRM_FIELD_LOCATION], null, "", val_locationid, key_locationid, " tabindex=\"1\"", "form-control")%> 
                                           </div>
                                    </div>
                                    <div class="col-md-6">
                                           
                                    </div>
                                </div>
                                <div class="row">
                                </div>
                                <div class="box-footer">
                                    <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                    <button type="submit" onclick="javascript:cmdAdd()" class="btn btn-primary pull-right" >Add New</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>

