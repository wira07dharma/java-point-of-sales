<%-- 
    Document   : menu_icon
    Created on : Oct 11, 2013, 3:11:14 PM
    Author     : user
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<!DOCTYPE html>
<%
    String nilai = FRMQueryString.requestString(request, "nilai");
%>
<script language="JavaScript">
    //alert("<//%=nilai%>");
    function openImgTemplate() {
        // alert("test");
        window.open("<%=approot%>/imgstyle/template2.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgBg() {
        window.open("<%=approot%>/imgstyle/colorBackground.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgHeader() {
        window.open("<%=approot%>/imgstyle/colorHeader.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgContent() {
        window.open("<%=approot%>/imgstyle/colorContent.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgBgMenu() {
        window.open("<%=approot%>/imgstyle/colorBackgroundMenu.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgFontMenu() {
        window.open("<%=approot%>/imgstyle/colorFontMenu.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgHoverMenu() {
        window.open("<%=approot%>/imgstyle/colorHoverMenu.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
    function openImgUploadPicture() {
        window.open("<%=approot%>/imgstyle/uploadPicture.PNG",
                "upload_Image", "status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
</script>
<%if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("0")) {%> 
<table>
    <tr>
        <td>Template 1</td>
    </tr>
    <tr>
        <td>
            Merupakan tema versi lama dari aplikasi. Browser yang support <img src="<%=approot%>/imgstyle/ie_.png"> 6 dan 7, support semua versi <img src="<%=approot%>/imgstyle/firefox_.png">.
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("1")) {%>
<table>
    <tr>
        <td>Template 2</td>
    </tr>
    <tr>
        <td>
            Merupakan tema versi terbaru dari aplikasi. Adapun beberapa versi terbarunya yaitu bisa merubah 
            background dengan gambar, memilih menu, dan bisa merubah warna menu tersebut. <br>
            Browser yang support <img src="<%=approot%>/imgstyle/ie_.png"> 7+ dan semua versi <img src="<%=approot%>/imgstyle/firefox_.png">.
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/template2.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgTemplate()">view image</a>
        </td>
    </tr>
</table> 
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("2")) {%>
<table>
    <tr>
        <td>Color Background</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. Berguna untuk merubah warna background sesuai dengan yang diinginkan. 
            Contohnya :   
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorBackground.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgBg()">view image</a>
        </td>
    </tr>
</table> 
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("3")) {%>
<table>
    <tr>
        <td>Color Header</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. 
            Berguna untuk merubah warna background header sesuai dengan yang diinginkan. Contohnya : 
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorHeader.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgHeader()">view image</a>
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("4")) {%>
<table>
    <tr>
        <td>Color Content</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. 
            Berguna untuk merubah warna background content sesuai dengan yang diinginkan. Contohnya : 
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorContent.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgContent()">view image</a>
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("5")) {%>
<table>
    <tr>
        <td>Color Background Menu</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. 
            Berguna untuk merubah warna background pada menu sesuai dengan yang diinginkan. Contohnya : 
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorBackgroundMenu.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgBgMenu()">view image</a>
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("6")) {%>
<table>
    <tr>
        <td>Color Font Menu</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. 
            Berguna untuk merubah warna tulisan pada menu sesuai dengan yang diinginkan. Contohnya : 
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorFontMenu.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgFontMenu()">view image</a>
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("7")) {%>
<table>
    <tr>
        <td>Color Hover Menu</td>
    </tr>
    <tr>
        <td>
            Fitur ini hanya bisa dipakai jika user memilih template 2. 
            Berguna untuk merubah warna background pada hover menu sesuai dengan yang diinginkan. Contohnya : 
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/colorHoverMenu.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgHoverMenu()">view image</a>
        </td>
    </tr>
</table>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("8")) {%>
<img src="<%=approot%>/imgstyle/menu_1.PNG" alt="Menu 1" width="175">
<p>
    Fitur ini hanya bisa dipakai jika user memilih template 2. Berguna untuk merubah tampilan menu dengan 
    tambahan icon dan bisa dirubah juga warna backgroundnya sesuai dengan yang diinginkan. <br>
    Browser yang support <img src="<%=approot%>/imgstyle/ie_.png"> 7+ dan semua versi <img src="<%=approot%>/imgstyle/firefox_.png">. 
</p>
<%} else if (nilai != null && nilai.length() > 0 && nilai.equalsIgnoreCase("9")) {%>
<img src="<%=approot%>/imgstyle/menu_3.png" alt="Menu 2" width="175">
<p>
    Fitur ini hanya bisa dipakai jika user memilih template 2. Berguna untuk merubah tampilan 
    menu standar tanpa icon dan bisa dirubah juga warna backgroundnya sesuai dengan yang diinginkan. <br>
    Browser yang support <img src="<%=approot%>/imgstyle/ie_.png"> 7+ dan semua versi <img src="<%=approot%>/imgstyle/firefox_.png">. 
</p>
<%} else {%>
<table>
    <tr>
        <td>Upload Picture</td>
    </tr>
    <tr>
        <td>
            User dapat menambahkan beberapa gambar untuk menghias halaman menu background sesuai dengan yang diinginkan. 
            Setiap gambar akan berbeda dengan user yang lain karena user sendiri yang akan menentukan gambar yang akan ditambahkan.
        </td>
    </tr>
    <tr>
        <td>
            <img src="<%=approot%>/imgstyle/uploadPicture.PNG" width="200">
        </td>
    </tr>
    <tr>
        <td>
            <a href="Javascript:openImgUploadPicture()">view image</a>
        </td>
    </tr>
</table>
<% }%>