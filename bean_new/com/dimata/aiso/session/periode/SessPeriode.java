/* Generated by Together */

package com.dimata.aiso.session.periode;

/* package java */
import java.util.*;
import java.util.Date;
import java.sql.*;

/* package qdep  */
import com.dimata.util.Formater;
import com.dimata.aiso.db.*;

/* package aiso  */
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.session.jurnal.*;

public class SessPeriode
{    
    /**
     * @param toDay
     * @param periodId
     * @return
     */    
    public static boolean isValidCloseBookTime(Date toDay, long periodId) 
    {
        String strWhClause = PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] + " = " + periodId;
        Vector tempPeriod = PstPeriode.list(0, 0, strWhClause, "");
        
        Periode per = new Periode();
        if (tempPeriod != null && tempPeriod.size() > 0) 
        {
            per = (Periode) tempPeriod.get(0);
        }
        else
        {
            return true;
        }

        toDay = new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate());
        if (toDay.before(per.getTglAkhir()))
        {            
            return false;
        }
        
        return true;        
    }    
 
    
    /*
     * This method used to set new period automatically 
     */
    public static long generateNewPeriod(long lLastPeriodId, int iMonthInterval, int iLastEntryDuration)
    {
        long lResult = 0;
        
        // definisikan nama bulan yg menjadi bagian nama periode
        String monthText[] = 
        {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        };

        
        // mencari data "tanggal akhir" dari periode terakhir 
        int iLastYear = 0;
        int iLastMonth = 0;
        int iLastDate = 0;        
        try
        {
            Periode objPeriode = PstPeriode.fetchExc(lLastPeriodId);
            iLastYear = objPeriode.getTglAkhir().getYear();
            iLastMonth = objPeriode.getTglAkhir().getMonth();
            iLastDate = objPeriode.getTglAkhir().getDate();            
        }
        catch(Exception e)
        {
            System.out.println("Exception when fetch data period : " + e.toString());
        }                

        
        // Set new date from new period
        Periode newObjPeriod = new Periode();
        Date newDate = new Date(iLastYear, iLastMonth, iLastDate+1);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(newDate);
        Date currentDate = newCalendar.getTime();        

        newObjPeriod.setTglAwal(newDate);
        newObjPeriod.setTglAkhir(new Date(iLastYear, iLastMonth+iMonthInterval, newCalendar.getActualMaximum(newCalendar.DAY_OF_MONTH)));
        newObjPeriod.setTglAkhirEntry(new Date(iLastYear, iLastMonth+iMonthInterval, iLastEntryDuration+newCalendar.getActualMaximum(newCalendar.DAY_OF_MONTH)));       
        newObjPeriod.setNama(monthText[currentDate.getMonth()] + " " +(1900+currentDate.getYear()));
        newObjPeriod.setPosted(PstPeriode.PERIOD_OPEN);       
        
        try
        {
           lResult = PstPeriode.insertExc(newObjPeriod);
        }
        catch(Exception e)
        {
           System.out.println("Exc when insert DATE TO METHOD generateNewPeriod  : " + e.toString());
        }
        
        return lResult;
    }    
    
    
    /**
     * @param lLastPeriodId
     * @param iMonthInterval
     * @param iLastEntryDuration
     * @return
     */    
    public boolean isNextPeriodIsNewYear(long lLastPeriodId, int iMonthInterval, int iLastEntryDuration)
    {
        boolean bResult = false;        
        
        try
        {
            Periode objPeriode = PstPeriode.fetchExc(lLastPeriodId);
            
            int iLastYear = objPeriode.getTglAkhir().getYear();
            int iLastMonth = objPeriode.getTglAkhir().getMonth();
            int iLastDate = objPeriode.getTglAkhir().getDate();     
            
            Date newDate = new Date(iLastYear, iLastMonth, iLastDate+1);
            if( (newDate.getYear() - iLastYear) > 0 )
            {
                bResult = true;
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception when isNextPeriodIsNewYear : " + e.toString());
        }                

        return bResult;
    }
    
        
    public static synchronized boolean isInYear(Date dateFrom, Date dateTo){
	boolean bResult = false;
	int iVariance = 0;
	if(dateFrom.before(dateTo)){
	    iVariance = dateTo.getYear() - dateFrom.getYear();
	    if(iVariance == 0){
		bResult = true;
	    }
	}
	return bResult;
    }
    
    public static synchronized int getTotMontInYear(Date dateFrom, Date dateTo){
	int iResult = 0;
	if(isInYear(dateFrom, dateTo)){
	    if(dateFrom.before(dateTo)){
		iResult = (dateTo.getMonth() - dateFrom.getMonth()) + 1;
	    }
	}
	return iResult;
    }
    
    
    
    public static synchronized int getTotYear(Date dateFrom, Date dateTo){
	int iResult = 0;
	if(!isInYear(dateFrom, dateTo)){
	    if(dateFrom.before(dateTo)){
		iResult = (dateTo.getYear() - dateFrom.getYear()) + 1;
	    }
	}
	return iResult;
    }
    
    public static synchronized int getTotMonth(Date dateFrom, Date dateTo){
	int iResult = 0;
	int iTotYear = 0;
	iTotYear = getTotYear(dateFrom, dateTo);
	if(iTotYear != 0){
	    iResult = (iTotYear * 12) - (dateFrom.getMonth()) - (12 - (dateTo.getMonth() + 1));
	}else{
	    iResult = getTotMontInYear(dateFrom, dateTo);
	}
	return iResult;
    }
}
