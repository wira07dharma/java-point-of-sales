/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;
import java.util.*;

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.search.*;


/**
 *
 * @author Dimata 007
 */
public class SessReposting {
    
    private static Hashtable thrTx = new Hashtable();

       /**
     *
     * @param cashMasterId
     * @return status dari thread transfer
     */
      //public static String getStatus(long oidMaterial){
        public static String getStatus(long oidLocation){
        try{
            //SessRepostingThread thrdReposting = (SessRepostingThread)thrTx.get(""+oidMaterial);
              SessRepostingThread thrdReposting = (SessRepostingThread)thrTx.get(""+oidLocation);
            if(thrdReposting==null){
                return "";
            }
            if(thrdReposting.isPauseTransfer())
                return "Paused";
            if(!thrdReposting.isPauseTransfer() && thrdReposting.isRunThread())
                return "Running";
            return  "Stopped";
        }catch(Exception ex){
            System.out.println(ex);
            return "";
        }


    }

    public static void startTransfer(Vector materialReposting, SrcMaterialRepostingStock srcMaterialRepostingStock){
        if(materialReposting==null || materialReposting.size()<1) {
            return ;
        }
        try{
                        SessRepostingThread thrdRepostingPrev = null;
                        long oidLocation = srcMaterialRepostingStock.getLocationId();
                        
                         if( getStatus(oidLocation)== "Paused"  || getStatus(oidLocation)=="Running" )  {
                                 SessRepostingThread thrdReposting = (SessRepostingThread)thrTx.get(""+oidLocation);
                                 thrdReposting.setPreviousThread(thrdRepostingPrev);
                                 thrdRepostingPrev = thrdReposting;
                                 thrdReposting.setRunThread(false);
                                 thrdReposting.setRunThread(true);
                                 Thread thr = new Thread(thrdReposting);
                                 thrdReposting.setRepostingProgress(new RepostingProgress());
                                 thr.setDaemon(false);
                                 thr.start();
                            }else{
                                SessRepostingThread thrdReposting = new SessRepostingThread(srcMaterialRepostingStock);
                                Thread thr = new Thread(thrdReposting);
                                
                                 thrdReposting.setPreviousThread(thrdRepostingPrev);
                                 thrdRepostingPrev = thrdReposting;
                                 thr.setDaemon(false);
                                 thr.start();
                                 thrTx.put(""+oidLocation, thrdReposting);

                           }
                        
                        //srcMaterialRepostingStock.setDateFrom(dateFrom);
                        //srcMaterialRepostingStock.setDateTo(dateTo);
                        //srcMaterialRepostingStock.setLocationId(oidLocation);
                        
                        //for(int i=0;i<materialReposting.size(); i++){
                            //long oidMaterial= Long.parseLong(""+materialReposting.get(i));

                            //Material mat = PstMaterial.fetchExc(oidMaterial);
                            //long materialId = mat.getOID();
                            
                            //Vector temp = (Vector)materialReposting.get(i);
                           // Material material = (Material)temp.get(0);
                            //Category category = (Category)temp.get(1);
            
                            //Merk merk = (Merk)temp.get(3);
                            
                            //long materialId = material.getOID();
                            
                        
                           // if( getStatus(materialId)=="Paused"  || getStatus(materialId)=="Running" )  {
                              //  SessRepostingThread thrdReposting = (SessRepostingThread)thrTx.get(""+materialId);
                              //  thrdReposting.setPreviousThread(thrdRepostingPrev);
                               // thrdRepostingPrev = thrdReposting;
                                //thrdPosting.setRunThread(false);
                               // thrdReposting.setRunThread(true);
                               // Thread thr = new Thread(thrdReposting);
                                //Thread thr = new Thread(new TransferDataToOutletThread(outletConn));
                               // thrdReposting.setRepostingProgress(new RepostingProgress());
                               // thr.setDaemon(false);
                               // thr.start();
                           // }else{
                               // SessRepostingThread thrdReposting = new SessRepostingThread(material, srcMaterialRepostingStock);
                               // Thread thr = new Thread(thrdReposting);
                                //
                              //  thrdReposting.setPreviousThread(thrdRepostingPrev);
                                //thrdRepostingPrev = thrdReposting;
                                //
                               // thr.setDaemon(false);
                               // thr.start();
                               // thrTx.put(""+materialId, thrdReposting);

                         //  }


                        //}
            } catch(Exception exc){
                 System.out.println(exc);
            }

    }


    /**
     *
     * @param location : thread dari material id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    //public static int pauseThread(long oidMaterial){
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

    //public static String getStatusText(long oidMaterial){
      public static String getStatusText(long oidLocation){
        try{
        SessRepostingThread thrdReposting = (SessRepostingThread) thrTx.get(""+oidLocation);
        if(thrdReposting==null){
            return "Stop";
        }
        return thrdReposting.getStatusTxt();

        }catch(Exception exc){
            System.out.println(exc);
            return "status failed";
        }
    }


     /**
     *
     * @param cashMasterId : thread dari material id yg akan di resume
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    //public static int resumeThread(long oidMaterial){
      public static int resumeThread(long oidLocation){
        try{
       SessRepostingThread thrdRePosting = (SessRepostingThread) thrTx.get(""+oidLocation);
        if(thrdRePosting==null){
            return 1;
        }
        thrdRePosting.setStatusTxt("resumed...");
        thrdRePosting.setPauseTransfer(false);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

     /**
     *
     * @param materialId : thread dari material id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    //public static int stopThread(long oidMaterial){
      public static int stopThread(long oidLocation){
        try{
        SessRepostingThread thrdRePosting = (SessRepostingThread)  thrTx.get(""+oidLocation);
        thrdRePosting.setStatusTxt("Stoped");
        if(thrdRePosting==null){
            return 1;
        }
        thrdRePosting.setRunThread(false);
        thrTx.remove(""+oidLocation);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

        //public static RepostingProgress getRepostingProgress(long oidMaterial){
        public static RepostingProgress getRepostingProgress(long oidLocation){
        try{
        //SessRepostingThread thrdRePosting = (SessRepostingThread) thrTx.get(""+oidMaterial);
          SessRepostingThread thrdRePosting = (SessRepostingThread) thrTx.get(""+oidLocation);
        if(thrdRePosting==null){
            return new RepostingProgress();
        }
         return thrdRePosting.getRepostingProgress();
        }catch(Exception exc){
            System.out.println(exc);
            return new RepostingProgress();
        }

    }
    
}
