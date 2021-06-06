/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.arap;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.arap.AccPayable;
import com.dimata.posbo.entity.arap.PstAccPayable;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author dimata005
 */
public class SessAccPayablePaymentNumber {
    public static int getIntCode(Date pDate) {
	int max = 0;
	DBResultSet dbrs = null;
	Date date = new Date();
	try {
	    String sql = "SELECT NO_URUT AS PMAX" +
		    " FROM " + PstAccPayable.TBL_ACC_PAYABLE +
		    " WHERE PAYMENT_DATE LIKE '%"+Formater.formatDate(pDate, "yyyy-MM-dd")+"%' ORDER BY PAYMENT_DATE DESC LIMIT 0,1 ";

	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while (rs.next()) {
		max = rs.getInt("PMAX");
	    }
            
            max = max+1;
            
	} catch (Exception e) {
	    System.out.println("SessMatReceive.getIntCode() err : " + e.toString());
	} finally {
	    DBResultSet.close(dbrs);
	}
	return max;
    }
    
    public static String getCodePaymentNumber(AccPayable accPayable) {
	String code = "PAY";
	String dateCode = "";
	if (accPayable.getPaymentDate() != null) {
	    /** get location code; gwawan@21juni2007 */
	    int nextCounter = accPayable.getNoUrut();//getMaxCounter(date);
	    Date date = accPayable.getPaymentDate();

	    int tgl = date.getDate();
	    int bln = date.getMonth() + 1;
	    int thn = date.getYear() + 1900;

	    dateCode = (String.valueOf(thn)).substring(2, 4);

	    if (bln < 10) {
		dateCode = dateCode + "0" + bln;
	    } else {
		dateCode = dateCode + bln;
	    }

	    if (tgl < 10) {
		dateCode = dateCode + "0" + tgl;
	    } else {
		dateCode = dateCode + tgl;
	    }

	    String counter = "";
	    if (nextCounter < 10) {
		counter = "00" + nextCounter;
	    } else {
		if (nextCounter < 100) {
		    counter = "0" + nextCounter;
		} else {
		    counter = "" + nextCounter;
		}
	    }
	    code = code + "-" + dateCode +  "-" + counter;
	}
	return code;
    }
}
