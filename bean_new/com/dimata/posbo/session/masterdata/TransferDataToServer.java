/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.OutletConnection;
import com.dimata.posbo.entity.masterdata.PstConnection;
import com.dimata.posbo.entity.masterdata.TransferToServer;
import com.dimata.util.Formater;
import java.util.*;
/**
 *
 * @author Rahde, Kartika
 * @objective : transfer data yang berhubungan dengan data penjualan dari outlet ke
 * server sesuai dengan jumlah data connection yang ada
 * Class ini :
 *  - sebagai induk class yang akan diakses oleh jsp
 *  - mengenrate thread sejumlah connection ke outlet kasir
 *
 */
public class TransferDataToServer {
    private static Hashtable thrTx = new Hashtable();
    
       /**
     *
     * @param cashMasterId
     * @return status dari thread transfer
     */
    public static String getStatus(long cashMasterId){
        try{
            TransferDataToServerThread thrdTrans = (TransferDataToServerThread)thrTx.get(""+cashMasterId);
            if(thrdTrans==null){
                return "";
            }
            if(thrdTrans.isPauseTransfer())
                return "Paused";
            if(!thrdTrans.isPauseTransfer() && thrdTrans.isRunThread())
                return "Running";
            return  "Stopped";
        }catch(Exception ex){
            System.out.println(ex);
            return "";
        }

        
    }

    public static void startTransfer(Vector toStartOutlet, TransferToServer transferToServer){
        if(toStartOutlet==null || toStartOutlet.size()<1) {
            return ;
        }
        try{
               // Vector cashMasterDBConn = PstConnection.listAll();// cash master list db connection
                //if(cashMasterDBConn!=null && cashMasterDBConn.size()>0){
                        for(int i=0;i<toStartOutlet.size(); i++){
                            long oidDbConn= Long.parseLong(""+toStartOutlet.get(i));
                            
                            OutletConnection outletConn = PstConnection.fetchExc(oidDbConn);
                            long cashMasterId = outletConn.getCash_master_id();
                           //String rr = "";
                           //rr = getStatus(cashMasterId);
                            // buat & launch thread
                            // set thread ke hashtable id cash master sebagai key
                           // TransferDataToOutletThread thrdTrans = new TransferDataToOutletThread(outletConn);
                            if( getStatus(cashMasterId)=="Paused"  || getStatus(cashMasterId)=="Running" )  {
                                TransferDataToServerThread thrdTrans = (TransferDataToServerThread)thrTx.get(""+cashMasterId);
                                //thrdTrans.setRunThread(false);
                                thrdTrans.setTransfer(transferToServer);
                                thrdTrans.setRunThread(true);
                                Thread thr = new Thread(thrdTrans);
                                //Thread thr = new Thread(new TransferDataToOutletThread(outletConn));
                                thrdTrans.setDataProgres(new DataProgress());
                                thr.setDaemon(false);
                                thr.start();
                            }else{
                                TransferDataToServerThread thrdTrans = new TransferDataToServerThread(outletConn,transferToServer);
                                Thread thr = new Thread(thrdTrans);
                                thr.setDaemon(false);
                                thr.start();
                                thrTx.put(""+cashMasterId, thrdTrans);

                           }
                            

                        }
            } catch(Exception exc){
                 System.out.println(exc);
            }
       
    }

    //public static void startTransfer( OutletConnection outletyConn){
     //   if(!isStarted()){
      //      setStarted(true);
       //     try{
        //        Vector cashMasterDBConn = PstConnection.listAll();// cash master list db connection

         //       if(cashMasterDBConn!=null && cashMasterDBConn.size()>0){
           //        for(int i=0;i<cashMasterDBConn.size(); i++){
         //               OutletConnection outletConn = (OutletConnection) cashMasterDBConn.get(i);
         //               long cashMasterId=outletConn.getCash_master_id();
                        // buat & launch thread
                        // set thread ke hashtable id cash master sebagai key
                        //TransferDataToOutletThread thrdTrans = new TransferDataToOutletThread(outletConn);
         //               Thread thr = new Thread(new TransferDataToOutletThread(outletConn));
       //                 thr.setDaemon(false);
        //                thr.start();
                        //thrTx.put(""+cashMasterId, thrdTrans);
       //             }
        //        }

        //    } catch(Exception exc){
       //          setStarted(false);
      //      }
     //   }
   // }

    /**
     *
     * @param cashMasterId : thread dari cashmaster id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int pauseThread(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        thrdTrans.setStatusTxt("Paused");
        if(thrdTrans==null){
            return 1;
        }
        thrdTrans.setStatusTxt("Paused...");
        thrdTrans.setPauseTransfer(true);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

    public static String getStatusText(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return "Stop";
        }
        return thrdTrans.getStatusTxt();
        
        }catch(Exception exc){
            System.out.println(exc);
            return "status failed";
        }
    }

    /**
     * update opie-eyek 20130912
     * @param cashMasterId
     * @return
     */
    public static String getAddStatusText(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return "";
        }
        return thrdTrans.getAddStatusText();

        }catch(Exception exc){
            System.out.println(exc);
            return "connection faied";
        }
    }
     /**
     *
     * @param cashMasterId : thread dari cashmaster id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int resumeThread(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return 1;
        }
        thrdTrans.setStatusTxt("resumed...");
        thrdTrans.setPauseTransfer(false);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

     /**
     *
     * @param cashMasterId : thread dari cashmaster id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int stopThread(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        thrdTrans.setStatusTxt("Stoped");
        if(thrdTrans==null){
            return 1;
        }
        thrdTrans.setRunThread(false);
        thrTx.remove(""+cashMasterId);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

        public static DataProgress getDataProgress(long cashMasterId){
        try{
        TransferDataToServerThread  thrdTrans= (TransferDataToServerThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return new DataProgress();
        }
         return thrdTrans.getDataProgres();
        }catch(Exception exc){
            System.out.println(exc);
            return new DataProgress();
        }

    }

        public static TransferToServer getTransferToServer(long cashMasterId){
            try{
                TransferDataToServerThread thrdTrans= (TransferDataToServerThread) thrTx.get("" + cashMasterId);
            if(thrdTrans==null){
                return new TransferToServer();
            }
            return thrdTrans.getTransfer();
            } catch(Exception exc){
                System.out.println(exc);
                return new TransferToServer();
            }
        }
}

