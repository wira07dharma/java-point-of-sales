<%-- 
    Document   : slide_template
    Created on : Oct 10, 2013, 2:32:42 PM
    Author     : user
--%>

<%@page import="java.util.Vector"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%


%>
<!DOCTYPE html>
<%
String picture="";
if (listPictureBackground != null && listPictureBackground.size() > 0) {
     pictureBackground = (PictureBackground) listPictureBackground.get(0);
         picture= "background-image:url(../imgupload/"+pictureBackground.getUploadPicture()+")";
}
%>
<style type="text/css">
    .contentBgPicture{
        
        position:relative;  min-height: 300px; 
        
        background-color:white;
        background-repeat: repeat-x;
        padding-top:61px;
         padding-left: 33px;
         background-repeat: no-repeat;
         background-position: center;
         <%=picture%>
    }
    
</style>
<% if (listPictureBackground != null && listPictureBackground.size() > 0 ) {%>
<div class="contentBgPicture"> <!--style="border:1px solid <//S%=garisContent%>"-->
    <%@include file="../styletemplate/flyout.jsp" %>
</div>

<%} else  {%>
<div class="content">
   <%@include file="../styletemplate/flyout.jsp" %>
</div>
<%}%>
<div id="body">
    <div class="inner"  >
        <script type="text/javascript">
            var imgid = 'image';
            var  imgdir = '<%=approot%>/imgupload';
            var imgext = '.jpg';
            var thumbid = 'thumbs';
            var auto = true;
            var autodelay = 5;
        </script>
        <script type="text/javascript" src="slide.js"></script>
    </div>
</div>

