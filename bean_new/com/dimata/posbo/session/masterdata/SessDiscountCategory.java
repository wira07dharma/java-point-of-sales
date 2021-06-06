/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 13, 2006
 * Time: 10:50:36 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.DiscountMapping;
import com.dimata.posbo.entity.masterdata.PstDiscountMapping;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.util.Command;

import java.util.Vector;

public class SessDiscountCategory {
    private long oidMaterial = 0;

    public void setOidMaterial(long oidmat){
        oidMaterial = oidmat;
    }

    public long getOidMaterial(){
        return oidMaterial;
    }

    public SessDiscountCategory(){
    }

    /**
     * gadnyana
     * proses pengecekan data data
     * penginsertan dan pengupdatetan data
     * @param oidCategory
     * @param vectDataDisc
     * @return
     */
    public Vector insertUpdateDiscountCategory(int cmd, long oidCategory, Vector vectDataDisc) {
        Vector listDisc = new Vector(1, 1);
        long oidMaterial = 0;
        if (cmd == Command.SAVE) {
            if (vectDataDisc != null && vectDataDisc.size() > 0) {
                String where = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + oidCategory;
                Vector list = PstMaterial.list(0, 0, where, PstMaterial.fieldNames[PstMaterial.FLD_SKU]);
                if (list != null && list.size() > 0) {
                    for (int k = 0; k < list.size(); k++) {
                        Material material = (Material) list.get(k);
                        oidMaterial = material.getOID();
                        setOidMaterial(oidMaterial);
                        // proses pengecekan dan insert data discount
                        // per barang
                        for (int i = 0; i < vectDataDisc.size(); i++) {
                            Vector vect = (Vector) vectDataDisc.get(i);
                            for (int j = 0; j < vect.size(); j++) {
                                Vector vect2 = (Vector) vect.get(j);
                                long oidDiscType = Long.parseLong((String) vect2.get(0));
                                long oidCurrType = Long.parseLong((String) vect2.get(1));
                                double valuePct = Double.parseDouble((String) vect2.get(2));
                                double value = Double.parseDouble((String) vect2.get(3));
                                DiscountMapping discMapping = new DiscountMapping();
                                discMapping.setCurrencyTypeId(oidCurrType);
                                discMapping.setDiscountPct(valuePct);
                                discMapping.setDiscountTypeId(oidDiscType);
                                discMapping.setDiscountValue(value);
                                discMapping.setMaterialId(material.getOID());

                                boolean checkOID = PstDiscountMapping.checkOID(oidDiscType, material.getOID(), oidCurrType);
                                if (!checkOID) {
                                    try {
                                        PstDiscountMapping.insertExc(discMapping);
                                    } catch (Exception err) {
                                        err.printStackTrace();
                                        System.out.println("err di insert to disc mapping " + err.toString());
                                    }
                                } else {
                                    try {
                                        PstDiscountMapping.updateExc(discMapping);
                                    } catch (Exception err) {
                                        err.printStackTrace();
                                        System.out.println("err di update to disc mapping " + err.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            String where = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + oidCategory;
            Vector list = PstMaterial.list(0, 0, where, PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " DESC ");
            if (list != null && list.size() > 0) {
                Material material = (Material) list.get(0);
                oidMaterial = material.getOID();
                setOidMaterial(oidMaterial);
            }
        }
        System.out.println(">>>> : oidMaterial : "+oidMaterial);
        // proses pegambilan data discount tapi sesuai dengan oid material yang terakhir
        String wh = PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID] + " = " + getOidMaterial();
        listDisc = PstDiscountMapping.list(0, 0, wh, "");
        return listDisc;
    }
}
