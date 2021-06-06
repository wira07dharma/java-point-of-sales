<%@ page import = "com.dimata.gui.jsp.ControlList,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.pos.form.billing.*,
                 com.dimata.pos.entity.billing.*,
                com.dimata.posbo.entity.masterdata.*,
                com.dimata.posbo.form.masterdata.*,
                com.dimata.posbo.entity.warehouse.*,
                com.dimata.posbo.form.warehouse.*,
                com.dimata.pos.form.balance.*,
                com.dimata.pos.entity.balance.*" %>
<%@ page contentType = "text/html" %>

<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<!--%@ include file = "../../../main/checkuser.jsp" %-->

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Serial Code","Nama Barang"},
    {"Serial Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SERIAL CODE","NAMA BARANG"},
    {"NO","SERIAL CODE","NAME PRODUCT"}
};

public String drawListMaterial(long oidMaterial,int language,Vector objectClass,int start){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"25%");

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
            Vector vt = (Vector)objectClass.get(i);

            DispatchStockCode dispatchStockCode = (DispatchStockCode)vt.get(0);
            Material material = (Material)vt.get(1);
            start = start + 1;

            Vector rowx = new Vector();

            rowx.add(""+start);
            rowx.add(material.getName());
            rowx.add(dispatchStockCode.getStockCode());

            lstData.add(rowx);

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');

            lstLinkData.add(name+"','"+dispatchStockCode.getStockCode());

        }
        return ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
    }
    return result;
}
%>

<!-- JSP Block -->
<%
long oidMaterial = FRMQueryString.requestLong(request,"material_id");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String orderBy = "";
int vectSize = 1000;

CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}

String pageTitle ="";
pageTitle = "DAFTAR SERIAL NUMBER BARANG";
Vector vect = PstBillDetailCode.getListStockCode(oidMaterial,start,recordToGet, orderBy);

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function addNewItem(){
    document.frmstockcodedosearch.command.value="<%=Command.ADD%>";
    document.frmstockcodedosearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmstockcodedosearch.submit();
}

function cmdEdit(itemName, stockCode){
    self.close();
    self.opener.document.forms.frmbillcode.<%=FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE]%>.value = stockCode;
    self.opener.document.forms.frmbillcode.<%=FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE]%>.focus();

}

function cmdListFirst(){
    document.frmstockcodedosearch.command.value="<%=Command.FIRST%>";
    document.frmstockcodedosearch.action="stockcodedosearch.jsp";
    document.frmstockcodedosearch.submit();
}

function cmdListPrev(){
    document.frmstockcodedosearch.command.value="<%=Command.PREV%>";
    document.frmstockcodedosearch.action="stockcodedosearch.jsp";
    document.frmstockcodedosearch.submit();
}

function cmdListNext(){
    document.frmstockcodedosearch.command.value="<%=Command.NEXT%>";
    document.frmstockcodedosearch.action="stockcodedosearch.jsp";
    document.frmstockcodedosearch.submit();
}

function cmdListLast(){
    document.frmstockcodedosearch.command.value="<%=Command.LAST%>";
    document.frmstockcodedosearch.action="stockcodedosearch.jsp";
    document.frmstockcodedosearch.submit();
}

function cmdSearch(){
    document.frmstockcodedosearch.start.value="0";
    document.frmstockcodedosearch.command.value="<%=Command.LIST%>";
    document.frmstockcodedosearch.action="stockcodedosearch.jsp";
    document.frmstockcodedosearch.submit();
}

function clear(){
    document.frmstockcodedosearch.txt_materialcode.value="";
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
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
        </tr>
        <tr>
          <td colspan="2">
            <hr size="1">
          </td>
        </tr>
        <tr>
          <td>
            <form name="frmstockcodedosearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="source_link2" value="stockcodedosearch.jsp">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">


                <tr>
                  <td colspan="2"><%=drawListMaterial(oidMaterial,SESS_LANGUAGE,vect,start)%></td>
                </tr>
                <tr>
                  <td colspan="2"><span class="command">
                    <%
                    ControlLine ctrlLine= new ControlLine();
                    %>
                    <%
                    ctrlLine.setLocationImg(approot+"/images");
                    ctrlLine.initDefault();
                    %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span></td>
                </tr>
              </table>
            </form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
