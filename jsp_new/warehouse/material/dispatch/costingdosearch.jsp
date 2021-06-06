
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                   com.dimata.posbo.entity.warehouse.MatCostingItem,
			 com.dimata.common.entity.periode.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.masterdata.CtrlMaterial" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = 
{
	{"Kategori","Sku","Nama Barang","Semua","Tampilkan"},
	{"Category","Material Code","Material Name","All","View"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{ 
	{"No","Kode Barang","Nama Barang","Unit","Qty Stok"},
	{"No","Code","Item Name","Unit","Stock Qty"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
	long oidreturn = 0;
	if(vectDf!=null && vectDf.size()>0){
		for(int k=0;k<vectDf.size();k++){
			MatCostingItem matDf = (MatCostingItem)vectDf.get(k);
			if(matDf.getMaterialId()==oidmaterial){
				oidreturn = matDf.getOID();
				break;
			}
		}
	}
	return oidreturn;
}

public String drawListMaterial(int language,Vector objectClass, Vector vectDf, int start,int tranUsedPriceHpp, int enableStokFisik, long locationId, Date costingDate, int typeOfBusinessDetail)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");	
		ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"35%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"15%");
		
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
			Vector list = (Vector)objectClass.get(i);
			Material material = (Material)list.get(0);
            MaterialStock materialStock = (MaterialStock)list.get(1);
            double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationId, costingDate, 0);
            Vector rowx = new Vector();
            Unit unit = new Unit();
            try{
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
            }catch(Exception e){}

            //for litama
            Category category = new Category();
            Color color = new Color();
            Material newMat = new Material();
            MaterialDetail matDetail = new MaterialDetail();

            try{
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
                newMat = PstMaterial.fetchExc(material.getOID());
                category = PstCategory.fetchExc(newMat.getCategoryId());
                color = PstColor.fetchExc(newMat.getPosColor());
                matDetail = PstMaterialDetail.fetchExcMaterialDetailId(material.getOID());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            String itemName = material.getName();
            if (typeOfBusinessDetail == 2) {
                if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newMat.getName();
                } else if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newMat.getName();
                }
            }

            start = start + 1;
            rowx.add("<div align=\"center\">"+start+"</div>");
            rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
            rowx.add(itemName);
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stokSisa)+"</div>");

            lstData.add(rowx);
           // long oid = getOidDfItem(vectDf,material.getOID());

            String name = "";
            name = itemName;
            name = name.replace('\"','`');
            name = name.replace('\'','`');
			if(tranUsedPriceHpp==0){
				lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
					//"','"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"','"+material.getDefaultPrice()+"','"+material.getDefaultStockUnitId()+"','0");
                                        "','"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"','"+material.getDefaultPrice()+"','"+material.getDefaultStockUnitId()+"','"+stokSisa+"','"+enableStokFisik+"','0'"
                                        + ",'"+matDetail.getBerat()+"','"+matDetail.getOngkos()+"','"+typeOfBusinessDetail+"");
			}else{
				double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(),PstPriceTypeMapping.getOidStandartRate(),PstPriceTypeMapping.getOidPriceType());
				lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
					//"','"+FRMHandler.userFormatStringDecimal(sellPrice)+"','"+material.getDefaultPrice()+"','"+material.getDefaultStockUnitId()+"','0");
                                        "','"+FRMHandler.userFormatStringDecimal(sellPrice)+"','"+material.getDefaultPrice()+"','"+material.getDefaultStockUnitId()+"','"+stokSisa+"','"+enableStokFisik+"','0'"
                                        + ",'"+matDetail.getBerat()+"','"+matDetail.getOngkos()+"','"+typeOfBusinessDetail+"");
			}				
        }
		return ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data barang ...</div>";				
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long locationId = FRMQueryString.requestLong(request,"location_id");
long periodeId = FRMQueryString.requestLong(request,"periode_id");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_costing_id");
int enableStokFisik = FRMQueryString.requestInt(request, "enable_stock_fisik");
int txtViewItem= FRMQueryString.requestInt(request, "txt_view_item");
String stringCostingDate = FRMQueryString.requestString(request, "costing_date");
//update opie-eyek 30012013 agar di BRR mau langsung default menampilkan item material berdasarkan lokasi pembiayaan
String matName = FRMQueryString.requestString(request,"txt_materialname");
String matCode = FRMQueryString.requestString(request,"mat_code");
DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
Date costingDate = new Date();
if(!stringCostingDate.equals("")){
    costingDate = format.parse(stringCostingDate);
}


int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String pageTitle = "Gudang > Costing > Pencarian Barang";

if(periodeId==0){
    Vector vt_mp = PstPeriode.list(0,0,""+PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING,"");
    if(vt_mp!=null && vt_mp.size()>0){
        Periode matPeriode = (Periode)vt_mp.get(0);
        periodeId = matPeriode.getOID();
    }
}

//int vectSize = PstMaterialStock.getCountDaftarBarangStock(locationId,periodeId,0,matCode,matName);
int vectSize = PstMaterialStock.getCountDaftarBarangStockorGlobalStock(locationId,periodeId,materialgroup,matCode,matName,txtViewItem);
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}

//Vector vect = PstMaterialStock.getDaftarBarangStock(start,recordToGet,locationId,periodeId,0,matCode,matName);
Vector vect = PstMaterialStock.getDaftarBarangStockorGlobalStock(start,recordToGet,locationId,periodeId,materialgroup,matCode,matName,txtViewItem);
Vector vectCos = new Vector(1,1);

/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */
int automaticSelected = Integer.valueOf(AppValue.getValueByKey("AUTOMATIC_SELECTED"));
if(vectSize == 1 && automaticSelected==0) {
    Vector list = (Vector)vect.get(0);
     Material material = (Material)list.get(0);
     MaterialStock materialStock = (MaterialStock)list.get(1);
     double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationId, costingDate, 0);
     Unit unit = new Unit();
			try {
				unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
			}
			catch(Exception e) {
				System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
			}
                       
   
    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
   
    %>
    <script language="JavaScript">
        self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_MATERIAL_ID]%>.value ="<%=material.getOID()%>";
        self.opener.document.forms.frm_matdispatch.matCode.value = "<%=material.getSku()%>";
	self.opener.document.forms.frm_matdispatch.matItem.value = "<%=name%>";
        self.opener.document.forms.frm_matdispatch.matUnit.value = "<%=unit.getCode()%>";
        self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";
        self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_HPP]%>.value = "<%=material.getAveragePrice()%>";
        <%if(enableStokFisik == 1 ){%>
                self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_BALANCE_QTY]%>.value="<%=stokSisa%>";
                self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_RESIDUE_QTY]%>.focus();
        <%} else {%>
                 self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.focus();
        <%}%>
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
//function cmdEdit(matOid,matCode,matItem,matUnit,matCost,matPrice,matUnitId,oidDfItem){
function cmdEdit(matOid,matCode,matItem,matUnit,matCost,matPrice,matUnitId,qtyStock,enableStokFisik,oidDfItem,matWeight,matOngkos,typeOfBusinessDetail){
	if(oidDfItem==0){
		self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
		self.opener.document.forms.frm_matdispatch.matCode.value = matCode;
		self.opener.document.forms.frm_matdispatch.matItem.value = matItem;
                //if(typeOfBusinessDetail != 2) {
                    self.opener.document.forms.frm_matdispatch.matUnit.value = matUnit;
                    self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
                //}
                
		//self.opener.document.forms.frm_matdispatch.hidden_receive_item_id.value = receiveId;
                self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_HPP]%>.value = matCost;
                if(typeOfBusinessDetail == 2) {
                    self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_WEIGHT]%>.value = matWeight;
                    self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_COST]%>.value = matOngkos;
					self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value = 1;
                    var total = matCost + matOngkos;
                    self.opener.document.forms.frm_matdispatch.sub_total.value = total;
                }
               if(enableStokFisik == 1 ){
                
                self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_BALANCE_QTY]%>.value=qtyStock;
                self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_RESIDUE_QTY]%>.focus();
		//self.opener.document.forms.frm_matdispatch.<%//=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.focus();
                
                }
                else {
                 self.opener.document.forms.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.focus();
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
	document.frmvendorsearch.action="costingdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="costingdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="costingdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="costingdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdSearch(){
    document.frmvendorsearch.start.value="0";
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="costingdosearch.jsp";
	document.frmvendorsearch.submit();
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

function clear(){
	document.frmvendorsearch.txt_materialcode.value="";
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



<%--autocomplate add by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
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
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
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
              <input type="hidden" name="enable_stock_fisik" value="<%=enableStokFisik%>">
              <input type="hidden" name="costing_date" value="<%=stringCostingDate%>">
              <input hidden="text" name="stringCostingDatex" value="<%=costingDate%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
              <input type="hidden" name="trap" value="">
                <!-- add category -->
                  <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> 
                  <%
                  Vector category = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                  Vector vectGroupVal = new Vector(1,1);
                  Vector vectGroupKey = new Vector(1,1);
                  vectGroupVal.add(textMaterialHeader[SESS_LANGUAGE][3]+" "+textMaterialHeader[SESS_LANGUAGE][0]);
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
                  //out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
                  %>
                  <%=ControlCombo.draw("txt_materialgroup", null, ""+materialgroup, vectGroupKey, vectGroupVal, " onkeydown=\"javascript:fnTrapKD(event)\"", "formElemen")%>
                  </td>
                </tr>
                
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> 
                    <input type="text" name="mat_code" size="30" value="<%=matCode%>" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> 
                    <input type="text" onKeyDown="javascript:keyDownCheck(event)" name="txt_materialname" size="30" value="<%=matName%>" class="formElemen" id="txt_materialname">
                  </td>
                </tr>
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                  <td width="87%">
                  <%

                    Vector key_sort = new Vector(1,1);
                    Vector val_sort = new Vector(1,1);

                    key_sort.add("0");
                    val_sort.add("ALL");

                    key_sort.add("1");
                    val_sort.add("Item Berdasarkan Lokasi Pembiayaan");

                  %>
                  <%=ControlCombo.draw("txt_view_item", null, ""+txtViewItem, key_sort, val_sort, "", "formElemen")%>
                  </td>
                </tr>
                <tr>  
                  <td width="13%">&nbsp;</td>
                  <td width="87%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,vectCos,start,tranUsedPriceHpp,enableStokFisik,locationId,costingDate,typeOfBusinessDetail)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <% 
						ControlLine ctrlLine= new ControlLine();	
					%>
                    <% ctrlLine.setLocationImg(approot+"/images");
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


<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>


</html>
