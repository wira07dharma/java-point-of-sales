package com.dimata.uploadbarcode;

import com.dimata.posbo.db.DBResultSet;
import com.dimata.common.entity.system.PstSystemProperty;


import com.dimata.util.Formater;
import com.dimata.util.blob.SmartUpload;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Date;
import java.util.Hashtable;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jul 6, 2007
 * Time: 3:37:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeTextFile {

    protected HttpServletRequest m_request;
    protected HttpServletResponse m_response;
    protected ServletContext m_application;
    protected ServletConfig m_config;
    private SmartUpload smartUploader = new SmartUpload();

    public BarcodeTextFile(ServletConfig config, HttpServletRequest request, HttpServletResponse response)
	    throws ServletException {

	m_config = config;
	m_application = config.getServletContext();
	m_request = request;
	m_response = response;
    }

    public static String ClearSpace(String str) {
	String valchr = "";
	int idxStart = 0;
	int idxEnd = 0;
	System.out.println(str);
	if (str.length() > 0) {
	    for (int k = 0; k < str.length(); k++) {
		valchr = str.substring(k, k + 1); // Right(Left(sNextLine, x + 1), 1);
		System.out.println("start>" + str + "-" + valchr);
		if (!valchr.matches(" ")) {
		    idxStart = k;
		    break;
		}
	    }

	    for (int j = str.length(); j > 0; j--) {
		valchr = str.substring(j - 1, j);
		System.out.println("end>" + str + "-" + valchr);
		if (!valchr.matches(" ")) {
		    idxEnd = j;
		    break;
		}
	    }
	    return str.substring(idxStart, idxEnd);
	} else {
	    return str = "";
	}  
    }

    public void renameFile(String path) { 
	try {
	    File virtualFile = new File(path);
	    System.out.println("################################ : " + virtualFile.getParent());
	    virtualFile.renameTo(new File(virtualFile.getParent()+"/"+Formater.formatDate(new Date(), "yyMMddHHmmss") + ".dat"));
	} catch (Exception e) {
	}
    }

    public Hashtable downloadTeFile(String path, int textFormat) {
	ResultTextFile resultTextFile = new ResultTextFile();

	try {
	    int maxCol1 = Integer.parseInt(PstSystemProperty.getValueByName("BARCODE_DIGIT_COL_1"));
	    int maxCol2 = Integer.parseInt(PstSystemProperty.getValueByName("BARCODE_DIGIT_COL_2"));
	    int maxCol3 = Integer.parseInt(PstSystemProperty.getValueByName("BARCODE_DIGIT_COL_3"));
	    int maxCol4 = Integer.parseInt(PstSystemProperty.getValueByName("BARCODE_DIGIT_COL_4"));

	    System.out.println("==>>> filename : " + path);
	    FileInputStream fstream = new FileInputStream(path);
	    System.out.println("==>>> filename : " + path);
	    DataInputStream ds = new DataInputStream(fstream);
	    System.out.println("==>>> filename : " + path);
	    BufferedReader in = new BufferedReader(new InputStreamReader(ds));


	    //BufferedReader in = new BufferedReader(new FileReader(path));
            int line=0;

	    String str;
	    DBResultSet dbrs = null;
	    Vector list = new Vector();
	    while ((str = in.readLine()) != null) {
		System.out.println(str);
		try {
		    list = new Vector();
		    String str1 = "";
		    switch (textFormat) {
			case 0:
			    // col 1
			    str1 = str.substring(0, maxCol1);
			    list.add(ClearSpace(str1));

			    //col 2
			    str1 = str.substring(maxCol1, maxCol2 + maxCol1);
			    list.add(ClearSpace(str1));

			    //col 3
			    str1 = str.substring(maxCol2 + maxCol1, maxCol3 + maxCol2 + maxCol1).trim();
			    list.add(ClearSpace(str1));

			    //col 4
			    str1 = str.substring(maxCol2 + maxCol1 + maxCol3, maxCol4 + maxCol3 + maxCol2 + maxCol1).trim();
			    list.add(ClearSpace(str1));
                                                    line=line+1;
			    break;
			case 1:
			    // col 1
			    str1 = str.substring(0, maxCol1);
			    list.add(ClearSpace(str1));

			    //col 2
			    str1 = str.substring(maxCol1, maxCol2 + maxCol1);
			    list.add(ClearSpace(str1));

			    //col 3
			    str1 = str.substring(maxCol2 + maxCol1, maxCol3 + maxCol2 + maxCol1).trim();
			    list.add(ClearSpace(str1));
                                                    line=line+1;
			    break;
		    }
		    System.out.println("result list =>> : " + list);
		    Date dt = new Date();
                    int month = 0;
                    int year = 0;
		    switch (textFormat) {
			case 0: // format with qty
			    String dateFormatText = (String) list.get(3);
			    //String format = dt.getYear()+ "-" + dateFormatText.substring(2, 4) + "-" + dateFormatText.substring(4, 6) + " " + dateFormatText.substring(6, 8) + ":" + dateFormatText.substring(8, 10);
			    dt.setDate(Integer.parseInt(dateFormatText.substring(4, 6)));
                            month = Integer.parseInt(dateFormatText.substring(2, 4));
                            //dt.setMonth(Integer.parseInt(dateFormatText.substring(2, 4)));
                            dt.setMonth(month-1);
                            year = Integer.parseInt(dateFormatText.substring(0, 2));
                            dt.setYear(year+100);
                            //dt.setYear(Integer.parseInt(dateFormatText.substring(0, 2)));
			    //dt.setMinutes(Integer.parseInt(dateFormatText.substring(2, 4)));
			    dt.setHours(Integer.parseInt(dateFormatText.substring(6, 8))); // Date(Integer.parseInt(dateFormatText.substring(4, 6)));
			    dt.setMinutes(Integer.parseInt(dateFormatText.substring(8, 10)));
			    Date date = new Date(dt.getYear(), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes()); //Formater.formatDate(format,"yyyy-MM-dd HH:mm");
                            Date onlyDate = new Date(dt.getYear(), dt.getMonth(), dt.getDate());
			    System.out.println("tanggal >>>> " + date);
                            System.out.println("tanggalAja >>>> " + onlyDate);
			    resultTextFile.setResultTextFile((String) list.get(0), (String) list.get(1), Integer.parseInt((String) list.get(2)), date, onlyDate);
			    break;
			case 1:
			    dateFormatText = (String) list.get(2);
			    dt.setDate(Integer.parseInt(dateFormatText.substring(4, 6)));
			    dt.setMinutes(Integer.parseInt(dateFormatText.substring(2, 4)));
			    dt.setHours(Integer.parseInt(dateFormatText.substring(6, 8))); // Date(Integer.parseInt(dateFormatText.substring(4, 6)));
			    dt.setMinutes(Integer.parseInt(dateFormatText.substring(8, 10)));
			    Date date1 = new Date(dt.getYear(), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes()); //Formater.formatDate(format,"yyyy-MM-dd HH:mm");
                            Date onlyDate1 = new Date(dt.getYear(), dt.getMonth(), dt.getDate());
			    System.out.println("tanggal >>>> " + date1);
			    resultTextFile.setResultTextFile((String) list.get(0), (String) list.get(1), 1, date1, onlyDate1);
			    break;
		    }
		} catch (Exception e) { 
		    System.out.println("EROOR =>> : " + e +"line ke :"+line);
		}
	    }
	    System.out.println("result =>> : " + resultTextFile.getResultTextFile());
	    in.close();
	} catch (IOException e) {
	    System.out.println(e);
	    return null;
	}
	return resultTextFile.getResultTextFile();
    }
	
	public Hashtable downloadTeFileMobicom(String path, int textFormat) {
	ResultTextFile resultTextFile = new ResultTextFile();

	try {
	    System.out.println("==>>> filename : " + path);
	    FileInputStream fstream = new FileInputStream(path);
	    System.out.println("==>>> filename : " + path);
	    DataInputStream ds = new DataInputStream(fstream);
	    System.out.println("==>>> filename : " + path);
	    BufferedReader in = new BufferedReader(new InputStreamReader(ds));


	    //BufferedReader in = new BufferedReader(new FileReader(path));
            int line=0;

	    String str;
	    DBResultSet dbrs = null;
	    Vector list = new Vector();
	    while ((str = in.readLine()) != null) {
		System.out.println(str);
		try {
			
			list = new Vector();
		    String str1 = "";
			
			String [] data = str.split(",");
			for (String i : data){
				list.add(i);
			}
			line=line+1;
		    System.out.println("result list =>> : " + list);
		    Date dt = new Date();
                    int month = 0;
                    int year = 0;
					
			String dateFormatText = (String) list.get(3);
			dt.setDate(Integer.parseInt(dateFormatText.substring(6, 8)));
                            month = Integer.parseInt(dateFormatText.substring(4, 6));
                            //dt.setMonth(Integer.parseInt(dateFormatText.substring(2, 4)));
                            dt.setMonth(month-1);
                            year = Integer.parseInt(dateFormatText.substring(0, 4));
                            dt.setYear(year-1900);
                            //dt.setYear(Integer.parseInt(dateFormatText.substring(0, 2)));
			    //dt.setMinutes(Integer.parseInt(dateFormatText.substring(2, 4)));
			    dt.setHours(Integer.parseInt(dateFormatText.substring(8, 10))); // Date(Integer.parseInt(dateFormatText.substring(4, 6)));
			    dt.setMinutes(Integer.parseInt(dateFormatText.substring(10, 12)));
			    Date date = new Date(dt.getYear(), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes()); //Formater.formatDate(format,"yyyy-MM-dd HH:mm");
                            Date onlyDate = new Date(dt.getYear(), dt.getMonth(), dt.getDate());
			    System.out.println("tanggal >>>> " + date);
                            System.out.println("tanggalAja >>>> " + onlyDate);
			    resultTextFile.setResultTextFile((String) list.get(0), (String) list.get(1), Integer.parseInt((String) list.get(2)), date, onlyDate);
		    
		} catch (Exception e) { 
		    System.out.println("EROOR =>> : " + e +"line ke :"+line);
		}
	    }
	    System.out.println("result =>> : " + resultTextFile.getResultTextFile());
	    in.close();
	} catch (IOException e) {
	    System.out.println(e);
	    return null;
	}
	return resultTextFile.getResultTextFile();
    }
}
