<%-- 
    Document   : print_invoice
    Created on : Feb 9, 2015, 7:13:02 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%-- 
    Document   : cashier_lyt_m
    Created on : Aug 14, 2014, 3:48:48 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberReg"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="org.apache.poi.ss.formula.functions.Now"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillDetail"%>
<%@page import="com.dimata.pos.form.billing.FrmBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.util.Command"%>
<%-- 
    Document   : src_list_open_bill_m
    Created on : Aug 13, 2014, 2:57:12 PM
    Author     : dimata005
--%>
<!DOCTYPE html>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@include file = "../main/checkuser.jsp" %>
<%@include file = "checkusercashieronline.jsp" %>
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal Buat","Supplier","Contact","Alamat","Telp.","Terms","Days","PPn","Ket.","Mata Uang","Gudang","Order Barang","Include","%","Rate","Kategori","Status","Tanggal Pengiriman"},
    {"No","Location","Create Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Order","Include","%","Rate","Category","Status","Delivery Date"}
};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?","Qty Tidak Boleh Kosong","Unit beli dan Unit Stock berbeda, Tidak ada konversi antara unit tersebut, Silahkan Setting di masterdata unit"},
    {"Are You Sure to Delete This Data? ","Qty Not Allow Empty","Unit Buying and Unit Stock is different. No conversion between these units, please Setting in Masterdata unit"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Kode","Nama","Harga","Tipe Pot.Harga","Pot.Harga","Jumlah Request","Unit","Qty Kirim","Jumlah Kirim","Total"},//18
    {"No","Code","Name","Price","Disc Type","Discount","Qty Request","Unit","Qty Delivery","Qty Issue", "Total"}  
};

/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand, long oidOpenBill,Vector objectMaterialIssue, long hidden_cash_bill_detail_id, String values) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItem[language][0],"3%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"12%");//kode
    ctrlist.addHeader(textListOrderItem[language][2],"25%");//nama
  
    ctrlist.addHeader("<center>"+textListOrderItem[language][6]+"</center>","5%");//jumlah request
    ctrlist.addHeader("<center>"+textListOrderItem[language][7]+"</center>","5%");//unit
    ctrlist.addHeader("<center>"+textListOrderItem[language][9]+"</center>","5%");//jumlah request

    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    
    rowx = new Vector();
    
    /*
    if(objectMaterialIssue.size()>0){
        int count=0;
        for(int i=0; i<objectMaterialIssue.size(); i++) {
            Vector vBillDetail =(Vector) objectMaterialIssue.get(i);
            Billdetail billdetail = (Billdetail) vBillDetail.get(0);
            Unit unit = (Unit) vBillDetail.get(1);
            count=count+1;
            rowx = new Vector();
            rowx.add(""+count+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+billdetail.getOID()+"')\">"+billdetail.getSku()+"</a>");
            rowx.add(""+billdetail.getItemName());

                if(1>2){
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                }

                rowx.add(""+billdetail.getQty()+"</div>");
                rowx.add(""+unit.getCode()+"</div>");

                double qtyIssue=0;
                qtyIssue=billdetail.getQty();
                rowx.add("<div align=\"center\">"+qtyIssue+"</div>");

                if(1>2){
                    rowx.add("");
                }

                lstData.add(rowx); 
            

        }
    }*/
    String tempValue[] = values.split(";");
    if (tempValue.length>0){
        for (int i = 0; i<tempValue.length; i++){
            int count = i + 1;
            String newTemp []=tempValue[i].split(":");
            long oidDetail = Long.parseLong(newTemp[0]);
            double qty = Double.valueOf(newTemp[1]);
            Billdetail billdetail = new Billdetail();
            try {
                billdetail = PstBillDetail.fetchExc(oidDetail);
            } catch (Exception e) {
            }
            Unit unit = new Unit();
            try {
                unit = PstUnit.fetchExc(billdetail.getUnitId());
            } catch (Exception e) {
            }
            
            rowx = new Vector();
            rowx.add(""+count+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+billdetail.getOID()+"')\">"+billdetail.getSku()+"</a>");
            rowx.add(""+billdetail.getItemName());
            rowx.add(""+billdetail.getQtyRequestSo()+"</div>");
            rowx.add(""+unit.getCode()+"</div>");
            rowx.add("<div align=\"center\">"+qty+"</div>");
            
            lstData.add(rowx);
        }
    }
    
    return ctrlist.drawBootstrap();
}


public String drawListPoItemNew(int language,int iCommand,Vector objectMaterial,FrmBillDetail frmBillDetail, long oidOpenBill,Vector objectMaterialIssue) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItem[language][0],"3%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"12%");//kode
    ctrlist.addHeader(textListOrderItem[language][2],"25%");//nama
    if(1>2){
        ctrlist.addHeader(textListOrderItem[language][3],"10%");//Harga
        ctrlist.addHeader(textListOrderItem[language][4],"10%");//Tipe Pot
        ctrlist.addHeader(textListOrderItem[language][5],"10%");//pot harga
    }
    ctrlist.addHeader("<center>"+textListOrderItem[language][6]+"</center>","5%");//jumlah request
    ctrlist.addHeader("<center>"+textListOrderItem[language][7]+"</center>","5%");//unit
    ctrlist.addHeader("<center>"+textListOrderItem[language][9]+"</center>","5%");//jumlah request
    
    if(1>2){
        ctrlist.addHeader(textListOrderItem[language][10],"5%");//total
    }
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    
    int start=0;
    
    rowx = new Vector();
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmBillDetail.errorSize()>0)){
        
        rowx.add("<input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_ID]+"\" value=\"\">");
        rowx.add("<div align=\"left\"></div><input type=\"text\" name=\"matCode\" value=\"\" onchange=\"myFunction(this.value)\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<div align=\"left\"></div><input type=\"text\" name=\"matItem\" value=\"\">");
        rowx.add("<div align=\"center\"><input type=\"text\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]+"\" value=\"\"></div>");
        rowx.add("<div align=\"center\"><input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_UNIT_ID]+"\" value=\"\"></div>");
        rowx.add("<div align=\"center\"><input type=\"text\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]+"\" value=\"\"></div>");        
        
        
        lstData.add(rowx);
    }
    
    
    return ctrlist.drawBootstrap();
}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidOpenbillmain = FRMQueryString.requestLong(request,"hidden_bill_main_id");
long hidden_cash_bill_detail_id = FRMQueryString.requestLong(request,"hidden_cash_bill_detail_id");
long oidBillMain = FRMQueryString.requestLong(request,"oidBillMain");
long customerId = FRMQueryString.requestLong(request,FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
int typeListBill = FRMQueryString.requestInt(request,"typeListBill");        
int typeBill = FRMQueryString.requestInt(request,"typeBill");

long docType = FRMQueryString.requestLong(request,FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]);
long transType = FRMQueryString.requestLong(request,FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]);
long transStatus = FRMQueryString.requestLong(request,FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]);

String value = FRMQueryString.requestString(request, "values");

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;

int iErrCode = FRMMessage.ERR_NONE;
Location locationFrom = new Location();
Location locationRequest = new Location();
String whereClause = "";
String notePo="";
long oidLocationReq=0;
long oidPurchaseID=0;
long oidLocationFrom=0;

BillMain billMain = new BillMain();
try{
   billMain=PstBillMain.fetchExc(oidBillMain);
} catch(Exception ex){

}

 try{
       locationFrom = PstLocation.fetchExc(billMain.getLocationId());
       oidLocationFrom=locationFrom.getOID();
}catch(Exception ex){}

try{
   String[] smartPhonesSplits = billMain.getNotes().split("\\;");
   notePo=smartPhonesSplits[0];
   oidLocationReq=Long.parseLong(smartPhonesSplits[1]);
   oidPurchaseID=Long.parseLong(smartPhonesSplits[2]);
   if(oidLocationReq!=0){
       locationRequest= PstLocation.fetchExc(oidLocationReq);
   }

}catch(Exception ex){}

String whereClauseX = ""
    + "" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
Vector lisBillDetailIssue = PstBillDetail.listMat(0, 0,whereClauseX, "");

%>
<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <link href="../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="../styles/bootstrap3.1/libs/html5shiv.js"></script>
          <script src="../styles/bootstrap3.1/libs/respond.min.js"></script>
        <![endif]-->
        <script language="JavaScript">
            function cmdSave()
            {
                document.frmcashier.command.value="<%=Command.SAVE%>";
                document.frmcashier.hidden_cash_bill_detail_id.value="0";
                document.frmcashier.action="cashier_lyt_m.jsp";
                document.frmcashier.submit();
            }
            
            function cmdUpdate()
            {
                document.frmcashier.command.value="<%=Command.SAVE%>";
                document.frmcashier.hidden_cash_bill_detail_id.value="<%=hidden_cash_bill_detail_id%>";
                document.frmcashier.action="cashier_lyt_m.jsp";
                document.frmcashier.submit();
            }
            
            function cmdEdit(oid)
            {
                document.frmcashier.command.value="<%=Command.EDIT%>";
                document.frmcashier.hidden_cash_bill_detail_id.value=oid;
                document.frmcashier.action="cashier_lyt_m.jsp";
                document.frmcashier.submit();
            }
            
            function cmdBack()
            {
                document.frmcashier.command.value="<%=Command.LIST%>";
                document.frmcashier.action="src_list_open_bill_m.jsp";
                document.frmcashier.submit();
            }
            
            function cmdBackIssue()
            {
                document.frmcashier.command.value="<%=Command.LIST%>";
                document.frmcashier.action="src_list_issue_bill_m.jsp";
                document.frmcashier.submit();
            }
            
            function keyDownCheck(e){
                if (e.keyCode == 13) {
                    cmdCheck(e);
                }
            }
            
            function change(element, evt){
                if (evt.keyCode == 13) {
                    changeFocus(element);
                }
            }
            
            function cmdPrinInvoice(){
                window.open("print_invoce.jsp?","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }
            
            function cmdCheck(){
                var strvalue  = "searchlistrequest.jsp?command=<%=Command.FIRST%>"+
                                                "&mat_code="+document.frmcashier.matCode.value+
                                                "&oidBillMain=<%=oidOpenbillmain%>"+
                                                "&oidBillMainIssue=<%=oidBillMain%>"+
                                                "&txt_materialname="+document.frmcashier.matItem.value;
                window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            
            function myFunction(val) {
                var strvalue  = "searchlistrequest.jsp?command=<%=Command.FIRST%>"+
                                                "&mat_code="+document.frmcashier.matCode.value+
                                                "&oidBillMain=<%=oidOpenbillmain%>"+
                                                "&txt_materialname="+document.frmcashier.matItem.value;
                window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            
        </script>
    </head>
    <body class="skin-blue">
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <!-- Right side column. Contains the navbar and content of the page -->
            <aside>
                <!-- Content Header (Page header) -->
                <!-- Main content -->
                <section class="content">
                    <form name="frmcashier" method ="post" action=""  role="form">
                        <!--form hidden -->
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="prev_command" value="">
                        <input type="hidden" name="start_item" value="">
                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidOpenbillmain%>">
                        <input type="hidden" name="oidBillMain" value="<%=oidBillMain%>">
                         <input type="hidden" name="typeBill" value="<%=typeBill%>">
                        <input type="hidden" name="hidden_cash_bill_detail_id" value="<%=hidden_cash_bill_detail_id%>"> 
                        <input type="hidden" name="typeListBill" value="<%=typeListBill%>"> 
                        <!--body-->
                        <div class="box-bodyx">
                            <div class="row">
                                <div class="col-xs-12">
                                    <a href="#"><img src="../styles/takingorder/img/queens-head.png" border="none" width="250"></a>
                                </div> 
                                <div class="col-xs-12">
                                    <hr>
                                </div>
                                <div class="col-xs-12">
                                    <!-- info row -->
                                    <div class="row invoice-info">
                                        <div class="col-sm-4 invoice-col">
                                            From
                                            <address>
                                                <strong><%=locationFrom.getName()%></strong><br>
                                                Phone: <%=locationFrom.getTelephone()%><br/>
                                                Email: <%=locationFrom.getEmail()%>
                                            </address>
                                        </div><!-- /.col -->
                                        <div class="col-sm-4 invoice-col">
                                            To
                                            <address>
                                               <strong><%=locationRequest.getName()%></strong><br>
                                                Phone: <%=locationRequest.getTelephone()%><br/>
                                                Email: <%=locationRequest.getEmail()%>
                                            </address>
                                        </div><!-- /.col -->
                                        <div class="col-sm-4 invoice-col"><br>
                                            <b><%=oidBillMain == 0 ? "SR #"+notePo : "Invoice # "+billMain.getInvoiceNumber()%></b><br/>
                                            <b>Request Date:</b><%= Formater.formatDate(billMain.getBillDate(), "dd/MM/yyyy")%><br/>
                                            <%
                                            Date issueDate = new Date();
                                            %>
                                            <b>Issue Date:</b><%= Formater.formatDate(issueDate, "dd/MM/yyyy")%>
                                        </div><!-- /.col -->
                                    </div><!-- /.row -->
                                </div>
                                <div class="col-xs-12">
                                    <%= drawListPoItem(SESS_LANGUAGE,iCommand,oidOpenbillmain,lisBillDetailIssue,hidden_cash_bill_detail_id,value)%>
                                </div>
                                <div class="col-xs-12">
                                    <table width="100%">
                                        <tr align="left" valign="top">
                                            <td height="40" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center">By,</td>
                                            <td width="25%" align="center">Receive,</td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="75" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center" nowrap>
                                                (.................................)
                                            </td>
                                            <td width="25%" align="center">
                                                (.................................)
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../styles/bootstrap3.1/libs/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../styles/bootstrap3.1/libs/raphael-min.js"></script>
        <script src="../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        
        <script language="JavaScript">
                    document.frmcashier.matCode.focus();
        </script>
    </body>
</html>
