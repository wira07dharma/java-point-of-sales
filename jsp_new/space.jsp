<%-- 
    Document   : space
    Created on : Jun 3, 2014, 9:58:59 AM
    Author     : sangtel6
--%>

<%
String val = request.getParameter("user_group");
String SESS_LANGUAGE_STRING = request.getParameter("SESS_LANGUAGE");
int groupUser = Integer.parseInt(val);
int SESS_LANGUAGE = Integer.parseInt(SESS_LANGUAGE_STRING);
/**
* pemilihan menu per user group.
* 0 = Admin
* 1 = Accounting Denpasar
* 2 = Accounting Bajo
* 3 = Staf Denpasar
* 4 = Staf Bajo
* 5 = Kasir Denpasar
* 6 = Kasir bajo
*/
	%>
<html>
<head>
<title>Prochain Logo</title>
<SCRIPT language=JavaScript>
<!--
function setMainFrameUrl(url){

 	parent.window.location=url;
}

function setSubFrames(upSize, midSize)
{
var url=parent.mainFrame.window.location;
parent.window.newframe = 

'<frameset cols="'+upSize+','+midSize+',*" frameborder="NO" border="0" framespacing="0">'+  
  '<frameset rows="5,*" frameborder="NO" border="0" framespacing="0">' + 
    '<frame name="logoFrame" scrolling="NO" noresize src="header.jsp" >' +
    '<frame name="leftFrame" scrolling="YES" noresize  src="leftmenu.jsp?user_group=<%=groupUser%>&SESS_LANGUAGE=<%=SESS_LANGUAGE%>">' +
  '</frameset>' +
  '<frameset rows="5,*" frameborder="NO" border="0" framespacing="0">' + 
    '<frame name="spaceTopFrame" scrolling="NO" noresize src="header.jsp" >' +
    '<frame name="spaceImageFrame" scrolling="NO" noresize src="spaceright.jsp?user_group=<%=groupUser%>&SESS_LANGUAGE=<%=SESS_LANGUAGE%>">' +
  '</frameset>' +
  '<frameset rows="5,*" frameborder="NO" border="0" framespacing="0" cols="*">' + 
    '<frame name="topFrame" scrolling="YES" noresize src="header.jsp" >' +
    '<frame name="mainFrame" id="mainFrame" scrolling="YES" noresize src="'+url+'">' +
  '</frameset>';
parent.window.location='javascript:parent.newframe';
}

/*function setSubFrames(upSize, upper, lower)
{
top.newframe =
'<FRAMESET rows="' + upSize + ',*" BORDER=3>' +
'<FRAME src="' + upper + '"' + ' NAME="upper">' +
'<FRAME src="' + lower + '"' + ' NAME="lower">' +
'</FRAMESET>';
top.leftframe.location='javascript:top.newframe';
}*/

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="styles/aisostyle/main.css" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="bodystyle" onLoad="MM_preloadImages('inactiveleft.jpg')">
<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#FFFFFF">  
  <tr> 
    <td height="500" > 
      <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
        <tr> 
          <td align="center"><a href="javascript:setSubFrames('0%','1.5%')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1','','activeleft.jpg',1)"><img src="inactiveleft.jpg" name="Image1" width="11" height="65" border="0"></a></td>
        </tr>
      </table>
    </td>
  </tr>  
</table>
</body>
</html>
