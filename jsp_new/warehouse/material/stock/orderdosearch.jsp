
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.entity.masterdata.Material" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package garment -->

<%@ page import = "com.dimata.system.entity.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- JSP Block -->
<%
long categoryId = FRMQueryString.requestLong(request, "category_id");
String barCode = FRMQueryString.requestString(request, "bar_code");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20; //RECORD_TO_GET_LIST_ITEM;

/*---- GET SEARCH START SESSION -----------*/
Vector vtProduct = new Vector();
String whereClause = PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" LIKE '%"+barCode+"%'";
whereClause += " AND "+PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS]+"!="+PstMaterial.DELETE;
if(categoryId != 0) {
    whereClause += " AND "+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"="+categoryId;
}
int vectSize = PstMaterial.getCount(whereClause);
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
        (iCommand == Command.NEXT || iCommand == Command.LAST)){
    switch(iCommand){
        case Command.FIRST :
            start = 0;
            break;
            
        case Command.PREV :
            start = start - recordToGet;
            if(start < 0){
                start = 0;
            }
            break;
            
        case Command.NEXT :
            start = start + recordToGet;
            if(start >= vectSize){
                start = start - recordToGet;
            }
            break;
            
        case Command.LAST :
            int mdl = vectSize % recordToGet;
            if(mdl>0){
                start = vectSize - mdl;
            } else{
                start = vectSize - recordToGet;
            }
            
            break;
            
        default:
            start = start;
            if(vectSize<1)
                start = 0;
            
            if(start > vectSize){
                mdl = vectSize % recordToGet;
                if(mdl>0){
                    start = vectSize - mdl;
                } else{
                    start = vectSize - recordToGet;
                }
            }
            break;
    }
}
Vector vect = PstMaterial.list(start,recordToGet,whereClause,"");

%>
<!-- End of JSP Block -->
<html>
<!-- #BeginTemplate "/Templates/maindosearch.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--


var idx=0;

	function cmdSearch(){
		document.frmsearchcatalog.start.value="0";
		document.frmsearchcatalog.command.value="<%=Command.LIST%>";
		document.frmsearchcatalog.action="orderdosearch.jsp";
		document.frmsearchcatalog.submit();
	}

	function enter(){
		if(event.keyCode==13)
			cmdSearch();
	}

	function cmdEdit(code){
		self.opener.document.forms.frmadditem.command.value = "<%=Command.ADD%>";
		self.opener.document.forms.frmadditem.TXT_BARCODE.value = code;
		self.opener.document.forms.frmadditem.submit();
		self.close();
	}

	function cmdListFirst(){
		document.frmsearchcatalog.command.value="<%=Command.FIRST%>";
		document.frmsearchcatalog.action="orderdosearch.jsp";
		document.frmsearchcatalog.submit();
	}

	function cmdListPrev(){
		document.frmsearchcatalog.command.value="<%=Command.PREV%>";
		document.frmsearchcatalog.action="orderdosearch.jsp";
		document.frmsearchcatalog.submit();
	}

	function cmdListNext(){
		document.frmsearchcatalog.command.value="<%=Command.NEXT%>";
		document.frmsearchcatalog.action="orderdosearch.jsp";
		document.frmsearchcatalog.submit();
	}

	function cmdListLast(){
		document.frmsearchcatalog.command.value="<%=Command.LAST%>";
		document.frmsearchcatalog.action="orderdosearch.jsp";
		document.frmsearchcatalog.submit();
	}

	function clear(){
		document.frmsearchcatalog.bar_code.value="";
	}

	function change(){
		<%
			for(int k=0;k<vect.size();k++){
				Material mat = (Material)vect.get(k);
		%>
			if(idx==<%=k%>)
				cmdEdit('<%=mat.getSku()%>');
		<%}%>
	}

	// function for next change
	function chk(){
		if(event.keyCode==38){
			if(idx!=0)
				idx = idx-1;

			ClearAll();
			CheckAll();
		}else if(event.keyCode==40){
			if(idx<<%=vect.size()%>)
				idx = idx+1;

			ClearAll();
			CheckAll();
		}else if(event.keyCode==45){
			change();
		}else if(event.keyCode==13){
			cmdSearch();
		}else if(event.keyCode==37){
			cmdListPrev();
		}else if(event.keyCode==39){
			cmdListNext();
		}
	}

    function CheckAll()
    {
		var ml = document.frmsearchcatalog;
		var len = ml.elements.length;
		for (var i = 0; i < len; i++){
			var e = ml.elements[i];
			if ((e.name == "hdn") && (e.value==idx)){
				Highlight(e);
			}
		}
    }

    function ClearAll()
    {
		var ml = document.frmsearchcatalog;
		var len = ml.elements.length;
		for (var i = 0; i < len; i++){
			var e = ml.elements[i];
			if (e.name == "hdn"){
				unHighlight(e);
			}
		}
    }

    function Highlight(e){
		var r = null;
		if (e.parentNode && e.parentNode.parentNode) {
			r = e.parentNode.parentNode;
		}else if (e.parentElement && e.parentElement.parentElement) {
			r = e.parentElement.parentElement;
		}
		if (r) {
			if (r.className == "listgensell") {
				r.className = "listfocus";
			}
		}
    }

    function unHighlight(e){
		var r = null;
		if (e.parentNode && e.parentNode.parentNode) {
			r = e.parentNode.parentNode;
		}
		else if (e.parentElement && e.parentElement.parentElement) {
			r = e.parentElement.parentElement;
		}
		if (r) {
			if (r.className == "listfocus") {
				r.className = "listgensell";
			}
		}
    }

	// end change

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- #BeginEditable "editfocus" -->
<script language="JavaScript">
	window.focus();
</script>
<!-- #EndEditable -->
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20"><font color="#FF8080" face="Century Gothic"><big><strong><!-- #BeginEditable "contenttitle" -->Pencarian
            Barang <!-- #EndEditable --></strong></big></font></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmsearchcatalog" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="category_id" value="<%=categoryId%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr>
                        <td width="9%" class="formElemen">Sku</td>
                        <td width="33%" nowrap class="formElemen"> :
                          <input type="text" name="bar_code" onKeyDown="javascript:chk()" value="<%=barCode%>" size="20" class="formElemen">
                          <a href="javascript:clear()">Clear</a> </td>
                        <td width="9%" class="formElemen">&nbsp;</td>
                        <td width="49%" class="formElemen">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" class="formElemen">List Barang</td>
                </tr>
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" class="listgen" cellpadding="0" cellspacing="1">
                      <tr>
                        <td class="listgentitle" width="19%">SKU</td>
                        <td class="listgentitle" width="50%">Nama Barang</td>
                      </tr>
                      <%
						for (int i = 0; i < vect.size(); i++) {
							Material material = (Material)vect.get(i);
					  %>
                      <tr class="listgensell">
                        <td width="19%"><a href="javascript:cmdEdit('<%=material.getSku()%>')"><%=material.getSku()%></a> <input type="hidden" name="hdn" value="<%=i%>">
                        </td>
                        <td width="50%"><%=material.getName()%></td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <%//= drawList(vect,start,vendorId,curType,currency)%>
                  </td>
                </tr>
                <tr>
                  <td width="87%"><span class="command">
                    <%
						ControlLine ctrlLine= new ControlLine();
					%>
                    <% ctrlLine.setLocationImg(approot+"/images");
					ctrlLine.initDefault();
					 %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span></td>
                  <td width="13%">&nbsp;</td>
                </tr>
                <tr>
                  <td align="right" colspan="2" height="14">
                    <table width="100%" border="0">
                      <tr>
                        <td width="24%">&nbsp;</td>
                        <td width="24%">&nbsp;</td>
                        <td width="29%" align="right"><font face="Times New Roman, Times, serif" size="2">Insert
                          </font></td>
                        <td width="23%"><font size="2" face="Times New Roman, Times, serif">=
                          Mengambil Barang</font></td>
                      </tr>
                      <tr>
                        <td width="24%">&nbsp;</td>
                        <td width="24%">&nbsp;</td>
                        <td width="29%">&nbsp;</td>
                        <td width="23%">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <input type="submit" value="Submit" style="width: 0; height: 0">
              </table>
            </form>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> <!-- #EndEditable -->
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
<script language="JavaScript">
	document.frmsearchcatalog.bar_code.focus();
</script>
</html>
