/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.emaslantakan;

/**
 *
 * @author Dimata 007
 */
public class EmasLantakanMonitor implements Runnable {

    public EmasLantakanMonitor() {

    }

    @Override
    public void run() {
        System.out.println("Update data started... ");
        
        int i = 0;
        while (EmasLantakanManager.running) {
            try {
                i++;
                System.out.println(i);
                //Thread.sleep((long) (0.1 * 60000));
                EmasLantakanManager.running = false;
            } catch (Exception e) {
                System.out.println("Interrupted : " + e);
            }
        }
        
        System.out.println("Update data stopped... ");
    }

}
