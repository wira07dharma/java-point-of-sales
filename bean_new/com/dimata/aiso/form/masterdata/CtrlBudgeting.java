/*
 * CtrlBudgeting.java
 * @author  rusdianta
 * Created on March 14, 2005, 9:42 AM
 */

package com.dimata.aiso.form.masterdata;

import java.util.*;
import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;

import com.dimata.aiso.entity.masterdata.*;

public class CtrlBudgeting extends Control implements I_Language {
    
    public static final int RSLT_UNKNOWN_ERROR = 0;
    
    public static String resultText[][] = {
        {"Ada kesalahan ..."},
        {"There are unknown error ..."}
    };
    
    private int language = LANGUAGE_DEFAULT;
    private String msgString;
    private AisoBudgeting aisoBudgeting;
    private PstAisoBudgeting pstAisoBudgeting;
    private FrmBudgeting frmBudgeting;
    
    /** Creates a new instance of CtrlBudgeting */
    public CtrlBudgeting() {
    }
    
    public CtrlBudgeting(HttpServletRequest request) {
        msgString = "";
        aisoBudgeting = new AisoBudgeting();
        try {
            pstAisoBudgeting = new PstAisoBudgeting(0);
        } catch (Exception error) {
            System.out.println(".:: CtrlBudgeting >> CtrlBudgeting() : " + error.toString());
        }
        frmBudgeting = new FrmBudgeting(request, aisoBudgeting);
    }
    
    public int getLanguage() {
        return language;
    }
    
    public void setLanguage(int language) {
        this.language = language;
    }
    
    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
        default : return resultText[language][RSLT_UNKNOWN_ERROR];   
        }
    }
    
    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
        default : return RSLT_UNKNOWN_ERROR; 
        }
    }
    
    public AisoBudgeting getAisoBudgeting() {
        return aisoBudgeting;
    }
    
    public FrmBudgeting getForm() {
        return frmBudgeting;
    }
    
    public String getMessage() {
        return msgString;
    }
    
    public static int action(int iCommand,
                             Vector vBudgeting)
    {
        switch (iCommand) {
        case Command.SAVE : 
            try {
                int iVectBudgetSize = vBudgeting.size();
                for (int item = 0; item < iVectBudgetSize; item++) {
                    AisoBudgeting objBudgeting = (AisoBudgeting) vBudgeting.get(item);
                    long lAccOid = objBudgeting.getIdPerkiraan();
                    long lPeriodOid = objBudgeting.getPeriodeId();
                    AisoBudgeting objBudgetDtbs = PstAisoBudgeting.getAisoBudgeting(lAccOid, lPeriodOid);
                    
                    long budgetingOid = objBudgetDtbs.getBudgetingOid();
                    if (budgetingOid > 0) {
                        objBudgeting.setOID(budgetingOid);
                        PstAisoBudgeting.updateExc(objBudgeting);
                    } else
                        PstAisoBudgeting.insertExc(objBudgeting);
                }
            } catch (Exception error) {
                System.out.println(".:: " + new CtrlBudgeting().getClass().getName() + ".action() : " + error.toString());
            }
        break;
        default : ;
        }
        return 0;
    }
    
    /*public static double getDecimalFromFormattedString(String sValue,
                                                       String sDigitGroup,
                                                       String sDecimalSymbol)
    {
        try {
            StringTokenizer strToken = new StringTokenizer(sValue, sDigitGroup);
            String strValue = "";
            while (strToken.hasMoreTokens()) {
                strValue = strValue + strToken.nextToken();
            }
            strValue = strValue.replace(USER_DECIMAL_SYMBOL.charAt(0),SYSTEM_DECIMAL_SYMBOL.charAt(0) );
            
            return strValue;
            
        }catch(Exception exc){
            System.out.println("error deFormatStringDecimal"+exc);
            return "";
        }
    }*/
    
    public static double getDecimalFromFormattedString(String sValue,
                                                       String sDigitGroup,
                                                       String sDecimalSymbol)
    {
        int iLength = sValue.length();
        String sInt = "";
        String sDecimal = "";        
        int iSecIndex = iLength;
        
        for (int item = 0; item < iLength; item++) {
            char ch = sValue.charAt(item);
            if (ch >= '0' && ch <= '9')
                sInt += ch;
            else if (ch == sDigitGroup.charAt(0))
                continue;
            else if (ch == sDecimalSymbol.charAt(0)) {
                iSecIndex = item + 1;
                break;
            } else
                return 0;
        }
        
        for (int item = iSecIndex; item < iLength; item++) {
            char ch = sValue.charAt(item);
            if (ch >= '0' && ch <= '9')
                sDecimal += ch;
            else 
                return 0;
        }
        
        double decimal = 0;
        
        try {
            decimal = Double.parseDouble(sInt);
        } catch (Exception error) {
            System.out.println(".:: " + new CtrlBudgeting().getClass().getName() + ".getDecimalFromFormattedString() : " + error.toString());
        }
        
        double dLastDecValue = 0;
        
        try {
            iLength = sDecimal.length();
            if (iLength > 0)
             dLastDecValue = Double.parseDouble(sDecimal) / Math.pow(10, iLength);
        } catch (Exception error) {
            System.out.println(".:: " + new CtrlBudgeting().getClass().getName() + ".getDecimalFromFormattedString() : " + error.toString());
        }
        
        return decimal + dLastDecValue;
    }
    
    /*public static void main(String args[]) {
        double a = getDecimalFromFormattedString("837.938", ".", ",");
        System.out.println("Nilai double dari a = " + a);
    }*/
}
