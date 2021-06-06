<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

		 com.dimata.common.entity.location.PstLocation,

		 com.dimata.common.entity.location.Location,

		 com.dimata.gui.jsp.ControlList,

		 com.dimata.qdep.form.FRMHandler,

		 com.dimata.qdep.form.FRMQueryString,

		 com.dimata.qdep.entity.I_PstDocType,

		 com.dimata.gui.jsp.ControlLine,

		 com.dimata.qdep.form.FRMMessage,

		 com.dimata.util.Command,

		 com.dimata.gui.jsp.ControlDate,

		 com.dimata.gui.jsp.ControlCombo,

		 com.dimata.posbo.entity.masterdata.Material,

		 com.dimata.posbo.entity.masterdata.Unit,

		 com.dimata.posbo.entity.masterdata.PstMaterial,

		 com.dimata.posbo.form.warehouse.CtrlMatDispatch,

		 com.dimata.posbo.form.warehouse.FrmMatDispatch,

		 com.dimata.posbo.entity.warehouse.*" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>



<%@ include file = "../../../main/javainit.jsp" %>       

<%	int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
	int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%	boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
	boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!	
	public static final String textListGlobal[][] = {
		{"Transfer", "Edit", "Tidak ada item transfer", "Cetak Transfer", "Proses transfer tidak dapat dilakukan pada lokasi yang sama", "Cetak Delivery Order", "Posting Stock", "Posting Harga Beli"},
		{"Dispatch", "Edit", "There is no Dispatch item", "Print Dispatch", "Transfer cant'be proceed in same location", "Print Delivery Order", "Posting Stock", "Posting Harga Beli"}

	};

	/* this constant used to list text of listHeader */
	public static final String textListOrderHeader[][]
			= {
				{"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Keterangan", "Nota Supplier", "Waktu", "Etalase Asal", "Etalase Tujuan", "Nomor BC", "Jenis Dokumen"},
				{"Number", "From Location", "Destination", "Date", "Status", "Remark", "Supplier Invoice", "Time", "From Etalase", "Etalase Destination", "Customs Number", "Document Type"}

			};

	/* this constant used to list text of listMaterialItem */
	public static final String textListOrderItem[][]= {
		{"No","Invoice","Detail Barang","Status", "Aksi"},
		{"No","Invoice","Detail Item","Status", "Action"}
	};

	public static final String textPosting[][] = {
		{"Anda yakin melakukan Transfer?", "Anda yakin melakukan Posting Harga ?"},
		{"Are You Sure to Transfer? ", "Are You Sure to Posting Cost Price?"}
	};

	public static final String textDelete[][] = {
		{"Apakah Anda Yakin Akan Menghapus Data ini? ", "Hapus dibatalkan!"},
		{"Are You Sure to Delete This Data? ", "Delete cancelled"}
	};

	public static final String textListOrderItemDetail[][] ={
		{"SKU","Nama Barang","Qty","Harga Satuan","Total Harga"},
		{"SKU","Item Name","Qty","Price per Item","Total Price"}
	};

	public static final String textCrud[][] = {
		{"Baru", "Tambah", "Hapus", "Ubah", "Simpan", "Cetak", "ke PDF", "ke Excel", "Kembali"},
		{"New", "Add", "Delete", "Update", "Save", "Print", "to PDF", "to Excel", "Back"}
	};

	public static final String textWarning[][]={
		{"Nomor BC tidak boleh kosong"},
		{"Customs Number must not empty"}
	};

	public double getPriceCost(Vector list, long oid) {
		double cost = 0.00;
		if (list.size() > 0) {
			for (int k = 0; k < list.size(); k++) {
				MatReceiveItem matReceiveItem = (MatReceiveItem) list.get(k);
				if (matReceiveItem.getMaterialId() == oid) {
					cost = matReceiveItem.getCost();
					break;
				}
			}
		}
		return cost;
	}

	
	public String drawListInvoices(int language, Vector objectClass, int start, int docStatus) {

		String result = "";
		if(objectClass != null && objectClass.size() > 0){
			ControlList ctrlist = new ControlList();
			ctrlist.setAreaWidth("100%");
			ctrlist.setListStyle("listgen");
			ctrlist.setTitleStyle("listgentitle");
			ctrlist.setCellStyle("listgensell");
			ctrlist.setHeaderStyle("listgentitle");

			ctrlist.addHeader(textListOrderItem[language][0], "1%");//no
			ctrlist.addHeader(textListOrderItem[language][1], "10%");//invoice
			ctrlist.addHeader(textListOrderItem[language][2], "40%");//detail
			ctrlist.addHeader(textListOrderItem[language][3], "5%");//status
			ctrlist.addHeader(textListOrderItem[language][4], "5%");//Aksi

			ctrlist.setLinkRow(1);
			ctrlist.setLinkSufix("");
			Vector lstData = ctrlist.getData();
			ctrlist.reset();
			int index = -1;
			if (start < 0) {
				start = 0;
			}

			Vector rowx = null;
			for (int i = 0; i < objectClass.size(); i++) {
				start++;
				rowx = new Vector(1, 1);
				Vector temp = (Vector) objectClass.get(i);
				MatDispatchBill mdb = (MatDispatchBill) temp.get(0);
				BillMain bm = (BillMain) temp.get(1);

				rowx.add("<div align=\"center\" style=\"font-size: 12pt; font-weight: bold;\">" + start + "</div>"); //nomor
				rowx.add("<div align=\"center\" style=\"font-size: 12pt; font-weight: bold;\">" + bm.getInvoiceNumber() + "</div>"); //invoice
				Vector billDetails = PstMatDispatchBill.getInvoiceDetail(0, 0, "", "", bm.getOID());
				rowx.add(drawInvoicesDetail(language, billDetails));//detail

				String status = PstBillMain.deliveryStatus[bm.getStatus()];
				rowx.add("<div align=\"center\" style=\"font-size: 12pt; font-weight: bold;\">" + status + "</div>"); //status

				String button = "";
				if(docStatus != I_DocStatus.DOCUMENT_STATUS_CLOSED){
					button = "<div style=\"text-align: center;\">"
								+ "<button type=\"button\""
								+ "align=\"center\" class=\"btn btn-danger delete-btn\""
								+ "style=\"padding-left: 25px; padding-right: 25px;\""
								+ "value=\""+ mdb.getOID() +"\">"
								+ "<i class=\"fa fa-trash\">&nbsp;</i>"
								+ textCrud[language][2]
								+ "</button>"
								+ "</div>";
				}

				rowx.add(button); //tombol aksi

				lstData.add(rowx);
				result = ctrlist.draw();
			}
		} else {
			String msgData = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][2]+"</div>";
			result = msgData;
		}
		return result;
	}


	public String drawInvoicesDetail(int language, Vector objectClass) {

		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader(textListOrderItemDetail[language][0], "10%");//no sku
		ctrlist.addHeader(textListOrderItemDetail[language][1], "10%");//item name
		ctrlist.addHeader(textListOrderItemDetail[language][2], "5%");//qty
		ctrlist.addHeader(textListOrderItemDetail[language][3], "10%");//price
		ctrlist.addHeader(textListOrderItemDetail[language][4], "10%");//total price

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		ctrlist.reset();

		Double totalqty = 0.0;
		Double totalprice = 0.0;
		Vector rowx = new Vector(1, 1);
		for (int i = 0; i < objectClass.size(); i++) {
			rowx = new Vector(1, 1);
			Billdetail bd = (Billdetail) objectClass.get(i);

			rowx.add("<div align=\"center\">" + bd.getSku() + "</div>"); //sku
			rowx.add("<div align=\"center\">" + bd.getItemName() + "</div>"); //item name
			rowx.add("<div align=\"center\">" + bd.getQty() + "</div>");//qty
			rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(bd.getItemPrice()) + "</div>");//item price
			rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(bd.getTotalPrice()) + "</div>"); //total price

			totalqty += bd.getQty();
			totalprice += bd.getTotalPrice();

			lstData.add(rowx);
		}
		rowx = new Vector(1, 1);
		rowx.add("");
		rowx.add("<div align=\"right\"> Total </div>");
		rowx.add("<div align=\"center\"> " + totalqty + " </div>");
		rowx.add("<div align=\"center\"> - </div>");
		rowx.add("<div align=\"center\"> " + FRMHandler.userFormatStringDecimal(totalprice) + " </div>");
		lstData.add(rowx);
		return ctrlist.draw();
	}


%>

<!-- Jsp Block -->
<%
	/**
	 *
	 * get approval status for create document
	 *
	 */
	I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();

	I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();

	I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();

	int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);


	/**
	 *
	 * get request data from current form
	 *
	 */
	int iCommand = FRMQueryString.requestCommand(request);
	int prevCommand = FRMQueryString.requestInt(request, "prev_command");
	int startItem = FRMQueryString.requestInt(request, "start_item");
	int cmdItem = FRMQueryString.requestInt(request, "command_item");
	int appCommand = FRMQueryString.requestInt(request, "approval_command");
	long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");
	long oidCashBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
	long oidDispatchMatBill = FRMQueryString.requestLong(request, "hidden_dispatch_bill_id");
	long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
	long timemls = System.currentTimeMillis();
	int oidDate = FRMQueryString.requestInt(request, FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]);
	String nomorBC = FRMQueryString.requestString(request, FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]);
	String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE_TRANSFER");
	String syspropHPP = PstSystemProperty.getValueByName("SHOW_HPP_TRANSFER");
	String syspropTotal = PstSystemProperty.getValueByName("SHOW_TOTAL_TRANSFER");

	/**
	 * add opie-eyek 20131205 untuk posting nilai jual
	 */
	/*if(iCommand==Command.REPOSTING){
    try{
        //System.out.println(">>> proses posting doc : "+oidReceiveMaterial);
        SessPosting sessPosting = new SessPosting();
        sessPosting.postedDispatchDoc(oidDispatchMaterial);
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}*/
	if (oidReceiveMaterial != 0 && iCommand == Command.EDIT) {

		try {
			String whereItemList = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "='" + oidReceiveMaterial + "'";
			String orderItemList = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
			Vector listItemRec = PstMatReceiveItem.listWithCurrentHPP(0, 1000, whereItemList, orderItemList);
			if (listItemRec != null && listItemRec.size() > 0) {
				for (int i = 0; i <= listItemRec.size(); i++) {
					MatReceiveItem matRI = (MatReceiveItem) listItemRec.get(i);
					if (matRI != null) {
						MatDispatchItem matDfi = new MatDispatchItem();
						matDfi.setDispatchMaterialId(oidDispatchMaterial);
						matDfi.setMaterialId(matRI.getMaterialId());
						matDfi.setUnitId(matRI.getUnitId());
						matDfi.setHpp(matRI.getCost());
						try {
							PstMatDispatchItem.insertExc(matDfi);

						} catch (Exception e) {
						}
					}
				}
			}

		} catch (Exception e) {

			System.out.println(e);

		}

	} else if(oidDispatchMatBill != 0 && iCommand == Command.DELETE){
		try {
			PstMatDispatchBill.deleteExc(oidDispatchMatBill);
		} catch (Exception e) {
		}
	}

	/**
	 *
	 * initialization of some identifier
	 *
	 */
	String errMsg = "";

	int iErrCode = FRMMessage.ERR_NONE;

	/**
	 *
	 * dispatch code and title
	 *
	 */
	String dfCode = "";//i_pstDocType.getDocCode(docType);

	String dfTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);

	String dfItemTitle = dfTitle + " Item";

	/**
	 *
	 * action process
	 *
	 */
	ControlLine ctrLine = new ControlLine();
	CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
//by dyas
//tambah userName dan userId
	iErrCode = ctrlMatDispatch.action(iCommand, oidDispatchMaterial, userName, userId);
	FrmMatDispatch frmdf = ctrlMatDispatch.getForm();
	MatDispatch df = ctrlMatDispatch.getMatDispatch();
	errMsg = ctrlMatDispatch.getMessage();

	/**
	 * add opie-eyek 20131205 untuk posting stock
	 */
	if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
		try {
			//System.out.println(">>> proses posting qty penerimaan tanpa PO : "+oidReceiveMaterial);
			SessPosting sessPosting = new SessPosting();
			sessPosting.postedDispatchDoc(oidDispatchMaterial);
			//rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
			df.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
			iCommand = Command.EDIT;
		} catch (Exception e) {
			iCommand = Command.EDIT;
		}
	}

//double curr = PstCurrencyRate.getLastCurrency();
	String priceCode = "Rp.";

//add opie, melakukan pengecekan apakah lokasi tujuan di assign ke user
	boolean locationAssign = false;
	boolean privManageData = true;
	if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
		privManageData = false;
	}

	/**
	 * list purchase order item
	 */
//	oidDispatchMaterial = df.getOID();
//	oidCashBillMain = df.getCashBillMainId();

	int recordToGetItem = 10;
	String whereClause = PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID] + "="+ oidDispatchMaterial;
	int vectSizeItem = PstMatDispatchBill.getCount(whereClause);

	
	if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
		startItem = ctrlMatDispatch.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
	}
	
	Vector listMatDispatchBC = PstMatDispatchBill.getListInvoices(startItem, recordToGetItem, whereClause, "");
	
	if (listMatDispatchBC.size() < 1 && startItem > 0) {
		if (vectSizeItem - recordToGetItem > recordToGetItem) {
			startItem = startItem - recordToGetItem;
		} else {
			startItem = 0;
			iCommand = Command.FIRST;
			prevCommand = Command.FIRST;
		}
		listMatDispatchBC = PstMatDispatchBill.getListInvoices(startItem, recordToGetItem, whereClause, "");
	}
	//Vector listMatDispatchItem = PstMatDispatchItem.list(startItem, recordToGetItem, oidDispatchMaterial, order);

	//Vector list = drawListOrderItem(SESS_LANGUAGE, listMatDispatchItem, startItem, df.getInvoiceSupplier(), tranUsedPriceHpp, brandInUse, privManageData, privShowQtyPrice, approot, typeOfBusinessDetail, df.getDispatchItemType(), syspropEtalase, syspropTotal);
	//Vector listError = (Vector) list.get(1);

	double total = PstMatDispatchItem.getTotalTransfer(oidDispatchMaterial);

%>

<!-- End of Jsp Block -->



<html>

	<head>
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
    <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>

		<title>Dimata - ProChain POS</title>

<script language="JavaScript">

<!--

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

function cmdEdit(oid){

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}



function gostock(oid){

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.hidden_dispatch_item_id.value=oid;

    document.frm_matdispatch.hidden_dispatch_id.value="<%=oidDispatchMaterial%>";

    document.frm_matdispatch.action="df_stockcode.jsp";

    document.frm_matdispatch.submit();

}



function compare(){

    var dt = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_dy.value;
    var mn = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_mn.value;
    var yy = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_yr.value;
    var dt = new Date(yy,mn-1,dt);
    var bool = new Boolean(compareDate(dt));
    return bool;

}




function cmdSave() {
    <%
    if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
            var transfer_from = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID]%>.value;
            var transfer_to = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO]%>.value;
			var nomor_bc = document.frm_matdispatch.<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC] %>.value;
            //alert(transfer_from);
            //alert(transfer_to);
			if(nomor_bc == ""){
				alert("<%= textWarning[SESS_LANGUAGE][0] %>");
			} else {
					if(transfer_from != transfer_to) {
					var statusDoc = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>.value;
					if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
						 var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
						 if(conf){ 
							document.frm_matdispatch.command.value="<%=Command.SAVE%>";
							document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
							document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
							if(compare()==true)
								document.frm_matdispatch.submit();
						}
					}else{
						document.frm_matdispatch.command.value="<%=Command.SAVE%>";
						document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
						document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
						if(compare()==true)
							document.frm_matdispatch.submit();
					}
				} else {
					<%if(typeOfBusinessDetail == 2){%>
						var statusDoc = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>.value;
						if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
							var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
							if(conf){ 
							   document.frm_matdispatch.command.value="<%=Command.SAVE%>";
							   document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
							   document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
							   if(compare()==true)
								   document.frm_matdispatch.submit();
						   }
						}else{
							document.frm_matdispatch.command.value="<%=Command.SAVE%>";
							document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
							document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
							if(compare()==true)
								document.frm_matdispatch.submit();
						}
					<%} else {%>
						alert("<%=textListGlobal[SESS_LANGUAGE][4]%>")
					<%}%>
				}
			}
    <%

    } else {

    %>

        alert("Document has been posted !!!");

    <%

    }

    %>

}

function cmdAsk(oid){

    <%

    if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {

    %>

        document.frm_matdispatch.command.value="<%=Command.ASK%>";

        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

        document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

        document.frm_matdispatch.submit();

    <%

    } else {

    %>

        alert("Document has been posted !!!");

    <%

    }

    %>

}



function cmdCancel(){

    document.frm_matdispatch.command.value="<%=Command.CANCEL%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}

function cmdDelete(oid){

    document.frm_matdispatch.command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}


function cmdConfirmDelete(oid){
	console.log(oid);
    document.frm_matdispatch.command.value="<%=Command.DELETE%>";

     document.frm_matdispatch.hidden_dispatch_bill_id.value=oid;

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	
    document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}
// add by fitra 17-05-2014
function cmdNewDelete(oid){
	var msg;
	msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
	var agree=confirm(msg);
	if (agree)
		return cmdConfirmDelete(oid) ;
	else
		return cmdEdit(oid);
}


function cmdBack(){

    document.frm_matdispatch.command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
    document.frm_matdispatch.<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>.value = 1;

    document.frm_matdispatch.action="srcdf_bc_wh_material.jsp";

    document.frm_matdispatch.submit();

}



function printForm()
{
    <%if(typeOfBusinessDetail == 2){%>
    var typePrint = document.frm_matdispatch.type_print_tranfer.value;
    window.open("print_out_df_stock_wh_material_item.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&type_print_tranfer="+typePrint,"printout_transfer");
    <%} else {%>
    var typePrint = document.frm_matdispatch.type_print_tranfer.value;
    window.open("df_stock_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"&timemls=<%=timemls%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    <%}%>
}

function printDeliveryOrder()
{
    var typePrint = document.frm_matdispatch.type_print_tranfer.value;
    window.open("../../../cashierweb/df_stock_order_print.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"&timemls=<%=timemls%>&hidden_bill_main_id=<%=oidCashBillMain%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function findInvoice()

{

	window.open("df_wh_material_receive.jsp","invoice_supplier","scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");

}


//add opie-eyek 20131205 untuk posting
function PostingStock() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.POSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
        document.frm_matdispatch.submit();
        }
}

//add opie-eyek 20131205 untuk posting
function PostingCostPrice() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.REPOSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
        document.frm_matdispatch.submit();
        }
}

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function addItem()

{

    <%

    if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {

    %>

        document.frm_matdispatch.command.value="<%=Command.ADD%>";

        document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";

        if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

    <%

    } else {

    %>

        alert("Document has been posted !!!");

    <%

    }

    %>

}


function addItemReceive()

{

    <%

    if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {

    %>

        document.frm_matdispatch.command.value="<%=Command.ADD%>";

        document.frm_matdispatch.action="df_stock_material_receive_search.jsp";

        if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

    <%

    } else {

    %>

        alert("Document has been posted !!!");

    <%

    }

    %>

}



function editItem(oid)

{

    <%

    if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {

    %>

        document.frm_matdispatch.command.value="<%=Command.EDIT%>";

        document.frm_matdispatch.hidden_dispatch_item_id.value=oid;

        document.frm_matdispatch.action="df_bc_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    <%

    } else {

    %>

        alert("Document has been posted !!!");

    <%

    }

    %>

}



function itemList(comm, oid){
    console.log(oid);
    document.frm_matdispatch.hidden_dispatch_id.value=oid;
	document.frm_matdispatch.command.value=comm;
    document.frm_matdispatch.prev_command.value=comm;
    document.frm_matdispatch.action="df_bc_wh_material_edit.jsp";
    document.frm_matdispatch.submit();
}


/**
 * by Dyas
 * penambahan function viewHistoryTable() untuk menampilkan halaman logHistory
 */
function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidDispatchMaterial%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function MM_swapImgRestore() { //v3.0

    var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc

    ;i++) x.src=x.oSrc;

}



function MM_preloadImages() { //v3.0

    var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();

    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)

    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}

}



function MM_swapImage() { //v3.0

    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)

    if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}

}



function MM_findObj(n, d) { //v4.01

    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);

    if(!x && d.getElementById) x=d.getElementById(n); return x;

}

//-->

</script>
<style>
      .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
      }
      table.listgen {
        margin: auto 1%;
        text-align: center;
        width: 99% !important;
      }
      a.btn.btn-primary.store {
        margin: 25px 0px 0px 30px;
      }
      .listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3;
      }
      .btn-add {
        font-family: sans-serif;
        text-decoration: none;
        background-color: #6cd85f;
        color: #fff;
        cursor: pointer;
        margin-left: 10px;
      }
      .btnini {
        padding: 10px 8px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
        margin: 10px 0px 0px 10px;
      }
      .btn {
        padding: 5px 8px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
      }
      .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
        cursor: not-allowed;
        background-color: #fff !important;
        opacity: 1;
      }
      select {
    height: 30px !important;
    border: 1px solid #ccc !important;
    width: 100%;
}
textarea.formElemen {
    border-radius: 0px;
    width: 100%;
}
.row {
    margin-bottom: 10px;
}
label.control-label.col-sm-4.padd {
    padding: 10px 0px;
}
label.control-label.col-sm-4.paddd {
    padding: 0px;
}
.col-sm-8 {
    margin-bottom: 10px;
}
label.control-label.col-sm-4 {
    padding-top: 10px;
}
select.tanggal {
    width: 30.9%;
}
i.fa.fa-backward {
    padding: 3px;
}
a.command.btn.btn-warning.atas {
    margin-top: -75px;
    margin-right: 20px;
}
.form-control {
  font-size: 12px
}
select.waktu {
    width: 30.2%;
}
.btn-sm, .btn-group-sm, .btn {
    padding: 5px 8px !important;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
}
    </style>


			<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
			<%if (menuUsed == MENU_ICON) {%>
			<link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
			<%}%>
			<link rel="stylesheet" href="../../../styles/main.css" type="text/css">

			<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

			
		</head>

        <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>', '<%=approot%>')">
      <section class="content-header">
        <h1><%=textListGlobal[SESS_LANGUAGE][0]%> Bea Cukai
            <small> <%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
          <ol class="ol">
            <li>
              <a>
                <li class="active">Transfer</li>
              </a>
            </li>
          </ol>
          </section>
          <p class="border"></p>
          <div class="container-pos"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
              <%if (menuUsed == MENU_PER_TRANS) {%>
              <tr><td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td></tr>
              <tr><td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td>	</tr>
              <%} else {%>
              <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                  <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
              </tr>
              <%}%>
              <tr>
                <td width="88%" valign="top" align="left">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td>
                        <form name="frm_matdispatch" method="post" action="">
                          <%
                            try {
                          %>
                          <input type="hidden" name="command" value="">
                          <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                          <input type="hidden" name="start_item" value="<%=startItem%>">
                          <input type="hidden" name="command_item" value="<%=cmdItem%>">
                          <input type="hidden" name="approval_command" value="<%=appCommand%>">
                          <input type="hidden" name="hidden_dispatch_bill_id" value="<%=oidDispatchMatBill%>">
                          <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
                          <input type="hidden" name="hidden_bill_main_id" value="<%=oidCashBillMain%>">
                          <input type="hidden" name="hidden_dispatch_item_id" value="">
                          <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE%>">
                          <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%=df.getDispatchCode()%>">
                          <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_STORE%>">
                          <input type="hidden" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="<%=oidDate%>">
                          <table width="100%" border="0">
                            <div class="row">
                              <div class="col-sm-12">
                                <!-- left side -->
                                <div class="col-sm-4">
                                  <div class="form-group">
                                    <label class="control-label col-sm-4" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                                    <div class="col-sm-8">
                                        <input id="selectNomor" class="form-control" type="text" name="" value="<%
                                          if (df.getDispatchCode() != "" && iErrCode == 0) {
                                            out.println(df.getDispatchCode());
                                          } else {
                                            out.println("Nomor Otomatis");
                                          }
                                        %>" readonly="">
                                        
                                    </div>
                                  </div>
                                    <div class="form-group">
                                    <label class="control-label col-sm-4" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label>
                                    <div class="col-sm-8">
                                      <%=ControlDate.drawDate(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), "tanggal")%>
                                    </div>
                                  </div>
                                    <div class="form-group">
                                    <label class="control-label col-sm-4" for="waktu"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label>
                                    <div class="col-sm-8">
                                      <%=ControlDate.drawTimeSec(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), "waktu")%>
                                    </div>
                                  </div>
                                </div>
								<!-- center  side -->             
                                <div class="col-sm-4">
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 padd" for="jenisDokumen"><%=textListOrderHeader[SESS_LANGUAGE][11]%></label>
                                    <div class="col-sm-8">
                                      <input type="text" id="jenisDokumen" class="form-control" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_JENIS_DOKUMEN]%>" id="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_JENIS_DOKUMEN]%>"  value="PPBTBB" readonly="">
                                    </div>
                                  </div>
                                    <div class="form-group">
                                    <label class="control-label col-sm-4 padd" for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        Vector obj_locationid = new Vector(1, 1);
                                        Vector val_locationid = new Vector(1, 1);
                                        Vector key_locationid = new Vector(1, 1);
                                        //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                        //add opie-eyek
                                        //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                        whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE
                                                + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_DUTY_FREE + ")";
                                        //whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                        //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int d = 0; d < vt_loc.size(); d++) {
                                          Location loc = (Location) vt_loc.get(d);
                                          val_locationid.add("" + loc.getOID() + "");
                                          key_locationid.add(loc.getName());
                                        }
                                        String select_locationid = "" + df.getLocationId(); //selected on combo box
                                      %>
                                      <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                                   </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        Vector obj_locationid1 = new Vector(1, 1);
                                        Vector val_locationid1 = new Vector(1, 1);
                                        Vector key_locationid1 = new Vector(1, 1);
                                        String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                        String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                        //Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
                                        for (int d = 0; d < vt_loc.size(); d++) {
                                          Location loc1 = (Location) vt_loc.get(d);
                                          val_locationid1.add("" + loc1.getOID() + "");
                                          key_locationid1.add(loc1.getName());
                                        }
                                        String select_locationid1 = "" + df.getDispatchTo(); //selected on combo box
                                      %>
                                      <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> 
                                   </div>
                                  </div>
                                </div>
                                    
                                <!--Right Side-->
                                <div class="col-sm-4">
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 paddd" for="nomorBC"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label>
                                    <div class="col-sm-8">
                                      <%
                                        if (!df.getNomorBeaCukai().equals("")) {
                                          nomorBC = df.getNomorBeaCukai();
                                        }
                                      %>
                                      <input type="text" id="nomorBC" class="form-control" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]%>" id="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]%>" value="<%= nomorBC%>">
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 paddd" for="status"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label>
                                    <div class="col-sm-8">
                                      <%

                                        Vector obj_status = new Vector(1, 1);
                                        Vector val_status = new Vector(1, 1);
                                        Vector key_status = new Vector(1, 1);
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                        //add by fitra
                                        if (listMatDispatchBC.size() > 0) {
                                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                        }

                                        // update opie-eyek 19022013
                                        // user bisa memfinalkan DF tidak bisa mengubah status dari final ke draft jika  jika  :
                                        // 1. punya priv. transfer approve = true
                                        // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                        locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", df.getLocationId());
                      //																	if (listMatDispatchItem.size() > 0 && privApproval == true && locationAssign == true && listError.size() == 0) {
                      //																		val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                      //																		key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                      //																	}

                                        String select_status = "" + df.getDispatchStatus();
                                        if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                          out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                        } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                          out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                        } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval == false || locationAssign == false)) {
                                          out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                        } else {
                                          out.println(ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS], null, select_status, val_status, key_status, "", "formElemen"));
                                        }
                                      %>
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="control-label col-sm-4 paddd "for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label>
                                    <div class="col-sm-8">
                                      <textarea id="textarea" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_REMARK]%>" class="formElemen" style="resize: none;" rows="3"><%=df.getRemark()%></textarea>
                                    </div>
                                  </div> 
                                </div> 
                              </div>
                            </div>
                            <tr>
                              <td colspan="3">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <tr id="topTableBtnSection">
                                    <td colspan="100%" height="50">
                                      <span id="saveButton" class="btn btn-primary">
                                        <i class="fa fa-plus"></i>
                                        <%= textCrud[SESS_LANGUAGE][1] + " " + textCrud[SESS_LANGUAGE][0]%>
                                      </span>
                                    </td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3">
                                      <%
                                        out.print(drawListInvoices(SESS_LANGUAGE, listMatDispatchBC, startItem, df.getDispatchStatus()));
                                      %>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td colspan="3" style="height: 100%;">&nbsp;</td>
                                  </tr>

                                  <%if (listMatDispatchBC != null && listMatDispatchBC.size() > 0) {%>

                                  <tr align="left" valign="top">
                                    <td height="20" width="5%" align="left" class="command">
                                      <span class="command">
                                        <%
                                          if (cmdItem != Command.FIRST && cmdItem != Command.PREV && cmdItem != Command.NEXT && cmdItem != Command.LAST) {
                                            cmdItem = Command.FIRST;
                                          }
                                          ctrLine.setLocationImg(approot + "/images");
                                          ctrLine.initDefault();
                                          ctrLine.setImageListName(approot + "/images", "item");
                                          ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "','" + oidDispatchMaterial + "')");
                                          ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "','" + oidDispatchMaterial + "')");
                                          ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "','" + oidDispatchMaterial + "')");
                                          ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "','" + oidDispatchMaterial + "')");

                                          out.print(ctrLine.drawImageListLimit(cmdItem, vectSizeItem, startItem, recordToGetItem));
                                        %> 
                                      </span> 																										
                                    </td>

                                    <td colspan="2" class="command">
                                      <div class="row">
                                        <div class="pull-right">
                                          <span id="printExcelReportBtn" class="btn btn-md btn-primary">
                                            <i class="fa fa-print"></i>
                                            <%= textCrud[SESS_LANGUAGE][5]%> <%= textCrud[SESS_LANGUAGE][7]%>

                                          </span>
                                          &nbsp;&nbsp;&nbsp;
                                          <span id="printReportBtn" class="btn btn-md btn-warning">
                                            <i class="fa fa-print"></i>
                                            <%= textCrud[SESS_LANGUAGE][5]%> <%= textCrud[SESS_LANGUAGE][6]%>
                                          </span>
                                          &nbsp;&nbsp;&nbsp;
                                        </div>
                                        <div class="pull-right">
                                          <a href="javascript:cmdSave()" id="saveDocumentBtn" class="btn btn-md btn-success">
                                            <i class="fa fa-save"></i>
                                            <%= textCrud[SESS_LANGUAGE][4]%>
                                          </a>
                                        </div>

                                      </div>
                                      </div> 																										
                                    </td>


                                  </tr>

                                  <!--														<tr align="left" valign="top">
                                  
                                                 <td height="22" valign="middle" colspan="3">
                                  
                                  <%								if (privAdd == true) {

                                  %>
                  
                                  <table width="50%" border="0" cellspacing="2" cellpadding="0">
                  
                                   <tr>
                                  <% if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                
                                  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                  <td width="15%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                  <%if (!typeOfBusiness.equals("3")) {%>
                                 <td width="6%"><a href="javascript:addItemReceive()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                 <td width="30%"><a href="javascript:addItemReceive()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Item Receive", ctrLine.CMD_ADD, true)%></a></td>
                                  <%}%>
                                  <% } %>
               
                                </tr>
               
                               </table>
               
                                  <%}%>
                  
                                 </td>
                  
                                </tr>-->

                                  <%}%>

                                </table>

                              </td>

                            </tr>

                            <!--											<tr>
                            
                                        <td colspan="2" valign="top">&nbsp;</td>
                            
                                        <td width="30%">&nbsp;</td>
                            
                                       </tr>
                            
                                       <tr>
                            
                                        <td colspan="2" valign="top">&nbsp;</td>
                            
                                        <td width="30%">&nbsp;</td>
                            
                                       </tr>-->

                            <!--											<tr>
                            
                                        <td colspan="3">
                            
                                         <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            
                                          <tr>
                            
                                           <td width="77%" rowspan="2">
                            
                            <%

                              ctrLine.setLocationImg(approot + "/images");

                              // set image alternative caption
                              ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));

                              ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");

                              ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));

                              ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));

                              ctrLine.initDefault();

                              ctrLine.setTableWidth("80%");

                              String scomDel = "javascript:cmdAsk('" + oidDispatchMaterial + "')";

                              String sconDelCom = "javascript:cmdDelete('" + oidDispatchMaterial + "')";

                              String scancel = "javascript:cmdEdit('" + oidDispatchMaterial + "')";

                              ctrLine.setCommandStyle("command");

                              ctrLine.setColCommStyle("command");

                              // set command caption
                              ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));

                              ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");

                              ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));

                              ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_DELETE, true));

                              ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));

                              if (privDelete) {

                                ctrLine.setConfirmDelCommand(sconDelCom);

                                ctrLine.setDeleteCommand(scomDel);

                                ctrLine.setEditCommand(scancel);

                              } else {

                                ctrLine.setConfirmDelCaption("");

                                ctrLine.setDeleteCaption("");

                                ctrLine.setEditCaption("");

                              }

                              if ((privAdd == false && privUpdate == false) || df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED || df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {

                                ctrLine.setSaveCaption("");

                              }

                              if (privAdd == false || df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {

                                ctrLine.setAddCaption("");

                              }

                              if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {

                                ctrLine.setDeleteCaption("");

                              }

                              if (iCommand == Command.SAVE && frmdf.errorSize() == 0) {

                                // iCommand=Command.EDIT;
                              }

                            %>
            
                            <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%> </td>
                            <% if (privShowQtyPrice) {%>
                           <td>
            
                            <select name="type_print_tranfer">
            
                             <option value="0">Price Cost</option>
            
                             <option value="1">Sell Price</option>
            
                             <option value="2">No Price</option>
            
                            </select>
            
                           </td>
                            <%} else {%>
                           <input type="hidden" name="type_print_tranfer" value="2">
                            <%}%>
                            </tr>
              
                            <tr>
              
                             <td width="23%">
              
                            <%if (listMatDispatchBC != null && listMatDispatchBC.size() > 0) {%>
            
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
            
                             <tr>
            
                            <% if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                            <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][3]%>"></a></td>
                            <td width="45%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" ><%=textListGlobal[SESS_LANGUAGE][3]%></a></td>
                            <%} else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {%>
                           <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][3]%>"></a></td>
                           <td width="45%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][3]%> </a></td>
                           <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a></td>
                           <td width="45%" nowrap>&nbsp; <a href="javascript:PostingStock('<%=oidDispatchMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][6]%> </a></td>
                            <%} else {%>
                            <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][3]%>"></a></td>
                            <td width="45%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][3]%> </a></td>
                            <%if (privShowQtyPrice && typeOfBusiness == "3") {%>
                            <td width="5%" valign="top"><a href="javascript:PostingCostPrice('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td>
                            <td width="45%" nowrap>&nbsp; <a href="javascript:PostingCostPrice('<%=oidDispatchMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][7]%> </a></td>
                            <%}%>
                            <%}%>
                           </tr>
          
                          </table>
          
                            <%}%>                        </td>
            
                          </tr>
            
                         </table>
            
                        </td>
            
                       </tr>-->

                            <tr>
                              <td colspan="100%" height="50">
                                <a href="javascript:cmdBack()" class="btn btn-primary">
                                  <i class="fa fa-arrow-left"></i>
                                  <span>
                                    <%= textCrud[SESS_LANGUAGE][8]%>										
                                  </span>
                                </a>
                              </td>
                            </tr>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td> <centre> <a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></centre></td>
                            </tr>

                          </table>

                          <%

                            } catch (Exception e) {

                              System.out.println(e);

                            }

                          %>

                        </form>

                      </td>

                    </tr>

                  </table>

                </td>

              </tr>

              <tr>

					<td colspan="2" height="20">
						<%if (menuUsed == MENU_ICON) {%>
						<%@include file="../../../styletemplate/footer.jsp" %>
						<%} else {%>
						<%@ include file = "../../../main/footer.jsp" %>
						<%}%>

					</td>


				<script language="JavaScript">
					<%

						// add by fitra 10-5-2014s
						if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE) {%>
					addItem();
					<% }%>
					
				</script>

			</tr>

		</table>
     </div>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>	
		
		<script type="text/javascript">
			$(function(){
				console.log("Dokumen is redi bois");
				var lastSelectedStatus = $('#<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS] %>').val();
				console.log(lastSelectedStatus);
				if(lastSelectedStatus == <%= I_DocStatus.DOCUMENT_STATUS_DRAFT %>){
					$('#saveDocumentBtn').hide();
				}
				$('#saveButton').click(function(){
					console.log("terklik beibeehhh");
					var cmd = "<%= Command.FIRST %>";
					var dispatchOid = "<%= oidDispatchMaterial %>";
					var billMainOid = "<%= oidCashBillMain %>";
					var nomorBc = $('#<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]%>_').val();
					var baseUrl = "dfbcdosearch.jsp";
					
					var url = baseUrl + "?command="+cmd
							+"&hidden_dispatch_id="+dispatchOid
							+"&hidden_bill_main_id="+billMainOid
							+"&nomorbc="+nomorBc
							+"&approval_command=<%= appCommand %>";
					
					var openWindow = window.open(url, "Invoice Search", "height=660,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
					if(window.focus){
						openWindow.focus();
					}
				});
				
				$('#<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS] %>').click(function(){
					lastSelectedStatus = $(this).val();
					var final = "<%= I_DocStatus.DOCUMENT_STATUS_FINAL %>";
					console.log(lastSelectedStatus);
					console.log(final);
					if(lastSelectedStatus == final){
						$('#saveDocumentBtn').show();
					} else {
						$('#saveDocumentBtn').hide();
					}
				});
				
				$('.delete-btn').click(function(){
					var textWarning = "<%= textDelete[SESS_LANGUAGE][0] %>";
					var textCancel = "<%= textDelete[SESS_LANGUAGE][1] %>";
					var confirms = confirm(textWarning);
					
					if(confirms == true){
						var oid = $(this).val();
						
						$.ajax({
							type: 'POST',
							url: "<%= approot %>/AjaxMatDispatchBill?command=<%= Command.DELETE %>&oid="+oid,
							cache: false,
							dataType: 'JSON',
							success: function (data) {
								alert(data.oid);
								self.window.location.reload();
							}
						});
					} else {
						alert(textCancel);
					}
					
				});
				
				$('#printExcelReportBtn').hide();
				$('#printReportBtn').click(function(){
					console.log("Print ke PDF BOIS");
					window.open("<%=approot%>/TransferBCPrintPDF?approot=<%= approot %>&sess_language=<%= SESS_LANGUAGE %>&dispatch_id=<%= oidDispatchMaterial %>");
				});
				$('#printExcelReportBtn').click(function(){
					console.log("Print ke PDF BOIS");
					window.open("<%=approot%>/TransferBCPrintPDF?approot=<%= approot %>&sess_language=<%= SESS_LANGUAGE %>&dispatch_id=<%= oidDispatchMaterial %>");
				});
				
				if(<%= df.getDispatchStatus() %> == <%= I_DocStatus.DOCUMENT_STATUS_CLOSED %>){
					$('#topTableBtnSection').hide();
					$('#saveDocumentBtn').hide();
				}
			});									

		</script>
	</body>

</html>



