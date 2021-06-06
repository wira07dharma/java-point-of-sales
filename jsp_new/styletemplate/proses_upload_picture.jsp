<%-- 
    Document   : proses_upload_picture
    Created on : Aug 7, 2013, 4:29:48 PM
    Author     : arin
--%>

<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="com.dimata.aplikasi.form.uploadpicture.CtrlPictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page import="com.dimata.aplikasi.form.uploadpicture.FrmPictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="com.dimata.qdep.form.*"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.oreilly.servlet.multipart.*" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../main/javainit.jsp" %>
<!DOCTYPE html>
<%
   pictureBackground = new PictureBackground();

//int iCommand = FRMQueryString.requestInt(request,"command");
/*int start = FRMQueryString.requestInt(request, "start");
     int prevCommand = FRMQueryString.requestInt(request, "prev_command");
     long oidJenisItemPicture = FRMQueryString.requestLong(request, "jenis_item_picture_oid");
     long oidJenisItemId = FRMQueryString.requestLong(request, "jenisItemId");
     String pictName =  FRMQueryString.requestString(request, "pict"); 
     */

    /*variable declaration*/
    int iErrCode = FRMMessage.NONE;
    int iCommand = 0;
    long oidPicture = FRMQueryString.requestLong(request, "hidden_picture_oid");
    long oidLoginUser = FRMQueryString.requestLong(request, "loginuser");
    String saveFile = "";
    CtrlPictureBackground ctrlPictureBackground = new CtrlPictureBackground(request);

    boolean isChangePicture = false;
    boolean submitIsOk = false;

    try {
        String contentType = request.getContentType();
        MultipartRequest multipartRequest = null;
        if (contentType != null) {
            multipartRequest = new MultipartRequest(request, pathImage);
        }
        String namePicture = multipartRequest != null ? multipartRequest.getParameter(FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_NAMA_PICTURE]) : "";
        String ketPicture = multipartRequest != null ? multipartRequest.getParameter(FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_KETERANGAN]) : "";
        String fileNamePicture = multipartRequest != null ? multipartRequest.getFilesystemName(FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_UPLOAD_PICTURE]) : "";
        String sIschangePic = multipartRequest != null ? multipartRequest.getParameter(FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_CHANGE_PICTURE]) : "false";

        if (multipartRequest != null) {
            String sOidPicture = multipartRequest != null ? multipartRequest.getParameter("hidden_picture_oid") : "";
            String sOidLoginUser = multipartRequest != null ? multipartRequest.getParameter("loginuser") : "";
            String sCommand = multipartRequest != null ? multipartRequest.getParameter("commands") : "";
            oidPicture = sOidPicture != null && sOidPicture.length() > 0 ? Long.parseLong(sOidPicture) : 0;
            oidLoginUser = sOidLoginUser != null && sOidLoginUser.length() > 0 ? Long.parseLong(sOidLoginUser) : 0;

            isChangePicture = sIschangePic != null && sIschangePic.length() > 0 ? Boolean.parseBoolean(sIschangePic) : false;
            iCommand = sCommand != null && sCommand.length() > 0 ? Integer.parseInt(sCommand) : 0;
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
            if (iCommand == Command.SAVE || iCommand == Command.DELETE) {
                pictureBackground.setKeterangan(ketPicture);
                pictureBackground.setNamaPicture(namePicture);
                pictureBackground.setUploadPicture(fileNamePicture);
                pictureBackground.setLoginId(oidLoginUser);
                pictureBackground.setChangePicture(isChangePicture);
                //insert
                //iCommand = Command.SAVE;
                if (pictureBackground != null && pictureBackground.getNamaPicture() != null && pictureBackground.getNamaPicture().length() > 0) {
                    iErrCode = ctrlPictureBackground.action(iCommand, oidPicture, pictureBackground, pathImage);
                    if (iErrCode == ctrlPictureBackground.RSLT_OK) {
                        submitIsOk = true;
                    }
                }
            }

        }
    } catch (Exception e) {
        System.out.println("Exc2 when upload image : " + e.toString());
    }


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Proses Upload Picture</title>
        <script language="JavaScript">
            <% if (submitIsOk) {%>
  
                window.opener.location.reload(true);
                window.close();
            <%}%>
                function disablefield(){ 
                    if (document.getElementById('change1').checked == 0){  
                        document.getElementById('file1').disabled='disabled';
                    }else{  
                        document.getElementById('file1').disabled='';
                    } 
                }   
                function cmdSave(){
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.commands.value="<%=Command.SAVE%>";
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.action="proses_upload_picture.jsp";
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.submit();
                }
                function cmdDelete(oid){
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.commands.value="<%=Command.DELETE%>";
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.hidden_picture_oid.value=oid;
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.action="proses_upload_picture.jsp";
                    document.<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>.submit();
                }
        </script>
        <link rel="stylesheet" href="../stylesheets/general_template_style.css" type="text/css"> 
    </head>
    <body bgcolor="#CAF1FA" style="color: #000;">
        <form ENCTYPE="multipart/form-data"  name="<%=FrmPictureBackground.FRM_PICTURE_BACKGROUND%>" method="post" action="">
            <input type="hidden" name="commands" value="">
            <input type="hidden" name="hidden_picture_oid" value="<%=oidPicture%>">
            <input type="hidden" name="loginuser" value="<%=oidLoginUser%>">
            <table width="609" height="39" cellpadding="5">
                <tr>
                    <td colspan="3" style="background: #4198ee; color:#D5F1FF;"><%=oidPicture != 0 ? "Edit" : "Add"%> Picture</td>
                </tr>
                <%if (oidPicture != 0 && iCommand!=Command.DELETE) {
                        pictureBackground = PstPictureBackground.fetchExc(oidPicture);
                        String namePicture = pictureBackground.getUploadPicture();
                %>

                <tr>
                    <td colspan="3" nowrap="nowrap" align="center">
                        <table>
                            <tr>
                                <td><img src="<%=approot%>/imgupload/<%=namePicture%>" width="208" height="218"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td width="7" nowrap="nowrap">Nama Picture</td>
                    <td width="7" nowrap="nowrap"> : </td>
                    <td><input type="text" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_NAMA_PICTURE]%>" value="<%=pictureBackground.getNamaPicture()%>"></td>
                </tr>
                <tr>
                    <td width="7" nowrap="nowrap">Keterangan</td>
                    <td width="7" nowrap="nowrap"> : </td>
                    <td><textarea name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_KETERANGAN]%>" cols="30" rows="4"><%=pictureBackground.getKeterangan()%></textarea>   </td>
                </tr>

                <tr>
                    <td width="7" nowrap="nowrap" valign="top"> Upload Picture </td>
                    <td width="7" nowrap="nowrap" valign="top"> : </td>
                    <td height="26" colspan="3">
                        <table>
                            <tr>
                                <td></td>
                            </tr>
                            <tr>
                                <td> Change Picture ? </td>
                            </tr>
                            <tr>
                                <td>
                                    <%if ((iCommand != Command.ADD && iCommand != Command.NONE) || (oidPicture != 0)) {%>
                                    <input id="change1" type="radio" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_CHANGE_PICTURE]%>" value="true" onChange='disablefield();' />Yes
                                    <input id="change2" type="radio" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_CHANGE_PICTURE]%>" value="false" onChange='disablefield();' checked />No
                                    <%} else {%>
                                    <input id="change1" type="radio" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_CHANGE_PICTURE]%>" value="true" onChange='disablefield();' checked="che"/>Yes
                                    <input id="change2" type="radio" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_CHANGE_PICTURE]%>" value="false" onChange='disablefield();'  />No
                                    <%}%>
                                </td>
                            </tr>  
                            <tr>
                                <td>
                                <input id="file1" type="file" name="<%=FrmPictureBackground.fieldNames[FrmPictureBackground.FRM_FLD_UPLOAD_PICTURE]%>" size="60" height="100"/></tr>
                    </td>
                </tr>
            </table>
            <tr >
                <td width="7" nowrap="nowrap"><a  href="Javascript:cmdSave()"  class="savestyle">Save</a></td>
                <td width="7" nowrap="nowrap"></td>
                <%
                    if (oidPicture != 0) {
                %>
                <td width="7" nowrap="nowrap"><a  href="Javascript:cmdDelete('<%=oidPicture%>')" class="deletestyle">Delete</a></td>
                <%}%>
            </tr>
        </table>
    </form>
</body>
</html>
