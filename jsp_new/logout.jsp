<%-- 
    Document   : logout1
    Created on : Oct 29, 2019, 1:51:45 PM
    Author     : Regen
--%>
<%@ page language="java" %>
<%@ include file = "main/javainit.jsp" %>
<% int appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<!--%@ include file = "main/checkuser.jsp" %-->


<%!  public static final String textListTitleHeader[][] = {
    {"EXIT", "LOGIN"},
    {"EXIT", "LOGIN"}
  };
%>                                                   

<%
  try {
    if (userSession.isLoggedIn() == true) {
      userSession.printAppUser();
      userSession.doLogout();
      session.removeValue(SessUserSession.HTTP_SESSION_NAME);
      session.removeValue("APPLICATION_LANGUAGE");
    }
    if (userSession != null) {
      session.removeValue(SessUserSession.HTTP_SESSION_SALES);
    }
  } catch (Exception exc) {
    System.out.println(" ==> Exception during logout user");
  }

  /**
   * cek integrasi
   */
  int INTEGRASI_POS = 0;
  int INTEGRASI_HANOMAN = 1;

  int pos_integ = 0;
  try {
    String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
    pos_integ = Integer.parseInt(designMat);
  } catch (Exception e) {
    pos_integ = 0;
  }

  int login_type = 0;
  try {
    String designMat = String.valueOf(PstSystemProperty.getValueByName("PROCHAIN_LOGIN_TYPE"));
    login_type = Integer.parseInt(designMat);
  } catch (Exception e) {
    login_type = 0;
  }

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="styles/bootstrap-3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="styles/font-awesome/font-awesome.min.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dimata - ProChain POS</title>

    <style>
      html {
        line-height: 1.15;
        -ms-text-size-adjust: 100%;
        -webkit-text-size-adjust: 100%;
      }

      body {
        margin: 0;
      }

      header,
      nav,
      section {
        display: block;
      }

      a {
        background-color: transparent;
        -webkit-text-decoration-skip: objects;
      }

      button {
        text-transform: none;
      }

      html {
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        font-family: sans-serif;
      }

      *,
      *::before,
      *::after {
        -webkit-box-sizing: inherit;
        box-sizing: inherit;
      }

      button {
        background: transparent;
        padding: 0;
      }

      button:focus {
        outline: 1px dotted;
        outline: 5px auto -webkit-focus-ring-color;
      }

      *,
      *::before,
      *::after {
        border-width: 0;
        border-style: solid;
        border-color: #dae1e7;
      }

      button,
      input {
        font-family: inherit;
      }

      button,
      [role=button] {
        cursor: pointer;
      }

      .bg-white {
        background-color: #fff;
      }

      .bg-no-repeat {
        background-repeat: no-repeat;
      }

      .bg-cover {
        background-size: cover;
      }

      .border-grey-light {
        border-color: #dae1e7;
      }

      .hover\:border-grey:hover {
        border-color: #b8c2cc;
      }

      .rounded-lg {
        border-radius: .5rem;
      }

      .border-2 {
        border-width: 2px;
      }

      .hidden {
        display: none;
      }

      .flex {
        display: -webkit-box;
        display: -ms-flexbox;
        display: flex;
      }

      .items-center {
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
      }

      .justify-center {
        -webkit-box-pack: center;
        -ms-flex-pack: center;
        justify-content: center;
      }

      .leading-normal {
        line-height: 1.5;
      }

      .m-8 {
        margin: 2rem;
      }

      .my-3 {
        margin-top: .75rem;
        margin-bottom: .75rem;
      }

      .mb-8 {
        margin-bottom: 2rem;
      }

      .max-w-sm {
        max-width: 30rem;
      }

      .min-h-screen {
        min-height: 100vh;
      }

      .py-3 {
        padding-top: .75rem;
        padding-bottom: .75rem;
      }

      .px-6 {
        padding-left: 1.5rem;
        padding-right: 1.5rem;
      }

      .pb-full {
        padding-bottom: 100%;
      }

      .absolute {
        position: absolute;
      }

      .relative {
        position: relative;
      }

      .pin {
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
      }

      .tracking-wide {
        letter-spacing: .05em;
      }

      .w-16 {
        width: 4rem;
      }

      .w-full {
        width: 100%;
      }

      @media (min-width: 768px) {
        .md\:bg-left {
          background-position: left;
        }

        .md\:bg-right {
          background-position: right;
        }

        .md\:flex {
          display: -webkit-box;
          display: -ms-flexbox;
          display: flex;
        }

        .md\:my-6 {
          margin-top: 1.5rem;
          margin-bottom: 1.5rem;
        }

        .md\:min-h-screen {
          min-height: 100vh;
        }

        .md\:pb-0 {
          padding-bottom: 0;
        }

        .md\:text-3xl {
          font-size: 1.875rem;
        }

        .md\:text-15xl {
          font-size: 9rem;
        }

        .md\:w-1\/2 {
          width: 50%;
        }
      }

      @media (min-width: 992px) {
        .lg\:bg-center {
          background-position: center;
        }
      }
      .detail {
        margin-bottom: 30px;
      }
      a.btn.btn-primary.exit {
        width: 35%;
        margin-right: 10px;
      }
a.btn.btn-primary.login {
    background-color: #2658cc !important;
    width: 60%;
    border: none !important;
}
      .judul {
    margin: 0px 10px 10px 0px;
}
.thanks {
    color: #2658cc;
}
label.desc {
    font-size: 12px;
    margin: 25px auto;
    font-weight: 400;
}
strong.color {
    color: #2658cc;
}
    </style>
  </head> 
  <body>
    <div class="md:flex min-h-screen">
      <!--Left Side-->
      <div class="w-full md:w-1/2 bg-white flex items-center justify-center">
        <div class="theme">
          <label class="thanks">Thanks for Using</label>
          <h1 class="judul">DIMATA PROCHAIN</h1>
          <label class="detail">RETAIL BUSINESS MANAGEMENT SYSTEM</label>
          <div class="button-pos">
            <!--<a class="btn btn-primary exit" id="close" href="javascript:closeWindow()">EXIT</a>// belum berfungsi-->
            <%if (login_type == 1) {%>
            <a class="btn btn-primary login" href="login_finger.jsp">Back to Login</a>
            <%} else {%>
            <a class="btn btn-primary login" href="login_new.jsp">Back to Login</a>
            <%}%>
          </div>
          <div>
            <label class="desc">Copyright © 2010 Dimata® IT Solution <br> Telp. (0361) 499029, 7869752; Fax (0361) 499029 <br> Hotline Support : 081237710011 <br> Email : <strong class="color">emarketing@dimata.com</strong> Website : <strong class="color">www.dimata.com</strong></label>
          </div>
        </div>
        <% if (pos_integ == INTEGRASI_POS) { %>
        <% } else if (pos_integ == INTEGRASI_HANOMAN) { %>
        <% }%>
<!--        <div class="theme">
          <label class="thanks">Thanks for Using</label>
          <h1 class="judul">DIMATA HANOMAN</h1>
          <label class="detail">PURCHASING INVENTORY SYSTEM</label>
          <div class="button-pos">
            <a class="btn btn-primary exit" id="close" href="javascript:closeWindow()">EXIT</a>// belum berfungsi
            <%if (login_type == 1) {%>
            <a class="btn btn-primary login" href="login_finger.jsp">Back to Login</a>
            <%} else {%>
            <a class="btn btn-primary login" href="login_new.jsp">Back to Login</a>
            <%}%>
          </div>
            <label class="desc">Copyright © 2010 Dimata® IT Solution <br> Telp. (0361) 499029, 7869752; Fax (0361) 499029 <br> Hotline Support : 081237710011 <br> Email : <strong class="color">emarketing@dimata.com</strong> Website : <strong class="color">www.dimata.com</strong></label>
        </div>-->
      </div>
      <!--Right Side-->
      <div class="relative pb-full md:flex md:pb-0 md:min-h-screen w-full md:w-1/2">
        <div class="absolute pin bg-cover bg-no-repeat md:bg-left lg:bg-center" style="background-image: url(images/plan.gif) "></div> 
      </div>
    </div>
    <script src="styles/jQuery-2.1.4.min.js "></script>
    <script language="JavaScript">
      $(document).ready(function () {
        $('#close').click(function () {
          window.close();
        });
      });
      function closeWindow() {
        window.close();
      }
    </script>
  </body>
</html>
