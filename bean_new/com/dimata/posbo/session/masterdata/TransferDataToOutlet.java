/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.OutletConnection;
import com.dimata.posbo.entity.masterdata.PstConnection;
import com.dimata.posbo.entity.masterdata.PstDataSyncStatus;
import java.util.*;
/**
 *
 * @author Rahde, Kartika
 * @objective : transfer data yang berhubungan dengan catalog dari server ke
 * outlet kasir sesuai dengan jumlah data connection yang ada
 * Class ini :
 *  - sebagai induk class yang akan diakses oleh jsp
 *  - mengenrate thread sejumlah connection ke outlet kasir
 * update opie-eyek 20130906 agar thread tidak lepas jika double select
 */
public class TransferDataToOutlet {
    private static Hashtable thrTx = new Hashtable();
       /**
     *
     * @param cashMasterId
     * @return status dari thread transfer
     */
    public static String getStatus(long cashMasterId){
        TransferDataToOutletThread thrdTrans = (TransferDataToOutletThread)thrTx.get(""+cashMasterId);
        if(thrdTrans.isPauseTransfer())
            return "Paused";
        if(!thrdTrans.isPauseTransfer() && thrdTrans.isRunThread())
            return "Running";
        return  "Stopped";
    }

    public static void startTransfer(Vector toStartOutlet){
        if(toStartOutlet==null || toStartOutlet.size()<1) {
            return ;
        }
        try{
            // Vector cashMasterDBConn = PstConnection.listAll();// cash master list db connection
            //if(cashMasterDBConn!=null && cashMasterDBConn.size()>0){
            for(int i=0;i<toStartOutlet.size(); i++){
                 long oidDbConn= Long.parseLong(""+toStartOutlet.get(i));
                 //OutletConnection outletConn = new OutletConnection();
                 //String r = getStatus(outletConn.getCash_master_id());
                 OutletConnection outletConn = PstConnection.fetchExc(oidDbConn);
                 long cashMasterId = outletConn.getCash_master_id();
                 TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
                 if(thrdTrans!=null){
                     if(thrdTrans.isRunThread()==false){
                        thrdTrans.run();
                     }
                 }else{
                     thrdTrans = new TransferDataToOutletThread(outletConn);
                     thrdTrans.setStatusText("Start transfer...");
                     Thread thr = new Thread(thrdTrans);
                      //Thread thr = new Thread(new TransferDataToOutletThread(outletConn));
                     thr.setDaemon(false);
                     thr.start();
                     thrTx.put(""+cashMasterId, thrdTrans);
                 }
                 //String rr = "";
                           //rr = getStatus(cashMasterId);
                            // buat & launch thread
                            // set thread ke hashtable id cash master sebagai key
                           // TransferDataToOutletThread thrdTrans = new TransferDataToOutletThread(outletConn);
             }
               //}
            } catch(Exception exc){
                 System.out.println(exc);
            }
       
    }

    public static void ResetDataTransfer(Vector outlet){
        if(outlet==null || outlet.size()<0){
            return ;
        }
        try{
            for(int i=0;i<outlet.size();i++){
                long oidDbConn = Long.parseLong(""+outlet.get(i));
                PstDataSyncStatus pstDataSyncStatus = new PstDataSyncStatus(0);
                pstDataSyncStatus.deleteRecExc(PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + " = " + oidDbConn);
            }

        }catch(Exception ex){
            System.out.println(ex);
        }
    }
    /**
     *
     * @param cashMasterId : thread dari cashmaster id yg akan di pause
     * @return 0=success 1= no thread with cash master ID 2= other exception
     */
    public static int pauseThread(long cashMasterId){
        try{
        TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return 1;
        }
        thrdTrans.setStatusText("Paused...");
        thrdTrans.setPauseTransfer(true);
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
    public static int resumeThread(long cashMasterId){
        try{
        TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return 1;
        }

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
        TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return 1;
        }
        thrdTrans.setStatusText("Stoped...");
        thrdTrans.setRunThread(false);
        thrTx.remove(""+cashMasterId);
        return 0;
        }catch(Exception exc){
            System.out.println(exc);
            return 2;
        }
    }

    public static String getStatusText(long cashMasterId){
        try{
        TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return "";
        }
        return thrdTrans.getStatusText();
        
        }catch(Exception exc){
            System.out.println(exc);
            return "connection faied";
        }
    }

    public static String getAddStatusText(long cashMasterId){
        try{
        TransferDataToOutletThread  thrdTrans= (TransferDataToOutletThread) thrTx.get(""+cashMasterId);
        if(thrdTrans==null){
            return "";
        }
        return thrdTrans.getAddStatusText();

        }catch(Exception exc){
            System.out.println(exc);
            return "connection faied";
        }
    }
}
