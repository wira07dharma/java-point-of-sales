<%@page contentType="text/html"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
				 com.dimata.posbo.entity.search.SrcMemberReg,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.session.contact.SessContactList,
                 com.dimata.common.form.contact.CtrlContactList,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.masterdata.FrmMatVendorPrice,
                 com.dimata.gui.jsp.ControlLine"%>
<%@ page import = "com.dimata.posbo.session.masterdata.SessMemberReg" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textListContact[][] = {
	{"No","Kode","Nama","Alamat","Telepon"},
	{"No","Code","Name","Address","Phone"}	
};
public static final String textListTitleHeader[][] = {
	{"Daftar Supplier"},
	{"Supplier List"}	
};
%>
<!-- JSP Block -->
<%
String vendorkode = FRMQueryString.requestString(request, "txt_vendorkode");
int iCommand = FRMQueryString.requestCommand(request);

int dpn = 2;
int blk = 2 + 3;
String kode = "";
    if(vendorkode.length() >= blk){
        kode = vendorkode.substring(dpn,blk);
    }
System.out.println("==========vendorkode >>>>>>>>>>>>>>>>>>> : "+vendorkode);
System.out.println("==========kode >>>>>>>>>>>>>>>>>>> : "+kode);
//String where  = PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]+" = "+kode;
//Vector list = PstContactList.list(0,0,where,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
SrcMemberReg srcMemberReg = new SrcMemberReg();
srcMemberReg.setCodeSupplier(kode);
Vector list = SessMemberReg.searchSupplier(srcMemberReg, 0, 0);
System.out.println("==========kode list : "+list.size());

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/maindosearch.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(oid,code,name){
	self.opener.document.forms.frmmaterial.txt_vendorcode.value = code;
    self.opener.document.forms.frmmaterial.txt_vendorname.value = name;
    self.opener.document.forms.frmmaterial.hidden_vendor_id.value = oid;
	self.close();
}

<%
    if(list!=null && list.size()>0){
        ContactList contactList = (ContactList)list.get(0);
%>
    cmdEdit('<%=contactList.getOID()%>','<%=contactList.getContactCode()%>','<%=contactList.getCompName()%>');

<%}else{%>
    self.close();
<%}%>
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
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- #BeginEditable "editfocus" -->
<script language="JavaScript">
	window.focus();
</script>
<!-- #EndEditable -->    
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
          <td height="20"><font color="#FF8080" face="Century Gothic"><big><strong><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></strong></big></font></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
			  <form name="frmvendorsearch" method="post" action="">
			  <input type="hidden" name="command" value="<%=iCommand%>">
			  <input type="hidden" name="txt_vendorcode" value="<%=vendorkode%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2"></td>
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
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
