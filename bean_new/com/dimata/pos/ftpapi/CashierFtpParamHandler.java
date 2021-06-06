/*
 * CashierFtpParamHandler.java
 *
 * Created on March 8, 2006, 4:37 PM
 */

package com.dimata.pos.ftpapi;

import com.dimata.common.entity.system.*;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  pulantara
 */
public class CashierFtpParamHandler {
    
    /** Creates a new instance of CashierFtpParamHandler */
    public CashierFtpParamHandler() {
    }
    
    private static boolean isExistConfig(){
        boolean result = false;
        try{
            result = !PstSystemProperty.getValueByName(CashierFtpConstant.fieldNames[CashierFtpConstant.REMOTE_HOST]).toUpperCase().equals("NOT INITIALIZED");
        }
        catch(Exception e){
            System.out.println("err on isExistConfig() = "+e.toString());
        }
        return result;
    }
    
    public static Hashtable getAllParam(){
        Hashtable result = new Hashtable();
        try{
            String where = "";
            for(int i=0; i<CashierFtpConstant.fieldNames.length; i++){
                where = where + PstSystemProperty.fieldNames[PstSystemProperty.FLD_NAME]+"='"+CashierFtpConstant.fieldNames[i] + "' OR ";
            }
            if(where.length()>0){
                where = where.substring(0,where.length()-3);
            } 
            Vector temp = PstSystemProperty.list(0,0,where,"");
            for(int i=0; i<temp.size(); i++){
                SystemProperty sp = new SystemProperty();
                sp = (SystemProperty) temp.get(i);
                result.put(sp.getName(),sp);
            }
        }
        catch(Exception e){
            System.out.println("err on getAllParam() = "+e.toString());
        }
        return result;
    }
    
    public static Hashtable getAllConfig(){
        Hashtable result = new Hashtable();
        try{
            String where = "";
            for(int i=0; i<CashierFtpConstant.fieldNames.length; i++){
                where = where + PstSystemProperty.fieldNames[PstSystemProperty.FLD_NAME]+"='"+CashierFtpConstant.fieldNames[i] + "' OR ";
            }
            if(where.length()>0){
                where = where.substring(0,where.length()-3);
            } 
            Vector temp = PstSystemProperty.list(0,0,where,"");
            for(int i=0; i<temp.size(); i++){
                SystemProperty sp = new SystemProperty();
                sp = (SystemProperty) temp.get(i);
                result.put(sp.getName(),sp.getValue());
            }
        }
        catch(Exception e){
            System.out.println("err on getAllParam() = "+e.toString());
        }
        return result;
    }
    
    public static boolean saveParam(String paramName, Hashtable param){
        boolean result = false;
        try{
            if(isExistConfig()){
                Hashtable oldVal = getAllParam();
                for(int i=0; i<CashierFtpConstant.fieldNames.length; i++){
                    SystemProperty sp = (SystemProperty) oldVal.get(CashierFtpConstant.fieldNames[i]);
                    sp.setValue((String) param.get(CashierFtpConstant.fieldNames[i]));
                    try{
                        PstSystemProperty.update(sp);
                    }
                    catch(Exception e){
                        System.out.println("err on update config: "+e.toString()); 
                    }
                }
            }
            else{
                for(int i=0; i<CashierFtpConstant.fieldNames.length; i++){
                    SystemProperty sp = new SystemProperty();
                    sp.setGroup(paramName);
                    sp.setValueType("STRING");
                    sp.setName(CashierFtpConstant.fieldNames[i]);
                    sp.setValue((String) param.get(CashierFtpConstant.fieldNames[i]));
                    try{
                        PstSystemProperty.insert(sp);
                    }
                    catch(Exception e){
                        System.out.println("err on insert config: "+e.toString());
                    }
                }
            }
            result = true;
        }
        catch(Exception e){
            System.out.println("err on getAllParam() = "+e.toString());
        }
        return result;
    }
    
    
}
