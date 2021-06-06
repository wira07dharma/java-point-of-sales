<%-- 
    Document   : matcategory_excel
    Created on : Sep 26, 2016, 11:45:27 PM
    Author     : dimata005
--%>

<%@ page language = "java" %>
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.Category,
                   com.dimata.posbo.entity.masterdata.PstCategory,
                   com.dimata.posbo.form.masterdata.CtrlCategory,
                   com.dimata.posbo.form.masterdata.FrmCategory,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.jsp.JspInfo,
                   com.dimata.common.entity.payment.DiscountType,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.posbo.entity.masterdata.PstDiscountMapping,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.PstDiscountType,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                    com.dimata.gui.jsp.ControlCombo,
                   com.dimata.posbo.session.masterdata.SessDiscountCategory" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATEGORY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Kode","Nama","Harga Poin","Keterangan","Lokasi Produksi","Parent Id","Status","Category","Type Category"},
	{"Code","Name","Point Price","Description","Production Location","Parent Id","Status","Category","Type Category"}
};

/* this method used to list material department */
public String drawList(int language,Vector objectClass,long departmentId, int start, String typeOfBusiness, int typeCategory)
{
	int mappingProduksi = Integer.parseInt(PstSystemProperty.getValueByName("MAPPING_PRINT_PRODUKSI"));
        ControlList ctrlist = new ControlList();

	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(1);
	ctrlist.dataFormat("No","5%","center","left");
	ctrlist.dataFormat(textListHeader[language][0],"10%","center","left");
	ctrlist.dataFormat(textListHeader[language][1],"15%","center","left");
        ctrlist.dataFormat(textListHeader[language][2],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][3],"10%","center","left");
        if(typeOfBusiness.equals("2")){
             ctrlist.dataFormat(textListHeader[language][4],"10%","center","left");
             ctrlist.dataFormat(textListHeader[language][7],"10%","center","left");
             if(mappingProduksi==2){
                ctrlist.dataFormat(textListHeader[language][6],"10%","center","left");
             }
        }
        
        

	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	int index = -1;
        String where=""+PstCategory.fieldNames[PstCategory.FLD_TYPE_CATEGORY]+"='"+typeCategory+"'";
        Vector masterCatAcak = PstCategory.list(0,0,""+where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
        //Category newCategory = new Category();
        //add opie-eyek 20130821
        Vector materGroup = PstCategory.structureList(masterCatAcak) ;
        if(materGroup!=null && materGroup.size()>0) {
            String parent="";
            Vector<Long> levelParent = new Vector<Long>();
            for(int i=0; i<materGroup.size(); i++) {
                Category mGroup = (Category)materGroup.get(i);
                    if(mGroup.getCatParentId()!=0){
                        for(int lv=levelParent.size()-1; lv > -1; lv--){
                            long oidLevel=levelParent.get(lv);
                            if(oidLevel==mGroup.getCatParentId()){
                                break;
                            }else{
                                levelParent.remove(lv);
                            }
                        }
                        parent="";
                        for(int lv=0; lv<levelParent.size(); lv++){
                           parent=parent+"&nbsp;&nbsp;&nbsp;&nbsp;";
                        }
                        levelParent.add(mGroup.getOID());

                    }else{
                        levelParent.removeAllElements();
                        levelParent.add(mGroup.getOID());
                        parent="";
                    }
                //disini
                Vector rowx = new Vector();
                rowx.add(""+(i+start+1));
                rowx.add(parent+mGroup.getCode());
		rowx.add(parent+mGroup.getName());
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mGroup.getPointPrice())+"</div>");
                rowx.add(mGroup.getDescription());
                //adding production location by Mirahu 20120511
                if(typeOfBusiness.equals("2")){
                    Location location = new Location();
                    try {
                        location = PstLocation.fetchExc(mGroup.getLocationId());
                    }catch(Exception e) {
                        // System.out.println("Location not found ...");
                    }
                    rowx.add(location.getName());
                    
                    rowx.add(""+PstCategory.category[mGroup.getTypeCategory()]);
                    
                    if(mappingProduksi==2){
                        rowx.add("");
                    }
                
                }
                
                lstData.add(rowx);
                //lstLinkData.add(String.valueOf(mGroup.getOID()));
            }
            
        }
    return ctrlist.drawMe(index);
}

%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMatDepartment = FRMQueryString.requestLong(request, "hidden_department_id");

int source = FRMQueryString.requestInt(request, "source");

String departmentTitle = JspInfo.txtMaterialInfo[SESS_LANGUAGE][JspInfo.MATERIAL_DEPARTMENT];

int recordToGet = 15;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
/**
* Special case if code is caracter of numeric (1,2,3,4,5, ...)
* dengan "+1" artinya String akan diubah secara implisit menjadi Numerik
*/
String orderClause = "("+PstCategory.fieldNames[PstCategory.FLD_CODE]+"+1)";

CtrlCategory ctrlMatCategory = new CtrlCategory(request);
ControlLine ctrLine = new ControlLine();
Vector listMatCategory = new Vector(1,1);

iErrCode = ctrlMatCategory.action(iCommand,oidMatDepartment);
FrmCategory frmMatDepartment = ctrlMatCategory.getForm();

// count list All MatDepartment
int vectSize = 0;//PstCategory.getCount(whereClause);

Category matCategory = ctrlMatCategory.getCategory();
msgString =  ctrlMatCategory.getMessage();

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	start = ctrlMatCategory.actionList(iCommand, start, vectSize, recordToGet);
}

// get record to display
orderClause = PstCategory.fieldNames[PstCategory.FLD_NAME];
//listMatCategory = PstCategory.list(start,recordToGet,whereClause,orderClause);

// handle condition if size of record to display=0 and start>0 after delete
if(listMatCategory.size()<1 && start>0)
{
  if (vectSize - recordToGet > recordToGet)
  {
	 start = start - recordToGet;
  }
  else
  {
	 start = 0 ;
	 iCommand = Command.FIRST;
	 prevCommand = Command.FIRST;
  }
}

long oidCurr = 0;
try {
    oidCurr = Long.parseLong((String)com.dimata.system.entity.PstSystemProperty.getValueByName("OID_CURR_FOR_PRICE_SALE"));
} catch (Exception e) {
    oidCurr = 0;
}

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "kategory_excel.xls" ); 
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable --> 
</head> 

<body >    
<table width="100%" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=departmentTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmcategory" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_department_id" value="<%=oidMatDepartment%>">
              <input type="hidden" name="source" value="<%=source%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+departmentTitle : departmentTitle+" List"%></u></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> 
                            <%=drawList(SESS_LANGUAGE,listMatCategory,oidMatDepartment,start,typeOfBusiness,0)%> 
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;</td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+departmentTitle +" Sales ": departmentTitle+" Sales List"%></u></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> 
                            <%=drawList(SESS_LANGUAGE,listMatCategory,oidMatDepartment,start,typeOfBusiness,1)%> 
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
</table>
</body>
<!-- #EndTemplate --></html>
