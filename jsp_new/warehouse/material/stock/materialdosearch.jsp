<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.warehouse.FrmMatStockOpnameItem,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
	{"Kategori","Sku","Nama Barang","Sub Kategori"},
	{"Category","Material Code","Material Name","Sub Kategory"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
	{"No","Category","Code","Item","Unit","Cost"}
};

public String drawListMaterial(int language,Vector objectClass,int start, int typeOfBusinessDetail, int allowZero, long oidLocation)
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
        //ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"50%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
        ctrlist.addHeader("Berat Stok","10%");
        ctrlist.addHeader("Qty Stok","10%");
        //ctrlist.addHeader(textListMaterialHeader[language][5],"10%");

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
            Material material  = (Material)objectClass.get(i);
            String unitName = "";
            String unitCode = "";
            
            //added by dewok 2018 for jewelry
            Material mat = new Material();
            Category category = new Category();
            Color color = new Color();
            MaterialDetail md = new MaterialDetail();
            Kadar kadar = new Kadar();
            Ksg ksg = new Ksg();
            MasterType masterTypeSize = new MasterType();
            
            try{
                Unit unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                //Unit unit = PstUnit.fetchExc(material.getBuyUnitId());
                unitName = unit.getName();
                unitCode = unit.getCode();
                mat = PstMaterial.fetchExc(material.getOID());
                category = PstCategory.fetchExc(mat.getCategoryId());
                if (PstColor.checkOID(mat.getPosColor())) {
                    color = PstColor.fetchExc(mat.getPosColor());
                }
                if (PstMaterialDetail.checkOID(PstMaterialDetail.checkOIDMaterialDetailId(mat.getOID()))) {
                    md = PstMaterialDetail.fetchExc(PstMaterialDetail.checkOIDMaterialDetailId(mat.getOID()));
                }
                if (PstKadar.checkOID(mat.getPosKadar())) {
                    kadar = PstKadar.fetchExc(mat.getPosKadar());
                }
                if (PstKsg.checkOID(mat.getGondolaCode())) {
                    ksg = PstKsg.fetchExc(mat.getGondolaCode());
                }
                long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, material.getOID());
                if (PstMasterType.checkOID(oidMappingSize)) {
                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                }
            }catch(Exception e){}
			
            String name = "";
            name = material.getName();
            //if (name.compareToIgnoreCase("\"") >= 0) {
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            //}
            
            String itemName = "" + mat.getName();
            if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
            } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
            }
            
            if(typeOfBusinessDetail == 2) {
                name = itemName;
            }
                        
            double qtyStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), oidLocation, new Date(), 0);
            double beratStock = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), oidLocation, new Date(), 0);
            if (allowZero == 0 && qtyStock <= 0) {continue;}
			
            start = start + 1;
            Vector rowx = new Vector();
            rowx.add(""+start);
            rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
            rowx.add(name);
            rowx.add("<div align=\"center\">"+unitCode+"</div>");
            rowx.add("<div align=\"center\">"+beratStock+"</div>");
            rowx.add("<div align=\"center\">"+qtyStock+"</div>");

            lstData.add(rowx);
            lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+material.getDefaultStockUnitId()+"','"+unitName
                    +"','"+beratStock+"','"+kadar.getOID()+"','"+mat.getBarCode()+"','"+color.getColorName()+"','"+masterTypeSize.getMasterName());
        }
		return ctrlist.draw();
    }
	else
    {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data barang...</div>";
    }
    return result;
}
%>

<!-- JSP Block -->
<%    
    String materialcode = FRMQueryString.requestString(request, "mat_code");
    long materialgroup = FRMQueryString.requestLong(request, "txt_materialgroup");
    long materialsubcategory = FRMQueryString.requestLong(request, "txt_material_sub_category");
    long materialsupplier = FRMQueryString.requestLong(request, "txt_material_supplier");
    long materialMasterType = FRMQueryString.requestLong(request,"txt_material_master_type") ;
    //get lokasi dan oidStockOpname
    long oidLocation = FRMQueryString.requestLong(request, "location_id");
    long oidStockOpname = FRMQueryString.requestLong(request, "stock_opname_id");
    //variabel for item not opname
    int notOpname = FRMQueryString.requestInt(request, "notOpname");

     String useForGreenbowl1 = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
    
    //--added by dewok for jewelry
    long etalaseId = FRMQueryString.requestLong(request, "etalase_id");
    int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");
    //int itemType = FRMQueryString.requestInt(request,"item_type");
    String whereAdd = "";
    if (typeOfBusinessDetail == 2) {
        whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " = '" + etalaseId + "'";
        if (lantakan == 1) {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + "='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
        } else {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + "!='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
        }
    }
    //--
    //added by dewok 2018
    int allowZero = FRMQueryString.requestInt(request, "ALLOW_ZERO_QTY");
    //data for litama
    String materialname = FRMQueryString.requestString(request, "txt_materialname");
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int recordToGet = 30;
    String pageTitle = "Opname Barang > Pencarian Barang";
    String pageTitleNotOpname = "Item belum opname pada periode ini";

    /**
     * instantiate material object that handle searching parameter
     */
    String orderBy = "MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
    Material objMaterial = new Material();
    objMaterial.setCategoryId(materialgroup);
    objMaterial.setSubCategoryId(materialsubcategory);
    objMaterial.setSku(materialcode);
    objMaterial.setName(materialname);

    //System.out.println("objMaterial.getCategoryId() "+objMaterial.getCategoryId()+"-"+materialgroup);
    //System.out.println("objMaterial.getSubCategoryId() "+objMaterial.getSubCategoryId());
    //System.out.println("objMaterial.getSku() "+objMaterial.getSku());
    //System.out.println("objMaterial.getName() "+objMaterial.getName());
    //System.out.println("materialsupplier "+materialsupplier);
    /**
     * get amount/count of material's list
     */
    //int vectSize = PstMaterial.getCountMaterial(materialsupplier,objMaterial);
    int vectSize = 0;
    if (notOpname == 1) {
        //vectSize = PstMaterial.getCountMaterialOpnameAll(start, recordToGet, orderBy, oidLocation);
        vectSize = PstMaterial.getCountMaterialOpnameAll(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);
        //add list opname not opname all
        //by mirahu
        //20120127
    } else if (notOpname == 2) {
        vectSize = PstMaterial.getCountMaterialOpnameAllNew(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);

    } else {
        //vectSize = PstMaterial.getCountMaterial(materialsupplier,objMaterial);
        //int vectSize = PstMaterial.getCountMaterialOpname(materialsupplier, objMaterial, oidLocation, oidStockOpname);
        vectSize = PstMaterial.getCountMaterialOpname(materialsupplier, objMaterial, oidLocation, oidStockOpname, whereAdd);
    }

    /**
     * generate start/mailstone for displaying data
     */
    CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
    }

    /**
     * get material list will displayed in this page
     */
    //Vector vect = PstMaterial.getListMaterial(materialsupplier,objMaterial,start,recordToGet, orderBy);
    Vector vect = new Vector();
    if (notOpname == 1) {
        //vect = PstMaterial.getListMaterialOpnameAll(start, recordToGet, orderBy, oidLocation);
        vect = PstMaterial.getListMaterialOpnameAll(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);
        //add list opname not opname all
        //by mirahu
        //20120127
    } else if (notOpname == 2) {
        //vect = PstMaterial.getListMaterialOpnameAllNew(start, recordToGet, orderBy, oidLocation);
        vect = PstMaterial.getListMaterialOpnameAllNew(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);

    } else {
        vect = PstMaterial.getListMaterialOpname(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname, whereAdd);
    }

    if (vectSize == 1 && typeOfBusinessDetail != 2) {
        //Vector vData =  (Vector)vect.get(0); 
        Material material = (Material) vect.get(0);
        Color color = new Color();
        MasterType masterTypeSize = new MasterType();
        
        try {
            material = PstMaterial.fetchExc(material.getOID());
            if (PstColor.checkOID(material.getPosColor())) {
                color = PstColor.fetchExc(material.getPosColor());
            }
            long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, material.getOID());
            if (PstMasterType.checkOID(oidMappingSize)) {
                masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
            }
        } catch (Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
        }
        Unit unit = new Unit();
        try {
            unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
        } catch (Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
        }
        String name = "";
        name = material.getName();
        name = name.replace('\"', '`');
        name = name.replace('\'', '`');

%>
    <script language="JavaScript">
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
    self.opener.document.forms.frm_matopname.matCode.value = "<%=material.getSku()%>";
    self.opener.document.forms.frm_matopname.matItem.value = "<%=name%>"; 
    self.opener.document.forms.frm_matopname.matUnit.value = "<%=unit.getCode()%>";
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_OPNAME]%>.focus();
    if (<%= useForGreenbowl1.equals("1")%>) {
        self.opener.document.forms.frm_matopname.matBarcode.value = "<%=material.getBarCode() %>";
        self.opener.document.forms.frm_matopname.matColor.value = "<%=color.getColorName() %>";
        self.opener.document.forms.frm_matopname.matSize.value = "<%=masterTypeSize.getMasterName() %>";
    }
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
function cmdEdit(matOid,matCode,matItem,unitId,unitName,berat,kadarId,barcode,color,size)
{
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_matopname.matCode.value = matCode;
    self.opener.document.forms.frm_matopname.matItem.value = matItem;
    self.opener.document.forms.frm_matopname.matUnit.value = unitName;
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_UNIT_ID]%>.value = unitId;
    self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_OPNAME]%>.focus();
    if (<%=typeOfBusinessDetail == 2%>) {
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_KADAR_ID]%>.value = kadarId;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_KADAR_OPNAME_ID]%>.value = kadarId;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT]%>.value = berat;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT_OPNAME]%>.value = berat;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT_SELISIH]%>.value = 0;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_ITEM]%>.value = 1;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_OPNAME]%>.value = 1;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_SELISIH]%>.value = 0;
        self.opener.document.forms.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_REMARK]%>.focus();
    }
    if (<%= useForGreenbowl1.equals("1")%>) {
        self.opener.document.forms.frm_matopname.matBarcode.value = barcode;
        self.opener.document.forms.frm_matopname.matColor.value = color;
        self.opener.document.forms.frm_matopname.matSize.value = size;
    }
    self.close();
}

function cmdListFirst()
{
	document.frmmaterialsearch.command.value="<%=Command.FIRST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListPrev(){
	document.frmmaterialsearch.command.value="<%=Command.PREV%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListNext(){
	document.frmmaterialsearch.command.value="<%=Command.NEXT%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListLast(){
	document.frmmaterialsearch.command.value="<%=Command.LAST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdSearch(){
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
        document.frmmaterialsearch.notOpname.value="0";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdSearchList(){
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function clear(){
	document.frmmaterialsearch.txt_materialcode.value="";
}

function cmdSearchNotOpname(){
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
        document.frmmaterialsearch.notOpname.value="1";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdSearchNotOpnameAll(){
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
        document.frmmaterialsearch.notOpname.value="2";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function addListNotOpname(oid) {
	var answer = confirm("Are you sure to set Qty Item to nol?")
	if (answer){
		//alert("Ok")
		 document.frmmaterialsearch.command.value="<%=Command.ADD%>";
                 document.frmmaterialsearch.notOpname.value="1";
                 //document.frmmaterialsearch.action="mat_opname_edit.jsp";
                 document.frmmaterialsearch.action="mat_opname_item.jsp";
                 document.frmmaterialsearch.submit();
                  self.close();
	}
	else{
		alert("Please Choose Item one by one")
	}

   
}

function addListNotOpnameAll(oid) {
		 document.frmmaterialsearch.command.value="<%=Command.ADD%>";
                 document.frmmaterialsearch.notOpname.value="2";
                 //document.frmmaterialsearch.action="mat_opname_edit.jsp";
                 document.frmmaterialsearch.action="mat_opname_item.jsp";
                 document.frmmaterialsearch.submit();
                  self.close();  
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
</SCRIPT>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr><%@include file="../../../styletemplate/template_header_empty.jsp" %>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
        </tr>
        <%if(notOpname==1||notOpname==2){%>
        <tr>
          <td height="20" class="mainheader" colspan="2"><%=pageTitleNotOpname%> </td>
        </tr>
        <%}%>
        <tr>
          <td>
            <form name="frmmaterialsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="txt_material_sub_category" value="<%=materialsubcategory%>">
              <input type="hidden" name="txt_material_supplier" value="<%=materialsupplier%>">
              <input type="hidden" name="txt_materialgroup" value="<%=materialgroup%>">
              <input type="hidden" name="location_id" value="<%=oidLocation%>">
              <input type="hidden" name="stock_opname_id" value="<%=oidStockOpname%>">
              <input type="hidden" name="notOpname" value="<%=notOpname%>">
              <input type="hidden" name="hidden_opname_id" value ="<%=oidStockOpname%>">
              <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
              <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">     
              <input type="hidden" name="etalase_id" value="<%=etalaseId%>">  
              
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <!--tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="83%"> :
                    <%/*
                        Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                        Vector vectGroupVal = new Vector(1,1);
                        Vector vectGroupKey = new Vector(1,1);
                        vectGroupVal.add("Semua Kategori");
                        vectGroupKey.add("0");
                        for(int i=0; i<materGroup.size(); i++)
                        {
                            Category mGroup = (Category)materGroup.get(i);
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                        out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
					*/
				  /*Category mGroup = new Category();
				  try{
				  		mGroup = PstCategory.fetchExc(materialgroup);
				  }catch(Exception e){}
				  out.println(mGroup.getName());*/
				  %> </td>
                </tr-->
                <tr>
                  <td width="20%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
				  <td width="83%">:
                    <input type="text" name="mat_code" size="30" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="83%"> :
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen">
                  </td>
                </tr> 
                <tr>
                    <td>Termasuk Qty 0 (Nol)</td>
                    <%
                        String checkStatus = "";if(allowZero == 1) {checkStatus = "checked";}
                    %>
                    <td>: <input type="checkbox" <%=checkStatus%> name="ALLOW_ZERO_QTY" value="1"></td>
                </tr>
                <tr>
                  <td width="17%">&nbsp;</td>
                  <td width="83%">&nbsp;
                    <input type="button" name="Button" value=" Cari " onClick="javascript:cmdSearch()" class="formElemen">
                    <input type="button" name="Button" value=" Cari Item Stok >0 & periode Opname barang belum diopname " onClick="javascript:cmdSearchNotOpname()" class="formElemen">
                    <input type="button" name="Button" value=" Cari Item barang belum diopname pada periode Opname ini  " onClick="javascript:cmdSearchNotOpnameAll()" class="formElemen">
                  </td>
                </tr>
                <% if(notOpname !=0 && vect.size()>0  ){%>
                <tr>
                  <td width="17%">&nbsp;</td>
                  <td width="83%">&nbsp;
                    <% if (notOpname == 1){%>
                       <a href="javascript:addListNotOpname()">Add List Not Opname set Qty to 0</a>
                    <%} else if (notOpname == 2){%>
                      <a href="javascript:addListNotOpnameAll()">Add List Not Opname</a>
                    <%}%>
                  </td>
                </tr>
                <%}%>
                
                <tr>
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,start,typeOfBusinessDetail, allowZero, oidLocation)%></td>
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
</html>




