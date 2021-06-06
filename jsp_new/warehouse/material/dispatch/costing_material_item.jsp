<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@ page import="com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                 com.dimata.posbo.form.warehouse.CtrlMatCosting,
                 com.dimata.posbo.form.warehouse.FrmMatCosting,
                 com.dimata.posbo.form.warehouse.CtrlMatCostingItem,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.posbo.entity.masterdata.Costing,
                 com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Pembiayaan","Gudang","Ke Toko","Tidak ada data pembiayaan"},
	{"Costing","Warehouse","Store","No costing data available"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Tipe Biaya","Entry Stock Fisik","Consume"},
	{"No","Location","Destination","Date","Status","Remark","Supplier Invoice", "Type Of Costing","Entry Physical Stock","Consume"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Kode","Nama","Unit","Qty","HPP","Total","Harga Jual","Stock balance","Stock Fisik", "Delete", "Barcode","Berat (gr)","Keterangan"},//10
	{"No","Code","Name","Unit","Qty","Cost","Total","Sell Price","Stock balance","Physical Stock","Delete", "Barcode","Weight","Remark"}
};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

public double getPriceCost(Vector list, long oid){
    double cost = 0.00;
    if(list.size()>0){
        for(int k=0;k<list.size();k++){
            MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(k);
            if(matReceiveItem.getMaterialId()==oid){
                cost = matReceiveItem.getCost();
                break;
            }
        }
    }
    return cost;
}

/**
* this method used to maintain dfList
*/
public Vector drawListDfItem(int language,int iCommand,FrmMatCostingItem frmObject,
                             MatCostingItem objEntity,Vector objectClass,
                             long dfItemId,int start, int tranUsedPriceHpp, int enableStockFisik,boolean privShowQtyPrice, String approot, int typeOfBusinessDetail)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"3%");//no
	ctrlist.addHeader(textListOrderItem[language][1],"10%");//Sku
	ctrlist.addHeader(textListOrderItem[language][2],"40%");//Nama Barang
        //if(typeOfBusinessDetail != 2) {
            ctrlist.addHeader(textListOrderItem[language][3],"5%");//Unit
        //}
        //adding stok balance & stok fisik
        //by mirahu 30032012
        if(enableStockFisik == 1){
        ctrlist.addHeader(textListOrderItem[language][8],"5%");//Stock balance
        ctrlist.addHeader(textListOrderItem[language][9],"5%");//Stock Fisik
        }
	ctrlist.addHeader(textListOrderItem[language][4],"5%");//Qty
        if(typeOfBusinessDetail == 2) {
            ctrlist.addHeader(textListOrderItem[language][12],"5%");//Berat
        }
	if(privShowQtyPrice){
            if(tranUsedPriceHpp==0){
                ctrlist.addHeader(textListOrderItem[language][5],"10%");
            }else{
                ctrlist.addHeader(textListOrderItem[language][7],"10%");
            }
            if(typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Ongkos / Batu (Rp)","8%");//Ongkos / Batu
            }
            ctrlist.addHeader(textListOrderItem[language][6],"10%");
        }
        
        ctrlist.addHeader(textListOrderItem[language][10],"10%");
        Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	   start=0;

        double total = 0;
        double totalQty = 0;
        for(int i=0; i<objectClass.size(); i++)
	{
		 Vector temp = (Vector)objectClass.get(i);
		 MatCostingItem dfItem = (MatCostingItem)temp.get(0);
		 Material mat = (Material)temp.get(1);
		 Unit unit = (Unit)temp.get(2);
		 rowx = new Vector();
		 start = start + 1;
		 totalQty = totalQty + dfItem.getQty();
		 
                //double cost = getPriceCost(listItem,dfItem.getMaterialId());

                //for litama
            Category category = new Category();
            Color color = new Color();
            Material newMat = new Material();

            try{
                unit = PstUnit.fetchExc(mat.getDefaultStockUnitId());//PstUnit.fetchExc(material.getBuyUnitId());
                newMat = PstMaterial.fetchExc(mat.getOID());
                category = PstCategory.fetchExc(newMat.getCategoryId());
                color = PstColor.fetchExc(newMat.getPosColor());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            String itemName = mat.getName();
            if (typeOfBusinessDetail == 2) {
                if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newMat.getName();
                } else if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newMat.getName();
                }
            }

		if (dfItemId == dfItem.getOID()) index = i;
		if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
		{
			rowx.add(""+start);
			rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+dfItem.getMaterialId()+
				"\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
			rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+itemName+"\" class=\"hiddenText\"  readOnly>");
                        //if(typeOfBusinessDetail != 2) {
                            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+dfItem.getUnitId()+
                                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\"  readOnly>");
                        //}
                        //adding stock balance and stock 
                        //by mirahu 31032012
                        if(enableStockFisik == 1){
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BALANCE_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getBalanceQty(),1)+"\" class=\"formElemen\" class=\"hiddenText\" style=\"text-align:right\" readOnly></div>");
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_RESIDUE_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getResidueQty(),1)+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:calculateStok()\"></div>");
                            if(privShowQtyPrice){
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"\" class=\"hiddenText\" style=\"text-align:right\"></div>");
                            }else{
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"\" class=\"hiddenText\" style=\"text-align:right\"></div>"
                                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp(),1)+"\" class=\"hiddenText\" readOnly></div>");
                            }
                        } else {
                            if(privShowQtyPrice){
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:calculate()\"></div>");
                            }else{
                                 rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:calculate()\"></div>"
                                         + "<div align=\"right\"><input type=\"hidden\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp(),1)+"\" class=\"hiddenText\" readOnly></div>");
                            }
                        }
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_WEIGHT]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getWeight(),1)+"\" class=\"hiddenText\" readOnly></div>");
                        }
                        if(privShowQtyPrice){
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp(),1)+"\" class=\"hiddenText\" readOnly></div>");
                            if(typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getCost(),1)+"\" class=\"formElemen hitungtotal\"></div>");
                            }
                            double xtotal=(dfItem.getHpp() * dfItem.getQty()) + dfItem.getCost();
                            rowx.add("<div align=\"right\" id=\"totalHtml\">"+FRMHandler.userFormatStringDecimal(xtotal,1)+"</div>");
                            total += (dfItem.getHpp() * dfItem.getQty()) + dfItem.getCost();
                        }
        }else{
                     
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(itemName);
            //if(typeOfBusinessDetail != 2) {
                rowx.add(unit.getCode());
            //}

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                int cnt = PstCostingStockCode.getCount(where);
                if(cnt<dfItem.getQty()){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty costing");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"</div>");
            }else{
                //adding stock balance and stock 
                //by mirahu 31032012
                if(enableStockFisik == 1){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getBalanceQty(),1)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getResidueQty(),1)+"</div>");
                }
                double qty=0;
                if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){
                    qty=dfItem.getQtyComposite();
                }else{
                    qty=dfItem.getQty();
                }
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qty,1)+"</div>");
                if(typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getWeight(),1)+"</div>");
                }
            }
            if(privShowQtyPrice){
                double xtotal = 0;
                double hpp=0;
                if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE){
                    //cari total hpp
                    xtotal = dfItem.getTotalHppComposite();
                    total += dfItem.getTotalHppComposite();
                    hpp = xtotal/dfItem.getQtyComposite();
                    
                }else{
                    
                    total += (dfItem.getHpp() * dfItem.getQty()) + dfItem.getCost();
                    xtotal = (dfItem.getHpp() * dfItem.getQty()) + dfItem.getCost();
                    hpp=dfItem.getHpp();
                    
                } 
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(hpp,1)+"</div>");
                if(typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getCost(),1)+"</div>");
                }
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(xtotal,1)+"</div>");
               
            }
	  }
          // add by fitra 17-05-2014     
          rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
          lstData.add(rowx);
	}

	rowx = new Vector();
	if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)){
		rowx.add(""+(start+1));
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
		//rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
                rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" onKeyDown=\"javascript:keyDownCheck(event)\" value=\""+""+"\" class=\"formElemen\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
                //if(typeOfBusinessDetail != 2) {
                    rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" class=\"hiddenText\" value=\""+""+
                            "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
                //}
                //adding stock balance and stock 
                //by mirahu 31032012
                if(enableStockFisik == 1){
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BALANCE_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" class=\"hiddenText\" readOnly>&nbsp;</div>");
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_RESIDUE_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:calculateStok(event)\"></div>");
                    if(privShowQtyPrice){
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"hiddenText\" onkeyup=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_WEIGHT]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                        }
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen hitungtotal\"></div>");
                        }
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"sub_total\" id=\"subTotal\" value=\"\" class=\"hiddenText\" readOnly></div>");
                    }else{
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"hiddenText\" onkeyup=\"javascript:calculate(event)\" style=\"text-align:right\"></div>"
                                + (typeOfBusinessDetail == 2 ? "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_WEIGHT]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>" : "")
                                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>"
                                + (typeOfBusinessDetail == 2 ? "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen hitungtotal\" ></div>" : "")
                                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"sub_total\" id=\"subTotal\" value=\"\" class=\"hiddenText\" readOnly></div>");
                    }
                }else {
                    if(privShowQtyPrice){
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\"  onKeyUp=\"javascript:calculate(event)\"></div>");
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_WEIGHT]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                        }
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen hitungtotal\" ></div>");
                        }
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"sub_total\" id=\"subTotal\" value=\"\" class=\"hiddenText\" readOnly></div>");
                    }else{
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:calculate(event)\"></div>"
                                + (typeOfBusinessDetail == 2 ? "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_WEIGHT]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>" : "")
                                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" id=\"hpp\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>"
                                + (typeOfBusinessDetail == 2 ? "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen hitungtotal\" ></div>" : "")
                                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"sub_total\" id=\"subTotal\" value=\"\" class=\"hiddenText\" readOnly></div>");
                    }
                }
                
                rowx.add("");
		lstData.add(rowx);
	}
    if(enableStockFisik == 1){
        rowx = new Vector(1,1);
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">Total : </div>");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total,1)+"</b></div>");
        lstData.add(rowx);
    }
    list.add(ctrlist.draw());
	list.add(listError);
	return list;
}

public Vector drawListDfCompositeItem(int language,int iCommand,FrmMatCostingItem frmObject,
                             MatCostingItem objEntity,Vector objectClass,
                             long dfItemId,int start, int tranUsedPriceHpp, int enableStockFisik,boolean privShowQtyPrice, String approot)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"3%");//No
	ctrlist.addHeader(textListOrderItem[language][1],"10%");//Sku
	ctrlist.addHeader(textListOrderItem[language][2],"40%");//Nama Barang
	ctrlist.addHeader(textListOrderItem[language][3],"5%");
        //adding stok balance & stok fisik
        //by mirahu 30032012
        if(enableStockFisik == 1){
        ctrlist.addHeader(textListOrderItem[language][8],"5%");
        ctrlist.addHeader(textListOrderItem[language][9],"5%");
        }
	ctrlist.addHeader(textListOrderItem[language][4],"5%");
	if(privShowQtyPrice){
            if(tranUsedPriceHpp==0){
                ctrlist.addHeader(textListOrderItem[language][5],"10%");
            }else{
                ctrlist.addHeader(textListOrderItem[language][7],"10%");
            }
            ctrlist.addHeader(textListOrderItem[language][6],"10%");
        }
        
        ctrlist.addHeader(textListOrderItem[language][10],"10%");
        Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	   start=0;

        double total = 0;
        double totalQty = 0;
        for(int i=0; i<objectClass.size(); i++)
	{
		 Vector temp = (Vector)objectClass.get(i);
		 MatCostingItem dfItem = (MatCostingItem)temp.get(0);
		 Material mat = (Material)temp.get(1);
		 Unit unit = (Unit)temp.get(2);
		 rowx = new Vector();
		 start = start + 1;
		 totalQty = totalQty + dfItem.getQty();
		 
                //double cost = getPriceCost(listItem,dfItem.getMaterialId());

		if (dfItemId == dfItem.getOID()) index = i;
                     
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(mat.getName());
            rowx.add(unit.getCode());

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                int cnt = PstCostingStockCode.getCount(where);
                if(cnt<dfItem.getQty()){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty costing");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"</div>");
            }else{
                //adding stock balance and stock 
                //by mirahu 31032012
                if(enableStockFisik == 1){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getBalanceQty(),1)+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getResidueQty(),1)+"</div>");
               }
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"</div>");
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp(),1)+"</div>");
                double xtotal= dfItem.getQty() * dfItem.getHpp();
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(xtotal,1)+"</div>");
                total += dfItem.getHpp() * dfItem.getQty();
            }
            // add by fitra 17-05-2014     
          rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
		lstData.add(rowx);
	}
    if(enableStockFisik == 1){
        rowx = new Vector(1,1);
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">Total : </div>");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total,1)+"</b></div>");
        lstData.add(rowx);
    }
    list.add(ctrlist.draw());
	list.add(listError);
	return list;
}

%>


<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request,"start");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidCostingMaterial = FRMQueryString.requestLong(request,"hidden_costing_id");
long oidCostingMaterialItem = FRMQueryString.requestLong(request,"hidden_costing_item_id");
int enableStockBalance = FRMQueryString.requestInt(request, "enable_stock_balance");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String costingCode = ""; //i_pstDocType.getDocCode(docType);
String costingTitle = "Costing Barang"; //i_pstDocType.getDocTitle(docType);
String costingItemTitle = costingTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


/**
* process on df main
*/
CtrlMatCosting ctrlMatCosting = new CtrlMatCosting(request);
iErrCode = ctrlMatCosting.action(Command.EDIT,oidCostingMaterial,userName,userId);
FrmMatCosting frmMatCosting = ctrlMatCosting.getForm();
MatCosting costing = ctrlMatCosting.getMatCosting();

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(costing.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = true;

ControlLine ctrLine = new ControlLine();
CtrlMatCostingItem ctrlMatCostingItem = new CtrlMatCostingItem(request);
ctrlMatCostingItem.setLanguage(SESS_LANGUAGE);

//by dyas - 20131126
//tambah variabel userName dan userId
iErrCode = ctrlMatCostingItem.action(iCommand,oidCostingMaterialItem,oidCostingMaterial, userName, userId);
FrmMatCostingItem frmMatCostingItem = ctrlMatCostingItem.getForm();
MatCostingItem costingItem = ctrlMatCostingItem.getMatCostingItem();
msgString = ctrlMatCostingItem.getMessage();

String whereClauseItem = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+"="+oidCostingMaterial+
                         " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";

String orderClauseItem = "";
int vectSizeItem = PstMatCostingItem.getCount(whereClauseItem);
int recordToGetItem = 500;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	start = ctrlMatCostingItem.actionList(iCommand,start,vectSizeItem,recordToGetItem);
}

String whereClauseItemx = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
Vector listMatCostingItem = PstMatCostingItem.list(start,recordToGetItem,oidCostingMaterial,whereClauseItemx);

String whereClauseItemComposite = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"!=0";
Vector listMatCostingItemComposite = PstMatCostingItem.list(start,recordToGetItem,oidCostingMaterial,whereClauseItemComposite);

if(listMatCostingItem.size()<1 && start>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		start = start - recordToGetItem;
	 }
	 else
	 {
		start = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST;
	 }
	 listMatCostingItem = PstMatCostingItem.list(start,recordToGetItem,oidCostingMaterial);
}

if(iCommand==Command.SAVE && iErrCode == 0) {
		iCommand = Command.ADD;
        oidCostingMaterialItem=0;
}


// add by fitra 17-05-2014
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function main(oid,comm){
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.hidden_costing_id.value=oid;
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}
function cmdAdd()
{
	document.frm_matdispatch.hidden_costing_item_id.value="0";
	document.frm_matdispatch.command.value="<%=Command.ADD%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	if(compareDateForAdd()==true)
		document.frm_matdispatch.submit();
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_costing_item_id.value=oid;
    document.frm_matdispatch.action="costing_stockcode.jsp";
    document.frm_matdispatch.submit();
}

function cmdEdit(oidCostingMaterialItem)
{
	document.frm_matdispatch.hidden_costing_item_id.value=oidCostingMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdAsk(oidCostingMaterialItem)
{
	document.frm_matdispatch.hidden_costing_item_id.value=oidCostingMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.ASK%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdSave(){
	document.frm_matdispatch.command.value="<%=Command.SAVE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oidCostingMaterialItem)
{
	document.frm_matdispatch.hidden_costing_item_id.value=oidCostingMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

// add by fitra 17-05-2014
function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid) ;
else
return cmdEdit(oid);
}

function cmdCancel(oidCostingMaterialItem)
{
	document.frm_matdispatch.hidden_costing_item_id.value=oidCostingMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack()
{
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}


function sumPrice()
{
}

function cmdCheck()
{
	var strvalue  = "costingdosearch.jsp?command=<%=Command.FIRST%>"+
                                        "&location_id=<%=costing.getLocationId()%>"+
					"&hidden_costing_id=<%=oidCostingMaterial%>"+
					"&mat_code="+document.frm_matdispatch.matCode.value+
                                        "&costing_date=<%=costing.getCostingDate()%>"+
                                        "&txt_materialname="+document.frm_matdispatch.matItem.value+
                                        "&enable_stock_fisik=<%=costing.getEnableStockFisik()%>";
	window.open(strvalue,"material", "height=600,width=700,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function calculate(evenClick){
  var qty = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
   if(qty<0.0000){

          document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=0;
          return;
        }
        
        
        
        if (evenClick.keyCode == 13) {
        cmdSave();
}
}


function calculateStok(e){
  var qtyBalance = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_BALANCE_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
  var qtyFisik = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_RESIDUE_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
   if(qtyFisik<0.0000){

          document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=0;
          return;
        }
   
        var qty = qtyBalance - qtyFisik;
        if(isNaN(qty)){
            qty = "0";
        }
        document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=(parseFloat(qty));
        
        if (e.keyCode == 13){
            
            cmdSave();
        }
        //document.frm_matdispatch.total_qty.value = (parseFloat(qty));
}

function cmdListFirst()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListPrev()
{
	document.frm_matdispatch.command.value="<%=Command.PREV%>";
	document.frm_matdispatch.prev_command.value="<%=Command.PREV%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListNext()
{
	document.frm_matdispatch.command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.prev_command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListLast()
{
	document.frm_matdispatch.command.value="<%=Command.LAST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.LAST%>";
	document.frm_matdispatch.action="costing_material_item.jsp";
	document.frm_matdispatch.submit();
}



function keyDownCheck(e){
  var trap = document.frm_matdispatch.trap.value;
   
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_matdispatch.trap.value="1";
  
   }
   
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matdispatch.matItem.value == "" ){
        document.frm_matdispatch.trap.value="0";
        cmdCheck();
   }
   if (e.keyCode == 13 && trap==1) {
       document.frm_matdispatch.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matdispatch.txt_materialname.value="";
   } 
}


function cmdBackList()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="costing_material_list.jsp";
	document.frm_matdispatch.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------



//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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


<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
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
          <!--<td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
           <!--&nbsp;Gudang &gt; Costing Barang &gt; Ke Toko <!-- #EndEditable --><!--</td>-->
           <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
           &nbsp;<%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable -->

        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matdispatch" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start" value="<%=start%>">
	      <input type="hidden" name="hidden_costing_id" value="<%=oidCostingMaterial%>">
              <input type="hidden" name="hidden_costing_item_id" value="<%=oidCostingMaterialItem%>">
              <input type="hidden" name="<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_COSTING_MATERIAL_ID]%>" value="<%=oidCostingMaterial%>">
              <input type="hidden" name="enable_stock_balance" value ="<%=enableStockBalance%>">
              
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              
                 <input type="hidden" name="trap" value="">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" class="title">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="right">
                        <td width="11%" align="right" valign="top">                      
                      </tr>
                      <tr>
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="29%" align="left">: <b><%=costing.getCostingCode()%></b>
                        </td>
                        <td width="9%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][7]%> :
                          <td width="11%"  align="left" valign="top">
                              <%
                                                            Vector obj_costingid = new Vector(1,1);
                                                            Vector val_costingid = new Vector(1,1);
                                                            Vector key_costingid = new Vector(1,1);
                                                            //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                            Vector vt_cost = PstCosting.list(0,0,"", PstCosting.fieldNames[PstCosting.FLD_NAME]);
                                                            for(int d=0;d<vt_cost.size();d++) {
                                                                    Costing cost = (Costing)vt_cost.get(d);
                                                                    val_costingid.add(""+cost.getOID()+"");
                                                                    key_costingid.add(cost.getName());
                                                            }
                                                            String select_costingid = ""+costing.getCostingId(); //selected on combo box
                            %>
                            <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_ID], null, select_costingid, val_costingid, key_costingid, "disabled=\"true\"", "formElemen")%>

                          </td>
                    
                          <td width="16%" align="right"> <%=textListOrderHeader[SESS_LANGUAGE][5]%> :
                          <td width="25%" rowspan="4" align="right" valign="top">
                          <textarea name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3" disabled="true"><%=costing.getRemark()%></textarea>                                                                        
                      </tr>
                      <tr>
                        <td width="10%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="29%" align="left" valign="bottom">: <%=ControlDate.drawDateWithStyle(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], (costing.getCostingDate()==null) ? new Date() : costing.getCostingDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                        <!--td width="9%" align="right"-->
                      <!-- add enable stok fisik -->
                        <!-- by mirahu 29032012 -->
                        <td width="9%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][9]%> :
                          <td width="11%"  align="left" valign="top">
                              <%
                                 Vector val_contactid = new Vector(1,1);
                                 Vector key_contactid = new Vector(1,1);
                                 Vector vt_contact = PstEmployee.list(0,0,"", PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
                                for(int d=0;d<vt_contact.size();d++) {
                                        Employee cost = (Employee)vt_contact.get(d);
                                        val_contactid.add(""+cost.getOID()+"");
                                        key_contactid.add(cost.getFullName());
                                }
                                String select_contactid = ""+costing.getContactId(); //selected on combo box
                            %>
                            <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_ID], null, select_contactid, val_contactid, key_contactid, "disabled=\"true\"", "formElemen")%>
                          </td>
                      </tr>
                      <tr>
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="29%" align="left">:
                          <%
                            Vector obj_locationid = new Vector(1,1);
                            Vector val_locationid = new Vector(1,1);
                            Vector key_locationid = new Vector(1,1);
                            //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,"");
                            Vector vt_loc = PstLocation.list(0,0,"","");
                            for(int d=0;d<vt_loc.size();d++) {
                                    Location loc = (Location)vt_loc.get(d);
                                    val_locationid.add(""+loc.getOID()+"");
                                    key_locationid.add(loc.getName());
                            }
                            String select_locationid = ""+costing.getLocationId(); //selected on combo box
                        %>
                        <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%> </td>
                        <td width="12%" align="right" valign="bottom"></td>
			<td> <input type="checkbox" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_ENABLE_STOCK_FISIK]%>" value="1" <% if(costing.getEnableStockFisik()==1){%>checked<%}%> disabled=\"true\">&nbsp;&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                      </tr>
                      <tr>
                        <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td align="left">:
                          <%
							Vector obj_locationid1 = new Vector(1,1);
							Vector val_locationid1 = new Vector(1,1);
							Vector key_locationid1 = new Vector(1,1);
							//Vector vt_loc1 = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE,"");
							Vector vt_loc1 = PstLocation.list(0,0,"","");
							for(int d = 0; d < vt_loc1.size(); d++)	{
								Location loc1 = (Location)vt_loc1.get(d);
								val_locationid1.add(""+loc1.getOID()+"");
								key_locationid1.add(loc1.getName());
							}
							String select_locationid1 = ""+costing.getCostingTo(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_TO], null, select_locationid1, val_locationid1, key_locationid1, "disabled=\"true\"", "formElemen")%> </td>
                        <td align="right">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top">
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr>
                        <td colspan="3" >
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top">
                              <%
							  Vector listError = new Vector();
							  try
								{
								%>
                              <td height="22" valign="middle" colspan="3"> <%

							  Vector list = drawListDfItem(SESS_LANGUAGE,iCommand,frmMatCostingItem, costingItem,listMatCostingItem,oidCostingMaterialItem,start,tranUsedPriceHpp, costing.getEnableStockFisik(),privShowQtyPrice,approot,typeOfBusinessDetail);
								out.println(""+list.get(0));
								listError = (Vector)list.get(1);
							  %> </td>
                              <%
								}
								catch(Exception e)
								{
									System.out.println(e);
								}
								%>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="8" align="left" colspan="3" class="command">
                                <span class="command">
                                <%
								int cmd = 0;
								if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST){
									cmd =iCommand;
								}else{
								    if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
								    else
										cmd =prevCommand;
							    }
                                ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,start,recordToGetItem));
								%>
                                </span> </td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="22" valign="middle" colspan="3"><span class="errfont">
                                <%
				  	for(int k=0;k<listError.size();k++){
						if(k==0)
							out.println(listError.get(k)+"<br>");
						else
							out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
					}
				  %>
                              </span></td>
                            </tr>
                            
                            <tr align="left" valign="top">
                              <td height="22" valign="middle" colspan="3">
                                <%
								ctrLine.setLocationImg(approot+"/images");

								// set image alternative caption
								ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ASK,true));
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_CANCEL,false));

								ctrLine.initDefault();
								ctrLine.setTableWidth("65%");
								String scomDel = "javascript:cmdAsk('"+oidCostingMaterialItem+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidCostingMaterialItem+"')";
								String scancel = "javascript:cmdEdit('"+oidCostingMaterialItem+"')";
								ctrLine.setCommandStyle("command");
								ctrLine.setColCommStyle("command");

								// set command caption
								ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ASK,true));
								ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_DELETE,true));
								ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_CANCEL,false));


								if (privDelete){
									ctrLine.setConfirmDelCommand(sconDelCom);
									ctrLine.setDeleteCommand(scomDel);
									ctrLine.setEditCommand(scancel);
								}else{
									ctrLine.setConfirmDelCaption("");
									ctrLine.setDeleteCaption("");
									ctrLine.setEditCaption("");
								}

								if(privAdd == false  && privUpdate == false && costing.getCostingStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
									ctrLine.setSaveCaption("");
								}

								if (privAdd == false){
									ctrLine.setAddCaption("");
								}

								String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
								if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
								%>
                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                    <% if(costing.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
									<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
									<% } %>
									<td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
                                  </tr>
                                </table>
                                <%
								}else{
									out.println(strDrawImage);
								}
								%>
                              </td>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="22" valign="middle" colspan="3">
                                    &nbsp;
                                </td> 
                            </tr>
                            <tr align="left" valign="top">
                                <td height="22" valign="left" colspan="3">
                                    <b>Detail Component</b>
                                </td> 
                            </tr>
                            <tr align="left" valign="top">
                              <%
                                try
                                {
                                %>
                                <td height="22" valign="middle" colspan="3"> 
                                    <%
                                    Vector list = drawListDfCompositeItem(SESS_LANGUAGE,iCommand,frmMatCostingItem, costingItem,listMatCostingItemComposite,oidCostingMaterialItem,start,tranUsedPriceHpp, costing.getEnableStockFisik(),privShowQtyPrice,approot);
                                    out.println(""+list.get(0));
                                    %> 
                                </td>
                                <%
                                }
                                catch(Exception e)
                                {
                                System.out.println(e);
                                }
                                %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="3">
                        </td>
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
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_matdispatch.matItem.focus();
       
    <% } %>
</script>

<script language="JavaScript">
         // add By Fitra
         var trap = document.frm_matdispatch.trap.value;       
         document.frm_matdispatch.trap.value="0";
         document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
		$('.hitungtotal').keyup(function () {
            var hpp = cleanNumberInt($('#hpp').val(),guiDigitGroup);
            var ongkos = cleanNumberInt($(this).val(),guiDigitGroup);
            var totalHp = +hpp + +ongkos;
            $('#subTotal').val(totalHp);
			$('#totalHtml').html(totalHp);
        });
	});
</script>

<!-- #EndTemplate --></html>

