<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

                   com.dimata.posbo.form.warehouse.*,

                   com.dimata.posbo.entity.warehouse.*,

                   com.dimata.posbo.entity.masterdata.Material,

                    com.dimata.posbo.session.masterdata.SessMaterial,

                   com.dimata.posbo.entity.masterdata.Unit" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.entity.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package common -->

<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ page import = "com.dimata.common.entity.contact.*" %>

<!--package material -->

<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>



<%@ include file = "../../../main/javainit.jsp" %>

<% //int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING); %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>

<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->

<%!

public static final String textListGlobal[][] = {

    {"Transfer","Edit","Tidak ada item transfer","Cetak Transfer","Proses transfer tidak dapat dilakukan pada lokasi yang sama"},

    {"Dispatch","Edit","There is no Dispatch item","Print Dispatch","Transfer cant'be proceed in same location"}

};

/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] = 

{

	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan"},

	{"No","Location","Destination","Date","Status","Remark"}	

};



/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =

{

    {"No","Sku","Nama Barang","Unit","Qty","Group","Source","Target","@Nilai Stok", "Total"},

    {"No","Code","Goods Name","Unit","Qty","Group","Source","Target","@Stock Value", "Total"}

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


public Vector drawListGroupItem(int language,Vector objectClass,int start) {

    Vector list = new Vector(1,1);

    Vector listError = new Vector(1,1);

    String result = "";

    if(objectClass!=null && objectClass.size()>0) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");

        ctrlist.setListStyle("listgen");

        ctrlist.setTitleStyle("listgentitle");

        ctrlist.setCellStyle("listgensell");

        ctrlist.setHeaderStyle("listgentitle");
        //ctrlist.setHeaderStyle("formElemen");
        ctrlist.setBorder(1);
        ctrlist.setCellSpacing("1");

        //ctrlist.dataFormat(textListOrderItem[language][5], "45%", "0","textListOrderItem[language][7]" , "left", "left");

        //ctrlist.addHeader(textListOrderItem[language][5],"5%");
       //ctrlist.addHeader(textListOrderItem[language][6],"45%");
        //ctrlist.addHeader(textListOrderItem[language][7],"45%");

        ctrlist.dataFormat(textListOrderItem[language][5],"5%","center","bottom");
        ctrlist.dataFormat(textListOrderItem[language][6],"45%","center","bottom");
        ctrlist.dataFormat(textListOrderItem[language][7],"45%","center","bottom");
        
        //ctrlist.addHeader(textListOrderItem[language][1],"10%");
        //ctrlist.addHeader(textListOrderItem[language][2],"30%");
        //ctrlist.addHeader(textListOrderItem[language][3],"5%");
       //ctrlist.addHeader(textListOrderItem[language][4],"5%");

        //Tambahan header untuk target
        //ctrlist.addHeader(textListOrderItem[language][0],"3%");
        //ctrlist.addHeader(textListOrderItem[language][1],"45%");
        //ctrlist.addHeader(textListOrderItem[language][2],"40%");
        //ctrlist.addHeader(textListOrderItem[language][3],"5%");
        //ctrlist.addHeader(textListOrderItem[language][4],"5%");



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
            //start = start + 1;
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            String order="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
            Vector listItem = PstMatDispatchReceiveItem.list(0,0,dfRecItem.getDfRecGroupId(),order);
            rowx.add(drawListSourceItem(language, listItem, start));
            rowx.add(drawListTargetItem(language, listItem, start));


            lstData.add(rowx);

        }

        result = ctrlist.draw();

    }else{

        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][2]+"</div>";

    }



    list.add(result);

    list.add(listError);

    return list;

}

/*
 * Drawlist Source Item
 * By Mirahu
*/
public String drawListSourceItem(int language,Vector objectClass,int start) {

    //Vector list = new Vector(1,1);

    //Vector listError = new Vector(1,1);

    String result = "";

    if(objectClass!=null && objectClass.size()>0) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");

        ctrlist.setListStyle("listgen");

        ctrlist.setTitleStyle("listgentitle");

        ctrlist.setCellStyle("listgensell");

        ctrlist.setHeaderStyle("listgentitle");
        //ctrlist.setHeaderStyle("formElemen");
        ctrlist.setBorder(1);
        ctrlist.setCellSpacing("1");

        //ctrlist.addHeader(textListOrderItem[language][0],"3%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"30%");
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        ctrlist.addHeader(textListOrderItem[language][4],"5%");
        //+Header Nilai Stok
       //by Mirahu
        ctrlist.addHeader(textListOrderItem[language][8],"5%");
        ctrlist.addHeader(textListOrderItem[language][9],"5%");

        Vector lstData = ctrlist.getData();

        Vector lstLinkData = ctrlist.getLinkData();

        Vector rowx = new Vector(1,1);

        ctrlist.reset();

        int index = -1;

        if(start<0)

            start=0;



        /**

         * get data receive for get price cost

         */

        /*String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoiceNumber+"'";

        Vector list = PstMatReceive.list(0,0,whereClause,"");

        Vector listItem = new Vector(1,1);

        if(list!=null && list.size()>0){

            MatReceive matReceive = (MatReceive)list.get(0);

            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceive.getOID();

            listItem = PstMatReceiveItem.list(0,0,whereClause,"");

        }*/



        for(int i=0; i<objectClass.size(); i++){
            MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
           // Vector temp = (Vector)objectClass.get(i);
           // MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)temp.get(0);
           // MatDispatchItem dfItem = (MatDispatchItem)temp.get(1);
           // Material mat = (Material)temp.get(2);
           // Unit unit = (Unit)temp.get(3);
           // MatReceiveItem recItem = (MatReceiveItem)temp.get(4);
            rowx = new Vector();
            //start = start + 1;
          if(dfRecItem.getSourceItem().getMaterialId()!=0){
            //rowx.add(start+i+1);
            //rowx.add("<div align=\"left\">"+(start+i+1)+"</div>");
            //rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfRecItem.getDfRecGroupId())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            //rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfRecItem.getOID())+"')\">"+dfRecItem.getSourceItem().getMaterialSource().getSku()+"</a>");
            rowx.add(dfRecItem.getSourceItem().getMaterialSource().getSku());
            rowx.add(dfRecItem.getSourceItem().getMaterialSource().getName());
            rowx.add(dfRecItem.getSourceItem().getUnitSource().getCode());

           // if(dfRecItem.getSourceItem().getMaterialSource().getRequiredSerialNumber()==PstMaterial.REQUIRED){
             // String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfRecItem.getSourceItem().getOID();

             // int cnt = PstDispatchStockCode.getCount(where);

                //if(cnt<dfRecItem.getSourceItem().getQty()){

                 //  if(listError.size()==0){

                     //  listError.add("Silahkan cek :");

                 //  }

                 // listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getSourceItem().getMaterialSource().getName()+" tidak sama dengan qty pengiriman");

             // }

              // rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getSourceItem().getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty())+"</div>");

         // }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty()));
                //+nilai stok dan total
                // by Mirahu
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHpp()));
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getHppTotal()));

        // }
            lstData.add(rowx);
        }

      }

        result = ctrlist.draw();

    }else{

        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][2]+"</div>";

    }



    //list.add(result);

    //list.add(listError);

    //return list;
      return result;

}

//End of DrawList Item

/*
 * Drawlist Target Item
 * By Mirahu
*/
public String drawListTargetItem(int language,Vector objectClass,int start) {

    //Vector list = new Vector(1,1);

    //Vector listError = new Vector(1,1);

    String result = "";

    if(objectClass!=null && objectClass.size()>0) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");

        ctrlist.setListStyle("listgen");

        ctrlist.setTitleStyle("listgentitle");

        ctrlist.setCellStyle("listgensell");

        ctrlist.setHeaderStyle("listgentitle");
        //ctrlist.setHeaderStyle("formElemen");
        ctrlist.setBorder(1);
        ctrlist.setCellSpacing("1");

        //Tambahan header untuk target
        //ctrlist.addHeader(textListOrderItem[language][0],"3%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"40%");
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        ctrlist.addHeader(textListOrderItem[language][4],"5%");
        //+nilai stok
        //mirahu
        ctrlist.addHeader(textListOrderItem[language][8],"5%");
        ctrlist.addHeader(textListOrderItem[language][9],"5%");



        Vector lstData = ctrlist.getData();

        Vector lstLinkData = ctrlist.getLinkData();

        Vector rowx = new Vector(1,1);

        ctrlist.reset();

        int index = -1;

        if(start<0)

            start=0;



        /**

         * get data receive for get price cost

         */

        /*String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoiceNumber+"'";

        Vector list = PstMatReceive.list(0,0,whereClause,"");

        Vector listItem = new Vector(1,1);

        if(list!=null && list.size()>0){

            MatReceive matReceive = (MatReceive)list.get(0);

            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceive.getOID();

            listItem = PstMatReceiveItem.list(0,0,whereClause,"");

        }*/



        for(int i=0; i<objectClass.size(); i++){
            MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)objectClass.get(i);
           // Vector temp = (Vector)objectClass.get(i);
           // MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem)temp.get(0);
           // MatDispatchItem dfItem = (MatDispatchItem)temp.get(1);
           // Material mat = (Material)temp.get(2);
           // Unit unit = (Unit)temp.get(3);
           // MatReceiveItem recItem = (MatReceiveItem)temp.get(4);
            rowx = new Vector();
            start = start + 1;

           //Tambahan Untuk Target
              if(dfRecItem.getTargetItem().getMaterialId()!=0){
            //rowx.add(start+i+1);
            //rowx.add("<div align=\"left\">"+(start+i+1)+"</div>");
            //rowx.add(""+start+"");

            //rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfRecItem.getOID())+"')\">"+dfRecItem.getTargetItem().getMaterialTarget().getSku()+"</a>");
            rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getSku());
            rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getName());
            rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());


            //if(dfRecItem.getTargetItem().getMaterialTarget().getRequiredSerialNumber()==PstMaterial.REQUIRED){
                //String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+dfRecItem.getTargetItem().getOID();
                 //int cnt = PstReceiveStockCode.getCount(where);
                  //if(cnt<dfRecItem.getTargetItem().getQty()){

                  //if(listError.size()==0){

                     //listError.add("Silahkan cek :");

                  //}

                 //listError.add(""+listError.size()+". Jumlah serial kode stok "+dfRecItem.getTargetItem().getMaterialTarget().getName()+" tidak sama dengan qty penerimaan");

            // }

             // rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfRecItem.getTargetItem().getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty())+"</div>");


         // }else{
              rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty()));
              rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getCost()));
              rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getTotal()));

           // }


            lstData.add(rowx);
           }

        }

        result = ctrlist.draw();

    }else{

        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][2]+"</div>";

    }



    //list.add(result);

    //list.add(listError);

    //return list;
      return result;

}

//End of DrawList Item

/**

* this method used to maintain dfList

*/

public String drawListDfItem(int language,int iCommand,

                             FrmMatDispatchItem frmObject,MatDispatchItem objEntity,

                             Vector objectClass,long dfItemId,int start, String invoiceNumber,int tranUsedPriceHpp)

{

	ControlList ctrlist = new ControlList();

	ctrlist.setAreaWidth("100%");

	ctrlist.setListStyle("listgen");

	ctrlist.setTitleStyle("listgentitle");

	ctrlist.setCellStyle("listgensell");

	ctrlist.setHeaderStyle("listgentitle");

	ctrlist.addHeader(textListOrderItem[language][0],"3%");

	ctrlist.addHeader(textListOrderItem[language][1],"15%");

	ctrlist.addHeader(textListOrderItem[language][2],"20%");

	ctrlist.addHeader(textListOrderItem[language][3],"5%");

	ctrlist.addHeader(textListOrderItem[language][4],"5%");

	if(tranUsedPriceHpp==0)

    	ctrlist.addHeader(textListOrderItem[language][5],"10%");

	else

		ctrlist.addHeader(textListOrderItem[language][7],"10%");

		

    ctrlist.addHeader(textListOrderItem[language][6],"10%");



	Vector lstData = ctrlist.getData();

	Vector rowx = new Vector(1,1);

	ctrlist.reset();

	ctrlist.setLinkRow(1);

	int index = -1;

	if(start<0)

	   start=0;



    for(int i=0; i<objectClass.size(); i++){

		Vector temp = (Vector)objectClass.get(i);

		MatDispatchItem dfItem = (MatDispatchItem)temp.get(0);

		Material mat = (Material)temp.get(1);

		Unit unit = (Unit)temp.get(2);

		rowx = new Vector();

		start = start + 1;



        double cost = 0; //getPriceCost(listItem,dfItem.getMaterialId());



		rowx.add("<div align=\"center\">"+start+"</div>");

		rowx.add(mat.getSku());

		rowx.add(mat.getName());

		rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");

		rowx.add("<div align=\"center\">"+String.valueOf(dfItem.getQty())+"</div>");

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(cost)+"</div>");

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");



		lstData.add(rowx);

	}	

	return ctrlist.draw();

}



%>

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

int startItem = FRMQueryString.requestInt(request,"start_item");

int prevCommand = FRMQueryString.requestInt(request,"prev_command");

int appCommand = FRMQueryString.requestInt(request,"approval_command");

long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");

long oidDispatchMaterialItem = FRMQueryString.requestLong(request,"hidden_dispatch_item_id");



int typePrint = FRMQueryString.requestInt(request,"type_print_tranfer");



/**

* initialization of some identifier

*/

int iErrCode = FRMMessage.NONE;

String msgString = "";



/**

* purchasing pr code and title

*/

String dfCode = ""; //i_pstDocType.getDocCode(docType);

String dfTitle = "Transfer Unit Barang"; //i_pstDocType.getDocTitle(docType);

String dfItemTitle = dfTitle + " Item";



/**

* purchasing pr code and title

*/

String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));





/**

* process on df main

*/

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

iErrCode = ctrlMatDispatch.action(Command.EDIT,oidDispatchMaterial);

FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

MatDispatch dispatch = ctrlMatDispatch.getMatDispatch();



/**

* check if document may modified or not 

*/

boolean privManageData = true; 



ControlLine ctrLine = new ControlLine();

CtrlMatDispatchItem ctrlMatDispatchItem = new CtrlMatDispatchItem(request);

ctrlMatDispatchItem.setLanguage(SESS_LANGUAGE);

iErrCode = ctrlMatDispatchItem.action(iCommand,oidDispatchMaterialItem,oidDispatchMaterial);

FrmMatDispatchItem frmMatDispatchItem = ctrlMatDispatchItem.getForm();

MatDispatchItem dispatchItem = ctrlMatDispatchItem.getMatDispatchItem();

msgString = ctrlMatDispatchItem.getMessage();



String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;

String orderClauseItem = "";

int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);

int recordToGetItem =0;



if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)

{

	startItem = ctrlMatDispatchItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);

} 

//Update by Mirah
String order="DFRI."+PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
//Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.list(startItem,recordToGetItem,oidDispatchMaterial,order);
Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem,recordToGetItem,oidDispatchMaterial,order);
if(listMatDispatchReceiveItem.size()<1 && startItem>0)

{

	 if(vectSizeItem-recordToGetItem > recordToGetItem)

	 {

		startItem = startItem - recordToGetItem;   

	 }

	 else

	 {

		startItem = 0 ;

		iCommand = Command.FIRST;

		prevCommand = Command.FIRST; 

	 }

	 listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem,recordToGetItem,oidDispatchMaterial,order);

}

%>

<html><!-- #BeginTemplate "/Templates/print.dwt" -->

<head>

<!-- #BeginEditable "doctitle" --> 

<title>Dimata - ProChain POS</title>

<!-- #EndEditable -->

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

<!-- #BeginEditable "stylestab" --> 

<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

<link rel="stylesheet" href="../../../styles/print.css" type="text/css">

<!-- #EndEditable -->

</head>  

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    

<table width="100%" border="0" cellspacing="0" cellpadding="0" >

  <tr>  

    <td width="88%" valign="top" align="left" height="56"> 

      <table width="100%" border="0" cellspacing="0" cellpadding="0">  

        <tr> 

          <td><!-- #BeginEditable "content" --> 

            <form name="frm_matdispatch" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">

              <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">

              <input type="hidden" name="approval_command" value="<%=appCommand%>">

              <table width="100%" cellpadding="1" cellspacing="0">

                <tr> 

                  <td valign="top" colspan="3">&nbsp; </td>

                </tr>

                <tr align="center"> 

                  <td colspan="3" class="title"><table width="100%" border="0" cellpadding="1">

                    <tr>

                      <td class="title" align="left" width="15%"><img src="../../../images/company.jpg" alt="cd"></td>

                      <td class="title" align="center" width="70%"><b>&nbsp;<%=dfTitle.toUpperCase()%></b></td>

                      <td width="15%"></td>

                    </tr>

                  </table>                    <b>&nbsp;</b></td>

                </tr>

                <tr align="center"> 

                  <td colspan="3" align="center"> 

                    <table width="100%" border="0" cellpadding="1">

                      <tr> 

                        <td width="9%" align="left" nowrap><%=dfCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="35%" align="left"> : <%=dispatch.getDispatchCode()%> </td>

                        <td align="center" valign="bottom" width="18%">&nbsp; </td>

                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : 

                          <% 

						  Location loc1 = new Location();

						  try{

						  	loc1 = PstLocation.fetchExc(dispatch.getDispatchTo());

						  }catch(Exception e){}

						  %>

                          <%=loc1.getName()%> 

                      </tr>

                      <tr> 

                        <td width="9%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td width="35%" align="left">: <%=Formater.formatDate(dispatch.getDispatchDate(),"dd MMMM yyyy")%> </td>

                        <td align="center" valign="bottom" width="18%"> 

                          <%//=strComboStatus%>

                        </td>

                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][1]%> : 

                          <%   

						  Location loc2 = new Location();

						  try{

						  	loc2 = PstLocation.fetchExc(dispatch.getLocationId());

						  }catch(Exception e){}

						  %>

                          <%=loc2.getName()%> 

                      </tr>

                    </table>

                  </td>

                </tr>

                <tr> 

                  <td valign="top"> 

                    <table width="100%" border="0" cellspacing="0" cellpadding="0" >

                      <tr align="left" valign="top">

                        <td height="22" valign="middle" colspan="3">

                       <%

                        //Vector list = drawListOrderItem(SESS_LANGUAGE,listMatDispatchReceiveItem,startItem);
                          Vector list = drawListGroupItem(SESS_LANGUAGE,listMatDispatchReceiveItem,startItem);

                        out.println(""+list.get(0));

                        Vector listError = (Vector)list.get(1);

                        %>

                        </td>

                      </tr>

                      <tr align="left" valign="top">

                        <td height="22" valign="middle" colspan="3"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=dispatch.getRemark()%></td>

                      </tr>

                      <tr align="left" valign="top"> 

                        <td height="40" valign="middle" colspan="3"></td>

                      </tr>

                      <tr> 

                        <td width="34%" align="center" nowrap>Dibuat Oleh,</td>

                        <!--<td align="center" valign="bottom" width="33%">Pengirim,</td>

                        <td width="33%" align="center">Penerima,</td>-->

                      </tr>

                      <tr align="left" valign="top"> 

                        <td height="75" valign="middle" colspan="3"></td>

                      </tr>

                      <tr> 

                        <td width="34%" align="center" nowrap> (.................................)

                        </td>

                        <!--<td align="center" valign="bottom" width="33%"> (.................................)

                        </td>

                        <td width="33%" align="center"> (.................................) 

                        </td>-->

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

</table>

</body>

<!-- #EndTemplate --></html>

