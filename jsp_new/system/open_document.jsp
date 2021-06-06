
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpname"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCosting"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturn"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatReturn"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatReceive"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.search.SrcPurchaseOrder"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseOrder"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command
                   ,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.form.admin.CtrlAppUser,
                   com.dimata.posbo.entity.admin.AppUser" %>
				   
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_POR);
%>

<%!
public static final String textListTitleHeader[][] =
{
	{"Sistem","Daftar Pemakai","ID Pemakai","Nama Lengkap","Status","Tambah Pemakai Baru"},
	{"System","User LIst","User ID","Full Name","Status","Add New User"}
};
public static final String textListMaterialHeaderPO[][] = 
{
	{"No","Kode","Tanggal","Supplier","Status","Keterangan","Mata Uang"},
	{"No","Code","Date","Supplier","Status","Remark","Currency"}
};
public static final String textListMaterialHeaderReceive[][] = {
	{"No","Kode","Tanggal","Mata Uang","Nomor PO","Supplier","Status","Keterangan"},
	{"No","Code","Date","Currency","PO Number","Supplier","Status","Description"}
};

public static final String textListMaterialHeaderReceiveFromTransfer[][] =  {
	{"No","Nomor","Tanggal","Penerimaan Dari","Lokasi Penerimaan","Status","Keterangan"},
	{"No","Number","Date","Receive From","Receive Location","Status","Remark"}
};

public static final String textListMaterialHeaderReturn[][] = {
	{"No","Kode","Tanggal","Nota Penerimaan","Suplier","Status","Keterangan"},
	{"No","Code","Date","Receipt","Supplier","Status","Remark"}
};

public static final String textListMaterialHeaderTransfer[][] = {
    {"No","Nomor","Tanggal","Lokasi Asal","Lokasi Tujuan","Status","Keterangan"},
    {"No","Number","Date","Dispatch From","Dispatch To","Status","Keterangan"}
};

public static final String textListMaterialHeaderCosting[][] = 
{
	{"No","Kode","Tanggal","Asal Pengiriman","Tujuan Pengiriman","Status"},
	{"No","Code","Date","Dispatch From","Dispatch To","Status"}
};

public static final String textListHeadersOpname[][] = {
	{"No","Nomor","Tanggal","Lokasi","Status","Keterangan","Status Item"},
	{"No","Number","Date","Location","Status","Remark","Status Item"}
};

public String drawListPO(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeaderPO[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeaderPO[language][1],"14%");
		ctrlist.addHeader(textListMaterialHeaderPO[language][2],"7%");
                ctrlist.addHeader(textListMaterialHeaderPO[language][6],"7%");
		ctrlist.addHeader(textListMaterialHeaderPO[language][3],"30%");
		ctrlist.addHeader(textListMaterialHeaderPO[language][4],"7%");
		ctrlist.addHeader(textListMaterialHeaderPO[language][5],"25%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0)
		{
			start = 0;		
		}	
		
		for(int i=0; i<objectClass.size(); i++){
			Vector vt = (Vector)objectClass.get(i);
			PurchaseOrder po = (PurchaseOrder)vt.get(0);
			ContactList contact = (ContactList)vt.get(1);		
			String cntName = contact.getCompName();					
			if(cntName.length()==0){
				cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());					
			}			
			start = start + 1;
			
			Vector rowx = new Vector();				
			rowx.add(""+start);
			rowx.add(po.getPoCode());
			
			String str_dt_PurchDate = ""; 
			try{
				Date dt_PurchDate = po.getPurchDate();
				if(dt_PurchDate==null){
					dt_PurchDate = new Date();
				}	
				str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
			}
			catch(Exception e){ str_dt_PurchDate = ""; }
	
			rowx.add(str_dt_PurchDate);

            // currency
            CurrencyType currType = new CurrencyType();
            try{
                currType = PstCurrencyType.fetchExc(po.getCurrencyId());
            }catch(Exception e){}
            rowx.add(currType.getCode());
			rowx.add(cntName);
                        if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_APPROVED){
                            rowx.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                        }else{
                            rowx.add(i_status.getDocStatusName(docType,po.getPoStatus()));
                        }  
			
                        rowx.add(po.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(po.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";		
	}
	return result;
}


public String drawListReceivePO(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeaderReceive[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeaderReceive[language][1],"14%");
		ctrlist.addHeader(textListMaterialHeaderReceive[language][2],"8%");
		ctrlist.addHeader(textListMaterialHeaderReceive[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeaderReceive[language][4],"14%");
		ctrlist.addHeader(textListMaterialHeaderReceive[language][5],"18%");
                ctrlist.addHeader(textListMaterialHeaderReceive[language][6],"8%");
                ctrlist.addHeader(textListMaterialHeaderReceive[language][7],"25%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start < 0)	{
			start = 0;		
		}	
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive rec = (MatReceive)vt.get(0);
			ContactList contact = (ContactList)vt.get(1);
			CurrencyType currencyType = (CurrencyType)vt.get(2);
			PurchaseOrder purchaseOrder = (PurchaseOrder)vt.get(3);
			
			String cntName = contact.getCompName();					
			if(cntName.length()==0) {
				cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());					
			}
			
			String str_dt_RecDate = ""; 
			try {
				Date dt_RecDate = rec.getReceiveDate();
				if(dt_RecDate==null) {
					dt_RecDate = new Date();
				}	
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
                        str_dt_RecDate = "";
                }
			
			Vector rowx = new Vector();				
			rowx.add(""+(start + i + 1));
			rowx.add(rec.getRecCode());
                        rowx.add(str_dt_RecDate);
                        rowx.add("<div align=\"center\">"+currencyType.getCode()+"</div>");
                                    rowx.add(purchaseOrder.getPoCode());
                        rowx.add(cntName);
			rowx.add(i_status.getDocStatusName(docType,rec.getReceiveStatus()));
			rowx.add(rec.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(rec.getOID()));
		}
		result = ctrlist.draw();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order penerimaan ...</div>";		
	}
	return result;
}



public String drawListReceiveFromDispatch(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][2],"15%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][3],"15%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][4],"15%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][5],"10%");
		ctrlist.addHeader(textListMaterialHeaderReceiveFromTransfer[language][6],"27%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start < 0) {
			start = 0;		
		}	
		PstLocation pstLocation= new PstLocation();
		Hashtable hashListLocation = pstLocation.getHashListLocation();
		String lokasiAsal = "";
		String lokasiTerima = "";
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive rec = (MatReceive)vt.get(0);
			Location loc = (Location)vt.get(1);		
			start = start + 1;
			lokasiAsal = (String)hashListLocation.get(String.valueOf(rec.getReceiveFrom()));
			lokasiTerima = (String)hashListLocation.get(String.valueOf(rec.getLocationId()));
			
			Vector rowx = new Vector();				
			rowx.add(""+start);
			rowx.add(rec.getRecCode());
			
			String str_dt_RecDate = ""; 
			try {
				Date dt_RecDate = rec.getReceiveDate();
				if(dt_RecDate==null) {
					dt_RecDate = new Date();
				}	
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
				str_dt_RecDate = "";
			}
	
			rowx.add(str_dt_RecDate);
			rowx.add(lokasiAsal);//loc.getName());
			rowx.add(lokasiTerima);			
			rowx.add(i_status.getDocStatusName(docType,rec.getReceiveStatus()));
			rowx.add(rec.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(rec.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp; Tidak ada data order penerimaan ...</div>";		
	}
	return result;
}


public String drawListReturn(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][2],"12%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][3],"15%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][4],"20%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][5],"10%");
		ctrlist.addHeader(textListMaterialHeaderReturn[language][6],"25%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start < 0) {
			start = 0;		
		}	
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReturn ret = (MatReturn)vt.get(0);
			ContactList contact = (ContactList)vt.get(1);
			MatReceive matReceive = (MatReceive)vt.get(2);
					
			String cntName = contact.getCompName();					
			if(cntName.length()==0)	{
				cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());					
			}
			
			String str_dt_RetDate = ""; 
			try	{
				Date dt_RetDate = ret.getReturnDate();
				if(dt_RetDate==null) {
					dt_RetDate = new Date();
				}	
				str_dt_RetDate = Formater.formatDate(dt_RetDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
				str_dt_RetDate = "";
			}
						
			Vector rowx = new Vector();				
			rowx.add(""+(start + 1 + i));
			rowx.add(ret.getRetCode());
			rowx.add(str_dt_RetDate);
			rowx.add(matReceive.getInvoiceSupplier());
			rowx.add(cntName);			
			rowx.add(i_status.getDocStatusName(docType,ret.getReturnStatus()));
			rowx.add(ret.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(ret.getOID()));
		}
		result = ctrlist.draw();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data retur ...</div>";		
	}
	return result;
}



public String drawListTransfer(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][1],"15%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][2],"10%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][3],"20%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][4],"20%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][5],"10%");
        ctrlist.addHeader(textListMaterialHeaderTransfer[language][6],"10%");
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        if(start<0) {
            start = 0;
        }
        for(int i=0; i<objectClass.size(); i++) {
            Vector rowx = new Vector();
            Vector vt = (Vector)objectClass.get(i);
            MatDispatch df = (MatDispatch)vt.get(0);
            Location loc1 = (Location)vt.get(1);
            Location loc2 = new Location();
            try {
                loc2 = PstLocation.fetchExc(df.getDispatchTo());
            } catch(Exception e) {
            }
            start += 1;
            
            rowx.add(""+start);
            rowx.add(df.getDispatchCode());
            rowx.add(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"));
            rowx.add(loc1.getName());
            rowx.add(loc2.getName());
            rowx.add(i_status.getDocStatusName(docType,df.getDispatchStatus()));
            rowx.add(df.getRemark());

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(df.getOID()));
        }
        result = ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data pengiriman ...</div>";
    }
    return result;
}


public String drawListCosting(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][2],"10%");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][3],"20%");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][4],"20%");
		ctrlist.addHeader(textListMaterialHeaderCosting[language][5],"10%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0){
			start = 0;		
		}	
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			MatCosting df = (MatCosting)vt.get(0);
			Location loc1 = (Location)vt.get(1);
			Location loc2 = new Location();
			try
			{
				loc2 = PstLocation.fetchExc(df.getCostingTo());
			}
			catch(Exception e)
			{
			}
			start += 1;
			
			rowx.add(""+start);
			rowx.add(df.getCostingCode());
			rowx.add(Formater.formatDate(df.getCostingDate(), "dd-MM-yyyy"));
			rowx.add(loc1.getName());			
			rowx.add(loc2.getName());			
			rowx.add(i_status.fieldDocumentStatus[df.getCostingStatus()]); //getDocStatusName(docType,df.getCostingStatus()));
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(df.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListMaterialHeaderCosting[language][4]+"</div>";		
	}
	return result;
}


public String drawListOpname(Vector objectClass, int start, int language, int docType, I_DocStatus i_status, int iStatusItem) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {	
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListHeadersOpname[language][0],"3%");
		ctrlist.addHeader(textListHeadersOpname[language][1],"15%");
		ctrlist.addHeader(textListHeadersOpname[language][2],"10%");
		ctrlist.addHeader(textListHeadersOpname[language][3],"25%");
		ctrlist.addHeader(textListHeadersOpname[language][4],"7%");
                ctrlist.addHeader(textListHeadersOpname[language][6],"7%");
		ctrlist.addHeader(textListHeadersOpname[language][5],"40%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start<0)
			start = 0;
	
		for (int i = 0; i < objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			Vector rowx = new Vector();
			start = start + 1;			
			MatStockOpname matStockOpname = (MatStockOpname)vt.get(0);
			Location location = (Location)vt.get(1);
			
                        int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                        int getCountSameMaterial = SessMatStockOpname.getCountSameMaterialList(matStockOpname.getOID());

                        rowx.add(""+start);
			String str_dt_OpnameDate = ""; 
			try	{
				Date dt_OpnameDate = matStockOpname.getStockOpnameDate();
				if(dt_OpnameDate==null)	{
					dt_OpnameDate = new Date();
				}
				str_dt_OpnameDate = Formater.formatDate(dt_OpnameDate, "dd-MM-yyyy");
			}
			catch(Exception e) { 
				str_dt_OpnameDate = ""; 
			}
			
			rowx.add(matStockOpname.getStockOpnameNumber());
			rowx.add(str_dt_OpnameDate);
			rowx.add(location.getName());
			rowx.add(i_status.getDocStatusName(docType,matStockOpname.getStockOpnameStatus()));

                        //status opname item
                        //int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                        if(getCounter > 1 || getCountSameMaterial > 1 ){
                            rowx.add("Double");
                        } else {
                            rowx.add("Clean");
                        }
                        
			rowx.add(matStockOpname.getRemark());

                       
			 lstData.add(rowx);
                        
			lstLinkData.add(String.valueOf(matStockOpname.getOID()));
                     }
                      
		result = ctrlist.draw();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListHeadersOpname[language][5]+"</div>";
	}
	return result;	
}
%>
<%

/* VARIABLE DECLARATION */
int recordToGet = 10;
String order = " " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID];
int oidSelectedDocument = FRMQueryString.requestInt(request, "index_document");
String no_document = FRMQueryString.requestString(request, "no_document");
int iCommand = FRMQueryString.requestCommand(request);
Vector records = new Vector();
ControlLine ctrLine = new ControlLine();
SrcMatReceive srcMatReceive = new SrcMatReceive();
if(iCommand==Command.SEARCH){
    if(!no_document.equals("")){
        no_document = no_document.trim();
    }
    switch (oidSelectedDocument) {
         case 1://po
             SrcPurchaseOrder srcPurchaseOrder = new SrcPurchaseOrder();
             srcPurchaseOrder.setPrmnumber(no_document);
             records = SessPurchaseOrder.searchPurchaseOrderMaterial(srcPurchaseOrder,0,0,100,"");
             break;
         case 2://receive from purchase
             srcMatReceive.setReceivenumber(no_document);
             records = SessMatReceive.searchMatReceiveSupplier(srcMatReceive,0,recordToGet,"");
             break;
         case 3://receive from transfer
             srcMatReceive.setReceivenumber(no_document);
             records = SessMatReceive.searchMatReceiveNonSupplier(srcMatReceive,0,recordToGet,"");    
             break;    
         case 4://dispatch/transfer
             break;
         case 5://return
             SrcMatReturn srcMatReturn = new SrcMatReturn();
             srcMatReturn.setReturnnumber(no_document);
             records = SessMatReturn.searchMatReturn(srcMatReturn,0,recordToGet,"");     
             break;    
         case 6://transfer
             SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
             srcMatDispatch.setDispatchCode(no_document);
             records = SessMatDispatch.listMatDispatch(srcMatDispatch,0,recordToGet,"");
             break;
         case 7://costing
             SrcMatCosting srcMatCosting = new SrcMatCosting();
             srcMatCosting.setCostingCode(no_document);
             srcMatCosting.setIgnoreDate(true);
             records = SessMatCosting.listMatCosting(srcMatCosting,0,recordToGet);
             break;   
         case 8://opname
             SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
             srcMatStockOpname.setOpnameNumber(no_document);
             records = SessMatStockOpname.searchMatStockOpname(srcMatStockOpname,0, recordToGet);
             break;       
         default:
    }
}

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdEditxx(oid){
	document.frmAppUser.user_oid.value=oid;
	document.frmAppUser.command.value="<%=Command.EDIT%>";
	document.frmAppUser.action="useredit.jsp";
	document.frmAppUser.submit();
}

function cmdEdit(oid) {
    var strvalue ="history_open_document.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid+"&index_document=<%=oidSelectedDocument%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdSearch(){
	document.frmAppUser.command.value="<%=Command.SEARCH%>";
	document.frmAppUser.action="open_document.jsp";
	document.frmAppUser.submit();
}

function cmdListPrev(){
	document.frmAppUser.command.value="<%=Command.PREV%>";
	document.frmAppUser.list_command.value="<%=Command.PREV%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

function cmdListNext(){
	document.frmAppUser.command.value="<%=Command.NEXT%>";
	document.frmAppUser.list_command.value="<%=Command.NEXT%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

function cmdListLast(){
	document.frmAppUser.command.value="<%=Command.LAST%>";
	document.frmAppUser.list_command.value="<%=Command.LAST%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
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
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListTitleHeader[SESS_LANGUAGE][0]%>
            > <%=textListTitleHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmAppUser" method="post" action="">
              <input type="hidden" name="command" value="">
              <table width="100%" cellspacing="0" cellpadding="0">
                <tr> 
                  <td colspan="2" class="listtitle">
                    <hr size="1">
                  </td>
                </tr>
              </table>
              <table width="100%" cellpadding="0" border="0" cellspacing="2">
                  <tr>
                    <td width="8%">Document Type</td>
                    <td width="2%">:</td>
                    <%  
                        Vector doc_value = new Vector();
                        Vector doc_key = new Vector();
                        if(SESS_LANGUAGE==1){
                            doc_key.add("Purchase");
                            doc_value.add(""+1);
                            doc_key.add("Receive from Purchase");
                            doc_value.add(""+2);
                            doc_key.add("Receive from Dispatch");
                            doc_value.add(""+3);
                            doc_key.add("Return");
                            doc_value.add(""+5);
                            doc_key.add("Dispatch");
                            doc_value.add(""+6);
                            doc_key.add("Costing");
                            doc_value.add(""+7);
                            doc_key.add("Opname");
                            doc_value.add(""+8);
                        }else{
                            doc_key.add("Pembelian");
                            doc_value.add(""+1);
                            doc_key.add("Penerimaan dari Pembelian");
                            doc_value.add(""+2);
                            doc_key.add("Penerimaan dari Transfer");
                            doc_value.add(""+3);
                            doc_key.add("Return");
                            doc_value.add(""+5);
                            doc_key.add("Transfer");
                            doc_value.add(""+6);
                            doc_key.add("Pembiayaan");
                            doc_value.add(""+7);
                            doc_key.add("Opname");
                            doc_value.add(""+8);
                        }
                    %>
                    <td width="90%">
                        <%= ControlCombo.draw("index_document", null, ""+oidSelectedDocument,doc_value,doc_key,"")%>
                    </td>
                  </tr>
                  <tr>
                    <td width="8%">No Document</td>
                    <td width="2%">:</td>
                    <td width="90%">
                        <input type="text" name="no_document" value="<%=no_document%>" class="formElemen">
                    </td>
                  </tr> 
                  <tr>
                    <td colspan="2"> 
                    <td width="90%">
                         <table width="20%" border="0" cellspacing="0" cellpadding="0">
                             <tr><br>
                              <!--td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_SEARCH,true)%>"></a></td-->
                              <td class="command" ><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_SEARCH,true)%></a></td>
                               <%if(privAdd){%>
                                <!--td nowrap width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ADD,true)%>"></a></td-->
                                <td class="command" ><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ADD,true)%></a></td>
                                <%}%>
                            </tr>
                      </table>
                    </td>
                  </tr> 
                  <tr>
                      <!--td colspan="3">nbps;</td-->
                  </tr>
                  <tr>
                    <td colspan="2"> 
                    <td width="90%">
                         <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <%
                               if(iCommand==Command.SEARCH){
                                    switch (oidSelectedDocument) {
                                         case 1://po
                                             %>
                                             <%=drawListPO(SESS_LANGUAGE,records,0,0,i_status)%>
                                             <%
                                             break;
                                         case 2://receive
                                             %>
                                             <%=drawListReceivePO(SESS_LANGUAGE,records,0,docType,i_status)%>
                                             <%
                                             break;
                                         case 3://receive transfer
                                             %>
                                             <%=drawListReceiveFromDispatch(SESS_LANGUAGE,records,0,docType,i_status)%>
                                             <%
                                             break;    
                                         case 4://dispatch/transfer
                                             break;
                                         case 5://return
                                             %>
                                             <%=drawListReturn(SESS_LANGUAGE,records,0,docType,i_status)%>
                                             <%
                                             break;    
                                         case 6://transfer
                                             %>
                                             <%=drawListTransfer(SESS_LANGUAGE,records,0,docType,i_status)%>
                                             <%
                                             break;
                                         case 7://costing
                                             %>
                                             <%=drawListCosting(SESS_LANGUAGE,records,0,docType,i_status)%>
                                             <%
                                             break;      
                                         case 8://opname 
                                             %>
                                             <%=drawListOpname(records,0,SESS_LANGUAGE,docType,i_status,0)%>
                                             <%
                                             break;     
                                         default:
                                    }
                                }
                              %>  
                              
                            </tr>
                      </table>
                    </td>
                  </tr> 
              </table>
            </form>
            <script language="JavaScript">
            <!--
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
            //-->
        </script>
            <link rel="stylesheet" href="../css/default.css" type="text/css">
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>