<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatch"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatchBill"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.pos.session.billing.SessBilling"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatCostingItem,
                 com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.warehouse.MatDispatchItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.warehouse.PstMatDispatchItem,
                 com.dimata.posbo.form.warehouse.FrmMatDispatchItem,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportStock"%>
<%@page contentType="text/html"%>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
    {"Pencarian Invoice","Tidak ada data invoice", "Simpan", "Cari", "Berhasil", "Hasil pencarian dengan Invoice Number yang mengandung: "},
    {"Invoice Search","No invoice data available", "Save", "Search", "Success", "Search result using Invoice Number contain: "}
};

public static final String textMaterialForm[][] =
{
    {"Nomor Invoice"},
    {"Invoice Number"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
    {"Kategori","Sku","Nama Barang"},
    {"Category","Material Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
    {"No","Invoice","Pilih"},
    {"No","Invoice","Select"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
    long oidDispatch = 0;
    if(vectDf!=null && vectDf.size()>0){
        for(int k=0;k<vectDf.size();k++){
            MatDispatchItem matDf = (MatDispatchItem)vectDf.get(k);
            if(matDf.getMaterialId()==oidmaterial){
                oidDispatch = matDf.getOID();
                break;
            }
        }
    }
    return oidDispatch;
}

	public static int getStrDutyFree(){
		String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
		System.out.println("#Duty Free: " + strDutyFree);
		int dutyFree = Integer.parseInt(strDutyFree);
		return dutyFree;
	}

%>

<%!
    public String drawListMaterial(int language, Vector objectClass, int start) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {

            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

			
            ctrlist.addHeader(textListMaterialHeader[language][0], "5%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "25%");
			String select_all = "<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">"
								+ "<tr>"
								+ "<td style=\"border:0;\"><input type=\"checkbox\" id=\"select-all_\"></td>"
								+ "<td style=\"border:0; color:white;\">"+textListMaterialHeader[language][2]+"</td>"
								+ "</tr>"
								+ "</table>"; 
								

            ctrlist.addHeader(select_all, "10%");

            ctrlist.setLinkRow(2);
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

            for (int i = 0; i < objectClass.size(); i++) {
                BillMain bm = (BillMain) objectClass.get(i);
				Vector rowx = new Vector();
				
				//nomor
				start += 1;
				rowx.add("<div align=\"center\">" + start + "</div>");

				//invoice
				rowx.add("<div class=\"text-center\">"+bm.getInvoiceNumber()+"</div>");

				//pilih (checkbox)
				String checkbox = "<div class=\"text-center\">"
							+ "<input class=\"select-box\" type=\"checkbox\" id=\"choosen_\" "
							+ "name=\""+FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_CASH_BILL_MAIN_ID]
							+ "\" value="+ bm.getOID() +">"
							+ "</div>";
				rowx.add(checkbox);
                lstData.add(rowx);
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][1] + "</div>";
        }
        return result;
    }

    public String drawListMaterialAuto(int language, Vector objectClass, Vector vectDf, int start, int allowZero) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {

            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "5%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "25%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "10%");

            ctrlist.setLinkRow(2);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEditAuto('");
            if (objectClass.size() == 1){
                ctrlist.setLinkSufix("')\" class=\"autoClick");
            } else {
                ctrlist.setLinkSufix("')");
            }
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector list = (Vector) objectClass.get(i);
                
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][1] + "</div>";
        }
        return result;
    }
%>

<!-- JSP Block -->
<%!
/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */
static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");
static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;

%>

<%
	int start = FRMQueryString.requestInt(request, "start");
	int iCommand = FRMQueryString.requestCommand(request);
	long oid = FRMQueryString.requestLong(request, "oid");
	long oidDispatchMat = FRMQueryString.requestLong(request, "hidden_dispatch_id");
	int appCommand = FRMQueryString.requestInt(request, "approval_command");
	String invNum = FRMQueryString.requestString(request, "invoiceNum");
	String nomorbc = FRMQueryString.requestString(request, "nomorbc");
	
	boolean search = false;
	int recordToGet = 15;
	
	String whereClause = "";
	if(invNum.length() > 0 && invNum != null && !invNum.equals("")){
		whereClause = "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + invNum + "%'";
		search = true;
	} else {
		search = false;
	}
	Vector vect = PstBillMain.getPaidInvoice(start, recordToGet, whereClause, oidDispatchMat);
	int vectSize = PstBillMain.getCountPaidInvoice(whereClause, oidDispatchMat);

	//int vectSize = PstMaterialStock.getCountDaftarBarangStock(locationId,periodeId,materialgroup,materialcode,materialname);
	/**
	 * get size stock list and stock value
	 */
	//Vector vctStockValue = SessReportStock.getStockValue(srcReportStock, calculateQtyNull);
	CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
	if (start < 0) {
		start = 0;
	}
	if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
		start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
	}
	if (start < 0) {
		start = 0;
	}



%>

<script language="JavaScript">
    self.opener.document.forms.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.command.value = "<%= iCommand%>";
    self.opener.document.forms.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.start.value = "<%= start%>";
    self.close();
</script>

<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
var mapForm = document.getElementsByName("<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>");
function cmdSaveInvoice(){
	var mapForm = document.getElementsByName("<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>");
	var data = serialize(mapForm);
	console.log(mapForm);
//	kurang ajax untuk menambahkan data pilihan user ke db
	mapForm.action="<%= approot %>/AjaxMatDispatchBill";
	mapForm.submit();
//	lalu refresh frame transfer agar data tersebut tampil
	self.opener.document.getElementById('frame-menu').contentWindow.location.reload();
	alert("Saved");
	self.close();
}

function cmdListFirst()
{
    document.FRM_NAME_MATDISPATCHBILL.command.value="<%=Command.FIRST%>";
	document.FRM_NAME_MATDISPATCHBILL.hidden_dispatch_id.value = "<%= oidDispatchMat %>";
	document.FRM_NAME_MATDISPATCHBILL.oid.value = "<%= oid %>";
    document.FRM_NAME_MATDISPATCHBILL.action="dfbcdosearch.jsp";
    document.FRM_NAME_MATDISPATCHBILL.submit();
}

function cmdListPrev(){
    document.FRM_NAME_MATDISPATCHBILL.command.value="<%=Command.PREV%>";
	document.FRM_NAME_MATDISPATCHBILL.hidden_dispatch_id.value = "<%= oidDispatchMat %>";
	document.FRM_NAME_MATDISPATCHBILL.oid.value = "<%= oid %>";
    document.FRM_NAME_MATDISPATCHBILL.action="dfbcdosearch.jsp";
    document.FRM_NAME_MATDISPATCHBILL.submit();
}

function cmdListNext(){
    document.FRM_NAME_MATDISPATCHBILL.command.value="<%=Command.NEXT%>";
	document.FRM_NAME_MATDISPATCHBILL.hidden_dispatch_id.value = "<%= oidDispatchMat %>";
	document.FRM_NAME_MATDISPATCHBILL.oid.value = "<%= oid %>";
    document.FRM_NAME_MATDISPATCHBILL.action="dfbcdosearch.jsp";
    document.FRM_NAME_MATDISPATCHBILL.submit();
}

function cmdListLast(){
    document.FRM_NAME_MATDISPATCHBILL.command.value="<%=Command.LAST%>";
	document.FRM_NAME_MATDISPATCHBILL.hidden_dispatch_id.value = "<%= oidDispatchMat %>";
	document.FRM_NAME_MATDISPATCHBILL.oid.value = "<%= oid %>";
    document.FRM_NAME_MATDISPATCHBILL.action="dfbcdosearch.jsp";
    document.FRM_NAME_MATDISPATCHBILL.submit();
}	

function cmdSearch(){
    document.FRM_NAME_MATDISPATCHBILL.command.value="<%=Command.LIST%>";
    document.FRM_NAME_MATDISPATCHBILL.start.value = '0';
    document.FRM_NAME_MATDISPATCHBILL.action="dfbcdosearch.jsp";
    document.FRM_NAME_MATDISPATCHBILL.submit();
}	

function clear(){
    document.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.txt_materialcode.value="";
}
function fnTrapKD(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSearch();
    }
}

function keyDownCheck(e){
    //activeTable();
   var trap = document.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.trap.value;

   if (e.keyCode == 13 && trap==0) {
    document.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.trap.value="1";
   }
   if (e.keyCode == 13 && trap==1) {
       document.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.trap.value="0";
       cmdSearch();
       
   }
   if (e.keyCode == 27) {
       //alert("sa");
       document.<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>.txt_materialname.value="";
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">




<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> <%@include file="../../../styletemplate/template_header_empty.jsp" %>
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%> </td>
        </tr>
        <tr> 
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
			  <form id="FRM_NAME_MATDISPATCHBILL" name="FRM_NAME_MATDISPATCHBILL">
              <input type="hidden" id="start_" name="start" value="<%=start%>">
              <input type="hidden" id="command_" name="command" value="<%= iCommand %>">
              <input type="hidden" id="oid_" name="oid" value="<%= oid %>">
			  <input type="hidden" id="hidden_dispatch_id_" name="hidden_dispatch_id" value="<%= oidDispatchMat %>"> 
			  <input type="hidden" id="<%= FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_STATUS] %>_" 
					 name="<%= FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_STATUS] %>" 
					 id="<%= FrmMatDispatchBill.fieldNames[FrmMatDispatchBill.FRM_FIELD_STATUS] %>" value="0">
              
               <%--autocomplate--%>
              <input type="hidden" name="trap" value="">
			  
              <table width="100%" border="0" cellspacing="1" cellpadding="1"> 
			  <tr>
				  <td colspan="2">
					  <table width="100%">
						  <tr>
							  <td width="15%">
								  <%= textMaterialForm[SESS_LANGUAGE][0]%>
							  </td>
							  <td>
								  <span> : </span>
							  </td>
							  <td>
								  <input type="text" id="invoiceNum_" name="invoiceNum">
							  </td>
						  </tr>
						  <tr>
							  <td>&nbsp;</td>
							  <td>&nbsp;</td>
							  <td>&nbsp;</td>
						  </tr>
						  <tr>
							  <td colspan="3" align="center">
								  <a href="javascript:cmdSearch()" class="btn btn-md btn-primary" style="padding-left: 50px; padding-right: 50px; padding-top: 10px; padding-bottom: 10px;">
									  <%= textListGlobal[SESS_LANGUAGE][3] %>
								  </a>
							  </td>
						  </tr>
						  <tr>
							  <% if (search) { %>
								  <td colspan="3">
									  <br>
									  <span><%= textListGlobal[SESS_LANGUAGE][5] %> </span>
									  <span style="font-weight: bold;"><%= invNum %></span>
									  <br>
								  </td>
							  <% } else { %>
								  <td>&nbsp;</td>
								  <td>&nbsp;</td>
								  <td>&nbsp;</td>
							  <% } %>
						  </tr>
					  </table>
				  </td>
			  </tr>
			  <tr> 
                  <td colspan="2">
                      <%=drawListMaterial(SESS_LANGUAGE,vect,start)%>
                  </td>
                </tr>
				
                <tr> 
                  <td>
					  <span class="command"> 
						<% 
							ControlLine ctrlLine= new ControlLine();
							ctrlLine.setLocationImg(approot+"/images");
							ctrlLine.initDefault();
							out.print(ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet));
						%> 
					  </span>
				  </td>
				  <td height="50">
					<div style="text-align: end;">
						<span id="saveInvoiceBtn" class="btn btn-primary" style="padding-left: 25px; padding-right: 25px; padding-top: 10px; padding-bottom: 10px;">
							<%= textListGlobal[SESS_LANGUAGE][2] %>
						</span>
					</div>
					</td>
                </tr>
              </table>
            </form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%--autocomplate--%>
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>	
<script src="../../../styles/jquery.autocomplete.js"></script>						
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
	$(function(){
		console.log("Document is Ready broo!!");
		$('#saveInvoiceBtn').click(function(){
			var data = $('#<%= FrmMatDispatchBill.FRM_NAME_MATDISPATCHBILL %>');
			var dispatchOid = $('#hidden_dispatch_id_').val();
			var oid = $('#oid').val();
			var nomorbc = "<%= nomorbc %>";
			var cmd = "<%= Command.EDIT %>"
			console.log(data.serialize());
			$.ajax({
				type:'GET',
				url:"<%= approot %>/AjaxMatDispatchBill?command="+<%=Command.SAVE%>+"&oid="+oid+"&hidden_dispatch_id="+dispatchOid+"&type=insertInvoice",
				data: data.serialize(),
				cache: false,
				dataType: 'JSON',
				success: function (data) {
					alert("<%= textListGlobal[SESS_LANGUAGE][2] %> <%= textListGlobal[SESS_LANGUAGE][4] %>");
					window.opener.location.href = "df_bc_wh_material_edit.jsp?command="+cmd
							+"&hidden_dispatch_id="+dispatchOid
							+"&<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_NOMOR_BC]%>="+nomorbc
							+"&approval_command=<%= appCommand %>";
					self.close();
                }
			}).done(function(){});
		});

		$('#select-all_').change(function(){
			$('.listgensell input:checkbox').prop('checked', this.checked);
		});

	});
</script>
</body>
</html>
