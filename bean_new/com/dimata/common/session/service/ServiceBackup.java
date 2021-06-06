/*
 * ServiceBackup.java
 *
 * Created on October 9, 2004, 8:21 AM
 */

package com.dimata.common.session.service;

import java.util.Date;
import com.dimata.common.entity.service.*; 

/**
 *  
 * @author  gedhy
 */
public class ServiceBackup implements Runnable {
    
    static String stCommandScript = "";         
    
    public ServiceBackup(String stCommand)
    {
        this.stCommandScript = stCommand;  
    }
    
    BackupDatabaseProcess objBackupDatabaseProcess = new BackupDatabaseProcess(stCommandScript);        
    
    /**
     * @created by Edhy
     */
    public synchronized void run()   
    {        
        System.out.println(".................... ServiceBackUpdatabase started ....................");                
        
        boolean firstProcess = true;        
        while (objBackupDatabaseProcess.running)      
        {
            try 
            {                
                
                if(firstProcess)  
                {
                    // sleeping time for first process                    
                    ServiceConfiguration svcConf = PstServiceConfiguration.getSvcConfigurationByType(PstServiceConfiguration.SERVICE_TYPE_BACKUPDB);
                    int sleepTime = getSleepTime(new Date(), svcConf.getStartTime());                                       
                    System.out.println(".:: First process start running back up database services, thread now sleep/pause for "+(sleepTime / (60 * 1000))+" minutes ("+sleepTime+" ms)");
                    Thread.sleep(sleepTime);                                                               
                    
                    firstProcess = false;                                       
                }                
                
                
                else
                {                    
                    // memproses backup database                        
                    objBackupDatabaseProcess.runCommandScript(stCommandScript);
                    ServiceConfiguration svcConf = PstServiceConfiguration.getSvcConfigurationByType(PstServiceConfiguration.SERVICE_TYPE_BACKUPDB);                        
                        
                    // convert periode (in minutes) to miliseconds (multiply by 60 * 1000)
                    int sleepTime = svcConf.getPeriode() * 60 * 1000;                        
                    System.out.println(".:: proses cek absence finished, thread now sleep/pause for "+svcConf.getPeriode()+" minutes ("+sleepTime+" ms)");
                    Thread.sleep(sleepTime);                                                                
                }             
                
            }
            catch (Exception e) 
            {                
                System.out.println("Exc ServiceBackup : " + e.toString());
            }
        }
    }

    
    /**
     * @param start
     * @param end
     * @return
     */    
    public int getSleepTime(Date start, Date end)
    {
        Date s = new Date();
        Date e = new Date();
        
        s.setHours(start.getHours());
        s.setMinutes(start.getMinutes());
        s.setSeconds(start.getSeconds());
        
        e.setHours(end.getHours());
        e.setMinutes(end.getMinutes());
        e.setSeconds(end.getSeconds());
        
        if(end.getHours() < start.getHours())
        {
            int dtEnd = e.getDate();
            e.setDate(dtEnd+1);
        }        
        
        long st = s.getTime();
        long en = e.getTime();
        long rs = en - st;
        if(rs < 0)
        {
            rs = 0;
        }
        
        return (new Long(rs)).intValue();
    }            
    
}
