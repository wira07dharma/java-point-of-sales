package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstBillDetail;

import java.util.Vector;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Feb 8, 2007
 * Time: 3:16:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessThreadUpdateAverage implements Runnable {

    public SessThreadUpdateAverage()
    {

    }

    public synchronized void run(){
        System.out.println(".................... balance stock is started ....................");
        boolean firstProcess = true;
        while (true){
            try{
                String where = "";
                where = PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+"="+PstMaterial.MAT_TYPE_REGULAR;
                Vector listMat = PstMaterial.list(0,0,where,"");
                if(listMat!=null && listMat.size()>0){
                    for(int i=0;i<listMat.size();i++){
                        Material mat = (Material)listMat.get(i);
                        mat.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                        try{
                            if(mat.getAveragePrice()>0) {
                                StandartRate standartRate = PstStandartRate.getActiveStandardRate(mat.getDefaultCostCurrencyId());
                                mat.setAveragePrice(standartRate.getSellingRate() * mat.getAveragePrice());
                                String sql = "update "+ PstBillDetail.TBL_CASH_BILL_DETAIL+
                                        " set "+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+"="+mat.getAveragePrice()+
                                        " where "+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"="+mat.getOID();
                                DBHandler.execUpdate(sql);
                            }else{
                                where = PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+mat.getOID();
                                Vector list = PstMatVendorPrice.list(0,0,where,"");
                                if(list!=null && list.size()>0){
                                    MatVendorPrice matVendorPrice = (MatVendorPrice)list.get(0);
                                    StandartRate standartRate = PstStandartRate.getActiveStandardRate(matVendorPrice.getPriceCurrency());
                                    if(mat.getAveragePrice()==0){
                                        try{
                                            mat.setAveragePrice(matVendorPrice.getCurrBuyingPrice());
                                            PstMaterial.updateExc(mat);
                                        }catch(Exception e){}
                                    }
                                    try{
                                        mat.setAveragePrice(standartRate.getSellingRate() * matVendorPrice.getCurrBuyingPrice());
                                        String sql = "update "+ PstBillDetail.TBL_CASH_BILL_DETAIL+
                                                " set "+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+"="+mat.getAveragePrice()+
                                                " where "+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"="+mat.getOID();
                                        DBHandler.execUpdate(sql);
                                    }catch(Exception e){}
                                }else{
                                    try{
                                        StandartRate standartRate = PstStandartRate.getActiveStandardRate(mat.getDefaultCostCurrencyId());
                                        mat.setAveragePrice(standartRate.getSellingRate() * mat.getDefaultCost());
                                        try{
                                            PstMaterial.updateExc(mat);
                                        }catch(Exception e){}
                                        String sql = "update "+ PstBillDetail.TBL_CASH_BILL_DETAIL+
                                                " set "+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+"="+mat.getAveragePrice()+
                                                " where "+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"="+mat.getOID();
                                        DBHandler.execUpdate(sql);
                                    }catch(Exception e){}
                                }
                            }
                        }catch(Exception e){
                            System.out.println("=>> Err upda material . : mt oid : "+mat.getOID());
                        }
                    }
                }
                System.out.println("============= selesai ======================");
                Thread.sleep(100000);

            }catch (Exception e){
                System.out.println("Exc ServiceBackup : " + e.toString());
            }
        }
    }

    public static void main(String[] a){
        try{
            SessThreadUpdateAverage sessThreadUpdateStock = new SessThreadUpdateAverage();
            sessThreadUpdateStock.run();
        }catch(Exception r){}
    }
}
