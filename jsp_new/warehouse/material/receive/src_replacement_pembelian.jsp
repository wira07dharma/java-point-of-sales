
<%@page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%> 
<%@page import="com.dimata.posbo.form.warehouse.CtrlMaterialStockCode"%> 
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>

<%@page import="com.dimata.posbo.form.warehouse.FrmMaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmDispatchStockCode"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatReceive"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatReceive"%>
<%@ page language = "java" %>

<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import="com.dimata.posbo.entity.search.SrcStockCard,
                 com.dimata.posbo.form.search.FrmSrcStockCard,
                 com.dimata.posbo.session.warehouse.SessStockCard,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;
public static final int ADD_TYPE_EDIT = 2; 
public static final int ADD_TYPE_SAVE = 3; 



public static final String textListGlobal[][] = {
	{"Penerimaan","Dari Replacement","Kartu Stok","Pencarian","Tidak ada data","Cetak Kartu Stok","Harus Diisi","Ganti"},
	{"Stock","Report","Stock Card","Searching","No stock available","Print Stock Card","Entry Required"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Serial Number","Kode/Nama Barang","Kategori","Periode","Urut Berdasar","Sub Kategori","Supplier","Status Dokumen","Masukan serial number yang akan di ganti","No"},
	{"Location","Code/Goods Name","Category","Period","Sort By","Sub Category","Supplier","Document Status"}
};


public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
	String result = "";
	if(addBody) {
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else {
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else {
		result = textListHeader[language][index];
	} 
	return result;
}

public boolean getTruedFalse(Vector vect, int index) {
    for(int i=0;i<vect.size();i++) {
        int iStatus = Integer.parseInt((String)vect.get(i));
        if(iStatus==index)
            return true;
    }
    return false;
}

public String drawListMaterial2(int language,long oidMaterialStockCode, Vector objectClasss,int start)
{
	String result = "";
	if(objectClasss!=null && objectClasss.size()>0){ 
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader(textListHeader[language][9],"20%");
                ctrlist.addHeader(textListHeader[language][1],"20%");
                ctrlist.addHeader(textListHeader[language][0],"60%");
		

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		if(start<0) start = 0;

		for(int i=0; i<objectClasss.size(); i++){
			MaterialStockCode materialStockCode  = (MaterialStockCode)objectClasss.get(i);
                        Material material = new Material();
                        try{
                            material = PstMaterial.fetchExc(materialStockCode.getMaterialId());
                        }catch(Exception ex){
                            material = new Material();
                        }
                        start = start + 1;
			Vector rowx = new Vector();
			rowx.add(""+start);
                        rowx.add(""+material.getName());
			rowx.add("<div align=\"center\">"+materialStockCode.getStockCode()+"</div>"); 
			lstData.add(rowx);
			lstLinkData.add(""+materialStockCode.getOID()+"','"+materialStockCode.getStockCode());
		}
		return ctrlist.draw();
	}
	else
	{
		result = "";
	}
	return result;
}












public String drawListMaterial(int language,long oidMaterialStockCode, Vector objectClass,int start)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0){ 
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader(textListHeader[language][9],"20%");
                 ctrlist.addHeader(textListHeader[language][1],"20%");
                ctrlist.addHeader(textListHeader[language][0],"60%");
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		if(start<0) start = 0;

		for(int i=0; i<objectClass.size(); i++){
			MaterialStockCode materialStockCode  = (MaterialStockCode)objectClass.get(i);
                        Material material = new Material();
                        try{
                            material = PstMaterial.fetchExc(materialStockCode.getMaterialId());
                        }catch(Exception ex){
                            material = new Material();
                        }
			start = start + 1;
			Vector rowx = new Vector();
			rowx.add(""+start);
                        rowx.add(""+material.getName());
			rowx.add("<div align=\"center\">"+materialStockCode.getStockCode()+"</div>"); 
			lstData.add(rowx);
			lstLinkData.add(""+materialStockCode.getOID()+"','"+materialStockCode.getStockCode());
		}
		return ctrlist.draw();
	}
	else
	{
		result = "";
	}
	return result;
}
%>

<!-- Jsp Block -->
<%I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);

/**
* get data from 'hidden form'
*/

CtrlMaterialStockCode ctrlMaterialStockCode = new CtrlMaterialStockCode(request);  
int start = FRMQueryString.requestInt(request, "start"); 
int iCommand = FRMQueryString.requestCommand(request);
int index = FRMQueryString.requestInt(request,"type");
String serialCode = FRMQueryString.requestString(request, "serialNumber");
String serialCodeInput = FRMQueryString.requestString(request,FrmMaterialStockCode.fieldNames[FrmMaterialStockCode.FRM_FIELD_STOCK_CODE]);
long oidMaterialStockCode = FRMQueryString.requestLong(request, "hidden_material_stock_code_Id");
long oidMaterialStockCodeItem = FRMQueryString.requestLong(request,"hidden_material_stock_code_id_Item");
int iErrCode = FRMMessage.NONE; 
FrmMaterialStockCode frmmaterialstockcode = ctrlMaterialStockCode.getForm(); 
//Vector listMaterialStockCode = new Vector(1, 1);
MaterialStockCode  materialStockCode = ctrlMaterialStockCode.getMaterialStockCode();
iErrCode = ctrlMaterialStockCode.action(iCommand, oidMaterialStockCode); 
//iErrCode = ctrlMaterialStockCode.action(Command.EDIT, oidMaterialStockCodeItem);  
String whereClause = "";
int vectSize = PstMaterialStockCode.getCount(whereClause);
String msgString = "";
msgString = ctrlMaterialStockCode.getMessage(); 
int recordToGet = 10;

String orderClause = "" + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE] + " ASC ";

  ControlList ctrlist = new ControlList();
ControlLine ctrLine = new ControlLine();


FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(); 






if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidMaterialStockCode == 0)) {
    start = PstMaterialStockCode.findLimitStart(materialStockCode.getOID(), recordToGet, whereClause); 
}

//listMaterialStockCode= PstMaterialStockCode.list(start, recordToGet, whereClause, orderClause);
     
//update by fitra
Vector list = new Vector();
String where = ""; 
if(iCommand==Command.LIST || iCommand==Command.EDIT){
    where = (serialCode!=null && serialCode.length()>0?PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE]+ " = '"+serialCode+"' AND ":"")
            +PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
    list = PstMaterialStockCode.list(0,0,where,PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE]);
}

if ((iCommand == Command.SAVE) && (iErrCode == 0) && (oidMaterialStockCode == 0)) {
    start = PstMaterialStockCode.findLimitStart(materialStockCode.getOID(), recordToGet, whereClause); 
}    

/*if (listMaterialStockCode.size() < 1 && start > 0) {
    if (vectSize - recordToGet > recordToGet) {
        start = start - recordToGet;   //go to Command.PREV
    } else {

        start = 0;

        iCommand = Command.FIRST;

    }
    listMaterialStockCode= PstMaterialStockCode.list(start, recordToGet, whereClause, orderClause);  
       }*/

if(iCommand==Command.SAVE && iErrCode == 0) {
	iCommand = Command.ADD; 
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

 function cmdAdd(){
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.command.value="<%=Command.ADD%>";
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.action="src_replacement_pembelian.jsp";
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.submit();
            }


 function cmdSave(){
    
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.command.value="<%=Command.SAVE%>"; 
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.action="src_replacement_pembelian.jsp";
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.submit();
            }


 function cmdEdit(oid, value){
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.command.value="<%=Command.EDIT%>"; 
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.hidden_material_stock_code_Id.value=oid,value;
                
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.action="src_replacement_pembelian.jsp";
                document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.submit();
            }
            
 function cmdSearch() {
		document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.command.value="<%=Command.LIST%>";
		document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.action="src_replacement_pembelian.jsp";
		document.<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>.submit();
	
}           
            
function cekbrg(){
    window.open("matstockcardsearch.jsp?mat_code='"+document.frmmaterialstockcode.txtkode.value+"'"+"&txt_materialname='"+document.frmmaterialstockcode.txtnama.value+"'","srhmatcard", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
  <table width="100%"  border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
        <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
          <%@ include file = "../../../main/header.jsp" %>
          <!-- #EndEditable --></td>
        </tr>
        <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
          <%@ include file = "../../../main/mnmain.jsp" %>
          <!-- #EndEditable --> </td>
        </tr>
        <%}else{%>
        <tr bgcolor="#FFFFFF">
        <td height="10" ID="MAINMENU">
          <%@include file="../../../styletemplate/template_header.jsp" %>
        </td>
        </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>" method="post" action="">
                <input type="hidden" name="start" value="<%=start%>">
                
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="type" value="<%=index%>">
              <input type="hidden" name="hidden_material_stock_code_Id" value="<%=oidMaterialStockCode%>">
               <!--<input type="hidden" name="hidden_material_stock_code_id_Item" value="<%=oidMaterialStockCodeItem%>"> -->
               <input type="hidden" name="<%=FrmMaterialStockCode.FRM_NAME_MATERIAL_STOCK_CODE%>" value="">
             
              <table width="100%" border="0">
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21" colspan="3"><hr size="1"></td>
                      </tr>
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(0,SESS_LANGUAGE,"",false)%></td> 
                        <td height="21" width="1%">:</td>
                        <td height="58" width="87%">&nbsp; 
                            <input tabindex="1" type="text" name="serialNumber"  value="<%=serialCode%>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD()">
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="15%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="84%" valign="top" align="left">
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="10%" nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][2],ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td width="90%" nowrap class="command"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][2],ctrLine.CMD_SEARCH,true)%></a></td>
                            </tr>
                            
                            
                            
                          </table>
                        </td>
                      </tr>
                      <tr>
                          <%if(list!=null && list.size()>0){%>
                            <td colspan="2"></td>
                            <td><%=drawListMaterial(SESS_LANGUAGE,oidMaterialStockCode, list, start)%></td>
                          <%}else{%>
                         <td>
                            record not found
                        </td>
                         <%}%>
                     </tr>
                  <%if(iCommand  == Command.EDIT ){%>        
                       <tr>
                            <td colspan="2">Masukkan Serial Number yang akan diganti</td>
                            <td><input type="text" name="<%=FrmMaterialStockCode.fieldNames[FrmMaterialStockCode.FRM_FIELD_STOCK_CODE]%>" value="<%=serialCodeInput%>" size="20"></td>
                       </tr>
                       <tr>
                            <td colspan="2"></td>
                            <td><a href="javascript:cmdSave()">Replace</a>|| <a href="javascript:cmdReset()">Reset</a></td>
                        </tr>
                    </table>
                  <%}%>   
              </table>       
        </form>     
            <!-- #EndEditable --></td> 
        </tr> 
      </table>               
    </td>
  </tr>
    
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
         <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
   
</table>
</body>
<!-- #EndTemplate --></html>
