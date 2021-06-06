/*
 * BackupDatabaseProcess.java
 *
 * Created on October 9, 2004, 8:21 AM
 */

package com.dimata.common.session.service;

// package core java
import java.util.Date;

// package qdep
import com.dimata.util.*;

/**
 *
 * @author  gedhy
 */
public class BackupDatabaseProcess {    
    
    static String stCommandScript = ""; // command script yang akan dijalankan
    static boolean running = false; // status service
    
    public BackupDatabaseProcess() {  
    }        

    public BackupDatabaseProcess(String stCommand) {  
        this.stCommandScript = stCommand;
    }        
    
    public boolean getStatus()
    {
        return running;
    }    
    
    public synchronized void startService() 
    {
        if (!running) 
        {
            System.out.println(".:: Backup database service started ... !!!");     
            try 
            {
                running = true;
                Thread thr = new Thread(new ServiceBackup(stCommandScript));
                thr.setDaemon(false);
                thr.start();

            }
            catch (Exception e) 
            {
                System.out.println(">>> Exc when Backup database start ... !!!");
            }
        }
    }

    public synchronized void stopService() 
    {
        running = false;
        System.out.println(".:: Backup database service stoped ... !!!");
    }
    
    
    /**
     * @param stCommandScript
     * @return
     * @created by Edhy
     */        
    public static int runCommandScript(String stCommandScript)  
    {        
        ExecCommand execCommand = new ExecCommand();  
        return execCommand.runCommmand(stCommandScript);
    }        
    
}
