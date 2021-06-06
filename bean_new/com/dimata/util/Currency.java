
package com.dimata.util;

import java.text.*;
import java.util.*;

public class Currency
{
    private float value = -1;
    private String format = "";
    private String simbol = "";

    public Currency() {
    }

    public Currency(float value) {
        this.value = value;
        this.format = "#,###.00";
    }

    public Currency(float value, String format) {
        this.value = value;
        this.format = format.trim();
    }

    public Currency(float value, String format, String simbol) {
        this.value = value;
        this.simbol = simbol.trim();
        this.format = format.trim();
    }


    public void setValue(float value){
        this.value = value;
    }

    public void setFormat(String format){
        this.format = format;
    }

    public void setSimbol(String simbol){
        this.simbol = simbol;
    }

    public float getValue() {
        return value;
    }

    public String getFormat() {
        return format;
    }

    public String getSimbol() {
        return simbol;
    }


    public String getFormatedCurrency(){
        String str = "No value";

        if(value >= 0) {
            str = simbol + " " + String.valueOf(value);

            if(!format.equals("") || format != null) {
                try {
                    DecimalFormat df = new DecimalFormat(format);
                    str = simbol + " " + df.format(value).toString();
                } catch(Exception e) {
                    return e.toString();
                }
            }
        }

        return str;
    }

    public String getLocaleCurrency(long number, Locale local) {
        this.value = number;
        NumberFormat df = NumberFormat.getCurrencyInstance(local);
        return df.format(this.value).toString();
    }

} //end of Currency
