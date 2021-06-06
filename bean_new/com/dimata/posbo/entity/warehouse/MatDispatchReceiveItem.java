/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

/*package java */
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import java.util.Date;

/*package qdep */
import com.dimata.qdep.entity.*;

public class MatDispatchReceiveItem extends Entity {

    private long dfRecGroupId = 0;
    private long dispatchMaterialId = 0;
    private MatDispatchItem sourceItem = new MatDispatchItem();
    private MatReceiveItem targetItem = new MatReceiveItem();

    /**
     * @return the dfRecGroupId
     */
    public long getDfRecGroupId() {
        return dfRecGroupId;
    }

    /**
     * @param dfRecGroupId the dfRecGroupId to set
     */
    public void setDfRecGroupId(long dfRecGroupId) {
        this.dfRecGroupId = dfRecGroupId;
    }

    /**
     * @return the dispatchMaterialId
     */
    public long getDispatchMaterialId() {
        return dispatchMaterialId;
    }

    /**
     * @param dispatchMaterialId the dispatchMaterialId to set
     */
    public void setDispatchMaterialId(long dispatchMaterialId) {
        this.dispatchMaterialId = dispatchMaterialId;
    }

    /**
     * @return the sourceItem
     */
    public MatDispatchItem getSourceItem() {
        if (sourceItem == null) {
            sourceItem = new MatDispatchItem();
        }
        return sourceItem;
    }

    /**
     * @param sourceItem the sourceItem to set
     */
    public void setSourceItem(MatDispatchItem sourceItem) {
        this.sourceItem = sourceItem;
    }

    /**
     * @return the targetItem
     */
    public MatReceiveItem getTargetItem() {
        if (targetItem == null) {
            targetItem = new MatReceiveItem();
        }
        return targetItem;
    }

    /**
     * @param targetItem the targetItem to set
     */
    public void setTargetItem(MatReceiveItem targetItem) {
        this.targetItem = targetItem;
    }

    //by dyas
    //tambah methods getLogDetail
    public String getLogDetail(Entity MatDispatchItem, Entity MatReceiveItem) {

        MatDispatchItem prevMatDispatchItem = (MatDispatchItem) MatDispatchItem;
        MatReceiveItem prevMatReceiveItem = (MatReceiveItem) MatReceiveItem;

        Material materialDispatch = null;
        Material materialReceiveItem = null;

        Unit unit = null;
        Unit unitRec = null;

        try {
            if (this != null && prevMatDispatchItem.getMaterialId() != 0) {
                materialDispatch = PstMaterial.fetchExc(prevMatDispatchItem.getMaterialId());
            }

            if (this != null && prevMatDispatchItem.getUnitId() != 0) {
                unit = PstUnit.fetchExc(prevMatDispatchItem.getUnitId());
            }
        } catch (Exception e) {
        }

        try {
            if (this != null && prevMatReceiveItem.getMaterialId() != 0) {
                materialReceiveItem = PstMaterial.fetchExc(prevMatReceiveItem.getMaterialId());
            }

            if (this != null && prevMatReceiveItem.getUnitId() != 0) {
                unitRec = PstUnit.fetchExc(prevMatReceiveItem.getUnitId());
            }
        } catch (Exception e) {
        }

        String logMatDispatchItem = "";

        String logMatReceiveItem = "";

        try {
            if (prevMatDispatchItem.getMaterialId() != 0) {
                logMatDispatchItem = " Material Source SKU : " + materialDispatch.getSku()
                        + " Nama Barang : " + materialDispatch.getName()
                        + " Unit : " + unit.getName()
                        + " Qty : " + this.getSourceItem().getQty()
                        + " HPP : " + this.getSourceItem().getHpp()
                        + " HPP Total : " + this.getSourceItem().getHppTotal();
            }
        } catch (Exception e) {
        }

        try {
            if (prevMatReceiveItem.getMaterialId() != 0) {
                logMatReceiveItem = " Material Target SKU : " + materialReceiveItem.getSku()
                        + " Nama Barang : " + materialReceiveItem.getName()
                        + " Unit : " + unitRec.getName()
                        + " Qty : " + this.getTargetItem().getQty();
                //" HPP : " + this.getTargetItem().get+
                //" HPP Total : " + this.getTargetItem().getHppTotal();
            }
        } catch (Exception e) {
        }

        String allLogMat = logMatDispatchItem + " " + logMatReceiveItem;

        return allLogMat;//+

    }

}
