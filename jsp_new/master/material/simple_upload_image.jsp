<%@ page import="com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ include file = "../../main/javainit.jsp" %>
<%
    //String oidmaterial = "0";
    int iCommand = FRMQueryString.requestCommand(request);
    int status = FRMQueryString.requestInt(request, "cmd_status");
    long oidmaterial = FRMQueryString.requestLong(request, "hidden_material_id");
	//System.out.println("iCommand: "+iCommand+" Load :"+Command.LOAD);
    Material mat = new Material();
	try{
        mat = PstMaterial.fetchExc(oidmaterial);
    }catch(Exception e){}
    if(iCommand==Command.LOAD){
		try{
		  oidmaterial = Long.parseLong((String)session.getValue("OID_MATERIAL"));
		}catch(Exception e){}
	}else{
		try{
		  session.putValue("OID_MATERIAL",String.valueOf(oidmaterial));
		}catch(Exception e){}
	}
	
    SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
    //String pictPath = objSessUploadCatalogPhoto.getRealFileName(String.valueOf(oidmaterial));
    String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(mat.getSku());
    //System.out.println(">>>>>>>>>."+mat.getSku());
%>
<html>
<head>
<title></title>
<script language="javascript">
function cmdUpload(oid){
    //document.frmmaterialimage.cmd_status.value="1";
	document.frmmaterialimage.command.value="<%=Command.EDIT%>";
	document.frmmaterialimage.action="uploadphotoprocess.jsp";
	document.frmmaterialimage.submit();
}

function cmdedit(oid){
	document.frmmaterialimage.hidden_material_id.value=oid;
    document.frmmaterial.hidden_material_id.value=oid;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.action="simple_upload_image.jsp";
	document.frmmaterial.submit();
}

function cmdrefresh(){
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.action="simple_upload_image.jsp";
	document.frmmaterial.submit();
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
.style9 {font-size: 14px; font-weight: bold; font-family: Arial, Helvetica, sans-serif; color: #FFFFFF; }
-->
</style>
</head>

<body bgcolor="#FCFDEC">
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="81%" valign="top"><form name="frmmaterial" method="post" action="">
    <input type="hidden" name="command" value="<%=iCommand%>">
    <input type="hidden" name="hidden_material_id" value="<%=oidmaterial%>">
    <input type="hidden" name="cmd_status" value="<%=status%>">
    <%
        //System.out.println("oidmaterial : "+oidmaterial);
        if(oidmaterial != 0){
            try{
    %>
    <table width="100%"  bgcolor="#FCFDEC" border="0" cellspacing="1" cellpadding="1">
    <tr align="center" bgcolor="#7B869A" class="listgentitle">
      <td bgcolor="#FF9900"><span class="style9"><%if(SESS_LANGUAGE==0){%>FOTO<%}else{%>PICTURE<%}%>: <%=mat.getSku()%> &nbsp;&nbsp;<%=mat.getName()%></span></td>
      </tr>
    <tr>
      <td width="31%" valign="top" align="center">
            <%
                if(pictPath!=null && pictPath.length()>0){
                    //String IMGCACHE_ABSPATH = PstSystemProperty.getValueByName("PROP_IMGCACHE");
                    out.println("<img width=\"500\" src=\""+approot+"/"+pictPath+"\">");
                }
          %>
      </td>
      </tr>
  </table>
  <%}catch(Exception e){System.out.println("err : "+e.toString());}
        }
  %>
	</form></td>
    <td width="19%" valign="top"><form name="frmmaterialimage" enctype="multipart/form-data" method="post" action="">
	<input type="hidden" name="command" value="<%=iCommand%>">
	<input type="hidden" name="hidden_material_id" value="<%=oidmaterial%>">
    <%
        //System.out.println("oidmaterial upload image: "+oidmaterial);
        if(oidmaterial != 0){
            try{
    %>
    <table width="100%"  bgcolor="#FCFDEC" border="0" cellspacing="1" cellpadding="1">
    <tr align="center" bgcolor="#7B869A" class="listgentitle">
      <td bgcolor="#FCFDEC">&nbsp;</td>
    </tr>
    <tr>
      <td width="69%" nowrap valign="top"><input type="file" size="15" name="filename"><br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="button" name="Button" value="Upload..." onClick="javascript:cmdUpload('<%=oidmaterial%>')"></td>
    </tr>
  </table>
  <%}catch(Exception e){System.out.println("err : "+e.toString());}
        }
  %>
</form></td>
  </tr>
</table>
</body>
</html>
