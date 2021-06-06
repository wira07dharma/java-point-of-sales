package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.session.masterdata.SessPosting;

import java.util.Date;
import java.io.Serializable;
import java.util.Vector; 

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Feb 8, 2007
 * Time: 3:16:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessThreadPostingDocumentInternal implements Runnable , Serializable {

    public synchronized void run(){
        System.out.println(".................... balance stock is started ....................");
        
        Vector vect = PstLocation.list(0, 0,"", "");
        //String sql = "update "+PstMaterial.TBL_MATERIAL+" set "+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+"="+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST];
        while (true){
            try{
                // DBHandler.execUpdate(sql);
                // System.out.println("Update average ..... [ OK ]");
                 
                System.out.println("Posting reload.....");
                SessPosting objSessPosting = new SessPosting();
                 
                for(int k=0;k<vect.size();k++){
                    Location location = (Location)vect.get(k);
                    
                    objSessPosting.postingTransDocument(new Date(), location.getOID() , false);
                }
                
                //objSessPosting.postingTransDocument(new Date(), 504404318374096129L, false);
                // 504404322322405817
                //objSessPosting.postingTransDocument(new Date(), 504404322322405817L, false);
                
                Thread.sleep(2000);
                
            }catch (Exception e){
                System.out.println("Exc ServiceBackup : " + e.toString());
            }
        }
    }

    public static void main(String[] a){
        try{
            SessThreadPostingDocumentInternal sessThreadUpdateStock = new SessThreadPostingDocumentInternal();
            sessThreadUpdateStock.run();
        }catch(Exception r){}
    }
}
