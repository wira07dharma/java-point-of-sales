package com.dimata.util;

public class WeekDay
{
    public static final int WEEKDAY_SUNDAY = 0;
    public static final int WEEKDAY_MONDAY = 1;
    public static final int WEEKDAY_TUESDAY =2;
    public static final int WEEKDAY_WEDNESDAY = 3;
    public static final int WEEKDAY_THURSDAY = 4;
    public static final int WEEKDAY_FRIDAY = 5;
    public static final int WEEKDAY_SATURDAY = 6;


    public static final int LANG_ENGLISH = 0;
    public static final int LANG_BAHASA_INDONESIA = 1;

    public static String[] longEngDayName={ "Sunday", "Monday","Tuesday",
                            "Wednesday", "Thursday", "Friday", "Saturday"};

    public static String[] shortEngDayName={ "Sun", "Mon","Tue",
                            "Wed", "Thu", "Fri", "Sat"};

    public static String[] longInaDayName={"Minggu","Senin", "Selasa","Rabu",
                            "Kamis", "Jumat", "Sabtu"};

    public WeekDay() {
    }

    public static String getLongEngDayName(int intDay) {
        String strDay = new String();

        if((intDay>=WEEKDAY_SUNDAY) && (intDay<=WEEKDAY_SATURDAY))
            strDay= longEngDayName[intDay];
        else
            strDay = "Invalid day number, use 0 to 6";
        return strDay;
    }


    public static String getShortEngDayName(int intDay) {
        String strDay = new String();

        if((intDay>=WEEKDAY_SUNDAY) && (intDay<=WEEKDAY_SATURDAY))
            strDay= shortEngDayName[intDay];
        else
            strDay = "Invalid day number, use 0 to 6";
        return strDay;
    }


    public static String getLongInaDayName(int intDay) {
        String strDay = new String();

        if((intDay>=WEEKDAY_SUNDAY) && (intDay<=WEEKDAY_SATURDAY))
            strDay= longInaDayName[intDay];
        else
            strDay = "Invalid day number, use 0 to 6";
        return strDay;
    }

    public static String getLongWeekDayName(int iLanguage, int intDay) {

        String strDay = new String();

        if((intDay>=WEEKDAY_SUNDAY) && (intDay<=WEEKDAY_SATURDAY))
        {
          switch  (iLanguage)
          {
            case LANG_ENGLISH:
                strDay= longEngDayName[intDay];
                break;

            case LANG_BAHASA_INDONESIA:
                strDay= longInaDayName[intDay];
                break;

            default: // default is english
                strDay= longEngDayName[intDay];
                break;

          }
        }
        else
            strDay = "Invalid day number, use 0 to 6";
        return strDay;
    }

}


