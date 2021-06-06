<%-- 
    Document   : matunit_excel
    Created on : Sep 26, 2016, 11:51:37 PM
    Author     : dimata005
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_UNIT); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"No","Kode","Nama","Satuan Dasar","Jumlah Per Satuan Dasar","Satuan"},
	{"No","Code","Name","Base Unit","Qty Base Unit","Unit"}
};

/* this method used to list material unit */
public String drawList(int language,int iCommand,FrmUnit frmObject,Unit objEntity,Vector objectClass,long unitId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
ctrlist.setBorder(1);
	ctrlist.addHeader(textListHeader[language][0],"3%");
	ctrlist.addHeader(textListHeader[language][1],"15%");
	ctrlist.addHeader(textListHeader[language][2],"30%");
        ctrlist.addHeader(textListHeader[language][3],"15%");
	ctrlist.addHeader(textListHeader[language][4],"17%");

	ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
		start = 0;

        // get unit for base unit
        Vector obj_base = new Vector(1,1); //vector of object to be listed
        Vector val_base = new Vector(1,1); //hidden values that will be deliver on request (oids)
        Vector key_base = new Vector(1,1); //texts that displayed on combo box
        
        Vector vectBase = PstUnit.list(0,0,"","CODE");

        val_base.add("0");
        key_base.add("-");
        for(int i=0;i<vectBase.size();i++){
            Unit unit = (Unit)vectBase.get(i);
            if (unitId!=0){
                if (unitId!=unit.getOID()){
                    val_base.add(""+unit.getOID()+"");
                    key_base.add(unit.getCode());
                }
            }else{
                val_base.add(""+unit.getOID()+"");
                key_base.add(unit.getCode());
            }
            
        }
        Vector masterUnitAcak = PstUnit.list(0, 0, "", PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID]);
        
        Vector materGroup = PstUnit.structureList(masterUnitAcak) ;
        
         if(materGroup!=null && materGroup.size()>0) {
            String parent="";
            Vector<Long> levelParent = new Vector<Long>();
            for(int i = 0; i < materGroup.size(); i++){
                Unit matUnit = (Unit)materGroup.get(i);

                if(matUnit.getBaseUnitId()!=0){
                    for(int lv=levelParent.size()-1; lv > -1; lv--){
                        long oidLevel=levelParent.get(lv);
                        if(oidLevel==matUnit.getBaseUnitId()){
                            break;
                        }else{
                            levelParent.remove(lv);
                        }
                    }
                    parent="";
                    for(int lv=0; lv<levelParent.size(); lv++){
                       parent=parent+"&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
                    levelParent.add(matUnit.getOID());

                }else{
                    levelParent.removeAllElements();
                    levelParent.add(matUnit.getOID());
                    parent="";
                }


                rowx = new Vector();
                if(unitId == matUnit.getOID())
                index = i;

                start = start + 1;
                if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
                    Unit baseUnit = new Unit();
                    try {
                        baseUnit = PstUnit.fetchExc(matUnit.getBaseUnitId());
                    } catch(Exception e) {
                    }
                        rowx.add(""+start);
                        rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_CODE] +"\" value=\""+matUnit.getCode()+"\" class=\"formElemen\"> * ");
                        rowx.add("<input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_NAME] +"\" value=\""+matUnit.getName()+"\" class=\"formElemen\"> * ");
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmUnit.FRM_FIELD_BASE_UNIT_ID], null, ""+matUnit.getBaseUnitId(),val_base,key_base,"","formElemen"));
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_QTY_PER_BASE_UNIT] +"\" value=\""+objEntity.getQtyPerBaseUnit()+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
                }else{
                        rowx.add("" + start);
                        rowx.add("<div align=\"left\">"+parent+matUnit.getCode()+"</div>");
                        rowx.add(parent+matUnit.getName());
                        String unitName = "-";
                        for(int k=0;k<vectBase.size();k++){
                            Unit xUnit = (Unit)vectBase.get(k);
                            if(xUnit.getOID()==matUnit.getBaseUnitId()){
                                unitName = xUnit.getCode();
                                break;
                            }
                        }
                        rowx.add(unitName);
                        rowx.add("<div align=\"right\">"+matUnit.getQtyPerBaseUnit()+"&nbsp;</div>");
                }
                lstData.add(rowx);
            }
        }
        
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{
			rowx.add(""+(start+1));
			rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_CODE] +"\" value=\""+objEntity.getCode()+"\" class=\"formElemen\"> * ");
			rowx.add("<input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_NAME] +"\" value=\""+objEntity.getName()+"\" class=\"formElemen\"> * ");

                        rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmUnit.FRM_FIELD_BASE_UNIT_ID], null, ""+objEntity.getBaseUnitId(),val_base,key_base,"","formElemen"));
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmUnit.FRM_FIELD_QTY_PER_BASE_UNIT] +"\" value=\""+objEntity.getQtyPerBaseUnit()+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
			lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidUnit = FRMQueryString.requestLong(request, "hidden_merk_id");
int source = FRMQueryString.requestInt(request, "source");
String merkTitle = textListHeader[SESS_LANGUAGE][5]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_UNIT];

/*variable declaration*/
int recordToGet =0;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "CODE";

CtrlUnit ctrlUnit = new CtrlUnit(request);
ControlLine ctrLine = new ControlLine();
Vector listUnit = new Vector(1,1);

/*switch statement */
iErrCode = ctrlUnit.action(iCommand , oidUnit);
/* end switch*/
FrmUnit frmUnit = ctrlUnit.getForm();

/*count list All Unit*/
int vectSize = PstUnit.getCount(whereClause);

/*switch list Unit*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlUnit.actionList(iCommand, start, vectSize, recordToGet);
  }
/* end switch list*/

Unit matUnit = ctrlUnit.getUnit();
msgString =  ctrlUnit.getMessage();

/* get record to display */
listUnit = PstUnit.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listUnit.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listUnit = PstUnit.list(start,recordToGet, whereClause , orderClause);
}

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "material_unit.xls" ); 

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=merkTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmatunit" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_merk_id" value="<%=oidUnit%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+merkTitle : merkTitle+" List"%></u></td>
						</tr>
                      <%
							try{
							%>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmUnit, matUnit,listUnit,oidUnit,start)%> </td>
                      </tr>
                      <%
						  }catch(Exception exc){
						  }%>
                      
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
