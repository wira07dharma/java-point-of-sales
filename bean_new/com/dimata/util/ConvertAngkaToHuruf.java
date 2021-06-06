/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.dtaxintegration.session;

/**
 *
 * @author dimata005
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dimata005
 */
public class ConvertAngkaToHuruf {
    long amount = 0;
    public ConvertAngkaToHuruf(long amount) {
        this.amount = amount;
    }

    public void setAmount(long amount){
        this.amount = amount;
    }

    /*static void percobaan(){
        convertAngkaToHuruf t = new convertAngkaToHuruf(1000898977800999L);
        System.out.println("1.000.898.977.800.999 adalah "+t.getText());
    }

    public static void main (String[] args){
        percobaan();
    }*/

    public String getText(){
        String s = ""+amount;
        String str = "";

        // Kasus di bawah 1000
        if (s.length() < 4) return convert3Digit(Integer.parseInt(s));

        // Kasus amount == 1000 s/d 9999
        if (s.length() == 4) return convert4Digit(Integer.parseInt(s));

        // Kasus 10000 s/d 999999
        if (s.length() >= 5 && s.length() <= 6) return convert5_6Digit(amount);

        // Kasus 1.000.000 s/d 999.999.999
        if (s.length() >= 7 && s.length() <= 9) return convert7_9Digit(amount);

        // Kasus 1.000.000.000 sd 999.999.999.999
        if (s.length() >= 10 && s.length() <= 12) return convert10_12Digit(amount);

        // Kasus 1.000.000.000.000 s/d 999.999.999.999.999
        if (s.length() >= 13 && s.length() <= 15) return convert13_15Digit(amount);

        // Kasus 1.000.000.000.000.000 s/d 999.999.999.999.999.999
        if (s.length() >= 16 && s.length() <= 18) return convert16_18Digit(amount);
        return str;
    }

    public String toString(){
        String str = getText();
        return str;
    }

    private String convert1Digit(int n){
        switch (n){
            case 0 : return "nol ";
            case 1 : return "satu ";
            case 2 : return "dua ";
            case 3 : return "tiga ";
            case 4 : return "empat ";
            case 5 : return "lima ";
            case 6 : return "enam ";
            case 7 : return "tujuh ";
            case 8 : return "delapan ";
            case 9 : return "sembilan ";
            default : return "";
        }
    }

    private String convert10_19(int n) {
        switch (n) {
            case 10 : return "sepuluh ";
            case 11 : return "sebelas ";
            case 12 : return "dua belas ";
            case 13 : return "tiga belas ";
            case 14 : return "empat belas ";
            case 15 : return "lima belas ";
            case 16 : return "enam blas ";
            case 17 : return "tujuh belas ";
            case 18 : return "delapan belas ";
            case 19 : return "sembilan belas ";
            default : return "";
        }
    }

    private String convert2Digit (int n){
        if (n==0) return "";
        if (n<10) return convert1Digit(n);
        if (n>=10 && n <= 19) return convert10_19(n);

        int div10 = n / 10;
        int mod10 = n % 10;

        String str="";
        str += convert1Digit(div10) + "puluh ";
        if (mod10 > 0) return (str + convert1Digit(mod10));
        return str;
    }

    private String convert3Digit(int n){
        if (n == 0) return "";

        String str = "";
        int div100 = n / 100;
        int mod100 = n % 100;

        switch (div100) {
            case 1 : str += "seratus "; break;
            case 2 :
            case 3 :
            case 4 :
            case 5 :
            case 6 :
            case 7 :
            case 8 :
            case 9 :
                str += convert1Digit(div100) + "ratus ";break;
            default : str += "";
        }
        if (mod100 > 0) return (str + convert2Digit(mod100));
        return str;
    }

    private String convert4Digit(int n){
        if (n == 0) return "";

        String str = "";
        int div1000 = n / 1000;
        int mod1000 = n % 1000;

        if (div1000 == 1) str += "seribu ";
        else str += convert1Digit(div1000) + "ribu ";

        if (mod1000 > 0) return (str + convert3Digit(mod1000));
        return str;
    }

    private String convert5_6Digit(long n){
       if (n == 0) return "";

       int div1000 = (int) (n / 1000);
       int mod1000 = (int) (n % 1000);

       if (div1000==0) return convert3Digit(mod1000);
       if (mod1000 > 0) return (convert3Digit(div1000)+"ribu "+convert3Digit(mod1000));
       return (convert3Digit(div1000)+"ribu ");
    }

    private String convert7_9Digit(long n){
        if (n == 0) return "";

        int div1Juta = (int) (n / 1000000L);
        long mod1Juta = n % 1000000L;

        if (div1Juta==0) return convert5_6Digit(mod1Juta);
        if (mod1Juta >0) return (convert3Digit(div1Juta)+"juta "+convert5_6Digit(mod1Juta));
        return (convert3Digit(div1Juta)+"juta ");
    }

    private String convert10_12Digit(long n){
        if (n == 0) return "";

        int div1Mil = (int) (n / 1000000000L);
        long mod1Mil = n % 1000000000L;

        if (div1Mil == 0) return convert7_9Digit(mod1Mil);
        if (mod1Mil >0) return (convert3Digit(div1Mil)+"milyar "+convert7_9Digit(mod1Mil));
        return (convert3Digit(div1Mil)+"milyar ");
    }

    private String convert13_15Digit(long n){
        if (n == 0) return "";

        int div1Tri = (int) (n / 1000000000000L);
        long mod1Tri = n % 1000000000000L;

        if (div1Tri ==0) return convert10_12Digit(mod1Tri);
        if (mod1Tri >0) return (convert3Digit(div1Tri)+"trilyun "+convert10_12Digit(mod1Tri));
        return (convert3Digit(div1Tri)+"trilyun ");
    }

    private String convert16_18Digit(long n){
        if (n == 0) return "";

        int div1Qua = (int) (n / 1000000000000000L);
        long mod1Qua = n % 1000000000000000L;

        if (div1Qua == 0) return convert13_15Digit(mod1Qua);
        if (mod1Qua > 0) return (convert3Digit(div1Qua)+"quadrilyun "+convert13_15Digit(mod1Qua));
        return (convert3Digit(div1Qua)+"quadrilyun ");
    }
}