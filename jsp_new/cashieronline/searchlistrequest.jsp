

<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.pos.form.billing.FrmBillDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html"%>
<%@ include file = "../main/javainit.jsp" %>
<%
boolean privShowStock = true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeMasterdata, AppObjInfo.COMMAND_VIEW_STOCK));
%>
<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Kategori","Sku","Nama Barang","Semua Barang"},
    {"Category","Code","Material Name","All Goods"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","UNIT","QTY REQUEST"},
    {"NO","CODE","NAME PRODUCT","UNIT","QTY REQUEST STOCK"}
};

public String drawListMaterial(int currency,int language,Vector objectClass,int start, boolean sts, boolean privShowStock){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"40%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
        
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
            
            Vector vBill = (Vector) objectClass.get(i); 
	    Billdetail billdetail =(Billdetail)vBill.get(0); 
            Unit unit = new Unit();
            try {
                    unit = PstUnit.fetchExc(billdetail.getUnitId());     
            }
            catch(Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }
            Material material= new Material();
            try {
                    material = PstMaterial.fetchExc(billdetail.getMaterialId());     
            }
            catch(Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }

            start = start + 1;
            Vector rowx = new Vector();

            rowx.add(""+start);
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(""+unit.getCode());
            rowx.add(""+billdetail.getQty());
            
            lstData.add(rowx);
            
            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
           
            
            if(sts){
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+
                        "','"+billdetail.getQty());
            }else{
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal(material.getDefaultCost())+"','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+
                        "','"+billdetail.getQty());
            }
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
String materialcode = FRMQueryString.requestString(request,"mat_code");
String nameMaterial = FRMQueryString.requestString(request,"txt_materialname");
long oidBillMain = FRMQueryString.requestLong(request,"oidBillMain");
long oidBillMainIssue = FRMQueryString.requestLong(request,"oidBillMainIssue");
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");

Vector lisBillDetailIssue = new Vector();
String whereClauseX = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";

if(materialcode!=""){
    whereClauseX = whereClauseX + " AND (CD."+PstBillDetail.fieldNames[PstBillDetail.FLD_SKU] + " like '%" + materialcode + "%' OR pm."+PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " like '%" + materialcode+"%')";
}

if(nameMaterial!=""){
    whereClauseX =whereClauseX + " AND CD."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + " like '%" + nameMaterial + "%'";
}

whereClauseX =whereClauseX +" AND CD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] + " NOT IN ( SELECT MATERIAL_ID FROM cash_bill_detail WHERE CASH_BILL_MAIN_ID='"+oidBillMainIssue+"')";

lisBillDetailIssue = PstBillDetail.listMat(0, 0,whereClauseX, "");
  
if(lisBillDetailIssue.size() == 1) {
    
    Vector vBill = (Vector) lisBillDetailIssue.get(0); 
    Billdetail billdetail =(Billdetail)vBill.get(0); 
    Unit unit = new Unit();
    try {
            unit = PstUnit.fetchExc(billdetail.getUnitId());     
    }
    catch(Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
    }
    Material material= new Material();
    try {
            material = PstMaterial.fetchExc(billdetail.getMaterialId());     
    }
    catch(Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
    }
    
    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
%>
    <script language="JavaScript">
        self.opener.document.forms.frmcashier.materialId.value = "<%=billdetail.getMaterialId()%>";
        self.opener.document.forms.frmcashier.matItem.value = "<%=name%>"; 
        self.opener.document.forms.frmcashier.matCode.value = "<%=material.getSku()%>"; 
        self.opener.document.forms.frmcashier.unitCode.value = "<%=unit.getCode()%>";
        //alert("2");
        self.opener.document.forms.frmcashier.qtyRequest.value = "<%=billdetail.getQty()%>";
        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>.value = "<%=billdetail.getQty()%>";
        //alert("3");
        self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>.focus();
        //self.opener.document.forms.frmcashier.submit();
        self.close();
    </script>
    <% 
    
}


%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function addNewItem(){
    document.frmvendorsearch.command.value="<%=Command.ADD%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
}

function cmdEdit(matOid,matCode,matItem,matUnit,matUnitId,qty,qty2){
    var cost = cleanNumberFloat(qty,guiDigitGroup,guiDecimalSymbol);
    //alert("cost"+cost);
    //alert("qty"+qty2);
    //alert(""+matOid);
    self.opener.document.forms.frmcashier.materialId.value = matOid;
    self.opener.document.forms.frmcashier.matCode.value = matCode;
    self.opener.document.forms.frmcashier.matItem.value = matItem;
    self.opener.document.forms.frmcashier.unitCode.value = matUnit;
    self.opener.document.forms.frmcashier.qtyRequest.value = qty2;
    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>.value = qty2;
    self.opener.document.forms.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY_ISSUE]%>.focus();
    self.close();
}

function clear(){
    document.frmvendorsearch.txt_materialcode.value="";
}

function keyDownCheck(e){
    //activeTable();
   var trap = document.frmvendorsearch.trap.value;
   
   if (e.keyCode == 13 && trap==0) {
    document.frmvendorsearch.trap.value="1";
   }
   if (e.keyCode == 13 && trap==1) {
       document.frmvendorsearch.trap.value="0";
       cmdSearch();
       
   }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frmvendorsearch.txt_materialname.value="";
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
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<link rel="stylesheet" href="../styles/tab.css" type="text/css">

<%--autocomplate--%>

<script type="text/javascript" src="../styles/jquery-1.4.2.min.js"></script>
<script src="../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../styles/style.css" />

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> <%@include file="../styletemplate/template_header_empty.jsp" %>
          <td height="20" class="mainheader" colspan="2"></td>
        </tr>
        <tr>
          <td colspan="2">
            <hr size="1">
          </td>
        </tr>
        <tr>
          <td>
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="mat_vendor" value="<%=oidBillMain%>">
              <%--autocomplate--%>
              <input type="hidden" name="trap" value="">
              <input type="hidden" name="source_link2" value="materialrequestsearch.jsp">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2"><%=drawListMaterial(1,SESS_LANGUAGE,lisBillDetailIssue,start,true,privShowStock)%></td>
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
<script language="JavaScript">
         document.frmvendorsearch.txt_materialname.focus();
</script>
</html>
