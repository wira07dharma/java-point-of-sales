package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.posbo.session.masterdata.SessPosting;

import java.util.Vector;
import java.util.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. 
 * User: gadnyana
 * Date: Feb 8, 2007
 * Time: 3:16:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessThreadPostingDocument implements Runnable , Serializable {

    private Location loc;
    public SessThreadPostingDocument(Location location)
    {
        this.loc = location;
    }

    public synchronized void run(){
        System.out.println(".................... balance stock is started ....................");
        while (true){
            try{
                Vector listLoc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_TYPE] );
                if(listLoc!=null && listLoc.size()>0){
                    for(int k=0;k<listLoc.size();k++){
                        Location loc = (Location)listLoc.get(k);
                        SessPosting objSessPosting = new SessPosting();
                        objSessPosting.postingTransDocument(new Date(), loc.getOID(), false);
                    }
                    System.out.println("service reload");
                    Thread.sleep(50000);
                }

            }catch (Exception e){
                System.out.println("Exc ServiceBackup : " + e.toString());
            }
        }
    }

    public static void main(String[] a){
        try{
            //SessThreadPostingDocument sessThreadUpdateStock = new SessThreadPostingDocument();
           // sessThreadUpdateStock.run();
        }catch(Exception r){}
    }
}
