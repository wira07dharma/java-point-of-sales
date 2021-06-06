
package com.dimata.util;

import java.text.*;
import java.util.*;

public class Formater {
    public static String FORMAT_DATE_SHORT_US ="MM/dd/yyyy";
    public static String FORMAT_DATE_SHORT_EUROPE ="dd/MM/yyyy";
    
    public Formater() {
    }
    
    public static String formatShortEuropeToUS(String strDateShortUS) {
        return formatDate(strDateShortUS,FORMAT_DATE_SHORT_EUROPE,FORMAT_DATE_SHORT_US);
    }
    
    
    public static String formatDate(String strDate, String inFormat, String outFormat) {
        String strRet = new String();
        SimpleDateFormat outDF = null;
        Date dt = null;
        
        try {
            SimpleDateFormat inDF = (SimpleDateFormat)DateFormat.getDateInstance();
            inDF.applyPattern(inFormat);
            dt = inDF.parse(strDate);
            
            outDF = (SimpleDateFormat)DateFormat.getDateInstance();
            outDF.applyPattern(outFormat);
            strRet = outDF.format(dt).toString();
            
        } catch(ParseException pe) {
            strRet = "ERROR::" + pe.toString();
        } catch(Exception e) {
            strRet = "ERROR::" + e.toString();
        }
        return strRet;
    }

     /**
     * to formating date by outFormat pattern. If date is null or another exception, outString will returned
     * @param date Date will format
     * @param outFormat String pattern
     * @param outString String out if null or exception
     * @return
     */
    public static String formatDate(Date date, String outFormat, String outString) {
        String strRet;
        SimpleDateFormat outDF = null;

        try {
            outDF = (SimpleDateFormat)DateFormat.getDateInstance();
            outDF.applyPattern(outFormat);
            strRet = outDF.format(date).toString();

        } catch(Exception e) {
            strRet = outString;
        }
        return strRet;
    }
    
    public static String formatDate(Date date) {
        String strRet = new String();
        
        int dtValue = Validator.getIntDate(date);
        int mnValue = Validator.getIntMonth(date);
        int yrValue = Validator.getIntYear(date);
        
        try {
            strRet = ""+dtValue+" "+YearMonth.getLongInaMonthName(mnValue)+" "+yrValue;
        } catch(Exception e) {
        }
        return strRet;
    }
    
    
    public static String formatDate(Date date, String outFormat) {
        String strRet = new String();
        SimpleDateFormat outDF = null;
        
        try {
            outDF = (SimpleDateFormat)DateFormat.getDateInstance();
            outDF.applyPattern(outFormat);
            strRet = outDF.format(date).toString();
            
        } catch(Exception e) {
            System.out.println("ERROR::" + e.toString());
        }
        return strRet;
    }
    
    
    /**/
    public static Date reFormatDate(Date date, String outFormat) {
        String strRet = new String();
        SimpleDateFormat outDF = null;
        SimpleDateFormat inDF = null;
        
        Date dt = null;
        try {
            outDF = (SimpleDateFormat)DateFormat.getDateInstance();
            outDF.applyPattern(outFormat);
            strRet = outDF.format(date).toString();
            
            inDF = (SimpleDateFormat)DateFormat.getDateInstance();
            inDF.applyPattern(outFormat);
            dt = inDF.parse(strRet);
            
        } catch(Exception e) {
            strRet = "ERROR::" + e.toString();
        }
        return dt;
    }
    
    
    public static String formatOnlyDateShortMonth(Date date) {
        String strRet = new String();
        
        try {
            strRet= Integer.toString(date.getDate()) + " " + YearMonth.getShortEngMonthName(date.getMonth()+1);
        } catch(Exception e) {
            strRet = "ERROR::" + e.toString();
        }
        return strRet;
    }
    
    
    
    public static Date formatDate(String strDate, String inFormat) {
        Date dt = null;
        try {
            SimpleDateFormat inDF = (SimpleDateFormat)DateFormat.getDateInstance();
            inDF.applyPattern(inFormat);
            dt = inDF.parse(strDate);
        } catch(Exception e) {
            System.out.println("ERROR::" + e.toString());
        }
        return dt;
    }
    
    public static String formatOnlyDateMonth(Date date) {
        String strRet = new String();
        
        try {
            strRet= Integer.toString(date.getDate()) + " " + YearMonth.getLongEngMonthName(date.getMonth()+1);
        } catch(Exception e) {
            strRet = "ERROR::" + e.toString();
        }
        return strRet;
    }
    
    public static String formatTimeLocale(Date dateTime) {
        return formatTimeLocale(dateTime, "" );
    }
    
    public static String formatTimeLocale(Date dateTime, String outFormat ) {
        String strRet = new String();
        SimpleDateFormat outDF = null;
        
        if( (outFormat==null) || (outFormat.length()==0))
            outFormat ="kk:mm";
        
        try {
            outDF = (SimpleDateFormat)DateFormat.getDateInstance();
            outDF.applyPattern(outFormat);
            strRet = outDF.format(dateTime).toString();
            
        } catch(Exception e) {
            strRet = "ERROR::" + e.toString();
        }
        return strRet;
        
        //return Integer.toString(dateTime.getHours())+":"+ Integer.toString(dateTime.getMinutes());
    }
    
    public static String formatNumber(double number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number).toString();
    }
    
    public static String formatNumber(int number) {
        String strNumber = "";
        if(number>9){
            strNumber = strNumber + number;
        }
        else{
            strNumber = strNumber + "0" +number;
        }
        return strNumber;
    }
    
    public static String formatNumber(long number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number).toString();
    }
    public static String formatNumber(float number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format((double)number).toString();
    }
    
    public static String formatLocale(long number, Locale local) {
        NumberFormat df = NumberFormat.getCurrencyInstance(local);
        return df.format(number).toString();
    }
    
    /**
     * modified by yogi
     */
    public static String formatDate(Date date, int languange, String outFormat) {
        String strRet = new String();
        
        Vector vect = Validator.tokenToVector(outFormat," ");
        String strDt = "";
        String strMn = "";
        String strYr = "";
        if(vect!=null&&vect.size()>0){
            strDt = (String)vect.get(0);
            strMn = (String)vect.get(1);
            strYr = (String)vect.get(2);
        }
        
        int dtValue = 0;
        int mnValue = 0;
        int yrValue = 0;
        String strDtValue = "";
        
        // if language is default
        if(languange==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
            dtValue = Validator.getIntDate(date);
            mnValue = Validator.getIntMonth(date);
            yrValue  = Validator.getIntYear(date);
            
            if(dtValue<10){
                strDtValue = "0"+dtValue;
            }else{
                strDtValue = ""+dtValue;
            }
            
            switch(strMn.length()){
                case 1:
                    strRet = strDtValue+" "+YearMonth.getShortInaMonthName(mnValue)+" "+yrValue;
                    break;
                case 2:
                    strRet = strDtValue+" "+YearMonth.getShortInaMonthName(mnValue)+" "+yrValue;
                    break;
                case 3:
                    strRet = strDtValue+" "+YearMonth.getShortInaMonthName(mnValue)+" "+yrValue;
                    break;
                case 4:
                    strRet = strDtValue+" "+YearMonth.getLongInaMonthName(mnValue)+" "+yrValue;
                    break;
                default:
                    strRet = strDtValue+" "+YearMonth.getLongInaMonthName(mnValue)+" "+yrValue;
                    break;
            }
            
            // if language is foreign
        }else{
            strRet = formatDate(date,outFormat);
        }
        return strRet;
    }
    
    /*
    public static String formatNumber(float dnumber, String format) {
        DecimalFormat df = new DecimalFormat(format);
        String number=df.format(dnumber).toString();  
        
        String numberSebenarnya="";
        String numberSebenarnya1="";
        String numberOutKoma="";
        
        try{
            StringTokenizer strToken=new StringTokenizer(format,".");
            String str="";
            
            while (strToken.hasMoreTokens()){
                str=strToken.nextToken();
            }
            
            if (format.length()==str.length())
                str="";
            
            // cek for fill decimal
            for (int zero=0;zero<str.length();zero++){
                numberOutKoma=numberOutKoma + "0";
            }
            
            df = new DecimalFormat("###." + numberOutKoma);
            String number1=df.format(dnumber).toString();
            
            // before dot
            try{
                long a=Long.parseLong(number);
                if (str.length()>0){
                    numberSebenarnya1=a + "." +numberOutKoma;
                }else{
                    numberSebenarnya1=a + numberOutKoma;
                }
            }catch (Exception e){
                
                for (int a=0;a<number.length();a++){
                    String chr=number.substring(a,a+1);
                    if (chr.equals(".")){
                        numberOutKoma="" ;
                        numberSebenarnya1=numberSebenarnya;
                        numberOutKoma=number.substring(a+1,number.length());
                        
                        for (int b=0;b<number1.length();b++){
                            String chr1=number1.substring(b,b+1);
                            if (chr1.equals(".")){
                                numberOutKoma=number1.substring(b+1,number1.length());
                                
                                if (numberOutKoma.length()>2){
                                    numberOutKoma=numberOutKoma.substring(0,str.length());
                                    break;
                                }
                            }
                        }
                    }else{
                        numberSebenarnya=numberSebenarnya+number.substring(a,a+1);
                    }
                }
                
                if (numberSebenarnya1.length()<1)
                    numberSebenarnya1=number+ "." +numberOutKoma;
                else
                    numberSebenarnya1=numberSebenarnya1+ "." +numberOutKoma;
                
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }finally{
            return numberSebenarnya1;
            
        }
    }
    */
    
    
    public static String formatDurationTime(Date startDateTime, Date toDateTime, boolean showSeconds){
        
        /* days hh:mm format*/
        long duration = DateCalc.timeDifference(startDateTime, toDateTime);
        //System.out.println("duration : "+duration);
        
        long days = 0;
        long left = 0;
        if(duration > DateCalc.DAY_MILLI_SECONDS){
            days = duration / DateCalc.DAY_MILLI_SECONDS;
            // System.out.println("days : "+days);
            left = duration % DateCalc.DAY_MILLI_SECONDS;
            //  System.out.println("left : "+left);
        }
        else{
            left = duration;
        }
        
        long hours = 0;
        //System.out.println("DateCalc.HOURS_MILLI_SECONDS : "+DateCalc.HOURS_MILLI_SECONDS);
        if(left > DateCalc.HOURS_MILLI_SECONDS){
            hours = left / DateCalc.HOURS_MILLI_SECONDS;
            left = left % DateCalc.HOURS_MILLI_SECONDS;
        }
        
        //System.out.println("hours : "+hours);
        // System.out.println("left : "+left);
        
        // System.out.println("DateCalc.MINUTES_MILLI_SECONDS : "+DateCalc.MINUTES_MILLI_SECONDS);
        
        long minutes = 0;
        if(left > DateCalc.MINUTES_MILLI_SECONDS){
            minutes = left / DateCalc.MINUTES_MILLI_SECONDS;
            left = left % DateCalc.MINUTES_MILLI_SECONDS;
        }
        
        // System.out.println("minutes : "+minutes);
        // System.out.println("left : "+left);
        
        //  System.out.println("DateCalc.SECONDS_MILLI_SECONDS : "+DateCalc.SECONDS_MILLI_SECONDS);
        
        long seconds = 0;
        if(left > DateCalc.SECONDS_MILLI_SECONDS){
            seconds = left / DateCalc.SECONDS_MILLI_SECONDS;
        }
        
        // System.out.println("seconds : "+seconds);
        
        String durationStr = "";
        if(days > 0){
            durationStr = days+ " day(s)";
        }
        
        if(hours > 0 ){
            durationStr = durationStr +" "+hours;
        }
        else{
            durationStr = durationStr +" 0";
        }
        
        if(minutes > 0){
            durationStr = durationStr +":"+getTimeString(minutes);
        }
        else{
            durationStr = durationStr +":00";
        }
        
        /* days hh:mm:ss format*/
        if(showSeconds){
            if(seconds > 0){
                durationStr = durationStr +":"+seconds;
            }
            else{
                durationStr = durationStr +":00";
            }
        }
        
        return durationStr;
    }
    
    public static String getTimeString(long number){
        if((""+number).length()<2){
            return "0"+number;
        }
        else{
            return ""+number;
        }
    }
    
    
    public static void main(String args[]){
        Date dt = new Date(104,3,1);
        String str = formatDate(dt,com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT,"dd MMM yyyy");
        String str2 = formatDate(dt,com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT,"dd MMMM yyyy");
        String str3 = formatDate(dt,com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN,"MMM dd, yyyy");
        System.out.println("str="+str);
        System.out.println("str2="+str2);
        System.out.println("str3="+str3);
    }
    
} // end of Formater



