package com.dimata.posbo.session.warehouse;

import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.qdep.entity.I_DocStatus;

import java.util.Date;
import java.util.Vector;
import java.io.Serializable;

//import org.postgresql.util.Serialize;
//import sun.security.krb5.internal.o;
/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Feb 8, 2007
 * Time: 3:16:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessThreadUpdateStock implements Runnable, Serializable {

    private Location loc;
    private boolean status = false;

    public void setStatus(boolean sts) {
        this.status = sts;
    }

    public SessThreadUpdateStock(Location location) {
        this.loc = location;
    }

    public synchronized void run() {
        System.out.println(".................... balance stock is started ....................");
        boolean firstProcess = true;
        Periode objPeriode = PstPeriode.getCurrentPeriode();
        while (true) {
            try {
                String where = "";
                Vector listLoc = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX]);
                where = PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=" + PstMaterial.DELETE +
                        " and " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + "=" + PstMaterial.MAT_TYPE_REGULAR;
                Vector listMat = PstMaterial.list(0, 0, where, "");
                Date dtstart = new Date();
                Date dtend = new Date();
                dtstart.setDate(dtend.getDate() - 1);

                SrcStockCard srcStockCard = new SrcStockCard();
                srcStockCard.setStardDate(dtstart);
                srcStockCard.setEndDate(dtend);
                Vector vDocStatus = new Vector(1, 1);
                vDocStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CLOSED));
                vDocStatus.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_POSTED));
                srcStockCard.setDocStatus(vDocStatus);
                if (listLoc != null && listLoc.size() > 0) {
                    for (int k = 0; k < listLoc.size(); k++) {
                        Location objLocation = (Location) listLoc.get(k);
                        srcStockCard.setLocationId(objLocation.getOID());
                        System.out.println("[" + (k + 1) + "/" + listLoc.size() + "] posting in location " + objLocation.getName() + "(" + objLocation.getOID() + ")");
                        if (listMat != null && listMat.size() > 0) {
                            for (int i = 0; i < listMat.size(); i++) {
                                Material mat = (Material) listMat.get(i);
                                srcStockCard.setMaterialId(mat.getOID());
                                Vector list = SessStockCard.createHistoryStockCard(srcStockCard);
                                double qtyReal = prosesGetPrivousDataStockCard(list);
                                long oidStock = PstMaterialStock.fetchByMaterialLocation(mat.getOID(), objLocation.getOID());
                                try {
                                    if (oidStock != 0) {
                                        MaterialStock matStock = PstMaterialStock.fetchExc(oidStock);
                                        matStock.setQty(qtyReal);
                                        PstMaterialStock.updateExc(matStock);
                                    //System.out.println("=>> success update stock : "+qtyReal);
                                    } else {
                                        System.out.print("=>> material: " + mat.getOID() + " di lokasi: " + objLocation.getOID() + " blm ada stok.");
                                        MaterialStock objMaterialStock = new MaterialStock();
                                        objMaterialStock.setMaterialUnitId(mat.getOID());
                                        objMaterialStock.setLocationId(objLocation.getOID());
                                        objMaterialStock.setPeriodeId(objPeriode.getOID());
                                        objMaterialStock.setQty(qtyReal);
                                        long oidStok = PstMaterialStock.insertExc(objMaterialStock);
                                    //System.out.println("  new stock oid : "+oidStok);
                                    }
                                } catch (Exception e) {
                                    System.out.println("=>> Err upda material . : mt oid : " + mat.getOID() + " loc :" + objLocation.getOID());
                                }
                            }
                        }
                        Thread.sleep(10000);
                    }
                    System.out.println("service reload.....");
                    Thread.sleep(300000);
                }

            } catch (Exception e) {
                System.out.println("Exc ServiceBackup : " + e.toString());
            }
        }
    }

    /*public synchronized void run(){
    System.out.println(".................... balance stock is started .................... loc : "+loc.getName());
    boolean firstProcess = true;
    while (status){
    try{
    Thread.sleep(50000);
    String where = "";
    //Vector listLoc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_TYPE] );
    where = PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS]+"!="+PstMaterial.DELETE+
    " and "+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+"="+PstMaterial.MAT_TYPE_REGULAR;
    Vector listMat = PstMaterial.list(0,0,where,"");
    Date dtstart = new Date();
    Date dtend = new Date();
    dtstart.setDate(dtend.getDate()-1);
    SrcStockCard srcStockCard = new SrcStockCard();
    srcStockCard.setStardDate(dtstart);
    srcStockCard.setEndDate(dtend);
    srcStockCard.setLocationId(loc.getOID());
    if(listMat!=null && listMat.size()>0){
    for(int i=0;i<listMat.size();i++){
    Material mat = (Material)listMat.get(i);
    srcStockCard.setMaterialId(mat.getOID());
    Vector list = SessStockCard.createHistoryStockCard(srcStockCard);
    int qtyReal = prosesGetPrivousDataStockCard(list);
    long oidStock = PstMaterialStock.fetchByMaterialLocation(mat.getOID(),loc.getOID());
    try{
    if(oidStock!=0){
    MaterialStock matStock = PstMaterialStock.fetchExc(oidStock);
    matStock.setQty(qtyReal);
    PstMaterialStock.updateExc(matStock);
    System.out.println("=>> Suksess update qty : mt oid : "+mat.getOID()+" loc :"+loc.getOID()+" stock : "+qtyReal);
    }else{
    System.out.println("=>> belum basuk stock mt oid : "+mat.getOID()+" loc :"+loc.getOID()+" stock : "+qtyReal);
    }
    }catch(Exception e){
    System.out.println("=>> Err upda material . : mt oid : "+mat.getOID()+" loc :"+loc.getOID());
    }
    }
    }
    }catch (Exception e){
    System.out.println("Exc ServiceBackup : " + e.toString());
    }
    }
    } */
    public static double prosesGetPrivousDataStockCard(Vector objectClassx) {
        double qtyawal = 0;
        StockCardReport stockCrp = (StockCardReport) objectClassx.get(0);
        Vector objectClass = (Vector) objectClassx.get(1);
        qtyawal = stockCrp.getQty();
        try {
            for (int i = 0; i < objectClass.size(); i++) {
                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
                switch (stockCardReport.getDocType()) {
                    case I_DocType.MAT_DOC_TYPE_LMRR:
                        qtyawal = qtyawal + stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_ROMR:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_DF:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_OPN:
                        qtyawal = stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_COS:
                        qtyawal = qtyawal - stockCardReport.getQty();
                        break;
                    case I_DocType.MAT_DOC_TYPE_SALE:
                        switch (stockCardReport.getTransaction_type()) {
                            case PstBillMain.TYPE_INVOICE:
                                qtyawal = qtyawal - stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_RETUR:
                                qtyawal = qtyawal + stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_GIFT:

                                break;
                            case PstBillMain.TYPE_COST:

                                break;
                            case PstBillMain.TYPE_COMPLIMENT:

                                break;
                        }
                        break;
                }
                System.out.println("==>>>>> Qty awal : " + qtyawal);
            }
        } catch (Exception e) {
            System.out.println("prosesGetPrivousDataStockCard : " + e.toString());
        }
        return qtyawal;
    }

    public static void main(String[] a) {
        try {
            Vector listLoc = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_TYPE]);
            if (listLoc != null && listLoc.size() > 0) {
                for (int i = 0; i < listLoc.size(); i++) {
                    Location lokasi = (Location) listLoc.get(i);
                    SessThreadUpdateStock sessThreadUpdateStock = new SessThreadUpdateStock(lokasi);
                    sessThreadUpdateStock.run();
                }
            }
        } catch (Exception r) {
        }
    }
}
