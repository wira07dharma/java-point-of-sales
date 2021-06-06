/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.masterdata.PstMaterial;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstMatDispatchReceiveItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MAT_DISPATCH_RECEIVE_ITEM = "pos_dispatch_receive_item";
    public static final int FLD_DF_REC_ITEM_ID = 0;
    public static final int FLD_RECEIVE_MATERIAL_ITEM_ID = 1;
    public static final int FLD_DISPATCH_MATERIAL_ITEM_ID = 2;
    public static final int FLD_DF_REC_GROUP_ID = 3;
    public static final int FLD_DISPATCH_MATERIAL_ID = 4;

    public static final String[] fieldNames = {
        "DF_REC_ITEM_ID",
        "RECEIVE_MATERIAL_ITEM_ID",
        "DISPATCH_MATERIAL_ITEM_ID",
        "DF_REC_GROUP_ID",
        "DISPATCH_MATERIAL_ID"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstMatDispatchReceiveItem() {
    }

    public PstMatDispatchReceiveItem(int i) throws DBException {
        super(new PstMatDispatchReceiveItem());
    }

    public PstMatDispatchReceiveItem(String sOid) throws DBException {
        super(new PstMatDispatchReceiveItem(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMatDispatchReceiveItem(long lOid) throws DBException {
        super(new PstMatDispatchReceiveItem(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_MAT_DISPATCH_RECEIVE_ITEM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatDispatchReceiveItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatDispatchReceiveItem matDispatchReceiveItem = fetchExc(ent.getOID());
        ent = (Entity) matDispatchReceiveItem;
        return matDispatchReceiveItem.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatDispatchReceiveItem) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatDispatchReceiveItem) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatDispatchReceiveItem fetchExc(long oid) throws DBException {
        try {
            MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(oid);

            /*matDispatchReceiveItem.setOID(oid);
             matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
             matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));
             //+matDispatchItem dan matReceiveItem
             matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
             matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
             */
            //new
            MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
            MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();
            Material materialSource = matDispatchItem.getMaterialSource();
            Unit unitSource = matDispatchItem.getUnitSource();
            Material materialTarget = matReceiveItem.getMaterialTarget();
            Unit unitTarget = matReceiveItem.getUnitTarget();

            PstMatDispatchItem pstMatDispatchItem = new PstMatDispatchItem(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            PstMaterial pstMaterialSource = new PstMaterial(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
            PstMaterial pstMaterialTarget = new PstMaterial(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
            PstUnit pstUnitSource = new PstUnit(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
            PstUnit pstUnitTarget = new PstUnit(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));

            matDispatchReceiveItem.setOID(oid);
            matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
            matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));
            matDispatchItem.setMaterialId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
            matDispatchItem.setQty(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_QTY));
            matDispatchItem.setUnitId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
            matDispatchItem.setHpp(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP));
            matDispatchItem.setHppTotal(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP_TOTAL));
            matReceiveItem.setMaterialId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
            matReceiveItem.setQty(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_QTY));
            matReceiveItem.setUnitId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));
            matReceiveItem.setCost(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_COST));
            matReceiveItem.setTotal(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_TOTAL));
            materialSource.setSku(pstMaterialSource.getString(PstMaterial.FLD_SKU));
            materialSource.setName(pstMaterialSource.getString(PstMaterial.FLD_NAME));
            materialTarget.setSku(pstMaterialTarget.getString(PstMaterial.FLD_SKU));
            materialTarget.setName(pstMaterialTarget.getString(PstMaterial.FLD_NAME));
            unitSource.setCode(pstUnitSource.getString(PstUnit.FLD_CODE));
            unitTarget.setCode(pstUnitTarget.getString(PstUnit.FLD_CODE));
            // end Of new

            //matDispatchReceiveItem.getSourceItem().setMaterialSource(PstMaterial.fetchExc(matDispatchReceiveItem.getSourceItem().getMaterialId()));
            //matDispatchReceiveItem.getTargetItem().setMaterialTarget(PstMaterial.fetchExc(matDispatchReceiveItem.getTargetItem().getMaterialId()));
            //matDispatchReceiveItem.getSourceItem().setUnitSource(PstUnit.fetchExc(matDispatchReceiveItem.getSourceItem().getUnitId()));
            //matDispatchReceiveItem.getTargetItem().setUnitTarget(PstUnit.fetchExc(matDispatchReceiveItem.getTargetItem().getUnitId()));
            return matDispatchReceiveItem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
    }

    public static MatDispatchReceiveItem fetchExcHpp(long oid) throws DBException {
        try {
            MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(oid);

            /*matDispatchReceiveItem.setOID(oid);
             matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
             matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));
             //+matDispatchItem dan matReceiveItem
             matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
             matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
             */
            //new
            MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
            MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();

            if (matDispatchItem.getMaterialId() != 0) {
                Material materialSource = matDispatchItem.getMaterialSource();
                Unit unitSource = matDispatchItem.getUnitSource();
                PstMatDispatchItem pstMatDispatchItem = new PstMatDispatchItem(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
                PstMaterial pstMaterialSource = new PstMaterial(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
                PstUnit pstUnitSource = new PstUnit(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
                matDispatchItem.setMaterialId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
                matDispatchItem.setQty(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_QTY));
                matDispatchItem.setUnitId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
                matDispatchItem.setHpp(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP));
                matDispatchItem.setHppTotal(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP_TOTAL));
                materialSource.setSku(pstMaterialSource.getString(PstMaterial.FLD_SKU));
                materialSource.setName(pstMaterialSource.getString(PstMaterial.FLD_NAME));
                unitSource.setCode(pstUnitSource.getString(PstUnit.FLD_CODE));
            }
            if (matReceiveItem.getMaterialId() != 0) {
                Material materialTarget = matReceiveItem.getMaterialTarget();
                Unit unitTarget = matReceiveItem.getUnitTarget();
                PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
                PstMaterial pstMaterialTarget = new PstMaterial(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
                PstUnit pstUnitTarget = new PstUnit(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));
                matReceiveItem.setMaterialId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
                matReceiveItem.setQty(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_QTY));
                matReceiveItem.setUnitId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));
                matReceiveItem.setCost(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_COST));
                matReceiveItem.setTotal(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_TOTAL));
                materialTarget.setSku(pstMaterialTarget.getString(PstMaterial.FLD_SKU));
                materialTarget.setName(pstMaterialTarget.getString(PstMaterial.FLD_NAME));
                unitTarget.setCode(pstUnitTarget.getString(PstUnit.FLD_CODE));
            }

            matDispatchReceiveItem.setOID(oid);
            matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
            matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));

            // end Of new
            //matDispatchReceiveItem.getSourceItem().setMaterialSource(PstMaterial.fetchExc(matDispatchReceiveItem.getSourceItem().getMaterialId()));
            //matDispatchReceiveItem.getTargetItem().setMaterialTarget(PstMaterial.fetchExc(matDispatchReceiveItem.getTargetItem().getMaterialId()));
            //matDispatchReceiveItem.getSourceItem().setUnitSource(PstUnit.fetchExc(matDispatchReceiveItem.getSourceItem().getUnitId()));
            //matDispatchReceiveItem.getTargetItem().setUnitTarget(PstUnit.fetchExc(matDispatchReceiveItem.getTargetItem().getUnitId()));
            return matDispatchReceiveItem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
    }

    public static MatDispatchReceiveItem fetchExcProduksi(long oid) throws DBException {
        try {
            MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(oid);

            /*matDispatchReceiveItem.setOID(oid);
             matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
             matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));
             //+matDispatchItem dan matReceiveItem
             matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
             matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
             */
            //new
            //MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
            MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();
            //Material materialSource = matDispatchItem.getMaterialSource();
            //Unit unitSource = matDispatchItem.getUnitSource();
            Material materialTarget = matReceiveItem.getMaterialTarget();
            Unit unitTarget = matReceiveItem.getUnitTarget();

            //PstMatDispatchItem pstMatDispatchItem = new PstMatDispatchItem(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            //PstMaterial pstMaterialSource = new PstMaterial(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
            PstMaterial pstMaterialTarget = new PstMaterial(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
            //PstUnit pstUnitSource = new PstUnit(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
            PstUnit pstUnitTarget = new PstUnit(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));

            matDispatchReceiveItem.setOID(oid);
            matDispatchReceiveItem.getSourceItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.getTargetItem().setOID(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            matDispatchReceiveItem.setDfRecGroupId(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_GROUP_ID));
            matDispatchReceiveItem.setDispatchMaterialId(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ID));
            //matDispatchItem.setMaterialId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_MATERIAL_ID));
            //matDispatchItem.setQty(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_QTY));
            //matDispatchItem.setUnitId(pstMatDispatchItem.getlong(PstMatDispatchItem.FLD_UNIT_ID));
            //matDispatchItem.setHpp(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP));
            //matDispatchItem.setHppTotal(pstMatDispatchItem.getdouble(PstMatDispatchItem.FLD_HPP_TOTAL));
            matReceiveItem.setMaterialId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_MATERIAL_ID));
            matReceiveItem.setQty(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_QTY));
            matReceiveItem.setUnitId(pstMatReceiveItem.getlong(PstMatReceiveItem.FLD_UNIT_ID));
            matReceiveItem.setCost(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_COST));
            matReceiveItem.setTotal(pstMatReceiveItem.getdouble(PstMatReceiveItem.FLD_TOTAL));
            //materialSource.setSku(pstMaterialSource.getString(PstMaterial.FLD_SKU));
            //materialSource.setName(pstMaterialSource.getString(PstMaterial.FLD_NAME));
            materialTarget.setSku(pstMaterialTarget.getString(PstMaterial.FLD_SKU));
            materialTarget.setName(pstMaterialTarget.getString(PstMaterial.FLD_NAME));
            //unitSource.setCode(pstUnitSource.getString(PstUnit.FLD_CODE));
            unitTarget.setCode(pstUnitTarget.getString(PstUnit.FLD_CODE));
            // end Of new

            //matDispatchReceiveItem.getSourceItem().setMaterialSource(PstMaterial.fetchExc(matDispatchReceiveItem.getSourceItem().getMaterialId()));
            //matDispatchReceiveItem.getTargetItem().setMaterialTarget(PstMaterial.fetchExc(matDispatchReceiveItem.getTargetItem().getMaterialId()));
            //matDispatchReceiveItem.getSourceItem().setUnitSource(PstUnit.fetchExc(matDispatchReceiveItem.getSourceItem().getUnitId()));
            //matDispatchReceiveItem.getTargetItem().setUnitTarget(PstUnit.fetchExc(matDispatchReceiveItem.getTargetItem().getUnitId()));
            return matDispatchReceiveItem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatDispatchReceiveItem matDispatchReceiveItem) throws DBException {
        try {

            PstMatDispatchItem.insertExc(matDispatchReceiveItem.getSourceItem());
            PstMatReceiveItem.insertExc(matDispatchReceiveItem.getTargetItem());

            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(0);

            pstMatDispatchReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, matDispatchReceiveItem.getTargetItem().getOID());
            pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, matDispatchReceiveItem.getSourceItem().getOID());
            pstMatDispatchReceiveItem.setLong(FLD_DF_REC_GROUP_ID, matDispatchReceiveItem.getDfRecGroupId());
            pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ID, matDispatchReceiveItem.getDispatchMaterialId());

            pstMatDispatchReceiveItem.insert();
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getInsertSQL());
            matDispatchReceiveItem.setOID(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_ITEM_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
        return matDispatchReceiveItem.getOID();
    }

    public static long insertExcProduksi(MatDispatchReceiveItem matDispatchReceiveItem) throws DBException {
        try {

            PstMatDispatchItem.insertExc(matDispatchReceiveItem.getSourceItem());
            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(0);

            pstMatDispatchReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, matDispatchReceiveItem.getTargetItem().getOID());
            pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, matDispatchReceiveItem.getSourceItem().getOID());
            pstMatDispatchReceiveItem.setLong(FLD_DF_REC_GROUP_ID, matDispatchReceiveItem.getDfRecGroupId());
            pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ID, matDispatchReceiveItem.getDispatchMaterialId());

            pstMatDispatchReceiveItem.insert();
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getInsertSQL());
            matDispatchReceiveItem.setOID(pstMatDispatchReceiveItem.getlong(FLD_DF_REC_ITEM_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
        return matDispatchReceiveItem.getOID();
    }

    public static long updateExc(MatDispatchReceiveItem matDispatchReceiveItem) throws DBException {
        long result = 0;
        try {
            if (matDispatchReceiveItem.getOID() != 0) {
                PstMatDispatchItem.updateExc(matDispatchReceiveItem.getSourceItem());
                PstMatReceiveItem.updateExc(matDispatchReceiveItem.getTargetItem());

                PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(matDispatchReceiveItem.getOID());

                pstMatDispatchReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, matDispatchReceiveItem.getTargetItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, matDispatchReceiveItem.getSourceItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DF_REC_GROUP_ID, matDispatchReceiveItem.getDfRecGroupId());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ID, matDispatchReceiveItem.getDispatchMaterialId());

                pstMatDispatchReceiveItem.update();
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getUpdateSQL());
                result = matDispatchReceiveItem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
        return result;
    }

    public static long updateExcHpp(MatDispatchReceiveItem matDispatchReceiveItem) throws DBException {
        long result = 0;
        try {
            if (matDispatchReceiveItem.getOID() != 0) {
                if (matDispatchReceiveItem.getSourceItem().getOID() != 0) {
                    PstMatDispatchItem.updateExc(matDispatchReceiveItem.getSourceItem());
                }

                if (matDispatchReceiveItem.getTargetItem().getOID() != 0) {
                    PstMatReceiveItem.updateExc(matDispatchReceiveItem.getTargetItem());
                }

                PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(matDispatchReceiveItem.getOID());

                pstMatDispatchReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, matDispatchReceiveItem.getTargetItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, matDispatchReceiveItem.getSourceItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DF_REC_GROUP_ID, matDispatchReceiveItem.getDfRecGroupId());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ID, matDispatchReceiveItem.getDispatchMaterialId());

                pstMatDispatchReceiveItem.update();
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getUpdateSQL());
                result = matDispatchReceiveItem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
        return result;
    }

    public static long updateExcProduksi(MatDispatchReceiveItem matDispatchReceiveItem) throws DBException {
        long result = 0;
        try {
            if (matDispatchReceiveItem.getOID() != 0) {
                PstMatDispatchItem.updateExc(matDispatchReceiveItem.getSourceItem());
                PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(matDispatchReceiveItem.getOID());

                pstMatDispatchReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, matDispatchReceiveItem.getTargetItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, matDispatchReceiveItem.getSourceItem().getOID());
                pstMatDispatchReceiveItem.setLong(FLD_DF_REC_GROUP_ID, matDispatchReceiveItem.getDfRecGroupId());
                pstMatDispatchReceiveItem.setLong(FLD_DISPATCH_MATERIAL_ID, matDispatchReceiveItem.getDispatchMaterialId());

                pstMatDispatchReceiveItem.update();
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getUpdateSQL());
                result = matDispatchReceiveItem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }
        return result;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(oid);
            pstMatDispatchReceiveItem.delete();

            try {
                PstMatDispatchItem.deleteExc(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            } catch (Exception e) {
                System.out.println(e);
            }

            try {
                PstMatReceiveItem.deleteExc(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            } catch (Exception e) {
                System.out.println(e);
            }
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstMatDispatchReceiveItem.getDeleteSQL());

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDispatchReceiveItem(0), DBException.UNKNOWN);
        }

        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //Untuk menampilkan komplit dengan material dan unit
    public static Vector list(int limitStart, int recordToGet, long oidMatDispatch) {
        return list(limitStart, recordToGet, oidMatDispatch, "");
    }

    //Untuk menampilkan komplit dengan material dan unit
    public static Vector list(int limitStart, int recordToGet, long oidDfRecGroupId, String order) {
        if (limitStart < 0) {
            limitStart = 0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + " AS DFI_QTY "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] + " AS DFI_UNIT_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] + " AS DFI_HPP "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + " AS DFI_HPP_TOTAL "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] + " AS RMI_UNIT_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + " AS RMI_COST "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + " AS RMI_TOTAL "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS DFI_SKU "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS DFI_NAME "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS DFI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID"
                    + " , UNT1." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS DFI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS RMI_SKU "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS RMI_NAME "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS RMI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID"
                    + " , UNT2." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS RMI_CODE "
                    + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " WHERE DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    //+ " WHERE DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    //+ " WHERE DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " = " + oidDfRecGroupId
                    //+ " = " + oidMatDispatch
                    //+ " ORDER BY DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + ((order != null && order.trim().length() > 0) ? (" ORDER BY " + order)
                            : (" ORDER BY DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                            + ", DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]));

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();

                //dfrItem.setOID(rs.getLong(1));
                //dfrItem.getSourceItem().setOID(rs.getLong(2));
                //dfrItem.setDfRecGroupId(rs.getLong(3));
                //temp.add(dfrItem);
                //dfItem.setMaterialId(rs.getLong(4));
                //dfItem.setUnitId(rs.getLong(5));
                //dfItem.setQty(rs.getDouble(6));
                //temp.add(dfItem);
                //mat.setSku(rs.getString(7));
                //mat.setName(rs.getString(8));
                //mat.setRequiredSerialNumber(rs.getInt(11));
                //mat.setGondolaCode(rs.getString(14));
                //temp.add(mat);
                // unit.setCode(rs.getString(7));
                //temp.add(unit);
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //resultToObject(rs, matDispatchItem);

            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk menampilkan komplit dengan material dan unit digroup by group id
    public static Vector listGroup(int limitStart, int recordToGet, long oidMatDispatch, String order) {
        if (limitStart < 0) {
            limitStart = 0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + " AS DFI_QTY "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] + " AS DFI_UNIT_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] + " AS DFI_HPP "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + " AS DFI_HPP_TOTAL "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] + " AS RMI_UNIT_ID "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS DFI_SKU "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS DFI_NAME "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS DFI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID"
                    + " , UNT1." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS DFI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS RMI_SKU "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS RMI_NAME "
                    + " , UNT2." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS RMI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS RMI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID"
                    + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " WHERE DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    //+ " WHERE DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " = " + oidMatDispatch
                    + " GROUP BY " + fieldNames[FLD_DF_REC_GROUP_ID]
                    + ((order != null && order.trim().length() > 0) ? (" ORDER BY " + order)
                            : (" ORDER BY DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]));

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("RS result :" + rs);

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();

                //dfrItem.setOID(rs.getLong(1));
                //dfrItem.getSourceItem().setOID(rs.getLong(2));
                //dfrItem.setDfRecGroupId(rs.getLong(3));
                //temp.add(dfrItem);
                //dfItem.setMaterialId(rs.getLong(4));
                //dfItem.setUnitId(rs.getLong(5));
                //dfItem.setQty(rs.getDouble(6));
                //temp.add(dfItem);
                //mat.setSku(rs.getString(7));
                //mat.setName(rs.getString(8));
                //mat.setRequiredSerialNumber(rs.getInt(11));
                //mat.setGondolaCode(rs.getString(14));
                //temp.add(mat);
                // unit.setCode(rs.getString(7));
                //temp.add(unit);
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //resultToObject(rs, matDispatchItem);

            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static Vector listGroupHpp(int limitStart, int recordToGet, long oidMatDispatch, String order) {
        if (limitStart < 0) {
            limitStart = 0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + " AS DFI_QTY "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] + " AS DFI_UNIT_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] + " AS DFI_HPP "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + " AS DFI_HPP_TOTAL "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] + " AS RMI_UNIT_ID "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS DFI_SKU "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS DFI_NAME "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS DFI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID"
                    + " , UNT1." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS DFI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS RMI_SKU "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS RMI_NAME "
                    + " , UNT2." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS RMI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS RMI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID"
                    + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " WHERE DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    //+ " WHERE DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " = " + oidMatDispatch
                    + " AND DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    //+ " WHERE DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " <> 0 "
                    + " GROUP BY " + fieldNames[FLD_DF_REC_GROUP_ID]
                    + ((order != null && order.trim().length() > 0) ? (" ORDER BY " + order)
                            : (" ORDER BY DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]));

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("RS result :" + rs);

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();

                //dfrItem.setOID(rs.getLong(1));
                //dfrItem.getSourceItem().setOID(rs.getLong(2));
                //dfrItem.setDfRecGroupId(rs.getLong(3));
                //temp.add(dfrItem);
                //dfItem.setMaterialId(rs.getLong(4));
                //dfItem.setUnitId(rs.getLong(5));
                //dfItem.setQty(rs.getDouble(6));
                //temp.add(dfItem);
                //mat.setSku(rs.getString(7));
                //mat.setName(rs.getString(8));
                //mat.setRequiredSerialNumber(rs.getInt(11));
                //mat.setGondolaCode(rs.getString(14));
                //temp.add(mat);
                // unit.setCode(rs.getString(7));
                //temp.add(unit);
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //resultToObject(rs, matDispatchItem);

            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk menampilkan qty target
    public static Vector getQty(int limitStart, int recordToGet, long oidDfRecGroupId) {
        if (limitStart < 0) {
            limitStart = 0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY] + " AS DFI_QTY "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID] + " AS DFI_UNIT_ID "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] + " AS DFI_HPP "
                    + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + " AS DFI_HPP_TOTAL "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] + " AS RMI_UNIT_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + " AS RMI_COST "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + " AS RMI_TOTAL "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS DFI_SKU "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS DFI_NAME "
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS DFI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS DFI_MATERIAL_ID"
                    + " , UNT1." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS DFI_CODE "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS RMI_SKU "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS RMI_NAME "
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS RMI_REQUIRED_SERIAL_NUMBER"
                    + " , MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " AS RMI_MATERIAL_ID"
                    + " , UNT2." + PstUnit.fieldNames[PstUnit.FLD_CODE] + " AS RMI_CODE "
                    + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " WHERE DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroupId
                    + " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " != 0";

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();

                //dfrItem.setOID(rs.getLong(1));
                //dfrItem.getSourceItem().setOID(rs.getLong(2));
                //dfrItem.setDfRecGroupId(rs.getLong(3));
                //temp.add(dfrItem);
                //dfItem.setMaterialId(rs.getLong(4));
                //dfItem.setUnitId(rs.getLong(5));
                //dfItem.setQty(rs.getDouble(6));
                //temp.add(dfItem);
                //mat.setSku(rs.getString(7));
                //mat.setName(rs.getString(8));
                //mat.setRequiredSerialNumber(rs.getInt(11));
                //mat.setGondolaCode(rs.getString(14));
                //temp.add(mat);
                // unit.setCode(rs.getString(7));
                //temp.add(unit);
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //resultToObject(rs, matDispatchItem);

            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk menampilkan current stock values material target 
    public static Vector getStockValueCurrentTarget(int limitStart, int recordToGet, long oidDfRecGroupId) {
        if (limitStart < 0) {
            limitStart = 0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]+ " AS DFI_MATERIAL_ID "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]+ " AS DFI_QTY "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]+ " AS DFI_UNIT_ID "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP] + " AS DFI_HPP "
                    //+ " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL] + " AS DFI_HPP_TOTAL "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY] + " AS RMI_QTY "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID] + " AS RMI_UNIT_ID "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] + " AS RMI_COST "
                    + " , RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL] + " AS RMI_TOTAL "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " AS RMI_SKU "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " AS RMI_NAME "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " AS RMI_AVERAGE_PRICE "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + " AS RMI_DEFAULT_COST "
                    + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] + " AS RMI_REQUIRED_SERIAL_NUMBER"
                    + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT "
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " WHERE DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroupId
                    + " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " != 0";

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //Vector temp = new Vector();
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();
                MatDispatchItem matDispatchItem = new MatDispatchItem();
                MatReceiveItem matReceiveItem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();

                matDispatchReceiveItem.getTargetItem().setQty(rs.getDouble("RMI_QTY"));
                matDispatchReceiveItem.getTargetItem().getMaterialTarget().setAveragePrice(rs.getDouble("RMI_AVERAGE_PRICE"));
                matDispatchReceiveItem.getTargetItem().getMaterialTarget().setDefaultCost(rs.getDouble("RMI_DEFAULT_COST"));
                //dfrItem.setOID(rs.getLong(1));
                //dfrItem.getSourceItem().setOID(rs.getLong(2));
                //dfrItem.setDfRecGroupId(rs.getLong(3));
                //temp.add(dfrItem);

                //dfItem.setMaterialId(rs.getLong(4));
                //dfItem.setUnitId(rs.getLong(5));
                //dfItem.setQty(rs.getDouble(6));
                //temp.add(dfItem);
                //mat.setSku(rs.getString(7));
                //mat.setName(rs.getString(8));
                //mat.setRequiredSerialNumber(rs.getInt(11));
                //mat.setGondolaCode(rs.getString(14));
                //temp.add(mat);
                // unit.setCode(rs.getString(7));
                //temp.add(unit);
                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //resultToObject(rs, matDispatchItem);

            }
            rs.close();

        } catch (Exception e) {
            lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    //Untuk menampilkan komplit dengan material dan unit
    /*public static Vector list(String whereClause, String orderClause) {
     Vector lists = new Vector();
     DBResultSet dbrs = null;
     try {
     String sql = "SELECT DFRI." + fieldNames[FLD_DF_REC_ITEM_ID]
     + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
     + " , DFRI." + fieldNames[FLD_DF_REC_GROUP_ID]
     + " , DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ID]
     + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
     + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]
     + " , DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
     + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
     + " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
     + " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE]
     + " FROM ((" + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
     + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
     + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
     + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
     + " ) INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT"
     + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
     + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
     + " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT"
     + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
     + " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

     if (whereClause.length() > 0) {
     sql += " WHERE " + whereClause;
     }

     if (orderClause.length() > 0) {
     sql += " ORDER BY " + whereClause;
     }

     dbrs = DBHandler.execQueryResult(sql);
     ResultSet rs = dbrs.getResultSet();
     while (rs.next()) {
     Vector temp = new Vector();
     MatDispatchItem dfItem = new MatDispatchItem();
     Material mat = new Material();
     Unit unit = new Unit();


     dfItem.setOID(rs.getLong(1));
     dfItem.setMaterialId(rs.getLong(2));
     dfItem.setUnitId(rs.getLong(3));
     dfItem.setQty(rs.getDouble(4));
     temp.add(dfItem);

     mat.setSku(rs.getString(5));
     mat.setName(rs.getString(6));
     mat.setDefaultCost(rs.getDouble(8));
     mat.setDefaultCostCurrencyId(rs.getLong(9));
     temp.add(mat);

     unit.setCode(rs.getString(7));
     temp.add(unit);



     lists.add(temp);
     }
     rs.close();

     } catch (Exception e) {
     lists = new Vector();
     System.out.println(e);
     } finally {
     DBResultSet.close(dbrs);
     }
     return lists;
     }*/
    public static void resultToObject(ResultSet rs, MatDispatchReceiveItem matDispatchReceiveItem) {

        try {
            MatDispatchItem matDispatchItem = matDispatchReceiveItem.getSourceItem();
            MatReceiveItem matReceiveItem = matDispatchReceiveItem.getTargetItem();
            Material materialSource = matDispatchItem.getMaterialSource();
            Unit unitSource = matDispatchItem.getUnitSource();
            Material materialTarget = matReceiveItem.getMaterialTarget();
            Unit unitTarget = matReceiveItem.getUnitTarget();
            matDispatchReceiveItem.setOID(rs.getLong(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_ITEM_ID]));

            matDispatchReceiveItem.getTargetItem().setOID(rs.getLong(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]));
            matDispatchReceiveItem.getSourceItem().setOID(rs.getLong(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ITEM_ID]));

            matDispatchReceiveItem.setDfRecGroupId(rs.getLong(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]));
            matDispatchReceiveItem.setDispatchMaterialId(rs.getLong(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID]));

            matDispatchItem.setMaterialId(rs.getLong("DFI_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]));
            matDispatchItem.setQty(rs.getLong("DFI_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_QTY]));
            matDispatchItem.setUnitId(rs.getLong("DFI_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]));
            matDispatchItem.setHpp(rs.getDouble("DFI_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP]));
            matDispatchItem.setHppTotal(rs.getDouble("DFI_" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_HPP_TOTAL]));
            matReceiveItem.setMaterialId(rs.getLong("RMI_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]));

            matReceiveItem.setQty(rs.getLong("RMI_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
            matReceiveItem.setUnitId(rs.getLong("RMI_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]));
            matReceiveItem.setCost(rs.getDouble("RMI_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
            matReceiveItem.setTotal(rs.getDouble("RMI_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));

            materialSource.setSku(rs.getString("DFI_" + PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            materialSource.setName(rs.getString("DFI_" + PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            materialSource.setRequiredSerialNumber(rs.getInt("DFI_" + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER]));
            materialSource.setOID(rs.getLong("DFI_" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));

            materialTarget.setSku(rs.getString("RMI_" + PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
            materialTarget.setName(rs.getString("RMI_" + PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
            materialTarget.setRequiredSerialNumber(rs.getInt("RMI_" + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER]));
            materialTarget.setOID(rs.getLong("RMI_" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));

            unitSource.setCode(rs.getString("DFI_" + PstUnit.fieldNames[PstUnit.FLD_CODE]));
            unitTarget.setCode(rs.getString("RMI_" + PstUnit.fieldNames[PstUnit.FLD_CODE]));

            //get hpp currenct
            //materialTarget.setAveragePrice(rs.getDouble("RMI_"+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
            //materialTarget.setDefaultCost(rs.getDouble("RMI_"+PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
        } catch (Exception e) {
        }
    }

    public static Vector checkOID(long oidDfRecGroup) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT " + fieldNames[FLD_DF_REC_ITEM_ID] + " FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM
                    + " WHERE " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroup;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //rs.getLong(connLog)
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();

                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //result = true;
            }
            rs.close();
        } catch (Exception e) {
            lists = new Vector();
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    /*
     * For searching cost di target
     */
    public static Vector checkGetCost(long oidDfRecGroup) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]
                    + " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS RMI "
                    + " INNER JOIN " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM + " AS DFRI "
                    + " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " WHERE DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID] + "=" + oidDfRecGroup
                    + " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + "!= 0";;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //rs.getLong(connLog)
                MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();

                resultToObject(rs, matDispatchReceiveItem);
                lists.add(matDispatchReceiveItem);
                //result = true;
            }
            rs.close();
        } catch (Exception e) {
            lists = new Vector();
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static int getCount(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + ") AS CNT FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM;
            //+ " GROUP BY " + fieldNames[FLD_DF_REC_GROUP_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY " + fieldNames[FLD_DF_REC_GROUP_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /*
     * This method used for count data group
     * By Mirahu
     */
    public static int getCountGroup(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT( DISTINCT DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + ") AS CNT FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM + " DFRI"
                    + " INNER JOIN " + PstMatDispatchItem.TBL_MAT_DISPATCH_ITEM + " DFI"
                    + " ON DFRI." + fieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " = DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID]
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " RMI"
                    + " ON DFRI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID]
                    + " = MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT1"
                    + " ON DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_UNIT_ID]
                    + " = UNT1." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]
                    + " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]
                    + " = MAT2." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT2"
                    + " ON RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]
                    + " = UNT2." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE DFRI." + whereClause;
            }

            //sql = sql + " GROUP BY " + fieldNames[FLD_DF_REC_GROUP_ID];
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MatDispatchReceiveItem matDispatchReceiveItem = (MatDispatchReceiveItem) list.get(ls);
                    if (oid == matDispatchReceiveItem.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }

        return start;
    }

    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                    }
                }
            }
        }

        return cmd;
    }

    /*public static long deleteExcByParent(long oid) throws DBException {
     long hasil = 0;
     try {
     String sql = "DELETE FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM +
     " WHERE " + fieldNames[FLD_DF_REC_ITEM_ID] +
     " = " + oid;
     int result = execUpdate(sql);
     hasil = oid;
     } catch (DBException dbe) {
     throw dbe;
     } catch (Exception e) {
     throw new DBException(new PstMatDispatchItem(0), DBException.UNKNOWN);
     }
     return hasil;
     }*/

    /* Method for delete Group
     * By Mirahu
     */
    public static int deleteGroup(long oidDfRecGroup) {

        DBResultSet dbrs = null;
        try {

            MatDispatchReceiveItem matDispatchReceiveItem = new MatDispatchReceiveItem();

            PstMatDispatchReceiveItem pstMatDispatchReceiveItem = new PstMatDispatchReceiveItem(matDispatchReceiveItem.getDfRecGroupId());

            String sql = "DELETE FROM " + TBL_MAT_DISPATCH_RECEIVE_ITEM
                    + " WHERE " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID]
                    + " = " + oidDfRecGroup;

            DBHandler.execUpdate(sql);

            try {
                PstMatDispatchItem.deleteExc(pstMatDispatchReceiveItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
            } catch (Exception e) {
                System.out.println(e);
            }

            try {
                PstMatReceiveItem.deleteExc(pstMatDispatchReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    //end of method Delete
    /**
     * Update master cost di material jika receive dari supplier
     *
     * @param oidMaterial
     * @param currencyId
     * @param newCost
     * @return
     */
    private static boolean updateCostTarget(long oidRecMaterialItem, long currencyId, double newCost) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL
                    + " SET " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]
                    + " = " + newCost;
            if (currencyId != 0) {
                sql = sql + ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]
                        + " = " + currencyId;
            }
            sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = " + oidRecMaterialItem;
            //System.out.println("updateCostMaster >>> "+sql);
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

    /**
     * di gunakan untuk mencari object dispatch item
     */
    public static MatDispatchItem getObjectDispatchItem(long oidMaterial, long oidDispatch) {
        MatDispatchItem matDispatchItem = new MatDispatchItem();
        try {
            String whereClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidDispatch
                    + " AND " + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_MATERIAL_ID] + "=" + oidMaterial;
            Vector vect = PstMatDispatchItem.list(0, 0, whereClause, "");
            if (vect != null && vect.size() > 0) {
                matDispatchItem = (MatDispatchItem) vect.get(0);
            }

        } catch (Exception e) {
        }
        return matDispatchItem;
    }
    
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatDispatchReceiveItem matDispatchReceiveItem = PstMatDispatchReceiveItem.fetchExc(oid);
         object.put(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_ITEM_ID], matDispatchReceiveItem .getOID());
         object.put(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID], matDispatchReceiveItem .getTargetItem().getOID());
         object.put(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ITEM_ID], matDispatchReceiveItem .getSourceItem().getOID());
         object.put(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID], matDispatchReceiveItem .getDfRecGroupId());
         object.put(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID], matDispatchReceiveItem .getDispatchMaterialId());
      }catch(Exception exc){}
      return object;
   }
}
