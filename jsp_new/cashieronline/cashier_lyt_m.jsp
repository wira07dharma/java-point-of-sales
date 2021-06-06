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
    {"No","Kode","Nama","Harga","Tipe Pot.Harga","Pot.Harga","Jumlah Request","Unit","Qty Kirim","Jumlah Kirim","Total","Jumlah Maksimal adalah", "Jumlah Minimal adalah"},//18
    {"No","Code","Name","Price","Disc Type","Discount","Qty Request","Unit","Qty Delivery","Qty Issue", "Total", "Maximum Quantity is","Minimum Quantity is"}  
};


public static final String textListConfirmation[][] = {
    {"Anda yakin menyimpan Order Isu?"},
    {"Are you sure to save order issue?"}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand,Vector objectMaterial,FrmBillDetail frmBillDetail, long oidOpenBill,Vector objectMaterialIssue, long hidden_cash_bill_detail_id) {
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
    
    rowx = new Vector();
    
    
    if(objectMaterialIssue.size()>0){
        int count=0;
        for(int i=0; i<objectMaterialIssue.size(); i++) {
            Vector vBillDetail =(Vector) objectMaterialIssue.get(i);
            Billdetail billdetail = (Billdetail) vBillDetail.get(0);
            Unit unit = (Unit) vBillDetail.get(1);
            count=count+1;
            rowx = new Vector();
            if(iCommand==Command.EDIT && hidden_cash_bill_detail_id==billdetail.getOID()){
                
                rowx.add("<input type=\"hidden\" name=\"materialId\" value=\""+billdetail.getMaterialId()+"\">"
                    + "<input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]+"\" value=\"0\">");
                rowx.add("<input type=\"text\" name=\"matCode\" value=\""+billdetail.getSku()+"\" onkeydown=\"javascript:keyDownCheck(event)\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
                rowx.add("<input type=\"text\" name=\"matItem\" value=\""+billdetail.getItemName()+"\" onkeydown=\"javascript:keyDownCheck(event)\">");

                if(1>2){
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                }

                rowx.add("<div align=\"center\"><input type=\"text\" name=\"qtyRequest\" value=\""+billdetail.getQty()+"\" onkeyup=\"javascript:change(this, event)\"></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" name=\"unitCode\" value=\""+unit.getCode()+"\" onkeyup=\"javascript:change(this, event)\"></div>");

                //double qtyIssue=0;
                rowx.add("<div align=\"center\"><input type=\"text\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY_ISSUE]+"\" value=\""+billdetail.getQty()+"\" onkeyup=\"javascript:change(this, event)\"></div>");
                if(1>2){
                    rowx.add("");
                }

                lstData.add(rowx);
                
            }else{
                
                rowx.add(""+count+"<input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_MATERIAL_ID]+"\" value=\""+billdetail.getMaterialId()+"\">");
                rowx.add("<a href=\"javascript:cmdEdit('"+billdetail.getOID()+"')\">"+billdetail.getSku()+"</a>");
                rowx.add(""+billdetail.getItemName());

                if(1>2){
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                }

                rowx.add("<div align=\"center\"><input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]+"\" value=\""+billdetail.getQty()+"\">"+billdetail.getQty()+"</div>");
                rowx.add("<div align=\"center\"><input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_UNIT_ID]+"\" value=\""+billdetail.getUnitId()+"\">"+unit.getCode()+"</div>");

                double qtyIssue=0;
                qtyIssue=billdetail.getQty();
                rowx.add("<div align=\"center\">"+qtyIssue+"</div>");

                if(1>2){
                    rowx.add("");
                }

                lstData.add(rowx); 
            
            }
            

        }
    }
    
    rowx = new Vector();
    if(iCommand==Command.ADD || iCommand==Command.SAVE){
        rowx.add("<input type=\"hidden\" name=\"materialId\" value=\"\">"
            + "<input type=\"hidden\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_PRICE]+"\" value=\"0\">");
        rowx.add("<input type=\"text\" name=\"matCode\" value=\"\" onkeydown=\"javascript:keyDownCheck(event)\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" name=\"matItem\" value=\"\" onkeydown=\"javascript:keyDownCheck(event)\">");

        if(1>2){
            rowx.add("");
            rowx.add("");
            rowx.add("");
        }

        rowx.add("<div align=\"center\"><input type=\"text\" name=\"qtyRequest\" value=\"\" onkeyup=\"javascript:change(this, event)\"></div>");
        rowx.add("<div align=\"center\"><input type=\"text\" name=\"unitCode\" value=\"\" onkeyup=\"javascript:change(this, event)\"></div>");

        //double qtyIssue=0;
        rowx.add("<div align=\"center\"><input type=\"text\" name=\""+frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY_ISSUE]+"\" value=\"\" onkeyup=\"javascript:change(this, event)\"></div>");
        if(1>2){
            rowx.add("");
        }

        lstData.add(rowx); 
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

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;

int iErrCode = FRMMessage.ERR_NONE;
/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);

//save open bill to bill jika command nya save
if (oidOpenbillmain != 0 && oidBillMain==0) {
    iErrCode = ctrlBillMain.actionSaveBillMainFromOpenBill(iCommand, oidBillMain, 0,typeBill,oidOpenbillmain);
}

FrmBillMain frmBillMain = ctrlBillMain.getForm(); 
BillMain billMain = ctrlBillMain.getBillMain();
if(billMain.getOID()!=0){
    oidBillMain=billMain.getOID();
}

CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
//simpan berdasarkan yang di inputkan
//iErrCode = ctrlBillDetail.action(iCommand,oidBillMain,request);
FrmBillDetail frmBillDetail = ctrlBillDetail.getForm(); 

BillMain OpenBillMain = new BillMain();
Vector lisBillDetail = new Vector();
Vector lisBillDetailIssue = new Vector();
Location locationFrom = new Location();
Location locationRequest = new Location();
String whereClause = "";
String notePo="";
long oidLocationReq=0;
long oidPurchaseID=0;
long oidLocationFrom=0;
//jika masih open bill dan belum ada invoice yang di generate 
if (oidOpenbillmain != 0 && oidBillMain==0) {
   try{
       OpenBillMain = PstBillMain.fetchExc(oidOpenbillmain);
       customerId=OpenBillMain.getCustomerId();
   }catch(Exception ex){}
   
   try{
       locationFrom = PstLocation.fetchExc(OpenBillMain.getLocationId());
       oidLocationFrom=locationFrom.getOID();
   }catch(Exception ex){}
   
   try{
       String[] smartPhonesSplits = OpenBillMain.getNotes().split("\\;");
       notePo=smartPhonesSplits[0];
       oidLocationReq=Long.parseLong(smartPhonesSplits[1]);
       oidPurchaseID=Long.parseLong(smartPhonesSplits[2]);
       if(oidLocationReq!=0){
           locationRequest= PstLocation.fetchExc(oidLocationReq);
       }
       
   }catch(Exception ex){}
   
   whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidOpenbillmain + "'";
   lisBillDetail = PstBillDetail.listMat(0, 0,whereClause, "");
   
}else{
  
   try{
       billMain=PstBillMain.fetchExc(oidBillMain);
   } catch(Exception ex){
   
   }
    
   try{
       OpenBillMain = PstBillMain.fetchExc(oidBillMain);
       customerId=OpenBillMain.getCustomerId();
   }catch(Exception ex){}
   
   try{
       locationFrom = PstLocation.fetchExc(OpenBillMain.getLocationId());
       oidLocationFrom=locationFrom.getOID();
   }catch(Exception ex){}
   
   try{
       String[] smartPhonesSplits = OpenBillMain.getNotes().split("\\;");
       notePo=smartPhonesSplits[0];
       oidLocationReq=Long.parseLong(smartPhonesSplits[1]);
       oidPurchaseID=Long.parseLong(smartPhonesSplits[2]);
       if(oidLocationReq!=0){
           locationRequest= PstLocation.fetchExc(oidLocationReq);
       }
       
   }catch(Exception ex){}

           
  //open berdasarkan invoice yang di generate
  whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidOpenbillmain + "'";
  lisBillDetail = PstBillDetail.listMat(0, 0,whereClause, "");
  
  Vector customer = PstMemberReg.listFromLocationId(0, 0, PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]+"='"+customerId+"'", "");
  MemberReg member = new MemberReg();
  long priceTypeId=0;
  if(customer.size()>0){
        for (int i = 0; i < customer.size(); i++) {
            member = (MemberReg) customer.get(0);
            customerId=member.getOID();
            MemberGroup memberGroup= PstMemberGroup.fetchExc(member.getMemberGroupId());
            priceTypeId=memberGroup.getPriceTypeId();
        }
  }
  
  iErrCode = ctrlBillDetail.actionDetail(iCommand,0,oidBillMain,priceTypeId,request,hidden_cash_bill_detail_id); 
  
   try{
      long updateOpenBill= PstBillMain.updateSalesOrder(oidOpenbillmain, 0, 0, 1, 2);
    }catch(Exception ex){

    }
  //list yang sudah di issue
  String whereClauseX = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
  lisBillDetailIssue = PstBillDetail.listMat(0, 0,whereClauseX, "");
  
}
             


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
                window.open("print_invoice.jsp?oidBillMain=<%=oidBillMain%>","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=yes,menubar=yes,location=no");
            }
            
            function changeFocus(element){
                //alert("1"+element.name);
                if(element.name == "matCode") {
                    document.frmcashier.matItem.focus();
                }
                else if(element.name == "matItem") {
                    document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_QTY]%>.focus();
                }
                else if(element.name == "qtyRequest") {
                    document.frmcashier.unitCode.focus();
                }
                else if(element.name == "unitCode") {
                    document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>.focus();
                }
                else if(element.name == "<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>") {
                    <%if(iCommand==Command.EDIT){%>
                        cmdUpdate();    
                    <%}else{%>
                        cmdSave();
                    <%}    
                    %>
                    
                }
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
            
            function changeAll(valuex,oidKonv,countx,qtySentReal){
                if(valuex!=0){
                         switch(countx){
                             <%for(int i=0; i<lisBillDetail.size(); i++){%>
                                 case <%=""+i%>:
                                         var agree=confirm("Edit Qty Kirim ? ");
                                         if (agree){
                                             document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.value="";
                                             document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.focus();
                                             /*var qtySent = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.value;
                                             alert("qtySent"+qtySentReal);
                                             var qtyRequest = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                                             if(qtySent>qtyRequest){
                                                 alert("Qty Melebihi Request");
                                                 document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.value=valuex;
                                             }*/
                                         }
                                 break;
                            <%}%>
                        }     
                  }
            }
            
            
            function changeQty(valuex,oidKonv,countx,qtySentReal){
                if(valuex!=0){
                         switch(countx){
                             <%for(int i=0; i<lisBillDetail.size(); i++){%>
                                 case <%=""+i%>:
                                         var qtySent = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.value;
                                         var qtyRequest = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                                         if(qtySent>qtyRequest){
                                             alert("Qty Melebihi Request");
                                             document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>_<%=""+i%>.value=qtySentReal;
                                         }
                                 break;
                            <%}%>
                        }     
                  }
            }
            
            
            
        </script>
    </head>
    <body class="skin-blue">
        <%@ include file = "../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmcashier" method ="post" action=""  role="form">
                        <!--form hidden -->
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="prev_command" value="">
                        <input type="hidden" name="start_item" value="">
                        <input type="hidden" name="noteBillMain" value="<%=OpenBillMain.getNotes()%>">
                        <input id="hidden_bill_main_id" type="hidden" name="hidden_bill_main_id" value="<%=oidOpenbillmain%>">
                        <input type="hidden" name="oidBillMain" value="<%=oidBillMain%>">
                         <input type="hidden" name="typeBill" value="<%=typeBill%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_APP_USER_ID]%>" value="<%=userId%>">
                        <input type="hidden" id="FRM_FIELD_CASH_CASHIER_ID" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=oidCashCashierOnline%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=oidLocationFrom%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_SHIFT_ID]%>" value="<%=oidCashierShifId%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_CUSTOMER_ID]%>" value="<%=customerId%>">
                        <input type="hidden" name="hidden_cash_bill_detail_id" value="<%=hidden_cash_bill_detail_id%>"> 
                        <%--<input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DOC_TYPE]%>" value="<%=customerId%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_TRANS_TYPE]%>" value="<%=customerId%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[frmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>" value="<%=customerId%>">--%>
                         
                        <input type="hidden" name="typeListBill" value="<%=typeListBill%>"> 
                        <!--body-->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-12">
                                    <!-- title row -->
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <h2 class="page-header">
                                                <i class="fa fa-globe"></i> <%=userName%>
                                                <small class="pull-right">Date: 2/10/2014</small>
                                            </h2>                            
                                        </div><!-- /.col -->
                                    </div>
                                    
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
                                            <b>Request Date:</b><%= Formater.formatDate(OpenBillMain.getBillDate(), "dd/MM/yyyy")%><br/>
                                            <%
                                            Date issueDate = new Date();
                                            %>
                                            <b>Issue Date:</b><%= Formater.formatDate(issueDate, "dd/MM/yyyy")%>
                                        </div><!-- /.col -->
                                    </div><!-- /.row -->
                                </div>
                                <div id="dataPlace"></div>
                                
                                <div class="col-xs-12">
                                     <div class="box-footer">
                                            <%if(iCommand==Command.EDIT){%>
                                                 <!-- <button class="btn btn-danger pull-left" style="margin-right: 5px;" onclick="javascript:cmdUpdate()" type="button"><i class="fa fa-download"></i> Save</button> -->  
                                            <%}else{%>
                                                <!-- <button class="btn btn-danger pull-left" style="margin-right: 5px;" onclick="javascript:cmdSave()" type="button"><i class="fa fa-download"></i> Save</button>-->
                                            <%}%>
                                            <%if(typeListBill==0){%>
                                                 <!-- <button class="btn btn-warning pull-left" style="margin-right: 5px;" onclick="javascript:cmdBack()" type="button"><i class="fa fa-download"></i> Back</button>-->
                                            <%}else{%>
                                                 <!-- <button class="btn btn-warning pull-left" style="margin-right: 5px;" onclick="javascript:cmdBackIssue()" type="button"><i class="fa fa-download"></i> Back</button>-->
                                            <%}%>
                                            <%if(oidBillMain!=0){%>
                                                 <!-- <button class="btn btn-primary pull-right" style="margin-right: 5px;" onclick="javascript:cmdPrinInvoice()" type="button"><i class="fa fa-download"></i>Invoice</button>-->
                                            <%}%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


<!-- jQuery 2.0.2 -->
<style>
    .whenFocus{
        background-color: bisque;
    }
</style>
<script src="../styles/bootstrap3.1/libs/jquery.min.js"></script>
<script src="../styles/dimata-app.js" type="text/javascript"></script>
<script>
    $(document).ready(function(){
        var dataSend;
        var command;
        var dataFor;
        var approot = "<%= approot%>";
        var poId;
        var locationId;
        var userName = "<%= userName %>";
        var userId = "<%= userId%>";
        var language = "<%= SESS_LANGUAGE%>";
        
        var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){         
            $(this).getData({
               onDone: function(data){
                   onDone(data);
               },
               onSuccess: function(data){
                    onSuccess(data);
               },
               approot          : approot,
               dataSend         : dataSend,
               servletName	: servletName,
               dataAppendTo	: dataAppendTo,
               notification     : notification,
               ajaxDataType	: dataType
            });
        };
        
        var loadAllItem = function(){
            command = "<%= Command.LIST%>";
            dataFor = "showAllItem";
            var oidBillMain = $("#hidden_bill_main_id").val();
            
            dataSend = {
                "FRM_FIELD_DATA_FOR"      : dataFor,
                "FRM_FIELD_BILL_MAIN_ID"  : oidBillMain,
                "FRM_FIELD_LANGUAGE"      : language,
                "command"		  : command
            };
            
            onSuccess = function(data){};
            onDone = function(data){
                findBarcode("#findBarcodeButton");
                findBarcodeEnter("#findBarcode");
                saveOrder("#saveOrder");
                $('.qtysend').on('blur', function(){
                    $(this).removeClass('whenFocus');
                }).on('focus', function(){
                    $(this).addClass('whenFocus');
                });
                
                $('.qtysend').keydown(function(e){
                    if (e.which==40){
                        if ($(this).hasClass("last")){
                            $(".first").focus();
                        }else{
                            var id = $(this).attr("id");
                            var splits = id.split("-");
                            var number =Number(splits[1]);
                            number = number + 1;
                            $("#qty-"+number+"").focus();
                        }
                        
                    }
                    if (e.which==38){
                        if ($(this).hasClass("first")){
                            $(".last").focus();
                        }else{
                            var id = $(this).attr("id");
                            var splits = id.split("-");
                            var number =Number(splits[1]);
                            number = number - 1;
                            $("#qty-"+number+"").focus();
                        }
                        
                    }
                    
                });
                invoice("#printOrder");
            };
            //alert(JSON.stringify(dataSend));
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMappingItemStore", "#dataPlace",true, "json");
            
        };
        
        var findBarcode = function(elementId){
            $(elementId).click(function(){
                var barcode = $("#findBarcode").val();
                var idFocus="";
                //alert(barcode);
                $(".qtysend").each(function(){
                    var barcodeQty = $(this).data("barcode");
                    if (barcode==barcodeQty){
                        idFocus = $(this).attr("id");
                    }
                });
                $("#findBarcode").val("");
                $("#"+idFocus+"").focus();
 
            });
        };
        
        var findBarcodeEnter = function(elementId){
            $(elementId).keypress(function(e) {
                if(e.which == 13) {
                    $("#findBarcodeButton").trigger("click");
                }
            });
        };
        
        var verification = function(){
            var result = false;
            var id = "";
            $(".peringatan").remove();
            $(".qtysend").each(function(){
                var qtyRequest = $(this).data("qtyrequest");
                var qty = $(this).val();
                if (qty.length==0){
                    qty= 0;
                }
                if (qty>=0){
                    if (qty>qtyRequest ){
                        result = true;
                        id = $(this).attr("id");
                        $(this).parent().append("<br><div style='margin-left:0px' class='alert peringatan alert-warning'><%= textListOrderItem[SESS_LANGUAGE][11]%> "+qtyRequest+"</div>");
                    }
                }else{
                    result = true;
                    id = $(this).attr("id");
                    $(this).parent().append("<br><div style='margin-left:0px' class='alert peringatan alert-warning'><%= textListOrderItem[SESS_LANGUAGE][12]%> 0</div>");
                }
                
            });
            $("#"+id+"").focus();
            return result;
        }
        
        var saveOrder = function(elementId){
            $(elementId).click(function(){
                var result = verification();
                if (result==false){
                    var confirmation = "<%= textListConfirmation[SESS_LANGUAGE][0]%>";
                    if (confirm(confirmation)==true){
                        $(this).attr("disabled",true);
                        var values = "";
                        var command = "<%= Command.SAVE%>";
                        var dataFor = "saveIssue";
                        var cashCashier = $("#FRM_FIELD_CASH_CASHIER_ID").val();
                        var oidBillMain = $("#hidden_bill_main_id").val();
                        $(".qtysend").each(function(){
                            var oid = $(this).data("oid");
                            var qty = $(this).val();
                            if (qty.length==0){
                                qty=0;
                            }
                           
                                if (values.length>0){
                                    values += ";"+oid+":"+qty+"";
                                }else{
                                    values += ""+oid+":"+qty+"";
                                }
                            
                            
                        });
                        
                        dataSend = {
                            "FRM_FIELD_DATA_FOR"      : dataFor,
                            "FRM_FIELD_BILL_MAIN_ID"  : oidBillMain,
                            "FRM_FIELD_LANGUAGE"      : language,
                            "FRM_FIELD_CASH_CASHIER_ID":cashCashier,
                            "FRM_FIELD_VALUES"        : values,
                            "command"		      : command
                        };
                        
                        //alert(JSON.stringify(dataSend));
                        
                        onSuccess = function(data){};
                        onDone = function(data){
                            $(this).removeAttr("disabled");
                            window.location = "<%= approot%>/cashieronline/src_list_open_bill_m.jsp";
                        };
                        
                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMappingItemStore", "",true, "json");
                    }
                }
            });
        };
        
        var invoice = function(elementId){
            $(elementId).click(function(){
                var oidBillMain = $("#hidden_bill_main_id").val();
                var values="";
                $(".qtysend").each(function(){
                    var oid = $(this).data("oid");
                    var qty = $(this).val();
                    if (qty.length==0){
                        qty=0;
                    }
                    if (qty>0){
                        if (values.length>0){
                            values += ";"+oid+":"+qty+"";
                        }else{
                            values += ""+oid+":"+qty+"";
                        } 
                    }
                    
                });
                window.open("print_invoice_new.jsp?oidBillMain="+oidBillMain+"&values="+values+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=yes,menubar=yes,location=no");
            });
        };
        
        loadAllItem();
        
    });        
</script>
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
