<%-- 
    Document   : picture_company
    Created on : Sep 20, 2013, 4:10:17 PM
    Author     : user
--%>

<%@page import="com.dimata.aplikasi.entity.picturecompany.PstPictureCompany"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="com.dimata.aplikasi.form.picturecompany.CtrlPictureCompany"%>
<%@page import="com.dimata.aplikasi.form.picturecompany.FrmPictureCompany"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="com.dimata.qdep.form.*"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.oreilly.servlet.multipart.*" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../main/javainit.jsp" %>
<%
    //PictureCompany pictureCompany = new PictureCompany();
    
    
    //long oidCompanyPic = ;
    
    boolean submitIsOk = false;
    
    int iErrCode = FRMMessage.NONE;
    int iCommand = 0; 
    long oidPictureCompany = FRMQueryString.requestLong(request, "oidCompanyPic");
    String saveFile = "";
    CtrlPictureCompany ctrlPictureCompany = new CtrlPictureCompany(request);



    try {
        String contentType = request.getContentType();
        MultipartRequest multipartRequest = null;
        if (contentType != null) {
            multipartRequest = new MultipartRequest(request, pathImg); 
        }
        String fileNamePictureCompany = multipartRequest != null ? multipartRequest.getFilesystemName(FrmPictureCompany.fieldNames[FrmPictureCompany.FRM_FLD_NAMA_PICTURE]) : "";
        String sCommand = multipartRequest != null ? multipartRequest.getParameter("command") : "";
        String sOidPictureCompany = multipartRequest != null ? multipartRequest.getParameter("oidCompanyPic") : "";
        
      if(multipartRequest!=null){
        
        iCommand = sCommand != null && sCommand.length() > 0 ? Integer.parseInt(sCommand) : 0;
        oidPictureCompany = sOidPictureCompany != null && sOidPictureCompany.length() > 0 ? Long.parseLong(sOidPictureCompany) : 0;
       }
        if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {

            try {
                Enumeration files = multipartRequest.getFileNames();

                while (files.hasMoreElements()) {
                    String upload = (String) files.nextElement();
                    String filename = multipartRequest.getFilesystemName(upload);
                    System.out.println(filename);
                }


            } catch (Exception exc) {
                System.out.println("System Exception" + exc);
            }


            try {
                //PictureBackground pictureBackground = new PictureBackground();
            } catch (Exception exc) {
                System.out.println("Exception" + exc);

            }
            if (iCommand == Command.SAVE) {
                pictureCompany.setNamaPicture(fileNamePictureCompany);
                pictureCompany.setOID(oidPictureCompany);
                
                if (pictureCompany != null && pictureCompany.getNamaPicture() != null && pictureCompany.getNamaPicture().length() > 0) {
                    iErrCode = ctrlPictureCompany.action(iCommand, oidPictureCompany, pictureCompany, pathImg);
                    if(iErrCode==ctrlPictureCompany.RSLT_OK){
                        submitIsOk=true;
                    }
                }
            }

        }
    } catch (Exception e) {
        System.out.println("Exc2 when upload image : " + e.toString());
    }

%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Picture Company</title>
        <link rel="stylesheet" href="<%=approot%>/stylesheets/general_template_style.css" type="text/css">
        <script language="JavaScript">
            <% if(submitIsOk){ %>
                //window.opener.location.href = window.opener.location.href;
                //document.forms[0].submit();
               window.opener.location.reload(true);
                window.close();
                
            <% } %>
            function cmdSave(){
                document.<%=FrmPictureCompany.FRM_PICTURE_COMPANY%>.command.value="<%=Command.SAVE%>";
                document.<%=FrmPictureCompany.FRM_PICTURE_COMPANY%>.action="picture_company.jsp";
                document.<%=FrmPictureCompany.FRM_PICTURE_COMPANY%>.submit();
            }
        </script>
    </head>
    <body bgcolor="#CAF1FA" style="color: #000;">
        <form ENCTYPE="multipart/form-data"  name="<%=FrmPictureCompany.FRM_PICTURE_COMPANY%>" method="post" action="">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="oidCompanyPic" value="<%=oidPictureCompany%>">
            <table width="100%" cellpadding="5">
                <tr>
                    <td colspan="3" style="background: #4198ee; color:#D5F1FF;">Change Picture</td>
                </tr>
                <%if(oidPictureCompany!=0){
                    pictureCompany=PstPictureCompany.fetchExc(oidPictureCompany);            
                    String namePicture=pictureCompany.getNamaPicture();
                %>
                <tr>
                    <td colspan="3" nowrap="nowrap" align="center">
                        <table>
                            <tr>
                                <td><img src="<%=approot%>/imgcompany/<%=namePicture%>" width="208"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td width="4" nowrap="nowrap">Upload Picture</td>
                    <td width="1" nowrap="nowrap"> : </td>
                    <td height="26" colspan="3"><input type="file" name="<%=FrmPictureCompany.fieldNames[FrmPictureCompany.FRM_FLD_NAMA_PICTURE]%>" /></tr>
                <tr>
                    <td nowrap="nowrap"></td>
                    <td width="1" nowrap="nowrap"></td>
                    <td width="7" nowrap="nowrap"><a  href="Javascript:cmdSave()"  class="savestyle">Save</a></td>
                </tr>
            </table>
        </form>
    </body>
</html>
