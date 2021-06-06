<%@ page language="java" %>
<%@ include file="main/javainit.jsp"%>
<%@ page import="com.dimata.printman.RemotePrintMan,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.session.admin.SessUserSession,
                 java.util.Vector,
                 com.dimata.gui.jsp.ControlCombo" %>
<%!
 //final static int CMD_NONE =0;
 final static int CMD_LOGIN=1;
 final static int MAX_SESSION_IDLE=100000;
%>
  
<%  
int iCommand = Integer.parseInt((request.getParameter("command")==null) ? "0" : request.getParameter("command"));
int dologin = 0;  // SessUserSession.DO_LOGIN_OK ;
int appLanguage  =  0;
if(iCommand==CMD_LOGIN){    
	String loginID = FRMQueryString.requestString(request,"login_id");
	String passwd  = FRMQueryString.requestString(request,"pass_wd");
    appLanguage  = FRMQueryString.requestInt(request,"app_language");
	String remoteIP = request.getRemoteAddr();
	com.dimata.posbo.session.admin.SessUserSession  userSess = new com.dimata.posbo.session.admin.SessUserSession(remoteIP );
	dologin=userSess.doLogin(loginID, passwd);
	if(dologin==SessUserSession.DO_LOGIN_OK){
		session.setMaxInactiveInterval(MAX_SESSION_IDLE);
		session.putValue(SessUserSession.HTTP_SESSION_NAME, userSess);
		userSess = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);

        session.putValue("APPLICATION_LANGUAGE", String.valueOf(appLanguage));
        String strLang = "";
        if(session.getValue("APPLICATION_LANGUAGE")!=null){
            strLang = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
        }
        appLanguage = (strLang!=null && strLang.length()>0) ? Integer.parseInt(strLang) : 0;
	}
}  


/** cek integrasi */
int INTEGRASI_POS = 0;
int INTEGRASI_HANOMAN = 1;

int pos_integ = 0;
try {
	String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
	pos_integ = Integer.parseInt(designMat);
}
catch (Exception e) {
	pos_integ = 0;
}
%> 
<html>
<head>
<title>Dimata - ProChain POS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="styles/default.css" type="text/css">
<script language="JavaScript">
function cmdLogin(){	
  document.frmLogin.action = "login.jsp";
  document.frmLogin.submit();
}

function fnTrapUserName(){
   if(event.keyCode == 13){ 
		document.frmLogin.pass_wd.focus();
   }
}

function fnTrapPasswd(){
   if(event.keyCode == 13){ 
		document.all.aLogin.focus();
		cmdLogin();
   }
}
	
function keybrdPress(frmObj, event) {
	if(event.keyCode == 13) {
		switch(frmObj.name) {
			case 'login_id':
				document.all.pass_wd.focus();
				break;
			case 'pass_wd':
				document.all.aLogin.focus();
				cmdLogin();
				break;
			case 'app_language':
				document.all.aLogin.focus();
				cmdLogin();
				break;
			default:
				break;
		}
	}
}

//<!--
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
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name="frmLogin" action="">
<input type="hidden" name="command" value="<%=CMD_LOGIN%>">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" height="346">
    <tr> 
      <td width="100%" height="112">&nbsp;</td>
    </tr>
    <%
	if((iCommand==CMD_LOGIN)&&(dologin == SessUserSession.DO_LOGIN_OK)){
        System.out.println("Reload Host and Printers");
        //RemotePrintMan.reloadPrintersOnAllHost();
		response.sendRedirect("homepage.jsp");
    }
	%>
    <tr>
      <td width="100%" height="109" valign="top" align="center">
	  	<% if(pos_integ == INTEGRASI_POS) { %>
			<div align="center">
              <p>&nbsp;</p>
              <p>
				  <span class="biglogintitle"><font size="5"><b><font color="#0000CC" size="6">DIMATA&reg;</font></b></font></span>
				  <font size="6" color="#0000CC"><b>PROCHAIN</b></font><br>
				  <span class="smalllogintitle"><font color="#0000CC" size="2">- RETAIL BUSINESS MANAGEMENT SYSTEM -</font></span>
			  </p>
              <p>&nbsp;</p>
            </div>
			<% } else if(pos_integ == INTEGRASI_HANOMAN) { %>
                        <% switch (MODUS_VIEW) {
                        case MODUS_EHOTEL: %>
                            <div align="center">
                            <p>&nbsp;</p>
                            <p>
                                      <span class="biglogintitle"><font size="5"><b><font color="#0000CC" size="6">SMART E-HOTEL</font></b></font></span> <br><br>
                                      <span class="smalllogintitle"><font color="#0000CC" size="2">- PURCHASING &amp; INVENTORY SYSTEM -</font></span>
                            </p>
                            <p>&nbsp;</p>
                            </div>
                            <% break;
                        default:%>
                            <div align="center">
                            <p>&nbsp;</p>
                            <p>
                                      <span class="biglogintitle"><font size="5"><b><font color="#0000CC" size="6">HANOMAN</font></b></font></span> <br>
                                      <span class="smalllogintitle"><font color="#0000CC" size="2">- PURCHASING &amp; INVENTORY SYSTEM -</font></span>
                            </p>
                            <p>&nbsp;</p>
                            </div>
                        <%}%>


		<% } %>
	  </td>
    </tr>
    <tr>
      <td width="100%" valign="middle" align="center" height="143">
        <table width="339" border="0" cellpadding="0" cellspacing="0" align="center">
          <tr>
            <td colspan="3" height="28" valign="top">
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="296" height="28" valign="top" background="images/login_images/uppmidd.jpg"><img src="images/login_images/uper_login.jpg" width="253" height="28"></td>
                  <td width="43" valign="top" align="right" background="images/login_images/uppmidd.jpg"><img src="images/login_images/upcorner.jpg" width="12" height="28"></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td width="12" valign="top" background="images/login_images/left.jpg"><img src="images/login_images/left.jpg" width="12" height="17">
            </td>
            <td width="315" valign="top">
              <table width="100%" border="0" cellpadding="1" cellspacing="0" bgcolor="#52BAFF">
                <tr valign="middle">
                  <td width="108" height="15">&nbsp;</td>
                  <td width="207">&nbsp;</td>
                </tr>
                <tr valign="middle">
                  <td nowrap height="15">Username</td>
                  <td>
                    <input type="text" name="login_id" size="20" onKeyDown="javascript:keybrdPress(this, event)">
                  </td>
                </tr>
                <tr valign="middle">
                  <td height="15" nowrap>Password</td>
                  <td>
                    <input type="password" name="pass_wd" size="15" onKeyPress="javascript:keybrdPress(this, event)">
                  </td>
                </tr>
                <tr valign="middle">
                  <td width="108" height="15">Language</td>
                  <td width="207">
                    <%
						String strLang[] = com.dimata.util.lang.I_Language.langName;

						Vector vectValue = new Vector(1,1);
						vectValue.add(""+langDefault);
						vectValue.add(""+langForeign);

						Vector vectKey = new Vector(1,1);
						vectKey.add(strLang[langDefault]);
						vectKey.add(strLang[langForeign]);
					    out.println(ControlCombo.draw("app_language",null,""+appLanguage,vectValue,vectKey,"onKeyDown=\"javascript:keybrdPress(this)\""));
						%>
                    </td>
                </tr>
                <tr valign="middle">
                  <td width="108" height="15">&nbsp;</td>
                  <td width="207">&nbsp;</td>
                </tr>
              </table>
            </td>
            <td width="12" valign="top" background="images/login_images/right.jpg"><img src="images/login_images/right.jpg" width="12" height="17"></td>
          </tr>
          <tr>
            <td colspan="3" height="42" valign="top">
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="105" height="42" valign="top" background="images/login_images/bottom_middle.jpg"><img src="images/login_images/bottom_left_corner.jpg" width="9" height="42"></td>
                  <td width="195" valign="top" background="images/login_images/bottom_middle.jpg" align="right"><a href="javascript:cmdLogin()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image7','','images/login_images/button_f2.jpg',1)" onKeyDown="javascript:keybrdPress(this)"><img id="aLogin" name="Image7" border="0" src="images/login_images/button.jpg" width="102" height="42" alt="Click to login" ></a></td>
                  <td width="27" valign="top" background="images/login_images/bottom_middle.jpg">&nbsp;</td>
                  <td width="12" valign="top" align="right" background="images/login_images/bottom_middle.jpg"><img src="images/login_images/bottom_right_corner.jpg" width="12" height="42"></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td height="0" valign="bottom" width="0">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <!--input type="submit" value="Submit" style="width: 0; height: 0"-->
		  <tr>
            <td width="1003" height="33" valign="middle" align="center" class="loginfooter">
              <%if( (iCommand==CMD_LOGIN) && (dologin != SessUserSession.DO_LOGIN_OK)) {%>
              <font class="errfont" color="#FF0000"><%if(appLanguage==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){%>nama atau password salah...<%}else{%>
              username or password wrong, try again...
              <%}%></font> 
              <%}%>
			</td>
          </tr>
          <% switch (MODUS_VIEW) {
                case MODUS_EHOTEL: %>
                     <tr>
                        <td width="100%">
                          <div align="center">
                              <img src="images/telkom.gif"  width="150" height="58"></div>
                        </td>
                     </tr>
            <% break;
            default:%>

        <%}%>
          <tr>
            <td width="100%">
              <div align="center">
                 &nbsp;</div>
            </td>
          </tr>
          <tr> 
            <td width="1003" height="26" valign="middle" align="center" class="loginfooter">
			  Copyright &copy; 2010 Dimata&reg; IT Solution<br>
			  Jl. Imam Bonjol, Perum Cipta Selaras no. 12, Denpasar 80119 - BALI.<br>
			  Telp. (0361) 499029, 7869752; Fax (0361) 499029<br>
			  Website : <a href="http://www.dimata.com" class="footerLink">www.dimata.com</a><br>
			  Email : <a href="mailto:marketing@dimata.com" class="footerLink">marketing@dimata.com</a>
	  		</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  </form>
</body>
<script language="JavaScript">
 document.frmLogin.login_id.focus();
</script>
</html>
