/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.license;

import com.dimata.posbo.entity.masterdata.PstCompany;
import com.dimata.util.Formater;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author dimata005
 */
public class LicenseProduct {

    public static final String LICENSE_KEY = "great";
    private static Hashtable licenseproduct = null;

    public static String btEnkripActionPerformed(String txtPlainText) {
        String key = LICENSE_KEY;
        String isitext = (key.length()) + key + txtPlainText;
        String out = "";
        for (int i = 0; i < isitext.length(); i++) {
            int index = isitext.charAt(i);
            char s = (char) (index + 1);
            out = out + String.valueOf(s);
        }
        return out;
    }

    public static String btDekripActionPerformed(String txtChiperText, String xx) {
        String isitext = txtChiperText;
        char k = (char) (isitext.charAt(0));
        String key = "", out = "";
        int jumKey = Integer.parseInt(String.valueOf(k)) - 1;
        for (int i = 1; i <= jumKey; i++) {
            int index = isitext.charAt(i);
            char s = (char) (index - 1);
            key = key + String.valueOf(s);
        }
        if (key.equals(LICENSE_KEY)) { // hasil true  
            for (int i = (jumKey + 1); i < isitext.length(); i++) {
                int index = isitext.charAt(i);
                char s = (char) (index - 1);
                out = out + String.valueOf(s);
            }
        } else {
            out = "Key tidak cocok";
        }
        return out;
    }

    public static boolean btDekripActionPerformed(String licenseKey) {
        boolean isValidKey = false;
        String txtChiperText = "";
        try{
            txtChiperText = licenseKey.trim();
        }catch(Exception ex){
            isValidKey = false;
        }
        
        String key = "", out = "";
        try {
            String isitext = txtChiperText;
            char k = (char) (isitext.charAt(0));
            int jumKey = Integer.parseInt(String.valueOf(k)) - 1;
            for (int i = 1; i <= jumKey; i++) {
                int index = isitext.charAt(i);
                char s = (char) (index - 1);
                key = key + String.valueOf(s);
            }
            if (key.equals(LICENSE_KEY)) { // hasil true  
                for (int i = (jumKey + 1); i < isitext.length(); i++) {
                    int index = isitext.charAt(i);
                    char s = (char) (index - 1);
                    out = out + String.valueOf(s);
                }
            } else {
                out = "false";
            }
        } catch (Exception e) {
            out = "false";
        }

        if (!out.equals("false")) {
            String string = out;
            String[] parts = string.split("\\|");
            try {
                // String generateKey = ""+
                //license.getCustomerId()+"|"+//1
                //license.getProductId()+"|"+//2
                //license.getVersionSystem()+"|"+//3
                //Formater.formatDate(license.getDateOperasi(),"yyyy-MM-dd")+"|"+//4
                //license.getPeriode()+"|"+//5
                //license.getValidDay()+"";//6
                String customerId = parts[0]; // 004
                String productId = parts[1]; // 034556
                String versionSystemId = parts[2]; // 034556
                String operasiDate = parts[3]; // 034556
                String periode = parts[4]; // 034556
                String validDays = parts[5]; // 034556
                if (licenseproduct == null) {
                    licenseproduct = new Hashtable();
                }

                License license = new License();
                license.setCustomerId(Long.parseLong(customerId));
                license.setProductId(Long.parseLong(productId));
                license.setVersionSystem(Integer.parseInt(versionSystemId));

                Date datexx = new Date();
                Date righNow = new Date();
                String srighNow = Formater.formatDate(righNow, "yyyy-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    datexx = formatter.parse(operasiDate);
                    righNow = formatter.parse(srighNow);
                    license.setDateOperasi(datexx);
                } catch (Exception e) {
                    return isValidKey = false;
                }
                license.setPeriode(Integer.parseInt(periode));
                license.setValidDay(Integer.parseInt(validDays));
                licenseproduct.put("license_key", license);

                //pemgecekan waktu validasi, apakah masih bisa atau tidak
                int validDaysAvaiblabe = 0;
                
                Date endRangeDate = (Date) license.getDateOperasi().clone();
                switch (license.getPeriode()) {
                    case 0: //month
                        validDaysAvaiblabe = 30;
                        endRangeDate.setDate(license.getDateOperasi().getDate() + validDaysAvaiblabe);
                        if(righNow.after(license.getDateOperasi()) && righNow.before(endRangeDate)) {
                            isValidKey = true;
                        } else {
                            isValidKey = false;
                        }
                        break;
                    case 1://year
                        validDaysAvaiblabe = 365;
                        endRangeDate.setDate(license.getDateOperasi().getDate() + validDaysAvaiblabe);
                        if(righNow.after(license.getDateOperasi()) && righNow.before(endRangeDate)) {
                            isValidKey = true;
                        } else {
                            isValidKey = false;
                        }
                        break;
                    default://day
                        validDaysAvaiblabe = license.getValidDay();
                        endRangeDate.setDate(license.getDateOperasi().getDate() + validDaysAvaiblabe);
                        if((righNow.after(license.getDateOperasi())|| righNow.equals(license.getDateOperasi())) && (righNow.before(endRangeDate) || righNow.equals(endRangeDate))) {
                            isValidKey = true;
                        } else {
                            isValidKey = false;
                        }
                        break;
                }
                long update = PstCompany.updateLicenseKey(licenseKey);
            } catch (Exception ex) {
                return isValidKey = false;
            }

        } else {
            isValidKey = false;
        }
        return isValidKey;
    }
    
    public static void main(String args[])
    {
        String xxx = btEnkripActionPerformed("tegalsari|tegalsaripass");
        System.out.println(xxx);
        String xxDec =  btDekripActionPerformed(xxx,"");
        System.out.println("dec "+xxDec);
        String[] parts = xxDec.split("\\|");
        String user = parts[0];
        String pass = parts[1];
        System.out.println("user : "+user+" pass : "+pass);
    }
    
}
