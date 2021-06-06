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
                 com.dimata.posbo.session.warehouse.SessReportStock,
                 com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem,
                 com.dimata.posbo.entity.warehouse.PstMatDispatchReceiveItem,
                 com.dimata.posbo.form.warehouse.FrmMatDispatchReceiveItem"%>
<%@page contentType="text/html"%>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final String textListGlobal[][] = {
    {"Pencarian Barang","Tidak ada data barang"},
    {"Goods Searching","No goods data available"}
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
    {"No","Sku","Nama Barang","Unit","Qty Stok"},
    {"No","Code","Item Name","Unit","Stock Qty"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
    long oidDispatch = 0;
    if(vectDf!=null && vectDf.size()>0){
        for(int k=0;k<vectDf.size();k++){
            MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)vectDf.get(k);
            if(dfRecItem.getSourceItem().getMaterialSource().getOID()==oidmaterial){
                oidDispatch =dfRecItem.getOID();
                break;
            }
        }
    }
    return oidDispatch;
}

public String drawListMaterial(int language,Vector objectClass, Vector vectDf, int start, int tranUsedPriceHpp) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"60%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"15%");

        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        if(start<0) start = 0;

        for(int i=0; i<objectClass.size(); i++){
            Vector list = (Vector)objectClass.get(i);
            Material material = (Material)list.get(0);
            MaterialStock materialStock = (MaterialStock)list.get(1);
            Unit unit = (Unit)list.get(2);
            Vector rowx = new Vector();
            /*Unit unit = new Unit();
            try{
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
            }catch(Exception e){}*/

            start = start + 1;
            rowx.add("<div align=\"center\">"+start+"</div>");
            rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
            rowx.add(material.getName());
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");

            lstData.add(rowx);
            long oid = getOidDfItem(vectDf,material.getOID());

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');

            //if(tranUsedPriceHpp==0){
                //lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                       // "','"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+
                       // "','"+material.getDefaultStockUnitId()+"','"+oid+"','"+materialStock.getQty());
           // }else{
               // double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(),PstPriceTypeMapping.getOidStandartRate(),PstPriceTypeMapping.getOidPriceType());
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                       // "','"+FRMHandler.userFormatStringDecimal(sellPrice)+
                        "','"+material.getDefaultStockUnitId()+"','"+oid+"','"+materialStock.getQty());
           //}
        }
        return ctrlist.draw();
    } else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>";
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
String materialcode = FRMQueryString.requestString(request,"mat_code1");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup1");
long locationId = FRMQueryString.requestLong(request,"location_id");
long periodeId = FRMQueryString.requestLong(request,"periode_id");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
String materialname = FRMQueryString.requestString(request,"txt_materialname1");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;

String whereClause = "";
String orderBy = "MAT."+PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

if(periodeId==0){
    Vector vt_mp = PstPeriode.list(0,0,""+PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING,"");
    if(vt_mp!=null && vt_mp.size()>0){
        Periode matPeriode = (Periode)vt_mp.get(0);
        periodeId = matPeriode.getOID();
    }
}


/** objek pencarian */
SrcReportStock srcReportStock = new SrcReportStock();
srcReportStock.setLocationId(locationId);
srcReportStock.setPeriodeId(periodeId);
srcReportStock.setCategoryId(materialgroup);
srcReportStock.setSku(materialcode);
srcReportStock.setMaterialName(materialname);

//int vectSize = PstMaterialStock.getCountDaftarBarangStock(locationId,periodeId,materialgroup,materialcode,materialname);
/** get size stock list and stock value */
Vector vctStockValue = SessReportStock.getStockValue(srcReportStock, calculateQtyNull);
int vectSize = Integer.parseInt((String)vctStockValue.get(0));

CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(start<0){
    start=0;
}
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
    start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}
if(start<0){
    start=0;
}


//Vector vect = PstMaterialStock.getDaftarBarangStock(start,recordToGet,locationId,periodeId,materialgroup,materialcode,materialname);
Vector vect = SessReportStock.getReportStockAll(srcReportStock, calculateQtyNull, start, recordToGet);

String whr = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;
Vector vectCos = PstMatDispatchReceiveItem.list(0,0,whr,"");

/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */
if(vect.size() == 1) {
    Vector list = (Vector)vect.get(0);
    Material material = (Material)list.get(0);
    MaterialStock materialStock = (MaterialStock)list.get(1);

    Unit unit = new Unit();
    try{
        unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
    }catch(Exception e){
    }
    long oidDfItem = getOidDfItem(vectCos,material.getOID());

    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');

    %>
    <script language="JavaScript">
        <% if(oidDfItem == 0) { %>
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_MATERIAL_TARGET_ID]%>.value = "<%=material.getOID()%>";
            self.opener.document.forms.frm_matdispatch.matCode1.value = "<%=material.getSku()%>";
            self.opener.document.forms.frm_matdispatch.matItem1.value = "<%=name%>";
            self.opener.document.forms.frm_matdispatch.matUnit1.value = "<%=unit.getCode()%>";
            //self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value = "<%=FRMHandler.userFormatStringDecimal(material.getAveragePrice())%>";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_UNIT_TARGET_ID]%>.value = "<%=material.getDefaultStockUnitId()%>";
            self.opener.document.forms.frm_matdispatch.avbl_qty1.value = "<%=materialStock.getQty()%>";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.style.display="";
            self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.focus();
	<% } else { %>
            self.opener.document.forms.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
            self.opener.document.forms.frm_matdispatch.command.value = "<%=Command.EDIT%>";
            self.opener.document.forms.frm_matdispatch.submit();
	<% } %>
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
function cmdEdit(matOid,matCode1,matItem1,matUnit1,matUnitId1,oidDfItem,avblQty1){
    if(oidDfItem==0){
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_MATERIAL_TARGET_ID]%>.value = matOid;
        self.opener.document.forms.frm_matdispatch.matCode1.value = matCode1;
        self.opener.document.forms.frm_matdispatch.matItem1.value = matItem1;
        self.opener.document.forms.frm_matdispatch.matUnit1.value = matUnit1;
        //self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value = matHpp;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_UNIT_TARGET_ID]%>.value = matUnitId1;
        self.opener.document.forms.frm_matdispatch.avbl_qty1.value = avblQty1;
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.style.display="";
        self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.focus();
        try{
             self.opener.document.forms.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.style.display="";
            }catch(err){
            }

    }else{
        self.opener.document.forms.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
        self.opener.document.forms.frm_matdispatch.command.value = "<%=Command.EDIT%>";
        self.opener.document.forms.frm_matdispatch.submit();
    }
    self.close();
}

function cmdListFirst()
{
    document.frmvendorsearch.command.value="<%=Command.FIRST%>";
    document.frmvendorsearch.action="dfunitdosearch_1.jsp";
    document.frmvendorsearch.submit();
}

function cmdListPrev(){
    document.frmvendorsearch.command.value="<%=Command.PREV%>";
    document.frmvendorsearch.action="dfunitdosearch_1.jsp";
    document.frmvendorsearch.submit();
}

function cmdListNext(){
    document.frmvendorsearch.command.value="<%=Command.NEXT%>";
    document.frmvendorsearch.action="dfunitdosearch_1.jsp";
    document.frmvendorsearch.submit();
}

function cmdListLast(){
    document.frmvendorsearch.command.value="<%=Command.LAST%>";
    document.frmvendorsearch.action="dfunitdosearch_1.jsp";
    document.frmvendorsearch.submit();
}

function cmdSearch(){
    document.frmvendorsearch.command.value="<%=Command.LIST%>";
    document.frmvendorsearch.start.value = '0';
    document.frmvendorsearch.action="dfunitdosearch_1.jsp";
    document.frmvendorsearch.submit();
}

function clear(){
    document.frmvendorsearch.txt_materialcode1.value="";
}
function fnTrapKD(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSearch();
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
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%> </td>
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
              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="location_id" value="<%=locationId%>">
              <input type="hidden" name="periode_id" value="<%=periodeId%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
               <tr>
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="82%">
                  <%
                    Vector category = PstCategory.list(0,0,"","");
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    vectGroupVal.add("Select ...");
                    vectGroupKey.add("0");
                    if(category!=null && category.size()>0) {
                        for(int i=0; i<category.size(); i++) {
                            Category mGroup = (Category)category.get(i);
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                    } else {
                        vectGroupVal.add("No Category Available");
                        vectGroupKey.add("0");
                    }
                  %>
                  <%=ControlCombo.draw("txt_materialgroup1", null, ""+materialgroup, vectGroupKey, vectGroupVal, " onkeydown=\"javascript:fnTrapKD(event)\"", "formElemen")%>
                  </td>
                </tr>
                <tr>
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="82%">
                    <input type="text" name="mat_code1" size="30" value="<%=materialcode%>" class="formElemen" onkeyup="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr>
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="82%">
                    <input type="text" name="txt_materialname1" size="30" value="<%=materialname%>" class="formElemen" onkeyup="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr>
                  <td width="18%">&nbsp;</td>
                  <td width="82%">
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,vectCos,start,tranUsedPriceHpp)%></td>
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
<script language="JavaScript">
    document.frmvendorsearch.txt_materialgroup.focus();
</script>
</html>
