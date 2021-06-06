package com.dimata.posbo.session.sales;

import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.posbo.form.masterdata.FrmPersonalDiscount;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Nov 17, 2006
 * Time: 7:52:58 PM
 * To change this template use File | Settings | File Templates.
 */ 
public class SessSaleCommision {

    public static final int COST_OTHER = 0;
    public static final int COST_CARGO = 1;
    public static final int COST_SUPPLIER = 2;
    public static final int COST_PPN = 3;
    public static final int SAVING_PC = 4;
    public static final int COMM_OUTLET = 5;
    public static final int DISC_OUTLET = 6;

    public static String[] strNameCommision = {"COST_OTHER","COST_CARGO","COST_SUPPLIER","COST_PPN","SAVING_PC","COMM_OUTLET","DISC_OUTLET"};

    public static void saveDataCommision(Vector list){
        try{
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    DataCustom dataCustom = (DataCustom)list.get(i);
                    try{
                        if(dataCustom.getOID()==0){
                            PstDataCustom.insertExc(dataCustom);
                            System.out.println("===>>>>>>> insert data custom *********** ");
                        }else{
                            PstDataCustom.updateExc(dataCustom);
                            System.out.println("===>>>>>> update data custom ************");
                        }
                    }catch(Exception e){}
                }
            }
        }catch(Exception e){
            System.out.println("...>>>>> err saveDataCommision"+e.toString());
        }
    }

    /**
     * gadnyana
     * proses get data custom untuk
     * penyimpanan data commision/cost
     */
    public static Vector getDataCommision(long ownerOid){
        Vector list = new Vector();
        try{
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[COST_OTHER]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[COST_CARGO]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[COST_SUPPLIER]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[COST_PPN]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[SAVING_PC]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[COMM_OUTLET]));
            list.add(PstDataCustom.getDataCustom(ownerOid,strNameCommision[DISC_OUTLET]));
        }catch(Exception e){}
        return list;
    }

    public static Vector getDataCommision(long ownerOid, String oidLink){
        Vector list = new Vector();
        try{
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[COST_OTHER]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[COST_CARGO]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[COST_SUPPLIER]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[COST_PPN]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[SAVING_PC]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[COMM_OUTLET]));
            list.add(PstDataCustom.getDataCustom(ownerOid,String.valueOf(oidLink),strNameCommision[DISC_OUTLET]));

        }catch(Exception e){}
        return list;
    }

    /**
     * gadnyana
     * proses pembuatan invoice untuk
     * internal yang akan di simpan di database
     */
    public static Vector insertDataCommision(HttpServletRequest request,long oid){
        try{
            /**
             * gadnyana
             * proses insert data discount data cost ppn
             * ini sesuai dengan permintaan dari client Surft Travel Online
             */
            double discOther = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            double discCargo = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            double discSupp = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            double costPpn = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            double savingPc = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            double commOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            double discOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);

            long oidLink = FRMQueryString.requestLong(request, FrmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_MATERIAL_ID]);
            Vector list = new Vector();
            DataCustom dataCustom = new DataCustom();
            // disc other
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            dataCustom.setDataValue(String.valueOf(discOther));
            list.add(dataCustom);
            // disc cargo
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            dataCustom.setDataValue(String.valueOf(discCargo));
            list.add(dataCustom);
            // disc supp
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            dataCustom.setDataValue(String.valueOf(discSupp));
            list.add(dataCustom);
            // cost ppn
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            dataCustom.setDataValue(String.valueOf(costPpn));
            list.add(dataCustom);
            // saving pc
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setDataValue(String.valueOf(savingPc));
            list.add(dataCustom);
            // commision outlet
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            dataCustom.setDataValue(String.valueOf(commOutlet));
            list.add(dataCustom);
            // discount outlet
            dataCustom = new DataCustom();
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            dataCustom.setDataValue(String.valueOf(discOutlet));
            list.add(dataCustom);

            SessSaleCommision.saveDataCommision(list);
        }catch(Exception e){}
        return new Vector();
    }

    /**
     * gadnyana
     * data untuk update data commision
     */
    public static Vector updateDataCommision(HttpServletRequest request,long oid){
        Vector list = new Vector();
        try{
            double discOther = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            double discCargo = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            double discSupp = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            double costPpn = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            double savingPc = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            double commOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            double discOutlet = FRMQueryString.requestDouble(request, SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            long oidLink = FRMQueryString.requestLong(request, FrmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_MATERIAL_ID]);

            // disc other
            DataCustom dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER]);
            dataCustom.setDataValue(String.valueOf(discOther));
            list.add(dataCustom);
            // disc cargo
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO]);
            dataCustom.setDataValue(String.valueOf(discCargo));
            list.add(dataCustom);
            // disc supp
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER]);
            dataCustom.setDataValue(String.valueOf(discSupp));
            list.add(dataCustom);
            // cost ppn
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]);
            dataCustom.setDataValue(String.valueOf(costPpn));
            list.add(dataCustom);
            // cost ppn
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setDataValue(String.valueOf(savingPc));
            list.add(dataCustom);
            // comm outlet
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.COMM_OUTLET]);
            dataCustom.setDataValue(String.valueOf(commOutlet));
            list.add(dataCustom);
            // disc outlet
            dataCustom = PstDataCustom.getDataCustom(oid,String.valueOf(oidLink),SessSaleCommision.strNameCommision[SessSaleCommision.SAVING_PC]);
            dataCustom.setOwnerId(oid);
            dataCustom.setLink(String.valueOf(oidLink));
            dataCustom.setDataName(SessSaleCommision.strNameCommision[SessSaleCommision.DISC_OUTLET]);
            dataCustom.setDataValue(String.valueOf(discOutlet));
            list.add(dataCustom);

            SessSaleCommision.saveDataCommision(list);

        }catch(Exception e){}
        return list;
    }

}
