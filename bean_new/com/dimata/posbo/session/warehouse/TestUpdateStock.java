package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Apr 12, 2007
 * Time: 12:09:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestUpdateStock {

    public static void main(String[] a){
        try{
            SessThreadUpdateStock sessThreadUpdateStock = new SessThreadUpdateStock(new Location());
            sessThreadUpdateStock.setStatus(true);
            Thread thr = new Thread(sessThreadUpdateStock);
            thr.setDaemon(false);
            thr.start();

            SessThreadPostingDocument sessThreadPostingDocument = new SessThreadPostingDocument(new Location());
            Thread thrposting = new Thread(sessThreadPostingDocument);
            thrposting.setDaemon(false);
            thrposting.start();

            SessThreadUpdateAverageSale sessThreadUpdateAverageSale = new SessThreadUpdateAverageSale();
            Thread thrs = new Thread(sessThreadUpdateAverageSale);
            thrs.setDaemon(false);
            thrs.start();

        }catch(Exception r){}
    }

}
