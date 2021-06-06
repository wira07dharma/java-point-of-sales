<%-- 
    Document   : dokumen_upload
    Created on : Dec 1, 2019, 6:05:07 PM
    Author     : WiraDharma
--%>
<%@page import="com.dimata.posbo.entity.warehouse.PstBillImageMapping"%>
<%@page import="com.dimata.posbo.entity.warehouse.BillImageMapping"%>
<%@page import="com.dimata.sedana.session.SessEmpRelevantDoc"%>
<%@page import="java.util.Vector"%>


<%@ include file = "../../../main/javainit.jsp" %>
<%@ page import="com.dimata.util.*" %>
<%@ page import="com.dimata.util.blob.*" %>
<%@ page import="com.dimata.qdep.form.*" %>


 <%//
    String oidDokumen ="";
    int iCommand =  FRMQueryString.requestInt(request,"command");
    iCommand = Command.SAVE;
    Vector file = new Vector(1,1);
        
    try{
        file = ((Vector)session.getValue("SELECTED_FILE")); 
    }catch(Exception e) {
        System.out.println(e.getMessage());
    }
    
    oidDokumen = (String)file.get(0);
 
    long longOidDoc = 0;
    //long oidBlobEmpPicture = 0;
    try{
        longOidDoc = Long.parseLong(oidDokumen);
    }catch(Exception ex){
        System.out.println(ex.getMessage());
    }
 
    BillImageMapping fetchRelevant = new BillImageMapping();
    try{
        fetchRelevant = PstBillImageMapping.fetchExc(longOidDoc);	
    }catch(Exception e){
 	System.out.println("err. fect EpRelvantDocumnet"+e.toString());
    }
 
    Vector vectResult = new Vector(1,1);
    try {
        ImageLoader uploader = new ImageLoader();
        int numFiles = uploader.uploadImage(config, request, response); 
        String fileName = "" + String.valueOf(fetchRelevant.getOID()) + "_" + uploader.getFileName();
        System.out.println("fileName real..."+fileName);
        try {
            // get object of specified location identified by form at previous location (path)
            String fieldFormName = "FRM_FIELD_NAMA_FILE";
            Object obj = uploader.getImage(fieldFormName);
            System.out.println("obj..."+obj);
            // casting object to its 'byte' format and generate file used it at specified location and specified name
            byte[] byteOfObj = (byte[]) obj;
            int intByteOfObjLength = byteOfObj.length;
            if(intByteOfObjLength > 0) {
                // --- start generate record peserta photo ---
                long oidBlobEmpPicture = 0;		
                SessEmpRelevantDoc objSessEmpRelevantDoc = new SessEmpRelevantDoc();			 			 				 
                String pathFileName = objSessEmpRelevantDoc.getAbsoluteFileName(fileName);
                java.io.ByteArrayInputStream byteIns = new java.io.ByteArrayInputStream(byteOfObj);		 
                int result = uploader.writeCache(byteIns, pathFileName, true);
                if (result == 0) {
                    try {
                        PstBillImageMapping.updateFileName(fileName,longOidDoc);
                        System.out.println("update sukses.."+fileName);
                    }catch(Exception e){
                        System.out.println("err update.."+e.toString())	;
                    }
                } else {
                    System.out.println("Error upload file");
                }
                // --- end generate photo peserta ---
                // --- start proses simpan hasil tulis gambar ke vector
                vectResult.add(""+longOidDoc);
                // --- end proses simpan hasil tulis gambar ke vector			 			 
            }
        } catch(Exception e) {
            System.out.println("Exc1 when upload image : " + e.toString());
        }
    //}
    } catch(Exception e) {
        System.out.println("Exc2 when upload image : " + e.toString());
    }

%>

<script language="JavaScript">
    <%if(iCommand==Command.SAVE){%>
        window.opener.location.reload();
        window.close();
    <%}%>
</script>