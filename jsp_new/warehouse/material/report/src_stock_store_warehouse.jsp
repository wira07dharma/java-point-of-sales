<%-- 
    Document   : src_stock_store_warehouse
    Created on : Jan 15, 2019, 10:56:01 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.MatMappKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMatMappKsg"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.PriceTypeMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.pos.entity.balance.PstCashCashier"%>
<%@page import="com.dimata.pos.entity.balance.CashCashier"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialMappingType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_SEARCH);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public String drawList(Vector objectClass, long locationId, long parentLocationId) {
        //2
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%"); //untuk mengatur width(lebar) table
        ctrlist.setAreaStyle("listgen"); //untuk mengatur nama class table
        ctrlist.setTitleStyle("tableheader"); //untuk mengatur nama class didalam kolom dalam baris table
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); //mengatur nama class baris table

        ctrlist.addHeader("No");
        ctrlist.addHeader("SKU");
        ctrlist.addHeader("Barcode");
        ctrlist.addHeader("Name");
        ctrlist.addHeader("Brand");
        ctrlist.addHeader("Category");
        ctrlist.addHeader("Sub Category");
        ctrlist.addHeader("Size");
        ctrlist.addHeader("Color");
        ctrlist.addHeader("Price");
        ctrlist.addHeader("Disc%");
        ctrlist.addHeader("Qty");
        ctrlist.addHeader("Gudang");
        ctrlist.addHeader("Rak");

        //membuat link dirow 0
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        //membuat link menuju ke edit

        ctrlist.reset();

        int index = -1;

        Vector rowx = new Vector(1, 1);
        double grandTotal = 0;
        String addInner = "";
        String addClass = "";
        int specialItem = 0;
        String fieldSpecial = "";

        if (objectClass.size() > 0) {
            double subTotalQtyStockStore = 0;
            double subTotalQtyStockWareHouse = 0;
            for (int i = 0; i < objectClass.size(); i++) {
                try {
                    Vector temp = (Vector) objectClass.get(i);
                    Material material = (Material) temp.get(0);
                    Merk merk = (Merk) temp.get(1);
                    Category cat = (Category) temp.get(2);
                    SubCategory subCat = (SubCategory) temp.get(3);
                    PriceTypeMapping map = (PriceTypeMapping) temp.get(4);

                    rowx = new Vector(1, 1);

                    Color color = new Color();
                    try {
                        color = PstColor.fetchExc(material.getPosColor());
                    } catch (Exception exc) {
                    }

                    long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, Long.valueOf(material.getMaterialId()));
                    long oidPromotion = PstMaterialMappingType.getSelectedTypeId(5, Long.valueOf(material.getMaterialId()));
                    String size = "-";
                    String promotion = "-";

                    try {
                        MasterType type = PstMasterType.fetchExc(oidMappingSize);
                        size = type.getMasterName();
                    } catch (Exception exc) {
                    }

                    try {
                        MasterType type = PstMasterType.fetchExc(oidPromotion);
                        promotion = type.getMasterName();
                    } catch (Exception exc) {
                    }

                    double qtyStockStockCardStore = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(Long.valueOf(material.getMaterialId()), locationId, new Date(), 0);
                    //double qtyStockRealTimeStore = SessMatCostingStokFisik.qtyMaterialBasedOnOpeningCashier(Long.valueOf(material.getMaterialId()), new Date());
                    double qtyStockStore = qtyStockStockCardStore;// - qtyStockRealTimeStore;
                    subTotalQtyStockStore += qtyStockStore;

                    double qtyStockStockCardWH = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(Long.valueOf(material.getMaterialId()), parentLocationId, new Date(), 0);
                    subTotalQtyStockWareHouse += qtyStockStockCardWH;
                    
                    String whereEtalase = "KSG.LOCATION_ID=" + parentLocationId + " AND MAP." + PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID] + "=" + material.getMaterialId();
                    Vector listEtalaseLoc = PstMatMappKsg.listJoinKsg(0, 0, whereEtalase, "");
                    String rak = "";
                    if (listEtalaseLoc.size() > 0) {
                        MatMappKsg mapKsg = (MatMappKsg) listEtalaseLoc.get(0);
                        Ksg etalase = new Ksg();
                        try {
                            etalase = PstKsg.fetchExc(mapKsg.getKsgId());
                            rak = etalase.getName();
                        } catch (Exception exc) {
                        }
                    }

                    rowx.add("" + (i + 1) + ".");
                    rowx.add("" + material.getSku());
                    rowx.add("" + material.getBarcode());
                    rowx.add("" + material.getName());
                    rowx.add("" + merk.getName());
                    rowx.add("" + cat.getName());
                    rowx.add("" + subCat.getName());
                    rowx.add("" + size);
                    rowx.add("" + color.getColorName());
                    rowx.add("" + map.getPrice());
                    rowx.add("0");
                    rowx.add("" + qtyStockStore);
                    rowx.add("" + qtyStockStockCardWH);
                    rowx.add("" + rak);
                    lstData.add(rowx);
                } catch (Exception exc) {
                    System.out.println(exc.toString());
                }
            }
            //ADDED BY DEWOK 20190903 FOR SUBTOTAL
            rowx = new Vector(1, 1);
            rowx.add("");
            rowx.add("<b>TOTAL</b>");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("<b>" + subTotalQtyStockStore + "</b>");
            rowx.add("<b>" + subTotalQtyStockWareHouse + "</b>");
            rowx.add("");
            lstData.add(rowx);
        }

        return ctrlist.drawBootstrapStripted();
    }
%>

<%//
    int searchType = FRMQueryString.requestInt(request, "filter");
    int iCommand = FRMQueryString.requestCommand(request);
    int sameStyle = FRMQueryString.requestInt(request, "sameStyle");
    String searchValue = FRMQueryString.requestString(request, "FRM_FIELD_ITEM_NAME");
    long idLocation = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION");

    Vector listMaterial = new Vector();
    Location location = new Location();
    
    if (iCommand == Command.NONE) {
        sameStyle = 2;
    }
    
    if (iCommand == Command.LIST) {
        if (searchType == 1) {
            Material material = new Material();
            try {
                material = PstMaterial.fetchBySkuBarcode(searchValue);
                location = PstLocation.fetchExc(idLocation);
            } catch (Exception exc) {
            }
            long oidMappingStyle = PstMaterialMappingType.getSelectedTypeId(4, material.getOID());

            String style = "";
            try {
                MasterType type = PstMasterType.fetchExc(oidMappingStyle);
                style = type.getMasterName();
            } catch (Exception exc) {
            }
            if (sameStyle > 0) {
                listMaterial = PstMaterial.listJoinMasterType(4, style, location.getStandarRateId(), location.getPriceTypeId(), sameStyle, material.getPosColor());
            } else {
                listMaterial = PstMaterial.listJoinMasterType(4, searchValue, location.getStandarRateId(), location.getPriceTypeId(), 1, material.getPosColor());
            }
        } else if (searchType == 2) {
            listMaterial = PstMaterial.listJoinMasterType(4, searchValue, location.getStandarRateId(), location.getPriceTypeId(), searchType,0);
        }
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ProChain</title>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />

        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>

        <style>
            table {font-size: 12px}
            #example1 th {background-color: #EEE; white-space: nowrap}
            #example1 tr:hover {background-color: yellow}
        </style>
    </head>
    <body style="background-color: #EEEEEE">
        <div class="content-header">
            <h1>Pencarian Stok</h1>
        </div>
        <br>
        <div class="col-md-12">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <form id="formSearch" class="form-inline" name="frm" method="post" style="margin-bottom: 0px">
                        <label for="rg-from">Filter By: </label>
                        <div class="form-group">
                            <input type="hidden" name="command" value="<%=Command.LIST%>">

                            <select id='filterBy' name="filter" class='form-control input-sm'>
                                <option <%= (searchType == 1) ? "selected" : ""%> value="1">Barcode</option>
                                <option <%= (searchType == 2) ? "selected" : ""%>  value="2">Style</option>
                            </select>

                            <input id="inputSearch" type="text" required="" name="FRM_FIELD_ITEM_NAME" class="form-control input-sm itemsearch" value="<%= ""%>">

                            <select id="filterLocation" class="form-control input-sm" name="FRM_FIELD_LOCATION">
                                <%
                                    Vector<Location> listLocation = PstLocation.listLocationStore(0, 0, null, PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                    for (Location l : listLocation) {
                                        String selected = (l.getOID() == idLocation) ? "selected" : "";
                                        out.print("<option " + selected + " value='" + l.getOID() + "'>" + l.getName() + "</option>");
                                    }
                                %>
                            </select>

                            <button id="btnSearch" type="submit" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Search</button>

                            <label class="radio-inline"><input type="radio" value="2" name="sameStyle" <%=(sameStyle == 2 ? "checked" : "")%>> Barcode Same Style</label>
                            <label class="radio-inline"><input type="radio" value="1" name="sameStyle" <%=(sameStyle == 1 ? "checked" : "")%>> Barcode Same Style & Color</label>
                        </div>
                    </form>
                </div>

                <% if (iCommand == Command.LIST) { %>
                <div class="box-body" id="CONTENT_LOAD">
                    <%
                        if (listMaterial.size() > 0) {
                            out.println(drawList(listMaterial, location.getOID(), location.getParentLocationId()));
                        } else {
                    %>
                    <h5>No Item Found</h5>
                    <%
                        }
                    %>
                </div>
                <% } %>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                function setFocus() {
                    $('.itemsearch').focus();
                }
                setFocus();
                
                $('#inputSearch').attr('placeholder',$('option:selected', '#filterBy').text());
                $('#inputSearch').focus();
                
                $('#filterBy').change(function () {
                    $('#inputSearch').attr('placeholder',$('option:selected', '#filterBy').text());
                    setFocus();
                });
                
                $('#filterLocation').change(function () {
                    setFocus();
                });
                
                $('.radio-inline').click(function () {
                    setFocus();
                });
                
                $('#formSearch').submit(function() {
                    $('#btnSearch').attr({'disabled':true}).html('<i class="fa fa-spinner fa-pulse"></i> Tunggu...');
                });
            });
            
            $('#example1').removeClass('table-striped');
            $('#example1 tr:last').css({
                'background-color':'#EEE',
                'font-weight':'bold'
            });
            
        </script>
    </body>
</html>
