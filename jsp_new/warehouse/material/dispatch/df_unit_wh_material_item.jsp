<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.util.Command,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.posbo.form.warehouse.FrmMatDispatchItem,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.warehouse.FrmMatDispatch,
                   com.dimata.posbo.form.warehouse.CtrlMatDispatch,
                   com.dimata.posbo.form.warehouse.FrmMatDispatchItem,
                   com.dimata.posbo.form.warehouse.CtrlMatDispatchItem,
                   com.dimata.posbo.form.warehouse.FrmMatDispatchReceiveItem,
                   com.dimata.posbo.form.warehouse.CtrlMatDispatchReceiveItem,
                   com.dimata.posbo.entity.masterdata.MaterialStock,
                   com.dimata.posbo.entity.masterdata.PstMaterialStock" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>



<%@ include file = "../../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>

<%@ include file = "../../../main/checkuser.jsp" %>



<%

/*if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){

	privAdd=false;

	privUpdate=false;

	privDelete=false;

}*/

%>



<!-- Jsp Block -->

<%!

public static final String textListGlobal[][] = {

    {"Transfer Unit","Edit","Quantity transfer unit melebihi quantity yang ada"},

    {"Dispatch","Edit","Dispatch quantity more than available quantity"}

};



/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] =

{

    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier", "Source", "Target"},

    {"Number","Location","Destination","Date","Status","Remark","Supplier Invoice", "Source", "Target"}

};



/* this constant used to list text of listMaterialItem */

public static final String textListOrderItem[][] =

{

    {"No","Sku","Nama Barang","Unit","Qty","Group","Source", "Target","@Nilai Stok", "Total","Hapus"},//10

    {"No","Code","Goods Name","Unit","Qty","Group","Source", "Target","@Stock Value", "Total","Delete"}

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
* drawlist for Group
*/

public Vector drawListGroupItem(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

    MatDispatchReceiveItem objEntity,Vector objectClass,

    long dfRecGroupId,int start,long locationId,long dfRecGroupIdItem, long dfRecItemId, int commandType,
    int startGroup,boolean privManageData, String approot, int typeOfBusinessDetail) {

    Vector list = new Vector(1,1);

    Vector listError = new Vector(1,1);

    String result = "";
    
        if(dfRecGroupId == dfRecGroupIdItem){
            dfRecGroupIdItem = 0;
        }

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");

        ctrlist.setListStyle("listgen");

        ctrlist.setTitleStyle("listgentitle");

        ctrlist.setCellStyle("listgensell");

        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListOrderItem[language][5],"5%");
        ctrlist.addHeader(textListOrderItem[language][6],"45%");
        ctrlist.addHeader(textListOrderItem[language][7],"45%");
        
        Vector lstData = ctrlist.getData();

        Vector lstLinkData = ctrlist.getLinkData();

        Vector rowx = new Vector(1,1);

        ctrlist.reset();

        int index = -1;

        if(start<0)

            start=0;

        for(int i=0; i<objectClass.size(); i++){
            MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
            rowx = new Vector();
            //startGroup = startGroup + 1;
            start = start + 1;
            if (dfRecGroupIdItem == dfRecItem.getDfRecGroupId()) index = i;
            rowx.add("<div align=\"center\">"+(start)+"</div><a href=\"javascript:cmdEdit('"+String.valueOf(dfRecItem.getDfRecGroupId())+"')\"><div align=\"center\">Edit</div>");
            String order="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
            Vector listItem = PstMatDispatchReceiveItem.list(0,0,dfRecItem.getDfRecGroupId(),order);
            
            //disini untuk menambahkan serial number
            Vector ListSourceItem = drawListSourceItem(language,iCommand,frmObject,objEntity,listItem,dfRecGroupId,start,locationId,dfRecGroupIdItem,dfRecItemId,commandType, privManageData, approot, typeOfBusinessDetail); 
            rowx.add(ListSourceItem.get(0));
            listError.add(ListSourceItem.get(1));
            
            Vector ListTargetItem = drawListTargetItem(language,iCommand,frmObject,objEntity,listItem,dfRecGroupId,start,locationId,dfRecGroupIdItem,dfRecItemId,commandType, privManageData, approot, typeOfBusinessDetail);      
            
            rowx.add(ListTargetItem.get(0));
            listError.add(ListTargetItem.get(1));
            
            lstData.add(rowx);
        }
        
       rowx = new Vector();

       String styleQty="";
       String styleQty1="";
        if(iCommand==Command.ADD && (dfRecGroupId==0 && dfRecGroupIdItem==0) && commandType==CtrlMatDispatchReceiveItem.COMMAND_TYPE_GROUP){
            
            MatDispatchReceiveItem dfRecItem = new MatDispatchReceiveItem();
            rowx.add("<div align=\"center\">"+(start+1)+"</div>");
            String order="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
            Vector listItem = PstMatDispatchReceiveItem.list(0,0,dfRecItem.getDfRecGroupId(),order);
            rowx.add(drawListSourceGroup(language,iCommand,frmObject,objEntity,listItem,dfRecGroupId,start,locationId));
            rowx.add(drawListTargetGroup(language,iCommand,frmObject,objEntity,listItem,dfRecGroupId,start,locationId));

            lstData.add(rowx);

       }
        result = ctrlist.draw(index);
        
    list.add(result);

    list.add(listError);

    return list;

}

//end Of drawlist Group


/**
* Drawlist for source item
* By Mirahu
*/

public Vector drawListSourceItem(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

        MatDispatchReceiveItem objEntity,Vector objectClass,

        long dfRecGroupId,int start,long locationId, long dfRecGroupIdItem, long dfRecItemId,
        int commandType, boolean privManageData, String approot, int typeOfBusinessDetail) {

    String result = "";

    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");
   // ctrlist.addHeader(textListOrderItem[language][5],"10");
    //ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    //header stok
    ctrlist.addHeader(textListOrderItem[language][8],"5%");
    ctrlist.addHeader(textListOrderItem[language][9],"5%");
     ctrlist.addHeader(textListOrderItem[language][10],"5%");

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;

   long oidDfRecGroupIdList = 0;

   for(int i=0; i<objectClass.size(); i++) {

        MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
        oidDfRecGroupIdList = dfRecItem.getDfRecGroupId();
        String itemName = dfRecItem.getSourceItem().getMaterialSource().getName();
        //added by dewok for litama 2018-02-16
        long materialId = dfRecItem.getSourceItem().getMaterialId();
        Category category = new Category();
        Color color = new Color();
        Material newMat = new Material();
        try{                
            newMat = PstMaterial.fetchExc(materialId);
            category = PstCategory.fetchExc(newMat.getCategoryId());
            color = PstColor.fetchExc(newMat.getPosColor());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if (typeOfBusinessDetail == 2) {
            if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                itemName = "" + category.getName() + " " + color.getColorName() + " " + newMat.getName();
            } else if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newMat.getName();
            }
        }

        rowx = new Vector();
        start = start + 1;

        if (dfRecItemId == dfRecItem.getOID()) index = i;
        //if (dfRecGroupId == dfRecItem.getDfRecGroupId()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {

            //mencari stok yang tersedia

            String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfRecItem.getSourceItem().getMaterialSource().getOID();
            whereClause += " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;

            Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");

            MaterialStock objMaterialStock = new MaterialStock();

            if(vMaterialStock.size() > 0) {

             objMaterialStock = (MaterialStock)vMaterialStock.get(0);

            }
            //rowx.add(""+start);
            //rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISPATCH_MATERIAL_ITEM_ID]+"\" value=\""+dfRecItem.getSourceItem().getOID()+
                    "\"><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+dfRecItem.getSourceItem().getMaterialId()+
                    "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+dfRecItem.getSourceItem().getMaterialSource().getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+itemName+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" value=\""+dfRecItem.getSourceItem().getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+dfRecItem.getSourceItem().getUnitSource().getCode()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"+
                    "<input type=\"hidden\" name=\"avbl_qty\" value=\""+objMaterialStock.getQty()+"\"></div>");
             //+nilai stok
            //by Mirahu
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_SOURCE]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHpp())+"\" class=\"hiddenText\" readOnly></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_SOURCE]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHppTotal())+"\" class=\"hiddenText\" readOnly></div>");

        } else {
            //rowx.add(""+start);
            //rowx.add(""+start+"");
           if(dfRecItem.getSourceItem().getMaterialId()!=0){
            if(dfRecGroupIdItem==dfRecItem.getDfRecGroupId()){
                rowx.add("<a href=\"javascript:cmdEditItem('"+String.valueOf(dfRecItem.getOID())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            }
            else {
                rowx.add(dfRecItem.getSourceItem().getMaterialSource().getSku());
            }
            //rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfRecItem.getDfRecGroupId())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            rowx.add(itemName);
            rowx.add(dfRecItem.getSourceItem().getUnitSource().getCode());
            
            //add opie-eyek untuk penambahan serial number source / tranfer
            if(dfRecItem.getSourceItem().getMaterialSource().getRequiredSerialNumber()==PstMaterial.REQUIRED){  
              String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfRecItem.getSourceItem().getOID();
                int cnt = PstDispatchStockCode.getCount(where);
                    if(cnt<dfRecItem.getSourceItem().getQty()){
                      if(listError.size()==0){
                        listError.add("Silahkan cek Source Item :");
                    }
                      listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getSourceItem().getMaterialSource().getName()+" tidak sama dengan qty pengiriman");
                 }

                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getSourceItem().getOID())+"','0')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"</div>");
            }
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHpp())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHppTotal())+"</div>");
            
            
            
            if(privManageData){
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfRecItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
            rowx.add("");
            }
          }
          }

        lstData.add(rowx);
       

        //new Item 4 Jan 2010

    }

    rowx = new Vector();

    String styleQty="";
    String styleQty1="";
      if(iCommand==Command.ADD && (dfRecGroupIdItem!=0) && dfRecGroupIdItem==oidDfRecGroupIdList && commandType == CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
           //styleUnit="display:none";

        }
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"35\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" class=\"hiddenText\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"+
                "<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        //+nilai stok
        //08022011
        //by Mirahu
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("");
        lstData.add(rowx);

    }
    //end of add Item

    //add Item Group
      
    //if(iCommand==Command.ADD && (dfRecGroupId!=0 || dfRecGroupIdItem!=0) || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
      if(iCommand==Command.ADD && (dfRecGroupId!=0) && dfRecGroupId==oidDfRecGroupIdList && commandType != CtrlMatDispatchReceiveItem.COMMAND_TYPE_GROUP || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
        }
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"35\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" class=\"hiddenText\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"+
                "<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        //+nilai stok
        //08022011
        //by Mirahu
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        //rowx.add("<div align=\"right\"></div>");
        rowx.add("");
        //rowx.add("<div align=\"right\"></div>");
        lstData.add(rowx);

    }
    //end of add Item Group */
      result = ctrlist.draw(index);
    //list.add(ctrlist.draw());
    list.add(result);  
    list.add(listError);

    return list;
    //return result;

}

//end of Drawlist Source item


/**
* Drawlist for source item Group
* By Mirahu
*/
public String drawListSourceGroup(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

        MatDispatchReceiveItem objEntity,Vector objectClass,

        long dfRecGroupId,int start,long locationId) {

    String result = "";

    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");
   // ctrlist.addHeader(textListOrderItem[language][5],"10");
    //ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    ctrlist.addHeader(textListOrderItem[language][8],"5");
    ctrlist.addHeader(textListOrderItem[language][9],"5");


    //Vector list = new Vector(1,1);
    //Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;

    rowx = new Vector();

    String styleQty="";
    String styleQty1="";
    //String styleUnit="";

    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {

        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
           //styleUnit="display:none";

        }
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"35\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" class=\"hiddenText\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"+
                "<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        //+input nilai stok
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_SOURCE]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        //rowx.add("<div align=\"right\"></div>");

        //rowx.add("<div align=\"right\"></div>");
        lstData.add(rowx);

    }


      result = ctrlist.draw();
    //list.add(ctrlist.draw());

    //list.add(listError);

    //return list;
    return result;

}

//end of Drawlist Source item Group


public Vector drawListTargetItem(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

        MatDispatchReceiveItem objEntity,Vector objectClass,

        long dfRecGroupId,int start,long locationId, long dfRecGroupIdItem, long dfRecItemId,
        int commandType, boolean privManageData, String approot, int typeOfBusinessDetail) {

    String result = "";
    
    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");

    //Tambahan header untuk target
    //ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    ctrlist.addHeader(textListOrderItem[language][8],"5%");
    ctrlist.addHeader(textListOrderItem[language][9],"5%");
    ctrlist.addHeader(textListOrderItem[language][10],"5%");
    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;


   long oidDfRecGroupIdList = 0;

   for(int i=0; i<objectClass.size(); i++) {

        MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
        oidDfRecGroupIdList = dfRecItem.getDfRecGroupId();
        String itemName = dfRecItem.getTargetItem().getMaterialTarget().getName();
        //added by dewok for litama 2018-02-16
        long materialId = dfRecItem.getTargetItem().getMaterialId();
        Category category = new Category();
        Color color = new Color();
        Material newMat = new Material();
        try{                
            newMat = PstMaterial.fetchExc(materialId);
            category = PstCategory.fetchExc(newMat.getCategoryId());
            color = PstColor.fetchExc(newMat.getPosColor());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if (typeOfBusinessDetail == 2) {
            if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                itemName = "" + category.getName() + " " + color.getColorName() + " " + newMat.getName();
            } else if (newMat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newMat.getName();
            }
        }
        rowx = new Vector();
        start = start + 1;

        if (dfRecItemId == dfRecItem.getOID()) index = i;
        //if (dfRecGroupId == dfRecItem.getDfRecGroupId()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {

            //mencari stok yang tersedia

            String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfRecItem.getSourceItem().getMaterialSource().getOID();
            whereClause += " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;

            Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");

            MaterialStock objMaterialStock = new MaterialStock();

            if(vMaterialStock.size() > 0) {

             objMaterialStock = (MaterialStock)vMaterialStock.get(0);

            }
            //rowx.add(""+start);
            //Tambahan Baris Untuk Target
            //rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]+"\" value=\""+dfRecItem.getTargetItem().getOID()+
                    "\"><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+dfRecItem.getTargetItem().getMaterialId()+
                    "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+dfRecItem.getTargetItem().getMaterialTarget().getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">"); //<a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"35\" name=\"matItem1\" value=\""+itemName+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" value=\""+dfRecItem.getTargetItem().getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+dfRecItem.getTargetItem().getUnitTarget().getCode()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"+
                    "<input type=\"hidden\" name=\"avbl_qty1\" value=\""+objMaterialStock.getQty()+"\"></div>");
            //+nilai stok
            //+by Mirahu
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getCost())+"\" class=\"hiddenText\" readOnly></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getTotal())+"\" class=\"hiddenText\" readOnly></div>");
           

        } else {
            //Tambahan Baris Untuk Target
           if(dfRecItem.getTargetItem().getMaterialId()!=0){
            if(dfRecGroupIdItem==dfRecItem.getDfRecGroupId()){
                rowx.add("<a href=\"javascript:cmdEditItem('"+String.valueOf(dfRecItem.getOID())+"')\">"+dfRecItem.getTargetItem().getMaterialTarget().getSku()+"</a>");
            }
            else {
                rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getSku());
            }
            rowx.add(itemName);
            rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());
            
            
            //add opie-eyek untuk penambahan serial number di target / receive
            if(dfRecItem.getTargetItem().getMaterialTarget().getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+dfRecItem.getTargetItem().getOID();
                     int cnt = PstReceiveStockCode.getCount(where);
                      if(cnt<dfRecItem.getTargetItem().getQty()){
                          if(listError.size()==0){
                             listError.add("Silahkan cek Target Item :");
                          }
                         listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getTargetItem().getMaterialTarget().getName()+" tidak sama dengan qty penerimaan");
                      }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getTargetItem().getOID())+"','1')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"</div>");
            }
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getCost())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getTotal())+"</div>");
            
            if(privManageData){
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfRecItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
            rowx.add("");
          }
        }
        }

        lstData.add(rowx);

    }
    rowx = new Vector();

    String styleQty="";
    String styleQty1="";
    //String styleUnit="";

     //if(iCommand==Command.ADD && (dfRecGroupId!=0 || dfRecGroupIdItem!=0) || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
       if(iCommand==Command.ADD && (dfRecGroupIdItem!=0) && dfRecGroupIdItem==oidDfRecGroupIdList && commandType == CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
           //styleUnit="display:none";

        }
        //Tambahan Baris Untuk Target
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+""+
               "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"35\" name=\"matItem1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\" id=\"txt_materialname1\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" class=\"hiddenText\" value=\""+""+
               "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty1+"\"   >"+
               "<input type=\"hidden\" name=\"avbl_qty1\" value=\"0\"></div>");
        //+nilai stok
        //By Mirahu
         rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
         rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
         rowx.add("");

        lstData.add(rowx);
     }

        //add Item Group

         if(iCommand==Command.ADD && (dfRecGroupId!=0) && dfRecGroupId==oidDfRecGroupIdList && commandType != CtrlMatDispatchReceiveItem.COMMAND_TYPE_GROUP || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {

            if(iCommand==Command.ADD){

                styleQty="display:none";
                styleQty1="display:none";
                //styleUnit="display:none";

            }
            //Tambahan Baris Untuk Target
            //rowx.add(""+(start+1));
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+""+
               "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
            rowx.add("<input type=\"text\" size=\"35\" name=\"matItem1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\" id=\"txt_materialname1\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" class=\"hiddenText\" value=\""+""+
               "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty1+"\"   >"+
               "<input type=\"hidden\" name=\"avbl_qty1\" value=\"0\"></div>");

             //+nilai stok
            //By Mirahu
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
            rowx.add("");
            lstData.add(rowx);

        }
         //end of Item Group*/

    //}
      result = ctrlist.draw(index);
    //list.add(ctrlist.draw());
    list.add(result);  
    list.add(listError);

    return list;
     // return result;

}

//end of Drawlist Target item




/**
* Drawlist for target item Group
* By Mirahu
*/

public String drawListTargetGroup(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

    MatDispatchReceiveItem objEntity,Vector objectClass,

    long dfRecGroupId,int start,long locationId) {

    String result = "";
    
    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");

    //Tambahan header untuk target
    //ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    ctrlist.addHeader(textListOrderItem[language][8],"5%");
    ctrlist.addHeader(textListOrderItem[language][9],"5%");

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;

    rowx = new Vector();

    String styleQty="";
    String styleQty1="";
    //String styleUnit="";

    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {

        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
           //styleUnit="display:none";

        }
        //Tambahan Baris Untuk Target
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+""+
               "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"35\" name=\"matItem1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\"   id=\"txt_materialname1\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" class=\"hiddenText\" value=\""+""+
               "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty1+"\"   >"+
               "<input type=\"hidden\" name=\"avbl_qty1\" value=\"0\"></div>");
        //nilai stok
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_TARGET]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");

        lstData.add(rowx);

    }
      result = ctrlist.draw();
    //list.add(ctrlist.draw());

    //list.add(listError);

    //return list;
      return result;

}

//end of Drawlist Target item Group



/**

 * this method used to maintain dfRecListItem

 */

public Vector drawListDfRecItem(int language,int iCommand,FrmMatDispatchReceiveItem frmObject,

        MatDispatchReceiveItem objEntity,Vector objectClass,

        long dfRecGroupId,int start,long locationId) {

    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");
   // ctrlist.addHeader(textListOrderItem[language][5],"10");
    ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"30%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");

    //Tambahan header untuk target
    //ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"40%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;

   for(int i=0; i<objectClass.size(); i++) {

        MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
        rowx = new Vector();
        start = start + 1;

        //if (dfRecItemId == dfRecItem.getOID()) index = i;
        if (dfRecGroupId == dfRecItem.getDfRecGroupId()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {

            //mencari stok yang tersedia

            String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfRecItem.getSourceItem().getMaterialSource().getOID();
            whereClause += " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;

            Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");

            MaterialStock objMaterialStock = new MaterialStock();

            if(vMaterialStock.size() > 0) {

             objMaterialStock = (MaterialStock)vMaterialStock.get(0);

            }
            //rowx.add(""+start);
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+dfRecItem.getSourceItem().getMaterialSource().getOID()+
                    "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+dfRecItem.getSourceItem().getMaterialSource().getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+dfRecItem.getSourceItem().getMaterialSource().getName()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" value=\""+dfRecItem.getSourceItem().getUnitSource().getOID()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+dfRecItem.getSourceItem().getUnitSource().getCode()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"+
                    "<input type=\"hidden\" name=\"avbl_qty\" value=\""+objMaterialStock.getQty()+"\"></div>");

            //Tambahan Baris Untuk Target
            //rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+dfRecItem.getTargetItem().getMaterialTarget().getOID()+
                    "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+dfRecItem.getTargetItem().getMaterialTarget().getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">"); //<a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"40\" name=\"matItem1\" value=\""+dfRecItem.getTargetItem().getMaterialTarget().getName()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" value=\""+dfRecItem.getTargetItem().getUnitTarget().getOID()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+dfRecItem.getTargetItem().getUnitTarget().getCode()+"\" class=\"hiddenText\"  readOnly>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"+
                    "<input type=\"hidden\" name=\"avbl_qty1\" value=\""+objMaterialStock.getQty()+"\"></div>");

        } else {
            //rowx.add(""+start);
            rowx.add(""+start+"");
            //rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfRecItem.getSourceItem().getOID())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfRecItem.getDfRecGroupId())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getName());
            rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());


            if(dfRecItem.getSourceItem().getMaterialSource().getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfRecItem.getSourceItem().getOID();

                int cnt = PstDispatchStockCode.getCount(where);

                if(cnt<dfRecItem.getSourceItem().getQty()){
                    if(listError.size()==0){

                        listError.add("Silahkan cek :");

                    }

                    listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getSourceItem().getMaterialSource().getName()+" tidak sama dengan qty pengiriman");

                }

                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getSourceItem().getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"</div>");

            }else{

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"</div>");

            }

            //rowx.add("<div align=\"right\">"+String.valueOf(dfItem.getQty())+"</div>");

            //Tambahan Baris Untuk Target
            //rowx.add(""+start+"");
            //rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getSku());
            rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getName());
            rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());

            if(dfRecItem.getTargetItem().getMaterialTarget().getRequiredSerialNumber()==PstMaterial.REQUIRED){

                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+dfRecItem.getTargetItem().getOID();

                int cnt = PstReceiveStockCode.getCount(where);

                if(cnt<dfRecItem.getTargetItem().getQty()){

                    if(listError.size()==0){

                        listError.add("Silahkan cek :");

                    }

                    listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getTargetItem().getMaterialTarget().getName()+" tidak sama dengan qty penerimaan");

                }

                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getTargetItem().getMaterialTarget().getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"</div>");

            }else{

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"</div>");

            }
        }

        lstData.add(rowx);

    }



    rowx = new Vector();

    String styleQty="";
    String styleQty1="";
    //String styleUnit="";

    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {

        if(iCommand==Command.ADD){

           styleQty="display:none";
           styleQty1="display:none";
           //styleUnit="display:none";

        }
        rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_SOURCE_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"10\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\" id=\"txt_materialname1\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_SOURCE_ID]+"\" class=\"hiddenText\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SOURCE] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"+
                "<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        //rowx.add("<div align=\"right\"></div>");

        //rowx.add("<div align=\"right\"></div>");

        //Tambahan Baris Untuk Target
        //rowx.add(""+(start+1));
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_TARGET_ID]+"\" value=\""+""+
               "\"><input type=\"text\" size=\"10\" name=\"matCode1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"40\" name=\"matItem1\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck1(event)\" id=\"txt_materialname1\">&nbsp;<a href=\"javascript:cmdCheck1()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_TARGET_ID]+"\" class=\"hiddenText\" value=\""+""+
               "\"><input type=\"text\" size=\"5\" name=\"matUnit1\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal1(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_TARGET] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty1+"\"   >"+
               "<input type=\"hidden\" name=\"avbl_qty1\" value=\"0\"></div>");

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



/**

 * get request data from current form

 */

int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
long oidDispatchReceiveItem = FRMQueryString.requestLong(request, "hidden_dispatch_receive_item_id");
long oidReceiveMaterial = FRMQueryString.requestLong(request,"hidden_receive_id");
long oidDfRecGroup = FRMQueryString.requestLong(request, "hidden_df_rec_group_id");
long oidDfRecGroupSame = FRMQueryString.requestLong(request, "hidden_df_rec_same_group_id");
int commandType = FRMQueryString.requestInt(request,"command_type");
int startGroup = FRMQueryString.requestInt(request, "start_group");





//dfgroupid
//MatDispatchReceiveItem matDfRecItem = new MatDispatchReceiveItem();
//long oidDfRecGroupNew = matDfRecItem.getDfRecGroupId();





/**

 * initialization of some identifier

 */

int iErrCode = FRMMessage.NONE;

String msgString = "";



/**

 * purchasing pr code and title

 */

String dfCode = ""; //i_pstDocType.getDocCode(docType);

String dfTitle = "Transfer Unit"; //i_pstDocType.getDocTitle(docType);

String dfItemTitle = dfTitle + " Item";



/**

 * purchasing pr code and title

 */

String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));





/**

 * process on df main

 */

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

iErrCode = ctrlMatDispatch.action(Command.EDIT,oidDispatchMaterial,userName,userId);

FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

MatDispatch df = ctrlMatDispatch.getMatDispatch();



/**
 * check if document already closed or not
 */

boolean documentClosed = false;

if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED) {

    documentClosed = true;

}



/**

 * check if document may modified or not

 */

boolean privManageData = true;



ControlLine ctrLine = new ControlLine();
CtrlMatDispatchReceiveItem ctrlMatDispatchReceiveItem = new CtrlMatDispatchReceiveItem(request);
iErrCode = ctrlMatDispatchReceiveItem.action(iCommand,oidDispatchReceiveItem,oidDfRecGroupSame,oidDispatchMaterial,oidDfRecGroup,commandType, userName, userId);
FrmMatDispatchReceiveItem frmMatDispatchReceiveItem = ctrlMatDispatchReceiveItem.getForm();
MatDispatchReceiveItem dfRecItem = ctrlMatDispatchReceiveItem.getMatDispatchReceiveItem();
msgString = ctrlMatDispatchReceiveItem.getMessage();

//oidDfRecGroup = dfRecItem.getDfRecGroupId();


/*int comm = iCommand;

if(iErrCode!=0 && dfItem.getOID()!=0 && iCommand==Command.SAVE){

    comm = Command.EDIT;

}else if(iErrCode!=0 && dfItem.getOID()==0 && iCommand==Command.SAVE){

    comm = Command.ADD;

}*/



String whereClauseItem = PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;
String orderClauseItem = "";
//int vectSizeItem = PstMatDispatchReceiveItem.getCount(whereClauseItem);
int vectSizeItem = PstMatDispatchReceiveItem.getCountGroup(whereClauseItem);
int recordToGetItem = 10;



if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {

    startItem = ctrlMatDispatchReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
      //startGroup = ctrlMatDispatchReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);

}



/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */

//if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {

    //iCommand = Command.ADD;

    //oidDispatchReceiveItem = 0;

    /** agar form add item tampil pada list paling akhir */

   // startItem = ctrlMatDispatchReceiveItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);

//}

//add new group
if(iCommand == Command.ADD){
    iCommand = Command.ADD;
    oidDispatchReceiveItem = 0;
    //oidDfRecGroup = dfRecItem.getDfRecGroupId();

    /** agar form add item tampil pada list paling akhir */

    //startItem = ctrlMatDispatchReceiveItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);
}

//Edit group
//if(iCommand == Command.EDIT ||(iCommand == Command.SAVE && iErrCode == 0)){
if(iCommand == Command.SAVE && iErrCode == 0){
    //iCommand = Command.ADD;
    iCommand = Command.ADD;
    commandType = CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM;
    oidDispatchReceiveItem = 0;
    oidDfRecGroup = oidDfRecGroup;

    /** agar form add item tampil pada list paling akhir */

    startItem = ctrlMatDispatchReceiveItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);
}



String order=" DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
//Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startItem, recordToGetItem, oidDispatchReceiveItem, order);
//Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startItem, recordToGetItem, oidDispatchMaterial, order);
//Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startItem, recordToGetItem, oidDfRecGroup, order);
//Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem, recordToGetItem, dfRecItem.getDfRecGroupId(), order);
Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem, recordToGetItem, oidDispatchMaterial, order);

if(listMatDispatchReceiveItem.size()<1 && startItem>0) {

    if(vectSizeItem-recordToGetItem > recordToGetItem) {

        startItem = startItem - recordToGetItem;

    } else {

        startItem = 0 ;

        iCommand = Command.FIRST;

        prevCommand = Command.FIRST;

    }

    listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startItem,recordToGetItem,oidDispatchMaterial);

}

//double total = PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroup);

//for Start Group
 /* if(listMatDispatchReceiveItem.size()<1 && startGroup>0) {

    if(vectSizeItem-recordToGetItem > recordToGetItem) {

        startGroup = startGroup - recordToGetItem;

    } else {

        startGroup = 0 ;

        iCommand = Command.FIRST;

        prevCommand = Command.FIRST;

    }

    listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startGroup,recordToGetItem,oidDispatchMaterial);

}*/





%>






<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->

<head>

<!-- #BeginEditable "doctitle" -->

<title>Dimata - ProChain POS</title>

<script language="JavaScript">

<!--

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

function main(oid,comm){

    document.frm_matdispatch.command.value=comm;

    document.frm_matdispatch.hidden_dispatch_id.value=oid;

    document.frm_matdispatch.action="df_unit_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}



function gostock(oid,type){

document.frm_matdispatch.command.value="<%=Command.EDIT%>";

document.frm_matdispatch.hidden_dispatch_receive_item_id.value=oid;

document.frm_matdispatch.hidden_dispatch_item_id.value=oid;

document.frm_matdispatch.hidden_receive_item_id.value=oid;

if(type==0){
      document.frm_matdispatch.type_doc.value="1";
      document.frm_matdispatch.action="df_stockcode.jsp";
    }else{
       document.frm_matdispatch.type_doc.value="3";
      document.frm_matdispatch.action="../receive/rec_wh_stockcode.jsp";  
    }


document.frm_matdispatch.submit();

}


function cmdAddItem()

{

    //document.frm_matdispatch.hidden_df_rec_group_id.value=oid;
    document.frm_matdispatch.command_type.value="<%=CtrlMatDispatchReceiveItem.COMMAND_TYPE_ITEM%>";
    
    document.frm_matdispatch.hidden_dispatch_receive_item_id.value="0";

    document.frm_matdispatch.command.value="<%=Command.ADD%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}

function cmdAddItemReceive()

{

    document.frm_matdispatch.hidden_dispatch_receive_item_id.value="0";

    document.frm_matdispatch.command.value="<%=Command.ADD%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_stock_material_receive_search.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}

function cmdAdd()

{


    document.frm_matdispatch.hidden_dispatch_receive_item_id.value="0";
    document.frm_matdispatch.hidden_df_rec_same_group_id.value="0";
    document.frm_matdispatch.hidden_df_rec_group_id.value="0";
    document.frm_matdispatch.command.value="<%=Command.ADD%>";
    document.frm_matdispatch.command_type.value="<%=CtrlMatDispatchReceiveItem.COMMAND_TYPE_GROUP%>";
    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}

function cmdAddGroup()

{


    document.frm_matdispatch.hidden_dispatch_receive_item_id.value="0";
    document.frm_matdispatch.hidden_df_rec_same_group_id.value="0";
    document.frm_matdispatch.hidden_df_rec_group_id.value="0";
    document.frm_matdispatch.command.value="<%=Command.ADD%>";
    document.frm_matdispatch.command_type.value="<%=CtrlMatDispatchReceiveItem.COMMAND_TYPE_GROUP%>";
    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}


function cmdEdit(oid)

{

    document.frm_matdispatch.hidden_df_rec_group_id.value=oid;
    //document.frm_matdispatch.hidden_df_rec_same_group_id.value=oid;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}

function cmdEditItem(oid)

{

    document.frm_matdispatch.hidden_dispatch_receive_item_id.value=oid;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}




function cmdAsk(oidDispatchReceiveItem)

{

    document.frm_matdispatch.hidden_dispatch_receive_item_id.value=oidDispatchReceiveItem;
    document.frm_matdispatch.hidden_df_rec_group_id.value="0";
    document.frm_matdispatch.hidden_df_rec_same_group_id.value="0";
    document.frm_matdispatch.command.value="<%=Command.ASK%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}

function cmdAskGroup(oidDispatchReceiveGroup)

{

    document.frm_matdispatch.hidden_df_rec_group_id.value=oidDispatchReceiveGroup;

    document.frm_matdispatch.command.value="<%=Command.ASK%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdSave(){

    var avblQty = parseFloat(document.frm_matdispatch.avbl_qty.value);
    var transferQty = parseFloat(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.value);
     var avblQty1 = parseFloat(document.frm_matdispatch.avbl_qty1.value);
    var transferQty1 = parseFloat(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.value);





<%!

/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */

static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");

static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;



%>

    if(transferQty<0.000 || transferQty1<0.000){

          // alert("Quantity < 0");

        } else {

            <% if(calculateQtyNull) { %>

                document.frm_matdispatch.command.value="<%=Command.SAVE%>";

                document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

                document.frm_matdispatch.submit();

            <%} else { %>

            if((transferQty <= avblQty) && (transferQty1 <= avblQty1)) {
                document.frm_matdispatch.command.value="<%=Command.SAVE%>";

                document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

                document.frm_matdispatch.submit();

            }

            else {
                alert("<%=textListGlobal[SESS_LANGUAGE][2]%> ("+avblQty+" "+document.frm_matdispatch.matUnit.value+")");

            }

            <%}%>

    }

}



function cmdConfirmDelete(oidDispatchReceiveItem)

{

    document.frm_matdispatch.hidden_dispatch_receive_item_id.value=oidDispatchReceiveItem;
    document.frm_matdispatch.hidden_df_rec_group_id.value="0";
    document.frm_matdispatch.hidden_df_rec_same_group_id.value="0";

    document.frm_matdispatch.command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}


function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid);
else
return cmdEdit(oid);
}

function cmdConfirmDeleteGroup(oidDispatchReceiveGroup)

{

    document.frm_matdispatch.hidden_df_rec_group_id.value=oidDispatchReceiveGroup;

    document.frm_matdispatch.command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdCancel(oidDispatchReceiveItem)

{

    document.frm_matdispatch.hidden_dispatch_receive_item_id.value=oidDispatchReceiveItem;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}

function cmdCancelGroup(oidDispatchReceiveGroup)

{

    document.frm_matdispatch.hidden_df_rec_group_id.value=oidDispatchReceiveGroup;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdBack()

{

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.start_item.value = 0;

    document.frm_matdispatch.action="df_unit_wh_material_edit.jsp";

    document.frm_matdispatch.submit();

}



var winSrcMaterial;

function cmdCheck()

{

    var strvalue  = "dfunitdosearch.jsp?command=<%=Command.FIRST%>"+
        "&location_id=<%=df.getLocationId()%>"+
        "&hidden_dispatch_id=<%=oidDispatchMaterial%>"+
        "&mat_code="+document.frm_matdispatch.matCode.value +
        "&txt_materialname="+document.frm_matdispatch.matItem.value;
    winSrcMaterial = window.open(strvalue,"Stock_Material", "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}

}

var winSrcMaterial;

function cmdCheck1()

{

    var strvalue  = "dfunitdosearch_1.jsp?command=<%=Command.FIRST%>"+
        "&location_id=<%=df.getDispatchTo()%>"+
        "&hidden_dispatch_id=<%=oidDispatchMaterial%>"+
        "&mat_code1="+document.frm_matdispatch.matCode1.value +
        "&txt_materialname1="+document.frm_matdispatch.matItem1.value;
    winSrcMaterial = window.open(strvalue,"Stock_Material", "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}

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

function keyDownCheck1(e){

   var trap = document.frm_matdispatch.trap.value;


   if (e.keyCode == 13 && trap==0) {
    document.frm_matdispatch.trap.value="1";

   }

    // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matdispatch.matItem.value == "" ){
        document.frm_matdispatch.trap.value="0";
        cmdCheck1();
}
   if (e.keyCode == 13 && trap==1) {
       document.frm_matdispatch.trap.value="0";
       cmdCheck1();
  }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matdispatch.txt_materialname1.value="";
   } 

}




function changeFocus(element){

    if(element.name == "matCode") {

        document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.focus();

    }

    else if(element.name == "<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>") {

        cmdSave();

    }
    if(element.name == "matCode1") {

        document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>.focus();

    }

    else if(element.name == "<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_TARGET]%>") {
        cmdSave();

    }


    else {

        cmdSave();

    }

}



function cntTotal(element, evt)

{

    var qty = cleanNumberInt(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.value,guiDigitGroup);

    var price = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_SOURCE]%>.value,guiDigitGroup,',');

   <%//double total = PstMatDispatchItem.getTotalHppSourceTransfer(oidDfRecGroup);%>

    //var TotalHpp = cleanNumberFloat(total);
    <% //if(total!=0){%>
        //document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET]%>.value=formatFloat(price, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    <%//}else {%>
        //document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_COST_TARGET]%>.value=TotalHpp;


    if(qty<0.0000){

          document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.value=0;


          return;

        }



  if(price==""){

          price = 0;

 }

    /*

    var qty = document.frm_recmaterial.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value;

    var price = document.frm_recmaterial.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value;

    */

    if(!(isNaN(qty)) && (qty != '0'))

    {

        var amount = price * qty;

        if(isNaN(amount)){

          amount = "0";

      }

      document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_TOTAL_SOURCE]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);


  }

  else

    {

        document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.focus();

    }



    if (evt.keyCode == 13) {

        changeFocus(element);

    }

}


function cntTotal1(element, evt)
{
    var qty = cleanNumberInt(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.value,guiDigitGroup);
    var price = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_SOURCE]%>.value,guiDigitGroup,',');
    if(qty<0.0000){
          document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.value=0;
          return;
    }
    if(price==""){
          price = 0;
    }
    if(!(isNaN(qty)) && (qty != '0'))
    {
        var amount = price * qty;
        if(isNaN(amount)){
          amount = "0";
      }
      document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_HPP_TOTAL_SOURCE]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);

  }else{
        document.frm_matdispatch.<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_QTY_SOURCE]%>.focus();
    }
    if (evt.keyCode == 13) {
        changeFocus(element);
    }
}

function cmdListFirst()

{

    document.frm_matdispatch.command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.prev_command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListPrev()

{

    document.frm_matdispatch.command.value="<%=Command.PREV%>";

    document.frm_matdispatch.prev_command.value="<%=Command.PREV%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListNext()

{

    document.frm_matdispatch.command.value="<%=Command.NEXT%>";

    document.frm_matdispatch.prev_command.value="<%=Command.NEXT%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListLast()

{

    document.frm_matdispatch.command.value="<%=Command.LAST%>";

    document.frm_matdispatch.prev_command.value="<%=Command.LAST%>";

    document.frm_matdispatch.action="df_unit_wh_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdBackList()

{

    document.frm_matdispatch.command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.action="df_unit_wh_material_list.jsp";

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



function MM_swapImage() { //v3.0

    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)

    if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}

}

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------



function MM_findObj(n, d) { //v4.01

    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);

    if(!x && d.getElementById) x=d.getElementById(n); return x;

}

//-->

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


<%--autocomplate add By fitra--%>
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

          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->

            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>

        </tr>

        <tr>

          <td><!-- #BeginEditable "content" -->

            <form name="frm_matdispatch" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_receive_id"value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_dispatch_receive_item_id" value="<%=oidDispatchReceiveItem%>">
              <input type="hidden" name="hidden_df_rec_group_id"value="<%=oidDfRecGroup%>">
              <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="<%=FrmMatDispatchReceiveItem.fieldNames[FrmMatDispatchReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_df_rec_same_group_id" value="<%=dfRecItem.getDfRecGroupId()%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="command_type" value="<%=commandType%>">
              <input type="hidden" name="start_group" value="<%=startItem%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="">
              <input type="hidden" name="type_doc" value="">
              <input type="hidden" name="hidden_receive_item_id" value="">
              <input type="hidden" name="trap" value="">    
              
              <table width="100%" cellpadding="1" cellspacing="0">

                <tr align="center">

                  <td colspan="3">

                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="25%">: <b>

                          <%

                          if(df.getDispatchCode()!="" && iErrCode==0) {

                                out.println(df.getDispatchCode());

                          } else {

                                out.println("");

                          }

                          %>

                        </b></td>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="31%">:

                        <%

                        Vector obj_locationid = new Vector(1,1);

                        Vector val_locationid = new Vector(1,1);

                        Vector key_locationid = new Vector(1,1);

                        //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE

                        Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);

                        for(int d=0;d<vt_loc.size();d++) {

                            Location loc = (Location)vt_loc.get(d);

                            val_locationid.add(""+loc.getOID()+"");

                            key_locationid.add(loc.getName());

                        }

                        String select_locationid = ""+df.getLocationId(); //selected on combo box

			%>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%> </td>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>

                         <td width="17%" rowspan="3" align="right" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3" disabled="true"><%=df.getRemark()%></textarea></td>

                      </tr>

                      <tr>

                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate()==null) ? new Date() : df.getDispatchDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>

                          <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>

                        <td>:

                        <%

                        Vector obj_locationid1 = new Vector(1,1);

                        Vector val_locationid1 = new Vector(1,1);

                        Vector key_locationid1 = new Vector(1,1);

                        String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;

                        String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);

                        Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);

                        for(int d = 0; d < vt_loc1.size(); d++) {

                            Location loc1 = (Location)vt_loc1.get(d);

                            val_locationid1.add(""+loc1.getOID()+"");

                            key_locationid1.add(loc1.getName());

                        }

                        String select_locationid1 = ""+df.getDispatchTo(); //selected on combo box

                        %>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "disabled=\"true\"", "formElemen")%> </td>

                        <td>&nbsp;</td>

                      </tr>

                      <tr>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                      </tr>

                    </table>

                   </td>

                </tr>

                <tr>

                  <td valign="top">

                    <table width="100%" cellpadding="1" cellspacing="1">
                        <!--<tr>
                            <td align="left" width="50%"><b><u><%=textListOrderHeader[SESS_LANGUAGE][7]%> </u></b>
						</td>

                            <td align="left" width="50%"><b><u><%=textListOrderHeader[SESS_LANGUAGE][8]%> </u></b></td>

                        </tr>-->

                      <tr>

                        <td colspan="3" >

                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >

                            <tr align="left" valign="top">

                              <%

                              Vector listError = new Vector(1,1);

                              try {

                              %>

                              <td height="22" valign="middle" colspan="3">

                              <%

                              //Vector list = drawListDfRecItem(SESS_LANGUAGE,iCommand,frmMatDispatchReceiveItem, dfRecItem,listMatDispatchReceiveItem,oidDfRecGroup,startItem,df.getLocationId());
                              Vector list = drawListGroupItem(SESS_LANGUAGE,iCommand,frmMatDispatchReceiveItem, dfRecItem,listMatDispatchReceiveItem,dfRecItem.getDfRecGroupId(),startItem,df.getLocationId(),oidDfRecGroup,oidDispatchReceiveItem, commandType, startGroup, privManageData, approot, typeOfBusinessDetail);

                              out.println(""+list.get(0));

                              listError = (Vector)list.get(1);

                              %>                              </td>

                              <%

                              } catch(Exception e) {

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

                                out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));

                                %>
                                </span> </td>
                            </tr>

                            <tr align="left" valign="top">

                              <td height="22" colspan="3" valign="middle" class="errfont">

                              <%

                              //for(int k=0;k<listError.size();k++){
                              try{
                                if(listError.size()> 0){ 
                                    for(int k=0;k<listError.size();k++){
                                        Vector detailList = (Vector) listError.get(k);
                                         if(detailList.size()>0){
                                             for(int l=0;l<detailList.size();l++){
                                                 if(l==0)
                                                      out.println(listError.get(l)+"<br>");
                                                  else
                                                      out.println("&nbsp;&nbsp;&nbsp;"+listError.get(l)+"<br>");
                                                }
                                        }
                                    }
                                }   
                              }catch(Exception ex){
                              }
                              %></td>
                            </tr>

                            <tr align="left" valign="top">

                              <td height="22" colspan="3" valign="middle">

                                <%

                                ctrLine.setLocationImg(approot+"/images");



                                // set image alternative caption

                                ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true));

                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));
                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true)+" List");

                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));

                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));

                                //for group
                                //ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" New Group", ctrLine.CMD_ADD,true));
                                  ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ASK,true));
                                //End for Group

                                ctrLine.initDefault();

                                ctrLine.setTableWidth("65%");

                                String scomDel = "javascript:cmdAsk('"+oidDispatchReceiveItem+"')";

                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidDispatchReceiveItem+"')";

                                String scancel = "javascript:cmdEditItem('"+oidDispatchReceiveItem+"')";

                                //for Group
                                String scomDelGroup = "javascript:cmdAskGroup('"+oidDfRecGroup+"')";

                                String sconDelGroup = "javascript:cmdConfirmDeleteGroup('"+oidDfRecGroup+"')";

                                String scancelGroup = "javascript:cmdEdit('"+oidDfRecGroup+"')";
                                //end for Group

                                ctrLine.setCommandStyle("command");

                                ctrLine.setColCommStyle("command");



                                // set command caption

                                ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true));
                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));
                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true)+" List");
                              
                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));
                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_DELETE,true));
                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));
                             
                                //for add group
                              if(oidDispatchReceiveItem == 0){
                                  //ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group", ctrLine.CMD_ADD, true));
                                  ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ASK,true));
                                  ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_DELETE,true));
                                  ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_CANCEL,false));
                               }
                                //end of group


                                if (privDelete){
                                     if(oidDispatchReceiveItem != 0){

                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);

                                    }else{

                                    
                                    //for GroupscomDelComGroup
                                     ctrLine.setConfirmDelCommand(sconDelGroup);
                                     ctrLine.setDeleteCommand(scomDelGroup);
                                     ctrLine.setEditCommand(scancelGroup);
                                  }
                                    //end of Group

                                }else{
                                    ctrLine.setConfirmDelCaption("");
                                    ctrLine.setDeleteCaption("");
                                    ctrLine.setEditCaption("");

                                }



                                if(privAdd == false  && privUpdate == false){

                                    ctrLine.setSaveCaption("");

                                }



                                if (privAdd == false){

                                    ctrLine.setAddCaption("");

                                }



                                String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);

                                if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){

				%>

                                <table width="70%" border="0" cellspacing="2" cellpadding="0">

                                  <tr>

                                    <% if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>

                                    <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="20%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%></a></td>
                                    <!--<td width="6%"><a href="javascript:cmdAddItemReceive()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>-->
                                    <!--<td width="20%"><a href="javascript:cmdAddItemReceive()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item Receive",ctrLine.CMD_ADD,true)%></a></td>-->

                                    <% } %>
                                    <% if(iCommand==Command.SAVE) {%>
                                        <!--<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%>"></a></td>-->
                                        <!--<td width="20%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%></a></td>-->
                                   <% } %>
                                    <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_BACK,true)%></a></td>
                                  </tr>
                                </table>

                              <%

                                }else{

                                    out.println(strDrawImage);


                                }
                              %>                              </td>
                            </tr>
                               <!--for add new group-->
                               <tr>
                                 <% if(iCommand!=Command.DELETE)  {%>
                                  <td width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%>"></a></td>
                                  <td width="96%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Group",ctrLine.CMD_ADD,true)%></a>
                                   <% } %>  
                                <% if(iCommand==Command.EDIT && oidDfRecGroup!=0 ) {%>
                                  <a href="javascript:cmdAddItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a>
                                  <a href="javascript:cmdAddItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                               <%} %>
                               <!--</tr>-->
                              <!--end of add new group-->

                               <!--for add new item-->
                               <!--<tr>-->
                                  <td width="4%">&nbsp;</td>
                                  <td width="96%">&nbsp;</td>
                               </tr>
                              <!--end of add new item-->
                          </table>

                        </td>

                      </tr>

                      <tr>

                        <td colspan="3"> </td>

                      </tr>
                            
                     <tr>
                        
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
	});
</script>

<script language="JavaScript">
         document.frmvendorsearch.txt_materialname1.focus();
</script>
<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname1").autocomplete("list.jsp");
	});
</script>

<!-- #EndTemplate --></html>



