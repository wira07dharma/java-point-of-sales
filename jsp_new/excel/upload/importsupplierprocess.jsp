<%@ page import="com.dimata.posbo.entity.admin.AppObjInfo,
                 com.dimata.util.blob.TextLoader,
                 java.io.FileOutputStream,
                 java.io.ByteArrayInputStream,
                 java.io.InputStream,
                 org.apache.poi.hssf.usermodel.HSSFDateUtil,
                 com.dimata.util.Excel"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>


<!-- JSP Block -->

<!-- core java package -->
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_SUPPLIER); %>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Upload Excel &gt; Daftar Supplier<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->

				  <%
					// ngambil/upload file sesuai dengan yang dipilih oleh "browse"
					String a = request.getParameter("file");
                      System.out.println("file : "+a);
                      //ImportExcelPeserta.ImportPeserta("E:\\user\\gadnyana\\project\\JKJ\\summary\\2005-17-02\\datatest.xls");

					TextLoader uploader = new TextLoader();
					FileOutputStream fOut = null;
					Vector v = new Vector();
				    Vector vectErr = new Vector();
					try{
						uploader.uploadText(config, request, response);
						Object obj = uploader.getTextFile("file");
						byte byteText[] = null;
						byteText = (byte[]) obj;
						ByteArrayInputStream inStream;
						inStream = new ByteArrayInputStream(byteText);
						Excel tp = new Excel();

						// jumlah kolom untu6+k dp opname excel
						int numcol = 10;
						v = tp.ReadStream((InputStream) inStream, numcol);
						double dt = 0.0;

						// proses untuk data dp stock employee
						out.println("<form name=\"frmimportsupplier\" method=\"post\" action=\"importsuppliersave.jsp\">");
						out.println("&nbsp;<b>Daftar Supplier</b>");
						out.println("<table><tr><td>");
						out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\"><tr>");

						// Jika ukuran vector yang menyimpan data dari excel lebih dari nol, maka proses
						if(v!=null && v.size()>0){
							int maxV = v.size();

							// create header/title
							for(int tit=0; tit<numcol; tit++){
								out.println("<td class=\"listgentitle\">"+v.elementAt(tit)+"</td>");
							}
							out.println("</tr>");

							// iterasi dilakukan mulai indeks ke (numcol) karena baris pertama dari schedule excel adalah JUDUL/TITLE
							for(int i=numcol; i<maxV; i++){
                                boolean stserror = false;
                                String[] arrErrMessage = new String[11];
								// dicheck sisa hasil bagi antara i dengan numcol, maka akan diproses sbb :
								int it = i / numcol;
								switch ((i % numcol)){
									case 0 : // kalo sisanya 0 ==> pada kolom I (nomor peserta)
                                        if(v.elementAt(i)!=null){
                                            String w = v.elementAt(i).toString();
                                            if(w.matches("-") || w.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[1] = "KD";
                                            }
                                            out.println("<tr>");
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[1] = "KD";
                                            out.println("<tr>");
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kode\" value=\"\">&nbsp;</td>");
                                        }
										break;

									case 1 : // kalo sisanya 1 ==> pada kolom II (nama)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[2] = "TP";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"type\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[2] = "TP";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"type\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 2 : // kalo sisanya 2 ==> pada kolom III (ktp/NIK)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[3] = "NAMA";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"nama\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[3] = "NAMA";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"nama\" value=\"\">&nbsp;</td>");
                                        }
										break;

									case 3 : // kalo sisanya 3 ==> pada kolom IV (jns kelamin)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[4] = "AL";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"alamat\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[4] = "AL";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"alamat\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 4 : // kalo sisanya 4 ==> pada kolom V (siak)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[5] = "TLP";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"telpon\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[5] = "TLP";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"telpon\" value=\"" + v.elementAt(i) + "\">"+v.elementAt(i)+"</td>");
                                        }
										break;

									case 5 : // kalo sisanya 6 ==> pada kolom VII (gol peserta)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[6] = "FAX";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"fax\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[6] = "FAX";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"fax\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 6 : // kalo sisanya 7 ==> pada kolom VIII (tempat lahir)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[7] = "ngr";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"negara\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[7] = "ngr";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"negara\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 7 : // kalo sisanya 8 ==> pada kolom IX (tanggal lahir)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[8] = "prs";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"propinsi\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[8] = "prs";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"propinsi\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 8 : // kalo sisanya 9 ==> pada kolom X (nama kk)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[9] = "KOTA";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kota\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[9] = "KOTA";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kota\" value=\"\" >&nbsp;</td>");
                                        }
										break;

									case 9 : // kalo sisanya 10 ==> pada kolom XI (nomor KK)
                                        if(v.elementAt(i)!=null){
                                            String x = v.elementAt(i).toString();
                                            if(x.matches("-") || x.length()==0){
                                                stserror = true;
                                                arrErrMessage[0] = String.valueOf((i/numcol));
                                                arrErrMessage[10] = "KONTAK";
                                            }
										    out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kontak\" value=\"" + v.elementAt(i) + "\" >"+v.elementAt(i)+"</td>");
                                        }else{
                                            stserror = true;
                                            arrErrMessage[0] = String.valueOf((i/numcol));
                                            arrErrMessage[10] = "KONTAK";
                                            out.println("<td class=\"listgensell\"><input type=\"hidden\" name=\"kontak\" value=\"\" >&nbsp;</td>");
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

						out.println("</table>");
						out.println("</td></tr></table>");

						out.println("<table><tr><td>");
						out.println("<input type=\"submit\" value=\" Upload Data Supplier \">");
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
