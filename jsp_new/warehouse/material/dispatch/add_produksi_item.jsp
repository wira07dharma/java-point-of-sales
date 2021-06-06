<%-- 
    Document   : add_produksi_item.jsp
    Created on : Dec 28, 2017, 3:19:01 PM
    Author     : Ed
--%>
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
<%@include file="../../../main/javainit.jsp" %>
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
%>

<%
    //MODAL DATA
    Material material = new Material();
    Material materialVector = new Material();
    MaterialDetail materialDetail = new MaterialDetail();

    PstMaterial pstMaterial = new PstMaterial();
    PstMaterialDetail pstMaterialDetail = new PstMaterialDetail();

    Vector listMaterial = new Vector();
    Vector listMaterialDetail = new Vector();

    
    listMaterial = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+" = 1 ", null);
     Vector<String> vector = new Vector<String>();
     Vector<String> vector1 = new Vector<String>();
     for (int i = 0; i < listMaterial.size(); i ++){
         materialVector = (Material) listMaterial.get(i);
         vector.add("'" + materialVector.getName() + "'");
         vector1.add("'" + materialVector.getSku()+ "'");
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
iErrCode = ctrlMatDispatchReceiveItem.actionProduksi(iCommand,oidDispatchReceiveItem,oidDfRecGroupSame,oidDispatchMaterial,oidDfRecGroup,commandType, userName, userId);
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Dokumen Produksi</title>

        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/timepicker/bootstrap-timepicker.css">

        <script type="text/javascript" src="../../../styles/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="../../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
        <script type="text/javascript" src="../../../styles/iCheck/icheck.min.js"></script>
        <script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap-notify.js"></script>
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datepicker.js"/>></script>
        <script type="text/javascript" src="../../../styles/timepicker/bootstrap-timepicker.js"></script>
        

    <style>
        .headEdit {font-size:  14px}
        #tableSource {font-size: 12px}
        #tableSource th {color: white}
        #tabletarget th {color: white}
        #tableTarget {font-size: 12px}
        
        .modal {
            position: absolute;
            top: 0;
            right: 0;
            bottom: 00px;
            left: 0;
            z-index: 10040;
            overflow: auto;
            overflow-y: hidden;
         }
        
    </style>  
    
    <script>
      jQuery(function(){
		$("#namaTarget").autocomplete("list.jsp");
	});
    </script>

</head>
<body style="background-color: white">
    
    <section class="content-header">
        <h1>Dokumen Produksi
            <small> Edit</small></h1>
        <ol class="breadcrumb">
            <li>
                <a href="src_produksi.jsp"> Produksi
                    <i class="fa fa-suitcase">

                    </i>
                    <li class="active">Edit</li>
                </a>
            </li>
        </ol>
    </section>
    <div id="divTop">

        <div class="row">
            <div class="col-sm-12">
                <div>
                    <div class="box-body">
                        <form id="mainform" class="form-horizontal">
                            <table class='table'>
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
                        
                        
                        <!--Main Form-->
                        <%
                            if (iCommand == Command.ADD || iCommand == Command.EDIT){
                        %>
                        <section>
                            
                        <%
                            }
                        %>
                        <div class="row">
                                <form role="form" name="frm_matdispatch">
                                
                                        
                                        
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
                                            <input type="hidden" id="unitidSource" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]%>" value="<%= dfRecItem.getSourceItem().getUnitId()%>">
                                            <input type="hidden" id="avblSource" name="avbl_qty" value="<%= objMaterialStock.getQty()%>">

                                <!--RIGHT-->
                                <input type="hidden" id="idMatTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]%>" value="<%= dfRecItem.getTargetItem().getMaterialId()%>">
                                <input type="hidden" id="oidtarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]%>" value="<%= dfRecItem.getTargetItem().getOID()%>">
                                <input type="hidden" id="unitidTarget" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]%>" value="<%= dfRecItem.getTargetItem().getUnitId()%>">
                                <input type="hidden" id="avbltarget" name="avbl_qty" value="<%= objMaterialStock.getQty()%>">
                                
                                        
                                        <%
                                            if (iCommand == Command.ADD || iCommand == Command.EDIT){
                                        %>
                                        <div class="col-sm-12">
                                        <div class="box box-primary">
                                        <div class="box-header with-border">
                                            <h3 class="box-title">Target</h3>
                                        </div>
                    
                                            <div class="box-body">

                                                <div class="row">
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label>SKU</label>
                                                            <div class="input-group input-group-sm">
                                                                <input placeholder="Tekan icon search untuk memilih item..." id="skuTarget" type="text" class="form-control" onkeyup="" name="matCode" value="<%=dfRecItem.getTargetItem().getMaterialTarget().getSku()%>">
                                                                <span class="input-group-btn">
                                                                    <button type="button" data-kode="1" id="btnCheckSKU" class="btnCheck btn btn-info btn-flat"><i class="fa fa-search"></i></button>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label for="namaTarget">Nama Barang</label>
                                                            <div class="input-group input-group-sm">
                                                                 <input placeholder="Tekan icon search untuk memilih item..." id="namaTarget" type="text" class="form-control" name="matItem" value="<%=dfRecItem.getTargetItem().getMaterialTarget().getName()%>">
                                                                <span class="input-group-btn">
                                                                    <button type="button" data-kode="1" id="btnCheckNama" class="btnCheck btn btn-info btn-flat"><i class="fa fa-search"></i></button>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label>Unit</label>
                                                            <input id="unitTarget" type="text" class="form-control" name="unitcodeTarget" value="<%=dfRecItem.getTargetItem().getUnitTarget().getCode() %>" disabled="true">
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label>Qty</label>
                                                            <input id="qtyTarget" type="text" class="form-control" name="<%=frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET]%>" value="<%= FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty()) %>">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label>@Nilai Stok</label>
                                                            <input id="stokTarget" type="text" class="form-control" name="<%= frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET] %>" value="<%= FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getCost())%>" disabled="true">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <div class="form-group">
                                                            <label>Total</label>
                                                            <input id="totalTarget" type="text" class="form-control" name="<%= frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_TARGET]%>" value="<%= FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getTotal())%>" disabled="true">
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>


                                      <%
                                            }
                                      %>
                                      <!--Button-->
                            <div class="form-group">
                                <%
                                    if (iCommand == Command.ADD || iCommand == Command.EDIT){
                                %>
                                <button type="button" class="btn btn-primary" id="btnSave" data-oidinduk2="<%= oidReceiveMaterial %>" data-oidinduk="<%= oidDispatchMaterial %>" data-oidgroupsame="<%= oidDfRecGroupSame %>" data-cmdType="<%= CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM %>" ><i class="fa fa-save"></i>&nbsp;Simpan</button>
                                <%
                                    }
                                %>
                                &nbsp;
                                <a href="add_produksi.jsp?hidden_dispatch_id=<%= oidDispatchMaterial%>&command=<%=Command.EDIT%>&hidden_receive_id=<%= oidReceiveMaterial%>" class="btn btn-primary" id="btnbtn" data-oid="<%= oidDispatchMaterial%>" data-for="addProduksi"><i class="fa fa-chevron-left"></i>&nbsp;Kembali&nbsp;ke&nbsp;Daftar&nbsp;Group</a>
                            </div>   
                                    </div>
                                </div>
                            </form>

                            </div>

                            
                        <%
                            if (iCommand == Command.ADD || iCommand == Command.EDIT){
                        %>
                        </section>
                        <%
                            }
                        %>
                          
                        <section>
                            <div id="divList" class="row">
                                <div class="col-sm-12">
                                    <div class="box box-primary">
                                        <div class="box-header with-border">
                                            <h3 class="box-title">List Items</h3>
                                        </div>
                                        <div>
                                            &nbsp;<button type="button" id="addGroup" data-oidgroup="0" data-oidgroup2="0" class="btn btn-success"><i class="fa fa-plus-circle"></i>&nbsp;Tambah Group Baru</button>
                                        </div>
                                        <br>
                            <table class="table table-bordered table-info">
                                <thead class="thead-inverse" style="background-color: lightblue">
                                    <tr>
                                        <th>Group</th>
                                        <th>Target</th>
                                        <th>Source</th>
                                        
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (int i=0; i < listMatDispatchReceiveItem.size(); i++){
                                        MatDispatchReceiveItem dfRecItem1 = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i);
                                        
                                    %>
                                    
                                        <!-- Modal dialog for delete group -->
                                            <div class="modal fade" id="myModalDeleteGroup" role="dialog">
                                              <div class="modal-dialog modal-sm">
                                                <div class="modal-content">
                                                  <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    <h4 class="modal-title">Delete Group</h4>
                                                  </div>
                                                  <div class="modal-body">
                                                    <p>Are you sure want delete these Group?</p>
                                                  </div>
                                                  <div class="modal-footer">
                                                    <button id="btnDeleteGroupModal" type="button" class="btn btn-primary" data-dismiss="modal">Yes</button>
                                                    <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>
                                                  </div>
                                                </div>
                                              </div>
                                            </div>
                                          </div>
                                    
                                        <tr>
                                            <td class="text-center" style="width: 3%"><%=i+1%>
                                                <div>
                                                    <button data-cmdtype="<%= CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM%>" data-oidtransfer="<%= dfRecItem1.getOID()%>" data-oidgroup="<%= dfRecItem1.getDfRecGroupId()%>" data-oidinduk="<%= dfRecItem1.getDispatchMaterialId()%>" data-oidinduk2="<%= dfRecItem1.getTargetItem().getReceiveMaterialId() %>" class="btn btn-sm btn-info addItem"><i class="fa fa-pencil-square-o"></i></button>
                                                    <a href="#divList" data-oidinduk="<%= dfRecItem1.getDispatchMaterialId()%>" data-oidinduk2="<%= dfRecItem1.getTargetItem().getReceiveMaterialId() %>" data-oidsource="<%= dfRecItem1.getSourceItem().getOID()%>" data-oidtarget="<%= dfRecItem1.getTargetItem().getOID()%>" data-oidgroup="<%= dfRecItem1.getDfRecGroupId()%>" data-oidtransfer="<%= dfRecItem1.getOID() %>" class="btn btn-sm btn-danger btnDeleteGroup"><i  class="fa fa-trash"></i></a>
                                                </div>
                                                
                                            </td>
                                            <%
                                               // if (oidDfRecGroupSame == 0){
                                            %>
                                            
                                            <!--Target-->
                                            <td class="text-center">
                                                
                                                <table id="tableTarget" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: lightseagreen">
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
                                                       String order2="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                       Vector listItem1 = PstMatDispatchReceiveItem.list(0,0,dfRecItem1.getDfRecGroupId(),order2);
                                                       if (!listItem1.isEmpty()){
                                                           MatDispatchReceiveItem dfRecItemAll1 = (MatDispatchReceiveItem) listItem1.get(0);
                                                    %>
                                                    <tr>
                                                        <td><%= dfRecItemAll1.getTargetItem().getMaterialTarget().getSku() %></td>
                                                        <td><%= dfRecItemAll1.getTargetItem().getMaterialTarget().getName() %></td>
                                                        <td><%= dfRecItemAll1.getTargetItem().getUnitTarget().getCode() %></td>
                                                        <td><%= dfRecItemAll1.getTargetItem().getQty() %></td>
                                                        <td><%= dfRecItemAll1.getTargetItem().getCost() %></td>
                                                        <td><%= dfRecItemAll1.getTargetItem().getTotal() %></td>
                                                        

                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                    
                                                </tbody>
                                            </table>
                                            </td>
                                            <td class="text-center">
                                                
                                                <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                                                    <thead class="thead-inverse" style="background-color: lightseagreen">
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
                                                       String order1="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                       Vector listItem = PstMatDispatchReceiveItem.list(0,0,dfRecItem1.getDfRecGroupId(),order1);
                                                       for (int j=0; j < listItem.size(); j++){
                                                           MatDispatchReceiveItem dfRecItemAll = (MatDispatchReceiveItem) listItem.get(j);
                                                    %>
                                                    <tr>
                                                        <td><%= dfRecItemAll.getSourceItem().getMaterialSource().getSku() %></td>
                                                        <td><%= dfRecItemAll.getSourceItem().getMaterialSource().getName() %></td>
                                                        <td><%= dfRecItemAll.getSourceItem().getUnitSource().getCode() %></td>
                                                        <td><%= dfRecItemAll.getSourceItem().getQty() %></td>
                                                        <td><%= dfRecItemAll.getSourceItem().getHpp() %></td>
                                                        <td><%= dfRecItemAll.getSourceItem().getHppTotal() %></td>

                                                    </tr>
                                                    <%
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
                                            }
                                        %>
                                </tbody> 
                            </table>
                                
                            </div>
                                &nbsp;<p>List: <%= sizeAwal%>-<%= sizeList%>, Total: <%= sizeTotal%></p>
                            </div>
                            </div>
                        </section>
                                            
                    </div>   
                </div>   
            </div>   
        </div>   
    </div>

    <script>
        $(document).ready(function () {
            var kode;
          
          //NAMA
            var availableTags = <%= vector.toString() %>;
            $("#namaTarget").autocomplete({
              source: availableTags
            });
            //SKU
            var availableTags1 = <%= vector1.toString() %>;
            $("#skuTarget").autocomplete({
              source: availableTags1
            });
  
            //Check Button
            $("#btnCheckNama").click(function () {
                var value = $("#namaTarget").val();
                kode = $(this).data("kode");
                $("#cdoc").modal("show");
                runDataTables();
                $('#cdoc input[type="search"]').val(value);
                $('#cdoc input[type="search"]').keyup();
            });                        
            $("#btnCheckSKU").click(function () {
                var value = $("#skuTarget").val();
                kode = $(this).data("kode");
                $("#cdoc").modal("show");
                runDataTables();
                $('#cdoc input[type="search"]').val(value);
                $('#cdoc input[type="search"]').keyup();
            });                        
            
            $('#namaTarget').keypress(function(e) {
                if(e.which == 13) {
                    var value = $("#namaTarget").val();
                    kode = $(this).data("kode");
                    $("#cdoc").modal("show");
                    runDataTables();
                    $('#cdoc input[type="search"]').val(value).keyup();
                    }
            });
            $('#skuTarget').keypress(function(e) {
                if(e.which == 13) {
                    var value = $("#skuTarget").val();
                    kode = $(this).data("kode");
                    $("#cdoc").modal("show");
                    runDataTables();
                    $('#cdoc input[type="search"]').val(value).keyup();
                    }
            });
            
            //enter submit
            $('#qtyTarget').keypress(function(e) {
                if(e.which == 13) {
                    $("#btnSave").click();
                }
            });
            

            //MODAL
            var modalSetting = function (elementId, backdrop, keyboard, show) {
                $(elementId).modal({
                    backdrop: backdrop,
                    keyboard: keyboard,
                    show: show
                });
            };
            modalSetting("#myModalDeleteGroup", "static", false, false);
            modalSetting("#cdoc", "static", false, false);
            
            $(".btnDeleteGroup").click(function(){
                var oidGroup = $(this).data("oidgroup");
                var oidSource = $(this).data("oidsource");
                var oidTarget = $(this).data("oidtarget");
                var oidInduk = $(this).data("oidinduk");
                var oidInduk2 = $(this).data("oidinduk2");
                var oidTransfer = $(this).data("oidtransfer");
                $("#groupId").val(oidGroup);
                $("#indukId").val(oidInduk);
                $("#indukId2").val(oidInduk2);                
                $("#myModalDeleteGroup").modal("show");                
            });

            //CHOOSE MATERIAL
            $('body').on("click",".source",function() {
                var oid = $(this).data("oid");
                var nama = $(this).data("nama");
                var qty = $(this).data("qty");
                var sku  = $(this).data("sku");
                var unit  = $(this).data("unit");
                var unitid  = $(this).data("unitid");
                
                if (kode === 0){
                    $("#qtySource").removeAttr("disabled");
                    $("#idMatSource").val(oid);
                    $("#unitidSource").val(unitid);
                    $("#namaSource").val(nama);
                    $("#skuSource").val(sku);
                    $("#stokSource").val(qty);
                    $("#unitSource").val(unit);
                    $("#totalSource").val("?");
                } else {
                   
                    $("#qtyTarget").removeAttr("disabled");
                    $("#idMatTarget").val(oid);
                    $("#unitidTarget").val(unitid);
                    $("#namaTarget").val(nama);
                    $("#skuTarget").val(sku);
                    $("#stokTarget").val(qty);
                    $("#unitTarget").val(unit);
                    $("#totalTarget").val("0");
                }
                $("#cdoc").modal("hide");
                $("#qtyTarget").focus();
                
            });
            
            //save button
            var cmd;
            $("#btnSave").click(function(){
                var oidGroupsame = $(this).data("oidgroupsame");
                $("#groupId2").val(oidGroupsame);
                document.frm_matdispatch.command.value="<%=Command.SAVE%>";
                document.frm_matdispatch.action="add_produksi_item.jsp";
                document.frm_matdispatch.submit();
            });
            
            //Add group
            $("#addGroup").click(function(){               
                var oidGroup = $(this).data("oidgroup");
                var oidGroup2 = $(this).data("oidgroup2");
                
                alert("Silahkan mengisi form yang telah disediakan diatas untuk menambah item pada group baru.");
                $("#groupId").val(oidGroup);
                $("#groupId2").val(oidGroup2);
 
                document.frm_matdispatch.command.value="<%=Command.ADD%>";
                document.frm_matdispatch.action="add_produksi_item.jsp?";
                document.frm_matdispatch.submit();
            });
            
            //EDIT ITEM
            $(".addItem").click(function(){
                var oidGroup = $(this).data("oidgroup");
                var oidSource = $(this).data("oidsource");
                var oidTarget = $(this).data("oidtarget");
                var oidInduk = $(this).data("oidinduk");
                var oidTransfer = $(this).data("oidtransfer");
                
                alert("Silahkan mengisi form yang telah disediakan diatas untuk mengubah item yang Anda pilih.");
                $("#groupId").val(oidGroup);
                $("#groupId2").val("0");
                $("#sourceId").val(oidSource);
                $("#targetId").val(oidTarget);
                $("#indukId").val(oidInduk);
                $("#transferId").val(oidTransfer); 
                document.frm_matdispatch.command.value="<%=Command.EDIT%>";
                document.frm_matdispatch.action="add_produksi_item.jsp";
                document.frm_matdispatch.submit();
                $("#qtySource").removeAttr("disabled");
                $("#qtyTarget").removeAttr("disabled");
            });
            
            //Delete Group
            $("#btnDeleteGroupModal").click(function(){ 
                $("#groupId2").val("0");
                $("#transferId").val("0"); 
                document.frm_matdispatch.command.value="<%=Command.DELETE %>";
                document.frm_matdispatch.action="add_produksi_item.jsp";
                document.frm_matdispatch.submit();
            });
            
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
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>",
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
                dataTablesOptions("#tableModal", "tabletableModal", "AjaxAddProduksiItem", "listevent", null);
            }

        });
    </script>
    
    <!--LIST SEARCH-->
    <div class="ac_results" style="position: absolute; width: 260px; top: 263px; left: 20px;" hidden="true">
        <select hidden="true" class="form-control" id="selectMat" name="matCode" style="max-height: 180px; overflow: auto;">
            <option hidden="true" id="optionMat" value=""></option>
        </select>
    </div>
    <!--//////////////////////-->
    
    <div id="cdoc" class="modal fade nonprint" tabindex="-1">
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
</body>
</html>
