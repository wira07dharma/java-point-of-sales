/*
 * SessActvityPeriod.java
 *
 * Created on January 5, 2007, 10:26 AM
 */

package com.dimata.aiso.session.periode;

/* import package java*/
import java.util.*;
import java.util.Date.*;

/* import package utility*/
import com.dimata.util.*;

/* import package aiso*/
import com.dimata.aiso.entity.periode.*;

/**
 *
 * @author  dwi
 */
public class SessActvityPeriod {
    
    /** Creates a new instance of SessActvityPeriod */
    public SessActvityPeriod() {
    }
    
     public static boolean isValidCloseBookTime(Date toDay, long periodId) 
    {
        String strWhClause = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] + " = " + periodId;
        Vector tempPeriod = PstActivityPeriod.list(0, 0, strWhClause, "");
        
        ActivityPeriod actPeriod = new ActivityPeriod();
        if (tempPeriod != null && tempPeriod.size() > 0) 
        {
            actPeriod = (ActivityPeriod) tempPeriod.get(0);
        }
        else
        {
            return true;
             
        }

        toDay = new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate());
        System.out.println("toDay ====> "+toDay);
        System.out.println("actPeriod.getEndDate() ====> "+actPeriod.getEndDate());
        if (toDay.before(actPeriod.getEndDate()))
        {     
            System.out.println("toDay ====> "+toDay);
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
            ActivityPeriod actPeriod = PstActivityPeriod.fetchExc(lLastPeriodId);
            iLastYear = actPeriod.getEndDate().getYear();
            iLastMonth = actPeriod.getEndDate().getMonth();
            iLastDate = actPeriod.getEndDate().getDate();            
        }
        catch(Exception e)
        {
            System.out.println("Exception when fetch data period : " + e.toString());
        }                

        
        // Set new date from new period
        ActivityPeriod actPeriod = new ActivityPeriod();
        Date newDate = new Date(iLastYear, iLastMonth, iLastDate+1);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(newDate);
        Date currentDate = newCalendar.getTime();        

        actPeriod.setStartDate(newDate);
        actPeriod.setEndDate(new Date(iLastYear, iLastMonth+iMonthInterval, newCalendar.getActualMaximum(newCalendar.DAY_OF_MONTH)));          
        actPeriod.setName("Period:  " +(1900+currentDate.getYear()));
        actPeriod.setDescription("Period:  " +(1900+currentDate.getYear()));
        actPeriod.setPosted(PstActivityPeriod.PERIOD_OPEN);       
        
        try
        {
           lResult = PstActivityPeriod.insertExc(actPeriod);
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
            ActivityPeriod actPeriod = PstActivityPeriod.fetchExc(lLastPeriodId);
            
            int iLastYear = actPeriod.getEndDate().getYear();
            int iLastMonth = actPeriod.getEndDate().getMonth();
            int iLastDate = actPeriod.getEndDate().getDate();     
            
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
    
    public long closingBookYearly(long lPeriodeId, int iMonthInterval, int iLastEntryDuration) {
        long lResult = 0;

        PstActivityPeriod.setPeriodPosted(lPeriodeId);
            if (!PstActivityPeriod.openPrepareOpenPeriod(lPeriodeId)) {
                // 5. membuat periode baru
                 //SessActvityPeriod objSessActPeriod = new SessActvityPeriod();
                 lResult = generateNewPeriod(lPeriodeId, iMonthInterval, iLastEntryDuration);
                }           

        return lResult;
    }
    
}
