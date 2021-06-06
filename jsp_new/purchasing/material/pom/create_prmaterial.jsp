<%-- 
    Document   : create_prmaterial
    Created on : Feb 14, 2014, 11:53:04 AM
    Author     : dimata005
--%>

<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.session.masterdata.SessMaterial,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
				   com.dimata.posbo.entity.search.SrcMinimumStock,
				   com.dimata.posbo.session.warehouse.SessMinimumStock" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
	public static final String textListGlobal[][] = {
		{"Buat PR", "Kembali ke Minimum Stock", "Daftar PR"},
		{"Create PR", "Back to Minimum Stock", "PR List"}
	};

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] =
    {
        {"No","Kode Barang","Nama Barang","Qty Order","Nama Supplier","Daftar Barang Order","Lokasi Order"},
        {"No","Code","Product Name","Qty PR","Supplier Name","Product Order List","Order Location"}
    };

	public String drawList(Vector list, Vector listQty, int language, long location_oid_select, long supplier_id_select){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>","4%");//no
		ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>","8%");//kode barang
		ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>","25%");//nama
                ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][6]+"</div>","8%");//supplier
                
                ctrlist.addHeader("<div align=\"center\"> Supplier </div>","25%"); //supplier
                ctrlist.addHeader("<div align=\"center\"> Nama Supplier </div>","25%"); //nama supplier
                ctrlist.addHeader("<div align=\"center\"> Unit Order </div>","25%"); //unit order
                ctrlist.addHeader("<div align=\"center\"> Price </div>","25%"); //price
		ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>","8%");//qty
                ctrlist.addHeader("<div align=\"center\"> Total Price </div>","25%"); //total
                ctrlist.addHeader("<div align=\"center\"> Unit Stock </div>","25%"); //unit
                

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

        // for lokasi
        Vector val_lokasi = new Vector(1,1);
        Vector key_lokasi = new Vector(1,1);
        String where="";
        if (location_oid_select!=0){
            where= PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+location_oid_select+"'";
        }
	Vector vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
        if(vt_lokasi!=null && vt_lokasi.size()>0){
            for(int d=0; d<vt_lokasi.size(); d++){
                Location loc = (Location)vt_lokasi.get(d);
                String cntName = loc.getName();
                if (cntName.compareToIgnoreCase("'") >= 0) {
                    cntName = cntName.replace('\'','`');
                }
                val_lokasi.add(String.valueOf(loc.getOID()));
                key_lokasi.add(cntName);
            }
        }

        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
	for (int i = 0; i < list.size(); i++){
            Vector v1 = (Vector)list.get(i);
            Material material = (Material)v1.get(0);
            double qty = Double.parseDouble((String)v1.get(1));
            Unit unit = (Unit) v1.get(2);
            
            //cek vendor yang ada berdasarkan oidmaterial
            //untuk create po dari minimum stock masuk ke credit PO jadi ga ada pemilihan new supplier
            String whereClause = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+material.getOID();
            Vector listMatVendorPrice = PstMatVendorPrice.listFiltter(0,0, whereClause , "");
            Vector vectUnitVal = new Vector(1,1);
            Vector vectUnitKey = new Vector(1,1);
            long firstVendor=0;
            String firstVendorName="";
            double firstPriceBuying=0.0;
            for(int v=0; v<listMatVendorPrice.size(); v++)
            {
                MatVendorPrice matVendorPrice = (MatVendorPrice)listMatVendorPrice.get(v);
                if(v==0){
                    firstVendor=matVendorPrice.getVendorId();
                    firstVendorName=PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
                }
                vectUnitKey.add(""+matVendorPrice.getVendorId());
                vectUnitVal.add(""+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId()));
            }
            
            
            //untuk unit stok
            Vector vectUnitSupVal = new Vector(1,1);
            Vector vectUnitSupKey = new Vector(1,1);
            if(firstVendor>0){
                String whereClauseUnitSupp = " MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+material.getOID()+""+
                                             " AND MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"="+firstVendor;
                Vector listJoinUnit = PstMatVendorPrice.listJoinUnit(0,0, whereClauseUnitSupp , "");
                 for(int j=0; j<listJoinUnit.size(); j++)
                    {
                        MatVendorPrice matVendorPrice = (MatVendorPrice)listJoinUnit.get(j);
                        if(j==0){
                                    firstPriceBuying=matVendorPrice.getOrgBuyingPrice();
                        }
                        vectUnitSupKey.add(""+matVendorPrice.getBuyingUnitId());
                        vectUnitSupVal.add(""+matVendorPrice.getBuyingUnitName());
                    } 
            }else{
            //tampilkan unit yang bisa di beli di material tsb
                String whereBaseUnitOrder = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID] + " ='"+material.getOID()+"'";
                Vector listBaseUnitOrder = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
                for(int n=0; n<listBaseUnitOrder.size(); n++){
                    MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listBaseUnitOrder.get(n);
                        vectUnitSupKey.add(""+materialUnitOrder.getUnitID());
                        vectUnitSupVal.add(""+materialUnitOrder.getUnitKode());
                    }
            }
            
            
            Vector rowx = new Vector();
            rowx.add(""+(i+1));//no
            rowx.add(String.valueOf(material.getSku()));//kode
            rowx.add(String.valueOf(material.getName()));//nama
            rowx.add(ControlCombo.draw("location_order_name",null,String.valueOf(0),val_lokasi,key_lokasi,"","formElemen"));//lokasi
            
            rowx.add(ControlCombo.draw("txt_supplier_id_"+i,"formElemen", null, "", vectUnitKey, vectUnitVal, "onChange=\"javascript:changeContractPrice(this.value,'"+material.getOID()+"','"+i+"')\""));//supplier/vendor
            
            rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"15\" name=\"txt_supplier_name_"+i+"\" value=\"\" class=\"formElemen\" style=\"text-align:right\"></div>");//nama supplier
            
            rowx.add("<div align=\"center\" id=\"posts_"+i+"+\">"+ControlCombo.draw("txt_unit_request_id_"+i,"formElemen", null, "", vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");//unit request

            rowx.add("<div align=\"center\"><input tabindex=\"3\" type=\"text\" size=\"8\" name=\"txt_price_buying_"+i+"\" value=\""+firstPriceBuying+"\" class=\"formElemen\" style=\"text-align:right\"></div>");//price
            
            rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\""+material.getOID()+"\" size=\"10\" value="+qty+" align=\"left\">" +
                                            " <input type=\"hidden\" name=\"txt_order\" class=\"formElemen\" size=\"6\" value="+material.getOID()+ "> "+
                                            " <input type=\"hidden\" name=\"txt_unit"+material.getOID()+"\" class=\"formElemen\" size=\"6\" value="+material.getDefaultStockUnitId()+"></div>");
            
            double totalprice=firstPriceBuying*qty;
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\"total_price_"+i+"\" value=\""+Formater.formatNumber(totalprice, "###.##")+"\" readOnly></div>");
            
            rowx.add("<input type=\"hidden\" name=\"unit_id\" value=\""+material.getDefaultStockUnitId()+"\">"+unit.getCode()+"");


            lstData.add(rowx);
          }
            return ctrlist.draw();
	}


	public boolean cekValue(Vector vect){
		for(int k=0;k<vect.size();){
			Vector vt = (Vector)vect.get(k);
			String strSKU = (String)vt.get(0);
			String strQty = (String)vt.get(1);
			if(!strSKU.equals("") && !strQty.equals("")){
				return true;
			}
		}
		return false;
	}

%>
<!-- JSP Block -->
<%
    long location_id_select = FRMQueryString.requestLong(request, "location_id_select");
    long supplier_id_select = FRMQueryString.requestLong(request, "supplier_id_select");
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int finish = FRMQueryString.requestInt(request, "finish");
    int typeRequest =Integer.parseInt((request.getParameter("typeRequest")==null) ? "0" : request.getParameter("typeRequest"));

    String txtqtypo[] = request.getParameterValues("txt_order");
    String txtlokasi[] = request.getParameterValues("location_order_name");
    String txtsupplier[] = request.getParameterValues("supplier_order_name");

	/** mendapatkan objek untuk search key */
	SrcMinimumStock srcMinimumStock = new SrcMinimumStock();
	try{
		srcMinimumStock = (SrcMinimumStock)session.getValue(SessMinimumStock.SESSION_MINIMUM_NAME);
		if (srcMinimumStock == null) srcMinimumStock = new SrcMinimumStock();
	 }catch(Exception e){
		srcMinimumStock = new SrcMinimumStock();
	 }
	 session.putValue(SessMinimumStock.SESSION_MINIMUM_NAME, srcMinimumStock);

    long oidlocationselect = srcMinimumStock.getLocationId();
    // ini proses pengambilan qty po automatic creation
    Vector vectQty = new Vector(1,1);
    if(finish ==0){
        if(txtqtypo.length > 0){
            for(int k=0;k < txtqtypo.length;k++){
                long oidMaterial = Long.parseLong(txtqtypo[k]);
                double qtypo = FRMQueryString.requestDouble(request, ""+oidMaterial+"");
                long oidUnit = FRMQueryString.requestLong(request,"txt_unit"+oidMaterial+"");
                //System.out.println("Qty PR : "+qtypo+ " oidMaterial : "+oidMaterial + " txtqtypo[k] : "+txtqtypo[k]);
                if(qtypo!=0){
                    Vector vt = new Vector();
                    vt.add(String.valueOf(oidMaterial));
                    vt.add(String.valueOf(qtypo));
                    vt.add(String.valueOf(oidUnit));
                    vectQty.add(vt);
                }
            }
            if(vectQty!=null && vectQty.size()>0){
                session.putValue("sess_new_po", vectQty);
            }
        }
    }
    // pencarian supplier untuk masing2 barang
    Vector list = SessMaterial.getListItemForPRMinimumStock(vectQty);
    if(finish==1){
        vectQty = new Vector();
        if(txtqtypo.length > 0){
            for(int k=0;k < txtqtypo.length;k++){
                long oidMaterial = Long.parseLong(txtqtypo[k]);
                double qtypo = FRMQueryString.requestDouble(request, ""+oidMaterial+"");
                long oidUnit = FRMQueryString.requestLong(request,"txt_unit"+oidMaterial+"");
                //System.out.println("Qty PO : "+qtypo+ " oidMaterial : "+oidMaterial + " txtqtypo[k] : "+txtqtypo[k]);
                if(qtypo > 0){
                    long oidlokasi = Long.parseLong(txtlokasi[k]);
                    //long oidsupplier = Long.parseLong(txtsupplier[k]);

                    Vector vt = new Vector();

                    vt.add(String.valueOf(oidMaterial));
                    vt.add(String.valueOf(qtypo));
                    vt.add(String.valueOf(oidlokasi));
                    //vt.add(String.valueOf(oidsupplier));
                    vt.add(new Long(oidUnit));

                    vectQty.add(vt);
                }
            }
            if(vectQty.size() > 0){
                PstPurchaseRequest.autoInsertPr(vectQty);
            }else{
                if(txtqtypo.length > 0){
                    for(int k=0;k < txtqtypo.length;k++){
                        long oidMaterial = Long.parseLong(txtqtypo[k]);
                        int qtypo = FRMQueryString.requestInt(request, ""+oidMaterial+"");
                        long oidUnit = FRMQueryString.requestLong(request,"txt_unit"+oidMaterial+"");
                        //System.out.println("Qty PO : "+qtypo+ " oidMaterial : "+oidMaterial + " txtqtypo[k] : "+txtqtypo[k]);
                        if(qtypo!=0){
                            Vector vt = new Vector();
                            vt.add(String.valueOf(oidMaterial));
                            vt.add(String.valueOf(qtypo));
                            vt.add(String.valueOf(oidUnit));
                            vectQty.add(vt);
                        }
                    }
                    if(vectQty!=null && vectQty.size()>0){
                        session.putValue("sess_new_po", vectQty);
                    }
                }
            }
        }
    }
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
	function cmdSave()
	{
		document.frm_newpo.command.value="<%=Command.SAVE%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action="create_prmaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdFinish() {
            //alert("ajsajsa");
            var x = confirm("Are you sure ?");
            if(x){
                    document.frm_newpo.command.value="<%=Command.SAVE%>";
                    document.frm_newpo.finish.value="1";
                    document.frm_newpo.typeRequest.value="<%=typeRequest%>";
                    document.frm_newpo.action="create_prmaterial.jsp";
                    document.frm_newpo.submit();
            }
        }

	function cmdBack() {
		document.frm_newpo.command.value="<%=Command.BACK%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action=  "../../../warehouse/material/report/reportstockmin_list.jsp";// "srcpomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdBackPo() {
		document.frm_newpo.command.value="<%=Command.BACK%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action=  "../../../purchasing/material/pom/prmaterial_list.jsp";
		document.frm_newpo.submit();
	}

	function cmdListFirst()
    {
		document.frm_newpo.command.value="<%=Command.FIRST%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListPrev()
    {
		document.frm_newpo.command.value="<%=Command.PREV%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListNext()
    {
		document.frm_newpo.command.value="<%=Command.NEXT%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListLast()
    {
		document.frm_newpo.command.value="<%=Command.LAST%>";
                document.frm_newpo.typeRequest.value="<%=typeRequest%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}



function changeContractPrice(vendorId, idmaterial, idx){
       checkAjaxUnitRequest(vendorId,idmaterial,idx);
       var unitRequestId = "0";
       
       unitRequestId=document.frm_newpo.txt_unit_request_id_+idx.value;
       
       
       checkAjaxPrice(unitRequestId,vendorId,idx);
       checkAjaxNameVendor(unitRequestId,vendorId,idx);
}

 function checkAjaxUnitRequest(vendorId,idmaterial,idx){
            $("#posts").html("");
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=2&vendorId="+vendorId+"&material_id="+idmaterial+"",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    content=data;
                    $(content).appendTo("#posts_"+idx);
            }
        });
}


 function checkAjaxPrice(unitId,vendorId,idx){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=1&unitId="+unitId+"&vendorId="+vendorId+"",
            type : "POST",
            async : false,
            success : function(data) {
                document.frm_newpo.txt_price_buying_+idx.value=data;
            }
        });
}

function checkAjaxNameVendor(unitId,vendorId,idx){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=4&unitId="+unitId+"&vendorId="+vendorId+"",
            type : "POST",
            async : false,
            success : function(data) {
                document.frm_newpo.txt_supplier_name_+idx.value=data;
            }
        });
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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
<!-- #BeginEditable "headerscript" untuk ajax-->
<script src="../../../styles/jquery.min.js"></script>

<SCRIPT language=JavaScript>
<!--
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

function hideObjectForSystem(){
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
//-->
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><strong>
            <%=textListMaterialHeader[SESS_LANGUAGE][5]%></strong><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <%
            try
            {
            %>
            <form name="frm_newpo" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="finish" value="">
              <input type="hidden" name="location_id_select"  value="<%=location_id_select%>">
              <input type="hidden" name="supplier_id_select"  value="<%=supplier_id_select%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
              <input type="hidden" name="typeRequest" value="<%=typeRequest%>">
              
                <%
                if (finish != 1){
                	if(list != null && list.size()>0){
                    	try{
				%>
                <tr>
                  <td colspan="2"><%= drawList(list,vectQty,SESS_LANGUAGE,location_id_select,supplier_id_select)%></td>
                </tr>
                <tr>
                  <td span class="command">
</td>
                </tr>
                <%
						}
						catch(Exception exc){
								System.out.println("DrawList : " + exc.toString());
							}
					}
					%>
                <tr>
                  <td class="command" colspan="2">
                    <table width="62%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="5%"><a href="javascript:cmdFinish()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnSaveOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][0]%>"></a></td>
                        <td class="command" nowrap width="25%"><a href="javascript:cmdFinish()" class="command"><%=textListGlobal[SESS_LANGUAGE][0]%></a></td>
                        <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][1]%>"></a></td>
                        <td class="command" nowrap width="65%"><a href="javascript:cmdBack()"><%=textListGlobal[SESS_LANGUAGE][1]%></a></td>
                      </tr>
                    </table>
                  </td>
                </tr>
				<%
					}else{
				%>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" span class="comment" bgcolor="#CCCCCC">Proses success ... </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="4%"><a href="javascript:cmdBackPo()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][2]%>"></a></td>
                        <td class="command" nowrap width="98%"><a href="javascript:cmdBackPo()"><%=textListGlobal[SESS_LANGUAGE][2]%></a></td>
                      </tr>
                    </table>
	           </td>
                </tr>
                <%
                }
                %>
              </table>
            </form>
            <%
                }
                catch(Exception eee)
                {
                    out.println("Form Exc : "+eee);
                }
            %>
            <script language="JavaScript">
	window.focus();
</script>
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

