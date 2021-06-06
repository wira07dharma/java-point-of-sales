<%-- 
    Document   : mat_opname_item_html
    Created on : Aug 10, 2016, 1:16:33 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpname"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.warehouse.FrmMatStockOpnameItem,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][]
            = {
                {"Kategori", "Sku", "Nama Barang", "Sub Kategori"},
                {"Category", "Material Code", "Material Name", "Sub Kategory"}
            };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][]
            = {
                {"No", "Grup", "Sku", "Nama Barang", "Unit", "Harga Beli", "Barcode", "QTY Stok"},
                {"No", "Category", "Code", "Item", "Unit", "Cost", "Barcode", "QTY Stok"}
            };

    public String drawListMaterial(int language, Vector objectClass, int start, Vector listTypeHrga, int typeOfBusinessDetail) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("center");
            ctrlist.setBorder(1);
            ctrlist.addHeader(textListMaterialHeader[language][0], "1%");//no
            if (typeOfBusinessDetail != 2){
                ctrlist.addHeader(textListMaterialHeader[language][2], "5%");//sku
            }
            ctrlist.addHeader(textListMaterialHeader[language][6], "3%");//barcode
            ctrlist.addHeader(textListMaterialHeader[language][3], "8%");//nama
            
            Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);
            if (typeOfBusinessDetail != 2) {
                if (listTypeHrga != null && listTypeHrga.size() > 0) {
                    for (int l = 0; l < listTypeHrga.size(); l++) {
                        for (int j = 0; j < listCurrStandardX.size(); j++) {
                            ctrlist.addHeader("Harga Jual", "10%");
                        }
                    }
                }
            }
            if (typeOfBusinessDetail != 2){
                ctrlist.addHeader(textListMaterialHeader[language][4], "10%");//unit                        
            }
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Qty", "3%");
                ctrlist.addHeader("Qty <br> SO", "3%");
                ctrlist.addHeader("Qty <br> Selisih", "3%");
                ctrlist.addHeader("Kadar <br> (%)", "5%");
                ctrlist.addHeader("Kadar <br> SO (%)", "5%");
                ctrlist.addHeader("Berat <br> (gr)", "5%");
                ctrlist.addHeader("Berat <br> SO (gr)", "5%");
                ctrlist.addHeader("Berat <br> Selisih (gr)", "5%");
                ctrlist.addHeader("Keterangan", "15%");
            } else {
                ctrlist.addHeader(textListMaterialHeader[language][7], "10%");//qty
            }

            ctrlist.setLinkRow(1);
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
                Material material = (Material) objectClass.get(i);

                String unitName = "";
                String unitCode = "";
                try {
                    Unit unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                    //Unit unit = PstUnit.fetchExc(material.getBuyUnitId());
                    unitName = unit.getName();
                    unitCode = unit.getCode();
                } catch (Exception e) {
                }

                String name = "";
                name = material.getName();
                name = name.replace('\"', '`');
                name = name.replace('\'', '`');
            //}
                //added by dewok 2018 for litama
                Material newmat = new Material();
                Category category = new Category();
                Color color = new Color();
                Kadar kadar = new Kadar();
                Kadar kadarOpname = new Kadar();
                MaterialDetail md = new MaterialDetail();
                try {
                    newmat = PstMaterial.fetchExc(material.getOID());
                    category = PstCategory.fetchExc(newmat.getCategoryId());
                    color = PstColor.fetchExc(newmat.getPosColor());
                    kadar = PstKadar.fetchExc(material.getPosKadar());
                    kadarOpname = PstKadar.fetchExc(material.getPosKadar());
                    md = PstMaterialDetail.fetchExc(PstMaterialDetail.checkOIDMaterialDetailId(material.getOID()));
                } catch (Exception e) {

                }
                String itemName = "" + newmat.getName();
                if(typeOfBusinessDetail == 2){
                    if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                        itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                    } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                        itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                    }
                    name = itemName;
                }

                start = start + 1;
                Vector rowx = new Vector();
                rowx.add("" + start);
                if (typeOfBusinessDetail != 2){
                    rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>" + material.getSku() + "</div>");
                }
                rowx.add("<div style='mso-number-format:\"\\@\"'>" + material.getBarCode() + "</div>");
                rowx.add(itemName);
                if (typeOfBusinessDetail != 2) {
                    SrcMaterial srcMaterial = new SrcMaterial();
                    //Material material = new Material();
                    Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, material.getOID());
                    if (listTypeHrga != null && listTypeHrga.size() > 0) {
                        for (int k = 0; k < listTypeHrga.size(); k++) {
                            PriceType pricetype = (PriceType) listTypeHrga.get(k);
                            for (int j = 0; j < listCurrStandardX.size(); j++) {
                                Vector temp = (Vector) listCurrStandardX.get(j);
                                Vector tempStand = (Vector) listCurrStandardX.get(j);
                                CurrencyType currx = (CurrencyType) tempStand.get(0);
                                StandartRate standart = (StandartRate) tempStand.get(1);
                                PriceTypeMapping pTypeMapping = null;
                                if (memberPrice != null && !memberPrice.isEmpty()) {

                                    pTypeMapping = (PriceTypeMapping) memberPrice.get("" + pricetype.getOID() + "_" + standart.getOID());
                                }

                                if (pTypeMapping == null) {
                                    pTypeMapping = new PriceTypeMapping();
                                    pTypeMapping.setMaterialId(material.getOID());
                                    pTypeMapping.setPriceTypeId(pricetype.getOID());
                                    pTypeMapping.setStandartRateId(currx.getOID());
                                }
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "</div>");
                            }
                        }
                    }
                    rowx.add("<div align=\"left\">" + unitCode + "</div>");
                }

                                
                if (typeOfBusinessDetail == 2) {
                    rowx.add("1");//qty
                    rowx.add("");//qty
                    rowx.add("");//qty
                } else {
                    rowx.add("");//qty
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add(""+kadar.getKadar());
                    rowx.add("");
                    rowx.add(""+String.format("%,.3f", md.getBerat()));
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                }
                lstData.add(rowx);
                //lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+material.getDefaultStockUnitId()+"','"+unitName);
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data barang...</div>";
        }
        return result;
    }
%>

<!-- JSP Block -->
<%//
    String materialcode = FRMQueryString.requestString(request, "mat_code");
    long materialgroup = FRMQueryString.requestLong(request, "txt_materialgroup");
    long materialsubcategory = FRMQueryString.requestLong(request, "txt_material_sub_category");
    long materialsupplier = FRMQueryString.requestLong(request, "txt_material_supplier");
//get lokasi dan oidStockOpname
    long oidLocation = FRMQueryString.requestLong(request, "location_id");
    long oidStockOpname = FRMQueryString.requestLong(request, "stock_opname_id");
//variabel for item not opname
    int notOpname = FRMQueryString.requestInt(request, "notOpname");
//data for litama
    long oidEtalase = FRMQueryString.requestLong(request, "etalase_id");
    /*
     out.println("materialsupplier : "+materialsupplier);
     out.println("materialgroup : "+materialgroup);
     out.println("materialsubcategory : "+materialsubcategory);
     */

    String materialname = FRMQueryString.requestString(request, "txt_materialname");
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int recordToGet = 0;
    String pageTitle = "Opname Barang";
    String pageTitleNotOpname = "Item belum opname pada periode ini";
    if (typeOfBusinessDetail == 2) {
        pageTitle = "STOK OPNAME";
    }

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
        vectSize = PstMaterial.getCountMaterialOpname(materialsupplier, objMaterial, oidLocation, oidStockOpname);
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
    String whereAdd = "";
    if (typeOfBusinessDetail == 2) {
        whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + "='" + oidEtalase + "'";
    }

    if (notOpname == 1) {
        //vect = PstMaterial.getListMaterialOpnameAll(start, recordToGet, orderBy, oidLocation);
        orderBy = " TRIM(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") ASC ";
        vect = PstMaterial.getListMaterialOpnameAll(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);
//add list opname not opname all
//by mirahu
//20120127
    } else if (notOpname == 2) {
        //vect = PstMaterial.getListMaterialOpnameAllNew(start, recordToGet, orderBy, oidLocation);
        orderBy = " TRIM(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") ASC ";
        vect = PstMaterial.getListMaterialOpnameAllNew(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname);
    } else {
        orderBy = " TRIM(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ") ASC ";
        if (typeOfBusinessDetail == 2) {
            orderBy = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
        }
        vect = PstMaterial.getListMaterialOpname(materialsupplier, objMaterial, start, recordToGet, orderBy, oidLocation, oidStockOpname, whereAdd);
    }

    MatStockOpname so = new MatStockOpname();
    try {
        if (oidStockOpname != 0) {
            so = PstMatStockOpname.fetchExc(oidStockOpname);
        }
    } catch (Exception ex) {
    }

    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "opname_item.xls");
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<style>
    .center {
        text-align: center;
        vertical-align: middle;
      }
</style>
</head>
<body>
    <table width="100%" border="1" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >  
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
                    <table width="100%" cellspacing="0" cellpadding="0" border="1">
                        <tr>
                            <td height="14" align="center" valign="middle" >
                                <h4><%=pageTitle%></h4>
                            </td>
                        </tr>
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">Tanggal/Jam :  </td>
                        </tr>
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">Kode Document : <%=so.getStockOpnameNumber()%> </td>
                        </tr>
                        <%
                            ContactList contactList = new ContactList();
                            Category category = new Category();
                            String categoryName = "";
                            String supplierName = "";
                            try {
                                if (so.getCategoryId() > 0) {
                                    category = PstCategory.fetchExc(so.getCategoryId());
                                    categoryName = category.getName();
                                } else {
                                    categoryName = "Semua kategori";
                                }

                                if (so.getSupplierId() > 0) {
                                    contactList = PstContactList.fetchExc(so.getSupplierId());
                                    supplierName = contactList.getCompName();
                                } else {
                                    supplierName = "Semua supplier";
                                }
                            } catch (Exception ex) {
                            }
                        %>  
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">Supplier : <%=supplierName%> </td>
                        </tr>
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">Categori : <%=categoryName%> </td>
                        </tr>
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">Keterangan : <%=so.getRemark()%> </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
<!--                            <td>&nbsp;</td>-->
                        </tr>
                        <% if (notOpname != 0 && vect.size() > 0) {%>
                        <tr>
                            <!--<td width="17%">&nbsp;</td>-->
                            <td width="83%">&nbsp;
                                <% if (notOpname == 1) {%>
                                <a href="javascript:addListNotOpname()">Add List Not Opname set Qty to 0</a>
                                <%} else if (notOpname == 2) {%>
                                <a href="javascript:addListNotOpnameAll()">Add List Not Opname</a>
                                <%}%>
                            </td>
                        </tr>
                        <%}%>
                        <%
                            Vector vectMember = new Vector(1, 1);
                            String[] strMember = null;
                            Vector listTypeHrga = new Vector();
                            strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
                            String sStrMember = "";
                            if (strMember != null && strMember.length > 0) {
                                for (int i = 0; i < strMember.length; i++) {
                                    try {
                                        if (strMember[i] != null && strMember[i].length() > 0) {
                                            vectMember.add(strMember[i]);
                                            sStrMember = sStrMember + strMember[i] + ",";
                                        }
                                    } catch (Exception exc) {
                                        System.out.println("err");
                                    }
                                }
                                if (sStrMember != null && sStrMember.length() > 0) {
                                    sStrMember = sStrMember.substring(0, sStrMember.length() - 1);
                                    String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] + " IN(" + sStrMember + ")";
                                    listTypeHrga = PstPriceType.list(0, 0, whereClauses, "");
                                }
                            }
                        %>
                        <tr>
                            <td height="22" valign="middle" align="center" colspan="3"><%=drawListMaterial(SESS_LANGUAGE, vect, start, listTypeHrga, typeOfBusinessDetail)%></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <!--<td>&nbsp;</td>-->
                        </tr>
                        <%
                            String sign1 = "PETUGAS 1";
                            String sign2 = "PETUGAS 2";
                            String sign3 = "KEPALA TOKO";
                            String sign4 = "EDP/INVENTORY";
                        %>
                        <tr>
                            <td width="100%" height="20" valign="middle" class="command">
                                <%=sign1%>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                
                                <%=sign2%>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                    
                                <%=sign3%>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <%=sign4%>
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td width="100%" height="75" valign="middle" class="command"></td>
                        </tr>
                        <tr>
                            <td width="100%" height="20" valign="middle" class="command">
                                (.................................)
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                
                                (.................................)
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                (.................................)
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                (.................................)
                            </td>
                        </tr>
                        <%--
                        <tr>
                            <td width="10%" height="20" valign="middle" class="command">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                    <tr align="left" valign="top"> 
                                        <td height="22" valign="middle" colspan="4">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                <tr align="left" valign="top"> 
                                                    <td height="40" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr> 
                                                    <td width="" align="center" nowrap>PETUGAS 1</td>  
                                                    <td width="" align="center" nowrap>PETUGAS 2</td>
                                                    <td align="" align="center" nowrap>KEPALA TOKO</td>
                                                    <td width="" align="center" nowrap>EDP/INVENTORY</td>
                                                </tr>
                                                <tr align="left" valign="top"> 
                                                    <td height="75" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr> 
                                                    <td width="" align="left" nowrap> (.................................) </td>  
                                                    <td width="" align="left" nowrap> (.................................) </td>
                                                    <td align="" align="left" nowrap> (.................................) </td>
                                                    <td width="" align="left" nowrap> (.................................) </td>
                                                </tr>
                                                <tr align="left" valign="top"> 
                                                    <td height="22" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr align="left" valign="top"> 
                                                    <td height="22" valign="middle" colspan="4">Note : Telah dicek dan dipastikan jumlah QTY sudah benar.</td>
                                                </tr>
                                            </table>   
                                        </td>
                                    </tr>
                                </table>                                                                                         
                            </td>
                        </tr>
                        --%>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</body>
</html>
