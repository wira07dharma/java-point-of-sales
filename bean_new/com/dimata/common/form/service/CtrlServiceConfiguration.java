/*
 * CtrlServiceConfiguration.java
 *
 * Created on September 27, 2004, 9:35 PM
 */

package com.dimata.common.form.service;

/* java package */
import java.util.*;
import javax.servlet.http.*;

/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.gui.jsp.*;

/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.posbo.db.DBException;



import com.dimata.common.entity.service.*;

/**
 *
 * @author  gedhy
 */
public class CtrlServiceConfiguration  extends Control implements I_Language {
    
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "Konfigurasi tipe ini sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "This configuration type exist", "Data incomplete"}
    };
    
    private int start;
    private String msgString;
    private ServiceConfiguration serviceConfiguration;
    private PstServiceConfiguration pstServiceConfiguration;
    private FrmServiceConfiguration frmServiceConfiguration;
    int language = LANGUAGE_DEFAULT;
    
    public CtrlServiceConfiguration(HttpServletRequest request){
        msgString = "";
        serviceConfiguration = new ServiceConfiguration();
        try{
            pstServiceConfiguration = new PstServiceConfiguration(0);
        }catch(Exception e){;}
        frmServiceConfiguration = new FrmServiceConfiguration(request, serviceConfiguration);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmServiceConfiguration.addError(frmServiceConfiguration.FRM_FIELD_SERVICE_TYPE, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public ServiceConfiguration getServiceConfiguration() { return serviceConfiguration; }
    
    public FrmServiceConfiguration getForm() { return frmServiceConfiguration; }
    
    public String getMessage(){ return msgString; }
    
    public int getStart() { return start; }
    
    public int action(int cmd , long oidServiceConfiguration, HttpServletRequest request){
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd){
            case Command.ADD :
                break;
                
            case Command.SAVE :
                if(oidServiceConfiguration != 0){
                    try{
                        serviceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);
                    }catch(Exception exc){
                    }
                }
                
                frmServiceConfiguration.requestEntityObject(serviceConfiguration);
                
                Date startTime = ControlDate.getTime(FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_START_TIME], request);
                
                if(frmServiceConfiguration.errorSize()>0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                serviceConfiguration.setStartTime(startTime);
                
                if(serviceConfiguration.getOID()==0){
                    try{
                        long oid = pstServiceConfiguration.insertExc(this.serviceConfiguration);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }else{
                    try {
                        long oid = pstServiceConfiguration.updateExc(this.serviceConfiguration);
                    }catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                    
                }
                break;
                
            case Command.EDIT :
                if (oidServiceConfiguration != 0) {
                    try {
                        serviceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidServiceConfiguration != 0) {
                    try {
                        serviceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidServiceConfiguration != 0){
                    try{
                        long oid = PstServiceConfiguration.deleteExc(oidServiceConfiguration);
                        if(oid!=0){
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        }else{
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }catch(Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            default :
                if (oidServiceConfiguration != 0) {
                    try {
                        serviceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);
                    } catch (Exception exc){
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
        }
        return rsCode;
    }
    
    
    /**
     * @param svcCount
     * @param arrCmd
     * @param arrOidServiceConfiguration
     * @param strSvcType
     * @param strTimeHour
     * @param strTimeMinutes
     * @param strInterval
     * @return
     */
    public Vector action(int svcCount, int[] arrCommand, long[] arrOidServiceConfiguration, String[] strSvcType, String[] strTimeHour, String[] strTimeMinutes, String[] strInterval) {
        Vector vectServiceConf = new Vector(1,1);
        for(int i=0; i<svcCount; i++) 
        {
            int iCommand = arrCommand[i];
            long oidServiceConfiguration = arrOidServiceConfiguration[i];                    
            switch(iCommand) 
            {
                case Command.SAVE :
//                    System.out.println("Process Save : " + i);
                    ServiceConfiguration svcConf = new ServiceConfiguration();
                    if(oidServiceConfiguration != 0) 
                    {                        
                        try 
                        {
                            serviceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);
                            svcConf = PstServiceConfiguration.fetchExc(oidServiceConfiguration);                            
                        }
                        catch(Exception exc) 
                        {
                            System.out.println("Exc fetch PstServiceConfiguration");
                        }
                    }

                    Date dtTemp = new Date();
                    Date startTime = new Date(dtTemp.getYear(), dtTemp.getMonth(), dtTemp.getDate(), Integer.parseInt(strTimeHour[i]), Integer.parseInt(strTimeMinutes[i]));

                    serviceConfiguration.setServiceType(Integer.parseInt(strSvcType[i]));
                    serviceConfiguration.setPeriode(Integer.parseInt(strInterval[i]));
                    serviceConfiguration.setStartTime(startTime);


                    // insert service conf
                    if(svcConf.getOID()==0) 
                    {
                        try 
                        {
                            long oid = pstServiceConfiguration.insertExc(serviceConfiguration);
                            vectServiceConf.add(getServiceConfiguration());                            
                        }
                        catch (Exception exc) 
                        {
                            System.out.println("Exc when insert serviceConf : " + exc.toString());
                        }
                    }


                    // update service conf
                    else 
                    {
                        try 
                        {
                            long oid = pstServiceConfiguration.updateExc(serviceConfiguration);
                            vectServiceConf.add(serviceConfiguration);                            
                        }
                        catch (Exception exc) 
                        {                                
                            System.out.println("Exc when update serviceConf : " + exc.toString());
                        }
                    }                    
                    break;

                    
                default :      
//                    System.out.println("Process default : " + i);
                    try 
                    {
                        String whereClause = PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_TYPE] + "=" + i;
                        Vector vectTemp = PstServiceConfiguration.list(0,0, whereClause, "");
                        ServiceConfiguration objServiceConfigurationDefault = new ServiceConfiguration();
                        if(vectTemp!=null && vectTemp.size()>0)
                        {
                            objServiceConfigurationDefault = (ServiceConfiguration)vectTemp.get(0);
                        }
                        vectServiceConf.add(objServiceConfigurationDefault);
                    }
                    catch(Exception exc) 
                    {                            
                        System.out.println("Exc fetch PstServiceConfiguration");
                    }
                    break;
            }
        }
        return vectServiceConf;          
    }
    

    
    /**
     * @param iCommand
     * @param oidServiceConfiguration
     * @param svcType
     * @param timeHour
     * @param timeMinutes
     * @param timeInterval
     * @return
     */
    public ServiceConfiguration action(int iCommand, long oidServiceConfiguration, int svcType, int timeHour, int timeMinutes, int timeInterval) 
    {
        ServiceConfiguration objServiceConfiguration = new ServiceConfiguration();
        if(iCommand == Command.SAVE)
        {                
            if(oidServiceConfiguration != 0) 
            {                        
                try   
                {
                    objServiceConfiguration = PstServiceConfiguration.fetchExc(oidServiceConfiguration);                        
                }
                catch(Exception exc) 
                {
                    System.out.println("Exc fetch PstServiceConfiguration");
                }
            }

            
            Date dtTemp = new Date();
            Date startTime = new Date(dtTemp.getYear(), dtTemp.getMonth(), dtTemp.getDate(), timeHour, timeMinutes);
            objServiceConfiguration.setServiceType(svcType);
            objServiceConfiguration.setPeriode(timeInterval);
            objServiceConfiguration.setStartTime(startTime);

            // insert service conf
            if(objServiceConfiguration.getOID()==0) 
            {
                try 
                {
                    long oid = pstServiceConfiguration.insertExc(objServiceConfiguration);                        
                }
                catch (Exception exc) 
                {
                    System.out.println("Exc when insert serviceConf : " + exc.toString());
                }
            }   


            // update service conf
            else 
            {
                try 
                {
                    long oid = pstServiceConfiguration.updateExc(objServiceConfiguration);                        
                }
                catch (Exception exc) 
                {                                
                    System.out.println("Exc when update serviceConf : " + exc.toString());
                }
            }                                
        }
        
        else
        {
            try 
            {
                String whereClause = PstServiceConfiguration.fieldNames[PstServiceConfiguration.FLD_SERVICE_TYPE] + "=" + svcType;
                Vector vectTemp = PstServiceConfiguration.list(0,0, whereClause, "");                    
                if(vectTemp!=null && vectTemp.size()>0)
                {
                    objServiceConfiguration = (ServiceConfiguration)vectTemp.get(0);
                }                    
            }
            catch(Exception exc) 
            {                            
                System.out.println("Exc fetch PstServiceConfiguration");
            }            
        }
        
        return objServiceConfiguration;          
    }

}
