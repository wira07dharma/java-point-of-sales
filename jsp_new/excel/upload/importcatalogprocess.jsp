<%@ page import="com.dimata.posbo.entity.admin.AppObjInfo,
                 com.dimata.util.blob.TextLoader,
                 java.io.FileOutputStream,
                 java.io.ByteArrayInputStream,
                 java.io.InputStream,
                 org.apache.poi.hssf.usermodel.HSSFDateUtil,
                 com.dimata.util.Excel,
                 com.dimata.qdep.form.FRMQueryString"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>


<!-- JSP Block -->

<!-- core java package -->
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Upload Excel</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../../main/mnmain.jsp" %>
            <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Upload Excel &gt; Daftar Catalog<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->

				  <%
					// ngambil/upload file sesuai dengan yang dipilih oleh "browse"
					TextLoader uploader = new TextLoader();
					FileOutputStream fOut = null;
					Vector v = new Vector();
				    Vector vectErr = new Vector();
					try{

						uploader.uploadText(config, request, response);
                        String str = uploader.getRequestText("col_sheet");
                        System.out.println("objSheet.toString() : "+str);
						Object obj = uploader.getTextFile("file");

						byte byteText[] = null;
						byteText = (byte[]) obj;
						ByteArrayInputStream inStream;
						inStream = new ByteArrayInputStream(byteText);
						Excel tp = new Excel();

						// jumlah kolom untu6+k dp opname excel
						int numcol = 12;
                        if(Integer.parseInt(str)==0){
                            numcol = 12;
                        }else if(Integer.parseInt(str)==1){
                            numcol = 13;
                        }else if(Integer.parseInt(str)==2){
                            numcol = 9;
                        }

						v = tp.ReadStream((InputStream) inStream, numcol,Integer.parseInt(str),0);
						double dt = 0.0;

    					// proses untuk data dp stock employee
						if(Integer.parseInt(str)==0){
                            out.println("<form name=\"frmimportcatalog\" method=\"post\" action=\"importcatalogsave.jsp\">");
                            out.println("&nbsp;<b>Daftar Catalog</b>");
                        }else if(Integer.parseInt(str)==1){
                            out.println("<form name=\"frmimportcatalog\" method=\"post\" action=\"importcatalogsellpricesave.jsp\">");
                            out.println("&nbsp;<b>Daftar Harga Jual</b>");
                        }else if(Integer.parseInt(str)==2){
                            out.println("<form name=\"frmimportcatalog\" method=\"post\" action=\"importcatalogsupppricesave.jsp\">");
                            out.println("&nbsp;<b>Daftar Harga Supplier</b>");
                        }
						out.println("<table><tr><td>");
						out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\">");

						// Jika ukuran vector yang menyimpan data dari excel lebih dari nol, maka proses
						if(v!=null && v.size()>0){
							int maxV = v.size();
							// create header/title
                            out.println("<tr>");
							for(int tit=0; tit<numcol; tit++){
								out.println("<td class=\"listgentitle\">"+v.elementAt(tit)+"</td>");
							}
							out.println("</tr>");

							// iterasi dilakukan mulai indeks ke (numcol) karena
                            // baris pertama dari schedule excel adalah JUDUL/TITLE
                            if(Integer.parseInt(str)==0){
                                for(int i=numcol; i<maxV; i++){
                                    boolean stserror = false;
                                    String[] arrErrMessage = new String[12];
                                    // dicheck sisa hasil bagi antara i dengan numcol, maka akan diproses sbb :
                                    int it = i / numcol;
                                    switch ((i % numcol)){
                                        case 0 : // kalo sisanya 0 ==> pada kolom I (nomor peserta)
                                            if(v.elementAt(i)!=null){
                                                String w = v.elementAt(i).toString();
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"\">&nbsp;</td>");
                                            }
                                            break;

                                        case 1 : // kalo sisanya 1 ==> pada kolom II (nama)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"barcode\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"barcode\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 2 : // kalo sisanya 2 ==> pada kolom III (ktp/NIK)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"category\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"category\" value=\"\">&nbsp;</td>");
                                            }
                                            break;

                                        case 3 : // kalo sisanya 3 ==> pada kolom IV (jns kelamin)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"merk\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"merk\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 4 : // kalo sisanya 4 ==> pada kolom V (siak)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }
                                            break;

                                        case 5 : // kalo sisanya 6 ==> pada kolom VII (gol peserta)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"serial_number\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"serial_number\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 6 : // kalo sisanya 7 ==> pada kolom VIII (tempat lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"last_buy_cur\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"last_buy_cur\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 7 : // kalo sisanya 8 ==> pada kolom IX (tanggal lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buy_unit\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buy_unit\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 8 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"last_buy_price\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"last_buy_price\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 9 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"point\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"point\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 10 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sell_unit\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sell_unit\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 11 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"cogs\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"cogs\" value=\"\" >&nbsp;</td>");
                                            }
                                            out.println("</tr>");
                                            break;
                                        default :
                                            out.println("<td class=\"listgensell\">" + v.elementAt(i) + "</td>");
                                            break;

                                    }
                                    if(stserror)
                                        vectErr.add(arrErrMessage);
                                }
                            }
                            // ini untuk harga jual barang
                            else if(Integer.parseInt(str)==1){
                                for(int i=numcol; i<maxV; i++){
                                    boolean stserror = false;
                                    String[] arrErrMessage = new String[12];
                                    //System.out.println("asdjflksdjflka;sdlkf");
                                    // dicheck sisa hasil bagi antara i dengan numcol, maka akan diproses sbb :
                                    int it = i / numcol;
                                    switch ((i % numcol)){
                                        case 0 : // kalo sisanya 0 ==> pada kolom I (nomor peserta)
                                            if(v.elementAt(i)!=null){
                                                String w = v.elementAt(i).toString();
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"\">&nbsp;</td>");
                                            }
                                            break;

                                        case 1 : // kalo sisanya 1 ==> pada kolom II (nama)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 2 : // kalo sisanya 1 ==> pada kolom II (nama)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellcurr\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellcurr\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 3 : // kalo sisanya 2 ==> pada kolom III (ktp/NIK)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellrate\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellrate\" value=\"\">&nbsp;</td>");
                                            }
                                            break;

                                        case 4 : // kalo sisanya 3 ==> pada kolom IV (jns kelamin)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellagent\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellagent\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 5 : // kalo sisanya 4 ==> pada kolom V (siak)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellgenmember\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellgenmember\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }
                                            break;

                                        case 6 : // kalo sisanya 6 ==> pada kolom VII (gol peserta)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellpublic\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"sellpublic\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 7 : // kalo sisanya 7 ==> pada kolom VIII (tempat lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discagentmemberpercen\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discagentmemberpercen\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 8 : // kalo sisanya 8 ==> pada kolom IX (tanggal lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discagentmembervalue\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discagentmembervalue\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 9 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discgenmemberpercen\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discgenmemberpercen\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 10 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discgenmembervalue\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discgenmembervalue\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 11 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discpublicmemberpercen\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discpublicmemberpercen\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 12 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discpublicmembervalue\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discpublicmembervalue\" value=\"\" >&nbsp;</td>");
                                            }
                                            out.println("</tr>");
                                            break;
                                        default :
                                            out.println("<td class=\"listgensell\">" + v.elementAt(i) + "</td>");
                                            break;

                                    }
                                    if(stserror)
                                        vectErr.add(arrErrMessage);
                                }
                            }else if(Integer.parseInt(str)==2){
                                for(int i=numcol; i<maxV; i++){
                                    boolean stserror = false;
                                    String[] arrErrMessage = new String[9];
                                    // dicheck sisa hasil bagi antara i dengan numcol, maka akan diproses sbb :
                                    int it = i / numcol;
                                    switch ((i % numcol)){
                                        case 0 : // kalo sisanya 0 ==> pada kolom I (nomor peserta)
                                            if(v.elementAt(i)!=null){
                                                String w = v.elementAt(i).toString();
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<tr>");
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"\">&nbsp;</td>");
                                            }
                                            break;

                                        case 1 : // kalo sisanya 1 ==> pada kolom II (nama)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"name\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 2 : // kalo sisanya 1 ==> pada kolom II (nama)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"suppcode\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                stserror = true;
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"suppcode\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 3 : // kalo sisanya 3 ==> pada kolom IV (jns kelamin)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"suppname\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"suppname\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 4 : // kalo sisanya 4 ==> pada kolom V (siak)
                                            if(v.elementAt(i)!=null){
                                                String x = v.elementAt(i).toString();
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"currbuy\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"currbuy\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                            }
                                            break;

                                        case 5 : // kalo sisanya 6 ==> pada kolom VII (gol peserta)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buyingunit\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buyingunit\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 6 : // kalo sisanya 7 ==> pada kolom VIII (tempat lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buyingprice\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"buyingprice\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 7 : // kalo sisanya 8 ==> pada kolom IX (tanggal lahir)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discountpercen\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"discountpercen\" value=\"\" >&nbsp;</td>");
                                            }
                                            break;

                                        case 8 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                            if(v.elementAt(i)!=null){
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"ppnpercen\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                            }else{
                                                out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"ppnpercen\" value=\"\" >&nbsp;</td>");
                                            }
                                            out.println("</tr>");
                                            break;
                                        default :
                                            out.println("<td class=\"listgensell\">" + v.elementAt(i) + "</td>");
                                            break;

                                    }
                                    if(stserror)
                                        vectErr.add(arrErrMessage);
                                }
                            }
						}

						out.println("</table>");
						out.println("</td></tr></table>");

						out.println("<table><tr><td>");
						out.println("<input type=\"submit\" value=\" Upload Data Catalog\">");
						out.println("</td></tr></table>");

						out.println("</form>");
						inStream.close();

					}catch (Exception e){
						System.out.println("---===Error : " + e.toString());
					}
				%>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
            <%@ include file = "../../main/footer.jsp" %>
            <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
