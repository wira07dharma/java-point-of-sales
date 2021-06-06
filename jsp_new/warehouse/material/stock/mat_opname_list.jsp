<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.MatStockOpname,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.posbo.entity.search.SrcMatStockOpname,
                 com.dimata.posbo.form.search.FrmSrcMatStockOpname,
                 com.dimata.util.Command,
                 com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname"},
	{"Stock","Opname","Search","List","Edit","No opname data available"}
};

public static final String textListHeader[][] = {
	{"No","Nomor","Tanggal","Lokasi","Status","Keterangan","Status Item"},
	{"No","Number","Date","Location","Status","Remark","Status Item"}
};

public String drawList(Vector objectClass, int start, int language, int docType, I_DocStatus i_status, int iStatusItem) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {	
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListHeader[language][0],"3%");
		ctrlist.addHeader(textListHeader[language][1],"15%");
		ctrlist.addHeader(textListHeader[language][2],"10%");
		ctrlist.addHeader(textListHeader[language][3],"25%");
		ctrlist.addHeader(textListHeader[language][4],"7%");
                ctrlist.addHeader(textListHeader[language][6],"7%");
		ctrlist.addHeader(textListHeader[language][5],"40%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start<0)
			start = 0;
	
		for (int i = 0; i < objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			Vector rowx = new Vector();
			start = start + 1;			
			MatStockOpname matStockOpname = (MatStockOpname)vt.get(0);
			Location location = (Location)vt.get(1);
			
                        int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                        int getCountSameMaterial = SessMatStockOpname.getCountSameMaterialList(matStockOpname.getOID());

                        rowx.add(""+start);
			String str_dt_OpnameDate = ""; 
			try	{
				Date dt_OpnameDate = matStockOpname.getStockOpnameDate();
				if(dt_OpnameDate==null)	{
					dt_OpnameDate = new Date();
				}
				str_dt_OpnameDate = Formater.formatDate(dt_OpnameDate, "dd-MM-yyyy");
			}
			catch(Exception e) { 
				str_dt_OpnameDate = ""; 
			}
			
			rowx.add(matStockOpname.getStockOpnameNumber());
			rowx.add(str_dt_OpnameDate);
			rowx.add(location.getName());
			rowx.add(i_status.getDocStatusName(docType,matStockOpname.getStockOpnameStatus()));

                        //status opname item
                        //int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                        if(getCounter > 1 || getCountSameMaterial > 1 ){
                            rowx.add("Double");
                        } else {
                            rowx.add("Clean");
                        }
                        
			rowx.add(matStockOpname.getRemark());

                       
			 lstData.add(rowx);
                        
			lstLinkData.add(String.valueOf(matStockOpname.getOID()));
                     }
                      
		result = ctrlist.draw();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";
	}
	return result;	
}
%>

<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_OPN);


boolean privManageData = true;%>

<%
String opnameCode = i_pstDocType.getDocCode(docType);
String opnameTitle = "Opname"; //i_pstDocType.getDocTitle(docType);

//added by dewok 2018 for emas lantakan
int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");

ControlLine ctrLine = new ControlLine();
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
long oidMatStockOpname = FRMQueryString.requestLong(request, "hidden_mat_stock_opname_id");
int iStatusItem = FRMQueryString.requestInt(request, "status_item");


int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";

SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname(request, srcMatStockOpname);
frmSrcMatStockOpname.requestEntityObject(srcMatStockOpname);

 //vector statusr
      Vector vectSt = new Vector(1,1);
       String[] strStatus = request.getParameterValues(FrmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]);
       if(strStatus!=null && strStatus.length>0) {
         for(int i=0; i<strStatus.length; i++) {
	   try {
                    vectSt.add(strStatus[i]);
                }
            catch(Exception exc) {
                    System.out.println("err");
                    }
                 }
            }
       srcMatStockOpname.setDocStatus(vectSt);
    //end of vector status

if((iCommand==Command.BACK)||(iCommand==Command.NEXT)||(iCommand==Command.FIRST)||(iCommand==Command.PREV)||(iCommand==Command.LAST)){
 try{ 
	if(session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME)!=null){
		srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME); 
	}
 }catch(Exception e){ 
		srcMatStockOpname = new SrcMatStockOpname();
 }
}

SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();
session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatStockOpname);


// proses get data from session ' if ready from barcode generate
try{
    Vector listreq = (Vector)session.getValue("BARCODE_RECEIVE");
    if(listreq!=null && listreq.size()>0){
	    String strcode = "";
	    for(int k=0;k<listreq.size();k++){
		MatStockOpname matStockOpname = (MatStockOpname)listreq.get(k);    
		if(strcode.length()>0){
			strcode = strcode + ","+matStockOpname.getStockOpnameNumber();
		    }else{
			strcode = matStockOpname.getStockOpnameNumber();
		    }
		}
	    srcMatStockOpname.setOpnameNumber(strcode);
	}
    }catch(Exception e){}

vectSize = sessMatStockOpname.getCountSearch(srcMatStockOpname);
ctrlMatStockOpname.action(iCommand , oidMatStockOpname);
if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)){
	start = ctrlMatStockOpname.actionList(iCommand, start, vectSize, recordToGet);
}

Vector records = sessMatStockOpname.searchMatStockOpname(srcMatStockOpname, start, recordToGet);
int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));
try{
	session.removeValue("SESSION_OPNAME_SELECTED_ITEM");
}catch(Exception e){}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
function cmdAdd(){
	document.frm_matstockopname.command.value="<%=Command.ADD%>";
	document.frm_matstockopname.approval_command.value="<%=Command.SAVE%>";
        <%if (prochainMenuBootstrap==0){%>
            document.frm_matstockopname.action="mat_opname_edit_new.jsp";
        <%}else{%>
            document.frm_matstockopname.action="mat_opname_edit.jsp";
        <%}%>
	if(compareDateForAdd()==true)
		document.frm_matstockopname.submit();
}

function cmdEdit(oid){
	document.frm_matstockopname.command.value="<%=Command.EDIT%>";
	document.frm_matstockopname.hidden_opname_id.value = oid;
	document.frm_matstockopname.approval_command.value="<%=Command.APPROVE%>";
        <%if (prochainMenuBootstrap==0){%>
            document.frm_matstockopname.action="mat_opname_edit_new.jsp";
        <%}else{%>
            document.frmsrcmatstockopname.action="mat_opname_edit.jsp";
        <%}%>
	document.frm_matstockopname.submit();
}

function cmdListFirst(){
	document.frm_matstockopname.command.value="<%=Command.FIRST%>";
	document.frm_matstockopname.action="mat_opname_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListPrev(){
	document.frm_matstockopname.command.value="<%=Command.PREV%>";
	document.frm_matstockopname.action="mat_opname_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListNext(){
	document.frm_matstockopname.command.value="<%=Command.NEXT%>";
	document.frm_matstockopname.action="mat_opname_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListLast(){
	document.frm_matstockopname.command.value="<%=Command.LAST%>";
	document.frm_matstockopname.action="mat_opname_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdBack(){
	document.frm_matstockopname.command.value="<%=Command.BACK%>";
	document.frm_matstockopname.action="mat_opname_src.jsp";
	document.frm_matstockopname.submit();
}

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
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
		  	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%>
                        <%
                            if(typeOfBusinessDetail == 2) {
                                if (lantakan == 1) {
                                    out.print(" : Emas Lantakan");
                                }else{
                                    out.print(" : Emas & Berlian");
                                }
                            }
                        %>
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_matstockopname" method="post" action="">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_opname_id" value="<%=oidMatStockOpname%>">
              <input type="hidden" name="status_item" value="<%=iStatusItem%>">
              <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">
              
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr> 
                  <td> 
                    <hr size="1" noshade>
                  </td>
                </tr>
				<tr> 
                  <td><%=drawList(records,start,SESS_LANGUAGE,docType,i_status,iStatusItem)%></td>
                </tr>
				<% 
				ctrLine.setLocationImg(approot+"/images");
				ctrLine.initDefault();
				String strList = ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet);
				if(strList.length()>0){
				%>				
                <tr> 
                  <td><%=strList%></td>
                </tr>
				<%}%>
                <tr> 				
                  <td> 
                    <table width="52%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <%//if(privAdd && privManageData){%>
                        <!--td nowrap width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                        <td class="command" nowrap width="48%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_ADD,true)%></a></td>
                        <%//}%>
                        <!--td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                        <td class="command" nowrap width="40%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                      </tr>
                    </table>
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
