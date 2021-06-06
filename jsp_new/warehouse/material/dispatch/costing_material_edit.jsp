<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialComposit"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<%@ page import="com.dimata.posbo.form.warehouse.CtrlMatCosting,
                 com.dimata.posbo.form.warehouse.FrmMatCosting,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatDispatch,
                 com.dimata.posbo.entity.masterdata.Costing,
                 com.dimata.posbo.entity.masterdata.PstCosting"%>
				 
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!

public static final String textListGlobal[][] = {
	{"Pembiayaan","Gudang","Ke Toko","Tidak ada data pembiayaan","Posting Stock","Posting Harga Beli"},
	{"Costing","Warehouse","Store","No costing data available","Posting Stock","Posting Harga Beli"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Waktu", "Tipe Biaya","Entry Stock Fisik","Consume", "Tipe Item Pembiayaan"},
	{"No","Location","Destination","Date","Status","Remark","Supplier Invoice","Time","Type of Costing","Entry Physical Stock","Costing Item Type"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Qty","HPP (Rp)","Total (Rp)","Harga Jual","Brand","Stock balance","Stock Fisik", "Hapus" ,"Konsumsi", "Barcode","Berat (gr)","Keterangan"},//11
	{"No","Code","Name","Unit","Qty","Cost","Total","Harga Jual","Brand","Stock balance","Physical Stock","Delete" ,"Consume", "Barcode","Weight","Remark"}
};

public static final String textPosting[][] = {
    {"Anda yakin melakukan Posting Stok ?","Anda yakin melakukan Posting Harga ?"},
    {"Are You Sure to Posting Stock ? ","Are You Sure to Posting Cost Price?"}
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
* this method used to list all po item
*/
public Vector drawListOrderItem(int language,Vector objectClass,int start, String invoiceNumber, int tranUsedPriceHpp, int enableBalanceStock, boolean privShowQtyPrice,boolean privManageData, String approot, int typeOfBusinessDetail)
{
    Vector listError = new Vector(1,1);
    Vector list = new Vector(1,1);
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
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
            if(typeOfBusinessDetail != 2) {
                ctrlist.addHeader(textListOrderItem[language][3],"5%");//Unit
            }
            //adding stok balance & stok fisik
            //by mirahu 30032012
            if(enableBalanceStock == 1){
            ctrlist.addHeader(textListOrderItem[language][9],"5%");//Stock balance
            ctrlist.addHeader(textListOrderItem[language][10],"5%");//Stock Fisik
           }
            //end of adding stok balance & stok fisik
            ctrlist.addHeader(textListOrderItem[language][4],"5%");//Qty
            if(typeOfBusinessDetail == 2) {
                ctrlist.addHeader(textListOrderItem[language][14],"5%");//Berat
            }
            if(privShowQtyPrice){
                
                if(tranUsedPriceHpp==0){
                ctrlist.addHeader(textListOrderItem[language][5],"10%");//HPP
                }else{
                ctrlist.addHeader(textListOrderItem[language][7],"10%");//Harga Jual
                }
                if(typeOfBusinessDetail == 2) {
                    ctrlist.addHeader("Ongkos / Batu (Rp)","8%");//Ongkos / Batu
                }
                ctrlist.addHeader(textListOrderItem[language][6],"10%");//Total
            }
            ctrlist.addHeader(textListOrderItem[language][11],"10%");//Hapus
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1,1);
            ctrlist.reset();
            int index = -1;
            if(start<0)
            start=0;
            
        double total = 0;
        for(int i=0; i<objectClass.size(); i++)
        {
                 Vector temp = (Vector)objectClass.get(i);
                 MatCostingItem dfItem = (MatCostingItem)temp.get(0);
                 Material mat = (Material)temp.get(1);
                 Unit unit = (Unit)temp.get(2);
                 rowx = new Vector();
                 start = start + 1;

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
                 
                rowx.add(""+start+"");
                if(privManageData){
                    rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
                }else{
                    rowx.add(""+mat.getSku());
                }
                
                rowx.add(itemName);
                if(typeOfBusinessDetail != 2) {
                    rowx.add(unit.getCode());
                }
                   //if(mat.getMaterialType()!= PstMaterial.MAT_TYPE_COMPOSITE){
                    if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                        String where = PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                        int cnt = PstCostingStockCode.getCount(where);
                        if(cnt<dfItem.getQty()){
                            if(listError.size()==0){
                                listError.add("Silahkan cek :");
                            }
                            listError.add(""+listError.size()+". Jumlah serial kode stok "+itemName+" tidak sama dengan qty pengiriman");
                        }
                        rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(dfItem.getQty())+"</div>");
                    }else{
                        //adding stok balance & stok fisik
                        //by mirahu 30032012
                        if(enableBalanceStock == 1){
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getBalanceQty(),1)+"</div>");
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getResidueQty(),1)+"</div>");
                        }
                        
                        double qty=0;
                        if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE || mat.getMaterialType() == PstMaterial.MAT_TYPE_SERVICE){
                            int withcomponent = PstMaterialComposit.getCount("MC."+ PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+"='"+dfItem.getMaterialId()+"'");
                            if(withcomponent==0){
                                qty=dfItem.getQty();
                            }else{
                                 qty=dfItem.getQtyComposite();
                            }
                        }else{
                            qty=dfItem.getQty();
                        }
                        
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qty,1)+"</div>");
                    }
                    if(typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getWeight(),1)+"</div>");
                    }
                    if(privShowQtyPrice){
                        double xtotal = 0;
                        double hpp=0;
                        if(mat.getMaterialType() == PstMaterial.MAT_TYPE_COMPOSITE || mat.getMaterialType() == PstMaterial.MAT_TYPE_SERVICE){
                            //cari total hpp
                            xtotal = dfItem.getTotalHppComposite();
                            total += dfItem.getTotalHppComposite();
                            int withcomponent = PstMaterialComposit.getCount("MC."+ PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+"='"+dfItem.getMaterialId()+"'");
                            if(withcomponent==0){
                                hpp = xtotal/dfItem.getQty();
                            }else{
                                hpp = xtotal/dfItem.getQtyComposite();
                            }
                            
                        }else{
                            if(typeOfBusinessDetail == 2) {
                                total += (dfItem.getHpp()+dfItem.getCost()) * dfItem.getQty();
                                xtotal = (dfItem.getHpp()+dfItem.getCost()) * dfItem.getQty();
                            } else {
                                total += dfItem.getHpp() * dfItem.getQty();
                                xtotal = dfItem.getHpp() * dfItem.getQty();
                            }
                            hpp=dfItem.getHpp();
                        }
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(hpp,1)+"</div>");
                        if(typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getCost(),1)+"</div>");
                        }
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(xtotal,1)+"</div>");
                    }
                    
                    // add by fitra 17-05-2014
                    if(privManageData){
                        rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                    }else{
                    rowx.add("");
                    }
                 /*} else {

                     rowx.add("");
                     rowx.add("");
                     rowx.add("");
                     rowx.add("");
                  }*/

                lstData.add(rowx);
        }
        if(privShowQtyPrice){
            rowx = new Vector(1,1);
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\">Total : </div>");
            if(typeOfBusinessDetail != 2) {
                rowx.add("");
            }
              if(enableBalanceStock == 1){
                rowx.add("");
                rowx.add("");
              }
            rowx.add("");
            if(typeOfBusinessDetail == 2) {
                rowx.add("");
            }
            rowx.add("");
            if(typeOfBusinessDetail == 2) {
                rowx.add("");
            }
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total,1)+"</b></div>");
            rowx.add("");
            
            
            
            lstData.add(rowx);

            
        }
        
        
        
        result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada barang ...</div>";
	}

    list.add(result);
	list.add(listError);
	return list;
}

public Vector drawListOrderItemComponent(int language,Vector objectClass,int start, String invoiceNumber, int tranUsedPriceHpp, int enableBalanceStock, boolean privShowQtyPrice,boolean privManageData, String approot)
{
    Vector listError = new Vector(1,1);
    Vector list = new Vector(1,1);
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0],"3%");
            ctrlist.addHeader(textListOrderItem[language][1],"10%");
            ctrlist.addHeader(textListOrderItem[language][2],"40%");
            ctrlist.addHeader(textListOrderItem[language][3],"5%");
            //adding stok balance & stok fisik
            //by mirahu 30032012
            if(enableBalanceStock == 1){
            ctrlist.addHeader(textListOrderItem[language][9],"5%");
            ctrlist.addHeader(textListOrderItem[language][10],"5%");
           }
            //end of adding stok balance & stok fisik
            ctrlist.addHeader(textListOrderItem[language][4],"5%");
            if(privShowQtyPrice){
                if(tranUsedPriceHpp==0){
                ctrlist.addHeader(textListOrderItem[language][5],"10%");
                }else{
                ctrlist.addHeader(textListOrderItem[language][7],"10%");
                }
                ctrlist.addHeader(textListOrderItem[language][6],"10%");
            }
            ctrlist.addHeader(textListOrderItem[language][11],"10%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1,1);
            ctrlist.reset();
            int index = -1;
            if(start<0)
            start=0;

        double total = 0;
        for(int i=0; i<objectClass.size(); i++)
        {
                 Vector temp = (Vector)objectClass.get(i);
                 MatCostingItem dfItem = (MatCostingItem)temp.get(0);
                 Material mat = (Material)temp.get(1);
                 Unit unit = (Unit)temp.get(2);
                 rowx = new Vector();
                 start = start + 1;

   // double cost = getPriceCost(listItem,dfItem.getMaterialId());

                rowx.add(""+start+"");
                if(privManageData){
                    rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
                }else{
                    rowx.add(""+mat.getSku());
                }
                
                rowx.add(mat.getName());
                rowx.add(unit.getCode());
                   if(mat.getMaterialType()!= PstMaterial.MAT_TYPE_COMPOSITE){
                    if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                        String where = PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                        int cnt = PstCostingStockCode.getCount(where);
                        if(cnt<dfItem.getQty()){
                            if(listError.size()==0){
                                listError.add("Silahkan cek :");
                            }
                            listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty pengiriman");
                        }
                        rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(dfItem.getQty())+"</div>");
                    }else{
                        //adding stok balance & stok fisik
                        //by mirahu 30032012
                         if(enableBalanceStock == 1){
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getBalanceQty(),1)+"</div>");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getResidueQty(),1)+"</div>");
                        }
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getQty(),1)+"</div>");
                    }
                    if(privShowQtyPrice){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp(),1)+"</div>");
                    total += dfItem.getHpp() * dfItem.getQty();
                    double xtotal = dfItem.getHpp() * dfItem.getQty();
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(xtotal,1)+"</div>");
                    }
                    
                    // add by fitra 17-05-2014
                    if(privManageData){
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                    }else{
                    rowx.add("");
                    }
                 } else {

                     rowx.add("");
                     rowx.add("");
                     rowx.add("");
                     rowx.add("");
                  }

                lstData.add(rowx);
        }
        if(privShowQtyPrice){
            rowx = new Vector(1,1);
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\">Total : </div>");
            rowx.add("");
              if(enableBalanceStock == 1){
                rowx.add("");
                rowx.add("");
              }
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total,1)+"</b></div>");
            
            
            
            
            lstData.add(rowx);

            
        }
        
        
        
        result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada barang ...</div>";
	}

    list.add(result);
	list.add(listError);
	return list;
}
%>


<!-- Jsp Block -->
<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF);
%>


<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidCostingMaterial = FRMQueryString.requestLong(request, "hidden_costing_id");
//long oidCashCashierId=FRMQueryString.requestLong(request, FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CASH_CASHIER_ID]);
//enable stock balance
int enableStockBalance = FRMQueryString.requestInt(request, "enable_stock_balance");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String dfCode = "";//i_pstDocType.getDocCode(docType);
String dfTitle = "Costing Barang";//i_pstDocType.getDocTitle(docType);
String dfItemTitle = dfTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatCosting ctrlMatCosting = new CtrlMatCosting(request);

//by dyas - 20131126
//tambah variabel userName dan userId
iErrCode = ctrlMatCosting.action(iCommand , oidCostingMaterial, userName, userId);
FrmMatCosting frmdf = ctrlMatCosting.getForm();
MatCosting df = ctrlMatCosting.getMatCosting();
errMsg = ctrlMatCosting.getMessage();

//
if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
    try{
        System.out.println(">>> proses posting doc : "+oidCostingMaterial);
        SessPosting sessPosting = new SessPosting();
        boolean isOK =  sessPosting.postedCostingDoc(oidCostingMaterial);
         if(isOK){
            df.setCostingStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
        }
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}
/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";

/**
* list purchase order item
*/
oidCostingMaterial = df.getOID();
int recordToGetItem = 500;
int vectSizeItem = PstMatCostingItem.getCount(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + " = " + oidCostingMaterial);

String whereClauseItem = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"=0";
Vector listMatCostingItem = PstMatCostingItem.list(startItem,recordToGetItem,oidCostingMaterial,whereClauseItem);
String whereClauseItemComposite = "DFI."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"!=0";
Vector listMatCostingItemComponent = PstMatCostingItem.list(startItem,recordToGetItem,oidCostingMaterial,whereClauseItemComposite);

// check if document may modified or not
boolean privManageData = true;
if(df.getCostingStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}

//add opie-eyek 20131206
Vector list = drawListOrderItem(SESS_LANGUAGE,listMatCostingItem,startItem, df.getInvoiceSupplier(),tranUsedPriceHpp,df.getEnableStockFisik(),privShowQtyPrice,privManageData, approot, typeOfBusinessDetail);
Vector listComponent = drawListOrderItemComponent(SESS_LANGUAGE,listMatCostingItemComponent,startItem, df.getInvoiceSupplier(),tranUsedPriceHpp,df.getEnableStockFisik(),privShowQtyPrice,privManageData, approot);

//out.println(""+list.get(0));
Vector listError = (Vector)list.get(1);

if(iCommand==Command.DELETE && iErrCode==0)
{
%>
	<jsp:forward page="srccosting_material.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function compare(){
	var dt = document.frm_matdispatch.<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE]%>_dy.value;
	var mn = document.frm_matdispatch.<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE]%>_mn.value;
	var yy = document.frm_matdispatch.<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_costing_item_id.value=oid;
    document.frm_matdispatch.action="costing_stockcode.jsp";
    document.frm_matdispatch.submit();
}


function PostingStock() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.POSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="costing_material_edit.jsp";
        document.frm_matdispatch.submit();
        }
}


function PostingCostPrice() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.REPOSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="costing_material_edit.jsp";
        document.frm_matdispatch.submit();
        }
}

function cmdSave()
{
	<%	if ((df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
                
                var statusDoc = document.frm_matdispatch.<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_STATUS]%>.value;
                if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                     if(conf){
                        document.frm_matdispatch.command.value="<%=Command.SAVE%>";
                        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
                        document.frm_matdispatch.action="costing_material_edit.jsp";
                        if(compare()==true)
                                document.frm_matdispatch.submit();
                     }
                }else{
                    document.frm_matdispatch.command.value="<%=Command.SAVE%>";
                    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
                    document.frm_matdispatch.action="costing_material_edit.jsp";
                    if(compare()==true)
                            document.frm_matdispatch.submit();
                }
                
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function cmdAsk(oid){
	<%	if ((df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
    {
	%>
            document.frm_matdispatch.command.value="<%=Command.ASK%>";
            document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
            document.frm_matdispatch.action="costing_material_edit.jsp";
			if(compare()==true)
				document.frm_matdispatch.submit();
	<%
    }
    else
    {
	%>
	alert("Document has been posted !!!");
	<%
    }
	%>
}

function cmdCancel(){
	document.frm_matdispatch.command.value="<%=Command.CANCEL%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}


function cmdDelete(oid){
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
       
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
        document.frm_matdispatch.hidden_costing_item_id.value=oid;
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

function cmdBack(){
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function printForm()
{
	window.open("costing_material_print_form.jsp?hidden_costing_id=<%=oidCostingMaterial%>&command=<%=Command.EDIT%>&entrystokfisik=<%=df.getEnableStockFisik()%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printInvoice()
{
	window.open("<%=approot%>/servlet/com.dimata.posbo.printing.costing.PrintCosting?hidden_costing_id=<%=oidCostingMaterial%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function findInvoice()
{
	window.open("df_wh_material_receive.jsp","invoice_supplier","scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if (df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.ADD%>";
		document.frm_matdispatch.action="costing_material_item.jsp";
		if(compareDateForAdd()==true)
			document.frm_matdispatch.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function editItem(oid)
{
	<%	if (df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.EDIT%>";
		document.frm_matdispatch.hidden_costing_item_id.value=oid;
		document.frm_matdispatch.action="costing_material_item.jsp";
		document.frm_matdispatch.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function itemList(comm){
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.prev_command.value=comm;
	document.frm_matdispatch.action="costing_material_item.jsp";
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

//by dyas - 20131126
//tambah function viewHistoryTable
//untuk mengambil oidCostingMaterial dan digunakan di historyPo
function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidCostingMaterial%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
            <!--&nbsp;Warehouse &gt; Costing &gt; Store <!-- #EndEditable --><!--</td>-->
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matdispatch" method="post" action="">
<%
try
{
%>
              <input type="hidden" name="command" value="">
	      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_costing_id" value="<%=oidCostingMaterial%>">
              <input type="hidden" name="hidden_costing_item_id" value="">
              <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE]%>" value="<%=df.getCostingStatus()%>">
              <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatCosting.FLD_TYPE_COSTING_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="enable_stock_balance" value ="<%=enableStockBalance%>">
              <input type="hidden" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=df.getCashCashierId()==0? userId : df.getCashCashierId() %>">
              <table width="100%" border="0">
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="right">                      
                        <td width="13%" align="right" valign="top">                      
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="26%" align="left">
                          : <b><%=df.getCostingCode()%></b>
                        </td>
                      <td width="8%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][8]%> :</td>
                      <td width="13%" align="left" valign="top">
                          <%

							Vector obj_costingid = new Vector(1,1);
							Vector val_costingid = new Vector(1,1);
							Vector key_costingid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							Vector vt_cost = PstCosting.list(0,0,"", PstCosting.fieldNames[PstCosting.FLD_NAME]);
							for(int d=0;d<vt_cost.size();d++) {
								Costing costing = (Costing)vt_cost.get(d);
								val_costingid.add(""+costing.getOID()+"");
								key_costingid.add(costing.getName());
							}
							String select_costingid = ""+df.getCostingId(); //selected on combo box
			%>
                        <% if((df.getCostingId()!=0) || df.getCostingStatus()!=I_DocStatus.DOCUMENT_STATUS_CLOSED){%>
                        <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_ID], null, select_costingid, val_costingid, key_costingid, "", "formElemen")%>
                        <%}%>

                        <%
                         if((df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED) && df.getCostingId()== 0 && df.getCashCashierId()!=0){
                            out.println("HPP Penjualan");
                         }
                         %>

                      </td>  
                      <td width="19%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][5]%> :                                              
                      <td width="22%" rowspan="5" align="right" valign="top"><textarea name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea>
                      </td>
                        
                      </tr>
                      <tr>
                        <td width="12%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="26%" align="left" valign="bottom">: <%=ControlDate.drawDateWithStyle(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], (df.getCostingDate()==null) ? new Date() : df.getCostingDate(), 1, -5, "formElemen", "")%></td>
                        <!--td width="8%" align="right"-->
                        <!-- add enable stok fisik -->
                        <!-- by mirahu 29032012 -->
                       <td width="8%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%> :</td>
                       <td width="13%" align="left" valign="top">
                           <% if(typeOfBusinessDetail == 2) { %>
                                <%
                                    Vector val_contactid = new Vector(1,1);
                                    Vector key_contactid = new Vector(1,1);
                                    Vector vt_contact = PstEmployee.list(0,0,"", PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
                                    
                                    val_contactid.add("0");
                                    key_contactid.add("-");
                                            
                                    for(int d=0;d<vt_contact.size();d++) {
                                            Employee employee = (Employee)vt_contact.get(d);
                                            val_contactid.add(""+employee.getOID()+"");
                                            key_contactid.add(employee.getFullName());
                                    }
                                    String select_contactid = ""+df.getContactId(); //selected on combo box
                            %>
                            <% if((df.getCostingId()!=0) || df.getCostingStatus()!=I_DocStatus.DOCUMENT_STATUS_CLOSED){%>
                            <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_CONTACT_ID], null, select_contactid, val_contactid, key_contactid, "", "formElemen")%>
                            <%}%>
                           <% } %>
                       </td>
                      </tr>
                      <tr>
                        <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td align="left">: <%=ControlDate.drawTimeSec(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_DATE], (df.getCostingDate()==null) ? new Date(): df.getCostingDate(),"formElemen")%></td>
                        <% if(typeOfBusiness.equals("2")) { %>
                        <td width="12%" align="right">&nbsp;
                        </td>
                        <td><input type="checkbox" name="<%=FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_ENABLE_STOCK_FISIK]%>" value="1" <% if(df.getEnableStockFisik()==1){%>checked<%}%>> &nbsp;&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                       <% } %> 
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="26%" align="left">:
                          <%
                                Vector obj_locationid = new Vector(1,1);
                                Vector val_locationid = new Vector(1,1);
                                Vector key_locationid = new Vector(1,1);
                                //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                //add opie-eyek
                                //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                for(int d=0;d<vt_loc.size();d++) {
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                                String select_locationid = ""+df.getLocationId(); //selected on combo box
                          %>
                        <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                        <td width="8%" align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="20" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td align="left">:
                          <%
							Vector obj_locationid1 = new Vector(1,1);
							Vector val_locationid1 = new Vector(1,1);
							Vector key_locationid1 = new Vector(1,1);
							String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
							String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
							//Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
							for(int d = 0; d < vt_loc.size(); d++)	{
								Location loc1 = (Location)vt_loc.get(d);
								val_locationid1.add(""+loc1.getOID()+"");
								key_locationid1.add(loc1.getName());
							}
							String select_locationid1 = ""+df.getCostingTo(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>
                        <td align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td align="left">: <%
                        Vector obj_status = new Vector(1,1);
                        Vector val_status = new Vector(1,1);
                        Vector key_status = new Vector(1,1);

                        // update opie-eyek 19022013
                        // user bisa memfinalkan costing tidak bisa mengubah status dari final ke draft jika  jika  :
                        // 1. punya priv. costing approve = true
                        // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                        boolean locationAssign=false;
                        locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",df.getCostingTo());
                        
                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
						//add by fitra
                        if(listMatCostingItem.size()>0){
						  val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
			}
                        if(listMatCostingItem.size()>0 && privApproval==true && locationAssign==true && listError.size()==0){
                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                        }
                        String select_status = ""+df.getCostingStatus();
                        if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                        }else if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                        }else if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval==false || locationAssign==false)){
                                out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                        }else{
                        %>
                        <%=ControlCombo.draw(FrmMatCosting.fieldNames[FrmMatCosting.FRM_FIELD_COSTING_STATUS],null,select_status,val_status,key_status,"","formElemen")%>
                        <%}%>
                        </td>
                        <td align="right">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=list.get(0)%></td>
                      </tr>
                      
                      
                      <%if(oidCostingMaterial!=0){%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
						  if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST){
								cmdItem = Command.FIRST;
						  }
						  ctrLine.setLocationImg(approot+"/images");
						  ctrLine.initDefault();
						  ctrLine.setImageListName(approot+"/images","item");

						  ctrLine.setListFirstCommand("javascript:itemList('"+Command.FIRST+"')");
						  ctrLine.setListNextCommand("javascript:itemList('"+Command.NEXT+"')");
						  ctrLine.setListPrevCommand("javascript:itemList('"+Command.PREV+"')");
						  ctrLine.setListLastCommand("javascript:itemList('"+Command.LAST+"')");

						  %>
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> 
                          </span>
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
                        <td height="22" valign="middle" colspan="3"><%=listComponent.get(0)%></td>
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
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                <td width="94%"><a class="btn-primary btn-lg" href="javascript:addItem()"><i class="fa fa-plus-circle"> &nbsp; <%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></i></a></td>
                              <% } %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">&nbsp;</td>
                  <td width="30%">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="100%">
                          <%
					ctrLine.setLocationImg(approot+"/images");

					// set image alternative caption
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_CANCEL,false));

					ctrLine.initDefault();
					ctrLine.setTableWidth("100%");
					String scomDel = "javascript:cmdAsk('"+oidCostingMaterial+"')";
					String sconDelCom = "javascript:cmdDelete('"+oidCostingMaterial+"')";
					String scancel = "javascript:cmdEdit('"+oidCostingMaterial+"')";
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");

					// set command caption
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ASK,true));
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_CANCEL,false));

					if(privDelete){
						ctrLine.setConfirmDelCommand(sconDelCom);
						ctrLine.setDeleteCommand(scomDel);
						ctrLine.setEditCommand(scancel);
					}else{
						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					if(privAdd==false && privUpdate==false){
						ctrLine.setSaveCaption("");
					}

					if(privAdd==false || df.getCostingStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
						ctrLine.setAddCaption("");
					}

					if(iCommand==Command.SAVE && frmdf.errorSize()==0){
						//iCommand=Command.EDIT;
					}

					%>
                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%> </td>
                        <td width="30%">
                          <%if(listMatCostingItem!=null && listMatCostingItem.size()>0){%>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              
                           
                                <% if(df.getCostingStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print <%=dfTitle%></i></a></td>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printInvoice('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print Invoice <%=dfTitle%></i></a></td>
                                <%}else if (df.getCostingStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL){%>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print <%=dfTitle%></i></a></td>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:PostingStock('<%=oidCostingMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][4]%> </a></td>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printInvoice('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print Invoice <%=dfTitle%><</i>/a></td>
                               <%}else{%>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print <%=dfTitle%></i></a></td>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printInvoice('<%=oidCostingMaterial%>')" class="command" ><i class="fa fa-print">&nbsp;Print Invoice <%=dfTitle%></i></a></td>
                                <%if(privShowQtyPrice  && typeOfBusiness=="3"){%>
                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:PostingCostPrice('<%=oidCostingMaterial%>')" class="command" > <i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][5]%> </i></a></td>
                                <%}%>
                              <%}%>
                             </tr>
                          </table>
			<%}%>
                        </td>
                      </tr>
    <!--                by dyas - 20131126
                    tambah baris untuk memanggil fungsi viewHistoryTable()-->
                    <tr> 
                        <td height="25" valign="top" width="9%" align="left">&nbsp;</td>
                     </tr>
                    <tr>
                        <td> <centre> <a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></centre></td>
                    </tr>
                    </table>
                  </td>
                </tr>
              </table>
<%
}
catch(Exception e)
{
	System.out.println(e);
}
%>
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
    
    
        <script language="JavaScript">
            <% 
            

   // add by fitra 10-5-2014
if(df.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>  
               addItem();
            <% } %>
      </script>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

