package com.dimata.posbo.excel.upload;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.session.warehouse.SessMatDispatch;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.periode.Periode;
import com.dimata.qdep.entity.I_IJGeneral;

import java.util.Vector;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Nov 9, 2006
 * Time: 10:03:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadDispatchExcel {

    public static Vector uploadDataDispatch(MatDispatch matDispatch, Vector vect){
        Vector vStatus = new Vector();
        try{
            if(vect!=null && vect.size()!=0){
                SessMatDispatch sessDispatch = new SessMatDispatch();
                int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                maxCounter = maxCounter + 1;
                matDispatch.setDispatchCodeCounter(maxCounter);
                matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));
                matDispatch.setLast_update(new Date());
                long oidMain = PstMatDispatch.insertExc(matDispatch);
                PstDocLogger.insertDataBo_toDocLogger(matDispatch.getDispatchCode(), I_IJGeneral.DOC_TYPE_INVENTORY_ON_DF, matDispatch.getLast_update(), matDispatch.getRemark());
                for(int i=0;i<vect.size();i++){
                    System.out.println("******* inserting index "+i);
                    MatDispatchItem matDispatchItem = (MatDispatchItem)vect.get(i);
                    matDispatchItem.setDispatchMaterialId(oidMain);
                    try{
                        PstMatDispatchItem.insertExc(matDispatchItem);
                    }catch(Exception e){
                        vStatus.add(matDispatchItem);
                    }
                }
                SessPosting sessPosting = new SessPosting();
                Periode periode = PstPeriode.getPeriodeRunning();
                Vector vErrUpdateStockByDispatchItem = sessPosting.updateStockByDocDispatchItem(periode.getOID(), matDispatch.getLocationId(), oidMain);

                // Set status document receive menjadi posted
                boolean isOK = false;
                //if (!(vErrUpdateStockByDispatchItem != null && vErrUpdateStockByDispatchItem.size() > 0)) {
                    isOK = SessPosting.setDocumentByPosted(oidMain, SessPosting.DOC_TYPE_DISPATCH);
                //}
            }
        }catch(Exception e){
            System.out.println("==>>>> error proses insert data opname");
        }
        return vStatus;
    }

}
