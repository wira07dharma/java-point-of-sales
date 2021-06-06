<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.entity.warehouse.MatConStockOpnameItem,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.posbo.form.warehouse.CtrlMatConStockOpname,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.posbo.form.warehouse.FrmMatConStockOpname,
                   com.dimata.posbo.entity.warehouse.MatConStockOpname,
                   com.dimata.posbo.entity.warehouse.PstMatConStockOpnameItem,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.posbo.entity.masterdata.*" %>
				   
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = 
{
	{"Nomor","Lokasi","Tanggal","Jam","Status","Supplier","Kategori","Sub Kategori","Keterangan"},
	{"No","Location","Date","Time","Status","Supplier","Category","Sub Category","Remark"}	
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = 
{
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname"},
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname"}
};
public static final String textListPrintHeader[][] = 
{
	{"Opname Barang","Semua Kategori","Semua Sub Kategori","Dibuat Oleh","Disetujui Oleh","Diketahui Oleh","Stok opname item masih kosong .."},
	{"Goods Opname","All Category","All Sub Category","Created by","Approved by","Approved by","Stock opname item still empty .."}
};

/**
* this method used to list all stock opname item
*/
public String drawListOpnameItem(int language,Vector objectClass,int start)
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
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"15%");
		ctrlist.addHeader(textListOrderItem[language][2],"20%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		ctrlist.addHeader(textListOrderItem[language][4],"15%");
		ctrlist.addHeader(textListOrderItem[language][5],"15%");
		ctrlist.addHeader(textListOrderItem[language][6],"5%");
	
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
		{
			start=0;
		}	
		
		for(int i=0; i<objectClass.size(); i++)
		{
			 Vector temp = (Vector)objectClass.get(i);
			 MatConStockOpnameItem soItem = (MatConStockOpnameItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 Category cat = (Category)temp.get(3);
			 SubCategory scat = (SubCategory)temp.get(4);
			 rowx = new Vector();
			 start = start + 1;
	
			 rowx.add(""+start+"");
			 rowx.add(mat.getSku());
			 rowx.add(mat.getName());
			 rowx.add(unit.getCode());			 
			 rowx.add(cat.getName());
			 rowx.add(scat.getName());			 
			 rowx.add("<div align=\"right\">"+String.valueOf(soItem.getQtyOpname())+"</div>");			 			 
	
			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListPrintHeader[language][6]+"</div>";		
	}
	return result;
}
	
%>
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
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
long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String soCode ="";// i_pstDocType.getDocCode(docType);
String opnTitle = textListPrintHeader[SESS_LANGUAGE][0]; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatConStockOpname ctrlMatConStockOpname = new CtrlMatConStockOpname(request);
iErrCode = ctrlMatConStockOpname.action(iCommand , oidStockOpname);
FrmMatConStockOpname frmso = ctrlMatConStockOpname.getForm();
MatConStockOpname so = ctrlMatConStockOpname.getMatConStockOpname();
errMsg = ctrlMatConStockOpname.getMessage();

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
//String priceCode = "Rp.";
/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/** 
* check if document may modified or not 
*/
boolean privManageData = false; 

/**
* list purchase order item
*/
oidStockOpname = so.getOID();
int recordToGetItem = 20;
int vectSizeItem = PstMatConStockOpnameItem.getCount(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
Vector listMatConStockOpnameItem = PstMatConStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);


%>
<html><!-- #BeginTemplate "/Templates/print.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../../../styles/print.css" type="text/css">
<!-- #EndEditable -->
</head>  
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_matopname" method="post" action="">
<%
try
{
%>
              <input type="hidden" name="command" value="">
			  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">			  
              <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">			  			  
              <input type="hidden" name="hidden_opname_item_id" value="">
              <input type="hidden" name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
              <table width="100%" border="0">
                <tr align="center"> 
                  <td colspan="3" class="title"><b><%=opnTitle.toUpperCase()%></b></td>
                </tr>
                <tr> 
                  <td colspan="3" valign="top"> <table width="100%" border="0" cellpadding="1">
                      <tr> 
                        <td width="33%" align="left"> <%
						  if(so.getStockOpnameNumber()!="" && iErrCode==0)
						  {
							out.println(textListOrderHeader[SESS_LANGUAGE][0]+" : "+so.getStockOpnameNumber());
						  }
						  else
						  {
						  	out.println("");
						  }
						  %> </td>
                        <td align="center" valign="bottom">&nbsp;</td>
                        <td width="33%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : <%=Formater.formatDate(so.getStockOpnameDate(),"dd MMMM yyyy")%> </tr>
                      <tr> 
                        <td width="33%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%> : 
                          <% 
						  Location loc1 = new Location();
						  try{
						  	loc1 = PstLocation.fetchExc(so.getLocationId());
						  }catch(Exception e){}
						  %> <%=loc1.getName()%> </td>
                        <td align="center" valign="bottom"> <%//=strComboStatus%> </td>
                        <td width="33%" align="right"></td></tr>
                      <tr> 
                        <td width="33%" align="left"> <%=textListOrderHeader[SESS_LANGUAGE][5]%> : 
                          <% 
							ContactList cnt = new ContactList();
							try{
								cnt = PstContactList.fetchExc(so.getSupplierId());
							}catch(Exception e){}
							String cntName = cnt.getCompName();
							if(cntName.length()==0){
								cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
							}
						  %> <%=cntName%></td>
                        <td align="center" valign="bottom"> <%=textListOrderHeader[SESS_LANGUAGE][6]%> : 
                          <%
						    Category cat = new Category();	
							cat.setName(textListPrintHeader[SESS_LANGUAGE][1]);
							try	
							{
								if(so.getCategoryId()!=0)
									cat = PstCategory.fetchExc(so.getCategoryId());
						  	}
							catch(Exception e){}
						  %> <%=cat.getName()%></td>
                        <td width="33%" align="right"></td></tr>
                    </table></td>
                </tr>
                <tr> 
                  <td valign="top"><table width="100%" border="1" cellspacing="0" cellpadding="0">
                      <tr align="center"> 
                        <td width="3%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                        <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                        <td width="27%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                        <td width="9%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                        <td width="8%"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                      </tr>
                      <%
					  	int start = 0;
						for(int i=0; i<listMatConStockOpnameItem.size(); i++)
						{
							 Vector temp = (Vector)listMatConStockOpnameItem.get(i);
							 MatConStockOpnameItem soItem = (MatConStockOpnameItem)temp.get(0);
							 Material mat = (Material)temp.get(1);
							 Unit unit = (Unit)temp.get(2);
							 Category catx = (Category)temp.get(3);
							 SubCategory scatx = (SubCategory)temp.get(4);
							 start = start + 1;
					  %>
                      <tr class="listgensell"> 
                        <td align="center">&nbsp;<%=start%></td>
                        <td class="listgensell">&nbsp;<%=mat.getSku()%></td>
                        <td class="listgensell">&nbsp;<%=mat.getName()%></td>
                        <td align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                        <td class="listgensell" align="right">&nbsp;<%=soItem.getQtyOpname()%></td>
                      </tr>
                      <%}%>
                    </table></td>
                </tr>
                <tr>
                  <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0" >
                      <tr align="left" valign="top"> 
                        <%
							try
							{
						%>
                        <td height="22" valign="middle" colspan="3"> <%=textListOrderHeader[SESS_LANGUAGE][8]%> : <%=so.getRemark()%> </td>
                        <%
							}
							catch(Exception e)
							{
								System.out.println(e);
							}
						%>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"></td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="40" valign="middle" colspan="3"></td>
                      </tr>
                      <tr> 
                        <td width="34%" align="center" nowrap><%=textListPrintHeader[SESS_LANGUAGE][3]%>,</td>
                        <td align="center" valign="bottom" width="33%"><%=textListPrintHeader[SESS_LANGUAGE][4]%>,</td>
                        <td width="33%" align="center"><%=textListPrintHeader[SESS_LANGUAGE][5]%>,</td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="75" valign="middle" colspan="3"></td>
                      </tr>
                      <tr> 
                        <td width="34%" align="center" nowrap> (.................................) 
                        </td>
                        <td align="center" valign="bottom" width="33%"> (.................................) 
                        </td>
                        <td width="33%" align="center"> (.................................) 
                        </td>
                      </tr>
                    </table></td>
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
</table>
</body>
<!-- #EndTemplate --></html>
