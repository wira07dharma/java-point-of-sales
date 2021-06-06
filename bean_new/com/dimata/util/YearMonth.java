package com.dimata.util;

public class YearMonth
{
    public YearMonth() {
    }

    public static String getLongEngMonthName(int intMonth) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                               "July", "August", "September", "October","November","December"};
        if(intMonth < 1 || intMonth > 12 ) {
            return "Invalid month number, use 1 to 12";
        }
        return monthNames[intMonth - 1];
    }


    public static String getShortEngMonthName(int intMonth) {
        String strMonth = new String();
        strMonth = getLongEngMonthName(intMonth);
        if(strMonth.length() >= 3) {
            if(strMonth.substring(0,3).equalsIgnoreCase("Invalid")) {
                return strMonth;
            }
        }
        return strMonth.substring(0,3);
    }


    public static String getLongInaMonthName(int intMonth) {
        String[] monthNames = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                               "Juli", "Agustus", "September", "Oktober","November","Desember"};
        if(intMonth < 1 || intMonth > 12 ) {
            return "Invalid month number, use 1 to 12";
        }
        return monthNames[intMonth - 1];
    }


    public static String getShortInaMonthName(int intMonth) {
        String strMonth = new String();
        strMonth = getLongInaMonthName(intMonth);
        if(strMonth.length() >= 7) {
            if(strMonth.substring(0,7).equalsIgnoreCase("Invalid")) {
                return strMonth;
            }
        }
        return strMonth.substring(0,3);
    }

}


