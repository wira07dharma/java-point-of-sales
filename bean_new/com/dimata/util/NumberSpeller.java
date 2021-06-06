
/**
 * Version     : 1.0
 * Copyright   : Copyright (c) March, 2001
 * Author      : Wihita Team
 * Company     : P.T Wihita Prakasa
 * Description : Wihita Project
 *
 *
 * int a = 1056;
 * int b = 1000;
 * int x;
 *
 * x = a % b;  -> mod = 56
 * x = a / b;  -> div = 1
 *
 * in this program :
 * a known as num
 * b known as denum
 *
 * max int can be convered is 2147483647
 */


package com.dimata.util;

import java.util.*;

public class NumberSpeller
{

    private StringBuffer strBuffSpell;

    public NumberSpeller() {
        strBuffSpell = new StringBuffer();
    }


    /**
     * List of english basic number
     */
    private void spellBasicNumToEng(int num) {
        String inaBasicStr = new String();

        switch(num) {
            case 1 : strBuffSpell.append(" One"); break;
            case 2 : strBuffSpell.append(" Two"); break;
            case 3 : strBuffSpell.append(" Three"); break;
            case 4 : strBuffSpell.append(" Four"); break;
            case 5 : strBuffSpell.append(" Five"); break;
            case 6 : strBuffSpell.append(" Six"); break;
            case 7 : strBuffSpell.append(" Seven"); break;
            case 8 : strBuffSpell.append(" Eight"); break;
            case 9 : strBuffSpell.append(" Nine"); break;
            case 10: strBuffSpell.append(" Ten"); break;
            case 11: strBuffSpell.append(" Eleven"); break;
            case 12: strBuffSpell.append(" Twelve"); break;
            case 13: strBuffSpell.append(" Thirteen"); break;
            case 14: strBuffSpell.append(" Fourteen"); break;
            case 15: strBuffSpell.append(" Fifteen"); break;
            case 16: strBuffSpell.append(" Sixteen"); break;
            case 17: strBuffSpell.append(" Seventeen"); break;
            case 18: strBuffSpell.append(" Eighteen"); break;
            case 19: strBuffSpell.append(" Nineteen"); break;
            case 20: strBuffSpell.append(" Twenty"); break;
            case 30: strBuffSpell.append(" Trhity"); break;
            case 40: strBuffSpell.append(" Forty"); break;
            case 50: strBuffSpell.append(" Fifty"); break;
            case 60: strBuffSpell.append(" Sixty"); break;
            case 70: strBuffSpell.append(" Seventy"); break;
            case 80: strBuffSpell.append(" Eighty"); break;
            case 90: strBuffSpell.append(" Ninety"); break;
        }
    }

    /**
     * List of Indonesian basic number
     */
    private void spellBasicNumToIna(int num) {
        String inaBasicStr = new String();

        switch(num) {
            case 1 : strBuffSpell.append(" Satu"); break;
            case 2 : strBuffSpell.append(" Dua"); break;
            case 3 : strBuffSpell.append(" Tiga"); break;
            case 4 : strBuffSpell.append(" Empat"); break;
            case 5 : strBuffSpell.append(" Lima"); break;
            case 6 : strBuffSpell.append(" Enam"); break;
            case 7 : strBuffSpell.append(" Tujuh"); break;
            case 8 : strBuffSpell.append(" Delapan"); break;
            case 9 : strBuffSpell.append(" Sembilan"); break;
            case 10: strBuffSpell.append(" Sepuluh"); break;
            case 11: strBuffSpell.append(" Sebelas"); break;
            case 100: strBuffSpell.append(" Seratus"); break;
            case 1000: strBuffSpell.append(" Seribu"); break;
        }
    }



    /**
     * Clear the spell buffer, to get to actual spalled string
     */
    private void reset() {
        strBuffSpell.delete(0, strBuffSpell.length());
    }



    private void spellNumToEng(int num) {
        int denum = 0;
        String str = new String();
        Integer numObj = new Integer(num);

        if(num > numObj.MAX_VALUE || num < 0) {
            strBuffSpell.append("ERROR::Invalid integer value to be spelled, value range: 0 - " + numObj.MAX_VALUE);
            return;
        }

        if(num < 20) {
            spellBasicNumToEng(num);
        } else {
            if(num < 100 && (num / 10 < 10)) {
                spellBasicNumToEng((num / 10) * 10);
                spellNumToEng(num % 10);
            } else {
                if(num <= 99) denum = 10;
                else if(num <= 999) denum = 100;
                else if(num <= 999999) denum = 1000;
                else if(num <= 999999999) denum = 1000000;
                else denum = 1000000000;

                    spellNumToEng(num / denum);
	                switch(denum)
                    {
	                    case 100 : strBuffSpell.append(" Hundred"); break;
	                    case 1000 : strBuffSpell.append(" Thousand"); break;
	                    case 1000000 : strBuffSpell.append(" Million"); break;
	                    case 1000000000 : strBuffSpell.append(" Billion"); break;
	                }
	                spellNumToEng(num % denum);
            }
        }

        strBuffSpell.toString().trim();
    }



    private void spellNumToIna(int num) {
        int denum = 0;
        Integer numObj = new Integer(num);

        if(num > numObj.MAX_VALUE || num < 0) {
            strBuffSpell.append("ERROR::Invalid integer value to be spelled, value range: 0 - " + numObj.MAX_VALUE);
            return;
        }

        if(num < 12 || num == 100 || num == 1000) {
            spellBasicNumToIna(num);
        } else {
            if(num < 20) {
                spellBasicNumToIna(num % 10);
                strBuffSpell.append(" Belas");
            } else {
                if(num <= 99) denum = 10;
                else if(num <= 999) denum = 100;
                else if(num <= 999999) denum = 1000;
                else if(num <= 999999999) denum = 1000000;
                else denum = 1000000000;

                if((num / denum == 1) && (denum == 100)) {
                    strBuffSpell.append(" Seratus");
                    spellNumToIna(num % denum);
                } else {
                    if((num / denum == 1) && (denum == 1000)) {
                        strBuffSpell.append(" Seribu");
                        spellNumToIna(num % denum);
                    } else {
                        spellNumToIna(num / denum);
                        switch(denum) {
                            case 10 : strBuffSpell.append(" Puluh"); break;
                            case 100 : strBuffSpell.append(" Ratus"); break;
                            case 1000 : strBuffSpell.append(" Ribu"); break;
                            case 1000000 : strBuffSpell.append(" Juta"); break;
                            case 1000000000 : strBuffSpell.append(" Milyar"); break;
                        }
                        spellNumToIna(num % denum);
                    }
                }
            }
        }

        strBuffSpell.toString().trim();
    }

    public String spellNumberToIna(int num) {
        this.reset();
        this.spellNumToIna(num);
        return this.strBuffSpell.toString();
    }

    public String spellNumberToEng(int num) {
        this.reset();
        this.spellNumToEng(num);
        return this.strBuffSpell.toString();
    }

    public String spellNumberToIna(double num) {

        String numStr = String.valueOf(num);
        System.out.println(numStr);
        StringTokenizer strToken = new StringTokenizer(numStr, ".");

		String firstStr = strToken.nextToken();
        System.out.println(firstStr);
        String lastStr = strToken.nextToken();
        System.out.println(lastStr);

        int firstInt = Integer.parseInt(firstStr);
        int lastInt = Integer.parseInt(lastStr);

        if(firstInt==0 && lastInt>0){
        	return "Nol Koma "+spellNumberToIna(lastInt);
        }

        if(firstInt==0 && lastInt==0){
            return "Nol";
        }

        if(firstInt>0 && lastInt==0){
            return spellNumberToIna(firstInt);
        }

        if(firstInt>0 && lastInt>0){
            return spellNumberToIna(firstInt)+" Koma "+spellNumberToIna(lastInt);
        }
        else{
            return "";
        }

    }

	public String spellNumberToEng(double num) {

        String numStr = String.valueOf(num);
        System.out.println(numStr);
        StringTokenizer strToken = new StringTokenizer(numStr, ".");

		String firstStr = strToken.nextToken();
        System.out.println(firstStr);
        String lastStr = strToken.nextToken();
        System.out.println(lastStr);

        int firstInt = Integer.parseInt(firstStr);
        int lastInt = Integer.parseInt(lastStr);

        if(firstInt==0 && lastInt>0){
        	return "Zero Point "+spellNumberToEng(lastInt);
        }

        if(firstInt==0 && lastInt==0){
            return "Zero";
        }

        if(firstInt>0 && lastInt==0){
            return spellNumberToEng(firstInt);
        }

        if(firstInt>0 && lastInt>0){
            return spellNumberToEng(firstInt)+" Point "+spellNumberToEng(lastInt);
        }
        else{
            return "";
        }

    }

    public String spellNumberToIna(float num) {

        String numStr = String.valueOf(num);
        System.out.println(numStr);
        StringTokenizer strToken = new StringTokenizer(numStr, ".");

		String firstStr = strToken.nextToken();
        System.out.println(firstStr);
        String lastStr = strToken.nextToken();
        System.out.println(lastStr);

        int firstInt = Integer.parseInt(firstStr);
        int lastInt = Integer.parseInt(lastStr);

        if(firstInt==0 && lastInt>0){
        	return "Nol Koma "+spellNumberToIna(lastInt);
        }

        if(firstInt==0 && lastInt==0){
            return "Nol";
        }

        if(firstInt>0 && lastInt==0){
            return spellNumberToIna(firstInt);
        }

        if(firstInt>0 && lastInt>0){
            return spellNumberToIna(firstInt)+" Koma "+spellNumberToIna(lastInt);
        }
        else{
            return "";
        }

    }

	public String spellNumberToEng(float num) {

        String numStr = String.valueOf(num);
        System.out.println(numStr);
        StringTokenizer strToken = new StringTokenizer(numStr, ".");

		String firstStr = strToken.nextToken();
        System.out.println(firstStr);
        String lastStr = strToken.nextToken();
        System.out.println(lastStr);

        int firstInt = Integer.parseInt(firstStr);
        int lastInt = Integer.parseInt(lastStr);

        if(firstInt==0 && lastInt>0){
        	return "Zero Point "+spellNumberToEng(lastInt);
        }

        if(firstInt==0 && lastInt==0){
            return "Zero";
        }

        if(firstInt>0 && lastInt==0){
            return spellNumberToEng(firstInt);
        }

        if(firstInt>0 && lastInt>0){
            return spellNumberToEng(firstInt)+" Point "+spellNumberToEng(lastInt);
        }
        else{
            return "";
        }

    }

} // end of NumberSpeller