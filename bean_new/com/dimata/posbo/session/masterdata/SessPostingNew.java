/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.masterdata;
import java.util.*;
import com.dimata.common.entity.location.*;
/**
 *
 * @author Dimata
 */
public class SessPostingNew {
    private static Hashtable thrTx = new Hashtable();

       /**
     *
     * @param cashMasterId
     * @return status dari thread transfer
     */
    public static String getStatus(long oidLocation){
        try{
            SessPostingNewThread thrdPosting = (SessPostingNewThread)thrTx.get(""+oidLocation);
            if(thrdPosting==null){
                return "";
            }
            if(thrdPosting.isPauseTransfer())
                return "Paused";
            if(!thrdPosting.isPauseTransfer() && thrdPosting.isRunThread())
                return "Running";
            return  "Stopped";
        }catch(Exception ex){
            System.out.println(ex);
            return "";
        }


    }

    public static void startTransfer(Vector locationPosting){
        if(locationPosting==null || locationPosting.size()<1) {
            return ;
        }
        try{
                        SessPostingNewThread thrdPostingPrev = null;
                        for(int i=0;i<locationPosting.size(); i++){
                            long oidLocation= Long.parseLong(""+locationPosting.get(i));

                            Location loc = PstLocation.fetchExc(oidLocation);
                            long locationId = loc.getOID();
                        
                            if( getStatus(locationId)=="Paused"  || getStatus(locationId)=="Running" )  {
                                SessPostingNewThread thrdPosting = (SessPostingNewThread)thrTx.get(""+locationId);
                                thrdPosting.setPreviousThread(thrdPostingPrev);
                                thrdPostingPrev = thrdPosting;
                                //thrdPosting.setRunThread(false);
                                thrdPosting.setRunThread(true);
                                Thread thr = new Thread(thrdPosting);
                                //Thread thr = new Thread(new TransferDataToOutletThread(outletConn));
                                thrdPosting.setPostingProgress(new PostingProgress());
                                thr.setDaemon(false);
                                thr.start();
                            }else{
                                SessPostingNewThread thrdPosting = new SessPostingNewThread(loc);
                                Thread thr = new Thread(thrdPosting);
                                //
                                thrdPosting.setPreviousThread(thrdPostingPrev);
                                thrdPostingPrev = thrdPosting;
                                //
                                thr.setDaemon(false);
                                thr.start();
                                thrTx.put(""+locationId, thrdPosting);

                           }


                        }
            } catch(Exception exc){
                 System.out.println(exc);
            }

    }


    /**
     *
     * @param location : thread dari location id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int pauseThread(long oidLocation){
        try{
        SessPostingNewThread thrdPosting = (SessPostingNewThread) thrTx.get(""+oidLocation);
        thrdPosting.setStatusTxt("Paused");
        if(thrdPosting==null){
            return 1;
        }
        thrdPosting.setStatusTxt("Paused...");
        thrdPosting.setPauseTransfer(true);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

    public static String getStatusText(long oidLocation){
        try{
        SessPostingNewThread thrdPosting = (SessPostingNewThread) thrTx.get(""+oidLocation);
        if(thrdPosting==null){
            return "Stop";
        }
        return thrdPosting.getStatusTxt();

        }catch(Exception exc){
            System.out.println(exc);
            return "status failed";
        }
    }


     /**
     *
     * @param cashMasterId : thread dari location id yg akan di resume
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int resumeThread(long oidLocation){
        try{
       SessPostingNewThread thrdPosting = (SessPostingNewThread) thrTx.get(""+oidLocation);
        if(thrdPosting==null){
            return 1;
        }
        thrdPosting.setStatusTxt("resumed...");
        thrdPosting.setPauseTransfer(false);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

     /**
     *
     * @param locationId : thread dari location id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int stopThread(long oidLocation){
        try{
        SessPostingNewThread thrdPosting = (SessPostingNewThread) thrTx.get(""+oidLocation);
        thrdPosting.setStatusTxt("Stopped");
        if(thrdPosting==null){
            return 1;
        }
        thrdPosting.setRunThread(false);
        thrTx.remove(""+oidLocation);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

        public static PostingProgress getPostingProgress(long oidLocation){
        try{
        SessPostingNewThread thrdPosting = (SessPostingNewThread) thrTx.get(""+oidLocation);
        if(thrdPosting==null){
            return new PostingProgress();
        }
         return thrdPosting.getPostingProgress();
        }catch(Exception exc){
            System.out.println(exc);
            return new PostingProgress();
        }

    }

      
}
