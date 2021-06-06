<%-- 
    Document   : harga_pokok_item
    Created on : Jan 17, 2018, 8:34:07 PM
    Author     : Ed
--%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCostingItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCostingItem"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatCostingItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatCostingItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.Costing"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCosting"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatchReceiveItem"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.qdep.entity.I_DocStatus"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@include file="../../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    //PEMBIAYAAN
    long oidDetail = FRMQueryString.requestLong(request, "oidDetail");
    long asal = 0;
    long tujuan = 0;
   
    int iCommand2 = FRMQueryString.requestInt(request, "command2");
    int prevCommand2 = FRMQueryString.requestInt(request,"prev_command");
    int startItem2 = FRMQueryString.requestInt(request,"start_item");
    int cmdItem2 = FRMQueryString.requestInt(request,"command_item");
    int appCommand2 = FRMQueryString.requestInt(request,"approval_command");
    long oidCostingMaterial = FRMQueryString.requestLong(request, "hidden_costing_id");
    //long oidCashCashierId=FRMQueryString.requestLong(request, FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CASH_CASHIER_ID]);
    //enable stock balance
    int enableStockBalance = FRMQueryString.requestInt(request, "enable_stock_balance");
    
    ArrayList<Long> oidTes = new ArrayList<Long>();
    oidTes.add(oidCostingMaterial);

    /**
    * initialization of some identifier
    */
    String errMsg2 = "";
    int iErrCode2 = FRMMessage.ERR_NONE;

    /**
    * dispatch code and title
    */
    String dfCode2 = "";//i_pstDocType.getDocCode(docType);
    String dfTitle2 = "Costing Barang";//i_pstDocType.getDocTitle(docType);
    String dfItemTitle2 = dfTitle2 + " Item";

    /**
    * action process
    */
    ControlLine ctrLine2 = new ControlLine();
    CtrlMatCosting ctrlMatCosting = new CtrlMatCosting(request);

  
    iErrCode2 = ctrlMatCosting.actionHpp(iCommand2 , oidCostingMaterial, userName, userId, request);
    FrmMatCosting frmdf = ctrlMatCosting.getForm();
    MatCosting df2 = ctrlMatCosting.getMatCosting();
    errMsg2 = ctrlMatCosting.getMessage();
    
//
if(df2.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
    try{
        System.out.println(">>> proses posting doc : "+oidCostingMaterial);
        SessPosting sessPosting2 = new SessPosting();
        boolean isOK =  sessPosting2.postedCostingDoc(oidCostingMaterial);
         if(isOK){
            df2.setCostingStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
        }
        iCommand2 = Command.EDIT;
    }catch(Exception e){
        iCommand2 = Command.EDIT;
    }
}

    //AUTOCOMPLETE
    String temp = "tes";
    Vector<String> vector = new Vector<String>();
    Vector listInduk = PstMatDispatch.list(0, 0, PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_TYPE]+" = 4 ", null);
    for (int i = 0; i < listInduk.size(); i ++){
        MatDispatch matDispatch = (MatDispatch) listInduk.get(i);
        
        Vector listAnakDis = PstMatDispatchItem.list(0, 0, PstMatDispatchItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+" = "
                +matDispatch.getOID()+" AND " 
                +PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+" > 0 ", null);
        if (!listAnakDis.isEmpty()){
            MatDispatchItem matDispatchItem = (MatDispatchItem) listAnakDis.get(0);
            Material mat = PstMaterial.fetchExc(matDispatchItem.getMaterialId());

                if (!mat.getName().equals(temp)){
                    vector.add("'" + mat.getName() + "'");
                    temp = mat.getName();
                }
        }
    }
     
     Vector<String> vector1 = new Vector<String>();


%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request,"start_item");
    int prevCommand = FRMQueryString.requestInt(request,"prev_command");
    int appCommand = FRMQueryString.requestInt(request,"approval_command");
    long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
    long oidDispatchReceiveItem = FRMQueryString.requestLong(request, "hidden_dispatch_receive_item_id");
    long oidReceiveMaterial = FRMQueryString.requestLong(request,"hidden_receive_id");
    long oidDfRecGroup = FRMQueryString.requestLong(request, "hidden_df_rec_group_id");
    long oidDfRecGroupSame = FRMQueryString.requestLong(request, "hidden_df_rec_same_group_id");
    int commandType = FRMQueryString.requestInt(request,"command_type");
    int startGroup = FRMQueryString.requestInt(request, "start_group");
    
    double totalAllTarget = 0;
    double totalAllSource = 0;
    double bobot = 0;
    List<Double> totalTarget = new ArrayList<Double>();
    List<Double> totalSource = new ArrayList<Double>();
    
%>
<%
    Vector statusDocVal = new Vector();
    Vector statusDocKey = new Vector();

    statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
    statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_CLOSED);
    statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_POSTED);
    statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_FINAL);

    statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
    statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
    statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
    statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
    
    FrmMatDispatchReceiveItem frmObject = new FrmMatDispatchReceiveItem();
    
    //MODAL DATA
    Material material = new Material();
    Material materialVector = new Material();
    MaterialDetail materialDetail = new MaterialDetail();

    PstMaterial pstMaterial = new PstMaterial();
    PstMaterialDetail pstMaterialDetail = new PstMaterialDetail();

    Vector listMaterial = new Vector();
    Vector listMaterialDetail = new Vector();

    //AUTOCOMPLETE FORM B
    listMaterial = PstMaterial.list(0, 0, null, null);
    // Vector<String> vector = new Vector<String>();
     Vector<String> vector2 = new Vector<String>();
     for (int i = 0; i < listMaterial.size(); i ++){
         materialVector = (Material) listMaterial.get(i);
         vector2.add("'" + materialVector.getName() + "'");
         //vector1.add("'" + materialVector.getSku()+ "'");
     }


%>

<%
//SOURCE _ TARGET
int iErrCode = FRMMessage.NONE;
String msgString = "";

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

iErrCode = ctrlMatDispatch.action(Command.EDIT,oidDispatchMaterial,userName,userId);

FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

MatDispatch df = ctrlMatDispatch.getMatDispatch();

    //LOCATION
    Location location = new Location();
    Location locationTo = new Location();
    PstLocation pstLocation = new PstLocation();
    Vector listLocation = new Vector();
    listLocation = PstLocation.list(0, 0, pstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
            + " = " + df.getLocationId(), null);
    if (listLocation.size() > 0){
        location = (Location) listLocation.get(0);
    }
    

    Vector listLocationTo = new Vector();
    listLocationTo = PstLocation.list(0, 0, pstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
            + " = " + df.getDispatchTo(), null);
    if (listLocationTo.size() > 0){
        locationTo = (Location) listLocationTo.get(0);
    }


/**
 * check if document already closed or not
 */

boolean documentClosed = false;

if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED) {

    documentClosed = true;

}



/**

 * check if document may modified or not

 */

boolean privManageData = true;

MatDispatchReceiveItem dfRecItem = new MatDispatchReceiveItem();
ControlLine ctrLine = new ControlLine();
CtrlMatDispatchReceiveItem ctrlMatDispatchReceiveItem = new CtrlMatDispatchReceiveItem(request);
iErrCode = ctrlMatDispatchReceiveItem.actionHpp(iCommand,oidDispatchReceiveItem,oidDfRecGroupSame,oidDispatchMaterial,oidDfRecGroup,commandType, userName, userId);
FrmMatDispatchReceiveItem frmMatDispatchReceiveItem = ctrlMatDispatchReceiveItem.getForm();
dfRecItem = ctrlMatDispatchReceiveItem.getMatDispatchReceiveItem();
msgString = ctrlMatDispatchReceiveItem.getMessage();

String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfRecItem.getSourceItem().getMaterialSource().getOID();
Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");
MaterialStock objMaterialStock = new MaterialStock();

if(vMaterialStock.size() > 0) {

             objMaterialStock = (MaterialStock)vMaterialStock.get(0);

            }
String order="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]+" DESC "
        +" LIMIT 0, 10";
Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem,0,oidDispatchMaterial,order);

Vector vTot = new Vector();
int sizeList = listMatDispatchReceiveItem.size();
vTot = PstMatDispatchReceiveItem.listGroup(startItem,0,oidDispatchMaterial,null);
int sizeTotal = vTot.size();
int sizeAwal = 0;
if (!listMatDispatchReceiveItem.isEmpty()){
    sizeAwal = 1;
} else {
    sizeAwal = 0;
}
%>

<%
    //PEMBIAYAAN
    int iCommand3 = FRMQueryString.requestInt(request, "command3");
    int start3 = FRMQueryString.requestInt(request,"start3");
    int prevCommand3 = FRMQueryString.requestInt(request,"prev_command3");
    int appCommand3 = FRMQueryString.requestInt(request,"approval_command3");
    long oidCostingMaterial3 = FRMQueryString.requestLong(request,"hidden_costing_id3");
    long oidCostingMaterialItem3 = FRMQueryString.requestLong(request,"hidden_costing_item_id3");
    int enableStockBalance3 = FRMQueryString.requestInt(request, "enable_stock_balance3");

    /**
    * initialization of some identifier
    */
    int iErrCode3 = FRMMessage.NONE;
    String msgString3 = "";

    /**
    * purchasing pr code and title
    */
    String costingCode3 = ""; //i_pstDocType.getDocCode(docType);
    String costingTitle3 = "Costing Barang"; //i_pstDocType.getDocTitle(docType);
    String costingItemTitle3 = costingTitle3 + " Item";

    /**
    * purchasing pr code and title
    */
    //String prCode3 = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


    /**
    * process on df main
    */
    CtrlMatCosting ctrlMatCosting3 = new CtrlMatCosting(request);
    iErrCode3 = ctrlMatCosting.action(Command.EDIT,oidCostingMaterial3,userName,userId);
    FrmMatCosting frmMatCosting3 = ctrlMatCosting.getForm();
    MatCosting costing3 = ctrlMatCosting.getMatCosting();

    /**
    * check if document already closed or not
    */
    boolean documentClosed3 = false;
    if(costing3.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
    {
            documentClosed = true;
    }

    /**
    * check if document may modified or not
    */
    boolean privManageData3 = true;

    ControlLine ctrLine3 = new ControlLine();
    CtrlMatCostingItem ctrlMatCostingItem3 = new CtrlMatCostingItem(request);
    ctrlMatCostingItem3.setLanguage(SESS_LANGUAGE);

    //by dyas - 20131126
    //tambah variabel userName dan userId
    iErrCode3 = ctrlMatCostingItem3.action(iCommand3,oidCostingMaterialItem3,oidCostingMaterial3, userName, userId);
    FrmMatCostingItem frmMatCostingItem3 = ctrlMatCostingItem3.getForm();
    MatCostingItem costingItem3 = ctrlMatCostingItem3.getMatCostingItem();
    msgString3 = ctrlMatCostingItem3.getMessage();

    String whereClauseItem3 = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+"="+oidCostingMaterial+
                             " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";

    String orderClauseItem = "";
    int vectSizeItem = PstMatCostingItem.getCount(whereClauseItem3);
    int recordToGetItem = 500;

    if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
    {
            start3 = ctrlMatCostingItem3.actionList(iCommand3,start3,vectSizeItem,recordToGetItem);
    }

    String whereClauseItemx3 = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
    Vector listMatCostingItem3 = PstMatCostingItem.list(start3,recordToGetItem,oidCostingMaterial3,whereClauseItemx3);

    String whereClauseItemComposite3 = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"!=0";
    Vector listMatCostingItemComposite3 = PstMatCostingItem.list(start3,recordToGetItem,oidCostingMaterial3,whereClauseItemComposite3);

    if(listMatCostingItem3.size()<1 && start3>0)
    {
             if(vectSizeItem-recordToGetItem > recordToGetItem)
             {
                    start3 = start3 - recordToGetItem;
             }
             else
             {
                    start3 = 0 ;
                    iCommand3 = Command.FIRST;
                    prevCommand3 = Command.FIRST;
             }
             listMatCostingItem3 = PstMatCostingItem.list(start3,recordToGetItem,oidCostingMaterial3);
    }

    if(iCommand3==Command.SAVE && iErrCode3 == 0) {
                    iCommand3 = Command.ADD;
            oidCostingMaterialItem3=0;
    }



%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Harga Pokok Item</title>

        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/datepicker/datepicker3.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/timepicker/bootstrap-timepicker.css">

        <script type="text/javascript" src="../../styles/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
        <script type="text/javascript" src="../../styles/iCheck/icheck.min.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-notify.js"></script>
        <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../styles/datepicker/bootstrap-datepicker.js"/>></script>
    <script type="text/javascript" src="../../styles/timepicker/bootstrap-timepicker.js"></script>
    <style>
        .headEdit {font-size:  14px}
        #tableResult th {color: white}
        #tableSource th {color: white}
        #tableTarget th {color: white}
        #tableItem th {color: white}
        #tableResult {font-size: 12px}
        #tableSource {font-size: 12px}
        #tableTarget {font-size: 12px}
        #tableItem {font-size: 12px}
        
        #mainTable td {border-color: black}
        #mainTable  {border-left-color: black}
        #mainTable  {border-right-color: black}
        #mainTable  {border-top-color: black}
        #mainTable th {border-color: black}
        
        #mainTable2 td {border-color: black}
        #mainTable2  {border-left-color: black}
        #mainTable2  {border-right-color: black}
        #mainTable2  {border-top-color: black}
        #mainTable2  {border-bottom-color: black}
        #mainTable2 th {border-color: black}
        
        #tableSource td {border-color: white}
        #tableTarget td {border-color: white}
        #tableSource th {border-color: white}
        #tableTarget th {border-color: white}
        #tableResult td {border-color: white}
        #tableResult th {border-color: white}
        #mainTable {border-bottom-color: black}
        
        ul.ui-autocomplete {
            z-index: 1100;
        }
        
        
    </style>
    
    
</head>
<body style="background-color: white">
    <section class="content-header">
        <h1>Harga Pokok Item
            <small> Calculation</small></h1>
        <ol class="breadcrumb">
            <li>
                <a href="http://localhost:8080/dProchain_20161124/menuapp/menu_page.jsp?menu=masterdata&titlemenu=Master%20Data"> Masterdata
                    <i class="fa fa-dashboard">

                    </i>
                    <li class="active">Calculation</li>
                </a>
            </li>
        </ol>
    </section>
    <section class="content">
        <!--MAIN ROW-->
        <div class="row">
            <!-- ABOVE -->
            <div class="col-md-12">
                <div class="row">
                    
                    <form id="mainform" class="form-horizontal" name="mainform">
                        <input type="hidden" id="oidDetail" name="oidDetail" value="">
                        <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
                        <input type="hidden" name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
                        
                        <!--STATUS EDIT PEMBIAYAAN-->
                        <% 
                            Vector statusDocVal3 = new Vector();
                            Vector statusDocKey3 = new Vector();

                            statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);
                            statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_FINAL);
                            statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_CLOSED);

                            statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                            statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                            statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                            statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                        %> 
                        <select hidden id="select1">
                            <option value="<%= statusDocVal3.get(0) %>"><%= statusDocKey3.get(0) %></option>
                            <option value="<%= statusDocVal3.get(1) %>"><%= statusDocKey3.get(1) %></option>
                        </select>
                        <select hidden id="select2">
                            <option value="<%= statusDocVal3.get(0) %>"><%= statusDocKey3.get(0) %></option>
                            <option value="<%= statusDocVal3.get(1) %>"><%= statusDocKey3.get(1) %></option>
                            <option value="<%= statusDocVal3.get(2) %>"><%= statusDocKey3.get(2) %></option>
                        </select>
                        
                        <!--CLOSED-->
                        <select hidden id="select3">
                            <option value="5">Closed</option>
                        </select>
                        
                            <table id="tableInduk" class='table'>
                                <tr>

                                    <td class="headEdit" style="width: 10%">
                                        <label>Nomor</label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                        : <%= df.getDispatchCode()%>
                                    </td>    


                                    <td class="headEdit" style="width: 10%">
                                        <label>Status</label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                        : <%= statusDocKey.get(df.getDispatchStatus())%>
                                    </td>       

                                    <td class="headEdit" style="width: 10%">
                                        <label>Keterangan</label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                        : <%= df.getRemark()%>
                                    </td>

                                </tr>
                                <tr>

                                    <td class="headEdit" style="width: 10%">
                                        <label>Lokasi Asal</label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                        : <%= location.getName()%>
                                    </td>    


                                    <td class="headEdit" style="width: 10%">
                                        <label>Lokasi Tujuan</label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                        : <%= locationTo.getName()%>
                                    </td>       
                                    <td class="headEdit" style="width: 10%">
                                        <label></label>
                                    </td>
                                    <td class="headEdit" style="width: 20%">
                                    </td>       

                                </tr>

                            </table>

                        </form>
                    
                    
                    
                </div>
                
                
            </div>
            
            <section class="content">
                <button id="btnAddGroup" type="button" class="btn btn-primary"><i class="fa fa-plus"></i>&nbsp;Tambah Group</button>
                <a href="add_harga_pokok_group.jsp?hidden_dispatch_id=<%= oidDispatchMaterial%>&command=<%=Command.EDIT%>&hidden_receive_id=<%= oidReceiveMaterial%>" class="btn btn-primary" id="backToGroup" data-oid="<%= oidDispatchMaterial%>"><i class="fa fa-chevron-left"></i>&nbsp;Kembali&nbsp;ke&nbsp;Daftar&nbsp;Group</a>
                <p></p>
            
            
            <!-- LIST  -->
            <div class="col-md-12">
                <div class="row">
                    <%
                        if (listMatDispatchReceiveItem.isEmpty()){
                    %>
                    <div class="row">
                                <div class="col-sm-12">
                                    <table class="table table-bordered table-striped table-info">
                                        <thead>
                                            <tr>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td style="background-color: lightgray" id='nodata' colspan='2' class='text-center'> Data tidak tersedia! </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
<!--                    <button data-cmdtype="0" data-group="0" data-group2="" id="btnAddBahanBaku1" type="button" class="btn btn-default"><i class="fa fa-plus-circle">&nbsp;Tambah Bahan Baku</i></button>-->
                    <%
                        } else {
                    %>
                    <table id="mainTable" class="table table-bordered table-info">
                                <thead class="thead-inverse" style="background-color: lightblue">
                                    <tr>
                                        <th>Group</th>
                                        <th>Source</th>
                                        <th>Target</th>
                                        
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        Double stock1 = 0.0;
                                        Double stock2 = 0.0;
                                        Double harga1 = 0.0;
                                        Double harga2 = 0.0;
                                        
                                        for (int i = 0; i < listMatDispatchReceiveItem.size(); i++) {
                                            totalAllTarget = 0;
                                            totalAllSource = 0;
                                            MatDispatchReceiveItem dfRecItemInduk = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i);

                                    %>
                                    <tr>
                                        <td class="text-center" style="width: 4%"><%=i + 1%>
                                              
                                            <div>
                                                <i id="" data-group="<%= dfRecItemInduk.getDfRecGroupId() %>" class=" btnDeleteGroup fa fa-trash-o"></i>
                                            </div>
                                        </td>
                                        
                                        <!--LIST SOURCE-->
                                        <td style="width: 48%"> 
                                            <button data-cmdtype="1" data-group="<%= dfRecItemInduk.getDfRecGroupId()%>" id="btnAddBahanBaku" type="button" class=" btnAddBahanBaku btn btn-default"><i class="fa fa-plus-circle">&nbsp;Tambah Bahan Baku</i></button>
                                            <p></p>

                                            <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: orange">
                                                    <tr>
                                                        <th>Item</th>
                                                        <th>Harga</th>
                                                        <th>Unit</th>
                                                        <th>Qty</th>
                                                        <th>Sub Total</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                   <%
                                                      
                                                        MatDispatchReceiveItem dfRecItem1 = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i); 
                                                        String order1="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                        Vector listItem = PstMatDispatchReceiveItem.list(0,0,dfRecItem1.getDfRecGroupId(),order1);
                                                        for (int k=0; k < listItem.size(); k++){
                                                            MatDispatchReceiveItem dfRecItemAll = (MatDispatchReceiveItem) listItem.get(k);
                                                            Vector listTarget = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]+" = "
                                                                    + dfRecItemAll.getDispatchMaterialId(), null);
                                                            MatReceive mr = (MatReceive) listTarget.get(0);
                                                            
                                                            if (dfRecItemAll.getSourceItem().getMaterialId() != 0){
                                                                Vector listStok = new Vector();
                                                                Vector listHarga = new Vector();
                                                                listStok = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = "
                                                                        +df.getLocationId()+ " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = " 
                                                                        + dfRecItemAll.getSourceItem().getMaterialSource().getOID() , null);
                                                                if (!listStok.isEmpty()){
                                                                    MaterialStock ms1 = (MaterialStock) listStok.get(0);
                                                                    stock1 = ms1.getQty(); 
                                                                }

                                                                listHarga = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = "
                                                                        + dfRecItemAll.getSourceItem().getMaterialSource().getOID(), null);
                                                                if (!listHarga.isEmpty()){
                                                                    Material m1 = (Material) listHarga.get(0);
                                                                    harga1 = m1.getAveragePrice();
                                                                }
                                                     %>
                                                     <tr>
                                                         <td><%= dfRecItemAll.getSourceItem().getMaterialSource().getName() %></td>
                                                         <td><%= dfRecItemAll.getSourceItem().getHpp() %></td>
                                                         <td><%= dfRecItemAll.getSourceItem().getUnitSource().getCode() %></td>
                                                         <td><%= dfRecItemAll.getSourceItem().getQty() %></td>
                                                         <td><%= String.format("%.2f", dfRecItemAll.getSourceItem().getHppTotal())%></td>
                                                         <td style="text-align: center">
                                                             <button data-stok="<%=stock1%>" data-harga="<%=harga1%>" data-unitid="<%=dfRecItemAll.getSourceItem().getUnitId() %>" data-materialid="<%= dfRecItemAll.getSourceItem().getMaterialId() %>" data-cmdtype="<%= CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM%>" data-oidtransfer="<%= dfRecItemAll.getOID()%>" data-oidgroup="<%= dfRecItemAll.getDfRecGroupId()%>" data-oidinduk="<%= dfRecItemAll.getDispatchMaterialId()%>" data-oidinduk2="<%= mr.getOID() %>" data-oid="<%= dfRecItemAll.getSourceItem().getOID() %>" data-oidtarget="<%= dfRecItemAll.getTargetItem().getOID() %>" data-nama="<%= dfRecItemAll.getSourceItem().getMaterialSource().getName() %>" data-unit="<%= dfRecItemAll.getSourceItem().getUnitSource().getCode() %>" id="editSource" type="button" class="editSource btn btn-info"><i class="fa fa-edit"></i></button>
                                                             <button data-transferid="<%= dfRecItemAll.getOID()%>" id="deleteSource" type="button" class="deleteSource btn btn-danger"><i class="fa fa-trash"></i></button>
                                                         </td>

                                                     </tr>
                                                     <%
                                                         }

                                                         totalAllSource += dfRecItemAll.getSourceItem().getHppTotal();
                                                         }
                                                         totalSource.add(totalAllSource);
                                                        
                                                     %>


                                                </tbody>
                                            </table>
                                        </td>
                                        
                                        <!--LIST TARGET-->
                                        <td style="width: 48%">
                                            <button data-group="<%= dfRecItemInduk.getDfRecGroupId() %>" data-cmdtype="1" id="btnAddPenjualan" type="button" class="btnAddPenjualan btn btn-default"><i class="fa fa-plus-circle">&nbsp;Tambah Penjualan</i></button>
                                            <p></p>

                                            <table id="tableTarget" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: green">
                                                    <tr>
                                                        <th>Item</th>
                                                        <th>Harga</th>
                                                        <th>Unit</th>
                                                        <th>Qty</th>
                                                        <th>Sub Total</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%            
                                                        MatDispatchReceiveItem dfRecItem2 = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i); 
                                                        String order2="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                        Vector listItem2 = PstMatDispatchReceiveItem.list(0,0,dfRecItem2.getDfRecGroupId(),order2);
                                                        for (int k=0; k < listItem2.size(); k++){
                                                            MatDispatchReceiveItem dfRecItemAll = (MatDispatchReceiveItem) listItem2.get(k);
                                                            Vector listTarget = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]+" = "
                                                                    + dfRecItemAll.getDispatchMaterialId(), null);
                                                            MatReceive mr = (MatReceive) listTarget.get(0);
                                                            
                                                             
                                                            
                                                            if (dfRecItemAll.getTargetItem().getMaterialId() != 0){
                                                                
                                                                Vector listStok2 = new Vector();
                                                                Vector listHarga2 = new Vector();
                                                                listStok2 = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = "
                                                                        +df.getLocationId()+ " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = " 
                                                                        + dfRecItemAll.getTargetItem().getMaterialTarget().getOID() , null);
                                                                if (!listStok2.isEmpty()){
                                                                    MaterialStock ms1 = (MaterialStock) listStok2.get(0);
                                                                    stock2 = ms1.getQty(); 
                                                                }

                                                                listHarga2 = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = "
                                                                        + dfRecItemAll.getTargetItem().getMaterialTarget().getOID(), null);
                                                                if (!listHarga2.isEmpty()){
                                                                    Material m1 = (Material) listHarga2.get(0);
                                                                    harga2 = m1.getAveragePrice();
                                                                }
                                                     %>

                                                    <tr>
                                                         <td><%= dfRecItemAll.getTargetItem().getMaterialTarget().getName() %></td>
                                                         <td><%= dfRecItemAll.getTargetItem().getCost()%></td>
                                                         <td><%= dfRecItemAll.getTargetItem().getUnitTarget().getCode() %></td>
                                                         <td><%= dfRecItemAll.getTargetItem().getQty() %></td>
                                                         <td><%= String.format("%.2f", dfRecItemAll.getTargetItem().getTotal())%></td>
                                                         <td style="text-align: center">
                                                             <button data-stok="<%=stock2%>" data-harga="<%=harga2%>" data-unitid="<%=dfRecItemAll.getTargetItem().getUnitId() %>" data-materialid="<%= dfRecItemAll.getTargetItem().getMaterialId() %>" data-cmdtype="<%= CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM%>" data-oidtransfer="<%= dfRecItemAll.getOID()%>" data-oidgroup="<%= dfRecItemAll.getDfRecGroupId()%>" data-oidinduk="<%= dfRecItemAll.getDispatchMaterialId()%>" data-oidinduk2="<%= mr.getOID() %>" data-oid="<%= dfRecItemAll.getTargetItem().getOID() %>" data-nama="<%= dfRecItemAll.getTargetItem().getMaterialTarget().getName() %>" data-unit="<%= dfRecItemAll.getTargetItem().getUnitTarget().getCode() %>" data-oidsource="<%= dfRecItemAll.getSourceItem().getOID() %>" id="editTarget" type="button" class="editTarget btn btn-info"><i class="fa fa-edit"></i></button>
                                                             <button data-transferid="<%= dfRecItemAll.getOID()%>" id="deleteTarget" type="button" class="deleteTarget btn btn-danger"><i class="fa fa-trash"></i></button>
                                                         </td>
                                                     </tr>
                                                     <%
                                                         }
                                                         totalAllTarget += dfRecItemAll.getTargetItem().getTotal();
                                                         }
                                                         
                                                         totalTarget.add(totalAllTarget);
                                                        
                                                     %>
                                                </tbody>
                                            </table> 
                                        </td>
                                        
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                    </table>
                    
                    
                    
                    
                </div>
            </div>
                                
            <div class="col-md-12">
                <div class="row">
                    
                    <table id="mainTable2" class="table table-bordered table-info">
                                <thead class="thead-inverse" style="background-color: lightblue">
                                    <tr>
                                        <th>Group</th>
                                        <th>Harga Pokok</th>
                                        
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        
                                        for (int i = 0; i < listMatDispatchReceiveItem.size(); i++) {
                                            MatDispatchReceiveItem dfRecItemInduk = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i);

                                    %>
                                    <tr>
                                        <td class="text-center" style="width: 2%"><%=i + 1%></td>
                                        
                                        <!--LIST HPP-->
                                        <td style="width: 98%"> 
                                             <table id="tableResult" class="table table-bordered table-striped table-info table-responsive">
                                        <thead class="thead-inverse" style="background-color: lightseagreen">
                                        <tr>
                                            <th>Item</th>
                                            <th>Bobot</th>
                                            <th>Total</th>
                                            <th>HPP</th>
                                            <th>Unit</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            MatDispatchReceiveItem dfRecItem2 = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i); 
                                            String order2="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                            Vector listItem2 = PstMatDispatchReceiveItem.list(0,0,dfRecItem2.getDfRecGroupId(),order2);
                                            for (int k=0; k < listItem2.size(); k++){
                                                MatDispatchReceiveItem dfRecItemAll = (MatDispatchReceiveItem) listItem2.get(k);
                                                Vector listTarget = PstMatReceive.list(0, 0, PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]+" = "
                                                        + dfRecItemAll.getDispatchMaterialId(), null);
                                                MatReceive mr = (MatReceive) listTarget.get(0);
                                                
                                                bobot = dfRecItemAll.getTargetItem().getTotal()/totalTarget.get(i);
                                                if (dfRecItemAll.getTargetItem().getMaterialId() != 0){
                                                   
                                         %>
                                        
                                        <tr>
                                            <td><%= dfRecItemAll.getTargetItem().getMaterialTarget().getName() %></td>
                                            <td><%= String.format("%.2f",bobot*100) %>%</td>
                                            <td><%= String.format("%.2f", bobot*totalSource.get(i)) %></td>
                                            <td><%= String.format("%.2f", (bobot*totalSource.get(i))/dfRecItemAll.getTargetItem().getQty())%></td>
                                            <td><%= dfRecItemAll.getTargetItem().getUnitTarget().getCode() %></td>
                                        </tr>
                                        <%
                                            }
                                            Vector listStok2 = new Vector();
                                            Vector listHarga2 = new Vector();
                                            listStok2 = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = "
                                                    +df.getLocationId()+ " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+" = " 
                                                    + dfRecItemAll.getTargetItem().getMaterialTarget().getOID() , null);
                                            if (!listStok2.isEmpty()){
                                                MaterialStock ms1 = (MaterialStock) listStok2.get(0);
                                                stock2 = ms1.getQty(); 
                                            }

                                            listHarga2 = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = "
                                                    + dfRecItemAll.getTargetItem().getMaterialTarget().getOID(), null);
                                            if (!listHarga2.isEmpty()){
                                                Material m1 = (Material) listHarga2.get(0);
                                                harga2 = m1.getAveragePrice();
                                            }
                                            }

                                        %>
                                       

                                    </tbody>
                                </table>
                                        </td>
                                        
                                        
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                    </table>
                                
                                
                                
                    <!--LIST PEMBIAYAAN-->

                    <button  data-cmdtype="1" id="btnAddPembiayaan" type="button" class="btnAddPembiayaan btn btn-default"><i class="fa fa-plus-circle">&nbsp;Tambah Pembiayaan</i></button>
                    <p></p>

                    <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                            <thead class="thead-inverse" style="background-color: #D60000">
                            <tr>
                                <th>No.</th>
                                <th>Nomor</th>
                                <th>Tipe Biaya</th>
                                <th>Status</th>
                                <th>keterangan</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                Vector vCosting = new Vector();
                                MatCosting matCost = new MatCosting();

                                vCosting = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]+" = 4 AND "
                                        +PstMatCosting.fieldNames[PstMatCosting.FLD_DOCUMENT_ID]+" = "+oidDispatchMaterial , null);

                                for (int z = 0; z < vCosting.size(); z ++){
                                    matCost = (MatCosting) vCosting.get(z);

                                    //size item
                                    Vector vCostitemCheck = PstMatCostingItem.list(0, 0, PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+ " = 0 "
                                        + " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+ " = " + matCost.getOID(), null);



                                    //Tipe Biaya
                                    Vector vType = PstCosting.list(0, 0, PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]+" = "
                                            + matCost.getCostingId(), null);
                                    Costing costing = (Costing) vType.get(0);

                                    //status conversion
                                    int statusConv = 0;
                                    if (matCost.getCostingStatus() == 0){
                                        statusConv = 0;
                                    } else if (matCost.getCostingStatus() == 1){
                                        statusConv = 1;
                                    } else {
                                        statusConv = 3;
                                    }


                            %>

                             <tr>

                                 <td><%= z + 1 %></td>
                                 <td><a class="detailPembiayaan" data-oid="<%= matCost.getOID() %>" href="#"><%= matCost.getCostingCode() %></a></td>
                                 <td><%= costing.getName() %></td>
                                 <td><%= statusDocKey3.get(statusConv)%></td>
                                 <td><%= matCost.getRemark()%></td>
                                 <td style="text-align: center">
                                     <%
                                         if (matCost.getCostingStatus() != 5){
                                     %>
                                        <button data-tipe="<%= matCost.getCostingId() %>" data-status="<%= matCost.getCostingStatus() %>" data-size="<%= vCostitemCheck.size() %>" data-remark="<%= matCost.getRemark()%>" data-sku="<%= matCost.getCostingCode() %>" data-oid="<%= matCost.getOID() %>" data-asal="<%= matCost.getLocationId() %>" data-tujuan="<%= matCost.getCostingTo()%>" class="editPembiayaan btn btn-info"><i class="fa fa-edit"></i></button>
                                        <%
                                         }
                                     %>
                                     <button data-oid="<%= matCost.getOID() %>" class="deletePembiayaan btn btn-danger"><i class="fa fa-trash"></i></button>
                                 </td>

                             </tr>
                             <%
                                 }

                             %>


                        </tbody>
                    </table>
                             <%
                                 }
                             %>                               
                    </div>
                </div>
            </section>
            
        </div>
    </section>
    
    <script>
        $(document).ready(function (){
            var kode;
            var qty;
            var sisa = $("#sisaA").val();
            var save;
            
            //MODAL
            var modalSetting = function (elementId, backdrop, keyboard, show) {
                $(elementId).modal({
                    backdrop: backdrop,
                    keyboard: keyboard,
                    show: show
                });
            };
            
            
            modalSetting("#cdoc", "static", false, false);
            modalSetting("#addBahanBaku", "static", false, false);
            modalSetting("#addDetailPembiayaan", "static", false, false);
            modalSetting("#formPembiayaan", "static", false, false);
            
        if ("<%=oidDetail%>" != 0 ) {
            $("#addDetailPembiayaan").modal("show");
        }
            
            //CHECK BAHAN BAKU MODAL
            $("#btnCheckA").click(function (){
                alert("apalah");
                var value = $("#itemA").val();
                kode = $(this).data("kode");
                $("#cdoc").modal("show");
                runDataTables();
                $('#cdoc input[type="search"]').val(value);
                $('#cdoc input[type="search"]').keyup();
            });
            //CHECK PENJUALANMODAL
            $("#btnCheckB").click(function (){
                var value = $("#itemB").val();
                kode = $(this).data("kode");
                $("#cdoc").modal("show");
                runDataTables2();
                $('#cdoc input[type="search"]').val(value);
                $('#cdoc input[type="search"]').keyup();
            });
            
            //CHECK PENJUALANMODAL
            $("#btnCheckE").click(function (){
                var value = $("#itemE").val();
                kode = $(this).data("kode");
                $("#cdoc").modal("show");
                runDataTables2();
                $('#cdoc input[type="search"]').val(value);
                $('#cdoc input[type="search"]').keyup();
            });
            
            var stok;
            var harga;
            //CHOOSE MATERIAL
            $('body').on("click",".source",function() {
                
                var nama = $(this).data("nama");
                stok = $(this).data("qty");
                var unitid  = $(this).data("unitid");
                var unit  = $(this).data("unit");
                harga  = $(this).data("harga");
                var oid  = $(this).data("oid");
                //WHEN INPUT SISA
                $("#sisaA").keyup(function (){
                    sisa = $(this).val();
                    qty = stok - sisa;
                    $("#qtyA").val(qty);
                    var total = $("#qtyA").val() * harga;
                    $("#totalA").val(total);
                });
                
                $("#qtyB").keyup(function (){
                   var total = $(this).val() * harga;
                   $("#totalB").val(total);
                });
                
                $("#qtyE").keyup(function (){
                   var total = $(this).val() * harga;
                   $("#totalE").val(total);
                });
                
                
                if (kode === 0){
                    $("#sisaA").removeAttr("disabled");
                    $("#itemA").val(nama);
                    $("#stokA").val(stok);
                    $("#unitA").val(unit);
                    $("#unitidSource").val(unitid);
                    $("#hargaA").val(harga);
                    $("#idMatSource").val(oid);
                    
                } else if (kode == 1) {
                    $("#itemB").val(nama);
                    $("#unitB").val(unit);
                    $("#hargaB").val(harga);
                    $("#unitidTarget").val(unitid);
                    $("#idMatTarget").val(oid);
                } else {
                    $("#itemE").val(nama);
                    $("#unitE").val(unit);
                    $("#hargaE").val(harga);
                    $("#unitIdPembiayaan").val(unitid);
                    $("#idMatPembiayaan").val(oid);
                }
                $("#cdoc").modal("hide");
                $("#qtyTarget").focus();
                
            });
            
            //EDIT SOURCE
            $(".editSource").click(function (){
                var oid = $(this).data("oid");
                var materialId = $(this).data("materialid");
                var nama = $(this).data("nama");
                var unit = $(this).data("unit");
                var unitid = $(this).data("unitid");
                stok = $(this).data("stok");
                harga = $(this).data("harga");
                
                var oidGroup = $(this).data("oidgroup");
                var oidSource = $(this).data("oidsource");
                var oidTarget = $(this).data("oidtarget");
                var oidInduk = $(this).data("oidinduk");
                var oidInduk2 = $(this).data("oidinduk2");
                var oidTransfer = $(this).data("oidtransfer");
                var cmdtype = $(this).data("cmdtype");
                
                //WHEN INPUT SISA
                $("#sisaA").keyup(function (){
                    sisa = $(this).val();
                    qty = stok - sisa;
                    $("#qtyA").val(qty);
                    var total = $("#qtyA").val() * harga;
                    $("#totalA").val(total);
                });
                
                
                $("#itemA").val(nama);
                $("#unitidSource").val(unitid);
                $("#oidSource").val(oid);
                $("#oidTargetForSource").val(oidTarget);
                $("#idMatSource").val(materialId);
                $("#stokA").val(stok);
                $("#hargaA").val(harga);
                $("#unitA").val(unit);
                
                $("#groupId").val(oidGroup);
                $("#groupId2").val("0");
                $("#sourceId").val(oidSource);
                $("#targetId").val(oidTarget);
                $("#indukId").val(oidInduk);
                $("#induk2Id").val(oidInduk2);
                $("#transferId").val(oidTransfer); 
                $("#cmdType").val(cmdtype); 
                document.generalformA.command.value="<%=Command.EDIT%>";
                $("#addBahanBaku").modal("show");
            });
            
            //EDIT Target
            $(".editTarget").click(function (){
                var oid = $(this).data("oid");
                var materialId = $(this).data("materialid");
                var nama = $(this).data("nama");
                var unit = $(this).data("unit");
                var unitid = $(this).data("unitid");
                stok = $(this).data("stok");
                harga = $(this).data("harga");
                
                var oidGroup = $(this).data("oidgroup");
                var oidSource = $(this).data("oidsource");
                var oidTarget = $(this).data("oidtarget");
                var oidInduk = $(this).data("oidinduk");
                var oidInduk2 = $(this).data("oidinduk2");
                var oidTransfer = $(this).data("oidtransfer");
                var cmdtype = $(this).data("cmdtype");
                
                //WHEN INPUT QTY
                $("#qtyB").keyup(function (){
                    var total = $(this).val() * harga;
                    $("#totalB").val(total);
                });
                
                
                $("#itemB").val(nama);
                $("#unitidTarget").val(unitid);
                $("#oidTarget").val(oid);
                $("#oidSourceForTarget").val(oidSource);
                $("#idMatTarget").val(materialId);
                $("#stokB").val(stok);
                $("#hargaB").val(harga);
                $("#unitB").val(unit);
                
                $("#groupIdB").val(oidGroup);
                $("#groupId2B").val("0");
                $("#sourceIdB").val(oidSource);
                $("#targetIdB").val(oidTarget);
                $("#indukIdB").val(oidInduk);
                $("#induk2IdB").val(oidInduk2);
                $("#transferIdB").val(oidTransfer); 
                $("#cmdTypeB").val(cmdtype); 
                document.generalformB.command.value="<%=Command.EDIT%>";
                //document.frm_matdispatch.action="add_produksi_item.jsp";
                //document.generalformA.submit();
                $("#addPenjualan").modal("show");
            });
            
            //first add
            $("#btnAddBahanBaku1").click(function (){
                $("#addBahanBaku").modal("show");
                $("#itemA").val("");
                $("#stokA").val("");
                $("#unitA").val("");
                $("#hargaA").val("");
                $("#qtyA").val("");
                $("#sisaA").val("");
                $("#totalA").val("");
                
                var cmdType = $(this).data("cmdtype");
                $("#cmdType").val(cmdType);
                var oidGroup = $(this).data("group");
                $("#groupId").val(oidGroup);
                var oidGroupsame = $(this).data("group2");
                $("#groupId2").val(oidGroupsame);
            });
            
            //Button ADD
            $(".btnAddBahanBaku").click(function (){
                $("#addBahanBaku").modal("show");
                $("#itemA").val("");
                $("#stokA").val("");
                $("#unitA").val("");
                $("#hargaA").val("");
                $("#qtyA").val("");
                $("#sisaA").val("");
                $("#totalA").val("");
                
                var cmdType = $(this).data("cmdtype");
                $("#cmdType").val(cmdType);
                var oidGroup = $(this).data("group");
                $("#groupId").val(oidGroup);
                $("#transferId").val("0");
                
            });
            
            //ADD ITEM PENJUALAN (TARGET)
            $(".btnAddPenjualan").click(function (){
               $("#addPenjualan").modal("show"); 
               
               $("#itemB").val("");
                $("#unitB").val("");
                $("#hargaB").val("");
                $("#qtyB").val("");
                $("#totalB").val("");
                
                var cmdType = $(this).data("cmdtype");
                $("#cmdTypeB").val(cmdType);
                var oidGroup = $(this).data("group");
                $("#groupIdB").val(oidGroup);
                $("#transferIdB").val("0");
            });
            
            $("#btnAddBahanBakuModal").click(function (){

                document.generalformA.command.value="<%=Command.SAVE%>";
                document.generalformA.action="harga_pokok_item.jsp";
                document.generalformA.submit();
            });
            
            $("#btnAddPenjualanModal").click(function (){

                document.generalformB.command.value="<%=Command.SAVE%>";
                document.generalformB.action="harga_pokok_item.jsp";
                document.generalformB.submit();
            });
            
            //ADD GROUP
            $("#btnAddGroup").click(function(){
               $("#transferId").val("0"); 
               $("#groupId").val("0"); 
               $("#groupId2").val("0"); 
               $("#cmdType").val("0"); 
               document.generalformA.command.value="<%=Command.ADD%>";
               $("#addBahanBaku").modal("show");
            });
            
            //DELETE ITEM SOURCE
            $(".deleteSource").click(function(){
                var agree=confirm("Are you sure want to delete this Item?");
                if (agree){
                    var trf = $(this).data("transferid");
                    $("#transferId").val(trf);
                    $("#cmdType").val("1");
                    document.generalformA.command.value="<%=Command.DELETE%>";
                    document.generalformA.submit();
                }
            });
            
            //DELETE ITEM TARGET
            $(".deleteTarget").click(function(){
                var agree=confirm("Are you sure want to delete this Item?");
                if (agree){
                    var trf = $(this).data("transferid");
                    $("#transferIdB").val(trf);
                    $("#cmdType").val("1");
                    document.generalformB.command.value="<%=Command.DELETE%>";
                    document.generalformB.submit();
                }
            });
            
            //DELETE GROUP
            $(".btnDeleteGroup").click(function(){
                var agree = confirm("Are you sure want to delete this group?");
                if (agree){
                    var group = $(this).data("group");
                    $("#transferId").val("0");
                    $("#groupId2").val("0"); 
                    $("#cmdType").val("0");
                    $("#groupId").val(group);
                    document.generalformA.command.value="<%=Command.DELETE%>";
                    document.generalformA.submit();
                }
            });
            
            //ADD PEMBIAYAAN
            $(".btnAddPembiayaan").click(function(){
                $("#asalPembiayaan").val("");
                $("#tujuanPembiayaan").val("");
                
                $("#oidCosting").val("");
                $("#kodePembiayaan").val("");
                $("#remarkPembiayaan").val("");
                
                $("#sizeItem").val("");
                $("#tipePembiayaan").val("");
                $("#statusPembiayaan").val("");
                $("#tipePembiayaan").val("");
                
               $("#addPembiayaan").modal("show"); 
            });
            
            //EDIT PEMBIAYAAN
            $(".editPembiayaan").click(function(){
                
                var oid = $(this).data("oid");
                var sku = $(this).data("sku");
                var remark = $(this).data("remark");
                var asal = $(this).data("asal");
                var tujuan = $(this).data("tujuan");
                var size = $(this).data("size");
                var status = $(this).data("status");
                var tipe = $(this).data("tipe");
                
                $("#asalPembiayaan").val(asal);
                $("#tujuanPembiayaan").val(tujuan);
                
                $("#oidCosting").val(oid);
                $("#kodePembiayaan").val(sku);
                $("#remarkPembiayaan").val(remark);
                $("#tipePembiayaan").val(tipe);
                
                $("#sizeItem").val(size);
                
                //CLOSED
                var closedDoc = $("#select3").html();
                
                
                //ADD FINAL STATUS 
                var select1 = $("#select1").html();
                var select2 = $("#select2").html();
                
                if (status == "5"){
                    $("#statusPembiayaan").html(closedDoc);
                } else {
                    if (size > 0 ){
                        $("#statusPembiayaan").html(select2);
                    } else {
                        $("#statusPembiayaan").html(select1);
                    }
                }
                
                
                $("#statusPembiayaan").val(status);
                
                
                
                $("#addPembiayaan").modal("show"); 
            });
            
             //Timepicker
            $('.datetimepicker').datetimepicker({
                format: "yyyy-mm-dd hh:ii:ss",
                autoclose: true
            });
            
            //SAVE PEMBIAYAAN
            $("#btnAddPembiayaanModal").click(function(){
                var induk1 = $(this).data("oidinduk1");
                var induk2 = $(this).data("oidinduk2");
                var status = $("#statusPembiayaan").val();
                
                if (status === "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                    var agree = confirm("Apakah Anda yakin akan melakukan posting dokumen?");
                    if (agree) {
                        document.generalformA.command.value="<%=Command.ADD%>";
                        $("#command2").val("<%= Command.SAVE %>"); 
                        $("#app_command2").val("<%= Command.SAVE %>");
                        document.generalformC.submit();
                    }
                } else {
                
                    //after saving
    //                $("#indukId").val(induk1);
    //                $("#induk2Id").val(induk2);
                    document.generalformA.command.value="<%=Command.ADD%>";

                    $("#command2").val("<%= Command.SAVE %>"); 
                    $("#app_command2").val("<%= Command.SAVE %>");

                    //document.generalformC.action="add_harga_pokok_group.jsp";
                    document.generalformC.submit();
                    //document.generalformA.submit();
                }
            });
            
            //DETAIL PEMBIAYAAN
            $(".detailPembiayaan").click(function(){
                var oid = $(this).data("oid");
                $("#oidDetail").val(oid);
                document.mainform.submit();
            });
            
            //ADD ITEM FOR PEMBIAYAAN
            $("#addItem").click(function(){
                $("#formPembiayaan").modal("show");
            });
            
            
            //SAVE ITEM PEMBIAYAAN
            $("#btnAddPembiayaanFinal").click(function(){
                save = $(this).data("save");
                $("#command3").val("<%= Command.SAVE %>"); 
                $("#app_command3").val("<%= Command.SAVE %>");
                $("#hidden_costing_id3").val("<%= oidTes.get(0) %>");
                document.generalformE.submit();
            });
            
            
            //DELETE GROUP PEMBIAYAAN
            $(".deletePembiayaan").click(function(){
                var agree = confirm("Are you sure want to delete this group?");
                if (agree){
                    var oid = $(this).data("oid");
                    $("#command2").val("<%=Command.DELETE%>");
                    $("#app_command2").val("<%=Command.DELETE%>");
                    $("#oidCosting").val(oid); 
                    document.generalformC.submit();
                    //document.generalformA.submit();
                }
            });
            
            //AUTOCOMPLETE
            //NAMA
            var availableTags = <%= vector.toString() %>;
            $("#itemA").autocomplete({
               
              source: availableTags
            });
            var availableTags2 = <%= vector2.toString() %>;
            $("#itemB").autocomplete({
               
              source: availableTags2
            });
            var availableTags2 = <%= vector2.toString() %>;
            $("#itemE").autocomplete({
               
              source: availableTags2
            });
            
            
            //ENTER in ItemA
            $('#itemA').keypress(function(e) {
                if(e.which == 13) {
                    var value = $("#itemA").val();
                    $("#cdoc").modal("show");
                    runDataTables();
                    $('#cdoc input[type="search"]').val(value).keyup();
                    $('#cdoc input[type="search"]').keyup();
                    }
            });            
            $('#itemB').keypress(function(e) {
                if(e.which == 13) {
                    var value = $("#itemB").val();
                    $("#cdoc").modal("show");
                    runDataTables2();
                    $('#cdoc input[type="search"]').val(value).keyup();
                    $('#cdoc input[type="search"]').keyup();
                    $('#cdoc input[type="search"]').keyup();
                    }
            });            
            $('#itemE').keypress(function(e) {
                if(e.which == 13) {
                    var value = $("#itemE").val();
                    $("#cdoc").modal("show");
                    runDataTables2();
                    $('#cdoc input[type="search"]').val(value).keyup();
                    $('#cdoc input[type="search"]').keyup();
                    $('#cdoc input[type="search"]').keyup();
                    }
            });            
            
            
            //ENTER KEY
            function keyEnter(element1, element2) {
                $(element1).keypress(function (e) {
                    if (e.which == 13) {
                        $(element2).focus();
                        return false;
                    }

                });
            }
            keyEnter("#sisaA", "#btnAddBahanBakuModal");
            keyEnter("#qtyB", "#btnAddPenjualanModal");
            keyEnter("#tanggalPembiayaan", "#statusPembiayaan");
            keyEnter("#statusPembiayaan", "#asalPembiayaan");
            keyEnter("#asalPembiayaan", "#tujuanPembiayaan");
            keyEnter("#tujuanPembiayaan", "#tipePembiayaan");
            keyEnter("#tipePembiayaan", "#remarkPembiayaan");
            keyEnter("#remarkPembiayaan", "#btnAddPembiayaanModal");
            keyEnter("#qtyE", "#btnAddPembiayaanFinal");
            
            

            ///////////////////////////DATA TABLE////////////////////////////////
            
            function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                var datafilter = "";
                var privUpdate = "";
                $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                $("#" + elementId).dataTable({"bDestroy": true,
                    "iDisplayLength": 10,
                    "bProcessing": true,
                    "oLanguage": {
                        "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                    },
                    "bServerSide": true,
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&FRM_FLD_LOCATION_ID=<%=df.getLocationId()%>",
                    aoColumnDefs: [
                        {
                            bSortable: false,
                            aTargets: [-1]
                        }
                    ],
                    "initComplete": function (settings, json) {
                        if (callBackDataTables !== null) {
                            callBackDataTables();
                        }
                    },
                    "fnDrawCallback": function (oSettings) {
                        if (callBackDataTables !== null) {
                            callBackDataTables();
                        }
                    },
                    "fnPageChange": function (oSettings) {

                    }
                });
                $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
                $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
                $("#" + elementId).css("width", "100%");
            }
            
            function runDataTables() {
                dataTablesOptions("#tableModal", "tabletableModal", "AjaxBahanBaku", "listevent", null);
            }
            function runDataTables2() {
                dataTablesOptions("#tableModal", "tabletableModal", "AjaxBahanBaku", "listevent2", null);
            }
            
        });
    </script> 
    
        <!--///////////MODAL BAHAN BAKU///////////-->
    
    <div style="z-index: 9999" id="cdoc" class="modal fade nonprint" tabindex="-1">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title"></h4>
                </div>
                <form id="generalform">

                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="tableModal" class="box-body addeditgeneral-body table-responsive">
                                    <table class="table table-bordered table-striped table-info">
                                        <thead class="thead-inverse">
                                            <tr>
                                                <th>No.</th>
                                                <th>SKU</th>
                                                <th>Nama Barang</th>  
                                                <th>Unit</th>  
                                                <th>Qty Stok</th>  
                                            </tr>
                                        </thead>
                                        
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">                                
                        <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Close</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
        
        <!--///////////MODAL ADD LIST BAHAN BAKU///////////-->
    
    <div id="addBahanBaku" class="modal fade nonprint" tabindex="0">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title">Input Data</h4>
                </div>
                <form id="generalform" role="form" name="generalformA">
                    
                    
                    
                    
                    <input type="hidden" name="command" value="<%=iCommand%>">

                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                    <input type="hidden" name="start_item" value="<%=startItem%>">

                    <input type="hidden" id="indukId" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
                    <input type="hidden" id="induk2Id" name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="transferId" name="hidden_dispatch_receive_item_id" value="<%=oidDispatchReceiveItem%>">
                    <input type="hidden" id="groupId" name="hidden_df_rec_group_id"value="<%=oidDfRecGroup%>">
                    <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">
                    <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="groupId2" name="hidden_df_rec_same_group_id" value="<%=dfRecItem.getDfRecGroupId()%>">
                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                    <input type="hidden" id="cmdType" name="command_type" value="<%=commandType%>">
                    <input type="hidden" name="start_group" value="<%=startItem%>">
                    <input type="hidden" id="sourceId" name="hidden_dispatch_item_id" value="">
                    <input type="hidden" name="type_doc" value="">
                    <input type="hidden" id="targetId" name="hidden_receive_item_id" value="">
                    <input type="hidden" name="trap" value="">
                    <input type="hidden" id="idMatSource" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]%>" value="<%= dfRecItem.getSourceItem().getMaterialId()%>">
                    <input type="hidden" id="oidSource" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID]%>" value="<%= dfRecItem.getSourceItem().getOID()%>">
                    <input type="hidden" id="oidTargetForSource"  name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]%>" value="<%= dfRecItem.getTargetItem().getOID()%>">
                    <input type="hidden" id="unitidSource" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]%>" value="<%= dfRecItem.getSourceItem().getUnitId()%>">
                    <input type="hidden" id="avblSource" name="avbl_qty" value="<%= objMaterialStock.getQty()%>">

                    
                   
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="bodyAddBahanBaku" class="box-body addeditgeneral-body">
                                    <div class="col-md-12">
                                        <h5 class="box-title">Source</h5>
                        <div class="box box-warning">
                            <div class="box-body">

                                <div class="row">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label>Nama Item</label>
                                            <div class="input-group input-group-sm">
                                                <input  id="itemA" type="text" class="form-control" onkeyup="" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_NAME_SOURCE] %>" value="">
                                                <span class="input-group-btn">
                                                    <button type="button" data-kode="0" id="btnCheckA" class="btnCheck btn btn-info btn-flat"><i class="fa fa-search"></i></button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label>Sisa</label>
                                            <input id="sisaA" type="text" class="form-control" name="sisaA" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label>Stok</label>
                                            <input readonly=""  id="stokA" type="text" class="form-control" name="" value="">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-2">
                                        <div class="form-group">
                                            <label>Qty</label>
                                            <input readonly=""  id="qtyA" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE] %>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group">
                                            <label>Unit</label>
                                            <input readonly=""  id="unitA" type="text" class="form-control" name="" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label>Harga</label>
                                            <input readonly="" id="hargaA" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_SOURCE] %>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label>Sub Total</label>
                                            <input readonly="" id="totalA" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_TOTAL_SOURCE] %>" value="">
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>

                    </div>
                                        
                       
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">  
                        <button id="btnAddBahanBakuModal" data-oidgroupsame="<%= oidDfRecGroupSame %>" type="button" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;Simpan</button>
                        <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
        <!--///////////MODAL ADD LIST PENJUALAN///////////-->
    
    <div id="addPenjualan" class="modal fade nonprint" tabindex="0">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title">Input Data</h4>
                </div>
                <form id="generalform" role="form" name="generalformB">
                    
                    <input type="hidden" name="command" value="<%=iCommand%>">

                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                    <input type="hidden" name="start_item" value="<%=startItem%>">

                    <input type="hidden" id="indukIdB" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
                    <input type="hidden" id="induk2IdB" name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="transferIdB" name="hidden_dispatch_receive_item_id" value="<%=oidDispatchReceiveItem%>">
                    <input type="hidden" id="groupIdB" name="hidden_df_rec_group_id"value="<%=oidDfRecGroup%>">
                    <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">
                    <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="groupId2B" name="hidden_df_rec_same_group_id" value="<%=dfRecItem.getDfRecGroupId()%>">
                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                    <input type="hidden" id="cmdTypeB" name="command_type" value="<%=commandType%>">
                    <input type="hidden" name="start_group" value="<%=startItem%>">
                    <input type="hidden" id="sourceIdB" name="hidden_dispatch_item_id" value="">
                    <input type="hidden" name="type_doc" value="">
                    <input type="hidden" id="targetIdB" name="hidden_receive_item_id" value="">
                    <input type="hidden" name="trap" value="">
                    
                    <!--RIGHT-->
                    <input type="hidden" id="idMatTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]%>" value="<%= dfRecItem.getTargetItem().getMaterialId()%>">
                    <input type="hidden" id="oidTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]%>" value="<%= dfRecItem.getTargetItem().getOID()%>">
                    <input type="hidden" id="unitidTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]%>" value="<%= dfRecItem.getTargetItem().getUnitId()%>">
                    <input type="hidden" id="avbltarget" name="avbl_qty" value="<%= objMaterialStock.getQty()%>">
                    <input type="hidden" id="oidSourceForTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID]%>" value="<%= dfRecItem.getSourceItem().getOID()%>">
                    
                    
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="bodyAddPenjualan" class="box-body addeditgeneral-body">

                                        
                        <!--TARGET-->
                        <div class="col-md-12">
                                <h5 class="box-title">Target</h5>        
                        <div class="box box-success">
                            
                            <div class="box-body">

                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Nama Item</label>
                                            <div class="input-group input-group-sm">
                                                <input  id="itemB" type="text" class="form-control" onkeyup="" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_NAME_TARGET] %>" value="">
                                                <span class="input-group-btn">
                                                    <button type="button" data-kode="1" id="btnCheckB" class="btnCheck btn btn-info btn-flat"><i class="fa fa-search"></i></button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Qty</label>
                                            <input  id="qtyB" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Unit</label>
                                            <input readonly=""  id="unitB" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_UNIT_TARGET_ID] %>" value="">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Harga</label>
                                            <input readonly="" id="hargaB" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET] %>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Sub Total</label>
                                            <input readonly="" id="totalB" type="text" class="form-control" name="<%= FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_TOTAL_TARGET] %>" value="">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> 
                    
                        

                                    </div>
                          
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">  
                        <button id="btnAddPenjualanModal" data-oidgroupsame="<%= oidDfRecGroupSame %>" type="button" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;Simpan</button>
                        <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
                        
                        
        <!--///////////MODAL ADD LIST PEMBIAYAAN///////////-->
    
    <div id="addPembiayaan" class="modal fade nonprint" tabindex="0">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title">Input Data</h4>
                </div>
                <form id="generalform" role="form" name="generalformC">
                    <input type="hidden"  name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
                    <input type="hidden"  name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="command2" name="command2" value="">
                    <input type="hidden" name="prev_command" value="<%=prevCommand2%>">
                    <input type="hidden" name="start" value="<%=startItem2%>">
                    <input type="hidden" name="command_item" value="<%=cmdItem2%>">
                    <input type="hidden" id="app_command2" name="approval_command" value="<%=appCommand2%>">
                    <input type="hidden" id="oidCosting" name="hidden_costing_id" value="<%=oidCostingMaterial%>">
                    <input type="hidden" name="hidden_costing_item_id" value="">
                    <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatCosting.FLD_TYPE_COSTING_LOCATION_HPP %>">
                    <input type="hidden" name="enable_stock_balance" value ="<%=enableStockBalance%>">
                    <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=df2.getCashCashierId()==0? userId : df2.getCashCashierId() %>">
                    <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_DOCUMENT_ID]%>" value="<%=df.getOID() %>">
                    
                    <input type="hidden" id="sizeItem" name="sizeItem" value="">

                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="bodyAddPembiayaan" class="box-body addeditgeneral-body">

                                  <div class="col-md-12">
                        <div class="box box-danger">
                            <div class="box-header with-border">
                                <h3 class="box-title">Pembiayaan</h3>
                            </div>
                            <div class="box-body">

                                <div class="row">
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Kode</label>
                                            <input disabled="true" id="kodePembiayaan" type="text" class="form-control" onkeyup="" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_CODE]%>" value="<%=df2.getCostingCode()%>">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Tanggal</label>
                                            <input  id="tanggalPembiayaan" class="datetimepicker form-control" type="text" name="FRM_FIELD_COSTING_DATE_STRING" value="<%= Formater.formatDate(df2.getCostingDate(), "yyyy-MM-dd HH:mm:ss") %>">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Status</label>
                                            <%
                                                            //CHECK ITEM PEMBIAYAAN FOR DISPLAY FINAL CONDITION
//                                                            Vector listPembiayaan = PstMatCostingItem.list(0, 0, PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+" = "
//                                                                    +oidCostingMaterial , null);
                                                            
                                                            Vector statusDocVal2 = new Vector();
                                                            Vector statusDocKey2 = new Vector();

                                                            statusDocVal2.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                                            statusDocVal2.add(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);

                                                            statusDocKey2.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                                            statusDocKey2.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);

//                                                            if (!listPembiayaan.isEmpty()) {
//                                                                statusDocVal2.add(I_DocStatus.DOCUMENT_STATUS_FINAL);
//                                                                statusDocKey2.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
//                                                            }
                                                        %>
                                                        <select id="statusPembiayaan" class="form-control" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_STATUS] %>" value="<%= df2.getCostingStatus() %>">
                                                            <%
                                                                for (int i = 0; i < statusDocVal2.size(); i++) {
                                                                    if (df.getDispatchStatus() == Integer.parseInt("" + statusDocVal2.get(i))) {
                                                            %>
                                                            <option selected="" value="<%= statusDocVal2.get(i)%>"><%= statusDocKey2.get(i)%></option>
                                                            <%
                                                            } else {
                                                            %>
                                                            <option value="<%= statusDocVal2.get(i)%>"><%= statusDocKey2.get(i)%></option>

                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Consume</label>
                                            <input disabled="true" id="consumePembiayaan" type="text" class="form-control" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CASH_CASHIER_ID] %>" value="<%= df2.getCashCashierId() %>">
                                        </div>
                                    </div>
                                    
                                </div>
                                
                                <div class="row">
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Lokasi Asal</label>
                                            <%
                                                MatDispatch mdp = PstMatDispatch.fetchExc(oidDispatchMaterial);
                                                Vector listLocation1 = new Vector();
                                                listLocation1 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = "
                                                        +mdp.getLocationId() , null);
                                                
                                                if (listLocation1.size() > 0) {
                                            %>
                                            <select id="asalPembiayaan" type="text" class="form-control" onkeyup="" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_LOCATION_ID] %>" value="<%= df2.getLocationId() %>">
                                            
                                            <%
                                                for (int i = 0; i < listLocation1.size(); i++) {
                                                    Location location1 = (Location) listLocation1.get(i);
                                                    if (df2.getLocationId() == location1.getOID()) {
                                            %>
                                                <option selected="" value="<%= location1.getOID()%>"><%= location1.getName()%></option>
                                            <%
                                                } else {
                                            %>
                                                <option value="<%= location1.getOID()%>"><%= location1.getName()%></option>

                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Lokasi Tujuan</label>
                                            <%
                                                Vector listLocation2 = new Vector();
                                                listLocation2 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = "
                                                        +mdp.getDispatchTo(), null);
                                                
                                                if (listLocation2.size() > 0) {
                                            %>
                                            <select  id="tujuanPembiayaan" type="text" class="form-control" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_TO] %>" value="<%= df2.getCostingTo() %>">
                                            <%
                                                for (int i = 0; i < listLocation2.size(); i++) {
                                                    Location location2 = (Location) listLocation2.get(i);
                                                    if (df2.getCostingTo()== location2.getOID()) {
                                            %>
                                                <option selected="" value="<%= location2.getOID()%>"><%= location2.getName()%></option>
                                            <%
                                                } else {
                                            %>
                                                <option value="<%= location2.getOID()%>"><%= location2.getName()%></option>

                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Tipe Biaya</label>
                                            <%
                                                Vector listType = new Vector();
                                                listType = PstCosting.listAll();
                                                
                                                if (!listType.isEmpty()){
                                            %>
                                            <select id="tipePembiayaan" type="text" class="form-control" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_ID] %>" value="<%= df2.getCostingId() %>">
                                            <%
                                                for (int i = 0; i < listType.size(); i++) {
                                                    Costing costing = (Costing) listType.get(i);
                                                    if (df2.getCostingId()== costing.getOID()) {
                                            %>
                                                <option selected="" value="<%= costing.getOID()%>"><%= costing.getName()%></option>
                                            <%
                                                } else {
                                            %>
                                                <option value="<%= costing.getOID()%>"><%= costing.getName()%></option>

                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                               
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Keterangan</label>
                                            
                                            <textarea id="remarkPembiayaan" class="form-control" rows="2" name="<%= FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_REMARK] %>"><%= df2.getRemark() %></textarea>
                                        </div>
                                    </div>
                                </div>

                            </div>


                        </div>

                    </div>      
                        
                          
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">  
                        <button  id="btnAddPembiayaanModal" data-oidinduk2="<%= oidReceiveMaterial %>" data-oidinduk1="<%= oidDispatchMaterial %>" type="button" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;Simpan</button>
                        <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
        
                        
    <!--///////////MODAL ADD LIST PEMBIAYAAN DAN DETAIL///////////-->
    
    <div id="addDetailPembiayaan" class="modal fade nonprint" tabindex="0">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title">Detail Pembiayaan & Input Data</h4>
                </div>
                
                <section class="content">
                    
                    
                <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: #D60000">
                                                    <tr>
                                                        <th>Kode</th>
                                                        <th>Tanggal</th>
                                                        <th>Status</th>
                                                        <th>Consume</th>
                                                        <th>Asal</th>
                                                        <th>Tujuan</th>
                                                        <th>Tipe Biaya</th>
                                                        <th>keterangan</th>
                                                        
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        Vector vCosting1 = new Vector();
                                                        MatCosting matCost1 = new MatCosting();
                                                        
                                                        vCosting1 = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]+" = 4 "
                                                                + " AND "+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]+" = "+oidDetail , null);
                                                        
                                                        for (int z = 0; z < vCosting1.size(); z ++){
                                                            matCost1 = (MatCosting) vCosting1.get(z);
                                                            
                                                            //Tipe Biaya
                                                            Vector vType = PstCosting.list(0, 0, PstCosting.fieldNames[PstCosting.FLD_COSTING_ID]+" = "
                                                                    + matCost1.getCostingId(), null);
                                                            Costing costing = (Costing) vType.get(0);
                                                            
                                                            //LOCATION
                                                            Vector vLoc = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = "
                                                                    + matCost1.getLocationId(), null);
                                                            Location locAsal = (Location) vLoc.get(0);
                                                            Vector vLoc2 = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = "
                                                                    + matCost1.getCostingTo(), null);
                                                            Location locTujuan = (Location) vLoc2.get(0);
                                                            
                                                            int statusConv1 = 0;
                                                            if (matCost1.getCostingStatus() == 0){
                                                                statusConv1 = 0;
                                                            } else if (matCost1.getCostingStatus() == 1){
                                                                statusConv1 = 1;
                                                            } else {
                                                                statusConv1 = 3;
                                                            }
                                                        
                                                    
                                                    %>
                                                   
                                                     <tr>
                                                         
                                                         <td><%= matCost1.getCostingCode() %></td>
                                                         <td><%= matCost1.getCostingDate() %></td>
                                                         <td><%= statusDocKey.get(statusConv1)%></td>
                                                         <td><%= matCost1.getTransferStatus()%></td>
                                                         <td><%= locAsal.getName() %></td>
                                                         <td><%= locTujuan.getName() %></td>
                                                         <td><%= costing.getName() %></td>
                                                         <td><%= matCost1.getRemark()%></td>
                                                         

                                                     </tr>
                                                     <%
                                                         
                                                     
                                                     %>


                                                </tbody>
                                            </table>
                
                <%
                    if (matCost1.getCostingStatus() != 5){
                %>
                <p></p>
                <button id="addItem" type="button" class="btn btn-primary"><i class="fa fa-plus"></i>&nbsp;Tambah Item</button>
                <p></p>
                <%
                    }
                %>
                
                <form id="generalform" role="form" name="generalformD">
                    <input type="hidden" id="oidDetailFormD" name="oidDetailFormD" value="">
                    <%
                        
                    %>
                    
                    <table id="tableItem" class="table table-bordered table-info">
                                <thead class="thead-inverse" style="background-color: #D60000">
                                    <tr>
                                        
                                        <th>Parent</th>
                                        <th>Detail</th>
                                        
                                    </tr>
                                </thead>
                                <tbody>
                                        
                                            
                                            <!--Target-->
                                            <td class="text-center">
                                                
                                                <table id="tableTarget" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: lightcoral">
                                                    <tr>
                                                        <th>SKU</th>
                                                        <th>Nama Barang</th>
                                                        <th>Unit</th>
                                                        <th>Qty</th>
                                                        <th>Stok</th>
                                                        <th>Total</th>
<!--                                                        <th>Action</th>-->
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
//                                                        Vector vCostInduk = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]+" = "
//                                                                +oidDetail , null);
//                                                        if (!vCostInduk.isEmpty()){
//                                                        MatCosting matCosting = (MatCosting) vCostInduk.get(0);
                                                        MatCostingItem matCostingItem = new MatCostingItem();
                                                        Vector vCostitem = PstMatCostingItem.list(0, 0, PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+ " = 0 "
                                                                + " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+ " = " + oidDetail, null);
                                                        for (int j = 0; j < vCostitem.size(); j++){
                                                            matCostingItem = (MatCostingItem) vCostitem.get(j);
                                                            Material mat = PstMaterial.fetchExc(matCostingItem.getMaterialId());
                                                            Unit un = PstUnit.fetchExc(matCostingItem.getUnitId());
                                                    %>
                                                    <tr>
                                                        <td><%= mat.getSku() %></td>
                                                        <td><%= mat.getName() %></td>
                                                        <td><%= un.getCode() %></td>
                                                        <td><%= matCostingItem.getQtyComposite()%></td>
                                                        <td><%= matCostingItem.getHpp()%></td>
                                                        <td><%= matCostingItem.getQtyComposite()*matCostingItem.getHpp() %></td>
                                                        

                                                    </tr>
                                                    <%
                                                        }
                                                        
                                                    %>
                                                    
                                                </tbody>
                                            </table>
                                            </td>
                                            <td class="text-center">
                                                
                                                <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: lightcoral">
                                                    <tr>
                                                        <th>SKU</th>
                                                        <th>Nama Barang</th>
                                                        <th>Unit</th>
                                                        <th>Qty</th>
                                                        <th>Stok</th>
                                                        <th>Total</th>
<!--                                                        <th>Action</th>-->
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        Vector vCostitem1 = PstMatCostingItem.list(0, 0, PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+ " > 0 "
                                                                + " AND "+ PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+ " = " +oidDetail, null);
                                                        for (int j = 0; j < vCostitem1.size(); j++){
                                                            MatCostingItem matCostingItem1 = (MatCostingItem) vCostitem1.get(j);
                                                            Material mat1 = PstMaterial.fetchExc(matCostingItem1.getMaterialId());
                                                            Unit un1 = PstUnit.fetchExc(matCostingItem1.getUnitId());
                                                    %>
                                                    <tr>
                                                        <td><%= mat1.getSku() %></td>
                                                        <td><%= mat1.getName() %></td>
                                                        <td><%= un1.getCode() %></td>
                                                        <td><%= matCostingItem1.getQty() %></td>
                                                        <td><%= matCostingItem1.getHpp() %></td>
                                                        <td><%= matCostingItem1.getQty()*matCostingItem1.getHpp() %></td>

                                                    </tr>
                                                    <%
                                                        
                                                        }
                                                        }
                                                    %>
                                                    
                                                </tbody>
                                            </table>
                                            </td>
                                            
                                            <%
                                                //} else {
                                            %>
<!--                                            <h3>BABI</h3>-->
                                            <%
                                               // }
                                            %>
                                        </tr>
                                        <%
                                            
                                        %>
                                </tbody> 
                            </table>
                    
                    
                    <div class="modal-footer">  
                        <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Cancel</button>
                    </div>
                </form>
                        </section>
            </div>
        </div>
    </div>
              
     <!--///////////MODAL FORM PEMBIAYAAN//////////-->
    
    <div id="formPembiayaan" class="modal fade nonprint" tabindex="0">
        <div class="modal-dialog nonprint modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button  data-save="0" type="button" class="cancelPembiayaan close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title">Input Data</h4>
                </div>
                <form id="generalform" role="form" name="generalformE">
                    <%
                        Vector vCost = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]+ " = 4", null);
                        if (!vCost.isEmpty()){
                            MatCosting mc = (MatCosting) vCost.get(0);
                        
                    %>
                    <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
                    <input type="hidden" name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
                    <input type="hidden" id="idMatPembiayaan" name="<%= FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_MATERIAL_ID]%>" value="">
                    <input type="hidden" id="command3" name="command3" value="">
                    <input type="hidden" name="prev_command3" value="<%=prevCommand3%>">
                    <input type="hidden" id="app_command3" name="approval_command" value="<%=appCommand3%>">
                    <input type="hidden" name="hidden_costing_id3" value="<%=oidDetail %>">
                    <input type="hidden" name="hidden_costing_item_id" value="">
                    <input type="hidden" name="enable_stock_balance3" value ="<%=enableStockBalance3%>">
                    <input type="hidden" name="hidden_costing_item_id3" value="<%=oidCostingMaterialItem3%>">
                    <input type="hidden" name="<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_COSTING_MATERIAL_ID]%>" value="<%= oidDetail %>">
                    <input type="hidden" id="unitIdPembiayaan" name="<%= FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_UNIT_ID] %>" value="">
                    <%
                        }
                    %>
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="bodyAddPenjualan" class="box-body addeditgeneral-body">

                                        
                        <!--Pembiayaan-->
                        <div class="col-md-12">
                                <h5 class="box-title">Costing Content</h5>        
                        <div class="box box-danger">
                            
                            <div class="box-body">

                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Nama Item</label>
                                            <div class="input-group input-group-sm">
                                                <input  id="itemE" type="text" class="form-control" onkeyup="" name="matItem" value="">
                                                <span class="input-group-btn">
                                                    <button type="button" data-kode="2" id="btnCheckE" class="btnCheck btn btn-info btn-flat"><i class="fa fa-search"></i></button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Qty</label>
                                            <input  id="qtyE" type="text" class="form-control" name="<%= FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label>Unit</label>
                                            <input readonly="" id="unitE" type="text" class="form-control" name="unit" value="">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Harga</label>
                                            <input readonly="" id="hargaE" type="text" class="form-control" name="<%= FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_HPP] %>" value="">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label>Sub Total</label>
                                            <input readonly="" id="totalE" type="text" class="form-control" name="sub_total" value="">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> 
                    
                        

                                    </div>
                          
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">  
                        <button id="btnAddPembiayaanFinal" data-save="1" type="button" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;Simpan</button>
                        <button id="cancelPembiayaan" data-save="0" type="button" data-dismiss="modal" class="cancelPembiayaan btn btn-danger"><i class="fa fa-ban"></i> Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
                                
                                
        
</body>
</html>
