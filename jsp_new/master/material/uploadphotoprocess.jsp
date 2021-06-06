<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>

<%@ page language="java" %>
<%@ page import = "com.dimata.util.*,
                   com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import = "com.dimata.util.blob.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<%
    boolean bool = true;
    //String oidmaterial = "0";
    long oidmaterial =0;
    try{
        ImageLoader uploader = new ImageLoader();
        int numFiles = uploader.uploadImage(config, request, response);
        oidmaterial = FRMQueryString.requestLong(request, "hidden_material_id");
		try{
			oidmaterial = Long.parseLong((String)session.getValue("OID_MATERIAL"));
		}catch(Exception e){}
        Material mat = new Material();
        try{
            mat = PstMaterial.fetchExc(oidmaterial);
        }catch(Exception e){}
		System.out.println("oid di proses upload image : "+oidmaterial);
        String fieldFormName = "filename";
        Object obj = uploader.getImage(fieldFormName);

        byte[] byteOfObj = (byte[]) obj;
        int intByteOfObjLength = byteOfObj.length;
        SessUploadCatalogPhoto sessUploadCatalogPhoto = new SessUploadCatalogPhoto();
        if(intByteOfObjLength > 0){
            String pathFileName = sessUploadCatalogPhoto.getAbsoluteFileName(String.valueOf(mat.getSku()));
            java.io.ByteArrayInputStream byteIns = new java.io.ByteArrayInputStream(byteOfObj);
            uploader.writeCache(byteIns, pathFileName, true);
        }

        //SessUploadCatalogPhoto.writeRenameCache(true);
    }catch(Exception e){
        bool = false;
        System.out.println("Exc when upload image : " + e.toString());
    }
    %>
	<jsp:forward page="simple_upload_image.jsp">
	<jsp:param name="command" value="<%=Command.LOAD%>" />
	<jsp:param name="hidden_material_id" value="<%=oidmaterial%>" />
	</jsp:forward>
    <%
%>
<!-- End of JSP Block -->
