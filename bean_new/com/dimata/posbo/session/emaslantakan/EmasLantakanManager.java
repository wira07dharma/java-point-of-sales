/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.emaslantakan;

import com.dimata.util.Formater;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class EmasLantakanManager {

    public static boolean running = false;
    public static String note = "";
    public static EmasLantakanManager lck = new EmasLantakanManager();

    public EmasLantakanManager() {

    }

    public void startMonitor() {
        if (running) {
            return;
        }
        Thread thLocker = new Thread(new EmasLantakanMonitor());
        thLocker.setDaemon(false);
        running = true;
        thLocker.start();
    }

    public void stopMonitor() {
        running = false;
        Date newDate = new Date();
        System.out.println("Monitoring stopped at : " + Formater.formatDate(newDate, "dd-MM-yyyy kk:mm"));
    }

    public boolean getStatus() {
        return running;
    }

    public String getNote() {
        return note;
    }
}
